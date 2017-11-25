package it.thefedex87.booknotes.note;

import it.thefedex87.booknotes.Camera;
import it.thefedex87.booknotes.R;
import it.thefedex87.booknotes.database.DBManager;
import it.thefedex87.booknotes.database.EmptyTableException;
import it.thefedex87.booknotes.images.Image;
import it.thefedex87.booknotes.images.ImageAdapter;
import it.thefedex87.booknotes.images.ImagesManager;
import it.thefedex87.booknotes.tabs.SlidingTabLayout;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class NotesListActivity extends ActionBarActivity {
	private Toolbar toolbar;
	private ViewPager mPager;
    private SlidingTabLayout mTabs;
	
	private EditText editTextEditNote;
	private ImageButton btnCamera;
	private ImageButton btnSTT;
	
	//private ArrayList<Image> images;
	
	/**
	 * ID dell' argomento selezionato passato mediante Intent
	 */
	private long argumentId;
	
	/**
	 * Titolo dell'argomento selezionato passato mediante Intent
	 */
	private String argumentTitle;
	
	/**
	 * Notes manager
	 */
	private NotesManager nm;
	
	/**
	 * Imges manager
	 */
	private ImagesManager im;
	
	private Note note;
	
	private Bitmap bm;
	
	private DBManager db;
	
	public static final String APP_IMG_FOLDER = Environment.getExternalStorageDirectory() + "/BookNotes/Media/Images/";
	
	private Activity thisActivity;
	
	private Camera camera;
	
	private GridView gallery;
	
	private ImageAdapter imageAdapter;
	
	private Thread decodeImageThread;
	
	private final int REQ_CODE_SPEECH_INPUT = 100;
	
	/**
	 * Memorizza la posizione del cursore in fase di perdita del focus cosi' da poter inserire il testo alla posizione
	 * del cursore in caso di sppech to text
	 */
	private int cursorPositionOnLostFocus=-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			//Recupero il messaggio passato dalla precedente activity: BOOK_ID
			argumentId = getIntent().getLongExtra("ID_ARGUMENT", -1);
			argumentTitle = getIntent().getStringExtra("TITLE_ARGUMENT");
			Log.i("BookNotes", "Get values from intent");
		}
		else {
			argumentId = savedInstanceState.getLong("ID_ARGUMENT");
			argumentTitle = savedInstanceState.getString("TITLE_ARGUMENT");
			Log.i("BookNotes", "Get values from savedInstanceState");
		}
		setContentView(R.layout.activity_notes_list);
		
		
		//Toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /////////
		
		//Sliding Tabs
        mPager = (ViewPager) findViewById(R.id.pager);
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);
        mTabs.setDistributeEvenly(true);
        mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        mPager.setAdapter(new MyPagerAdapter());
        mTabs.setViewPager(mPager);
        ///////////////////
		
		thisActivity = this;
		
		decodeImageThread = new Thread(new DecodeImageRunnable());
		decodeImageThread.setPriority(Thread.MIN_PRIORITY);
		
		imageAdapter = new ImageAdapter(this, this.getResources().getDisplayMetrics().widthPixels);
		
		
		
		nm = new NotesManager(this);
		
		im = new ImagesManager(this);
			
		db = new DBManager(this);
		
		//images = new ArrayList<Image>();
		
		//Recupera le view del layout
		recuperaLayoutViews();
		
		//Recupero il messaggio passato dalla precedente activity:ARGUMENT_ID
		argumentId = getIntent().getLongExtra("ID_ARGUMENT", -1);
		argumentTitle = getIntent().getStringExtra("TITLE_ARGUMENT");
		this.setTitle(argumentTitle);
		
		getNote();
		
		loadGallery();
	}
	
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		bundle.putLong("ID_ARGUMENT", argumentId);
		bundle.putString("TITLE_ARGUMENT", argumentTitle);
	}
	
	@Override
    public void onBackPressed() {
            super.onBackPressed();
            if (bm!=null) {
            	bm.recycle();
            	bm = null;
            }
            this.finish();
    }
	
	private void recuperaLayoutViews() {
		//tableLayoutNotesTitle = (TableLayout)findViewById(R.id.tableLayoutNotes);
		//Recupero il riferimento alla GridView della gallery
		
		gallery = (GridView)findViewById(R.id.gridViewImages);
		gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
				Intent intent = new Intent();
    	    	intent.setAction(Intent.ACTION_VIEW);
    	    	intent.setDataAndType(Uri.parse("file://" + ((Image)(imageAdapter.getItem(pos))).getName()), "image/*");
    	    	startActivity(intent);
			}
		});
		gallery.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) menuInfo;
				final int pos = contextMenuInfo.position;
				
				menu.setHeaderTitle(Uri.parse(((Image)(imageAdapter.getItem(pos))).getName()).getLastPathSegment());
				menu.add("Elimina").setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem menuItem) {
						AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
						builder.setTitle(getResources().getString(R.string.delDialogTitle));
						
						builder.setNegativeButton(getResources().getString(R.string.delDialogCancel), null);
						
						builder.setPositiveButton(getResources().getString(R.string.delDialogConfirm), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (im.deleteImage(db, ((Image)(imageAdapter.getItem(pos))).getId())) {
									
									ImagesManager.deleteImageFromSD(((Image)(imageAdapter.getItem(pos))).getName());
									
									//images.remove(pos);
									imageAdapter.removeImage(pos);
								}
							};
						});
						
						Dialog dialog = builder.create();
						dialog.show();
						
						return true;
					}
				});
			}
		});
		
		//Recupero il riferimento al TextEdit dove viene visualizzata la nota
		editTextEditNote = (EditText)findViewById(R.id.editTextEditNote);
		editTextEditNote.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if (!hasFocus) {
					cursorPositionOnLostFocus = editTextEditNote.getSelectionStart();
				}
			}
		});
		
	
		//Recupero il riferimento al bottone che apre la fotocamera
		btnCamera = (ImageButton)findViewById(R.id.btnCamera);
		btnCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String newPhotoName =  im.getNextImageName(db);
				
				camera = new Camera(APP_IMG_FOLDER, thisActivity, newPhotoName);
				camera.scatta();
			}
		});
		
		//Recupero il riferimento al bottone che apre lo speech to text
		btnSTT = (ImageButton)findViewById(R.id.btnSTT);
		btnSTT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				promptSpeechInput();
			}
		});
		
		
		
		//gallerySlider = (Gallery)findViewById(R.id.gallerySlider);
		
		//Bitmap bp = BitmapFactory.decodeFile(BooksListActivity.APP_IMG_FOLDER + "00000000000000" + ".jpg");
		//ImageView img = new ImageView(this);
		//ImageView img = (ImageView)findViewById(R.id.imageView1);
		//img.setImageBitmap(bp);
		
		//gallerySlider.addView(img);
		

		setupTabHost();
	}
	
	/**
	 * Metodo richiamato automaticamente dopo avere scattato la foto
	 */
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

       if (requestCode == 1 && resultCode == RESULT_OK) {
    	   
    	   //Se e' stata scattata la foto su una nota non salvata, salvo la nota corrente nel DB
    	   if (note == null) {
    		   note = new Note(editTextEditNote.getText().toString(), argumentId);
    		   note.setID(nm.saveNote(db, note));
    	   }
    	   
    	   
    	   BitmapFactory.Options options = new BitmapFactory.Options();	   
    	   options.inJustDecodeBounds = true;
	   	   options.inPreferredConfig = Config.RGB_565;
	  	   options.inDither = true;
	  	   bm = BitmapFactory.decodeFile(camera.getLastShotName(), options);
	  	   
	  	   options.inSampleSize = ImagesManager.calculateInSampleSize(options, 200,150);
	  	   
	  	   options.inJustDecodeBounds = false;
	  	   
	   	   bm = BitmapFactory.decodeFile(camera.getLastShotName(), options);
	   	   Image newPhoto = new Image(camera.getLastShotName(), bm, note.getID());
    	   
	   	   newPhoto.setId(im.saveImage(db, newPhoto));
	   	   addImageToGallery(newPhoto);
       }
       //Se abbiamo il risultato dello speech to text
       else if(requestCode == REQ_CODE_SPEECH_INPUT) {
    	   if (resultCode == RESULT_OK && null != data) {
    		   
               ArrayList<String> result = data
                       .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
               
               cursorPositionOnLostFocus = editTextEditNote.getSelectionStart();
               String addSTT = editTextEditNote.getText().toString();
               StringBuilder sb = new StringBuilder(addSTT);
               addSTT = sb.insert(cursorPositionOnLostFocus, result.get(0)).toString();
               editTextEditNote.setText(addSTT);
           }
       }
    }
	
	private void setupTabHost() {
		/*TabHost host = (TabHost)findViewById(R.id.tab_host);
		host.setup();

		TabSpec spec = host.newTabSpec("Nota");
		spec.setContent(R.id.tab_text);
		spec.setIndicator("Nota");
		host.addTab(spec);

		spec = host.newTabSpec("Immagini");
		spec.setContent(R.id.tab_gallery);
		spec.setIndicator("Immagini");
		host.addTab(spec);
		
		for(int i=0;i<host.getTabWidget().getChildCount();i++) 
	    {
			View tab = host.getTabWidget().getChildTabViewAt(i);
		    TextView t = (TextView)tab.findViewById(android.R.id.title);
		    //t.setTextColor(this.getResources().getColorStateList(R.color.orange));
	    } */
	}
	
	private void addImageToGallery(Image image) {
		Log.i("BookNotes", image.getName());
		//images.add(image);
		imageAdapter.addImage(image);
	}
	
	private void loadGallery() {
		gallery.setAdapter(imageAdapter);
		
		if (note != null) {
			//Recupero tutte le immagini dal db con la notaId corrente
			//Cursor cursor = db.getImages(note.getID());
			((LinearLayout)findViewById(R.id.layout_loading)).setVisibility(View.VISIBLE);
			decodeImageThread.start();
			
		}
		
		/*BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inDither = true;
		bm = BitmapFactory.decodeFile(APP_IMG_FOLDER + "pic1.jpg", options);
		Bitmap imageArra[] = {bm,bm,bm,bm,bm,bm,bm,bm,bm,bm,bm,bm,bm,bm};*/
		
		/*ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageArra);
		ViewPager myPager = (ViewPager) findViewById(R.id.pager);
		myPager.setAdapter(adapter);
		myPager.setCurrentItem(0);*/
		//DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		//int width = metrics.widthPixels;
		//GridView gridView = (GridView) findViewById(R.id.gridViewImages);
        // Instance of ImageAdapter Class
        //gridView.setAdapter(new ImageAdapter(this, imageArra, width));
		/*myPager.setPadding(20, 0, 20, 0);
		myPager.setClipToPadding(false);
		myPager.setPageMargin(10);*/
		//int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80*2, getResources().getDisplayMetrics());
		//myPager.setPageMargin(-margin);
        
        
	}
	
	private void getNote() {
		//Note note = null;
		try {
			note = nm.getNotesList(db, argumentId);
			
			/*for (int i = 0; i < arguments.size(); i++) {
        		makeArgumentGUI(arguments.get(i), 0);
        	}*/

			editTextEditNote.setText(note.getNote());
		}
		catch (EmptyTableException ex) {
			Toast err = Toast.makeText(this, "Nessuna nota per l'argomento selezionato", Toast.LENGTH_LONG);
        	err.show();
		}
		
	}
	
	/**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
	
	
	
	
	/*private void makeNotesGUI() {
		SpannableStringBuilder formattedString = createSsb(editTextEditNote.getText().toString());
		
		note = formattedString.toString();
		
		editTextEditNote.setText(formattedString, BufferType.SPANNABLE);
		
		
	}*/

	
	/*
	private SpannableStringBuilder createSsb(String text) {
		SpannableStringBuilder ssb = new SpannableStringBuilder(text);
		
		ArrayList imgTag = new ArrayList();
		
		int index = 0;
		while (index < text.length() && index != -1) {
			index = text.indexOf("%%IMG::");
			
			if (index > -1) {
				String imageName = text.substring(index + 7, index + 21);
				
				Bitmap bp = null;
				BitmapFactory.Options options = new BitmapFactory.Options();
				//options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				options.inJustDecodeBounds = true;
				bp = BitmapFactory.decodeFile(BooksListActivity.APP_IMG_FOLDER + imageName + ".jpg", options);
				options.inSampleSize = calculateInSampleSize(options, 200, 200);
				options.inJustDecodeBounds = false;
				bp = BitmapFactory.decodeFile(BooksListActivity.APP_IMG_FOLDER + imageName + ".jpg", options);
				
				
				ssb.setSpan(new ImageSpan(this, bp), index, index + 23, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			    //ssb.setSpan(clickableSpan, 3, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			}
		}
		
		return ssb;
	}
	
	
    */
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes_list, menu);
		
		/*
		// Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    //searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default*/
		
		
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
			case R.id.home:
				NavUtils.navigateUpFromSameTask(this);
		        return true;

		
			case R.id.action_delete_note:
				if (note == null) return true;
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getResources().getString(R.string.delDialogTitle));
				builder.setMessage(getResources().getString(R.string.delNoteDialogDescription));
				
				builder.setNegativeButton(getResources().getString(R.string.delDialogCancel), null);
				
				builder.setPositiveButton(getResources().getString(R.string.delDialogConfirm), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						while (imageAdapter.getCount() > 0) {
							if (!ImagesManager.deleteImageFromSD(((Image)(imageAdapter.getItem(0))).getName())) {return;};
							if (!im.deleteImage(db, ((Image)(imageAdapter.getItem(0))).getId())) {return;};
							//images.remove(0);
							imageAdapter.removeImage(0);
						}
						nm.deleteNote(db, note);
						
						finish();
					}	
				});
				
				builder.create().show();	
				return true;

				
				
			case R.id.action_save_note:
				if (note == null) {
					note = new Note(editTextEditNote.getText().toString(), argumentId);
				}
				else {
					note.setNote(editTextEditNote.getText().toString());
				}
				long lastId = nm.saveNote(db, note);
				note.setID(lastId);
				Toast.makeText(this, getResources().getString(R.string.savedNote), Toast.LENGTH_LONG).show();
				break;
				
			
			case R.id.action_camera:
				String newPhotoName =  im.getNextImageName(db);
				camera = new Camera(APP_IMG_FOLDER, thisActivity, newPhotoName);
				camera.scatta();
				break;
				
				
			case R.id.action_stt:
				promptSpeechInput();
				break;
				
				
				
			case R.id.action_search:
				onSearchRequested();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	
	private class DecodeImageRunnable implements Runnable {
		@Override
		public void run() {
			//setPriority(Thread.MIN_PRIORITY);
			synchronized (this) {
				try {
					final ArrayList<Image> images = im.getImagesList(db, note.getID());
					if (images.size() > 0) {
						for (int i = 0; i < images.size(); i++) {
							bm = images.get(i).getBm();
							final Image image = new Image(images.get(i).getName(), bm, note.getID());
							image.setId(images.get(i).getId());
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									addImageToGallery(image);
								}
							});
						}
					}
				}
				catch(EmptyTableException ex) {
					//Toast.makeText(thisActivity, "Nessuna immagine salvata su questa nota", Toast.LENGTH_LONG).show();
				}
				finally {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							((LinearLayout)findViewById(R.id.layout_loading)).setVisibility(View.GONE);
						}
					});
				}
			}
		};
	}
	
	
	
	/*class MyPagerAdapter extends FragmentPagerAdapter {
        //int[] icons = {R.drawable.ic_action_tab1, R.drawable.ic_action_tab2, R.drawable.ic_action_tab3};
        String[] tabs;
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs_note_activity);
        }

        @Override
        public Fragment getItem(int position) {
            MyFragment myFragment = MyFragment.getInstance(position);
            return myFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //Drawable drawable = getResources().getDrawable(icons[position]);
            //drawable.setBounds(0, 0, 36, 36);
            //ImageSpan imageSpan = new ImageSpan(drawable);
            //SpannableString spannableString = new SpannableString(" ");
            //spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //return spannableString;
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 2;
        }
    }*/
    class MyPagerAdapter extends PagerAdapter {
    	String[] tabs;
		 
	    public Object instantiateItem(View collection, int position) {
	        int resId = 0;
	        switch (position) {
	            case 0:
	                resId = R.id.tab_note_text;
	                break;
	            case 1:
	                resId = R.id.tab_note_gallery;
	                break;
	        }

	        return findViewById(resId);
	    } 
	
	    @Override
	    public int getCount() {
	        return 2;
	    }
	
	    @Override
	    public boolean isViewFromObject(View arg0, Object arg1) {
	        return arg0 == ((View) arg1);
	    }
	    
	    @Override
	    public CharSequence getPageTitle(int position) {
	    	tabs = getResources().getStringArray(R.array.tabs_note_activity);
	    	return tabs[position];
	    }
    }
}
