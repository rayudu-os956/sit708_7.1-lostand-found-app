package com.example.lostfoundapp;

import com.google.android.gms.maps.model.LatLng;

public class Item {
    public long   id;
    public String type;
    public String name;
    public String phone;
    public String description;
    public String date;
    public String location;
    public double latitude;
    public double longitude;

    public Item(long id,
                String type,
                String name,
                String phone,
                String description,
                String date,
                String location,
                double latitude,
                double longitude) {
        this.id          = id;
        this.type        = type;
        this.name        = name;
        this.phone       = phone;
        this.description = description;
        this.date        = date;
        this.location    = location;
        this.latitude    = latitude;
        this.longitude   = longitude;
    }

    /** Returns a LatLng or null if we didn’t save coords */
    public LatLng getLatLng() {
        // you could also check for a sentinel like latitude==0&&longitude==0
        return new LatLng(latitude, longitude);
    }

    /** MapActivity wants a title for the marker */
    public String getName() {
        return name;
    }
}