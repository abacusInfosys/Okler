package com.okler.utils;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.okler.android.MedicalServices;
import com.okler.android.MyAccount;
import com.okler.android.MyCart;
import com.okler.android.NewSignIn;
import com.okler.android.NotificationActivity;
import com.okler.android.OtpConfirmationActivity;
import com.okler.android.Physiotherapy;
import com.okler.android.PrescriptionDelivery;
import com.okler.android.ProductCheckoutListView;
import com.okler.android.UploadPrescription;
import com.okleruser.R;
import com.okler.android.ServiceCategoryActivity;
import com.okler.android.SignUp;
import com.okler.android.SignUp.SignUpAsyncTask;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.diagnostics.EnterPatientInfoActivity;
import com.okler.diagnostics.SelectPatientInfoActivity;
import com.okler.dialogs.VerifyPhoneNumDialog;
import com.okler.helpnsupport.SupportHelpActivity;
import com.okler.network.VolleyRequest;
import com.okler.network.WebBooleanRequest;
import com.okler.network.WebJsonObjectRequest;

public class SocialMediaUtils implements ConnectionCallbacks,
		OnConnectionFailedListener {

	public static GoogleApiClient mGoogleApiClient;
	// ********05012015
	private boolean mIntentInProgress, isUserCollector = false, flagCol;
	private String collectorURL; // ********05012015
	static String facebook_id = "", google_id = "", user_FName = "",
			user_Email = "";

	Person currentPerson;
	String personName;
	String personGooglePlusProfile;
	String email;
	int usrId;

	/*
	 * Request code used to invoke sign in user interactions.
	 */
	private static final int RC_SIGN_IN = 100000;
	private static boolean launchPatientsScreen;
	static ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	static String GetUserIdByPhoneUrl, u_name, u_email, userPhone,
			GetUserInfoUrl;// 24_12_2015 Gitesh

	static String logged_from;
	Activity activity;
	Context con;

	public SocialMediaUtils() {

	}

	public SocialMediaUtils(Activity act, boolean launchPatientsScreen) {
		activity = act;
		initGoogleClient(activity);
		this.launchPatientsScreen = launchPatientsScreen;
	}

	public void initGoogleClient(Activity activity) {
		mGoogleApiClient = new GoogleApiClient.Builder(
				activity.getApplicationContext()).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

	}

	public static void initFacebookSDK(Context appContext) {
		FacebookSdk.sdkInitialize(appContext);
	}

	public static void setFacebookCallback(CallbackManager callbackManager,
			final Activity activity, LoginButton fbLoginButton) {

		fbLoginButton
				.setReadPermissions(Arrays.asList("public_profile, email"));

		fbLoginButton.registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {
						// App code
						// Toast.makeText(context,"Success",
						// Toast.LENGTH_LONG).show();
						Log.d("Suc", "Success msg");
						GraphRequest request = GraphRequest.newMeRequest(
								AccessToken.getCurrentAccessToken(),
								new GraphRequest.GraphJSONObjectCallback() {
									@Override
									public void onCompleted(JSONObject object,
											GraphResponse response) {
										// If user with given phone number does
										// not exists then call savenewuser web
										// service.
										// else if user exists then just
										// authenticate and save in shared pref
										// and common instance
										// Application code
										Log.e("FB Response",
												"d" + response.toString());
										JSONObject resp = response
												.getJSONObject();
										try {

											logged_from = "LOGGED_IN_FB";

											// JSONObject graphObj =
											// resp.getJSONObject("graphObject");
											facebook_id = resp.getString("id");
											user_Email = resp
													.getString("email");
											user_FName = resp.getString("name");
											nameValuePairs
													.add(new BasicNameValuePair(
															"name", user_FName));
											nameValuePairs
													.add(new BasicNameValuePair(
															"facebook_id",
															facebook_id));
											nameValuePairs
													.add(new BasicNameValuePair(
															"email", user_Email));

										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										new SocialMediaUtils(activity, false)
												.checkUserExists(user_Email);

									}

								});
						Bundle parameters = new Bundle();
						parameters.putString("fields", "id,name,link,email");
						request.setParameters(parameters);
						request.executeAsync();
					}

					@Override
					public void onCancel() {
						// App code
						Log.e("On Cancel", "Cancel msg");
					}

					@Override
					public void onError(FacebookException exception) {
						// App code
						Log.e("On error", "exception msg" + exception);
					}
				});

	}

	private void redirectUser() {
		switch (Okler.getInstance().getBookingType()) {
		case 0: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			/*
			 * Intent success=new
			 * Intent(activity,ProductCheckoutListView.class);
			 * activity.startActivity(success);
			 */
			activity.finish();
			break;
		}
		case 1: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			/*
			 * Intent success=new
			 * Intent(activity,ProductCheckoutListView.class);
			 * activity.startActivity(success);
			 */
			activity.finish();
			break;

		}
		case 3: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			/*
			 * Intent success=new
			 * Intent(activity,ProductCheckoutListView.class);
			 * activity.startActivity(success);
			 */
			activity.finish();
			break;

		}
		case 4: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			/*
			 * Intent success=new
			 * Intent(activity,ProductCheckoutListView.class);
			 * activity.startActivity(success);
			 */
			activity.finish();
			break;

		}
		case 9: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			UsersDataBean ubean = Utilities
					.getCurrentUserFromSharedPref(activity);
			ArrayList<AddressDataBean> paList = new ArrayList<AddressDataBean>();
			paList = ubean.getPatAddList();
			if (paList.size() <= 0) {
				Intent intent = new Intent(activity,
						EnterPatientInfoActivity.class);
				activity.startActivity(intent);
			} else {
				Intent intent = new Intent(activity,
						SelectPatientInfoActivity.class);
				activity.startActivity(intent);
			}
			break;

		}
		case 11: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(activity, UploadPrescription.class);
			activity.startActivity(success);
			activity.finish();
			break;
		}
		case 12: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(activity, MyAccount.class);
			activity.startActivity(success);
			activity.finish();
			break;
		}
		case 100: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(activity, ServiceCategoryActivity.class);
			Okler.getInstance().setBookingType(100);
			activity.startActivity(success);
			activity.finish();
			break;
		}
		case 13: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(activity, Physiotherapy.class);
			Okler.getInstance().setBookingType(100);
			activity.startActivity(success);
			activity.finish();
			break;
		}
		case 14: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(activity, MedicalServices.class);
			Okler.getInstance().setBookingType(100);
			activity.startActivity(success);
			activity.finish();
			break;
		}
		case 18: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(activity, SupportHelpActivity.class);
			Okler.getInstance().setBookingType(100);
			activity.startActivity(success);
			activity.finish();
			break;
		}

		case 19: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(activity, MyCart.class);
			Okler.getInstance().setBookingType(100);
			activity.startActivity(success);
			activity.finish();
			break;
		}
		case 20: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			UsersDataBean ubean = Utilities
					.getCurrentUserFromSharedPref(activity);
			ArrayList<AddressDataBean> paList = new ArrayList<AddressDataBean>();
			paList = ubean.getPatAddList();
			if (paList.size() <= 0) {
				Intent intent = new Intent(activity,
						EnterPatientInfoActivity.class);
				activity.startActivity(intent);
			} else {
				Intent intent = new Intent(activity,
						SelectPatientInfoActivity.class);
				activity.startActivity(intent);
			}
			break;
		}
		case 21: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(activity, NotificationActivity.class);
			Okler.getInstance().setBookingType(100);
			activity.startActivity(success);
			activity.finish();
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
			activity.finish();
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
			Intent startUpPresc = new Intent(activity,UploadPrescription.class);
			startUpPresc.putExtra("isMedPres", false);
			Okler.getInstance().setBookingType(UIUtils.getBookingType("Diagnostic"));
			activity.startActivity(startUpPresc);
			activity.finish();
			break;
		}

		// }
		}
	}

	private boolean MyStartActivity(Intent aIntent) {
		try {
			activity.startActivity(aIntent);
			return true;
		} catch (ActivityNotFoundException e) {
			return false;
		}
	}

	private void checkUserExists(String email) {
		String isUserExist = activity.getString(R.string.is_user_exists_url)
				+ activity.getString(R.string.mail) + email;

		WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET,
				isUserExist, new JSONObject(), new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub

						try {
							JSONObject responseObj = (JSONObject) response;
							/*
							 * JSONArray result =
							 * responseObj.getString("message"); JSONObject obj1
							 * = result.getJSONObject(0); id =
							 * obj1.optString("id"); String result =
							 * responseObj.getString("result");
							 */String message = responseObj
									.getString("message");

							if (message.equals("User Available")) {
								SocialLoginEnum sLogin = Utilities
										.getActivityName(activity);
								if (sLogin == SocialLoginEnum.SIGNIN) {
									// If user exists- Write its info to shared
									// pref
									Utilities.writeUserTosharedPrefFromJSON(
											activity,
											responseObj.getJSONArray("result"));
									if (logged_from == "LOGGED_IN_FB") {
										Utilities.writeUserStatusToSharedPref(
												activity,
												UserStatusEnum.LOGGED_IN_FB);
									} else if (logged_from == "LOGGED_IN_GOOGLE") {
										Utilities
												.writeUserStatusToSharedPref(
														activity,
														UserStatusEnum.LOGGED_IN_GOOGLE);
									}
									redirectUser();
								} else if (sLogin == SocialLoginEnum.SIGNUP) {
									Toast.makeText(
											activity,
											"User already exists. Please login to continue",
											Toast.LENGTH_LONG).show();
									SocialMediaUtils.signOutOfFB(activity);
								}
							} else {
								// Toast.makeText(activity, "Invalid User",
								// Toast.LENGTH_LONG).show();
								new SocialMediaUtils(activity, false).new SignUpSocialLoginAsyncTask()
										.execute("");
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

					}
				});

		VolleyRequest.addJsonObjectRequest(activity, webjson);
	}

	@Override
	public void onConnected(Bundle arg0) {

		// If user with given phone number does not exists then call savenewuser
		// web service. else if user exists then just authenticate and save in
		// shared pref and common instance
		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {

			currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
			personName = currentPerson.getDisplayName();

			// String personPhoto = currentPerson.getImage();
			personGooglePlusProfile = currentPerson.getUrl();

			email = Plus.AccountApi.getAccountName(mGoogleApiClient);
			Log.d("User Info", personName + "email" + email);
			google_id = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient)
					.getId();
			// Write to shared pref that user is logged in using google account
			// UserStatusEnum UserStatus = UserStatusEnum.LOGGED_IN;
			// Utilities.writeUserStatusEnumToSharedPref(activity, UserStatus);

			// 24_12_2015 Gitesh start

			logged_from = "LOGGED_IN_GOOGLE";
			u_name = personName;
			u_email = email;
			nameValuePairs.add(new BasicNameValuePair("name", u_name));
			nameValuePairs.add(new BasicNameValuePair("google_id", google_id));
			nameValuePairs.add(new BasicNameValuePair("email", u_email));

			new SocialMediaUtils(activity, false).checkUserExists(u_email);

			activity.finish();
		}

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Google connection Failed
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(activity.getApplicationContext());
		if (status != ConnectionResult.SUCCESS) {
			GooglePlayServicesUtil.getErrorDialog(status, activity, 10).show();
		}
		if (!mIntentInProgress && result.hasResolution()) {
			try {
				mIntentInProgress = true;
				activity.startIntentSenderForResult(result.getResolution()
						.getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
			} catch (SendIntentException e) {
				// The intent was canceled before it was sent. Return to the
				// default
				// state and attempt to connect to get an updated
				// ConnectionResult.
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		} else {
			// Toast.makeText(activity,
			// "Please check your network connection or try again",Toast.LENGTH_LONG).show();
			// mIntentInProgress = false;
		}
	}

	public static void signOutOfGoogle() {
		if (mGoogleApiClient != null) {
			if (mGoogleApiClient.isConnected()) {
				Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
				mGoogleApiClient.disconnect();
				Log.d("Google Sign Out", "Signing out from google");
			}
		}
	}

	public static void signOutOfFB(Activity activity) {
		LoginManager.getInstance().logOut();
		Utilities.writeUserStatusEnumToSharedPref(activity,
				UserStatusEnum.LOGGED_OUT);
	}

	public class SignUpSocialLoginAsyncTask extends
			AsyncTask<String, Object, Object> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = activity.getString(R.string.registerUser);
			String Result = String.valueOf(Utilities.RegisterNewUser(url,
					nameValuePairs));
			return Result;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				JSONObject json = new JSONObject(result.toString());
				String messageReceived = json.getString("message");
				if (messageReceived.equals("User registration successful.")) {
					// Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();
					int uid = json.getInt("user_id");
					String userimage = json.optString("profile_url");
					UsersDataBean userBean = Okler.getInstance().getuDataBean();
					userBean.setId(uid);
					userBean.setUser_image(userimage);

					new ApprovedStatusAsyncTask(uid).execute("");
					// redirectUser();
					/*
					 * Intent success=new
					 * Intent(SignUp.this,ServiceCategoryActivity.class);
					 * startActivity(success);
					 */
				} else {
					Toast.makeText(activity, messageReceived, Toast.LENGTH_LONG)
							.show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("IN Post execute" + result);
		}
	}

	public class ApprovedStatusAsyncTask extends
			AsyncTask<String, Object, Object> {

		int usrId;
		ArrayList<NameValuePair> apprStatus = new ArrayList<NameValuePair>();

		public ApprovedStatusAsyncTask(int userId) {
			// TODO Auto-generated constructor stub
			usrId = userId;
			apprStatus.add(new BasicNameValuePair("user_id", String
					.valueOf(usrId)));
			apprStatus.add(new BasicNameValuePair("approved_status", String
					.valueOf(1)));
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			System.out.println("in preexecute");
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = activity.getString(R.string.registerUser);
			String Result = String.valueOf(Utilities.RegisterNewUser(url,
					apprStatus));
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
					UsersDataBean userBean = Okler.getInstance().getuDataBean();
					if (logged_from == "LOGGED_IN_FB") {
						Utilities.writeUserStatusToSharedPref(activity,
								UserStatusEnum.LOGGED_IN_FB);
					} else if (logged_from == "LOGGED_IN_GOOGLE") {
						Utilities.writeUserStatusToSharedPref(activity,
								UserStatusEnum.LOGGED_IN_GOOGLE);
					}
					Utilities.writeCurrentUserToSharedPref(activity, userBean);
					Toast.makeText(activity, "Logged in successfully",
							Toast.LENGTH_LONG).show();
					// Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();
					redirectUser();

					WebCallForSocialLogin();

					/*
					 * userBean=Okler.getInstance().getuDataBean();
					 * Utilities.writeUserStatusEnumToSharedPref(act,
					 * UserStatusEnum.LOGGED_IN);
					 * Utilities.writeCurrentUserToSharedPref
					 * (OtpConfirmationActivity.this, userBean); Intent
					 * success=new
					 * Intent(OtpConfirmationActivity.this,ServiceCategoryActivity
					 * .class); startActivity(success);
					 */
				} else {
					Toast.makeText(activity, messageReceived, Toast.LENGTH_LONG)
							.show();
					/*
					 * Intent intent = new
					 * Intent(OtpConfirmationActivity.this,SignUp.class);
					 * startActivity(intent);
					 */
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void WebCallForSocialLogin() {
		// TODO Auto-generated method stub

		// http://183.82.110.105:8081/oklerapi//emailtemplateservice/user_registration_callback?salutation=&cust_id=&customer_name=&email=

		String socal_call_back = activity.getResources().getString(
				R.string.serverUrl)
				+ activity.getResources().getString(
						R.string.social_login_callback)
				+ "salutation="
				+ "&cust_id="
				+ usrId
				+ "&customer_name="
				+ personName
				+ "&email=" + email;

		WebJsonObjectRequest webObjReq = new WebJsonObjectRequest(Method.GET,
				socal_call_back, new JSONObject(),
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

						Log.i("contact us", "***** fail to send mail****");

					}

				});

		VolleyRequest.addJsonObjectRequest(activity, webObjReq);

	}

}