package com.example.jasmine.goalachieverassistant;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

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

}
