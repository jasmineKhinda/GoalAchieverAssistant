package com.example.jasmine.goalachieverassistant.RecyclerviewExpandedItem.viewHolders;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.jasmine.goalachieverassistant.EditTaskActivity;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.CustomBottomSheetDialogFragment;
import com.example.jasmine.goalachieverassistant.Models.TaskModel;
import com.example.jasmine.goalachieverassistant.RecyclerviewExpandedItem.adapter.SubGoalAdapter;
import com.example.jasmine.goalachieverassistant.Utilities;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.DatePickerFragment;
import com.example.jasmine.goalachieverassistant.R;

import java.util.Calendar;
import java.util.Date;

import io.realm.ParentViewHolder;
import io.realm.Realm;
import io.realm.RealmResults;

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
    private ImageView starredIngredientCountIcon;
    private TextView dueDate;
    private ImageView dueDateIcon;
    private CardView card;
    private ImageButton buttonViewOption;
    private CheckBox isTaskDone;
    private TextView dueDateDialog;
    private Context context;
    Realm realm;
    FrameLayout frameBorder;
    Date taskDueDate;
    LinearLayout layoutChecked;
 //   private TextView ingredientCount;
    SubGoalStateChanged changed;
    private TaskModel subGoal;




    public interface SubGoalStateChanged{
        void subGoalChanged();
    }
    public void setSubGoalChangedListener(SubGoalStateChanged changed){
        this.changed = changed;
    }


    public SubGoalViewHolder(@NonNull View itemView)  {
        super(itemView);

        subGoalTextView = (TextView) itemView.findViewById(R.id.task_title);
        arrowExpandImageView = (ImageView) itemView.findViewById(R.id.chevron_Ne);
        starredIngredientCount = (TextView) itemView.findViewById(R.id.done_count);
        dueDateIcon =(ImageView) itemView.findViewById(R.id.due_date_icon);
        starredIngredientCountIcon = (ImageView) itemView.findViewById(R.id.number_tasks_complete_icon);
        dueDate = (TextView) itemView.findViewById(R.id.task_duedate);
        buttonViewOption = (ImageButton) itemView.findViewById(R.id.overFlow);
        isTaskDone = (CheckBox) itemView.findViewById(R.id.task_item_done);




    }





    @Override
    public void onDateSet(Date view) {


        // This method will be called with the date from the `DatePicker`.
        if(null != view ){

            String dateToDisplay = Utilities.parseDateForDisplay(view);
            dueDateDialog.setText(dateToDisplay);
            Log.d("GOALS", "onDateSet: ");

        }else{
            dueDate.setText(R.string.no_due_date);
        }

        taskDueDate = view;


    }


