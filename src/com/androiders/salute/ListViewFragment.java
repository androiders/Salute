package com.androiders.salute;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListViewFragment extends ListFragment {

	private ViewSaluteFragment mViewSaluteFragment;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Resources res = getResources();
				final long id2 = id;
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    	builder.setTitle(R.string.confirm_delete)
		    		.setMessage(res.getString(R.string.delete_salute))
		    		.setIcon(android.R.drawable.ic_dialog_alert)
		    		.setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {
		    	    public void onClick(DialogInterface dialog, int which) {			      	
		    	    	//Yes button clicked, do something
		    	    	final long lId = id2;
		    	    	Uri uri = ContentUris.withAppendedId(SaluteContentProvider.CONTENT_URI, lId);
		    	    	getActivity().getContentResolver().delete(uri,null,null);
	    				getActivity().getContentResolver().notifyChange(uri,null);
	    				Toast.makeText(getActivity(), "Goodbye salute!",Toast.LENGTH_SHORT).show();
		    	    }
		    	})
		    	.setNegativeButton(res.getString(R.string.no), null)//Do nothing on no
		    	.show();
			
				return true;			}
		
		
		});		
	
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		    
			View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
			
			return rootView;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Cursor c = ((CursorAdapter)l.getAdapter()).getCursor();
		c.moveToPosition(position);
		
		mViewSaluteFragment = new ViewSaluteFragment(	c.getString(c.getColumnIndex(SaluteDB.KEY_NAME)),
														c.getString(c.getColumnIndex(SaluteDB.KEY_DESCR)),
														c.getString(c.getColumnIndex(SaluteDB.KEY_IMAGE_PATH)),
														c.getFloat(c.getColumnIndex(SaluteDB.KEY_RATING)));
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.addToBackStack(null);
		ft.replace(R.id.container, mViewSaluteFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
		
	}
	
	
	
}
