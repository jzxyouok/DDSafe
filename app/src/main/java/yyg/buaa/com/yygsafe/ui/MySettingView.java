package yyg.buaa.com.yygsafe.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import yyg.buaa.com.yygsafe.R;

/**
 * 特殊的相对布局。 创建的时候 就把里面的内容初始化出来
 * Created by yyg on 2016/4/14.
 */
public class MySettingView extends RelativeLayout {

    private TypedArray ta;
    private TextView tv_settingview_title;
    private TextView tv_settingview_desc;
    private CheckBox cb_settingview;
    private String[] descs;

    public MySettingView(Context context) {
        super(context);
        init(context);
    }

    public MySettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        //通过这个方法，将在attrs.xml中定义的declare-styleable的所有属性的值存储到TypedArray中
        ta = context.obtainStyledAttributes(attrs, R.styleable.MySettingView);
        String mytitle = ta.getString(R.styleable.MySettingView_mytitle);
        String mydesc = ta.getString(R.styleable.MySettingView_mydesc);
        //获取完TypedArray的值后，一般要调用recycle方法来避免重新创建时的错误
        ta.recycle();
        descs = mydesc.split("#");

        setTitle(mytitle);
        setDesc(false);
    }

    public MySettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.ui_setting_view, MySettingView.this);
        tv_settingview_title = (TextView) findViewById(R.id.tv_settingview_title);
        tv_settingview_desc = (TextView) findViewById(R.id.tv_settingview_desc);
        cb_settingview = (CheckBox) findViewById(R.id.cb_settingview);
        this.setBackgroundResource(R.drawable.list_selector);
    }

    /**
     * 设置自定义view的mytitle
     * @param mytitle
     */
    public void setTitle(String mytitle) {
        tv_settingview_title.setText(mytitle);
    }

    public void setDesc(boolean checked) {
        setChecked(checked);
    }

    public boolean isChecked() {
        return cb_settingview.isChecked();
    }

    public void setChecked(boolean checked) {
        cb_settingview.setChecked(checked);
        if (checked && descs != null) {
            //已经被选中
            tv_settingview_desc.setTextColor(Color.GREEN);
            tv_settingview_desc.setText(descs[0]);

        } else {
            //没有被选中
            tv_settingview_desc.setTextColor(Color.RED);
            tv_settingview_desc.setText(descs[1]);
        }
    }
}
