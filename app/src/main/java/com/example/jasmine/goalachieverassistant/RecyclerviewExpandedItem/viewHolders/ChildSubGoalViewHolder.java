package com.example.jasmine.goalachieverassistant.RecyclerviewExpandedItem.viewHolders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.jasmine.goalachieverassistant.EditSubTaskActivity;
import com.example.jasmine.goalachieverassistant.EditTaskActivity;
import com.example.jasmine.goalachieverassistant.Utilities;
import com.example.jasmine.goalachieverassistant.Models.ChildSubGoalModel;
import com.example.jasmine.goalachieverassistant.R;

import java.util.Calendar;
import java.util.Date;

import io.realm.ChildViewHolder;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jasmine on 18/01/18.
 */

public class ChildSubGoalViewHolder extends ChildViewHolder implements View.OnClickListener {

    private TextView childSubGoalTextView;
    private ImageButton buttonViewOption;
    public TextView dueDate;
    private CheckBox isTaskDone;
    private CardView card;
    private ImageView dueDateIcon;
    private ChildSubGoalModel childSubGoal;
    Realm realm;

    public ChildSubGoalViewHolder(@NonNull View itemView) {
        super(itemView);
        childSubGoalTextView = (TextView) itemView.findViewById(R.id.child_subGoal_textview);
        buttonViewOption = (ImageButton) itemView.findViewById(R.id.overFlow);
        dueDate = (TextView) itemView.findViewById(R.id.childDueDueDate);
        isTaskDone = (CheckBox) itemView.findViewById(R.id.task_item_done);
        dueDateIcon =(ImageView) itemView.findViewById(R.id.due_date_icon);
        itemView.setOnClickListener(this);
    }

    public void bind(@NonNull ChildSubGoalModel r) {
       this.childSubGoal = r;
        childSubGoalTextView.setText(childSubGoal.getName());

        final String taskId = childSubGoal.getId();


        card = (CardView) itemView.findViewById(R.id.card_view);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent viewTaskIntent = new Intent(view.getContext(), EditSubTaskActivity.class);
                viewTaskIntent.putExtra("EDIT_TASKUUID", childSubGoal.getId());
                viewTaskIntent.putExtra("EDIT_TASKNAME", childSubGoal.getName());
                Log.d("GOALS", "calling activity is1 "+view.getContext().getClass().getName()  );
                viewTaskIntent.putExtra("CALLING_ACTIVITY",view.getContext().getClass().getName() );
                view.getContext().startActivity(viewTaskIntent);
            }
        });



        //if no due date display the default "No Due Date" from layout file
//        if(null !=childSubGoal.getDueDate()){
//            String dateToDisplay = Utilities.parseDateForDisplay(childSubGoal.getDueDate());
//            dueDate.setText(dateToDisplay);
//        }else{
//            dueDate.setText(R.string.no_due_date);
//        }

        if(null!=childSubGoal.getDueDate()){

            Date currentTime = Calendar.getInstance().getTime();
            if(null!=childSubGoal.getDueDate()&& ((currentTime.getTime() - childSubGoal.getDueDate().getTime()))>0){
                dueDate.setTextColor(Color.RED);
            }else if (null!=childSubGoal.getDueDate()&& ((currentTime.getTime() - childSubGoal.getDueDate().getTime()))<0){
                Log.d("GOALS", "text colour: " +dueDate.getTextColors().getDefaultColor());
                dueDate.setTextColor(dueDate.getTextColors().getDefaultColor());
            }

            String dateToDisplay = Utilities.parseDateForDisplay(childSubGoal.getDueDate());
            dueDate.setVisibility(View.VISIBLE);
            dueDate.setText(dateToDisplay);
            dueDateIcon.setVisibility(View.VISIBLE);
        }else{
            dueDate.setVisibility(View.INVISIBLE);
            dueDateIcon.setVisibility(View.INVISIBLE);
        }


        isTaskDone.setChecked(childSubGoal.getDone());
        if(childSubGoal.getDone()){
            childSubGoalTextView.setPaintFlags( childSubGoalTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            childSubGoalTextView.setAlpha(0.38f);

        }//if item is unchecked(not done) then set checkbox to false and un-strike through the textview
        else{
            childSubGoalTextView.setPaintFlags(childSubGoalTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            childSubGoalTextView.setAlpha(1f);
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
