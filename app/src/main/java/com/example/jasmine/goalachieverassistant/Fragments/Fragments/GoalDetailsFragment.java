package com.example.jasmine.goalachieverassistant.Fragments.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.jasmine.goalachieverassistant.DateDisplayParser;
import com.example.jasmine.goalachieverassistant.EditGoalActivity;
import com.example.jasmine.goalachieverassistant.GoalListActivity;
import com.example.jasmine.goalachieverassistant.Models.GoalModel;
import com.example.jasmine.goalachieverassistant.R;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalDetailsFragment extends Fragment implements DatePickerFragment.DatePickerFragmentListener{

    private static final String GOAL_UUID = "GoalUUID";
    private  EditText goalDueDate;
    EditText goalName;
    EditText goalReason;
    Spinner spinner;
    private Date dueDate;
    ImageView imageCalendar;
    private Realm realm;


    public GoalDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDateSet(Date view) {

//        Log.d("GOALS", "in onDateSet");
//        // This method will be called with the date from the `DatePicker`.
//
//
//        SimpleDateFormat sdfWithYear = new SimpleDateFormat(getResources().getString(R.string.full_date_format));
//
//        String dateToDisplay = sdfWithYear.format(view);
        if(null != view ){

            String dateToDisplay = DateDisplayParser.parseDateForDisplay(view);
            goalDueDate.setText(dateToDisplay);

        }else{
            goalDueDate.setText(R.string.no_due_date);
        }

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
        return inflater.inflate(R.layout.fragment_goal_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final DatePickerFragment fragment =DatePickerFragment.newInstance(this);

        final String uuId = getArguments().get(GOAL_UUID).toString();
        Log.d("GOALS", "onViewCreated: GOALSDETAILSFRAGMENT.java");


        goalName = (EditText) view.findViewById(R.id.editGoal);
        goalReason = (EditText) view.findViewById(R.id.addReason);

        spinner = (Spinner) view.findViewById(R.id.spinnerType);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        goalDueDate =(EditText) view.findViewById(R.id.add_task_ending);
        goalDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.show(getFragmentManager(), "Task Date");
            }
        });

        imageCalendar = (ImageView) view.findViewById(R.id.add_task_date);
        imageCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.show(getFragmentManager(), "Task Date");
            }
        });

        //if we are accessing the this fragment via the EditGoals Activity, then grab the goal data from realm and put in appropriate views, else skip
        if(EditGoalActivity.class.getName().contains(getActivity().getLocalClassName())){

            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", uuId).findFirst();
                    goalName.setText(goalModel.getName());

                    if(null != goalModel.getDueDate()){

                        String dateToDisplay = DateDisplayParser.parseDateForDisplay(goalModel.getDueDate());
                        goalDueDate.setText(dateToDisplay);
                    }else{
                        goalDueDate.setText(R.string.no_due_date);
                    }

                    goalReason.setText(goalModel.getReason());
                    //               spinner1.setSelection(adapter1.getPosition(goalModel.getPriority()));
                    spinner.setSelection(adapter.getPosition(goalModel.getType()));
                }
            });
            realm.close();
        }


    }


    /**
     * Triggered from addGaolactivity when the "save" check mark is pressed for the goal
     * This method saves all the goal form data to realm
     */
    public void addGoalDetailsToRealm(){

        final String uuId = getArguments().get(GOAL_UUID).toString();

        Log.d("GOALS", "in addGoalDetailsToRealm to realm in fragment ");
        Log.d("GOALS", "data is  "+ uuId+ "  goalName"+ goalName.getText().toString());

            long mDate = System.currentTimeMillis();
            SimpleDateFormat mSdf = new SimpleDateFormat("MMM MM dd,yyy h:mm a");
            final String mDateString = mSdf.format(mDate);

            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", uuId).findFirst();
                goalModel.setName(goalName.getText().toString());
                goalModel.setTime(mDateString);
                goalModel.setReason(goalReason.getText().toString());
                goalModel.setType(spinner.getSelectedItem().toString());
                goalModel.setTimeStamp(System.currentTimeMillis());
                goalModel.setDueDate(dueDate);

                Log.d("GOALS", "adding goal details into realm");


                }
            });
            realm.close();
            Intent addGoalIntent = new Intent(getContext(), GoalListActivity.class);
            startActivity(addGoalIntent);
    }


}


