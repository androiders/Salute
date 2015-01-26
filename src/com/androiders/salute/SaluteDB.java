package com.androiders.salute;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SaluteDB extends SQLiteOpenHelper {
	
	/** Database name */
    private static String DBNAME = "sqlsalutedb";
 
    /** Version number of the database */
    private static int VERSION = 1;
 
    /** Field 1 of the table cust_master, which is the primary key */
    public static final String KEY_ROW_ID = "_id";
 
    /** Field 2 of the table stores the name */
    public static final String KEY_NAME = "name";
 
    /** Field 3 of the table stores the rating */
    public static final String KEY_RATING = "rating";
 
    /** Field 4 of the table stores the path to an image */
    public static final String KEY_IMAGE_PATH = "image_path";
 
    /** Field 4 of the table stores the path to an image */
    public static final String KEY_DESCR = "description";
    
    
    /** A constant, stores the the table name */
    private static final String DATABASE_TABLE = "salute_table";
 
    /** An instance variable for SQLiteDatabase */
    private SQLiteDatabase mDB;
 
	
	
	public SaluteDB(Context context) {
		super(context, DBNAME, null, VERSION);
        this.mDB = getWritableDatabase();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + DATABASE_TABLE 
				+ " ( " + KEY_ROW_ID + " integer primary key autoincrement , " 
				+ KEY_NAME       + " text , " 
				+ KEY_RATING     + " REAL , " 
				+ KEY_IMAGE_PATH + " text , "
				+ KEY_DESCR      + " text ) ";

		db.execSQL(sql);

		sql = "insert into " + DATABASE_TABLE + " ( " + KEY_NAME + ","
				+ KEY_RATING + "," + KEY_IMAGE_PATH + "," + KEY_DESCR + " ) "
				+ " values ( 'Generic red wine', 3.5,'', 'good, good wine' )";
		db.execSQL(sql);

	}

	 /** Returns all the customers in the table */
    public Cursor getAll(){
        return mDB.query(DATABASE_TABLE, new String[] { KEY_ROW_ID,  KEY_NAME , KEY_RATING, KEY_IMAGE_PATH, KEY_DESCR } ,
                            null, null, null, null,
                            KEY_RATING + " desc ");
    }
 
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public long insert(ContentValues values){
		return mDB.insert(DATABASE_TABLE, null, values);
	}
	
	public int delete(int row){
		Log.i("db","deleting id " + row);
		int res = mDB.delete(DATABASE_TABLE, KEY_ROW_ID + " = " + row,null);
		Log.i("cp","deleted rows " + res);
		return res;
	}
	
}
