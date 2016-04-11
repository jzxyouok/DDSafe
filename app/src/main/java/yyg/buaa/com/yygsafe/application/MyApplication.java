package yyg.buaa.com.yygsafe.application;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.xutils.x;


/**
 * Created by yyg on 2016/4/7.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);   //Xutils3初始化

        x.Ext.setDebug(false);   // 设置是否输出debug

        Logger
            .init()
            .setMethodCount(3)
            .hideThreadInfo()
            .setLogLevel(LogLevel.FULL);


    }
}
