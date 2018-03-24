package com.example.jasmine.goalachieverassistant.Fragments.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;

import com.android.colorpicker.ColorPickerPalette;
import com.android.colorpicker.ColorPickerSwatch;
import com.example.jasmine.goalachieverassistant.EditGoalActivity;
import com.example.jasmine.goalachieverassistant.EditSubTaskActivity;
import com.example.jasmine.goalachieverassistant.EditTaskActivity;
import com.example.jasmine.goalachieverassistant.Models.TaskModel;
import com.example.jasmine.goalachieverassistant.R;

import io.realm.Realm;

/**
 * Created by jasmine on 06/02/18.
 */

public class ColorPickerAlertDialog extends DialogFragment {


    int colorSelected=0;
        Realm realm;
        public static ColorPickerAlertDialog newInstance(String title, String goalUUID, ColorPickerListener listener) {
            Log.d("GOALS", "onColorSelected: color RESTARTED ");
            ColorPickerAlertDialog frag = new ColorPickerAlertDialog();
            Bundle args = new Bundle();
            args.putString("Title", title);
            args.putString("GOALUUID", goalUUID);
            frag.setArguments(args);
            frag.setColorPickerListener(listener);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("Title");

            final String uuId = getArguments().getString("GOALUUID");
            final int[] colors = new int[]{

                    getResources().getColor(R.color.colour1),
                    getResources().getColor(R.color.colour2),
                    getResources().getColor(R.color.colour14),
                    getResources().getColor(R.color.colour5),
                    getResources().getColor(R.color.colour6),
                    getResources().getColor(R.color.colour7),
                    getResources().getColor(R.color.colour8),
                    getResources().getColor(R.color.colour9),
                    getResources().getColor(R.color.colour11),
                    getResources().getColor(R.color.colour12),
                    getResources().getColor(R.color.colour13),
                    getResources().getColor(R.color.white)



            };
            //if there user has already selected a color and saved it to DB display the color as the the selected one


            if(EditGoalActivity.class.getName().contains(getActivity().getLocalClassName())){
                try{
                    realm = Realm.getDefaultInstance();
                    TaskModel goalModel = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
                    //TODO: fix this recurring issue that causes intermittent crash 87678
                    colorSelected = goalModel.getLabelColor();
                }finally{
                    realm.close();
                }
                Log.d("GOALS", "color picker 1 ");
            }else if(EditTaskActivity.class.getName().contains(getActivity().getLocalClassName())){
                try{
                    realm = Realm.getDefaultInstance();
                    TaskModel subGoalModel = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
                    //TODO: fix this recurring issue that causes intermittent crash 87678
                    colorSelected = subGoalModel.getLabelColor();
                }finally{
                    realm.close();
                }
                Log.d("GOALS", "color picker 2 ");

            }else if(EditSubTaskActivity.class.getName().contains(getActivity().getLocalClassName())){
                try{
                    realm = Realm.getDefaultInstance();
                    TaskModel childSubGoalModel = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
                    //TODO: fix this recurring issue that causes intermittent crash 87678
                    colorSelected = childSubGoalModel.getLabelColor();
                }finally{
                    realm.close();
                }
                Log.d("GOALS", "color picker 3 ");

            }else{
                Log.e("ERROR", "Matching Class not found ");
            }





            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            final ColorPickerPalette colorPickerPalette =
                    (ColorPickerPalette) layoutInflater.inflate(R.layout.colorpickerpalette, null);

            colorPickerPalette.init(colors.length, 4, new ColorPickerSwatch.OnColorSelectedListener() {
                @Override
                public void onColorSelected(int color) {
                    Log.d("GOALS", "onColorSelected: color selected is "+ color);
                    colorPickerPalette.drawPalette(colors,color);
                    colorSelected = color;

                }

            });
            //if user has selected a color and saved it to db, then select the color in palette, otherwise select the transparent color by default
            if(0 !=colorSelected){
                Log.d("GOALS", "onColorSet:color1 "+ colorSelected);
                colorPickerPalette.drawPalette(colors, colorSelected);
            }else{
                Log.d("GOALS", "onColorSet:color2 " +colorSelected);
                colorPickerPalette.drawPalette(colors, getResources().getColor(R.color.white));
            }

            //put the color palette inside an alert dialog for user to choose from
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                    .setTitle(title)
                    .setView(colorPickerPalette)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {

                            if(EditGoalActivity.class.getName().contains(getActivity().getLocalClassName())){
                                try{
                                    realm = Realm.getDefaultInstance();
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            TaskModel goalModel = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
                                            if(colorSelected!=0){
                                                goalModel.setLabelColor(colorSelected);
                                            }
                                        }
                                    });
                                }finally{
                                    realm.close();
                                    notifyDatePickerListener(colorSelected);
                                }
                                Log.d("GOALS", "color picker 1 ");
                            }else if(EditTaskActivity.class.getName().contains(getActivity().getLocalClassName())){
                                try{
                                    realm = Realm.getDefaultInstance();
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            TaskModel subGoalModel = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
                                            if(colorSelected!=0){
                                                subGoalModel.setLabelColor(colorSelected);
                                            }
                                        }
                                    });
                                }finally{
                                    realm.close();
                                    notifyDatePickerListener(colorSelected);
                                }
                                Log.d("GOALS", "color picker 2 ");

                            }else if(EditSubTaskActivity.class.getName().contains(getActivity().getLocalClassName())){
                                try{
                                    realm = Realm.getDefaultInstance();
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            TaskModel childSubGoalModel = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
                                            if(colorSelected!=0){
                                                childSubGoalModel.setLabelColor(colorSelected);
                                            }
                                        }
                                    });
                                }finally{
                                    realm.close();
                                    notifyDatePickerListener(colorSelected);
                                }
                                Log.d("GOALS", "color picker 3 ");

                            }else{
                                Log.e("ERROR", "Matching Class not found ");
                            }




//
//                            realm = Realm.getDefaultInstance();
//                            realm.executeTransactionAsync(new Realm.Transaction() {
//                                @Override
//                                public void execute(Realm realm) {
//                                    GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", uuId).findFirst();
//                                    if(colorSelected!=0){
//                                        goalModel.setLabelColor(colorSelected);
//                                    }
//                                }
//                            });
//                            realm.close();
//                            notifyDatePickerListener(colorSelected);
                        }
                    })
                    .setNegativeButton("Cancel", null);

            return dialog.create();
        }

        //Create an interface so that the AlertDialog can communicate the users colour choice to the Details view, and it will display it
        //dynamically (refresh the view) when the "okay"/"positive" button is pressed in the Alert Dialog. If cancel is pressed nothing will be updated.
    private ColorPickerListener mListener;
    public interface ColorPickerListener {
         void onColorSet(int color);
    }

    public void setColorPickerListener(ColorPickerListener listener) {
        this.mListener = listener;
    }
    protected void notifyDatePickerListener(int color) {
        if(this.mListener != null) {
            Log.d("GOALS", "in notifyDatePickerListener ");
            this.mListener.onColorSet(color);
        }else{
            Log.d("GOALS", "in notifyDatePickerListener null");
        }


    }

    }


