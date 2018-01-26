package com.example.jasmine.goalachieverassistant.recyclerview.viewHolders;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jasmine.goalachieverassistant.ChildSubGoalModel;
import com.example.jasmine.goalachieverassistant.R;

import io.realm.ChildViewHolder;
import io.realm.Realm;

/**
 * Created by jasmine on 18/01/18.
 */

public class ChildSubGoalViewHolder extends ChildViewHolder implements View.OnClickListener {

    private TextView childSubGoalTextView;
    private ChildSubGoalModel childSubGoal;

    public ChildSubGoalViewHolder(@NonNull View itemView) {
        super(itemView);
        childSubGoalTextView = (TextView) itemView.findViewById(R.id.child_subGoal_textview);
        itemView.setOnClickListener(this);
    }

    public void bind(@NonNull ChildSubGoalModel childSubGoal) {
        this.childSubGoal = childSubGoal;
        childSubGoalTextView.setText(childSubGoal.getName());
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
