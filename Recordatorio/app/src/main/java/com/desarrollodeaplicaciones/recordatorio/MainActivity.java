package com.desarrollodeaplicaciones.recordatorio;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.desarrollodeaplicaciones.recordatorio.model.Note;
import com.desarrollodeaplicaciones.recordatorio.model.NotesDBHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity  extends AppCompatActivity  implements DialogForNote.DialogAddNoteListener {
    private static final String TAG = MainActivity.class.getName();

    private NotesDBHelper mDbHelper;
    private List<Note> mAllNotes;
    private ArrayList<String> mNotesTitles;
    private ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //Instanciamos el DBHelper
        mDbHelper = new NotesDBHelper(this);

        //Preparamos el ListView
        preaparListView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Aqui se manejan las opciones del menu del Action Bar

        //Obtenemos el ID de la opción de Menu
        int id = item.getItemId();

        //Comparamos el ID para saber que opcion es la que el user selecciono
        if (id == R.id.action_settings) {
            showAddNoteDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void preaparListView(){
        //Instanciamos el ListView
        ListView mListView = (ListView) findViewById(R.id.lvNotes);

        // Definimos un array con los valores de los titulos de las notas
        // que presentaremos en el ListView
        resfreshNotes();

        //Definimos el Adaptar el cual se ecaragará de mapear con el UI los difernetes
        // titulos de las notas
        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, mNotesTitles);


        // Asiganmos el adapter al ListView, para presentar los datos en pantalla
        if (mListView != null) {
            mListView.setAdapter(mAdapter);
            //Definimos un OnItemClickListener para saber cuando el usuario ha seleccionado
            // una Nota y poder navegar a la vista de detalle de la nota
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Note selNote = mAllNotes.get(position);
                    Log.d(TAG, "Selected Note: " + selNote.getTitle());
                    //Instanciamos el DialogForNote
                    DialogForNote newAddNote = new DialogForNote();

                    //Seteamos la nota que deseamos actualizar
                    newAddNote.setmNoteForUpd(selNote);

                    //Mostramos el DialogForNote asignadole el tag "addnote"
                    newAddNote.show(getSupportFragmentManager(), "addnote");

                }
            });

            //Definimos un  OnItemLongClickListener con el cual conseguiremos borrar la nota
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG, "onItemLongClick" + mAllNotes.get(position).getTitle());
                    //Obtenemos la referencia a la nota sobre la cual el usuario
                    // esta haciendo LongClick
                    Note delNote = mAllNotes.get(position);
                    if (mDbHelper != null){
                        //Borramos la nota de la base de datos
                        mDbHelper.deleteNote(delNote);
                        //Refrescamos la lista de notas
                        resfreshNotes();
                        //Devolvemos true para evitar que se ejecute el OnItemClickListener
                        return true;
                    }

                    //Devolvemos false para que se ejecute el OnItemClickListener ya que no
                    // se ha logrado el borrado de la nota
                    return false;
                }
            });
        }
    }

    private void resfreshNotes(){
        //Cargamos todas las notas
        mAllNotes = mDbHelper.getAllNotes();

        //Iteramos sobre todas las notas para pasar los titulos a un Array String
        int idx = 0;
        mNotesTitles = new ArrayList<String>();
        for (Note aNote : mAllNotes) {
            mNotesTitles.add(aNote.getTitle());
            idx++;
        }
        //Si el Adapter esta instanciado notificamos los cambios
        if (mAdapter != null){
            //Limpiamos todos los datos
            mAdapter.clear();
            //Agreamos los nuevos datos
            mAdapter.addAll(mNotesTitles);
            //Notificamos los cambios
            mAdapter.notifyDataSetChanged();
        }
    }

    private void showAddNoteDialog(){
        Log.d(TAG, "showAddNoteDialog");
        //Instanciamos el DialogForNote
        DialogForNote newAddNote = new DialogForNote();
        //Mostramos el DialogForNote asignadole el tag "addnote"
        newAddNote.show(getSupportFragmentManager(), "addnote");
    }

    @Override
    public void onDialogAddNote(Note newNote) {
        Log.d(TAG, "onDialogAddNote");
        if (mDbHelper != null){
            //Insertamos el nuevo registro
            mDbHelper.insertNote(newNote);
            //Refrescamos el ListView
            resfreshNotes();
        }
    }

    @Override
    public void onDialogCancel() {
        Log.d(TAG, "onDialogCancel");
    }

    @Override
    public void onDialogUpdNote(Note updNote) {
        Log.d(TAG, "onDialogUpdNote");
        if (mDbHelper != null){
            //Insertamos el nuevo registro
            mDbHelper.updateNote(updNote);
            //Refrescamos el ListView
            resfreshNotes();
        }
    }
}
