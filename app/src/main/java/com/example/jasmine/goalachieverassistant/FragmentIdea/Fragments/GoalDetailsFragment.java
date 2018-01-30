package com.example.jasmine.goalachieverassistant.FragmentIdea.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.jasmine.goalachieverassistant.FragmentIdea.Activities.AddGoalActivity;
import com.example.jasmine.goalachieverassistant.GoalListActivity;
import com.example.jasmine.goalachieverassistant.GoalModel;
import com.example.jasmine.goalachieverassistant.R;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalDetailsFragment extends Fragment {

    private static final String GOAL_UUID = "GoalUUID";
    private  EditText taskDueDate;
    EditText goalName;
    EditText goalReason;
    Spinner spinner;
    ImageView addTaskDate;
    private Realm realm;


    public GoalDetailsFragment() {
        // Required empty public constructor
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


        goalName = (EditText) view.findViewById(R.id.editGoal);
        goalReason = (EditText) view.findViewById(R.id.addReason);

        spinner = (Spinner) view.findViewById(R.id.spinnerType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        taskDueDate =(EditText) view.findViewById(R.id.add_task_ending);
        taskDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickersFragment().show(getFragmentManager(), "Task Date");
            }
        });

        addTaskDate = (ImageView) view.findViewById(R.id.add_task_date);
        addTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickersFragment().show(getFragmentManager(), "Task Date");
            }
        });
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

                    Log.d("GOALS", "goal due date is"+ taskDueDate.getText().toString());

                    goalModel.setDueDate(taskDueDate.getText().toString());

                }
            });

            Intent addGoalIntent = new Intent(getContext(), GoalListActivity.class);
            startActivity(addGoalIntent);
    }


    public static class DatePickersFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        int newYear, newMonth, newDay;
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


            Log.d("GOALS", "Month "+  month);
            Log.d("GOALS", "Day "+  day);

            ((EditText) getActivity().findViewById(R.id.add_task_ending)).setText(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day));

        }

    }


}


