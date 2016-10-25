package com.okler.diagno;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.okler.android.BaseActivity;
import com.okler.android.LocationActivity;
import com.okler.android.NewSignIn;
import com.okleruser.R;
import com.okler.android.UploadPrescription;
import com.okler.databeans.UsersDataBean;
import com.okler.databeans.diagnobean.DiagnoOrder;
import com.okler.enums.DiagnoOrderType;
import com.okler.utils.GPSTracker;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import android.support.v7.widget.Toolbar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DiagnosticsActivityHome extends BaseActivity {

	View bottomBarLayout;
	Toolbar toolBar;
	ImageView diagnoTestIv, diagnoPackageIv, diagnoDiseaseIv, diagnoUploadPres;
	Context context;
	ImageView imgBack;
	Activity ack;
	TextView locationTv,userLocation;
	GPSTracker gps;
	double latti,longi;
	boolean isLoacation=false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagnostics);
		context = this;
		ack = this;
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		UIUtils.setBackClick(toolBar, ack);
		Utilities.setTitleText(toolBar, "Diagnostic Test");
		diagnoTestIv = (ImageView) findViewById(R.id.diagnoTestIv);
		diagnoPackageIv = (ImageView) findViewById(R.id.diagnoPackageIv);
		diagnoDiseaseIv = (ImageView) findViewById(R.id.diagnoDiseaseIv);
		diagnoUploadPres = (ImageView) findViewById(R.id.diagnoUpPrescIv);
		userLocation = (TextView)findViewById(R.id.userLocation);
		
		diagnoTestIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent startDiagnoTest = new Intent(context,
						DiagnoTestPkgHome.class);
				Okler.getInstance().getDiagnoOrder().setOrderType(DiagnoOrderType.TEST);
				startActivity(startDiagnoTest);
			}
		});

		diagnoPackageIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent startDPack = new Intent(context, DiagnoTestPkgHome.class);
				Okler.getInstance().getDiagnoOrder().setOrderType(DiagnoOrderType.PACKAGE);
				startActivity(startDPack);
			}
		});
		diagnoDiseaseIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent startDiseases = new Intent(context,
						DiagnoDiseaseHome.class);
				startActivity(startDiseases);
			}
		});
		diagnoUploadPres.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Utilities
						.getUserStatusFromSharedPref(DiagnosticsActivityHome.this) == UserStatusEnum.LOGGED_IN
						|| (Utilities
								.getUserStatusFromSharedPref(DiagnosticsActivityHome.this) == UserStatusEnum.LOGGED_IN_FB)
						|| (Utilities
								.getUserStatusFromSharedPref(DiagnosticsActivityHome.this) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
				Intent startUpPresc = new Intent(context,
						UploadPrescription.class);
				startUpPresc.putExtra("isMedPres", false);
				startActivity(startUpPresc);
				}else{
					Intent in = new Intent(DiagnosticsActivityHome.this,
							NewSignIn.class);
					int ab = UIUtils.getBookingType("DiagnoPresc");
					Okler.getInstance().setBookingType(ab);
					startActivity(in);
				}
			}
		});

	userLocation.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			isLoacation = true;
			Intent intent = new Intent(DiagnosticsActivityHome.this,LocationActivity.class);
			startActivity(intent);
		}
	});

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		DiagnoOrder diagnoOrder = new DiagnoOrder();
		Okler.getInstance().setDiagnoOrder(diagnoOrder);
		boolean isFromLocation = getIntent().getBooleanExtra("isFromLocation", false);
		if(isFromLocation||isLoacation)
		{
			isLoacation =false;
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
	    	if(addresss.size()>0){
		    	city = addresss.get(0).getLocality();
		    	country = addresss.get(0).getCountryName();
		    	userLocation.setText(city+","+country);
		    	UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
		    	ubean.setUserCity(city);
		    	ubean.setUserCountry(country);
		    	Utilities.writeCurrentUserToSharedPref(ack, ubean);
	    	}
	    	else{
	    		userLocation.setText("Select Your Location");
	    	}
		}
	}
}
