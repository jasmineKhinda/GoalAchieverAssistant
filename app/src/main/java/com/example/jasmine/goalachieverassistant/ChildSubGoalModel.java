package com.example.jasmine.goalachieverassistant;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.model.Child;

/**
 * Created by jasmine on 17/01/18.
 */

public class ChildSubGoalModel implements RealmModel,Child {
    @Required
    @PrimaryKey
    private String id;
    @Required
    private String name;
    private long timeStamp;
    private String dueDate;
    private boolean done;
    private boolean isExpand;
    SubGoalModel subGoal;

    public ChildSubGoalModel() {

    }

    public SubGoalModel getSubGoal() {
        return subGoal;
    }

    public void setGoal(SubGoalModel goal) {
        this.subGoal = goal;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}