package com.example.jasmine.goalachieverassistant.Models;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.jasmine.goalachieverassistant.XoldClassesToDeleteAfterTesting.SubGoalModel;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;
import io.realm.model.Parent;

/**
 * Created by jasmine on 19/03/18.
 */

@RealmClass
public class TaskModel  extends RealmObject implements RealmModel, Parent<TaskModel> {
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
    private boolean isGoal=false;
    @Nullable
    private String parentTaskId =null;
    @Nullable
    private String parentGoalId =null;
    private String time;
    private int labelColor=0;
    private boolean expanded=false;
    private boolean dueDateNotEmpty;
    ListCategory taskCategory;
    private RealmList<TaskModel> subTasks;
    private RealmList<TaskModel> tasks;


    public ListCategory getTaskCategory() {
        return taskCategory;
    }

    //only set for project Tasks, and Parent Tasks. For Subtasks, do not set a category. The category
    //for a subtask will be inherited from the parent task.

    /**
     *
     * @param goal
     */
    public void setTaskCategory(ListCategory goal) {
        this.taskCategory = goal;
    }



    public RealmList<TaskModel> getTasks() {
        return tasks;
    }



    public TaskModel(){

    }
    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
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


    /**
     * Getter for the list of this parent's child items.
     * <p>
     * If list is empty, the parent has no children.
     *
     * @return A {@link RealmList} of the children of this {@link Parent}
     */
    public RealmList<TaskModel> getChildList(){
        return subTasks;
    }

    public void setChildSubgoals(RealmList<TaskModel> subTaskList) {
        this.subTasks = subTaskList;
    }

    /**
     * return the total number of subtasks (belonging to a Task)
     * @return
     */
    public int getSubTaskCount() {
        return subTasks.size();
    }

    public int getTotalSubTaskComplete() {
        int subTaskCompleteCount=0;
        for ( TaskModel r: subTasks) {
            if (r.getDone()){
                subTaskCompleteCount++;
            }
        }
        return subTaskCompleteCount;
    }

    public TaskModel getSubTask(int position) {
        return subTasks.get(position);
    }


    /**
     * return the total number of tasks (belonging to Goal)
     * @return
     */
    public int getTaskCount() {
        return tasks.size();
    }

    public int getTotalTaskComplete() {
        int taskCompleteCount=0;
        for ( TaskModel r: tasks) {
            if (r.getDone()){
                taskCompleteCount++;
            }
        }
        return taskCompleteCount;
    }

    public TaskModel getTask(int position) {
        return tasks.get(position);
    }


    /**
     * if the task belongs to a goal set the goal id as reference
     */
    public String getParentGoalId() {
        return parentGoalId;
    }
    public void setParentGoalId(String goal) {
        this.parentGoalId = goal;
    }



    /**
     * if the task is a subtask, set the parent Task id as reference
     * @return
     */
    public String getParentTaskId() {
        return parentTaskId;
    }
    public void setParentTaskId(String task) {
        this.parentTaskId = task;
    }


    /**
     * if the task is a goal set the isGoal variable to true
     * @param isGoal
     */
    public void setAsGoal (boolean isGoal){
        this.isGoal = isGoal;
    }

    public boolean isGoal(){
        return isGoal;
    }

}
