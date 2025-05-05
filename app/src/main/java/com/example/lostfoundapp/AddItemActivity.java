package com.example.lostfoundapp;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {
    private RadioGroup rgType;
    private EditText   edtName, edtPhone, edtDesc, edtDate, edtLocation;
    private DBHelper   dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Init
        dbHelper    = new DBHelper(this);
        rgType      = findViewById(R.id.rgType);
        edtName     = findViewById(R.id.edtName);
        edtPhone    = findViewById(R.id.edtPhone);
        edtDesc     = findViewById(R.id.edtDesc);
        edtDate     = findViewById(R.id.edtDate);
        edtLocation = findViewById(R.id.edtLocation);
        Button btnSave = findViewById(R.id.btnSave);

        // Pre‐fill today
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());
        edtDate.setText(today);

        // Show date picker on click
        edtDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int y = cal.get(Calendar.YEAR),
                    m = cal.get(Calendar.MONTH),
                    d = cal.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(
                    AddItemActivity.this,
                    (DatePicker dp, int selY, int selM, int selD) -> {
                        String dateStr = String.format(Locale.getDefault(),
                                "%04d-%02d-%02d", selY, selM + 1, selD);
                        edtDate.setText(dateStr);
                    },
                    y, m, d
            ).show();
        });

        // Save handler
        btnSave.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {
        String type     = (rgType.getCheckedRadioButtonId() == R.id.rbLost)
                ? "Lost" : "Found";
        String name     = edtName.getText().toString().trim();
        String phone    = edtPhone.getText().toString().trim();
        String desc     = edtDesc.getText().toString().trim();
        String date     = edtDate.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COL_TYPE,     type);
        cv.put(DBHelper.COL_NAME,     name);
        cv.put(DBHelper.COL_PHONE,    phone);
        cv.put(DBHelper.COL_DESC,     desc);
        cv.put(DBHelper.COL_DATE,     date);
        cv.put(DBHelper.COL_LOCATION, location);
        db.insert(DBHelper.TABLE_ITEMS, null, cv);

        finish();
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