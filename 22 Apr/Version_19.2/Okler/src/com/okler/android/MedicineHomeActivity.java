package com.okler.android;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MedicineHomeActivity extends BaseActivity {

	View bottomBarLayout;
	Toolbar toolBar;
	Spinner locationSpinner;
	TextView locationTv;
	Button notifCount;
	ImageView allopathyIv,ayurvedicIv,homeopathyIv;
	UIUtils uiutils;
	ImageView imgBack;
	Activity ack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medicine_home);
		toolBar = (Toolbar)findViewById(R.id.toolbar);
		ack = this;
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
		locationSpinner = (Spinner)findViewById(R.id.locationSpinner);
		locationTv = (TextView)findViewById(R.id.locationText);
		allopathyIv = (ImageView)findViewById(R.id.allopathyIv);
		ayurvedicIv = (ImageView)findViewById(R.id.ayurvedicIv);
		homeopathyIv = (ImageView)findViewById(R.id.homeopathyIv);
		
		
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
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		/*getMenuInflater().inflate(R.menu.medicine_home, menu);
		MenuItem item = menu.findItem(R.id.action_settings);
		item.setActionView(R.layout.feed_update_count);
	    //
		 View count = item.getActionView();
		    notifCount = (Button) count.findViewById(R.id.notif_count);
		    notifCount.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				//	Toast.makeText(getApplicationContext(), "btn clicked", Toast.LENGTH_LONG).show();
					Intent i = new Intent(getApplicationContext(), MyCart.class);
					startActivity(i);
				}
			});
		    CartDataBean cbean = Okler.getInstance().getMainCart();
			   ArrayList<ProductDataBean>pdList = cbean.getProdList();
			   // pdList = odbean.getProdList();
			   int maincount = pdList.size(); 
			   
			   if(maincount>9){
			    notifCount.setText(""+maincount);
			    }
			    else{
			    	notifCount.setText("0"+maincount);
			    }*/
		    
		return true;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(notifCount, ack);
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
}
