package yyg.buaa.com.yygsafe.activity.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import yyg.buaa.com.yygsafe.utils.ActivityCollector;

/**
 * Created by yyg on 2016/4/7.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    public SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BaseActivity_onCreate", getClass().getSimpleName());
        ActivityCollector.addActivity(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);

        initView();
        initData();
        initListener();
    }

    public abstract void initView();
    public abstract void initData();
    public abstract void initListener();
    public abstract void progressClick(View v);

    @Override
    public void onClick(View v) {
        progressClick(v);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("BaseActivity_onDestroy", getClass().getSimpleName());
        ActivityCollector.removeActivity(this);
    }

    /**
     * 显示跳转到另一个activity,并finish自己
     * @param cls
     */
    public void startActivityAndFinishSelf(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }

    /**
     * 显示跳转到另一个activity,不finish自己
     * @param cls
     */
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
