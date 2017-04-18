package com.desarrollodeaplicaciones.recordatorios.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irbing on 18/04/2017.
 */

public class NotesDBHelper extends SQLiteOpenHelper {
    //Definimos in Contructor para Instanciar el Databse Helper
    public NotesDBHelper(Context context) {
        super(context, NotesDBDef.DATABASE_NAME, null, NotesDBDef.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creamos las tablas en la Base de datos
        db.execSQL(NotesDBDef.NOTES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //El método onUpgrade se ejecuta cada vez que recompilamos e instalamos la app con un
        //nuevo numero de version de base de datos (DATABASE_VERSION), de tal mamera que en este
        // método lo que hacemos es:
        // 1. Con esta sentencia borramos la tabla "notes"
        db.execSQL(NotesDBDef.NOTES_TABLE_DROP);

        // 2. Con esta sentencia llamamos de nuevo al método onCreate para que se cree de nuevo
        //la tabla "notes" la cual seguramente al cambair de versión ha tenido modifciaciones
        // en cuanto a su estructura de columnas
        this.onCreate(db);
    }


    /*
    * OPERACIONES CRUD (Create, Read, Update, Delete)
    * A partir de aqui se definen los metodos para insertar, leer, actualizar y borrar registros de
    * la base de datos (BD)
    * */

    public void insertNote(Note book){
        //Con este método insertamos las notas nuevas que el usuario vaya creando

        // 1. Obtenemos una reference de la BD con permisos de escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Creamos un obejto de tipo ContentValues para agregar los pares
        // de Claves de Columna y Valor
        ContentValues values = new ContentValues();
        values.put(NotesDBDef.NOTES.TITLE_COL, book.getTitle()); // Titulo
        values.put(NotesDBDef.NOTES.DATE_COL, book.getDate()); // Titulo
        values.put(NotesDBDef.NOTES.DESCRIP_COL, book.getDescription()); // Descripción

        // 3. Insertamos los datos en la tabla "notes"
        db.insert(NotesDBDef.NOTES.TABLE_NAME, null, values);

        // 4. Cerramos la conexión comn la BD
        db.close();
    }

    //Obtener uan Nota dado un ID
    public Note getNoteById(int id){
        // Declaramos un objeto Note para instanciarlo con el resultado del query
        Note aNote = null;

        // 1. Obtenemos una reference de la BD con permisos de lectura
        SQLiteDatabase db = this.getReadableDatabase();

        //Definimos un array con los nombres de las columnas que deseamos sacar
        String[] COLUMNS = {NotesDBDef.NOTES.ID_COL, NotesDBDef.NOTES.TITLE_COL, NotesDBDef.NOTES.DATE_COL, NotesDBDef.NOTES.DESCRIP_COL};


        // 2. Contruimos el query
        Cursor cursor =
                db.query(NotesDBDef.NOTES.TABLE_NAME,  //Nomre de la tabla
                        COLUMNS, // b. Nombre de las Columnas
                        " id = ?", // c. Columnas de la clausula WHERE
                        new String[] { String.valueOf(id) }, // d. valores de las columnas de la clausula WHERE
                        null, // e. Clausula Group by
                        null, // f. Clausula having
                        null, // g. Clausula order by
                        null); // h. Limte de regsitros

        // 3. Si hemos obtenido algun resultado entonces sacamos el primero de ellos ya que se supone
        //que ha de existir un solo registro para un id
        if (cursor != null) {
            cursor.moveToFirst();
            // 4. Contruimos el objeto Note
            aNote = new Note();
            aNote.setId(Integer.parseInt(cursor.getString(0)));
            aNote.setTitle(cursor.getString(1));
            aNote.setDate(cursor.getString(2));
            aNote.setDescription(cursor.getString(3));

        }

        // 5. Devolvemos le objeto Note
        return aNote;
    }


    // Obtener todas las notas
    public List<Note> getAllNotes() {
        //Instanciamos un Array para llenarlo con todos los objetos Notes que saquemos de la BD
        List<Note> notes = new ArrayList<Note>();

        // 1. Aramos un String con el query a ejecutar
        String query = "SELECT  * FROM " + NotesDBDef.NOTES.TABLE_NAME;

        // 2. Obtenemos una reference de la BD con permisos de escritura y ejecutamos el query
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. Iteramos entre cada uno de olos registros y agregarlos al array de Notas
        Note aNote = null;
        if (cursor.moveToFirst()) {
            do {
                aNote = new Note();
                aNote.setId(Integer.parseInt(cursor.getString(0)));
                aNote.setTitle(cursor.getString(1));
                aNote.setDate(cursor.getString(2));
                aNote.setDescription(cursor.getString(3));

                // Add book to books
                notes.add(aNote);
            } while (cursor.moveToNext());
        }

        //Cerramos el cursor
        cursor.close();

        // Devolvemos las notas encontradas o un array vacio en caso de que no se encuentre nada
        return notes;
    }


    //Actualizar los datos en una nota
    public int updateNote(Note note) {

        // 1. Obtenemos una reference de la BD con permisos de escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Creamos el objeto ContentValues con las claves "columna"/valor
        // que se desean actualizar
        ContentValues values = new ContentValues();
        values.put(NotesDBDef.NOTES.TITLE_COL, note.getTitle());
        values.put(NotesDBDef.NOTES.DATE_COL, note.getDate());
        values.put(NotesDBDef.NOTES.DESCRIP_COL, note.getDescription());

        // 3. Actualizamos el registro con el método update el cual devuelve la cantidad
        // de registros actualizados
        int i = db.update(NotesDBDef.NOTES.TABLE_NAME, //table
                values, // column/value
                NotesDBDef.NOTES.ID_COL+" = ?", // selections
                new String[] { String.valueOf(note.getId()) }); //selection args

        // 4. Cerramos la conexión a la BD
        db.close();

        // Devolvemos la cantidad de registros actualizados
        return i;
    }

    // Borrar una Nota
    public void deleteNote(Note note) {

        // 1. Obtenemos una reference de la BD con permisos de escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Borramos el registro
        db.delete(NotesDBDef.NOTES.TABLE_NAME,
                NotesDBDef.NOTES.ID_COL+" = ?",
                new String[] { String.valueOf(note.getId()) });

        // 3. Cerramos la conexión a la Bd
        db.close();
    }
}
