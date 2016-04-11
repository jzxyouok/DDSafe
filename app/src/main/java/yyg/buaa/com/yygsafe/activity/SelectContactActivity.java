package yyg.buaa.com.yygsafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import yyg.buaa.com.yygsafe.R;
import yyg.buaa.com.yygsafe.activity.base.BaseActivity;
import yyg.buaa.com.yygsafe.adapter.SelectContactAdapter;
import yyg.buaa.com.yygsafe.domain.ContactInfo;
import yyg.buaa.com.yygsafe.engine.ContactInfoParser;

/**
 * Created by yyg on 2016/4/9.
 */
public class SelectContactActivity extends BaseActivity {

    private ListView lv_contacts;

    @Override
    public void initView() {
        setContentView(R.layout.activity_contacts);
        lv_contacts = (ListView) findViewById(R.id.lv_contacts);
    }

    @Override
    public void initData() {
        final List<ContactInfo> infos = ContactInfoParser.findAll(this);
        SelectContactAdapter adapter = new SelectContactAdapter(this, infos);
        lv_contacts.setAdapter(adapter);

        //条目点击侦听
        lv_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                String phone = infos.get(position).getPhone();
                intent.putExtra("phone", phone);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void initListener() {

    }

    @Override
    public void progressClick(View v) {

    }
}
