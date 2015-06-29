package com.example.rcdsm.zone_indexer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rcdsm.zone_indexer.Entity.Note;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by rcdsm on 26/06/2015.
 */
public class NoteAdapter extends BaseAdapter {

    List<Note> notes;
    Context context;
    LayoutInflater inflate;

    NoteAdapter(List<Note> list, Context context){
        this.notes = list;
        this.context = context;
        inflate = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return this.notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){

            convertView = inflate.inflate(R.layout.list_item, null);

            holder = new ViewHolder(convertView);

            //we do a setTag for saving current object
            convertView.setTag(holder);
        }
        else{
            //view already exist
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(notes.get(position).getTitle());
        holder.littleContent.setText(notes.get(position).getContent().substring(0, 20) + "...");

        return convertView;
    }

    public class ViewHolder{
        public TextView title;
        public TextView littleContent;

        ViewHolder(View view)
        {
            this.title = (TextView) view.findViewById(R.id.note_title);
            this.littleContent = (TextView) view.findViewById(R.id.little_content);
        }
    }
}
