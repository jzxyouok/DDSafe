package yyg.buaa.com.yygsafe.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import yyg.buaa.com.yygsafe.global.Constant;
import yyg.buaa.com.yygsafe.utils.UIUtils;

/**
 * Created by yyg on 2016/4/19.
 */
public class CallSmsSafeActivity extends BaseActivity {

    @Bind(R.id.button)
    Button button;
    @Bind(R.id.ll_add_number_tips)
    LinearLayout llAddNumberTips;
    @Bind(R.id.ll_loading)
    LinearLayout llLoading;
    @Bind(R.id.lv_callsms_safe)
    ListView lvCallsmsSafe;
    @Bind(R.id.spiltpage)
    Button spiltpage;

    private List<BlackNumberInfo> list;

    /**
     * 开始获取数据的位置
     */
    private int startIndex = 0;

    /**
     * 一次最多获取几条数据
     */
    private int maxCount = 20;

    private int totalCount = 0;
    private BlackNumberDAO dao;
    private CallSmsSafeAdapter adapter;
    /**
     * 消息处理器
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            llLoading.setVisibility(View.INVISIBLE);
            if (list.size() == 0) {
                //没有数据，设置添加数据的提醒
                llAddNumberTips.setVisibility(View.VISIBLE);
            } else {
                //list
                if (adapter == null) {  //减少不必要的new
                    adapter = new CallSmsSafeAdapter(CallSmsSafeActivity.this, list, dao);
                    lvCallsmsSafe.setAdapter(adapter);
                } else {
                    //数据适配器是已经存在的，list还是以前的list
                    //因为数据适配器里的数据发生变化，刷新界面。
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_callsms_safe);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);     //必须先setContentView，不然会报空指针
        fillDate();
    }

    private void fillDate() {
        dao = new BlackNumberDAO(this);

        totalCount = dao.getTotalNumber();
        //数据库的总条目个数 / 每个页面最多显示多少条数据
        //耗时的操作逻辑应该放在子线程里面
        llLoading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (list == null) {
                    list = dao.queryPart(startIndex, maxCount);
                } else {
                    //集合里面原来有数据，新的数据应该放在旧的集合的后面
                    list.addAll(dao.queryPart(startIndex, maxCount));
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public void initListener() {
        button.setOnClickListener(this);
        spiltpage.setOnClickListener(this);
        lvCallsmsSafe.setOnScrollListener(new AbsListView.OnScrollListener() {
            // 滚动状态发生变化调用的方法。
            // AbsListView.OnScrollListener.SCROLL_STATE_FLING 惯性滑动
            // AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL 触摸滑动
            // AbsListView.OnScrollListener.SCROLL_STATE_IDLE 静止
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: //静止状态
                        //判断是不是最后一个条目
                        int lastPosition = lvCallsmsSafe.getLastVisiblePosition();
                        System.out.println("最后一个可见条目的位置：" + lastPosition);
                        if (lastPosition == list.size() - 1) {
                            //加载更多的数据，更改加载数据的开始位置
                            startIndex += maxCount;
                            if (startIndex >= totalCount) {
                                UIUtils.showToast(CallSmsSafeActivity.this, "没有更多的数据了");
                                return;
                            }
                            fillDate();
                        }
                }
            }

            //只要listView发生滚动就会调用下面的方法
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    @Override
    public void progressClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View dialogView = View.inflate(this, R.layout.dialog_add_blacknumber, null);
                final ViewHolder holder = new ViewHolder(dialogView);
                final AlertDialog dialog = builder.create();    //使用create，因为AlertDialog可以修改距窗体四边间距
                holder.btOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String blackNumber = holder.etBlackNumber.getText().toString().trim();
                        if (TextUtils.isEmpty(blackNumber)) {
                            UIUtils.showToast(CallSmsSafeActivity.this, "请输入号码");
                            return;
                        }
                        String mode = Constant.BlackNumber.MODE_NONE;
                        if (holder.cbPhone.isChecked() && holder.cbSms.isChecked()) {
                            mode = Constant.BlackNumber.MODE_ALL;
                        } else if (holder.cbPhone.isChecked()) {
                            mode = Constant.BlackNumber.MODE_PHONE;
                        } else if (holder.cbSms.isChecked()) {
                            mode = Constant.BlackNumber.MODE_SMS;
                        } else {
                            UIUtils.showToast(CallSmsSafeActivity.this, "请选择拦截模式");
                            return;
                        }
                        //把数据添加到数据库
                        boolean result = dao.insertDao(blackNumber, mode);
                        //刷新界面，把数据加入到list集合里面
                        if (result) {
                            BlackNumberInfo info = new BlackNumberInfo();
                            info.setNumber(blackNumber);
                            info.setMode(mode);
                            list.add(0, info);  //把数据添加到第0个位置
                            //通知界面刷新
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            } else {
                                adapter = new CallSmsSafeAdapter(CallSmsSafeActivity.this, list, dao);
                                lvCallsmsSafe.setAdapter(adapter);
                            }
                        }
                        dialog.dismiss();
                    }
                });
                holder.btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.setView(dialogView, 0, 0, 0, 0);
                dialog.show();
                break;
            case R.id.spiltpage:
                startActivity(CallSmsSafeActivity2.class);
        }
    }

    @Override
    public void initView() {
    }


    @Override
    public void initData() {
    }

    static class ViewHolder {
        @Bind(R.id.et_black_number)
        EditText etBlackNumber;
        @Bind(R.id.cb_phone)
        CheckBox cbPhone;
        @Bind(R.id.cb_sms)
        CheckBox cbSms;
        @Bind(R.id.bt_ok)
        Button btOk;
        @Bind(R.id.bt_cancel)
        Button btCancel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
