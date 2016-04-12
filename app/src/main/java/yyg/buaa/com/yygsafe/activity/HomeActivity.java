package yyg.buaa.com.yygsafe.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import yyg.buaa.com.yygsafe.R;
import yyg.buaa.com.yygsafe.activity.base.BaseActivity;
import yyg.buaa.com.yygsafe.adapter.HomeAdapter;
import yyg.buaa.com.yygsafe.utils.Md5Utils;
import yyg.buaa.com.yygsafe.utils.UIUtils;

/**
 * Created by yyg on 2016/4/7.
 */
public class HomeActivity extends BaseActivity {

    private GridView grid_home;
    private String[] names = { "手机防盗", "通讯卫士", "软件管家", "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心" };
    private int[] icons = { R.mipmap.safe, R.mipmap.callmsgsafe,
            R.drawable.app_selector, R.mipmap.taskmanager, R.mipmap.netmanager,
            R.mipmap.trojan, R.mipmap.sysoptimize, R.mipmap.atools,
            R.mipmap.settings };
    private HomeAdapter adapter;
    private EditText et_setpwd_dialog;
    private EditText et_confirmpwd_dialog;
    private Button btn_confirm_dialog;
    private Button btn_cancel_dialog;
    private Dialog dialog;

    @Override
    public void initView() {
        setContentView(R.layout.activity_home);
        grid_home = (GridView) findViewById(R.id.grid_home);
    }

    @Override
    public void initData() {
        //条目点击事件
        grid_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: //手机防盗
                        //判断是否设置过密码
                        if (!isSetPwd()) {
                            //没有设置过，显示设置密码对话框
                            showSetPwdDialog();
                        } else {
                            //已经设置过密码，显示确认密码对话框
                            showConfirmPwdDialog();
                        }


                }
            }
        });
    }

    @Override
    protected void onStart() {
        grid_home.setAdapter(new HomeAdapter(HomeActivity.this, names, icons));
        super.onStart();
    }

    private boolean isSetPwd() {
        String spPwd = sp.getString("password", null);
        if (TextUtils.isEmpty(spPwd)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 显示设置密码对话框
     */
    private void showSetPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_setup_pwd, null);

        //取出数据，设置监听
        et_setpwd_dialog = (EditText) view.findViewById(R.id.et_setpwd_dialog);
        et_confirmpwd_dialog = (EditText) view.findViewById(R.id.et_confirmpwd_dialog);
        btn_confirm_dialog = (Button) view.findViewById(R.id.btn_confirm_dialog);
        btn_cancel_dialog = (Button) view.findViewById(R.id.btn_cancel_dialog);

        //对按钮设置监听
        //确定按钮
        btn_confirm_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setpwd = et_setpwd_dialog.getText().toString().trim();
                String setpwdConfirm = et_confirmpwd_dialog.getText().toString().trim();
                if (TextUtils.isEmpty(setpwd) || TextUtils.isEmpty(setpwdConfirm)) {
                    //密码为空
                    UIUtils.showToast(HomeActivity.this, "密码不能为空");
                    return;
                } else if (!setpwd.equals(setpwdConfirm)) {
                    //两次密码不一致
                    UIUtils.showToast(HomeActivity.this, "两次密码不一致");
                    return;
                }
                //把密码保存到sharedPreference中
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("password", Md5Utils.encode(setpwd));
                editor.commit();
                dialog.dismiss();
                UIUtils.showToast(HomeActivity.this, "设置密码成功");
            }
        });
        //取消按钮
        btn_cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        builder.setView(view);
        dialog = builder.show();
    }

    /**
     * 显示输入密码对话框
     */
    private void showConfirmPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_confirm_pwd, null);

        //取出数据，设置监听
        et_confirmpwd_dialog = (EditText) view.findViewById(R.id.et_confirmpwd_dialog);
        btn_confirm_dialog = (Button) view.findViewById(R.id.btn_confirm_dialog);
        btn_cancel_dialog = (Button) view.findViewById(R.id.btn_cancel_dialog);

        //对按钮设置监听
        //确定按钮
        btn_confirm_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1、获取用户输入的密码
                String confirmPwd = et_confirmpwd_dialog.getText().toString().trim();
                //2、获取用户原来设置的密码
                String pwd = sp.getString("password", "");
                //3、检查密码是否正确
                if (TextUtils.isEmpty(confirmPwd)) {
                    //密码为空
                    UIUtils.showToast(HomeActivity.this, "密码不能为空");
                    return;
                } else if (!Md5Utils.encode(confirmPwd).equals(pwd)) {
                    //密码不正确,请重新输入
                    UIUtils.showToast(HomeActivity.this, "密码不正确,请重新输入");
                    return;
                }
                //密码正确，进入密码防盗界面
                startActivity(LostFindActivity.class);
                dialog.dismiss();
            }
        });
        //取消按钮
        btn_cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        builder.setView(view);
        dialog = builder.show();
    }

    @Override
    public void initListener() {
    }

    @Override
    public void progressClick(View v) {
    }
}
