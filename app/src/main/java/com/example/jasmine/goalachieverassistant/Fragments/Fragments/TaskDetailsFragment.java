package com.example.jasmine.goalachieverassistant.Fragments.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jasmine.goalachieverassistant.EditGoalActivity;
import com.example.jasmine.goalachieverassistant.EditTaskActivity;
import com.example.jasmine.goalachieverassistant.GoalListActivity;

import com.example.jasmine.goalachieverassistant.Models.ChildSubGoalModel;
import com.example.jasmine.goalachieverassistant.Models.GoalModel;
import com.example.jasmine.goalachieverassistant.Models.SubGoalModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by jasmine on 15/02/18.
 */

public class TaskDetailsFragment extends Fragment implements DatePickerFragment.DatePickerFragmentListener, ColorPickerAlertDialog.ColorPickerListener, NotesDialogFragment.AddNotesListener{

    private static final String TASK_UUID = "GoalUUID";
    private EditText taskDueDate;

    TextView taskReason;
    ImageButton colourLabelButton;
    ImageView tagIcon;
    private Date dueDate;
    ImageView imageCalendar;
    private Realm realm;
    int colorSelected=0;
    int labelColorFromDB=0;



    public TaskDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onNoteAdded(String note){
        taskReason.setText(note);
    }

    @Override
    public void onColorSet(int color){

        colourLabelButton.setColorFilter(color);


    }

    @Override
    public void onDateSet(Date view) {
        Log.d("GOALS", "onDateset "+ view);
        if(null != view ){

            String dateToDisplay = Utilities.parseDateForDisplay(view);
            taskDueDate.setText(dateToDisplay);


        }
//        else{
//            taskDueDate.setText(R.string.no_due_date);
//
//
//        }

        dueDate = view;
    }


    public static TaskDetailsFragment newInstance(String param3) {
        TaskDetailsFragment frag = new TaskDetailsFragment();
        Bundle args = new Bundle();
        args.putString(TASK_UUID, param3);
        Log.d("GOALS", "GoalDetailsFragment "+  args.get(TASK_UUID).toString()+ "  param3 "+ param3);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("GOALS", "onCreateView: details ");
        return inflater.inflate(R.layout.fragment_task_details, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final DatePickerFragment fragment =DatePickerFragment.newInstance(this);
        final String uuId = getArguments().get(TASK_UUID).toString();
        final DialogFragment colorFrag = ColorPickerAlertDialog.newInstance(getResources().getString(R.string.label_color_picker_dialog),uuId,this);
        final NotesDialogFragment fragmentNotes =NotesDialogFragment.newInstance(getResources().getString(R.string.notes_dialog_title),uuId,this);
        Log.d("GOALS", "onViewCreated: TASKDETAILSFRAGMENT.java");



        taskReason = (TextView) view.findViewById(R.id.addReason);
        Log.d("GOALS", "intialized the taskDueDate ");
        taskReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNotes.show(getFragmentManager(), "Goal Notes");
            }
        });

        ImageView editNotesIcon = (ImageView) view.findViewById(R.id.add_task_notes);
        editNotesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNotes.show(getFragmentManager(), "Goal Notes");
            }
        });


        colourLabelButton = view.findViewById(R.id.color_label_button);

        tagIcon =view.findViewById(R.id.add_project_label_color);
        tagIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //open the colour picker dialog fragment for user to pick colours

                colorFrag.show(getFragmentManager(), " Goal Color");

            }
        });

        Button selectColour =view.findViewById(R.id.pick_colour);
        selectColour.setAlpha(0.38F);
        selectColour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //open the colour picker dialog fragment for user to pick colours
                colorFrag.show(getFragmentManager(), "Goal Color");

            }
        });

        colourLabelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open the colour picker dialog fragment for user to pick colours
                colorFrag.show(getFragmentManager(), "Goal Color");

            }
        });

        //if user has already set the label colour in db, then display it in the colourLabelButton view
        try {
            realm = Realm.getDefaultInstance();
            final SubGoalModel mGoal = realm.where(SubGoalModel.class).equalTo("id", uuId).findFirst();
            int selectedLabelColor = mGoal.getLabelColor();

            if(0!= selectedLabelColor ){
                colourLabelButton.setColorFilter(selectedLabelColor);
                Log.d("GOALS", "selected color from db ");
            }else{
                colourLabelButton.setColorFilter(Color.TRANSPARENT);
                Log.d("GOALS", "default ");
            }

        }finally{
            realm.close();
        }


        taskDueDate =(EditText) view.findViewById(R.id.add_task_ending);
        Log.d("GOALS", "intiliazed the taskDueDate ");
        taskDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.show(getFragmentManager(), "Goal Date");
            }
        });

        imageCalendar = (ImageView) view.findViewById(R.id.add_task_date);
        imageCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.show(getFragmentManager(), "Goal Date");
            }
        });

        //if we are accessing the this fragment via the EditTask Activity, then grab the task data from realm and put in appropriate views, else skip
//        if(EditGoalActivity.class.getName().contains(getActivity().getLocalClassName())){

            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    SubGoalModel taskModel = realm.where(SubGoalModel.class).equalTo("id", uuId).findFirst();
                    //                    goalName.setText(goalModel.getName());
                    Log.d("GOALS", "the label colour is "+taskModel.getLabelColor() +" for task " +taskModel.getName());
                    labelColorFromDB = taskModel.getLabelColor();
                    if(null != taskModel.getDueDate()){

                        String dateToDisplay = Utilities.parseDateForDisplay(taskModel.getDueDate());
                        taskDueDate.setText(dateToDisplay);
                        dueDate = taskModel.getDueDate();
                    }
//                    else{
//                        taskDueDate.setText(R.string.no_due_date);
//                    }

                    taskReason.setText(taskModel.getReason());
                    //               spinner1.setSelection(adapter1.getPosition(goalModel.getPriority()));
                    // spinner.setSelection(adapter.getPosition(goalModel.getType()));
                }
            });
            realm.close();
 //       }


        Log.d("GOALS", "onViewCreated: TASKDETAILSFRAGMENT.java END");




    }


    /**
     * Triggered from editTaskactivity when the "save" check mark is pressed for the task
     * This method saves all the task form data to realm
     */
    public void addTaskDetailsToRealm(final String taskTitle){

        final String uuId = getArguments().get(TASK_UUID).toString();

        Log.d("GOALS", "in addTaskDetailsToRealm to realm in fragment ");


        long mDate = System.currentTimeMillis();
        SimpleDateFormat mSdf = new SimpleDateFormat("MMM MM dd,yyy h:mm a");
        final String mDateString = mSdf.format(mDate);

        try{
            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    SubGoalModel taskModel = realm.where(SubGoalModel.class).equalTo("id", uuId).findFirst();
                    taskModel.setName(taskTitle);
                    taskModel.setTime(mDateString);
                    taskModel.setReason(taskReason.getText().toString());
                    taskModel.setTimeStamp(System.currentTimeMillis());
                    taskModel.setDueDate(dueDate);
                    taskModel.setDueDateNotEmpty(dueDate);
                    Log.d("GOALS", "adding task details into realm");
                    Log.d("GOALS", "due date is"+ dueDate);


                }}, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {

                            //getActivity().finish();
                        }
                });



        }finally{
            realm.close();
        }



    }




}


