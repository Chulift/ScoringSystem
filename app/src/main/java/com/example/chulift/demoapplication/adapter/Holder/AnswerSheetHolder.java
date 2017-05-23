package com.example.chulift.demoapplication.adapter.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chulift.demoapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AnswerSheetHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.answerSheet_id)
    TextView idOfExam;
    @BindView(R.id.score_row)
    TextView scoreOfExam;
    @BindView(R.id.answerSheet_status)
    TextView statusOfAnswerSheet;
    @BindView(R.id.student_code)
    TextView studentCode;
    @BindView(R.id.layout)
    LinearLayout layout;

    public AnswerSheetHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public TextView getStudentCode() {
        return studentCode;
    }

    public TextView getIdOfExam() {
        return idOfExam;
    }

    public TextView getScoreOfExam() {
        return scoreOfExam;
    }

    public TextView getStatusOfAnswerSheet() {
        return statusOfAnswerSheet;
    }
}
