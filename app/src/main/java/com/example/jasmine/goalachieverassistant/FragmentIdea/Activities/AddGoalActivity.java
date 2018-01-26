package com.example.jasmine.goalachieverassistant.FragmentIdea.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jasmine.goalachieverassistant.FragmentIdea.Adapters.CustomPagerFragmentAdapter;
import com.example.jasmine.goalachieverassistant.FragmentIdea.Fragments.GoalDetailsFragment;
import com.example.jasmine.goalachieverassistant.FragmentIdea.Fragments.GoalTasksFragment;
import com.example.jasmine.goalachieverassistant.R;

import io.realm.Realm;

/**
 * Created by jasmine on 24/01/18.
 */

public class AddGoalActivity extends AppCompatActivity  {


    private Realm realm;
    CustomPagerFragmentAdapter adapter;
    private String GoalName;
    private String GoalReason;
    private String Spinner;
    private String DueDate;
    private GoalDetailsFragment detailsFrag;
    private GoalTasksFragment tasksFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.x_add_goal_view_fragments);
        final String goalUUID =getIntent().getExtras().getString("GOAL_UUID");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_add_goal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


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

        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        } else if (item.getItemId() == R.id.action_settings_done) {

            detailsFrag.addGoalDetailsToRealm();


//
//            long mDate = System.currentTimeMillis();
//            SimpleDateFormat mSdf = new SimpleDateFormat("MMM MM dd,yyy h:mm a");
//            final String mDateString = mSdf.format(mDate);
//
//            realm = Realm.getDefaultInstance();
//            realm.executeTransactionAsync(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//
//                    GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", goalUUID).findFirst();
//                    goalModel.setName(mGoalName);
//                    goalModel.setTime(mDateString);
//                    Log.d("GOALS", "reason :  " + mReasonGoalText);
//                    goalModel.setReason(mReasonGoalText);
//                    goalModel.setType(selectedType);
//                    goalModel.setDueDate(selectedDueDate);
//                    goalModel.setTimeStamp(System.currentTimeMillis());
//                    Log.d("GOALS", " goalModel added id: " + goalUUID + " goalModel name is " + goalModel.getName());
//
//                }
//            });
//
//            Intent addGoalIntent = new Intent(AddGoalActivity.this, GoalListActivity.class);
//            startActivity(addGoalIntent);
        }
        return super.onOptionsItemSelected(item);

    }}
