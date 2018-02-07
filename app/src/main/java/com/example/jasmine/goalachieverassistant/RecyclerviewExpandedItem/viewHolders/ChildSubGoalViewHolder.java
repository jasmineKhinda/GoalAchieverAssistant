package com.example.jasmine.goalachieverassistant.RecyclerviewExpandedItem.viewHolders;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.jasmine.goalachieverassistant.Utilities;
import com.example.jasmine.goalachieverassistant.Models.ChildSubGoalModel;
import com.example.jasmine.goalachieverassistant.R;

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
        if(null !=childSubGoal.getDueDate()){
            String dateToDisplay = Utilities.parseDateForDisplay(childSubGoal.getDueDate());
            dueDate.setText(dateToDisplay);
        }else{
            dueDate.setText(R.string.no_due_date);
        }


        isTaskDone.setChecked(childSubGoal.getDone());
        if(childSubGoal.getDone()){
            childSubGoalTextView.setPaintFlags( childSubGoalTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            childSubGoalTextView.setAlpha(0.38f);

        }//if item is unchecked(not done) then set checkbox to false and un-strike through the textview
        else{
            childSubGoalTextView.setPaintFlags(childSubGoalTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }


        Log.d("GOALS", "true or false? " + childSubGoal.getDone());


        isTaskDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (childSubGoal.getDone()) {

//                    CheckBox checkBox = (CheckBox) v.findViewById(R.id.task_item_done);
//                    checkBox.setChecked(!checkBox.isChecked());


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

//                    CheckBox checkBox = (CheckBox) v.findViewById(R.id.task_item_done);
//                    checkBox.setChecked(!checkBox.isChecked());


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


    }

    @Override
    public void onClick(View v) {
        Log.i("Realm", "onColorSet: childSubGoal");
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
