package com.okler.android;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.okler.databeans.UsersDataBean;
import com.okler.utils.GPSTracker;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;
import com.okleruser.R.id;
import com.okleruser.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends BaseActivity implements SearchView.OnQueryTextListener {
	
	SearchView searchView;
	Geocoder geocoder;
	List<Address> addresses = new ArrayList<Address>();
	TextView location_text;
	RelativeLayout auto_detect_rl;
	GPSTracker gps;
	Activity ack;
	double longi,latti;
	ListView locationsListView;
	ArrayAdapter<String> cityAdp;
	String[] cityLoc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		ack = this;
		cityAdp = new ArrayAdapter<>(ack, android.R.id.text1);
		searchView = (SearchView) findViewById(R.id.searchview_location);
		//locationsListView = (ListView) findViewById(R.id.cityList);
	//	location_text = (TextView)findViewById(R.id.location_text);
		setUpSearchView();
		searchView.setVisibility(View.GONE);
		auto_detect_rl = (RelativeLayout)findViewById(R.id.auto_detect_rl);
	//	locationsListView = (ListView)findViewById(R.id.cityList);
		
		auto_detect_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gps = new GPSTracker(ack);
				if(gps.canGetLocation()){
					latti = gps.getLatitude();
					longi = gps.getLongitude();
				//	Toast.makeText(ack, "Latti "+latti+"\n"+"longi "+longi, 2000).show();
					UIUtils.setUserLocationToSharedPrefs(longi, latti,ack);
					UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
				/*	String city,country;
			    	city = ubean.getUserCity();
			    	country = ubean.getUserCountry();
			    */
			    	Intent startMedHome = new Intent(getApplicationContext(),MedicineHomeActivity.class);
			    	startMedHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    	startMedHome.putExtra("isFromLocation", true);
			    	startActivity(startMedHome);			    	
			    	finish();
			    	
				}else{
					gps.showSettingsAlert();
				}
				
				}	
			
		});
		
	}

	private void setUpSearchView() {
		// TODO Auto-generated method stub
		searchView.setIconifiedByDefault(false);
		//searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint("Start typing your location");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.location, menu);
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
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		geocoder = new Geocoder(this, Locale.getDefault());
		String city="",state="",country="",knownName="",postalCode="";
		if(newText.length()>=3){
		try {
			List<Address> addresses = geocoder.getFromLocationName(newText, 2);
			cityLoc = new String[addresses.size()];
			for(int i = 0; i < addresses.size() ; i++)
			{
				city = "";
				country = "";
				if(!(addresses.get(i).getLocality()==null))
				{
				city = addresses.get(0).getLocality();
		    	Log.d("City", city);
		    	if(!(addresses.get(0).getCountryName()==null))
				{	
		    	country = addresses.get(0).getCountryName();
		    	Log.d("Country", country);
				}
		    	String loc = city+country;
		    	cityLoc[i] = loc;
		    	cityAdp.add(loc);
				}	
			}
			cityAdp.notifyDataSetChanged();
			if(!(addresses.get(0).getLocality()==null))
			{
			city = addresses.get(0).getLocality();
	    	Log.d("City", city);
			}
			if(!(addresses.get(0).getAdminArea()==null))
			{	
	    	state = addresses.get(0).getAdminArea();
	    	Log.d("State", state);
			}
			if(!(addresses.get(0).getCountryName()==null))
			{	
	    	country = addresses.get(0).getCountryName();
	    	Log.d("Country", country);
			}
			if(!(addresses.get(0).getPostalCode()==null))
			{	
	    	postalCode = addresses.get(0).getPostalCode();
	    	Log.d("postalCode", postalCode);
			}
			if(!(addresses.get(0).getFeatureName()==null))
			{	
	    	knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
	    	Log.d("knownName", knownName);
			}
	    	locationsListView.setAdapter(cityAdp);
	    	
		//	location_text.setText(city+"\n"+state+"\n"+country+"\n"+postalCode+"\n"+knownName);
			
			//Toast.makeText(getApplicationContext(), city+"\n"+state+"\n"+country+"\n"+postalCode+"\n"+knownName	, 2000).show();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		}
		else
		return false;
	}
}
