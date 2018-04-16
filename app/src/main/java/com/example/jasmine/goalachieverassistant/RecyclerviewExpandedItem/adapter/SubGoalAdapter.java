package com.example.jasmine.goalachieverassistant.RecyclerviewExpandedItem.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.jasmine.goalachieverassistant.Models.TaskModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.RecyclerviewExpandedItem.viewHolders.ChildSubGoalViewHolder;
import com.example.jasmine.goalachieverassistant.RecyclerviewExpandedItem.viewHolders.SubGoalViewHolder;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmExpandableRecyclerAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by jasmine on 18/01/18.
 */

public class SubGoalAdapter extends RealmExpandableRecyclerAdapter<TaskModel, TaskModel, SubGoalViewHolder, ChildSubGoalViewHolder> {


    private List<TaskModel> recipeList;
    private View itemView;
    Realm realm;










    View recipeView;

    public SubGoalAdapter(@NonNull OrderedRealmCollection<TaskModel> taskList, @NonNull String filterKey) {
        super(taskList);
        this.recipeList = taskList;
    }

    @UiThread
    @NonNull
    @Override
    public SubGoalViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        //View recipeView;

        LayoutInflater mInflater = LayoutInflater.from(parentViewGroup.getContext());

        recipeView = mInflater.inflate(R.layout.sub_goal_row, parentViewGroup, false);
        Log.d("GOALS", "onCreateParentViewHolder 1");

        return new SubGoalViewHolder(recipeView);

    }

    @UiThread
    @NonNull
    @Override
    public ChildSubGoalViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View ingredientView;
        LayoutInflater mInflater = LayoutInflater.from(childViewGroup.getContext());

        ingredientView = mInflater.inflate(R.layout.child_sub_goal_row, childViewGroup, false);
        Log.d("GOALS", "onCreateParentViewHolder 2 " +childViewGroup + "    " + viewType);

        return new ChildSubGoalViewHolder(ingredientView);
    }

    @UiThread
    @Override
    public void onBindParentViewHolder(@NonNull SubGoalViewHolder recipeViewHolder, int parentPosition, @NonNull TaskModel recipe) {
        Log.d("GOALS", "onCreateParentViewHolder 3");
        recipeViewHolder.bind(recipe);

    }

    @UiThread
    @Override
    public void onBindChildViewHolder(@NonNull ChildSubGoalViewHolder ingredientViewHolder, int parentPosition, int childPosition, @NonNull TaskModel ingredient) {
        Log.d("GOALS", "onCreateParentViewHolder 4" + childPosition +"     ing"+ ingredient);
        ingredientViewHolder.bind(ingredient);


    }
    /**
     * filtering the list with multiple (2) fields then finally sorting
     * @param primaryFilterColumn : the name of the field to intially filter the list
     * @param primaryFilterValue : the value for "primaryFilterColumn" to filer the list
     * @param secondaryFilterColumn : the secondary field to filter the list with
     * @param secondaryFilterValue : search the "secondaryFilterColumn" column with the containing string
     * @param secondaryFilterValueDoesContain : true if "secondaryFilterValue" should be in column values, false otherwise
     * @param sortingField : field to which to sort by
     * @param sortOrder Ascending or descending order for the final sort
     */
    public void filterAndSortList(String primaryFilterColumn, String primaryFilterValue, @Nullable String secondaryFilterColumn, @Nullable String secondaryFilterValue, Boolean secondaryFilterValueDoesContain, Sort sortOrder, String sortingField) {

        try {

             realm = Realm.getDefaultInstance();
            OrderedRealmCollection<TaskModel> sorting = realm.where(TaskModel.class).equalTo(primaryFilterColumn, primaryFilterValue).findAll();

            Log.d("GOALS", "sort attributes  " + secondaryFilterColumn + "   " + sortOrder);
            if (null == secondaryFilterColumn && null != sortingField) {
                //no secondary sort is needed, just filter the primary filtered list "sorting"
                sorting = sorting.sort(sortingField, sortOrder);

            } else if (null == secondaryFilterValue && false == secondaryFilterValueDoesContain) {
                //filtering "sorting" list where final list contains all entries where the value of "secondaryFilterColumn" is NOT null, then finally sort list
                sorting = sorting.where().not().isNull(secondaryFilterColumn).findAllSorted(sortingField, sortOrder);
                Log.d("GOALS", "onItemSelected:  sort 1");
            } else if (null == secondaryFilterValue && true == secondaryFilterValueDoesContain) {
                //filtering "sorting" list where final list contains all entries where the value of "secondaryFilterColumn" is null, then finally sort list
                Log.d("GOALS", "onItemSelected:  sort 2");
                sorting = sorting.where().isNull(secondaryFilterColumn).findAllSorted(sortingField, sortOrder);
            } else if (null != secondaryFilterValue && false == secondaryFilterValueDoesContain) {
                //filtering "sorting" list where final list contains all entries where the value of "secondaryFilterColumn" is NOT equal to "secondaryFilterValue", then finally sort list
                Log.d("GOALS", "onItemSelected:  sort 3");
                sorting = sorting.where().notEqualTo(secondaryFilterColumn, secondaryFilterValue).findAllSorted(sortingField, sortOrder);
            } else if (null != secondaryFilterValue && true == secondaryFilterValueDoesContain) {
                //filtering "sorting" list where final list contains all entries where the value of "secondaryFilterColumn" is "secondaryFilterValue", then finally sort list
                Log.d("GOALS", "onItemSelected:  sort 4");
                sorting = sorting.where().equalTo(secondaryFilterColumn, secondaryFilterValue).findAllSorted(sortingField, sortOrder);

            }
            updateData(sorting);


        } catch (Exception e) {
            Log.e("GOALS", "Not able to update the sorting of the task list, see stack trace" + e.toString());
            Toast toast = Toast.makeText(recipeView.getContext(), "Oops, something went wrong!", Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        } finally {
            realm.close();
        }
    }



}
