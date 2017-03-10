package com.example.chulift.demoapplication.Class;

import android.text.Spanned;

/**
 * Created by chulift on 2/18/2017.
 */

public class InputFilter implements android.text.InputFilter {
    private int min, max;

    public InputFilter(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public InputFilter(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
