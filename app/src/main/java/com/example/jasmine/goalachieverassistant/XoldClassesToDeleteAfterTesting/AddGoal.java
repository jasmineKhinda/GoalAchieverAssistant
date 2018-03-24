package com.example.jasmine.goalachieverassistant.XoldClassesToDeleteAfterTesting;

import android.support.v7.app.AppCompatActivity;


public class AddGoal extends AppCompatActivity {

//    EditText mEditGoal;
//    EditText mReasonGoal;
//    private Realm realm;
//    private static String selectedDate;
//    private static EditText taskDueDate;
//    String selectedDueDate;
//    private SubGoalAdapter adapter;
//    RealmResults<SubGoalModel> subgoalsForThisGoal;
//    private Toolbar toolbar;
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
//
//    //public final String subGoalUUID = UUID.randomUUID().toString();
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.x_to_delete_activity_add_goal);
//        final String goalUUID =getIntent().getExtras().getString("GOAL_UUID");
//
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        getSupportActionBar().setTitle(R.string.goal_edit_title_toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
////        toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////
////        viewPager = (ViewPager) findViewById(R.id.viewpager);
////        setupViewPager(viewPager);
////
////        tabLayout = (TabLayout) findViewById(R.id.tbl_pages);
////        tabLayout.setupWithViewPager(viewPager);
//
////        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
////        final ActionBar actionBar=getSupportActionBar();
////        actionBar.setDisplayHomeAsUpEnabled(false);
//
//
////        getSupportActionBar().hide();
////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
////        toolbar.setTitle(R.string.goal_title_toolbar);
//
////        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
////        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_action_navigation_more_vert));
////
////        toolbar.inflateMenu(R.menu.menu_add_goal);
////        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
////            @Override
////            public boolean onMenuItemClick(MenuItem item) {
////                return onOptionsItemSelected(item);
////            }
////        });
////
////        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onColorSet(View v) {
////                //What to do on back clicked
////                if (onSupportNavigateUp()){
////                    Intent addGoalIntent = new Intent(AddGoal.this, GoalListActivity.class);
////                    startActivity(addGoalIntent);
////                }
////
////            }
////        });
//
//
//
//
////        ViewPager viewPager=(ViewPager)findViewById(R.id.viewpager);
////
////        setUpViewPager(viewPager);
//
////        TabLayout tabLayout = (TabLayout) findViewById(R.id.tbl_pages);
////        tabLayout.addTab(tabLayout.newTab().setText("Details"));
////        tabLayout.addTab(tabLayout.newTab().setText("Tasks"));
////        tabLayout.setupWithViewPager(viewPager);
//
//
//
//
//
//
// //       final CollapsingToolbarLayout mCollapsingToolbarLayout =(CollapsingToolbarLayout) findViewById(R.id.collapsing_tool_bar_layout);
// //       mCollapsingToolbarLayout.setTitle("Title");
////        AppBarLayout appBarLayout =(AppBarLayout) findViewById(R.id.app_bar_layout);
//
//
// //       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//
////
////        getSupportActionBar().setTitle(R.string.goal_title_toolbar);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//
//
////        getSupportActionBar().hide();
////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////
//////        toolbar.setTitle(R.string.goal_title_toolbar);
////
////        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
////        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_action_navigation_more_vert));
////
////        toolbar.inflateMenu(R.menu.menu_add_goal);
////        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
////            @Override
////            public boolean onMenuItemClick(MenuItem item) {
////                return onOptionsItemSelected(item);
////            }
////        });
////
////        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onColorSet(View v) {
////                //What to do on back clicked
////                 if (onSupportNavigateUp()){
////                    Intent addGoalIntent = new Intent(AddGoal.this, GoalListActivity.class);
////                    startActivity(addGoalIntent);
////                }
////
////            }
////        });
//
////        TabLayout tabLayout =(TabLayout)findViewById(R.id.tbl_pages) ;
////        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.tab1)));
////        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.tab2)));
////        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.tab3)));
//
//
//        //tab tabBar = (Toolbar) findViewById(R.id.tbl_pages);
//
//
//
//
//        // Specify that tabs should be displayed in the action bar.
////        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
////        getSupportActionBar().hide();
//
//
//
//
//
//        realm = Realm.getDefaultInstance();
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final EditText taskEditText = new EditText(AddGoal.this);
//                AlertDialog dialog = new AlertDialog.Builder(AddGoal.this)
//                        .setTitle("Add Task")
//                        .setView(taskEditText)
//                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //realm = Realm.getDefaultInstance();
//                                realm.executeTransactionAsync(new Realm.Transaction() {
//                                                                  @Override
//                                                                  public void execute(Realm realm) {
//                                                                      final String uuID = UUID.randomUUID().toString();
//                                                                      realm.createObject(SubGoalModel.class, uuID)
//                                                                              .setName(String.valueOf(taskEditText.getText()));
//                                                                      SubGoalModel sub = realm.where(SubGoalModel.class).equalTo("id", uuID).findFirst();
//                                                                      GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", goalUUID).findFirst();
//                                                                      goalModel.getSubgoals().add(sub);
//                                                                      sub.setGoal(goalModel);
//                                                                      Log.d("GOALS", "added subgoal ");
//
//                                                                  }
//                                                              },
//                                        new Realm.Transaction.OnSuccess() {
//                                            @Override
//                                            public void onSuccess() {
//                                                Log.d("GOALS", "onSuccess: ");
//
//
//                                            }
//                                        }, new Realm.Transaction.OnError() {
//                                            @Override
//                                            public void onError(Throwable error) {
//                                                Log.d("GOALS", "onError: ");
//                                            }
//                                        });
//                            }
//                        })
//                        .setNegativeButton("Cancel", null)
//                        .create();
//                dialog.show();
//            }
//        });
//
//
//        //recycler view of all the sub goals and child sub goals
//        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        subgoalsForThisGoal = realm.where(SubGoalModel.class).equalTo("goal.id", goalUUID).findAll();
//
//        subgoalsForThisGoal.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<SubGoalModel>>() {
//            @Override
//            public void onChange(RealmResults<SubGoalModel> persons, OrderedCollectionChangeSet changeset) {
//
//                adapter = new SubGoalAdapter(persons, "id");
//                recyclerView.setAdapter(adapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(AddGoal.this));
//                Log.d("GOALS", "onChange!!!!!!!!!!: ");
//
//
//            }
//        });
//
//        adapter = new SubGoalAdapter(subgoalsForThisGoal, "id");
////        recyclerView.setAdapter(adapter);
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        Log.d("GOALS", "onCreate real data  : "+  subgoalsForThisGoal);
//
//
//
//        //expanding and collapsing each subgoal or child sub goal
//        adapter.setExpandCollapseListener(new RealmExpandableRecyclerAdapter.ExpandCollapseListener() {
//            @UiThread
//            @Override
//            public void onParentExpanded(int parentPosition) {
//                SubGoalModel expandedRecipe = adapter.getItem(parentPosition);
//
//                String toastMsg = getResources().getString(R.string.expanded, expandedRecipe.getName());
////                Toast.makeText(AddGoal.this,
////                        toastMsg,
////                        Toast.LENGTH_SHORT)
////                        .show();
//            }
//
//            @UiThread
//            @Override
//            public void onParentCollapsed(int parentPosition) {
//                SubGoalModel collapsedRecipe = adapter.getItem(parentPosition);
//
//                String toastMsg = getResources().getString(R.string.collapsed, collapsedRecipe.getName());
////                Toast.makeText(AddGoal.this,
////                        toastMsg,
////                        Toast.LENGTH_SHORT)
////                        .show();
//            }
//        });
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//        Spinner spinner2 = (Spinner) findViewById(R.id.spinnerType);
//        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
//                R.array.category, android.R.layout.simple_spinner_item);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner2.setAdapter(adapter2);
//
//        taskDueDate =(EditText) findViewById(R.id.add_task_ending);
//        taskDueDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new DatePickerFragment().show(getSupportFragmentManager(), "Task Date");
//            }
//        });
//
//        ImageView addTaskDate = (ImageView) findViewById(R.id.add_task_date);
//        addTaskDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new DatePickerFragment().show(getSupportFragmentManager(), "Task Date");
//            }
//        });
//
//
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_add_goal, menu);
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        final String mGoals = getResources().getString(R.string.db_parent_Goals);
//        final String mName = getResources().getString(R.string.db_Goal_Name);
//        final String mReason = getResources().getString(R.string.db_Goal_Reason);
//        final String mTime = getResources().getString(R.string.db_Goal_Time);
//        final String mPriority = getResources().getString(R.string.db_Goal_Priority);
//        final String mType = getResources().getString(R.string.db_Goal_Type);
//        final String goalUUID =getIntent().getExtras().getString("GOAL_UUID");
//
//
//        // handle arrow click here
//        if (item.getItemId() == android.R.id.home) {
//            finish(); // close this activity and return to preview activity (if there is any)
//        } else if (item.getItemId() == R.id.action_settings_done) {
//
//            mEditGoal = (EditText) findViewById(R.id.editGoal);
//            final String mGoalName = mEditGoal.getText().toString();
//
//            mReasonGoal = (EditText) findViewById(R.id.addReason);
//            final String mReasonGoalText = mReasonGoal.getText().toString();
//
//            Spinner spinner2 = (Spinner) findViewById(R.id.spinnerType);
//            final String selectedType = spinner2.getSelectedItem().toString();
//
////            Spinner spinner1 = (Spinner) findViewById(R.id.spinnerPriority);
////            final String selectedPriority = spinner1.getSelectedItem().toString();
//
//            selectedDueDate = taskDueDate.getText().toString();
//
//
//            long mDate = System.currentTimeMillis();
//            SimpleDateFormat mSdf = new SimpleDateFormat("MMM MM dd,yyy h:mm a");
//            final String mDateString = mSdf.format(mDate);
// //           final String uuId = UUID.randomUUID().toString();
////            final RealmResults<SubGoalModel> realmResultsSubGoal = realm.where(SubGoalModel.class).findAll();
//
////            realm = Realm.getDefaultInstance();
////            realm.executeTransactionAsync(new Realm.Transaction() {
////                @Override
////                public void execute(Realm realm) {
////
////
//// //                   realm.createObject(GoalModel.class, uuId)
//// //                           .setName(mGoalName);
////                    GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", goalUUID).findFirst();
////                    goalModel.setName(mGoalName);
////                    goalModel.setTime(mDateString);
////                    Log.d("GOALS", "reason :  " + mReasonGoalText);
////                    goalModel.setReason(mReasonGoalText);
//////                   goalModel.setPriority(selectedPriority);
////                    goalModel.setType(selectedType);
////                    goalModel.setDueDate(selectedDueDate);
////                    goalModel.setTimeStamp(System.currentTimeMillis());
////
////
////                    Log.d("GOALS", " goalModel added id: " + goalUUID + " goalModel name is " + goalModel.getName());
////
////                }
////            });
//
//            Intent addGoalIntent = new Intent(AddGoal.this, GoalListActivity.class);
//            startActivity(addGoalIntent);
//        }
//        return super.onOptionsItemSelected(item);
//
//    }
//
//    public void changeTaskDone(final String taskId) {
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                SubGoalModel task = realm.where(SubGoalModel.class).equalTo("id", taskId).findFirst();
//                task.setDone(!task.getDone());
//            }
//        });
//    }
//
//    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//            return new DatePickerDialog(getActivity(), this, year, month, day);
//        }
//        public void onDateSet(DatePicker view, int year, int month, int day) {
//
//            Log.d("GOALS", "Month "+  month);
//            Log.d("GOALS", "Day "+  day);
//            selectedDate = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
//            taskDueDate.setText(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
//
//            //TextView textview = (TextView) getActivity().findViewById(R.id.textView1);
//
//        }
//    }
//
//    private void setListViewHeight(ExpandableListView listView,
//                                   int group) {
//        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
//        int totalHeight = 0;
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
//                View.MeasureSpec.EXACTLY);
//        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
//            View groupItem = listAdapter.getGroupView(i, false, null, listView);
//            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//
//            totalHeight += groupItem.getMeasuredHeight();
//
//            if (((listView.isGroupExpanded(i)) && (i != group))
//                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
//                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
//                    View listItem = listAdapter.getChildView(i, j, false, null,
//                            listView);
//                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//
//                    totalHeight += listItem.getMeasuredHeight();
//
//                }
//            }
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        int height = totalHeight
//                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
//        if (height < 10)
//            height = 200;
//        params.height = height;
//        listView.setLayoutParams(params);
//        listView.requestLayout();
//
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        realm.close();
//        super.onDestroy();
//    }



    }
