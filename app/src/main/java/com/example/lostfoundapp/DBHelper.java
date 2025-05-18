package com.example.lostfoundapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME      = "lostfound.db";
    public static final int    DB_VERSION   = 3;

    public static final String TABLE_ITEMS  = "items";
    public static final String COL_ID       = "_id";
    public static final String COL_TYPE     = "type";
    public static final String COL_NAME     = "name";
    public static final String COL_PHONE    = "phone";
    public static final String COL_DESC     = "description";
    public static final String COL_DATE     = "date";
    public static final String COL_LOCATION = "location";
    public static final String COL_LAT      = "latitude";
    public static final String COL_LNG      = "longitude";

    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_ITEMS + " (" +
                    COL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TYPE  + " TEXT, " +
                    COL_NAME  + " TEXT, " +
                    COL_PHONE + " TEXT, " +
                    COL_DESC  + " TEXT, " +
                    COL_DATE  + " TEXT, " +
                    COL_LOCATION + " TEXT, " +
                    COL_LAT   + " REAL, " +
                    COL_LNG   + " REAL" +
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

    /** Query all rows and build a List<Item> */
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_ITEMS,
                null,   // all columns
                null, null, null, null,
                COL_ID + " DESC"  // newest first
        );

        if (c.moveToFirst()) {
            do {
                long   id       = c.getLong(c.getColumnIndexOrThrow(COL_ID));
                String type     = c.getString(c.getColumnIndexOrThrow(COL_TYPE));
                String name     = c.getString(c.getColumnIndexOrThrow(COL_NAME));
                String phone    = c.getString(c.getColumnIndexOrThrow(COL_PHONE));
                String desc     = c.getString(c.getColumnIndexOrThrow(COL_DESC));
                String date     = c.getString(c.getColumnIndexOrThrow(COL_DATE));
                String location = c.getString(c.getColumnIndexOrThrow(COL_LOCATION));
                double lat      = c.getDouble(c.getColumnIndexOrThrow(COL_LAT));
                double lng      = c.getDouble(c.getColumnIndexOrThrow(COL_LNG));

                items.add(new Item(
                        id, type, name, phone, desc, date, location, lat, lng
                ));
            } while (c.moveToNext());
        }
        c.close();
        return items;
    }
}