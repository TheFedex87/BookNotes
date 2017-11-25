package it.thefedex87.booknotes;

import it.thefedex87.booknotes.arguments.Argument;
import it.thefedex87.booknotes.arguments.ArgumentBaseAdapter;
import it.thefedex87.booknotes.arguments.ArgumentsListActivity;
import it.thefedex87.booknotes.arguments.ArgumentsManager;
import it.thefedex87.booknotes.arguments.ArgumentsRecyclerViewAdapter;
import it.thefedex87.booknotes.books.Book;
import it.thefedex87.booknotes.books.BookBaseAdapter;
import it.thefedex87.booknotes.books.BookRecyclerViewAdapter;
import it.thefedex87.booknotes.books.BooksManager;
import it.thefedex87.booknotes.database.DBManager;
import it.thefedex87.booknotes.database.NoMatchFoundException;
import it.thefedex87.booknotes.note.Note;
import it.thefedex87.booknotes.note.NotesArrayAdapter;
import it.thefedex87.booknotes.note.NotesListActivity;
import it.thefedex87.booknotes.note.NotesManager;
import it.thefedex87.booknotes.note.NotesRecyclerViewAdapter;
import it.thefedex87.booknotes.tabs.SlidingTabLayout;

import java.util.ArrayList;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class SearchActivity extends ActionBarActivity {
	private Toolbar toolbar;
	private ViewPager mPager;
    private SlidingTabLayout mTabs;
	
    private Activity thisActivity;
	
    private DBManager db;
	
    private BooksManager bm;
    private BookRecyclerViewAdapter booksAdapter;
    //private BookBaseAdapter bookAdapter;
	
    private ArgumentsManager am;
    private ArgumentsRecyclerViewAdapter argumentsAdapter;
    //private ArgumentBaseAdapter argumentAdapter;
	
    private NotesManager nm;
    private NotesRecyclerViewAdapter notesAdapter;
    //private NotesArrayAdapter noteAdapter;
	
    /*private ListView lvBooks;
    private ListView lvArguments;
    private ListView lvNotes;*/
	private RecyclerView rvBooks;
	private RecyclerView rvArguments;
	private RecyclerView rvNotes;
	
    private String query = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		// Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    query = "";
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      query = intent.getStringExtra(SearchManager.QUERY);
	      //Toast.makeText(this, "TEST RICERCA: " + query, Toast.LENGTH_LONG).show();
	    }
	    
	    //Toolbar
	    toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
	    //Hide action bar logo
	    getSupportActionBar().setDisplayShowHomeEnabled(false);
	    
	    //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    ///////
	    
	    //Sliding Tabs
	    mPager = (ViewPager) findViewById(R.id.pager);
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);
        mTabs.setDistributeEvenly(true);
        mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(new MyPagerAdapter());
        mTabs.setViewPager(mPager);
        ///////////////////
        
	    thisActivity = this;
	    
	    db = new DBManager(this);
	    
	    bm = new BooksManager(this);
	    booksAdapter = new BookRecyclerViewAdapter(this);
	    //bookAdapter = new BookBaseAdapter(this);
	    
	    am = new ArgumentsManager(this);
	    argumentsAdapter = new ArgumentsRecyclerViewAdapter(this);
	    //argumentAdapter = new ArgumentBaseAdapter(this);/
	    
	    nm = new NotesManager(this);
	    
	    
	    
	    findBooks(query);
	    findArguments(query);
	    findNotes(query);
	    
	    
	    recuperaLayoutViews();
	}
	
	private void recuperaLayoutViews() {
		setupTabHost();
		
		setTitle("Ricerca: " + query);
		
		/*lvBooks = (ListView) findViewById(R.id.list_view_search_books);
		lvBooks.setOnItemClickListener(clickItemBookListener);
		lvBooks.setAdapter(bookAdapter);
		
		lvArguments = (ListView) findViewById(R.id.list_view_search_arguments);
		lvArguments.setOnItemClickListener(clickItemArgumentListener);
		lvArguments.setAdapter(argumentAdapter);
		
		lvNotes = (ListView) findViewById(R.id.list_view_search_notes);
		lvNotes.setOnItemClickListener(clickItemNoteListener);
		lvNotes.setAdapter(noteAdapter);*/

		rvBooks = (RecyclerView)findViewById(R.id.recyclerViewBooks);
		rvBooks.setAdapter(booksAdapter);
		rvBooks.setLayoutManager(new LinearLayoutManager(this));
		rvBooks.addOnItemTouchListener(new RecyclerTouchListener(this, rvBooks, new ClickListener() {
			@Override
			public void onClick(View v, int position) {
				Book book = booksAdapter.getItemAtPosition(position);

				Intent intent = new Intent(thisActivity, ArgumentsListActivity.class);
				intent.putExtra("ID_BOOK", book.getID());
				intent.putExtra("TITLE_BOOK", book.getTitle());
				startActivity(intent);
			}
		}));


		rvArguments = (RecyclerView)findViewById(R.id.recyclerViewArguments);
		rvArguments.setAdapter(argumentsAdapter);
		rvArguments.setLayoutManager(new LinearLayoutManager(this));
		rvArguments.addOnItemTouchListener(new RecyclerTouchListener(this, rvArguments, new ClickListener() {
			@Override
			public void onClick(View v, int position) {
				Argument argument = (Argument) argumentsAdapter.getItemAtPosition(position);

				Intent intent = new Intent(thisActivity, NotesListActivity.class);
				intent.putExtra("ID_ARGUMENT", argument.getID());
				intent.putExtra("TITLE_ARGUMENT", argument.getArgumentName());
				startActivity(intent);
			}
		}));


		rvNotes = (RecyclerView)findViewById(R.id.recyclerViewNotes);
		rvNotes.setAdapter(notesAdapter);
		rvNotes.setLayoutManager(new LinearLayoutManager(this));
		rvNotes.addOnItemTouchListener(new RecyclerTouchListener(this, rvNotes, new ClickListener() {
			@Override
			public void onClick(View v, int position) {
				Note note = (Note)notesAdapter.getItemAtPosition(position);

				try {
					Intent intent = new Intent(thisActivity, NotesListActivity.class);
					intent.putExtra("ID_ARGUMENT", note.getArgumentId());
					intent.putExtra("TITLE_ARGUMENT", am.getArgumentTitleById(db, note.getArgumentId()));
					startActivity(intent);
				}
				catch (NoMatchFoundException ex) {
					Toast.makeText(thisActivity, ex.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		}));
	}

	private void setupTabHost() {
		/*TabHost host = (TabHost)findViewById(R.id.tab_host_search);
		host.setup();

		TabSpec spec = host.newTabSpec("Books");
		spec.setContent(R.id.tab_search_book);
		spec.setIndicator("Libri");
		host.addTab(spec);

		spec = host.newTabSpec("Arguments");
		spec.setContent(R.id.tab_search_argument);
		spec.setIndicator("Argomenti");
		host.addTab(spec);
		
		spec = host.newTabSpec("Notes");
		spec.setContent(R.id.tab_search_note);
		spec.setIndicator("Note");
		host.addTab(spec);
		
		for(int i=0;i<host.getTabWidget().getChildCount();i++) 
	    {
			View tab = host.getTabWidget().getChildTabViewAt(i);
		    TextView t = (TextView)tab.findViewById(android.R.id.title);
		    //t.setTextColor(this.getResources().getColorStateList(R.color.orange));
	    } */
	}
	
	private void findBooks(String queryString) {
		try {
			ArrayList<Book> listBooks = bm.findBook(db, queryString);
			
			for (Book book: listBooks) {
				booksAdapter.addItem(book);
			}
		}
		catch (NoMatchFoundException ex) {	
		}
	}
	
	private void findArguments(String queryString) {
		try {
			ArrayList<Argument> listArguments = am.findArgument(db, queryString);
			
			for (Argument argument: listArguments) {
				argumentsAdapter.addItem(argument);
			}
		}
		catch (NoMatchFoundException ex) {	
		}
	}
	
	private void findNotes(String queryString) {
		ArrayList<Note> noteList = new ArrayList<Note>();
		try {
			ArrayList<Note> listNotes = nm.findNote(db, queryString);
			
			for (Note note: listNotes) {
				noteList.add(note);
			}
			
			//noteAdapter = new NotesArrayAdapter(this, R.layout.line_layout, listNotes);
			notesAdapter = new NotesRecyclerViewAdapter(this, listNotes);
		}
		catch (NoMatchFoundException ex) {	
		}
	}
	
	OnItemClickListener clickItemBookListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			Book book = (Book)adapter.getItemAtPosition(position);

        	Intent intent = new Intent(thisActivity, ArgumentsListActivity.class);
			intent.putExtra("ID_BOOK", book.getID());
			intent.putExtra("TITLE_BOOK", book.getTitle());
			startActivity(intent);
		}
	};
	
	OnItemClickListener clickItemArgumentListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			Argument argument = (Argument)adapter.getItemAtPosition(position);

        	Intent intent = new Intent(thisActivity, NotesListActivity.class);
        	intent.putExtra("ID_ARGUMENT", argument.getID());
			intent.putExtra("TITLE_ARGUMENT", argument.getArgumentName());
			startActivity(intent);
		}
	};
	
	OnItemClickListener clickItemNoteListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			Note note = (Note)adapter.getItemAtPosition(position);

			try {
	        	Intent intent = new Intent(thisActivity, NotesListActivity.class);
				intent.putExtra("ID_ARGUMENT", note.getArgumentId());
				intent.putExtra("TITLE_ARGUMENT", am.getArgumentTitleById(db, note.getArgumentId()));
				startActivity(intent);	
			}
			catch (NoMatchFoundException ex) {
				Toast.makeText(thisActivity, ex.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	class MyPagerAdapter extends PagerAdapter {
    	String[] tabs;
		 
	    public Object instantiateItem(View collection, int position) {
	        int resId = 0;
	        switch (position) {
	            case 0:
	                resId = R.id.tab_search_book;
	                break;
	            case 1:
	                resId = R.id.tab_search_argument;
	                break;
	            case 2:
	                resId = R.id.tab_search_note;
	                break;
	        }

	        return findViewById(resId);
	    } 
	
	    @Override
	    public int getCount() {
	        return 3;
	    }
	
	    @Override
	    public boolean isViewFromObject(View arg0, Object arg1) {
	        return arg0 == ((View) arg1);
	    }
	    
	    @Override
	    public CharSequence getPageTitle(int position) {
	    	tabs = getResources().getStringArray(R.array.tabs_search_activity);
	    	return tabs[position];
	    }
    }

	class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
		GestureDetector gestureDetector;

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
	}
}
