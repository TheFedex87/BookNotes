package it.thefedex87.booknotes.images;

import it.thefedex87.booknotes.R;
import it.thefedex87.booknotes.database.DBManager;
import it.thefedex87.booknotes.database.DBStrings;
import it.thefedex87.booknotes.database.EmptyTableException;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImagesManager {

	private Context context;
	
	public ImagesManager(Context context) {
		this.context = context;
	}

	public ArrayList<Image> getImagesList(DBManager dbManager, long noteId) throws EmptyTableException {
		ArrayList<Image> imagesList = new ArrayList<Image>();
		
		if (dbManager==null) {
			Log.e("BookNotes", "Errore dbManager");
		}
		
		Cursor cursor = dbManager.getImages(noteId);
    	
		if (cursor.getCount() <= 0) {
			throw new EmptyTableException(context.getResources().getString(R.string.empty_db_ex));
		}
		
		cursor.moveToFirst();
    	for (int i = 0; i < cursor.getCount(); i++) {
    		BitmapFactory.Options options = new BitmapFactory.Options();
     	    options.inJustDecodeBounds = true;
 	   	    options.inPreferredConfig = Config.RGB_565;
 	  	    options.inDither = true;
 	  	    
 	  	    Bitmap bm;
 	  	    
 	  	    bm = BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndex(DBStrings.TABLE_IMAGES_FIELD_NAME)), options);
	  	   
 	  	    options.inSampleSize = calculateInSampleSize(options, 200, 150);
	  	   
	  	    options.inJustDecodeBounds = false;
 	  	    
    		bm = BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndex(DBStrings.TABLE_IMAGES_FIELD_NAME)), options);
    		
    		Image image = new Image(cursor.getString(cursor.getColumnIndex(DBStrings.TABLE_IMAGES_FIELD_NAME)), 
    				bm, 
    				cursor.getLong(cursor.getColumnIndex(DBStrings.TABLE_IMAGES_FIELD_ID_NOTE)));
    		image.setId(cursor.getLong(cursor.getColumnIndex(DBStrings.TABLE_IMAGES_FIELD_ID)));
    		imagesList.add(image);
    		
    		cursor.moveToNext();
    	}
    	
    	return imagesList;
	}

	public long saveImage(DBManager dbManager, Image image) {
		try {
			return dbManager.putNewImage(image);
		}
		catch (SQLiteException ex) {
			return -1;
		}
	}
	
	public String getNextImageName(DBManager dbManager) {
		return ("0000000000" + Long.toString(dbManager.getLastTableImagesID() + 1))
				.substring(("0000000000" + Long.toString(dbManager.getLastTableImagesID() + 1)).length() - 10);
	}
	
	public boolean deleteImage(DBManager dbManager, long imageId) {
		try {
			return dbManager.deleteImage(imageId);
		}
		catch (SQLiteException ex) {
			return false;
		}
	}
	
	public static boolean deleteImageFromSD(String path) {
		File file = new File(path);
		boolean deleted = file.delete();
		if (deleted) {
			Log.d("BookNotes", file.getAbsolutePath() + ": Immagine eliminata");
		}
		else {
			Log.d("BookNotes", file.getAbsolutePath() + ": Errore eliminazione immagine");
		}
		return deleted;
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
