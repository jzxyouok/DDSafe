package yyg.buaa.com.yygsafe.activity;

import android.view.View;

import yyg.buaa.com.yygsafe.R;
import yyg.buaa.com.yygsafe.activity.base.BaseSetupActivity;

/**
 * Created by yyg on 2016/4/8.
 */
public class Setup1Activity extends BaseSetupActivity {
    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void progressClick(View v) {
        super.progressClick(v);
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public void showNext() {
        startActivityAndFinishSelf(Setup2Activity.class);
    }

    @Override
    public void showPre() {
    }
}
