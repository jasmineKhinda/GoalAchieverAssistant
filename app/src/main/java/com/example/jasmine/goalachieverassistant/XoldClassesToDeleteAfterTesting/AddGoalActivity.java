package com.example.jasmine.goalachieverassistant.XoldClassesToDeleteAfterTesting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
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
import com.example.jasmine.goalachieverassistant.MainActivity;
import com.example.jasmine.goalachieverassistant.Models.TaskModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.Utilities;

import io.realm.Realm;

/**
 * Created by jasmine on 24/01/18.
 */

public class AddGoalActivity extends AppCompatActivity  {


    CustomPagerFragmentAdapter adapter;
    private String GoalName;
    private String GoalReason;
    private String Spinner;
    private String DueDate;
    private GoalDetailsFragment detailsFrag;
    private GoalTasksFragment tasksFrag;
    AppBarLayout appbar;
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_edit_goal_view_fragments);
        final String goalUUID =getIntent().getExtras().getString("GOAL_UUID");
        CoordinatorLayout clay = findViewById(R.id.clayout);
        final EditText goalTitle = findViewById(R.id.ltitle);
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

                                TaskModel goalModel = realm.where(TaskModel.class).equalTo("id", goalUUID).findFirst();
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


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.menu_add_goal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //in order for the EditText for goal name to get focus when view is initialized (to help used input goal name first thing)
        clay.setFocusableInTouchMode(false);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        // Create an adapter that knows which fragment should be shown on each page
        adapter = new CustomPagerFragmentAdapter(this, getSupportFragmentManager(),goalUUID);


        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tbl_pages);
        tabLayout.setupWithViewPager(viewPager);

        adapter.startUpdate(viewPager);
        detailsFrag = (GoalDetailsFragment)adapter.instantiateItem(viewPager, 0);
        tasksFrag = (GoalTasksFragment) adapter.instantiateItem(viewPager, 1);
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomBottomSheetDialogFragment bottomSheetDialogFragment = CustomBottomSheetDialogFragment.newInstance(goalUUID,false, "",false,null);
                bottomSheetDialogFragment.show(getSupportFragmentManager(),"BottomSheet");




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


        final String goalUUID =getIntent().getExtras().getString("GOAL_UUID");
        final String mGoalName = GoalName;
        final String mReasonGoalText = GoalReason;
        final String selectedType = Spinner;
        final String selectedDueDate = DueDate;
        final EditText goalTitle = findViewById(R.id.ltitle);


        final TextInputLayout goalTextInputLayout = findViewById(R.id.lNameLayout);
        String goalName="";



        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            //validate the goal Name is entered and not blank
            if(TextUtils.isEmpty(goalTitle.getText())){
               goalTitle.requestFocus();
               goalTitle.setError(Utilities.getSpannableStringForErrorOutput("Enter Goal Name", Color.WHITE));
               goalTitle.clearFocus();

            }else{
                goalTitle.clearFocus();
                goalName = goalTitle.getText().toString();
                detailsFrag.addGoalDetailsToRealm(goalName);

                Intent addGoalIntent = new Intent(AddGoalActivity.this, MainActivity.class);
                startActivity(addGoalIntent);
            }
        } else if (item.getItemId() == R.id.action_settings_done) {
            //validate the goal Name is entered and not blank
            if(TextUtils.isEmpty(goalTitle.getText())){
                goalTitle.requestFocus();
                goalTitle.setError(Utilities.getSpannableStringForErrorOutput("Enter Goal Name", Color.WHITE));
                goalTitle.clearFocus();

            }else{
                goalTitle.clearFocus();
                goalName = goalTitle.getText().toString();
                detailsFrag.addGoalDetailsToRealm(goalName);

                Intent addGoalIntent = new Intent(AddGoalActivity.this, MainActivity.class);
                startActivity(addGoalIntent);
            }

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("GOALS", "onPause: in here");
    }


    @Override
    protected void onDestroy() {
        Log.d("GOALS", "onDestroy: ");
        super.onDestroy();
    }



}
