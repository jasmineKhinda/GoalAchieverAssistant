package com.example.jasmine.goalachieverassistant;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import android.app.AlertDialog;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddGoal extends AppCompatActivity {

    EditText mEditGoal;
    EditText mReasonGoal;
    private Realm realm;
    private static String selectedDate;
    private static EditText taskDueDate;
    String selectedDueDate;
    //public final String subGoalUUID = UUID.randomUUID().toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        final String goalUUID =getIntent().getExtras().getString("GOAL_UUID");

 //       final CollapsingToolbarLayout mCollapsingToolbarLayout =(CollapsingToolbarLayout) findViewById(R.id.collapsing_tool_bar_layout);
 //       mCollapsingToolbarLayout.setTitle("Title");
//        AppBarLayout appBarLayout =(AppBarLayout) findViewById(R.id.app_bar_layout);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setTitle(R.string.goal_title_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().hide();


//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = false;
//            int scrollRange = -1;
//
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                if (scrollRange + verticalOffset == 0) {
//                    mCollapsingToolbarLayout.setTitle(" "); // Careful! There should be a space between double quote. Otherwise it won't work.
//                    isShow = false;
//                } else if (!isShow) {
//                    mCollapsingToolbarLayout.setTitle("Title");
//                    isShow = true;
//                }
//            }
//        });
        realm = Realm.getDefaultInstance();
        RealmResults<SubGoalModel> tasks = realm.where(SubGoalModel.class).equalTo("goal.id", goalUUID).findAll();
        final SubGoalListAdapter adapter = new SubGoalListAdapter(this, tasks);

//
//        ExpandableListView mListView = (ExpandableListView) findViewById(R.id.subgoal_list);
//     //   ExpandableListAdapter adapterExpandable = new ExpandableListAdapter(this, tasks.);
//
//        mListView.setAdapter(adapterExpandable);
//        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v,
//                                        int groupPosition, long id) {
//                setListViewHeight(parent, groupPosition);
//                return false;
//            }
//        });


//        ListView listView = (ListView) findViewById(R.id.subgoal_list);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                final SubGoalModel task = (SubGoalModel) adapterView.getAdapter().getItem(i);
//                final EditText taskEditText = new EditText(AddGoal.this);
//                taskEditText.setText(task.getName());
//                AlertDialog dialog = new AlertDialog.Builder(AddGoal.this)
//                        .setTitle("Edit Task")
//                        .setView(taskEditText)
//                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // TODO: 5/4/17 Save Edited Task
//                            }
//                        })
//                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // TODO: 5/4/17 Delete Task
//                            }
//                        })
//                        .create();
//                dialog.show();
//            }
//        });






        Spinner spinner2 = (Spinner) findViewById(R.id.spinnerType);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        taskDueDate =(EditText) findViewById(R.id.add_task_ending);
        taskDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerFragment().show(getSupportFragmentManager(), "Task Date");
            }
        });

        ImageView addTaskDate = (ImageView) findViewById(R.id.add_task_date);
        addTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerFragment().show(getSupportFragmentManager(), "Task Date");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText taskEditText = new EditText(AddGoal.this);
                AlertDialog dialog = new AlertDialog.Builder(AddGoal.this)
                        .setTitle("Add Task")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                realm = Realm.getDefaultInstance();
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        final String uuID= UUID.randomUUID().toString();
                                        realm.createObject(SubGoalModel.class,uuID)
                                                .setName(String.valueOf(taskEditText.getText()));
                                        SubGoalModel sub =realm.where(SubGoalModel.class).equalTo("id", uuID).findFirst();
                                        GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", goalUUID).findFirst();
                                        goalModel.getSubgoals().add(sub);
                                        sub.setGoal(goalModel);

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_goal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final String mGoals = getResources().getString(R.string.db_parent_Goals);
        final String mName = getResources().getString(R.string.db_Goal_Name);
        final String mReason = getResources().getString(R.string.db_Goal_Reason);
        final String mTime = getResources().getString(R.string.db_Goal_Time);
        final String mPriority = getResources().getString(R.string.db_Goal_Priority);
        final String mType = getResources().getString(R.string.db_Goal_Type);
        final String goalUUID =getIntent().getExtras().getString("GOAL_UUID");


        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        } else if (item.getItemId() == R.id.action_settings_done) {

            mEditGoal = (EditText) findViewById(R.id.editGoal);
            final String mGoalName = mEditGoal.getText().toString();

            mReasonGoal = (EditText) findViewById(R.id.addReason);
            final String mReasonGoalText = mReasonGoal.getText().toString();

            Spinner spinner2 = (Spinner) findViewById(R.id.spinnerType);
            final String selectedType = spinner2.getSelectedItem().toString();

//            Spinner spinner1 = (Spinner) findViewById(R.id.spinnerPriority);
//            final String selectedPriority = spinner1.getSelectedItem().toString();

            selectedDueDate = taskDueDate.getText().toString();


            long mDate = System.currentTimeMillis();
            SimpleDateFormat mSdf = new SimpleDateFormat("MMM MM dd,yyy h:mm a");
            final String mDateString = mSdf.format(mDate);
 //           final String uuId = UUID.randomUUID().toString();
//            final RealmResults<SubGoalModel> realmResultsSubGoal = realm.where(SubGoalModel.class).findAll();

            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {


 //                   realm.createObject(GoalModel.class, uuId)
 //                           .setName(mGoalName);
                    GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", goalUUID).findFirst();
                    goalModel.setTime(mDateString);
                    Log.d("GOALS", "reason :  " + mReasonGoalText);
                    goalModel.setReason(mReasonGoalText);
//                   goalModel.setPriority(selectedPriority);
                    goalModel.setType(selectedType);
                    goalModel.setDueDate(selectedDueDate);
                    goalModel.setTimeStamp(System.currentTimeMillis());


                    Log.d("GOALS", " goalModel added id: " + goalUUID + " goalModel name is " + goalModel.getName());

                }
            });

            Intent addGoalIntent = new Intent(AddGoal.this, GoalListActivity.class);
            startActivity(addGoalIntent);
        }
        return super.onOptionsItemSelected(item);

    }
    public void changeTaskDone(final String taskId) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SubGoalModel task = realm.where(SubGoalModel.class).equalTo("id", taskId).findFirst();
                task.setDone(!task.isDone());
            }
        });
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {

            Log.d("GOALS", "Month "+  month);
            Log.d("GOALS", "Day "+  day);
            selectedDate = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
            taskDueDate.setText(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day));

            //TextView textview = (TextView) getActivity().findViewById(R.id.textView1);

        }
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
}
