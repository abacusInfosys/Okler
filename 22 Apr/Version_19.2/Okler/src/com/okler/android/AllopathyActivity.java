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
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AllopathyActivity extends BaseActivity {
	View bottomBarLayout;
	Toolbar toolBar;
	Button notifCount;
	ImageView searchByMedi,searchByDiesease;
	Activity ack;
	ImageView imgBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allopathy);
		ack = this;
		toolBar = (Toolbar)findViewById(R.id.toolbar);
		notifCount = (Button) toolBar.findViewById(R.id.notif_count);
		setSupportActionBar(toolBar);
		ActionBar ab = getSupportActionBar();
		//ab.setDisplayHomeAsUpEnabled(true);
		if(Okler.getInstance().getBookingType()==0){
			//ab.setTitle(R.string.title_activity_allopathy);
			Utilities.setTitleText(toolBar, getString(R.string.allopathy));
		}else if(Okler.getInstance().getBookingType()==3){
			Utilities.setTitleText(toolBar, getString(R.string.ayurvedic));
			//ab.setTitle(R.string.title_activity_ayurvedic);
		}else{
			Utilities.setTitleText(toolBar, getString(R.string.homeopathy));
			//ab.setTitle(R.string.title_activity_homeopathy);
		}
		toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler.getInstance().getBookingType()));
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
		
		//toolBar.setBackgroundResource(R.drawable.custom_view_grad_medicine);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		searchByMedi = (ImageView)findViewById(R.id.searchMedicineIv);
		searchByDiesease = (ImageView)findViewById(R.id.searchDeseasesIv);
		
		searchByDiesease.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),SearchByDiseasesActivity.class);
				startActivity(intent);
			}
		});
		searchByMedi.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),SearchByMed1Activity.class);
				intent.putExtra("FromWhere", "searchByMedi");
				startActivity(intent);		
				
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(notifCount, ack);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		/*getMenuInflater().inflate(R.menu.allopathy, menu);
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
			    }
		    */
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

}
