package com.okler.Profile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.okler.android.BaseActivity;
import com.okleruser.R;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.diagnostics.SelectPatientInfoActivity;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileNewAddress extends BaseActivity {
	
	Button button_saveinfo;
	Toolbar toolBar;
	View bottomBarLayout;
	ImageView imgBack;
	ArrayList<String> relation = new ArrayList<String>();
	ArrayList<String> relation_id = new ArrayList<String>();
	ArrayList<String> cities = new ArrayList<String>();
	ArrayList<String> city_id_list = new ArrayList<String>();
	//	ArrayList<Integer> city_id = new ArrayList<Integer>();
	ArrayList<String> states= new ArrayList<String>();
	ArrayList<String> state_id_list= new ArrayList<String>();
	int state_id2;
	String city,bcity,cityId,bcityId;
	int cid,bcid;
	int pat_id;
	JSONObject addbean;
	int relation_id1;
	WebJsonObjectRequest webjson;
	CheckBox checkbill,checkship;
	String addrerss_id;
	int addr_id, bill, ship, user_id, city_id;
	String state_id;
		String adId;
	String add_address;
	int rel_id;
	String[] gender = new String[]{"Gender","Male", "Female"};
	
	EditText nameTV, surnameTV, addr1TV, addr2TV, landmarkTV, pincodeTV, mobileTV;
	TextView  dobTV;
	AutoCompleteTextView cityTV,stateTv;
	Calendar newCalendar;
	
	
	Spinner spinnerRelation, spinnerGender;
	
	int content;
	
	Activity act;
	ArrayAdapter<String> adapter,adapter2,adapterSt;
	boolean flag =false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profile_new_address);
		Okler.getInstance().getStates().clear();
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		button_saveinfo = (Button)findViewById(R.id.button_saveinfo);
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		final ActionBar ab=getSupportActionBar();
		
		relation.add("Relation");
		content = getIntent().getIntExtra("int value", 0);
		
		nameTV = (EditText) findViewById(R.id.nameTv);
		surnameTV = (EditText) findViewById(R.id.surnameTv);
		dobTV = (TextView) findViewById(R.id.dobTv);
		addr1TV = (EditText) findViewById(R.id.addr1Tv);
		addr2TV = (EditText) findViewById(R.id.addr2Tv);
		landmarkTV = (EditText) findViewById(R.id.landmarkTv);
		pincodeTV = (EditText) findViewById(R.id.pincodeTv);
		mobileTV = (EditText) findViewById(R.id.mobileNoTv);
		cityTV = (AutoCompleteTextView) findViewById(R.id.cityTv);
		stateTv = (AutoCompleteTextView) findViewById(R.id.stateTv);
		act = this;
		
		checkbill = (CheckBox) findViewById(R.id.checkBox_bill_addr);
		checkship = (CheckBox) findViewById(R.id.checkBox_delivery_addr);
		
		checkbill.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
			}
		});
		
		checkship.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
			}
		});
		
		spinnerRelation = (Spinner) findViewById(R.id.relationSpinner);
		spinnerGender = (Spinner) findViewById(R.id.genderSpinner);
		
		button_saveinfo = (Button) findViewById(R.id.button_saveinfo);
		
		boolean flag = getIntent().getBooleanExtra("title", false);
		if(flag == true)
		{
			TextView text_title = (TextView) toolBar.findViewById(R.id.toolbar_title);
			text_title.setText("Edit Your Address");
		}
		
		stateTv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				getCity(stateTv);
			}
		});
		
		cityTV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				city = cityTV.getText().toString();
			}
		});
		dobTV.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				DatePickerDialog fromDatePickerDialog;
			    final SimpleDateFormat dateFormatter;
			    dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);	        
			    newCalendar = Calendar.getInstance();	
			    newCalendar.set(Calendar.HOUR, 00);
			    newCalendar.set(Calendar.MINUTE, 00);
			    newCalendar.set(Calendar.SECOND, 00);
			    newCalendar.set(Calendar.MILLISECOND, 00);
				dobTV.setText("");
				fromDatePickerDialog = new DatePickerDialog(act, new OnDateSetListener() 
				{
					 
				     public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
				    {
				         Calendar newDate = Calendar.getInstance();
				         newDate.set(year, monthOfYear, dayOfMonth);
				         newDate.set(Calendar.HOUR, 00);
				         newDate.set(Calendar.MINUTE, 00);
				         newDate.set(Calendar.SECOND, 00);
				         newDate.set(Calendar.MILLISECOND, 00);
				         if((newDate.compareTo(newCalendar)==-1)||(newDate.compareTo(newCalendar)==0)){	
				         dobTV.setText(dateFormatter.format(newDate.getTime()));
				         }else{
				        	 dobTV.setText("");
				        	 Toast.makeText(getApplicationContext(), "Invalid Date", Toast.LENGTH_LONG).show();
				         }
				    } 
				},newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
					    fromDatePickerDialog.show();
				
				
			}
		});
		
		getRelation();
		
		
		addbean = null;
		
		button_saveinfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
								
				switch (content) 
				{
				case 1:
					
				//	Toast.makeText(getApplicationContext(), "SAVGE CLICKED", Toast.LENGTH_LONG).show();	
					
					addNewAddress();
					
					break;
					
				case 2:
					
				//	Toast.makeText(getApplicationContext(), "SAVGE CLICKED", Toast.LENGTH_LONG).show();
					editAddress();
					break;
				}		
				
			}
		});
		
		JSONObject addbean = null;
		
		content = getIntent().getIntExtra("int value", 0);
		if(content == 2)
		{
			
			String add = getIntent().getStringExtra("addressbean");
			try {
				addbean = new JSONObject(add);
				nameTV.setText(addbean.optString("firstname"));
				surnameTV.setText(addbean.optString("lastname"));
				addr1TV.setText(addbean.optString("address1"));
				addr2TV.setText(addbean.optString("address2"));
				dobTV.setText(addbean.optString("dob"));
				mobileTV.setText(addbean.optString("phone"));
				landmarkTV.setText(addbean.optString("landmark"));
				pincodeTV.setText(addbean.optString("zip"));
				cityTV.setText(addbean.optString("city"));
				stateTv.setText(addbean.optString("state"));
				addrerss_id = addbean.optString("addr_id");
				pat_id = addbean.optInt("pat_id");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		
		}		
		getStates();
		states = Okler.getInstance().getStates();
		adapterSt = new ArrayAdapter<String>(act, android.R.layout.simple_dropdown_item_1line,states);
		stateTv.setAdapter(adapterSt);
		
		//ab.setDisplayHomeAsUpEnabled(true);
		//Okler.getInstance().setBookingType(9);
		int a = Okler.getInstance().getBookingType();
		if(Okler.getInstance().getBookingType()==9||Okler.getInstance().getBookingType()==20){
			toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
			Utilities.setTitleText(toolBar, "Patient Information");
			button_saveinfo.setBackgroundColor(getResources().getColor(R.color.ModerateMagenta));
			button_saveinfo.setText("SAVE INFORMATION & CONTINUE");
			checkbill.setVisibility(View.INVISIBLE);
			checkship.setVisibility(View.INVISIBLE);
		}else{
		toolBar.setBackgroundColor(getResources().getColor(R.color.Blue));
		Utilities.setTitleText(toolBar, "Add a New Address");
		}
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(imgBack.getWindowToken(), 0);
			}
		});	
			
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, gender);
			spinnerGender.setAdapter(adapter);
			
			/*stateTv.setOnFocusChangeListener(new OnFocusChangeListener() 
			{
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) 
				{
					// TODO Auto-generated method stub
					String city = stateTv.getText().toString();
					if(city.equals(" ") != false)
					{
						Toast.makeText(getApplicationContext(), "city is:"+city, Toast.LENGTH_LONG).show();
						int id1 = states.indexOf(city);
						Toast.makeText(getApplicationContext(), "id1:"+id1, Toast.LENGTH_LONG).show();
						state_id = state_id_list.get(id1);						
						Toast.makeText(getApplicationContext(), "city id:"+state_id, Toast.LENGTH_LONG).show();
					
					}
				}
			});*/
			
	}
	
	public void addNewAddress()
	{
		/*city = cityTV.getText().toString();
		
		if(!(cities.size()<=0)){
	  		cid = cities.indexOf(city);
	  		if(!(city_id_list.size()<=0))
			cityId = city_id_list.get(cid);
	  		}*/

		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(act);
		
		user_id = ubean.getId();
		
		String name="", surname="", dob="",address1="",address2="",landmark1="",name1="",surname1="", add1="", add2="", relation="", gender="", landmark="", pincode="", mobile="", city="",state="";
		
		TextValidations tval;
		UIUtils uiUtils;
		
		flag =false;
			tval = new TextValidations(nameTV);
		if(tval.ValidateMinimumChars("Enter a valid name", 3)){
			name = nameTV.getText().toString();
			tval = new TextValidations(surnameTV);
		if(tval.ValidateMinimumChars("Enter a Valid Surname", 3)){
			surname = surnameTV.getText().toString();
			String dob2 = dobTV.getText().toString();
		if((dob2.equals(""))){
			Toast.makeText(act, "Please Enter Your Birthdate", Toast.LENGTH_LONG).show();
		}else{
			dob = dobTV.getText().toString();
			tval = new TextValidations(addr1TV);
		if(tval.validateText("Please Enter a valid Address")){
			add1 = addr1TV.getText().toString();
			tval = new TextValidations(addr2TV);
		if(tval.validateText("Please Enter a valid Address")){
			add2 = addr2TV.getText().toString();
			uiUtils = new UIUtils(spinnerRelation);
		if(uiUtils.validateSpinner("Please Select Relation", "Relation", spinnerRelation.getSelectedItem().toString(),act)){
			relation = (String) spinnerRelation.getSelectedItem();
			uiUtils = new UIUtils(spinnerGender);
		if(uiUtils.validateSpinner("Please Select Gender", "Gender", spinnerGender.getSelectedItem().toString(),act)){
			gender = (String) spinnerGender.getSelectedItem();
			tval = new TextValidations(landmarkTV);
		if(tval.validateText("Please Enter a Landmark")){
			landmark = landmarkTV.getText().toString();
			tval = new TextValidations(pincodeTV);
		if(tval.validatePinCode("Please Enter a Valid Pincode")){
			pincode = pincodeTV.getText().toString();
			tval = new TextValidations(mobileTV);
		if(tval.validateMobile("Please Enter a Valid Mobine number")){
			mobile = mobileTV.getText().toString();
			uiUtils = new UIUtils(stateTv);
			String st = stateTv.getText().toString();
		if(uiUtils.validateState("Please Enter a Valid State", st, act)){
			uiUtils = new UIUtils(cityTV);
			String ct = cityTV.getText().toString();
		if(uiUtils.validateCity("Please Enter a Valid City", ct, act)){
			city = cityTV.getText().toString();
			cities = Okler.getInstance().getCities();
			if(!(cities.size()<=0)){
				int cid = cities.indexOf(city);
			city_id_list = Okler.getInstance().getCiti_ids();	
				if(!(city_id_list.size()<=0))
				city_id = Integer.parseInt(city_id_list.get(cid));
				//adbean.setCity_id(city_id);
				//city = city_id;
				flag = true;
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		try {
			address1= URLEncoder.encode(add1, "UTF-8");
			address2= URLEncoder.encode(add2, "UTF-8");
			landmark1 = URLEncoder.encode(landmark, "UTF-8");
			name1= URLEncoder.encode(name, "UTF-8");
			surname1 = URLEncoder.encode(surname, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	if(Okler.getInstance().getBookingType()==9||Okler.getInstance().getBookingType()==20){
			add_address = "https://www.okler.com/api/users/AdduserPatientAddress?user_id=" + user_id  
					+"&firstname=" + name1 +"&surname=" + surname1 + "&dob=" + dob + "&relation_id=" + rel_id + "&gender=" + "m"+ "" 
					+ "&mobileNo=" + mobile + "&addr1=" + address1 + "&addr2=" + address2 + "&land_mark=" + landmark1 + "&pincode="+pincode
					+ "&city_id=" + city_id;
		}else{
		 add_address = getResources().getString(R.string.add_patient_address) + "customer_id="+ user_id + "&company="+ "" 
				+"&customer_name=" + name +"&surname=" + surname + "&dob=" + dob + "&relation_id=" + rel_id + "&gender=" + "m"+ "" 
				+"&email=" + "" + "&mobileNo=" + mobile + "&addr1=" + add1 + "&addr2=" + add2 + "&landmark=" + "" + "&zip="+pincode
				+ "&country_id=" + "" + "&zone_id=" + "" + "&state_id=" +state_id+ "&city_id=" +city_id; 
	}
		if(flag){
			flag = false;
		WebJsonObjectRequest webjson =new WebJsonObjectRequest(Method.GET, add_address, new JSONObject(),new Listener<JSONObject>() 
				{
					@Override
					public void onResponse(JSONObject response) 
					{
						//Toast.makeText(getApplicationContext(), "response", Toast.LENGTH_LONG).show();
						
						try
						{
						JSONObject responseObj =(JSONObject)response;
						JSONObject doctorsArr = responseObj.getJSONObject("result");
						//Toast.makeText(getApplicationContext(), "try", Toast.LENGTH_LONG).show();
						String messaqge = doctorsArr.getString("message");
						if(Okler.getInstance().getBookingType()==9||Okler.getInstance().getBookingType()==20){
							Intent	intent = new Intent(ProfileNewAddress.this,SelectPatientInfoActivity.class);
							startActivity(intent);
						}else{
						addr_id = doctorsArr.getInt("addr_id");
						}
						if(messaqge.equals("inserted successfully"))
						{
							if(checkbill.isChecked() == true)
							{
								bill = addr_id;
							}
							if(checkship.isChecked() == true)
							{
								ship = addr_id;
							}
							String default_bill_ship_url = "https://www.okler.com/api/users/defaultBillingShippinguserAddress?cust_id="+user_id+"&ship_id="+ship+"&bill_id="+bill;
							
							WebJsonObjectRequest webjson=new WebJsonObjectRequest(Method.GET, default_bill_ship_url, new JSONObject(),new Listener<JSONObject>() 
									{
										@Override
										public void onResponse(JSONObject response) 
										{
											// TODO Auto-generated method stub
											
											try
											{
											JSONObject responseObj =(JSONObject)response;
											//String result = responseObj.getString("result");
											String message = responseObj.getString("result");
											//Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
											
											}catch(JSONException jsonEx)
											{
												Log.e("Exception json", jsonEx.getStackTrace().toString());
											}
									
										}}, 
										new Response.ErrorListener() 
										{

											@Override
											public void onErrorResponse(VolleyError error) 
											{
												Log.i("error", String.valueOf(error));
												// TODO Auto-generated method stub
									
											}
										}
							);
						
					VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
						}
						
						
				//		Toast.makeText(getApplicationContext(), messaqge, Toast.LENGTH_LONG).show();
						
						//docCount=responseObj.getInt("TotalCount");
						
						}catch(JSONException jsonEx)
						{
							Log.e("Exception json", jsonEx.getStackTrace().toString());
						}
						Intent intent = null;
						if(Okler.getInstance().getBookingType()==9||Okler.getInstance().getBookingType()==20){
							intent = new Intent(ProfileNewAddress.this,SelectPatientInfoActivity.class);
							startActivity(intent);
						}else{
						intent = new Intent(ProfileNewAddress.this,MyProfileActivity.class);
						startActivity(intent);
						}
					}
					}, 
					new Response.ErrorListener() 
					{

						@Override
						public void onErrorResponse(VolleyError error) 
						{
						
						//	Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
							Log.i("error", String.valueOf(error));
							// TODO Auto-generated method stub
						}
					}
		);
			
		VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
		}else{
		Toast.makeText(act, "Please Enter Valid Address", Toast.LENGTH_LONG).show();
		}
	}
	
	public void editAddress()
	{
		/*city = cityTV.getText().toString();
		
		if(!(cities.size()<=0)){
	  		cid = cities.indexOf(city);
			if(!(city_id_list.size() <=0))
				cityId = city_id_list.get(cid);
	  		}*/
		
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(act);
		user_id = ubean.getId();
		String name="", surname="", dob="", add1="", add2="",address1="",address2="",landmark1="",name1="",surname1="", relation="", gender="", landmark="", pincode="", mobile="", city="";
		
		TextValidations tval;
		UIUtils uiUtils;
		
		flag =false;
			tval = new TextValidations(nameTV);
		if(tval.ValidateMinimumChars("Enter a valid name", 3)){
			name = nameTV.getText().toString();
			tval = new TextValidations(surnameTV);
		if(tval.ValidateMinimumChars("Enter a Valid Surname", 3)){
			surname = surnameTV.getText().toString();
			String dob2 = dobTV.getText().toString();
		if((dob2.equals(""))){
			Toast.makeText(act, "Please Enter Your Birthdate", Toast.LENGTH_LONG).show();
		}else{
			dob = dobTV.getText().toString();
			tval = new TextValidations(addr1TV);
		if(tval.validateText("Please Enter a valid Address")){
			add1 = addr1TV.getText().toString();
			tval = new TextValidations(addr2TV);
		if(tval.validateText("PPlease Enter a valid Address")){
			add2 = addr2TV.getText().toString();
			uiUtils = new UIUtils(spinnerRelation);
		if(uiUtils.validateSpinner("Please Select Relation", "Relation", spinnerRelation.getSelectedItem().toString(),act)){
			relation = (String) spinnerRelation.getSelectedItem();
			this.relation = Okler.getInstance().getRelationList();
			if(!(this.relation.size()<=0)){
				int rid = this.relation.indexOf(relation);
			}
			relation_id = Okler.getInstance().getRelationIdList();
				if(!(relation_id.size()<=0))
				rel_id = Integer.parseInt(relation_id.get(cid));
			uiUtils = new UIUtils(spinnerGender);
		if(uiUtils.validateSpinner("Please Select Gender", "Gender", spinnerGender.getSelectedItem().toString(),act)){
			gender = (String) spinnerGender.getSelectedItem();
			tval = new TextValidations(landmarkTV);
		if(tval.validateText("Please Enter a Landmark")){
			landmark = landmarkTV.getText().toString();
			tval = new TextValidations(pincodeTV);
		if(tval.validatePinCode("Please Enter a Valid Pincode")){
			pincode = pincodeTV.getText().toString();
			tval = new TextValidations(mobileTV);
		if(tval.validateMobile("Please Enter a Valid Mobine number")){
			mobile = mobileTV.getText().toString();
			uiUtils = new UIUtils(stateTv);
			String st = stateTv.getText().toString();
		if(uiUtils.validateState("Please Enter a Valid State", st, act)){
			uiUtils = new UIUtils(cityTV);
			String ct = cityTV.getText().toString();
		if(uiUtils.validateCity("Please Enter a Valid City", ct, act)){
			city = cityTV.getText().toString();
			cities = Okler.getInstance().getCities();
			if(!(cities.size()<=0)){
				int cid = cities.indexOf(city);
			city_id_list = Okler.getInstance().getCiti_ids();	
				if(!(city_id_list.size()<=0))
				city_id = Integer.parseInt(city_id_list.get(cid));
				//adbean.setCity_id(city_id);
				//city = city_id;
				flag = true;
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		}
		
		
		
		/*		name = nameTV.getText().toString();
		surname = surnameTV.getText().toString();
		dob = dobTV.getText().toString();
		add1 = addr1TV.getText().toString();
		add2 = addr2TV.getText().toString();
		relation = (String) spinnerRelation.getSelectedItem();
		gender = (String) spinnerGender.getSelectedItem();
		landmark = landmarkTV.getText().toString();
		pincode = pincodeTV.getText().toString();
		mobile = mobileTV.getText().toString();
		city = cityTV.getText().toString();*/
	//String pat_id="";
		
		try {
			address1= URLEncoder.encode(add1, "UTF-8");
			address2= URLEncoder.encode(add2, "UTF-8");
			landmark1 = URLEncoder.encode(landmark, "UTF-8");
			name1= URLEncoder.encode(name, "UTF-8");
			surname1 = URLEncoder.encode(surname, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(Okler.getInstance().getBookingType()==9||Okler.getInstance().getBookingType()==20){
			add_address = "https://www.okler.com/api/users/AdduserPatientAddress?user_id=" + user_id + "&pat_id="+ pat_id 
					+"&firstname=" + name1 +"&surname=" + surname1 + "&dob=" + dob + "&relation_id=" + rel_id + "&gender=" + "m"+ "" 
					+ "&mobileNo=" + mobile + "&addr1=" + address1 + "&addr2=" + address2 + "&land_mark=" + landmark1 + "&pincode="+pincode
					+ "&city_id=" + city_id;
		}else{
		
		add_address = getResources().getString(R.string.add_patient_address) +"addr_id="+addrerss_id+"&customer_id="+ user_id+"" + "&company="+ "" 
				+"&customer_name=" + name1 +"&surname=" + surname1 + "&dob=" + dob + "&relation_id=" + rel_id + "&gender=" + "m"+ "" 
				+"&email=" + "" + "&mobileNo=" + mobile + "&addr1=" + address1 + "&addr2=" + address2 + "&landmark=" + landmark1 + "&zip="+pincode
				+ "&country_id=" + "99" + "&zone_id=" + "" + "&state_id=" +state_id+ "&city_id=" +city_id;
	}
		if(flag){
		WebJsonObjectRequest webjson=new WebJsonObjectRequest(Method.GET, add_address, new JSONObject(),new Listener<JSONObject>() 
				{
					@Override
					public void onResponse(JSONObject response) 
					{
						// TODO Auto-generated method stub						
						try
						{
						JSONObject responseObj =(JSONObject)response;
						JSONObject doctorsArr = responseObj.getJSONObject("result");
						
						String messaqge = doctorsArr.getString("message");
						Toast.makeText(getApplicationContext(), messaqge, Toast.LENGTH_LONG).show();
						
						}catch(JSONException jsonEx)
						{
							Log.e("Exception json", jsonEx.getStackTrace().toString());
						}
						
						Intent intent = null;
						if(Okler.getInstance().getBookingType()==9||Okler.getInstance().getBookingType()==20){
							intent = new Intent(ProfileNewAddress.this,SelectPatientInfoActivity.class);
						}else{
							intent = new Intent(ProfileNewAddress.this,MyProfileActivity.class);
						}
						startActivity(intent);
					}}, 
					new Response.ErrorListener() 
					{

						@Override
						public void onErrorResponse(VolleyError error) 
						{
							// TODO Auto-generated method stub
							Log.e("ERROR", String.valueOf(error));
						}
					}
		);
			
		VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
		}else{
			Toast.makeText(act, "Please Enter Valid Address", Toast.LENGTH_LONG).show();
		}
	}
	
	public void getRelation()
	{
		// web call for relation
				WebJsonObjectRequest webjson=new WebJsonObjectRequest(Method.GET, getResources().getString(R.string.relationUrl), new JSONObject(),new Listener<JSONObject>() 
						{
							@Override
							public void onResponse(JSONObject response) 
							{
								// TODO Auto-generated method stub
								
								try
								{
								JSONObject responseObj =(JSONObject)response;
								JSONArray doctorsArr = responseObj.getJSONArray("result");
								//docCount=responseObj.getInt("TotalCount");
								for(int i = 0 ; i < doctorsArr.length();i++)
								{
									try
									{
										JSONObject docObj =(JSONObject) doctorsArr.get(i);
										relation.add(docObj.getString("relation"));
										relation_id.add(docObj.getString("relation_id"));
										Log.i("tag", "json object" + docObj);
										}catch (JSONException e) {
											// TODO: handle exception
											Log.e("JSON Exception", e.getMessage());
										}
								 }
								Okler.getInstance().setRelationList(relation);
								Okler.getInstance().setRelationIdList(relation_id);
								adapter = new ArrayAdapter<String>(ProfileNewAddress.this, android.R.layout.simple_dropdown_item_1line, relation);
								spinnerRelation.setAdapter(adapter);
								}catch(JSONException jsonEx)
								{
									Log.e("Exception json", jsonEx.getStackTrace().toString());
								}
						
							}}, 
							new Response.ErrorListener() 
							{

								@Override
								public void onErrorResponse(VolleyError error) 
								{
									
									Log.i("error", String.valueOf(error));
								//	Toast.makeText(getApplicationContext(), "error"+error, Toast.LENGTH_LONG).show();
									// TODO Auto-generated method stub
						
								}
							}
							,true);
					
				VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
				
				
	}
public void getCity(EditText edt){
		
		int id = 0;
		String state = edt.getText().toString();
		states = Okler.getInstance().getStates();
		id = states.indexOf(state);
		Log.i("id_tag", "id is:" + id);
		state_id = String.valueOf(1268 + id);
		String cityUrl = getResources().getString(R.string.cityUrl) + "state_id="+state_id;
		webjson=new WebJsonObjectRequest(Method.GET, cityUrl, new JSONObject(),new Listener<JSONObject>() 
				{
					@Override
					public void onResponse(JSONObject response) 
					{
						try
						{
						JSONObject responseObj =(JSONObject)response;
						JSONArray doctorsArr = responseObj.getJSONArray("result");
						for(int i = 0 ; i < doctorsArr.length();i++)
						{
							try
							{
								JSONObject docObj =(JSONObject) doctorsArr.get(i);
								cities.add(docObj.getString("city_name"));
								city_id_list.add(docObj.optString("id")); 
								Log.i("tag", "json object" + docObj);
								}catch (JSONException e) {
									// TODO: handle exception
									Log.e("JSON Exception", e.getMessage());
								}
						 }
						Okler.getInstance().setCities(cities);
						Okler.getInstance().setCiti_ids(city_id_list);
						adapter2 = new ArrayAdapter<String>(act	, android.R.layout.simple_dropdown_item_1line,cities)
						//adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, cities)
						{
							 @Override
							 public View getView(int position, View convertView, ViewGroup parent) {
								 View view = super.getView(position, convertView, parent);
					       TextView text = (TextView) view.findViewById(android.R.id.text1);
					       text.setTextColor(Color.BLACK);
					       return view;
							 }
						 }
						 ;
						cityTV.setAdapter(adapter2);
						adapter2.notifyDataSetChanged();
						}catch(JSONException jsonEx)
						{
							Log.e("Exception json", jsonEx.getStackTrace().toString());
						}
				
					}}, 
					new Response.ErrorListener() 
					{

						@Override
						public void onErrorResponse(VolleyError error) 
						{
							// TODO Auto-generated method stub
							Log.e("ERROR", String.valueOf(error));
						}
					}
					,true);
			
		VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
		
		
		Log.i("city url", cityUrl);

	}
	
	public void getStates(){
		  //web call for states
		webjson=new WebJsonObjectRequest(Method.GET, getResources().getString(R.string.stateUrl), new JSONObject(),new Listener<JSONObject>() 
				{
					@Override
					public void onResponse(JSONObject response) 
					{
						// TODO Auto-generated method stub
						
						try
						{
						JSONObject responseObj =(JSONObject)response;
						JSONArray doctorsArr = responseObj.getJSONArray("result");
						//docCount=responseObj.getInt("TotalCount");
						for(int i = 0 ; i < doctorsArr.length();i++)
						{
							try
							{
								JSONObject docObj =(JSONObject) doctorsArr.get(i);
								states.add(docObj.getString("state_name"));
								Log.i("tag", "json object" + docObj);
								}catch (JSONException e) {
									// TODO: handle exception
									Log.e("JSON Exception", e.getMessage());
								}
						 }
						Okler.getInstance().setStates(states);
						if(content==2){
							city = cityTV.getText().toString();
							getCity(stateTv);
						}
						}catch(JSONException jsonEx)
						{
							Log.e("Exception json", jsonEx.getStackTrace().toString());
						}
				
					}}, 
					new Response.ErrorListener() 
					{

						@Override
						public void onErrorResponse(VolleyError error) 
						{
							// TODO Auto-generated method stub
							Log.e("ERROR", String.valueOf(error));
						}
					}
					,true);
			
		VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
	}
}	
	/*public void getStates()
	{
		
		webjson=new WebJsonObjectRequest(Method.GET, getResources().getString(R.string.stateUrl), new JSONObject(),new Listener<JSONObject>() 
				{
					@Override
					public void onResponse(JSONObject response) 
					{
						// TODO Auto-generated method stub
						
						try
						{
						JSONObject responseObj =(JSONObject)response;
						JSONArray doctorsArr = responseObj.getJSONArray("result");
						//docCount=responseObj.getInt("TotalCount");
						for(int i = 0 ; i < doctorsArr.length();i++)
						{
							try
							{
								JSONObject docObj =(JSONObject) doctorsArr.get(i);
								states.add(docObj.getString("state_name"));
								state_id_list.add(docObj.getString("id"));
								Log.i("tag", "json object" + docObj);
								}catch (JSONException e) {
									// TODO: handle exception
									Log.e("JSON Exception", e.getMessage());
								}
						 }
						adapter = new ArrayAdapter<String>(ProfileNewAddress.this, android.R.layout.simple_dropdown_item_1line, states);
						stateTv.setAdapter(adapter);
						}catch(JSONException jsonEx)
						{
							Log.e("Exception json", jsonEx.getStackTrace().toString());
						}
				
					}}, 
					new Response.ErrorListener() 
					{

						@Override
						public void onErrorResponse(VolleyError error) 
						{
							// TODO Auto-generated method stub
				
						}
					}
		);
			
		VolleyRequest.addJsonObjectRequest(this,webjson);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, states);
		stateTv.setAdapter(adapter);
		
		stateTv.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				
				int id = 0;
				
				String state = stateTv.getText().toString();
				
				id = states.indexOf(state);
				state_id2 = 1268 + id;
				
				String cityUrl = getResources().getString(R.string.cityUrl) + "state_id="+state_id2;
				
				
				webjson=new WebJsonObjectRequest(Method.GET, cityUrl, new JSONObject(),new Listener<JSONObject>() 
						{
							@Override
							public void onResponse(JSONObject response) 
							{
								// TODO Auto-generated method stub
								
								try
								{
								JSONObject responseObj =(JSONObject)response;
								JSONArray doctorsArr = responseObj.getJSONArray("result");
								//docCount=responseObj.getInt("TotalCount");
								for(int i = 0 ; i < doctorsArr.length();i++)
								{
									try
									{
										JSONObject docObj =(JSONObject) doctorsArr.get(i);
										cities.add(docObj.getString("city_name"));
										city_id_list.add(docObj.getString("id"));
										Log.i("tag", "json object" + docObj);
										}catch (JSONException e) {
											// TODO: handle exception
											Log.e("JSON Exception", e.getMessage());
										}
								 }
								adapter = new ArrayAdapter<String>(ProfileNewAddress.this, android.R.layout.simple_dropdown_item_1line, cities);
								cityTV.setAdapter(adapter);
								}catch(JSONException jsonEx)
								{
									Log.e("Exception json", jsonEx.getStackTrace().toString());
								}
						
							}}, 
							new Response.ErrorListener() 
							{

								@Override
								public void onErrorResponse(VolleyError error) 
								{
									// TODO Auto-generated method stub
						
								}
							}
				);
					
				VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
				
				
				Log.i("city url", cityUrl);
			}
		});

			
		VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
		
		
	}

	/*@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
		int id = spinnerRelation.getSelectedItemPosition();
		relation_id1 = relation_id.get(id);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}*/
	

//}
