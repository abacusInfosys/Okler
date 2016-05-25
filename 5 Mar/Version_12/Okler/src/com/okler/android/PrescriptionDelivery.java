package com.okler.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.google.gson.Gson;
import com.okler.databeans.PrescriptionsDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.telephony.gsm.GsmCellLocation;
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
	LinearLayout yesAllMedsLL,callMeLL,titleLayout;
	LinearLayout progressLinLayout;
	ImageView yesAllMedsImg,callMeImg;
	PrescriptionsDataBean prescriptionsDataBean;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	String[] imagesBase64,imageTypes; 
	boolean isMedPres;
	int required_type=1;
	Activity act;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_prescription_delivery);
		prescriptionsDataBean = Okler.getInstance().getPrescriptionsDataBeans();
		proceed = (Button) findViewById(R.id.btn_proceed);
		isMedPres = getIntent().getBooleanExtra("isMedPres", true);
		progressLinLayout = (LinearLayout)findViewById(R.id.progressLinLayout);
		uploadPrescrUrl = getString(R.string.upload_pres);
		act = this;
	
		proceed.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
					
				nameValuePairs = Utilities.getPrescriptionNameValPairFromBean(act, prescriptionsDataBean, isMedPres, required_type);
				String str ="";// gson.toJson(postParams);
				new UploadPrescAsyncTask().execute(str);
				showProgress(true);
		/*		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						postParams.size());

				Enumeration<String> keys = postParams.keys();
				while (keys.hasMoreElements()) {
					String key = keys.nextElement();

					nameValuePairs.add(new BasicNameValuePair(key,
							postParams.get(key)));
				}
				try {
					new UrlEncodedFormEntity(nameValuePairs);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				//String predDB = gson.toJson(prescriptionsDataBean);
		
			
		}}	);
		yesAllMedsImg = (ImageView)findViewById(R.id.yesAllMedsImg);
		 callMeImg  = (ImageView)findViewById(R.id.callMeImg);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		yesAllMedsLL = (LinearLayout)findViewById(R.id.yesAllMedsLL);
		callMeLL = (LinearLayout)findViewById(R.id.callMeLL);
		
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		final ActionBar ab=getSupportActionBar();
		//ab.setDisplayHomeAsUpEnabled(true);
		if(Okler.getInstance().getBookingType() == 9){
			titleLayout=(LinearLayout) findViewById(R.id.title_layout);
			toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
			titleLayout.setBackgroundColor(Color.parseColor("#c054ca"));
			proceed.setBackgroundColor(Color.parseColor("#c054ca"));
		
		}else{
			toolBar.setBackgroundResource(R.drawable.custom_view_grad_upload_pesc);
		}
		
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	      Utilities.setTitleText(toolBar, "Upload Prescriptions[3/3]");      
	}
	
	public void yesAllClicked(View view)
	{
		
		yesAllMedsImg.setImageResource(R.drawable.blue_radio_btn);
		callMeImg.setImageResource(R.drawable.grey_radio_btn);
		required_type=1;
	}
	
	public void callMeClicked(View view)
	{
		yesAllMedsImg.setImageResource(R.drawable.grey_radio_btn);
		callMeImg.setImageResource(R.drawable.blue_radio_btn);
		required_type=2;
	}
	
	public class UploadPrescAsyncTask extends AsyncTask<String, Object, Object>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected Object doInBackground(String... params) {
			try
			{
				InputStream inputStream= null;
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(uploadPrescrUrl);
			//	StringEntity stringEntity = new StringEntity(params[0]);
			//	httpPost.setEntity(stringEntity);
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//	httpPost.setHeader("Accept","application/Json");
				httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
				HttpResponse httpResponse = httpClient.execute(httpPost);
				inputStream=httpResponse.getEntity().getContent();
				
				if(inputStream!=null){
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					String line="";
					String result="";
					while ((line = bufferedReader.readLine()) != null) {
						result+=line;
						}
					
					Log.e("ERRORISIS","IS: "+result);
					inputStream.close();
					return result;
				}
			}catch (UnsupportedEncodingException e) {				
				e.printStackTrace();
				return null;
			} catch (ClientProtocolException e) {				
				e.printStackTrace();
				return null;
				
			} catch (IOException e) {				
				e.printStackTrace();
				return null;
			} 
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String resu = String.valueOf(result);
			//String resu = result.toString();
			showProgress(false);
			if(resu.contains("result\":\"true"))
			{
				Toast.makeText(getApplicationContext(), "Prescription is uploaded", Toast.LENGTH_LONG).show();	
				
				String s1[] = resu.split(",");
				String s2 = s1[3];
				String s3[] = s2.split(":");
				String ss = s3[1].substring(0, s3[1].length()-1);
				String presc_id= ss;
				UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(PrescriptionDelivery.this);
				int cust_id = ubean.getId();
				String customer_name = ubean.getFname();
				String email = ubean.getEmail();
				PrescriptionsDataBean presbean = Okler.getInstance().getPrescriptionsDataBeans();
				String doctor_name = presbean.getDocName();
				String patient_name = presbean.getPatientName();
				
				String upload_pres_email = getResources().getString(R.string.serverUrl)+getResources().getString(R.string.upload_pres_email)+"salutation="+"cust_id="+cust_id+"&customer_name="+customer_name+"&email="+email+"&presc_id="+presc_id+"&doctor_name="+doctor_name+"&patient_name="+patient_name;

				WebJsonObjectRequest webObjReq=new WebJsonObjectRequest(Method.GET, upload_pres_email, new JSONObject(),new Response.Listener<JSONObject>() 
						{

							@Override
							public void onResponse(JSONObject response) 
							{
								
							}

							
						},new Response.ErrorListener() 
						{

							@Override
							public void onErrorResponse(VolleyError error) {
								
						}
							
						});
						
						VolleyRequest.addJsonObjectRequest(act,webObjReq);
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Some error occured while uploading prescription", Toast.LENGTH_LONG).show();
			}
			Intent in = new Intent(PrescriptionDelivery.this, PrescriptionList.class);
			in.putExtra("isMedPres", isMedPres);
				in.putExtra("Check", 11);
			startActivity(in);
		}
		
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

}
