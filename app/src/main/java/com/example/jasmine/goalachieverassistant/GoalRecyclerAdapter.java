package com.example.jasmine.goalachieverassistant;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;


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
        Log.d("GOALS", "getName "+  mTaskModel.getName());

//        if(!TextUtils.isEmpty(mTaskModel.getDateTime())){
//            holder.taskDueDate.setText(mTaskModel.getDateTime());
//        }else{
//            holder.taskDueDate.setText(R.string.no_time);
//        }
//        //holder.taskCategory.setText(mTaskModel.getType());
        if(!TextUtils.isEmpty(mTaskModel.getDueDate())){
            holder.taskDate.setText("Due: "+mTaskModel.getDueDate().toString());
        }else{
            holder.taskDate.setText(R.string.no_due_date);
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent viewTaskIntent = new Intent(view.getContext(), EditGoal.class);
                viewTaskIntent.putExtra("TASK_OBJECT", mTaskModel.getId().toString());
                view.getContext().startActivity(viewTaskIntent);
            }
        });



    }



    class TaskViewHolder extends RecyclerView.ViewHolder{
        public CardView card;
        public TextView taskName;
        public TextView taskDate;
        public TextView taskCategory;
        public ImageButton buttonViewOption;

        public TaskViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.card_view);
            taskName = (TextView)itemView.findViewById(R.id.goalName);
            //taskDueDate = (TextView)itemView.findViewById(R.id.task_date_time);
            taskDate = (TextView) itemView.findViewById(R.id.goalTimeAdded);
            buttonViewOption = (ImageButton) itemView.findViewById(R.id.imageButton);

            buttonViewOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.popup_menu, popup.getMenu());


                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            final int position = getAdapterPosition();
                            final GoalModel mTaskModel = getData().get(position);
                            final String id = mTaskModel.getId().toString();


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