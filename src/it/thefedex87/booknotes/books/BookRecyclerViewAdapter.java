package it.thefedex87.booknotes.books;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import it.thefedex87.booknotes.R;

/**
 * Created by federico.creti on 20/04/2015.
 */
public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.MyViewHolder> {
    LayoutInflater inflater;
    Context context;
    ArrayList<Book> books;

    public BookRecyclerViewAdapter(Context context) {
        this.context = context;
        //this.books = books;
        inflater = LayoutInflater.from(context);
        books = new ArrayList<Book>();
    }

    public void addItem(Book book) {
        books.add(book);
        notifyItemInserted(books.size() - 1);
    }

    public void removeItem(Book book) {
        int position = books.indexOf(book);
        books.remove(position);
        notifyItemRemoved(position);
    }

    public void updateItem(Book book, int position) {
        books.set(position, book);
        notifyDataSetChanged();
    }

    public Book getItemAtPosition(int position) {
        return books.get(position);
    }

    @Override
    public BookRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.line_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        Log.d("BookNotes", "onCreateViewOlder called");

        return holder;
    }

    @Override
    public void onBindViewHolder(BookRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.title.setText(books.get(position).getTitle());

        Log.d("BookNotes", "onBinViewHolder called " + position);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.textViewString);

        }
    }
}
