package com.okler.android;

import android.support.v7.widget.Toolbar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.okler.databeans.UsersDataBean;
import com.okler.utils.GPSTracker;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
		locationManager = (LocationManager) ack.getSystemService(LOCATION_SERVICE);
		setSupportActionBar(toolBar);
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_medicine);
		notifCount = (Button) toolBar.findViewById(R.id.notif_count);
		
		UIUtils.setBackClick(toolBar, ack);
	       
	      Utilities.setTitleText(toolBar, "Medicines");
	      userLocation = (TextView)findViewById(R.id.userLocation);
		locationTv = (TextView)findViewById(R.id.locationText);
		allopathyIv = (ImageView)findViewById(R.id.allopathyIv);
		ayurvedicIv = (ImageView)findViewById(R.id.ayurvedicIv);
		homeopathyIv = (ImageView)findViewById(R.id.homeopathyIv);
		
		allopathyIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(getApplicationContext(),AllopathyActivity.class);
				uiutils = new UIUtils();
				Okler.getInstance().setBookingType(UIUtils.getBookingType("Allopathy"));
				intent.putExtra("bookingType", "Allopathy");
				startActivity(intent);
			}
		});
		
		ayurvedicIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(getApplicationContext(),AllopathyActivity.class);
				uiutils = new UIUtils();
				Okler.getInstance().setBookingType(UIUtils.getBookingType("Ayurvedic"));
				intent.putExtra("bookingType", "Ayurvedic");
				startActivity(intent);
				}
		});
		
		homeopathyIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(getApplicationContext(),AllopathyActivity.class);
				uiutils = new UIUtils();
				Okler.getInstance().setBookingType(UIUtils.getBookingType("Homeopathy"));
				intent.putExtra("bookingType", "Homeopathy");
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
		return true;
	}
	
	@Override
	protected void onResume() {
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
					setGeoCoderAddress();					
				}else{
					gps.showSettingsAlert();
				}
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void setAddress()
    {
		UIUtils.setUserLocationToSharedPrefs(longi, latti,ack);
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
		String city,country;
    	city = ubean.getUserCity();
    	country = ubean.getUserCountry();
    	if(city.equals("")||country.equals(""))
    		userLocation.setText(city+","+country);
    	else
    		userLocation.setText("Select Your Location");
    }
	
	public void setGeoCoderAddress() {
		new GeoCoderThread(ack).execute();
    }
	
	public class GeoCoderThread extends AsyncTask<Void, Void,List<Address>>{

		private Context context;		
		protected UsersDataBean ubean;
		Geocoder geocoder;
    	List<Address> addresses;
		public GeoCoderThread(Context context) {
			super();
			this.context = context;
			this.addresses = new ArrayList<Address>();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ubean = Utilities.getCurrentUserFromSharedPref(ack);
			geocoder = new Geocoder(context, Locale.getDefault());
		}
		
		@Override
		protected List<Address> doInBackground(Void... params) {
			try {
				if(Geocoder.isPresent()){
					Log.d("geoCoder","present");
					addresses = geocoder.getFromLocation(latti, longi, 1);
				}else{
				Log.d("geoCoder","not present");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return addresses;
		}
		
		@Override
		protected void onPostExecute(List<Address> addresss) {
			super.onPostExecute(addresss);
			String city,country;
	    	if(addresses.size()>0){
		    	city = addresses.get(0).getLocality();
		    	country = addresses.get(0).getCountryName();
		    	userLocation.setText(city+","+country);
	    	}
	    	else{
	    		userLocation.setText("Select Your Location");
	    	}
		}
	}
}