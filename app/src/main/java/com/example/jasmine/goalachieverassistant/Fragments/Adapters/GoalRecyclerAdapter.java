package com.example.jasmine.goalachieverassistant.Fragments.Adapters;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jasmine.goalachieverassistant.EditGoalActivity;
import com.example.jasmine.goalachieverassistant.GoalListActivity;
import com.example.jasmine.goalachieverassistant.MainActivity;
import com.example.jasmine.goalachieverassistant.Models.GoalModel;

import io.realm.Realm;

import com.example.jasmine.goalachieverassistant.Models.SubGoalModel;
import com.example.jasmine.goalachieverassistant.Models.TaskModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.Sort;

/**
 * Created by jasmine on 15/01/18.
 */

public class GoalRecyclerAdapter extends RealmRecyclerViewAdapter<TaskModel, GoalRecyclerAdapter.TaskViewHolder>{
    private FragmentActivity activity;
    private View itemView;
    private Realm realm;

    public GoalRecyclerAdapter(@NonNull FragmentActivity activity, @Nullable OrderedRealmCollection<TaskModel> data, boolean autoUpdate) {
        super(data, autoUpdate);
        this.activity = activity;
    }
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_list_row, parent, false);
        itemView = view;
        return new TaskViewHolder(view);
    }
    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        Log.d("GOALS", "WHY AM I COMING IN HERE?");

        final TaskModel mTaskModel = getData().get(position);

        holder.taskName.setText(mTaskModel.getName());
        holder.frameBorder.setBackgroundColor(mTaskModel.getLabelColor());


        if(mTaskModel.getTaskCount()>0) {
            float totalSubGoalCount = mTaskModel.getTaskCount();
            float doneSubGoals = mTaskModel.getTotalTaskComplete();

            float totalChildSubGoals=0;
            float doneChildSubGoals =0;
            for (TaskModel r : mTaskModel.getTasks()) {
                if (r.getSubTaskCount()>0){
                    totalChildSubGoals= totalChildSubGoals + r.getSubTaskCount();
                    doneChildSubGoals = doneChildSubGoals + r.getTotalSubTaskComplete();
                }
            }
            float progress = ( ((doneChildSubGoals) +(doneSubGoals))/((totalSubGoalCount)+(totalChildSubGoals))) * 100;

            Log.d("GOALS", "1 subgoal count  "+ totalSubGoalCount );
            Log.d("GOALS", "1 Child subgoal count  "+totalChildSubGoals);

            Log.d("GOALS", "2 Child subgoal complete "+ doneSubGoals);
            Log.d("GOALS", "2 subgoal complete "+ doneChildSubGoals);
            int totalTasks= (int) (totalSubGoalCount+totalChildSubGoals);
            int totalDoneTasks =(int) (doneSubGoals +doneChildSubGoals);


            int progress_print = (int) progress;
            holder.progressBar.setProgress(progress_print);
            Log.d("GOALS", "progres is "+ mTaskModel.getName()+ "  "+ progress_print);
            holder.progressbar_percentage.setText(String.valueOf(progress_print) + "%");
            holder.numberTasksComplete.setText(totalDoneTasks+"/"+totalTasks);
            holder.numberTasksComplete.setVisibility(View.VISIBLE);
            holder.numberTasksCompleteIcon.setVisibility(View.VISIBLE);


//            if(null != mTaskModel.getDueDate()){
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.numberTasksCompleteIcon.getLayoutParams();
//                params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.goalTimeAdded);
//                params.setMargins(0,0,0,0);
//
//                holder.numberTasksCompleteIcon.setLayoutParams(params);
//                holder.numberTasksCompleteIcon.setVisibility(View.VISIBLE);
//
//            }else{
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.numberTasksCompleteIcon.getLayoutParams();
//                params.addRule(RelativeLayout.ALIGN_START, R.id.goalName);
//                params.addRule(RelativeLayout.ALIGN_TOP, R.id.number_tasks_complete);
//                params.setMargins(0,0,0,0);
//
//                holder.numberTasksCompleteIcon.setLayoutParams(params);
//                holder.numberTasksCompleteIcon.setVisibility(View.VISIBLE);
 //           }
        }else{
            holder.progressBar.setProgress(0);
            Log.d("GOALS", "progres is else statement "+ mTaskModel.getName()+ "  "+ 0);
            holder.progressbar_percentage.setText(String.valueOf(0) + "%");
            holder.numberTasksComplete.setVisibility(View.INVISIBLE);
            holder.numberTasksCompleteIcon.setVisibility(View.INVISIBLE);

        }
        Log.d("GOALS", "getName "+  mTaskModel.getName());

        if(null != mTaskModel.getDueDate() ){


                String dateToDisplay = Utilities.parseDateForDisplay(mTaskModel.getDueDate());
                holder.taskDate.setText(dateToDisplay);
                holder.taskDate.setVisibility(View.VISIBLE);
                holder.dueDateIcon.setVisibility(View.VISIBLE);

        }else{

            holder.taskDate.setVisibility(View.INVISIBLE);
            holder.dueDateIcon.setVisibility(View.INVISIBLE);
        }

        Date currentTime = Calendar.getInstance().getTime();
//        String mDate = showDateFormat(System.currentTimeMillis());
//        SimpleDateFormat mSdf = new SimpleDateFormat("MMM MM dd,yyy h:mm a");
//        final String mDateString = mSdf.format(mDate);
//        Date dtCurrent = mSdf.parse(mDate);

        Log.d("GOALS", "goal is "+mTaskModel.getName());
        Log.d("GOALS", "current time is "+currentTime.getTime());
