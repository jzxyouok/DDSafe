package yyg.buaa.com.yygsafe.activity;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import yyg.buaa.com.yygsafe.R;
import yyg.buaa.com.yygsafe.activity.base.BaseActivity;

/**
 * Created by yyg on 2016/4/8.
 */
public class LostFindActivity extends BaseActivity {

    private SharedPreferences sp;
    private TextView tv_lostfind_resetup;
    private TextView tv_lostfind_phone;
    private ImageView iv_lostfind_lockstatus;

    @Override
    public void initView() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        if (isFinishSetup()) {
            //完成配置，显示手机防盗界面
            setContentView(R.layout.activity_lostfind);
            tv_lostfind_phone = (TextView) findViewById(R.id.tv_lostfind_phone);
            iv_lostfind_lockstatus = (ImageView) findViewById(R.id.iv_lostfind_lockstatus);
            tv_lostfind_resetup = (TextView) findViewById(R.id.tv_lostfind_resetup);

            //initData
            tv_lostfind_phone.setText(sp.getString("safeNumber", ""));
            if (sp.getBoolean("protecting", false)) {
                //防盗保护已开启
                iv_lostfind_lockstatus.setImageResource(R.drawable.lock);
            } else {
                //防盗保护未开启
                iv_lostfind_lockstatus.setImageResource(R.mipmap.unlock);
            }

            //initListener
            tv_lostfind_resetup.setOnClickListener(this);
        } else {
            //进入设置向导Activity
            startActivityAndFinishSelf(Setup1Activity.class);
        }
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
    }

    @Override
    public void progressClick(View v) {
        switch (v.getId()) {
            case R.id.tv_lostfind_resetup:
                startActivityAndFinishSelf(Setup1Activity.class);
        }
    }

    /**
     * 判断是否进行过配置
     * @return
     */
    private boolean isFinishSetup() {
        return sp.getBoolean("finishSetup", false);
    }
}
