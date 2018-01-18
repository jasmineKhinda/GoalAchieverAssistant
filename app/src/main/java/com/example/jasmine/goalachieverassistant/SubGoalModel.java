package com.example.jasmine.goalachieverassistant;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import io.realm.RealmList;
import io.realm.model.Parent;

/**
 * Created by jasmine on 17/01/18.
 */

public class SubGoalModel implements RealmModel, Parent {
    @Required
    @PrimaryKey
    private String id;
    @Required
    private String name;
    private long timeStamp;
    private String dueDate;
    private boolean done;
    private boolean isExpand = false;
    GoalModel goal;
    private RealmList<ChildSubGoalModel> childSubgoals;



    public RealmList<ChildSubGoalModel> getChildSubgoals() {
        return childSubgoals;
    }

    public void setChildSubgoals(RealmList<ChildSubGoalModel> childSubgoals) {
        this.childSubgoals = childSubgoals;
    }


    public SubGoalModel(){

    }

    public GoalModel getGoal() {
        return goal;
    }

    public void setGoal(GoalModel goal) {
        this.goal = goal;
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

    /**
     * Getter used to determine if this {@link Parent}'s
     * {@link android.view.View} should show up initially as expanded.
     *
     * @return true if expanded, false if not
     */
    public boolean isExpanded(){
        return isExpand;
    }
}
