package a.fmy.com.mylibrary;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此方注解写于activity类中 控件变量上 可以省去findViewId 的烦恼
 * @author 范明毅
 * @version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FmyViewView {
	/**
	 * 保存view控件的id
	 * @return view控件id
	 */
	int value();
}