//TODO blah
    public void bind(@NonNull final TaskModel r) {

        final TaskModel subgoal;
        this.subGoal = r;
        subgoal = r;
        subGoalTextView.setText(r.getName());
        final String taskId = subgoal.getId();
        final String taskName = subgoal.getName();
        final DatePickerFragment fragment =DatePickerFragment.newInstance(this);
        layoutChecked = (LinearLayout)itemView.findViewById(R.id.layout_checked);
        frameBorder =(FrameLayout) itemView.findViewById(R.id.card_frame);

        frameBorder.setBackgroundColor(subgoal.getLabelColor());

        Log.d("GOALS", "the sub goal is "+ subGoal.getName() +  " child list is + "+ subgoal.getChildList().size()+ "  "+ subgoal.getDueDate());



        card = (CardView) itemView.findViewById(R.id.card_view);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent viewTaskIntent = new Intent(view.getContext(), EditTaskActivity.class);
                viewTaskIntent.putExtra("EDIT_TASKUUID", subgoal.getId());
                viewTaskIntent.putExtra("EDIT_TASKNAME", subgoal.getName());
                view.getContext().startActivity(viewTaskIntent);
            }
        });


        //if no subgoals to this goal, dont display the subgoal count done item
        if(subgoal.getSubTaskCount()>0){
            starredIngredientCount.setVisibility(View.VISIBLE);
            starredIngredientCountIcon.setVisibility(View.VISIBLE);
            starredIngredientCount.setText(subgoal.getTotalSubTaskComplete()+"/" +subgoal.getSubTaskCount() );
        }else{
            starredIngredientCount.setVisibility(View.GONE);
            starredIngredientCountIcon.setVisibility(View.GONE);
            starredIngredientCount.setText("");
        }



        //if due date exists in db then display it
        //if no due date display the default "No Due Date" from layout file
        if(null!=subgoal.getDueDate()){

            Date currentTime = Calendar.getInstance().getTime();
            if(null!=subgoal.getDueDate()&& ((currentTime.getTime() - subgoal.getDueDate().getTime()))>0){
                dueDate.setTextColor(itemView.getResources().getColor(R.color.color_past_due));
                dueDateIcon.setColorFilter(itemView.getResources().getColor(R.color.color_past_due));
            }else if (null!=subgoal.getDueDate()&& ((currentTime.getTime() - subgoal.getDueDate().getTime()))<0){
                Log.d("GOALS", "text colour: " +dueDate.getTextColors().getDefaultColor());
                dueDate.setTextColor(dueDate.getTextColors().getDefaultColor());
                dueDateIcon.setColorFilter(itemView.getResources().getColor(R.color.color_icons));
            }

            String dateToDisplay = Utilities.parseDateForDisplay(subgoal.getDueDate());
            dueDate.setVisibility(View.VISIBLE);
            dueDate.setText(dateToDisplay);
            dueDateIcon.setVisibility(View.VISIBLE);
        }else{
            dueDate.setVisibility(View.GONE);
            dueDateIcon.setVisibility(View.GONE);
        }


        //if the Task has sub tasks show the expander/collapser icon, if else keep the expander/collapser hidden
            if (subgoal.getChildList().size()>0){
                arrowExpandImageView.setVisibility(View.VISIBLE);
            }else{
                arrowExpandImageView.setVisibility(View.INVISIBLE);
            }

            //if item is checked(done) then check off the checkbox and strike through the textview
            isTaskDone.setChecked(subgoal.getDone());
            if(subgoal.getDone()){
                subGoalTextView.setPaintFlags( subGoalTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                subGoalTextView.setAlpha(0.38f);

            }//if item is unchecked(not done) then set checkbox to false and un-strike through the textview
            else{
                subGoalTextView.setPaintFlags(subGoalTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                subGoalTextView.setAlpha(1f);
            }




            Log.d("GOALS", "true or false? " +subgoal.getDone());


        layoutChecked.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickedCheckBox();
            }
        });

        isTaskDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickedCheckBox();
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

                        switch (item.getItemId()) {
                            case R.id.delete_task:
                                try{
                                    realm = Realm.getDefaultInstance();
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {

                                            RealmResults<TaskModel> subGoalModel = realm.where(TaskModel.class).equalTo("id",taskId).findAll();

                                            TaskModel subGoalModelChild = realm.where(TaskModel.class).equalTo("id",taskId).findFirst();

                                            Log.d("GOALS", "child subgoal list is size  "+ subGoalModel.size());
                                            if(subGoalModelChild.getChildList().size()>0){
                                                subGoalModelChild.getChildList().deleteAllFromRealm();
                                            }
                                            subGoalModel.deleteFirstFromRealm();


                                            Log.d("GOALS", "deleted item? ");
                                            // realm.close();
                                        }

                                    });
                                }finally{
                                    realm.close();

                                }


                                break;
                            case R.id.add_subtask:

