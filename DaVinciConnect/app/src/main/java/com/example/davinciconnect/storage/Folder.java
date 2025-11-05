package com.example.davinciconnect.storage;

public class Folder {
    private String name;
    private int iconRes;

    public Folder(String name, int iconRes) {
        this.name = name;
        this.iconRes = iconRes;
    }

    public String getName() {
        return name;
    }

    public int getIconRes() {
        return iconRes;
    }
}
