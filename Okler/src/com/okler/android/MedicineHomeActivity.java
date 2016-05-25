package com.okler.android;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.utils.GPSTracker;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MedicineHomeActivity extends BaseActivity {

	View bottomBarLayout;
	Toolbar toolBar;
	TextView locationTv,userLocation;
	Button notifCount;
	ImageView allopathyIv,ayurvedicIv,homeopathyIv;
	UIUtils uiutils;
	ImageView imgBack;
	Activity ack;
	double longi,latti;
	Location loc,newLoc;
	
	LocationManager locationManager;
	
	GPSTracker gps;
	boolean isLocation=false;
	RelativeLayout back_layout;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medicine_home);
		toolBar = (Toolbar)findViewById(R.id.toolbar);
		ack = this;
		locationManager = (LocationManager) ack
                .getSystemService(LOCATION_SERVICE);
		setSupportActionBar(toolBar);
		ActionBar ab = getSupportActionBar();
		//ab.setDisplayHomeAsUpEnabled(true);
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_medicine);
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
		notifCount = (Button) toolBar.findViewById(R.id.notif_count);
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	      Utilities.setTitleText(toolBar, "Medicines");
	      userLocation = (TextView)findViewById(R.id.userLocation);
		locationTv = (TextView)findViewById(R.id.locationText);
		allopathyIv = (ImageView)findViewById(R.id.allopathyIv);
		ayurvedicIv = (ImageView)findViewById(R.id.ayurvedicIv);
		homeopathyIv = (ImageView)findViewById(R.id.homeopathyIv);
		
		
		
		/*gps = new GPSTracker(ack);
		if(gps.canGetLocation()){
			latti = gps.getLatitude();
			longi = gps.getLongitude();
			if(gps.getLocation()==null){
				if(!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)))
				{
					gps.showSettingsAlert();
				}
			}
			else
				setAddress();
			
		}else{
			gps.showSettingsAlert();
		}*/
		//setUserLocation();
		allopathyIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(getApplicationContext(),AllopathyActivity.class);
				uiutils = new UIUtils();
				Okler.getInstance().setBookingType(uiutils.getBookingType("Allopathy"));
				startActivity(intent);
			}
		});
		
		ayurvedicIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(getApplicationContext(),AllopathyActivity.class);
				uiutils = new UIUtils();
				Okler.getInstance().setBookingType(uiutils.getBookingType("Ayurvedic"));
				startActivity(intent);
				}
		});
		
		homeopathyIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(getApplicationContext(),AllopathyActivity.class);
				uiutils = new UIUtils();
				Okler.getInstance().setBookingType(uiutils.getBookingType("Homeopathy"));
				startActivity(intent);
			}
		});
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		
		userLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isLocation =true;
				Intent intent = new Intent(MedicineHomeActivity.this,LocationActivity.class);
				startActivity(intent);
				
			}
		});
		
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.	
		    
		return true;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(notifCount, ack);
		boolean isFromLocation = getIntent().getBooleanExtra("isFromLocation", false);
		if(isFromLocation||isLocation)
		{
			isLocation = false;
			UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
			String city,country;
	    	city = ubean.getUserCity();
	    	country = ubean.getUserCountry();
	    	if(city.equals("")||country.equals(""))
	    		userLocation.setText("Select Your Location");
	    	else
	    		userLocation.setText(city+","+country);	    	
		}
		else{
				gps = new GPSTracker(ack);
				if(gps.canGetLocation()){
					latti = gps.getLatitude();
					longi = gps.getLongitude();
					setAddress();
			//	Toast.makeText(ack, "Latti "+latti+"\n"+"longi "+longi, 2000).show();
				}else{
			gps.showSettingsAlert();
				}
		}
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
		return super.onOptionsItemSelected(item);
	}
	
	/*public void setUserLocation(){

	  pLocTracker = new ProviderLocationTracker(getApplicationContext(), ProviderType.GPS);
      pLocTracker.start();
      loc = pLocTracker.getLocation();
     
      if(loc == null)
      {
		pLocTracker = new ProviderLocationTracker(getApplicationContext(), ProviderType.NETWORK);
		pLocTracker.start();
		loc = pLocTracker.getLocation();
			    
	if(loc==null){
		if(pLocTracker.hasPossiblyStaleLocation())
	
			{
				loc = pLocTracker.getPossiblyStaleLocation();
				longi = loc.getLongitude();
			    latti = loc.getLatitude();
			    Log.d("Longitutde", ""+loc.getLongitude());
			    Log.d("Latitude",""+ loc.getLatitude());
			    setAddress();
			}
		else{
			//updateUserLocation();
		}
			}else{
				 longi = loc.getLongitude();
				 latti = loc.getLatitude();
				 setAddress();
			}
			
			
      }else{
    	  longi = loc.getLongitude();
		  latti = loc.getLatitude();
		  setAddress();
      }
      
	}*/
	
	
	/*public void updateUserLocation(){
		locUpList = new LocationUpdateListener() {
			
			@Override
			public void onUpdate(Location oldLoc, long oldTime, Location newLoc, long newTime) {
				
				 latti = newLoc.getLatitude();
				 longi = newLoc.getLongitude();
				 setAddress();
			}
		};
		locUpList.onUpdate(loc, 100, newLoc, 1000);
	}*/
	public void setAddress()
    {
		UIUtils.setUserLocationToSharedPrefs(longi, latti,ack);
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
		String city,country;
    	city = ubean.getUserCity();
    	country = ubean.getUserCountry();
    	if(city.equals("")||country.equals(""))
    		userLocation.setText("Select Your Location");
    	else
    		userLocation.setText(city+","+country);
    	/*Geocoder geocoder;
    	List<Address> addresses = new ArrayList<Address>();
    	geocoder = new Geocoder(this, Locale.getDefault());

    	try {
			addresses = geocoder.getFromLocation(latti, longi, 1);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		} 
    	
    	String city,country;
    	if(addresses.size()>0){
    	city = addresses.get(0).getLocality();
    	country = addresses.get(0).getCountryName();
    	userLocation.setText(city+","+country);
    	}
    	else{
    		userLocation.setText("Select Your Location");
    	}*/
    }
}
