package com.example.jasmine.goalachieverassistant.FragmentIdea.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.jasmine.goalachieverassistant.AddGoal;
import com.example.jasmine.goalachieverassistant.GoalModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.SubGoalModel;
import com.example.jasmine.goalachieverassistant.recyclerview.adapter.SubGoalAdapter;

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
public class GoalTasksFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String goalUUID;
    private Realm realm;
    private SubGoalAdapter adapter;
    RealmResults<SubGoalModel> subgoalsForThisGoal;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private static final String GOAL_UUID = "GoalUUID";



    public GoalTasksFragment() {
        // Required empty public constructor
    }


    public static GoalTasksFragment newInstance(String param3) {
        GoalTasksFragment frag = new GoalTasksFragment();
        Bundle args = new Bundle();
        args.putString(GOAL_UUID, param3);
        Log.d("GOALS", "GOAL_UUID "+  args.get(GOAL_UUID).toString()+ "  param3 "+ param3);
        frag.setArguments(args);
        return frag;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goal_tasks, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goalUUID = getArguments().getString(GOAL_UUID);
        Log.d("GOALS", "onViewCreated: goal UUID"+ goalUUID);

        realm = Realm.getDefaultInstance();
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText taskEditText = new EditText(getActivity());
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Add Task")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //realm = Realm.getDefaultInstance();
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                                                  @Override
                                                                  public void execute(Realm realm) {
                                                                      final String uuID = UUID.randomUUID().toString();
                                                                      realm.createObject(SubGoalModel.class, uuID)
                                                                              .setName(String.valueOf(taskEditText.getText()));
                                                                      SubGoalModel sub = realm.where(SubGoalModel.class).equalTo("id", uuID).findFirst();
                                                                      GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", goalUUID).findFirst();
                                                                      goalModel.getSubgoals().add(sub);
                                                                      sub.setGoal(goalModel);
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
                Log.d("GOALS", "onChange!!!!!!!!!!: ");


            }
        });

        adapter = new SubGoalAdapter(subgoalsForThisGoal, "id");
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
