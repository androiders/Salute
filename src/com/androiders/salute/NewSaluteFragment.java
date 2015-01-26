package com.androiders.salute;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

public class NewSaluteFragment extends Fragment {

//	private Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	
	private int CAMERA_REQUEST_CODE = 1;

	private ImageView mImageView;
	
	String mCurrentPhotoPath;

	private Bitmap mBitmap;

	private static String IMG_PATH_TAG = "currentImgPath";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(MainActivity.TAG,"onCreate in NewSaluteFragment");

		mBitmap = null;
		if(savedInstanceState != null){
			mCurrentPhotoPath = savedInstanceState.getString(IMG_PATH_TAG);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.new_salute_fragment, container,false);
		Log.i(MainActivity.TAG,"onCreateView in NewSaluteFragment");
		return root;
	}

	@Override
		public void onResume() {
			super.onResume();
			
			//mBitmap should be saved in onSave....
			if(mCurrentPhotoPath != null && mBitmap == null)
				fixImage();
			
			if(mBitmap != null){
				mImageView.setImageBitmap(mBitmap);
//				mImage.setBackground(new BitmapDrawable(getResources(),mBitmap));
			}
			
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	
		Log.i(MainActivity.TAG,"onActivityCreated in NewSaluteFragment");
		
		mImageView = (ImageView) getView().findViewById(R.id.image);
		
		Button b = (Button) getView().findViewById(R.id.add_button);
		
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Uri mNewUri;

				// Defines an object to contain the new values to insert
				ContentValues mNewValues = new ContentValues();

				String name = getName();
				if(name.length() == 0){
					Toast.makeText(getActivity(), "Please fill in at least name",Toast.LENGTH_SHORT).show();
					return;
				}
				/*
				 * Sets the values of each column and inserts the word. The arguments to the "put"
				 * method are "column name" and "value"
				 */
				mNewValues.put(SaluteDB.KEY_NAME, getName());
				mNewValues.put(SaluteDB.KEY_DESCR, getDescription());
				mNewValues.put(SaluteDB.KEY_RATING, getRating());
				mNewValues.put(SaluteDB.KEY_IMAGE_PATH, mCurrentPhotoPath);

				Log.i(MainActivity.TAG,"saving image " + mCurrentPhotoPath);
				
				mNewUri = getActivity().getContentResolver().insert(
				    SaluteContentProvider.CONTENT_URI,   // the user dictionary content URI
				    mNewValues                          // the values to insert
				);
				getActivity().getContentResolver().notifyChange(mNewUri,null);
				
				//close this fragment
				getFragmentManager().popBackStack();
				
				
			}

		});
		
		
		Button pic = (Button) getView().findViewById(R.id.photo_button);
		
		pic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				dispatchTakePictureIntent();
			}

		});
		
		
		mImageView.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    	builder.setTitle("Rotate image")
		    		.setMessage("Rotate image")
		    		.setIcon(android.R.drawable.ic_menu_directions)
		    		.setPositiveButton("counter clockwise", new DialogInterface.OnClickListener() {
		    	    public void onClick(DialogInterface dialog, int which) {			      	
		    	    	//Yes button clicked, do something
		    	    	ImageHandler.rotatePhoto90ccw(getActivity(), mCurrentPhotoPath);
		    	    	fixImage();
		    	    	mImageView.setImageBitmap(mBitmap);
		    	    }
		    	})
		    	.setNegativeButton("clockwise", new DialogInterface.OnClickListener() {
		    	    public void onClick(DialogInterface dialog, int which) {			      	
		    	    	//No button clicked, do something
		    	    	ImageHandler.rotatePhoto90cw(getActivity(), mCurrentPhotoPath);
		    	    	fixImage();
		    	    	mImageView.setImageBitmap(mBitmap);
		    	    }
		    	})
		    	.show();
				return true;			}
		});
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CAMERA_REQUEST_CODE)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				try{
					
					fixImage();
				}catch(Exception e)
				{
					Log.i(MainActivity.TAG,e.toString());
				}
			}
		}
	}
	
	
	private void fixImage(){
//		int rotateImage = ImageHandler.getCameraPhotoOrientation(getActivity(), mCurrentPhotoPath);
//		Matrix rot = new Matrix();
//		rot.postRotate(rotateImage);
//		Bitmap tmp = ImageHandler.getScaledBitmap(mCurrentPhotoPath, 2);
//		tmp = Bitmap.createBitmap(tmp , 0, 0, tmp.getWidth(), tmp.getHeight(), rot, true);
//		mBitmap = tmp;
		
		mBitmap = ImageHandler.getScaledAndRotatedImage(getActivity(), mCurrentPhotoPath, 2);
	}
	
	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	        	Toast.makeText(getActivity(), "Could not create image file!",Toast.LENGTH_SHORT).show();
	        	Log.i(MainActivity.TAG,ex.toString());
	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	                    Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
	        }
	    }
	}

	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis()));
	    String imageFileName = "Salute_" + timeStamp + "_";
	    File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = image.getAbsolutePath();
	    Log.i(MainActivity.TAG,"saving image file " + mCurrentPhotoPath);
	    return image;
	}
	
	private float getRating() {
		RatingBar rb = (RatingBar) getView().findViewById(R.id.rating);
		return rb.getRating();
	}

	private String getDescription() {
		EditText descr = (EditText) getView().findViewById(R.id.descr);
		return descr.getText().toString();
	}

	private String getName() {
		EditText name = (EditText) getView().findViewById(R.id.name);
		return name.getText().toString();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		
		outState.putString(IMG_PATH_TAG , mCurrentPhotoPath);
		super.onSaveInstanceState(outState);
	
	}
	
}
