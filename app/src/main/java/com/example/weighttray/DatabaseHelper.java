package com.example.weighttray;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String dbNAME = "weight_tray";
    public static int VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, dbNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //디비가 생성될 때!
        //유저 정보 테이블
        String sql = "create table if not exists user( "+
                "user_id text primary key, "+
                "password text, "+
                "email text, "+
                "weight_goal)";
        db.execSQL(sql);

        //몸무게 정보 테이블
        String sql2 = "create table if not exists weight_info( "+
                "user_id text, "+
                "weight real, "+
                "date text, " +
                "foreign key(user_id) references user(user_id))";
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > 1){
            db.execSQL("DROP TABLE IF EXISTS user");
            db.execSQL("DROP TABLE IF EXISTS weight_info");
        }
    }
}
