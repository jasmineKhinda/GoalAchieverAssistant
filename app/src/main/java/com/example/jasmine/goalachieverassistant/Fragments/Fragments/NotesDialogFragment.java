package com.example.jasmine.goalachieverassistant.Fragments.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jasmine.goalachieverassistant.Models.GoalModel;
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
            GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", uuId).findFirst();
            notesFromDb = goalModel.getReason();
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




        //put the color palette inside an alert dialog for user to choose from
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setView(subView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {

                        realm = Realm.getDefaultInstance();
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                GoalModel goalModel = realm.where(GoalModel.class).equalTo("id", uuId).findFirst();

                                    goalModel.setReason(goalNotesText.getText().toString());

                            }
                        });
                        realm.close();
                        notifyAddNotesListener(goalNotesText.getText().toString());
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
