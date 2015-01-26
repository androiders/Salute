package com.androiders.salute;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class ImageHandler {


	public static Bitmap getScaledBitmap(String imagePath, int scaleFactor){
		// Get the dimensions of the bitmap
		
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//	    bmOptions.inJustDecodeBounds = true;
//	    BitmapFactory.decodeFile(imagePath, bmOptions);

	    // Determine how much to scale down the image

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    //bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
	    return bitmap;
	}

	public static Bitmap getScaledBitmapToWidth(String imagePath, int newWidth){
		// Get the dimensions of the bitmap
		
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(imagePath, bmOptions);

	    int factor = bmOptions.outWidth / newWidth; 

	    return getScaledBitmap(imagePath, factor);
	    
	    // Determine how much to scale down the image

//	    // Decode the image file into a Bitmap sized to fill the View
//	    bmOptions.inJustDecodeBounds = false;
//	    bmOptions.inSampleSize = scaleFactor;
//	    //bmOptions.inPurgeable = true;
//
//	    Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
//	    return bitmap;
	}
	
	
	static public Bitmap getScaledAndRotatedImage(Context context,String imagePath, int scaleFactor){
		int rotateImage = getCameraPhotoOrientation(context, imagePath);
		Matrix rot = new Matrix();
		rot.postRotate(rotateImage);
		Bitmap tmp = getScaledBitmap(imagePath, scaleFactor);
		tmp = Bitmap.createBitmap(tmp , 0, 0, tmp.getWidth(), tmp.getHeight(), rot, true);
		
		return tmp;
	}
	
	
	static public void rotatePhoto90cw(Context context, String imagePath){
		setPhotoOrientation(context, imagePath, 90);
	}

	static public void rotatePhoto90ccw(Context context, String imagePath){
		setPhotoOrientation(context, imagePath, -90);
	}

	
	/**
	 * 
	 * @param context
	 * @param imagePath
	 * @param rotation must be -90 or 90!!!
	 * @return
	 */
	static private int setPhotoOrientation(Context context, String imagePath, int rotation){
	    int rotate = 0;
	    try {

	    	File imageFile = new File(imagePath);

	        ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
	        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//	        orientation = getOrientationFromMediaStore(context, imagePath);

	        switch (orientation) {
	        case ExifInterface.ORIENTATION_ROTATE_270:
	            rotate = 270;
	            break;
	        case ExifInterface.ORIENTATION_ROTATE_180:
	            rotate = 180;
	            break;
	        case ExifInterface.ORIENTATION_ROTATE_90:
	            rotate = 90;
	            break;
	        }

	        rotate += rotation;
	        int rotVal = 0;
	        
	        if(Math.abs(rotate) == 360 || Math.abs(rotate) == 0)
	        	rotVal = ExifInterface.ORIENTATION_NORMAL;
	        else if(Math.abs(rotate) == 180)
	        	rotVal = ExifInterface.ORIENTATION_ROTATE_180;
	        else if(rotate == -90)
	        	rotVal = ExifInterface.ORIENTATION_ROTATE_270;
	        else if(rotate == 90)
	        	rotVal = ExifInterface.ORIENTATION_ROTATE_90;
	        
	        
	        String rot = String.valueOf(rotVal);
	        exif.setAttribute(ExifInterface.TAG_ORIENTATION, rot);
	    
	        exif.saveAttributes();
	        
//	        Log.i("RotateImage", "Exif orientation: " + orientation);
//	        Log.i("RotateImage", "Rotate value: " + rotate);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return rotate;

	}
	
	static public int getCameraPhotoOrientation(Context context, String imagePath){
		
	    int rotate = 0;
	    try {
	        //context.getContentResolver().notifyChange(imageUri, null);
	        File imageFile = new File(imagePath);

	        ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
	        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//	        orientation = getOrientationFromMediaStore(context, imagePath);

	        switch (orientation) {
	        case ExifInterface.ORIENTATION_ROTATE_270:
	            rotate = 270;
	            break;
	        case ExifInterface.ORIENTATION_ROTATE_180:
	            rotate = 180;
	            break;
	        case ExifInterface.ORIENTATION_ROTATE_90:
	            rotate = 90;
	            break;
	        }

//	        Log.i("RotateImage", "Exif orientation: " + orientation);
//	        Log.i("RotateImage", "Rotate value: " + rotate);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return rotate;
	}
	
	
	
//	private static int getOrientationFromMediaStore(Context context, String imagePath) {
//	    Uri imageUri = getImageContentUri(context, imagePath);
//	    if(imageUri == null) {
//	        return -1;
//	    }
//
//	    String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
//	    Cursor cursor = context.getContentResolver().query(imageUri, projection, null, null, null);
//
//	    int orientation = -1;
//	    if (cursor != null && cursor.moveToFirst()) {
//	        orientation = cursor.getInt(0);
//	        cursor.close();
//	    }
//
//	    return orientation;
//	}
//	
//	private static Uri getImageContentUri(Context context, String imagePath) {
//	    String[] projection = new String[] {MediaStore.Images.Media._ID};
//	    String selection = MediaStore.Images.Media.DATA + "=? ";
//	    String[] selectionArgs = new String[] {imagePath};
//	    Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, 
//	            selection, selectionArgs, null);
//
//	    if (cursor != null && cursor.moveToFirst()) {
//	        int imageId = cursor.getInt(0);
//	        cursor.close();
//
//	        return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(imageId));
//	    } 
//
//	    if (new File(imagePath).exists()) {
//	        ContentValues values = new ContentValues();
//	        values.put(MediaStore.Images.Media.DATA, imagePath);
//
//	        return context.getContentResolver().insert(
//	                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//	    } 
//
//	    return null;
//	}
	
}
