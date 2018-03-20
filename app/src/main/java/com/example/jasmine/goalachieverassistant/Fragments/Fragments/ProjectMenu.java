package com.example.jasmine.goalachieverassistant.Fragments.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jasmine.goalachieverassistant.Fragments.Adapters.CustomSpinnerAdapter;
import com.example.jasmine.goalachieverassistant.Fragments.Adapters.GoalRecyclerAdapter;
import com.example.jasmine.goalachieverassistant.GoalListActivity;
import com.example.jasmine.goalachieverassistant.Models.ListCategory;
import com.example.jasmine.goalachieverassistant.Models.TaskModel;
import com.example.jasmine.goalachieverassistant.R;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ProjectMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectMenu extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Realm realm;
    private RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public ProjectMenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectMenu newInstance(String param1, String param2) {
        ProjectMenu fragment = new ProjectMenu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_project_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Project?");
        // Inflate the layout for this fragment

       // super.onCreate(savedInstanceState);
       // setContentView(R.layout.nav_goal_list);
      //  mDrawerLayout = getView().findViewById(R.id.drawer_layout);


        recyclerView = (RecyclerView)getView().findViewById(R.id.goal_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        realm = Realm.getDefaultInstance();


        RealmResults<TaskModel> tasks =realm.where(TaskModel.class).equalTo("isGoal", true).findAllSortedAsync(
                new String[] {"dueDateNotEmpty", "dueDate"},
                new Sort[] { Sort.DESCENDING, Sort.ASCENDING });

        //TODO: filter only goals here
        final GoalRecyclerAdapter adapter = new GoalRecyclerAdapter(getActivity(), tasks, true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        String[] filterGoalsList;
        filterGoalsList= getResources().getStringArray(R.array.goalfilter);
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





        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomBottomSheetDialogFragment bottomSheetDialogFragment = CustomBottomSheetDialogFragment.newInstance("",false, "",true);
                bottomSheetDialogFragment.show(getFragmentManager(),"BottomSheet");


            }
        });




    }



}
