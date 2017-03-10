package com.example.chulift.demoapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.chulift.demoapplication.Answer.SelectAnswerActivity;
import com.example.chulift.demoapplication.R;

import java.util.ArrayList;

/**
 * Created by Level51 on 1/23/2017.
 */

public class SelectAnswerAdapter extends BaseAdapter {
    Context context;
    ArrayList arrayList;
    public SelectAnswerAdapter(Context context,ArrayList arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.answerselect_4, null);
            return initAnswer(position,convertView);
    }
    private View initAnswer(final int position, View convertView) {
        TextView textView1 = (TextView)convertView.findViewById(R.id.number_answerSelect) ;
        Button button1 = (Button)convertView.findViewById(R.id.button1);
        Button button2 = (Button)convertView.findViewById(R.id.button2);
        Button button3 = (Button)convertView.findViewById(R.id.button3);
        Button button4 = (Button)convertView.findViewById(R.id.button4);
        textView1.setText(""+(position+1));
        final Button[] buttons = {button1,button2,button3,button4};
        String[] values = {"A","B","C","D"};
        for (int i = 0; i < buttons.length;i++){
            final int finalI = i;
            buttons[i].setText(values[i]);
            if(SelectAnswerActivity.array_a[position] == i+1){
                buttons[i].setBackgroundResource(R.color.green);
            }
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectAnswerActivity.array_a[position] = finalI +1;
                    changeColor(buttons,finalI);
                }
            });
        }

        return convertView;
    }
    private void changeColor(Button[] buttons,int index){
        for (int i = 0; i <  buttons.length;i++){
            buttons[i].setBackgroundResource(R.color.iron);
        }
        buttons[index].setBackgroundResource(R.color.green);
    }


}
