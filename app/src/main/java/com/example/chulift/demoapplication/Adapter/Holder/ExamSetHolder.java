package com.example.chulift.demoapplication.Adapter.Holder;

import android.view.View;
import android.widget.TextView;

import com.example.chulift.demoapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chulift on 3/6/2017.
 */

public class ExamSetHolder {
    @BindView(R.id.template_name)
    TextView templateOfSetExam;
    @BindView(R.id.name_exam_set)
    TextView nameOfSetExam;
    @BindView(R.id.num_score_exam_set)
    TextView numScoreOfexamSet;
    @BindView(R.id.sequence_exam_set)
    TextView sequenceExamSet;

    public TextView getNameOfSetExam() {
        return nameOfSetExam;
    }

    public TextView getNumScoreOfexamSet() {
        return numScoreOfexamSet;
    }

    public TextView getSequenceExamSet() {
        return sequenceExamSet;
    }

    public TextView getTemplateOfSetExam() {
        return templateOfSetExam;
    }

    public ExamSetHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
