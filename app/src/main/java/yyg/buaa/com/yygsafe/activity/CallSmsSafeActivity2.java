package yyg.buaa.com.yygsafe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yyg.buaa.com.yygsafe.R;
import yyg.buaa.com.yygsafe.activity.base.BaseActivity;
import yyg.buaa.com.yygsafe.adapter.CallSmsSafeAdapter;
import yyg.buaa.com.yygsafe.db.dao.BlackNumberDAO;
import yyg.buaa.com.yygsafe.domain.BlackNumberInfo;
import yyg.buaa.com.yygsafe.utils.UIUtils;

/**
 * 实现数据分页显示
 * Created by yyg on 2016/4/19.
 */
public class CallSmsSafeActivity2 extends BaseActivity {

    @Bind(R.id.ll_add_number_tips)
    LinearLayout llAddNumberTips;
    @Bind(R.id.ll_loading)
    LinearLayout llLoading;
    @Bind(R.id.lv_callsms_safe)
    ListView lvCallsmsSafe;
    @Bind(R.id.prePage)
    Button prePage;
    @Bind(R.id.nextPage)
    Button nextPage;
    @Bind(R.id.jump)
    Button jump;
    @Bind(R.id.et_page_number)
    EditText etPageNumber;
    @Bind(R.id.tv_page_info)
    TextView tvPageInfo;
    private List<BlackNumberInfo> list;
    private static final int pageSize = 20;
    private int currentPageNumber = 0;
    private int totalPage = 0;
    private BlackNumberDAO dao;
    private CallSmsSafeAdapter adapter;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            llLoading.setVisibility(View.INVISIBLE);
            if (list.size() == 0) {
                llAddNumberTips.setVisibility(View.VISIBLE);
            } else {
                llAddNumberTips.setVisibility(View.INVISIBLE);
                //因为list是重新赋值，已经不是原来的list了，所以要重新new adapter
                adapter = new CallSmsSafeAdapter(CallSmsSafeActivity2.this, list, dao);
                lvCallsmsSafe.setAdapter(adapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_callsms_safe2);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);     //必须先setContentView，不然会报空指针
    }

    @Override
    public void initData() {
        dao = new BlackNumberDAO(this);
        totalPage = dao.getTotalNumber() / pageSize;
        tvPageInfo.setText(currentPageNumber + "/" + totalPage );
        llLoading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = dao.queryPart2(currentPageNumber, pageSize);
                handler.sendEmptyMessage(0);
            }
        }).start();

    }

    @Override
    public void initListener() {
        prePage.setOnClickListener(this);
        nextPage.setOnClickListener(this);
        jump.setOnClickListener(this);
    }

    @Override
    public void progressClick(View v) {
        switch (v.getId()) {
            case R.id.prePage:
                if (currentPageNumber <= 0) {
                    UIUtils.showToast(this, "已经是第一页");
                    return;
                }
                currentPageNumber--;
                initData();
                break;
            case R.id.nextPage:
                if (currentPageNumber > (totalPage - 1)) {
                    UIUtils.showToast(this, "已经是最后一页");
                    return;
                }
                currentPageNumber++;
                initData();
                break;
            case R.id.jump:
                String str_pagenumber = etPageNumber.getText().toString().trim();
                if(TextUtils.isEmpty(str_pagenumber)){
                    UIUtils.showToast(this, "页码不能为空");
                }else{
                    int number = Integer.parseInt(str_pagenumber);
                    if(number>=0&&number<totalPage){
                        currentPageNumber = number;
                        initData();
                    }else{
                        UIUtils.showToast(this, "打开失败");
                    }
                }
                break;
        }
    }

    @Override
    public void initView() {
    }


}
