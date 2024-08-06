package com.example.myapplication;// Row.java
import java.util.ArrayList;
import java.util.List;

public class Row {
    private String title;
    private List<Item> items;

    public Row(String title) {
        this.title = title;
        items = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }
}
