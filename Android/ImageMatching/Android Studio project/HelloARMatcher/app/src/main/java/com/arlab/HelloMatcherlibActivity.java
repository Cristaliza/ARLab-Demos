package com.arlab;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlab.callbacks.ARmatcherImageCallBack;
import com.arlab.callbacks.ARmatcherQRCallBack;
import com.arlab.imagerecognition.ARmatcher;
import com.arlab.imagerecognition.ROI;

@SuppressLint("UseSparseArrays")
public class HelloMatcherlibActivity extends Activity implements ARmatcherImageCallBack, ARmatcherQRCallBack {
    private static final String API_KEY_MATCHER = "R+fA2ng9RWU3xWpdJfC0VA==";
	/** Called when the activity is first created. */
	
	/**The matcher instance */
	private ARmatcher aRmatcher;
	
	/**HashMap that holds added images IDs and its titles in the matching pool */
	private HashMap<Integer, String> imageTitles=new HashMap<Integer,String>();
	/**Debug Tag */
	private static final String TAG = "ARLAB_Hello";
	
	TextView textView = null;
	private int screenheight;
	private int screenwidth;
	private FrameLayout frame;
    private RelativeLayout grid;
    private RelativeLayout crop; 
               
    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        /**Get full screen size */
        DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		screenheight = getWindowManager().getDefaultDisplay().getHeight();
		screenwidth = getWindowManager().getDefaultDisplay().getWidth();
		
		/** Set activity view*/
		setContentView(R.layout.main);
        frame=(FrameLayout)findViewById(R.id.frame);
        grid=(RelativeLayout)findViewById(R.id.grid);
        crop=(RelativeLayout)findViewById(R.id.crop);
        
        /**Create an instance of the ARmatcher object. */
        aRmatcher = new ARmatcher(this,API_KEY_MATCHER,ARmatcher.SCREEN_ORIENTATION_PORTRAIT,screenwidth,screenheight,true);
       
        aRmatcher.getTrueCameraViewWidth();
        aRmatcher.getTrueCameraViewHeight();
        
        /**Set image and QR matching callbacks */
        aRmatcher.setImageRecognitionCallback(this); 
        aRmatcher.setQRRecognitionCallback(this);
   
    	/**Add camera view instance to content view */
    	frame.addView(aRmatcher.getCameraViewInstance());
                
    	/**Set the matching type.*/
    	aRmatcher.setMatchingType(ARmatcher.BOTH_IMAGE_AND_QR_MATCHER);
       
        /**Enable median filter ,witch help to reduce noise and mismatches in IMAGE matching .(Optional) */
         aRmatcher.enableMedianFilter(true);
        
        /**Set minimum image quality threshold, for image to be accepted in the image pool (0 - 10) */      
        aRmatcher.setImageQuality(5); 
             
        /**Add TextView to the view in order to show matching results. */
        addResultTextView();
        
        /**Set cropping. */
        //setCroppingArea();
        //setROIs();
        
