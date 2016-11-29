package a.fmy.com.mylibrary;

import java.lang.annotation.Documented;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

import android.app.Activity;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

/**
 * 用于注解的初始化
 * 
 * @version 1.0
 * @author 范明毅
 *
 */
public class FmyViewInject {

	/**
	 * 保存传入的activity
	 */
	private static Class<?> activityClass;

	/**
	 * 保存传入的fragment
	 */
	private static Class<?> fragemtnClass;

	/**
	 * fragment 填充出来的view 为了防止内存泄漏 所以用词保存
	 */
	private static WeakReference<View> weakReferenceView;

	/**
	 * 初始化activity和所有注解
	 * 
	 * @param obj
	 *            你需要初始化的activity
	 */
	public static void inject(Object obj) {

		// 清理上次使用保留的影响
		activityClass = null;
		fragemtnClass = null;

		activityClass = obj.getClass();

		// 初始化activity布局文件
		injectContent(obj);

		// 初始化所有控件实例 省去findViewId的痛苦
		injectView(obj);

		// 初始化所有控件的点击事件
		inijectOnClick(obj);
	}

	/**
	 * fragemnt 的初始化
	 * 
	 * @param obj
	 * @param inflater
	 * @param container
	 * @return
	 */
	public static View injectfragment(Object obj, LayoutInflater inflater,
			ViewGroup container) {

		// 清理上次使用保留的影响
		activityClass = null;
		fragemtnClass = null;

		fragemtnClass = obj.getClass();

		// 初始化activity布局文件
		View view = injectInitFragment(obj, inflater, container);

		// 赋值给弱应用
		weakReferenceView = new WeakReference<View>(view);

		// 初始化所有控件实例 省去findViewId的痛苦
		injectView(obj);

		// 初始化所有控件的点击事件
		inijectOnClick(obj);

		return view;
	}

	/**
	 * 填充fragement
	 * 
	 * @param obj
	 * @param inflater
	 * @param container
	 */
	private static View injectInitFragment(Object obj, LayoutInflater inflater,
			ViewGroup container) {
		// 填充出来的view
		View view = null;
		// 获得对应注解
		FmyContentView annotation = fragemtnClass
				.getAnnotation(FmyContentView.class);

		if (annotation != null) {
			// 获得注解的数值
			int value = annotation.value();
			// 得到填充对象
			view = inflater.inflate(value, container, false);
		}

		return view;
	}

	/**
	 * 初始化所有控件的点击事件 只需要某方法上写上对应注解和id即可
	 * 
	 * @param activity
	 */
	private static void inijectOnClick(Object activityOrFragment) {

		//获得所有方法
		Method[] methods  = null;
		
		if (activityOrFragment instanceof Activity) {
			 methods = activityClass.getMethods();
		}else{
			methods = fragemtnClass.getMethods();
		}
	

		// 遍历所有的activity下的方法
		for (Method method : methods) {
			// 获取方法的注解
			FmyClickView fmyClickView = method
					.getAnnotation(FmyClickView.class);
			// 如果存在此注解
			if (fmyClickView != null) {

				// 所有注解的控件的id
				int[] ids = fmyClickView.value();

				// 代理处理类
				MInvocationHandler handler = new MInvocationHandler(activityOrFragment,
						method);

				// 代理实例
				Object newProxyInstance = Proxy.newProxyInstance(
						OnClickListener.class.getClassLoader(),
						new Class<?>[] { OnClickListener.class }, handler);

				// 遍历所有的控件id 然后设置代理
				for (int i : ids) {
					try {
						Object view = null;
						
					//如果对象是activity
						if (activityOrFragment instanceof Activity) {
							 view = activityClass.getMethod("findViewById",
										int.class).invoke(activityOrFragment, i);
						}else{
							//如果对象是fragment
							if (weakReferenceView!=null&&weakReferenceView.get()!=null) {
								view = weakReferenceView.get().getClass().getMethod("findViewById",
										int.class).invoke( weakReferenceView.get(), i);
							}
							
						}
						
						if (view != null) {
							Method method2 = view.getClass().getMethod(
									"setOnClickListener",
									OnClickListener.class);
							method2.invoke(view, newProxyInstance);
						}
					} catch (Exception e) {

						e.printStackTrace();
					}

				}

			}
		}

	}

	/**
	 * 代理处理点击逻辑代码
	 * 
	 * @author 范明毅
	 *
	 */
	static class MInvocationHandler implements InvocationHandler {

		private Object target;

		// 用户自定义view 的点击事件方法
		private Method method;

		public MInvocationHandler(Object target, Method method) {
			super();
			this.target = target;
			this.method = method;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			// 调用用户自定义方法的点击事件
			return this.method.invoke(target, args);
		}

	}

	/**
	 * 初始化activity中的所有view控件 让其不用一个findViewid 实例化
	 * 
	 * 
	 * @param activity
	 */
	private static void injectView(Object activityOrFragment) {

		// 对象所有的属性
		Field[] declaredFields = null;

		if (activityOrFragment instanceof Activity) {

			// 健壮性
			if (activityClass != null) {
				// 获取du所有的属性 包含私有 保护 默认 共开 但不包含继承等
				// getFields可以获取到所有公开的包括继承的 但无法获取到私有的属性
				declaredFields = activityClass.getDeclaredFields();
			}

		} else {
			// 健壮性
			if (fragemtnClass != null) {
				// 获取du所有的属性 包含私有 保护 默认 共开 但不包含继承等
				// getFields可以获取到所有公开的包括继承的 但无法获取到私有的属性
				declaredFields = fragemtnClass.getDeclaredFields();
			}

		}
		// 健壮性
		if (declaredFields != null) {
			// 遍历所有的属性变量
			for (Field field : declaredFields) {

				// 获取属性变量上的注解
				FmyViewView annotation = field.getAnnotation(FmyViewView.class);

				// 如果此属性变量 包含FMYViewView
				if (annotation != null) {
					// 获取属性id值
					int id = annotation.value();

					Object obj = null;
					try {
						//如果是activity
						if (activityOrFragment instanceof Activity) {
							// 获取activity中方法
							 obj = activityClass.getMethod("findViewById",
									int.class).invoke(activityOrFragment, id);
						}else{
							//健壮性检查
							if (weakReferenceView!=null&&weakReferenceView.get()!=null) {
								// 获取activity中方法
								 obj = weakReferenceView.get().getClass().getMethod("findViewById",
										int.class).invoke(weakReferenceView.get(), id);
							}
							
						}
						
						Log.e("FMY", "" + field.getClass());
						// 设置属性变量 指向实例

						// 如果修饰符不为公共类
						if (Modifier.PUBLIC != field.getModifiers()) {
							// 打破封装性
							field.setAccessible(true);
						}
						// 这里相当于 field= acitivity.obj
						field.set(activityOrFragment, obj);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

	}

	/**
	 * 初始化activity布局文件 让其不用调用setContentView
	 * 
	 * @param activity
	 */
	private static void injectContent(Object obj) {

		// 获取注解
		FmyContentView annotation = activityClass
				.getAnnotation(FmyContentView.class);

		if (annotation != null) {
			// 获取注解中的对应的布局id
			int id = annotation.value();
			try {
				// 得到activity中的方法 第一个参数为方法名 第二个为可变参数 类型为 参数类型的字节码
				Method method = activityClass.getMethod("setContentView",
						int.class);

				// 调用方法 第一个参数为哪个实例去掉用 第二个参数为 参数
				method.invoke(obj, id);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

	}
}
