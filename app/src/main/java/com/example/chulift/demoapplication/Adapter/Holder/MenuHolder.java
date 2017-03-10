package com.example.chulift.demoapplication.Adapter.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chulift.demoapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chulift on 3/6/2017.
 */

public class MenuHolder {
    @BindView(R.id.image_menu)
    ImageView imageOfMenu;
    @BindView(R.id.name_menu)
    TextView nameOfMenu;

    public MenuHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public ImageView getImageOfMenu() {
        return imageOfMenu;
    }

    public TextView getNameOfMenu() {
        return nameOfMenu;
    }
}
