package com.example.jasmine.goalachieverassistant.Models;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.jasmine.goalachieverassistant.Models.SubGoalModel;

import java.util.Date;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;
import io.realm.model.Child;

/**
 * Created by jasmine on 17/01/18.
 */

@RealmClass
public class ChildSubGoalModel implements RealmModel,Child {
    @Required
    @PrimaryKey
    private String id;
    @Required
    private String name;
    private long timeStamp;
    private Date dueDate;
    private boolean done;
    private boolean isExpand;
    SubGoalModel subGoal;
    private boolean dueDateNotEmpty;

    public ChildSubGoalModel() {

    }

    public SubGoalModel getSubGoal() {
        return subGoal;
    }

    public void setSubGoal(SubGoalModel goal) {
        this.subGoal = goal;
    }

    @Nullable
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate)
    {
        this.dueDate = dueDate;
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

    public void setDone(boolean done) {
        this.done = done;
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