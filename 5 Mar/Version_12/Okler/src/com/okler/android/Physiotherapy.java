package com.okler.android;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.okler.databeans.PhysioAndMedicalBean;
import com.okler.databeans.UsersDataBean;
import com.okler.fragment.AdditionalInfo;
import com.okler.fragment.PersonalInfo;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.network.WebStringRequest;
import com.okler.utils.MediPhysioTextValidations;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;
import com.okleruser.R;



import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Physiotherapy extends BaseActivity {

	Button btnAddInfo, btnAppointment,notifCount;
	Toolbar toolBar;
	 android.app.FragmentManager fragmentManager;
   FragmentTransaction fragmentTransaction;
	View bottomBarLayout;
	static int mNotifCount = 0;
	int id=1;
	View view;
	Activity currActivity;
	
	EditText edt_firstname,edt_surname, edt_email, edt_phoneno, edt_address, edt_pincode, edt_weight;
	AutoCompleteTextView edt_state, edt_city;
	Spinner spinner_relation, spinner_service;
	TextView txtcity, txtstate, txtrelation, txtservice, edt_From, edt_To;
	LinearLayout progressLinLayout;
	String response_from;
	static String rel_id,sev_id;
	
int currStatus=0;
	ImageView imgBack;
static String state1;
static String city1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_physiotherapy);
		btnAddInfo = (Button) findViewById(R.id.addinfo_btn);
		btnAppointment = (Button) findViewById(R.id.apppointmant_btn);
		bottomBarLayout = findViewById(R.id.bottombar);
		currActivity = this;
		handleMapping(bottomBarLayout);
		progressLinLayout = (LinearLayout)findViewById(R.id.progressLinLayout);
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		final ActionBar ab=getSupportActionBar();
		//ab.setDisplayHomeAsUpEnabled(true);
		
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_physio);
		
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
	      Utilities.setTitleText(toolBar, "Physiotherapy Servicves");
	      fragmentManager = getFragmentManager();
	      fragmentTransaction = fragmentManager.beginTransaction();
	      
	      PersonalInfo info_fragment = new PersonalInfo();
	      
	      fragmentTransaction.add(info_fragment, "info_frag");
	      fragmentTransaction.commit();
	      
	      	// values from fragment personal info
	      	view = findViewById(R.id.fragment_info);
			edt_firstname = (EditText) view.findViewById(R.id.edtTet_firstname);
			edt_surname = (EditText) view.findViewById(R.id.edtTxt_surname); 
			edt_email = (EditText) view.findViewById(R.id.edtTxt_email);
			edt_phoneno = (EditText) view.findViewById(R.id.edtTxt_phonenumber);
			edt_state = (AutoCompleteTextView) view.findViewById(R.id.edtTxt_state);
			edt_city = (AutoCompleteTextView) view.findViewById(R.id.edtTxt_city);		
			edt_From = (TextView) view.findViewById(R.id.txtFrom);
			edt_To = (TextView) view.findViewById(R.id.txtTo);
			txtcity = (TextView) view.findViewById(R.id.txt_city_id);
			txtstate = (TextView) view.findViewById(R.id.txt_state_id);	  
			
			LinearLayout layout = (LinearLayout) findViewById(R.id.layout_addinfo);
			View view = findViewById(R.id.fragment_info);
			View frgLayout = layout.findViewById(R.id.fragment_add_info);
			LinearLayout frgLayout1 = (LinearLayout)frgLayout.findViewById(R.id.frag_add_info);
			edt_address = (EditText) frgLayout.findViewById(R.id.edtTxt_address);
			edt_pincode = (EditText) frgLayout.findViewById(R.id.edtTxt_pincode);
			spinner_relation = (Spinner) frgLayout.findViewById(R.id.relation_spinner);
			spinner_service = (Spinner) frgLayout.findViewById(R.id.service_spinner);
			edt_weight = (EditText) frgLayout.findViewById(R.id.edtTxt_weight);
			txtrelation = (TextView) frgLayout.findViewById(R.id.txt_relationid);
			txtservice = (TextView) frgLayout.findViewById(R.id.txt_serviceid);
			progressLinLayout = (LinearLayout)findViewById(R.id.progressLinLayout);	      
			btnAddInfo.setOnClickListener(new OnClickListener() { 
	    	  
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				/*fragmentManager = getFragmentManager();
			    fragmentTransaction = fragmentManager.beginTransaction();			      
			    AdditionalInfo addinfo_fragment = new AdditionalInfo();			      
		        fragmentTransaction.add(R.id.layout_addinfo, addinfo_fragment);
			    fragmentTransaction.commit();*/
				LinearLayout layout = (LinearLayout) findViewById(R.id.layout_addinfo);
				if(currStatus == 0)
			    {
					//btnAddInfo.setEnabled(false);
					Drawable icon=getApplicationContext().getResources().getDrawable(R.drawable.minus);					
					btnAddInfo.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);		
					currStatus =1;					
					layout.setVisibility(View.VISIBLE);
			    }
				else
				{
					
					Drawable icon=getApplicationContext().getResources().getDrawable(R.drawable.add);					
					btnAddInfo.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
					currStatus = 0;
					layout.setVisibility(View.GONE);
				}
				// values from fragment additional info
				
				
				/*View view = findViewById(R.id.fragment_info);
				View frgLayout = layout.findViewById(R.id.fragment_add_info);
				LinearLayout frgLayout1 = (LinearLayout)frgLayout.findViewById(R.id.frag_add_info);
				edt_address = (EditText) frgLayout.findViewById(R.id.edtTxt_address);
				edt_pincode = (EditText) frgLayout.findViewById(R.id.edtTxt_pincode);
				spinner_relation = (Spinner) frgLayout.findViewById(R.id.relation_spinner);
				spinner_service = (Spinner) frgLayout.findViewById(R.id.service_spinner);
				edt_weight = (EditText) frgLayout.findViewById(R.id.edtTxt_weight);
				txtrelation = (TextView) frgLayout.findViewById(R.id.txt_relationid);
				txtservice = (TextView) frgLayout.findViewById(R.id.txt_serviceid);*/
				
				
			}
		});
	      
	      btnAppointment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				showDialog();
			}
		});

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		//toolBar.inflateMenu(resId);
		/* MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.physiotherapy, menu);
			MenuItem item = menu.findItem(R.id.action_settings);
			item.setActionView(R.layout.feed_update_count);
		    //
			 View count = item.getActionView();
			    notifCount = (Button) count.findViewById(R.id.notif_count);
			    notifCount.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "btn clicked", Toast.LENGTH_LONG).show();
						Intent i = new Intent(getApplicationContext(), Physiotherapy.class);
						startActivity(i);
					}
				});
			    notifCount.setText(String.valueOf(5));*/

		 /*   MenuItem item = menu.findItem(R.id.badge);
		    MenuItemCompat.setActionView(item, R.layout.feed_update_count); 
		    View count = item.getActionView();
		    notifCount = (Button) count.findViewById(R.id.notif_count);
		    notifCount.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "btn clicked", Toast.LENGTH_LONG).show();
				}
			});
		    notifCount.setText(String.valueOf(mNotifCount));*/
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
	
	public void showDialog()
	{
		boolean yes=false;
		boolean flag = true;	
		
		
		
		MediPhysioTextValidations medical = new MediPhysioTextValidations(view);
		yes = medical.validateFirstName();
		if(yes)
		{
			//flag = false;
			yes = medical.validateSurnmae();
			if(yes)
			{
				//flag = false;
				yes = medical.validateEmail();
				if(yes)
				{
					//flag = false;
					yes = medical.validatePhoneNo();
					if(yes)
					{
						//flag = false;
						yes = medical.validateState();
						if(yes)
						{
							//flag = false;
							yes = medical.validateCity();
							if(yes)
							{
							String pin = edt_pincode.getText().toString();
							if(pin.equals("")||pin.length()==0){
								yes = true;
							}else{
								int len = pin.length();
								if(len<6||len>6)
								{
										
									edt_pincode.setError("Enter Valid Pincode");
									yes =false;
								}
								long mobile_no = Long.parseLong(pin);
								if(mobile_no == 0)
								{
									edt_pincode.setError("Enter Valid pincode");
									yes =false;
								}
							}
							if(yes){
								String weight = edt_weight.getText().toString();
								if(weight.equals("")||weight.length()==0){
									yes=true;
								}else{
									long mobile_no = Long.parseLong(edt_weight.getText().toString());
									if(mobile_no == 0)
									{
										edt_weight.setError("Enter Valid Weight");
										yes=false;
									}
									
									else
									{
										int text = Integer.parseInt(weight);
										if(text<=0){
										//edtTxt_pincode.setText("");
											edt_weight.setError("Enter Valid Weight");
											yes=false;
									}
										int length = edt_weight.getText().toString().length();
										if(length>3)
										{
											edt_weight.setError("Enter Valid Weight");
											yes = false;
										}
									}
								}
								if(yes){
									String from = edt_From.getText().toString();
									if(from.equals("")||from.length()==0){
										yes=false;
										edt_From.setError("Please Select a correct starting date");
									}else{
										
										yes=true;
									}
									if(yes){
										String to = edt_To.getText().toString();
										if(to.equals("")||to.length()==0){
											yes=false;
											edt_To.setError("Please Select a correct ending date");
										}else{
											yes=true;
										}
										if(yes){
											callWebService();
											}
									}
									
								}
								
								
							}
							
								
								//flag = false;
					/*			yes = medical.validateFrom();
								if(yes)
								{
									//flag = false;
									yes = medical.validateTo();
									if(yes)
									{*/
										//callWebService();
										//flag = false;
							//		}
						//		}
							}
						}
					}
				}
			}
		}
	
	}
	public static void stateCityIds(String stateId,String cityId){
		state1 = stateId;
		city1 = cityId;
	}
	public void callWebService()
	{
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(this);
		int uid = ubean.getId();
		
		PhysioAndMedicalBean pDatabean = new PhysioAndMedicalBean();
		String fstname= edt_firstname.getText().toString();
		
		String fname=null;
		try {
			fname = URLEncoder.encode(fstname, "utf-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String famiName= edt_surname.getText().toString();
		String sname=null;
		try {
			sname = URLEncoder.encode(famiName, "utf-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String email = edt_email.getText().toString();
		String phoneNumber = edt_phoneno.getText().toString();
		String state = edt_state.getText().toString();
		String city = edt_city.getText().toString();		
		String From = edt_From.getText().toString();
		String to = edt_To.getText().toString();	
		txtcity.setText(city1);
		txtstate.setText(state1);
		String city_id = txtcity.getText().toString();
		String state_id = txtstate.getText().toString();
		String relation_id = txtrelation.getText().toString();
		String service_id = txtservice.getText().toString();
		
		String address = edt_address.getText().toString();
		
		try {
			address = URLEncoder.encode(address, "utf-8");

		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
   		String pincode = edt_pincode.getText().toString();
   		String relation = spinner_relation.getSelectedItem().toString();
   		String service = spinner_service.getSelectedItem().toString();
   		String weight = edt_weight.getText().toString();
   		
   		pDatabean.setFirstname(fstname);
   		pDatabean.setsurname(famiName);
   		pDatabean.setphoneno(phoneNumber);
   		pDatabean.setemail(email);
   		pDatabean.setState(state);
   		pDatabean.setCity(city);
   		pDatabean.setPincode(pincode);
   		pDatabean.setfrom(From);
   		pDatabean.setto(to);
   		pDatabean.setAddress(address);
   		pDatabean.setRelation(relation);
   		pDatabean.setService(service);
   		pDatabean.setCustId(""+uid);
		Okler.getInstance().setPhysioMedBean(pDatabean);
		
		
		SimpleDateFormat orgFormat = new SimpleDateFormat("dd-MM-yyyy");
	    Date dtOrg;
	    String formatedFromdt=From;
		try {
			dtOrg = orgFormat.parse(From);
			orgFormat = new SimpleDateFormat("yyyy-MM-dd");	    
			formatedFromdt =orgFormat.format(dtOrg);
			Log.e("Str", "fbbfv"+formatedFromdt);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
		String book_appointment_url = getResources().getString(R.string.bookAppointment)+"user_id="+uid+"&email="+ email 
				+"&firstname="+ fname
				+"&lastname="+ sname
				+"&mobile_no="+ phoneNumber
				+"&addr="+address
				+"&city="+ city_id + "&state=" + state_id + "&pincode="+pincode+"&relation="+rel_id 
				+"&required_type=3" +"&required_for="+ sev_id +"&patient_kgs="+weight+
				"&start_date="+formatedFromdt+"&end_date="+to;
		
		Log.e("final url", book_appointment_url);
		WebStringRequest webjson=new WebStringRequest(book_appointment_url, Method.GET, new Listener<String>() 
				{
					@Override
					public void onResponse(String response) 
					{
						// TODO Auto-generated method stub
						
						
						String responseObj =(String)response;
						JSONObject obj,resObj;
						try {
							 obj = new JSONObject(responseObj);
							 resObj = obj.getJSONObject("result");
							 String message = obj.getString("message");
							 
							 id = resObj.getInt("id");
							 Okler.getInstance().getPhysioMedBean().setOrder_id(""+id);
							 
							 UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(Physiotherapy.this);
								int cust_id = ubean.getId();
								String customer_name = ubean.getFname();
								String email = ubean.getEmail();
								
								if(message == "Physiotherapy service booking inserted successfully")
								{
									String order_physio_email = getResources().getString(R.string.send_email_order_url)+"salutation="+"cust_id="+cust_id+"&customer_name="+customer_name+"&email="+email+"&order_id="+id;
									
									WebJsonObjectRequest webObjReq=new WebJsonObjectRequest(Method.GET, order_physio_email, new JSONObject(),new Response.Listener<JSONObject>() 
											{

											@Override
											public void onResponse(JSONObject response) 
											{
												Log.i("email", "mail sent... ");

											}

											
										},new Response.ErrorListener() 
										{

												@Override
												public void onErrorResponse(VolleyError error) {
													
													Log.i("email", "mail not sent... ");

													
											}
												
											});
											
											VolleyRequest.addJsonObjectRequest(getApplicationContext(),webObjReq);
								}
							 
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//docCount=responseObj.getInt("TotalCount");
						
					//	response_from = responseObj.getString("message");
						Log.i("response", responseObj);
						//id = result.getString("id");
						//Okler.getInstance().getPhysioMedBean().setOrder_id(id);
									
						
						showProgress(false);
						
						Intent in = new Intent(Physiotherapy.this, MyOrderMS_History.class);
						in.putExtra("value", 2);
						in.putExtra("orderId", id);
						startActivity(in);
						
						/*AlertDialog alertDialog = new AlertDialog.Builder(currActivity).create();
					      alertDialog.setMessage("Thank You for submitting the details. Our Okler Assoiciate will contact you shortly.");
					      
					      alertDialog.setButton("Ok",new DialogInterface.OnClickListener() {
					         @Override
					         public void onClick(DialogInterface dialog, int which) {
					            finish();
					         }
					      });
					      
					      alertDialog.show();*/
					
						
						
				
					}}, 
					new Response.ErrorListener() 
					{

						@Override
						public void onErrorResponse(VolleyError error) 
						{
							// TODO Auto-generated method stub
							Log.e("Exception json", ""+error);;
							showProgress(false);
							Toast.makeText(getBaseContext(), "Error in connecting to Server", Toast.LENGTH_LONG).show();
						}
					}
		);
			
		if (VolleyRequest.addStringRequest(this,webjson))
		{
			showProgress(true);
		}
		else
		{
			showProgress(false);
		}
	}
	
	private void setNotifCount(int count){
	    mNotifCount = count;
	    invalidateOptionsMenu();
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
	 
	 public static void getRelid(String rId){
			rel_id = rId;
			
		}
		public static void getSevid(String sId){
			sev_id = sId;
			
		}
}
