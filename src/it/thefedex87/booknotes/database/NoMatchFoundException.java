package it.thefedex87.booknotes.database;

public class NoMatchFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NoMatchFoundException() {
		// TODO Auto-generated constructor stub
	}

	public NoMatchFoundException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public NoMatchFoundException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	public NoMatchFoundException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

}
