package com.okler.android;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductCheckoutUserDetail extends BaseActivity {
	private Toolbar toolBar;
	private Context context;
	Activity ack;
	private View bottomBarLayout;
	private EditText pincode_et;
	private Button notifCount,nextButton;
	TextView tv_checkout,btv_prefferedDelTime;
	ImageView progressInScreen,imgBack;
	UsersDataBean ubean;
	EditText editText1,editText2,editText3,editText4,et_firstName,et_surName,et_address,et_addressLine2,et_prefferedDelTime,bet_firstName,bet_surName,bet_address,bet_addressLine2,bet_pincode,bet_prefferedDelTime;
	//EditText et_pincode;
	WebJsonObjectRequest webjson;
ArrayAdapter<String> adapter,adapter2;
AutoCompleteTextView edt_city, edt_state,bedt_city, bedt_state;
	ArrayList<String> cities = new ArrayList<String>();
	ArrayList<String> states = new ArrayList<String>();
	ArrayList<String> citi_ids = new ArrayList<String>();
	int state_id,bStateId,maincount;
	AddressDataBean adbean;
	String cityUrl;
	ArrayList<AddressDataBean> addrList;
	RelativeLayout billing_addr_RL;
	CartDataBean odbean;
	ArrayList<ProductDataBean> pdList;
	CheckBox bcheckBox1;
	int check;
	String cityId,bcityId="";
	String city,bcity;
	int cid,bcid;
	//String s_add_id,b_add_id;
boolean flag =false,flag2 = false,stateFound=false,cityFound=false,firstTime=false;
	
	int user_id;
	RelativeLayout back_layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_checkout_user_detail);
		  toolBar=(Toolbar) findViewById(R.id.toolbar);
		  Okler.getInstance().getStates().clear();
		  notifCount = (Button)toolBar.findViewById(R.id.notif_count);
		  check = getIntent().getIntExtra("Check", 1);
		  ack = this;
	       context=getApplicationContext();
	        ubean = Utilities.getCurrentUserFromSharedPref(ack);
			user_id = ubean.getId();
	  		addrList = ubean.getAddDatabean();
	  		int adrsize = addrList.size();
	  		/*if(adrsize<=0){
	  			firstTime=true;
	  		}*/
			
	       //mImageLoader = VolleyRequest.getInstance(context).getImageLoader();
	       setSupportActionBar(toolBar);
	       final ActionBar ab=getSupportActionBar();
	       if(check == 1){
	       odbean = Okler.getInstance().getSingleCart();
	       }
	       else{
	    	   odbean = Okler.getInstance().getMainCart();
	       }
	 //      ab.setDisplayHomeAsUpEnabled(true);
	       tv_checkout = (TextView)findViewById(R.id.tv_checkout);
	       progressInScreen = (ImageView)findViewById(R.id.progressInScreen);
	       if(Okler.getInstance().getBookingType()==0){
				//ab.setTitle(R.string.title_activity_allopathy);
				progressInScreen.setImageResource(R.drawable.md_process_user_details_image);
				tv_checkout.setBackgroundColor(getResources().getColor(R.color.Brightyellow));
				Utilities.setTitleText(toolBar, getString(R.string.allopathy));
				
			}else if(Okler.getInstance().getBookingType()==3){
				progressInScreen.setImageResource(R.drawable.md_process_user_details_image);
				tv_checkout.setBackgroundColor(getResources().getColor(R.color.Brightyellow));
				Utilities.setTitleText(toolBar, getString(R.string.ayurvedic));
				//ab.setTitle(R.string.title_activity_ayurvedic);
			}else if(Okler.getInstance().getBookingType()==4){
				progressInScreen.setImageResource(R.drawable.md_process_user_details_image);
				tv_checkout.setBackgroundColor(getResources().getColor(R.color.Brightyellow));
				//ab.setTitle(R.string.title_activity_homeopathy);
				Utilities.setTitleText(toolBar, getString(R.string.homeopathy));
				
			}else{
				//ab.setTitle(R.string.title_activity_health_shop_grid);
				 Utilities.setTitleText(toolBar, "Health Shop");
			}
	       toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler.getInstance().getBookingType()));
	       if(check == 0){
	    	   toolBar.setBackgroundColor(Color.BLUE);
	    	   tv_checkout.setBackgroundColor(Color.BLUE);
	    	   Utilities.setTitleText(toolBar, "Cart");
	       }
			//end of code to be copied
	      /* back_layout = (RelativeLayout)toolBar.findViewById(R.id.back_layout);
			back_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				finish();	
				}
			});
	       imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       imgBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});*/
	       
	       UIUtils.setBackClick(toolBar, ack);
			
	       /*toolBar.setBackgroundResource(R.drawable.custom_view_grad_healthshop);*/
	       bottomBarLayout = findViewById(R.id.bottombar);
	       handleMapping(bottomBarLayout);
	       
	       pincode_et=(EditText) findViewById(R.id.et_pincode);
	       pincode_et.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.location_icon), null);
	       nextButton=(Button) findViewById(R.id.nextButton);
	       nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				edt_city.setAdapter(adapter2);
				bedt_city.setAdapter(adapter2);
				// TODO Auto-generated method stub
				TextValidations tval;
				UIUtils uiutil;
				AddressDataBean curbean = new AddressDataBean();
				ArrayList<AddressDataBean> newList = new ArrayList<AddressDataBean>();
				/*if(et_firstName.getText().toString().trim().equals("")||et_firstName.getText().toString().trim().equals(null)){
					
				}*/
				flag2 =false;
		  		UsersDataBean ubean2 = new UsersDataBean();
		  		ubean2.setAddDatabean(newList);
		  		tval = new TextValidations(editText1);
		  		flag2 =false;
		  		if(tval.ValidateMinimumChars("Please Enter a Name",3)){
	       			ubean2.setFname(editText1.getText().toString());
	       		tval = new TextValidations(editText2);
	       		flag2 =false;
	       		if(tval.validateText("Please Enter a SurName")){
	       			ubean2.setLname(editText2.getText().toString());
	       		tval = new TextValidations(editText3);
	       		flag2 =false;
	       		if(tval.validateMobile("Please Enter a Valid Mobile Number")){
	       			ubean2.setPhone(editText3.getText().toString());
	       		tval = new TextValidations(editText4);
	       		flag2 =false;
	       		if(tval.ValidateEmailId()){
	       			ubean2.setEmail(editText4.getText().toString());
	       		flag2=true;	
	       		}
	       		}
	       		}
	       		}
		  		
		  		if(flag2){
	       		flag2 =false;
				tval = new TextValidations(et_firstName);
				if(tval.validateText("Please Enter Name")){
		  		curbean.setFirstname(et_firstName.getText().toString());
				tval = new TextValidations(et_surName);
				flag2 =false;
				if(tval.validateText("Please Enter SurName")){
				curbean.setLastname(et_surName.getText().toString());	
				tval = new TextValidations(et_address);
				flag2 =false;
				if(tval.validateText("Please Enter Address")){
				curbean.setAddress1(et_address.getText().toString());	
				tval = new TextValidations(et_addressLine2);
				flag2 =false;
				if(tval.validateText("Please Enter Address")){
				curbean.setAddress2(et_addressLine2.getText().toString());
				uiutil = new UIUtils(edt_state);
				String st = edt_state.getText().toString(); 
				flag2 =false;
				if(uiutil.validateState("Please Enter a Valid State",st,ack)){
				curbean.setState(edt_state.getText().toString());	
				tval = new TextValidations(pincode_et);
				flag2 =false;
				if(tval.validatePinCode("Please Enter Valid Pincode")){
				curbean.setZip(pincode_et.getText().toString());
				uiutil = new UIUtils(edt_city);
				flag2 =false;
				String city2 = edt_city.getText().toString();
				if(uiutil.validateCity("Please Enter a City",city2,ack)){
				curbean.setCity(edt_city.getText().toString());
				if(!(cities.size()<=0)){
				cid = cities.indexOf(city);
				if(!(citi_ids.size()<=0))
				cityId = citi_ids.get(cid);
				}
				curbean.setCity_id(cityId);
				tval = new TextValidations(et_prefferedDelTime);
				flag2 =false;
				if(tval.validateText("Please Enter Delivery Time")){
				curbean.setPreferred_del_time(et_prefferedDelTime.getText().toString());
				curbean.setDefault_shipping(1);
				curbean.setSelected(true);
				curbean.setState_id(String.valueOf(state_id));
				newList.add(curbean);
				flag2=true;
			  	}
				}
				}
				}
				}
				}
				}
				}
	       		}
				
				if(flag2){
				AddressDataBean curbean2 = new AddressDataBean();
				tval = new TextValidations(bet_firstName);
				flag2 =false;
				if(tval.validateText("Please Enter Name")){
		  		curbean2.setFirstname(bet_firstName.getText().toString());
				tval = new TextValidations(bet_surName);
				flag2 =false;
				if(tval.validateText("Please Enter SurName")){
				curbean2.setLastname(bet_surName.getText().toString());	
				tval = new TextValidations(bet_address);
				flag2 =false;
				if(tval.validateText("Please Enter Address")){
				curbean2.setAddress1(bet_address.getText().toString());	
				tval = new TextValidations(bet_addressLine2);
				flag2 =false;
				if(tval.validateText("Please Enter Address")){
				curbean2.setAddress2(bet_addressLine2.getText().toString());
				
				
				uiutil = new UIUtils(bedt_state);
				String st = bedt_state.getText().toString(); 
				flag2 =false;
				if(uiutil.validateState("Please Enter a Valid State",st,ack)){
				curbean2.setState(bedt_state.getText().toString());	
				if(bcheckBox1.isChecked()){
					bStateId = state_id;
				}
				curbean2.setState_id(String.valueOf(bStateId));
				tval = new TextValidations(bet_pincode);
				flag2 =false;				
				if(tval.validatePinCode("Please Enter Valid Pincode")){
				curbean2.setZip(bet_pincode.getText().toString());	
				curbean2.setDefault_billing(1);
				newList.add(curbean2);
				uiutil = new UIUtils(bedt_city);
				flag2 =false;
				String city = bedt_city.getText().toString();
				if(uiutil.validateCity("Please Enter a Valid City",city,ack)){
				curbean2.setCity(bedt_city.getText().toString());
				if(!(cities.size()<=0)){
				cid = cities.indexOf(city);
				if(!(citi_ids.size()<=0))
					if(bcheckBox1.isChecked()){
					bcityId = cityId;	
					}
				bcityId = citi_ids.get(cid);
				}
				curbean2.setCity_id(bcityId);
				flag2 =true;
			  	}
				}
				}
				}
				}
				}
				}
				}
				
		  		ubean.setAddDatabean(newList);
		  		
		  		
		  		if(check == 1){
		  		odbean = Okler.getInstance().getSingleCart();
		  		odbean.setCurUBean(ubean2);
		  		Okler.getInstance().setSingleCart(odbean);
		  		}
		  		else{
		  			odbean = Okler.getInstance().getMainCart();
		  			odbean.setCurUBean(ubean2);
			  		Okler.getInstance().setSingleCart(odbean);
		  		}
		  		//int size = ubean.getAddDatabean().size();
		  		String fname="",sname="",add1="",add2="",bfname="",bsname="",badd1="",badd2="",pref_del_time1="";
		  		
		  		
		  		try {
					fname = URLEncoder.encode(et_firstName.getText().toString(), "UTF-8");
					sname = URLEncoder.encode(et_surName.getText().toString(), "UTF-8");
					add1 = URLEncoder.encode(et_address.getText().toString(), "UTF-8");
					add2 = URLEncoder.encode(et_addressLine2.getText().toString(), "UTF-8");
					
					bfname = URLEncoder.encode(bet_firstName.getText().toString(), "UTF-8");
					bsname = URLEncoder.encode(bet_surName.getText().toString(), "UTF-8");
					badd1 = URLEncoder.encode(bet_address.getText().toString(), "UTF-8");
					badd2 = URLEncoder.encode(bet_addressLine2.getText().toString(), "UTF-8");
					pref_del_time1 = URLEncoder.encode(et_prefferedDelTime.getText().toString(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  		if(flag2){
		  		String add_address = getResources().getString(R.string.add_patient_address) + "customer_id="+ user_id + "&company="+ ubean.getCompany()  
				+"&customer_name=" + fname +"&surname=" + sname + "" + "&dob=" + ubean.getDob() + "&gender=" + ubean.getGender()
				+"&email=" + ubean.getEmail() + "&mobileNo=" + ubean.getPhone() + "&addr1=" + add1 + "&addr2=" + add2 + "&landmark=" + "" + "&zip="+pincode_et.getText().toString()
				+ "&country_id=" + "99" + "&zone_id=" + "" + "&state_id=" + state_id + "&city_id=" + cityId+ "&delivery_time=" + pref_del_time1;
		  		flag = false;
		  		addNewAddress(add_address);
		  		/*String add_address2 = getResources().getString(R.string.add_patient_address) + "customer_id="+ user_id + "&company="+ ubean.getCompany()  
				+"&customer_name=" + bfname +"&surname=" + bsname + "" + "&dob=" + ubean.getDob() + "&gender=" + ubean.getGender()
				+"&email=" + ubean.getEmail() + "&mobileNo=" + ubean.getPhone() + "&addr1=" + badd1 + "&addr2=" + badd2 + "&landmark=" + "" + "&zip="+ bet_pincode.getText().toString()
				+ "&country_id=" + "99" + "&zone_id=" + "" + "&state_id=" + bStateId + "&city_id=" + bcityId;
		  		flag = true;
		  		addNewAddress(add_address2);*/
		  		}
			}
		});
	       editText1 = (EditText)findViewById(R.id.editText1);
	       editText2 = (EditText)findViewById(R.id.editText2);
	       editText3 = (EditText)findViewById(R.id.editText3);
	       editText4 = (EditText)findViewById(R.id.editText4);
	       et_firstName = (EditText)findViewById(R.id.et_firstName);
	       et_surName = (EditText)findViewById(R.id.et_surName);
	       et_address = (EditText)findViewById(R.id.et_address);
	       et_addressLine2 = (EditText)findViewById(R.id.et_addressLine2);
	       et_prefferedDelTime = (EditText)findViewById(R.id.et_prefferedDelTime);
	       billing_addr_RL = (RelativeLayout)findViewById(R.id.billing_addr_RL);
	       bet_firstName = (EditText)billing_addr_RL.findViewById(R.id.et_firstName);
	       bet_surName = (EditText)billing_addr_RL.findViewById(R.id.et_surName);
	       bet_address = (EditText)billing_addr_RL.findViewById(R.id.et_address);
	       bet_addressLine2 = (EditText)billing_addr_RL.findViewById(R.id.et_addressLine2);
	       bet_pincode = (EditText)billing_addr_RL.findViewById(R.id.et_pincode);
	       bet_prefferedDelTime = (EditText)billing_addr_RL.findViewById(R.id.et_prefferedDelTime);
	       bcheckBox1 = (CheckBox)billing_addr_RL.findViewById(R.id.checkBox1);
	       bet_prefferedDelTime.setVisibility(View.GONE);
	       btv_prefferedDelTime = (TextView)billing_addr_RL.findViewById(R.id.tv_prefferedDelTime);
	       btv_prefferedDelTime.setVisibility(View.GONE);
	      
	    
	    
	   // ubean = Utilities.getCurrentUserFromSharedPref(ack);
	    editText1.setText(ubean.getFname());
	    editText2.setText(ubean.getLname());
	    editText3.setText(ubean.getPhone());
	    editText4.setText(ubean.getEmail());
	       
	    edt_city = (AutoCompleteTextView)findViewById(R.id.edtTxt_city);
		edt_state = (AutoCompleteTextView)findViewById(R.id.edtTxt_state);
		bedt_city = (AutoCompleteTextView)billing_addr_RL.findViewById(R.id.edtTxt_city);
		bedt_state = (AutoCompleteTextView)billing_addr_RL.findViewById(R.id.edtTxt_state);
		getStates();
		
	  		
	  		
	  		states = Okler.getInstance().getStates();
	  		adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, states)
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
	  		edt_state.setAdapter(adapter);
	  		bedt_state.setAdapter(adapter);
	  		
	  		edt_state.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
				getCityList(edt_state);	
				edt_city.setAdapter(adapter2);
	  			}
	  		});
	  		
	  		edt_city.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					city = edt_city.getText().toString();	
					edt_city.setAdapter(adapter2);
				}
			});	
	  				
	  				
			bedt_state.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		  				getCityList(bedt_state);
	  				bedt_city.setAdapter(adapter2);
	  				}
	  		});
	  		
			bedt_city.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
					bcity = bedt_city.getText().toString();
	  				bedt_city.setAdapter(adapter2);
					
				}
			});
	  		
	  		bcheckBox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						bet_firstName.setText(et_firstName.getText().toString());
						bet_surName.setText(et_surName.getText().toString());
						bet_address.setText(et_address.getText().toString());
						bet_addressLine2.setText(et_addressLine2.getText().toString());
						bet_pincode.setText(pincode_et.getText().toString());
						
						bedt_city.setText(edt_city.getText().toString());
						bcityId = cityId;
						bedt_state.setText(edt_state.getText().toString());
						bStateId = state_id;
					}
					
				}
			});
	  		
	  		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
	
	public void addNewAddress(String Url)
	{
		
		WebJsonObjectRequest webjson =new WebJsonObjectRequest(Method.GET, Url, new JSONObject(),new Listener<JSONObject>() 
				{
					@Override
					public void onResponse(JSONObject response) 
					{
					//	Toast.makeText(getApplicationContext(), "response", Toast.LENGTH_LONG).show();
						
						try
						{
						JSONObject responseObj =(JSONObject)response;
						JSONObject doctorsArr = responseObj.getJSONObject("result");
					//	Toast.makeText(getApplicationContext(), "try", Toast.LENGTH_LONG).show();
						String messaqge = doctorsArr.getString("message");
						if((messaqge.equals("inserted successfully"))||messaqge.equals("updated successfully")){
							//if(firstTime){
							int add_id = Integer.parseInt(doctorsArr.getString("addr_id"));
							String u1,u2,u3,setDefaultAddUrl;
							u1 = getString(R.string.DefaultBillShipPart1);
							u2 = getString(R.string.DefaultBillShipPart2);
							u3 = getString(R.string.DefaultBillShipPart3);
							if(flag){
								setDefaultAddUrl = u1+user_id+u2+add_id+u3;
								
							
							
							}else{
								setDefaultAddUrl = u1+user_id+u2+u3+add_id;
							}
					WebJsonObjectRequest adjson = new WebJsonObjectRequest(Method.GET, setDefaultAddUrl, new JSONObject(), new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							/*if(flag){*/
								Utilities.writeCurrentUserToSharedPref(ack, ubean);
								Intent intent = null;
								CartDataBean cbean = new CartDataBean();
								if(check==1){
								cbean = Okler.getInstance().getSingleCart();
								}
								else{
								cbean = Okler.getInstance().getMainCart();	
								}
								ArrayList<ProductDataBean> prodList1 = cbean.getProdList();
								int prescNeeded = 0;
								int length = prodList1.size();
								for (int i = 0; i < length; i++) {
									prescNeeded = prodList1.get(i).getPresc_needed();
									if(prescNeeded==1){
										break;
									}
								}
								if(!flag)
								{
									if(prescNeeded==1)
									{
										intent = new Intent(getApplicationContext(),AlloPrescActivity.class);
										intent.putExtra("Check", check);
										finish();
										startActivity(intent);
									}
									else
									{
										intent = new Intent(getApplicationContext(),ProductCheckoutSummary.class);
										intent.putExtra("Check", check);
										finish();
										startActivity(intent);
									}
								}
								else
								{
									flag=false;
								}							
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub
							Log.e("ERROR", String.valueOf(error));
						}
					});
					VolleyRequest.addJsonObjectRequest(ack, adjson);
						
						}
						}catch(JSONException jsonEx)
						{
							Log.e("Exception json", jsonEx.getStackTrace().toString());
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
						}
					}
		);
			
		VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);		
	}
	
	public void getCityList(EditText edt){
		int id1 = 0;
			
		cities.clear();
		citi_ids.clear();
			String state = edt.getText().toString();
			
			id1 = states.indexOf(state);
			
			Log.i("id_tag", "id is:" + id1);
			
			state_id = 1268 + id1;
			
			cityUrl = getResources().getString(R.string.cityUrl) + "state_id="+state_id;
			
			if(state_id>=1268){
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
									citi_ids.add(docObj.optString("id")); 
									Log.i("tag", "json object" + docObj);
									}catch (JSONException e) {
										// TODO: handle exception
										Log.e("JSON Exception", e.getMessage());
									}
							 }
							Okler.getInstance().setCities(cities);
							Okler.getInstance().setCiti_ids(citi_ids);
							cities = Okler.getInstance().getCities();
					  		adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, cities) {
					  		    @Override
					  		    public View getView(int position, View convertView, ViewGroup parent) {
					  		        View view = super.getView(position, convertView, parent);
					  		        TextView text = (TextView) view.findViewById(android.R.id.text1);
					  		        text.setTextColor(Color.BLACK);
					  		        return view;
					  		    }
					  		};
					  		edt_city.setAdapter(adapter2);
					  		bedt_city.setAdapter(adapter2);
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
					
							}
						}
						,true	);
				
			VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
			}else{
				
			}
			
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
  					,true);
  			
  		VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(notifCount, ack);
	}
}