        beginAddImages();		 
    }
    
    private void beginAddImages() {
    	AddImagesTask ait=new AddImagesTask(this);
    	ait.execute((Void)null);
	}
    
	protected void onResume() {
    	super.onResume();
    }

    protected void onStop() {			
    	super.onStop();
    	try {
    		/**Empty image matching pool*/    		
    		aRmatcher.releaseResources();
    		
    	} catch (Exception e) {	 }
    	System.gc();
    }

    protected void onPause() {
    	super.onPause();
    	  /**Stop matching*/    	
    	   aRmatcher.stop();  	
    }
    
    protected void setCroppingArea() {
    	/**Select cropping area to be analyzed */
        /**
         * Take in consideration that the cropping area is defined on landscape and portrait modes differently,
         * In Portrait Mode the (0,0) point will be the upper left corner holding the device portrait ,
         *  In Landscape Mode the (0,0) point will be the upper left corner holding the device landscape left (upper right in portrait)
         * 
         */
    	  	
    	/**Set cropping area in TABLET or DEVICES where Camera Preview is NOT set to full screen */
        /**
         * Here we use camerawidth and cameraweight because of the camera preview that is not
         * in a full screen of the device, so we need to know
         * the exact size to not exceed the camera view borders.
         * 
         * the screenwidth and screenheight used for drawing the red rectangel marker
         * 
         */
    	/*aRmatcher.setImageCropRect(camerawidth/2, 0, camerawidth, cameraheight/2); 	
    	
    	RelativeLayout.LayoutParams p=new RelativeLayout.LayoutParams(screenwidth/2, screenheight/2);
        p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        p.addRule(RelativeLayout.ALIGN_PARENT_TOP);*/
    	
        
        
        /**Set cropping area in PHONES or Devices with camera preview set to full screen */
        /**
         * Here we use screenwidth and screenwidth for both cropping and drawing the marker rectangle
         * becasue of the full video preview
         * 
         * 
         */
    	aRmatcher.setImageCropRect(screenwidth/2, 0, screenwidth, screenheight/2);   
    	 
        RelativeLayout.LayoutParams p=new RelativeLayout.LayoutParams(screenwidth/2, screenheight/2);
        p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        
        
        
        crop.setLayoutParams(p);
        crop.setVisibility(View.VISIBLE);
	}

    protected void setROIs() {
    	/**Add 4 ROIs */
        /**
         * We define 4 regions of interest on the mobile screen to match simultaneous QR codes.
         */
    	/**
         * Take in consideration that the cropping area is defined on landscape and portrait modes differently,
         * In Portrait Mode the (0,0) point will be the upper left corner holding the device portrait ,
         *  In Landscape Mode the (0,0) point will be the upper left corner holding the device landscape left (upper right in portrait)
         * 
         */
		//ROI1
        ROI r = new ROI(0, 0, screenwidth/2, screenheight/2);
        aRmatcher.addRoi(r);
        //ROI2
        r = new ROI(0,screenheight/2, screenwidth/2, screenheight/2);
        aRmatcher.addRoi(r);
        //ROI3
        r = new ROI(screenwidth/2,0,screenwidth/2, screenheight/2);
        aRmatcher.addRoi(r);
        //ROI4
        r = new ROI(screenwidth/2, screenheight/2,screenwidth/2, screenheight/2);
        aRmatcher.addRoi(r);
        grid.setVisibility(View.VISIBLE);
	}

	private int addLargeImageWithId(int resource, String title, int id, int maxSize) {
    	
    	/**Add LARGE (High resolution) image with unique ID*/

		 /**
		  * The API provides a helper function to reduce the image size, to avoid memory allocation errors.
		  */
		 Bitmap b = aRmatcher.decodeScaledImageFromResource(resource, maxSize);
		 
		 if(aRmatcher.addImage(b,id)){
			 imageTitles.put(id,title);	
			 b.recycle();
		  	
		  	Log.i(TAG,"image added to the pool with id: " + id);
	     }else{
		  	Log.i(TAG,"image not added to the pool"); 
		 }
		 
		 return id;
	}

	private int addImageFromURL(String url, String title) {
    	/**Add image from URL */
        /**It is possible to load remote images to the image pool by setting a valid URL */ 

        int imagePool_Id = aRmatcher.addImage(url);        
        if(imagePool_Id != -1){
        	imageTitles.put(imagePool_Id,title);	 
        	Log.i(TAG,"image added to the pool with id: " + imagePool_Id);
        }else{
        	Log.i(TAG,"image not added to the pool"); 
        } 
        
        return imagePool_Id;
	}

	private int addImageFromResources(int imageResourceId, String title) {
		/**Add images from local resources to image matching pool
         * Adding image returns image id assigned to the image in the matching pool.
         * We will save it to know which image was matched
         * 
         * (The best practice is to make image adding in another thread to avoid the system to get stuck)
         * 
         * IMORTANT !!! Decoding large images to Bitmap may result in insufficient memory allocations and application crashing, 
         * therefore large images must be reduced/scaled.
         */          
        
    	Bitmap bmp = BitmapFactory.decodeResource(getResources(), imageResourceId);
    	int imagePool_Id;
   
    	imagePool_Id = aRmatcher.addImage(bmp);
    	
    	if(imagePool_Id != -1){
    		imageTitles.put(imagePool_Id,title);

    		Log.i(TAG,"image added to the pool with id: " + imagePool_Id);
    	}else{
    		Log.i(TAG,"image not added to the pool"); 
    	}
    	
    	bmp.recycle();
    	
    	return imagePool_Id;
    }
	
	private int addImageDataFromPath(String path,String title)
	{		
		int imagePool_Id ;
				
		imagePool_Id = aRmatcher.addImageFromData(path);   	
		
		if(imagePool_Id != -1){
    		imageTitles.put(imagePool_Id,title);

    		Log.i(TAG,"image added to the pool with id: " + imagePool_Id);
    	}else{
    		Log.i(TAG,"image not added to the pool"); 
    	}
		
		return imagePool_Id;
	}
	
	@SuppressWarnings("unused")
	private int addImageDataFromUrl(String url,String title)
	{		
		int imagePool_Id ;
				
		imagePool_Id = aRmatcher.addImageFromDataThroughUrl(url);
		
		if(imagePool_Id != -1){
    		imageTitles.put(imagePool_Id,title);
    		Log.i(TAG,"image added to the pool with id: " + imagePool_Id);
    	}else{
    		Log.i(TAG,"image not added to the pool"); 
    	}
		return imagePool_Id;
	}

	/**Add TextView to the screen to show the matching results. */
    private void addResultTextView()
    {
    	textView = new TextView(this);
    	textView.setTextColor(Color.WHITE);
    	textView.setTextSize(23);
		FrameLayout.LayoutParams frame = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT,Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
	
		addContentView(textView,frame);
    }

    /**Callback that will accept all IMAGE matching results */
    @Override
	public void onImageRecognitionResult(int result) {
    	
    	if (result != -1) {
    		textView.setText(imageTitles.get(result));    	
    	} 
    	else
    	{				
    		textView.setText("Nothing found");    	
		}	
	}
   
    /**Callback that will accept all QR codes matching results */
    @Override
	public void onSingleQRrecognitionResult(String result) {
    	if(result != "")
    	{
			textView.setText(result);
			Toast.makeText(getApplication(), result, Toast.LENGTH_SHORT).show();
    	}
    	else
    	{				
    		textView.setText("Nothing found");    	
		}
	}
    
    /**Callback that will accept all QR codes matching results */
	@Override
	public void onMultipleQRrecognitionResult(ArrayList<ROI> roiList) {
		String output = "";
    	int i = 0;
    	for(i=0; i<roiList.size(); i++){
    		if(roiList.get(i).foundResult != null)
    			output += roiList.get(i).foundResult + "\n";
    	}
    	textView.setText(output);
    	if(output == "")
    	{				
    		textView.setText("Nothing found");    	
		}
	}

    /**
     * Through the AsyncTask the images are added to the library.
     * @author ARLab
     *
     */
    private class AddImagesTask extends AsyncTask<Void, Void, Boolean>{
    	private Context context;
    	private ProgressDialog progressDialog;
    	public AddImagesTask(Context context) {
    		this.context=context;
		}
    	/**
    	 * Create and show the progress view
    	 */
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		progressDialog=ProgressDialog.show(context, "Loading", "Adding Images to Library");
    	}
    	
    	/**
    	 * Add the images to the lib.
    	 * Wait until the activity has been resumed.
    	 */
		@Override
		protected Boolean doInBackground(Void... params) {
		    //Adding Images
			
			/** Add image from local resources **/
		    addImageFromResources(R.drawable.horse, "Horse"); 
	        addImageFromResources(R.drawable.dolphins, "Dolphins");        
	        addImageFromResources(R.drawable.women, "Women");	 	        
	        addImageFromResources(R.drawable.extremely_poster, "Extremely");
	        
	        /** Add images from URL **/
	        addImageFromURL("http://www.breakingdawnmovie.org/images/2010/06/breaking-dawn-poster5.jpg", "Breaking Dawn");
	        addImageFromURL("http://www.emiraydin.com/blog/wp-content/uploads/2009/02/5-the-dark-knight.jpg", "Dark Knight");
	        addLargeImageWithId(R.drawable.underworld,"Underworld",333,1200);
	       	  
	        /** Add image info from type II .dat files **/
			String filename = "ARLab@pic4bmpdetector_data.dat";
			String path = Environment.getExternalStorageDirectory() + File.separator + filename;
			addImageDataFromPath(path,"GOAT type II");		
						
			/*		 
			filename = "ARLab@pic5jpegdetector_data.dat";
			path = Environment.getExternalStorageDirectory() + File.separator + filename;
			addImageDataFromPath(path,"CAT type II");	        
			*/
			
			/** Add image info from type I .dat files **/
			/*
	        String filename = "ARLab@pic4bmp.dat";
	        String path = Environment.getExternalStorageDirectory() + File.separator + filename;
			addImageDataFromPath(path,"GOAT type I");	
			*/		 
			
			filename = "ARLab@pic5jpeg.dat";
			path = Environment.getExternalStorageDirectory() + File.separator + filename;
			addImageDataFromPath(path,"CAT type I");	
					
	        return true;
		}
		
		/**
		 * Dismiss the ProgressView.
		 */
    	@Override
    	protected void onPostExecute(Boolean result) {
    		super.onPostExecute(result);
    		if(progressDialog.isShowing())
    			progressDialog.dismiss();
    		
    		/**Start matching*/   	
    	    	aRmatcher.start();		
    	}
    }

}