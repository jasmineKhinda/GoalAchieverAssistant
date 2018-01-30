package com.example.jasmine.goalachieverassistant.recyclerview.viewHolders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.CompoundButton;

import com.example.jasmine.goalachieverassistant.ChildSubGoalModel;
import com.example.jasmine.goalachieverassistant.FragmentIdea.Activities.AddGoalActivity;
import com.example.jasmine.goalachieverassistant.FragmentIdea.Fragments.DatePickerFragment;
import com.example.jasmine.goalachieverassistant.FragmentIdea.Fragments.GoalTasksFragment;
import com.example.jasmine.goalachieverassistant.GoalModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.SubGoalModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import io.realm.ParentViewHolder;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Created by jasmine on 18/01/18.
 */

public class SubGoalViewHolder extends ParentViewHolder implements DatePickerFragment.DatePickerFragmentListener {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 45f;

    @NonNull
    private final ImageView arrowExpandImageView;
    private TextView subGoalTextView;
    private TextView starredIngredientCount;
    private TextView dueDate;
    public ImageButton buttonViewOption;
    private CheckBox isTaskDone;
    private TextView dueDateDialog;
    private Context context;
    Realm realm;
 //   private TextView ingredientCount;

    private SubGoalModel subGoal;





    public SubGoalViewHolder(@NonNull View itemView)  {
        super(itemView);

        subGoalTextView = (TextView) itemView.findViewById(R.id.subGoal_textview);
        arrowExpandImageView = (ImageView) itemView.findViewById(R.id.chevron_Ne);
        starredIngredientCount = (TextView) itemView.findViewById(R.id.done_count);
        dueDate = (TextView) itemView.findViewById(R.id.goalDueDate);
        buttonViewOption = (ImageButton) itemView.findViewById(R.id.overFlow);
        isTaskDone = (CheckBox) itemView.findViewById(R.id.task_item_done);



    }


    @Override
    public void onDateSet(Date view) {


        // This method will be called with the date from the `DatePicker`.

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d");
        String date = sdf.format(view);
        Log.d("GOALS", "in onDateSet "+ date);
        dueDateDialog.setText(date);
    }

    public void bind(@NonNull final SubGoalModel r) {

        this.subGoal = r;
        subGoalTextView.setText(r.getName());
        final String taskId = subGoal.getId();
        final DatePickerFragment fragment =DatePickerFragment.newInstance(this);







        //if no subgoals to this goal, dont display the subgoal count done item
        if(subGoal.getChildSubGoalCount()>0){
            starredIngredientCount.setVisibility(View.VISIBLE);
            starredIngredientCount.setText("0 / " +subGoal.getChildSubGoalCount());
        }

        //if no due date display the default "No Due Date" from layout file
        if(!subGoal.getDueDate().isEmpty()){
            dueDate.setText(subGoal.getDueDate());
        }


        //if the Task has sub tasks show the expander/collapser icon, if else keep the expander/collapser hidden
            if (subGoal.getChildList().size()>0){
                arrowExpandImageView.setVisibility(View.VISIBLE);
            }



            isTaskDone.setChecked(subGoal.getDone());


            Log.d("GOALS", "true or false? " +subGoal.getDone());


            isTaskDone.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(final View v) {

                                                  if (subGoal.getDone()) {

                                                      CheckBox checkBox = (CheckBox) v.findViewById(R.id.task_item_done);
                                                      checkBox.setChecked(!checkBox.isChecked());
                                                      Realm realm = Realm.getDefaultInstance();
                                                      realm.executeTransaction(new Realm.Transaction() {
                                                          @Override
                                                          public void execute(Realm realm) {
                                                              subGoal.setDone(false);
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
                                                              subGoal.setDone(true);
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

                                //setting the layout of the add Task dialog

                                LayoutInflater inflater = LayoutInflater.from(v.getContext());

                                View subView = inflater.inflate(R.layout.dialog_add_task_subtask, null);
                                EditText addTaskDueDate = (EditText) subView.findViewById(R.id.add_task_dueDate);
                                dueDateDialog = (EditText) subView.findViewById(R.id.add_task_dueDate);
                                final EditText addTaskName = (EditText) subView.findViewById(R.id.add_task_name);
                                final ImageView addTaskDueDateImage = (ImageView) subView.findViewById(R.id.add_task_date_image);

                                final AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                                        .setTitle("Add Sub-Task")
                                        .setView(subView)
                                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //realm = Realm.getDefaultInstance();
                                                realm.executeTransactionAsync(new Realm.Transaction() {
                                                                                  @Override
                                                                                  public void execute(Realm realm) {
                                                                                      final String uuID = UUID.randomUUID().toString();
                                                                                      realm.createObject(ChildSubGoalModel.class, uuID)
                                                                                              .setName(String.valueOf(addTaskName .getText()));
                                                                                      ChildSubGoalModel sub = realm.where(ChildSubGoalModel.class).equalTo("id", uuID).findFirst();
                                                                                      SubGoalModel subGoalModel = realm.where(SubGoalModel.class).equalTo("id", taskId).findFirst();
                                                                                      subGoalModel.getChildList().add(sub);
                                                                                      sub.setSubGoal(subGoalModel);
                                                                                      sub.setDueDate(dueDateDialog.getText().toString());
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
                                dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#757575"));
                                dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#757575"));
                                realm.close();


                            addTaskDueDate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    // DatePickerFragment date = new DatePickerFragment();
                                    fragment.show(fragment.getActivity().getSupportFragmentManager(), "Task Date");
                                }
                            });

                            addTaskDueDateImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {



                                    FragmentManager manager = ((AppCompatActivity) scanForActivity(v.getContext())).getSupportFragmentManager();
                                    Log.d("GOALS", "fragman "+ manager);
                                    //DatePickerFragment fragment = new DatePickerFragment();
                                    fragment.show(manager, "Task Date");
                                    //new GoalTasksFragment.DatePickersFragment().show(getFragmentManager(), "Task Date");
                                }
                            });
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

    private static Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity)cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper)cont).getBaseContext());

        return null;
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

