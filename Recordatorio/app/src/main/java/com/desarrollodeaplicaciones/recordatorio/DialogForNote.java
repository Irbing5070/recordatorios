package com.desarrollodeaplicaciones.recordatorio;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.desarrollodeaplicaciones.recordatorio.model.Note;

/**
 * Created by Irbing on 18/04/2017.
 */

public class DialogForNote extends DialogFragment {
    private static final String TAG = DialogForNote.class.getName();

    /*Declaramos los EditText*/
    private EditText mTitle;
    private EditText mDate;
    private EditText mDescription;

    /*Declaramos una instancia de Note para albergar los datos de una nota que se desee actualizar*/
    private Note mNoteForUpd;

    /* La Activity que instancia este Dialogo debe implementar la siguiente interfaz
     * por medio de la cual se logra la comiunicacion entre el dialogo y la activity  */
    public interface DialogAddNoteListener {
        //Método que se dispara cuando se intenta agregar uan nueva nota
        void onDialogAddNote(Note newNote);
        //Método que se dispara cuando se cancela el dialogo de agregar nueva nota
        void onDialogCancel();
        //Método que se dispara cuando se desea actualizar una nota
        void onDialogUpdNote(Note newNote);
    }

    // Declaramos una instancia de la interfaz antes descrita para usarla para enviar
    // la info al Activity Principal
    DialogAddNoteListener mListener;

    // Sobre escribimos el metodo Fragment.onAttach() para instanciar method al DialogAddNoteListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verificamos se la MainActivity implementa la interfaz callback DialogAddNoteListener
        try {
            // Instanciamos el listener DialogAddNoteListener para que pueda comunucarse luego
            // con la MainActivity
            mListener = (DialogAddNoteListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Obtenemos una referencia al layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Inflamos la vista del Dialogo
        View rootView = inflater.inflate(R.layout.dialog_addnote, null);
        mTitle = (EditText) rootView.findViewById(R.id.title);
        mDate = (EditText) rootView.findViewById(R.id.date);
        mDescription = (EditText) rootView.findViewById(R.id.description);
        //Seteamos los valores de la nota que se desea actualizar (si aplica)
        setDataToUpdate();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(rootView)
                // Add action buttons
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null){
                            if (mNoteForUpd != null) {
                                //Actualizamos las propiedades de la nota
                                mNoteForUpd.setTitle(mTitle.getText().toString());
                                mNoteForUpd.setDate(mDate.getText().toString());
                                mNoteForUpd.setDescription(mDescription.getText().toString());
                                //Ejecutamos el método onDialogUpdNote que esta implementado en el MainActivity
                                mListener.onDialogUpdNote(mNoteForUpd);
                            } else {
                                //Instanciamos la nueva nota
                                Note aNote = new Note(mTitle.getText().toString(), mDate.getText().toString(), mDescription.getText().toString());
                                //Ejecutamos el método onDialogAddNote que esta implementado en el MainActivity
                                mListener.onDialogAddNote(aNote);
                            }
                        }
                        //Cancelamos el Dialogo
                        DialogForNote.this.getDialog().cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null){
                            //Ejecutamos el método onDialogCancel que esta implementado en el MainActivity
                            mListener.onDialogCancel();
                        }
                        //Cancelamos el Dialogo
                        DialogForNote.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public Note getmNoteForUpd() {
        return mNoteForUpd;
    }

    public void setmNoteForUpd(Note mNoteForUpd) {
        this.mNoteForUpd = mNoteForUpd;
    }

    private void setDataToUpdate(){
        if (mNoteForUpd != null){
            mTitle.setText(mNoteForUpd.getTitle());
            mDate.setText(mNoteForUpd.getDate());
            mDescription.setText(mNoteForUpd.getDescription());
        }
    }
}
