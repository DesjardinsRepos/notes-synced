package com.example.notes_synced;

public class Note {

    private String title;
    private String body;

    public Note(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Note() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    /*
    @Override
    public String toString() {
        return "Note{" + "title='" + title + '\'' + ", body='" + body +'\'' + '}';
    }

     */
}
