package com.arlab;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;

import com.arlab.arbrowser.general.ARbrowserView;
import com.arlab.arbrowser.general.POI;
import com.arlab.arbrowser.general.POIaction;
import com.arlab.arbrowser.general.POIlabel;

import com.arlab.sample.R;

public class HelloARlibActivity extends Activity {
    		
	/** ARbrowser view instance variable. */
	private ARbrowserView aRbrowserView;
	
	/** Custom handler to get POI clicked and POI selected CALLBACKS. */
	private Handler userCustomPopupsHadler;
	
	/**Debug Tag */
	private static final String TAG = "ARLAB_Hello";
			
    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Initiation of AR Browser */
                
        /** Create an instance of ARbrowserView object (Valid API_KEY required).
         * Depending on the device type (tablet or smartphone) the default orientation and UI sizes are set */
  
    	if(isTablet())
        	aRbrowserView = new ARbrowserView(this,ARbrowserView.UI_ORIENTATION_ALL,"4lCXAXGdAzFQPTVvZH0Rpo7X3GvG2K62PUilpTaDeNA=", ARbrowserView.SCREEN_ORIENTATION_LANDSCAPE,true,true);
        else
        	aRbrowserView = new ARbrowserView(this,ARbrowserView.UI_ORIENTATION_ALL,"4lCXAXGdAzFQPTVvZH0Rpo7X3GvG2K62PUilpTaDeNA=", ARbrowserView.SCREEN_ORIENTATION_PORTRAIT,false,true);
        
