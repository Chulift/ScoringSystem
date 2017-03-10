package com.example.chulift.demoapplication.Class;

import android.graphics.Color;

/**
 * Created by Chulift on 11/15/2016.
 */

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

    public String getMenuID() {
        return menuID;
    }

    public int getImagePath() {
        return imagePath;
    }
}
