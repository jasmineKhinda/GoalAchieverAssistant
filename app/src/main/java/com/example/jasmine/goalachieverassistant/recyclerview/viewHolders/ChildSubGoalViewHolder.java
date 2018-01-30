package com.example.jasmine.goalachieverassistant.recyclerview.viewHolders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.jasmine.goalachieverassistant.ChildSubGoalModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.SubGoalModel;

import java.util.UUID;

import io.realm.ChildViewHolder;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jasmine on 18/01/18.
 */

public class ChildSubGoalViewHolder extends ChildViewHolder implements View.OnClickListener {

    private TextView childSubGoalTextView;
    public ImageButton buttonViewOption;
    public TextView dueDate;
    private CheckBox isTaskDone;
    private ChildSubGoalModel childSubGoal;
    Realm realm;

    public ChildSubGoalViewHolder(@NonNull View itemView) {
        super(itemView);
        childSubGoalTextView = (TextView) itemView.findViewById(R.id.child_subGoal_textview);
        buttonViewOption = (ImageButton) itemView.findViewById(R.id.overFlow);
        dueDate = (TextView) itemView.findViewById(R.id.childDueDueDate);
        isTaskDone = (CheckBox) itemView.findViewById(R.id.task_item_done);
        itemView.setOnClickListener(this);
    }

    public void bind(@NonNull ChildSubGoalModel r) {
       this.childSubGoal = r;
        childSubGoalTextView.setText(childSubGoal.getName());

        final String taskId = childSubGoal.getId();

        //if no due date display the default "No Due Date" from layout file
        if(!childSubGoal.getDueDate().isEmpty()){
            dueDate.setText(childSubGoal.getDueDate());
        }



        isTaskDone.setChecked(childSubGoal.getDone());


        Log.d("GOALS", "true or false? " + childSubGoal.getDone());


        isTaskDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (childSubGoal.getDone()) {

                    CheckBox checkBox = (CheckBox) v.findViewById(R.id.task_item_done);
                    checkBox.setChecked(!checkBox.isChecked());
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            childSubGoal.setDone(false);
                            Log.d("GOALS", "IN TRUEEEEEE");
                        }
                    });
                    realm.close();

                } else {

                    CheckBox checkBox = (CheckBox) v.findViewById(R.id.task_item_done);
                    checkBox.setChecked(!checkBox.isChecked());

                    Realm realm2 = Realm.getDefaultInstance();
                    realm2.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            childSubGoal.setDone(true);
                            Log.d("GOALS", "IN FALSE");
                        }
                    });
                    realm2.close();
                }
            }
        });


        buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_menu_subtask, popup.getMenu());


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final int position = getAdapterPosition();
                        //  final GoalModel mTaskModel = getData().get(position);
                        // final String id = mTaskModel.getId().toString();


                        switch (item.getItemId()) {
                            case R.id.delete_task:
                                realm = Realm.getDefaultInstance();
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {

                                        RealmResults<ChildSubGoalModel> childSubGoalModel = realm.where(ChildSubGoalModel.class).equalTo("id", taskId).findAll();
                                        childSubGoalModel.deleteFirstFromRealm();
                                        Log.d("GOALS", "deleted item? ");
                                        // realm.close();
                                    }

                                });
                                realm.close();
                                break;

                        }
                        return false;

                    }
                });
                //displaying the popup
                popup.show();

            }
        });


//        if (childSubGoal.getDone()) {
//            itemView.setBackgroundColor(Color.YELLOW);
//        } else {
//            itemView.setBackgroundColor(Color.WHITE);
//        }
    }

    @Override
    public void onClick(View v) {
        Log.i("Realm", "onClick: childSubGoal");
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm r) {
                childSubGoal.setDone(!childSubGoal.getDone());
            }
        });
        realm.close();
    }

}
