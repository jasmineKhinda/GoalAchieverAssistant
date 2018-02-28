package com.example.jasmine.goalachieverassistant;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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

import com.example.jasmine.goalachieverassistant.Fragments.Adapters.CustomPagerFragmentAdapterTask;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.ChildTaskDetailsFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.CustomBottomSheetDialogFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.TaskDetailsFragment;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.TaskSubTaskListFragment;
import com.example.jasmine.goalachieverassistant.Models.ChildSubGoalModel;
import com.example.jasmine.goalachieverassistant.Models.GoalModel;
import com.example.jasmine.goalachieverassistant.Models.SubGoalModel;

import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jasmine on 23/02/18.
 */

public class EditSubTaskActivity extends AppCompatActivity {





    private ChildTaskDetailsFragment detailsFrag;
    private TaskSubTaskListFragment tasksFrag;
    private String taskKey;
    private String taskName;
    private String callingActivity;
    //EditText taskTitle;
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_task_view_fragment);
        TextInputLayout textInputLayout = (TextInputLayout)findViewById(R.id.lNameLayout);
        TabLayout tab = (TabLayout)findViewById(R.id.tbl_pages_task);
        tab.setVisibility(View.GONE);
        textInputLayout.setHint(getResources().getString(R.string.sub_task_name_hint));
        TextView breadCrumb =(TextView) findViewById(R.id.subTask_breadcrumb);
        breadCrumb.setVisibility(View.VISIBLE);
        taskKey = getIntent().getExtras().getString("EDIT_TASKUUID");
        callingActivity = getIntent().getExtras().getString("CALLING_ACTIVITY");
        taskName =getIntent().getExtras().getString("EDIT_TASKNAME");
        final EditText taskTitle =(EditText) findViewById(R.id.task_title);


        realm = Realm.getDefaultInstance();
        final ChildSubGoalModel childSubGoalModel = realm.where(ChildSubGoalModel.class).equalTo("id", taskKey).findFirst();
       // breadCrumb.setText("Subtask of "+ childSubGoalModel.getSubGoal().getName() );
        breadCrumb.setText(Html.fromHtml("Subtask of  &#160;" +   "<font color=\"#0645AD\"<b>"+childSubGoalModel.getSubGoal().getName()+"</b></font>"));


        breadCrumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewTaskIntent = new Intent(EditSubTaskActivity.this, EditTaskActivity.class);
                viewTaskIntent.putExtra("EDIT_TASKUUID", childSubGoalModel.getSubGoal().getId());
                viewTaskIntent.putExtra("EDIT_TASKNAME", childSubGoalModel.getSubGoal().getName());
                startActivity(viewTaskIntent);
            }
        });

        taskTitle.setImeOptions(EditorInfo.IME_ACTION_DONE);
        taskTitle.setRawInputType(InputType.TYPE_CLASS_TEXT);
        taskTitle.setMaxLines(5);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_task);
        fab.setVisibility(View.GONE);

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

                                ChildSubGoalModel childSubGoalModel = realm.where(ChildSubGoalModel.class).equalTo("id", taskKey).findFirst();
                                childSubGoalModel.setName(taskTitle.getText().toString());

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


        View viewLayout = (View) findViewById(R.id.clayout);



        //View detailsFragment = (View) findViewById(R.id.detail_holder);
        detailsFrag = ChildTaskDetailsFragment.newInstance(taskKey);

        //final String goalUUID =getIntent().getExtras().getString("GOAL_UUID");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_edit_goal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final String TASK_UUID = "GoalUUID";
        Bundle args = new Bundle();
        args.putString(TASK_UUID, taskKey);

        android.support.v4.app.FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        Fragment fragment = Fragment.instantiate(this,ChildTaskDetailsFragment.class.getName(),args);
        ft.add(viewLayout.getId(), fragment, "SubTDetails");
        ft.commit();

        taskTitle.setText(taskName);

        detailsFrag = (ChildTaskDetailsFragment) fragment;




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
                taskTitle.setError(Utilities.getSpannableStringForErrorOutput("Enter Sub Task Name", Color.WHITE));

            }else{
                childTaskName = taskTitle.getText().toString();

                detailsFrag.addSubTaskDetailsToRealm(childTaskName);

                try{
                    startIntentAfterUserCompletesActivityFromSubTask();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

        } else if (item.getItemId() == R.id.action_settings_done) {
            //validate the goal Name is entered and not blank
            if(TextUtils.isEmpty(taskTitle.getText())){
                taskTitle.requestFocus();
                taskTitle.setError(Utilities.getSpannableStringForErrorOutput("Enter Sub Task Name", Color.WHITE));

            }else{
                childTaskName = taskTitle.getText().toString();
                detailsFrag.addSubTaskDetailsToRealm(childTaskName);
                try{
                    startIntentAfterUserCompletesActivityFromSubTask();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }else if (item.getItemId() == R.id.delete) {


            Realm realm = Realm.getDefaultInstance();
            try{
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
//                        RealmResults<SubGoalModel> childSubGoalModel = realm.where(SubGoalModel.class).equalTo("id", taskKey).findAll();
                        //                       childSubGoalModel.deleteFirstFromRealm();
                        RealmResults<ChildSubGoalModel> subGoalModel = realm.where(ChildSubGoalModel.class).equalTo("id",taskKey).findAll();

                        //delete the subtask
                        subGoalModel.deleteFirstFromRealm();
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

            try{
                startIntentAfterUserCompletesActivityFromSubTask();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }



        }
        return super.onOptionsItemSelected(item);

    }

    public void startIntentAfterUserCompletesActivityFromSubTask()throws  ClassNotFoundException  {
//        try{
//            realm = Realm.getDefaultInstance();
//            ChildSubGoalModel childSubGoalModel =realm.where(ChildSubGoalModel.class).equalTo("id", taskKey).findFirst();
//            String subGoalKey = childSubGoalModel.getSubGoal().getId();
//            SubGoalModel subGoalModel =realm.where(SubGoalModel.class).equalTo("id", subGoalKey).findFirst();
//            String goalUUID= subGoalModel.getGoal().getId();
//            String goalName= subGoalModel.getGoal().getName();
//
//            Log.d("GOALS", "calling activity is "+ callingActivity);
//            Class callerClass = Class.forName(callingActivity);
    //        if(EditGoalActivity.class.getName().contains(getActivity().getLocalClassName())){
//                Intent addGoalIntent = new Intent(EditSubTaskActivity.this, callerClass);
//                addGoalIntent.putExtra("EDIT_GOALUUID",goalUUID);
//                addGoalIntent.putExtra("EDIT_GOALNAME",goalName);



 //           startActivity(addGoalIntent);
//        }finally{
//            realm.close();
//        }

        finish();
    }



    @Override
    protected void onDestroy() {
        Log.d("GOALS", "onDestroy: ");
        if(!realm.isClosed()){
            realm.close();
        }
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



}



