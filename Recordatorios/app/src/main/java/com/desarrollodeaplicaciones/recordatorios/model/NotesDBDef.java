package com.desarrollodeaplicaciones.recordatorios.model;

/**
 * Created by Irbing on 18/04/2017.
 */

public class NotesDBDef {
    //Nombre del esquema de Base de Datos
    public static final String DATABASE_NAME = "NotasDB";
    //Version de la Base de Datos (Este parámetro es importante  )
    public static final int DATABASE_VERSION = 1;

    //Una clase estatica en la que se definen las propiedaes que determinan la tabla Notes
    // y sus 4 columnas
    public static class NOTES {
        //Nombre de la tabla
        public static final String TABLE_NAME = "notes";
        //Nombre de las Columnas que contiene la tabla
        public static final String ID_COL = "id";
        public static final String TITLE_COL = "title";
        public static final String DESCRIP_COL = "description";
        public static final String DATE_COL = "fecha";
    }

    //Setencia SQL que permite crear la tabla Notes
    public static final String NOTES_TABLE_CREATE =
            "CREATE TABLE " + NOTES.TABLE_NAME + " (" +
                    NOTES.ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOTES.TITLE_COL + " TEXT, " +
                    NOTES.DATE_COL + " TEXT, " +
                    NOTES.DESCRIP_COL + " TEXT);";

    //Setencia SQL que permite eliminar la tabla Notes
    public static final String NOTES_TABLE_DROP = "DROP TABLE IF EXISTS " + NOTES.TABLE_NAME;

}
