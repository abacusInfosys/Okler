package com.okler.android;

import org.json.JSONException;
import org.json.JSONObject;

import com.okler.databeans.PhysioAndMedicalBean;
import com.okler.dialogs.PhysiomedDialog;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MyOrderMS_History extends BaseActivity {

	TextView txt_firstname, txt_surname, txt_email, txt_phoneno, txt_From,
			txt_To, txt_address, txt_pincode, txt_weight, txt_relation,
			txt_service, txt_sercice_rq;
	TextView txt_state, txt_city, addr_title_name, orderId;

	static Button btn_cancel_order;

	Toolbar toolBar;
	ActionBar ab;
	TextView textView_screen_title;
	ImageView imgBack;
	PhysioAndMedicalBean msDatabean;
	Activity act;
	String relation;
	String service;
	String status;
	View bottomBarLayout;
	JSONObject addbean;
	ImageView order_image;
	String add;
	String custId, ordId;
	int servType, get;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order_ms__history);
		textView_screen_title = (TextView) findViewById(R.id.textView_screen_title);
		addr_title_name = (TextView) findViewById(R.id.addr_title_name);
		order_image = (ImageView) findViewById(R.id.edit_red_pencil);
		toolBar = (Toolbar) findViewById(R.id.toolbarMyorder);
		setSupportActionBar(toolBar);
		ab = getSupportActionBar();
		// ab.setDisplayHomeAsUpEnabled(true);
		act = this;
		msDatabean = Okler.getInstance().getPhysioMedBean();
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		orderId = (TextView) findViewById(R.id.orderId);

		View view = (View) findViewById(R.id.titleOrderId1);
		TextView text1 = (TextView) view.findViewById(R.id.title_mycart);
		text1.setText("ORDER ID");
		View view1 = (View) findViewById(R.id.titleOrderId2);
		TextView text2 = (TextView) view1.findViewById(R.id.title_mycart);
		text2.setText("APPOINTMENT INFORMATION");

		View view3 = (View) findViewById(R.id.titleOrderId4);
		TextView text4 = (TextView) view3.findViewById(R.id.title_mycart);
		text4.setText("ORDER STATUS");

		View view4 = (View) findViewById(R.id.titleOrderId5);
		TextView text5 = (TextView) view4.findViewById(R.id.title_mycart);
		text5.setText("ADDRESS");

		btn_cancel_order = (Button) findViewById(R.id.btn_cancel_order);

		// txt_firstname = (TextView) findViewById(R.id.);

		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// finish();
				if (get == 1) {
					Intent newIntent = new Intent(act,
							ServiceCategoryActivity.class);
					newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(newIntent);
				} else {
					if (get == 2) {
						Intent newIntent = new Intent(act,
								ServiceCategoryActivity.class);
						newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(newIntent);
					} else {
						finish();
					}
				}
			}
		});
		txt_To = (TextView) findViewById(R.id.app_dt_to_value_tv);
		txt_From = (TextView) findViewById(R.id.app_dt_value_tv);
		txt_city = (TextView) findViewById(R.id.tv_city_value);
		addr_title_name = (TextView) findViewById(R.id.addr_title_name);
		txt_address = (TextView) findViewById(R.id.addr_tv);
		txt_relation = (TextView) findViewById(R.id.relation_value_tv);
		txt_service = (TextView) findViewById(R.id.services_required_for_value_tv);
		txt_sercice_rq = (TextView) findViewById(R.id.services_required_value_tv);
		int service_rq = msDatabean.getUsertype();

		add = getIntent().getStringExtra("physiobean");

		get = getIntent().getIntExtra("value", -1);
		switch (get) {
		case 1:
			toolBar.setBackgroundResource(R.drawable.custom_view_grad_medi_services);
			textView_screen_title.setText("Your order is placed");
			textView_screen_title.setTextColor(Color.WHITE);
			textView_screen_title
					.setBackgroundResource(R.drawable.custom_view_grad_medi_services);
			Utilities.setTitleText(toolBar, "My Order");

			txt_To.setText(msDatabean.getto());

			txt_From.setText(msDatabean.getfrom());

			txt_city.setText(msDatabean.getCity());

			addr_title_name.setText(msDatabean.getfirstname() + " "
					+ msDatabean.getsurname());

			txt_address.setText(msDatabean.getAddress() + "\n"
					+ msDatabean.getCity() + " " + "-" + " "
					+ msDatabean.getPincode() + "\n" + msDatabean.getphoneno()
					+ "\n" + msDatabean.getemail());

			relation = msDatabean.getRelation();
			if (relation == "Relation") {
				txt_relation.setText("");
			} else {
				txt_relation.setText(relation);
			}

			service = msDatabean.getService();
			if (service == "Service Required For") {
				txt_service.setText("");
			} else {
				txt_service.setText(service);
			}
			// txt_relation.setText(msDatabean.getRelation());

			// txt_service.setText(msDatabean.getService());
			orderId.setText(msDatabean.getOrder_id());
			switch (service_rq) {
			case 2:
				txt_sercice_rq.setText("Nurse");
				servType = 2;
				break;
			case 4:
				txt_sercice_rq.setText("Medical Attendant");
				servType = 4;
			default:
				break;
			}
			custId = msDatabean.getCustId();
			ordId = msDatabean.getOrder_id();
			if (ordId.isEmpty()) {
				ordId = "" + getIntent().getIntExtra("orderId", 0);
			}
			btn_cancel_order
					.setBackgroundResource(R.drawable.custom_view_grad_medi_services);
			btn_cancel_order.setTextColor(Color.WHITE);

			break;

		case 2:

			toolBar.setBackgroundResource(R.drawable.custom_view_grad_physio);
			textView_screen_title.setText("Your order is placed");
			textView_screen_title.setTextColor(Color.WHITE);
			textView_screen_title
					.setBackgroundResource(R.drawable.custom_view_grad_physio);
			Utilities.setTitleText(toolBar, "My Order");

			txt_To.setText(msDatabean.getto());

			txt_From.setText(msDatabean.getfrom());

			txt_city.setText(msDatabean.getCity());

			addr_title_name.setText(msDatabean.getfirstname() + " "
					+ msDatabean.getsurname());

			txt_address.setText(msDatabean.getAddress() + "\n"
					+ msDatabean.getCity() + " " + "-" + " "
					+ msDatabean.getPincode() + "\n" + msDatabean.getphoneno()
					+ "\n" + msDatabean.getemail());

			relation = msDatabean.getRelation();
			if (relation == "Relation") {
				txt_relation.setText("");
			} else {
				txt_relation.setText(relation);
			}

			service = msDatabean.getService();
			if (service == "Service Required For") {
				txt_service.setText("");
			} else {
				txt_service.setText(service);
			}
			txt_sercice_rq.setText("Physiotherapy");
			// txt_relation.setText(pDataBean.getRelation());

			// txt_service.setText(pDataBean.getService());

			custId = msDatabean.getCustId();
			ordId = msDatabean.getOrder_id();
			if (ordId.isEmpty()) {
				ordId = "" + getIntent().getIntExtra("orderId", 0);
			}
			servType = 3;
			orderId.setText(msDatabean.getOrder_id());
			btn_cancel_order
					.setBackgroundResource(R.drawable.custom_view_grad_physio);
			btn_cancel_order.setTextColor(Color.WHITE);

			break;
		case 3:

			toolBar.setBackgroundResource(R.drawable.custom_view_grad_physio);
			textView_screen_title.setText("Physiotherapy order history");
			textView_screen_title.setTextColor(Color.WHITE);
			textView_screen_title
					.setBackgroundResource(R.drawable.custom_view_grad_physio);
			Utilities.setTitleText(toolBar, "My Order");

			try {
				addbean = new JSONObject(add);
				txt_To.setText(addbean.optString("To"));

				txt_From.setText(addbean.optString("From"));

				txt_city.setText(addbean.optString("city"));

				orderId.setText(addbean.optString("booking_id"));

				addr_title_name.setText(addbean.optString("firstname") + " "
						+ addbean.optString("surname"));

				txt_address.setText(addbean.optString("address") + "\n"
						+ addbean.optString("city") + " " + "-" + " "
						+ addbean.optString("pincode") + "\n"
						+ addbean.optString("phoneno") + "\n"
						+ addbean.optString("email"));

				relation = addbean.optString("relation");
				if (relation.equals("Relation") || relation.equals("null")
						|| relation.equals("")) {
					txt_relation.setText("");
				} else {
					txt_relation.setText(relation);
				}

				service = addbean.optString("service");
				if (service.equals("Service Required For")
						|| service.equals("") || service.equals("")) {
					txt_service.setText("");
				} else {
					txt_service.setText(service);
				}

				status = addbean.optString("booking_status");

				// txt_relation.setText(pDataBean.getRelation());

				// txt_service.setText(pDataBean.getService());

				custId = ""
						+ Utilities.getCurrentUserFromSharedPref(this).getId();
				ordId = addbean.optString("booking_id");
				servType = 3;
				String ordStatus = addbean.optString("booking_status");
				if (ordStatus.equalsIgnoreCase("Placed")) {
					order_image
							.setImageResource(R.drawable.blue_order_status_placed_image);
					// order_image.setImageDrawable(getDrawable(R.drawable.blue_order_status_placed_image));
				}
				if (ordStatus.equalsIgnoreCase("Scheduled")) {

					order_image
							.setImageResource(R.drawable.blue_order_status_scheduled_image);
				}
				if (ordStatus.equalsIgnoreCase("In-Service")) {
					order_image
							.setImageResource(R.drawable.blue_order_status_processing_image);
				}
				if (ordStatus.equalsIgnoreCase("Completed")) {
					order_image
							.setImageResource(R.drawable.blue_order_status_complited_image);
				}

				if (ordStatus.equalsIgnoreCase("Cancelled"))
					btn_cancel_order.setVisibility(View.INVISIBLE);
				else {
					btn_cancel_order
							.setBackgroundResource(R.drawable.custom_view_grad_physio);
					btn_cancel_order.setTextColor(Color.WHITE);
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			break;

		case 4:

			toolBar.setBackgroundResource(R.drawable.custom_view_grad_medi_services);
			textView_screen_title.setText("Medical services order history");
			textView_screen_title.setTextColor(Color.WHITE);
			textView_screen_title
					.setBackgroundResource(R.drawable.custom_view_grad_medi_services);
			Utilities.setTitleText(toolBar, "My Order");
			imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);

			try {
				addbean = new JSONObject(add);
				txt_To.setText(addbean.optString("To"));

				txt_From.setText(addbean.optString("From"));

				txt_city.setText(addbean.optString("city"));

				orderId.setText(addbean.optString("booking_id"));

				addr_title_name.setText(addbean.optString("firstname") + " "
						+ addbean.optString("surname"));

				txt_address.setText(addbean.optString("address") + "\n"
						+ addbean.optString("city") + " " + "-" + " "
						+ addbean.optString("pincode") + "\n"
						+ addbean.optString("phoneno") + "\n"
						+ addbean.optString("email"));

				relation = addbean.optString("relation");
				if (relation.equals("Relation") || relation.equals("null")
						|| relation.equals("")) {
					txt_relation.setText("");
				} else {
					txt_relation.setText(relation);
				}

				service = addbean.optString("service");
				if (service.equals("Service Required For")
						|| service.equals("") || service.equals("null")) {
					txt_service.setText("");
				} else {
					txt_service.setText(service);
				}

				status = addbean.optString("booking_status");

				// txt_relation.setText(pDataBean.getRelation());

				// txt_service.setText(pDataBean.getService());

				custId = ""
						+ Utilities.getCurrentUserFromSharedPref(this).getId();
				ordId = addbean.optString("booking_id");
				String type = addbean.optString("service");
				if (type.equals("Trained Attendant"))
					servType = 4;
				else
					servType = 2;
				/*
				 * custId = msDatabean.getCustId(); ordId =
				 * msDatabean.getOrder_id(); servType =
				 * msDatabean.getUsertype();
				 */
				String ordStatus = addbean.optString("booking_status");

				if (ordStatus.equalsIgnoreCase("Placed")) {
					order_image
							.setImageResource(R.drawable.blue_order_status_placed_image);
					// order_image.setImageDrawable(getDrawable(R.drawable.blue_order_status_placed_image));
				}
				if (ordStatus.equalsIgnoreCase("Scheduled")) {

					order_image
							.setImageResource(R.drawable.blue_order_status_scheduled_image);
				}
				if (ordStatus.equalsIgnoreCase("In-Service")) {
					order_image
							.setImageResource(R.drawable.blue_order_status_processing_image);
				}
				if (ordStatus.equalsIgnoreCase("Completed")) {
					order_image
							.setImageResource(R.drawable.blue_order_status_complited_image);
				}

				if (ordStatus.equalsIgnoreCase("Cancelled"))
					btn_cancel_order.setVisibility(View.INVISIBLE);
				else {
					btn_cancel_order
							.setBackgroundResource(R.drawable.custom_view_grad_medi_services);
					btn_cancel_order.setTextColor(Color.WHITE);
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			break;
		}

		btn_cancel_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				PhysiomedDialog physiomed = new PhysiomedDialog(
						MyOrderMS_History.this, custId, ordId, servType);

				physiomed.show();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.my_order_med__hs__details, menu);
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

	public static void disableCancel() {
		btn_cancel_order.setVisibility(View.GONE);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (get == 3 || get == 4)
			finish();
		else {
			Intent intent = new Intent(getApplicationContext(),
					ServiceCategoryActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}

	}
}
