package com.androiders.salute;

import java.util.Vector;

import android.graphics.Bitmap;

public class Histogram {

	private Vector<Short> mHistogram;
	
	private int mNrBuckets;
	
	public Histogram(Bitmap bmp, int nrBuckets) {
		
		mNrBuckets = nrBuckets;
		mHistogram = new Vector<Short>(mNrBuckets);
		for(int i = 0; i < mNrBuckets; ++i){
			mHistogram.add((short)0);
		}
		
		calcHist(bmp);
	}
	
	
	/**
	 * calculates a grayscale historgram of the bitmap
	 * @param bmp
	 */
	private void calcHist(Bitmap bmp){
		
		Vector<Integer> histogram = new Vector<Integer>(mNrBuckets);
		for(int i = 0; i < mNrBuckets; ++i){
			histogram.add(0);
		}
		
		for(int x = 0; x < bmp.getWidth(); ++x){
			for(int y = 0; y < bmp.getHeight(); ++y){
				int pix = bmp.getPixel(x, y);
				short R = (short)((pix >> 16) & 0xff);
				short G = (short)((pix >> 8) & 0xff);
				short B = (short)(pix & 0xff);
				
				int mean = (R + G + B) / 3;
				//find correct bucket
				int bucket = findBucketForValue(mean);
				
				int val = histogram.get(bucket);
				val += 1;
				histogram.set(bucket, val);
			}
		}
		
		normalize(histogram);
		
	}
	
	
	private int findBucketForValue(int value){
		//255 is highest mean value of color
		int valuesPerBucket = 255 / mNrBuckets;
		int bucket = value / valuesPerBucket;
		return bucket;
		
	}
	/**
	 * normalizes the histogram to have every column between 0 and 255;
	 */
	private void normalize(Vector<Integer> histogram){
		int maxIdx = findMaxIndex(histogram);
		float maxVal = histogram.elementAt(maxIdx);
		
		//normalize values
		for(int i = 0; i < histogram.size(); ++i){
			float tmp = histogram.elementAt(i);
			short normVal = (short)((tmp / maxVal) * 255);
			mHistogram.set(i, normVal);
		}
		
	}
	
	private int findMaxIndex(Vector<Integer> histogram){
		int maxIdx = 0;
		int maxVal = -100;
		for(int i = 0; i < histogram.size(); ++i){
			if(histogram.elementAt(i) > maxVal){
				maxIdx = i;
				maxVal = histogram.elementAt(i);
			}
		}
		
		return maxIdx;
	}

	/**
	 * compare 2 histograms. Returns a value between 0 and 1 representing how equal they are. Lower is better.
	 * NOTE: this method is very brute force and needs to be revised!
	 * @param other
	 * @return
	 */
	public float compare(Histogram other){
		
		//must be of equal size!!!
		if(mNrBuckets != other.mNrBuckets){
			return 99999;
		}
		
		float error  = 0;
		for(int i = 0; i < mHistogram.size(); ++i){
			error +=  Math.abs(mHistogram.get(i) - other.mHistogram.get(i)) / 255.0f;
		}
		
		return error;
	}
	
}
