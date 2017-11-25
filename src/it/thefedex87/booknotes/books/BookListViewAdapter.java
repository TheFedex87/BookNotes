package it.thefedex87.booknotes.books;

import it.thefedex87.booknotes.R;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BookListViewAdapter extends ArrayAdapter<Book> {

	Context context;
	ArrayList<Book> list;
	
	public BookListViewAdapter(Context context, int resource) {
		super(context, resource);
		//this.context = context;
		//this.list = list;
		list = new ArrayList<Book>();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.line_layout, null);
		}
		
		TextView textViewString = (TextView)convertView.findViewById(R.id.textViewString);
		textViewString.setText(((Book)(list.get(position))).getTitle());
		
		return convertView;
	}
	
	@Override
	public void add(Book book) {
		super.add(book);
		list.add(book);
		notifyDataSetChanged();
	}
	
	@Override
	public void remove(Book book) {
		super.remove(book);
		list.remove(book);
	}
	
	public void updateItem(Book book, int position) {
		list.set(position, book);
		notifyDataSetChanged();
	}
}
