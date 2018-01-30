package com.example.jasmine.goalachieverassistant;

import io.realm.RealmModel;
import io.realm.RealmObject;
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
    private String dueDate;
    private boolean done;
    private boolean isExpand = false;
    GoalModel goal;
    private boolean expanded;

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

    public boolean getDone() {
        return done;
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



}
