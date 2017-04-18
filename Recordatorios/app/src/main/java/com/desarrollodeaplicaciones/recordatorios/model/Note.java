package com.desarrollodeaplicaciones.recordatorios.model;

/**
 * Created by Irbing on 18/04/2017.
 */

public class Note {
    private int id;
    private String title;
    private String date;
    private String description;

    public Note(){

    }

    public Note(String pTitle, String pDate, String pDescrip){
        this.setTitle(pTitle);
        this.setDate(pDate);
        this.setDescription(pDescrip);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
