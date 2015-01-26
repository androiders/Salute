package com.androiders.salute;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ViewSaluteFragment extends Fragment {

	
	private String mName;
	
	private String mDescr;
	
	private String mImagePath;
	
	private float mRating;

	private TextView mNameView;

	private TextView mDescrView;

	private RatingBar mRatingBar;

	private ImageView mImageView;
	
	public ViewSaluteFragment() {
		super();
	}
	
	public ViewSaluteFragment(String name, String descr, String imgPath, float rating) {
		super();
		mName = name;
		mDescr = descr;
		mImagePath = imgPath;
		mRating = rating;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null){
			mName = savedInstanceState.getString("name");
			mDescr = savedInstanceState.getString("descr");
			mImagePath = savedInstanceState.getString("path");
			mRating = savedInstanceState.getFloat("rating");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		    
		View rootView = inflater.inflate(R.layout.view_salute_fragment, container, false);
		mNameView = (TextView) rootView.findViewById(R.id.viewname);	
		mDescrView = (TextView) rootView.findViewById(R.id.viewdescr);
		mRatingBar =(RatingBar) rootView.findViewById(R.id.viewrating);
		mRatingBar.setIsIndicator(true);
		mImageView = (ImageView) rootView.findViewById(R.id.viewimage);
		
		
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mNameView.setText(mName);
		mRatingBar.setRating(mRating);
		mDescrView.setText(mDescr);
		
		Bitmap bitmap = ImageHandler.getScaledAndRotatedImage(getActivity(), mImagePath, 2);
		mImageView.setImageBitmap(bitmap);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("name", mName);
		outState.putString("descr", mDescr);
		outState.putString("path", mImagePath);
		outState.putFloat("rating", mRating);
		super.onSaveInstanceState(outState);
	}
	
}
