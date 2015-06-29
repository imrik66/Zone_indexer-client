package com.example.rcdsm.zone_indexer.Entity;

import android.content.Context;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rcdsm on 25/06/2015.
 */
public class Note {

    private int noteId;
    private int ownerId;
    private int zoneId;
    private String title;
    private String content;

    public static Note currentNote = new Note();

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void createNote(Context context, final NoteManagement listener){
        Map<String,String> note = new HashMap<String,String>();
        note.put("createNote","1");
        note.put("title", this.title);
        note.put("text", this.content);
        note.put("zoneId", String.valueOf(this.zoneId));
        note.put("ownerId", String.valueOf(this.ownerId));


        final Note theNote = this;
        String url = "http://pierre-mar.net/Zone_indexer/";
        AQuery aq = new AQuery(context);

        aq.ajax(url,note , JSONObject.class, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject response, AjaxStatus status)
            {
                if(response != null){
                    try {
                        theNote.noteId = response.getInt("id");

                        listener.noteActionSuccess("Note created");
                    }
                    catch(Exception e){
                        listener.noteActionFailed("Failed to create note");
                    }
                }
                else{
                    listener.noteActionFailed("Failed to create note");
                }

            }
        });
    };

    public void updateNote(Context context, final NoteManagement listener){

        Map<String,String> note = new HashMap<String,String>();
        note.put("updateNote", "1");
        note.put("title", this.title);
        note.put("text", this.content);

        final Note theNote = this;
        String url = "http://pierre-mar.net/Zone_indexer/";
        AQuery aq = new AQuery(context);

        aq.ajax(url, note, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject response, AjaxStatus status) {
                if (response != null) {
                    listener.noteActionSuccess("Note updated");
                } else {
                    listener.noteActionFailed("Failed to create note");
                }

            }
        });
    };

    public void deleteNote(Context context, final NoteManagement listener){
        Map<String,String> note = new HashMap<String,String>();
        note.put("deleteNote", "1");
        note.put("id", String.valueOf(this.noteId));

        String url = "http://pierre-mar.net/Zone_indexer/";
        AQuery aq = new AQuery(context);

        aq.ajax(url,note , JSONObject.class, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject response, AjaxStatus status)
            {
                if(response != null){
                    listener.noteActionSuccess("Note deleted");
                }
                else{
                    listener.noteActionFailed("Failed to delete note");
                }

            }
        });
    };

    public static void getNote(final int noteId, Context context, final NoteManagement listener){
        Map<String,String> note = new HashMap<String,String>();
        note.put("getNote", "1");
        note.put("id", String.valueOf(noteId));

        String url = "http://pierre-mar.net/Zone_indexer/";
        AQuery aq = new AQuery(context);

        aq.ajax(url,note , JSONObject.class, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject response, AjaxStatus status)
            {
                if(response != null){
                    try {
                        Note note = new Note();
                        note.noteId = noteId;
                        note.ownerId = response.getInt("ownerId");
                        note.zoneId = response.getInt("zoneId");
                        note.title = response.getString("title");
                        note.content = response.getString("text");
                        listener.getNote(note);
                    }
                    catch (Exception e){

                    }
                }
                else{
                    listener.noteActionFailed("Failed to get note");
                }

            }
        });
    }
    public static void getAllNotesFromUser(int userId, Context context, final NoteManagement listener){
        Map<String,String> note = new HashMap<String,String>();
        note.put("getAllNotesFromUser", "1");
        note.put("idUser", String.valueOf(userId));

        String url = "http://pierre-mar.net/Zone_indexer/";
        AQuery aq = new AQuery(context);

        aq.ajax(url,note , JSONArray.class, new AjaxCallback<JSONArray>(){
            @Override
            public void callback(String url, JSONArray response, AjaxStatus status)
            {
                if(response != null){
                    ArrayList<Note> notesList = new ArrayList<Note>();
                    try{
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject JSONNote = response.getJSONObject(i);
                            Note note = new Note();
                            note.noteId = JSONNote.getInt("id");
                            note.title = JSONNote.getString("title");
                            note.content = JSONNote.getString("text");
                            note.zoneId = JSONNote.getInt("zoneId");
                            note.ownerId = JSONNote.getInt("ownerId");
                            notesList.add(note);
                        }

                    }
                    catch(Exception e){
                        listener.noteActionFailed("Failed to get notes");
                    }
                    listener.getNotes(notesList);
                }
                else{
                    listener.noteActionFailed("Failed to get notes");
                }

            }
        });
    }
    public static void getAllNotesFromUserAndZone(int userId,  int zoneId, Context context, final NoteManagement listener){
        Map<String,String> note = new HashMap<String,String>();
        note.put("getAllNotesFromUserAndZone", "1");
        note.put("idUser", String.valueOf(userId));
        note.put("idZone", String.valueOf(zoneId));

        String url = "http://pierre-mar.net/Zone_indexer/";
        AQuery aq = new AQuery(context);

        aq.ajax(url,note , JSONArray.class, new AjaxCallback<JSONArray>(){
            @Override
            public void callback(String url, JSONArray response, AjaxStatus status)
            {
                if(response != null){
                    try{
                        ArrayList<Note> notesList = new ArrayList<Note>();
                        JSONArray notes = response;
                        for (int i = 0; i < notes.length(); i++) {
                            JSONObject JSONNote = notes.getJSONObject(i);
                            Note note = new Note();
                            note.noteId = JSONNote.getInt("id");
                            note.title = JSONNote.getString("title");
                            note.content = JSONNote.getString("text");
                            note.zoneId = JSONNote.getInt("zoneId");
                            note.ownerId = JSONNote.getInt("ownerId");
                            notesList.add(note);
                        }

                        listener.getNotes(notesList);
                    }
                    catch(Exception e){
                        listener.noteActionFailed("Failed to get notes");
                    }
                }
                else{
                    listener.noteActionFailed("Failed to get notes");
                }

            }
        });
    }

    public interface NoteManagement {
        public void noteActionSuccess(String message);
        public void noteActionFailed(String message);
        public void getNote(Note note);
        public void getNotes(List<Note> notes);
    }
}
