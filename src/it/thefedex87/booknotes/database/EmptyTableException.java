package it.thefedex87.booknotes.database;

public class EmptyTableException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmptyTableException() {
		// TODO Auto-generated constructor stub
	}

	public EmptyTableException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public EmptyTableException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	public EmptyTableException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

}
