package com.okler.diagnostics;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Network;
import com.android.volley.Request.Method;
import com.okler.android.BaseActivity;
import com.okleruser.R;
import com.okler.customviews.CustomViewSelectedLabs;
import com.okler.customviews.CustomViewSelectedTests;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.DiagnoLabsDataBean;
import com.okler.databeans.DiagnoOrderDataBean;
import com.okler.databeans.DiagnoPackageDataBean;
import com.okler.databeans.TestDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.databeans.diagnobean.DiagnoLabBranchDataBean;
import com.okler.databeans.diagnobean.DiagnoLabTestDataBean;
import com.okler.databeans.diagnobean.DiagnoOrder;
import com.okler.databeans.diagnobean.DiagnoPackagesDataBean;
import com.okler.databeans.diagnobean.DiagnoTestDataBean;
import com.okler.diagno.DiagnoTestPkgHome;
import com.okler.dialogs.ConfirmTestDeleteDialog;
import com.okler.dialogs.DiagnoOrderCancellation;
import com.okler.enums.DiagnoOrderType;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DiagnoOrderSummary extends BaseActivity implements
		DialogInterface.OnDismissListener {
	View bottomBarLayout;
	Toolbar toolBar;
	LinearLayout lab_addr_RL, LL_for_selected_tests, LL_for_selected_labs;
	RelativeLayout RL_for_customView1, RL_for_customView2, RL_for_cl1,
			RL_for_cl2;

	CustomViewSelectedLabs customLab;
	LinearLayout.LayoutParams params;
	ImageView imgBack;
	String pickupType="";
	//DiagnoOrderDataBean dobean = new DiagnoOrderDataBean();
	DiagnoOrderType diagnoOrderType;
	DiagnoOrder dobean = new DiagnoOrder();
	//DiagnoLabsDataBean labbean = new DiagnoLabsDataBean();
	DiagnoLabBranchDataBean labbean = new DiagnoLabBranchDataBean();
	TextView lab_title, home_title, shipping_charg_value_tv, shipping_charg_tv,
			user_details_Tv, nameTv, lab_addr_tv, lab_addr_title_tv,
			title_appo_info, dateValue, timeValue, atValue, coupon_appl_text,
			coupon_disc_value_tv, title_Coupon, labName, title_Lab, title_Test,
			title_Bill, amount_value_tv, okler_disc_value_tv, tax_value_tv,
			net_pay_value_tv;
	RelativeLayout pickup_addr_RL, appoint_info_RL, coupon_disc_RL,
			coupon_code_RL, selected_labs_RL, selected_tests_RL,
			bill_summary_RL, shipping_charg_RL, order_id_RL, orderStatusRL,
			coupon_code_heading;
	// ArrayList<TestDataBean> tList;
	Activity ack;
	Button apply_button,txt_add_more_tests;
	EditText coupon_code;
	String couponCodeUrl;
	ArrayList<DiagnoPackagesDataBean> dpList = new ArrayList<DiagnoPackagesDataBean>();
	//DiagnoPackageDataBean dpbean = new DiagnoPackageDataBean();
	float amount = 0.00f, discount = 0.00f, tax = 0.00f, net_pay = 0.00f,
			home_visit = 0.00f, redAmt = 0.00f;
	UsersDataBean ubean = new UsersDataBean();
	//ArrayList<AddressDataBean> paList = new ArrayList<AddressDataBean>();
	AddressDataBean paList = new AddressDataBean();
	ArrayList<DiagnoTestDataBean> tList = new ArrayList<DiagnoTestDataBean>();
	//ArrayList<TestDataBean> tList = new ArrayList<TestDataBean>();
	String mainUrl;
	int userId, pat_id;
	String result;
	ImageView edit_red_pencil;
	boolean isFromOrder;
	int pos = 0, pickup;
	ArrayList<DiagnoOrder> usersDiagnoOrder;
	static Button sched_appoint;
	RelativeLayout coupon_code_inner_RL;

	String lab_id;
	int slot_id;
	String lab_city_id, state_lab_id, lab_branch_id, ap_date, disc;
	String entity_price, relation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagno_order_summary);
		ack = this;
		isFromOrder = getIntent().getBooleanExtra("isFromOrder", false);
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		userId = ubean.getId();
		diagnoOrderType = Okler.getInstance().getDiagnoOrder().getOrderType();
		if (isFromOrder) {
			usersDiagnoOrder = Okler.getInstance().getUserDiagnoOrders();
			pos = getIntent().getIntExtra("position", 0);
			paList = usersDiagnoOrder.get(pos).getPatientAddr();
			pickupType = usersDiagnoOrder.get(pos).getPickupType();
			dobean = usersDiagnoOrder.get(pos);
			diagnoOrderType = dobean.getOrderType();
		} else {
			dobean = Okler.getInstance().getDiagnoOrder();
			paList = dobean.getPatientAddr();
			pickupType = dobean.getPickupType(); 
					//getIntent().getStringExtra("PickupType");
			
		}
		if(pickupType==null){
			pickupType="";
		}
		
		order_id_RL = (RelativeLayout) findViewById(R.id.orderIdInnerRL);
		TextView title_orderId = (TextView) order_id_RL
				.findViewById(R.id.title_mycart);
		title_orderId.setText("ORDER ID");

		LL_for_selected_labs = (LinearLayout) findViewById(R.id.LL_for_selected_labs);
		txt_add_more_tests = (Button)findViewById(R.id.txt_add_more_tests);
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		LL_for_selected_tests = (LinearLayout) findViewById(R.id.LL_for_selected_tests);
		selected_labs_RL = (RelativeLayout) findViewById(R.id.selected_labs_RL);
		selected_tests_RL = (RelativeLayout) findViewById(R.id.selected_tests_RL);
		bill_summary_RL = (RelativeLayout) findViewById(R.id.bill_summary_RL);
		shipping_charg_RL = (RelativeLayout) findViewById(R.id.shipping_charg_RL);
		orderStatusRL = (RelativeLayout) findViewById(R.id.orderStatusRL);
		coupon_code_heading = (RelativeLayout) findViewById(R.id.coupon_code_heading);
		shipping_charg_RL.setVisibility(View.GONE);
		title_Lab = (TextView) selected_labs_RL.findViewById(R.id.title_mycart);
		title_Test = (TextView) selected_tests_RL
				.findViewById(R.id.title_mycart);
		if(diagnoOrderType.equals(DiagnoOrderType.TEST)){
			labbean = dobean.getLabTestDataBean().getCurrentLab();
			title_Test.setText("SELECTED TESTS");
		}else{
			labbean = dobean.getLabPkgDataBean().getCurrentLab();
			txt_add_more_tests.setText("ADD MORE PACKAGES");
			title_Test.setText("SELECTED PACKAGES");
		}
		title_Lab.setText("SELECTED LAB");
		
		
		title_Bill = (TextView) bill_summary_RL.findViewById(R.id.title_mycart);
		title_Bill.setText("BILL SUMMARY");

		coupon_code_inner_RL = (RelativeLayout) findViewById(R.id.coupon_code_inner_RL);
		amount_value_tv = (TextView) findViewById(R.id.amount_value_tv);
		okler_disc_value_tv = (TextView) findViewById(R.id.okler_disc_value_tv);
		tax_value_tv = (TextView) findViewById(R.id.tax_value_tv);
		net_pay_value_tv = (TextView) findViewById(R.id.net_pay_value_tv);
		coupon_code_RL = (RelativeLayout) findViewById(R.id.coupon_code_RL);
		apply_button = (Button) findViewById(R.id.apply_button);
		coupon_code = (EditText) findViewById(R.id.coupon_code);
		coupon_disc_RL = (RelativeLayout) findViewById(R.id.coupon_disc_RL);
		coupon_disc_RL.setVisibility(View.VISIBLE);
		coupon_disc_value_tv = (TextView) findViewById(R.id.coupon_disc_value_tv);
		coupon_appl_text = (TextView) findViewById(R.id.coupon_appl_text);
		dateValue = (TextView) findViewById(R.id.dateValue);
		timeValue = (TextView) findViewById(R.id.timeValue);
		atValue = (TextView) findViewById(R.id.atValue);
		appoint_info_RL = (RelativeLayout) findViewById(R.id.appoint_info_RL);
		title_appo_info = (TextView) appoint_info_RL
				.findViewById(R.id.title_mycart);
		title_appo_info.setText("APPOINTMENT INFORMATION");
		lab_addr_RL = (LinearLayout) findViewById(R.id.lab_addr_RL);
		lab_title = (TextView) lab_addr_RL.findViewById(R.id.title_mycart);
		lab_title.setText("SELECTED LAB ADDRESS");
		pickup_addr_RL = (RelativeLayout) findViewById(R.id.pickup_addr_RL);
		home_title = (TextView) pickup_addr_RL.findViewById(R.id.title_mycart);
		home_title.setText("PICKUP ADDRESS");
		lab_addr_title_tv = (TextView) findViewById(R.id.addr_title_tv);
		lab_addr_tv = (TextView) findViewById(R.id.addr_tv);
		nameTv = (TextView) findViewById(R.id.nameTv);
		user_details_Tv = (TextView) findViewById(R.id.user_details_Tv);
		shipping_charg_tv = (TextView) findViewById(R.id.shipping_charg_tv);
		shipping_charg_value_tv = (TextView) findViewById(R.id.shipping_charg_value_tv);
		edit_red_pencil = (ImageView) findViewById(R.id.edit_red_pencil);
		edit_red_pencil.setVisibility(View.GONE);
		sched_appoint = (Button) findViewById(R.id.sched_appoint);
		tList = new ArrayList<DiagnoTestDataBean>();
		setSupportActionBar(toolBar);
		final ActionBar ab = getSupportActionBar();

		// ab.setDisplayHomeAsUpEnabled(true);
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);

		/*imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});*/
		coupon_disc_value_tv.setText("Rs." + redAmt);
		UIUtils.setBackClick(toolBar, ack);
		if (!isFromOrder){
			Utilities.setTitleText(toolBar, "Diagnostic Test [5/5]");
			txt_add_more_tests.setVisibility(View.VISIBLE);
		}
		else
			Utilities.setTitleText(toolBar, "Diagnostic Order History");

		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 10, 0, 0);

		codeForDiagnoTest();
		codeForMyOrder();
		if (isFromOrder) {
			order_id_RL.setVisibility(View.VISIBLE);
			// orderStatusRL.setVisibility(View.VISIBLE);
			TextView ordId = (TextView) findViewById(R.id.orderId);
			ordId.setText(usersDiagnoOrder.get(pos).getOrderId());
			// coupon_code_inner_RL.setVisibility(View.GONE);
			// coupon_code_heading.setVisibility(View.GONE);
			coupon_code_RL.setVisibility(View.GONE);
			if (!usersDiagnoOrder.get(pos).getStatus()
					.equalsIgnoreCase("cancelled")) {
				sched_appoint.setText("Cancel Order");

			} else
				sched_appoint.setVisibility(View.GONE);
		}
		apply_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String cCode = coupon_code.getText().toString().trim();
				//float amt = Float.parseFloat(amount_value_tv.getText()
						//.toString());
				try {
					cCode = URLEncoder.encode(cCode, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(cCode.equals("")|| cCode.length()==0)
				{
					Toast.makeText(ack, "Please enter coupon code", Toast.LENGTH_SHORT).show();
				}else{	
					couponCodeUrl = getString(R.string.couponCodeUrlPart1) + cCode
							+ getString(R.string.couponCodeUrlPart2) + 2
							+ getString(R.string.couponCodeUrlPart3) + amount;

				WebJsonObjectRequest cjson = new WebJsonObjectRequest(
						Method.GET, couponCodeUrl, new JSONObject(),
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								Log.d("Response", String.valueOf(response));
								JSONObject jobj = (JSONObject) response;
								try {
									if (jobj.getString("message").equals(
											"Coupon Valid")) {
										redAmt = Float.parseFloat(jobj
												.getString("reduction_amount"));
										float net = net_pay;
										// Float.parseFloat(net_pay_value_tv.getText().toString());
										if (net > redAmt) {
											net = net - redAmt;
											net_pay = net;
											coupon_disc_RL
													.setVisibility(View.VISIBLE);
											net_pay_value_tv.setText("Rs. "
													+ net_pay);
											coupon_disc_value_tv.setText("Rs. "
													+ redAmt);
											coupon_appl_text
													.setVisibility(View.VISIBLE);
										} else {
											coupon_appl_text
													.setText("Invalid Coupon");
											coupon_appl_text
													.setVisibility(View.VISIBLE);
										}
									} else {
										coupon_appl_text
												.setText("Invalid Coupon");
										coupon_appl_text
												.setVisibility(View.VISIBLE);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								Log.d("Error", String.valueOf(error));
								coupon_appl_text.setText("Invalid Coupon");
								coupon_appl_text.setVisibility(View.VISIBLE);
							}
						});

				VolleyRequest.addJsonObjectRequest(ack, cjson);
				}
			}
		});
		if (pickupType.equals("Lab Visit")) {
			setLabAddr();
			pickup = 2;
		} else {
			setHomeAddr();
			pickup = 1;
		}
		
		txt_add_more_tests.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DiagnoOrderSummary.this,DiagnoTestPkgHome.class);
				intent.putExtra("isFromSummary", true);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("isTestsPkgsByLabs", true);
				startActivity(intent);
				
			}
		});	
	}// end of onCreate

	private void setHomeAddr() {
		AddressDataBean abean = new AddressDataBean();
		if (isFromOrder) {
			abean = paList;
			String lname = abean.getLastname();
			if(lname==null)
				lname="";
			
			nameTv.setText(abean.getFirstname() + " " + lname);
			String addr = abean.getDob() + "\n";
			String rel = abean.getRelation();
			if (!(rel.equals("null")))
				addr = addr + rel + "\n";
			addr = addr + abean.getGender() + "\n" + abean.getAddress1() + "\n"
					+ abean.getAddress2() + "\n" + abean.getCity() + " - "
					+ abean.getZip() + "\n" + "+91 " + abean.getPhone();
			user_details_Tv.setText(addr);
		} else {
			/*int length = paList.size();
			for (int i = 0; i < length; i++) {*/
				//abean = new AddressDataBean();

				//abean = paList.get(i);
				//if (abean.isSelected()) {
					nameTv.setText(paList.getFirstname() + " "
							+ paList.getLastname());
					String addr = paList.getDob() + "\n";
					String rel = paList.getRelation();
					if (!(rel.equals("null")))
						addr = addr + rel + "\n";
					addr = addr + paList.getGender() + "\n"
							+ paList.getAddress1() + "\n" + paList.getAddress2()
							+ "\n" + paList.getCity() + " - " + paList.getZip()
							+ "\n" + "+91 " + paList.getPhone();
					user_details_Tv.setText(addr);
					relation = paList.getRelation();
					//break;
				//}
			//}
		}
	}

	private void setLabAddr() {
		lab_addr_RL.setVisibility(View.VISIBLE);
		pickup_addr_RL.setVisibility(View.GONE);
		
		DiagnoLabBranchDataBean dbean = new DiagnoLabBranchDataBean(); 
		if(diagnoOrderType.equals(DiagnoOrderType.TEST))		
			dbean = dobean.getLabTestDataBean().getCurrentLab();
		else
			dbean = dobean.getLabPkgDataBean().getCurrentLab();
		//DiagnoLabsDataBean dbean = dobean.getSelectedLab();
		lab_addr_title_tv.setText(dbean.getLab_name());
		String addr = dbean.getAddr1();
				//dbean.getLab_address() + ", " + dbean.getLab_city()
				//+ " - " + dbean.getLab_pinCode() + ", " + dbean.getLab_state();
		lab_addr_tv.setText(addr);
	}

	private void codeForMyOrder() {
		lab_addr_RL.setVisibility(View.GONE);
		pickup_addr_RL.setVisibility(View.VISIBLE);

	}

	private void codeForDiagnoTest() {
		ImageView edit_pencil_red;

		edit_pencil_red = (ImageView) findViewById(R.id.edit_pencil_red);
		edit_pencil_red.setVisibility(View.GONE);

		coupon_code_inner_RL.setVisibility(View.VISIBLE);

		sched_appoint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isFromOrder) {
					// Start cancel order flow
					DiagnoOrderCancellation dgOrd = new DiagnoOrderCancellation(
							ack, "" + userId, usersDiagnoOrder.get(pos)
									.getOrderId()+"", ap_date);
					dgOrd.show();
				} else {
					sched_appoint.setEnabled(false);
					String fname = "fname";
					String lname = "lname";
					String name = fname + lname;
					String dob = "";
					String addr1 = "", adr2 = "", city_id = "", state_id = "", zip = "", landmark = "";
					String mobile = "";

					//int length = paList;

					/*for (int i = 0; i < length; i++) {
						AddressDataBean abean = new AddressDataBean();
						abean = paList.get(i);
						if (abean.isSelected()) {*/
							fname = paList.getFirstname();
							lname = paList.getLastname();
							name = fname + " " + lname;
							dob = paList.getDob();
							addr1 = paList.getAddress1();
							adr2 = paList.getAddress2();
							city_id = paList.getCity_id();
							state_id = "" + paList.getState_id();
							zip = paList.getZip();
							landmark = paList.getLandmark();
							mobile = paList.getPhone();
							pat_id = paList.getPat_id();

						//}
					//}
					String placeOrder = getString(R.string.PlaceOrderDiagnoUrl);
					tList = dobean.getLabTestDataBean().getCurrentTests();
					
					//tList = labbean.getTestBean();
					String testId = "";
					entity_price = "";
					String packId = "";
					int len = tList.size();
					for (int i = 0; i < len; i++) {
						DiagnoTestDataBean tbean = new DiagnoTestDataBean();
						tbean = tList.get(i);
						if (testId.equals("")) {
							testId = String.valueOf(tbean.getTestId());
						} else {
							testId = testId + "," + tbean.getTestId();
						}
						if (entity_price.equals("")) {
							entity_price = tbean.getOklerTestPrice() + "";
						} else {
							entity_price = entity_price + ","
									+ tbean.getOklerTestPrice();
						}
					}
					if (diagnoOrderType.equals(DiagnoOrderType.PACKAGE)) {
						dpList = dobean.getLabPkgDataBean().getCurrentPkgs();
						for (int i = 0; i < dpList.size(); i++) {
							if(packId.equals("")){
								packId = String.valueOf(dpList.get(i).getPacakageId());
							}else{
								packId = packId + "," + String.valueOf(dpList.get(i).getPacakageId());
							}
							if(entity_price.equals("")){
								entity_price = dpList.get(i).getPackOklerPrice()+"";
							}else{
								entity_price = entity_price + ","
										+ dpList.get(i).getPackOklerPrice()+"";
							}
						}
						//packId = String.valueOf(dpbean.getPacakageId());
						//entity_price = String.valueOf(dpbean.getPack_oklerPrice());
					}

					lab_id = String.valueOf(labbean.getLabId());
					slot_id = dobean.getAppTimeSlotId();
					lab_city_id = String.valueOf(labbean.getCityId());
					if(lab_city_id==null)
						lab_city_id = "";
					state_lab_id = String.valueOf(labbean.getStateId());
					if (state_lab_id==null)
						state_lab_id="";
					lab_branch_id = String.valueOf(labbean.getBranchId());
					if(lab_branch_id==null)
						lab_branch_id="";
					ap_date = dobean.getApptDt();
					disc = String.valueOf(discount);
					// okler_disc_value_tv.getText().toString();
					String tax1 = String.valueOf(tax);
					// tax_value_tv.getText().toString();
					String amounttxt = String.valueOf(amount);
					// amount_value_tv.getText().toString();
					String oklerPrice = String.valueOf(net_pay);
					// net_pay_value_tv.getText().toString();
					String homeCharge = String.valueOf(home_visit);
					// shipping_charg_value_tv.getText().toString();
					String cCode = coupon_code.getText().toString();
					String cDisc = String.valueOf(redAmt);
					// coupon_disc_value_tv.getText().toString();

					String name1 = "", address1 = "", address2 = "", landmark1 = "", cCode1 = "";
					try {
						name1 = URLEncoder.encode(name, "UTF-8");
						address1 = URLEncoder.encode(addr1, "UTF-8");
						address2 = URLEncoder.encode(adr2, "UTF-8");
						landmark1 = URLEncoder.encode(landmark, "UTF-8");
						cCode1 = URLEncoder.encode(cCode, "UTF-8");

					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						sched_appoint.setEnabled(true);
						e1.printStackTrace();
					}

					mainUrl = placeOrder + userId + "&lab_id=" + lab_id
							+ "&slot_id=" + slot_id + "&lab_city_id="
							+ lab_city_id + "&pat_id=" + pat_id
							+ "&lab_state_id=" + state_lab_id
							+ "&lab_branch_id=" + lab_branch_id
							+ "&lab_booked_date=" + ap_date + "&pack_id="
							+ packId + "&test_id=" + testId
							+ "&okler_discount=" + disc + "&tax=" + tax1
							+ "&entity_price=" + entity_price + "&amount="
							+ amounttxt + "&txn_amount=" + oklerPrice
							+ "&okler_discount_amt=" + disc
							+ "&home_visit_charges=" + homeCharge
							+ "&pickup_type=" + pickup + "&name=" + name1
							+ "&dob=" + dob + "&addr1=" + address1 + "&addr2="
							+ address2 + "&city=" + city_id + "&state="
							+ state_id + "&zip=" + zip + "&landmark="
							+ landmark1 + "&ccode=" + cCode1
							+ "&coupon_discount=" + cDisc
							+ "&mobile=" + mobile;
					if(redAmt>0)
						mainUrl = mainUrl+getString(R.string.coupon_type)+2;
					String uri = mainUrl;
					Log.d("MainUrl", mainUrl);
					
					if(net_pay>0){
					WebJsonObjectRequest wjson = new WebJsonObjectRequest(
							Method.GET, mainUrl, new JSONObject(),
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									if (response
											.optString("message")
											.equals("lab booking inserted successfully")) {
										JSONObject jobj = (JSONObject) response;
										try {
											JSONObject jobj2 = jobj
													.getJSONObject("result");
											result = jobj2.optString("id");

											UsersDataBean ubean = Utilities
													.getCurrentUserFromSharedPref(DiagnoOrderSummary.this);
											int cust_id = ubean.getId();
											String customer_name = ubean
													.getFname();
											String email = ubean.getEmail();
											String salutation = ubean
													.getSalutation();

											String time = dobean.getAppTime();
											time = URLEncoder.encode(time,
													"UTF-8");
											String lab_name = labbean.getLab_name();
													
											lab_name = URLEncoder.encode(
													lab_name, "UTF-8");
											customer_name = URLEncoder.encode(
													customer_name, "UTF-8");

											// cust_id=1&salutation=Mr&customer_name=Mark&email=testmail@gmail.com&appointment_date=2016/03/03&time=4pm&rs=100&relation=father&pick_up_type=home&book_order=123&lab_name=metro
											AddressDataBean abean = new AddressDataBean();
											String order_diagno_email = getResources()
													.getString(
															R.string.serverUrl)
													+ getResources()
															.getString(
																	R.string.diagno_booking_conform_url)
													+ "cust_id="
													+ cust_id
													+ "&salutation="
													+ salutation
													+ "&customer_name="
													+ customer_name
													+ "&email="
													+ email
													+ "&appointment_date="
													+ ap_date
													+ "&time="
													+ time
													+ "&rs="
													+ entity_price
													+ "&relation="
													+ relation
													+ "&pick_up_type="
													+ pickup
													+ "&book_order="
													+ result
													+ "&lab_name=" + lab_name;

											WebJsonObjectRequest webObjReq = new WebJsonObjectRequest(
													Method.GET,
													order_diagno_email,
													new JSONObject(),
													new Response.Listener<JSONObject>() {

														@Override
														public void onResponse(
																JSONObject response) {
															Log.e("diagno_mail",
																	"mail sent");
														}

													},
													new Response.ErrorListener() {

														@Override
														public void onErrorResponse(
																VolleyError error) {

															Log.e("error",
																	error.getStackTrace()
																			.toString());
															// Log.e("Error",
															// new
															// String(error.networkResponse.data));
															Log.e("diagno_mail",
																	"mail not sent");

														}

													}, true);

											VolleyRequest.addJsonObjectRequest(
													getApplicationContext(),
													webObjReq);

										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											sched_appoint.setEnabled(true);
											// Toast.makeText(ack,
											// "Some error Ocurred while booking",
											// Toast.LENGTH_LONG).show();
										} catch (UnsupportedEncodingException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											sched_appoint.setEnabled(true);
										}

										// Toast.makeText(ack, result,
										// Toast.LENGTH_LONG).show();
										Intent intent = new Intent(
												getApplicationContext(),
												DiagnoOrderDetails.class);
										intent.putExtra("PickupType",
												pickupType);
										intent.putExtra("BookingId", result);
										startActivity(intent);
									} else {
										Toast.makeText(
												ack,
												"Some error Ocurred while booking",
												Toast.LENGTH_LONG).show();
										sched_appoint.setEnabled(true);
									}

								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									 Log.e("Error",
												 new
												 String(error.networkResponse.data));
									Toast.makeText(ack,
											"Some error Ocurred while booking",
											Toast.LENGTH_LONG).show();
									sched_appoint.setEnabled(true);

								}
							});
					if(VolleyRequest.addJsonObjectRequest(ack, wjson))
						sched_appoint.setEnabled(false);
					else
						sched_appoint.setEnabled(true);
					}else{
						Toast.makeText(ack, "Please select atleast one test/package", Toast.LENGTH_SHORT).show();
						sched_appoint.setEnabled(true);
					}
				}
			}
		});

		/*
		 * if(isFromOrder) sched_appoint.setVisibility(View.INVISIBLE);
		 */

		customLab = new CustomViewSelectedLabs(getApplicationContext());
		TextView labName = (TextView) customLab.findViewById(R.id.testNameTv);
		labName.setText(labbean.getLab_name());
		customLab.setLayoutParams(params);
		LL_for_selected_labs.addView(customLab);

		setTestUi();

		atValue.setText(pickupType);
		dateValue.setText(dobean.getApptDt());
		timeValue.setText(dobean.getAppTime());
		/*
		 * title_Coupon = (TextView)findViewById(R.id.title_Coupon);
		 * title_Coupon.setText();
		 */

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// setTestUi();
	}

	public void setUIForOrder() {
		labbean = dobean.getLabTestDataBean().getCurrentLab();
		// ArrayList<TestDataBean> tList = new ArrayList<TestDataBean>();

		tList = dobean.getLabTestDataBean().getCurrentTests();
		dpList = dobean.getLabPkgDataBean().getCurrentPkgs();
	}

	public void setTestUi() {
		// DiagnoOrderDataBean dobean = new DiagnoOrderDataBean();
		// dobean = Okler.getInstance().getDiagnoOrderDataBean();
		// DiagnoLabsDataBean labbean = new DiagnoLabsDataBean();

	//	labbean = dobean.getLabTestDataBean().getCurrentLab();
		// ArrayList<TestDataBean> tList = new ArrayList<TestDataBean>();

		tList = dobean.getLabTestDataBean().getCurrentTests();
		dpList = dobean.getLabPkgDataBean().getCurrentPkgs();

		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params2.setMargins(0, 10, 0, 0);

		if (diagnoOrderType.equals(DiagnoOrderType.TEST)) {
			int length = tList.size();
			CustomViewSelectedTests customTest[] = new CustomViewSelectedTests[length];
			amount = 0.0f;
			discount = 0.0f;
			tax = 0.0f;
			net_pay = 0.0f;
			home_visit = 0.0f;
			LL_for_selected_tests.removeAllViews();
			for (int i = 0; i < length; i++) {

				customTest[i] = new CustomViewSelectedTests(ack);
				// customTest[i].setTag(""+i+50);
				TextView testNameTv = (TextView) customTest[i]
						.findViewById(R.id.testNameTv);
				TextView priceTv = (TextView) customTest[i]
						.findViewById(R.id.priceTv);
				ImageView deleteImage = (ImageView) customTest[i]
						.findViewById(R.id.deleteImage);
				deleteImage.setTag("" + i);

				/*
				 * if(length==1){ deleteImage.setVisibility(View.INVISIBLE); }
				 */
				// deleteImage.setTag(""+i+100);
				DiagnoTestDataBean tb = new DiagnoTestDataBean();
				tb = tList.get(i);
				testNameTv.setText(tb.getTestname());
				priceTv.setText("Rs." + tb.getOklerTestPrice());
				customTest[i].setLayoutParams(params2);
				LL_for_selected_tests.addView(customTest[i]);
				deleteImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						

						// TODO Auto-generated method stub
						final int id = Integer.parseInt(String.valueOf(v.getTag()));
						int viewid = v.getId();
						//int id/* = viewid - 1000*/;
						final AlertDialog alertDialog = new AlertDialog.Builder(ack).create();
				alertDialog.setTitle("Alert");
				alertDialog.setMessage("Are you sure, you want to delete the selected test");
				alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
								
								// id = id -100;
								tList.remove(id);
								// dobean.getSelectedLab().setSelectedTest(tList);
								dobean.getLabTestDataBean().setCurrentTests(tList);
								//dobean.setSelectedLab(labbean);
								Okler.getInstance().setDiagnoOrder(dobean);
								amount_value_tv.setText("");
								okler_disc_value_tv.setText("");
								shipping_charg_value_tv.setText("");
								tax_value_tv.setText("");
								net_pay_value_tv.setText("");
								coupon_disc_value_tv.setText("");

								setTestUi();
							}
						});

				alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});

				alertDialog.show();
					}
				});
				if (isFromOrder)
					deleteImage.setVisibility(View.INVISIBLE);
				else {
					amount = amount + (tb.getMarketPrice());
					
					net_pay = net_pay + (tb.getOklerTestPrice());
				}
				
			}tax = dobean.getTax();

			if (isFromOrder) {
				amount = dobean.getMrp();
				tax = dobean.getTax();
				net_pay = (dobean.getNetPayable());
				discount = (dobean.getYouSaveRs());
				amount_value_tv.setText("Rs." + amount);
				okler_disc_value_tv.setText("Rs." + discount);
				tax_value_tv.setText("Rs." + tax);
				net_pay_value_tv.setText("Rs." + net_pay);
				coupon_disc_value_tv.setText("Rs." + redAmt);
				if (pickupType.equals("Home Visit")) {
					shipping_charg_RL.setVisibility(View.VISIBLE);
					shipping_charg_tv.setText("Home Visit Charges");
					home_visit = 150;
					shipping_charg_value_tv.setText("Rs." + home_visit);
					// net_pay = net_pay+home_visit;
					// net_pay_value_tv.setText(""+net_pay);
				}
			} else {
				discount = amount - net_pay;
				amount_value_tv.setText("Rs." + amount);
				okler_disc_value_tv.setText("Rs." + discount);
				tax_value_tv.setText("Rs." + tax);
				net_pay_value_tv.setText("Rs." + net_pay);
				coupon_disc_value_tv.setText("Rs." + redAmt);
			}
			if (pickupType.equals("Home")) {
				shipping_charg_RL.setVisibility(View.VISIBLE);
				shipping_charg_tv.setText("Home Visit Charges");
				
				if(amount>0)
					home_visit = 150;
				else 
					home_visit = 0;
				shipping_charg_value_tv.setText("Rs." + home_visit);
				net_pay = net_pay + home_visit;
				net_pay_value_tv.setText("Rs." + net_pay);
				coupon_disc_value_tv.setText("Rs." + redAmt);
			}
		} else {
			int len2 = dpList.size();
			CustomViewSelectedTests customPkg[] = new CustomViewSelectedTests[len2];
			LL_for_selected_tests.removeAllViews();
			for (int i = 0; i < len2; i++) {
				customPkg[i] = new CustomViewSelectedTests(ack);
				
				TextView testNameTv2 = (TextView) customPkg[i]
						.findViewById(R.id.testNameTv);
				TextView priceTv2 = (TextView) customPkg[i].findViewById(R.id.priceTv);
				ImageView deleteImage2 = (ImageView) customPkg[i]
						.findViewById(R.id.deleteImage);
				if(isFromOrder)
				deleteImage2.setVisibility(View.INVISIBLE);
				deleteImage2.setTag(""+i);

				testNameTv2.setText(dpList.get(i).getPackageName());
				priceTv2.setText("Rs." + dpList.get(i).getPackOklerPrice());
				customPkg[i].setLayoutParams(params2);
				LL_for_selected_tests.addView(customPkg[i]);

				deleteImage2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final int id = Integer.parseInt(String.valueOf(v.getTag()));
						int viewid = v.getId();
						//int id/* = viewid - 1000*/;
						final AlertDialog alertDialog = new AlertDialog.Builder(ack).create();
				alertDialog.setTitle("Alert");
				alertDialog.setMessage("Are you sure, you want to delete the selected package");
				alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dpList.remove(id);
								dobean.getLabPkgDataBean().setCurrentPkgs(dpList);
								//dobean.setSelectedLab(labbean);
								Okler.getInstance().setDiagnoOrder(dobean);
								amount_value_tv.setText("");
								okler_disc_value_tv.setText("");
								shipping_charg_value_tv.setText("");
								tax_value_tv.setText("");
								net_pay_value_tv.setText("");
								coupon_disc_value_tv.setText("");
								
								amount=0.00f;
								discount=0.00f;
								tax=0.00f;
								net_pay=0.00f;
								home_visit = 0.00f;

								setTestUi();
							}
						});

				alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});

				alertDialog.show();
					
						}
				});
				if (!isFromOrder) {
					amount = amount + dpList.get(i).getPackMrp();
					net_pay = net_pay + dpList.get(i).getPackOklerPrice(); 
					
				}
				if (isFromOrder) {
					amount = (dobean.getMrp());
					tax = tax+0;
					net_pay = (dobean.getNetPayable());
					amount_value_tv.setText("Rs." + amount);
					okler_disc_value_tv.setText("Rs." + (amount - net_pay));
					tax_value_tv.setText("Rs." + tax);
					net_pay_value_tv.setText("Rs." + net_pay);
					coupon_disc_value_tv.setText("Rs." + redAmt);
					
				}
			}
			amount_value_tv.setText("Rs." + amount);
			discount = amount - net_pay;
			okler_disc_value_tv.setText("Rs."+ discount);
			tax_value_tv.setText("Rs." + tax);
			net_pay_value_tv.setText("Rs." + net_pay);
			coupon_disc_value_tv.setText("Rs." + redAmt);
			if (pickupType.equals("Home")) {
					shipping_charg_RL.setVisibility(View.VISIBLE);
					shipping_charg_tv.setText("Home Visit Charges");
					if(amount>0)
					home_visit = 150;
					else 
						home_visit = 0;
					shipping_charg_value_tv.setText("Rs." + home_visit);
					net_pay = net_pay + home_visit;
					net_pay_value_tv.setText("Rs." + net_pay);
					coupon_disc_value_tv.setText("Rs." + redAmt);
			}
			if(pickupType.equals("Home Visit")){
				shipping_charg_RL.setVisibility(View.VISIBLE);
				shipping_charg_tv.setText("Home Visit Charges");
				if(amount>0)
					home_visit = 150;
					else 
						home_visit = 0;
					shipping_charg_value_tv.setText("Rs." + home_visit);
			}

			
		}
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

	public static void btnDisable() {
		sched_appoint.setVisibility(View.GONE);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {

	}
}
