package com.example.jasmine.goalachieverassistant;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Context;



import com.example.jasmine.goalachieverassistant.recyclerview.adapter.SubGoalAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmExpandableRecyclerAdapter;
import io.realm.RealmResults;

/**
 * Created by jasmine on 21/12/17.
 */

public class EditGoal extends AppCompatActivity {


    EditText mEditGoal;
    EditText mReasonGoal;
    private String task_key;
    String selectedSpinnerPriority;
    String selectedSpinnerType;
    private Realm realm;
    private SubGoalAdapter adapter;

    private static String selectedDate;
    private static EditText taskDueDate;
    String selectedDueDate;
    RealmResults<SubGoalModel> subgoalsForThisGoal;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        task_key = getIntent().getExtras().getString("TASK_OBJECT");
        Log.d("GOALS", "task key: " + task_key);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setTitle(R.string.goal_edit_title_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mEditGoal = (EditText) findViewById(R.id.editGoal);
        mReasonGoal = (EditText) findViewById(R.id.addReason);

        final Spinner spinner2 = (Spinner) findViewById(R.id.spinnerType);
        final ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        taskDueDate = (EditText) findViewById(R.id.add_task_ending);
        taskDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerFragment().show(getSupportFragmentManager(), "Task Date");
            }
        });

        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", task_key).findFirst();
                mEditGoal.setText(goalModel.getName());
                taskDueDate.setText(goalModel.getDueDate());
                mReasonGoal.setText(goalModel.getReason());
                //               spinner1.setSelection(adapter1.getPosition(goalModel.getPriority()));
                spinner2.setSelection(adapter2.getPosition(goalModel.getType()));
            }
        });


        ImageView addTaskDate = (ImageView) findViewById(R.id.add_task_date);
        addTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerFragment().show(getSupportFragmentManager(), "Task Date");
            }
        });


// added sample code here

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText taskEditText = new EditText(EditGoal.this);
                AlertDialog dialog = new AlertDialog.Builder(EditGoal.this)
                        .setTitle("Add Task")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //realm = Realm.getDefaultInstance();
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                                                  @Override
                                                                  public void execute(Realm realm) {
                                                                      final String uuID = UUID.randomUUID().toString();
                                                                      realm.createObject(SubGoalModel.class, uuID)
                                                                              .setName(String.valueOf(taskEditText.getText()));
                                                                      SubGoalModel sub = realm.where(SubGoalModel.class).equalTo("id", uuID).findFirst();
                                                                      GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", task_key).findFirst();
                                                                      goalModel.getSubgoals().add(sub);
                                                                      sub.setGoal(goalModel);
                                                                      Log.d("GOALS", "added subgoal ");

                                                                  }
                                                              },
                                        new Realm.Transaction.OnSuccess() {
                                            @Override
                                            public void onSuccess() {
                                                Log.d("GOALS", "onSuccess: ");


                                            }
                                        }, new Realm.Transaction.OnError() {
                                            @Override
                                            public void onError(Throwable error) {
                                                Log.d("GOALS", "onError: ");
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });


        //recycler view of all the sub goals and child sub goals
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        subgoalsForThisGoal = realm.where(SubGoalModel.class).equalTo("goal.id", task_key).findAll();

        subgoalsForThisGoal.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<SubGoalModel>>() {
            @Override
            public void onChange(RealmResults<SubGoalModel> persons, OrderedCollectionChangeSet changeset) {

                adapter = new SubGoalAdapter(persons, "id");
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(EditGoal.this));
                Log.d("GOALS", "onChange!!!!!!!!!!: ");


            }
        });

        adapter = new SubGoalAdapter(subgoalsForThisGoal, "id");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("GOALS", "onCreate real data  : "+  subgoalsForThisGoal);



        //expanding and collapsing each subgoal or child sub goal
        adapter.setExpandCollapseListener(new RealmExpandableRecyclerAdapter.ExpandCollapseListener() {
            @UiThread
            @Override
            public void onParentExpanded(int parentPosition) {
                SubGoalModel expandedRecipe = adapter.getItem(parentPosition);

                String toastMsg = getResources().getString(R.string.expanded, expandedRecipe.getName());
//                Toast.makeText(AddGoal.this,
//                        toastMsg,
//                        Toast.LENGTH_SHORT)
//                        .show();
            }

            @UiThread
            @Override
            public void onParentCollapsed(int parentPosition) {
                SubGoalModel collapsedRecipe = adapter.getItem(parentPosition);

                String toastMsg = getResources().getString(R.string.collapsed, collapsedRecipe.getName());
//                Toast.makeText(AddGoal.this,
//                        toastMsg,
//                        Toast.LENGTH_SHORT)
//                        .show();
            }
        });







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_goal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final String mGoals= getResources().getString(R.string.db_parent_Goals);
        final String mName= getResources().getString(R.string.db_Goal_Name);
        final String mReason= getResources().getString(R.string.db_Goal_Reason);
        final String mTime= getResources().getString(R.string.db_Goal_Time);
        final String mPriority= getResources().getString(R.string.db_Goal_Priority);
        final String mType= getResources().getString(R.string.db_Goal_Type);
        task_key = getIntent().getExtras().getString("TASK_OBJECT");
        Log.d("GOALS", "task key: "+task_key );

        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }else if (item.getItemId() == R.id.action_settings_done){

            mEditGoal = (EditText) findViewById(R.id.editGoal);
            final String mGoalName = mEditGoal.getText().toString();

            mReasonGoal = (EditText) findViewById(R.id.addReason);
            final String mReasonGoalText = mReasonGoal.getText().toString();

            Spinner spinner2 = (Spinner) findViewById(R.id.spinnerType);
            final String selectedType = spinner2.getSelectedItem().toString();

            selectedDueDate = taskDueDate.getText().toString();

            long mDate = System.currentTimeMillis();
            SimpleDateFormat mSdf = new SimpleDateFormat("MMM MM dd,yyy h:mm a");
            final String mDateString = mSdf.format(mDate);



            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", task_key).findFirst();
                    goalModel.setName(mGoalName);
                    goalModel.setTime(mDateString);
                    goalModel.setReason(mReasonGoalText);
                    goalModel.setDueDate(selectedDueDate);
                    goalModel.setType(selectedType);

                }
            });


            Intent addGoalIntent = new Intent(EditGoal.this, GoalListActivity.class);
            startActivity(addGoalIntent);
        }else if (item.getItemId() == R.id.delete){

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", task_key).findFirst();
                    goalModel.deleteFromRealm();
                }
            });


            Intent addGoalIntent = new Intent(EditGoal.this, GoalListActivity.class);
            startActivity(addGoalIntent);

        }
        return super.onOptionsItemSelected(item);


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
            selectedDate = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
            taskDueDate.setText(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
        }
    }



    @Override
    protected void onDestroy() {
        Log.d("GOALS", "onDestroy: ");
        realm.close();
        super.onDestroy();
    }




    @Override
    protected void onStop() {

        Log.d("GOALS", "onStop: ");
        subgoalsForThisGoal.removeAllChangeListeners();
        super.onStop();

    }




}


