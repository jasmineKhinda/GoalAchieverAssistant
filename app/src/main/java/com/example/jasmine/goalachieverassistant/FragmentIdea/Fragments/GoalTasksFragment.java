package com.example.jasmine.goalachieverassistant.FragmentIdea.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.jasmine.goalachieverassistant.AddGoal;
import com.example.jasmine.goalachieverassistant.ChildSubGoalModel;
import com.example.jasmine.goalachieverassistant.GoalModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.SubGoalModel;
import com.example.jasmine.goalachieverassistant.recyclerview.adapter.SubGoalAdapter;

import java.util.Calendar;
import java.util.UUID;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmExpandableRecyclerAdapter;
import io.realm.RealmResults;

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
    RealmResults<SubGoalModel> subgoalsForThisGoal;
    public EditText addTaskDueDate;

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

        Log.d("GOALS", "in onDateSet");
        // This method will be called with the date from the `DatePicker`.

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d");
        String date = sdf.format(view);

        addTaskDueDate.setText(date);
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
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goalUUID = getArguments().getString(GOAL_UUID);
        Log.d("GOALS", "onViewCreated: goal UUID" + goalUUID);
        final DatePickerFragment fragment =DatePickerFragment.newInstance(this);

        realm = Realm.getDefaultInstance();
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //setting the layout of the add Task dialog
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View subView = inflater.inflate(R.layout.dialog_add_task_subtask, null);
                final EditText addTaskName = (EditText) subView.findViewById(R.id.add_task_name);
                addTaskDueDate = (EditText) subView.findViewById(R.id.add_task_dueDate);
                final ImageView addTaskDueDateImage = (ImageView) subView.findViewById(R.id.add_task_date_image);



                addTaskDueDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       // DatePickerFragment date = new DatePickerFragment();
                        fragment.show(getChildFragmentManager(), "Task Date");

                    }
                });

                addTaskDueDateImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //DatePickerFragment fragment = new DatePickerFragment();
                        fragment.show(getChildFragmentManager(), "Task Date");
                        //new GoalTasksFragment.DatePickersFragment().show(getFragmentManager(), "Task Date");
                    }
                });


                // final EditText taskEditText = new EditText(getActivity());
                //Adding the add task layout to the AlertDialog builder
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Add a Task")
                        .setView(subView)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //realm = Realm.getDefaultInstance();
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                                                  @Override
                                                                  public void execute(Realm realm) {
                                                                      final String uuID = UUID.randomUUID().toString();
                                                                      realm.createObject(SubGoalModel.class, uuID)
                                                                              .setName(String.valueOf(addTaskName.getText()));
                                                                      SubGoalModel sub = realm.where(SubGoalModel.class).equalTo("id", uuID).findFirst();
                                                                      GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", goalUUID).findFirst();
                                                                      goalModel.getSubgoals().add(sub);
                                                                      sub.setGoal(goalModel);
                                                                      sub.setDueDate(addTaskDueDate.getText().toString());
                                                                      Log.d("GOALS", "added subgoal ");

                                                                  }
                                                              },
                                        new Realm.Transaction.OnSuccess() {
                                            @Override
                                            public void onSuccess() {
                                                Log.d("GOALS", "onSuccess: ");


                                            }
                                        }, new Realm.Transaction.OnError() {
                                            @Override
                                            public void onError(Throwable error) {
                                                Log.d("GOALS", "onError: ");
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

            }
        });


        //recycler view of all the sub goals and child sub goals
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_task_list);
        subgoalsForThisGoal = realm.where(SubGoalModel.class).equalTo("goal.id", goalUUID).findAll();

        subgoalsForThisGoal.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<SubGoalModel>>() {
            @Override
            public void onChange(RealmResults<SubGoalModel> persons, OrderedCollectionChangeSet
                    changeset) {

                adapter = new SubGoalAdapter(persons, "id");

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                Log.d("GOALS", "onChange!!!!!!!!!!: " + persons.first().getName() + "     " + persons.first().getDone());


            }
        });

        adapter = new SubGoalAdapter(subgoalsForThisGoal, "id");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("GOALS", "onCreate real data  : " + subgoalsForThisGoal);


        //expanding and collapsing each subgoal or child sub goal
        adapter.setExpandCollapseListener(new RealmExpandableRecyclerAdapter.ExpandCollapseListener() {
            @UiThread
            @Override
            public void onParentExpanded(int parentPosition) {
                SubGoalModel expandedRecipe = adapter.getItem(parentPosition);

                String toastMsg = getResources().getString(R.string.expanded, expandedRecipe.getName());
//                Toast.makeText(AddGoal.this,
//                        toastMsg,
//                        Toast.LENGTH_SHORT)
//                        .show();
            }

            @UiThread
            @Override
            public void onParentCollapsed(int parentPosition) {
                SubGoalModel collapsedRecipe = adapter.getItem(parentPosition);

                String toastMsg = getResources().getString(R.string.collapsed, collapsedRecipe.getName());
//                Toast.makeText(AddGoal.this,
//                        toastMsg,
//                        Toast.LENGTH_SHORT)
//                        .show();
            }
        });


    }



}