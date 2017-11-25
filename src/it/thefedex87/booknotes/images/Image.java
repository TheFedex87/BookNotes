package it.thefedex87.booknotes.images;

import android.graphics.Bitmap;

public class Image {
	private long idNote;
	private Bitmap bm;
	private String name;
	private long id;
	
	private String description;
	
	public Image(String name, Bitmap bitmap, long idNote) {
		this.name = name;
		this.bm = bitmap;
		this.idNote = idNote;
	}

	public long getIdNote() {
		return idNote;
	}

	public Bitmap getBm() {
		return bm;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
