package it.thefedex87.booknotes.note;

import it.thefedex87.booknotes.R;
import it.thefedex87.booknotes.database.DBManager;
import it.thefedex87.booknotes.database.DBStrings;
import it.thefedex87.booknotes.database.EmptyTableException;
import it.thefedex87.booknotes.database.NoMatchFoundException;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class NotesManager {

	private Context context;
	
	public NotesManager(Context context) {
		this.context = context;
	}

	public Note getNotesList(DBManager dbManager, long argumentId) throws EmptyTableException {
		//ArrayList<Argument> argumentsList = new ArrayList<Argument>();
		Note note = null;
		
		if (dbManager==null) {
			Log.e("BookNotes", "Errore dbManager");
		}
		
		Cursor cursor = dbManager.getNotesTable(argumentId);
    	
		if (cursor.getCount() <= 0) {
			throw new EmptyTableException(context.getResources().getString(R.string.empty_db_ex));
		}
		
		cursor.moveToFirst();
    	for (int i = 0; i < cursor.getCount(); i++) {
    		/*Argument argument = new Argument(cursor.getString(cursor.getColumnIndex(DBStrings.TABLE_ARGUMENTS_FIELD_ARGUMENT)), 
    				cursor.getLong(cursor.getColumnIndex(DBStrings.TABLE_ARGUMENTS_FIELD_ID)), 
    				cursor.getLong(cursor.getColumnIndex(DBStrings.TABLE_ARGUMENTS_FIELD_ID_BOOK)));
    		argumentsList.add(argument);*/
    		note = new Note(cursor.getString(cursor.getColumnIndex(DBStrings.TABLE_NOTES_FIELD_NOTE)), 
    				cursor.getLong(cursor.getColumnIndex(DBStrings.TABLE_NOTES_FIELD_ID)), 
    				cursor.getLong(cursor.getColumnIndex(DBStrings.TABLE_NOTES_FIELD_ID_ARGUMENT)));
    		
    		cursor.moveToNext();
    	}
    	
    	//return notesList;
    	return note;
	}

	public long saveNote(DBManager dbManager, Note note) {
		try {
			if (note.getID() == -1) {
				return dbManager.putNewNote(note);
			}
			else {
				return dbManager.updateNote(note);
			}
		}
		catch (SQLiteException ex) {
			return -1;
		}
	}
	
	public boolean deleteNote(DBManager dbManager, Note note) {
		try {
			return dbManager.deleteNote(note);
		}
		catch (SQLiteException ex) {
			return false;
		}
	}
	
	public ArrayList<Note> findNote(DBManager dbManager, String queryString) throws NoMatchFoundException {
		ArrayList<Note> notesList = new ArrayList<Note>();
		
		//Tolgo gli eventuali spazi dall'inizio e dalla fine della stringa di ricerca
		queryString = queryString.replaceAll("\\s+$", "").replaceAll("^\\s+", "");
		String[] splittedQuery = queryString.split(" ");
		
		ArrayList<String> queryStrings = new ArrayList<String>();
		queryStrings.add(queryString);
		
		for (int i = 0; i < splittedQuery.length; i++) {
			queryStrings.add(splittedQuery[i]);
		}
		
		
		Cursor cursor = dbManager.findNotes(queryStrings);
    	
		/*if (cursor.getCount() <= 0) {
			throw new EmptyTableException(context.getResources().getString(R.string.empty_db_ex));
		}*/
		
		cursor.moveToFirst();
    	for (int i = 0; i < cursor.getCount(); i++) {
    		Note note = new Note(cursor.getString(cursor.getColumnIndex(DBStrings.TABLE_NOTES_FIELD_NOTE)), 
    				cursor.getLong(cursor.getColumnIndex(DBStrings.TABLE_NOTES_FIELD_ID)), 
    				cursor.getLong(cursor.getColumnIndex(DBStrings.TABLE_NOTES_FIELD_ID_ARGUMENT)));
    		notesList.add(note);
    		cursor.moveToNext();
    	}
    	
    	return notesList;
	}
}
