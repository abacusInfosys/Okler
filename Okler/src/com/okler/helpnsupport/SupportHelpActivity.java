package com.okler.helpnsupport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.okler.android.BaseActivity;
import com.okler.android.ServiceCategoryActivity;
import com.okleruser.R;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.Utilities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Helpers;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SupportHelpActivity extends BaseActivity {
	View bottomBarLayout;
	Toolbar toolBar;
	ImageView imgBack, molbile_img, email_img;
	Spinner reason_spinner;
	Button btn_send_message;
	RelativeLayout mobile_rl, email_rl;
	EditText give_details, capcha_text;
	int like = 1;
	String reason;

	ArrayList<String> reason_list = new ArrayList<String>();
	ArrayList<Integer> reason_id = new ArrayList<Integer>();

	TextView capcha_img;

	int reason_id1 = 0;
	int cust_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_support_help);
		reason_list.add("Select Reason");

		toolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);

		RelativeLayout rl = (RelativeLayout) bottomBarLayout
				.findViewById(R.id.questionsRL);
		ImageButton btnSupport = (ImageButton) rl
				.findViewById(R.id.btnQuestions);
		LinearLayout active_support = (LinearLayout) rl
				.findViewById(R.id.active_support);
		active_support.setVisibility(View.VISIBLE);
		btnSupport.setEnabled(false);
		rl.setEnabled(false);

		reason_spinner = (Spinner) findViewById(R.id.reason_spinner);
		btn_send_message = (Button) findViewById(R.id.send_message);
		molbile_img = (ImageView) findViewById(R.id.mobile_img);
		email_img = (ImageView) findViewById(R.id.email_img);
		mobile_rl = (RelativeLayout) findViewById(R.id.moblie_Rl);
		email_rl = (RelativeLayout) findViewById(R.id.email_RL);

		capcha_text = (EditText) findViewById(R.id.sec_code);

		give_details = (EditText) findViewById(R.id.give_details);

		capcha_img = (TextView) findViewById(R.id.capcha_img);

		char[] CHARSET_AZ_09 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
				.toCharArray();

		randomString(CHARSET_AZ_09, 5);

		mobile_rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				mobileClicked();

			}
		});

		email_rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				emailClicked();

			}
		});

		handleMapping(bottomBarLayout);
		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		toolBar.setBackgroundColor(getResources().getColor(R.color.Blue));
		Utilities.setTitleText(toolBar, "Support/Help");

		String helpnsupport = getString(R.string.serverUrl)+getString(R.string.support_reason);

		WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET,
				helpnsupport, new JSONObject(), new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub

						try {
							JSONObject responseObj = (JSONObject) response;
							JSONArray doctorsArr = responseObj
									.getJSONArray("result");
							// docCount=responseObj.getInt("TotalCount");
							for (int i = 0; i < doctorsArr.length(); i++) {
								try {
									JSONObject docObj = (JSONObject) doctorsArr
											.get(i);
									reason_list.add(docObj.getString("name"));
									reason_id.add(docObj.getInt("id"));
									Log.i("tag", "json object" + docObj);
								} catch (JSONException e) {
									// TODO: handle exception
									Log.e("JSON Exception", e.getMessage());
								}
							}
						} catch (JSONException jsonEx) {
							Log.e("Exception json", jsonEx.getStackTrace()
									.toString());
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				}, true);

		VolleyRequest.addJsonObjectRequest(this, webjson);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, reason_list);
		reason_spinner.setAdapter(adapter);

		btn_send_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(validateData())
				{
					
				}

				String original_capcha = capcha_img.getText().toString();
				String user_enter = capcha_text.getText().toString();

				if (original_capcha.equals(user_enter) == true) {
					UsersDataBean ubean = Utilities
							.getCurrentUserFromSharedPref(SupportHelpActivity.this);

					cust_id = ubean.getId();

					String details = give_details.getText().toString();
					// Temporary hard coding.. remove later on.

					try {
						details = URLEncoder.encode(details, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String send_message = getString(R.string.serverUrl)+getString(R.string.support_send_message)+getString(R.string.customer_id)
							+ cust_id
							+ getString(R.string.like_contact)
							+ like
							+ getString(R.string.reason_id)
							+ reason_id1 + getString(R.string.details) + details;

					WebJsonObjectRequest webjson = new WebJsonObjectRequest(
							Method.GET, send_message, new JSONObject(),
							new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									// TODO Auto-generated method stub

									try {
										JSONObject responseObj = (JSONObject) response;
										String message = responseObj
												.getString("message");
										Toast.makeText(
												getApplicationContext(),
												"Your query reached us. We will contact you shortly!!",
												Toast.LENGTH_LONG).show();
										/*
										 * String id =
										 * responseObj.getString("id");
										 * //Toast.makeText
										 * (getApplicationContext(), id,
										 * Toast.LENGTH_LONG).show(); JSONArray
										 * doctorsArr =
										 * responseObj.getJSONArray("result");
										 */
										// docCount=responseObj.getInt("TotalCount");

										if (message
												.equals("inserted successfully")) {
											UsersDataBean ubean = Utilities
													.getCurrentUserFromSharedPref(SupportHelpActivity.this);

											String name = ubean.getFname();
											String email = ubean.getEmail();
											String mobile = ubean.getPhone();
											String salutation = ubean
													.getSalutation();

											name = URLEncoder.encode(name,
													"UTF-8");

											String help_support = getResources()
													.getString(
															R.string.serverUrl)
													+ getResources()
															.getString(
																	R.string.helpsupport)
													+ "salutation="
													+ salutation
													+ "&cust_id="
													+ cust_id
													+ "&customer_name="
													+ name
													+ "&email="
													+ email
													+ "&reason_id="
													+ reason_id1;

											WebJsonObjectRequest webObjReq = new WebJsonObjectRequest(
													Method.GET,
													help_support,
													new JSONObject(),
													new Response.Listener<JSONObject>() {

														@Override
														public void onResponse(
																JSONObject response) {
															Log.i("contact us",
																	"*****  mail sent*****");
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

														}

													}, true);

											VolleyRequest.addJsonObjectRequest(
													getApplicationContext(),
													webObjReq);
										}

									} catch (JSONException jsonEx) {
										Log.e("Exception json", jsonEx
												.getStackTrace().toString());
									} catch (UnsupportedEncodingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {

									Toast.makeText(getApplicationContext(),
											String.valueOf(error),
											Toast.LENGTH_LONG).show();
									// TODO Auto-generated method stub

								}
							});

					// DefaultRetryPolicy defRetryPol = new
					// DefaultRetryPolicy(100000, 20, 3);
					// webjson.setRetryPolicy(defRetryPol);

					VolleyRequest.addJsonObjectRequest(getApplicationContext(),
							webjson);

					Intent intent = new Intent(SupportHelpActivity.this,
							ServiceCategoryActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							"Captcha not match", Toast.LENGTH_LONG).show();
				}
			}
		});

		reason_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				int id = reason_spinner.getSelectedItemPosition();
				if (id != 0) {
					reason_id1 = reason_id.get(id - 1);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void mobileClicked() {

		molbile_img.setImageResource(R.drawable.red_circle_with_white_icon);
		email_img.setImageResource(R.drawable.grey_radio_btn);
		like = 1;
	}

	public void emailClicked() {
		molbile_img.setImageResource(R.drawable.grey_radio_btn);
		email_img.setImageResource(R.drawable.red_circle_with_white_icon);
		like = 2;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		/* getMenuInflater().inflate(R.menu.support_help, menu); */
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

	public void randomString(char[] characterSet, int length) {
		Random random = new Random();
		char[] result = new char[length];
		for (int i = 0; i < result.length; i++) {
			// picks a random index out of character set > random character
			int randomCharIndex = random.nextInt(characterSet.length);
			result[i] = characterSet[randomCharIndex];
			/*
			 * capcha_img.setText(result[i]); capcha_img.setText("   ");
			 */
		}
		capcha_img.setText(new String(result));
	}
	
	private boolean validateData()
	{


		// Verify Spinner
		if (reason_spinner.getSelectedItemId() == 0) {
			TextView errorText = (TextView) reason_spinner.getSelectedView();
			errorText.setError("Please select reason");
			Toast.makeText(getApplicationContext(), "Please select reason",
					Toast.LENGTH_LONG).show();
			return false;
		}
		reason = reason_spinner.getSelectedItem().toString();
		// Verify Patient Name
		
		if(give_details.getText().toString().trim().length() == 0)
		{
			give_details.setError("Please give details");
			return false;
		}
		return true;
	
	}
}
