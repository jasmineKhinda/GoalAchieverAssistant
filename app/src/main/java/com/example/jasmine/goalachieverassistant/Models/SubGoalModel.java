package com.example.jasmine.goalachieverassistant.Models;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Date;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

import io.realm.RealmList;
import io.realm.model.Parent;

/**
 * Created by jasmine on 17/01/18.
 */

@RealmClass
public class SubGoalModel implements RealmModel, Parent<ChildSubGoalModel> {
    @Required
    @PrimaryKey
    private String id;
    @Required
    private String name;
    private long timeStamp;
    private Date dueDate;
    private String reason;
    private boolean done;
    private boolean isExpand = false;
    GoalModel goal;
    private String time;
    private int labelColor=0;
    private boolean expanded=false;
    private boolean dueDateNotEmpty;

    private RealmList<ChildSubGoalModel> childSubgoals;




    /**
     * Getter for the list of this parent's child items.
     * <p>
     * If list is empty, the parent has no children.
     *
     * @return A {@link RealmList} of the children of this {@link Parent}
     */
    public RealmList<ChildSubGoalModel> getChildList(){
        return childSubgoals;
    }

    public void setChildSubgoals(RealmList<ChildSubGoalModel> childSubgoals) {
        this.childSubgoals = childSubgoals;
    }


    public SubGoalModel(){

    }
    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    public GoalModel getGoal() {
        return goal;
    }

    public void setGoal(GoalModel goal) {
        this.goal = goal;
    }

    @Nullable
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate)
    {
        this.dueDate = dueDate;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean getDone() {
        return done;
    }


    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }



    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public boolean isExpanded() {
        return expanded;
    }

    public int getChildSubGoalCount() {
        return childSubgoals.size();
    }

    public int getChildSubgoalsComplete() {
        int subGoalsCompleteCount=0;
        for ( ChildSubGoalModel r: childSubgoals) {
            if (r.getDone()){
                subGoalsCompleteCount++;
            }
        }
        return subGoalsCompleteCount;
    }

    public ChildSubGoalModel getChildSubGoal(int position) {
            return childSubgoals.get(position);
    }


    public void setDone(boolean isDone) {
//        for (int i = 0, size = childSubgoals.size(); i < size; i++) {
//            ChildSubGoalModel ingredient = childSubgoals.get(i);
//            ingredient.setDone(isDone);
//        }
        this.done = isDone;
    }


    public boolean getDueDateNotEmpty() {
        return this.dueDateNotEmpty;

    }

    public void setDueDateNotEmpty(@Nullable Date dueDate) {
        if(null==dueDate){
            this.dueDateNotEmpty =false;

            final String TAG = "GOALS " + SubGoalModel.class.getSimpleName();
            Log.d(TAG, "setDueDateIsEmpty: null");
        }else{
            this.dueDateNotEmpty =true;

            final String TAG = "GOALS " + SubGoalModel.class.getSimpleName();
            Log.d(TAG, "setDueDateIsEmpty: GOALS not null");
        }
    }


}
