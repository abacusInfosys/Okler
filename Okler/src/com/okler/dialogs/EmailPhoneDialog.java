package com.okler.dialogs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.OrdersDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.Utilities;

public class EmailPhoneDialog extends Dialog implements
		android.view.View.OnClickListener {

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

		flag = validateEmail();
		if (flag == true) {
			phone = edt_phone.getText().toString();
			email = edt_email.getText().toString();
			// flag=validatePhoneNo();

			Utilities.writeToLogFIle("Got response. Phone " + phone + " Email"
					+ email);
			String isUserExist = c.getString(R.string.is_user_exists_url)
					+ phone + c.getString(R.string.mail) + email;

			WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET,
					isUserExist, new JSONObject(), new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub

							try {
								JSONObject responseObj = (JSONObject) response;
								Utilities.writeToLogFIle("Got response");

								String message = responseObj
										.getString("message");
								Utilities
										.writeToLogFIle("Got response from msg");
								if (message.equals("User Available")) {
									Utilities
											.writeToLogFIle("Got response, User available");
									JSONArray jrray = responseObj
											.optJSONArray("result");
									Utilities.writeToLogFIle("Get json arr");
									JSONObject resObject = jrray
											.getJSONObject(0);
									Utilities
											.writeToLogFIle("Got response respo obj");
									setUserData(resObject);
									Utilities
											.writeToLogFIle("after set user data");

									String url1, url2, forgotPassUrl;
									Utilities.writeToLogFIle("String");
									url1 = c.getString(R.string.serverUrl);
									Utilities
											.writeToLogFIle("Got response from msg. URL1:"
													+ url1);
									url2 = c.getString(R.string.forget_password);
									Utilities
											.writeToLogFIle("Got response from msg URL2:"
													+ url2);
									forgotPassUrl = url1 + url2 + email;
									Utilities
											.writeToLogFIle("Got response from msg.Forgot PWD:"
													+ forgotPassUrl);
									WebJsonObjectRequest forgotjson = new WebJsonObjectRequest(
											Method.GET,
											forgotPassUrl,
											new JSONObject(),
											new Response.Listener<JSONObject>() {

												@Override
												public void onResponse(
														JSONObject response) {

													JSONObject responseObj = (JSONObject) response;
													try {
														String result = responseObj
																.getString("result");
														if (result
																.equals("true")) {
															// salutation=Mr&customer_name=mark&email=testmail@gmail.com
															Utilities
																	.writeToLogFIle("Got response from msg.Result true");
															UsersDataBean ubean = Utilities
																	.getCurrentUserFromSharedPref(c);
															Utilities
																	.writeToLogFIle("Got response from msg.Result true. Ubean");
															String salutation = ubean
																	.getSalutation();
															salutation = URLEncoder
																	.encode(salutation,
																			"UTF-8");
															Utilities
																	.writeToLogFIle("Got response from msg.Result true. Salutation:"
																			+ salutation);
															String name = ubean
																	.getFname();
															name = URLEncoder
																	.encode(name,
																			"UTF-8");
															Utilities
																	.writeToLogFIle("Got response from msg.Result true. Name:"
																			+ name);
															String email = ubean
																	.getEmail();
															Utilities
																	.writeToLogFIle("Got response from msg.Result true. emial:"
																			+ email);
															Utilities
																	.writeToLogFIle("Got response from msg.ID:"
																			+ ubean.getId());
															String forgotpassUrl = c
																	.getString(R.string.serverUrl)
																	+ c.getString(R.string.forgotPasswordUrl)
																	+ "salutation="
																	+ salutation
																	+ "&customer_name="
																	+ name
																	+ "&email="
																	+ email;
															Utilities
																	.writeToLogFIle("Got response from msg. Forgot pass url:"
																			+ forgotpassUrl);
															WebJsonObjectRequest webObjReq = new WebJsonObjectRequest(
																	Method.GET,
																	forgotpassUrl,
																	new JSONObject(),
																	new Response.Listener<JSONObject>() {
																		@Override
																		public void onResponse(
																				JSONObject response) {
																			Log.i("contact us",
																					"*****  mail sent*****");
																			Utilities
																					.writeToLogFIle("Got response from msg. Forgot pass on response");
																		}

																	},
																	new Response.ErrorListener() {
																		@Override
																		public void onErrorResponse(
																				VolleyError error) {

																			// Log.e("error",
																			// new
																			// String(error.networkResponse.data));

																			Log.i("error",
																					""
																							+ error.getStackTrace());

																			Log.i("contact us",
																					"***** fail to send mail****");
																			Utilities
																					.writeToLogFIle("Got response from msg. Error response"
																							+ error.getStackTrace());
																		}

																	});

															VolleyRequest
																	.addJsonObjectRequest(
																			c,
																			webObjReq);

														}
													} catch (JSONException e) {
														// TODO Auto-generated
														// catch block
														e.printStackTrace();
													} catch (UnsupportedEncodingException e) {
														// TODO Auto-generated
														// catch block
														e.printStackTrace();
													}

													Utilities
															.writeToLogFIle("Got response. before Password confirmation dialog");

													PasswordConfirmationDialog newpass = new PasswordConfirmationDialog(
															c);
													newpass.show();
													Utilities
															.writeToLogFIle("Got response. after Password confirmation dialog");
													Log.e("Response", String
															.valueOf(response));

												}
											}, new Response.ErrorListener() {

												@Override
												public void onErrorResponse(
														VolleyError error) {
													// TODO Auto-generated
													// method stub
													Log.e("ERROR", String
															.valueOf(error));
													Utilities
															.writeToLogFIle("Got response. on error response");
												}
											}, true);
									VolleyRequest.addJsonObjectRequest(c,
											forgotjson);

								} else {
									Toast.makeText(c, "User Not Available",
											Toast.LENGTH_LONG).show();
								}
							} catch (JSONException jsonEx) {
								Log.e("Exception json", jsonEx.getStackTrace()
										.toString());
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Log.i("error", String.valueOf(error));
							// TODO Auto-generated method stub
							Utilities.writeToLogFIle("on err resposne");
						}
					});

			VolleyRequest.addJsonObjectRequest(c, webjson);
			dismiss();
		}
	}

	// wrte user details

	private void setUserData(JSONObject jobj1) {

		UsersDataBean ubean;
		try {
			ubean = new UsersDataBean();
			ubean.setId(Integer.parseInt(jobj1.optString("id")));
			Utilities.writeToLogFIle("In SetUserData() ID:" + ubean.getId());
			ubean.setSalutation(jobj1.optString("salutation"));
			Utilities.writeToLogFIle("In SetUserData() salutation:"
					+ ubean.getSalutation());
			ubean.setFname(jobj1.optString("firstname"));
			Utilities.writeToLogFIle("In SetUserData() first name"
					+ ubean.getFname());
			ubean.setLname(jobj1.optString("lastname"));
			Utilities.writeToLogFIle("In SetUserData() lastname:"
					+ ubean.getLname());
			ubean.setEmail(jobj1.optString("email"));
			Utilities.writeToLogFIle("In SetUserData() Email:"
					+ ubean.getEmail());
			ubean.setPhone(jobj1.optString("phone"));
			Utilities.writeToLogFIle("In SetUserData() Phone:"
					+ ubean.getPhone());
			ubean.setUserName(jobj1.optString("username"));
			Utilities.writeToLogFIle("In SetUserData() user name:"
					+ ubean.getUserName());
			ubean.setUser_image(jobj1.optString("user_image"));
			Utilities.writeToLogFIle("In SetUserData() user image:"
					+ ubean.getUser_image());
			ubean.setGender(jobj1.optString("gender"));
			Utilities.writeToLogFIle("In SetUserData() gender:"
					+ ubean.getGender());
			ubean.setDob(jobj1.optString("dob"));
			Utilities.writeToLogFIle("In SetUserData() dob:" + ubean.getDob());
			ubean.setRefCode(jobj1.optString("referral_code"));
			Utilities.writeToLogFIle("In SetUserData() ref code:"
					+ ubean.getRefCode());
			ubean.setCompany(jobj1.optString("company"));
			Utilities.writeToLogFIle("In SetUserData() company:"
					+ ubean.getCompany());
			ubean.setAvailable_status(Integer.parseInt(jobj1
					.optString("available_status")));
			Utilities.writeToLogFIle("In SetUserData() avail status:"
					+ ubean.getAvailable_status());
			ubean.setCurr_balance(Float.parseFloat(jobj1
					.optString("curr_balance")));
			Utilities.writeToLogFIle("In SetUserData() curr bal:"
					+ ubean.getCurr_balance());
			ubean.setAge(Integer.parseInt(jobj1.optString("age")));
			Utilities.writeToLogFIle("In SetUserData() Age:" + ubean.getAge());
			ubean.setBlood_group(jobj1.optString("blood_group"));
			Utilities.writeToLogFIle("In SetUserData() blood group:"
					+ ubean.getBlood_group());

			ArrayList<AddressDataBean> addList = new ArrayList<AddressDataBean>();
			AddressDataBean adbean;
			JSONObject jobj2;
			if (!jobj1.isNull("userAddr")) {
				Utilities.writeToLogFIle("In SetUserData() Got user address");
				JSONArray jarr2 = jobj1.getJSONArray("userAddr");
				int length = jarr2.length();

				for (int i = 0; i < length; i++) {
					adbean = new AddressDataBean();
					jobj2 = new JSONObject();
					int a = i + 1;
					if (a == length) {
						break;
					}
					jobj2 = jarr2.getJSONObject(a);
					// jobj2 = jarr2.getJSONObject(i);
					adbean.setFirstname(jobj2.optString("customer_name"));
					Utilities
							.writeToLogFIle("In SetUserData() Addresses fname:"
									+ adbean.getFirstname());
					adbean.setLastname(jobj2.optString("surname"));
					Utilities
							.writeToLogFIle("In SetUserData() Addresses fname:"
									+ adbean.getLastname());
					adbean.setAddress1(jobj2.optString("addr1"));
					Utilities
							.writeToLogFIle("In SetUserData() Addresses set addr1:"
									+ adbean.getAddress1());
					adbean.setAddress2(jobj2.optString("addr2"));
					Utilities
							.writeToLogFIle("In SetUserData() Addresses addr2:"
									+ adbean.getAddress2());
					adbean.setZip(jobj2.optString("zip"));
					Utilities.writeToLogFIle("In SetUserData() Addresses zip:"
							+ adbean.getZip());
					adbean.setCountry_id(jobj2.optString("country_id"));
					Utilities
							.writeToLogFIle("In SetUserData() Addresses country id:"
									+ adbean.getCountry_id());
					adbean.setAddr_id(jobj2.optString("addr_id"));
					Utilities
							.writeToLogFIle("In SetUserData() Addresses addr id:"
									+ adbean.getAddr_id());
					adbean.setCity(jobj2.optString("city_name"));
					Utilities
							.writeToLogFIle("In SetUserData() Addresses city name:"
									+ adbean.getCity());
					adbean.setState(jobj2.optString("state_name"));
					Utilities
							.writeToLogFIle("In SetUserData() Addresses state name:"
									+ adbean.getState());
					adbean.setCity_id(jobj2.optString("city_id"));
					Utilities
							.writeToLogFIle("In SetUserData() Addresses city id:"
									+ adbean.getCity_id());
					adbean.setState_id(jobj2.optString("state_id"));
					Utilities
							.writeToLogFIle("In SetUserData() Addresses state id:"
									+ adbean.getState_id());
					adbean.setCountry(jobj2.optString("country_name"));
					Utilities
							.writeToLogFIle("In SetUserData() Addresses country:"
									+ adbean.getCountry());
					adbean.setDefault_billing(jobj2.optInt("default_billing"));
					Utilities
							.writeToLogFIle("In SetUserData() Addresses default:"
									+ adbean.getDefault_billing());
					adbean.setDefault_shipping(jobj2.optInt("default_shiping"));
					Utilities
							.writeToLogFIle("In SetUserData() Addresses shipping:"
									+ adbean.getDefault_shipping());
					addList.add(adbean);
				}
				ubean.setAddDatabean(addList);
				Utilities.writeToLogFIle("In SetUserData() Addresses Set");
			}
			Utilities.writeToLogFIle("After if");
			Okler.getInstance().setuDataBean(ubean);
			Utilities.writeToLogFIle("In set ubean");
			Utilities.writeCurrentUserToSharedPref(c, ubean);
			Utilities.writeToLogFIle("User current ");
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("Exception", e.getStackTrace().toString());
			Utilities.writeToLogFIle("Got exception." + e.getStackTrace());
		}
	}

	/*
	 * private void getOtp(String response) { JSONObject jsonObj=null; try{ Gson
	 * gson=new Gson(); jsonObj=new JSONObject(response); String
	 * message=jsonObj.getString("message"); JSONObject
	 * result=jsonObj.getJSONObject("result"); String
	 * otpReceived=result.getString("sixdigitno");
	 * Utilities.writeOtpToSharedPreferences(c, otpReceived);
	 * 
	 * Intent i = new Intent(c, OtpConfirmationActivity.class); int in = 1;
	 * i.putExtra("password", in); c.startActivity(i);
	 * 
	 * 
	 * Toast.makeText(c, otpReceived, Toast.LENGTH_LONG).show(); String
	 * msg=result.getString("msg"); }catch (JSONException e) {
	 * e.printStackTrace();
	 * 
	 * }
	 * 
	 * }
	 */

	public boolean validateEmail() {
		this.editTextToValidate = edt_email;
		text = new TextValidations(editTextToValidate);

		return text.ValidateEmailId();
	}

	public boolean validatePhoneNo() {
		this.editTextToValidate = edt_phone;
		if (editTextToValidate.length() < 10
				|| editTextToValidate.length() > 10) {
			editTextToValidate.setError("Please enter 10 digit mobile number");
			return false;
		}

		long mobile_no = Long.parseLong(edt_phone.getText().toString());
		if (mobile_no == 0) {
			editTextToValidate.setError("Please enter your mobile number");
			return false;
		}

		return true;
	}

}
