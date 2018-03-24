package com.example.jasmine.goalachieverassistant.XoldClassesToDeleteAfterTesting;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jasmine.goalachieverassistant.Fragments.Adapters.CustomSpinnerAdapter;
import com.example.jasmine.goalachieverassistant.Fragments.Adapters.GoalRecyclerAdapter;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.CustomBottomSheetDialogFragment;
import com.example.jasmine.goalachieverassistant.MainActivity;
import com.example.jasmine.goalachieverassistant.Models.ListCategory;
import com.example.jasmine.goalachieverassistant.Models.TaskModel;
import com.example.jasmine.goalachieverassistant.R;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class GoalListActivity extends AppCompatActivity {
    private Realm realm;
    private RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_goal_list);
        mDrawerLayout = findViewById(R.id.drawer_layout);


        recyclerView = (RecyclerView)findViewById(R.id.goal_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GoalListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        realm = Realm.getDefaultInstance();
//        RealmResults<GoalModel> tasks = realm.where(GoalModel.class).findAll();
//        tasks = tasks.sort("name", Sort.ASCENDING);

        RealmResults<TaskModel> tasks =realm.where(TaskModel.class).equalTo("isGoal", true).findAllSortedAsync(
                        new String[] {"dueDateNotEmpty", "dueDate"},
                        new Sort[] { Sort.DESCENDING, Sort.ASCENDING });

        //TODO: filter only goals here
        final GoalRecyclerAdapter adapter = new GoalRecyclerAdapter(this, tasks, true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        String[] filterGoalsList;
        filterGoalsList= getResources().getStringArray(R.array.goalfilter);
        NavigationView navigationView= findViewById(R.id.nav_view);
        List<String> mGoalFilterArrayList = Arrays.asList(filterGoalsList);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar =getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);


        final Menu menu = navigationView.getMenu();
        final SubMenu subMenu = menu.addSubMenu("Lists");
        RealmResults<ListCategory> cat = realm.where(ListCategory.class).findAll();
        for(ListCategory category: cat) {

            if ((!(category.getName().trim().equalsIgnoreCase(getResources().getString(R.string.category_Inbox).trim()))) && (!(category.getName().trim().equalsIgnoreCase(getResources().getString(R.string.category_Project).trim()))))
            {

                subMenu.add(category.getName()).setIcon(R.drawable.ic_list_black_24dp);

            }


        }



        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        //menuItem.setChecked(true);
                        if (menuItem.isChecked()) {
                            menuItem.setChecked(false);
                            mDrawerLayout.closeDrawers();
                        } else {
                            menuItem.setChecked(true);
                            mDrawerLayout.closeDrawers();
                            switch (menuItem.getItemId()) {


                                //Replacing the main content with ContentFragment Which is our Inbox View;
                                case R.id.nav_project:
                                    Toast.makeText(getApplicationContext(), "Project clicked", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(GoalListActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    // For rest of the options we just show a toast on click
                                    //TODO
                                    break;
                                case R.id.nav_inbox:
                                    Toast.makeText(getApplicationContext(), "Inbox clicked", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.nav_today:
                                    Toast.makeText(getApplicationContext(), "Today clicked", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.nav_week:
                                    Toast.makeText(getApplicationContext(), "Week Clicked", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.nav_all:
                                    Toast.makeText(getApplicationContext(), "All Clicked", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                                    break;

                            }
                        }




                        return true;
                    }
                });
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter (getSupportActionBar().getThemedContext(),android.R.layout.simple_spinner_item, mGoalFilterArrayList);
        customSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner mNavigationSpinner = (Spinner) findViewById(R.id.spinner_main);
        mNavigationSpinner.setAdapter(customSpinnerAdapter);

        mNavigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemSelected = parent.getItemAtPosition(position).toString();
                Log.d("GOALS", "onItemSelected: in toolbar for Goals. Item selected is " + itemSelected);
                String[] filterGoalsList;
                filterGoalsList = getResources().getStringArray(R.array.goalfilter);

                //TODO: filter only goals here for the following selections
                if (itemSelected.equals(filterGoalsList[0])) {
                    //default "All goals" sorts the goals with due dates ascending and puts the empty due dates at bottom

                    realm = Realm.getDefaultInstance();
                    RealmResults<TaskModel> tasks = realm.where(TaskModel.class).equalTo("isGoal", true)
                            .findAllSortedAsync(
                                    new String[]{"dueDateNotEmpty", "dueDate"},
                                    new Sort[]{Sort.DESCENDING, Sort.ASCENDING});
                    adapter.updateData(tasks);

                } else if (itemSelected.equals(filterGoalsList[1])) {
                    //By Name Ascending
                    adapter.sortGoalListWithSortOrder("name", Sort.ASCENDING);

                } else if (itemSelected.equals(filterGoalsList[2])) {
                    //No Due Date
                    adapter.sortGoalListContainsTextAndReturnsSortedList("dueDate", Sort.ASCENDING, null, true, "name");


                } else {
                    Log.e("ERROR:", "onItemSelected: valid choice not selected");

                }
                // realm.close();

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
                CustomBottomSheetDialogFragment bottomSheetDialogFragment = CustomBottomSheetDialogFragment.newInstance("",false, "",true, null);
                bottomSheetDialogFragment.show(getSupportFragmentManager(),"BottomSheet");

//                final String uuId = UUID.randomUUID().toString();
//
//                realm = Realm.getDefaultInstance();
//                realm.executeTransactionAsync(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        realm.createObject(TaskModel.class, uuId);
//
//                TaskModel goal = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
//                ListCategory cat = realm.where(ListCategory.class).equalTo("name", getResources().getString(R.string.category_Project)).findFirst();
//                goal.setTaskCategory(cat);
//                cat.getTaskList().add(goal);
//                goal.setAsGoal(true);
//                        Log.d("GOALS", " created the goal with UUID: " + uuId);
//                    }
//                });
//                Intent intent = new Intent(GoalListActivity.this, AddGoalActivity.class);
//                //pass in the unique UUI id key for the new goal to be added
//                intent.putExtra("GOAL_UUID",uuId);
//
//                startActivity(intent);
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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
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
