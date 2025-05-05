package com.example.lostfoundapp;

public class Item {
    public long   id;
    public String title;
    public String description;
    public String type;
    public String date;

    public Item(long id, String title, String description, String type, String date) {
        this.id          = id;
        this.title       = title;
        this.description = description;
        this.type        = type;
        this.date        = date;
    }
}