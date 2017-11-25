package it.thefedex87.booknotes.note;

/**
 * Classe che descrive una nota
 * @author federico.creti
 *
 */
public class Note {
	private String note;
	
	/**
	 * Tiene traccia dell'ID della nota
	 */
	private long ID;
	
	/**
	 * Tiene traccia dell'ID dell'argomento a cui e' associata la nota
	 */
	private long argumentId;
	
	public Note(String note, long argumentId) {
		this.note = note;
		this.argumentId =argumentId;
		ID = -1;
	}
	
	public Note(String note, long noteId, long argumentId) {
		this(note, argumentId);
		this.ID = noteId;
	}

	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}

	
	public long getArgumentId() {
		return argumentId;
	}
	public void setArgumentId(long argumentId) {
		this.argumentId = argumentId;
	}
}
