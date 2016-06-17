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
import com.okler.android.OtpConfirmationActivity.ResetpassAsyncTask;
import com.okler.databeans.UsersDataBean;
import com.okler.interfaces.OtpReceivedCallback;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UserStatusEnum;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VerifyPhoneNumDialog extends DialogFragment {

	private static int WAIT_TIMEOUT = 2000, TOTAL_WAIT_TIMEOUT = 50000,
			CURRENT_TIMEOUT = 2000;
	Context diaContext;
	String phoneNumberReceived;
	Button editButton, okbutton;
	String otpUrl;
	String otpReceived;
	UsersDataBean userBean;
	Activity actCntx;
	static boolean otpReceivedFlag = false;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	ArrayList<NameValuePair> resetPassNameValPair = new ArrayList<NameValuePair>();
	boolean isFromNewSignIn = false;
	EditText ed_phoneNumberED;
	TextView viewPhoneNumber, tv_secondLine, tv_firstLine;

	public VerifyPhoneNumDialog() {
	}

	public VerifyPhoneNumDialog(Activity context, String phoneNumber,
			boolean isFromNewSignin) {

		actCntx = context;
		diaContext = actCntx.getApplicationContext();
		this.phoneNumberReceived = phoneNumber;
		WAIT_TIMEOUT = 2000;
		TOTAL_WAIT_TIMEOUT = 50000;
		CURRENT_TIMEOUT = 2000;
		isFromNewSignIn = isFromNewSignin;

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_verfiy_phone_number, null);
		builder.setView(view);
		final AlertDialog alert = builder.create();
		okbutton = (Button) view.findViewById(R.id.tv_ok);
		editButton = (Button) view.findViewById(R.id.tv_edit);

		viewPhoneNumber = (TextView) view.findViewById(R.id.tv_phoneNumber);
		tv_firstLine = (TextView) view.findViewById(R.id.tv_firstLine);
		tv_secondLine = (TextView) view.findViewById(R.id.tv_secondLine);

		ed_phoneNumberED = (EditText) view.findViewById(R.id.ed_phoneNumberED);
		if (isFromNewSignIn) {
			editButton.setVisibility(View.GONE);
			tv_secondLine.setVisibility(View.GONE);
			tv_firstLine.setText("Enter Phone number to verify OTP");
			ed_phoneNumberED.setVisibility(View.VISIBLE);
			viewPhoneNumber.setVisibility(View.GONE);

		}
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
				boolean flg=false;
			//	new SignUpAsyncTask().execute("");
				if(isFromNewSignIn)
				{
					 phoneNumberReceived = ed_phoneNumberED.getText().toString();
					 if(phoneNumberReceived == null)
					 {
						 ed_phoneNumberED.setError("Please enter correct phone number");
						 flg = true;
					 }
					 else if(phoneNumberReceived.equals(""))
					 {
						 ed_phoneNumberED.setError("Please enter correct phone number");
						 flg = true;
					 }
					 else if(phoneNumberReceived.length() !=10)
					 {
						 ed_phoneNumberED.setError("Please enter 10 digit phone number");
						 flg = true;
					 }
					 phoneNumberReceived = ed_phoneNumberED.getText().toString(); 
					 
					
					 
					 webOtpCall();
					 dismiss();
				}
				if(!flg)
				{
					if(/*Utilities.getUserStatusFromSharedPref(actCntx.getApplicationContext()) == UserStatusEnum.LOGGED_IN ||*/ 
							Utilities.getUserStatusFromSharedPref(actCntx) == UserStatusEnum.LOGGED_IN_FB || 
							Utilities.getUserStatusFromSharedPref(actCntx) == UserStatusEnum.LOGGED_IN_GOOGLE)
					{
						webOtpCall();
					}
					else
					{
						new SignUpAsyncTask().execute("");
					}
				}
				dismiss();
					
				//webOtpCall();
			//	alert.dismiss();
			}

		});

		return alert;

	}

	private void getOtp(String response) {
		JSONObject jsonObj = null;
		try {
			Gson gson = new Gson();
			jsonObj = new JSONObject(response);
			String message = jsonObj.getString("message");
			JSONObject result = jsonObj.getJSONObject("result");
			otpReceived = result.getString("sixdigitno");
			Log.e("OTP", otpReceived);
			Utilities.writeOtpToSharedPreferences(actCntx, otpReceived);
			// Toast.makeText(act, otpReceived, Toast.LENGTH_LONG).show();
			String msg = result.getString("msg");
		} catch (JSONException e) {
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
					String url=actCntx.getString(R.string.registerUser);
					userBean=Okler.getInstance().getuDataBean();
					Gson gson=new Gson();
					//System.out.println("oklerInstance"+(Okler.getInstance().getuDataBean()));
					nameValuePairs.add(new BasicNameValuePair("name", userBean.getFname()));
					nameValuePairs.add(new BasicNameValuePair("lastname", userBean.getLname()));
					nameValuePairs.add(new BasicNameValuePair("phone", userBean.getPhone()));
					nameValuePairs.add(new BasicNameValuePair("email", userBean.getEmail()));
					nameValuePairs.add(new BasicNameValuePair("image", userBean.getUser_image()));
					nameValuePairs.add(new BasicNameValuePair("password", userBean.getPassword()));
					nameValuePairs.add(new BasicNameValuePair("salutation", userBean.getSalutation()));
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
						
						//Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();
						int uid = json.getInt("user_id");
						String userimage = json.optString("profile_url");
						userBean=Okler.getInstance().getuDataBean();	
						userBean.setId(uid);
						userBean.setUser_image(userimage);
						Utilities.writeCurrentUserToSharedPref(actCntx,userBean);	
						//Utilities.writeUserStatusToSharedPref(actCntx.getApplicationContext(), UserStatusEnum.LOGGED_IN);
						//registrationCallbackMail();
						//userBean=Okler.getInstance().getuDataBean();
						//Utilities.writeCurrentUserToSharedPref(actCntx, userBean);
						webOtpCall();
						/*Intent success=new Intent(actCntx,OtpConfirmationActivity.class);
						actCntx.startActivity(success);*/
					}else{
				
						Toast.makeText(actCntx,messageReceived,Toast.LENGTH_LONG).show();
					}
						
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
			}
			}

	

	public void webOtpCall() {
		final ProgressDialog ringProgressDialog = ProgressDialog.show(actCntx,
				"Verifying OTP ...", "", true);
		ringProgressDialog.setCancelable(false);

		otpUrl = actCntx.getString(R.string.get_otp_for_phoneNum1)
				+ phoneNumberReceived
				+ actCntx.getString(R.string.get_otp_for_phoneNum2);
		WebJsonObjectRequest webObjReq = new WebJsonObjectRequest(Method.GET,
				otpUrl, new JSONObject(), new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						registrationCallbackMail();
						JSONObject j = (JSONObject) response;
						getOtp(response.toString());

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.d("AA", "Error" + error);
						// 712016****
						Toast.makeText(actCntx, "No Network Connection",
								Toast.LENGTH_LONG).show();
						// dismiss();
					}

				});

		VolleyRequest.addJsonObjectRequest(actCntx, webObjReq);
		// alert.dismiss();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				CURRENT_TIMEOUT = CURRENT_TIMEOUT + WAIT_TIMEOUT;
				String str = Utilities.getOtpStatusFromSharedPref(actCntx);
				if (str.equals("Verfied")) {
					// Toast.makeText(actCntx,
					// "Got otp.calling approved status",
					// Toast.LENGTH_LONG).show();
					userBean = Utilities.getCurrentUserFromSharedPref(actCntx); // Okler.getInstance().getuDataBean();
					Gson gson = new Gson();
					resetPassNameValPair.add(new BasicNameValuePair("user_id",
							String.valueOf(userBean.getId())));
					resetPassNameValPair.add(new BasicNameValuePair(
							"approved_status", String.valueOf(1)));
					new ResetpassAsyncTask().execute("");
				}

				else if (CURRENT_TIMEOUT > TOTAL_WAIT_TIMEOUT) {
					// Toast.makeText(actCntx,
					// "current timeout == wait timeout",
					// Toast.LENGTH_LONG).show();
					Intent startAct = new Intent(actCntx,
							OtpConfirmationActivity.class);
					actCntx.startActivity(startAct);
					actCntx.finish();
				} else {
					// Toast.makeText(actCntx, "wait for next 2sec",
					// Toast.LENGTH_LONG).show();
					waitForNext2Sec();
				}
			}
		}, WAIT_TIMEOUT);
	}

	/*
	 * @Override public void otpReceived() { // TODO Auto-generated method stub
	 * otpReceivedFlag = true; }
	 */

	private void waitForNext2Sec() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				// if user status is log in fb / log in google, then make user
				// login again
				CURRENT_TIMEOUT = CURRENT_TIMEOUT + WAIT_TIMEOUT;
				String str = Utilities.getOtpStatusFromSharedPref(actCntx);
				if (str.equals("Verfied")) {
					// Toast.makeText(actCntx,
					// "Got otp.calling approved status",
					// Toast.LENGTH_LONG).show();
					userBean = Utilities.getCurrentUserFromSharedPref(actCntx); // Okler.getInstance().getuDataBean();
					Gson gson = new Gson();
					resetPassNameValPair.add(new BasicNameValuePair("user_id",
							String.valueOf(userBean.getId())));
					resetPassNameValPair.add(new BasicNameValuePair(
							"approved_status", String.valueOf(1)));
					new ResetpassAsyncTask().execute("");
				}

				else if (CURRENT_TIMEOUT > TOTAL_WAIT_TIMEOUT) {
					// Toast.makeText(actCntx,
					// "current timeout == wait timeout",
					// Toast.LENGTH_LONG).show();
					Intent startAct = new Intent(actCntx,
							OtpConfirmationActivity.class);
					actCntx.startActivity(startAct);
					actCntx.finish();
				} else {
					// Toast.makeText(actCntx, "wait for next 2sec",
					// Toast.LENGTH_LONG).show();
					waitForNext2Sec();
				}
			}
		}, WAIT_TIMEOUT);
	}

	public class ResetpassAsyncTask extends AsyncTask<String, Object, Object> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			System.out.println("in preexecute");
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = actCntx.getResources().getString(R.string.registerUser);
			String Result = String.valueOf(Utilities.RegisterNewUser(url,
					resetPassNameValPair));
			return Result;

		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				System.out.println("in postexecute");
				JSONObject json = new JSONObject(result.toString());
				String messageReceived = json.getString("message");
				String respstatus = json.getString("status");
				System.out.println("status" + respstatus);
				if (messageReceived.equals("User Update successful.")) {
					userBean = Okler.getInstance().getuDataBean();
					Utilities.writeUserStatusEnumToSharedPref(actCntx,
							UserStatusEnum.LOGGED_IN);
					Utilities.writeCurrentUserToSharedPref(actCntx, userBean);
					Intent success = new Intent(actCntx,
							ServiceCategoryActivity.class);
					startActivity(success);
					Toast.makeText(actCntx, "OTP is verified successfully",
							Toast.LENGTH_LONG).show();

					//registrationCallbackMail();
				} else {
					Toast.makeText(actCntx, messageReceived, Toast.LENGTH_LONG)
							.show();
					Intent intent = new Intent(actCntx, SignUp.class);
					startActivity(intent);
				}
				dismiss();
				actCntx.finish();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	public void registrationCallbackMail(){
		UsersDataBean ubean = Utilities
				.getCurrentUserFromSharedPref(actCntx);

		int cust_id = ubean.getId();
		String salutation = ubean.getSalutation();
		String name = ubean.getFname();
		String email = ubean.getEmail();

		String user_registration = actCntx.getResources()
				.getString(R.string.serverUrl)
				+ actCntx.getResources().getString(
						R.string.user_registration_url)
				+ "salutation="
				+ salutation
				+ "&cust_id="
				+ cust_id
				+ "&customer_name="
				+ name
				+ "&email="
				+ email;

		WebJsonObjectRequest webObjReq = new WebJsonObjectRequest(
				Method.GET, user_registration, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.i("contact us", "*****  mail sent*****");
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

						// Log.e("error", new
						// String(error.networkResponse.data));

						Log.i("error", "" + error.getStackTrace());

						Log.i("contact us",
								"***** fail to send mail****");

					}

				});

		VolleyRequest.addJsonObjectRequest(actCntx, webObjReq);

	}
}
