package it.thefedex87.booknotes.database;

/**
 * Classe che contiene le stringhe del DB libri
 * @author federico.creti
 *
 */
public abstract class DBStrings {
	/**
	 * Stringa identificativa del nome del DB
	 */
	public static final String DB_NAME="booknotes";
	
	/*
	 * Nome tabella libri
	 */
	public static final String TABLE_BOOKS="books";
	/*
	 * Campo id tabella libri
	 */
	public static final String TABLE_BOOKS_FIELD_ID="rowid"; //"id";
	/**
	 * Campo titolo libro tabella libri
	 */
	public static final String TABLE_BOOKS_FIELD_BOOK_TITLE="book_title";
	
	/**
	 * Nome tabella argomenti
	 */
	public static final String TABLE_ARGUMENTS="arguments";
	/**
	 * Campo ID tabella argomenti
	 */
	public static final String TABLE_ARGUMENTS_FIELD_ID="id";
	/**
	 * Campo argomento tabella argomenti
	 */
	public static final String TABLE_ARGUMENTS_FIELD_ARGUMENT="argument";
	/**
	 * Campo id libro associato tabella argomenti
	 */
	public static final String TABLE_ARGUMENTS_FIELD_ID_BOOK="id_book";
	
	/**
	 * Nome tabella note
	 */
	public static final String TABLE_NOTES="notes";
	/**
	 * Campo id tabella note
	 */
	public static final String TABLE_NOTES_FIELD_ID="id";
	/**
	 * Campo nota tabella note
	 */
	public static final String TABLE_NOTES_FIELD_NOTE="note";
	/**
	 * Campo id argomento associato tabella note
	 */
	public static final String TABLE_NOTES_FIELD_ID_ARGUMENT="id_argument";
	
	/**
	 * Nome tabella immagini
	 */
	public static final String TABLE_IMAGES="images";
	/**
	 * Campo id tabella immagini
	 */
	public static final String TABLE_IMAGES_FIELD_ID="id";
	/**
	 * Campo descrizione immagine
	 */
	public static final String TABLE_IMAGES_FIELD_DESCR="description";
	/**
	 * Campo con il nome della immagine salvata in memoria
	 */
	public static final String TABLE_IMAGES_FIELD_NAME="name";
	/**
	 * Campo id nota, legato in foreign key al campo id della tabella notes
	 */
	public static final String TABLE_IMAGES_FIELD_ID_NOTE="id_note";
}
