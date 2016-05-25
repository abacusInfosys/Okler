package com.okler.dialogs;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;

import com.okler.android.OtpConfirmationActivity;
import com.okleruser.R;
import com.okler.android.SignUp;
import com.okler.databeans.OrdersDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.Utilities;

public class EmailPhoneDialog extends Dialog implements android.view.View.OnClickListener {

	  

	public Activity c;
	  public Dialog d;
	  String cancel_order;
	  
	  EditText edt_email, edt_phone;
	  TextView txt_submit;
	private EditText editTextToValidate;
	TextValidations text;
	  boolean flag;
	  public static String email, phone;
	  public static String id;
	  
	  
	  public EmailPhoneDialog(Activity a) {
			super(a);
			// TODO Auto-generated constructor stub
			this.c = a;
		}
	  
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.dialog_email_phone);
	    edt_email = (EditText) findViewById(R.id.edt_email);
	    edt_phone = (EditText) findViewById(R.id.edt_phone);
	    txt_submit = (TextView) findViewById(R.id.text_submit);
	    txt_submit.setOnClickListener(this);
	  }

	  @Override
	  public void onClick(View v) {
		  
		  flag=validateEmail();
			if(flag==true)
			{
				email = edt_email.getText().toString();					
				//flag=validatePhoneNo();
				if(flag==true)
				{
					//phone = edt_phone.getText().toString();
					
					String isUserExist = "https://www.okler.com/api/users/getphonenumMail?phoneno="+phone+"&mail="+email;
					  
					  WebJsonObjectRequest webjson=new WebJsonObjectRequest(Method.GET, isUserExist, new JSONObject(),new Listener<JSONObject>() 
								{
									@Override
									public void onResponse(JSONObject response) 
									{
										// TODO Auto-generated method stub
										
										try
										{
										JSONObject responseObj =(JSONObject)response;
										/*JSONArray result = responseObj.getString("message");
										JSONObject obj1 = result.getJSONObject(0);
										id = obj1.optString("id");
										String result = responseObj.getString("result");
										*/String message = responseObj.getString("message");
										
										if(message.equals("User Available"))
										{
											String otpUrl="https://www.okler.com/api/otp/send_otp?mobileNumber="+phone+"&reason_type=reset";
											WebJsonObjectRequest webObjReq=new WebJsonObjectRequest(Method.GET, otpUrl, new JSONObject(),new Response.Listener<JSONObject>() 
											{

									@Override
									public void onResponse(JSONObject response) 
									{
										// TODO Auto-generated method stub
										JSONObject j=(JSONObject) response;
										getOtp(response.toString());
									}

									
								},new Response.ErrorListener() 
								{

									@Override
									public void onErrorResponse(VolleyError error) {
										// TODO Auto-generated method stub
										Log.d("AA", "Error"+error);
										//712016****
										Toast.makeText(c,"No Network Connection",Toast.LENGTH_LONG).show();
								}
									
								});
								
								VolleyRequest.addJsonObjectRequest(c,webObjReq);
								
								final ProgressDialog ringProgressDialog = ProgressDialog.show(c, "Sending OTP ...", "", true);
				    	        ringProgressDialog.setCancelable(true);
								
								
							}
							else 
							{
								Toast.makeText(c, "Invalid User", Toast.LENGTH_LONG).show();
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
								Log.i("error", String.valueOf(error));
								// TODO Auto-generated method stub
					
							}
						}
			);
		
	VolleyRequest.addJsonObjectRequest(c,webjson);
	dismiss();
				}
			}
			else
			{
				
				Toast.makeText(c, "All fields are mandatory", Toast.LENGTH_LONG).show();
			}
	  }
	  
		private void getOtp(String response) {
			JSONObject jsonObj=null;
			try{
				Gson gson=new Gson();
				jsonObj=new JSONObject(response);
				String message=jsonObj.getString("message");
				JSONObject result=jsonObj.getJSONObject("result");
				String otpReceived=result.getString("sixdigitno");
				Utilities.writeOtpToSharedPreferences(c, otpReceived);
				
				Intent i = new Intent(c, OtpConfirmationActivity.class);
				int in = 1;
				i.putExtra("password", in);
				c.startActivity(i);

				
				Toast.makeText(c, otpReceived, Toast.LENGTH_LONG).show();
				String msg=result.getString("msg");
			}catch (JSONException e) {
				e.printStackTrace();

			}
	
		}
		
		
		public boolean validateEmail()
		{
			this.editTextToValidate = edt_email;
			text = new TextValidations(editTextToValidate);
			
			return text.ValidateEmailId();
		}
		
		public boolean validatePhoneNo()
		{
			this.editTextToValidate = edt_phone;
			if(editTextToValidate.length()<10 ||editTextToValidate.length()>10)
			{
				editTextToValidate.setError("Please enter 10 digit mobile number");
				return false;
			}
			
			long mobile_no = Long.parseLong(edt_phone.getText().toString());
			if(mobile_no == 0)
			{
				editTextToValidate.setError("Please enter your mobile number");
				return false;
			}
			
				return true;
		}
	  
	}
