package a.fmy.com.mylibrary;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 此方注解写于activity类上 可以免去 setContentView()步骤 
 * @author 范明毅
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FmyContentView {	
	/**
	 * 保存布局文件的id eg:R.layout.main
	 * @return 返回 布局id
	 */
	int value();

}
