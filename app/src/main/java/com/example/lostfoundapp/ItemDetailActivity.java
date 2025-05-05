package com.example.lostfoundapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private long     itemId;

    private TextView tvTitle, tvDate, tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper   = new DBHelper(this);
        itemId     = getIntent().getLongExtra("itemId", -1);

        tvTitle    = findViewById(R.id.tvTitle);
        tvDate     = findViewById(R.id.tvDate);
        tvLocation = findViewById(R.id.tvLocation);
        Button btnRemove = findViewById(R.id.btnRemove);

        loadItem();
        btnRemove.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(DBHelper.TABLE_ITEMS, DBHelper.COL_ID+"=?", new String[]{String.valueOf(itemId)});
            startActivity(new Intent(this, ListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });
    }

    private void loadItem() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TABLE_ITEMS, null, DBHelper.COL_ID+"=?",
                new String[]{String.valueOf(itemId)}, null,null,null);
        if(c.moveToFirst()){
            tvTitle.setText(c.getString(c.getColumnIndexOrThrow(DBHelper.COL_NAME)));
            tvDate .setText(c.getString(c.getColumnIndexOrThrow(DBHelper.COL_DATE)));
            tvLocation.setText(c.getString(c.getColumnIndexOrThrow(DBHelper.COL_LOCATION)));
        }
        c.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){ finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}