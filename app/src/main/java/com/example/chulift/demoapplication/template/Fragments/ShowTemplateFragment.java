package com.example.chulift.demoapplication.template.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chulift.demoapplication.classes.Template;
import com.example.chulift.demoapplication.image.ZoomableImageView;
import com.example.chulift.demoapplication.R;
import com.example.chulift.demoapplication.template.ShowTemplateListActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowTemplateFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_TEMPLATE = "template";
    private int param;
    private Template template;
    private Target loadTarget;
    @BindView(R.id.examImg)
    ZoomableImageView _examImg;
    private ProgressDialog progressDialog;

    public ShowTemplateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param = getArguments().getInt(ARG_SECTION_NUMBER);
            String jsonObject;
            jsonObject = getArguments().getString(ARG_TEMPLATE);
            template = new Gson().fromJson(jsonObject, Template.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_template, container, false);
    }

    public static ShowTemplateFragment newInstance(int sectionNumber, Template template) {
        ShowTemplateFragment fragment = new ShowTemplateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_TEMPLATE, new Gson().toJson(template));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("กำลังโหลดข้อมุลเทมเพลท...");
        progressDialog.show();
        ButterKnife.bind(this, view);
        setImage();
    }

    private void setImage() {
        Log.i("Tag", template.getDataIsSet().toString());
        if (template.getDataIsSet() || template != null) {
            final float AS_startX = Float.parseFloat(template.getStartXRate());
            final float AS_startY = Float.parseFloat(template.getStartYRate());
            final float AS_width = Float.parseFloat(template.getWidthRate());
            final float AS_height = Float.parseFloat(template.getHeightRate());
            //Code session
            final float CS_startX = Float.parseFloat(template.getStartXRateCode());
            final float CS_startY = Float.parseFloat(template.getStartYRateCode());
            final float CS_width = Float.parseFloat(template.getWidthRateCode());
            final float CS_height = Float.parseFloat(template.getHeightRateCode());
            //Information session
            final float IS_startX = Float.parseFloat(template.getStartXRateInfo());
            final float IS_startY = Float.parseFloat(template.getStartYRateInfo());
            final float IS_width = Float.parseFloat(template.getWidthRateInfo());
            final float IS_height = Float.parseFloat(template.getHeightRateInfo());
            loadTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    try {
                        bitmap = Bitmap.createBitmap(bitmap);
                    } catch (Exception e) {
                        Log.e("Create bitmap Error", e.toString());
                    }
                    try {
                        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Canvas canvas = new Canvas(mutableBitmap);
                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setAntiAlias(true);
                        paint.setStrokeWidth(5);
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeJoin(Paint.Join.ROUND);
                        paint.setStrokeCap(Paint.Cap.ROUND);
                        _examImg.setImageBitmap(mutableBitmap);

                        int imageView_width = mutableBitmap.getWidth();
                        int imageView_height = mutableBitmap.getHeight();
                        Log.w("iw/ih", _examImg.getWidth() + "," + _examImg.getHeight());
                        Log.w("w/h", mutableBitmap.getWidth() + "," + mutableBitmap.getHeight());
                        float startX = AS_startX * imageView_width;
                        float startY = AS_startY * imageView_height;
                        float endX = startX + (AS_width * imageView_width);
                        float endY = startY + (AS_height * imageView_height);

                        Log.w("value", startX + "," + startY + "," + endX + "," + endY);
                        canvas.drawRect(startX, startY, endX, endY, paint);
                        paint.setColor(Color.GREEN);
                        startX = CS_startX * imageView_width;
                        startY = CS_startY * imageView_height;
                        endX = startX + (CS_width * imageView_width);
                        endY = startY + (CS_height * imageView_height);

                        canvas.drawRect(startX, startY, endX, endY, paint);
                        paint.setColor(Color.BLUE);

                        startX = IS_startX * imageView_width;
                        startY = IS_startY * imageView_height;
                        endX = startX + (IS_width * imageView_width);
                        endY = startY + (IS_height * imageView_height);

                        canvas.drawRect(startX, startY, endX, endY, paint);

                    } catch (Exception e) {
                        Log.e("Draw Rect", e.toString());
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Log.e("onBitmapFailed", "Error to download image.");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(getActivity()).load(template.getTemplatePath()).into(loadTarget);
        } else {
            Log.i("Data Error", "Don't have template");
        }
    }

    @OnClick(R.id.back_btn)
    void back() {
        startActivity(new Intent(getActivity(), ShowTemplateListActivity.class));
        getActivity().finish();
    }
}