//                                FloatingActionButton fab = (FloatingActionButton) itemView.findViewById(R.id.fab_task);
//                                fab.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {

                                        FragmentManager manager = ((AppCompatActivity) scanForActivity(v.getContext())).getSupportFragmentManager();
                                        CustomBottomSheetDialogFragment bottomSheetDialogFragment = CustomBottomSheetDialogFragment.newInstance(taskId,true, taskName,false,null);
                                        bottomSheetDialogFragment.show(manager,"BottomSheet");



//
//                                    }
//                                });
//                                realm = Realm.getDefaultInstance();
//                                taskDueDate=null;
//
//                                //setting the layout of the add Task dialog
//
//                                LayoutInflater inflater = LayoutInflater.from(v.getContext());
//
//                                View subView = inflater.inflate(R.layout.dialog_add_task_subtask, null);
//                                EditText addTaskDueDate = (EditText) subView.findViewById(R.id.add_task_dueDate);
//                                dueDateDialog = (EditText) subView.findViewById(R.id.add_task_dueDate);
//                                final EditText addTaskName = (EditText) subView.findViewById(R.id.add_task_name);
//                                final ImageView addTaskDueDateImage = (ImageView) subView.findViewById(R.id.add_task_date_image);
//
//                                final AlertDialog dialog = new AlertDialog.Builder(v.getContext())
//                                        .setTitle("Add Sub-Task")
//                                        .setView(subView)
//                                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                realm.executeTransactionAsync(new Realm.Transaction() {
//                                                                                  @Override
//                                                                                  public void execute(Realm realm) {
//                                                                                      final String uuID = UUID.randomUUID().toString();
//                                                                                      realm.createObject(ChildSubGoalModel.class, uuID)
//                                                                                              .setName(String.valueOf(addTaskName .getText()));
//                                                                                      ChildSubGoalModel sub = realm.where(ChildSubGoalModel.class).equalTo("id", uuID).findFirst();
//                                                                                      SubGoalModel subGoalModel = realm.where(SubGoalModel.class).equalTo("id", taskId).findFirst();
//                                                                                      subGoalModel.getChildList().add(sub);
//                                                                                      sub.setSubGoal(subGoalModel);
//
//                                                                                      if(null !=taskDueDate && dueDateDialog.getText().toString().equals(Utilities.parseDateForDisplay(taskDueDate))){
//                                                                                          sub.setDueDate(taskDueDate);
//                                                                                          sub.setDueDateNotEmpty(taskDueDate);
//                                                                                          Log.d("GOALS", "Due dates matched! added subgoal due date");
//                                                                                      }else{
//                                                                                          Log.d("GOALS", "Due dates didn't matched!");
//                                                                                      }
////
////                                                                                      if(null!=taskDueDate){
////                                                                                          sub.setDueDate(taskDueDate);
////                                                                                      }
//                                                                                      Log.d("GOALS", "added subgoal ");
//
//
//
//                                                                                  }
//                                                                              },
//                                                        new Realm.Transaction.OnSuccess() {
//                                                            @Override
//                                                            public void onSuccess() {
//
//                                                            }
//                                                        }, new Realm.Transaction.OnError() {
//                                                            @Override
//                                                            public void onError(Throwable error) {
//
//                                                            }
//                                                        });
//
//
//                                            }
//                                        })
//                                        .setNegativeButton("Cancel", null)
//                                        .create();
//                                dialog.show();
//                                realm.close();
//
//
//                            addTaskDueDate.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    fragment.show(fragment.getActivity().getSupportFragmentManager(), "Task Date");
//                                }
//                            });
//
//                            addTaskDueDateImage.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    FragmentManager manager = ((AppCompatActivity) scanForActivity(v.getContext())).getSupportFragmentManager();
//                                    Log.d("GOALS", "fragman "+ manager);
//                                    fragment.show(manager, "Task Date");
//                                }
//                            });
//                            break;
                        }
                        return false;

                    }
                });
                //displaying the popup
                popup.show();

            }
        });



    }
    public void onClickedCheckBox(){
        //Do whatever you want to do here
        if (subGoal.getDone()) {

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

