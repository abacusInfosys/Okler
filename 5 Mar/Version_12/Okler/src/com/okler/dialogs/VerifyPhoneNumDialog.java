package com.okler.dialogs;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import com.android.volley.Response;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import com.okler.android.IntroScreen;

import com.okler.android.OtpConfirmationActivity;
import com.okleruser.R;
import com.okler.android.ServiceCategoryActivity;
import com.okler.android.SignUp;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VerifyPhoneNumDialog extends DialogFragment {
	
		private static int SPLASH_TIME_OUT = 20000;	
		Context diaContext;
		String phoneNumberReceived;
		Button editButton,okbutton;
		String otpUrl; 
		String otpReceived;
		UsersDataBean userBean;
		Activity act;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			public VerifyPhoneNumDialog(Activity context, String phoneNumber) {
		// TODO Auto-generated constructor stub
				
				//this.diaContext=context;
				//diaContext= getContext();
				act = context;
				this.phoneNumberReceived=phoneNumber;
			}
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
				View view=getActivity().getLayoutInflater().inflate(R.layout.dialog_verfiy_phone_number,null);
				builder.setView(view);
				final AlertDialog alert = builder.create();
				okbutton=(Button) view.findViewById(R.id.tv_ok);
				editButton=(Button) view.findViewById(R.id.tv_edit);
				TextView viewPhoneNumber;
				viewPhoneNumber =(TextView) view.findViewById(R.id.tv_phoneNumber);

				viewPhoneNumber.setText(phoneNumberReceived);
				editButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					dismiss();
					}
				});
				
				okbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new SignUpAsyncTask().execute("");
				alert.dismiss();
			}
								
				})	;
								
								
								
								
								
				/*otpUrl=getString(R.string.get_otp_for_phoneNum1)+phoneNumberReceived+getString(R.string.get_otp_for_phoneNum2);
				WebJsonObjectRequest webObjReq=new WebJsonObjectRequest(Method.GET, otpUrl, new JSONObject(),new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						JSONObject j=(JSONObject) response;
						getOtp(response.toString());
					}

					
				},new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.d("AA", "Error"+error);
						//712016****
						Toast.makeText(diaContext,"No Network Connection",Toast.LENGTH_LONG).show();
					}
					
				});
				
				VolleyRequest.addJsonObjectRequest(diaContext,webObjReq);
				alert.dismiss();
			}
		*/
		
		
		return alert;
		
	}
			private void getOtp(String response) {
				JSONObject jsonObj=null;
				try{
					Gson gson=new Gson();
					jsonObj=new JSONObject(response);
					String message=jsonObj.getString("message");
					JSONObject result=jsonObj.getJSONObject("result");
					otpReceived=result.getString("sixdigitno");
					Utilities.writeOtpToSharedPreferences(act, otpReceived);
					//Toast.makeText(act, otpReceived, Toast.LENGTH_LONG).show();
					String msg=result.getString("msg");
				}catch (JSONException e) {
					e.printStackTrace();

				}
		
			}
	
			public class SignUpAsyncTask extends AsyncTask<String,Object,Object>{

				
				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					System.out.println("in preexecute");
					super.onPreExecute();
				}
				@Override
				protected Object doInBackground(String... params) {
					// TODO Auto-generated method stub
					String url=act.getString(R.string.registerUser);
					userBean=Okler.getInstance().getuDataBean();
					Gson gson=new Gson();
					//System.out.println("oklerInstance"+(Okler.getInstance().getuDataBean()));
					nameValuePairs.add(new BasicNameValuePair("name", userBean.getFname()));
					nameValuePairs.add(new BasicNameValuePair("lastname", userBean.getLname()));
					nameValuePairs.add(new BasicNameValuePair("phone", userBean.getPhone()));
					nameValuePairs.add(new BasicNameValuePair("email", userBean.getEmail()));
					nameValuePairs.add(new BasicNameValuePair("image", userBean.getUser_image()));
					nameValuePairs.add(new BasicNameValuePair("password", userBean.getPassword()));
					//String json=gson.toJson(Okler.getInstance().getuDataBean());
					String Result = String.valueOf(Utilities.RegisterNewUser(url,nameValuePairs));
					return Result;
					
				}
				
					
			@Override
			protected void onPostExecute(Object result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				try {
					System.out.println("in postexecute");
					JSONObject json=new JSONObject(result.toString());
					String messageReceived=json.getString("message");
					String respstatus=json.getString("status");
					System.out.println("status"+respstatus);
					if(messageReceived.equals("User registration successful."))
				{
						webOtpCall();
						//Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();
						int uid = json.getInt("user_id");
						String userimage = json.optString("profile_url");
						userBean=Okler.getInstance().getuDataBean();	
						userBean.setId(uid);
						userBean.setUser_image(userimage);
						Utilities.writeCurrentUserToSharedPref(act,userBean);	
						//userBean=Okler.getInstance().getuDataBean();
					//	Utilities.writeCurrentUserToSharedPref(null, userBean);
						Intent success=new Intent(act,OtpConfirmationActivity.class);
						act.startActivity(success);
					}else{
				
						Toast.makeText(act,messageReceived,Toast.LENGTH_LONG).show();
					}
						
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
			}
			}

			public void webOtpCall() {
				final ProgressDialog ringProgressDialog = ProgressDialog.show(act, "Sending OTP ...", "", true);
    	        ringProgressDialog.setCancelable(true);
    	        
				otpUrl=act.getString(R.string.get_otp_for_phoneNum1)+phoneNumberReceived+act.getString(R.string.get_otp_for_phoneNum2);
				WebJsonObjectRequest webObjReq=new WebJsonObjectRequest(Method.GET, otpUrl, new JSONObject(),new Response.Listener<JSONObject>() 
				{

					@Override
					public void onResponse(JSONObject response) 
					{
						// TODO Auto-generated method stub
						JSONObject j=(JSONObject) response;
						getOtp(response.toString());
							/*userBean=Okler.getInstance().getuDataBean();
								Gson gson=new Gson();
								//System.out.println("oklerInstance"+(Okler.getInstance().getuDataBean()));
								nameValuePairs.add(new BasicNameValuePair("name", userBean.getFname()));
								nameValuePairs.add(new BasicNameValuePair("lastname", userBean.getLname()));
								nameValuePairs.add(new BasicNameValuePair("phone", userBean.getPhone()));
								nameValuePairs.add(new BasicNameValuePair("email", userBean.getEmail()));
								nameValuePairs.add(new BasicNameValuePair("image", userBean.getUser_image()));
								nameValuePairs.add(new BasicNameValuePair("password", userBean.getPassword()));
								String json=gson.toJson(Okler.getInstance().getuDataBean());
							*/	
								//System.out.println("JSON1"+json);
								//new SignUpAsyncTask().execute("");
								dismiss();
					}

					
				},new Response.ErrorListener() 
				{

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.d("AA", "Error"+error);
						//712016****
						Toast.makeText(act,"No Network Connection",Toast.LENGTH_LONG).show();
				}
					
				});
				
				VolleyRequest.addJsonObjectRequest(act,webObjReq);
				//alert.dismiss();
				
				new Handler().postDelayed(new Runnable() {			 
		            
		             /** Showing splash screen with a timer. This will be useful when you
		             * want to show case your app logo / company*/
		             	 
		            @Override
		            public void run() {
		                // This method will be executed once the timer is over
		                // Start your app main activity
		            	//if user status is log in fb / log in google, then make user login again

		            	Intent startAct = new Intent(act,OtpConfirmationActivity.class);
		        		act.startActivity(startAct);
		        		act.finish();
		            }
		        }, SPLASH_TIME_OUT);
				
								
								userBean=Okler.getInstance().getuDataBean();
								Gson gson=new Gson();
								//System.out.println("oklerInstance"+(Okler.getInstance().getuDataBean()));
								nameValuePairs.add(new BasicNameValuePair("name", userBean.getFname()));
								nameValuePairs.add(new BasicNameValuePair("lastname", userBean.getLname()));
								nameValuePairs.add(new BasicNameValuePair("phone", userBean.getPhone()));
								nameValuePairs.add(new BasicNameValuePair("email", userBean.getEmail()));
								nameValuePairs.add(new BasicNameValuePair("image", userBean.getUser_image()));
								nameValuePairs.add(new BasicNameValuePair("password", userBean.getPassword()));
								String json=gson.toJson(Okler.getInstance().getuDataBean());
								
								System.out.println("JSON1"+json);
								new SignUpAsyncTask().execute("");
								dismiss();
								
						
				
			}
}
			
		/*private void showProgress(boolean b) {
		// TODO Auto-generated method stub
		 if (b)
		    {
		      this.mProgress.setVisibility(View.VISIBLE);
		      return;
		    }
		    this.mProgress.setVisibility(View.INVISIBLE);
	}*/
			