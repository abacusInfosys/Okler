package com.okler.diagnostics;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import com.okler.android.BaseActivity;
import com.okler.android.MyOrderHome;
import com.okleruser.R;

import com.okler.databeans.AddressDataBean;
import com.okler.databeans.DiagnoOrderDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.android.ServiceCategoryActivity;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;

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
import android.widget.TextView;
import android.widget.Toast;

public class DiagnoOrderDetails extends BaseActivity {
	Button chk_ord_status, place_new_order;
	ImageView imgBack;
	Toolbar toolBar;
	View bottomBarLayout;
	TextView orderId, nameTv, user_details_Tv, mobileTv, dateValue, timeValue,
			atValue;
	DiagnoOrderDataBean dobean;
	String pickupType;
	UsersDataBean ubean = new UsersDataBean();
	ArrayList<AddressDataBean> paList = new ArrayList<AddressDataBean>();
	Activity ack;
	String BookingId;
	TextView title_mycart;
	RelativeLayout back_layout;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagno_order_details);
		ack = this;
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		paList = ubean.getPatAddList();

		pickupType = getIntent().getStringExtra("PickupType");
		BookingId = getIntent().getStringExtra("BookingId");
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		dateValue = (TextView) findViewById(R.id.dateValue);
		timeValue = (TextView) findViewById(R.id.timeValue);
		atValue = (TextView) findViewById(R.id.atValue);
		chk_ord_status = (Button) findViewById(R.id.chk_ord_status);
		nameTv = (TextView) findViewById(R.id.nameTv);
		user_details_Tv = (TextView) findViewById(R.id.user_details_Tv);
		mobileTv = (TextView) findViewById(R.id.mobileTv);
		title_mycart = (TextView) findViewById(R.id.title_mycart);
		title_mycart.setText("APPOINTMENT INFORMATION");
		orderId = (TextView) findViewById(R.id.orderId);
		orderId.setText(BookingId);
		chk_ord_status.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						MyOrderHome.class);
				intent.putExtra("value", 2);
				startActivity(intent);

			}
		});
		back_layout = (RelativeLayout)toolBar.findViewById(R.id.back_layout);
		back_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent startServCat = new Intent(ack,
						ServiceCategoryActivity.class);
				startServCat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(startServCat);	
			}
		});
		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent startServCat = new Intent(ack,
						ServiceCategoryActivity.class);
				startServCat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(startServCat);
			}
		});
		Utilities.setTitleText(toolBar, "Diagnostic Test");
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		place_new_order = (Button) findViewById(R.id.place_new_order);
		place_new_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						DiagnosticsActivityHome.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				finish();
				// Toast.makeText(getApplicationContext(), "Order Placed",
				// Toast.LENGTH_SHORT).show();
				startActivity(intent);

			}
		});
		dobean = Okler.getInstance().getDiagnoOrderDataBean();
		atValue.setText(pickupType);
		dateValue.setText(dobean.getAp_date());
		timeValue.setText(dobean.getAp_time());

		AddressDataBean abean = new AddressDataBean();
		int length = paList.size();
		for (int i = 0; i < length; i++) {
			abean = new AddressDataBean();
			abean = paList.get(i);
			if (abean.isSelected()) {
				nameTv.setText(abean.getFirstname() + " " + abean.getLastname());
				String addr = abean.getDob() + "\n";
				String rel = abean.getRelation();
				if (!(rel.equals("null")))
					addr = addr + rel + "\n";
				addr = addr + abean.getGender() + "\n" + abean.getAddress1()
						+ "\n" + abean.getAddress2() + "\n" + abean.getCity()
						+ " - " + abean.getZip() + abean.getState();

				user_details_Tv.setText(addr);
				mobileTv.setText("+91 " + abean.getPhone());
				break;
			}
		}

	}// end of onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.diagno_order_details, menu);
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
		Intent startServCat = new Intent(ack, ServiceCategoryActivity.class);
		startServCat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(startServCat);
	}

}
