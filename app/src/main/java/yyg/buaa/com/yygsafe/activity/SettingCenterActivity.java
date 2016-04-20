package yyg.buaa.com.yygsafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import yyg.buaa.com.yygsafe.R;
import yyg.buaa.com.yygsafe.activity.base.BaseActivity;
import yyg.buaa.com.yygsafe.service.CallSmsSafeService;
import yyg.buaa.com.yygsafe.ui.MySettingView;

/**
 * Created by yyg on 2016/4/14.
 */
public class SettingCenterActivity extends BaseActivity {

    private MySettingView sv_autoupdate;
    private MySettingView sv_blacknumber;
    private MySettingView sv_showaddress;
    private Intent intent;

    @Override
    public void initView() {
        setContentView(R.layout.activity_setting_center);
        sv_autoupdate = (MySettingView) findViewById(R.id.sv_autoupdate);
        sv_blacknumber = (MySettingView) findViewById(R.id.sv_blacknumber);
        sv_showaddress = (MySettingView) findViewById(R.id.sv_showaddress);
    }

    @Override
    public void initData() {
        //数据回显
        sv_autoupdate.setChecked(sp.getBoolean("autoupdate", false));
        sv_blacknumber.setChecked(sp.getBoolean("blacknumber", false));
        sv_showaddress.setChecked(sp.getBoolean("showaddress", false));

        intent = new Intent(this, CallSmsSafeService.class);
    }

    @Override
    public void initListener() {
        //点击侦听
        sv_autoupdate.setOnClickListener(this);
        sv_blacknumber.setOnClickListener(this);
        sv_showaddress.setOnClickListener(this);
    }

    @Override
    public void progressClick(View v) {
        switch (v.getId()) {
            case R.id.sv_autoupdate:
                setCheckedAndSp(sv_autoupdate, "autoupdate");
                break;
            case R.id.sv_blacknumber:
                blackNumberSetting();
                break;
            case R.id.sv_showaddress:
                setCheckedAndSp(sv_showaddress, "showaddress");

        }
    }

    private void blackNumberSetting() {
        if (sv_blacknumber.isChecked()) {
            //已选中，点击未选中
            sv_blacknumber.setChecked(false);
            stopService(intent);
        } else {
            //未选中，点击已选中,开启服务
            sv_blacknumber.setChecked(true);
            startService(intent);
        }
    }

    public void setCheckedAndSp(MySettingView settingView, String string) {
        SharedPreferences.Editor editor = sp.edit();
        if (settingView.isChecked()) {
            //点击之前已选中，需要改为未选中
            settingView.setChecked(false);
            editor.putBoolean(string, false);
        } else {
            //点击之前未选中，需要改为已选中
            settingView.setChecked(true);
            editor.putBoolean(string, true);
        }
        editor.commit();
    }
}
