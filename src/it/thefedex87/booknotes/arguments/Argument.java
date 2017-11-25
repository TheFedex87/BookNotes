package it.thefedex87.booknotes.arguments;

/**
 * Classe che descrive un argomento
 * @author federico.creti
 *
 */
public class Argument {
	private String argumentName;
	
	/**
	 * Tiene traccia dell'ID dell'argomento
	 */
	private long ID;
	
	/**
	 * Tiene traccia dell'ID del libro a cui e' associato l'argomento
	 */
	private long bookId;
	
	public Argument(String argumentName, long bookId) {
		this.argumentName = argumentName;
		this.bookId = bookId;
	}
	
	public Argument(String argumentName, long argumentId, long bookId) {
		this(argumentName, bookId);
		this.ID = argumentId;
	}


	public long getBookId() {
		return bookId;
	}
	public void setBookId(long value) {
		this.bookId = value;
	}
	

	public String getArgumentName() {
		return argumentName;
	}
	public void setArgumentName(String argumentName) {
		this.argumentName = argumentName;
	}


	public long getID() {
		return ID;
	}
	public void setID(long value) {
		this.ID = value;
	}
	

}
