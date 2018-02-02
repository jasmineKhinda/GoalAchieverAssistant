package com.example.jasmine.goalachieverassistant;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.jasmine.goalachieverassistant.Fragments.Adapters.CustomPagerFragmentAdapter;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.GoalDetailsFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.GoalTasksFragment;
import com.example.jasmine.goalachieverassistant.Models.GoalModel;
import com.example.jasmine.goalachieverassistant.XoldClassesToDeleteAfterTesting.EditGoal;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jkhinda on 31/01/18.
 */

public class EditGoalActivity extends AppCompatActivity {



        CustomPagerFragmentAdapter adapter;
        private String GoalName;
        private String GoalReason;
        private String Spinner;
        private String DueDate;
        private GoalDetailsFragment detailsFrag;
        private GoalTasksFragment tasksFrag;
        private String task_key;
        private String goal_name;
        EditText goalTitle;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.add_edit_goal_view_fragments);
            task_key = getIntent().getExtras().getString("EDIT_GOALUUID");
            goal_name =getIntent().getExtras().getString("EDIT_GOALNAME");
            goalTitle =(EditText) findViewById(R.id.ltitle);

            GoalModel goalModel;



            //final String goalUUID =getIntent().getExtras().getString("GOAL_UUID");


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.menu_edit_goal);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


            // Find the view pager that will allow the user to swipe between fragments
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

            // Create an adapter that knows which fragment should be shown on each page
            adapter = new CustomPagerFragmentAdapter(this, getSupportFragmentManager(),task_key);


            // Set the adapter onto the view pager
            viewPager.setAdapter(adapter);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tbl_pages);
            tabLayout.setupWithViewPager(viewPager);

            adapter.startUpdate(viewPager);
            detailsFrag = (GoalDetailsFragment)adapter.instantiateItem(viewPager, 0);
            tasksFrag = (GoalTasksFragment) adapter.instantiateItem(viewPager, 1);
            adapter.finishUpdate(viewPager);

            //set the goal title in the toolbar

//            GoalModel results = realm.where(GoalModel.class).equalTo("id", task_key).findFirst();
            goalTitle.setText(goal_name);


//                realm = Realm.getDefaultInstance();
//                realm.executeTransactionAsync(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//
//                        GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", task_key).findFirst();
//                        Log.d("GOALS", "goal name i          s" +goalModel.getName().toString());
//                        String goalName =goalModel.getName().toString();
//
//                    }}, new Realm.Transaction.OnSuccess() {
//                        @Override
//                        public void onSuccess() {
//
//                            goalTitle.setText(goalName);
//                        }
//                });

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_edit_goal, menu);
            return true;
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {


            final String goalUUID =getIntent().getExtras().getString("GOAL_UUID");
            final String mGoalName = GoalName;
            final String mReasonGoalText = GoalReason;
            final String selectedType = Spinner;
            final String selectedDueDate = DueDate;
      //      final EditText goalTitle =(EditText) findViewById(R.id.ltitle);
            String goalName="";

            if (item.getItemId() == android.R.id.home) {
                //validate the goal Name is entered and not blank
                if(TextUtils.isEmpty(goalTitle.getText())){
                    goalTitle.requestFocus();
                    goalTitle.setError(Utilities.getSpannableStringForErrorOutput("Enter Goal Name", Color.WHITE));

                }else{
                    goalName = goalTitle.getText().toString();
                    detailsFrag.addGoalDetailsToRealm(goalName);
                }

            } else if (item.getItemId() == R.id.action_settings_done) {
                //validate the goal Name is entered and not blank
                if(TextUtils.isEmpty(goalTitle.getText())){
                    goalTitle.requestFocus();
                    goalTitle.setError(Utilities.getSpannableStringForErrorOutput("Enter Goal Name", Color.WHITE));

                }else{
                    goalName = goalTitle.getText().toString();
                    detailsFrag.addGoalDetailsToRealm(goalName);
                }

            }else if (item.getItemId() == R.id.delete) {


                Realm realm = Realm.getDefaultInstance();
                try{
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", task_key).findFirst();
                            goalModel.deleteFromRealm();
                        }
                    });

                }finally{
                    realm.close();
                }
                Intent addGoalIntent = new Intent(EditGoalActivity.this, GoalListActivity.class);
                startActivity(addGoalIntent);
            }
            return super.onOptionsItemSelected(item);

        }


    @Override
    protected void onDestroy() {
        Log.d("GOALS", "onDestroy: ");

        super.onDestroy();
    }

    }

