package com.example.richard.notetoself;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Richard on 22/03/2016.
 */
public class DialogShowNote extends DialogFragment
{
    private Note mNote;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_show_note, null);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) dialogView.findViewById(R.id.txtDescription);
        txtTitle.setText(mNote.getTitle());
        txtDescription.setText(mNote.getDescription());

        ImageView ivImportant = (ImageView) dialogView.findViewById(R.id.imageViewImportant);
        ImageView ivTodo = (ImageView) dialogView.findViewById(R.id.imageViewTodo);
        ImageView ivIdea = (ImageView) dialogView.findViewById(R.id.imageViewIdea);

        if (!mNote.isImportant())
        {
            ivImportant.setVisibility(View.GONE);
        }
        else
        {
            ivImportant.setVisibility(View.VISIBLE);
        }
        if (!mNote.isTodo())
        {
            ivTodo.setVisibility(View.GONE);
        }
        else
        {
            ivTodo.setVisibility(View.VISIBLE);
        }
        if (!mNote.isIdea())
        {
            ivIdea.setVisibility(View.GONE);
        }
        else
        {
            ivIdea.setVisibility(View.VISIBLE);
        }

        Button btnOK = (Button) dialogView.findViewById(R.id.btnOK);

        builder.setView(dialogView);

        //builder.setMessage("Your Note");

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        return builder.create();
    }

    public void sendNoteSelected(Note noteSelected)
    {
        mNote = noteSelected;
    }
}
