package it.thefedex87.booknotes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ViewPagerAdapter extends PagerAdapter {

	 Activity activity;
	 Bitmap imageArray[];

	 public ViewPagerAdapter(Activity act, Bitmap[] imgArra) {
		 imageArray = imgArra;
		 activity = act;
	 }

	 public int getCount() {
		 return imageArray.length;
	 }

	 public Object instantiateItem(View collection, int position) {
		  ImageView view = new ImageView(activity);
		  view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				    LayoutParams.WRAP_CONTENT));
		  
		  view.setScaleType(ScaleType.FIT_XY);
		  //view.setBackgroundResource(imageArray[position]);
		  view.setAdjustViewBounds(true);
		  view.setScaleType(ImageView.ScaleType.FIT_CENTER);
		  Bitmap bm = imageArray[position];
		  view.setImageBitmap(Bitmap.createScaledBitmap(bm, 150, 120, false));
		  ((ViewPager) collection).addView(view, 0);
		  return view;
	 }

	 @Override
	 public void destroyItem(View arg0, int arg1, Object arg2) {
	  ((ViewPager) arg0).removeView((View) arg2);
	 }

	 @Override
	 public boolean isViewFromObject(View arg0, Object arg1) {
	  return arg0 == ((View) arg1);
	 }

	 @Override
	 public Parcelable saveState() {
	  return null;
	 }
	}
