package com.example.jasmine.goalachieverassistant.Fragments.Fragments;

import android.os.Bundle;
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

import com.example.jasmine.goalachieverassistant.Fragments.Adapters.SubTaskRecyclerAdapter;
import com.example.jasmine.goalachieverassistant.Models.ChildSubGoalModel;
import com.example.jasmine.goalachieverassistant.Models.SubGoalModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.RecyclerviewExpandedItem.adapter.SubGoalAdapter;
import com.example.jasmine.goalachieverassistant.Utilities;

import java.util.Date;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmExpandableRecyclerAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by jasmine on 15/02/18.
 */

public class TaskSubTaskListFragment extends Fragment implements DatePickerFragment.DatePickerFragmentListener{

    private String taskUUID;
    private Realm realm;
    private SubTaskRecyclerAdapter adapter;
    RealmResults<ChildSubGoalModel> subTasks;
    public EditText addTaskDueDate;
    Date dueDate;
    private static final String TASK_UUID = "GoalUUID";


    public TaskSubTaskListFragment() {
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


    public static TaskSubTaskListFragment newInstance(String param3) {
        TaskSubTaskListFragment frag = new TaskSubTaskListFragment();
        Bundle args = new Bundle();
        args.putString(TASK_UUID, param3);
        Log.d("GOALS", "GoalTasksFragment " + args.get(TASK_UUID).toString() + "  param3 " + param3);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("GOALS", "onCreateView: task ");
        View v = inflater.inflate(R.layout.fragment_task_list, container, false);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        taskUUID = getArguments().getString(TASK_UUID);
        Log.d("GOALS", "onViewCreated:  for TasksFragment goal UUID" + taskUUID);
        final DatePickerFragment fragment =DatePickerFragment.newInstance(this);

       // realm = Realm.getDefaultInstance();

        //recycler view of all the sub tasks
        Log.d("GOALS", "in TaskList Fragment setting up recycler adapter!!!");
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_task_list);
        //  subgoalsForThisGoal = realm.where(SubGoalModel.class).equalTo("goal.id", taskUUID).findAllSorted("name", Sort.ASCENDING);
        realm = Realm.getDefaultInstance();

//        SubGoalModel task = realm.where(SubGoalModel.class).equalTo("id", taskUUID).findFirst();
//        task.getChildList();
//
//        uytut


//TODO : add the sorting filter
        subTasks =realm.where(ChildSubGoalModel.class).equalTo("subGoal.id", taskUUID).
                findAllSortedAsync(
                        new String[] {"dueDateNotEmpty", "dueDate"},
                        new Sort[] { Sort.DESCENDING, Sort.ASCENDING });

        Log.d("GOALS", "any subtasks???" + subTasks.size());
        Log.d("GOALS", "task id " + taskUUID);
//        subTasks.addChangeListener(new RealmChangeListener<RealmResults<ChildSubGoalModel>>() {
//            @Override
//            public void onChange(RealmResults<ChildSubGoalModel> childSubGoalModels) {
//
//            }
//        });

        subTasks.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<ChildSubGoalModel>>() {
            @Override
            public void onChange(RealmResults<ChildSubGoalModel> persons, OrderedCollectionChangeSet
                    changeset) {

                adapter = new SubTaskRecyclerAdapter(getActivity(),persons,true);

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                Log.d("GOALS", "onChange Tasks Sub tasks!!!");

            }
        });

        adapter = new SubTaskRecyclerAdapter(getActivity(),subTasks,true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onStop() {

        Log.d("GOALS", "onStop: ");
        subTasks.removeAllChangeListeners();
        realm.close();
        super.onStop();

    }

}
