package yyg.buaa.com.yygsafe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yyg.buaa.com.yygsafe.R;
import yyg.buaa.com.yygsafe.activity.base.BaseActivity;
import yyg.buaa.com.yygsafe.adapter.CallSmsSafeAdapter;
import yyg.buaa.com.yygsafe.db.dao.BlackNumberDAO;
import yyg.buaa.com.yygsafe.domain.BlackNumberInfo;

/**
 * 实现数据分页显示
 * Created by yyg on 2016/4/19.
 */
public class CallSmsSafeActivity2 extends BaseActivity {

    @Bind(R.id.button1)
    Button button1;
    @Bind(R.id.ll_add_number_tips)
    LinearLayout llAddNumberTips;
    @Bind(R.id.ll_loading)
    LinearLayout llLoading;
    @Bind(R.id.lv_callsms_safe)
    ListView lvCallsmsSafe;
    private List<BlackNumberInfo> list;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            llLoading.setVisibility(View.INVISIBLE);
            if (list.size() == 0) {
                llAddNumberTips.setVisibility(View.VISIBLE);
            } else {
                // TODO: 2016/4/19 改为查询部分
                CallSmsSafeAdapter adapter = new CallSmsSafeAdapter(CallSmsSafeActivity2.this, list);
                lvCallsmsSafe.setAdapter(adapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_callsms_safe);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);     //必须先setContentView，不然会报空指针
    }

    @Override
    public void initData() {
        final BlackNumberDAO dao = new BlackNumberDAO(this);
        llLoading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = dao.queryAll();
                handler.sendEmptyMessage(0);
            }
        }).start();

    }

    @Override
    public void initListener() {

    }

    @Override
    public void progressClick(View v) {

    }

    @Override
    public void initView() {
    }


}
