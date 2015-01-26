package com.androiders.salute;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SaluteContentProvider extends ContentProvider {

	
	public static final String PROVIDER_NAME = "com.androiders.salute.SaluteContentProvider";
	  
	/** A uri to do operations salute table. A content provider is identified by its uri */
	public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/salutes" );
	 
	/** Constants to identify the requested operation */
	private static final int SALUTES = 1;
	private static final int SALUTES_ID = 2;
	 
	private static final UriMatcher uriMatcher ;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "salutes", SALUTES);
		uriMatcher.addURI(PROVIDER_NAME, "salutes/#", SALUTES_ID);
	}
	 
	    /** This content provider does the database operations by this object */
	    SaluteDB mSaluteDB;
	
	@Override
	public boolean onCreate() {
		mSaluteDB = new SaluteDB(getContext());
		return true;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		 if(uriMatcher.match(uri)==SALUTES){
	            Cursor c =  mSaluteDB.getAll();
	            c.setNotificationUri(getContext().getContentResolver(), CONTENT_URI);
	            return c;
	        }else{
	            return null;
	        }
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(uriMatcher.match(uri)!=SALUTES){	
			return null;
		}
		long id = mSaluteDB.insert(values);
		Uri ret = ContentUris.withAppendedId(uri, id);
		return ret;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if(uriMatcher.match(uri)==SALUTES_ID){	
			int id = (int) ContentUris.parseId(uri);
			Log.i("cp","deleting id " + id);
			return mSaluteDB.delete(id);
		}
		else
			return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
