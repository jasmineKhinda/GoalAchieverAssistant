package com.example.jasmine.goalachieverassistant.Fragments.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

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
            final ImageView clearDueDateButton =(ImageView) getView().findViewById(R.id.remove_date);
            String dateToDisplay = Utilities.parseDateForDisplay(view);
            taskDueDate.setText(dateToDisplay);
            taskDueDate.setAlpha(1f);
            clearDueDateButton.setVisibility(View.VISIBLE);


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
        final EditText taskDueDates =(EditText) view.findViewById(R.id.add_task_ending);
        final DialogFragment colorFrag = ColorPickerAlertDialog.newInstance(getResources().getString(R.string.label_color_picker_dialog),uuId,this);
        final NotesDialogFragment fragmentNotes =NotesDialogFragment.newInstance(getResources().getString(R.string.notes_dialog_title),uuId,this);
        Log.d("GOALS", "onViewCreated: TASKDETAILSFRAGMENT.java");
        final Button projectSelection =(Button) view.findViewById(R.id.project_selection);
        final ImageView clearProjectButton =(ImageView) view.findViewById(R.id.delete_project);
        final ImageView clearDueDateButton =(ImageView) view.findViewById(R.id.remove_date);
        //final TextInputLayout textInputLayout = (TextInputLayout)view.findViewById(R.id.lNameLayout);
        final TextInputLayout dueDateInputLayout = (TextInputLayout)view.findViewById(R.id.lNameLayoutDate);

        clearDueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        SubGoalModel sub = realm.where(SubGoalModel.class).equalTo("id", uuId).findFirst();
                        sub.setDueDate(null);
                        Log.d("GOALS", "in the delete due date "+ sub.getDueDate());

                    }},new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        taskDueDates.setText("");
                        Log.d("GOALS", "onSuccess: ");
                        dueDate=null;
                        clearDueDateButton.setVisibility(View.INVISIBLE);
                        realm.close();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.d("GOALS", "onError: "+ error);
                        realm.close();
                    }
                });
            }
        });





        List<String> spinnerArray=  new ArrayList<String>();

        try{
            //spinnerArray = new ArrayList(realm.where(GoalModel.class).findAllSorted("name"));
            realm = Realm.getDefaultInstance();

            RealmResults<GoalModel> results =realm.where(GoalModel.class).findAllSorted("name");

            for(GoalModel goal: results)
            {
                if(null!= goal.getName()){
                    spinnerArray.add(goal.getName().toString());
                }

            }

        }finally{
            realm.close();
        }

        //set the project name if there is already an project associated with the task
        try {
            realm = Realm.getDefaultInstance();
            SubGoalModel sub = realm.where(SubGoalModel.class).equalTo("id", uuId).findFirst();

            if (null != sub.getGoal() && (0 == sub.getGoal().getLabelColor()||-1 == sub.getGoal().getLabelColor()) ) {

                Log.d("GOALS", "default colour button " );
//
                projectSelection.setText(sub.getGoal().getName().toString());
                Utilities.setRoundedDrawable(getContext(),projectSelection, Color.LTGRAY, Color.LTGRAY);
                projectSelection.setTextColor(taskDueDates.getTextColors().getDefaultColor());
//                projectSelection.setTextAppearance(getContext(),
//                        R.style.AudioFileInfoOverlayText);
                clearProjectButton.setVisibility(View.VISIBLE);

            }else if(null != sub.getGoal() && (0 != sub.getGoal().getLabelColor() && -1 != sub.getGoal().getLabelColor() )){
                projectSelection.setText(sub.getGoal().getName().toString());
                Utilities.setRoundedDrawable(getContext(),projectSelection, sub.getGoal().getLabelColor(), sub.getGoal().getLabelColor());
                projectSelection.setTextColor(getResources().getColor(R.color.colorWhite));
                projectSelection.setTypeface(null, Typeface.BOLD );
                clearProjectButton.setVisibility(View.VISIBLE);
            }else{
                projectSelection.setText(getResources().getString(R.string.add_project_hint));
                Utilities.setRoundedDrawableDottedLine(getContext(), projectSelection, Color.TRANSPARENT, Color.LTGRAY);
                projectSelection.setTextColor(taskDueDates.getTextColors().getDefaultColor());
                projectSelection.setTypeface(null,Typeface.NORMAL);
                clearProjectButton.setVisibility(View.INVISIBLE);
            }
        }finally{
            realm.close();
        }

        //simple_selectable_list_item
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, spinnerArray);
        projectSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Add to Project")
                        .setSingleChoiceItems(adapter,0, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(final DialogInterface dialog, int which) {

                                // TODO: user specific action

                                ListView lw = ((AlertDialog)dialog).getListView();
                                lw.setItemChecked(which, true);
                                final Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());

                                    realm = Realm.getDefaultInstance();
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {


                                            String projectName =checkedItem.toString();

                                            Log.d("GOALS", "project name selected is  " + projectName);

                                            SubGoalModel sub = realm.where(SubGoalModel.class).equalTo("id", uuId).findFirst();
                                             //if user is changing the project context I.e. adding a different project than original, first remove the task from the original project
                                            Log.d("GOALS", "after sub  " + sub.getName());
                                            try{
                                                if (!checkedItem.equals(null)){
                                                    GoalModel goal = sub.getGoal();
                                                    if(null!= goal){
                                                        goal.getSubgoals().remove(sub);
                                                        Log.d("GOALS", " the subgals row is"+goal.getSubgoals() );
                                                    }
                                                }

                                            }finally {

                                                if (!checkedItem.equals(null)) {

                                                    Log.d("GOALS", "in update project name  ");
//                                                GoalModel goalModel = realm.where(GoalModel.class).equalTo("name", sub.getGoal().getId()).findFirst();
//                                                goalModel.getSubgoals().remove(sub);
                                                    //now add task to the new project Object
                                                    GoalModel goalModelNew = realm.where(GoalModel.class).equalTo("name", projectName).findFirst();
                                                    goalModelNew.getSubgoals().add(sub);
                                                    //now add the new project to the Task Object
                                                    sub.setGoal(goalModelNew);
                                                }
                                            }

                                        }},new Realm.Transaction.OnSuccess() {
                                                @Override
                                                public void onSuccess() {
                                                    Log.d("GOALS", "onSuccess: ");
                                                    if(null != checkedItem){

                                                        SubGoalModel sub = realm.where(SubGoalModel.class).equalTo("id", uuId).findFirst();
                                                        Log.d("GOALS", "inside the if " + sub.getGoal().getLabelColor());
                                                        clearProjectButton.setVisibility(View.VISIBLE);
                                                        if (0 == sub.getGoal().getLabelColor()||-1 == sub.getGoal().getLabelColor()) {
                                                            projectSelection.setText(checkedItem.toString());
                                                            Utilities.setRoundedDrawable(getContext(),projectSelection, Color.LTGRAY, Color.LTGRAY);
                                                            projectSelection.setTextColor(taskDueDates.getTextColors().getDefaultColor());
                                                            //projectSelection.setAlpha(1f);
                                                        }else{
                                                            projectSelection.setText(checkedItem.toString());
                                                            Utilities.setRoundedDrawable(getContext(),projectSelection, sub.getGoal().getLabelColor(), sub.getGoal().getLabelColor());
                                                            projectSelection.setTextColor(getResources().getColor(R.color.colorWhite));
                                                            projectSelection.setTypeface(null, Typeface.BOLD );
                                                            //projectSelection.setAlpha(1f);
                                                        }
                                                            Log.d("GOALS", "default colour button " );
//



                                                    }
//
                                                    realm.close();

                                                }
                                            }, new Realm.Transaction.OnError() {
                                                @Override
                                                public void onError(Throwable error) {
                                                    Log.d("GOALS", "onError: "+ error);
                                                    realm.close();
                                                }
                                            });

                                        dialog.dismiss();
                                }
                        })
                        .create().show();
            }
        });