        /** Add view instance to the application content view. */
        setContentView(aRbrowserView.getARviewInstance(), new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        /** Create GeoPoint with user current Location (Latitude and Longitude can be obtained from GPS or Network provider) */ 
        Location loc = new Location(LocationManager.GPS_PROVIDER);
        loc.setLatitude(32.787996);
        loc.setLongitude(34.959526);      
        
        /** Set user current location (Can be updated at run time) */
        aRbrowserView.setMyCurrentLocation(loc);
        
        /** Customize POI view ( If not set the Default value is used) */
        aRbrowserView.setPoiSize(POI.POI_SIZE_LARGE);
        
        /** Customize Radar appearance ( If not set ,the Default value is used) */
        aRbrowserView.setCustomRadarProperties("#f58025", 170, "#f58025", 190, 50,-1,-1);       
        aRbrowserView.setCustomRadarPoiProperties(null, null, 3);
        
        /** Set POPUP mode **/
        aRbrowserView.setPopupViewMode(ARbrowserView.POPUP_MODE_ALL);
        
        /** Set Altitude mode ( If not set ,the Default value (ARbrowserView.STANDARD_ALTITUDE_MODE) is used**/  
        aRbrowserView.setAltitudeMode(ARbrowserView.DERGEES_ALTITUDE_MODE);
        //aRbrowserView.setAltitudeMode(ARbrowserView.STANDARD_ALTITUDE_MODE);
        
        /** Custom picking and selection callback Handler, allow user to implement his own POPUPS. **/
        userCustomPopupsHadler = new Handler(){
			@Override
			public void dispatchMessage(Message msg) {
				handleMessage(msg);	
 
				int selected = msg.arg1;
				int picked = msg.arg2;
				
				POI selPOI = new POI();
				
				if(selected != -1)
				{
					selPOI = aRbrowserView.getPoiById(selected);
					Log.i(TAG,"Label with poi id ["+selected+"] was selected with bearing = " + selPOI.getBearing() + " and distance = " + selPOI.getDistanceFromUser());
				}
				if(picked != -1)
				{
					selPOI = aRbrowserView.getPoiById(picked);
					Log.i(TAG,"Label with poi id ["+picked+"] was clicked with bearing = " + selPOI.getBearing() + " and distance = " + selPOI.getDistanceFromUser());
				}
				
			}
		};
        		
		/** Set custom handler to AR browser instance and select desired CALLBACKS**/
		aRbrowserView.setCustomSelectionHandlerCallback(userCustomPopupsHadler, true, true);
        
       /** Creation and insertion of the POIs to render list */
           
        POI mPoi;
    	
        /*********************/
        /** Create first POI */
        /*********************/
        
     	mPoi = new POI();  	
     	//mPoi.setIconRes(R.drawable.smiley);
     	mPoi.setIconFromUrl("http://thecustomizewindows.com/wp-content/uploads/2011/11/Best-Android-Apps-List-of-50-Free-Android-Apps.png"); 
     	mPoi.setLatitude(32.801308);
     	mPoi.setLongitude(34.951158);  
     	
     	mPoi.setNotSelectedPoiAlpha(0.3f);
     	mPoi.setSelectedPoiAlpha(1.0f);
     	
     	/** Set POI Altitude 
     	 * ( The POI can be set above or below the horizon, 
     	 *   values between -45 and 45 grades  )
     	 */
     	mPoi.setDegreesAltitude(45);
     	//mPoi.setStandartAltitude(200); // Real altitude in meters
     	
     	/** Create first POI label property*/
     	POIlabel label = new POIlabel();
        label.setLabelTitle("Smiley");
     	label.setLabelDescription("First POI description\nSecond line");
     	label.setLabelDescriptionFontColor("#b0b0b3");
     	label.setLabelTitleFontColor("#f58025");
     	label.setLabelTitleFontSize(11);
     	label.setLabelLogoResource(R.drawable.watermark2);
     	
     	/** Add first POI label property*/
     	mPoi.setPoiLabelProperty(label);
     	
     	
     	/** Create POI Actions*/
     	
     	// AUDIO //
     	POIaction action = new POIaction();
     	action.setAudioAction("http://www.samisite.com/sound/cropShadesofGrayMonkees.mp3");
     	/** Add action to POI actions list*/
     	mPoi.addPoiActionToList(action);
     	
     	// VIDEO //
     	action = new POIaction();
     	action.setVideoAction("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
     	/** Add action to POI actions list*/
     	mPoi.addPoiActionToList(action);
     	
     	// WEB //
     	action = new POIaction();
     	action.setWebAction("http://www.arlab.com/");
     	/** Add action to POI actions list*/
     	mPoi.addPoiActionToList(action);
     	     	
     	     	
     	/** Add second POI to render list
     	 * This POI is added with no custom ID, so the system will assign one.  
     	 */
     	int index = aRbrowserView.addPoiToRenderList(mPoi);
     	if(index != 1)
     		Log.d(TAG, "POI with index " + index + " added");
        
     	
     	 /**********************/
     	 /** Create second POI */
     	 /**********************/
     	
    	mPoi = new POI();  	
     	mPoi.setIconRes(R.drawable.target);
     	mPoi.setLatitude(32.7955);
     	mPoi.setLongitude(34.968882);
     	
     	
     	/** Set POI Altitude 
     	 * ( The POI can be set above or below the horizon, 
     	 *   values between -45 and 45 grades  )
     	 */
     	mPoi.setDegreesAltitude(-30);
     	//mPoi.setStandartAltitude(aRbrowserView.convertFeetToMeters(1000)); // Real altitude using helper function to convert from feet to meters
     	
     	/** Create second POI label property*/
     	label = new POIlabel();
        label.setLabelTitle("Target");
     	label.setLabelDescription("Second POI description");
     	label.setLabelDescriptionFontColor("#b0b0b3");
     	label.setLabelTitleFontColor("#f58025");
     	label.setLabelTitleFontSize(11);
     	label.setLabelLogoResource(R.drawable.watermark2);
     	
     	/** Add second POI label property*/
     	mPoi.setPoiLabelProperty(label);
     	
       /** Create POI Actions*/
     	
     	// MAP DIRECTIONS //
     	action = new POIaction();
     	action.setMapDirectionAction(true);
     	/** Add action to POI actions list*/
     	mPoi.addPoiActionToList(action);
     	
     	// IMAGE //
     	action = new POIaction();
     	action.setPictureAction("http://www.arlab.com/wp-content/uploads/2012/02/arlink.png");
     	/** Add action to POI actions list*/
     	mPoi.addPoiActionToList(action);
     	
     	// EMAIL //
     	action = new POIaction();
     	action.setEmailAction("info@arlab.com", "Test", "This message is for testing purpeses");
     	/** Add action to POI actions list*/
     	mPoi.addPoiActionToList(action);
     	     	
     	/** Add second POI to render list.
     	 * POI added with custom ID. 	
     	 */
     	index = 111;
     	if(aRbrowserView.addPoiToRenderList(mPoi, index) == true)
     		Log.d(TAG, "POI with index " + index + " added");
        
     	/*********************/
     	/** Create third POI */	
     	/*********************/
     	mPoi = new POI();  	
     	mPoi.setIconRes(R.drawable.v);
     	mPoi.setLatitude(32.781556);
     	mPoi.setLongitude(34.965985);
     	mPoi.setPoiLabelProperty(label);
     	
     	/** Create third POI label property*/
     	label = new POIlabel();
        label.setLabelTitle("V");
     	label.setLabelDescription("Third POI description");
     	label.setLabelDescriptionFontColor("#b0b0b3");
     	label.setLabelTitleFontColor("#f58025");
     	label.setLabelTitleFontSize(11);
     	label.setLabelLogoResource(R.drawable.watermark2);
     	
     	/** Add third POI label property*/
     	mPoi.setPoiLabelProperty(label);
     
     	/** Create POI Actions*/
     	
     	// PHONE //
     	action = new POIaction();
     	action.setPhoneAction("9999999");
     	/** Add action to POI actions list*/
     	mPoi.addPoiActionToList(action);
     	
     	// SMS //
     	action = new POIaction();
     	action.setSmsAction("999999999");
     	/** Add action to POI actions list*/
     	mPoi.addPoiActionToList(action);
     	
     	// FACEBOOK //  
     	action = new POIaction();
     	action.setFaceBookAction("Facebook test message");  /** In order of this action to work ,FaceBook APP ID initiation is a must! */	
     	/** Add action to POI actions list*/
     	mPoi.addPoiActionToList(action);
     	
     	/** Add third POI to render list.
     	 * POI added with custom ID. 
     	 */
     	index = 222;
     	if(aRbrowserView.addPoiToRenderList(mPoi, index) == true)
     		Log.d(TAG, "POI with index " + index + " added"); 
    }
    
	/**
	 * Find device base orientation by device display size (Phones: <b>Portrait</b> , Tablets: <b>Landscape</b>)
	 * 
	 * 
	 */	
   	private boolean isTablet()
   	{    		
   	   DisplayMetrics dm = new DisplayMetrics();
	   this.getWindowManager().getDefaultDisplay().getMetrics(dm);
	   int wPix = dm.widthPixels;
	   int hPix = dm.heightPixels;
	   
	   Log.d("ARLAB", "w: " + wPix + " h :" + hPix);
	   
	   if( ((float)hPix/(float)wPix) > 1)
	   {
		   return false;  // Not tablet
	   }
	   else
	   {
		   return true;  // tablet
	   }
   	}
   	
    @Override
    protected void onResume() {
        super.onResume();
        
        /** Resumes and starts AR view*/
        aRbrowserView.ResumeArView(); 
    }


    @Override
    protected void onPause() {
        super.onPause();
              
        /** Stops AR view*/
        aRbrowserView.PauseArView(); 
    }
    
}