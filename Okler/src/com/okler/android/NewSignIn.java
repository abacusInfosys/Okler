package com.okler.android;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.okler.utils.SocialMediaUtils;
//import com.okler.utils.SocialMediaUtils;
import com.okler.Profile.ProfileChangePassword;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.helpnsupport.SupportHelpActivity;
import com.okler.diagnostics.EnterPatientInfoActivity;
import com.okler.diagnostics.SelectPatientInfoActivity;
import com.okler.dialogs.EmailPhoneDialog;
import com.okler.dialogs.VerifyPhoneNumDialog;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.SocialLoginEnum;
import com.okler.utils.TextValidations;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NewSignIn extends BaseActivity {
	private String email, password;
	private EditText et_email, et_passWord;
	private Button sigin;
	private Context context;
	private EditText editTextToValidate;
	private boolean flag;
	private TextValidations text;
	private TextView forgetPassword;
	private ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	private String otpReceived;
	private int SPLASH_TIME_OUT = 1200;
	String getMyCartUrl;
	ArrayList<AddressDataBean> paList = new ArrayList<AddressDataBean>();
	// Gitesh 13 feb 2016
	LoginButton facebook_Login;
	Button fbNew;
	ImageView google_login;
	SocialMediaUtils scUtils;
	CallbackManager callbackManager;
	private static final int RC_SIGN_IN = 100000;
	private boolean mIntentInProgress;
	Activity ack;
	UsersDataBean ubean2 = new UsersDataBean();
	UsersDataBean ubean = new UsersDataBean();
	String msg;
	ArrayList<ProductDataBean> myfav = new ArrayList<ProductDataBean>();

	// end
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scUtils = new SocialMediaUtils(this, false);
		SocialMediaUtils.initFacebookSDK(this);
		callbackManager = CallbackManager.Factory.create();
		setContentView(R.layout.activity_new_sign_in);
		context = getApplicationContext();
		et_email = (EditText) findViewById(R.id.et_emailAdd);
		et_passWord = (EditText) findViewById(R.id.et_password);
		sigin = (Button) findViewById(R.id.signIN_button);
		facebook_Login = (LoginButton) findViewById(R.id.facebook_Login);
		fbNew = (Button) findViewById(R.id.fbNewBtn);
		google_login = (ImageView) findViewById(R.id.google_Login);
		ack = this;
		SocialMediaUtils.signOutOfFB(ack);
		// for google login

		google_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!SocialMediaUtils.mGoogleApiClient.isConnected())
					SocialMediaUtils.mGoogleApiClient.connect();
				SocialLoginEnum scLogin = SocialLoginEnum.SIGNIN;
				Utilities.writeSignInSignUpStatus(ack, scLogin);
			}
		});

		fbNew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				SocialMediaUtils.setFacebookCallback(callbackManager, ack,
						facebook_Login);
				facebook_Login.performClick();
				fbNew.setEnabled(false);
				SocialLoginEnum scLogin = SocialLoginEnum.SIGNIN;
				Utilities.writeSignInSignUpStatus(ack, scLogin);
			}
		});

		if (Utilities
				.getUserStatusFromSharedPref(NewSignIn.this) == UserStatusEnum.LOGGED_OUT) {
			SocialMediaUtils.signOutOfFB(ack);
		}
		TextView tv_elseLogin = (TextView) findViewById(R.id.tv_elseLogin);
		tv_elseLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(NewSignIn.this, SignUp.class);
				startActivity(i);
			}
		});

		sigin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = validateEmail();
				if (flag == true) {
					email = et_email.getText().toString();
				}
				flag = validatePassword();
				if (flag == true) {
					password = et_passWord.getText().toString();
				}
				// String
				// url=getString(R.string.signinUser1)+email+getString(R.string.signinUser2);
				nameValuePairs.clear();
				nameValuePairs.add(new BasicNameValuePair("email", email));
				nameValuePairs
						.add(new BasicNameValuePair("password", password));
				new SignInAsyncTask().execute("");

			}
		});
		forgetPassword = (TextView) findViewById(R.id.tv_forgetPassword);
		forgetPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				EmailPhoneDialog emailPhone = new EmailPhoneDialog(
						NewSignIn.this);
				emailPhone.show();

			}

		});
	}

	public void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == RC_SIGN_IN || requestCode == 34464) {
			// For google
			mIntentInProgress = false;
			if (!SocialMediaUtils.mGoogleApiClient.isConnected()) {
				SocialMediaUtils.mGoogleApiClient.connect();
			}
		} else {
			// For FB
			callbackManager.onActivityResult(requestCode, responseCode, intent);
		}
	}

	private void getOtp(String response) {
		JSONObject jsonObj = null;
		try {
			Gson gson = new Gson();
			jsonObj = new JSONObject(response);
			String message = jsonObj.getString("message");
			JSONObject result = jsonObj.getJSONObject("result");
			otpReceived = result.getString("sixdigitno");
			Utilities.writeStringToSharedPref(getParent(), "otpReceived",
					otpReceived);
			new Handler().postDelayed(new Runnable() {
				/*
				 * Showing splash screen with a timer. This will be useful when
				 * you want to show case your app logo / company
				 */
				@Override
				public void run() {
					// This method will be executed once the timer is over
					// Start your app main activity
					// if user status is log in fb / log in google, then make
					// user login again
					String statusOtp = Utilities
							.getOtpStatusFromSharedPref(context);
					if (statusOtp.equals("Verfied")) {
						// Toast.makeText(context,"Otp verified",Toast.LENGTH_LONG).show();
						Intent startAct = new Intent(getApplicationContext(),
								ProfileChangePassword.class);
						startActivity(startAct);
						finish();
					} else {
						// Toast.makeText(context,"TimeOut,please try again",Toast.LENGTH_LONG).show();
					}
				}
			}, SPLASH_TIME_OUT);
			String msg = result.optString("msg");
		} catch (JSONException e) {
			e.printStackTrace();

		}

	}

	public class SignInAsyncTask extends AsyncTask<String, Object, Object> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = getString(R.string.signinUser1);
			String Result = String.valueOf(Utilities.RegisterNewUser(url,
					nameValuePairs));
			return Result;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				String res = "[" + String.valueOf(result) + "]";

				JSONArray jarr = new JSONArray(res);
				msg = jarr.getJSONObject(0).optString("message");
				if (msg.equals("User authentcation is successfull.")) {
					JSONObject jobj1 = jarr.getJSONObject(0).getJSONObject(
							"result");
					JSONArray jarr3 = jobj1.getJSONArray("cart_items");
					setUserData(jobj1);
					setUserCart(jarr3);
					setUserAddress();
					getAllFavourites();
					Toast.makeText(getApplicationContext(),
							"Logged in Successfully", Toast.LENGTH_LONG).show();
					navigation();
				} else if (msg
						.equals("User registration is not Approved (OTP).")) {
					JSONObject jobj1 = jarr.getJSONObject(0).getJSONObject(
							"result");
					setUserData(jobj1);
					// Start OTP flow, verify OTP
					UsersDataBean curUsr = Utilities
							.getCurrentUserFromSharedPref(ack);
					String phoneNum = curUsr.getPhone();
					// Toast.makeText(getApplicationContext(),"Login unsuccessful,Please Complete Registration Process",Toast.LENGTH_LONG).show();
					new VerifyPhoneNumDialog(ack, phoneNum, true).show(
							getFragmentManager(), "");
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Failed to authenticate user, The email and password you entered does not match.",
							Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("IN Post execute" + result);
		}
	}

	private void setUserData(JSONObject jobj1) {
		try {
			ubean = new UsersDataBean();
			ubean.setId(Integer.parseInt(jobj1.optString("id")));
			ubean.setFname(jobj1.optString("firstname"));
			ubean.setLname(jobj1.optString("lastname"));
			ubean.setEmail(jobj1.optString("email"));
			ubean.setPhone(jobj1.optString("phone"));
			ubean.setUserName(jobj1.optString("username"));
			ubean.setUser_image(jobj1.optString("user_image"));
			ubean.setGender(jobj1.optString("gender"));
			ubean.setDob(jobj1.optString("dob"));
			ubean.setRefCode(jobj1.optString("referral_code"));
			ubean.setCompany(jobj1.optString("company"));
			ubean.setAvailable_status(Integer.parseInt(jobj1
					.optString("available_status")));
			ubean.setCurr_balance(Float.parseFloat(jobj1
					.optString("curr_balance")));
			ubean.setAge(Integer.parseInt(jobj1.optString("age")));
			ubean.setBlood_group(jobj1.optString("blood_group"));

			ArrayList<AddressDataBean> addList = new ArrayList<AddressDataBean>();
			AddressDataBean adbean;
			JSONObject jobj2;
			if (!jobj1.isNull("userAddr")) {
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
					adbean.setLastname(jobj2.optString("surname"));
					adbean.setAddress1(jobj2.optString("addr1"));
					adbean.setAddress2(jobj2.optString("addr2"));
					adbean.setZip(jobj2.optString("zip"));
					adbean.setCountry_id(jobj2.optString("country_id"));
					adbean.setAddr_id(jobj2.optString("addr_id"));
					adbean.setCity(jobj2.optString("city_name"));
					adbean.setState(jobj2.optString("state_name"));
					adbean.setCity_id(jobj2.optString("city_id"));
					adbean.setState_id(jobj2.optString("state_id"));
					adbean.setCountry(jobj2.optString("country_name"));
					adbean.setDefault_billing(jobj2.optInt("default_billing"));
					adbean.setDefault_shipping(jobj2.optInt("default_shiping"));
					addList.add(adbean);
				}
				ubean.setAddDatabean(addList);
			}
			Okler.getInstance().setuDataBean(ubean);
			Utilities.writeCurrentUserToSharedPref(NewSignIn.this, ubean);
			if (msg.equals("User authentcation is successfull."))
				Utilities.writeUserStatusToSharedPref(NewSignIn.this,
						UserStatusEnum.LOGGED_IN);
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("Exception", e.getStackTrace().toString());
		}
	}

	private void setUserCart(JSONArray jarr3) {
		CartDataBean mainCart = new CartDataBean();
		try {
			int length2 = jarr3.length();
			ArrayList<ProductDataBean> prodList = new ArrayList<ProductDataBean>();
			ProductDataBean pdbean;
			for (int i = 0; i < length2; i++) {
				JSONObject jobj3 = jarr3.getJSONObject(i);
				pdbean = new ProductDataBean();
				pdbean.setCart_id(jobj3.optString("cart_id"));
				pdbean.setCart_num(jobj3.optString("cart_number"));
				pdbean.setProdId(Integer.parseInt(jobj3.optString("id")));
				pdbean.setTax(Float.parseFloat(jobj3.optString("tax")));
				pdbean.setPresc_id(jobj3.optString("prescription_id"));
				pdbean.setProdName(jobj3.optString("name"));
				pdbean.setUnits(Integer.parseInt(jobj3.optString("quantity")));
				pdbean.setCart_item_id(Integer.parseInt(jobj3
						.optString("cart_item_id")));
				pdbean.setMrp(Float.parseFloat(jobj3.optString("price")));
				pdbean.setOklerPrice(Float.parseFloat(jobj3
						.optString("saleprice")));
				pdbean.setDiscount(Float.parseFloat(jobj3.optString("discount")));
				String ajimg = jobj3.optString("images");
				String aurl2;
				if (ajimg.equals(null) || ajimg.equals("null")
						|| ajimg.equals("")) {
					aurl2 = "drawable://" + R.drawable.no_image_found;
				} else {
					if (ajimg.contains(",")) {

						String aj1[] = ajimg.split(",");
						String aj2 = aj1[0];
						String acolon = ":";
						String aj3[] = aj2.split(acolon);
						String aurl = aj3[2];
						String aurl1 = aurl.substring(1);
						int alength = aurl.length();
						aurl2 = aurl.substring(1, (alength - 1));
					} else {
						aurl2 = ajimg;
					}
				}
				pdbean.setImgUrl(aurl2);
				if (jobj3.optString("prescription_need").equals("null")
						|| jobj3.optString("prescription_need").equals(null)
						|| jobj3.optString("prescription_need").equals("")) {
					pdbean.setPresc_needed(0);
				} else {
					pdbean.setPresc_needed(Integer.parseInt(jobj3
							.optString("prescription_need")));
				}
				// pdbean.setGeneric_name(jobj1.optString(name));
				pdbean.setCompany(jobj3.optString("company_name"));
				pdbean.setProdType(Integer.parseInt(jobj3.optString("pro_type")));
				prodList.add(pdbean);
			}
			mainCart.setProdList(prodList);
			Okler.getInstance().setMainCart(mainCart);
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("Exception", "Ex");
		}
	}

	private void setUserAddress() {
		// Gitesh 13 feb 2016
		int userId = ubean.getId();

		String AddrUrl = getString(R.string.GetPatientsAllAddr) + userId;
		WebJsonObjectRequest pjson = new WebJsonObjectRequest(Method.GET,
				AddrUrl, new JSONObject(), new Response.Listener<JSONObject>() {

					AddressDataBean adbean = new AddressDataBean();
					JSONObject jobj = new JSONObject();
					JSONObject jobj2;

					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray jarr = response.getJSONArray("result");
							int length = jarr.length();

							for (int i = 0; i < length; i++) {
								// jobj = new JSONObject();
								jobj = jarr.getJSONObject(i);
								adbean = new AddressDataBean();
								adbean.setPat_id(Integer.parseInt(jobj
										.optString("pat_id")));
								adbean.setFirstname(jobj.optString("firstname"));
								adbean.setLastname(jobj.optString("middlename"));
								adbean.setDob(jobj.optString("dob"));
								adbean.setRelationId(jobj
										.optString("relationid"));
								adbean.setGenderId(jobj.optString("gender"));
								adbean.setPhone(jobj.optString("mobileno"));
								jobj2 = new JSONObject();
								jobj2 = jobj
										.getJSONObject("residential_address");
								adbean.setAddress1(jobj2.optString("addr1"));
								adbean.setAddress2(jobj2.optString("addr2"));
								adbean.setLandmark(jobj2.optString("land_mark"));
								adbean.setZip(jobj2.optString("pincode"));
								adbean.setCity_id(jobj2.optString("city_id"));
								adbean.setCity(jobj2.optString("city_name"));
								adbean.setGender(jobj.optString("userGender"));
								adbean.setRelation(jobj
										.optString("relation_name"));

								paList.add(adbean);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ubean.setPatAddList(paList);
						Utilities.writeCurrentUserToSharedPref(ack, ubean);
						// setUi();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						// setUi();
					}
				});
		VolleyRequest.addJsonObjectRequest(ack, pjson);
	}

	private void navigation() {
		switch (Okler.getInstance().getBookingType()) {
		case 0: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			/*
			 * Intent success=new
			 * Intent(NewSignIn.this,ProductCheckoutListView.class);
			 * startActivity(success);
			 */
			finish();
			break;
		}
		case 1: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			/*
			 * Intent success=new
			 * Intent(NewSignIn.this,ProductCheckoutListView.class);
			 * startActivity(success);
			 */
			finish();
			break;

		}
		case 3: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			/*
			 * Intent success=new
			 * Intent(NewSignIn.this,ProductCheckoutListView.class);
			 * startActivity(success);
			 */
			finish();
			break;

		}
		case 4: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			/*
			 * Intent success=new
			 * Intent(NewSignIn.this,ProductCheckoutListView.class);
			 * startActivity(success);
			 */
			finish();
			break;

		}
		case 9: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
			ArrayList<AddressDataBean> paList = new ArrayList<AddressDataBean>();
			paList = ubean.getPatAddList();
			if (paList.size() <= 0) {
				Intent intent = new Intent(getApplicationContext(),
						EnterPatientInfoActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(getApplicationContext(),
						SelectPatientInfoActivity.class);
				startActivity(intent);
			}
			break;

		}
		case 11: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(NewSignIn.this,
					UploadPrescription.class);
			startActivity(success);
			finish();
			break;
		}
		case 12: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(NewSignIn.this, MyAccount.class);
			startActivity(success);
			finish();
			break;
		}
		case 100: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(NewSignIn.this,
					ServiceCategoryActivity.class);
			Okler.getInstance().setBookingType(100);
			startActivity(success);
			finish();
			break;
		}
		case 13: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(NewSignIn.this, Physiotherapy.class);
			Okler.getInstance().setBookingType(100);
			startActivity(success);
			finish();
			break;
		}
		case 14: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(NewSignIn.this, MedicalServices.class);
			Okler.getInstance().setBookingType(100);
			startActivity(success);
			finish();
			break;
		}

		case 18: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(NewSignIn.this,
					SupportHelpActivity.class);
			Okler.getInstance().setBookingType(100);
			startActivity(success);
			finish();
			break;
		}

		case 19: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(NewSignIn.this, MyCart.class);
			Okler.getInstance().setBookingType(100);
			startActivity(success);
			finish();
			break;
		}
		case 20: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
			ArrayList<AddressDataBean> paList = new ArrayList<AddressDataBean>();
			paList = ubean.getPatAddList();
			if (paList.size() <= 0) {
				Intent intent = new Intent(getApplicationContext(),
						EnterPatientInfoActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(getApplicationContext(),
						SelectPatientInfoActivity.class);
				startActivity(intent);
			}
			break;
		}
		case 21: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(NewSignIn.this,
					NotificationActivity.class);
			Okler.getInstance().setBookingType(100);
			startActivity(success);
			finish();
			break;
		}
		case 22: {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			Uri Uri = null;
			intent.setData(Uri.parse("market://details?id=com.okleruser"));// 11_01_2016
																			// Gitesh
			if (!MyStartActivity(intent)) {
				// Market (Google play) app seems not installed, let's try to
				// open a webbrowser
				intent.setData(Uri
						.parse("https://play.google.com/store/apps/details?id=com.okleruser"));// 11_01_2016
																								// Gitesh
				if (!MyStartActivity(intent)) {
					// Well if this also fails, we have run out of options,
					// inform the user.
					// Toast.makeText(this,
					// "Could not open Android market, please install the market app.",
					// Toast.LENGTH_SHORT).show();
				}
			}
			finish();
			/*
			 * Log.i("bookin type",toString().valueOf(Okler.getInstance().
			 * getBookingType())); Intent success=new
			 * Intent(NewSignIn.this,NotificationActivity.class);
			 * Okler.getInstance().setBookingType(100); startActivity(success);
			 * finish();
			 */
			break;
		}
		case 23: {
			Intent startUpPresc = new Intent(NewSignIn.this,UploadPrescription.class);
			startUpPresc.putExtra("isMedPres", false);
			Okler.getInstance().setBookingType(UIUtils.getBookingType("Diagnostic"));
			startActivity(startUpPresc);
			finish();
			break;
		}

		}

	}

	public boolean validateEmail() {
		this.editTextToValidate = et_email;
		text = new TextValidations(editTextToValidate);
		text.ValidateEmailId();
		return true;
	}

	public boolean validatePassword() {
		this.editTextToValidate = et_passWord;
		text = new TextValidations(editTextToValidate);
		return text.validateText("Please Enter password");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_sign_in, menu);
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

	// chetan
	private boolean MyStartActivity(Intent aIntent) {
		try {
			startActivity(aIntent);
			return true;
		} catch (ActivityNotFoundException e) {
			return false;
		}
	}
	
	private void getAllFavourites()
	{
		
			if (Utilities.getUserStatusFromSharedPref(this) == UserStatusEnum.LOGGED_IN ||
					(Utilities.getUserStatusFromSharedPref(this) == UserStatusEnum.LOGGED_IN_FB) ||
					(Utilities.getUserStatusFromSharedPref(this) == UserStatusEnum.LOGGED_IN_GOOGLE))
			{
				UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(this);
				int uid = ubean.getId();
	    	  String get_fav = getString(R.string.get_fav_url)+uid;
	 	     
	 	     
	 	     WebJsonObjectRequest webjson=new WebJsonObjectRequest(Method.GET, get_fav, new JSONObject(),new Listener<JSONObject>() 
	 					{
	 						@Override
	 						public void onResponse(JSONObject response) 
	 						{
	 							// TODO Auto-generated method stub
	 							
	 							ProductDataBean pbean;
	 							
	 							try
	 							{
	 							JSONObject responseObj =(JSONObject)response;
	 							String result = responseObj.getString("result");
	 					//		Toast.makeText(getApplicationContext(), "result" + result, Toast.LENGTH_LONG).show();
	 							
	 							JSONArray doctorsArr = responseObj.getJSONArray("result");
	 							//docCount=responseObj.getInt("TotalCount");
	 							myfav = new ArrayList<ProductDataBean>();
	 							for(int i = 0 ; i < doctorsArr.length();i++)
	 							{
	 								pbean = new ProductDataBean();
	 								try
	 								{
	 									JSONObject docObj =(JSONObject) doctorsArr.get(i);
	 									pbean.setProdName(docObj.getString("name"));
	 									pbean.setProdId(docObj.getInt("id"));
	 									pbean.setDesc(docObj.getString("description"));
	 									pbean.setMrp(docObj.getInt("price"));
	 									pbean.setOklerPrice(docObj.getInt("saleprice"));
	 									Float discount = Float.parseFloat(docObj.getString("discount"));
	 									pbean.setDiscount(discount);
	 									
	 									String jimg = docObj.getString("images");
	 									String url2;
	 									if(jimg.equals(null)||jimg.equals("null")||jimg.equals("")){
	 										url2 = "drawable://" + R.drawable.tempcuff;
	 									}else{
	 									String j1[] =jimg.split(",");
	 									String j2=j1[0];
	 									String colon = ":";
	 									String j3[]=j2.split(colon);
	 									String url = j3[2];
	 									//String url1 = url.substring(1);
	 									int length = url.length();
	 									url2 = url.substring(1, (length-1));
	 									}
	 									//JSONObject jimg2 = jimg.getJSONObject("");
	 									//JSONObject jimg3 = jobj2.getJSONObject("images");
	 									//pdBean.setImgUrl(jobj2.getJSONObject("images").getJSONObject("").getString("filename"));
	 									pbean.setImgUrl(url2);
	 									
	 									Log.i("tag", "json object" + docObj);
	 									}catch (JSONException e) {
	 										// TODO: handle exception
	 										Log.e("JSON Exception", e.getMessage());
	 									}
	 								
	 								myfav.add(pbean);
	 								
	 							 }
	 								Okler.getInstance().setFavourites(myfav);
	 								
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
			}
	      
	      {
	    	  ProductDataBean pbean = new ProductDataBean();
	    	  //pbean = Utilities.getFavouritesFromSharedPref(this);
	    	  Log.i("product", ""+pbean);
	      }
	}
}
