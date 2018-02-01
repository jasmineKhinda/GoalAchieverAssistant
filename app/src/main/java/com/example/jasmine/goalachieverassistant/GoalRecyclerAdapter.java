package com.example.jasmine.goalachieverassistant;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.jasmine.goalachieverassistant.EditGoalActivity;
import com.example.jasmine.goalachieverassistant.GoalListActivity;
import com.example.jasmine.goalachieverassistant.Models.GoalModel;
import com.example.jasmine.goalachieverassistant.R;
import io.realm.Realm;

import com.example.jasmine.goalachieverassistant.Models.GoalModel;
import com.example.jasmine.goalachieverassistant.Models.SubGoalModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by jasmine on 15/01/18.
 */

public class GoalRecyclerAdapter extends RealmRecyclerViewAdapter<GoalModel, GoalRecyclerAdapter.TaskViewHolder>{
    private GoalListActivity activity;
    private View itemView;
    private Realm realm;

    public GoalRecyclerAdapter(@NonNull GoalListActivity activity, @Nullable OrderedRealmCollection<GoalModel> data, boolean autoUpdate) {
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

        final GoalModel mTaskModel = getData().get(position);


        holder.taskName.setText(mTaskModel.getName());


        if(mTaskModel.getSubGoalCount()>0) {
            float totalSubGoalCount = mTaskModel.getSubGoalCount();
            float doneSubGoals = mTaskModel.getSubgoalsComplete();

            float totalChildSubGoals=0;
            float doneChildSubGoals =0;
            for (SubGoalModel r : mTaskModel.getSubgoals()) {
                if (r.getChildSubGoalCount()>0){
                    totalChildSubGoals= totalChildSubGoals + r.getChildSubGoalCount();
                    doneChildSubGoals = doneChildSubGoals + r.getChildSubgoalsComplete();
                }
            }
            float progress = ( ((doneChildSubGoals) +(doneSubGoals))/((totalSubGoalCount)+(totalChildSubGoals))) * 100;

            Log.d("GOALS", "1 subgoal count  "+ totalSubGoalCount );
            Log.d("GOALS", "1 Child subgoal count  "+totalChildSubGoals);

            Log.d("GOALS", "2 Child subgoal complete "+ doneSubGoals);
            Log.d("GOALS", "2 subgoal complete "+ doneChildSubGoals);



            int progress_print = (int) progress;
            holder.progressBar.setProgress(progress_print);
            holder.progressbar_percentage.setText(String.valueOf(progress_print) + "%");
        }
        Log.d("GOALS", "getName "+  mTaskModel.getName());

//        if(!TextUtils.isEmpty(mTaskModel.getDateTime())){
//            holder.taskDueDate.setText(mTaskModel.getDateTime());
//        }else{
//            holder.taskDueDate.setText(R.string.no_time);
//        }
//        //holder.taskCategory.setText(mTaskModel.getType());
        if(null != mTaskModel.getDueDate() ){

            String dateToDisplay = DateDisplayParser.parseDateForDisplay(mTaskModel.getDueDate());
            holder.taskDate.setText(dateToDisplay);

        }else{
            holder.taskDate.setText(R.string.no_due_date);
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent viewTaskIntent = new Intent(view.getContext(), EditGoalActivity.class);
                viewTaskIntent.putExtra("EDIT_GOALUUID", mTaskModel.getId());
                view.getContext().startActivity(viewTaskIntent);
            }
        });



    }



    class TaskViewHolder extends RecyclerView.ViewHolder{
        public CardView card;
        public TextView taskName;
        public TextView taskDate;
        public TextView taskCategory;
        public ProgressBar progressBar;
        public ImageButton buttonViewOption;
        public TextView progressbar_percentage;

        public TaskViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.card_view);
            taskName = (TextView)itemView.findViewById(R.id.goalName);
            //taskDueDate = (TextView)itemView.findViewById(R.id.task_date_time);
            taskDate = (TextView) itemView.findViewById(R.id.goalTimeAdded);
            buttonViewOption = (ImageButton) itemView.findViewById(R.id.imageButton);
            progressBar = (ProgressBar) itemView.findViewById(R.id.cmll_progrssbar);
            progressbar_percentage =(TextView) itemView.findViewById(R.id.cmll_completed_per);

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
                            final GoalModel mTaskModel = getData().get(position);
                            final String id = mTaskModel.getId();


                            switch (item.getItemId()) {
                                case R.id.delete:
                                    realm = Realm.getDefaultInstance();
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {

                                            GoalModel goalModel = realm.where(GoalModel.class).equalTo("id",id ).findFirst();
                                            goalModel.deleteFromRealm();
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