package com.example.jasmine.goalachieverassistant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jasmine.goalachieverassistant.Fragments.Adapters.CustomPagerFragmentAdapter;
import com.example.jasmine.goalachieverassistant.Fragments.Adapters.CustomPagerFragmentAdapterTask;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.CustomBottomSheetDialogFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.GoalDetailsFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.GoalTasksFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.TaskDetailsFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.TaskSubTaskListFragment;
import com.example.jasmine.goalachieverassistant.Models.ChildSubGoalModel;
import com.example.jasmine.goalachieverassistant.Models.GoalModel;
import com.example.jasmine.goalachieverassistant.Models.SubGoalModel;
import com.example.jasmine.goalachieverassistant.Models.TaskModel;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jasmine on 15/02/18.
 */

public class EditTaskActivity extends AppCompatActivity {





    private TaskDetailsFragment detailsFrag;
    private TaskSubTaskListFragment tasksFrag;
    private String taskKey;
    private String taskName;
    //EditText taskTitle;
    Realm realm;
    private CustomPagerFragmentAdapterTask adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_task_view_fragment);
        taskKey = getIntent().getExtras().getString("EDIT_TASKUUID");
        taskName =getIntent().getExtras().getString("EDIT_TASKNAME");
        TextView breadCrumb =(TextView) findViewById(R.id.subTask_breadcrumb);


        breadCrumb.setVisibility(View.GONE);

        TextInputLayout textInputLayout = (TextInputLayout)findViewById(R.id.lNameLayout);
        textInputLayout.setHint(getResources().getString(R.string.task_name_hint));
        final EditText taskTitle =(EditText) findViewById(R.id.task_title);
        taskTitle.setImeOptions(EditorInfo.IME_ACTION_DONE);
        taskTitle.setHint(R.string.task_name_hint);
        taskTitle.setRawInputType(InputType.TYPE_CLASS_TEXT);
        taskTitle.setMaxLines(5);
        taskTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //Log.d("GOALS", "adding goal name into realm" + event + "   "+ actionId+ "  "+ EditorInfo.IME_ACTION_DONE);
                if(actionId== EditorInfo.IME_ACTION_DONE){

                    try {
                        realm = Realm.getDefaultInstance();
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {

                                TaskModel subGoalModel = realm.where(TaskModel.class).equalTo("id", taskKey).findFirst();
                                subGoalModel.setName(taskTitle.getText().toString());

                                Log.d("GOALS", "adding goal name into realm");



                            }
                        });
                    }finally{
                        realm.close();
                    }
                    taskTitle.clearFocus();
                }
                return false;
            }
        });



        //final String goalUUID =getIntent().getExtras().getString("GOAL_UUID");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_edit_goal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager_editTask);

//        // Create an adapter that knows which fragment should be shown on each page
         adapter = new CustomPagerFragmentAdapterTask(this, getSupportFragmentManager(), taskKey);
        Log.d("GOALS", " taskNAme is "+ taskName);
        Log.d("GOALS", " context is "+ this);
        Log.d("GOALS", " adapter is "+ adapter.getItem(0));
//
//
//        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tbl_pages_task);
        tabLayout.setupWithViewPager(viewPager);

        adapter.startUpdate(viewPager);
        Log.d("GOALS", " goalbee item at position "+ adapter.instantiateItem(viewPager, 0));
        detailsFrag = (TaskDetailsFragment) adapter.instantiateItem(viewPager, 0);

        tasksFrag = (TaskSubTaskListFragment) adapter.instantiateItem(viewPager, 1);
        adapter.finishUpdate(viewPager);
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
        taskTitle.setText(taskName);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomBottomSheetDialogFragment bottomSheetDialogFragment = CustomBottomSheetDialogFragment.newInstance(taskKey,true,taskName,false);
                bottomSheetDialogFragment.show(getSupportFragmentManager(),"BottomSheet");


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
        final EditText taskTitle =(EditText) findViewById(R.id.task_title);

        //final String goalUUID =getIntent().getExtras().getString("GOAL_UUID");

        //      final EditText taskTitle =(EditText) findViewById(R.id.ltitle);
        String childTaskName="";

        if (item.getItemId() == android.R.id.home) {
            //validate the goal Name is entered and not blank
            if(TextUtils.isEmpty(taskTitle.getText())){
                taskTitle.requestFocus();
                taskTitle.setError(Utilities.getSpannableStringForErrorOutput("Enter Task Name", Color.WHITE));

            }else{
                childTaskName = taskTitle.getText().toString();
                detailsFrag.addTaskDetailsToRealm(childTaskName);
                startIntentAfterUserCompletesActivity();
            }

        } else if (item.getItemId() == R.id.action_settings_done) {
            //validate the goal Name is entered and not blank
            if(TextUtils.isEmpty(taskTitle.getText())){
                taskTitle.requestFocus();
                taskTitle.setError(Utilities.getSpannableStringForErrorOutput("Enter Task Name", Color.WHITE));

            }else{
                childTaskName = taskTitle.getText().toString();
                detailsFrag.addTaskDetailsToRealm(childTaskName);
                startIntentAfterUserCompletesActivity();
            }

        }else if (item.getItemId() == R.id.delete) {


            Realm realm = Realm.getDefaultInstance();
            try{
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
//                        RealmResults<SubGoalModel> childSubGoalModel = realm.where(SubGoalModel.class).equalTo("id", taskKey).findAll();
 //                       childSubGoalModel.deleteFirstFromRealm();
                        RealmResults<TaskModel> taskModel = realm.where(TaskModel.class).equalTo("id",taskKey).findAll();

                        TaskModel subGoalModelChild = realm.where(TaskModel.class).equalTo("id",taskKey).findFirst();

                        //delete all children that belong to the Task first
                        Log.d("GOALS", "child subgoal list is size  "+ subGoalModelChild.getSubTaskCount() );
                        if(subGoalModelChild.getChildList().size()>0){
                            subGoalModelChild.getChildList().deleteAllFromRealm();
                        }
                        //delete the task after children are deleted
                        taskModel.deleteFirstFromRealm();
                        Log.d("GOALS", "deleted item? ");
                        // realm.close();


                    }
                }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                finish();
            }});

            }finally{
                realm.close();

            }

            startIntentAfterUserCompletesActivity();

           
            
        }
        return super.onOptionsItemSelected(item);

    }

    public void startIntentAfterUserCompletesActivity(){
//        try{
//            realm = Realm.getDefaultInstance();
//            SubGoalModel subGoalModel =realm.where(SubGoalModel.class).equalTo("id", taskKey).findFirst();
//            String goalUUID= subGoalModel.getGoal().getId();
//            String goalName= subGoalModel.getGoal().getName();
//
//            Intent addGoalIntent = new Intent(EditTaskActivity.this, EditGoalActivity.class);
//            addGoalIntent.putExtra("EDIT_GOALUUID",goalUUID);
//            addGoalIntent.putExtra("EDIT_GOALNAME",goalName);
//            startActivity(addGoalIntent);
//        }finally{
//            realm.close();
//        }
        finish();
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
        super.onResume();
        adapter.notifyDataSetChanged();
        Log.d("GOALS", "onResume:  here!!!");
    }



}


