package com.example.chulift.demoapplication.adapter.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chulift.demoapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;



public class MenuHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.image_menu)
    ImageView imageOfMenu;
    @BindView(R.id.name_menu)
    TextView nameOfMenu;
    @BindView(R.id.menuLayout)
    LinearLayout menuLayout;


    public MenuHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public LinearLayout getMenuLayout() {
        return menuLayout;
    }

    public ImageView getImageOfMenu() {
        return imageOfMenu;
    }

    public TextView getNameOfMenu() {
        return nameOfMenu;
    }
}
