package yyg.buaa.com.yygsafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

import yyg.buaa.com.yygsafe.db.BlackNumberDBOpenHelper;
import yyg.buaa.com.yygsafe.domain.BlackNumberInfo;
import yyg.buaa.com.yygsafe.global.Constant;

/**
 * Created by yyg on 16-4-14.
 */
public class BlackNumberDAO {

    private final BlackNumberDBOpenHelper helper;

    public BlackNumberDAO(Context context) {
        helper = new BlackNumberDBOpenHelper(context);
    }

    /**
     * 黑名单插入数据
     * @param number
     * @param mode
     * @return
     */
    public boolean insertDao(String number, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", mode);
        long rowid = db.insert(Constant.BlackNumber.TABLE, null, values);
        if (rowid == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 黑名单删除数据
     * @param number
     * @return
     */
    public boolean deleteDao(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rownum = db.delete(Constant.BlackNumber.TABLE, "number = ?", new String[]{number});
        if (rownum == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 黑名单修改mode数据
     * @param number
     * @param newmode
     * @return
     */
    public boolean changeMode(String number, String newmode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", newmode);
        long rownum = db.update(Constant.BlackNumber.TABLE, values, "number = ?", new String[]{number});
        if (rownum == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 黑名单根据number查询mode数据
     * @param number
     * @return
     */
    public String queryMode(String number) {
        String mode = "0";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(Constant.BlackNumber.TABLE, new String[]{"mode"}, "number = ?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            mode = cursor.getString(cursor.getColumnIndex("mode"));
        }
        cursor.close();
        db.close();
        return mode;
    }

    /**
     * 黑名单查询所有数据
     * @return
     */
    public List<BlackNumberInfo> queryAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(Constant.BlackNumber.TABLE, null, null, null, null, null, null);
        List<BlackNumberInfo> infoList = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            info.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            info.setMode(cursor.getString(cursor.getColumnIndex("mode")));
            infoList.add(info);
        }
        cursor.close();
        db.close();
        SystemClock.sleep(3000);
        return infoList;
    }
}
