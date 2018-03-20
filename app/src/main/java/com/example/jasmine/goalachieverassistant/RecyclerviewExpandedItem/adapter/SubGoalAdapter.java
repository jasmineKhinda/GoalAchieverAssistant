package com.example.jasmine.goalachieverassistant.RecyclerviewExpandedItem.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jasmine.goalachieverassistant.EditGoalActivity;
import com.example.jasmine.goalachieverassistant.EditTaskActivity;
import com.example.jasmine.goalachieverassistant.Models.ChildSubGoalModel;
import com.example.jasmine.goalachieverassistant.Models.TaskModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.Models.SubGoalModel;
import com.example.jasmine.goalachieverassistant.RecyclerviewExpandedItem.viewHolders.ChildSubGoalViewHolder;
import com.example.jasmine.goalachieverassistant.RecyclerviewExpandedItem.viewHolders.SubGoalViewHolder;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmExpandableRecyclerAdapter;
import io.realm.RealmExpandableSearchRecyclerAdapter;

/**
 * Created by jasmine on 18/01/18.
 */

public class SubGoalAdapter extends RealmExpandableRecyclerAdapter<TaskModel, TaskModel, SubGoalViewHolder, ChildSubGoalViewHolder> {


    private List<TaskModel> recipeList;

    public SubGoalAdapter(@NonNull OrderedRealmCollection<TaskModel> taskList, @NonNull String filterKey) {
        super(taskList);
        this.recipeList = taskList;
    }

    @UiThread
    @NonNull
    @Override
    public SubGoalViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View recipeView;
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



}
