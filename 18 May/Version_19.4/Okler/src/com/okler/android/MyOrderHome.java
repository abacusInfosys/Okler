package com.okler.android;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.okler.adapters.MyOrderDiagnoOrderAdapter;
import com.okler.adapters.MyOrdersAdapter;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.DiagnoLabsDataBean;
import com.okler.databeans.DiagnoOrderDataBean;
import com.okler.databeans.DiagnoPackageDataBean;
import com.okler.databeans.OrdersDataBean;
import com.okler.databeans.PhysioAndMedicalBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.TestDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.diagnostics.DiagnoLabsAvailable;
import com.okler.diagnostics.DiagnoOrderSummary;
import com.okler.dialogs.PhysiomedDialog;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;
import com.okler.network.*;
import com.okleruser.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.provider.Telephony.Sms.Conversations;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyOrderHome extends BaseActivity implements OnScrollListener {
	private View bottomBarLayout;
	private Toolbar toolBar;
	private Context context;
	ImageView imgBack;
	ArrayList<OrdersDataBean> odtBeanArr;
	ListView myOrderList;
	String myOrderUrl;
	int get;
	int pos = 0;
	int intentValue;
	ArrayList<PhysioAndMedicalBean> physio;
	ArrayList<DiagnoOrderDataBean> diagnoOdtBeanArr;
	JSONObject addressobj;
	Button toolbarcount;
	MyOrdersAdapter ordAdapter;
	LinearLayout progressLinLayout;
	boolean wsCalled = false, fromOrder = false, isAllNurse = false,
			isAllAtt = false, isCalledOnce = false;
	String mURL = "";
	MyOrderDiagnoOrderAdapter diagnoAdp;
	int pageNo = 0, nursepgno = 0, attpgno = 0;
	String myPhysioUrl, myMedUrl, digno_url;
	ArrayList<ProductDataBean> parray = new ArrayList<ProductDataBean>();
	int totalResultsFromWebService = 0, nurseTotal = 0, attendantTotal = 0;
	int pageCount = 0, order_id = 0, nursepgcnt = 0, attpgcnt = 0;
	UsersDataBean ubean;
	int cust_id, service_type;
	Activity ack;
	TextView text_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order_home);
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		toolBar.setBackgroundColor(Color.BLUE);
		ack = this;
		toolbarcount = (Button) findViewById(R.id.toolBarCount);
		toolbarcount.setVisibility(View.GONE);
		progressLinLayout = (LinearLayout) findViewById(R.id.progressLinLayout);
		fromOrder = getIntent().getBooleanExtra("fromOrder", false);
		context = getApplicationContext();
		setSupportActionBar(toolBar);
		final ActionBar ab = getSupportActionBar();
		myOrderList = (ListView) findViewById(R.id.myOrderList);
		myOrderList.setOnScrollListener(this);
		ubean = Utilities.getCurrentUserFromSharedPref(this);
		cust_id = ubean.getId();
		// myPhysioUrl =
		// "http://183.82.110.105:8081/oklerapi//nurse/GetAllservicesByuser?user_id="+cust_id+"&service_type=";
		// myMedUrl =
		// "http://183.82.110.105:8081/oklerapi/order/getorders?cust_id="+cust_id+"&order_id="+"&page=";
		myMedUrl = setMedUrl(cust_id, order_id, pageNo);
		// digno_url =
		// "http://183.82.110.105:8081/oklerapi/lab/retrivelabbook?cust_id="+cust_id;
		digno_url = setDiagnoUrl(cust_id, order_id, pageNo);

		myOrderUrl = "";

		get = getIntent().getIntExtra("value", -1);
		odtBeanArr = new ArrayList<OrdersDataBean>();
		text_name = (TextView) findViewById(R.id.my_order_text);
		physio = new ArrayList<PhysioAndMedicalBean>();
		diagnoOdtBeanArr = new ArrayList<DiagnoOrderDataBean>();
		// for medicines
		// myOrderUrl= myMedUrl;

		// for other services
		// myOrderUrl= myPhysioUrl;
		Utilities.writeToLogFIle("Physio and diagno");

		Utilities.setTitleText(toolBar, "My Orders");
		Utilities.writeToLogFIle("My order URL set title");
		// myOrderList.setFocusable(false);

		myOrderList.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), "on item sel",
				// Toast.LENGTH_LONG).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), "on item not sel",
				// Toast.LENGTH_LONG).show();
			}
		});
		myOrderList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if (get == 2) {
					// If diagno order - then stat new activity
					Intent startDiagno = new Intent(context,
							DiagnoOrderSummary.class);
					startDiagno.putExtra("isFromOrder", true);
					startDiagno.putExtra("position", position);
					startActivity(startDiagno);
				} else {
					pos = myOrderList.getSelectedItemPosition();
					Intent startOrderDetails;
					PhysioAndMedicalBean physioMedBean = new PhysioAndMedicalBean();
					Gson gson;
					String aBean;

					switch (get) {
					case 1:

						intentValue = 1;
						startOrderDetails = new Intent(context,
								MyOrderMed_Hs_Details.class);
						startOrderDetails.putExtra("intent_value", intentValue);
						startOrderDetails.putExtra("position", position);
						Okler.getInstance().setPostion(position);
						startActivity(startOrderDetails);

						break;

					case 2:

						intentValue = 2;
						startOrderDetails = new Intent(context,
								MyOrderMed_Hs_Details.class);
						startOrderDetails.putExtra("intent_value", intentValue);
						startOrderDetails.putExtra("position", pos);
						Okler.getInstance().setPostion(position);
						startActivity(startOrderDetails);

						break;

					case 3:
						physioMedBean = physio.get(position);
						gson = new Gson();
						aBean = gson.toJson(physioMedBean);

						startOrderDetails = new Intent(context,
								MyOrderMS_History.class);
						startOrderDetails.putExtra("value", 4);
						startOrderDetails.putExtra("position", pos);
						startOrderDetails.putExtra("physiobean", aBean);
						Okler.getInstance().setPostion(position);
						startActivity(startOrderDetails);

						break;

					case 4:
						physioMedBean = physio.get(position);
						gson = new Gson();
						aBean = gson.toJson(physioMedBean);

						startOrderDetails = new Intent(context,
								MyOrderMS_History.class);
						startOrderDetails.putExtra("value", 3);
						startOrderDetails.putExtra("position", pos);
						startOrderDetails.putExtra("physiobean", aBean);
						Okler.getInstance().setPostion(position);
						startActivity(startOrderDetails);

						break;
					}
				}
			}

		});
		// webService();

		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		if (get == 2) {
			Utilities.writeToLogFIle("get =2 ");
			diagnoAdp = new MyOrderDiagnoOrderAdapter(context, diagnoOdtBeanArr);
			myOrderList.setAdapter(diagnoAdp);
			Utilities.writeToLogFIle("my ord lst");
		} else {
			Utilities.writeToLogFIle("get != 2 ");
			ordAdapter = new MyOrdersAdapter(context, odtBeanArr);
			myOrderList.setAdapter(ordAdapter);
			ordAdapter.notifyDataSetChanged();
			Utilities.writeToLogFIle("My ord lisr ewlse");
		}

		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (fromOrder) {
					Intent intent = new Intent(MyOrderHome.this,
							ServiceCategoryActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}
				finish();
			}

		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showProgress(false);
		Utilities.writeToLogFIle("in on resume");
		switch (get) {
		case 1:
			text_name.setText("MEDICINES & HEALTHSHOP");
			odtBeanArr.clear();
			ordAdapter.notifyDataSetChanged();
			myOrderUrl = myMedUrl;
			webService(myMedUrl);

			break;

		case 2:
			text_name.setText("DIAGNOSTIC SERVICES");
			diagnoOdtBeanArr.clear();
			diagnoAdp.notifyDataSetChanged();
			Utilities.writeToLogFIle("in on resume get 2");
			myOrderUrl = digno_url;
			webService(digno_url);

			break;

		case 3:
			text_name.setText("MEDICAL SERVICES");
			odtBeanArr.clear();
			ordAdapter.notifyDataSetChanged();

			if (!isAllNurse) {
				service_type = 2;
				nursepgno = 0;
				attpgno = 0;
				mURL = setMedPhysioUrl(cust_id, order_id, nursepgno,
						service_type);
				webService(mURL);
			}

			break;

		case 4:
			text_name.setText("PHYSIOTHERAPY");
			odtBeanArr.clear();
			ordAdapter.notifyDataSetChanged();
			pageNo = 0;
			myOrderUrl = setMedPhysioUrl(cust_id, order_id, pageNo, 3);
			webService(myOrderUrl);

			// myPhysioUrl+3;
			break;

		default:
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_order_home, menu);
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

	public void webService(String myOrderUr) {
		showProgress(false);
		mURL = myOrderUr;
		Utilities.writeToLogFIle("in web service. Order URL" + myOrderUr);
		WebJsonObjectRequest alJson = new WebJsonObjectRequest(Method.GET,
				mURL, new JSONObject(), new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Utilities
								.writeToLogFIle("in web service. Order URLin on response");
						showProgress(false);
						Utilities
								.writeToLogFIle("in web service. Order URL after show progress");
						JSONObject ajobj = (JSONObject) response;
						Utilities
								.writeToLogFIle("in web service. Order URL after show progress. response"
										+ ajobj.toString());
						JSONArray ajobj1 = new JSONArray();
						JSONObject ajobj2 = new JSONObject();
						JSONObject ajobj3 = new JSONObject();
						JSONArray ajArray = new JSONArray();
						try {

							if (ajobj.getString("message").equals(
									"No data found.")) {
								Utilities
										.writeToLogFIle("in web service. in if no data found");
								if (get == 3 || get == 4)
									if (physio.size() <= 0)
										Toast.makeText(MyOrderHome.this,
												"You dont have any order ...",
												Toast.LENGTH_LONG).show();

								Utilities
										.writeToLogFIle("in web service. physio"
												+ physio.size());

							} else {
								if (get == 1) {
									// ajobj2 = ajobj.getJSONObject("result");
									Utilities
											.writeToLogFIle("in web service. get ==1");
								} else {
									ajobj1 = ajobj.getJSONArray("result");
									for (int tempi = 0; tempi < ajobj1.length(); tempi++)
										Utilities.writeToLogFIle("in web service. ajobj"
												+ ajobj1.getString(tempi));
								}

								switch (get) {
								case 1:
									Utilities
											.writeToLogFIle("in web service. in case1");
									setMedHSOrders(ajobj);
									Utilities
											.writeToLogFIle("in web service. in case1 set called");
									break;

								case 2:
									Utilities
											.writeToLogFIle("in web service. in case2");
									setDiagnoOrder(ajobj);
									Utilities
											.writeToLogFIle("in web service. in case2 set called");
									break;
								case 3:
									Utilities
											.writeToLogFIle("in web service. in case3");
									setMedServies(ajobj);
									Utilities
											.writeToLogFIle("in web service. in case3 set called");
									/*
									 * if(!isAllAtt){ if(!wsCalled) {
									 * 
									 * wsCalled=true; webService(myOrderUrl);
									 * 
									 * } }
									 */
									break;

								case 4:
									Utilities
											.writeToLogFIle("in web service. in case4");
									setPhysioOrders(ajobj1);
									Utilities
											.writeToLogFIle("in web service. in case4 set called");
									break;
								}
								Utilities
										.writeToLogFIle("in web service. before notify ds changed");
								// odtBean.setPrice(ajobj2.optString("total"));
								ordAdapter.notifyDataSetChanged();
								Utilities
										.writeToLogFIle("in web service. in case2");

							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							String resp2 = String.valueOf(e);
							// Toast.makeText(getApplicationContext(), resp2,
							// Toast.LENGTH_LONG).show();
							e.printStackTrace();
							Utilities
									.writeToLogFIle("in web service. in exception"
											+ e.getStackTrace());
							showProgress(false);
						}
						Okler.getInstance().setUsersOrders(odtBeanArr);
						Utilities
								.writeToLogFIle("in web service. set users orders");
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						showProgress(false);
						Utilities.writeToLogFIle("in web service. in case2"
								+ error.getLocalizedMessage());
					}
				});

		Utilities.writeToLogFIle("in web service. before set cache");
		// put that response in odtBean
		alJson.setShouldCache(false);
		Utilities.writeToLogFIle("in web service. after set cache");
		DefaultRetryPolicy defRetryPol = new DefaultRetryPolicy(10000, 2, 1);
		Utilities.writeToLogFIle("in web service. after retry policy");
		alJson.setRetryPolicy(defRetryPol);
		Utilities.writeToLogFIle("in web service. afteer set policy");
		if (VolleyRequest.addJsonObjectRequest(getApplicationContext(), alJson)) {
			showProgress(true);
			Utilities.writeToLogFIle("in web service. web service called");
			// Toast.makeText(getApplicationContext(), "got response",
			// Toast.LENGTH_LONG).show();
		} else {
			showProgress(false);
			// Toast.makeText(getApplicationContext(), "value not found",
			// Toast.LENGTH_LONG).show();
			Utilities.writeToLogFIle("in web service. no netwok");
		}

	}

	private void setMedServies(JSONObject ajobj) {
		try {
			JSONArray ajobj1 = ajobj.getJSONArray("result");
			JSONObject ajobj2;

			if (service_type == 2) {
				nurseTotal = ajobj.optInt("service_booking_count");
				nursepgcnt = ajobj.optInt("page_count");
				nursepgno = ajobj.optInt("current_page");
				// attendantTotal = 0;
			}

			else {
				attendantTotal = ajobj.optInt("service_booking_count");
				attpgcnt = ajobj.optInt("page_count");
				attpgno = ajobj.optInt("current_page");
				totalResultsFromWebService = nurseTotal + attendantTotal;
				// nurseTotal = 0;
			}

			for (int ai = 0; ai < ((ajobj1.length())); ai++) {
				Log.i("in loop", "in loop");

				ajobj2 = ajobj1.getJSONObject(ai);

				OrdersDataBean odtBean = new OrdersDataBean();
				PhysioAndMedicalBean pnmBean = new PhysioAndMedicalBean();

				odtBean = new OrdersDataBean();
				odtBean.setDate(ajobj2.optString("start_date"));
				odtBean.setOrderId(ajobj2.optString("booking_id"));
				odtBean.setOrderStatus(ajobj2.optString("booking_status_name"));

				pnmBean.setfrom(ajobj2.optString("start_date"));
				pnmBean.setto(ajobj2.optString("end_date"));
				pnmBean.setOrder_id(ajobj2.optString("booking_id"));
				pnmBean.setBooking_id(ajobj2.optString("booking_id"));
				pnmBean.setBooking_status(ajobj2
						.optString("booking_status_name"));
				addressobj = ajobj2.getJSONObject("address_details");
				pnmBean.setCity(ajobj2.optString("city_name"));
				pnmBean.setFirstname(addressobj.optString("firstname"));
				pnmBean.setsurname(addressobj.optString("lastname"));
				pnmBean.setAddress(addressobj.optString("street_address"));
				pnmBean.setRelation(ajobj2.optString("booking_for_relation"));
				pnmBean.setService(ajobj2.optString("service_name"));
				pnmBean.setBooking_status(ajobj2
						.optString("booking_status_name"));
				odtBeanArr.add(odtBean);
				physio.add(pnmBean);
			}
		} catch (Exception ex) {

		}
	}

	private void setPhysioOrders(JSONArray ajobj1) {
		Utilities.writeToLogFIle("in set physio odr");
		try {
			JSONObject ajobj2;
			Utilities.writeToLogFIle("in set physio odr in try");
			for (int ai = 0; ai < ((ajobj1.length())); ai++) {
				Log.i("in loop", "in loop");
				Utilities.writeToLogFIle("in set physio odr. in loop");
				ajobj2 = ajobj1.getJSONObject(ai);
				Utilities.writeToLogFIle("in set physio odr. got ajobj2");
				OrdersDataBean odtBean = new OrdersDataBean();
				PhysioAndMedicalBean pnmBean = new PhysioAndMedicalBean();
				Utilities.writeToLogFIle("in set physio odr. got objects");
				odtBean = new OrdersDataBean();
				odtBean.setDate(ajobj2.optString("start_date"));
				Utilities.writeToLogFIle("in set physio odr. date set");
				odtBean.setOrderId(ajobj2.optString("booking_id"));
				Utilities.writeToLogFIle("in set physio odr. order id set");
				odtBean.setOrderStatus(ajobj2.optString("booking_status_name"));
				Utilities
						.writeToLogFIle("in set physio odr. booking status set");
				pnmBean.setfrom(ajobj2.optString("start_date"));
				Utilities.writeToLogFIle("in set physio odr. start date set");
				pnmBean.setto(ajobj2.optString("end_date"));
				Utilities.writeToLogFIle("in set physio odr. end set");
				pnmBean.setOrder_id(ajobj2.optString("booking_id"));
				Utilities.writeToLogFIle("in set physio odr. booking id set");
				pnmBean.setBooking_id(ajobj2.optString("booking_id"));
				addressobj = new JSONObject();
				try {

					addressobj = ajobj2.optJSONObject("address_details");
				} catch (Exception e) {
					Utilities.writeToLogFIle("in set physio odr. date set"
							+ e.getStackTrace());
				}
				if (addressobj == null)
					addressobj = new JSONObject();
				if (addressobj.length() > 0) {
					pnmBean.setCity(ajobj2.optString("city_name"));
					Utilities
							.writeToLogFIle("in set physio odr. city name set");
					pnmBean.setFirstname(addressobj.optString("firstname"));
					Utilities
							.writeToLogFIle("in set physio odr. first name set");
					pnmBean.setsurname(addressobj.optString("lastname"));
					Utilities.writeToLogFIle("in set physio odr. lastnmae set");
					pnmBean.setAddress(addressobj.optString("street_address"));
					Utilities
							.writeToLogFIle("in set physio odr. street addr set");
				}
				pnmBean.setRelation(ajobj2.optString("booking_for_relation"));
				Utilities.writeToLogFIle("in set physio odr. relation set");
				pnmBean.setService(ajobj2.optString("service_name"));
				Utilities.writeToLogFIle("in set physio odr. service set");
				pnmBean.setBooking_status(ajobj2
						.optString("booking_status_name"));
				Utilities
						.writeToLogFIle("in set physio odr. booking status set");
				odtBeanArr.add(odtBean);
				physio.add(pnmBean);
			}
		} catch (Exception ex) {
			Utilities.writeToLogFIle("in set physio odr. date set"
					+ ex.getStackTrace());
		}
		ordAdapter.notifyDataSetChanged();
	}

	private void setMedHSOrders(JSONObject ajobj1) {
		try {
			Utilities.writeToLogFIle("In Set Med HS Orders");
			// JSONObject ajobj3 = ajobj1.getJSONObject("result");
			JSONArray ajobj3 = ajobj1.getJSONArray("result");
			Utilities.writeToLogFIle("In Set Med HS Orders. Results");
			JSONObject ajobj2;
			Utilities.writeToLogFIle("In Set Med HS Orders. ajboj2");
			int length = ajobj3.length() - 1;
			for (int ai = 0; ai <= length; ai++) {
				Log.i("in loop", "in loop");
				Utilities.writeToLogFIle("in loop");

				ajobj2 = ajobj3.getJSONObject(ai);

				OrdersDataBean odtBean = new OrdersDataBean();
				// PhysioAndMedicalBean pnmBean = new PhysioAndMedicalBean();

				ProductDataBean pbean = null;
				AddressDataBean add = null;
				add = new AddressDataBean();

				odtBean.setDate(ajobj2.optString("ordered_on"));
				Utilities.writeToLogFIle("In Set Med HS Orders"
						+ ajobj2.optString("ordered_on"));
				// odtBean.setOrderId(ajobj2.optString("order_number"));
				odtBean.setOrderId(ajobj2.optString("id"));
				Utilities.writeToLogFIle("In Set Med HS Orders"
						+ ajobj2.optString("id"));
				odtBean.setOrderStatus(ajobj2.optString("status"));
				Utilities.writeToLogFIle("In Set Med HS Orders"
						+ ajobj2.optString("status"));
				odtBean.setPrice(ajobj2.optString("total"));
				Utilities.writeToLogFIle("In Set Med HS Orders"
						+ ajobj2.optString("total"));
				odtBean.setShipping_charges(ajobj2
						.optString("shipping_charges"));
				Utilities.writeToLogFIle("In Set Med HS Orders"
						+ ajobj2.optString("shipping_charges"));
				totalResultsFromWebService = ajobj1.optInt("total");
				Utilities.writeToLogFIle("In Set Med HS Orders"
						+ totalResultsFromWebService);
				pageCount = ajobj1.optInt("page_count");
				pageNo = ajobj1.optInt("current_page");

				Utilities.writeToLogFIle("Page count" + pageCount);
				Utilities.writeToLogFIle("Page count" + pageNo);

				add.setShip_fname(ajobj2.optString("ship_firstname"));
				Utilities.writeToLogFIle("first name"
						+ ajobj2.optString("ship_firstname"));
				add.setShip_lname(ajobj2.optString("ship_lastname"));
				Utilities.writeToLogFIle("first name"
						+ ajobj2.optString("ship_lastname"));
				add.setShip_add1(ajobj2.optString("ship_address1"));
				Utilities.writeToLogFIle("first name"
						+ ajobj2.optString("ship_address1"));
				add.setShip_add2(ajobj2.optString("ship_address2"));
				Utilities.writeToLogFIle("first name"
						+ ajobj2.optString("ship_address2"));
				add.setShip_city(ajobj2.optString("ship_city_name"));
				Utilities.writeToLogFIle("first name"
						+ ajobj2.optString("ship_city_name"));
				add.setShip_state(ajobj2.optString("ship_state"));
				Utilities.writeToLogFIle("first name"
						+ ajobj2.optString("ship_state"));
				add.setShip_zip(ajobj2.optString("ship_zip"));
				Utilities.writeToLogFIle("first name"
						+ ajobj2.optString("ship_zip"));
				add.setBill_fname(ajobj2.optString("bill_firstname"));
				Utilities.writeToLogFIle("first name"
						+ ajobj2.optString("bill_firstname"));
				add.setBill_lname(ajobj2.optString("bill_lastname"));
				Utilities.writeToLogFIle("first name"
						+ ajobj2.optString("bill_lastname"));
				add.setBill_add1(ajobj2.optString("bill_address1"));
				Utilities.writeToLogFIle("first name"
						+ ajobj2.optString("bill_address1"));
				add.setBill_add2(ajobj2.optString("bill_address2"));
				Utilities.writeToLogFIle("first name"
						+ ajobj2.optString("bill_address2"));
				add.setBill_city(ajobj2.optString("bill_city_name"));
				Utilities.writeToLogFIle("first name"
						+ ajobj2.optString("bill_city_name"));
				add.setBill_state(ajobj2.optString("bill_state"));
				Utilities.writeToLogFIle("first name"
						+ ajobj2.optString("bill_state"));
				add.setBill_zip(ajobj2.optString("bill_zip"));
				Utilities.writeToLogFIle("first name"
						+ ajobj2.optString("bill_zip"));

				JSONArray prodlist = ajobj2.getJSONArray("order_items");
				Utilities.writeToLogFIle("length" + prodlist.length());
				parray = new ArrayList<ProductDataBean>();
				ArrayList<AddressDataBean> add2 = new ArrayList<AddressDataBean>();

				for (int j = 0; j < prodlist.length(); j++) {
					JSONObject docObj = prodlist.getJSONObject(j);

					pbean = new ProductDataBean();
					Utilities.writeToLogFIle("in for loop");
					pbean.setProdId(docObj.optInt("product_id"));
					Utilities.writeToLogFIle("product id"
							+ docObj.optInt("product_id"));
					pbean.setProdName(docObj.getString("name"));
					Utilities.writeToLogFIle("product name"
							+ docObj.optInt("name"));
					pbean.setDesc(docObj.getString("description"));
					Utilities.writeToLogFIle("product description"
							+ docObj.optInt("description"));
					pbean.setMrp(docObj.getInt("price"));
					Utilities.writeToLogFIle("product price"
							+ docObj.optInt("price"));
					pbean.setOklerPrice(docObj.getInt("saleprice"));
					Utilities.writeToLogFIle("saleprice"
							+ docObj.optInt("saleprice"));
					pbean.setDiscount(docObj.getInt("discount"));
					Utilities.writeToLogFIle("discountd"
							+ docObj.optInt("discount"));
					if (docObj.has("company_name"))
						pbean.setCompany(docObj.optString("company_name"));

					if (docObj.has("generic_val"))
						pbean.setGeneric_name(docObj.optString("generic_val"));
					if (docObj.optString("prescription_need").equals("")
							|| docObj.optString("prescription_need").equals(
									null)
							|| docObj.optString("prescription_need").equals(
									"null")) {
						pbean.setPresc_needed(0);
					} else {
						pbean.setPresc_needed(Integer.parseInt(docObj
								.optString("prescription_need")));
					}
					pbean.setTax(Float.parseFloat(docObj.optString("tax")));
					pbean.setProdType(Integer.parseInt(docObj
							.optString("pro_type")));
					if (docObj.optString("quantity").equals(null)
							|| docObj.optString("quantity").equals("null")) {
						pbean.setUnits(01);
					} else {
						pbean.setUnits(Integer.parseInt(docObj
								.optString("quantity")));
					}
					String jimg = docObj.getString("images");
					String url2;
					if (jimg.equals(null) || jimg.equals("null")
							|| jimg.equals("")) {
						url2 = "drawable://" + R.drawable.tempcuff;
					} else {
						if (jimg.contains(",")) {
							String arr1[] = jimg.split(":");
							String sec = arr1[2];
							String arr2[] = sec.split("\"");
							url2 = arr2[1];

						} else {
							url2 = jimg;
						}
					}
					String image_name = "";
					try {
						image_name = URLEncoder.encode(url2, "UTF-8");

					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pbean.setImgUrl(image_name);
					JSONObject uobj = new JSONObject();
					uobj = ajobj1.getJSONObject("productimage_url");
					pbean.setMediumUrl(uobj
							.optString("productimage_url_medium"));
					pbean.setSmallUrl(uobj.optString("productimage_url_small"));
					pbean.setThumbUrl(uobj
							.optString("productimage_url_thumbnail"));
					pbean.setClipArtUrl(uobj
							.optString("productimage_clipArt_images"));

					parray.add(pbean);
					add2.add(add);

				}

				odtBeanArr.add(odtBean);
				// physio.add(pnmBean);
				odtBean.setProd_list(parray);
				odtBean.setAddbean(add2);
				odtBean.setAddressDataBean(add);

				ArrayList<OrdersDataBean> order = new ArrayList<OrdersDataBean>();
				order.add(odtBean);

				Okler.getInstance().setUsersOrders(order);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("ERROR", String.valueOf(e));
			Log.d("ERROR", String.valueOf(e));
		}
	}

	private void setDiagnoOrder(JSONObject ajobj) {
		try {
			JSONArray ajobj1 = ajobj.getJSONArray("result");
			JSONObject ajobj2;

			totalResultsFromWebService = ajobj.optInt("total");
			pageCount = ajobj.optInt("page_count");
			pageNo = ajobj.optInt("current_page");

			for (int ai = 0; ai < ((ajobj1.length())); ai++) {
				Log.i("in loop", "in loop");

				ajobj2 = ajobj1.getJSONObject(ai);

				DiagnoOrderDataBean odtBean = new DiagnoOrderDataBean();

				ProductDataBean pbean1 = null;

				odtBean.setAp_date(ajobj2.optString("lab_booked_date"));
				// odtBean.setOrderId(ajobj2.optString("order_number"));
				odtBean.setOrderId(ajobj2.optString("book_order"));

				odtBean.setStatus(ajobj2.optString("order_status"));
				odtBean.setAmount(ajobj2.optString("amount"));
				odtBean.setTax(ajobj2.optString("tax"));
				odtBean.setOklerDiscount(ajobj2.optString("okler_discount_amt"));
				odtBean.setNetPayable(ajobj2.optString("txn_amount"));
				// odtBean.setCouponDiscount(ajobj2.optString(name))
				odtBean.setAp_time(ajobj2.optString("slot_period"));
				odtBean.setPickupType(ajobj2.optString("pickup_type"));

				// Set lab for order
				DiagnoLabsDataBean diagnoLab = new DiagnoLabsDataBean();
				diagnoLab.setLabId(ajobj2.optInt("lab_id"));
				diagnoLab.setLab_name(ajobj2.optString("lab_name"));
				diagnoLab.setLab_address("labBranchAddress");

				// Set test or package - depending on order
				JSONArray prodlist1 = ajobj2.optJSONArray("test_Array");
				if (prodlist1 != null) {
					// Set test
					ArrayList<TestDataBean> tests = new ArrayList<TestDataBean>();
					for (int testCnt = 0; testCnt < prodlist1.length(); testCnt++) {
						TestDataBean tes = new TestDataBean();
						JSONObject obj = prodlist1.getJSONObject(testCnt);
						tes.setTestname(obj.optString("test_name"));
						tes.setOklerTestPrice(obj.optInt("test_price"));
						tests.add(tes);
					}
					diagnoLab.setTestBean(tests);
				} else {
					prodlist1 = ajobj2.getJSONArray("pkg_Array");

					if (prodlist1 != null) {
						/*
						 * ArrayList<DiagnoPackageDataBean> tests = new
						 * ArrayList<DiagnoPackageDataBean>(); for(int
						 * testCnt=0;testCnt<prodlist1.length();testCnt++) {
						 */
						DiagnoPackageDataBean pck = new DiagnoPackageDataBean();
						JSONObject obj = prodlist1.getJSONObject(0);
						pck.setPackage_name(obj.optString("pkg_name"));
						pck.setPack_oklerPrice(obj.optInt("pkg_price"));
						// tests.add(pck);
						// }
						diagnoLab.setPackageBean(pck);
					}
				}

				UsersDataBean udb = new UsersDataBean();
				udb.setId(ajobj2.optInt("cust_id"));
				ArrayList<AddressDataBean> patAddr = new ArrayList<AddressDataBean>();
				AddressDataBean adr1 = new AddressDataBean();
				adr1.setAddress1(ajobj2.optString("pat_addr1"));
				adr1.setAddress2(ajobj2.optString("pat_addr2"));
				adr1.setFirstname(ajobj2.optString("pat_name"));
				adr1.setGender(ajobj2.optString("pat_gender"));
				adr1.setPhone(ajobj2.optString("pat_mobile"));
				adr1.setCity(ajobj2.optString("pat_city"));
				adr1.setZip(ajobj2.optString("pat_zip"));
				adr1.setDob(ajobj2.optString("pat_dob"));
				adr1.setRelation(ajobj2.optString("pat_relation"));
				patAddr.add(adr1);
				udb.setPatAddList(patAddr);
				odtBean.setUserBean(udb);
				odtBean.setSelectedLab(diagnoLab);

				diagnoOdtBeanArr.add(odtBean);
			}
			diagnoAdp.notifyDataSetChanged();
			Okler.getInstance().setUserDiagnoOrders(diagnoOdtBeanArr);

		} catch (Exception ex) {
		}
	}

	private void showProgress(boolean paramBoolean) {
		if (paramBoolean) {
			this.progressLinLayout.setVisibility(View.VISIBLE);
			return;
		}
		this.progressLinLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (fromOrder) {
			Intent intent = new Intent(MyOrderHome.this,
					ServiceCategoryActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

		int lastItemInScreen = firstVisibleItem + visibleItemCount;
		int size = 0;
		if ((lastItemInScreen == totalItemCount)) {

			switch (get) {
			case 1:
				size = odtBeanArr.size();
				if (totalResultsFromWebService != size) {

					if (((totalResultsFromWebService / 10) + 1) == pageNo)
						return;
					else {
						pageNo++;
						myOrderUrl = setMedUrl(cust_id, order_id, pageNo);
						webService(myOrderUrl);
					}
				}
				break;

			case 2:
				size = diagnoOdtBeanArr.size();
				if (totalResultsFromWebService != size) {
					if (((totalResultsFromWebService / 10) + 1) == pageNo)
						return;
					else {
						pageNo++;
						myOrderUrl = setDiagnoUrl(cust_id, order_id, pageNo);
						webService(myOrderUrl);
					}
				}
				break;

			case 3:
				size = odtBeanArr.size();
				if (nurseTotal > 0) {
					if (nurseTotal > size) {

						if (((nurseTotal / 10) + 1) == nursepgno) {
							isAllNurse = true;
							// return;
						}
						service_type = 2;
						nursepgno++;
						myOrderUrl = setMedPhysioUrl(cust_id, order_id,
								nursepgno, service_type);
						// wsCalled=false;
						isAllNurse = false;
						webService(myOrderUrl);
					} else {
						if (attendantTotal > 0) {

							if (((attendantTotal / 10) + 1) == attpgno) {
								isAllAtt = true;
								return;
							} else {
								// nursepgno++;
								attpgno++;
								service_type = 4;
								myOrderUrl = setMedPhysioUrl(cust_id, order_id,
										attpgno, service_type);
								// wsCalled=false;
								isAllAtt = false;
								webService(myOrderUrl);
							}
						} else {
							if (attendantTotal <= 0 && !(isCalledOnce)) {
								service_type = 4;
								myOrderUrl = setMedPhysioUrl(cust_id, order_id,
										attpgno, service_type);
								isAllAtt = false;
								isCalledOnce = true;
								webService(myOrderUrl);
							}
						}

					}
				}
				// myOrderUrl = myPhysioUrl;
				break;

			case 4:
				size = odtBeanArr.size();
				if (totalResultsFromWebService != size) {
					if (((totalResultsFromWebService / 10) + 1) == pageNo)
						return;
					else {
						pageNo++;
						myOrderUrl = setMedPhysioUrl(cust_id, order_id, pageNo,
								3);
						webService(myOrderUrl);
					}
				}
				// myOrderUrl = myPhysioUrl+3;
				break;

			default:
				break;
			}
		}
	}

	public String setMedUrl(int cust_id, int order_id, int page_no) {

		String mymedUrl = getString(R.string.serverUrl)+getString(R.string.medicine_order_url)+getString(R.string.cust_id)
				+ cust_id + getString(R.string.orderId) + order_id + getString(R.string.getProdsByGroup3) + page_no;

		return mymedUrl;

	}

	public String setDiagnoUrl(int cust_id, int order_id, int page_no) {

		String myDiagnoUrl = getString(R.string.serverUrl)+getString(R.string.diagno_order_url)+ getString(R.string.cust_id)
				+ cust_id + getString(R.string.book_order_id) + order_id + getString(R.string.getProdsByGroup3) + page_no;

		return myDiagnoUrl;
	}

	public String setMedPhysioUrl(int cust_id, int order_id, int page_no,
			int service_type) {

		String myPhysioUrl = getString(R.string.serverUrl)+getString(R.string.physio_order_url)+getString(R.string.user_id_field)+
				+ cust_id
				+ getString(R.string.service_type)
				+ service_type
				+ getString(R.string.booking_id_field)
				+ order_id +getString(R.string.getProdsByGroup3) + page_no;

		return myPhysioUrl;
	}
}
