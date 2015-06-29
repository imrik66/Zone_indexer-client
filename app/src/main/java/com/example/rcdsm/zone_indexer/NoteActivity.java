package com.example.rcdsm.zone_indexer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.example.rcdsm.zone_indexer.Entity.Note;
import com.example.rcdsm.zone_indexer.Entity.Zone;

/**
 * Created by rcdsm on 29/06/2015.
 */
public class NoteActivity extends ActionBarActivity {

    Note note;
    TextView title;
    TextView about_zone;
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.note_activity);
        this.title = (TextView) findViewById(R.id.title_note);
        this.content = (TextView) findViewById(R.id.content_note);
        this.note = Note.currentNote;

        this.title.setText(note.getTitle());
        this.content.setText(note.getContent());
    }
}
