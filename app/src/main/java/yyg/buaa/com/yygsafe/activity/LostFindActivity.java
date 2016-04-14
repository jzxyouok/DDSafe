package yyg.buaa.com.yygsafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

    //menu创建时调用
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lost_find_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //menu条目被选中时触发
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.item_change_name == item.getItemId()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("设置手机防盗的新名称");
            final EditText et = new EditText(this);
            builder.setView(et);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sp.edit().putString("newname", et.getText().toString().trim()).commit();
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
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
