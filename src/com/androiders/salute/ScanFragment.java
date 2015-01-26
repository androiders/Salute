package com.androiders.salute;

import java.io.IOException;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

public class ScanFragment extends Fragment implements TextureView.SurfaceTextureListener /*Camera.PreviewCallback*/ {

	TextureView mTextureView;
//    SurfaceHolder mHolder;
	private Context mContext;
	private Camera mCamera;
	private HistogramList mHistogramList;
//	private boolean mSurfaceCreated;
	private RenderingThread mThread;

	
	public ScanFragment(Context context) {
		super();
		
		mContext = context;
		
//		mSurfaceView = new SurfaceView(context);
		//addView(mSurfaceView);

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View root = inflater.inflate(R.layout.scan_fragment, container,false);

		mTextureView = (TextureView)root.findViewById(R.id.textureView1);
		
//		mHolder = mSurfaceView.getHolder();
//		mHolder.addCallback(this);
//		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		mTextureView.setSurfaceTextureListener(this);
		
		return root; 
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		safeCameraOpen(0);
	}

//	@Override
//	public void surfaceChanged(SurfaceHolder holder, int format, int width,
//			int height) {
//		Camera.Parameters parameters = mCamera.getParameters();
//		parameters.setPreviewSize(width, height);
////		requestLayout();
//		mCamera.setParameters(parameters);
//
//		// Important: Call startPreview() to start updating the preview surface.
//		// Preview must be started before you can take a picture.
//		mCamera.setPreviewCallback(this);
//		mCamera.startPreview();		
//	}
//
//	@Override
//	public void surfaceCreated(SurfaceHolder holder) {
//		// TODO Auto-generated method stub
//		mSurfaceCreated = true;
//		
//	}
//
//	@Override
//	public void surfaceDestroyed(SurfaceHolder holder) {
//		   // Surface will be destroyed when we return, so stop the preview.
//	    if (mCamera != null) {
//	        // Call stopPreview() to stop updating the preview surface.
//	        mCamera.stopPreview();
//	    }		
//	}
	
	private boolean safeCameraOpen(int id) {
	    boolean qOpened = false;
	  
	    try {
	        releaseCameraAndPreview();
	        mCamera = Camera.open(id);
	        qOpened = (mCamera != null);
	    } catch (Exception e) {
	        Log.e(getString(R.string.app_name), "failed to open Camera");
	        e.printStackTrace();
	    }

	    return qOpened;    
	}

	private void releaseCameraAndPreview() {
	    setCamera(null);
	    if (mCamera != null) {
	        mCamera.release();
	        mCamera = null;
	    }
	}
	
	private void setCamera(Camera camera) {
	    if (mCamera == camera) { return; }
	    
	    stopPreviewAndFreeCamera();
	    
	    mCamera = camera;
	    
	    if (mCamera != null) {
	        List<Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
//	        mSupportedPreviewSizes = localSizes;
//	        requestLayout();
	      
//	        try {
//	            mCamera.setPreviewDisplay(mHolder);
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
	      
	        // Important: Call startPreview() to start updating the preview
	        // surface. Preview must be started before you can take a picture.
//	        mCamera.startPreview();
	    }
	}

	/**
	 * When this function returns, mCamera will be null.
	 */
	private void stopPreviewAndFreeCamera() {

	    if (mCamera != null) {
	        // Call stopPreview() to stop updating the preview surface.
	        mCamera.stopPreview();
	    
	        // Important: Call release() to release the camera for use by other
	        // applications. Applications should release the camera immediately
	        // during onPause() and re-open() it during onResume()).
	        mCamera.release();
	    
	        mCamera = null;
	    }
	}

//	@Override
//	public void onPreviewFrame(byte[] data, Camera camera) {
//		// TODO Auto-generated method stub
//		if(!mSurfaceCreated)
//			return;
//		int len = data.length;
//		
////		Bitmap bitmapPicture = BitmapFactory.decodeByteArray(data, 0,
////				data.length);
////		if(bitmapPicture == null)
////			return;
//		Canvas c = mHolder.lockCanvas();
//		c.drawColor(len);
//		c.drawColor(Color.WHITE);
////		c.drawBitmap(bitmapPicture, new Matrix(),null);
////		c.drawCircle(50, 50, 50, null);
//		mHolder.unlockCanvasAndPost(c);
//		return;
//		
//	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture arg0, int width,
			int height) {
		
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPreviewSize(640, 480);
//		requestLayout();
		mCamera.setParameters(parameters);
		
		 try {
             mCamera.setPreviewTexture(arg0);
             mCamera.startPreview();
         } catch (IOException ioe) {
             // Something bad happened
         }		
		 
		 mThread = new RenderingThread(mTextureView);
		 mThread.start();
		 
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
		 mCamera.stopPreview();
         mCamera.release();
         if (mThread != null) mThread.stopRendering();
         return true;
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1,
			int arg2) {
		// TODO Auto-generated method stub
		Log.i("hej", "Surface changed");
		return;
		
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
		// TODO Auto-generated method stub
		Log.i("hej", "Surface changed");
		
		//arg0.updateTexImage();
		mTextureView.postInvalidate();
		
//		Bitmap bmp = mTextureView.getBitmap();
//		Histogram histogram = new Histogram(bmp,10);
//		String mName = mHistogramList.getNameOfBestMatch(histogram);
//		if(mName != null){
		
//		}
		
		
		
	}

	public void setHistogramList(HistogramList hl) {
		mHistogramList = hl;
		
	}
	
	private static class RenderingThread extends Thread {
        private final TextureView mSurface;
        private volatile boolean mRunning = true;
 
        public RenderingThread(TextureView surface) {
            mSurface = surface;
        }
 
        @Override
        public void run() {
          
            Paint paint = new Paint();
            paint.setColor(Color.RED);
 
            while (mRunning && !Thread.interrupted()) {
                
            	final Canvas canvas = mSurface.lockCanvas(null);
                try {
                	
        			paint.setColor(Color.RED);
        			canvas.drawText("Hej hej hej", 100, 100, paint);
                } finally {
                    mSurface.unlockCanvasAndPost(canvas);
                }
 
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    // Interrupted
                }
            }
        }
       
        void stopRendering() {
            interrupt();
            mRunning = false;
        }
    }
	
	
}
