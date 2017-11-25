package it.thefedex87.booknotes.books;

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

import java.io.File;
import java.util.ArrayList;

import it.thefedex87.booknotes.R;
import it.thefedex87.booknotes.arguments.ArgumentsListActivity;
import it.thefedex87.booknotes.database.DBManager;
import it.thefedex87.booknotes.database.EmptyTableException;
import it.thefedex87.booknotes.note.NotesListActivity;

/**
 * Classe che visualizza l'activity per la gestione dei libri
 * @author federico.creti
 *
 */
public class BooksListActivity extends ActionBarActivity {
	private Toolbar toolbar;

	private RecyclerView listBooks;
	private ImageButton btnSaveBookTitle;
	private EditText editTextBookTitle;

	private BooksManager bm;
	public static DBManager db;


	private Activity thisActivity;

	//private BookBaseAdapter listViewAdapter;
	private BookRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);

        if (savedInstanceState == null) {
        }

        thisActivity = this;

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.drawable.ic_book);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        /////////


        db = new DBManager(this);
        bm = new BooksManager(this);

        //listViewAdapter = new BookListViewAdapter(this, R.layout.book_line_layout);
        //listViewAdapter = new BookBaseAdapter(this);


        recuperaLayoutViews();
        initApp();
        getBooksList();
    }

    private void getBooksList() {
		ArrayList<Book> booksList;

    	booksList = new ArrayList<Book>();
        try {
        	booksList = bm.getBookList(db);

        	for (int i = 0; i < booksList.size(); i++) {
        		makeBookGUI(booksList.get(i), 0);
        	}


        }
        catch (EmptyTableException ex) {
        	Toast err = Toast.makeText(this, "Database libri vuoto", Toast.LENGTH_LONG);
        	err.show();
        }

    }

    private void recuperaLayoutViews() {
    	/*listViewBooks = (ListView)findViewById(R.id.listViewBooks);
    	listViewBooks.setOnItemClickListener(clickItemBookListener);
    	listViewBooks.setOnCreateContextMenuListener(onCreateContextMenuBookListener);
    	listViewBooks.setAdapter(listViewAdapter);*/
		listBooks = (RecyclerView) findViewById(R.id.recyclerViewBooks);
		recyclerViewAdapter = new BookRecyclerViewAdapter(this);
		listBooks.setAdapter(recyclerViewAdapter);
		listBooks.setLayoutManager(new LinearLayoutManager(this));

		listBooks.addOnItemTouchListener(new RecyclerTouchListener(this, listBooks, new ClickListener() {
			@Override
			public void onClick(View view, int position) {
				Book book = (Book)recyclerViewAdapter.getItemAtPosition(position);

				Intent intent = new Intent(thisActivity, ArgumentsListActivity.class);
				intent.putExtra("ID_BOOK", book.getID());
				intent.putExtra("TITLE_BOOK", book.getTitle());
				startActivity(intent);
			}

			//@Override
			//public void onLongClick(View view, final int position) {

			//}

			@Override
			public void onDown(View view, final int position) {
				final TextView textView = (TextView)view.findViewById(R.id.textViewString);

				//Posizione elemento selezionato
				//final int position = ((AdapterContextMenuInfo)contextMenuInfo).position;

				//Recupero l'elemento selezionato
				final Book book = recyclerViewAdapter.getItemAtPosition(position);

				textView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					@Override
					public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo) {
						//Posizione elemento selezionato
						//final int position = ((AdapterContextMenuInfo)contextMenuInfo).position;


						//Recupero l'elemento selezionato
						final Book book = recyclerViewAdapter.getItemAtPosition(position);

						contextMenu.setHeaderTitle(getResources().getString(R.string.contextMenuBooks));
						contextMenu.add(getResources().getString(R.string.contextMenuEdit)).setOnMenuItemClickListener(new OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(MenuItem arg0) {
								//Toast.makeText(thisActivity, "Modifico elemento con ID: " + book.getID(), Toast.LENGTH_LONG).show();
								//return false;
								AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
								builder.setTitle(getResources().getString(R.string.editDialogTitle));

								final EditText editText = new EditText(thisActivity);
								editText.setText(book.getTitle());
								editText.setInputType(InputType.TYPE_CLASS_TEXT);
								editText.selectAll();
								editText.post(new Runnable() {
									public void run() {
										InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
										inputMethodManager.toggleSoftInputFromWindow(editText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
										//editText.requestFocus();
									}
								});
								builder.setView(editText);

								builder.setNegativeButton(getResources().getString(R.string.editDialogCancel), null);


								builder.setPositiveButton(getResources().getString(R.string.editDialogConfirm), new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										book.setTitle(editText.getText().toString());
										if (bm.updateBook(db, book) != -1) {
											//tvBookTitle.setText(book.getTitle());
											//listViewAdapter.updateItem(book, position);
											recyclerViewAdapter.updateItem(book, position);
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
								AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
								builder.setTitle(getResources().getString(R.string.delDialogTitle));

								builder.setPositiveButton(R.string.delDialogConfirm, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialogInterface, int i) {
										if (bm.deleteBook(db, book) == true) {
											//listViewAdapter.remove(book);
											//listViewAdapter.removeItem(book);
											recyclerViewAdapter.removeItem(book);
										}
									}
								});

								builder.setNegativeButton(R.string.delDialogCancel, null);

								Dialog dialog = builder.create();
								dialog.show();

								return true;
							}
						});
					}
				});
			}
		}));



    	//Recupero la view Button per salvare il libro
        btnSaveBookTitle = (ImageButton)findViewById(R.id.btnSaveBookTitle);
        btnSaveBookTitle.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        		editTextBookTitle = (EditText)findViewById(R.id.editTextBookTitle);
        		Book book = new Book(editTextBookTitle.getText().toString());
        		long lastId = bm.saveBook(db, book);
        		book.setID(lastId);
        		makeBookGUI(book, 0);
        	}
        });
    }

    /*OnItemClickListener clickItemBookListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view,
            int position, long id) {
        	Book book = (Book)adapter.getItemAtPosition(position);

        	Intent intent = new Intent(thisActivity, ArgumentsListActivity.class);
			intent.putExtra("ID_BOOK", book.getID());
			intent.putExtra("TITLE_BOOK", book.getTitle());
			startActivity(intent);
        }
    };
    
    OnCreateContextMenuListener onCreateContextMenuBookListener = new OnCreateContextMenuListener() {
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			
			//Get the ListView
			ListView list = (ListView)v;

			//Posizione elemento selezionato
			final int position = ((AdapterContextMenuInfo)menuInfo).position;

			//Recupero l'elemento selezionato
			final Book book = (Book) list.getItemAtPosition(position);
			
			menu.setHeaderTitle(getResources().getString(R.string.contextMenuBooks));
			menu.add(getResources().getString(R.string.contextMenuEdit)).setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem arg0) {
					//Toast.makeText(thisActivity, "Modifico elemento con ID: " + book.getID(), Toast.LENGTH_LONG).show();
					//return false;
					AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
					builder.setTitle(getResources().getString(R.string.editDialogTitle));
					
					final EditText editText = new EditText(thisActivity);
					editText.setText(book.getTitle());
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
							book.setTitle(editText.getText().toString());
							if (bm.updateBook(db, book) != -1) {
								//tvBookTitle.setText(book.getTitle());
								//listViewAdapter.updateItem(book, position);
								
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
					if (bm.deleteBook(db, book) == true) {
						//listViewAdapter.remove(book);
						//listViewAdapter.removeItem(book);
					}
					return true;
				}
			});
		}
    };*/


    private void initApp() {
    	createAppDirs();


    }

    private boolean createAppDirs() {
    	File appDirectory = new File(NotesListActivity.APP_IMG_FOLDER);
    	return appDirectory.mkdirs();
    }

    private void makeBookGUI(final Book book, int index) {
    	//listViewAdapter.add(book);
    	//listViewAdapter.addItem(book);
        recyclerViewAdapter.addItem(book);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.books_list, menu);


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

		public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
			gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
				@Override
				public boolean onSingleTapUp(MotionEvent e) {
					View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
					if (child != null && clickListener != null) {
						clickListener.onClick(child, recyclerView.getChildPosition(child));
					}
					return true;
				}

				/*@Override
				public void onLongPress(MotionEvent e) {
					View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
					if (child != null && clickListener != null) {
						clickListener.onLongClick(child, recyclerView.getChildPosition(child));
					}
				}*/

				@Override
				public boolean onDown(MotionEvent e) {
					View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
					if (child != null && clickListener != null) {
						clickListener.onDown(child, recyclerView.getChildPosition(child));
					}
					return false;
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
		void onClick(View view, int position);
		//void onLongClick(View view, int position);
		void onDown(View view, int position);
	}
}
