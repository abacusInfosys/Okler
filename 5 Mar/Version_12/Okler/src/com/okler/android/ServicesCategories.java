package com.okler.android;

import com.okler.diagnostics.DiagnosticsActivityHome;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okleruser.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ServicesCategories extends BaseActivity {

	View bottomBarLayout;
	LinearLayout layoutUploadPresc, layoutPharmacy, layoutDignisticService, 
	layoutMedicalService, layoutPhysiotherapy, layoutHealthShop;
	Intent intent;
	Context context;
	UIUtils uiUtils;
	ImageButton btnHome;
	   
	    private ListView mDrawerList;
	    private ActionBarDrawerToggle mDrawerToggle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_services_categories);
		context = getApplicationContext();
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
				
		btnHome =(ImageButton) bottomBarLayout.findViewById(R.id.btnHome);
		btnHome.setEnabled(false);
		layoutUploadPresc = (LinearLayout) findViewById(R.id.upload_presc_layout);
		layoutPharmacy = (LinearLayout) findViewById(R.id.pharmacy_layout);
		layoutDignisticService = (LinearLayout) findViewById(R.id.dignostic_service_layout);
		layoutMedicalService = (LinearLayout) findViewById(R.id.medical_service_layout);
		layoutPhysiotherapy = (LinearLayout) findViewById(R.id.physiotherapy_layout);
		layoutHealthShop = (LinearLayout) findViewById(R.id.health_shop_layout);
				
		setUploadPresListener();
		setPharmacyListener();
		setDiagnosticsListener();
		setMediListener();
		setPhysioListener();
		setHealthShopListener();
		
		RelativeLayout rl = (RelativeLayout)bottomBarLayout.findViewById(R.id.homeRL);
		btnHome =(ImageButton) rl.findViewById(R.id.btnHome);
		btnHome.setEnabled(false);
	}
	private void setUploadPresListener() {
		layoutUploadPresc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context, UploadPrescription.class);
				intent.putExtra("isMedPres", true);
				startActivity(intent);
			}
		});
	}
	
	private void setPharmacyListener() {
		layoutPharmacy.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "Pharmacy activity shown", Toast.LENGTH_LONG).show();
				intent = new Intent(context, MedicineHomeActivity.class);//12_01_2016 Gitesh
				startActivity(intent);//12_01_2016 Gitesh
			}
		});
	}
	private void setDiagnosticsListener() {
		layoutDignisticService.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	Toast.makeText(getApplicationContext(), "Diagnosis activity shown", Toast.LENGTH_LONG).show();
				intent = new Intent(context, DiagnosticsActivityHome.class);
				int ab = uiUtils.getBookingType("Diagnostic");
				Okler.getInstance().setBookingType(ab);
				startActivity(intent);
			}
		});
	}

	private void setMediListener()
	{
			layoutMedicalService.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context, MedicalServices.class);
				startActivity(intent);
			}
		});
	}
		
	private void setPhysioListener()
	{
		layoutPhysiotherapy.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context, Physiotherapy.class);
				startActivity(intent);
			}
		});
	}
	private void setHealthShopListener() {
		layoutHealthShop.setOnClickListener(new OnClickListener() {			
		@Override
		public void onClick(View v) {
			//Toast.makeText(getApplicationContext(), "Health shop activity shown", Toast.LENGTH_LONG).show();
			intent = new Intent(context, HSNew.class);
			int in = 0;
			intent.putExtra("name", 0);
			uiUtils = new UIUtils();
			int ab = uiUtils.getBookingType("Healthshop");
			Okler.getInstance().setBookingType(ab);
			int a = Okler.getInstance().getBookingType();
		//	Toast.makeText(getApplicationContext(), ""+a, Toast.LENGTH_LONG).show();
			startActivity(intent);
		}
	});
}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.services_categories, menu);
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(this,IntroScreen.class);
		startActivity(intent);
	}
	
}
