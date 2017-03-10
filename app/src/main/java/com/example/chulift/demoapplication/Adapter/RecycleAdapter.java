package com.example.chulift.demoapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chulift.demoapplication.ExamSet.CUExamSetActivity;
import com.example.chulift.demoapplication.Template.EditTemplateActivity;
import com.example.chulift.demoapplication.Class.Template;
import com.example.chulift.demoapplication.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chulift on 2/9/2017.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    private ArrayList galleryList;
    private Context context;
    private String page;
    private int selectedRow = -1;

    public RecycleAdapter(Context context, ArrayList galleryList) {
        this.galleryList = galleryList;
        this.context = context;
        this.page = "default";
    }

    public RecycleAdapter(Context context, ArrayList galleryList, String page) {
        this.galleryList = galleryList;
        this.context = context;
        this.page = page;
    }

    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_gallery_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleAdapter.ViewHolder viewHolder, final int position) {
        final Template template = (Template) galleryList.get(position);
        viewHolder.title.setText(template.getUser_input_template_name());
        viewHolder.templateMaxChoice.setText("จำนวนตัวเลือก : " + ((Template) galleryList.get(position)).getNumberOfChoice());
        final int max = Integer.parseInt(template.getAnswerPerCol()) * Integer.parseInt(template.getNumberOfCol());
        String maxScore = max + "(" + template.getAnswerPerCol() + "*" + template.getNumberOfCol() + ")";
        viewHolder.templateMaxScore.setText("จำนวนข้อสูงสุด : " + maxScore);
        String imgUrl = ((Template) galleryList.get(position)).getTemplate_path();
        Picasso.with(context).load(imgUrl).into(viewHolder.img);

        if (page == "CUExamSetActivity") {
            //if (CUExamSetActivity.templateSet != null) {
            if (CUExamSetActivity.IssetTemplate) {
                if (CUExamSetActivity.IDTemplate != null) {
                    //if (CUExamSetActivity.templateSet.getId_template().equals(template.getId_template())) {
                    if (CUExamSetActivity.IDTemplate.equals(template.getId_template())) {
                        CUExamSetActivity.templateSet = template;
                        CUExamSetActivity.position = position;
                        viewHolder.templateLayout.setBackgroundResource(R.drawable.border_indigo);
                    } else {
                        viewHolder.templateLayout.setBackgroundResource(R.drawable.border);
                    }
                }

            }
        }

        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page == "CUExamSetActivity") {
                    // v.setEnabled(false);
                    selectedRow = position;
                    Log.i("selectedRow", selectedRow + "");
                    CUExamSetActivity.IssetTemplate = true;
                    CUExamSetActivity.templateSet = template;
                    CUExamSetActivity.IDTemplate = template.getId_template();
                    //CUExamSetActivity.numScore.setText(max+"");
                    Toast.makeText(context, "เลือก " + template.getUser_input_template_name() + "    " + position, Toast.LENGTH_SHORT).show();

                    notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(context, EditTemplateActivity.class);
                    intent.putExtra("template", new Gson().toJson(template));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
