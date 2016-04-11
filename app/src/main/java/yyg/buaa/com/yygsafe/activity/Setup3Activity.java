package yyg.buaa.com.yygsafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import yyg.buaa.com.yygsafe.R;
import yyg.buaa.com.yygsafe.activity.base.BaseSetupActivity;
import yyg.buaa.com.yygsafe.utils.UIUtils;

/**
 * Created by yyg on 2016/4/8.
 */
public class Setup3Activity extends BaseSetupActivity {

    private EditText et_setup3_number;
    private Button btn_setup3_selectContact;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_setup3);

        et_setup3_number = (EditText) findViewById(R.id.et_setup3_number);
        btn_setup3_selectContact = (Button) findViewById(R.id.btn_setup3_selectContact);
    }

    @Override
    public void initData() {
        super.initData();

        //号码回显
        et_setup3_number.setText(sp.getString("safeNumber", ""));
    }

    @Override
    public void initListener() {
        super.initListener();
        btn_setup3_selectContact.setOnClickListener(this);
    }

    @Override
    public void progressClick(View v) {
        super.progressClick(v);
        switch (v.getId()) {
            case R.id.btn_setup3_selectContact:
                Intent intent = new Intent(this, SelectContactActivity.class);
                startActivityForResult(intent, 0);
        }
    }

    @Override
    public void showNext() {
        if (TextUtils.isEmpty(et_setup3_number.getText())) {
            //文本为空，提示输入或选择号码
            UIUtils.showToast(this, "请输入或选择号码");
        } else {
            //文本有内容,点击进入下一个activity
            savePhoneToSp();
            startActivityAndFinishSelf(Setup4Activity.class);
        }
    }

    @Override
    public void showPre() {
        savePhoneToSp();
        startActivityAndFinishSelf(Setup2Activity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String phone = data.getStringExtra("phone");

            phone = phone.replace("-", ""); //去掉"-"
            phone = phone.replace(" ", ""); //去掉空格

            et_setup3_number.setText(phone);
            //移动光标到文本最后
            et_setup3_number.setSelection(phone.length());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void savePhoneToSp() {
        //保存电话到sp
        String phone = et_setup3_number.getText().toString().trim();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("safeNumber", phone);
        editor.commit();
    }
}
