package com.example.chulift.demoapplication.Answer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.chulift.demoapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowSelectedAnswerActivity extends Activity {

    //private Template template;
    private Boolean isEmptyTemplate;
    @BindView(R.id.showAns)TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_selected_answer);

        String jsonMyObject = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("myObject");
            //template = new Gson().fromJson(jsonMyObject, Template.class);
        }
        else {
            Log.d("Gson", "Fail");
            isEmptyTemplate = true;
        }
        ButterKnife.bind(this);
        String str = "";
        for(int i = 0;i< SelectAnswerActivity.array_a.length;i++) {
            // textView.setText(textView.getText()+" "+i+"."+"("+SelectAnswerActivity.array_a[i]+")");
            str += getString(R.string.tab)+(i+1)+"."+"("+SelectAnswerActivity.array_a[i]+")";
            if((i+1)%5 ==0)
                str +="\n";
        }
        textView.setText(str);
    }
}
