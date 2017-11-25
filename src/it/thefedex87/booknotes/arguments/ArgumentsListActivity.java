package it.thefedex87.booknotes.arguments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.thefedex87.booknotes.R;
import it.thefedex87.booknotes.database.DBManager;
import it.thefedex87.booknotes.database.EmptyTableException;
import it.thefedex87.booknotes.note.NotesListActivity;

public class ArgumentsListActivity extends ActionBarActivity {
	private Toolbar toolbar;
	
	private RecyclerView listArguments;
	private ImageButton btnSaveArgumentTitle;
	private EditText editTextArgumentTitle;
	
	//private ArgumentBaseAdapter listViewAdapter;
	private ArgumentsRecyclerViewAdapter recyclerViewAdapter;
	
	/**
	 * ID del libro selezionato passato mediante Intent
	 */
	private long bookId;
	/**
	 * Titolo del libro selezionato passato mediante Intent
	 */
	private String bookTitle;
	
	private ArrayList<Argument> arguments;
	
	/**
	 * Argumentes manager
	 */
	private ArgumentsManager am;
	
	private Activity thisActivity;
	
	private DBManager db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arguments_list);

		if (savedInstanceState == null) {
			//Recupero il messaggio passato dalla precedente activity: BOOK_ID
			bookId = getIntent().getLongExtra("ID_BOOK", -1);
			bookTitle = getIntent().getStringExtra("TITLE_BOOK");
			Log.i("BookNotes", "Get values from intent");
		}
		else {
			bookId = savedInstanceState.getLong("ID_BOOK");
			bookTitle = savedInstanceState.getString("TITLE_BOOK");
			Log.i("BookNotes", "Get values from savedInstanceState");
		}
		
		//Toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        
		//Hide action bar logo
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		
		//Show back button on action bar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		/////////
		
		thisActivity = this;
		
		
		
		am = new ArgumentsManager(this);
		
		db = new DBManager(this);
		
		//Recupera le view del layout
		recuperaLayoutViews();
		
		getArgumentsList();
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		bundle.putLong("ID_BOOK", bookId);
		bundle.putString("TITLE_BOOK", bookTitle);
	}
	
	/**
	 * Metodo che recupera le View del layout collegato all'activity
	 */
	private void recuperaLayoutViews() {
		listArguments = (RecyclerView) findViewById(R.id.recyclerViewArguments);
		recyclerViewAdapter = new ArgumentsRecyclerViewAdapter(this);
		listArguments.setAdapter(recyclerViewAdapter);
		listArguments.setLayoutManager(new LinearLayoutManager(this));

		listArguments.addOnItemTouchListener(new RecyclerTouchListener(this, listArguments, new ClickListener() {
			@Override
			public void onClick(View v, int position) {
				Argument argument = recyclerViewAdapter.getItemAtPosition(position);

				Intent intent = new Intent(thisActivity, NotesListActivity.class);
				intent.putExtra("ID_ARGUMENT", argument.getID());
				intent.putExtra("TITLE_ARGUMENT", argument.getArgumentName());
				startActivity(intent);
			}

			@Override
			public void onDown(View v, final int position) {
				final TextView textView = (TextView)v.findViewById(R.id.textViewString);

				//Recupero l'elemento selezionato
				final Argument argument = recyclerViewAdapter.getItemAtPosition(position);

				textView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					@Override
					public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo) {
						contextMenu.setHeaderTitle(getResources().getString(R.string.contextMenuBooks));
						contextMenu.add(getResources().getString(R.string.contextMenuEdit)).setOnMenuItemClickListener(new OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(MenuItem arg0) {
								//Toast.makeText(thisActivity, "Modifico elemento con ID: " + book.getID(), Toast.LENGTH_LONG).show();
								//return false;
								AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
								builder.setTitle(getResources().getString(R.string.editDialogTitle));

								final EditText editText = new EditText(thisActivity);
								editText.setText(argument.getArgumentName());
								editText.setInputType(InputType.TYPE_CLASS_TEXT);
								editText.selectAll();
								editText.post(new Runnable() {
									public void run() {
										InputMethodManager inputMethodManager =  (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
										inputMethodManager.toggleSoftInputFromWindow(editText.getApplicationWindowToken(),     InputMethodManager.SHOW_FORCED, 0);
										//editText.requestFocus();
									}
								});
								builder.setView(editText);

								builder.setNegativeButton(getResources().getString(R.string.editDialogCancel), null);


								builder.setPositiveButton(getResources().getString(R.string.editDialogConfirm), new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										argument.setArgumentName(editText.getText().toString());
										if (am.updateArgument(db, argument) != -1) {
											recyclerViewAdapter.updateItem(argument, position);
										}
									}
								});

								Dialog dialog = builder.create();
								dialog.show();

								return true;
							}
						});
						contextMenu.add(getResources().getString(R.string.contextMenuDelete)).setOnMenuItemClickListener(new OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(MenuItem arg0) {
								if (am.deleteArgument(db, argument.getID())) {
									recyclerViewAdapter.removeItem(argument);
								}
								return true;
							}
						});
					}
				});


			}
		}));

		
		//Recupero il riferimento alla EditText del titolo argomento
		editTextArgumentTitle = (EditText) findViewById(R.id.editTextArgumentTitle);
		
		//Recupero il riferimento al bottone di salvataggio dell'argomento
		btnSaveArgumentTitle = (ImageButton) findViewById(R.id.btnSaveArgumentTitle);
		btnSaveArgumentTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
        		Argument argument = new Argument(editTextArgumentTitle.getText().toString(), bookId);
        		long lastId = am.saveArgument(db, argument);
        		argument.setID(lastId);
        		makeArgumentGUI(argument, 0);
			}
		});
		
		
		
		//Assegno alla label della activity il nome del libro selezionato
		setTitle(bookTitle);
	}
	
	/*OnItemClickListener clickItemArgumentListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view,
            int position, long id) {
        	Argument argument = (Argument)adapter.getItemAtPosition(position);

        	Intent intent = new Intent(thisActivity, NotesListActivity.class);
			intent.putExtra("ID_ARGUMENT", argument.getID());
			intent.putExtra("TITLE_ARGUMENT", argument.getArgumentName());
			startActivity(intent);
        }
    };
    
    OnCreateContextMenuListener onCreateContextMenuArgumentListener = new OnCreateContextMenuListener() {
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			
			//Get the ListView
			ListView list = (ListView)v;
			
			//Posizione elemento selezionato
			final int position = ((AdapterContextMenuInfo)menuInfo).position;
			
			//Recupero l'elemento selezionato
			final Argument argument = (Argument) list.getItemAtPosition(position);
			
			menu.setHeaderTitle(getResources().getString(R.string.contextMenuBooks));
			menu.add(getResources().getString(R.string.contextMenuEdit)).setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem arg0) {
					//Toast.makeText(thisActivity, "Modifico elemento con ID: " + book.getID(), Toast.LENGTH_LONG).show();
					//return false;
					AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
					builder.setTitle(getResources().getString(R.string.editDialogTitle));
					
					final EditText editText = new EditText(thisActivity);
					editText.setText(argument.getArgumentName());
					editText.setInputType(InputType.TYPE_CLASS_TEXT);
					editText.selectAll();
					editText.post(new Runnable() {
					    public void run() {
					        InputMethodManager inputMethodManager =  (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
					        inputMethodManager.toggleSoftInputFromWindow(editText.getApplicationWindowToken(),     InputMethodManager.SHOW_FORCED, 0);
					        //editText.requestFocus();
					    };
					});
					builder.setView(editText);
					
					builder.setNegativeButton(getResources().getString(R.string.editDialogCancel), null);
					
					
					builder.setPositiveButton(getResources().getString(R.string.editDialogConfirm), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							argument.setArgumentName(editText.getText().toString());
							if (am.updateArgument(db, argument) != -1) {
								//tvBookTitle.setText(book.getTitle());
								//listViewAdapter.updateItem(argument, position);
								
							}
						};
					});
					
					Dialog dialog = builder.create();
					dialog.show();
					
					return true;
				}
			});
			menu.add(getResources().getString(R.string.contextMenuDelete)).setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem arg0) {
					if (am.deleteArgument(db, argument.getID()) == true) {
						//listViewAdapter.remove(book);
						//listViewAdapter.removeItem(argument);
					}
					return true;
				}
			});
		}
    };*/
	
	/**
	 * Metodo che recupera la lista degli argomenti dal DB associati al libro selezionato
	 */
	private void getArgumentsList() {
		arguments = new ArrayList<Argument>();
		
		try {
			arguments = am.getArgumentsList(db, bookId);
			
			for (int i = 0; i < arguments.size(); i++) {
        		makeArgumentGUI(arguments.get(i), 0);
        	}
		}
		catch (EmptyTableException ex) {
			Toast err = Toast.makeText(this, "Nessun argomento per il libro selezionato", Toast.LENGTH_LONG);
        	err.show();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			//Toast err = Toast.makeText(this, "Errore imprevisto: " + ex.getMessage(), Toast.LENGTH_LONG);
		}
	}
	
	
	
	private void makeArgumentGUI(final Argument argument, int index) {
		recyclerViewAdapter.addItem(argument);
    }
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.arguments_list, menu);


		//Search
		MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
        //////////////  
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {	
			case R.id.action_search:
				onSearchRequested();
				return true;
	    }
		return super.onOptionsItemSelected(item);
	}


	class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
		private GestureDetector gestureDetector;

		public RecyclerTouchListener(Context context,final RecyclerView recyclerView,final ClickListener clickListener) {
			gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
				@Override
				public boolean onSingleTapUp(MotionEvent e) {
					View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
					if (child != null && clickListener != null) {
						clickListener.onClick(child, recyclerView.getChildPosition(child));
					}
					return true;
				}

				@Override
				public boolean onDown(MotionEvent e) {
					View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
					if (child != null && clickListener != null) {
						clickListener.onDown(child, recyclerView.getChildPosition(child));
					}
					return true;
				}
			});
		}

		@Override
		public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
			gestureDetector.onTouchEvent(e);
			return false;
		}

		@Override
		public void onTouchEvent(RecyclerView rv, MotionEvent e) {
			gestureDetector.onTouchEvent(e);
		}
	}


	interface ClickListener {
		void onClick(View v, int position);
		void onDown(View v, int position);
	}
}
