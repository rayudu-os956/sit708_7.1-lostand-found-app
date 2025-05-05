package com.example.lostfoundapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME     = "lostfound.db";
    public static final int    DB_VERSION  = 2;

    public static final String TABLE_ITEMS = "items";
    public static final String COL_ID      = "_id";
    public static final String COL_TYPE    = "type";
    public static final String COL_NAME    = "name";
    public static final String COL_PHONE   = "phone";
    public static final String COL_DESC    = "description";
    public static final String COL_DATE    = "date";
    public static final String COL_LOCATION = "location";

    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_ITEMS + " (" +
                    COL_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TYPE    + " TEXT, " +
                    COL_NAME    + " TEXT, " +
                    COL_PHONE   + " TEXT, " +
                    COL_DESC    + " TEXT, " +
                    COL_DATE    + " TEXT, " +
                    COL_LOCATION+ " TEXT" +
                    ");";

    public DBHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }
}