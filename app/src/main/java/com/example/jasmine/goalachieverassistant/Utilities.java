package com.example.jasmine.goalachieverassistant;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jkhinda on 31/01/18.
 */

public class Utilities {

    @NonNull
    public static String parseDateForDisplay(Date date){
        SimpleDateFormat year =  new SimpleDateFormat("yyyy");
        String selectedYear = year.format(date);
        String dateToDisplay;
        Log.d("GOALS", "selected year  "+ selectedYear);
        Log.d("GOALS", "current year  "+ Calendar.getInstance().get(Calendar.YEAR));

        if(Integer.parseInt(selectedYear) > Calendar.getInstance().get(Calendar.YEAR) || Integer.parseInt(selectedYear) < Calendar.getInstance().get(Calendar.YEAR)){
            SimpleDateFormat sdfWithYear = new SimpleDateFormat("MMM dd, yyyy");
            dateToDisplay = sdfWithYear.format(date);
        }else {
            SimpleDateFormat sdfWithWithoutYear = new SimpleDateFormat("EEE, MMM dd");
            dateToDisplay = sdfWithWithoutYear.format(date);
        }
        return dateToDisplay;
    }

    @NonNull
    public static SpannableStringBuilder getSpannableStringForErrorOutput(String errorMessage,int color){
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(color);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(errorMessage);
        ssbuilder.setSpan(fgcspan, 0, errorMessage.length(), 0);
        return ssbuilder;
    }

    /**
     * sets a view with a rounded border and specified background and border colours
     * @param context
     * @param view
     * @param backgroundColor
     * @param borderColor
     */
    public static void setRoundedDrawable(Context context, View view, int backgroundColor, int borderColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(90f);
        shape.setColor(backgroundColor);
        if (borderColor != 0){
            shape.setStroke(2, borderColor);
        }
        view.setBackground(shape);
    }

    /**
     * sets a view with a rounded border with dotted line and specified background and border colours
     * @param context
     * @param view
     * @param backgroundColor
     * @param borderColor
     */
    public static void setRoundedDrawableDottedLine(Context context, View view, int backgroundColor, int borderColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(90f);
        Log.d("GOALS", "in dotted line");
        shape.setColor(backgroundColor);
        if (borderColor != 0){
            shape.setStroke(5,borderColor,15,7);
        }
        view.setBackground(shape);
    }

}
