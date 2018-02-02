package com.example.jasmine.goalachieverassistant.Fragments.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by jkhinda on 29/01/18.
 */

    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static DatePickerFragment newInstance(DatePickerFragmentListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setDatePickerListener(listener);
        return fragment;
    }
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {


            Log.d("GOALS", "InDatePicker Utility "+  day+month+year);


            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            Date date = c.getTime();

            notifyDatePickerListener(date);

        }


        private DatePickerFragmentListener datePickerListener;
        public interface DatePickerFragmentListener {
            public void onDateSet(Date date);
        }

        public DatePickerFragmentListener getDatePickerListener() {
            return this.datePickerListener;
        }

        public void setDatePickerListener(DatePickerFragmentListener listener) {
            this.datePickerListener = listener;
        }

        protected void notifyDatePickerListener(Date date) {
            if(this.datePickerListener != null) {
                Log.d("GOALS", "in notifyDatePickerListener ");
                this.datePickerListener.onDateSet(date);
            }else{
                Log.d("GOALS", "in notifyDatePickerListener null");
            }


        }


    }

