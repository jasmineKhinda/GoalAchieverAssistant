package com.example.jasmine.goalachieverassistant.XoldClassesToDeleteAfterTesting;


import android.support.annotation.Nullable;
import android.util.Log;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import java.util.Date;


public class GoalModel  implements RealmModel {

//        extends RealmObject {
    @Required
    @PrimaryKey
    private String id;
//    @Nullable
//    private String name;
//    private String time;
//    private String key;
//    private String reason;
//    private String type;
//    private String priority;
//    private String dateTime;
//    private long timeStamp;
//    private Date dueDate;
//    private int labelColor=0;
//    private boolean dueDateNotEmpty;
//
//    public int getLabelColor() {
//        return labelColor;
//    }
//
//    public void setLabelColor(int labelColor) {
//        this.labelColor = labelColor;
//    }
//
//    //   @LinkingObjects("goal")
//    private RealmList<SubGoalModel> subgoals;
//
//
//    public GoalModel(){
//
//    }
//
//    public RealmList<SubGoalModel> getSubgoals() {
//        return subgoals;
//    }
//
//
//    public int getSubgoalsComplete() {
//        int subGoalsCompleteCount=0;
//        for ( SubGoalModel r: subgoals) {
//            if (r.getDone()){
//                subGoalsCompleteCount++;
//            }
//        }
//        return subGoalsCompleteCount;
//    }
//
//
//
//    public void setSubgoals(RealmList<SubGoalModel> subgoals) {
//        this.subgoals = subgoals;
//    }
//
//    public Date getDueDate() {
//        return dueDate;
//    }
//
//    public void setDueDate(Date dueDate) {
//        this.dueDate = dueDate;
//
//
//    }
//
//    public void setDueDateNotEmpty(@Nullable Date dueDate) {
//        if(null==dueDate){
//            this.dueDateNotEmpty =false;
//
//            final String TAG = "GOALS " + SubGoalModel.class.getSimpleName();
//            Log.d(TAG, "setDueDateIsEmpty: null");
//        }else{
//            this.dueDateNotEmpty =true;
//
//            final String TAG = "GOALS " + SubGoalModel.class.getSimpleName();
//            Log.d(TAG, "setDueDateIsEmpty: GOALS not null");
//        }
//    }
//
//    public long getTimeStamp() {
//        return timeStamp;
//    }
//
//    public void setTimeStamp(long timeStamp) {
//        this.timeStamp = timeStamp;
//    }
//
//    public String getId() {
//        return id;
//    }
//
////    public void setId(String id) {
////        this.id = id;
////    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getPriority() {
//        return priority;
//    }
//
//    public void setPriority(String priority) {
//        this.priority = priority;
//    }
//
//    public GoalModel(String name, String time, String reason, String type, String priority){
//        this.name = name;
//        this.time = time;
//        //this.key = key;
//        this.reason = reason;
//        this.type = type;
//        this.priority = priority;
//    }
//
//    public String getKey() {
//        return key;
//    }
//
//    public void setKey(String key) {
//        this.key = key;
//    }
//
//    @Nullable
//    public String getName() {
//        return name;
//    }
//
//    public void setReason(String reason) {
//        this.reason = reason;
//    }
//
//    public String getReason() {
//        return reason;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getTime() {
//        return time;
//    }
//    public String getDateTime() {
//        return dateTime;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public int getSubGoalCount() {
//        return subgoals.size();
//    }
//
//
//    public boolean getDueDateNotEmpty() {
//        return this.dueDateNotEmpty;
//
//    }



}