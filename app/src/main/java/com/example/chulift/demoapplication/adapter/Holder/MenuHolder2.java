package com.example.chulift.demoapplication.adapter.Holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chulift.demoapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuHolder2 extends RecyclerView.ViewHolder {
    @BindView(R.id.card_view)
    CardView cardView;
    @BindView(R.id.menu_name)
    TextView nameOfMenu;

    public MenuHolder2(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public CardView getCardView() {
        return cardView;
    }

    public TextView getNameOfMenu() {
        return nameOfMenu;
    }
}
