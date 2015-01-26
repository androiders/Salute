package com.androiders.salute;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;


public class MainActivity extends Activity implements LoaderCallbacks<Cursor>{

	public static String TAG ="SALUTE";
	
	private SaluteListAdapter mAdapter;
	private ListViewFragment mListFragment;
	
	private boolean mRestartLoader;

	private ScanFragment mScanFragment;

	private Cursor mCursor;

	private static String LIST_FRAG_TAG = "listFrag";
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(mRestartLoader)
			//restart loader since it was previously created and initialized
			getLoaderManager().restartLoader(0, null, this);
		else
			// Initialize the loader
			getLoaderManager().initLoader(0, null, this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			mListFragment = new ListViewFragment();
			getFragmentManager().beginTransaction().add(R.id.container, mListFragment).commit();
		}
		else{
			 //Restore the fragment's instance
			mListFragment = (ListViewFragment) getFragmentManager().getFragment(savedInstanceState, LIST_FRAG_TAG);
		}
		
		mRestartLoader = savedInstanceState != null;
		
		mAdapter = new SaluteListAdapter(this, R.layout.salute_list_item, null, 0);
		mListFragment.setListAdapter(mAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_new) {
			NewSaluteFragment sf = new NewSaluteFragment();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.addToBackStack(null);
			ft.replace(R.id.container, sf);
			//ft.setTransition(FragmentTransaction.TRANSIT_NONE);
            ft.commit();
//            getSupportFragmentManager().executePendingTransactions();
			return true;
		}
		if (id == R.id.action_scan) {
			mScanFragment = new ScanFragment(this);
			HistogramList hl = new HistogramList(mCursor, this);
			mScanFragment.setHistogramList(hl);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.addToBackStack(null);
			ft.replace(R.id.container, mScanFragment);
            ft.commit();
			return true;
		}

		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		
		Uri uri = SaluteContentProvider.CONTENT_URI;
		String sortOrder = SaluteDB.KEY_RATING + " desc";
		return new CursorLoader(this,uri,null,null,null,sortOrder);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		mAdapter.swapCursor(arg1);
		mCursor = arg1;
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);

	//Save the fragment's instance
	getFragmentManager().putFragment(outState, LIST_FRAG_TAG, mListFragment);


	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
//	/**
//	 * A placeholder fragment containing a simple view.
//	 */
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_main, container,
//					false);
//			return rootView;
//		}
//	}
}
