package it.thefedex87.booknotes.books;

import it.thefedex87.booknotes.R;
import it.thefedex87.booknotes.arguments.ArgumentsManager;
import it.thefedex87.booknotes.database.DBManager;
import it.thefedex87.booknotes.database.DBStrings;
import it.thefedex87.booknotes.database.EmptyTableException;
import it.thefedex87.booknotes.database.NoMatchFoundException;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

public class BooksManager {

	private Context context;
	
	public BooksManager(Context context) {
		this.context = context;
	}
	
	public ArrayList<Book> getBookList(DBManager dbManager) throws EmptyTableException {
		ArrayList<Book> booksList = new ArrayList<Book>();
		
		Cursor cursor = dbManager.getBooksTable();
    	
		if (cursor.getCount() <= 0) {
			throw new EmptyTableException(context.getResources().getString(R.string.empty_db_ex));
		}
		
		cursor.moveToFirst();
    	for (int i = 0; i < cursor.getCount(); i++) {
    		Book book = new Book(cursor.getString(cursor.getColumnIndex(DBStrings.TABLE_BOOKS_FIELD_BOOK_TITLE)), 
    				cursor.getLong(cursor.getColumnIndex(DBStrings.TABLE_BOOKS_FIELD_ID)));
    		booksList.add(book);
    		cursor.moveToNext();
    	}
    	
    	return booksList;
	}
	
	public long saveBook(DBManager dbManager, Book book) {
		try {
			return dbManager.putNewBook(book);
		}
		catch (SQLiteException ex) {
			return -1;
		}
	}
	
	public long updateBook(DBManager dbManager, Book book) {
		try {
			return dbManager.updateBook(book);
		}
		catch (SQLiteException ex) {
			return -1;
		}
	}
	
	public boolean deleteBook(DBManager dbManager, Book book) {
		try {
			//Recupero tutte gli argomenti del libro da eliminare
			Cursor cursorArguments = dbManager.getArgumentsTable(book.getID());
			if (cursorArguments.getCount() > 0) {
				cursorArguments.moveToFirst();
				ArgumentsManager am = new ArgumentsManager(context);
				for (int i = 0; i < cursorArguments.getCount(); i++) {
					if (!am.deleteArgument(dbManager, cursorArguments.getLong(cursorArguments.getColumnIndex(DBStrings.TABLE_IMAGES_FIELD_ID)))) {
						return false;
					}
					cursorArguments.moveToNext();
				}
			}
			
			return dbManager.deleteBook(book);
		}
		catch (SQLiteException ex) {
			return false;
		}
	}
	
	public ArrayList<Book> findBook(DBManager dbManager, String queryString) throws NoMatchFoundException {
		ArrayList<Book> booksList = new ArrayList<Book>();
		
		//Tolgo gli eventuali spazi dall'inizio e dalla fine della stringa di ricerca
		queryString = queryString.replaceAll("\\s+$", "").replaceAll("^\\s+", "");
		String[] splittedQuery = queryString.split(" ");
		
		ArrayList<String> queryStrings = new ArrayList<String>();
		queryStrings.add(queryString);
		
		for (int i = 0; i < splittedQuery.length; i++) {
			queryStrings.add(splittedQuery[i]);
		}
		
		
		Cursor cursor = dbManager.findBooks(queryStrings);
    	
		/*if (cursor.getCount() <= 0) {
			throw new EmptyTableException(context.getResources().getString(R.string.empty_db_ex));
		}*/
		
		cursor.moveToFirst();
    	for (int i = 0; i < cursor.getCount(); i++) {
    		Book book = new Book(cursor.getString(cursor.getColumnIndex(DBStrings.TABLE_BOOKS_FIELD_BOOK_TITLE)), 
    				cursor.getLong(cursor.getColumnIndex(DBStrings.TABLE_BOOKS_FIELD_ID)));
    		booksList.add(book);
    		cursor.moveToNext();
    	}
    	
    	return booksList;
	}
}