//        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
//        Spinner sItems = (Spinner) view.findViewById(R.id.project_selection);
//        sItems.setAdapter(adapter);

        clearProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            SubGoalModel sub = realm.where(SubGoalModel.class).equalTo("id", uuId).findFirst();
                            try{
                                GoalModel goal = sub.getGoal();
                                if(null!= goal){
                                    goal.getSubgoals().remove(sub);
                                    Log.d("GOALS", " the subgals row is"+goal.getSubgoals() );
                                }
                            }finally{
                                sub.setGoal(null);
                            }

                        }},new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.d("GOALS", "onSuccess: ");
//                        SubGoalModel sub = realm.where(SubGoalModel.class).equalTo("id", uuId).findFirst();
                        projectSelection.setText(getResources().getString(R.string.add_project_hint));
                        clearProjectButton.setVisibility(View.INVISIBLE);
 //                       projectSelection.setAlpha(0.38F);
                        Utilities.setRoundedDrawableDottedLine(getContext(), projectSelection, Color.TRANSPARENT, Color.LTGRAY);
                        projectSelection.setTextColor(taskDueDates.getTextColors().getDefaultColor());
                        projectSelection.setTypeface(null,Typeface.NORMAL);
//                        projectSelection.setTextAppearance(getContext(),
//                                R.style.AudioFileInfoOverlayText);
                        realm.close();
                    }
                    });
            }
        });

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
//        selectColour.setAlpha(0.38F);
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
            if(0 !=mGoal.getLabelColor()) {
                int selectedLabelColor = mGoal.getLabelColor();
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

        dueDateInputLayout.setOnClickListener(new View.OnClickListener() {
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

        try {

            realm = Realm.getDefaultInstance();
            //           realm.executeTransaction(new Realm.Transaction() {
            //               @Override
            //               public void execute(Realm realm) {
            SubGoalModel taskModel = realm.where(SubGoalModel.class).equalTo("id", uuId).findFirst();
            //                    goalName.setText(goalModel.getName());
            Log.d("GOALS", "the label colour is " + taskModel.getLabelColor() + " for task " + taskModel.getName());
            labelColorFromDB = taskModel.getLabelColor();
            if (null != taskModel.getDueDate()) {

                String dateToDisplay = Utilities.parseDateForDisplay(taskModel.getDueDate());


                dueDate = taskModel.getDueDate();
                if (null == dateToDisplay) {
//                            taskDueDate.setText(getResources().getString(R.string.due_date_hint));
//                            taskDueDate.setTextColor(Color.BLACK);
                    // dueDateInputLayout.setHint(getResources().getString(R.string.due_date_hint));
                    //taskDueDate.setText("");
                    //taskDueDates.setText("");
                    //taskDueDate.setAlpha(0.38f);
                    //dueDateInputLayout.setAlpha(0.38f);
                    //dueDateInputLayout.setHintTextAppearance(R.style.TextLabelInput);
                    clearDueDateButton.setVisibility(View.INVISIBLE);
                    // taskDueDate.setAlpha(0.38f);
                    Log.d("GOALS", "in null date ");
                    // dueDate=null;
                } else {
                    taskDueDate.setText(dateToDisplay);
                    clearDueDateButton.setVisibility(View.VISIBLE);
                    Log.d("GOALS", "in  not null date ");
                }
            } else {
                clearDueDateButton.setVisibility(View.INVISIBLE);
            }
            Log.d("GOALS", "onViewCreated: TASKDETAILSFRAGMENT.java END " + taskModel.getDueDate());
//                    else{
//                        taskDueDate.setText(R.string.no_due_date);
//                    }

            taskReason.setText(taskModel.getReason());
            //               spinner1.setSelection(adapter1.getPosition(goalModel.getPriority()));
            //                   // spinner.setSelection(adapter.getPosition(goalModel.getType()));
            //               }
            //           });
        }finally {
            realm.close();
        }

 //       }







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

    /**
     * Triggered from editTaskactivity when the "save" check mark is pressed for the task
     * This method saves all the task form data to realm
     */
    public void addSubTaskDetailsToRealm(final String taskTitle){

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

                    ChildSubGoalModel taskModel = realm.where(ChildSubGoalModel.class).equalTo("id", uuId).findFirst();
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


