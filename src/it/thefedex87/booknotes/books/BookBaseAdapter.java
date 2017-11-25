package it.thefedex87.booknotes.books;

import it.thefedex87.booknotes.R;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BookBaseAdapter extends BaseAdapter {
	Context context;
	ArrayList<Book> list;
	
	
	public BookBaseAdapter(Context context) {
		this.context = context;
		list = new ArrayList<Book>();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.line_layout, null);
		}
		
		TextView textViewString = (TextView)convertView.findViewById(R.id.textViewString);
		textViewString.setText(list.get(position).getTitle());
		
		return convertView;
	}
	
	public void addItem(Book book){
		list.add(book);
		notifyDataSetChanged();
	}
	
	public void removeItem(Book book) {
		list.remove(book);
		notifyDataSetChanged();
	}

	public void updateItem(Book book, int position) {
		list.set(position, book);
		notifyDataSetChanged();
	}
}
