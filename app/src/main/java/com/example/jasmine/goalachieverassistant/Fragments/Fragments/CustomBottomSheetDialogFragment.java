package com.example.jasmine.goalachieverassistant.Fragments.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jasmine.goalachieverassistant.Models.ListCategory;
import com.example.jasmine.goalachieverassistant.Models.TaskModel;
import com.example.jasmine.goalachieverassistant.R;
import com.example.jasmine.goalachieverassistant.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import io.realm.Realm;


public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment implements DatePickerFragment.DatePickerFragmentListener {

    private static final String PARENT_UUID = "PARENT_UUID";
    private static final String PARENT_NAME = "PARENT_NAME";
    private static final String LIST_CATEGORY = "LIST_CATEGORY";
    private static final String IS_CHILD_SUB_TASK = "IS_CHILD_SUB_TASK";
    private static final String IS_GOAL = "IS_GOAL";

    private Date dueDate =null;
    ImageView imageCalendar;
    private Realm realm;
    Dialog mDialog=null;
    private String parentUuId="";
    private String listCategory="";
    private Boolean isChildSubTask=false;
    EditText addTask;
    private boolean isGoal=false;
    private boolean taskIsAGoal=false;
    @Override
    public void onDateSet(Date view) {
        Log.d("GOALS", "onDateset "+ view);
        if(null != view ){

            String dateToDisplay = Utilities.parseDateForDisplay(view);


        }
//        else{
//            goalDueDate.setText(R.string.no_due_date);
//
//
//        }

        dueDate = view;
    }

    public static CustomBottomSheetDialogFragment newInstance(@Nullable String parentUUID , Boolean isChildSubTask, @Nullable String parentName,@Nullable Boolean isGoal, @Nullable String listCategory) {
        CustomBottomSheetDialogFragment frag = new CustomBottomSheetDialogFragment();
        Bundle args = new Bundle();
        Log.d("GOALS"," in new instance " +parentUUID+ isChildSubTask );
        args.putString(PARENT_UUID, parentUUID);
        args.putBoolean(IS_CHILD_SUB_TASK,isChildSubTask);
        args.putBoolean(IS_GOAL,isGoal);
        args.putString(PARENT_NAME,parentName );
        args.putString(LIST_CATEGORY,listCategory);
        frag.setArguments(args);
        Log.d("GOALS", "parent new instance " + args.getString(PARENT_UUID));
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_custom_bottom_sheet_dialog, container, false);
        this.mDialog = getDialog();
        this.mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.mDialog.setCanceledOnTouchOutside(false);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final DatePickerFragment fragment =DatePickerFragment.newInstance(this);
        final String parentName=getArguments().get(PARENT_NAME).toString();

        parentUuId = getArguments().get(PARENT_UUID).toString();
        isGoal= getArguments().getBoolean(IS_GOAL);
        isChildSubTask = getArguments().getBoolean(IS_CHILD_SUB_TASK);
        addTask = (EditText)view.findViewById(R.id.taskName);
        Log.d("GOALS", "onViewCreated: list category???????????? "+ getArguments().get(LIST_CATEGORY) + "    ");

        if(null != getArguments().get(LIST_CATEGORY)){
            listCategory = getArguments().get(LIST_CATEGORY).toString();
        }


        if(true ==isGoal){
            addTask.setHint("What's your Project?");
        }
        else if (false == isChildSubTask) {
            addTask.setHint("What's your task?");
        } else {
            addTask.setHint("What's your subtask for \""+ parentName+"\" ?");
        }

       // taskName = (EditText) view.findViewById(R.id.taskName);


        ImageView send = (ImageView) view.findViewById(R.id.send_task_to_list);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editText = addTask.getText().toString();
                if (!TextUtils.isEmpty(editText)) {
                    if(true ==isGoal){
                        addProjectToRealm();
                    }
                    else if(false ==isChildSubTask){
                        addParentTaskToRealm();
                    }else{
                        addChildSubTaskToRealm();
                    }
                    mDialog.dismiss();
                    dueDate =null;
                }

            }
        });


        imageCalendar = (ImageView) view.findViewById(R.id.add_task_date);
        imageCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.show(getFragmentManager(), "Goal Date");
            }
        });




        EditText.OnEditorActionListener newTaskListener =new EditText.OnEditorActionListener(){
            public boolean onEditorAction(final TextView exampleView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    realm = Realm.getDefaultInstance();
//                    realm.executeTransactionAsync(new Realm.Transaction() {
//                                                      @Override
//                                                      public void execute(Realm realm) {
                                                         String editText = addTask.getText().toString();
                                                          if (!TextUtils.isEmpty(editText)) {
                                                              if(true ==isGoal){
                                                                  addProjectToRealm();
                                                              }
                                                              else if (false == isChildSubTask) {
                                                                  addParentTaskToRealm();
                                                              } else {
                                                                  addChildSubTaskToRealm();
                                                              }

//                                                          }
//                                                      }
//                                                  },
//                            new Realm.Transaction.OnSuccess() {
//                                @Override
//                                public void onSuccess() {
//                                    Log.d("GOALS", "onSuccess: ");
//                                    addTask.setText("");
//                                    dueDate =null;
//
//                                }
//                            }, new Realm.Transaction.OnError() {
//                                @Override
//                                public void onError(Throwable error) {
//                                    Log.d("GOALS", "onError: ");
//                                    dueDate =null;
//                                    addTask.setText("");
                               }
 //                           });
//                    realm.close();

//match this behavior to your 'Send' (or Confirm) button
                }return true;
            }
        };

        addTask.setOnEditorActionListener(newTaskListener);


    }


    /**
     * Triggered from Task fragment. Adds a Task to the Parent Goal List
     */
    public void addParentTaskToRealm(){

        final String uuId = UUID.randomUUID().toString();

        Log.d("GOALS", "in addGoalDetailsToRealm to realm in fragment ");


        long mDate = System.currentTimeMillis();
        SimpleDateFormat mSdf = new SimpleDateFormat("MMM MM dd,yyy h:mm a");
        final String mDateString = mSdf.format(mDate);

        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {


                Log.d("GOALS", "goal UUID IS   "+ parentUuId.length());
                realm.createObject(TaskModel.class, uuId)
                        .setName(addTask.getText().toString());
                TaskModel task = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();

                if(parentUuId.length()> 0){
                    Log.d("GOALS", "in goal"+ uuId);
                    TaskModel parent = realm.where(TaskModel.class).equalTo("id", parentUuId).findFirst();
                    if(parent.isGoal()){
                        task.setParentGoalId(parent.getId());
                        parent.getTasks().add(task);
                        ListCategory cat = realm.where(ListCategory.class).equalTo("name", getResources().getString(R.string.category_Project)).findFirst();
                        task.setTaskCategory(cat);
                        cat.getTaskList().add(task);
                    }
                }else{
                    Log.d("GOALS", "in task"+ listCategory);
                    if(null!=listCategory){
                        ListCategory cat = realm.where(ListCategory.class).equalTo("name", listCategory).findFirst();
                        task.setTaskCategory(cat);
                        cat.getTaskList().add(task);
                    }else{
                        if(getView().getContext()!=null) {
                            Toast toast = Toast.makeText(getView().getContext(), "Oops, something went wrong!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                }

                if(null !=dueDate )
                {
                    task.setDueDate(dueDate);
                    task.setDueDateNotEmpty(dueDate);
                    Log.d("GOALS", "Due dates matched! added subgoal due date");
                }


//                goalModel.setTime(mDateString);
//                goalModel.setTimeStamp(System.currentTimeMillis());
//                goalModel.setDueDate(dueDate);
//                goalModel.setDueDateNotEmpty(dueDate);



            }},
                new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.d("GOALS", "onSuccess: ");
                    if (getContext()!=null) {
                        Toast toast = Toast.makeText(getView().getContext(), "Your task \"" + addTask.getText().toString() + "\" was created successfully!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    addTask.setText("");
                    dueDate =null;
                    realm.close();

                }
                },
                new Realm.Transaction.OnError() {
                @Override
                public void onError (Throwable error){
                    Log.d("GOALS", "onError: ");
                    if (getContext()!=null) {
                        Toast toast = Toast.makeText(getView().getContext(), "Oops, something went wrong.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    dueDate = null;
                    addTask.setText("");
                    realm.close();
                }

        });

    }


    /**
     * Triggered from Goal fragment. Adds a Task as a Goal
     */
    public void addProjectToRealm(){

        final String uuId = UUID.randomUUID().toString();

        Log.d("GOALS", "in addGoalDetailsToRealm to realm in fragment ");


        long mDate = System.currentTimeMillis();
        SimpleDateFormat mSdf = new SimpleDateFormat("MMM MM dd,yyy h:mm a");
        final String mDateString = mSdf.format(mDate);

        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm realm) {


                                              Log.d("GOALS", "goal UUID IS"+ uuId);
                                              realm.createObject(TaskModel.class, uuId)
                                                      .setName(addTask.getText().toString());
                                              TaskModel goal = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
                                              goal.setAsGoal(true);

                                              ListCategory cat = realm.where(ListCategory.class).equalTo("name", getResources().getString(R.string.category_Project)).findFirst();
                                              Log.d("GOALS", "goal cat IS "+ cat);

                                              cat.getTaskList().add(goal);
                                              goal.setTaskCategory(cat);
                                              if(null !=dueDate )
                                              {
                                                  goal.setDueDate(dueDate);
                                                  goal.setDueDateNotEmpty(dueDate);
                                                  Log.d("GOALS", "Due dates matched! added Goal due date");
                                              }


//                goalModel.setTime(mDateString);
//                goalModel.setTimeStamp(System.currentTimeMillis());
//                goalModel.setDueDate(dueDate);
//                goalModel.setDueDateNotEmpty(dueDate);



                                          }},
                new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.d("GOALS", "onSuccess: ");
                        if (getContext()!=null) {
                            Toast toast = Toast.makeText(getView().getContext(), "Your goal \"" + addTask.getText().toString() + "\" was created successfully!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        addTask.setText("");
                        dueDate =null;
                        realm.close();

                    }
                },
                new Realm.Transaction.OnError() {
                    @Override
                    public void onError (Throwable error){
                        Log.d("GOALS", "onError: add goal");
                        if (getContext()!=null) {
                            Toast toast = Toast.makeText(getView().getContext(), "Oops, something went wrong.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        dueDate = null;
                        addTask.setText("");
                        realm.close();
                    }

                });

    }

    /**
     * Triggered from Task fragment. Adds a Subtask to the Parent Task List
     */
    public void addChildSubTaskToRealm(){

        final String uuId = UUID.randomUUID().toString();

        Log.d("GOALS", "in addGoalDetailsToRealm to realm in fragment ");


        long mDate = System.currentTimeMillis();
        SimpleDateFormat mSdf = new SimpleDateFormat("MMM MM dd,yyy h:mm a");
        final String mDateString = mSdf.format(mDate);

        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {


                realm.createObject(TaskModel.class, uuId)
                        .setName(addTask.getText().toString());
                TaskModel subTask = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
                TaskModel task = realm.where(TaskModel.class).equalTo("id", parentUuId).findFirst();
//                ListCategory cat = realm.where(ListCategory.class).equalTo("name", getResources().getString(R.string.category_Project)).findFirst();
//                cat.getTaskList().add(subTask);
//                subTask.setTaskCategory(cat);
                task.getChildList().add(subTask);
                subTask.setParentTaskId(task.getId());



                if(task.getParentGoalId()!=null){

                    ListCategory cat = realm.where(ListCategory.class).equalTo("name", getResources().getString(R.string.category_Project)).findFirst();
                    subTask.setTaskCategory(cat);
                    cat.getTaskList().add(subTask);
                }
                if(null !=dueDate )
                {
                    subTask.setDueDate(dueDate);
                    subTask.setDueDateNotEmpty(dueDate);
                    Log.d("GOALS", "Due dates matched! added subgoal due date");
                }


                Log.d("GOALS", "adding goal details into realm");
                //               Log.d("GOALS", "due date is"+ dueDate);

            }},
                new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {

                    if (getContext()!=null){
                        Toast toast = Toast.makeText(getContext(), "Your subtask \""+ addTask.getText().toString()+ "\" was created successfully!", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    Log.d("GOALS", "onSuccess: ");
                    addTask.setText("");
                    dueDate =null;
                }
                },
                new Realm.Transaction.OnError() {
                @Override
                public void onError (Throwable error){
                    Log.d("GOALS", "onError: ");
                    if (getContext()!=null){
                        Toast toast = Toast.makeText(getContext(), "Oops, something went wrong!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    dueDate = null;
                    addTask.setText("");
            }

            });
        realm.close();
        }
    }


//
//    @Override
//    public void onDismiss(DialogInterface dialog)
//    {
//        super.onDismiss(dialog);
//        // this works fine but fires one time too often for my use case, it fires on screen rotation as well, although this is a temporarily dismiss only
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return new Dialog(getContext(), getTheme()){
//            @Override
//            public void onBackPressed() {
//                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
//                    getActivity().getSupportFragmentManager().popBackStack();
//                }
//            }
//
//        };
//    }



