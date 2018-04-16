package com.example.jasmine.goalachieverassistant.Fragments.Fragments;

import java.util.Date;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.jasmine.goalachieverassistant.Models.TaskModel;
import com.example.jasmine.goalachieverassistant.Utilities;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.RecyclerviewExpandedItem.adapter.SubGoalAdapter;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmExpandableRecyclerAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link GoalTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalTasksFragment extends Fragment implements DatePickerFragment.DatePickerFragmentListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String goalUUID;
    private Realm realm;
    private SubGoalAdapter adapter;
    RealmResults<TaskModel> tasksForThisGoal;
    public EditText addTaskDueDate;
    Date dueDate;
    private int mScrollY;
    private boolean dueDateEmpty;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;


    private static final String GOAL_UUID = "GoalUUID";


    public GoalTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDateSet(Date view) {

        Log.d("GOALS", "in onDateSet  " + view);
        // This method will be called with the date from the `DatePicker`.
        if(null != view ){

            String dateToDisplay = Utilities.parseDateForDisplay(view);
            addTaskDueDate.setText(dateToDisplay);

        }else{
            addTaskDueDate.setText(R.string.no_due_date);
        }

        dueDate = view;

    }


    public static GoalTasksFragment newInstance(String param3) {
        GoalTasksFragment frag = new GoalTasksFragment();
        Bundle args = new Bundle();
        args.putString(GOAL_UUID, param3);
        Log.d("GOALS", "GOAL_UUID " + args.get(GOAL_UUID).toString() + "  param3 " + param3);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("GOALS", "onCreateView: goal ");
        View v = inflater.inflate(R.layout.fragment_goal_tasks, container, false);
        Log.d("GOALS", "GOALS1 SAVED INSTANCE STATE");

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("GOALS", "GOALS1 onViewCreated");
       recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_task_list);
        goalUUID = getArguments().getString(GOAL_UUID);
        Log.d("GOALS", "onViewCreated:  for GoalTasksFragment goal UUID" + goalUUID);
        final DatePickerFragment fragment =DatePickerFragment.newInstance(this);

        realm = Realm.getDefaultInstance();
