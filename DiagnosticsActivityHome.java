package com.okler.diagnostics;

import com.okler.android.BaseActivity;
import com.okler.android.LocationActivity;
import com.okler.android.MedicineHomeActivity;
import com.okler.android.NewSignIn;
import com.okleruser.R;
import com.okler.android.UploadPrescription;
import com.okler.databeans.UsersDataBean;
import com.okler.utils.GPSTracker;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
/*This is changed*/
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagnostics);
		context = this;
		ack = this;
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		final ActionBar ab = getSupportActionBar();
		// ab.setDisplayHomeAsUpEnabled(true);
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		/*imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});*/
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
						DiagnoTestsHome.class);
				startActivity(startDiagnoTest);
			}
		});

		diagnoPackageIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent startDPack = new Intent(context, DiagnoPackageHome.class);
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
				// TODO Auto-generated method stub
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.diagnostics, menu);
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
					setAddress();
			//	Toast.makeText(ack, "Latti "+latti+"\n"+"longi "+longi, 2000).show();
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
}
