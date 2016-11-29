# FmyView
A simplified developers use findViewId when using the activity or fragments or setOnclickListener a simplified developers use findViewId when using the activity or fragments or setOnclickListener 
一个简化开发者在使用activity 或者fragment 的时候 使用findViewId or setOnclickListener

eg:
##Activity
```java
package a.fmy.com.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import a.fmy.com.mylibrary.FmyClickView;
import a.fmy.com.mylibrary.FmyContentView;
import a.fmy.com.mylibrary.FmyViewInject;
import a.fmy.com.mylibrary.FmyViewView;
// your Activity's LayoutID 你的activity的布局文件id
@FmyContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    //你想实例化控件的id
    //Do you want to control instance id 
    // 等价于 findViewByid
    //Equivalent to the findViewByid
    @FmyViewView(R.id.tv)
    TextView tv;
    @FmyViewView(R.id.tv2)
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initActivity
        // 初始化activity
        FmyViewInject.inject(this);
    }

    //你想给哪个控件添加 添加事件 的id
    //Do you want to add add event id to which controls
    @FmyClickView({R.id.tv,R.id.tv2})
    public void myOnclick(View view){
        switch (view.getId()) {
            case R.id.tv:
                tv.setText("TV1  "+Math.random()*100);
            break;
            case R.id.tv2:
                tv2.setText("TV2  "+Math.random()*100);
                break;
            default:

        }

    }
}

```
## R.layout.activity_main
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    tools:context="a.fmy.com.myapplication.MainActivity">

    <TextView
        android:id="@+id/tv"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="tv1" />
    <TextView
        android:layout_width="wrap_content"
        android:id="@+id/tv2"
        android:text="tv2"
        android:layout_height="wrap_content" />
</LinearLayout>

```

##Fragment

```java
package a.fmy.com.myapplication;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import a.fmy.com.mylibrary.FmyClickView;
import a.fmy.com.mylibrary.FmyContentView;
import a.fmy.com.mylibrary.FmyViewInject;
import a.fmy.com.mylibrary.FmyViewView;

//你的fragment的布局id  Your fragment's LayoutId
@FmyContentView(R.layout.fragment_blank)
public class BlankFragment extends Fragment {
    //你想实例化控件的id
    //Do you want to control instance id
    // 等价于 findViewByid
    //Equivalent to the findViewByid
    @FmyViewView(R.id.tv1)
    TextView tv1;
    @FmyViewView(R.id.tv2)
    TextView tv2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       //初始化fragment Initialize Fragement
        return FmyViewInject.injectfragment(this,inflater,container);
    }
    //你想给哪个控件添加 添加事件 的id
    //Do you want to add add event id to which controls
    @FmyClickView({R.id.tv1,R.id.tv2})
    public void myOnclick(View view){
        switch (view.getId()) {
            case R.id.tv1:
                tv1.setText("TV1  "+Math.random()*100);
                break;
            case R.id.tv2:
                tv2.setText("TV2  "+Math.random()*100);
                break;
            default:

        }

    }
}

```


##fragment_blank.xml
```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="a.fmy.com.myapplication.BlankFragment">


    <TextView
        android:id="@+id/tv1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="tv1" />

    <TextView
        android:layout_gravity="center"
        android:id="@+id/tv2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="tv2" />
</FrameLayout>

```

#How to use怎么使用


###在android Studio
Step 1.
Add it in your root build.gradle at the end of repositories:
在你的工程文件的gradle添加以下仓库
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```


Step 2. Add the dependency

在你需要添加依赖的module的gradle添加以下代码 

```
dependencies {
	        compile 'com.github.fanmingyi:FmyView:FmyView1.0'
	}
```

Step 3 use

使用

The article first sample
文章最开始的示例