//        Log.d("GOALS", "due date is time is "+mTaskModel.getDueDate().getTime());

        if(null!=mTaskModel.getDueDate()&& ((currentTime.getTime() - mTaskModel.getDueDate().getTime()))>0){
            holder.taskDate.setTextColor(itemView.getResources().getColor(R.color.color_past_due));
            holder.dueDateIcon.setColorFilter(itemView.getResources().getColor(R.color.color_past_due));
        }else if (null!=mTaskModel.getDueDate()&& ((currentTime.getTime() - mTaskModel.getDueDate().getTime()))<0){
            Log.d("GOALS", "text colour: " +holder.taskDate.getTextColors().getDefaultColor());
            holder.taskDate.setTextColor(holder.taskDate.getTextColors().getDefaultColor());
            holder.dueDateIcon.setColorFilter(itemView.getResources().getColor(R.color.color_icons));
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent viewTaskIntent = new Intent(view.getContext(), EditGoalActivity.class);
                viewTaskIntent.putExtra("EDIT_GOALUUID", mTaskModel.getId());
                viewTaskIntent.putExtra("EDIT_GOALNAME", mTaskModel.getName());
                view.getContext().startActivity(viewTaskIntent);
            }
        });



    }

    /**
     * sorting the goals list with sort order on the selected column in db
     * @param sortType : Spinner drop down menu item which was selected by the user from the Goals List
     * @param sortOrder Ascending or descending order
     */
    public void sortGoalListWithSortOrder(String sortType, Sort sortOrder) {
        try{
            realm = Realm.getDefaultInstance();
            OrderedRealmCollection<TaskModel> sorting = realm.where(TaskModel.class).equalTo("isGoal", true).findAll();
            //TODO add filtered of just Goals
            updateData(sorting.sort(sortType,sortOrder));
        }catch(Exception e) {
            Log.e("GOALS", "Not able to update the sorting of the goals list, see stack trace" );
            e.printStackTrace();
        }finally {
            realm.close();
        }


    }
    /**
     * sorting the goals with a primary text search filter (searches the column in db for text), then finally a sorted list is returned
     * @param goalSpinnerSelectedFilter : Spinner drop down menu item which was selected by the user from the Goals List
     * @param sortOrder Ascending or descending order
     * @param containsValue : search the "sortType" column with the containing string
     * @param doesContain : true if "containsValue" should be in column values, false otherwise
     */
    public void sortGoalListContainsTextAndReturnsSortedList(String goalSpinnerSelectedFilter, Sort sortOrder, String containsValue, Boolean doesContain,String secondarySort) {
        try {
            //OrderedRealmCollection<GoalModel> sorting = realm.where(GoalModel.class).findAll();

            //RealmQuery<GoalModel> sorting = getData().where();
            //RealmResults<GoalModel> tasks = realm.where(GoalModel.class).findAll();

            //TODO filter just goals after refactor

            realm = Realm.getDefaultInstance();
            OrderedRealmCollection<TaskModel> sorting = realm.where(TaskModel.class).equalTo("isGoal", true).findAll();

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


        }catch(Exception e) {
            Log.e("GOALS", "Not able to update the sorting of the goals list, see stack trace" +e.toString() );
            e.printStackTrace();
        }finally {
            realm.close();
        }


    }


    class TaskViewHolder extends RecyclerView.ViewHolder{
        public CardView card;
        public TextView taskName;
        public TextView taskDate;
        public TextView taskCategory;
        public ProgressBar progressBar;
        public ImageButton buttonViewOption;
        public TextView progressbar_percentage;
        public TextView numberTasksComplete;
        public ImageView numberTasksCompleteIcon;
        public ImageView dueDateIcon;

        FrameLayout frameBorder;

        public TaskViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.card_view);
            taskName = (TextView)itemView.findViewById(R.id.goalName);
            //taskDueDate = (TextView)itemView.findViewById(R.id.task_date_time);
            taskDate = (TextView) itemView.findViewById(R.id.goalTimeAdded);
            buttonViewOption = (ImageButton) itemView.findViewById(R.id.imageButton);
            progressBar = (ProgressBar) itemView.findViewById(R.id.cmll_progrssbar);
            progressbar_percentage =(TextView) itemView.findViewById(R.id.cmll_completed_per);
            frameBorder =(FrameLayout) itemView.findViewById(R.id.card_frame);
            numberTasksComplete = (TextView) itemView.findViewById(R.id.number_tasks_complete);
            numberTasksCompleteIcon =(ImageView) itemView.findViewById(R.id.number_tasks_complete_icon);
            dueDateIcon =(ImageView) itemView.findViewById(R.id.due_date_icon);

            buttonViewOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.popup_menu_goal, popup.getMenu());


                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            final int position = getAdapterPosition();
                            final TaskModel mTaskModel = getData().get(position);
                            final String id = mTaskModel.getId();


                            switch (item.getItemId()) {
                                case R.id.delete:
                                    realm = Realm.getDefaultInstance();
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {

                                            TaskModel goalModel = realm.where(TaskModel.class).equalTo("id",id ).findFirst();
                                            goalModel.deleteFromRealm();

                                            //TODO delete tasks and subtasks too
                                            Log.d("GOALS", "deleted item? ");
                                      // realm.close();
                                        }
                                    });
                                    realm.close();
                                    break;
                                case R.id.share:
                                    //handle menu2 click
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
    }


}