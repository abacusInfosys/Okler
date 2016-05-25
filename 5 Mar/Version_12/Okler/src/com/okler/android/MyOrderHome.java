package com.okler.android;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyOrderHome extends BaseActivity {
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
	  boolean wsCalled=false,fromOrder=false;
	  String mURL="";
	  MyOrderDiagnoOrderAdapter diagnoAdp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order_home);
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		toolBar.setBackgroundColor(Color.BLUE);
		toolbarcount = (Button) findViewById(R.id.toolBarCount);
		toolbarcount.setVisibility(View.GONE);
		progressLinLayout=(LinearLayout)findViewById(R.id.progressLinLayout);
		fromOrder = getIntent().getBooleanExtra("fromOrder", false);
	       context=getApplicationContext();
	       setSupportActionBar(toolBar);
	       final ActionBar ab=getSupportActionBar();
	       myOrderList = (ListView)findViewById(R.id.myOrderList);
	       UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(this);
	       int cust_id= ubean.getId();
	       String myPhysioUrl = "https://www.okler.com/api//nurse/GetAllservicesByuser?user_id="+cust_id+"&service_type=";
	       String myMedUrl = "https://www.okler.com/api/order/getorders?cust_id="+cust_id;
	       String digno_url = "https://www.okler.com/api/lab/retrivelabbook?cust_id="+cust_id;
	       myOrderUrl ="";
	       
	       get = getIntent().getIntExtra("value", -1);
	       odtBeanArr = new ArrayList<OrdersDataBean>();
	       TextView text_name = (TextView) findViewById(R.id.my_order_text);
	       switch (get) {
		case 1:
			text_name.setText("MEDICINES & HEALTHSHOP");				
			myOrderUrl = myMedUrl;
			break;
			
		case 2:
			text_name.setText("DIAGNOSTIC SERVICES");	
			myOrderUrl = digno_url;
			break;
			
		case 3:
			text_name.setText("MEDICAL SERVICES");	
			myOrderUrl = myPhysioUrl;
			break;
			
		case 4:
			text_name.setText("PHYSIOTHERAPHY");	
			myOrderUrl = myPhysioUrl+3;
			break;

		default:
			break;
		}
	       physio = new ArrayList<PhysioAndMedicalBean>();
	       diagnoOdtBeanArr = new ArrayList<DiagnoOrderDataBean>();
	       //for medicines
	       //myOrderUrl= myMedUrl;
	       
	       //for other services
	       //myOrderUrl= myPhysioUrl;      
	    	
	    	

	      Utilities.setTitleText(toolBar, "My Orders");
	      
	      //myOrderList.setFocusable(false);
	      
	      myOrderList.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "on item sel", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			//	Toast.makeText(getApplicationContext(), "on item not sel", Toast.LENGTH_LONG).show();
			}
		});
	      myOrderList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub			
				
				if(get == 2)
				{
					//If diagno order - then stat new activity
					Intent startDiagno = new Intent(context,DiagnoOrderSummary.class);
					startDiagno.putExtra("isFromOrder", true);
					startDiagno.putExtra("position", position);
					startActivity(startDiagno);					
				}
				else
				{
						pos = myOrderList.getSelectedItemPosition();
						Intent startOrderDetails;
						PhysioAndMedicalBean physioMedBean = new PhysioAndMedicalBean();
						Gson gson;
						String aBean;					
						
						switch (get) {
						case 1:
							
							/*ArrayList<OrdersDataBean> order_array = Okler.getInstance().getUsersOrders();
							
							ProductDataBean pdbean = new ProductDataBean();
							pdbean = order_array.get(pos);
							
							gson = new Gson();
							String pbean = gson.toJson(pdbean);*/
							
							intentValue = 1;
							startOrderDetails = new Intent(context,MyOrderMed_Hs_Details.class);
							startOrderDetails.putExtra("intent_value", intentValue);
							startOrderDetails.putExtra("position", position);
							Okler.getInstance().setPostion(position);
							startActivity(startOrderDetails);
							
							break;
							
						case 2:
							
							intentValue = 2;
							startOrderDetails = new Intent(context,MyOrderMed_Hs_Details.class);
							startOrderDetails.putExtra("intent_value", intentValue);
							startOrderDetails.putExtra("position", pos);
							Okler.getInstance().setPostion(position);
							startActivity(startOrderDetails);
							
							break;
							
						case 3:
							physioMedBean = physio.get(position);	
							gson = new Gson();
							aBean = gson.toJson(physioMedBean);
			
							startOrderDetails = new Intent(context,MyOrderMS_History.class);
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
			
							startOrderDetails = new Intent(context,MyOrderMS_History.class);
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
	      //webService();
	      
	       bottomBarLayout = findViewById(R.id.bottombar);
	       handleMapping(bottomBarLayout);
	       if(get == 2)
	       {
	    	   diagnoAdp = new MyOrderDiagnoOrderAdapter(context, diagnoOdtBeanArr);
	    	   myOrderList.setAdapter(diagnoAdp);	    	   
	       }
	       else
	       {
	    	   ordAdapter = new MyOrdersAdapter(context,odtBeanArr);			
	    	   myOrderList.setAdapter(ordAdapter);
	    	   ordAdapter.notifyDataSetChanged();
	       }
	       
	       imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			if(fromOrder){
					Intent intent = new Intent(MyOrderHome.this,ServiceCategoryActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
			}
			finish();	
			}

	       });

	     
	}
