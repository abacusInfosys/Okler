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
import com.okler.databeans.diagnobean.DiagnoLabBranchDataBean;
import com.okler.databeans.diagnobean.DiagnoLabPackageDataBean;
import com.okler.databeans.diagnobean.DiagnoLabTestDataBean;
import com.okler.databeans.diagnobean.DiagnoOrder;
import com.okler.databeans.diagnobean.DiagnoPackagesDataBean;
import com.okler.databeans.diagnobean.DiagnoTestDataBean;
//import com.okler.diagnostics.DiagnoLabsAvailable;
import com.okler.diagnostics.DiagnoOrderSummary;
import com.okler.dialogs.PhysiomedDialog;
import com.okler.enums.DiagnoOrderType;
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
import com.google.android.gms.nearby.messages.Strategy;
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
	ArrayList<PhysioAndMedicalBean> physio;
	ArrayList<DiagnoOrder> diagnoOdtBeanArr;
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
	Intent startOrderDetails;
	Gson gson;
	String aBean;
	PhysioAndMedicalBean  physioMedBean;
	OrdersDataBean odtBean;
	ArrayList<DiagnoTestDataBean> tests = new ArrayList<DiagnoTestDataBean>();
	ArrayList<DiagnoPackagesDataBean> pkgs = new ArrayList<DiagnoPackagesDataBean>();
	DiagnoLabTestDataBean dLTbean = new DiagnoLabTestDataBean();
	DiagnoLabPackageDataBean dLPbean = new DiagnoLabPackageDataBean();
	
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
		myMedUrl = setMedUrl(cust_id, order_id, pageNo);
		digno_url = setDiagnoUrl(cust_id, order_id, pageNo);
		myOrderUrl = "";
		get = getIntent().getIntExtra("value", -1);
		if(get==3 || get==4)
		{
			physio = new ArrayList<PhysioAndMedicalBean>();
			physioMedBean = new PhysioAndMedicalBean();
		}
		if(get==2)
		{
			diagnoOdtBeanArr = new ArrayList<DiagnoOrder>();
			diagnoAdp = new MyOrderDiagnoOrderAdapter(context, diagnoOdtBeanArr);
			myOrderList.setAdapter(diagnoAdp);
			Utilities.writeToLogFIle("my ord lst");
		}
		else
		{
			odtBeanArr = new ArrayList<OrdersDataBean>();
			odtBean=new OrdersDataBean();
			ordAdapter = new MyOrdersAdapter(context, odtBeanArr);
			myOrderList.setAdapter(ordAdapter);
			ordAdapter.notifyDataSetChanged();
		}
		text_name = (TextView) findViewById(R.id.my_order_text);
		Utilities.setTitleText(toolBar, "My Orders");
		myOrderList.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		myOrderList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
					pos = myOrderList.getSelectedItemPosition();
					switch (get) {
					case 1:
						odtBean=odtBeanArr.get(position);
						gson=new Gson();
						aBean=gson.toJson(odtBean);
						startOrderDetails = new Intent(context,MyOrderMed_Hs_Details.class);
						startOrderDetails.putExtra("position", position);
						startOrderDetails.putExtra("MedOrderDetails",aBean);
						startActivity(startOrderDetails);
						break;
					case 2:
						Intent startDiagno = new Intent(context,DiagnoOrderSummary.class);
						startDiagno.putExtra("isFromOrder", true);
						startDiagno.putExtra("position", position);
						startActivity(startDiagno);
						break;
					case 3:
					case 4:	
						physioMedBean = physio.get(position);
						gson = new Gson();
						aBean = gson.toJson(physioMedBean);
						startOrderDetails = new Intent(context,MyOrderMS_History.class);
						startOrderDetails.putExtra("value",get);
						startOrderDetails.putExtra("physiobean", aBean);
						startActivity(startOrderDetails);
						break;
					}
				}
		});
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		
		if(get==3 || get==4)
		{
			ordAdapter = new MyOrdersAdapter(context,physio,true);
			myOrderList.setAdapter(ordAdapter);
			ordAdapter.notifyDataSetChanged();
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
			myOrderUrl = digno_url;
			webService(digno_url);

			break;

		case 3:
			text_name.setText("MEDICAL SERVICES");
			physio.clear();
			ordAdapter.notifyDataSetChanged();

			if (!isAllNurse) {
				service_type = 2;
				nursepgno = 0;
				attpgno = 0;
				nurseTotal=0;
				attendantTotal=0;
				isCalledOnce=false;
				isAllNurse = false;
				isAllAtt = false;
				mURL = setMedPhysioUrl(cust_id, order_id, nursepgno,
						service_type);
				webService(mURL);
			}
			break;

		case 4:
			text_name.setText("PHYSIOTHERAPY");
			physio.clear();
			ordAdapter.notifyDataSetChanged();
			pageNo = 0;
			myOrderUrl = setMedPhysioUrl(cust_id, order_id, pageNo, 3);
			webService(myOrderUrl);
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
						Utilities.writeToLogFIle("in web service. Order URLin on response");
						showProgress(false);
						Utilities.writeToLogFIle("in web service. Order URL after show progress");
						JSONObject ajobj = (JSONObject) response;
						Utilities.writeToLogFIle("in web service. Order URL after show progress. response"
										+ ajobj.toString());
						JSONArray ajobj1 = new JSONArray();
						JSONObject ajobj2 = new JSONObject();
						JSONObject ajobj3 = new JSONObject();
						JSONArray ajArray = new JSONArray();
						try {
							if (ajobj.getString("message").equals(
									"No data found.")) {
								Utilities.writeToLogFIle("in web service. in if no data found");
								if (get == 3 || get == 4)
									if (physio.size() <= 0)
										Toast.makeText(MyOrderHome.this,
												"You dont have any order ...",
												Toast.LENGTH_LONG).show();
								Utilities.writeToLogFIle("in web service. physio"
												+ physio.size());
							} else {
								if (get == 1) {
									Utilities.writeToLogFIle("in web service. get ==1");
								} else {
									ajobj1 = ajobj.getJSONArray("result");
									for (int tempi = 0; tempi < ajobj1.length(); tempi++)
										Utilities.writeToLogFIle("in web service. ajobj"
												+ ajobj1.getString(tempi));
								}
								switch (get) {
								case 1:
									setMedHSOrders(ajobj);
									break;
								case 2:
									setDiagnoOrder(ajobj);
									break;
								case 3:
									setMedServies(ajobj);
									break;
								case 4:
									setPhysioOrders(ajobj1);
									break;
								}
								ordAdapter.notifyDataSetChanged();
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							String resp2 = String.valueOf(e);
							e.printStackTrace();
							Utilities.writeToLogFIle("in web service. in exception"
											+ e.getStackTrace());
							showProgress(false);
						}
						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						showProgress(false);
						Utilities.writeToLogFIle("in web service. in case2"
								+ error.getLocalizedMessage());
					}
				});
		alJson.setShouldCache(false);
		DefaultRetryPolicy defRetryPol = new DefaultRetryPolicy(10000, 2, 1);
		alJson.setRetryPolicy(defRetryPol);
		if (VolleyRequest.addJsonObjectRequest(getApplicationContext(), alJson)) {
			showProgress(true);
		} else {
			showProgress(false);
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
			}
			else {
				attendantTotal = ajobj.optInt("service_booking_count");
				attpgcnt = ajobj.optInt("page_count");
				attpgno = ajobj.optInt("current_page");
				totalResultsFromWebService = nurseTotal + attendantTotal;
			}
			for (int ai = 0; ai < ((ajobj1.length())); ai++) {
				Log.i("in loop", "in loop");
				ajobj2 = ajobj1.getJSONObject(ai);
				PhysioAndMedicalBean pnmBean = new PhysioAndMedicalBean();
				pnmBean.setfrom(ajobj2.optString("start_date"));
				pnmBean.setto(ajobj2.optString("end_date"));
				pnmBean.setOrder_id(ajobj2.optString("booking_id"));
				pnmBean.setBooking_id(ajobj2.optString("booking_id"));
				pnmBean.setBooking_status(ajobj2.optString("booking_status_name"));
				addressobj = ajobj2.getJSONObject("address_details");
				if (addressobj != null)
					if (addressobj.length() > 0) {
				pnmBean.setCity(ajobj2.optString("city_name"));
				pnmBean.setFirstname(addressobj.optString("first_name"));
				pnmBean.setsurname(addressobj.optString("last_name"));
				pnmBean.setAddress(addressobj.optString("address"));
				pnmBean.setPincode(addressobj.optString("pincode"));
				pnmBean.setphoneno(addressobj.optString("mobile"));
				pnmBean.setemail(addressobj.optString("email_id"));
					}
				pnmBean.setRelation(ajobj2.optString("booking_for_relation"));
				pnmBean.setService(ajobj2.optString("service_name"));
pnmBean.setService_required_for(ajobj2.optString("service_required_for"));
				pnmBean.setBooking_status(ajobj2.optString("booking_status_name"));
				physio.add(pnmBean);
			}
		} catch (Exception ex) {
			Log.e("error",ex.toString());
		}
	}

	private void setPhysioOrders(JSONArray ajobj1) {
		try {
			JSONObject ajobj2;
			for (int ai = 0; ai < ((ajobj1.length())); ai++) {
				Log.i("in loop", "in loop");
				ajobj2 = ajobj1.getJSONObject(ai);
				PhysioAndMedicalBean pnmBean = new PhysioAndMedicalBean();
				pnmBean.setfrom(ajobj2.optString("start_date"));
				pnmBean.setto(ajobj2.optString("end_date"));
				pnmBean.setOrder_id(ajobj2.optString("booking_id"));
				pnmBean.setBooking_id(ajobj2.optString("booking_id"));
				addressobj = new JSONObject();
				try {
					addressobj = ajobj2.optJSONObject("address_details");
				} catch (Exception e) {
					Utilities.writeToLogFIle("in set physio odr. date set"
							+ e.getStackTrace());
				}
				if (addressobj != null)
				if (addressobj.length() > 0) {
					pnmBean.setCity(ajobj2.optString("city_name"));
					pnmBean.setFirstname(addressobj.optString("first_name"));
					pnmBean.setsurname(addressobj.optString("last_name"));
					pnmBean.setAddress(addressobj.optString("address"));
					pnmBean.setphoneno(addressobj.optString("mobile"));
					pnmBean.setPincode(addressobj.optString("pincode"));
					pnmBean.setemail(addressobj.optString("email_id"));
				}
				pnmBean.setRelation(ajobj2.optString("booking_for_relation"));
				pnmBean.setService(ajobj2.optString("service_name"));
pnmBean.setService_required_for(ajobj2.optString("service_required_for"));
				pnmBean.setBooking_status(ajobj2.optString("booking_status_name"));
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
			JSONArray ajobj3 = ajobj1.getJSONArray("result");
			JSONObject ajobj2;
			int length = ajobj3.length() - 1;
			for (int ai = 0; ai <= length; ai++) {
				Log.i("in loop", "in loop");
				ajobj2 = ajobj3.getJSONObject(ai);
				OrdersDataBean odtBean = new OrdersDataBean();
				ProductDataBean pbean = null;
				AddressDataBean add = null;
				add = new AddressDataBean();
				odtBean.setDate(ajobj2.optString("ordered_on"));
				odtBean.setOrderId(ajobj2.optString("id"));
				odtBean.setOrderStatus(ajobj2.optString("status"));
				odtBean.setPrice(ajobj2.optString("total"));
				odtBean.setShipping_charges(ajobj2.optString("shipping_charges"));
				totalResultsFromWebService = ajobj1.optInt("total");
				pageCount = ajobj1.optInt("page_count");
				pageNo = ajobj1.optInt("current_page");
				add.setShip_fname(ajobj2.optString("ship_firstname"));
				add.setShip_lname(ajobj2.optString("ship_lastname"));
				add.setShip_add1(ajobj2.optString("ship_address1"));
				add.setShip_add2(ajobj2.optString("ship_address2"));
				add.setShip_city(ajobj2.optString("ship_city_name"));
				add.setShip_state(ajobj2.optString("ship_state"));
				add.setShip_zip(ajobj2.optString("ship_zip"));
				add.setBill_fname(ajobj2.optString("bill_firstname"));
				add.setBill_lname(ajobj2.optString("bill_lastname"));
				add.setBill_add1(ajobj2.optString("bill_address1"));
				add.setBill_add2(ajobj2.optString("bill_address2"));
				add.setBill_city(ajobj2.optString("bill_city_name"));
				add.setBill_state(ajobj2.optString("bill_state"));
				add.setBill_zip(ajobj2.optString("bill_zip"));
				add.setPreferred_del_time(ajobj2.optString("ship_delivery_slots_timings"));
				JSONArray prodlist = ajobj2.getJSONArray("order_items");
				parray = new ArrayList<ProductDataBean>();
				ArrayList<AddressDataBean> add2 = new ArrayList<AddressDataBean>();
				for (int j = 0; j < prodlist.length(); j++) {
					JSONObject docObj = prodlist.getJSONObject(j);
					pbean = new ProductDataBean();
					pbean.setProdId(docObj.optInt("product_id"));
					pbean.setProdName(docObj.getString("name"));
					pbean.setDesc(docObj.getString("description"));
					pbean.setMrp(docObj.getInt("price"));
					pbean.setOklerPrice(docObj.getInt("saleprice"));
					pbean.setDiscount(docObj.getInt("discount"));
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
					pbean.setProdType(Integer.parseInt(docObj.optString("pro_type")));
					if (docObj.optString("quantity").equals(null)
							|| docObj.optString("quantity").equals("null")) {
						pbean.setUnits(01);
					} else {
						pbean.setUnits(Integer.parseInt(docObj.optString("quantity")));
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
					pbean.setMediumUrl(uobj.optString("productimage_url_medium"));
					pbean.setSmallUrl(uobj.optString("productimage_url_small"));
					pbean.setThumbUrl(uobj.optString("productimage_url_thumbnail"));
					pbean.setClipArtUrl(uobj.optString("productimage_clipArt_images"));
					parray.add(pbean);
					add2.add(add);
				}
				odtBeanArr.add(odtBean);
				odtBean.setProd_list(parray);
				odtBean.setAddbean(add2);
				odtBean.setAddressDataBean(add);
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

				DiagnoOrder odtBean = new DiagnoOrder();
				
				//ProductDataBean pbean1 = null;

				odtBean.setApptDt(ajobj2.optString("lab_booked_date"));
				// odtBean.setOrderId(ajobj2.optString("order_number"));
				odtBean.setOrderId(ajobj2.optString("book_order"));

				odtBean.setStatus(ajobj2.optString("order_status"));
				String amt = ajobj2.optString("amount");
				if(amt.equals("null"))
					amt = "0";
				odtBean.setMrp(Float.parseFloat(amt));
				String tx = ajobj2.optString("tax");
				if(tx.equals("null"))
					tx="0";
				odtBean.setTax(Float.parseFloat(tx));
				String dsc = ajobj2.optString("okler_discount_amt");
				if(dsc.equals("null"))
					dsc="0";
				odtBean.setYouSaveRs(Float.parseFloat(dsc));
				String okPr = ajobj2.optString("txn_amount");
				if(okPr.equals("null"))
					okPr="0";
				odtBean.setNetPayable(Float.parseFloat(okPr));
				
				// odtBean.setCouponDiscount(ajobj2.optString(name))
				odtBean.setAppTime(ajobj2.optString("slot_period"));
				odtBean.setPickupType(ajobj2.optString("pickup_type"));

				
				
				// Set test or package - depending on order
				JSONArray testArray = ajobj2.optJSONArray("test_Array");
				if (testArray != null) {
					odtBean.setOrderType(DiagnoOrderType.TEST);
					// Set test
					tests = new ArrayList<DiagnoTestDataBean>();
					for (int testCnt = 0; testCnt < testArray.length(); testCnt++) {
						DiagnoTestDataBean tes = new DiagnoTestDataBean();
						JSONObject obj = testArray.getJSONObject(testCnt);
						tes.setTestname(obj.optString("test_name"));
						tes.setOklerTestPrice(obj.optInt("test_price"));
						tests.add(tes);
					}
					tests.trimToSize();
					
					//diagnoLab.setTestBean(tests);
				} else {
					JSONArray pkgArray = ajobj2.getJSONArray("pkg_Array");

					if (pkgArray != null) {
						odtBean.setOrderType(DiagnoOrderType.PACKAGE);
						 pkgs = new ArrayList<DiagnoPackagesDataBean>(); 
						 for(int testCnt=0;testCnt<pkgArray.length();testCnt++) {
						
						DiagnoPackagesDataBean pck = new DiagnoPackagesDataBean();
						JSONObject obj = pkgArray.getJSONObject(testCnt);
						pck.setPackageName(obj.optString("pkg_name"));
						pck.setPackOklerPrice(obj.optInt("pkg_price"));
						 pkgs.add(pck);
						 }
						 pkgs.trimToSize();
						//diagnoLab.setPackageBean(pck);
					}
				}
				
				// Set lab for order
				DiagnoLabBranchDataBean diagnoLab = new DiagnoLabBranchDataBean();
				diagnoLab.setLabId(ajobj2.optInt("lab_id"));
				diagnoLab.setLab_name(ajobj2.optString("lab_name"));
				diagnoLab.setAddr1(ajobj2.optString("labBranchAddress"));
				
				if(testArray!=null){
					dLTbean = new DiagnoLabTestDataBean();
					dLTbean.setCurrentLab(diagnoLab);
					dLTbean.setCurrentTests(tests);
					odtBean.setLabTestDataBean(dLTbean);
				}else{
					dLPbean = new DiagnoLabPackageDataBean();
					dLPbean.setCurrentLab(diagnoLab);
					dLPbean.setCurrentPkgs(pkgs);
					odtBean.setLabPkgDataBean(dLPbean);
				}
				
				//UsersDataBean udb = new UsersDataBean();
				//udb.setId(ajobj2.optInt("cust_id"));
				AddressDataBean patAddr = new AddressDataBean();
				AddressDataBean adr1 = new AddressDataBean();
				adr1.setAddress1(ajobj2.optString("pat_addr1"));
				adr1.setAddress2(ajobj2.optString("pat_addr2"));
				adr1.setFirstname(ajobj2.optString("pat_name"));
				adr1.setGender(ajobj2.optString("pat_gender"));
				adr1.setPhone(ajobj2.optString("pat_mobile"));
				adr1.setCity(ajobj2.optString("pat_city"));
				adr1.setLandmark(ajobj2.optString("pat_landmark"));
				adr1.setZip(ajobj2.optString("pat_zip"));
				adr1.setDob(ajobj2.optString("pat_dob"));
				adr1.setRelation(ajobj2.optString("pat_relation"));
				adr1.setAddr_id(ajobj2.optString("pat_id"));
				//patAddr.add(adr1);
				//udb.setPatAddList(patAddr);
				//odtBean.setUserBean(udb);
				odtBean.setPatientAddr(adr1);
				//odtBean.setSelectedLab(diagnoLab);
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
				size = physio.size();
				if (nurseTotal > 0) {
					if (nurseTotal > size) {

						if (((nurseTotal / 10) + 1) == nursepgno) {
							isAllNurse = true;
							
						}
						service_type = 2;
						nursepgno++;
						myOrderUrl = setMedPhysioUrl(cust_id, order_id,
								nursepgno, service_type);
						
						isAllNurse = false;
						webService(myOrderUrl);
					} else {
						if (attendantTotal > 0) {

							if (((attendantTotal / 10) + 1) == attpgno) {
								isAllAtt = true;
								return;
							} else {
								
								attpgno++;
								service_type = 4;
								myOrderUrl = setMedPhysioUrl(cust_id, order_id,
										attpgno, service_type);
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
				
				break;

			case 4:
				size = physio.size();
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
