package com.okler.android;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

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
import android.widget.RelativeLayout;

public class AllopathyActivity extends BaseActivity {
	View bottomBarLayout;
	Toolbar toolBar;
	Button notifCount;
	ImageView searchByMedi, searchByDiesease;
	Activity ack;
	ImageView imgBack;
	RelativeLayout back_layout;
	String bookingtype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allopathy);
		ack = this;
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		notifCount = (Button) toolBar.findViewById(R.id.notif_count);
		setSupportActionBar(toolBar);
		ActionBar ab = getSupportActionBar();
		
		
		/*imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		back_layout = (RelativeLayout)toolBar.findViewById(R.id.back_layout);
		back_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});*/
		
		UIUtils.setBackClick(toolBar, ack);

		// toolBar.setBackgroundResource(R.drawable.custom_view_grad_medicine);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		searchByMedi = (ImageView) findViewById(R.id.searchMedicineIv);
		searchByDiesease = (ImageView) findViewById(R.id.searchDeseasesIv);

		searchByDiesease.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						SearchByDiseasesActivity.class);
				intent.putExtra("bookingType", bookingtype);
				startActivity(intent);
			}
		});
		searchByMedi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						GroupByMedsActivity.class);
				intent.putExtra("FromWhere", "searchByMedi");
				intent.putExtra("bookingType", bookingtype);
				startActivity(intent);

			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bookingtype = getIntent().getStringExtra("bookingType");
		Okler.getInstance().setBookingType(UIUtils.getBookingType(bookingtype));
		
		//Toast.makeText(getApplicationContext(), ""+boingtype, Toast.LENGTH_SHORT)
	
		if (Okler.getInstance().getBookingType() == 0) {
				Utilities.setTitleText(toolBar, getString(R.string.allopathy));
		} else if (Okler.getInstance().getBookingType() == 3) {
			Utilities.setTitleText(toolBar, getString(R.string.ayurvedic));
	
		} else {
			Utilities.setTitleText(toolBar, getString(R.string.homeopathy));
	
		}
		toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler
				.getInstance().getBookingType()));
		UIUtils.setCartCount(notifCount, ack);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
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
