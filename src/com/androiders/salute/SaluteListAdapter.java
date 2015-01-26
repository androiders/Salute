package com.androiders.salute;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class SaluteListAdapter extends ResourceCursorAdapter {

	private ImageView mImageView;
	private String mCurrentPhotoPath;
	private Context mContext;

	public SaluteListAdapter(Context context, int layout, Cursor c, int flag) {
		super(context, layout, c, flag);
		mContext = context;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// Set display name
		TextView textView = (TextView) view.findViewById(R.id.listname);
		textView.setText(cursor.getString(cursor
				.getColumnIndex(SaluteDB.KEY_NAME)));

		RatingBar bar = (RatingBar) view.findViewById(R.id.listrating);
		bar.setRating(cursor.getFloat(cursor.getColumnIndex(SaluteDB.KEY_RATING)));
		
		mImageView = (ImageView) view.findViewById(R.id.listimage);
		
		mCurrentPhotoPath = cursor.getString(cursor.getColumnIndex(SaluteDB.KEY_IMAGE_PATH));
		Log.i(MainActivity.TAG,"reading image " + mCurrentPhotoPath);
		
		if(mCurrentPhotoPath != null && mCurrentPhotoPath.length() != 0)
			setPic();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		return inflater.inflate(R.layout.salute_list_item, parent, false);

	}
	
	private void setPic() {
		
		Bitmap bitmap = ImageHandler.getScaledAndRotatedImage(mContext, mCurrentPhotoPath, 10);
	    mImageView.setImageBitmap(bitmap);
	}
	
}
