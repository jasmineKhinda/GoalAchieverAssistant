package com.example.jasmine.goalachieverassistant.XoldClassesToDeleteAfterTesting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.jasmine.goalachieverassistant.Models.SubGoalModel;
import com.example.jasmine.goalachieverassistant.Models.TaskModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.XoldClassesToDeleteAfterTesting.AddGoal;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by jasmine on 17/01/18.
 */

public class SubGoalListAdapter {

        //extends RealmBaseAdapter<TaskModel> implements ListAdapter {

//    private AddGoal activity;
//
//    private static class ViewHolder {
//        TextView taskName;
//        CheckBox isTaskDone;
//
//    }
//
//    SubGoalListAdapter(AddGoal activity, OrderedRealmCollection<TaskModel> data) {
//        super(data);
//        this.activity = activity;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final ViewHolder viewHolder;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.sub_goal_row, parent, false);
//            viewHolder = new ViewHolder();
//            viewHolder.taskName = (TextView) convertView.findViewById(R.id.child_subGoal_textview);
//            viewHolder.isTaskDone = (CheckBox) convertView.findViewById(R.id.task_item_done);
//            viewHolder.isTaskDone.setOnClickListener(listener);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//        if (adapterData != null) {
//            TaskModel task = adapterData.get(position);
//            viewHolder.taskName.setText(task.getName());
//            viewHolder.isTaskDone.setChecked(task.getDone());
//            viewHolder.isTaskDone.setTag(position);
//        }
//
//        return convertView;
//    }
//
//    private View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            int position = (Integer) view.getTag();
//            if (adapterData != null) {
//                TaskModel task = adapterData.get(position);
//                activity.changeTaskDone(task.getId());
//            }
//        }
//    };
}
