package com.example.chulift.demoapplication.classes;

import android.graphics.Color;


public class Menu {
    private String menuID, menuName;
    private int color = Color.WHITE;
    private int imagePath;



    public Menu(String menuID, String menuName, int imagePath, int color) {
        this.menuID = menuID;
        this.menuName = menuName;
        this.imagePath = imagePath;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public String getMenuName() {
        return menuName;
    }

// --Commented out by Inspection START (4/23/2017 7:05 AM):
//    public String getMenuID() {
//        return menuID;
//    }
// --Commented out by Inspection STOP (4/23/2017 7:05 AM)

    public int getImagePath() {
        return imagePath;
    }
}
