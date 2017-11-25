package it.thefedex87.booknotes.note;

import java.util.List;

import it.thefedex87.booknotes.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 
 * @author federico.creti
 * Classe usata dalla activity di ricerca
 */
public class NotesArrayAdapter extends ArrayAdapter<Note> {

	Context context;
	
	public NotesArrayAdapter(Context context, int resource, List<Note> objects) {
		super(context, resource, objects);
		
		this.context = context;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.line_layout, null);
		}
		
		TextView textViewString = (TextView)convertView.findViewById(R.id.textViewString);
		String note = getItem(position).getNote();
		if (note.length() > 35) {
			note = note.substring(0, 35) + "...";
		}
		textViewString.setText(note);
		
		return convertView;
	}

}
