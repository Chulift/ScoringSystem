package com.example.chulift.demoapplication.adapter.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chulift.demoapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;



public class TemplateHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.template_layout)
    LinearLayout templateLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.template_max_score)
    TextView templateMaxScore;
    @BindView(R.id.template_max_choice)
    TextView templateMaxChoice;

    public TemplateHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public ImageView getImg() {
        return img;
    }

    public LinearLayout getTemplateLayout() {
        return templateLayout;
    }

    public TextView getTemplateMaxChoice() {
        return templateMaxChoice;
    }

    public TextView getTemplateMaxScore() {
        return templateMaxScore;
    }

    public TextView getTitle() {
        return title;
    }
}
