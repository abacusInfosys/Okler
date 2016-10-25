package com.okler.diagnostics;

import android.support.v7.widget.Toolbar;
import com.okler.android.BaseActivity;
import com.okler.android.MyOrderHome;
import com.okleruser.R;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.databeans.diagnobean.DiagnoOrder;
import com.okler.diagno.DiagnosticsActivityHome;
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

public class DiagnoOrderDetails extends BaseActivity {
	Button chk_ord_status, place_new_order;
	ImageView imgBack;
	Toolbar toolBar;
	View bottomBarLayout;
	TextView orderId, nameTv, user_details_Tv, mobileTv, dateValue, timeValue,
			atValue;
	DiagnoOrder dobean;
	String pickupType;
	UsersDataBean ubean = new UsersDataBean();
	AddressDataBean paList = new AddressDataBean();
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
		dobean = Okler.getInstance().getDiagnoOrder();
		paList = dobean.getPatientAddr();
		pickupType = dobean.getPickupType();
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
				onBackPressed();
			}
		});
		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		Utilities.setTitleText(toolBar, "Diagnostic Test");
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		place_new_order = (Button) findViewById(R.id.place_new_order);
		place_new_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DiagnoOrderDetails.this,
						DiagnosticsActivityHome.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		atValue.setText(pickupType);
		dateValue.setText(dobean.getApptDt());
		timeValue.setText(dobean.getAppTime());
				nameTv.setText(paList.getFirstname() + " " + paList.getLastname());
				String addr = paList.getDob() + "\n";
				String rel = paList.getRelation();
				if (!(rel.equals("null")))
					addr = addr + rel + "\n";
				addr = addr + paList.getGender() + "\n" + paList.getAddress1()
						+ "\n" + paList.getAddress2() + "\n" + paList.getCity()
						+ " - " + paList.getZip() + paList.getState();

				user_details_Tv.setText(addr);
				mobileTv.setText("+91 " + paList.getPhone());
	}// end of onCreate

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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent startServCat = new Intent(ack, ServiceCategoryActivity.class);
		startServCat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(startServCat);
	}

}
