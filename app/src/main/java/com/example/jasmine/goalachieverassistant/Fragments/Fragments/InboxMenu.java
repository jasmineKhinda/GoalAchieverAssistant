package com.example.jasmine.goalachieverassistant.Fragments.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class InboxMenu extends Fragment {
//inbox

    private Realm realm;
    private RecyclerView recyclerView;


    public InboxMenu() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

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


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        ((MainActivity)getActivity()).setToolbar(toolbar);


        recyclerView = (RecyclerView)getView().findViewById(R.id.goal_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        realm = Realm.getDefaultInstance();
        RealmResults<TaskModel> tasks =realm.where(TaskModel.class).equalTo("taskCategory.name", getResources().getString(R.string.category_Inbox)).findAllSortedAsync(
                new String[] {"dueDateNotEmpty", "dueDate"},
                new Sort[] { Sort.DESCENDING, Sort.ASCENDING });


        final AllTasksRecyclerAdapter adapter = new AllTasksRecyclerAdapter(getActivity(), tasks, true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        String[] filterGoalsList;
        filterGoalsList= getResources().getStringArray(R.array.inboxfilter);
        NavigationView navigationView= getView().findViewById(R.id.nav_view);
        List<String> mGoalFilterArrayList = Arrays.asList(filterGoalsList);

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter (getContext(),android.R.layout.simple_spinner_item, mGoalFilterArrayList);
        customSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner mNavigationSpinner = (Spinner) getView().findViewById(R.id.spinner_main);
        mNavigationSpinner.setAdapter(customSpinnerAdapter);

        mNavigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemSelected = parent.getItemAtPosition(position).toString();
                Log.d("GOALS", "onItemSelected: in toolbar for Goals. Item selected is " + itemSelected);
                String[] filterTaskList;
                filterTaskList = getResources().getStringArray(R.array.inboxfilter);


                if (itemSelected.equals(filterTaskList[0])) {
                    //default "All goals" sorts the goals with due dates ascending and puts the empty due dates at bottom

                    realm = Realm.getDefaultInstance();
                    RealmResults<TaskModel> tasks =realm.where(TaskModel.class).equalTo("taskCategory.name", getResources().getString(R.string.category_Inbox)).findAllSortedAsync(
                            new String[] {"dueDateNotEmpty", "dueDate"},
                            new Sort[] { Sort.DESCENDING, Sort.ASCENDING });

                    adapter.updateData(tasks);

                } else if (itemSelected.equals(filterTaskList[1])) {
                    //By Name Ascending
                    adapter.filterAndSortList("taskCategory.name", getResources().getString(R.string.category_Inbox), null, null, true, Sort.ASCENDING, "name") ;

                    //adapter.filterTaskListContainsTextAndReturnsSortedList("taskCategory", io.realm.Sort.ASCENDING,getResources().getString(R.string.category_Inbox),true,"name");

                } else if (itemSelected.equals(filterTaskList[2])) {
                    //No Due Date
                    adapter.filterAndSortList("taskCategory.name", getResources().getString(R.string.category_Inbox), "dueDate", null, true, Sort.ASCENDING, "name") ;

                } else {
                    Log.e("ERROR:", "onItemSelected: valid choice not selected");

                }
                // realm.close();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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


}
