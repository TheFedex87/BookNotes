package it.thefedex87.booknotes.mycontrols;

import android.text.style.ClickableSpan;
import android.view.View;

public abstract class LongClickableSpan extends ClickableSpan {
	
	abstract public void onLongClick(View view);

}
