package it.thefedex87.booknotes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Classe che inizializza il DB o ne esegue l'aggiornmaento se richiesto
 * @author federico.creti
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, DBStrings.DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		//Creazione tabella libri
		String sql="CREATE TABLE " + DBStrings.TABLE_BOOKS + " (" + DBStrings.TABLE_BOOKS_FIELD_ID + 
				" INTEGER PRIMARY KEY AUTOINCREMENT, " + DBStrings.TABLE_BOOKS_FIELD_BOOK_TITLE + " TEXT)";
		//String sql = "CREATE VIRTUAL TABLE " + DBStrings.TABLE_BOOKS + " USING fts3 (" + DBStrings.TABLE_BOOKS_FIELD_BOOK_TITLE + " TEXT)";
		db.execSQL(sql);
		
		//Creazione tabella argomenti con foreign key legata ai libri
		sql = "CREATE TABLE " + DBStrings.TABLE_ARGUMENTS + " (" + DBStrings.TABLE_ARGUMENTS_FIELD_ID + 
				" INTEGER PRIMARY KEY AUTOINCREMENT, " + DBStrings.TABLE_ARGUMENTS_FIELD_ARGUMENT + " TEXT, "
				+ DBStrings.TABLE_ARGUMENTS_FIELD_ID_BOOK + " INTEGER, FOREIGN KEY (" + DBStrings.TABLE_ARGUMENTS_FIELD_ID_BOOK
				+ ") REFERENCES " + DBStrings.TABLE_BOOKS + "(" + DBStrings.TABLE_BOOKS_FIELD_ID + "))";
		db.execSQL(sql);
		
		//Creazione tabella note con foreign key leagata a argomenti
		sql = "CREATE TABLE " + DBStrings.TABLE_NOTES + " (" + DBStrings.TABLE_NOTES_FIELD_ID + 
				" INTEGER PRIMARY KEY AUTOINCREMENT, " + DBStrings.TABLE_NOTES_FIELD_NOTE + " TEXT, " 
				+ DBStrings.TABLE_NOTES_FIELD_ID_ARGUMENT + " INTEGER, FOREIGN KEY (" + DBStrings.TABLE_NOTES_FIELD_ID_ARGUMENT
				+ ") REFERENCES " + DBStrings.TABLE_ARGUMENTS + "(" + DBStrings.TABLE_ARGUMENTS_FIELD_ID + "))";
		db.execSQL(sql);
		
		//Creazione tabella immagini con foreign key legata a note
		sql = "CREATE TABLE " + DBStrings.TABLE_IMAGES + " (" + DBStrings.TABLE_IMAGES_FIELD_ID +
				" INTEGER PRIMARY KEY AUTOINCREMENT, " + DBStrings.TABLE_IMAGES_FIELD_NAME + " TEXT, " + DBStrings.TABLE_IMAGES_FIELD_DESCR + " TEXT, "
				+ DBStrings.TABLE_IMAGES_FIELD_ID_NOTE + " INTEGER, FOREIGN KEY (" + DBStrings.TABLE_IMAGES_FIELD_ID_NOTE
				+ ") REFERENCES " + DBStrings.TABLE_NOTES + "(" + DBStrings.TABLE_NOTES_FIELD_ID + "))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
