package yyg.buaa.com.yygsafe.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import java.util.List;

import yyg.buaa.com.yygsafe.domain.ContactInfo;

/**
 * Created by yyg on 2016/4/9.
 */
public class ContactInfoParser {

    /**
     * 获取系统全部联系人的API方法
     * @return
     */
    public static List<ContactInfo> findAll(Context context) {
        ContentResolver resolver = context.getContentResolver();
        //1.查询raw_contacts表，把联系人的id取出来
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        List<ContactInfo> infos = new ArrayList<ContactInfo>();
        Cursor cursor = resolver.query(uri, new String[]{"contact_id"}, null, null, null);
        while(cursor.moveToNext()) {
            String id = cursor.getString(0);
            if (!TextUtils.isEmpty(id)) {
                Logger.i("联系人id:", id);
                ContactInfo info = new ContactInfo();
                info.setId(id);

                //2.根据联系人的id,查询data表，把这个id的数据取出来
                //系统api，查询data的时候不是真正的查询data，而是查询的data表的视图
                Cursor dataCursor = resolver.query(dataUri, new String[]{"data1", "mimetype"},
                        "raw_contact_id = ?", new String[]{id}, null);
                while (dataCursor.moveToNext()) {
                    String data1 = dataCursor.getString(0);
                    String mimetype = dataCursor.getString(1);
                    if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        Logger.i("姓名：" + data1);
                        info.setName(data1);
                    } else if ("vnd.android.cursor.item/email_v2".equals(mimetype)) {
                        Logger.i("邮箱：" + data1);
                        info.setEmail(data1);
                    } else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                        Logger.i("电话：" + data1);
                        info.setPhone(data1);
                    } else if ("vnd.android.cursor.item/im".equals(mimetype)) {
                        Logger.i("QQ：" + data1);
                        info.setQq(data1);
                    }
                }
                infos.add(info);
                Log.i("ContactInfoParser", "--------------------");
                dataCursor.close();
            }
        }
        cursor.close();
        return infos;
    }
}
