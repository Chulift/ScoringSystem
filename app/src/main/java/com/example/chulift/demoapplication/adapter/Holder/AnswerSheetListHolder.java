package com.example.chulift.demoapplication.adapter.Holder;

import android.view.View;
import android.widget.TextView;

import com.example.chulift.demoapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;



public class AnswerSheetListHolder {
    @BindView(R.id.answerSheet_id)
    TextView idOfExam;
    @BindView(R.id.score_row)
    TextView scoreOfExam;
    @BindView(R.id.answerSheet_status)
    TextView statusOfAnswerSheet;

    public AnswerSheetListHolder(View view) {
        ButterKnife.bind(this, view);
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
