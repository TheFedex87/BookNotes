package it.thefedex87.booknotes.arguments;

import it.thefedex87.booknotes.R;
import it.thefedex87.booknotes.database.DBManager;
import it.thefedex87.booknotes.database.DBStrings;
import it.thefedex87.booknotes.database.EmptyTableException;
import it.thefedex87.booknotes.database.NoMatchFoundException;
import it.thefedex87.booknotes.images.ImagesManager;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

public class ArgumentsManager {

	private Context context;
	
	public ArgumentsManager(Context context) {
		this.context = context;
	}
	
	public ArrayList<Argument> getArgumentsList(DBManager dbManager, long bookId) throws EmptyTableException {
		ArrayList<Argument> argumentsList = new ArrayList<Argument>();
		
		Cursor cursor = dbManager.getArgumentsTable(bookId);
    	
		if (cursor.getCount() <= 0) {
			throw new EmptyTableException(context.getResources().getString(R.string.empty_db_ex));
		}
		
		cursor.moveToFirst();
    	for (int i = 0; i < cursor.getCount(); i++) {
    		Argument argument = new Argument(cursor.getString(cursor.getColumnIndex(DBStrings.TABLE_ARGUMENTS_FIELD_ARGUMENT)), 
    				cursor.getLong(cursor.getColumnIndex(DBStrings.TABLE_ARGUMENTS_FIELD_ID)), 
    				cursor.getLong(cursor.getColumnIndex(DBStrings.TABLE_ARGUMENTS_FIELD_ID_BOOK)));
    		argumentsList.add(argument);
    		cursor.moveToNext();
    	}
    	
    	return argumentsList;
	}

	public long saveArgument(DBManager dbManager, Argument argument) {
		try {
			return dbManager.putNewArgument(argument);
		}
		catch (SQLiteException ex) {
			return -1;
		}
	}
	public long updateArgument(DBManager dbManager, Argument argument) {
		try {
			return dbManager.updateArgument(argument);
		}
		catch (SQLiteException ex) {
			return -1;
		}
	}
	public boolean deleteArgument(DBManager dbManager, long argumentId) {
		try {
			//Recupero tutte le note dell'argometno da eliminare
			Cursor cursorNotes = dbManager.getNotesTable(argumentId);
			if (cursorNotes.getCount()>0) {
				cursorNotes.moveToFirst();
				for (int i = 0; i < cursorNotes.getCount(); i++) {
					long noteId = cursorNotes.getLong(cursorNotes.getColumnIndex(DBStrings.TABLE_NOTES_FIELD_ID));
					
					//Recupero tutte le immagini della nota da eliminare dell'argomento dal eliminare
					Cursor cursorImages = dbManager.getImages(noteId);
					if (cursorImages.getCount()>0) {
						cursorImages.moveToFirst();
						for (int j = 0; j < cursorImages.getCount(); j++) {
							ImagesManager.deleteImageFromSD(cursorImages.getString(cursorImages.getColumnIndex(DBStrings.TABLE_IMAGES_FIELD_NAME)));
							cursorImages.moveToNext();
						}
						if (!dbManager.deleteImages(noteId)) {return false;};
						cursorNotes.moveToNext();
					}
				}
				if (!dbManager.deleteNotes(argumentId)) {return false;};
			}
			
			return dbManager.deleteArgument(argumentId);
		}
		catch (SQLiteException ex) {
			return false;
		}
	}
	public ArrayList<Argument> findArgument(DBManager dbManager, String queryString) throws NoMatchFoundException {
		ArrayList<Argument> argumentsList = new ArrayList<Argument>();
		
		//Tolgo gli eventuali spazi dall'inizio e dalla fine della stringa di ricerca
		queryString = queryString.replaceAll("\\s+$", "").replaceAll("^\\s+", "");
		String[] splittedQuery = queryString.split(" ");
		
		ArrayList<String> queryStrings = new ArrayList<String>();
		queryStrings.add(queryString);
		
		for (int i = 0; i < splittedQuery.length; i++) {
			queryStrings.add(splittedQuery[i]);
		}
		
		
		Cursor cursor = dbManager.findArguments(queryStrings);
    	
		/*if (cursor.getCount() <= 0) {
			throw new EmptyTableException(context.getResources().getString(R.string.empty_db_ex));
		}*/
		
		cursor.moveToFirst();
    	for (int i = 0; i < cursor.getCount(); i++) {
    		Argument argument = new Argument(cursor.getString(cursor.getColumnIndex(DBStrings.TABLE_ARGUMENTS_FIELD_ARGUMENT)), 
    				cursor.getLong(cursor.getColumnIndex(DBStrings.TABLE_ARGUMENTS_FIELD_ID)), 
    				cursor.getLong(cursor.getColumnIndex(DBStrings.TABLE_ARGUMENTS_FIELD_ID_BOOK)));
    		argumentsList.add(argument);
    		cursor.moveToNext();
    	}
    	
    	return argumentsList;
	}
	public String getArgumentTitleById(DBManager db, long id) throws NoMatchFoundException {
		try {
			return db.getArgumentTitleById(id);
		}
		catch (SQLiteException ex) {
			NoMatchFoundException noMatchFoundException = new NoMatchFoundException(ex.getMessage());
			throw noMatchFoundException;
		}
	}
}
