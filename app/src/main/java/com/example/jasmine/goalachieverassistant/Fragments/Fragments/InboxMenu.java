package com.example.jasmine.goalachieverassistant.Fragments.Fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.jasmine.goalachieverassistant.Fragments.Adapters.AllTasksRecyclerAdapter;
import com.example.jasmine.goalachieverassistant.Fragments.Adapters.CustomSpinnerAdapter;
import com.example.jasmine.goalachieverassistant.MainActivity;
import com.example.jasmine.goalachieverassistant.Models.TaskModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.RecyclerviewExpandedItem.adapter.SubGoalAdapter;

import java.util.Arrays;
import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmExpandableRecyclerAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class InboxMenu extends Fragment {
//inbox

    private Realm realm;
    //private RecyclerView recyclerView;
    SubGoalAdapter adapter;
    RealmResults<TaskModel> tasksForThisGoal;
    RecyclerView recyclerView;
    private int mScrollY;


    public InboxMenu() {
        // Required empty public constructor
    }



//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_inbox_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("TasKappa");

        Log.d("GOALS", "in View created");

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        ((MainActivity)getActivity()).setToolbar(toolbar);

        String[] filterGoalsList;
        filterGoalsList= getResources().getStringArray(R.array.inboxfilter);
        NavigationView navigationView= getView().findViewById(R.id.nav_view);
        final List<String> mGoalFilterArrayList = Arrays.asList(filterGoalsList);

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter (getContext(),android.R.layout.simple_spinner_item, mGoalFilterArrayList);
        customSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner mNavigationSpinner = (Spinner) getView().findViewById(R.id.spinner_main);
        mNavigationSpinner.setAdapter(customSpinnerAdapter);

//        mNavigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String itemSelected = parent.getItemAtPosition(position).toString();
//                Log.d("GOALS", "onItemSelected: in toolbar for Goals. Item selected is " + itemSelected);
//                String[] filterTaskList;
//                filterTaskList = getResources().getStringArray(R.array.inboxfilter);
//
//
//                if (itemSelected.equals(filterTaskList[0])) {
//                    //default "All goals" sorts the goals with due dates ascending and puts the empty due dates at bottom
//
//                    realm = Realm.getDefaultInstance();
//                    RealmResults<TaskModel> tasks =realm.where(TaskModel.class).equalTo("isGoal",false).findAllSorted(
//                            new String[] {"dueDateNotEmpty", "dueDate"},
//                            new Sort[] {Sort.DESCENDING, Sort.ASCENDING });
//
//                    adapter.updateData(tasks);
//
//                } else if (itemSelected.equals(filterTaskList[1])) {
//                    //By Name Ascending
//                    adapter.filterAndSortList("taskCategory.name", getResources().getString(R.string.category_Inbox), null, null, true, Sort.ASCENDING, "name") ;
//
//                    //adapter.filterTaskListContainsTextAndReturnsSortedList("taskCategory", io.realm.Sort.ASCENDING,getResources().getString(R.string.category_Inbox),true,"name");
//
//                } else if (itemSelected.equals(filterTaskList[2])) {
//                    //No Due Date
//                    adapter.filterAndSortList("taskCategory.name", getResources().getString(R.string.category_Inbox), "dueDate", null, true, Sort.ASCENDING, "name") ;
//
//                } else {
//                    Log.e("ERROR:", "onItemSelected: valid choice not selected");
//
//                }
//                // realm.close();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
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
        // recyclerView = (RecyclerView)getView().findViewById(R.id.goal_list);
         recyclerView = (RecyclerView) view.findViewById(R.id.goal_list);
        final String inbox = getContext().getResources().getString(R.string.category_Inbox);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //recyclerView.setLayoutManager(linearLayoutManager);

//        realm = Realm.getDefaultInstance();
//        RealmResults<TaskModel> tasks =realm.where(TaskModel.class).equalTo("taskCategory.name", getResources().getString(R.string.category_Inbox)).findAllSortedAsync(
//                new String[] {"dueDateNotEmpty", "dueDate"},
//                new Sort[] { Sort.DESCENDING, Sort.ASCENDING });
//
//
//        final AllTasksRecyclerAdapter adapter = new AllTasksRecyclerAdapter(getActivity(), tasks, true);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);


        //recycler view of all the sub goals and child sub goals

        //  tasksForThisGoal = realm.where(SubGoalModel.class).equalTo("goal.id", goalUUID).findAllSorted("name", Sort.ASCENDING);
        realm = Realm.getDefaultInstance();
        final String[] filterTaskList = getResources().getStringArray(R.array.inboxfilter);

        tasksForThisGoal =realm.where(TaskModel.class)
                .equalTo("isGoal",false)
                .isNull("parentGoalId")
                .isNull("parentTaskId")
                .findAllSorted(
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
                Log.d("GOALS", "in change listener inbox ");

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));




        //expanding and collapsing each subgoal or child sub goal
        adapter.setExpandCollapseListener(new RealmExpandableRecyclerAdapter.ExpandCollapseListener() {
            @UiThread
            @Override
            public void onParentExpanded(int parentPosition) {
                TaskModel expandedTask = adapter.getItem(parentPosition);

                String toastMsg = getResources().getString(R.string.expanded, expandedTask.getName());
//                Toast.makeText(AddGoal.this,
//                        toastMsg,
//                        Toast.LENGTH_SHORT)
//                        .show();
            }

            @UiThread
            @Override
            public void onParentCollapsed(int parentPosition) {
                TaskModel collapsedTask = adapter.getItem(parentPosition);

                String toastMsg = getResources().getString(R.string.collapsed, collapsedTask.getName());
//                Toast.makeText(AddGoal.this,
//                        toastMsg,
//                        Toast.LENGTH_SHORT)
//                        .show();
            }


        });




        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomBottomSheetDialogFragment bottomSheetDialogFragment = CustomBottomSheetDialogFragment.newInstance("",false, "",false,getResources().getString(R.string.category_Inbox));
                bottomSheetDialogFragment.show(getFragmentManager(),"BottomSheet");

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
