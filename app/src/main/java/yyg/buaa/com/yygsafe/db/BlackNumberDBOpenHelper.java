package yyg.buaa.com.yygsafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import yyg.buaa.com.yygsafe.global.Constant;

/**
 * Created by yyg on 16-4-14.
 */
public class BlackNumberDBOpenHelper extends SQLiteOpenHelper{

    public BlackNumberDBOpenHelper(Context context) {
        super(context, Constant.BlackNumber.DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL("create table " + Constant.BlackNumber.TABLE +
                    " (_id integer primary key autoincrement, " +
                    "number varchar(20), " +
                    "mode varchar(2) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
