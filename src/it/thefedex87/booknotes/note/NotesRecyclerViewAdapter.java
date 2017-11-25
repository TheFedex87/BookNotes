package it.thefedex87.booknotes.note;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.thefedex87.booknotes.R;
import it.thefedex87.booknotes.books.Book;

/**
 * Created by federico.creti on 21/04/2015.
 */
public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<Note> notes;

    public NotesRecyclerViewAdapter (Context contex, List notesList) {
        this.context = contex;
        this.inflater = LayoutInflater.from(contex);
        notes = notesList;
    }

    public Note getItemAtPosition(int position) {
        return notes.get(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.line_layout, null, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String note = notes.get(position).getNote();
        if (note.length() > 35) {
            note = note.substring(0, 35) + "...";
        }
        holder.title.setText(note);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textViewString);
        }
    }
}
