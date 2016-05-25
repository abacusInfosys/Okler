package com.okler.diagnostics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;

import com.android.volley.Response;
import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.okler.adapters.DiagnoPackageAdapter;
import com.okler.android.BaseActivity;
import com.okleruser.R;

import com.okler.customviews.CustomViewLabAddress;
import com.okler.customviews.CustomViewLabsForSelectedPckg;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.DiagnoLabsDataBean;
import com.okler.databeans.DiagnoOrderDataBean;
import com.okler.databeans.DiagnoPackageDataBean;
import com.okler.databeans.TestDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class DiagnoSchedulePickup extends BaseActivity implements  Response.Listener, Response.ErrorListener{
	private Button book_appoint_btn_home,book_appoint_btn_lab;
	private ImageView locationIv,imgBack,home_whiteCirle,home_white_selectedCircle,Lab_whiteCirle,Lab_white_selectedCircle,lessThan_home,greaterThan_home,lessThan_Lab,greaterThan_Lab;
	private Toolbar toolBar;
	private View bottomBarLayout;
	private RelativeLayout headingLayoutHome,headingLayoutLab,mainLabsLayout,mainHomeLayout,lab_innerLayout;
	private Spinner time_home,time_lab;
	private String tym_slotUrl;
	private ArrayAdapter<String> adapter;
	private Context context;
	private WebJsonObjectRequest webobj;
	private ArrayList<String> listTymSlotValue = new ArrayList<String>();
	private TextView dateTvhome,dateTvLab;
	ArrayList<TestDataBean> tList = new ArrayList<TestDataBean>();
	private Calendar calendar;
	private EditText et_pincode;
	Calendar curCal,maxCal,minCal;
	SimpleDateFormat df;
	DiagnoLabsDataBean labbean = new DiagnoLabsDataBean();
	DiagnoOrderDataBean dobean = new DiagnoOrderDataBean();
	DiagnoPackageDataBean dpbean = new DiagnoPackageDataBean();
	String formattedDate;
	String todaysDate;
	CustomViewLabAddress customLabsNear[] = new CustomViewLabAddress[100];
	CustomViewLabAddress customLabsPinCode[] = new CustomViewLabAddress[100];
	String getLabs1,getLabs2,getLabs3,getLabs4,getLabsForPincodeUrl;
	int pinCode,pinCode2,labId;
	ImageView radio_grey,radio_red,nradio_grey,nradio_red;
	CheckBox chk_n,chk;
	TextView lab_name,lab_addr,nlab_name,nlab_addr;
	ArrayList<DiagnoLabsDataBean> dLabList,dLabList2;
	LinearLayout LL_for_labs_pin,LL_for_labs_near;
	DiagnoLabsDataBean dbbean,db2;
	DiagnoOrderDataBean doBean;
	AddressDataBean adbean;
	ArrayList<AddressDataBean> paList  = new ArrayList<AddressDataBean>();
	DiagnoLabsDataBean dbean;
	UsersDataBean ubean;
	RelativeLayout inner_RL_home_pickup;
	DiagnoLabsDataBean tdbean;
	Activity ack;
	boolean isSelected,isAvailable= false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagno_schedule_pickup);
		context=this;
		ack = this;
		doBean = new DiagnoOrderDataBean();
		doBean = Okler.getInstance().getDiagnoOrderDataBean();
		//temp hardcoded data
		db2 = doBean.getSelectedLab();
		//db2.setLabId(7);
		labId = db2.getLabId();
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		paList = ubean.getPatAddList();
		//adbean = new AddressDataBean();
		//adbean.setZip("506356");
		//adList.add(adbean);
		int length = paList.size();
		for (int i = 0; i < length; i++) {
			adbean = new AddressDataBean();
			adbean = paList.get(i);
			//temp hardcoded
			//adbean.setZip("506356");
			if(length==1){
				String zip = adbean.getZip();
				if(zip==null||zip.equals("")||zip.length()<=0||zip.equals("null")){
					zip = "0";
				}
				pinCode = Integer.parseInt(zip);
				break;
			}
			else if(adbean.isSelected()){
				String zip = adbean.getZip();
				if(zip==null||zip.equals("")||zip.length()<=0||zip.equals("null")){
					zip = "0";
				}
				pinCode = Integer.parseInt(zip);
				break;
			}
		}
		tList = new ArrayList<TestDataBean>();
		dobean = Okler.getInstance().getDiagnoOrderDataBean();
		labbean = dobean.getSelectedLab();
		book_appoint_btn_home = (Button)findViewById(R.id.book_appoint_btn_home);
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		bottomBarLayout = findViewById(R.id.bottombar);
		getLabs1 = getString(R.string.getLabsForPinPart1);
		getLabs2 = getString(R.string.getLabsForPinPart2);
		getLabs3 = getString(R.string.getLabsForPinPart3);
		getLabs4 = getString(R.string.getLabsForPinPart4);
		locationIv = (ImageView)findViewById(R.id.locationIv);
		LL_for_labs_pin = (LinearLayout)findViewById(R.id.LL_for_labs_pin);
		time_home=(Spinner) findViewById(R.id.timeSpinner);
	    time_lab=(Spinner) findViewById(R.id.timeSpinner_lab);
	    tym_slotUrl=getString(R.string.getAlltymslotsForLab);
	    book_appoint_btn_lab = (Button)findViewById(R.id.book_appoint_btn_lab);
	    
		handleMapping(bottomBarLayout);
	
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	    imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	      Utilities.setTitleText(toolBar, "Diagnostic");
	      mainLabsLayout=(RelativeLayout) findViewById(R.id.lab_Visit_RL);
	      mainHomeLayout = (RelativeLayout)findViewById(R.id.home_pickup_RL);
	      
	      headingLayoutHome=(RelativeLayout) findViewById(R.id.heading_layout_home);
	      home_whiteCirle=(ImageView) findViewById(R.id.white_circle);
	      home_white_selectedCircle=(ImageView) findViewById(R.id.white_circle_selected);
	      headingLayoutLab=(RelativeLayout) findViewById(R.id.heading_layout_lab);
	      Lab_whiteCirle=(ImageView) findViewById(R.id.white_circle_lab);
	      Lab_white_selectedCircle=(ImageView) findViewById(R.id.white_circle_selected_lab);
	      et_pincode=(EditText) findViewById(R.id.pincode_ET);
	      lab_innerLayout=(RelativeLayout) findViewById(R.id.RL_for_labs_addr);
	      LL_for_labs_near = (LinearLayout)findViewById(R.id.LL_for_labs_near);
	      inner_RL_home_pickup = (RelativeLayout)findViewById(R.id.inner_RL_home_pickup);
	    //date related 
	      lessThan_home=(ImageView) findViewById(R.id.lessThan);
	      greaterThan_home=(ImageView) findViewById(R.id.greaterThan);
	      lessThan_Lab=(ImageView) findViewById(R.id.lessThan_lab);
	      greaterThan_Lab=(ImageView) findViewById(R.id.greaterThan_lab);
	      dateTvhome=(TextView) findViewById(R.id.dateValueTv);
	      dateTvLab=(TextView) findViewById(R.id.dateValueTv_lab);
	      
	      headingLayoutHome.setOnClickListener(new OnClickListener() {
	      
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				homeLayoutClick();
			}
		});
	      headingLayoutLab.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Lab_whiteCirle.setVisibility(View.GONE);
				Lab_whiteCirle.setBackgroundColor(Color.parseColor("#87cefa"));
				Lab_white_selectedCircle.setVisibility(View.VISIBLE);
				home_white_selectedCircle.setVisibility(View.GONE);
				home_whiteCirle.setVisibility(View.VISIBLE);
				lab_innerLayout.setVisibility(View.VISIBLE);
				//Lab_white_selectedCircle.setBackgroundColor(Color.parseColor("#87cefa"));
				//headingLayoutHome.setEnabled(false);
				headingLayoutLab.setBackgroundColor(getResources().getColor(R.color.VividBlue));
				headingLayoutHome.setBackgroundColor(Color.parseColor("#87cefa"));
				lessThan_home.setEnabled(false);
				greaterThan_home.setEnabled(false);
				time_home.setEnabled(false);
				
				lessThan_Lab.setEnabled(true);
				greaterThan_Lab.setEnabled(true);
				time_lab.setEnabled(true);
				book_appoint_btn_lab.setEnabled(true);
				book_appoint_btn_home.setEnabled(false);
				
			}
		});
	      
	     
	      setCurrentDate();
	      	
	      	lessThan_home.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					minusDate(dateTvhome);
				}
			});
	      	greaterThan_home.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				addDate(dateTvhome);
				}
			});
	      	lessThan_Lab.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					minusDate(dateTvLab);
					
				}
			});
	      	greaterThan_Lab.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					addDate(dateTvLab);
					
				}
			});
	      //time slot related
	      
	      webServiceCallForTymslots(tym_slotUrl);
	      
	      book_appoint_btn_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Calendar cal1 = curCal;
				cal1.set(Calendar.HOUR, 00);
				cal1.set(Calendar.MINUTE, 00);
				cal1.set(Calendar.SECOND, 00);
				cal1.set(Calendar.MILLISECOND, 00);
				long d = cal1.getTimeInMillis();
				
				Calendar cal2 = minCal;
				cal2.set(Calendar.HOUR, 00);
				cal2.set(Calendar.MINUTE, 00);
				cal2.set(Calendar.SECOND, 00);
				cal2.set(Calendar.MILLISECOND, 00);
				long min = cal2.getTimeInMillis();
				//Toast.makeText(ack, "curDate = "+curCal.get(Calendar.DAY_OF_MONTH)+"\n"+"minDate = "+minCal.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_LONG).show();
				if(min==d){
					String gottime = time_home.getSelectedItem().toString();
					String arr1[] = gottime.split("-");
					String cmpTime=arr1[1];
					String arr2[] = cmpTime.split(" ");
					String time = arr2[1];
					String ampm = arr2[2];
					Calendar cal = Calendar.getInstance();
					int t = cal.get(Calendar.HOUR_OF_DAY);
					int t2 = Integer.parseInt(time);
					if(ampm.equals("PM"))
						t2=t2+12;
					if(t<t2){
						//Toast.makeText(ack, "if"+"t= "+t+" t2= "+t2, Toast.LENGTH_LONG);
						doBean.setAp_time(time_home.getSelectedItem().toString());
						int slot_id = time_home.getSelectedItemPosition();
						doBean.setSlot_id(slot_id+1);
						doBean.setAp_date(dateTvhome.getText().toString());
						doBean.setAp_at("Home");
						tdbean = doBean.getSelectedLab();
						doBean.setSelectedLab(tdbean);
						Okler.getInstance().setDiagnoOrderDataBean(doBean);
						Intent intent = new Intent(getApplicationContext(),DiagnoOrderSummary.class);
						intent.putExtra("PickupType", "Home");
						startActivity(intent);
					}else{
						Toast.makeText(ack, "Time you Have selected is already passed", 1500).show();
					}
					
				}else{
					doBean.setAp_time(time_home.getSelectedItem().toString());
					int slot_id = time_home.getSelectedItemPosition();
					doBean.setSlot_id(slot_id+1);
					doBean.setAp_date(dateTvhome.getText().toString());
					doBean.setAp_at("Home");
					tdbean = doBean.getSelectedLab();
					doBean.setSelectedLab(tdbean);
					Okler.getInstance().setDiagnoOrderDataBean(doBean);
					Intent intent = new Intent(getApplicationContext(),DiagnoOrderSummary.class);
					intent.putExtra("PickupType", "Home");
					startActivity(intent);
				}
				
			}
		});
	    
	    book_appoint_btn_lab.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String pin = et_pincode.getText().toString();
				if(pin.length()>0){
					if(isAvailable){
					if(isSelected){
				Calendar cal1 = curCal;
				cal1.set(Calendar.HOUR, 00);
				cal1.set(Calendar.MINUTE, 00);
				cal1.set(Calendar.SECOND, 00);
				cal1.set(Calendar.MILLISECOND, 00);
				long d = cal1.getTimeInMillis();
				
				Calendar cal2 = minCal;
				cal2.set(Calendar.HOUR, 00);
				cal2.set(Calendar.MINUTE, 00);
				cal2.set(Calendar.SECOND, 00);
				cal2.set(Calendar.MILLISECOND, 00);
				long min = cal2.getTimeInMillis();
				//Toast.makeText(ack, "curDate = "+curCal.get(Calendar.DAY_OF_MONTH)+"\n"+"minDate = "+minCal.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_LONG).show();
				if(min==d){
					String gottime = time_lab.getSelectedItem().toString();
					String arr1[] = gottime.split("-");
					String cmpTime=arr1[1];
					String arr2[] = cmpTime.split(" ");
					String time = arr2[1];
					String ampm = arr2[2];
					Calendar cal = Calendar.getInstance();
					int t = cal.get(Calendar.HOUR_OF_DAY);
					int t2 = Integer.parseInt(time);
					if(ampm.equals("PM"))
						t2=t2+12;
					if(t<t2){
						//Toast.makeText(ack, "if"+"t= "+t+" t2= "+t2, Toast.LENGTH_LONG);
						doBean.setAp_time(time_lab.getSelectedItem().toString());
						int slot_id = time_lab.getSelectedItemPosition();
						doBean.setSlot_id(slot_id+1);
						doBean.setAp_date(dateTvLab.getText().toString());
						doBean.setAp_at("Lab Visit");
						tdbean = doBean.getSelectedLab();
						doBean.setSelectedLab(tdbean);
						Okler.getInstance().setDiagnoOrderDataBean(doBean);
						//Intent intent = new Intent(getApplicationContext(),DiagnoOrderSummary.class);
						//intent.putExtra("PickupType", "Home");
						//startActivity(intent);
					}else{
						Toast.makeText(ack, "Time you Have selected is already passed", 1500).show();
					}
					
				}else{
					doBean.setAp_time(time_lab.getSelectedItem().toString());
					int slot_id = time_lab.getSelectedItemPosition();
					doBean.setSlot_id(slot_id+1);
					doBean.setAp_date(dateTvhome.getText().toString());
					doBean.setAp_at("Lab Visit");
					tdbean = doBean.getSelectedLab();
					doBean.setSelectedLab(tdbean);
					Okler.getInstance().setDiagnoOrderDataBean(doBean);
					//Intent intent = new Intent(getApplicationContext(),DiagnoOrderSummary.class);
					//intent.putExtra("PickupType", "Home");
					//startActivity(intent);
				}
				
				
				//doBean.setAp_time(time_lab.getSelectedItem().toString());
				//int slot_id = time_lab.getSelectedItemPosition();
				//doBean.setSlot_id(slot_id+1);
				//Toast.makeText(getApplicationContext(), ""+slot_id, Toast.LENGTH_LONG).show();
				//doBean.setAp_date(dateTvLab.getText().toString());
				//doBean.setAp_at("Lab Visit");
				//Okler.getInstance().setDiagnoOrderDataBean(doBean);
				//String addr = doBean.getSelectedLab().getLab_address();
				//if(addr.equals("")||addr.equals(null)||addr.equals("null")){
				DiagnoLabsDataBean val = doBean.getSelectedLab();
				if(doBean.getSelectedLab()==null){
					
				Toast.makeText(getApplicationContext(), "Please select atleast one Lab branch", 1500).show();	
				}else{
				Intent intent = new Intent(getApplicationContext(),DiagnoOrderSummary.class);
				intent.putExtra("PickupType", "Lab Visit");
				startActivity(intent);
				}
					}else{
						Toast.makeText(ack, "Please Select at least one lab branch", 1500).show();	
					}
				}else{
					Toast.makeText(ack, "Please click on location icon to get Labs available at your location", 1500).show();
				}
				}else{
					Toast.makeText(ack, "Please Enter a Pincode for checking available lab branches", 1500).show();
				}
			}
		});  
	    setCustomViewsNearBy();
	    locationIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextValidations tval = new TextValidations(et_pincode);
				if(tval.validatePinCode("Enter Valid Pincode"))
				setCustomViewsPinCode();
		//	Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();	
			}
		});
	    homeLayoutClick(); 
	
	}

	private void homeLayoutClick() {
		home_whiteCirle.setVisibility(View.GONE);
		home_whiteCirle.setBackgroundColor(Color.parseColor("#87cefa"));
		home_white_selectedCircle.setVisibility(View.VISIBLE);
		Lab_white_selectedCircle.setVisibility(View.GONE);
		Lab_whiteCirle.setVisibility(View.VISIBLE);
		//home_white_selectedCircle.setBackgroundColor(Color.parseColor("#87cefa"));
		//headingLayoutLab.setEnabled(false);
		headingLayoutHome.setBackgroundColor(getResources().getColor(R.color.VividBlue));
		headingLayoutLab.setBackgroundColor(Color.parseColor("#87cefa"));
		mainLabsLayout.setEnabled(false);
		lab_innerLayout.setVisibility(View.GONE);
		lessThan_home.setEnabled(true);
		greaterThan_home.setEnabled(true);
		time_home.setEnabled(true);
		book_appoint_btn_home.setEnabled(true);
		lessThan_Lab.setEnabled(false);
		greaterThan_Lab.setEnabled(false);
		time_lab.setEnabled(false);
		book_appoint_btn_lab.setEnabled(false);
		book_appoint_btn_home.setEnabled(true);
	}
	
	private void setCustomViewsPinCode() {
		String pin = et_pincode.getText().toString();
		if(pin.equals("")){
			pin = "1";
		}
		pinCode2 = Integer.parseInt(pin);
		
		tList= labbean.getTestBean();
		String testId = "";
		String entity_price = "";
		String packId = "";
		int len = tList.size();
		for (int i = 0; i < len; i++) {
			TestDataBean tbean = new TestDataBean();
			tbean = tList.get(i);
			if(testId.equals("")){
				testId =String.valueOf(tbean.getTestId());	
			}else{
				testId = testId+","+tbean.getTestId();
			}
			if(entity_price.equals("")){
				entity_price =tbean.getOklerTestPrice()+"";
			}else{
			entity_price = entity_price+","+tbean.getOklerTestPrice();
			}
		}
		if(labbean.getPackageBean()!=null){
			dpbean = labbean.getPackageBean();
			packId = String.valueOf(dpbean.getPacakageId());
			
		}
		//labId = 7;
		getLabsForPincodeUrl = getLabs1+pinCode2+getLabs2+labId+getLabs3+testId+getLabs4+packId;
		WebJsonObjectRequest pinjson = new WebJsonObjectRequest(Method.GET, getLabsForPincodeUrl, new JSONObject(), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
			String resp = String.valueOf(response);	
				if(response.optString("message").equals("Pincode Available")){
					isAvailable=true;
			try {
				JSONArray jarr = response.getJSONArray("result");
				JSONObject jobj1 = new JSONObject();
				
				dLabList2 = new ArrayList<DiagnoLabsDataBean>();
				int length = jarr.length();
				for (int i = 0; i < length; i++) {
					dbean = new DiagnoLabsDataBean();
					jobj1 = jarr.getJSONObject(i);
					dbean.setLabId(Integer.parseInt(jobj1.optString("lab_id")));
					dbean.setLab_name(jobj1.optString("lab_name"));
					dbean.setLab_address(jobj1.optString("lab_address"));
					dbean.setLab_city(jobj1.optString("lab_city_name"));
					dbean.setLab_state(jobj1.optString("lab_state_name"));
					dbean.setLab_pinCode(jobj1.optString("lab_branch_pin"));
					dbbean.setLab_city_branch_id(jobj1.optString("lab_branch_city_id"));
					dLabList2.add(dbean);
				}
			Okler.getInstance().setDiagLabAddrListNear(dLabList2);	
			int length2 = dLabList2.size();
			
			
			for (int i1 = 0; i1 < length2; i1++) {
				dbbean = new DiagnoLabsDataBean();
				dbbean = dLabList2.get(i1);
				customLabsPinCode[i1] = new CustomViewLabAddress(getApplicationContext());
				
				//radio_grey = (ImageView)customLabsPinCode[i1].findViewById(R.id.radio_grey);
				//radio_grey.setTag(""+i1+100);
				chk = (CheckBox)customLabsPinCode[i1].findViewById(R.id.chkbx_lab);
				chk.setTag(""+i1);
				lab_name = (TextView)customLabsPinCode[i1].findViewById(R.id.lab_name);
				lab_addr = (TextView)customLabsPinCode[i1].findViewById(R.id.lab_addr);
				
				lab_name.setText(dbbean.getLab_name()+":");
				String addr = dbbean.getLab_address()+ "\n" + dbbean.getLab_city() + " - "+ dbbean.getLab_pinCode()+ "\n" + dbbean.getLab_state()+".";
				lab_addr.setText(addr);
				LL_for_labs_pin.addView(customLabsPinCode[i1]);
			}
			chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						int id= Integer.parseInt(String.valueOf(buttonView.getTag()));
						
						DiagnoLabsDataBean dbbean2 = new DiagnoLabsDataBean();
						dbbean = doBean.getSelectedLab();
						dbbean2 = dLabList2.get(id);
						dbbean.setLab_name(dbbean2.getLab_name());
						dbbean.setLab_address(dbbean2.getLab_address());
						dbbean.setLab_city(dbbean2.getLab_city());
						dbbean.setLab_state(dbbean2.getLab_state());
						dbbean.setLab_pinCode(dbbean2.getLab_pinCode());
						dbbean.setSelected(true);
						doBean.setSelectedLab(dbbean);
						Okler.getInstance().setDiagnoOrderDataBean(doBean);
						//dLabList.add(dbbean);
						Okler.getInstance().setDiagLabAddrListNear(dLabList2);
						isSelected=true;
						//book_appoint_btn_lab.setEnabled(true);
						
					}else{
						isSelected=false;
					}
					
				}
			});
			/*radio_grey.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					radio_grey.setVisibility(View.GONE);
					radio_red.setVisibility(View.VISIBLE);
					//radio_grey.setBackgroundResource(R.drawable.red_circle_with_white_icon);
					int id = Integer.parseInt(String.valueOf(v.getTag()));
					id = id -100;
					DiagnoLabsDataBean dbbean2 = new DiagnoLabsDataBean();
					dbbean = doBean.getSelectedLab();
					dbbean2 = dLabList2.get(id);
					dbbean.setLab_name(dbbean2.getLab_name());
					dbbean.setLab_address(dbbean2.getLab_address());
					dbbean.setLab_city(dbbean2.getLab_city());
					dbbean.setLab_state(dbbean2.getLab_state());
					dbbean.setLab_pinCode(dbbean2.getLab_pinCode());
					dbbean.setSelected(true);
					doBean.setSelectedLab(dbbean);
					Okler.getInstance().setDiagnoOrderDataBean(doBean);
					//dLabList.add(dbbean);
					Okler.getInstance().setDiagLabAddrListNear(dLabList2);
					book_appoint_btn_lab.setEnabled(true);
				}
			});*/
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				isAvailable=false;
			}
		}else{
			isAvailable=false;
			//book_appoint_btn_lab.setEnabled(false);
			et_pincode.requestFocus();
			et_pincode.setText("");
			et_pincode.setError("Enter another pincode");
			Toast.makeText(ack, "There are no labs available at given Pincode", 1500).show();
			
		}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				String resp = String.valueOf(error);
				isAvailable=false;
			}
		});
		VolleyRequest.addJsonObjectRequest(getApplicationContext(), pinjson);
		
	
	}
	
	private void setCustomViewsNearBy() {
		
		tList= labbean.getTestBean();
		String testId = "";
		String entity_price = "";
		String packId = "";
		int len = tList.size();
		for (int i = 0; i < len; i++) {
			TestDataBean tbean = new TestDataBean();
			tbean = tList.get(i);
			if(testId.equals("")){
				testId =String.valueOf(tbean.getTestId());	
			}else{
				testId = testId+","+tbean.getTestId();
			}
			if(entity_price.equals("")){
				entity_price =tbean.getOklerTestPrice()+"";
			}else{
			entity_price = entity_price+","+tbean.getOklerTestPrice();
			}
		}
		if(labbean.getPackageBean()!=null){
			dpbean = labbean.getPackageBean();
			packId = String.valueOf(dpbean.getPacakageId());
			
		}
		
		//pinCode = 506356;
		//labId = 7;
		getLabsForPincodeUrl = getLabs1+pinCode+getLabs2+labId+getLabs3+testId+getLabs4+packId;
		WebJsonObjectRequest pinjson = new WebJsonObjectRequest(Method.GET, getLabsForPincodeUrl, new JSONObject(), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
			String resp = String.valueOf(response);	
			
				if(response.optString("message").equals("Pincode Available")){
					isAvailable=true;
			try {
				JSONArray jarr = response.getJSONArray("result");
				JSONObject jobj1 = new JSONObject();
				DiagnoLabsDataBean dbean;
				dLabList = new ArrayList<DiagnoLabsDataBean>();
				int length = jarr.length();
				for (int i = 0; i < length; i++) {
					dbean = new DiagnoLabsDataBean();
					jobj1 = jarr.getJSONObject(i);
					dbean.setLabId(Integer.parseInt(jobj1.optString("lab_id")));
					dbean.setLab_name(jobj1.optString("lab_name"));
					dbean.setLab_address(jobj1.optString("lab_address"));
					dbean.setLab_city(jobj1.optString("lab_city_name"));
					dbean.setLab_state(jobj1.optString("lab_state_name"));
					dbean.setLab_pinCode(jobj1.optString("lab_branch_pin"));
					dLabList.add(dbean);
				}
			Okler.getInstance().setDiagLabAddressList(dLabList);	
			
			tdbean = doBean.getSelectedLab();
			
			tdbean.setLabId(dLabList.get(0).getLabId());
			tdbean.setLab_name(dLabList.get(0).getLab_name());
			tdbean.setLab_address(dLabList.get(0).getLab_address());
			tdbean.setLab_city(dLabList.get(0).getLab_city());
			tdbean.setLab_state(dLabList.get(0).getLab_state());
			tdbean.setLab_pinCode(dLabList.get(0).getLab_pinCode());
			doBean.setSelectedLab(tdbean);
			Okler.getInstance().setDiagnoOrderDataBean(doBean);
			
			int length2 = dLabList.size();
			
			
			for (int i1 = 0; i1 < length2; i1++) {
				dbbean = new DiagnoLabsDataBean();
				dbbean = dLabList.get(i1);
				customLabsNear[i1] = new CustomViewLabAddress(getApplicationContext());
				chk_n = (CheckBox)customLabsNear[i1].findViewById(R.id.chkbx_lab);
				chk_n.setTag(""+i1);
				//nradio_red = (ImageView)customLabsNear[i1].findViewById(R.id.radio_red);
				nlab_name = (TextView)customLabsNear[i1].findViewById(R.id.lab_name);
				nlab_addr = (TextView)customLabsNear[i1].findViewById(R.id.lab_addr);
				
				nlab_name.setText(dbbean.getLab_name()+":");
				String addr = dbbean.getLab_address()+ "\n" + dbbean.getLab_city() + " - "+ dbbean.getLab_pinCode()+ "\n" + dbbean.getLab_state()+".";
				nlab_addr.setText(addr);
				LL_for_labs_near.addView(customLabsNear[i1]);
			}
			chk_n.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						int id = Integer.parseInt(String.valueOf(buttonView.getTag()));
						DiagnoLabsDataBean dbbean2 = new DiagnoLabsDataBean();
						//dbbean = doBean.getSelectedLab();
						dbbean = new DiagnoLabsDataBean();
						dbbean2 = dLabList.get(id);
						//dbbean2 = dLabList2.get(id);
						dbbean.setLab_name(dbbean2.getLab_name());
						dbbean.setLab_address(dbbean2.getLab_address());
						dbbean.setLab_city(dbbean2.getLab_city());
						dbbean.setLab_state(dbbean2.getLab_state());
						dbbean.setLab_pinCode(dbbean2.getLab_pinCode());
						dbbean.setSelected(true);
						//dLabList.add(dbbean);
						doBean.setSelectedLab(dbbean);
						Okler.getInstance().setDiagnoOrderDataBean(doBean);
						Okler.getInstance().setDiagLabAddressList(dLabList);
						//book_appoint_btn_lab.setEnabled(true);
						isSelected=true;
						
					}else{
						isSelected=false;
					}
					
				}
			});
			/*nradio_grey.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					nradio_grey.setVisibility(View.GONE);
					nradio_red.setVisibility(View.VISIBLE);
					//radio_grey.setBackgroundResource(R.drawable.red_circle_with_white_icon);
					int id = Integer.parseInt(String.valueOf(v.getTag()));
					id = id -100;
					DiagnoLabsDataBean dbbean2 = new DiagnoLabsDataBean();
					//dbbean = doBean.getSelectedLab();
					dbbean = new DiagnoLabsDataBean();
					dbbean2 = dLabList.get(id);
					//dbbean2 = dLabList2.get(id);
					dbbean.setLab_name(dbbean2.getLab_name());
					dbbean.setLab_address(dbbean2.getLab_address());
					dbbean.setLab_city(dbbean2.getLab_city());
					dbbean.setLab_state(dbbean2.getLab_state());
					dbbean.setLab_pinCode(dbbean2.getLab_pinCode());
					dbbean.setSelected(true);
					//dLabList.add(dbbean);
					doBean.setSelectedLab(dbbean);
					Okler.getInstance().setDiagnoOrderDataBean(doBean);
					Okler.getInstance().setDiagLabAddressList(dLabList);
					book_appoint_btn_lab.setEnabled(true);
				}
			});*/
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				isAvailable=false;
			}
		}else{
			isAvailable=false;
		//book_appoint_btn_lab.setEnabled(false);
		}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				String resp = String.valueOf(error);
				isAvailable=false;
				
			}
		});
		VolleyRequest.addJsonObjectRequest(getApplicationContext(), pinjson);
	}

	protected void minusDate(TextView dateTv){
		long d = curCal.getTimeInMillis();
		long min = minCal.getTimeInMillis();
		if(min<d){
			curCal.add(Calendar.DATE, -1);
        formattedDate = df.format(curCal.getTime());
        dateTv.setText(formattedDate);
		}
	}

	protected void addDate(TextView dateTv){
		long d = curCal.getTimeInMillis();
		long max = maxCal.getTimeInMillis();
		/*int dm  = curCal.get(Calendar.MONTH);
		int maxm = maxCal.get(Calendar.MONTH);
		long l = curCal.getTimeInMillis();
		long l2 = maxCal.getTimeInMillis();*/
		if(max>d){
			curCal.add(Calendar.DATE, 1);
		formattedDate = df.format(curCal.getTime());
		dateTv.setText(formattedDate);	
			//setNextDate(dateTvhome);
		}
	}
	protected void setNextDate(TextView textView) {
		// TODO Auto-generated method stub
		String currentdate=textView.getText().toString();
		Date convertedDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    try {
			convertedDate = dateFormat.parse(currentdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Date currentdateFormat=currentdate;
		//calendar.add(convertedDate, 1);
		Date tomorrow = calendar.getTime();
		//Use SimpleDateFormat to format the Date as a String:
		
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String tomorrowAsString = dateFormat.format(tomorrow);
		
	}



	/*protected void setPreviousDate(TextView textView) {
		// TODO Auto-generated method stub
		
	}
*/


	private void setCurrentDate() {
		
		curCal = Calendar.getInstance();
		df = new SimpleDateFormat("yyyy-MM-dd");
		formattedDate = df.format(curCal.getTime());
		maxCal = Calendar.getInstance();
		maxCal.add(Calendar.DATE, 6);
		minCal = Calendar.getInstance();
		dateTvhome.setText(formattedDate);
      	dateTvLab.setText(formattedDate);
		
	}



	private void webServiceCallForTymslots(String tym_slotUrl) {
		// TODO Auto-generated method stub
		webobj=new WebJsonObjectRequest(Method.GET,tym_slotUrl,new JSONObject(),this,this,true);
		VolleyRequest.addJsonObjectRequest(context,webobj);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.diagno_schedule_pickup, menu);
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
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		String err=String.valueOf(error);
		System.out.println("VolleyError"+err);
		Toast.makeText(context,"Time-out,please try again",Toast.LENGTH_LONG).show();
	}



	@Override
	public void onResponse(Object response) {
		// TODO Auto-generated method stub
		displayTymslots(response);
	}



	private void displayTymslots(Object response) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		JSONObject jobj = (JSONObject)response;
		JSONObject innerObj = new JSONObject();
		JSONArray jarr = new JSONArray();
		try {
			jarr = jobj.getJSONArray("result");
			int len = jarr.length();
			for (int i = 0; i < jarr.length(); i++) {
				innerObj = jarr.getJSONObject(i);
				listTymSlotValue.add(innerObj.getString("solt_period"));
				}
					//set spinner tymslot list
				adapter= new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line,listTymSlotValue);
			    time_home.setAdapter(adapter);
				time_lab.setAdapter(adapter);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}
}
