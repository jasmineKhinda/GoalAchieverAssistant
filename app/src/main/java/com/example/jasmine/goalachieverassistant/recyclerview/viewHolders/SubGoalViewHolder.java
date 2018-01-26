package com.example.jasmine.goalachieverassistant.recyclerview.viewHolders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.jasmine.goalachieverassistant.ChildSubGoalModel;
import com.example.jasmine.goalachieverassistant.GoalModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.SubGoalModel;

import java.util.UUID;

import io.realm.ParentViewHolder;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by jasmine on 18/01/18.
 */

public class SubGoalViewHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 45f;

    @NonNull
    private final ImageView arrowExpandImageView;
    private TextView subGoalTextView;
    private TextView starredIngredientCount;
    public ImageButton buttonViewOption;
    Realm realm;
 //   private TextView ingredientCount;

    private SubGoalModel subGoal;

    public SubGoalViewHolder(@NonNull View itemView) {
        super(itemView);
        subGoalTextView = (TextView) itemView.findViewById(R.id.subGoal_textview);
        arrowExpandImageView = (ImageView) itemView.findViewById(R.id.chevron_Ne);
        starredIngredientCount = (TextView) itemView.findViewById(R.id.done_count);
        buttonViewOption = (ImageButton) itemView.findViewById(R.id.overFlow);
     //   ingredientCount = (TextView) itemView.findViewById(R.id.childSubGoal_count);




    }

    public void bind(@NonNull SubGoalModel r) {

        this.subGoal = r;
        subGoalTextView.setText(r.getName());
        final String taskId = subGoal.getId();
        starredIngredientCount.setText(String.valueOf(subGoal.getDone()));


        //if the Task has sub tasks show the expander/collapser icon, if else keep the expander/collapser hidden
            if (subGoal.getChildList().size()>0){
                arrowExpandImageView.setVisibility(View.VISIBLE);
            }









        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Realm", "onClick: subGoal");
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                subGoal.setDone(!subGoal.getDone());
                realm.commitTransaction();
                realm.close();
            }
        });


        buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_menu_task, popup.getMenu());


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

                                        RealmResults<SubGoalModel> subGoalModel = realm.where(SubGoalModel.class).equalTo("id",taskId).findAll();

                                        SubGoalModel subGoalModelChild = realm.where(SubGoalModel.class).equalTo("id",taskId).findFirst();

                                        Log.d("GOALS", "child subgoal list is size  "+ subGoalModelChild.getChildSubGoalCount() );
                                       if(subGoalModelChild.getChildList().size()>0){
                                           subGoalModelChild.getChildList().deleteAllFromRealm();
                                       }
                                        subGoalModel.deleteFirstFromRealm();
                                        Log.d("GOALS", "deleted item? ");
                                        // realm.close();
                                    }

                                });
                                realm.close();
                                break;
                            case R.id.add_subtask:
                                realm = Realm.getDefaultInstance();
                                final EditText taskEditText = new EditText(v.getContext());
                                AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                                        .setTitle("Add Sub-Task")
                                        .setView(taskEditText)
                                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //realm = Realm.getDefaultInstance();
                                                realm.executeTransactionAsync(new Realm.Transaction() {
                                                                                  @Override
                                                                                  public void execute(Realm realm) {
                                                                                      final String uuID = UUID.randomUUID().toString();
                                                                                      realm.createObject(ChildSubGoalModel.class, uuID)
                                                                                              .setName(String.valueOf(taskEditText.getText()));
                                                                                      ChildSubGoalModel sub = realm.where(ChildSubGoalModel.class).equalTo("id", uuID).findFirst();
                                                                                      SubGoalModel subGoalModel = realm.where(SubGoalModel.class).equalTo("id", taskId).findFirst();
                                                                                      subGoalModel.getChildList().add(sub);
                                                                                      sub.setSubGoal(subGoalModel);
                                                                                      Log.d("GOALS", "added subgoal ");



                                                                                  }
                                                                              },
                                                        new Realm.Transaction.OnSuccess() {
                                                            @Override
                                                            public void onSuccess() {

                                                            }
                                                        }, new Realm.Transaction.OnError() {
                                                            @Override
                                                            public void onError(Throwable error) {

                                                            }
                                                        });


                                            }
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .create();
                                dialog.show();
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
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (expanded) {
                arrowExpandImageView.setRotation(ROTATED_POSITION);
            } else {
                arrowExpandImageView.setRotation(INITIAL_POSITION);
            }
        }
    }

    @Override
    public void onExpansionToggled(final boolean expanded) {
        super.onExpansionToggled(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            RotateAnimation rotateAnimation;
            if (expanded) { // rotate clockwise
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else { // rotate counterclockwise
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            arrowExpandImageView.startAnimation(rotateAnimation);
        }

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                subGoal.setExpanded(!expanded);
            }
        });
        realm.close();
    }

    @Override
    public void setMainItemClickToExpand() {
        arrowExpandImageView.setOnClickListener(this);
    }

}

