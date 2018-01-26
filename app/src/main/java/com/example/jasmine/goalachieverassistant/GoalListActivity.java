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

import com.example.jasmine.goalachieverassistant.FragmentIdea.Activities.AddGoalActivity;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class GoalListActivity extends AppCompatActivity {
    private Realm realm;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("All Goals");



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


        recyclerView = (RecyclerView)findViewById(R.id.goal_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GoalListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);


        realm = Realm.getDefaultInstance();
        RealmResults<GoalModel> tasks = realm.where(GoalModel.class).findAll();
        tasks = tasks.sort("timeStamp");
        final GoalRecyclerAdapter adapter = new GoalRecyclerAdapter(this, tasks, true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
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
