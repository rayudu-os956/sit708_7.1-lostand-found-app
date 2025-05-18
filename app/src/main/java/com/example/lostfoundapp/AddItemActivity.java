package com.example.lostfoundapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.gms.common.api.Status;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {
    private static final int AUTOCOMPLETE_REQUEST = 100;
    private static final int LOCATION_PERMISSION  = 200;

    private RadioGroup                  rgType;
    private EditText                    edtName, edtPhone, edtDesc, edtDate, edtLocation;
    private Button                      btnCurrentLoc, btnSave;
    private DBHelper                    dbHelper;
    private FusedLocationProviderClient fusedClient;
    private CancellationTokenSource     cancelToken;
    private double                      pickedLat = 0, pickedLng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Up‐arrow on ActionBar
        if (getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Init Places & FusedLocation
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),
                    getString(R.string.google_maps_key));
        }
        cancelToken  = new CancellationTokenSource();
        fusedClient  = LocationServices.getFusedLocationProviderClient(this);

        // Bind views
        rgType        = findViewById(R.id.rgType);
        edtName       = findViewById(R.id.edtName);
        edtPhone      = findViewById(R.id.edtPhone);
        edtDesc       = findViewById(R.id.edtDesc);
        edtDate       = findViewById(R.id.edtDate);
        edtLocation   = findViewById(R.id.edtLocation);
        btnCurrentLoc = findViewById(R.id.btnCurrentLoc);
        btnSave       = findViewById(R.id.btnSave);
        dbHelper      = new DBHelper(this);

        // DatePicker → default today
        String today = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());
        edtDate.setText(today);
        edtDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (DatePicker dp, int y, int m, int d) ->
                            edtDate.setText(String.format(
                                    Locale.getDefault(),
                                    "%04d-%02d-%02d", y, m+1, d)),
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Places Autocomplete
        edtLocation.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.LAT_LNG,
                    Place.Field.ADDRESS
            );
            Intent i = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY,
                    fields
            ).build(this);
            startActivityForResult(i, AUTOCOMPLETE_REQUEST);
        });

        // Get Current Location
        btnCurrentLoc.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                        LOCATION_PERMISSION
                );
            } else {
                fetchCurrentLocation();
            }
        });

        // Save to DB
        btnSave.setOnClickListener(v -> {
            String type = (rgType.getCheckedRadioButtonId() == R.id.rbLost)
                    ? "Lost" : "Found";

            ContentValues cv = new ContentValues();
            cv.put(DBHelper.COL_TYPE,     type);
            cv.put(DBHelper.COL_NAME,     edtName.getText().toString().trim());
            cv.put(DBHelper.COL_PHONE,    edtPhone.getText().toString().trim());
            cv.put(DBHelper.COL_DESC,     edtDesc.getText().toString().trim());
            cv.put(DBHelper.COL_DATE,     edtDate.getText().toString().trim());
            cv.put(DBHelper.COL_LOCATION, edtLocation.getText().toString().trim());
            cv.put(DBHelper.COL_LAT,      pickedLat);
            cv.put(DBHelper.COL_LNG,      pickedLng);

            dbHelper.getWritableDatabase()
                    .insert(DBHelper.TABLE_ITEMS, null, cv);
            finish();
        });
    }

    /** Uses the new getCurrentLocation() API */
    @SuppressLint("MissingPermission")
    private void fetchCurrentLocation() {
        fusedClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                cancelToken.getToken()
        ).addOnSuccessListener(loc -> {
            if (loc != null) {
                pickedLat = loc.getLatitude();
                pickedLng = loc.getLongitude();
                try {
                    Geocoder geo = new Geocoder(this, Locale.getDefault());
                    List<Address> adds = geo
                            .getFromLocation(pickedLat, pickedLng, 1);
                    if (!adds.isEmpty()) {
                        edtLocation.setText(
                                adds.get(0).getAddressLine(0));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Handle Autocomplete result
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if (place.getLatLng() != null) {
                    pickedLat = place.getLatLng().latitude;
                    pickedLng = place.getLatLng().longitude;
                }
                edtLocation.setText(place.getAddress());
            }
            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(this,
                        "Places error: " + status.getStatusMessage(),
                        Toast.LENGTH_LONG).show();
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this,
                        "Place selection cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int code,
                                           @NonNull String[] perms,
                                           @NonNull int[] results) {
        super.onRequestPermissionsResult(code, perms, results);
        if (code == LOCATION_PERMISSION
                && results.length>0
                && results[0]==PackageManager.PERMISSION_GRANTED) {
            fetchCurrentLocation();
        }
    }

    // “Up” button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish(); return true;
        }
        return super.onOptionsItemSelected(item);
    }
}