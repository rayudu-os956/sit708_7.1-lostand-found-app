package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnCreate).setOnClickListener(v ->
                startActivity(new Intent(this, AddItemActivity.class))
        );
        findViewById(R.id.btnShowAll).setOnClickListener(v ->
                startActivity(new Intent(this, ListActivity.class))
        );
        findViewById(R.id.btnShowMap).setOnClickListener(v ->
                startActivity(new Intent(this, MapActivity.class))
        );
    }
}