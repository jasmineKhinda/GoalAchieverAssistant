package com.example.jasmine.goalachieverassistant.Fragments.Adapters;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jasmine.goalachieverassistant.EditGoalActivity;
import com.example.jasmine.goalachieverassistant.EditSubTaskActivity;
import com.example.jasmine.goalachieverassistant.EditTaskActivity;
import com.example.jasmine.goalachieverassistant.Models.TaskModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.Utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.ChildViewHolder;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by jasmine on 21/03/18.
 */

public class AllTasksRecyclerAdapter extends RealmRecyclerViewAdapter<TaskModel, AllTasksRecyclerAdapter.TaskViewHolder> {

    private List<TaskModel> taskList;
    private View itemView;
    private FragmentActivity activity;
    private CardView card;
    Realm realm;


    public AllTasksRecyclerAdapter(@NonNull FragmentActivity activity, @Nullable OrderedRealmCollection<TaskModel> taskList, boolean autoUpdate) {
        super(taskList,autoUpdate);
        this.taskList = taskList;
        this.activity = activity;
    }

    @Override
    public AllTasksRecyclerAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_tasks_row, parent, false);
        itemView = view;
        return new AllTasksRecyclerAdapter.TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllTasksRecyclerAdapter.TaskViewHolder holder, int position) {
        holder.bind();

    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView taskTitle;
        public ImageButton buttonViewOption;
        private TextView dueDate;
        private ImageView projectIcon;
        private TextView projectText;
        private CheckBox isTaskDone;
        private TaskModel mTask;
        private ImageView dueDateIcon;
        private TextView starredIngredientCount;
        private ImageView starredIngredientCountIcon;
        private LinearLayout layoutChecked;
        Realm realm;
        View view;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            taskTitle = (TextView) itemView.findViewById(R.id.task_title);
            buttonViewOption = (ImageButton) itemView.findViewById(R.id.overFlow);
            dueDate = (TextView) itemView.findViewById(R.id.task_duedate);
            isTaskDone = (CheckBox) itemView.findViewById(R.id.task_item_done);
            dueDateIcon =(ImageView) itemView.findViewById(R.id.due_date_icon);
            projectIcon =(ImageView) itemView.findViewById(R.id.project_icon);
            projectText = (TextView) itemView.findViewById(R.id.project_text);
            starredIngredientCount = (TextView) itemView.findViewById(R.id.done_count);
            starredIngredientCountIcon = (ImageView) itemView.findViewById(R.id.number_tasks_complete_icon);
            layoutChecked = (LinearLayout)itemView.findViewById(R.id.layout_checked);

            itemView.setOnClickListener(this);
        }


        public void bind() {


            final TaskModel mTaskModel = getData().get(getAdapterPosition());
            this.mTask = mTaskModel;

            FrameLayout frameBorder =(FrameLayout) itemView.findViewById(R.id.card_frame);
            frameBorder.setBackgroundColor(mTask.getLabelColor());


            taskTitle.setText(mTask.getName());

            final String taskId = mTask.getId();

            card = (CardView) itemView.findViewById(R.id.card_view);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent viewTaskIntent;
                    if(mTask.isGoal())
                    { viewTaskIntent = new Intent(view.getContext(), EditGoalActivity.class);

                    }else if(mTask.getParentTaskId()!=null){
                        viewTaskIntent = new Intent(view.getContext(), EditSubTaskActivity.class);
                    }else{
                        viewTaskIntent = new Intent(view.getContext(), EditTaskActivity.class);
                    }

                    viewTaskIntent.putExtra("EDIT_TASKUUID", mTask.getId());
                    viewTaskIntent.putExtra("EDIT_TASKNAME", mTask.getName());
                    Log.d("GOALS", "calling activity is2 "+view.getContext().getClass().getName()  );
                    viewTaskIntent.putExtra("CALLING_ACTIVITY", activity.getClass().getName());
                    view.getContext().startActivity(viewTaskIntent);
                }
            });

            if(null!= mTask.getDueDate()){


                Date currentTime = Calendar.getInstance().getTime();
                if(null!= mTask.getDueDate()&& ((currentTime.getTime() - mTask.getDueDate().getTime()))>0){
                    dueDate.setTextColor(itemView.getResources().getColor(R.color.color_past_due));
                    dueDateIcon.setColorFilter(itemView.getResources().getColor(R.color.color_past_due));
                }else if (null!= mTask.getDueDate()&& ((currentTime.getTime() - mTask.getDueDate().getTime()))<0){
                    Log.d("GOALS", "text colour: " +dueDate.getTextColors().getDefaultColor());
                    dueDate.setTextColor(dueDate.getTextColors().getDefaultColor());
                    dueDateIcon.setColorFilter(itemView.getResources().getColor(R.color.color_icons));
                }

                String dateToDisplay = Utilities.parseDateForDisplay(mTask.getDueDate());
                dueDate.setVisibility(View.VISIBLE);
                dueDate.setText(dateToDisplay);
                dueDateIcon.setVisibility(View.VISIBLE);


            }else{
                dueDate.setVisibility(View.GONE);
                dueDateIcon.setVisibility(View.GONE);


            }


            Log.d("GOALS", "how many subtasks? "+mTask.getName() + "   " + mTask.getSubTaskCount());
            //if no subgoals to this goal, dont display the subgoal count done item
            if(0==mTask.getSubTaskCount()){
                starredIngredientCount.setVisibility(View.GONE);
                starredIngredientCountIcon.setVisibility(View.GONE);
                starredIngredientCount.setText("");



            }else{


                starredIngredientCount.setVisibility(View.VISIBLE);
                starredIngredientCountIcon.setVisibility(View.VISIBLE);
                starredIngredientCount.setText(mTask.getTotalSubTaskComplete()+" / " +mTask.getSubTaskCount() );

                Log.d("GOALS", "how many subtasks? VISIBLE " + getAdapterPosition());
            }



            isTaskDone.setChecked(mTask.getDone());
            if(mTask.getDone()){
                taskTitle.setPaintFlags( taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                taskTitle.setAlpha(0.38f);

            }//if item is unchecked(not done) then set checkbox to false and un-strike through the textview
            else{
                taskTitle.setPaintFlags(taskTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                taskTitle.setAlpha(1f);
            }


            Log.d("GOALS", "true or false? " + mTask.getDone());

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

                                            RealmResults<TaskModel> task = realm.where(TaskModel.class).equalTo("id", taskId).findAll();
                                            task.deleteFirstFromRealm();
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
            Log.i("Realm", "onColorSet: mTask");
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm r) {
                    mTask.setDone(!mTask.getDone());
                }
            });
            realm.close();
        }