/*	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Toast.makeText(getApplicationContext(), "Restart", Toast.LENGTH_SHORT).show();
	}*/
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(get==2)
		{
			diagnoOdtBeanArr.clear();
			diagnoAdp.notifyDataSetChanged();
		}
		else
		{
			odtBeanArr.clear();
			ordAdapter.notifyDataSetChanged();
		}
		mURL = myOrderUrl;
		wsCalled=false;
		webService(myOrderUrl);
		//Toast.makeText(getApplicationContext(), "Resume", Toast.LENGTH_SHORT).show();
	}
	
/*	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Toast.makeText(getApplicationContext(), "Pause", Toast.LENGTH_SHORT).show();
	}*/
	
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
	public void webService(String myOrderUr){

		mURL=myOrderUr;
		if(get == 3 && wsCalled )
			mURL=myOrderUr+4;
		else
			if(get==3 && !wsCalled)
				mURL=myOrderUr+2;
			
				
	       WebJsonObjectRequest alJson = new WebJsonObjectRequest(Method.GET, mURL, new JSONObject(), new Response.Listener<JSONObject>() {
	    		
				@Override
				public void onResponse(JSONObject response) {
					showProgress(false);
					JSONObject ajobj = (JSONObject)response;
					JSONArray ajobj1 = new JSONArray();
					JSONObject ajobj2 = new JSONObject();
					JSONObject ajobj3 = new JSONObject();
					JSONArray ajArray = new JSONArray();		
					try {
						
						if(ajobj.getString("message").equals("No data found."))
						{
							Toast.makeText(MyOrderHome.this, "You dont have any order ...", Toast.LENGTH_LONG).show();
						}
						else
						{
							if(get==1){
								//ajobj2 = ajobj.getJSONObject("result");
							}else{
							ajobj1 = ajobj.getJSONArray("result");
							}									
						
							
							switch (get) {
							case 1:
								setMedHSOrders(ajobj);
								break;
								
							case 2:
								setDiagnoOrder(ajobj1);
								break;
							case 3:
								setMedServies(ajobj1);
								if(!wsCalled)
								{
									wsCalled=true;
									webService(myOrderUrl);
									
								}
								break;
								
							case 4:
								setPhysioOrders(ajobj1);	
													       
							    break;							
								}			       
						      
						    //   odtBean.setPrice(ajobj2.optString("total")); 
						       ordAdapter.notifyDataSetChanged();
			
						
						}					
					}
					catch(Exception e) {
						// TODO Auto-generated catch block
						String resp2 = String.valueOf(e);
					//	Toast.makeText(getApplicationContext(), resp2, Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
					Okler.getInstance().setUsersOrders(odtBeanArr);
					
				}
	       }, new Response.ErrorListener() {
	    	   @Override
				public void onErrorResponse(VolleyError error) {
					showProgress(false);
				}
			});
	       
	       
	       //put that response in odtBean
	       alJson.setShouldCache(false);
	       DefaultRetryPolicy defRetryPol = new DefaultRetryPolicy(10000, 2, 1);
	      alJson.setRetryPolicy(defRetryPol);
	   	if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), alJson))
		{
       		showProgress(true);
       		
		//	Toast.makeText(getApplicationContext(), "got response", Toast.LENGTH_LONG).show();
		}
		else
		{
			showProgress(false);
			//Toast.makeText(getApplicationContext(), "value not found", Toast.LENGTH_LONG).show();
		}
	
	}
	
	private void setMedServies(JSONArray ajobj1)
	{
		try
		{
		JSONObject ajobj2;
		for (int ai = 0; ai < ((ajobj1.length())); ai++) 
		{
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
	    pnmBean.setBooking_status(ajobj2.optString("booking_status_name"));
	    addressobj = ajobj2.getJSONObject("address_details");
	    pnmBean.setCity(ajobj2.optString("city_name")); 
	    pnmBean.setFirstname(addressobj.optString("firstname"));
	    pnmBean.setsurname(addressobj.optString("lastname"));
	    pnmBean.setAddress(addressobj.optString("street_address"));
	    pnmBean.setRelation(ajobj2.optString("booking_for_relation"));
	    pnmBean.setService(ajobj2.optString("service_name"));
	    pnmBean.setBooking_status(ajobj2.optString("booking_status_name"));
	    odtBeanArr.add(odtBean);
	       physio.add(pnmBean);
		}
		}catch(Exception ex){}
	}
	
	private void setPhysioOrders(JSONArray ajobj1)
	{
		try
		{
			JSONObject ajobj2;
		for (int ai = 0; ai < ((ajobj1.length())); ai++) 
		{
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
	    addressobj = ajobj2.getJSONObject("address_details");
	    pnmBean.setCity(ajobj2.optString("city_name")); 
	    pnmBean.setFirstname(addressobj.optString("firstname"));
	    pnmBean.setsurname(addressobj.optString("lastname"));
	    pnmBean.setAddress(addressobj.optString("street_address"));
	    pnmBean.setRelation(ajobj2.optString("booking_for_relation"));
	    pnmBean.setService(ajobj2.optString("service_name"));
	    pnmBean.setBooking_status(ajobj2.optString("booking_status_name"));
	    odtBeanArr.add(odtBean);
	       physio.add(pnmBean);
		}
		}catch(Exception ex){}
		ordAdapter.notifyDataSetChanged();
	}
	
	private void setMedHSOrders(JSONObject ajobj1)
	{
		try {
		
		//	JSONObject ajobj3 = ajobj1.getJSONObject("result");
			JSONArray ajobj3 = ajobj1.getJSONArray("result");
			JSONObject ajobj2;
		 int length = ajobj3.length()-1;
		for (int ai = 0; ai <= length; ai++) 
		{
			Log.i("in loop", "in loop");
			
			
				ajobj2 = ajobj3.getJSONObject(ai);
			
			
			OrdersDataBean odtBean = new OrdersDataBean();
			PhysioAndMedicalBean pnmBean = new PhysioAndMedicalBean();
		
		ProductDataBean pbean = null;					
		AddressDataBean add = null;
		add = new AddressDataBean();
		
		odtBean.setDate(ajobj2.optString("ordered_on"));
		//odtBean.setOrderId(ajobj2.optString("order_number"));
		odtBean.setOrderId(ajobj2.optString("id"));
		odtBean.setOrderStatus(ajobj2.optString("status"));
		odtBean.setPrice(ajobj2.optString("total"));
		odtBean.setShipping_charges(ajobj2.optString("shipping_charges"));
		
		
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
		
		JSONArray prodlist = ajobj2.getJSONArray("order_items");
		
		ArrayList<ProductDataBean> parray = new ArrayList<ProductDataBean>();
		ArrayList<AddressDataBean> add2 = new ArrayList<AddressDataBean>();
		for(int j = 0; j <prodlist.length(); j ++)
		{
			JSONObject docObj = prodlist.getJSONObject(j);
			
			pbean = new ProductDataBean();
			
			pbean.setProdId(docObj.getInt("product_id"));
			pbean.setProdName(docObj.getString("name"));
			pbean.setDesc(docObj.getString("description"));
			pbean.setMrp(docObj.getInt("price"));
			pbean.setOklerPrice(docObj.getInt("saleprice"));
			pbean.setDiscount(docObj.getInt("discount"));
			if(docObj.has("company_name"))
			pbean.setCompany(docObj.optString("company_name"));
			
			if(docObj.has("generic_val"))
			pbean.setGeneric_name(docObj.optString("generic_val"));
			if(docObj.optString("prescription_need").equals("")||docObj.optString("prescription_need").equals(null)||docObj.optString("prescription_need").equals("null")){
			pbean.setPresc_needed(0);	
			}else{
			pbean.setPresc_needed(Integer.parseInt(docObj.optString("prescription_need")));	
			}
			pbean.setTax(Float.parseFloat(docObj.optString("tax")));
			pbean.setProdType(Integer.parseInt(docObj.optString("pro_type")));
			if(docObj.optString("quantity").equals(null)||docObj.optString("quantity").equals("null")){
				pbean.setUnits(01);
			}else{
			pbean.setUnits(Integer.parseInt(docObj.optString("quantity")));
			}
			String jimg = docObj.getString("images");
			String url2;
			if(jimg.equals(null)||jimg.equals("null")||jimg.equals("")){
				url2 = "drawable://" + R.drawable.tempcuff;
			}else{
			String j1[] =jimg.split(",");
			String j2=j1[0];
			String colon = ":";
			String j3[]=j2.split(colon);
			String url = j3[2];
			//String url1 = url.substring(1);
			int length1 = url.length();
			url2 = url.substring(1, (length1-1));
			}
			JSONObject uobj= new JSONObject();
			uobj = ajobj1.getJSONObject("productimage_url");
			pbean.setMediumUrl(uobj.optString("productimage_url_medium"));
			pbean.setSmallUrl(uobj.optString("productimage_url_small"));
			pbean.setThumbUrl(uobj.optString("productimage_url_thumbnail"));
			
			
			//JSONObject jimg2 = jimg.getJSONObject("");
			//JSONObject jimg3 = jobj2.getJSONObject("images");
			//pdBean.setImgUrl(jobj2.getJSONObject("images").getJSONObject("").getString("filename"));
			pbean.setImgUrl(url2);
			
			
			parray.add(pbean);
			add2.add(add);
			
		}
		
		
		odtBeanArr.add(odtBean);
	       physio.add(pnmBean);
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
	
	private  void setDiagnoOrder(JSONArray ajobj1)
	{
		try{
			JSONObject ajobj2;
		for (int ai = 0; ai < ((ajobj1.length())); ai++) 
		{
			Log.i("in loop", "in loop");
			
			ajobj2 = ajobj1.getJSONObject(ai);
			
			DiagnoOrderDataBean odtBean = new DiagnoOrderDataBean();
		
		
		ProductDataBean pbean1 = null;
		
		
		
		odtBean.setAp_date(ajobj2.optString("lab_booked_date"));
		//odtBean.setOrderId(ajobj2.optString("order_number"));
		odtBean.setOrderId(ajobj2.optString("book_order"));
		
		odtBean.setStatus(ajobj2.optString("order_status"));
		odtBean.setAmount(ajobj2.optString("amount"));
		odtBean.setTax(ajobj2.optString("tax"));
		odtBean.setOklerDiscount(ajobj2.optString("okler_discount_amt"));
		odtBean.setNetPayable(ajobj2.optString("txn_amount"));
		//odtBean.setCouponDiscount(ajobj2.optString(name))
		odtBean.setAp_time(ajobj2.optString("slot_period"));
		odtBean.setPickupType(ajobj2.optString("pickup_type"));
		
	
		//Set lab for order
		DiagnoLabsDataBean diagnoLab = new DiagnoLabsDataBean();
		diagnoLab.setLabId(ajobj2.optInt("lab_id"));
		diagnoLab.setLab_name(ajobj2.optString("lab_name"));
		diagnoLab.setLab_address("labBranchAddress");
		
		//Set test or package - depending on order
		JSONArray prodlist1 = ajobj2.optJSONArray("test_Array");
		if(prodlist1 != null)
		{
			//Set test 
			ArrayList<TestDataBean> tests = new ArrayList<TestDataBean>();
			for(int testCnt=0;testCnt<prodlist1.length();testCnt++)
			{
				TestDataBean tes = new TestDataBean();
				JSONObject obj = prodlist1.getJSONObject(testCnt);
				tes.setTestname(obj.optString("test_name"));
				tes.setOklerTestPrice(obj.optInt("test_price"));
				tests.add(tes);
			}
			diagnoLab.setTestBean(tests);
		}
		else
		{
			prodlist1 = ajobj2.getJSONArray("pkg_Array");

			if( prodlist1 != null )
		    {
				/*ArrayList<DiagnoPackageDataBean> tests = new ArrayList<DiagnoPackageDataBean>();
				for(int testCnt=0;testCnt<prodlist1.length();testCnt++)
				{*/
					DiagnoPackageDataBean pck = new DiagnoPackageDataBean();
					JSONObject obj = prodlist1.getJSONObject(0);
					pck.setPackage_name(obj.optString("pkg_name"));
					pck.setPack_oklerPrice(obj.optInt("pkg_price"));
				//	tests.add(pck);
			//	}
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
		/*ArrayList<ProductDataBean> parray1 = new ArrayList<ProductDataBean>();
		ArrayList<AddressDataBean> add1 = new ArrayList<AddressDataBean>();
		for(int j = 0; j <prodlist1.length(); j ++)
		{
			JSONObject docObj = prodlist1.getJSONObject(j);
			
		 ProductDataBean pbean = new ProductDataBean();
			
			pbean.setProdId(docObj.getInt("product_id"));
			pbean.setProdName(docObj.getString("test_name"));
		//	pbean.setDesc(docObj.getString("description"));
			pbean.setMrp(docObj.getInt("test_price"));
		//	pbean.setOklerPrice(docObj.getInt("saleprice"));
		//	pbean.setDiscount(docObj.getInt("discount"));
			
			String jimg = docObj.getString("images");
			String url2;
			if(jimg.equals(null)||jimg.equals("null")||jimg.equals("")){
				url2 = "drawable://" + R.drawable.tempcuff;
			}else{
			String j1[] =jimg.split(",");
			String j2=j1[0];
			String colon = ":";
			String j3[]=j2.split(colon);
			String url = j3[2];
			//String url1 = url.substring(1);
			int length = url.length();
			url2 = url.substring(1, (length-1));
			}
			//JSONObject jimg2 = jimg.getJSONObject("");
			//JSONObject jimg3 = jobj2.getJSONObject("images");
			//pdBean.setImgUrl(jobj2.getJSONObject("images").getJSONObject("").getString("filename"));
			pbean.setImgUrl(url2);			
			parray1.add(pbean);*/
			diagnoOdtBeanArr.add(odtBean);				
	}
diagnoAdp.notifyDataSetChanged();
		Okler.getInstance().setUserDiagnoOrders(diagnoOdtBeanArr);

	 }catch(Exception ex){}
	}
	 private void showProgress(boolean paramBoolean)
	  {
	    if (paramBoolean)
	    {
	      this.progressLinLayout.setVisibility(View.VISIBLE);
	      return;
	    }
	    this.progressLinLayout.setVisibility(View.INVISIBLE);
	  }
	 @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(fromOrder){
			Intent intent = new Intent(MyOrderHome.this,ServiceCategoryActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
}
