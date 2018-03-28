package com.example.jasmine.goalachieverassistant.Fragments.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.jasmine.goalachieverassistant.Models.TaskModel;
import com.example.jasmine.goalachieverassistant.Utilities;
import com.example.jasmine.goalachieverassistant.EditGoalActivity;
import com.example.jasmine.goalachieverassistant.R;

import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalDetailsFragment extends Fragment implements  DatePickerFragment.DatePickerFragmentListener, ColorPickerAlertDialog.ColorPickerListener, NotesDialogFragment.AddNotesListener{

    private static final String GOAL_UUID = "GoalUUID";
    private  EditText goalDueDate;
    EditText goalName;
    TextView goalReason;
    ImageButton colourLabelButton;
    ImageView tagIcon;
    private Date dueDate;
    ImageView imageCalendar;
    private Realm realm;
    int colorSelected=0;
    int labelColorFromDB=0;


    private ChangeTabs changedTab;
    public interface ChangeTabs {
        public void onTabChange(int tabNumber);

    }




    public GoalDetailsFragment() {
        // Required empty public constructor
    }





    @Override
    public void onNoteAdded(String note){
        goalReason.setText(note);
    }

    @Override
    public void onColorSet(int color){

        colourLabelButton.setColorFilter(color);


    }

    @Override
    public void onDateSet(Date view) {
        Log.d("GOALS", "onDateset "+ view);
        final ImageView clearDueDateButton =(ImageView) getView().findViewById(R.id.remove_date);
        if(null != view ){

            String dateToDisplay = Utilities.parseDateForDisplay(view);
            goalDueDate.setText(dateToDisplay);
            clearDueDateButton.setVisibility(View.VISIBLE);


        }
//        else{
//            goalDueDate.setText(R.string.no_due_date);
//
//
//        }

        dueDate = view;
    }


    public static GoalDetailsFragment newInstance(String param3) {
        GoalDetailsFragment frag = new GoalDetailsFragment();
        Bundle args = new Bundle();
        args.putString(GOAL_UUID, param3);
        Log.d("GOALS", "GOAL_UUID "+  args.get(GOAL_UUID).toString()+ "  param3 "+ param3);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("GOALS", "onCreateView: details ");
        return inflater.inflate(R.layout.fragment_goal_details, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final DatePickerFragment fragment = DatePickerFragment.newInstance(this);
        final String uuId = getArguments().get(GOAL_UUID).toString();
        final DialogFragment colorFrag = ColorPickerAlertDialog.newInstance(getResources().getString(R.string.label_color_picker_dialog), uuId, this);
        final ImageView clearDueDateButton =(ImageView) view.findViewById(R.id.remove_date);
        final TextView numberOfTasks =(TextView) view.findViewById(R.id.number_oftasks);
        final TextView percentageComplete =(TextView) view.findViewById(R.id.percentage_complete);
        final NotesDialogFragment fragmentNotes = NotesDialogFragment.newInstance(getResources().getString(R.string.notes_dialog_title), uuId, this);
        Log.d("GOALS", "onViewCreated: GOALSDETAILSFRAGMENT.java");
        final Button categoryListSelection =(Button) view.findViewById(R.id.category_list_Selection);


        try{
            realm = Realm.getDefaultInstance();
            TaskModel subt = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
            Log.d("GOALS", "category of goal is"+ subt.getTaskCategory());
            categoryListSelection.setText(Html.fromHtml(getResources().getString(R.string.category_list_prefix_in_list)+"&#160;" +"<font color=\"#0645AD\"<b>"+subt.getTaskCategory().getName()+"</b></font>") );
        }finally{
              realm.close();
        }

        categoryListSelection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast toast = Toast.makeText(getView().getContext(), getResources().getString(R.string.toast_cant_change_list), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

//
//        //populate Category List array
//        List<String> spinnerArray=  new ArrayList<String>();
//        try{
//            realm = Realm.getDefaultInstance();
//
//            RealmResults<ListCategory> results =realm.where(ListCategory.class).findAllSorted("name");
//
//            for(ListCategory cat: results)
//            {
//                if(null!= cat.getName()){
//                    if (!cat.getName().equalsIgnoreCase(getResources().getString(R.string.category_Inbox)) &&!cat.getName().equalsIgnoreCase(getResources().getString(R.string.category_Project)) )
//                    {
//                        spinnerArray.add(cat.getName().toString());
//                    }
//                }
//
//            }
//
//        }finally{
//            realm.close();
//        }
//
//
//        //simple_selectable_list populated with the category list items
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, spinnerArray);
//        projectSelection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(getContext())
//                        .setTitle("Add to List")
//                        .setSingleChoiceItems(adapter,0, new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(final DialogInterface dialog, int which) {
//
//                                // TODO: user specific action
//
//                                ListView lw = ((AlertDialog)dialog).getListView();
//                                lw.setItemChecked(which, true);
//                                final Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
//
//                                realm = Realm.getDefaultInstance();
//                                realm.executeTransactionAsync(new Realm.Transaction() {
//                                    @Override
//                                    public void execute(Realm realm) {
//
//
//                                        String projectName =checkedItem.toString();
//
//                                        Log.d("GOALS", "project name selected is  " + projectName);
//
//                                        TaskModel sub = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
//                                        String goalID= sub.getParentGoalId();
//                                        TaskModel goal = realm.where(TaskModel.class).equalTo("id",goalID ).findFirst();
//                                        //if user is changing the project context I.e. adding a different project than original, first remove the task from the original project
//                                        Log.d("GOALS", "after sub  " + sub.getName());
//                                        try{
//                                            if (!checkedItem.equals(null)){
//                                                //GoalModel goal = sub.getGoal();
//                                                if(null!= goal){
//                                                    goal.getTasks().remove(sub);
//                                                    Log.d("GOALS", " the subgals row is"+goal.getTasks() );
//                                                }
//                                            }
//
//                                        }finally {
//
//                                            if (!checkedItem.equals(null)) {
//
//                                                Log.d("GOALS", "in update project name  ");
////                                                GoalModel goalModel = realm.where(GoalModel.class).equalTo("name", sub.getGoal().getId()).findFirst();
////                                                goalModel.getSubgoals().remove(sub);
//                                                //now add task to the new project Object
//                                                TaskModel goalModelNew = realm.where(TaskModel.class).equalTo("name", projectName).findFirst();
//                                                goalModelNew.getTasks().add(sub);
//                                                //now add the new project to the Task Object
//                                                sub.setParentGoalId(goalModelNew.getId());
//                                            }
//                                        }
//
//                                    }},new Realm.Transaction.OnSuccess() {
//                                    @Override
//                                    public void onSuccess() {
//                                        Log.d("GOALS", "onSuccess: ");
//                                        if(null != checkedItem){
//
//                                            TaskModel sub = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
//                                            TaskModel goal = realm.where(TaskModel.class).equalTo("id", sub.getParentGoalId()).findFirst();
//                                            clearProjectButton.setVisibility(View.VISIBLE);
//                                            if (0 == goal.getLabelColor()||-1 == goal.getLabelColor()) {
//                                                projectSelection.setText(checkedItem.toString());
//                                                Utilities.setRoundedDrawable(getContext(),projectSelection, Color.LTGRAY, Color.LTGRAY);
//                                                projectSelection.setTextColor(taskDueDates.getTextColors().getDefaultColor());
//                                                //projectSelection.setAlpha(1f);
//                                            }else{
//                                                projectSelection.setText(checkedItem.toString());
//                                                Utilities.setRoundedDrawable(getContext(),projectSelection, goal.getLabelColor(), goal.getLabelColor());
//                                                projectSelection.setTextColor(getResources().getColor(R.color.colorWhite));
//                                                projectSelection.setTypeface(null, Typeface.BOLD );
//                                                //projectSelection.setAlpha(1f);
//                                            }
//                                            Log.d("GOALS", "default colour button " );
////
//
//
//
//                                        }
////
//                                        realm.close();
//
//                                    }
//                                }, new Realm.Transaction.OnError() {
//                                    @Override
//                                    public void onError(Throwable error) {
//                                        Log.d("GOALS", "onError: "+ error);
//                                        realm.close();
//                                    }
//                                });
//
//                                dialog.dismiss();
//                            }
//                        })
//                        .create().show();
//            }
//        });
//



        clearDueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        TaskModel sub = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
                        sub.setDueDate(null);
                        Log.d("GOALS", "in the delete due date "+ sub.getDueDate());

                    }},new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        goalDueDate.setText("");
                        Log.d("GOALS", "onSuccess: ");
                        dueDate=null;
                        clearDueDateButton.setVisibility(View.INVISIBLE);
                        realm.close();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.d("GOALS", "onError: "+ error);
                        realm.close();
                    }
                });
            }
        });

        goalReason = (TextView) view.findViewById(R.id.addReason);
        Log.d("GOALS", "intiliazed the goalDueDate ");
        goalReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNotes.show(getFragmentManager(), "Goal Notes");
                     }
        });

        ImageView editNotesIcon = (ImageView) view.findViewById(R.id.add_goal_notes);
        editNotesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNotes.show(getFragmentManager(), "Goal Notes");
            }
        });

        float subGoalSize =0;
        float subTasks =0;
        float subGoalComplete=0;
        float subTasksComplete=0;
        float progress=0;
        try{
            realm =Realm.getDefaultInstance();
            TaskModel sub = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();

            if(null!=sub.getTasks()){
                subGoalSize =sub.getTasks().size();
                subGoalComplete = sub.getTotalTaskComplete();
                for (TaskModel r : sub.getTasks()) {

                    if (r.getSubTaskCount()>0){
                        subTasks= subTasks + r.getSubTaskCount();
                        subTasksComplete=  subTasksComplete + r.getTotalSubTaskComplete();
                    }
                }

            }

        }finally{
            realm.close();
        }

        if(subGoalSize>0){
             progress =  ((subTasksComplete) +(subGoalComplete))/((subTasks)+(subGoalSize)) * 100;
        }


        percentageComplete.setText((int)progress +" %" );


        numberOfTasks.setText(Html.fromHtml("<font color=\"#0645AD\"<b>"+ (int)subGoalSize +"</b></font>"+  " Task(s) and "+"<font color=\"#0645AD\"<b>"+(int)subTasks +"</b></font>"+  " Subtask(s) " ));
        numberOfTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changedTab = (ChangeTabs) getActivity();
                changedTab.onTabChange(1);
            }
        });


        colourLabelButton = view.findViewById(R.id.color_label_button);

        tagIcon = view.findViewById(R.id.add_project_label_color);
        tagIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //open the colour picker dialog fragment for user to pick colours

                colorFrag.show(getFragmentManager(), " Goal Color");

            }
        });

        Button selectColour = view.findViewById(R.id.pick_colour);
        selectColour.setAlpha(0.38F);
        selectColour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //open the colour picker dialog fragment for user to pick colours
                colorFrag.show(getFragmentManager(), "Goal Color");

            }
        });

        colourLabelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open the colour picker dialog fragment for user to pick colours
                colorFrag.show(getFragmentManager(), "Goal Color");

            }
        });

        //if user has already set the label colour in db, then display it in the colourLabelButton view
        try {
            realm = Realm.getDefaultInstance();
            final TaskModel mGoal = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
            int selectedLabelColor = mGoal.getLabelColor();

            if (0 != selectedLabelColor) {
                colourLabelButton.setColorFilter(selectedLabelColor);
                Log.d("GOALS", "selected color from db ");
            } else {
                colourLabelButton.setColorFilter(Color.TRANSPARENT);
                Log.d("GOALS", "default ");
            }

        } finally {
            realm.close();
        }


        goalDueDate = (EditText) view.findViewById(R.id.add_task_ending);
        Log.d("GOALS", "intiliazed the goalDueDate ");
        goalDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.show(getFragmentManager(), "Goal Date");
            }
        });

        imageCalendar = (ImageView) view.findViewById(R.id.add_task_date);
        imageCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.show(getFragmentManager(), "Goal Date");
            }
        });

        //if we are accessing the this fragment via the EditGoals Activity, then grab the goal data from realm and put in appropriate views, else skip
        if (EditGoalActivity.class.getName().contains(getActivity().getLocalClassName())) {

            try {

                realm = Realm.getDefaultInstance();
                //realm.executeTransactionAsync(new Realm.Transaction() {
                //   @Override
                //    public void execute(Realm realm) {
                TaskModel goalModel = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
                //                    goalName.setText(goalModel.getName());
                labelColorFromDB = goalModel.getLabelColor();
                if (null != goalModel.getDueDate()) {

                    String dateToDisplay = Utilities.parseDateForDisplay(goalModel.getDueDate());
                    goalDueDate.setText(dateToDisplay);
                    dueDate = goalModel.getDueDate();
                    clearDueDateButton.setVisibility(View.VISIBLE);
                }else{
                    clearDueDateButton.setVisibility(View.INVISIBLE);
                }

                goalReason.setText(goalModel.getReason());
                //               spinner1.setSelection(adapter1.getPosition(goalModel.getPriority()));
                // spinner.setSelection(adapter.getPosition(goalModel.getType()));

            //       });
        }finally{

            realm.close();
        }
    }






    }


    /**
     * Triggered from addGaolactivity when the "save" check mark is pressed for the goal
     * This method saves all the goal form data to realm
     */
    public void addGoalDetailsToRealm(final String goalTitle){

        final String uuId = getArguments().get(GOAL_UUID).toString();

        Log.d("GOALS", "in addGoalDetailsToRealm to realm in fragment ");


            long mDate = System.currentTimeMillis();
            SimpleDateFormat mSdf = new SimpleDateFormat("MMM MM dd,yyy h:mm a");
            final String mDateString = mSdf.format(mDate);

            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    TaskModel goalModel = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
                goalModel.setName(goalTitle);
                goalModel.setTime(mDateString);
                goalModel.setReason(goalReason.getText().toString());
                goalModel.setTimeStamp(System.currentTimeMillis());
                goalModel.setDueDate(dueDate);
                goalModel.setDueDateNotEmpty(dueDate);
                Log.d("GOALS", "adding goal details into realm");
                    Log.d("GOALS", "due date is"+ dueDate);


                }
            });
            realm.close();
        Log.d("GOALS", "addGoalDetailsToRealm2: ");
//            Intent addGoalIntent = new Intent(getContext(), GoalListActivity.class);
//            startActivity(addGoalIntent);
    }




}


