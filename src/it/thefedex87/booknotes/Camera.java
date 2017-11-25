package it.thefedex87.booknotes;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class Camera {

	private String appImgFolder;
	static final int REQUEST_TAKE_PHOTO = 1;
	private Activity activity;
	private String lastShotName;
	
	public Camera(String appImgFolder, Activity activity, String lastShotName) {
		this.appImgFolder = appImgFolder;
		this.activity = activity;
		this.lastShotName = lastShotName + ".jpg";
	}

	public void scatta() {
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intent, 0);
		
		// Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        
        // Continue only if the File was successfully created
        if (photoFile != null) {
        	lastShotName = photoFile.getAbsolutePath();
        	intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
        	activity.startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
	}
	
	
    
    public String getLastShotName() {
		return lastShotName;
	}



	String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        /*String imageFileName = lastShotName;
        File storageDir = new File(appImgFolder);
        File image = File.createTempFile(
            imageFileName,  // prefix
            ".jpg",         // suffix
            storageDir      // directory
        );*/

    	
    	File image = new File(appImgFolder + lastShotName);
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
}
