package com.okler.android;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.okler.databeans.UsersDataBean;
import com.okler.diagnostics.DiagnosticsActivityHome;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.GPSTracker;
import com.okler.utils.Okler;
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
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	RelativeLayout auto_detect_rl,back_layout;
	GPSTracker gps;
	Activity ack;
	double longi,latti;
	ListView locationsListView;
	ArrayAdapter<String> cityAdp;
	String[] city;
	String serverUrl,getCitiesUrl,searchText="";
	int pageNo=1,totalWebServiceResults,totalPages;
	ArrayList<UsersDataBean> citiesList = new ArrayList<UsersDataBean>();
	int count;
	LinearLayout progressLinLayout;
	Toolbar toolBar;
	ImageView imgback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		ack = this;
		findViews();
			cityAdp = new ArrayAdapter<>(ack, android.R.layout.simple_list_item_1, android.R.id.text1);
		//	location_text = (TextView)findViewById(R.id.location_text);
		setUpSearchView();
		setSupportActionBar(toolBar);
		ActionBar ab = getSupportActionBar();
		/*int a = Okler.getInstance().getBookingType();*/
		toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler.getInstance().getBookingType()));
		Utilities.setTitleText(toolBar, "Select Location");
	//	UIUtils.setBackClick(toolBar, ack);
		serverUrl = getString(R.string.serverUrl);
		
		locationsListView.setAdapter(cityAdp);
		listOnScroll();
	//	locationsListView = (ListView)findViewById(R.id.cityList);
		
		auto_detect_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showProgress(true);
				gps = new GPSTracker(ack);
				if(gps.canGetLocation()){
					latti = gps.getLatitude();
					longi = gps.getLongitude();
				//	Toast.makeText(ack, "Latti "+latti+"\n"+"longi "+longi, 2000).show();
					UIUtils.setUserLocationToSharedPrefs(longi, latti,ack);
					/*UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
					String city,country;
			    	city = ubean.getUserCity();
			    	country = ubean.getUserCountry();*/
			    
			    	Intent startMedHome = new Intent(getApplicationContext(),MedicineHomeActivity.class);
			    	startMedHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    	startMedHome.putExtra("isFromLocation", true);
			    	startActivity(startMedHome);			    	
			    	finish();
			    	
				}else{
					gps.showSettingsAlert();
					showProgress(false);
				}
				
				}	
			
		});
		
		locationsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				UsersDataBean ubean = new UsersDataBean();
				ubean = Utilities.getCurrentUserFromSharedPref(ack);
				ubean.setUserCity(citiesList.get(position).getUserCity());
				ubean.setUserCityId(citiesList.get(position).getUserCityId());
				ubean.setUserCountry(citiesList.get(position).getUserCountry());
				ubean.setUserCountryId(citiesList.get(position).getUserCountryId());
				Utilities.writeCurrentUserToSharedPref(ack, ubean);
				
				Intent startMedHome = null;
				if(Okler.getInstance().getBookingType()==9){
					startMedHome = new Intent(getApplicationContext(),DiagnosticsActivityHome.class);	
				}else{
					startMedHome = new Intent(getApplicationContext(),MedicineHomeActivity.class);
				}
				startMedHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    	startMedHome.putExtra("isFromLocation", true);
		    	startActivity(startMedHome);			    	
		    	finish();
				//citiesList.get(position).getUserCity();
				                                                                 
			}
		});
		
		back_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		imgback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}

	public void findViews(){
	
		searchView = (SearchView) findViewById(R.id.searchview_location);
		locationsListView = (ListView) findViewById(R.id.cityList);
		auto_detect_rl = (RelativeLayout)findViewById(R.id.auto_detect_rl);
		progressLinLayout = (LinearLayout) findViewById(R.id.progressLinLayout);
		toolBar = (Toolbar)findViewById(R.id.toolbar);
		
		/*	back_layout = (RelativeLayout)findViewById(R.id.back_layout);*/
		imgback = (ImageView)findViewById(R.id.toolbar_back);
	}
	
	private void setUpSearchView() {
		// TODO Auto-generated method stub
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
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
		//geocoder = new Geocoder(this, Locale.getDefault());
		String city="",state="",country="",knownName="",postalCode="";
		if(newText.length()>=3){
			searchText=newText;
			getCitiesUrl = setCitiesUrl(1, searchText);
			cityAdp.clear();
			pageNo=1;
			citiesList = new ArrayList<UsersDataBean>();
			callWebService();
	
			
		/*try {
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
		}*/
		
		/*}
		else*/
		//return false;
		}
		else{
			cityAdp.clear();
			searchText="";
		}
		return true;	
}
	public String setCitiesUrl(int pageNo,String search){
		
		if(pageNo==0)
			pageNo=1;
		
		String url = serverUrl+getString(R.string.getAllCitiesPart1)+pageNo+getString(R.string.getProdsByGroup4)+search;
		
		return url;
	}
	
	public void callWebService(){
		Log.e("TP", getCitiesUrl);
		WebJsonObjectRequest getCityJson = new WebJsonObjectRequest(Method.GET, getCitiesUrl, new JSONObject(), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				//cityAdp.clear();
				processResponse(response);
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				
			Log.e("ERROR", String.valueOf(error.getStackTrace()));
			//Utilities.writeToLogFIle("LocationActivity Error "+String.valueOf(error.getStackTrace()));
			}
		});
		
		VolleyRequest.addJsonObjectRequest(ack, getCityJson);
	}
	
	public void processResponse(JSONObject response){
		if(response.optInt("cities_count")>0){
		
			JSONArray resArr = response.optJSONArray("result");
			
			UsersDataBean cityBean;
			for (int i = 0; i < resArr.length(); i++) {
				
				JSONObject jobj = resArr.optJSONObject(i);
				cityBean = new UsersDataBean();
				
				cityBean.setUserCity(jobj.optString("city_name"));
				cityBean.setUserCityId(jobj.optString("city_id"));
				cityBean.setUserCountryId(jobj.optString("country_id"));
				cityBean.setUserCountry(jobj.optString("country_name"));
				citiesList.add(cityBean);
				
				Log.e("TP", citiesList.get(i).getUserCity());
				Log.e("TP web", jobj.optString("city_name"));
			}
			
			
		totalWebServiceResults = response.optInt("cities_count");
		totalPages = response.optInt("page_count");
			
		cityAdp.clear();
		for (int i = 0; i < citiesList.size(); i++) {
		cityAdp.add(citiesList.get(i).getUserCity());
		}
		}else{
			cityAdp.clear();	
		}
		
		cityAdp.notifyDataSetChanged();
		
		
	}
	public void listOnScroll(){
		locationsListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				
				int lastItemInScreen = firstVisibleItem + visibleItemCount;
				if ((lastItemInScreen == totalItemCount)
						&& totalWebServiceResults != citiesList.size()) {
					pageNo++;
					count++;
					//Toast.makeText(ack, "total "+totalItemCount+" \ntotalWeb "+totalWebServiceResults+" \nlistsize "+citiesList.size()+" \ncount "+count, 2000).show();
					
					getCitiesUrl = setCitiesUrl(pageNo, searchText);
					callWebService();
				}
				
			}
		});
	}
	
	private void showProgress(boolean paramBoolean) {
		if (paramBoolean) {
			this.progressLinLayout.setVisibility(View.VISIBLE);
			return;
		}
		this.progressLinLayout.setVisibility(View.INVISIBLE);
	}
}
