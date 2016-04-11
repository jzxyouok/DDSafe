package yyg.buaa.com.yygsafe.activity;

import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import yyg.buaa.com.yygsafe.R;
import yyg.buaa.com.yygsafe.activity.base.BaseSetupActivity;
import yyg.buaa.com.yygsafe.utils.UIUtils;

/**
 * Created by yyg on 2016/4/8.
 */
public class Setup2Activity extends BaseSetupActivity {

    private RelativeLayout rl_setup2_status;
    private ImageView iv_setup2_status;
    private TelephonyManager pm;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_setup2);
        rl_setup2_status = (RelativeLayout) findViewById(R.id.rl_setup2_status);
        iv_setup2_status = (ImageView) findViewById(R.id.iv_setup2_status);
    }

    @Override
    public void initData() {
        super.initData();
        pm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String simSerial = sp.getString("sim", null);
        if (TextUtils.isEmpty(simSerial)) {
            //未绑定，显示解绑图片
            iv_setup2_status.setImageResource(R.mipmap.unlock);
        } else {
            //已绑定，显示绑定图片
            iv_setup2_status.setImageResource(R.drawable.lock);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        rl_setup2_status.setOnClickListener(this);
    }

    @Override
    public void progressClick(View v) {
        super.progressClick(v);
        switch (v.getId()) {
            case R.id.rl_setup2_status:
                String savedSim = sp.getString("sim", null);
                if (TextUtils.isEmpty(savedSim)) {
                    //还没有绑定，点击绑定
                    //sim卡唯一的标识
                    String simSerial = pm.getSimSerialNumber();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("sim", simSerial);
                    editor.commit();
                    UIUtils.showToast(this, "sim卡绑定成功");
                    iv_setup2_status.setImageResource(R.drawable.lock);
                } else {
                    //已经绑定成功，点击解除绑定
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("sim", null);
                    editor.commit();
                    UIUtils.showToast(this, "解除绑定成功");
                    iv_setup2_status.setImageResource(R.mipmap.unlock);
                }
        }
    }

    @Override
    public void showNext() {
        String simSerial = sp.getString("sim", null);
        if (TextUtils.isEmpty(simSerial)) {
            //未绑定，提示用户绑定
            UIUtils.showToast(this, "请先绑定");
        } else {
            //已绑定，进入下一个activity
            startActivityAndFinishSelf(Setup3Activity.class);
        }
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(Setup1Activity.class);
    }
}
