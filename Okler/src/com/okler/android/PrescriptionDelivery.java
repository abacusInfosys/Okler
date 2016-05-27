package com.okler.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.okler.databeans.PrescriptionsDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PrescriptionDelivery extends BaseActivity {

	Toolbar toolBar;
	View bottomBarLayout;

	Button proceed;
	String uploadPrescrUrl;
	ImageView imgBack;
	LinearLayout yesAllMedsLL, callMeLL, titleLayout;
	LinearLayout progressLinLayout;
	ImageView yesAllMedsImg, callMeImg;
	PrescriptionsDataBean prescriptionsDataBean;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	String[] imagesBase64, imageTypes;
	boolean isMedPres;
	int required_type = 1;
	Activity act;
	PrescriptionsDataBean presbean = Okler.getInstance()
			.getPrescriptionsDataBeans();
	int cust_id;
	String customer_name, email, salutation, doctor_name, patient_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_prescription_delivery);
		prescriptionsDataBean = Okler.getInstance().getPrescriptionsDataBeans();
		proceed = (Button) findViewById(R.id.btn_proceed);
		isMedPres = getIntent().getBooleanExtra("isMedPres", true);
		progressLinLayout = (LinearLayout) findViewById(R.id.progressLinLayout);
		uploadPrescrUrl = getString(R.string.upload_pres);

		UsersDataBean ubean = Utilities
				.getCurrentUserFromSharedPref(PrescriptionDelivery.this);
		cust_id = ubean.getId();
		customer_name = ubean.getFname();
		email = ubean.getEmail();
		salutation = ubean.getSalutation();
		PrescriptionsDataBean presbean = Okler.getInstance()
				.getPrescriptionsDataBeans();
		doctor_name = presbean.getDocName();
		patient_name = presbean.getPatientName();

		act = this;
		Utilities.writeToLogFIle("Prdel: 1");
		proceed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utilities.writeToLogFIle("Prdel: proceed clicked");
				nameValuePairs = Utilities.getPrescriptionNameValPairFromBean(
						act, prescriptionsDataBean, isMedPres, required_type);
				for (int i = 0; i < nameValuePairs.size(); i++) {
					Utilities.writeToLogFIle("Prdel: nvpairs "
							+ nameValuePairs.get(i).getName() + "  "
							+ nameValuePairs.get(i).getValue());
				}
				String str = "";// gson.toJson(postParams);
				new UploadPrescAsyncTask().execute(str);
				Utilities.writeToLogFIle("Prdel: asyncCalled");
				showProgress(true);

			}
		});
		yesAllMedsImg = (ImageView) findViewById(R.id.yesAllMedsImg);
		callMeImg = (ImageView) findViewById(R.id.callMeImg);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		yesAllMedsLL = (LinearLayout) findViewById(R.id.yesAllMedsLL);
		callMeLL = (LinearLayout) findViewById(R.id.callMeLL);

		toolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		final ActionBar ab = getSupportActionBar();
		// ab.setDisplayHomeAsUpEnabled(true);
		if (Okler.getInstance().getBookingType() == 9) {
			titleLayout = (LinearLayout) findViewById(R.id.title_layout);
			toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
			titleLayout.setBackgroundColor(Color.parseColor("#c054ca"));
			proceed.setBackgroundColor(Color.parseColor("#c054ca"));
			Utilities.writeToLogFIle("Prdel: diagno");

		} else {
			toolBar.setBackgroundResource(R.drawable.custom_view_grad_upload_pesc);
			Utilities.writeToLogFIle("Prdel: med");
		}

		/*imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utilities.writeToLogFIle("Prdel: imgback");
				finish();
			}
		});*/
		UIUtils.setBackClick(toolBar, act);
		Utilities.setTitleText(toolBar, "Upload Prescriptions[3/3]");
	}

	public void yesAllClicked(View view) {

		yesAllMedsImg.setImageResource(R.drawable.blue_radio_btn);
		callMeImg.setImageResource(R.drawable.grey_radio_btn);
		required_type = 1;
		Utilities.writeToLogFIle("Prdel: allmeds");
	}

	public void callMeClicked(View view) {
		yesAllMedsImg.setImageResource(R.drawable.grey_radio_btn);
		callMeImg.setImageResource(R.drawable.blue_radio_btn);
		required_type = 2;
		Utilities.writeToLogFIle("Prdel: callmediscuss");
	}

	public class UploadPrescAsyncTask extends AsyncTask<String, Object, Object> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Utilities.writeToLogFIle("Prdel: pre_execute");
		}

		@Override
		protected Object doInBackground(String... params) {
			try {
				InputStream inputStream = null;
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(uploadPrescrUrl);

				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				for (int i = 0; i < nameValuePairs.size(); i++)
					Utilities.writeToLogFIle("Prdel: nvpairs = "
							+ nameValuePairs.get(i).getName() + " "
							+ nameValuePairs.get(i).getValue());
				httpPost.addHeader("content-type",
						"application/x-www-form-urlencoded");
				HttpResponse httpResponse = httpClient.execute(httpPost);
				inputStream = httpResponse.getEntity().getContent();

				if (inputStream != null) {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(inputStream));
					String line = "";
					String result = "";
					while ((line = bufferedReader.readLine()) != null) {
						result += line;
					}

					Log.e("ERRORISIS", "IS: " + result);
					Utilities.writeToLogFIle("Prdel: UpResult " + result);
					inputStream.close();
					return result;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				Utilities
						.writeToLogFIle("Prdel: UpResult UnsupportedEncodingException "
								+ e.getStackTrace());
				return null;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Utilities
						.writeToLogFIle("Prdel: UpResult ClientProtocolException "
								+ e.getStackTrace());
				return null;

			} catch (IOException e) {
				e.printStackTrace();
				Utilities.writeToLogFIle("Prdel: UpResult IOException "
						+ e.getStackTrace());
				return null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String resu = String.valueOf(result);

			showProgress(false);
			if (resu.contains("result\":\"true")) {
				Toast.makeText(getApplicationContext(),
						"Prescription is uploaded", Toast.LENGTH_LONG).show();

				String s1[] = resu.split(",");
				String s2 = s1[3];
				String s3[] = s2.split(":");
				String ss = s3[1].substring(0, s3[1].length() - 1);
				String presc_id = ss;
				Utilities.writeToLogFIle("Prdel: upload Success " + presc_id);

				try {
					customer_name = URLEncoder.encode(customer_name, "UTF-8");
					doctor_name = URLEncoder.encode(doctor_name, "UTF-8");
					patient_name = URLEncoder.encode(patient_name, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String upload_pres_email = getResources().getString(
						R.string.serverUrl)
						+ getResources().getString(R.string.upload_pres_email)
						+ "salutation="
						+ salutation
						+ "&cust_id="
						+ cust_id
						+ "&customer_name="
						+ customer_name
						+ "&email="
						+ email
						+ "&presc_id="
						+ presc_id
						+ "&doctor_name="
						+ doctor_name + "&patient_name=" + patient_name;

				WebJsonObjectRequest webObjReq = new WebJsonObjectRequest(
						Method.GET, upload_pres_email, new JSONObject(),
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								Utilities
										.writeToLogFIle("In on response of mail callback");
							}

						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								Utilities
										.writeToLogFIle("In on error response of mail callback"
												+ error.getLocalizedMessage());
							}

						}, true);

				VolleyRequest.addJsonObjectRequest(act, webObjReq);

				// call me to discuss.

				if (required_type == 2) {
					try {
						customer_name = URLEncoder.encode(customer_name,
								"UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String call_me_url = getResources().getString(
							R.string.serverUrl)
							+ getResources().getString(
									R.string.call_me_to_discuss_url)
							+ "salutation="
							+ salutation
							+ "&cust_id="
							+ cust_id
							+ "&customer_name="
							+ customer_name
							+ "&email=" + email + "&presc_id=" + presc_id;

					WebJsonObjectRequest callMeReq = new WebJsonObjectRequest(
							Method.GET, call_me_url, new JSONObject(),
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									Utilities
											.writeToLogFIle("In on response of mail callback");
								}

							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									Utilities.writeToLogFIle("In on error response of mail callback"
											+ error.getLocalizedMessage());

								}

							}, true);

					VolleyRequest.addJsonObjectRequest(act, callMeReq);

				}

				Intent in = new Intent(PrescriptionDelivery.this,
						PrescriptionList.class);
				in.putExtra("isMedPres", isMedPres);
				in.putExtra("Check", 11);
				startActivity(in);
			} else {
				Toast.makeText(getApplicationContext(),
						"Some error occured while uploading prescription",
						Toast.LENGTH_LONG).show();
				Utilities.writeToLogFIle("Prdel: upload fail");
			}

		}

	}

	private void showProgress(boolean paramBoolean) {
		if (paramBoolean) {
			this.progressLinLayout.setVisibility(View.VISIBLE);
			return;
		}
		this.progressLinLayout.setVisibility(View.INVISIBLE);
	}

}
