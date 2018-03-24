package com.example.jasmine.goalachieverassistant;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jasmine.goalachieverassistant.Fragments.Adapters.CustomPagerFragmentAdapter;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.CustomBottomSheetDialogFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.GoalDetailsFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.GoalTasksFragment;
import com.example.jasmine.goalachieverassistant.XoldClassesToDeleteAfterTesting.GoalModel;
import com.example.jasmine.goalachieverassistant.Models.TaskModel;

import io.realm.Realm;

/**
 * Created by jkhinda on 31/01/18.
 */

public class EditGoalActivity extends AppCompatActivity implements GoalDetailsFragment.ChangeTabs{



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
        private ViewPager viewPager;
        Realm realm;




        @Override
        public void onTabChange(int tabNumber){
            viewPager.setCurrentItem(tabNumber);
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.add_edit_goal_view_fragments);
            task_key = getIntent().getExtras().getString("EDIT_GOALUUID");
            goal_name =getIntent().getExtras().getString("EDIT_GOALNAME");
            goalTitle =(EditText) findViewById(R.id.ltitle);
            goalTitle.setImeOptions(EditorInfo.IME_ACTION_DONE);
            goalTitle.setRawInputType(InputType.TYPE_CLASS_TEXT);
            goalTitle.setMaxLines(5);
            goalTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    //Log.d("GOALS", "adding goal name into realm" + event + "   "+ actionId+ "  "+ EditorInfo.IME_ACTION_DONE);
                    if(actionId==EditorInfo.IME_ACTION_DONE){

                        try {
                            realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {

                                    TaskModel goalModel = realm.where(TaskModel.class).equalTo("id", task_key).findFirst();
                                    goalModel.setName(goalTitle.getText().toString());

                                    Log.d("GOALS", "adding goal name into realm");



                                }
                            });
                        }finally{
                            realm.close();
                        }
                        goalTitle.clearFocus();
                    }
                    return false;
                }
            });

            GoalModel goalModel;



            //final String goalUUID =getIntent().getExtras().getString("GOAL_UUID");


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.menu_edit_goal);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


            // Find the view pager that will allow the user to swipe between fragments
            viewPager = (ViewPager) findViewById(R.id.viewPager);

            // Create an adapter that knows which fragment should be shown on each page
            adapter = new CustomPagerFragmentAdapter(this, getSupportFragmentManager(),task_key);
            Log.d("GOALS", " goals context is "+ this);
            Log.d("GOALS", " goal adapter is "+ adapter.getItem(0));

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

            final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_task);


            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    if(tab.getPosition()==0){
                        fab.setVisibility(View.GONE);
                    }else if (tab.getPosition()==1){
                        fab.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CustomBottomSheetDialogFragment bottomSheetDialogFragment = CustomBottomSheetDialogFragment.newInstance(task_key,false,"",false,null);
                    bottomSheetDialogFragment.show(getSupportFragmentManager(),"BottomSheet");


                }
            });

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(0==position){
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


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
                    finish();
//                    Intent addGoalIntent = new Intent(EditGoalActivity.this, MainActivity.class);
//                    startActivity(addGoalIntent);

                }

            } else if (item.getItemId() == R.id.action_settings_done) {
                //validate the goal Name is entered and not blank
                if(TextUtils.isEmpty(goalTitle.getText())){
                    goalTitle.requestFocus();
                    goalTitle.setError(Utilities.getSpannableStringForErrorOutput("Enter Goal Name", Color.WHITE));

                }else{
                    goalName = goalTitle.getText().toString();
                    detailsFrag.addGoalDetailsToRealm(goalName);
                    finish();
//                    Intent addGoalIntent = new Intent(EditGoalActivity.this, MainActivity.class);
//                    startActivity(addGoalIntent);
                }

            }else if (item.getItemId() == R.id.delete) {


                Realm realm = Realm.getDefaultInstance();
                try{
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            TaskModel goalModel = realm.where(TaskModel.class).equalTo("id", task_key).findFirst();
                            goalModel.deleteFromRealm();
                            //TODO delete tasks and subtasks too
                        }
                    });

                }finally{
                    realm.close();
                }
                Intent addGoalIntent = new Intent(EditGoalActivity.this, MainActivity.class);
                startActivity(addGoalIntent);
            }
            return super.onOptionsItemSelected(item);

        }


    @Override
    protected void onDestroy() {
        Log.d("GOALS", "onDestroy: ");

        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }


    @Override
    public void onResume() {

        //adapter.notifyDataSetChanged();
        super.onResume();
        adapter.notifyDataSetChanged();
        //viewPager.getAdapter().notifyDataSetChanged();
 //       adapter.notifyUpdate();
        Log.d("GOALS", "onResume2:  here!!!"+ getLocalClassName());
    }
    }

