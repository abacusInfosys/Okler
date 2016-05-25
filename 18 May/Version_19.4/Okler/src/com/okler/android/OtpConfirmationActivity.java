package com.okler.android;

import java.util.ArrayList;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.okler.databeans.UsersDataBean;
import com.okler.dialogs.EmailPhoneDialog;
import com.okler.dialogs.PasswordConfirmationDialog;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OtpConfirmationActivity extends BaseActivity {

	// Variables init
	EditText edt_getotp;
	Button btn_enter;
	String otpfompref;
	UsersDataBean userBean;
	Activity act;
	int in;
	String customer_name;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	public static int flag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_otp_confirmation);
		edt_getotp = (EditText) findViewById(R.id.editText_otp);
		btn_enter = (Button) findViewById(R.id.button_enter);
		act = this;
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(act);
		customer_name = ubean.getFname();

		otpfompref = Utilities.getOtpFromSharedPref(getApplicationContext());

		in = getIntent().getIntExtra("password", 0);
		btn_enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String otp_to_confirm = edt_getotp.getText().toString();
				if (otpfompref.equals(otp_to_confirm)) {
					if (in == 1) {
						Random randomGenerator = new Random();
						int guid = randomGenerator.nextInt(100000);
						String email = EmailPhoneDialog.email;
						String forgot_pass = getResources().getString(
								R.string.serverUrl)
								+ getResources().getString(
										R.string.forget_password)
								+ "salutation="
								+ "&customer_name="
								+ customer_name + "email=" + email;
						Log.d("Forgot pwd url", forgot_pass);
						WebJsonObjectRequest webObjReq = new WebJsonObjectRequest(
								Method.GET, forgot_pass, new JSONObject(),
								new Response.Listener<JSONObject>() {
									@Override
									public void onResponse(JSONObject response) {
										Log.d("Forgot password",
												"Web Serviceresponse received");
										try {
											JSONObject j = (JSONObject) response;
											JSONObject result = j
													.getJSONObject("result");
											String message = result
													.getString("message");
											Log.i("Forgot password", message);
										} catch (Exception e) {
											Log.i("Forgot password",
													String.valueOf(e));
										}
									}

								}, new Response.ErrorListener() {

									@Override
									public void onErrorResponse(
											VolleyError error) {
										// TODO Auto-generated method stub
										Log.d("Forgot password", "Error"
												+ error);
										// 712016****
										Toast.makeText(getApplicationContext(),
												"No Network Connection",
												Toast.LENGTH_LONG).show();
										Log.d("Forgot password",
												"Web Service called");
									}

								});
						Log.d("Forgot", "Web Service erro response received");

						/*
						 * if(flag == 0) {
						 */
						DefaultRetryPolicy retry = new DefaultRetryPolicy(3000,
								0, 2);
						webObjReq.setRetryPolicy(retry);
						VolleyRequest.addJsonObjectRequest(
								getApplicationContext(), webObjReq);
						// flag++;
						// Log.i("Forgot password flag value:", ""+(++flag));
						// }

						PasswordConfirmationDialog newpass = new PasswordConfirmationDialog(
								OtpConfirmationActivity.this);
						newpass.show();

					} else {
						userBean = Utilities
								.getCurrentUserFromSharedPref(OtpConfirmationActivity.this); // Okler.getInstance().getuDataBean();
						Gson gson = new Gson();
						nameValuePairs.add(new BasicNameValuePair("user_id",
								String.valueOf(userBean.getId())));
						nameValuePairs.add(new BasicNameValuePair(
								"approved_status", String.valueOf(1)));
						new ResetpassAsyncTask().execute("");
						/*
						 * Intent intent = new
						 * Intent(OtpConfirmationActivity.this,
						 * ServiceCategoryActivity.class);
						 * startActivity(intent);
						 */

					}
				} else {
					Toast.makeText(getApplicationContext(), "Otp incorrect",
							Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	public class SignUpAsyncTask extends AsyncTask<String, Object, Object> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			System.out.println("in preexecute");
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = getResources().getString(R.string.registerUser);
			String Result = String.valueOf(Utilities.RegisterNewUser(url,
					nameValuePairs));
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
				if (messageReceived.equals("User registration successful."))
				// if (respstatus == "1")
				{
					// Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();

					userBean = Okler.getInstance().getuDataBean();
					Utilities.writeUserStatusEnumToSharedPref(act,
							UserStatusEnum.LOGGED_IN);
					Utilities.writeCurrentUserToSharedPref(
							OtpConfirmationActivity.this, userBean);
					Intent success = new Intent(OtpConfirmationActivity.this,
							ServiceCategoryActivity.class);
					startActivity(success);
				} else {

					Toast.makeText(getApplicationContext(),
							messageReceived + "\n Please Enter Another Email",
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent(OtpConfirmationActivity.this,
							SignUp.class);
					startActivity(intent);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
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
			String url = getString(R.string.registerUser);
			String Result = String.valueOf(Utilities.RegisterNewUser(url,
					nameValuePairs));
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
				if (messageReceived.equals("User Update successful."))
				// if (respstatus == "1")
				{
					// Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();

					userBean = Okler.getInstance().getuDataBean();
					Utilities.writeUserStatusEnumToSharedPref(act,
							UserStatusEnum.LOGGED_IN);
					Utilities.writeCurrentUserToSharedPref(
							OtpConfirmationActivity.this, userBean);
					Intent success = new Intent(OtpConfirmationActivity.this,
							ServiceCategoryActivity.class);
					startActivity(success);
					
					UsersDataBean ubean = Utilities
							.getCurrentUserFromSharedPref(OtpConfirmationActivity.this);

					int cust_id = ubean.getId();
					String salutation = ubean.getSalutation();
					String name = ubean.getFname();
					String email = ubean.getEmail();

					String user_registration = getResources()
							.getString(R.string.serverUrl)
							+ getResources().getString(
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

					VolleyRequest.addJsonObjectRequest(OtpConfirmationActivity.this, webObjReq);
					
					
				} else {

					Toast.makeText(getApplicationContext(),
							messageReceived + "\n Please Enter Another Email",
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent(OtpConfirmationActivity.this,
							SignUp.class);
					startActivity(intent);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
