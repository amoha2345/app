package com.example.myapplication;
// Item.java
public class Item {
    private String id; // Optional: If you have a unique ID in Firestore
    private String title;
    private String description; // Example additional field

    public Item(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
