package com.example.lostfoundapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DBHelper(this);
        ListView lv = findViewById(R.id.lvItems);

        // Bind TYPE, DESCRIPTION, and DATE to the three TextViews
        String[] fromFields = { DBHelper.COL_TYPE, DBHelper.COL_DESC, DBHelper.COL_DATE };
        int[]    toViews    = { R.id.txtType,      R.id.txtTitle,     R.id.txtDate  };

        adapter = new SimpleCursorAdapter(
                this,
                R.layout.item_list_row,
                null,
                fromFields,
                toViews,
                0
        );
        lv.setAdapter(adapter);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(this, ItemDetailActivity.class);
            i.putExtra("itemId", id);
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(
                DBHelper.TABLE_ITEMS,
                null, null, null, null, null,
                DBHelper.COL_ID + " DESC"
        );
        adapter.changeCursor(c);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}