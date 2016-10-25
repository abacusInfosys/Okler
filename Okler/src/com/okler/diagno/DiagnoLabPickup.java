package com.okler.diagno;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.okler.android.BaseActivity;
import com.okler.customviews.CustomViewDignoTimeSlots;
import com.okler.customviews.CustomViewLabAddress;
import com.okler.databeans.diagnobean.DiagnoLabBranchDataBean;
import com.okler.databeans.diagnobean.DiagnoPackagesDataBean;
import com.okler.databeans.diagnobean.DiagnoTestDataBean;
import com.okler.diagnostics.DiagnoOrderSummary;
import com.okler.enums.DiagnoOrderType;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DiagnoLabPickup extends BaseActivity {

	LinearLayout date_layout1, date_layout2, date_layout3, 
				 date_layout4, date_layout5, date_layout6, date_layout7,inner_lin_date1, inner_lin_date2,
				 inner_lin_date3, inner_lin_date4, inner_lin_date5, inner_lin_date6, inner_lin_date7,
				 linear_down_arrow1, linear_down_arrow2, linear_down_arrow3, linear_down_arrow4,
				 linear_down_arrow5, linear_down_arrow6, linear_down_arrow7,timeSlotsLL,LL_for_labs_near,
				 LL_for_labs_pin;
	TextView	 text_date1, text_date2, text_date3, text_date4, text_date5, text_date6, text_date7,
				no_lab_near,no_lab_pin;
	Activity ack;
	ArrayList<String> timeSlotsList = new ArrayList<String>();
	ArrayList<String> timeSlotListids = new ArrayList<String>();
	SimpleDateFormat sdf;
	Button btn_homepickup, txt_book_appointment,btn_labvisit;
	RelativeLayout RL_for_labs_addr;
	DiagnoOrderType diagnoOrderType;
	String testId="",pkgId="",labId="",pincode="",getLabsForPincodeUrl="";
	RelativeLayout locationRL;
	EditText pincode_ET;
	Toolbar toolBar;
	View bottomBarLayout;
	boolean isLabSelected=false;
	ArrayList<DiagnoLabBranchDataBean> labListNear,labListPin;
	Calendar curCal, minCal, cal1, cal2, cal;
	long d, min;
	String time;
	
	DiagnoLabBranchDataBean labbean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ack = this;
		setContentView(R.layout.activity_diagno_lab_pickup);
		findViews();
		setSupportActionBar(toolBar);
		handleMapping(bottomBarLayout);
		UIUtils.setBackClick(toolBar, ack);
		Utilities.setTitleText(toolBar, "Diagnostic Test [4/5]");
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		setDates(ack);
		webServiceCallForTimeSlots(getString(R.string.getAlltymslotsForLab));
		onClick(inner_lin_date1);
		Okler.getInstance().getDiagnoOrder().setPickupType("Home");
		labListNear = new ArrayList<DiagnoLabBranchDataBean>();
		labListPin = new ArrayList<DiagnoLabBranchDataBean>();

		curCal = Calendar.getInstance();
		minCal = Calendar.getInstance();
		diagnoOrderType = Okler.getInstance().getDiagnoOrder().getOrderType();
		
		if(diagnoOrderType.equals(DiagnoOrderType.TEST)){
			ArrayList<DiagnoTestDataBean> testList = 
					Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests();
			for(int i =0;i<testList.size();i++){
				if(i==0)
					testId = testList.get(i).getTestId()+"";
				else
					testId = testId + "," +
							 testList.get(i).getTestId();
				labId = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentLab().getLabId()+"";
			}
		}else{
			ArrayList<DiagnoPackagesDataBean> pkgList = 
					Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs();
			for (int i = 0; i < pkgList.size(); i++) {
				if(i==0)
					pkgId = pkgList.get(i).getPacakageId()+"";
				else
					pkgId = pkgId + "," +
							pkgList.get(i).getPacakageId();
				labId = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentLab().getLabId()+"";
			}
		}
		setLabsNear();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		
		switch (v.getId()) {
		case R.id.btn_homepickup:
			btn_homepickup.setBackgroundResource(R.drawable.homepickup_selected);
			Okler.getInstance().getDiagnoOrder().setPickupType("Home");
			btn_labvisit.setBackgroundResource(R.drawable.lab_visit_unselected);
			RL_for_labs_addr.setVisibility(View.GONE);
			break;
			
		case R.id.btn_labvisit:
			btn_labvisit.setBackgroundResource(R.drawable.lab_visit_selected);
			Okler.getInstance().getDiagnoOrder().setPickupType("Lab Visit");
			btn_homepickup.setBackgroundResource(R.drawable.homepickup_unselected);
			RL_for_labs_addr.setVisibility(View.VISIBLE);
			break;
			
		case R.id.inner_linlay_cal1:
			changeColor(v);
			break;
			
		case R.id.inner_linlay_cal2:
			changeColor(v);
			break;
			
		case R.id.inner_linlay_cal3:
			changeColor(v);
			break;
			
		case R.id.inner_linlay_cal4:
			changeColor(v);
			break;
			
		case R.id.inner_linlay_cal5:
			changeColor(v);
			break;
			
		case R.id.inner_linlay_cal6:
			changeColor(v);
			break;
			
		case R.id.inner_linlay_cal7:
			changeColor(v);
			break;
			
		case R.id.txt_book_appointment:
				if(Okler.getInstance().getDiagnoOrder().getPickupType().equals("Home"))
				{
					cal1 = cal;
					cal1.set(Calendar.HOUR, 00);
					cal1.set(Calendar.MINUTE, 00);
					cal1.set(Calendar.SECOND, 00);
					cal1.set(Calendar.MILLISECOND, 00);
					d = cal1.getTimeInMillis();
					cal2 = minCal;
					cal2.set(Calendar.HOUR, 00);
					cal2.set(Calendar.MINUTE, 00);
					cal2.set(Calendar.SECOND, 00);
					cal2.set(Calendar.MILLISECOND, 00);
					min = cal2.getTimeInMillis();
					if (min == d) 
					{
						String gottime = time;
						String arr1[] = gottime.split("-");
						String cmpTime = arr1[1];
						String arr2[] = cmpTime.split(" ");
						String time = arr2[1];
						String ampm = arr2[2];
						Calendar cal = Calendar.getInstance();
						int t = cal.get(Calendar.HOUR_OF_DAY);
						int t2 = Integer.parseInt(time);
						if (ampm.equals("PM"))
							t2 = t2 + 12;
						if (t < t2) 
						{
							Intent intent = new Intent(getApplicationContext(),
									DiagnoOrderSummary.class);
							intent.putExtra("PickupType", "Home");
							startActivity(intent);
						} 
						else 
						{
							Toast.makeText(ack, "Time you Have selected is already passed", 1500).show();
						}
					}
					else
					{
						Intent intent = new Intent(getApplicationContext(), DiagnoOrderSummary.class);
						intent.putExtra("PickupType", "Home");
						startActivity(intent);
					}
				}
			
			if(Okler.getInstance().getDiagnoOrder().getPickupType().equals("Lab Visit")){
				
				cal1 = cal;
				cal1.set(Calendar.HOUR, 00);
				cal1.set(Calendar.MINUTE, 00);
				cal1.set(Calendar.SECOND, 00);
				cal1.set(Calendar.MILLISECOND, 00);
				d = cal1.getTimeInMillis();

				cal2 = minCal;
				cal2.set(Calendar.HOUR, 00);
				cal2.set(Calendar.MINUTE, 00);
				cal2.set(Calendar.SECOND, 00);
				cal2.set(Calendar.MILLISECOND, 00);
				min = cal2.getTimeInMillis();
				if (min == d) 
				{
					String gottime = time;
					String arr1[] = gottime.split("-");
					String cmpTime = arr1[1];
					String arr2[] = cmpTime.split(" ");
					String time = arr2[1];
					String ampm = arr2[2];
					Calendar cal = Calendar.getInstance();
					int t = cal.get(Calendar.HOUR_OF_DAY);
					int t2 = Integer.parseInt(time);
					if (ampm.equals("PM"))
						t2 = t2 + 12;
					if (t > t2) 
					{
						Toast.makeText(ack, "Time you Have selected is already passed", 1500).show();
						break;
					} 
					else 
					{
						if(isLabSelected){
							Intent intent = new Intent(DiagnoLabPickup.this,DiagnoOrderSummary.class);
							startActivity(intent);
						}else{
							Toast.makeText(ack, "Please select a lab near you.", Toast.LENGTH_SHORT).show();
						}
						
					}
					}
				else
				{
					if(isLabSelected){
						Intent intent = new Intent(DiagnoLabPickup.this,DiagnoOrderSummary.class);
						startActivity(intent);
					}else{
						Toast.makeText(ack, "Please select a lab near you.", Toast.LENGTH_SHORT).show();
					}
				}

				}
			break;
		
		case R.id.locationRL:
			TextValidations tval = new TextValidations(pincode_ET);
			if(tval.validatePinCode("Enter correct pincode.")){
				setLabsForPin();
			}
			break;
		default:
			break;
		}
	}
	
	public void changeColor(View v)
	{	
		LinearLayout croppedLL = (LinearLayout)v;
		View calMainview = (View) v.getParent();
		LinearLayout calMain = (LinearLayout)calMainview;
		View mainCalendarView = (View)calMain.getParent();
		LinearLayout mainCalendarLL = (LinearLayout)mainCalendarView; 
		for (int i = 0; i < mainCalendarLL.getChildCount(); i++) {
			View calMainview1 = (View)mainCalendarLL.getChildAt(i);
			LinearLayout calMainLL1 = (LinearLayout)calMainview1;
			View cropV = (View)calMainLL1.getChildAt(0);
			LinearLayout croppedLL1 = (LinearLayout)cropV;
			View dArrV = (View)calMainLL1.getChildAt(1);
			LinearLayout dArrLL = (LinearLayout)dArrV;
			int tag = Integer.parseInt(calMainLL1.getTag().toString());
			
			if(tag%2 == 0){
				croppedLL1.setBackgroundResource(R.color.SuperLightGray);
				View rlV = croppedLL1.getChildAt(0);
				RelativeLayout rl = (RelativeLayout)rlV;
				View txtv = rl.getChildAt(0);
				TextView txt = (TextView)txtv;
				txt.setTextColor(getResources().getColor(R.color.Black));
				dArrLL.setVisibility(View.INVISIBLE);
			}else{
				croppedLL1.setBackgroundResource(R.color.LightestGray);
				View rlV = croppedLL1.getChildAt(0);
				RelativeLayout rl = (RelativeLayout)rlV;
				View txtv = rl.getChildAt(0);
				TextView txt = (TextView)txtv;
				txt.setTextColor(getResources().getColor(R.color.Black));
				dArrLL.setVisibility(View.INVISIBLE);
			}
		}	
		croppedLL.setBackgroundResource(R.drawable.cropped_image);
		View rlV = croppedLL.getChildAt(0);
		RelativeLayout rl = (RelativeLayout)rlV;
		View txtv = rl.getChildAt(0);
		TextView txt = (TextView)txtv;
		txt.setTextColor(getResources().getColor(R.color.White));
		View dArrV = (View)calMain.getChildAt(1);
		int tag = Integer.parseInt(calMain.getTag().toString());
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, tag);
		String date = sdf.format(cal.getTime());
		Okler.getInstance().getDiagnoOrder().setApptDt(date);
		LinearLayout dArrLL = (LinearLayout)dArrV;
		dArrLL.setVisibility(View.VISIBLE);
	}
	
	public void findViews(){
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		bottomBarLayout = findViewById(R.id.bottombar);
		
		date_layout1 = (LinearLayout) findViewById(R.id.calendar_date1);
		date_layout2 = (LinearLayout) findViewById(R.id.calendar_date2);
		date_layout3 = (LinearLayout) findViewById(R.id.calendar_date3);
		date_layout4 = (LinearLayout) findViewById(R.id.calendar_date4);
		date_layout5 = (LinearLayout) findViewById(R.id.calendar_date5);
		date_layout6 = (LinearLayout) findViewById(R.id.calendar_date6);
		date_layout7 = (LinearLayout) findViewById(R.id.calendar_date7);
		
		inner_lin_date1 = (LinearLayout) findViewById(R.id.inner_linlay_cal1);
		inner_lin_date2 = (LinearLayout) findViewById(R.id.inner_linlay_cal2);
		inner_lin_date3 = (LinearLayout) findViewById(R.id.inner_linlay_cal3);
		inner_lin_date4 = (LinearLayout) findViewById(R.id.inner_linlay_cal4);
		inner_lin_date5 = (LinearLayout) findViewById(R.id.inner_linlay_cal5);
		inner_lin_date6 = (LinearLayout) findViewById(R.id.inner_linlay_cal6);
		inner_lin_date7 = (LinearLayout) findViewById(R.id.inner_linlay_cal7);
		
		linear_down_arrow1 = (LinearLayout) findViewById(R.id.linear_down_arrow1);
		linear_down_arrow2 = (LinearLayout) findViewById(R.id.linear_down_arrow2);
		linear_down_arrow3 = (LinearLayout) findViewById(R.id.linear_down_arrow3);
		linear_down_arrow4 = (LinearLayout) findViewById(R.id.linear_down_arrow4);
		linear_down_arrow5 = (LinearLayout) findViewById(R.id.linear_down_arrow5);
		linear_down_arrow6 = (LinearLayout) findViewById(R.id.linear_down_arrow6);
		linear_down_arrow7 = (LinearLayout) findViewById(R.id.linear_down_arrow7);
		
		text_date1 = (TextView) findViewById(R.id.txt_date1);
		text_date2 = (TextView) findViewById(R.id.txt_date2);
		text_date3 = (TextView) findViewById(R.id.txt_date3);
		text_date4 = (TextView) findViewById(R.id.txt_date4);
		text_date5 = (TextView) findViewById(R.id.txt_date5);
		text_date6 = (TextView) findViewById(R.id.txt_date6);
		text_date7 = (TextView) findViewById(R.id.txt_date7);

		txt_book_appointment = (Button)findViewById(R.id.txt_book_appointment);
		btn_homepickup = (Button)findViewById(R.id.btn_homepickup);
		btn_labvisit = (Button)findViewById(R.id.btn_labvisit);
		RL_for_labs_addr = (RelativeLayout)findViewById(R.id.RL_for_labs_addr);
		LL_for_labs_near = (LinearLayout)findViewById(R.id.LL_for_labs_near);
		LL_for_labs_pin = (LinearLayout)findViewById(R.id.LL_for_labs_pin);
		no_lab_near = (TextView)findViewById(R.id.no_lab_near);
		no_lab_pin = (TextView)findViewById(R.id.no_lab_pin);
		locationRL = (RelativeLayout)findViewById(R.id.locationRL);
		pincode_ET = (EditText)findViewById(R.id.pincode_ET);

		LL_for_labs_pin.removeAllViews();
		inner_lin_date1.setOnClickListener(this);
		inner_lin_date2.setOnClickListener(this);
		inner_lin_date3.setOnClickListener(this);
		inner_lin_date4.setOnClickListener(this);
		inner_lin_date5.setOnClickListener(this);
		inner_lin_date6.setOnClickListener(this);
		inner_lin_date7.setOnClickListener(this);
		btn_homepickup.setOnClickListener(this);
		btn_labvisit.setOnClickListener(this);
		txt_book_appointment.setOnClickListener(this);
		locationRL.setOnClickListener(this);
		timeSlotsLL = (LinearLayout)findViewById(R.id.timeSlotsLL);
	}
	
	public void setDates(Activity ack){
		Calendar now1 = Calendar.getInstance();
		Calendar now2 = Calendar.getInstance();
		Calendar now3 = Calendar.getInstance();
		Calendar now4 = Calendar.getInstance();
		Calendar now5 = Calendar.getInstance();
		Calendar now6 = Calendar.getInstance();
		Calendar now7 = Calendar.getInstance();
		now2.add(Calendar.DATE, 1);
		now3.add(Calendar.DATE, 2);
		now4.add(Calendar.DATE, 3);
		now5.add(Calendar.DATE, 4);
		now6.add(Calendar.DATE, 5);
		now7.add(Calendar.DATE, 6);
		
		text_date1.setText(""+now1.get(Calendar.DATE)+
				"\n"+UIUtils.getMonthOfYear(now1.get(Calendar.MONTH))+
				"\n"+UIUtils.getDayOfWeek(now1.get(Calendar.DAY_OF_WEEK)));
		text_date2.setText(""+now2.get(Calendar.DATE)+
				"\n"+UIUtils.getMonthOfYear(now2.get(Calendar.MONTH))+
				"\n"+UIUtils.getDayOfWeek(now2.get(Calendar.DAY_OF_WEEK)));
		text_date3.setText(""+now3.get(Calendar.DATE)+
				"\n"+UIUtils.getMonthOfYear(now3.get(Calendar.MONTH))+
				"\n"+UIUtils.getDayOfWeek(now3.get(Calendar.DAY_OF_WEEK)));
		text_date4.setText(""+now4.get(Calendar.DATE)+
				"\n"+UIUtils.getMonthOfYear(now4.get(Calendar.MONTH))+
				"\n"+UIUtils.getDayOfWeek(now4.get(Calendar.DAY_OF_WEEK)));
		text_date5.setText(""+now5.get(Calendar.DATE)+
				"\n"+UIUtils.getMonthOfYear(now5.get(Calendar.MONTH))+
				"\n"+UIUtils.getDayOfWeek(now5.get(Calendar.DAY_OF_WEEK)));
		text_date6.setText(""+now6.get(Calendar.DATE)+
				"\n"+UIUtils.getMonthOfYear(now6.get(Calendar.MONTH))+
				"\n"+UIUtils.getDayOfWeek(now6.get(Calendar.DAY_OF_WEEK)));
		text_date7.setText(""+now7.get(Calendar.DATE)+
				"\n"+UIUtils.getMonthOfYear(now7.get(Calendar.MONTH))+
				"\n"+UIUtils.getDayOfWeek(now7.get(Calendar.DAY_OF_WEEK)));
	}
	
	private void webServiceCallForTimeSlots(String tym_slotUrl) {
		WebJsonObjectRequest getTimeSlots = new WebJsonObjectRequest(Method.GET, tym_slotUrl, new JSONObject(), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				JSONArray jarr = new JSONArray();
				JSONObject innerObj = new JSONObject();
				jarr = response.optJSONArray("result");
				timeSlotsList = new ArrayList<String>();
				for (int i = 0; i < jarr.length(); i++) {
					innerObj = jarr.optJSONObject(i);
					timeSlotsList.add(innerObj.optString("solt_period"));
					timeSlotListids.add(innerObj.optString("id"));
				}
				setTimeSlotUi();
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Error", error.toString());
			}
		},true);
		VolleyRequest.addJsonObjectRequest(ack, getTimeSlots);
	}
	
	public void setTimeSlotUi(){
		CustomViewDignoTimeSlots[] cvSlots = new CustomViewDignoTimeSlots[50];
		
		for (int i = 0; i < timeSlotsList.size(); i++) {
			cvSlots[i] = new CustomViewDignoTimeSlots(ack);
			TextView timeSlotTxt = (TextView)cvSlots[i].findViewById(R.id.timeSlotTxt);
			timeSlotTxt.setText(timeSlotsList.get(i));
			cvSlots[i].setTag(i);
			cvSlots[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					callTimeSlotOnClick(v);
				}
			});
			timeSlotsLL.addView(cvSlots[i]);
		}
		callTimeSlotOnClick(cvSlots[0]);
	}
	
	private void callTimeSlotOnClick(View v){
		LinearLayout ll = (LinearLayout)v.getParent();
		int a = ll.getChildCount();
		for (int j = 0; j < a; j++) {
			CustomViewDignoTimeSlots rl2 = (CustomViewDignoTimeSlots)ll.getChildAt(j);
			ImageView chkBoxTimeSlot = (ImageView)rl2.findViewById(R.id.chkBoxTimeSlot);
			chkBoxTimeSlot.setBackgroundResource(R.drawable.grey_radio_btn);
		}
		CustomViewDignoTimeSlots rl = (CustomViewDignoTimeSlots)v;
		ImageView chkBoxTimeSlot = (ImageView)rl.findViewById(R.id.chkBoxTimeSlot);
		chkBoxTimeSlot.setBackgroundResource(R.drawable.red_circle_with_white_icon);
		Okler.getInstance().getDiagnoOrder().setAppTimeSlotId
		(Integer.parseInt(timeSlotListids.get(Integer.parseInt(v.getTag().toString()))));
		Okler.getInstance().getDiagnoOrder().setAppTime(timeSlotsList.get
				(Integer.parseInt(v.getTag().toString())));
		time = Okler.getInstance().getDiagnoOrder().getAppTime();
	}
	
	public void setLabsNear(){
		
		pincode = Okler.getInstance().getDiagnoOrder().getPatientAddr().getZip();
		
		getLabsForPincodeUrl = getString(R.string.getLabsForPinPart1) 
				+ pincode 
				+ getString(R.string.getLabsForPinPart2) 
				+ labId 
				+ getString(R.string.getLabsForPinPart3)
				+ testId 
				+ getString(R.string.getLabsForPinPart4) 
				+ pkgId;
		WebJsonObjectRequest getLabsNear = new WebJsonObjectRequest(Method.GET, getLabsForPincodeUrl, new JSONObject(), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.e("Response", response.toString());
				JSONArray result = response.optJSONArray("result");
				if(result!=null){
					LL_for_labs_near.removeAllViews();
					for (int i = 0; i < result.length(); i++) {
						JSONObject obj1 = result.optJSONObject(i);
						if(obj1!=null){
							labListNear = new ArrayList<DiagnoLabBranchDataBean>();
							JSONArray labArr = obj1.optJSONArray("lab_branches");
							for(int  j = 0;j<labArr.length();j++){
								JSONObject labobj = labArr.optJSONObject(j);
								labbean = new DiagnoLabBranchDataBean();
								labbean.setAddr1(labobj.optString("lab_address_branch"));
								labbean.setBranchId(Integer.parseInt(labobj.optString("lab_branch_id")));
								labbean.setLab_name(obj1.optString("lab_name"));
								labListNear.add(labbean);
							}
						}
					}
					setLabsNearUi();
				}else{
					LL_for_labs_near.removeAllViews();
					LL_for_labs_near.addView(no_lab_near);
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Error", error.toString());
				LL_for_labs_near.removeAllViews();
				LL_for_labs_near.addView(no_lab_near);
			}
		});
	VolleyRequest.addJsonObjectRequest(ack, getLabsNear);	
	}
	
	public void setLabsForPin(){
		
		pincode = pincode_ET.getText().toString();
		if(pincode==null||pincode.equals(""))
			pincode="1";
		
		getLabsForPincodeUrl = getString(R.string.getLabsForPinPart1) 
				+ pincode 
				+ getString(R.string.getLabsForPinPart2) 
				+ labId 
				+ getString(R.string.getLabsForPinPart3)
				+ testId 
				+ getString(R.string.getLabsForPinPart4) 
				+ pkgId;
		WebJsonObjectRequest getLabsPin = new WebJsonObjectRequest(Method.GET, getLabsForPincodeUrl, new JSONObject(), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.e("Response", response.toString());
				JSONArray result = response.optJSONArray("result");
				if(result!=null){
					LL_for_labs_pin.removeAllViews();
					for (int i = 0; i < result.length(); i++) {
						JSONObject obj1 = result.optJSONObject(i);
						if(obj1!=null){
							labListPin = new ArrayList<DiagnoLabBranchDataBean>();
							JSONArray labArr = obj1.optJSONArray("lab_branches");
							for(int  j = 0;j<labArr.length();j++){
								JSONObject labobj = labArr.optJSONObject(j);
							labbean = new DiagnoLabBranchDataBean();
							labbean.setAddr1(labobj.optString("lab_address_branch"));
							labbean.setBranchId(Integer.parseInt(labobj.optString("lab_branch_id")));
							labbean.setLab_name(obj1.optString("lab_name"));
							labListPin.add(labbean);
							}
						}
					}
					setLabsPinUi();
				}else{
					LL_for_labs_pin.removeAllViews();
					LL_for_labs_pin.addView(no_lab_pin);
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Error", error.toString());
				LL_for_labs_pin.removeAllViews();
				LL_for_labs_pin.addView(no_lab_pin);
			}
		});
	VolleyRequest.addJsonObjectRequest(ack, getLabsPin);	
	}
	
	public void setLabsPinUi(){
		if(labListPin.size()>0){
		CustomViewLabAddress labAddress2[] = new CustomViewLabAddress[labListPin.size()];
		LL_for_labs_pin.removeAllViews();
		for(int i=0;i<labListPin.size();i++){
			labAddress2[i] = new CustomViewLabAddress(ack);
			ImageView radio_grey_pin = (ImageView)labAddress2[i].findViewById(R.id.radio_grey);
			TextView lab_name_pin = (TextView)labAddress2[i].findViewById(R.id.lab_name);
			TextView lab_addr_pin = (TextView)labAddress2[i].findViewById(R.id.lab_addr);
			radio_grey_pin.setTag(i);
			labAddress2[i].setTag(i);
			lab_name_pin.setText(labListPin.get(i).getLab_name());
			lab_addr_pin.setText(labListPin.get(i).getAddr1());
			labAddress2[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					for(int i=0;i<LL_for_labs_pin.getChildCount();i++){
						CustomViewLabAddress cvAddr = (CustomViewLabAddress)LL_for_labs_pin.getChildAt(i);
						ImageView img = (ImageView)cvAddr.findViewById(R.id.radio_grey);
						img.setBackgroundResource(R.drawable.grey_radio_btn);
					}
					int tag = Integer.parseInt(v.getTag().toString());
					ImageView img = (ImageView)v.findViewById(R.id.radio_grey);
					img.setBackgroundResource(R.drawable.red_circle_with_white_icon);
					if(diagnoOrderType.equals(DiagnoOrderType.TEST)){
						Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentLab().setAddr1(labListPin.get(tag).getAddr1());
						Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentLab().setBranchId(labListPin.get(tag).getBranchId());
					}else{
						Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentLab().setAddr1(labListPin.get(tag).getAddr1());
						Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentLab().setBranchId(labListPin.get(tag).getBranchId());
						}
					isLabSelected=true;
					setLabsNearUi();
				}
			});
			LL_for_labs_pin.addView(labAddress2[i]);
		}
		}else{
			LL_for_labs_pin.removeAllViews();
			LL_for_labs_pin.addView(no_lab_pin);
		}
	}
	public void setLabsNearUi(){
		if(labListNear.size()>0){
		CustomViewLabAddress labAddress[] = new CustomViewLabAddress[labListNear.size()];
		LL_for_labs_near.removeAllViews();
		for(int i=0;i<labListNear.size();i++){
			labAddress[i] = new CustomViewLabAddress(ack);
			ImageView radio_grey_near = (ImageView)labAddress[i].findViewById(R.id.radio_grey);
			TextView lab_name_near = (TextView)labAddress[i].findViewById(R.id.lab_name);
			TextView lab_addr_near = (TextView)labAddress[i].findViewById(R.id.lab_addr);
			lab_name_near.setText(labListNear.get(i).getLab_name());
			lab_addr_near.setText(labListNear.get(i).getAddr1());
			radio_grey_near.setTag(i);
			labAddress[i].setTag(i);
			labAddress[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					for(int i=0;i<LL_for_labs_near.getChildCount();i++){
						CustomViewLabAddress cvAddr = (CustomViewLabAddress)LL_for_labs_near.getChildAt(i);
						ImageView img = (ImageView)cvAddr.findViewById(R.id.radio_grey);
						img.setBackgroundResource(R.drawable.grey_radio_btn);
					}
					int tag = Integer.parseInt(v.getTag().toString());
					ImageView img = (ImageView)v.findViewById(R.id.radio_grey);
					img.setBackgroundResource(R.drawable.red_circle_with_white_icon);
					if(diagnoOrderType.equals(DiagnoOrderType.TEST)){
						Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentLab().setAddr1(labListNear.get(tag).getAddr1());
						Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentLab().setBranchId(labListNear.get(tag).getBranchId());
					}else{
						Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentLab().setAddr1(labListNear.get(tag).getAddr1());
						Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentLab().setBranchId(labListNear.get(tag).getBranchId());
					}
					isLabSelected=true;
					setLabsPinUi();
				}
			});
			LL_for_labs_near.addView(labAddress[i]);
		}
		}else{
			LL_for_labs_near.removeAllViews();
			LL_for_labs_near.addView(no_lab_near);
		}
	}
}