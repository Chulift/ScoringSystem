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
    int numChoice;

    public SelectAnswerAdapter(Context context, ArrayList arrayList, int numChoice) {
        this.context = context;
        this.arrayList = arrayList;
        this.numChoice = numChoice;
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

            switch (numChoice) {
                case 3:
                    convertView = inflater.inflate(R.layout.answerselect_3, null);
                    break;
                case 4:
                    convertView = inflater.inflate(R.layout.answerselect_4, null);
                    break;
                default:
                    convertView = inflater.inflate(R.layout.answerselect_5, null);
                    break;
            }

        return initAnswer(position, convertView);
    }

    private View initAnswer(final int position, View convertView) {
        switch (numChoice) {
            case 3:
                return initThreeAnswer(position, convertView);
            case 4:
                return initFourAnswer(position, convertView);
            default:
                return initFiveAnswer(position, convertView);
        }
    }

    public View initThreeAnswer(final int position, View convertView) {

        TextView textView1 = (TextView) convertView.findViewById(R.id.number_answerSelect);
        Button button1 = (Button) convertView.findViewById(R.id.button1);
        Button button2 = (Button) convertView.findViewById(R.id.button2);
        Button button3 = (Button) convertView.findViewById(R.id.button3);
        textView1.setText("" + (position + 1));
        final Button[] buttons = {button1, button2, button3};
        String[] values = {"ก", "ข", "ค"};
        for (int i = 0; i < buttons.length; i++) {
            final int finalI = i;
            buttons[i].setText(values[i]);
            if (SelectAnswerActivity.array_a[position] == i + 1) {
                buttons[i].setBackgroundResource(R.color.green);
            }
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectAnswerActivity.array_a[position] = finalI + 1;
                    changeColor(buttons, finalI);
                }
            });
        }
        return convertView;
    }

    private View initFiveAnswer(final int position, View convertView) {
        TextView textView1 = (TextView) convertView.findViewById(R.id.number_answerSelect);
        Button button1 = (Button) convertView.findViewById(R.id.button1);
        Button button2 = (Button) convertView.findViewById(R.id.button2);
        Button button3 = (Button) convertView.findViewById(R.id.button3);
        Button button4 = (Button) convertView.findViewById(R.id.button4);
        Button button5 = (Button) convertView.findViewById(R.id.button5);
        textView1.setText("" + (position + 1));
        final Button[] buttons = {button1, button2, button3,button4,button5};
        String[] values = {"ก", "ข", "ค", "ง", "จ"};
        for (int i = 0; i < buttons.length; i++) {
            final int finalI = i;
            buttons[i].setText(values[i]);
            if (SelectAnswerActivity.array_a[position] == i + 1) {
                buttons[i].setBackgroundResource(R.color.green);
            }
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectAnswerActivity.array_a[position] = finalI + 1;
                    changeColor(buttons, finalI);
                }
            });
        }
        return convertView;
    }

    public View initFourAnswer(final int position, View convertView) {
        TextView textView1 = (TextView) convertView.findViewById(R.id.number_answerSelect);
        Button button1 = (Button) convertView.findViewById(R.id.button1);
        Button button2 = (Button) convertView.findViewById(R.id.button2);
        Button button3 = (Button) convertView.findViewById(R.id.button3);
        Button button4 = (Button) convertView.findViewById(R.id.button4);
        textView1.setText("" + (position + 1));
        final Button[] buttons = {button1, button2, button3, button4};
        String[] values = {"ก", "ข", "ค", "ง"};
        for (int i = 0; i < buttons.length; i++) {
            final int finalI = i;
            buttons[i].setText(values[i]);
            if (SelectAnswerActivity.array_a[position] == i + 1) {
                buttons[i].setBackgroundResource(R.color.green);
            }
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectAnswerActivity.array_a[position] = finalI + 1;
                    changeColor(buttons, finalI);
                }
            });
        }
        return convertView;
    }

    private void changeColor(Button[] buttons, int index) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBackgroundResource(R.color.iron);
        }
        buttons[index].setBackgroundResource(R.color.green);
    }


}
