package a.fmy.com.mylibrary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 设置点击事件的注解 只需要在某方法 上写上此注解即可 如@FmyClickView({R.id.bt1,R.id.bt2})
 * @version 1.0
 * @author 范明毅
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FmyClickView {
	/**
	 * 保存所有需要设置点击事件控件的id
	 * @return 
	 */
	int [] value();
}