//        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_task);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                CustomBottomSheetDialogFragment bottomSheetDialogFragment = CustomBottomSheetDialogFragment.newInstance(goalUUID);
//                bottomSheetDialogFragment.show(getFragmentManager(),"BottomSheet");
//
////                //setting the layout of the add Task dialog
////                LayoutInflater inflater = LayoutInflater.from(getContext());
////                View subView = inflater.inflate(R.layout.dialog_add_task_subtask, null);
////                final EditText addTaskName = (EditText) subView.findViewById(R.id.add_task_name);
////                addTaskDueDate = (EditText) subView.findViewById(R.id.add_task_dueDate);
////                final ImageView addTaskDueDateImage = (ImageView) subView.findViewById(R.id.add_task_date_image);
////                dueDate=null;
////
////
////                addTaskDueDate.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////
////                       // DatePickerFragment date = new DatePickerFragment();
////                        fragment.show(getChildFragmentManager(), "Task Date");
////
////                    }
////                });
////
////                addTaskDueDateImage.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////
////                        //DatePickerFragment fragment = new DatePickerFragment();
////                        fragment.show(getChildFragmentManager(), "Task Date");
////                        //new GoalTasksFragment.DatePickersFragment().show(getFragmentManager(), "Task Date");
////                    }
////                });
////
////
////                 //final EditText taskEditText = new EditText(getActivity());
////                //Adding the add task layout to the AlertDialog builder
////                AlertDialog dialog = new AlertDialog.Builder(getActivity())
////                        .setTitle("Add a Task")
////                        .setView(subView)
////                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
////
////                            @Override
////                            public void onClick(DialogInterface dialogInterface, int i) {
////                                //realm = Realm.getDefaultInstance();
////
////                                    realm.executeTransactionAsync(new Realm.Transaction() {
////                                                                      @Override
////                                                                      public void execute(Realm realm) {
////                                                                          final String uuID = UUID.randomUUID().toString();
////                                                                          realm.createObject(SubGoalModel.class, uuID)
////                                                                                  .setName(String.valueOf(addTaskName.getText()));
////                                                                          SubGoalModel sub = realm.where(SubGoalModel.class).equalTo("id", uuID).findFirst();
////                                                                          GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", goalUUID).findFirst();
////                                                                          goalModel.getSubgoals().add(sub);
////                                                                          sub.setGoal(goalModel);
////                                                                          Log.d("GOALS", "right before setting duedate empty "+ dueDate);
////                                                                          if(null !=dueDate && addTaskDueDate.getText().toString().equals(Utilities.parseDateForDisplay(dueDate))){
////                                                                              sub.setDueDate(dueDate);
////                                                                              sub.setDueDateNotEmpty(dueDate);
////                                                                              Log.d("GOALS", "Due dates matched! added subgoal due date");
////                                                                          }
////                                                                          else{
////                                                                              Log.d("GOALS", "Due dates didn't matched!+ " + sub.getDueDateNotEmpty());
////                                                                          }
//////                                                                          if(null !=dueDate){
//////                                                                              sub.setDueDate(dueDate);
//////                                                                              Log.d("GOALS", "Due date is not null adding to realm");
//////                                                                          }
////
////
////                                                                      }
////                                                                  },
////                                            new Realm.Transaction.OnSuccess() {
////                                                @Override
////                                                public void onSuccess() {
////                                                    Log.d("GOALS", "onSuccess: ");
////
////
////                                                }
////                                            }, new Realm.Transaction.OnError() {
////                                                @Override
////                                                public void onError(Throwable error) {
////                                                    Log.d("GOALS", "onError: ");
////                                                }
////                                            });
////
////
////
////                            }
////                        })
////                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialogInterface, int i) {
////                                //resetting the dueDate class variable so that the next instance that calls this fragment starts from clean due date
////
////
////                                Log.d("GOALS", "resetting the variable");
////                            }
////                        })
////                        .create();
////                dialog.show();
//
//
//            }
//        });

         RecyclerView.OnScrollListener mTotalScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollY += dy;
            }
        };

        //recycler view of all the sub goals and child sub goals
     //  final RecyclerView recyclerViewInside = (RecyclerView) view.findViewById(R.id.recyclerview_task_list);
       //tasksForThisGoal = realm.where(SubGoalModel.class).equalTo("goal.id", goalUUID).findAllSorted("name", Sort.ASCENDING);

        tasksForThisGoal =realm.where(TaskModel.class).equalTo("parentGoalId", goalUUID).
                findAllSorted(
                        new String[] {"dueDateNotEmpty", "dueDate"},
                        new Sort[] { Sort.DESCENDING, Sort.ASCENDING });



        tasksForThisGoal.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<TaskModel>>() {
            @Override
            public void onChange(RealmResults<TaskModel> persons, OrderedCollectionChangeSet
                    changeset) {
//new below ****
 //              adapter = new SubGoalAdapter(persons, "id");
//                adapter.notifyParentDataSetChanged();
//                adapter.notifyDataSetChanged();


                //recyclerView.setAdapter(adapter);

                Log.d("GOALS", "in change listener goal tasks");
                adapter.updateData(persons);

//  recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
              //  recyclerView.scrollToPosition(10);




            }
        });

        adapter = new SubGoalAdapter(tasksForThisGoal, "id");
        recyclerView.setAdapter(adapter);
       // recyclerView.setHasFixedSize(false);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));





        //expanding and collapsing each subgoal or child sub goal
        adapter.setExpandCollapseListener(new RealmExpandableRecyclerAdapter.ExpandCollapseListener() {
            @UiThread
            @Override
            public void onParentExpanded(int parentPosition) {
               TaskModel expandedTask = adapter.getItem(parentPosition);
                Log.d("GOALS", "GOALS1 exp");




            }

            @UiThread
            @Override
            public void onParentCollapsed(int parentPosition) {
                TaskModel collapsedTask = adapter.getItem(parentPosition);

                Log.d("GOALS", "GOALS1 col");
            }


        });


    }

    @Override
    public void onStop() {

        Log.d("GOALS", "onStop: ");
        if(!(realm.isClosed())){
            tasksForThisGoal.removeAllChangeListeners();
            realm.close();
        }

        super.onStop();

    }

    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";

    /**
     * This is a method for Fragment.
     * You can do the same in onCreate or onRestoreInstanceState
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("GOALS", "GOALS1 RESTORED");
        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("GOALS", "GOALS1 SAVED INSTANT STATE");
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
    }

}