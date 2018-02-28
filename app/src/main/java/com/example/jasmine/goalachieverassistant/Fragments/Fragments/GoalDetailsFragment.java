package com.example.jasmine.goalachieverassistant.Fragments.Fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerPalette;
import com.android.colorpicker.ColorPickerSwatch;
import com.example.jasmine.goalachieverassistant.Models.SubGoalModel;
import com.example.jasmine.goalachieverassistant.Utilities;
import com.example.jasmine.goalachieverassistant.EditGoalActivity;
import com.example.jasmine.goalachieverassistant.GoalListActivity;
import com.example.jasmine.goalachieverassistant.Models.GoalModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.XoldClassesToDeleteAfterTesting.AddGoal;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalDetailsFragment extends Fragment implements DatePickerFragment.DatePickerFragmentListener, ColorPickerAlertDialog.ColorPickerListener, NotesDialogFragment.AddNotesListener{

    private static final String GOAL_UUID = "GoalUUID";
    private  EditText goalDueDate;
    EditText goalName;
    TextView goalReason;
    ImageButton colourLabelButton;
    ImageView tagIcon;
    private Date dueDate;
    ImageView imageCalendar;
    private Realm realm;
    int colorSelected=0;
    int labelColorFromDB=0;





    public GoalDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onNoteAdded(String note){
        goalReason.setText(note);
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
            goalDueDate.setText(dateToDisplay);


        }
//        else{
//            goalDueDate.setText(R.string.no_due_date);
//
//
//        }

        dueDate = view;
    }


    public static GoalDetailsFragment newInstance(String param3) {
        GoalDetailsFragment frag = new GoalDetailsFragment();
        Bundle args = new Bundle();
        args.putString(GOAL_UUID, param3);
        Log.d("GOALS", "GOAL_UUID "+  args.get(GOAL_UUID).toString()+ "  param3 "+ param3);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("GOALS", "onCreateView: details ");
        return inflater.inflate(R.layout.fragment_goal_details, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final DatePickerFragment fragment =DatePickerFragment.newInstance(this);
        final String uuId = getArguments().get(GOAL_UUID).toString();
        final DialogFragment colorFrag = ColorPickerAlertDialog.newInstance(getResources().getString(R.string.label_color_picker_dialog),uuId,this);
        final NotesDialogFragment fragmentNotes =NotesDialogFragment.newInstance(getResources().getString(R.string.notes_dialog_title),uuId,this);
        Log.d("GOALS", "onViewCreated: GOALSDETAILSFRAGMENT.java");



        goalReason = (TextView) view.findViewById(R.id.addReason);
        Log.d("GOALS", "intiliazed the goalDueDate ");
        goalReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNotes.show(getFragmentManager(), "Goal Notes");
            }
        });

        ImageView editNotesIcon = (ImageView) view.findViewById(R.id.add_goal_notes);
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
            final GoalModel mGoal = realm.where(GoalModel.class).equalTo("id", uuId).findFirst();
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


        goalDueDate =(EditText) view.findViewById(R.id.add_task_ending);
        Log.d("GOALS", "intiliazed the goalDueDate ");
        goalDueDate.setOnClickListener(new View.OnClickListener() {
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

        //if we are accessing the this fragment via the EditGoals Activity, then grab the goal data from realm and put in appropriate views, else skip
        if(EditGoalActivity.class.getName().contains(getActivity().getLocalClassName())){

            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", uuId).findFirst();
                    //                    goalName.setText(goalModel.getName());
                    labelColorFromDB = goalModel.getLabelColor();
                    if(null != goalModel.getDueDate()){

                        String dateToDisplay = Utilities.parseDateForDisplay(goalModel.getDueDate());
                        goalDueDate.setText(dateToDisplay);
                        dueDate = goalModel.getDueDate();
                    }
//                    else{
//                        goalDueDate.setText(R.string.no_due_date);
//                    }

                    goalReason.setText(goalModel.getReason());
                    //               spinner1.setSelection(adapter1.getPosition(goalModel.getPriority()));
                    // spinner.setSelection(adapter.getPosition(goalModel.getType()));
                }
            });
            realm.close();
        }







    }


    /**
     * Triggered from addGaolactivity when the "save" check mark is pressed for the goal
     * This method saves all the goal form data to realm
     */
    public void addGoalDetailsToRealm(final String goalTitle){

        final String uuId = getArguments().get(GOAL_UUID).toString();

        Log.d("GOALS", "in addGoalDetailsToRealm to realm in fragment ");


            long mDate = System.currentTimeMillis();
            SimpleDateFormat mSdf = new SimpleDateFormat("MMM MM dd,yyy h:mm a");
            final String mDateString = mSdf.format(mDate);

            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", uuId).findFirst();
                goalModel.setName(goalTitle);
                goalModel.setTime(mDateString);
                goalModel.setReason(goalReason.getText().toString());
                goalModel.setTimeStamp(System.currentTimeMillis());
                goalModel.setDueDate(dueDate);
                goalModel.setDueDateNotEmpty(dueDate);
                Log.d("GOALS", "adding goal details into realm");
                    Log.d("GOALS", "due date is"+ dueDate);


                }
            });
            realm.close();
        Log.d("GOALS", "addGoalDetailsToRealm2: ");
//            Intent addGoalIntent = new Intent(getContext(), GoalListActivity.class);
//            startActivity(addGoalIntent);
    }




}


