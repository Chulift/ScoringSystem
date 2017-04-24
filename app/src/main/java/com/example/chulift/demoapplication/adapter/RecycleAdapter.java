package com.example.chulift.demoapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chulift.demoapplication.adapter.Holder.ViewHolder;
import com.example.chulift.demoapplication.examStorage.CUExamStorageActivity;
import com.example.chulift.demoapplication.template.EditTemplateActivity;
import com.example.chulift.demoapplication.classes.Template;
import com.example.chulift.demoapplication.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;


public class RecycleAdapter extends RecyclerView.Adapter<ViewHolder> {
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
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_gallery_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final Template template = (Template) galleryList.get(position);
        viewHolder.getTitle().setText(template.getUser_input_template_name());
        viewHolder.getTemplateMaxChoice().setText("จำนวนตัวเลือก : " + ((Template) galleryList.get(position)).getNumberOfChoice());
        final int max = Integer.parseInt(template.getAnswerPerCol()) * Integer.parseInt(template.getNumberOfCol());
        String maxScore = max + "(" + template.getAnswerPerCol() + "*" + template.getNumberOfCol() + ")";
        viewHolder.getTemplateMaxScore().setText("จำนวนข้อสูงสุด : " + maxScore);
        String imgUrl = ((Template) galleryList.get(position)).getTemplate_path();
        Picasso.with(context).load(imgUrl).into(viewHolder.getImg());

        if (Objects.equals(page, "CUExamStorageActivity")) {
            //if (CUExamStorageActivity.templateSet != null) {
            if (CUExamStorageActivity.IssetTemplate) {
                if (CUExamStorageActivity.IDTemplate != null) {
                    //if (CUExamStorageActivity.templateSet.getId_template().equals(template.getId_template())) {
                    if (CUExamStorageActivity.IDTemplate.equals(template.getId_template())) {
                        CUExamStorageActivity.templateSet = template;
                        viewHolder.getTemplateLayout().setBackgroundResource(R.drawable.border_indigo);
                    } else {
                        viewHolder.getTemplateLayout().setBackgroundResource(R.drawable.border);
                    }
                }

            }
        }

        viewHolder.getTemplateLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(page, "CUExamStorageActivity")) {
                    // v.setEnabled(false);
                    selectedRow = viewHolder.getAdapterPosition();
                    Log.i("selectedRow", selectedRow + "");
                    CUExamStorageActivity.IssetTemplate = true;
                    CUExamStorageActivity.templateSet = template;
                    CUExamStorageActivity.IDTemplate = template.getId_template();
                    //CUExamStorageActivity.numScore.setText(max+"");
                    Toast.makeText(context, "เลือก " + template.getUser_input_template_name() + "    " + viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();

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

}
