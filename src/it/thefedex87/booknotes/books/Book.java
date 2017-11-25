package it.thefedex87.booknotes.books;


/**
 * Classe che descrive un libro
 * @author federico.creti
 *
 */
public class Book {
	private String title;
	
	/**
	 * Tiene traccia dell'ID del libro nel DB
	 */
	private long ID;
	
	

	public Book(String title) {
		this.title = title;
	}
	
	public Book(String title, long ID) {
		this(title);
		this.ID = ID;
	}
	
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
