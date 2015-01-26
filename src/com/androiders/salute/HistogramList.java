package com.androiders.salute;

import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

public class HistogramList {

	private Vector<Histogram> mHistograms;
	private Context mContext;
	private Cursor mCursor;
	
	//reads all images from c and constructs histograms for them
	public HistogramList(Cursor c, Context context) {

		mCursor = c;
		mContext = context;
		mHistograms = new Vector<Histogram>();
		c.moveToFirst();
		for(int i = 0; i < c.getCount(); ++i){
			String imagepath = c.getString(c.getColumnIndex(SaluteDB.KEY_IMAGE_PATH));
			Bitmap bmp = ImageHandler.getScaledAndRotatedImage(mContext, imagepath, 4);
			Histogram hist = new Histogram(bmp, 10);
			mHistograms.add(hist);
			c.moveToNext();
		}
	}
	
	public String getNameOfBestMatch(Histogram hist){
		int bestMatchIndex = 0;
		float bestMatch = 999999;
		for(int i = 0; i < mHistograms.size(); ++i){
			float comp = mHistograms.get(i).compare(hist);
			if( comp < bestMatch){
				bestMatch = comp;
				bestMatchIndex = i;
			}
		}
		
		mCursor.moveToPosition(bestMatchIndex);
		
		return mCursor.getString(mCursor.getColumnIndex(SaluteDB.KEY_NAME));
	}
}
