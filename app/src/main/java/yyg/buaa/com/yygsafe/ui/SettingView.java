package yyg.buaa.com.yygsafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import yyg.buaa.com.yygsafe.R;

/**
 * Created by yyg on 16-4-13.
 */
public class SettingView extends RelativeLayout {

    private Context context;
    private CheckBox cb;

    public SettingView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public SettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init() {
        View.inflate(context, R.layout.ui_setting_view, SettingView.this);
        cb = (CheckBox) findViewById(R.id.cb_setting);

    }

    /**
     *
     * @
     */
    public boolean isSelected() {

    }
}
