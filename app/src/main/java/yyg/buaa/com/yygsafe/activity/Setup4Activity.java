package yyg.buaa.com.yygsafe.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import yyg.buaa.com.yygsafe.R;
import yyg.buaa.com.yygsafe.activity.base.BaseSetupActivity;
import yyg.buaa.com.yygsafe.receiver.MyDeviceAdminReceiver;

/**
 * Created by yyg on 2016/4/8.
 */
public class Setup4Activity extends BaseSetupActivity {

    private CheckBox cb_setup4_checklock;
    private TextView tv_setup4_status;
    private Button btn_setup4_admin;
    private TextView tv_setup4_admin;
    private DevicePolicyManager dpm;
    private ComponentName component;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_setup4);
        cb_setup4_checklock = (CheckBox) findViewById(R.id.cb_setup4_checklock);
        tv_setup4_status = (TextView) findViewById(R.id.tv_setup4_status);
        btn_setup4_admin = (Button) findViewById(R.id.btn_setup4_admin);
        tv_setup4_admin = (TextView) findViewById(R.id.tv_setup4_admin);
    }

    @Override
    public void initData() {
        super.initData();
        //数据回显，判断防盗保护是否开启
        if (sp.getBoolean("protecting", false)) {
            //选中，显示已开启
            cb_setup4_checklock.setChecked(true);
            tv_setup4_status.setText("防盗保护已开启");
        } else {
            //未选中，显示未开启
            cb_setup4_checklock.setChecked(false);
            tv_setup4_status.setText("请开启防盗保护");
        }

        //数据回显，判断管理员权限是否开启
        //获取DevicePolicyManager
        dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        component = new ComponentName(this, MyDeviceAdminReceiver.class);
        if (dpm.isAdminActive(component)) {
            //已经开启
            tv_setup4_admin.setText("管理员权限已开启");
        } else {
            tv_setup4_admin.setText("请开启管理员权限");
        }

        //checkBox点击侦听
        cb_setup4_checklock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //选中，显示已开启
                    tv_setup4_status.setText("防盗保护已开启");
                } else {
                    //未选中，显示未开启
                    tv_setup4_status.setText("请开启防盗保护");
                }

                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("protecting", isChecked);
                editor.commit();
            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
        btn_setup4_admin.setOnClickListener(this);
    }


    @Override
    public void progressClick(View v) {
        super.progressClick(v);
        switch (v.getId()) {
            case R.id.btn_setup4_admin:
                openAdmin();
                tv_setup4_admin.setText("管理员权限已开启");
                break;
        }
    }

    @Override
    public void showNext() {
        //写入配置信息
        SharedPreferences.Editor et = sp.edit();
        et.putBoolean("finishSetup", true);
        et.commit();
        startActivityAndFinishSelf(LostFindActivity.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(Setup3Activity.class);
    }

    private void openAdmin() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, component);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "打开超级管理员权限才可以使用");
        startActivity(intent);
    }
}
