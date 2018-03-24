package com.example.jasmine.goalachieverassistant.Fragments.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.jasmine.goalachieverassistant.EditGoalActivity;
import com.example.jasmine.goalachieverassistant.EditSubTaskActivity;
import com.example.jasmine.goalachieverassistant.EditTaskActivity;
import com.example.jasmine.goalachieverassistant.Models.TaskModel;
import com.example.jasmine.goalachieverassistant.R;

import io.realm.Realm;

/**
 * Created by jasmine on 07/02/18.
 */

public class NotesDialogFragment extends DialogFragment {


        String notesFromDb;
        Realm realm;

    public static NotesDialogFragment newInstance(String title, String goalUUID, NotesDialogFragment.AddNotesListener listener) {
        Log.d("GOALS", "onColorSelected: color RESTARTED ");
        NotesDialogFragment frag = new NotesDialogFragment();
        Bundle args = new Bundle();
        args.putString("Title", title);
        args.putString("GOALUUID", goalUUID);
        frag.setArguments(args);
        frag.setAddNotesListener(listener);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("Title");
        final String uuId = getArguments().getString("GOALUUID");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View subView = inflater.inflate(R.layout.dialog_notes, null);

        //if there user has already has a note saved in DB display it
        try{
            realm = Realm.getDefaultInstance();

            if(EditGoalActivity.class.getName().contains(getActivity().getLocalClassName())){
                TaskModel goalModel = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
                notesFromDb = goalModel.getReason();
                Log.d("GOALS", "onCreateDialog:1 ");
            }else if(EditTaskActivity.class.getName().contains(getActivity().getLocalClassName())){
                TaskModel goalModel = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
                notesFromDb = goalModel.getReason();
                Log.d("GOALS", "onCreateDialog2: ");

            }else if(EditSubTaskActivity.class.getName().contains(getActivity().getLocalClassName())){
                TaskModel goalModel = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();
                notesFromDb = goalModel.getReason();
                Log.d("GOALS", "onCreateDialog3: ");

            }else{
                Log.e("ERROR", "Matching Class not found ");
            }


        }finally{
            realm.close();
        }


        //if user has notes in the db then display it upon open of Edit Note.
       // final EditText taskEditText = new EditText(getActivity());
        final EditText goalNotesText = (EditText) subView.findViewById(R.id.goal_notes_text);
        if (null!= notesFromDb){
            goalNotesText.setText(notesFromDb);
        }
        goalNotesText.setSelection(goalNotesText.getText().length());


        goalNotesText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getDialog().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
            }
        });

        //put the edit notes inside an alert dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setView(subView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {

                        if(EditGoalActivity.class.getName().contains(getActivity().getLocalClassName())){
                            realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    TaskModel goalModel = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();

                                    goalModel.setReason(goalNotesText.getText().toString());

                                }
                            });
                            realm.close();
                            notifyAddNotesListener(goalNotesText.getText().toString());
                        }else if(EditTaskActivity.class.getName().contains(getActivity().getLocalClassName())){
                            realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    TaskModel subGoalModel = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();

                                    subGoalModel.setReason(goalNotesText.getText().toString());

                                }
                            });
                            realm.close();
                            notifyAddNotesListener(goalNotesText.getText().toString());
                        }else if(EditSubTaskActivity.class.getName().contains(getActivity().getLocalClassName())){
                            realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    TaskModel ChildSubGoalModel = realm.where(TaskModel.class).equalTo("id", uuId).findFirst();

                                    ChildSubGoalModel.setReason(goalNotesText.getText().toString());

                                }
                            });
                            realm.close();
                            notifyAddNotesListener(goalNotesText.getText().toString());

                        }else{
                            Log.e("ERROR", "Matching Class not found ");
                        }


                    }
                })
                .setNegativeButton("Cancel", null);





        return dialog.create();
    }

    //Create an interface so that the AlertDialog can communicate the users note added to the Details view, and it will display it
    //dynamically (refresh the view) when the "okay"/"positive" button is pressed in the Alert Dialog. If cancel is pressed note will not be updated.
    private NotesDialogFragment.AddNotesListener mListener;
    public interface AddNotesListener {
        void onNoteAdded(String note);
    }

    public void setAddNotesListener(NotesDialogFragment.AddNotesListener listener) {
        this.mListener = listener;
    }
    protected void notifyAddNotesListener(String note) {
        if(this.mListener != null) {
            Log.d("GOALS", "in notifyAddNotesListener ");
            this.mListener.onNoteAdded(note);
        }else{
            Log.d("GOALS", "in notifyAddNotesListener null");
        }


    }

}
