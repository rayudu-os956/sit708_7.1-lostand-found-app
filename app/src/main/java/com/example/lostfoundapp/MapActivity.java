package com.example.lostfoundapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_map);
        dbHelper = new DBHelper(this);

        SupportMapFragment frag = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        frag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        int count = 0;

        List<Item> items = dbHelper.getAllItems();
        for (Item it : items) {
            LatLng pos = it.getLatLng();
            // if you really want to skip “0,0” fallback coords, add a null check
            map.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(it.getName()));
            builder.include(pos);
            count++;
        }

        if (count>0) {
            map.moveCamera(
                    CameraUpdateFactory.newLatLngBounds(builder.build(), 100)
            );
        } else {
            // no items → some default
            LatLng googleplex = new LatLng(37.4220, -122.0841);
            map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(googleplex, 12f)
            );
        }
    }
}