package it.thefedex87.booknotes.database;

import java.util.ArrayList;

import it.thefedex87.booknotes.arguments.Argument;
import it.thefedex87.booknotes.books.Book;
import it.thefedex87.booknotes.images.Image;
import it.thefedex87.booknotes.note.Note;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DBManager implements Cloneable  {

	private DBHelper dbHelper;
	
	public DBManager(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * Salvataggio del libro sul DB
	 * @param bookTitle Titolo libro da memorizzare
	 * @return Id of the last inserted row
	 * @throws SQLiteException
	 */
	public long putNewBook(Book book) throws SQLiteException {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		
		cv.put(DBStrings.TABLE_BOOKS_FIELD_BOOK_TITLE, book.getTitle());
		
		try {
			return db.insert(DBStrings.TABLE_BOOKS, null, cv);
		}
		catch (SQLiteException ex) {
			throw new SQLiteException("Errore inserimento dati: " + ex.getMessage());
		}
	}
	public long updateBook(Book book) throws SQLiteException {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		
		cv.put(DBStrings.TABLE_BOOKS_FIELD_BOOK_TITLE, book.getTitle());
		
		try {
			db.update(DBStrings.TABLE_BOOKS, cv, DBStrings.TABLE_BOOKS_FIELD_ID + "=" + book.getID(), null);
			return book.getID();
		}
		catch (SQLiteException ex) {
			throw new SQLiteException("Errore aggiornamento dati: " + ex.getMessage());
		}
	}
	public Cursor getBooksTable() throws SQLiteException {
		Cursor cursor = null;
		
		try {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			//cursor = db.query(DBStrings.TABLE_BOOKS, null, null, null, null, null, null, null);
			//String sql = "SELECT rowid,* FROM " + DBStrings.TABLE_BOOKS;
			String sql = "SELECT * FROM " + DBStrings.TABLE_BOOKS;
			cursor = db.rawQuery(sql, null);
		}
		catch(SQLiteException ex) {
			throw new SQLiteException("Errore recupero dati: " + ex.getMessage());
		}
		
		return cursor;
	}
	public boolean deleteBook(Book book) {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		
		try {
			return db.delete(DBStrings.TABLE_BOOKS, DBStrings.TABLE_BOOKS_FIELD_ID + "=" + book.getID(), null) > 0;
		}
		catch (SQLiteException ex) {
			throw new SQLiteException("Errore elminazione dati: " + ex.getMessage());
		}
	}
	public Cursor findBooks(ArrayList<String> queryStrings) {
		Cursor cursor = null;
		
		try {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			//cursor = db.query(DBStrings.TABLE_BOOKS, null, null, null, null, null, null, null);
			//String sql = "SELECT rowid,* FROM " + DBStrings.TABLE_BOOKS;
			String sql = "SELECT * FROM " + DBStrings.TABLE_BOOKS + " WHERE " + DBStrings.TABLE_BOOKS_FIELD_BOOK_TITLE;
			
			for (int i = 0; i < queryStrings.size(); i++) {
				if (i==0) {
					sql += " LIKE '%" + queryStrings.get(i) + "%'";
				}
				else {
					sql += " OR " + DBStrings.TABLE_BOOKS_FIELD_BOOK_TITLE + " LIKE '%" + queryStrings.get(i) + "%'";
				}
			}
			
			cursor = db.rawQuery(sql, null);
		}
		catch(SQLiteException ex) {
			throw new SQLiteException("Errore recupero dati: " + ex.getMessage());
		}
		
		return cursor;
	}
	
	
	
	
	
	
	public long putNewArgument(Argument argument) throws SQLiteException {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		
		cv.put(DBStrings.TABLE_ARGUMENTS_FIELD_ARGUMENT, argument.getArgumentName());
		cv.put(DBStrings.TABLE_ARGUMENTS_FIELD_ID_BOOK, argument.getBookId());
		
		try {
			return db.insert(DBStrings.TABLE_ARGUMENTS, null, cv);
		}
		catch (SQLiteException ex) {
			throw new SQLiteException("Errore inserimento argomento: " + ex.getMessage());
		}
	}
	public Cursor getArgumentsTable(long bookId) throws SQLiteException {
		Cursor cursor = null;
		
		try {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			//cursor = db.query(DBStrings.TABLE_ARGUMENTS, null, null, null, null, null, null, null);
			String sql = "SELECT * FROM " + DBStrings.TABLE_ARGUMENTS + " WHERE " + DBStrings.TABLE_ARGUMENTS_FIELD_ID_BOOK
					+ " = " + Long.toString(bookId);
			cursor = db.rawQuery(sql, null);
		}
		catch(SQLiteException ex) {
			throw new SQLiteException("Errore recupero argomenti: " + ex.getMessage());
		}
		
		return cursor;
	}
	public long updateArgument(Argument argument) throws SQLiteException {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		
		cv.put(DBStrings.TABLE_ARGUMENTS_FIELD_ARGUMENT, argument.getArgumentName());
		
		try {
			db.update(DBStrings.TABLE_ARGUMENTS, cv, DBStrings.TABLE_ARGUMENTS_FIELD_ID + "=" + argument.getID(), null);
			return argument.getID();
		}
		catch (SQLiteException ex) {
			throw new SQLiteException("Errore aggiornamento dati: " + ex.getMessage());
		}
	}
	public boolean deleteArgument(long argumentId) {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		
		try {
			return db.delete(DBStrings.TABLE_ARGUMENTS, DBStrings.TABLE_ARGUMENTS_FIELD_ID + "=" + argumentId, null) > 0;
		}
		catch (SQLiteException ex) {
			throw new SQLiteException("Errore eliminazione dati: " + ex.getMessage());
		}
	}
	public Cursor findArguments(ArrayList<String> queryStrings) {
		Cursor cursor = null;
		
		try {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			//cursor = db.query(DBStrings.TABLE_BOOKS, null, null, null, null, null, null, null);
			//String sql = "SELECT rowid,* FROM " + DBStrings.TABLE_BOOKS;
			String sql = "SELECT * FROM " + DBStrings.TABLE_ARGUMENTS + " WHERE " + DBStrings.TABLE_ARGUMENTS_FIELD_ARGUMENT;
			
			for (int i = 0; i < queryStrings.size(); i++) {
				if (i==0) {
					sql += " LIKE '%" + queryStrings.get(i) + "%'";
				}
				else {
					sql += " OR " + DBStrings.TABLE_ARGUMENTS_FIELD_ARGUMENT + " LIKE '%" + queryStrings.get(i) + "%'";
				}
			}
			
			cursor = db.rawQuery(sql, null);
		}
		catch(SQLiteException ex) {
			throw new SQLiteException("Errore recupero dati: " + ex.getMessage());
		}
		
		return cursor;
	}
	public String getArgumentTitleById(long argumentId) {
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		
		try {
			String sql = "SELECT " + DBStrings.TABLE_ARGUMENTS_FIELD_ARGUMENT + " FROM " + DBStrings.TABLE_ARGUMENTS
					+ " WHERE " + DBStrings.TABLE_ARGUMENTS_FIELD_ID + " = " + argumentId;
			
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			return cursor.getString(0);
		}
		catch (SQLiteException ex) {
			throw new SQLiteException("Errore recupero titolo: " + ex.getMessage());
		}
	}
	
	
	
	
	
	public long putNewNote(Note note) throws SQLiteException {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		
		cv.put(DBStrings.TABLE_NOTES_FIELD_NOTE, note.getNote());
		cv.put(DBStrings.TABLE_NOTES_FIELD_ID_ARGUMENT, note.getArgumentId());
		
		try {
			return db.insert(DBStrings.TABLE_NOTES, null, cv);
		}
		catch (SQLiteException ex) {
			throw new SQLiteException("Errore inserimento nota: " + ex.getMessage());
		}
	}
	
	public long updateNote(Note note) throws SQLiteException {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		
		cv.put(DBStrings.TABLE_NOTES_FIELD_NOTE, note.getNote());
		cv.put(DBStrings.TABLE_NOTES_FIELD_ID_ARGUMENT, note.getArgumentId());
		
		try {
			db.update(DBStrings.TABLE_NOTES, cv, DBStrings.TABLE_NOTES_FIELD_ID + "=" + note.getID(), null);
			//String sql = "UPDATE notes SET note = '" + note.getNote() + "' WHERE id = 0"; 
			//db.rawQuery(sql, null);
			return note.getID();
		}
		catch (SQLiteException ex) {
			throw new SQLiteException("Errore aggiornamento nota: " + ex.getMessage());
		}
	}
	
	public Cursor getNotesTable(long argumentId) throws SQLiteException {
		Cursor cursor = null;
		
		try {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			//cursor = db.query(DBStrings.TABLE_ARGUMENTS, null, null, null, null, null, null, null);
			String sql = "SELECT * FROM " + DBStrings.TABLE_NOTES + " WHERE " + DBStrings.TABLE_NOTES_FIELD_ID_ARGUMENT
					+ " = " + Long.toString(argumentId);
			cursor = db.rawQuery(sql, null);
		}
		catch(SQLiteException ex) {
			throw new SQLiteException("Errore recupero nota: " + ex.getMessage());
		}
		catch(Exception ex) {
			ex.printStackTrace();
			Log.e("BookNotes", "Errore imprevisto in debug manager");
		}
			
		
		
		return cursor;
	}
	public boolean deleteNote(Note note) {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		
		try {
			return db.delete(DBStrings.TABLE_NOTES, DBStrings.TABLE_NOTES_FIELD_ID + "=" + note.getID(), null) > 0;
		}
		catch (SQLiteException ex) {
			throw new SQLiteException("Errore eliminazione dati: " + ex.getMessage());
		}
	}
	public boolean deleteNotes(long argumentId) {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		
		try {
			return db.delete(DBStrings.TABLE_NOTES, DBStrings.TABLE_NOTES_FIELD_ID_ARGUMENT + "=" + argumentId, null) > 0;
		}
		catch (SQLiteException ex) {
			throw new SQLiteException("Errore eliminazione dati: " + ex.getMessage());
		}
	}
	public Cursor findNotes(ArrayList<String> queryStrings) {
		Cursor cursor = null;
		
		try {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			//cursor = db.query(DBStrings.TABLE_BOOKS, null, null, null, null, null, null, null);
			//String sql = "SELECT rowid,* FROM " + DBStrings.TABLE_BOOKS;
			String sql = "SELECT * FROM " + DBStrings.TABLE_NOTES + " WHERE " + DBStrings.TABLE_NOTES_FIELD_NOTE;
			
			for (int i = 0; i < queryStrings.size(); i++) {
				if (i==0) {
					sql += " LIKE '%" + queryStrings.get(i) + "%'";
				}
				else {
					sql += " OR " + DBStrings.TABLE_NOTES_FIELD_NOTE + " LIKE '%" + queryStrings.get(i) + "%'";
				}
			}
			
			cursor = db.rawQuery(sql, null);
		}
		catch(SQLiteException ex) {
			throw new SQLiteException("Errore recupero dati: " + ex.getMessage());
		}
		
		return cursor;
	}
	
	
	
	public long putNewImage(Image image) throws SQLiteException {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		
		cv.put(DBStrings.TABLE_IMAGES_FIELD_DESCR, image.getDescription());
		cv.put(DBStrings.TABLE_IMAGES_FIELD_ID_NOTE, image.getIdNote());
		cv.put(DBStrings.TABLE_IMAGES_FIELD_NAME,  image.getName());
		
		try {
			return db.insert(DBStrings.TABLE_IMAGES, null, cv);
		}
		catch (SQLiteException ex) {
			throw new SQLiteException("Errore inserimento nota: " + ex.getMessage());
		}
	}
	
	public Cursor getImages(long noteId) throws SQLiteException {
		Cursor cursor = null;
		
		try {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			//cursor = db.query(DBStrings.TABLE_ARGUMENTS, null, null, null, null, null, null, null);
			String sql = "SELECT * FROM " + DBStrings.TABLE_IMAGES + " WHERE " + DBStrings.TABLE_IMAGES_FIELD_ID_NOTE
					+ " = " + Long.toString(noteId);
			cursor = db.rawQuery(sql, null);
		}
		catch(SQLiteException ex) {
			throw new SQLiteException("Errore recupero nota: " + ex.getMessage());
		}
		catch(Exception ex) {
			ex.printStackTrace();
			Log.e("BookNotes", "Errore imprevisto in debug manager");
		}
		return cursor;
	}
	
	public long getLastTableImagesID() {
		try {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor cursor = db.query(DBStrings.TABLE_IMAGES, null, null, null, null, null, null, null);
			if (cursor.getCount() > 0) {
				cursor.moveToLast();
				return cursor.getLong(0);
			}
			else {
				return 0;
			}
		}
		catch(SQLiteException ex) {
			throw new SQLiteException("Errore recupero dati: " + ex.getMessage());
		}
	}
	public boolean deleteImage(long imageId) {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		
		try {
			return db.delete(DBStrings.TABLE_IMAGES, DBStrings.TABLE_IMAGES_FIELD_ID + "=" + imageId, null) > 0;
		}
		catch (SQLiteException ex) {
			throw new SQLiteException("Errore inserimento dati: " + ex.getMessage());
		}
	}
	public boolean deleteImages(long noteId) {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		
		try {
			return db.delete(DBStrings.TABLE_IMAGES, DBStrings.TABLE_IMAGES_FIELD_ID_NOTE + "=" + noteId, null) > 0;
		}
		catch (SQLiteException ex) {
			throw new SQLiteException("Errore inserimento dati: " + ex.getMessage());
		}
	}
	
	
	
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
