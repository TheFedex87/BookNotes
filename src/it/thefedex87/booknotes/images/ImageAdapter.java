package it.thefedex87.booknotes.images;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    //private Image imageArray[];
    private ArrayList<Image> imageArray;
    private int width;
 
    // Keep all Images in array
    /*public Integer[] mThumbIds = {
            R.drawable.pic_1, R.drawable.pic_2,
            R.drawable.pic_3, R.drawable.pic_4,
            R.drawable.pic_5, R.drawable.pic_6,
            R.drawable.pic_7, R.drawable.pic_8,
            R.drawable.pic_9, R.drawable.pic_10,
            R.drawable.pic_11, R.drawable.pic_12,
            R.drawable.pic_13, R.drawable.pic_14,
            R.drawable.pic_15
    };*/
 
    // Constructor
    /*public ImageAdapter(Context c, ArrayList<Image> imageArray, int width){
        this(c, width);
        this.imageArray = imageArray;
    }*/
    
    public ImageAdapter(Context c, int width) {
    	this.mContext = c;
    	this.width = width - 64;
    	imageArray = new ArrayList<Image>();
    }
 
    @Override
    public int getCount() {
        return imageArray.size();
    }
 
    @Override
    public Object getItem(int position) {
        return imageArray.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	if (convertView == null) {
    		convertView = new ImageView(mContext);
    	}
        
        
        //Bitmap bm = imageArray.get(position).getBm();
        //double rappImmagine = (bm.getWidth() * 1.0f) / (bm.getHeight() * 1.0f);
        convertView.setRotation(90);
        //imageView.setImageBitmap(Bitmap.createScaledBitmap(bm, (int)((width / 2 - width / 100 * 2) * rappImmagine), (int)((width / 2 - width / 100 * 2)), false));
        ((ImageView)convertView).setImageBitmap(imageArray.get(position).getBm());
        ((ImageView)convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setLayoutParams(new GridView.LayoutParams(width / 3 - width / 100 * 2, (int)((width / 3 - width / 100 * 2) / rappImmagine)));
        convertView.setLayoutParams(new GridView.LayoutParams((int)((width / 2 - width / 100 * 2)), (int)((width / 2 - width / 100 * 2))));
        
        /*imgPath = imageArray.get(position).getName();
        imageView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
			}
        });*/
        
        return convertView;
    }
    
    public void addImage(Image image) {
    	imageArray.add(image);
    	notifyDataSetChanged();
    }
    
    public void removeImage(int pos) {
    	imageArray.remove(pos);
    	notifyDataSetChanged();
    }
 
}
