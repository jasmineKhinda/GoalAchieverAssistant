package com.example.jasmine.goalachieverassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.jasmine.goalachieverassistant.Fragments.Adapters.CustomSpinnerAdapter;
import com.example.jasmine.goalachieverassistant.Models.GoalModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class GoalListActivity extends AppCompatActivity {
    private Realm realm;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_list);



        recyclerView = (RecyclerView)findViewById(R.id.goal_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GoalListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        realm = Realm.getDefaultInstance();
//        RealmResults<GoalModel> tasks = realm.where(GoalModel.class).findAll();
//        tasks = tasks.sort("name", Sort.ASCENDING);

        RealmResults<GoalModel> tasks =realm.where(GoalModel.class)
                .findAllSortedAsync(
                        new String[] {"dueDateNotEmpty", "dueDate"},
                        new Sort[] { Sort.DESCENDING, Sort.ASCENDING });

        final GoalRecyclerAdapter adapter = new GoalRecyclerAdapter(this, tasks, true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        String[] filterGoalsList;
        filterGoalsList= getResources().getStringArray(R.array.goalfilter);
        List<String> mGoalFilterArrayList = Arrays.asList(filterGoalsList);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter (getSupportActionBar().getThemedContext(),android.R.layout.simple_spinner_item, mGoalFilterArrayList);
        customSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner mNavigationSpinner = (Spinner) findViewById(R.id.spinner_main);
        mNavigationSpinner.setAdapter(customSpinnerAdapter);

        mNavigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemSelected = parent.getItemAtPosition(position).toString();
                Log.d("GOALS", "onItemSelected: in toolbar for Goals. Item selected is " +itemSelected);
                String[] filterGoalsList;
                filterGoalsList= getResources().getStringArray(R.array.goalfilter);

                if(itemSelected.equals(filterGoalsList[0])){
                //default "All goals" sorts the goals with due dates ascending and puts the empty due dates at bottom
                    RealmResults<GoalModel> tasks =realm.where(GoalModel.class)
                            .findAllSortedAsync(
                                    new String[] {"dueDateNotEmpty", "dueDate"},
                                    new Sort[] { Sort.DESCENDING, Sort.ASCENDING });
                    adapter.updateData(tasks);

                }else if(itemSelected.equals(filterGoalsList[1])){
                    //By Name Ascending
                    adapter.sortGoalListWithSortOrder("name",Sort.ASCENDING);

                }else if(itemSelected.equals(filterGoalsList[2])){
                    //No Due Date
                    adapter.sortGoalListContainsTextAndReturnsSortedList("dueDate", Sort.ASCENDING, null, true,"name");


                }else{
                    Log.e("ERROR:", "onItemSelected: valid choice not selected" );
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

  //      toolbar.addView(mNavigationSpinner);





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String uuId = UUID.randomUUID().toString();

                realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createObject(GoalModel.class, uuId);
                        Log.d("GOALS", " created the goal with UUID: " + uuId);
                    }
                });
                Intent intent = new Intent(GoalListActivity.this, AddGoalActivity.class);
                //pass in the unique UUI id key for the new goal to be added
                intent.putExtra("GOAL_UUID",uuId);

                startActivity(intent);
            }
        });





        //        RealmResults<GoalModel> tasksNull = realm.where(GoalModel.class).equalTo("dueDate", "").findAll();
//
//        RealmResults<GoalModel> tasksNotNull = realm.where(GoalModel.class).notEqualTo("dueDate", "").findAll();
//        tasksNotNull = tasksNotNull.sort("dueDate", Sort.ASCENDING);

//
//        List<RealmResults<GoalModel>> results = new ArrayList<>();
//        results.add(tasksNotNull);
//        results.add(tasksNull);






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_goal_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
