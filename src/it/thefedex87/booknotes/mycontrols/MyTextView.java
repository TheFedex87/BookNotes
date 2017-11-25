package it.thefedex87.booknotes.mycontrols;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Questa classe estende la classe android.widget.TextView, aggiungedo un campo privato per memorizzare a quale indice
 * del database corrisponde l'elemento mostrato dalla TextView
 * @author federico.creti
 *
 */
public class MyTextView extends TextView {

	private long idDb;
	
	public MyTextView(Context context) {
		super(context);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public long getIdDb() {
		return idDb;
	}
	public void setIdDb(long value) {
		this.idDb = value;
	}
}
