package com.example.chulift.demoapplication.adapter.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chulift.demoapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ExamStorageHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.template_name)
    TextView templateOfExamStorage;
    @BindView(R.id.name_exam_set)
    TextView nameOfExamStorage;
    @BindView(R.id.num_score_exam_set)
    TextView numScoreOfExamStorage;
    @BindView(R.id.sequence_exam_set)
    TextView sequenceExamStorage;
    @BindView(R.id.layout)LinearLayout layout;

    public LinearLayout getLayout() {
        return layout;
    }

    public TextView getNameOfExamStorage() {
        return nameOfExamStorage;
    }

    public TextView getNumScoreOfExamStorage() {
        return numScoreOfExamStorage;
    }

    public TextView getSequenceExamStorage() {
        return sequenceExamStorage;
    }

    public TextView getTemplateOfExamStorage() {
        return templateOfExamStorage;
    }

    public ExamStorageHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