//        private void setSubItemsPosition(){
//
//            if(null==mTask.getDueDate()){
//
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)starredIngredientCountIcon.getLayoutParams();
//                params.addRule(RelativeLayout.BELOW,R.id.task_title);
//                params.addRule(RelativeLayout.ALIGN_START, R.id.task_title);
//                params.addRule(RelativeLayout.ALIGN_LEFT, R.id.task_title);
//
//                Log.d("GOALS", "due date gone "+ mTask.getName());
//                starredIngredientCountIcon.setLayoutParams(params);
//            }else{
//
//                Log.d("GOALS", "due date VISIBLE " + mTask.getName());
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)starredIngredientCountIcon.getLayoutParams();
//                params.addRule(RelativeLayout.BELOW,R.id.task_title);
//                params.addRule(RelativeLayout.RIGHT_OF, R.id.task_duedate);
//
//                starredIngredientCountIcon.setLayoutParams(params);
//
//            }
//        }
//
//        private void setSubItemsPositionFromDueDate(boolean isDueDateVisible){
//
//            if(isDueDateVisible!=true){
//
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)starredIngredientCountIcon.getLayoutParams();
//                params.addRule(RelativeLayout.BELOW,R.id.task_title);
//                params.addRule(RelativeLayout.ALIGN_START, R.id.task_title);
//                params.addRule(RelativeLayout.ALIGN_LEFT, R.id.task_title);
//
//                Log.d("GOALS", "due date gone set"+ mTask.getName());
//                starredIngredientCountIcon.setLayoutParams(params);
//            }else{
//
//                Log.d("GOALS", "due date VISIBLE set" + mTask.getName());
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)starredIngredientCountIcon.getLayoutParams();
//                params.addRule(RelativeLayout.BELOW,R.id.task_title);
//                params.addRule(RelativeLayout.RIGHT_OF, R.id.task_duedate);
//
//                starredIngredientCountIcon.setLayoutParams(params);
//
//            }
//        }





        public void onClickedCheckBox(){
            //Do whatever you want to do here
            if (mTask.getDone()) {

                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        mTask.setDone(false);
                        Log.d("GOALS", "IN TRUEEEEEE");
                    }
                });
                realm.close();

            } else {


                Realm realm2 = Realm.getDefaultInstance();
                realm2.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        mTask.setDone(true);
                        Log.d("GOALS", "IN FALSE");
                    }
                });
                realm2.close();
            }
        }
    }

    /**
     * filtering the task list with sort order on the selected column in db
     * @param sortType : Spinner drop down menu item which was selected by the user from the Goals List
     * @param sortOrder Ascending or descending order
     */
    public void sortTaskListWithSortOrder(String sortType, Sort sortOrder) {
        try{
            realm = Realm.getDefaultInstance();
            OrderedRealmCollection<TaskModel> sorting = realm.where(TaskModel.class).findAll();
            //TODO add filtered of just Goals
            updateData(sorting.sort(sortType,sortOrder));
        }catch(Exception e) {
            Log.e("GOALS", "Not able to update the sorting of the task list, see stack trace" );
            Toast toast = Toast.makeText(itemView.getContext(), "Oops, something went wrong!", Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        }finally {
            realm.close();
        }


    }

    /**
     * filtering the list with multiple (2) fields then finally sorting
     * @param primaryFilterColumn : the name of the field to intially filter the list
     * @param primaryFilterValue : the value for "primaryFilterColumn" to filer the list
     * @param secondaryFilterColumn : the secondary field to filter the list with
     * @param secondaryFilterValue : search the "secondaryFilterColumn" column with the containing string
     * @param secondaryFilterValueDoesContain : true if "secondaryFilterValue" should be in column values, false otherwise
     * @param sortingField : field to which to sort by
     * @param sortOrder Ascending or descending order for the final sort
     */
    public void filterAndSortList(String primaryFilterColumn, String primaryFilterValue, @Nullable String secondaryFilterColumn,@Nullable String secondaryFilterValue, Boolean secondaryFilterValueDoesContain, Sort sortOrder, String sortingField) {

        try {

            realm = Realm.getDefaultInstance();
            OrderedRealmCollection<TaskModel> sorting = realm.where(TaskModel.class).equalTo(primaryFilterColumn, primaryFilterValue).findAll();

            Log.d("GOALS", "sort attributes  " + secondaryFilterColumn + "   " + sortOrder);
            if (null == secondaryFilterColumn && null != sortingField) {
                //no secondary sort is needed, just filter the primary filtered list "sorting"
                sorting = sorting.sort(sortingField, sortOrder);

            } else if (null == secondaryFilterValue && false == secondaryFilterValueDoesContain) {
                //filtering "sorting" list where final list contains all entries where the value of "secondaryFilterColumn" is NOT null, then finally sort list
                sorting = sorting.where().not().isNull(secondaryFilterColumn).findAllSorted(sortingField, sortOrder);
                Log.d("GOALS", "onItemSelected:  sort 1");
            } else if (null == secondaryFilterValue && true == secondaryFilterValueDoesContain) {
                //filtering "sorting" list where final list contains all entries where the value of "secondaryFilterColumn" is null, then finally sort list
                Log.d("GOALS", "onItemSelected:  sort 2");
                sorting = sorting.where().isNull(secondaryFilterColumn).findAllSorted(sortingField, sortOrder);
            } else if (null != secondaryFilterValue && false == secondaryFilterValueDoesContain) {
                //filtering "sorting" list where final list contains all entries where the value of "secondaryFilterColumn" is NOT equal to "secondaryFilterValue", then finally sort list
                Log.d("GOALS", "onItemSelected:  sort 3");
                sorting = sorting.where().notEqualTo(secondaryFilterColumn, secondaryFilterValue).findAllSorted(sortingField, sortOrder);
            } else if (null != secondaryFilterValue && true == secondaryFilterValueDoesContain) {
                //filtering "sorting" list where final list contains all entries where the value of "secondaryFilterColumn" is "secondaryFilterValue", then finally sort list
                Log.d("GOALS", "onItemSelected:  sort 4");
                sorting = sorting.where().equalTo(secondaryFilterColumn, secondaryFilterValue).findAllSorted(sortingField, sortOrder);

            }
            updateData(sorting);


        } catch (Exception e) {
            Log.e("GOALS", "Not able to update the sorting of the task list, see stack trace" + e.toString());
            Toast toast = Toast.makeText(itemView.getContext(), "Oops, something went wrong!", Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        } finally {
            realm.close();
        }
    }




    /**
     * filtering the task with a primary text search filter (searches the column in db for text), then finally a sorted list is returned
     * @param goalSpinnerSelectedFilter : Spinner drop down menu item which was selected by the user from the Goals List
     * @param sortOrder Ascending or descending order
     * @param containsValue : search the "sortType" column with the containing string
     * @param doesContain : true if "containsValue" should be in column values, false otherwise
     */
    public void filterTaskListContainsTextAndReturnsSortedList(String goalSpinnerSelectedFilter, Sort sortOrder, String containsValue, Boolean doesContain, String secondarySort) {
        try {
            //OrderedRealmCollection<GoalModel> sorting = realm.where(GoalModel.class).findAll();

            //RealmQuery<GoalModel> sorting = getData().where();
            //RealmResults<GoalModel> tasks = realm.where(GoalModel.class).findAll();

            //TODO filter just goals after refactor

            realm = Realm.getDefaultInstance();
            OrderedRealmCollection<TaskModel> sorting = realm.where(TaskModel.class).findAll();

            Log.d("GOALS", "sort attributes  " + goalSpinnerSelectedFilter + "   " + sortOrder);
            if (null == containsValue && false == doesContain) {
                sorting = sorting.where().not().isNull(goalSpinnerSelectedFilter).findAllSorted(secondarySort, sortOrder);
                Log.d("GOALS", "onItemSelected:  sort 1");
            } else if (null == containsValue && true == doesContain) {
                Log.d("GOALS", "onItemSelected:  sort 2");
                sorting = sorting.where().isNull(goalSpinnerSelectedFilter).findAllSorted(secondarySort, sortOrder);
            } else if (null != containsValue && false == doesContain) {
                Log.d("GOALS", "onItemSelected:  sort 3");
                sorting = sorting.where().notEqualTo(goalSpinnerSelectedFilter, containsValue).findAllSorted(secondarySort, sortOrder);
            } else {
                Log.d("GOALS", "onItemSelected:  sort 4");
                sorting = sorting.where().equalTo(goalSpinnerSelectedFilter, containsValue).findAllSorted(secondarySort, sortOrder);

            }
            updateData(sorting);


        } catch (Exception e) {
            Log.e("GOALS", "Not able to update the sorting of the task list see stack trace" + e.toString());
            Toast toast = Toast.makeText(itemView.getContext(), "Oops, something went wrong!", Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        } finally {
            realm.close();
        }
    }



}
