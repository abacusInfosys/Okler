package com.okler.dialogs;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.okleruser.R;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Utilities;

public class CancelDialog extends Dialog implements android.view.View.OnClickListener {

	  

	public Activity c;
	  public Dialog d;
	 
	  
	  TextView text_no_need, text_too_late, text_bought_myself, text_by_mistake, text_cancel;
	  
	  
	  public CancelDialog(Activity a) {
			super(a);
			// TODO Auto-generated constructor stub
			this.c = a;
		}
	  
	  
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.dialog_reason_for_cancel);
	    text_no_need = (TextView) findViewById(R.id.text_no_need);
	    text_too_late = (TextView) findViewById(R.id.text_too_late);
	    text_bought_myself = (TextView) findViewById(R.id.text_bought_myself);
	    text_by_mistake = (TextView) findViewById(R.id.text_by_mistake);
	    text_cancel = (TextView) findViewById(R.id.text_cancel);
	    text_no_need.setOnClickListener(this);
	    text_too_late.setOnClickListener(this);
	    text_bought_myself.setOnClickListener(this);
	    text_by_mistake.setOnClickListener(this);
	    text_cancel.setOnClickListener(this);

	  }

	  @Override
	  public void onClick(View v) {
		  
		  	String reason;
	    	String cancel_order = "https://www.okler.com/api/order/delete?order_id=8&cust_id=15&notes=my%20test%20cancel";
	    	WebJsonObjectRequest webjson;
	    	
	    switch (v.getId()) 
	    {
	    case R.id.text_no_need:	    
	    	
	    	Toast.makeText(c, "text_no_need", Toast.LENGTH_LONG).show();
	    	reason = text_no_need.getText().toString();
	    	webjson=new WebJsonObjectRequest(Method.GET, cancel_order, new JSONObject(),new Listener<JSONObject>() 
					{
						@Override
						public void onResponse(JSONObject response) 
						{
							// TODO Auto-generated method stub
							
							try
							{
							JSONObject responseObj =(JSONObject)response;
							String message = responseObj.getString("message");
						//	Toast.makeText(c, message, Toast.LENGTH_LONG).show();							
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
				
			VolleyRequest.addJsonObjectRequest(c,webjson);
			
			break;
	    case R.id.text_too_late:
	    	
	    	reason = text_no_need.getText().toString();
	    	
	    	
	    	webjson=new WebJsonObjectRequest(Method.GET, cancel_order, new JSONObject(),new Listener<JSONObject>() 
					{
						@Override
						public void onResponse(JSONObject response) 
						{
							// TODO Auto-generated method stub
							
							try
							{
							JSONObject responseObj =(JSONObject)response;
							String message = responseObj.getString("message");
							Toast.makeText(c, message, Toast.LENGTH_LONG).show();
							
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
				
			VolleyRequest.addJsonObjectRequest(c,webjson);
			break;
			
	    case R.id.text_bought_myself:
	    	
	    	reason = text_no_need.getText().toString();
	    	
	    	
	    	webjson=new WebJsonObjectRequest(Method.GET, cancel_order, new JSONObject(),new Listener<JSONObject>() 
					{
						@Override
						public void onResponse(JSONObject response) 
						{
							// TODO Auto-generated method stub
							
							try
							{
							JSONObject responseObj =(JSONObject)response;
							String message = responseObj.getString("message");
					//		Toast.makeText(c, message, Toast.LENGTH_LONG).show();
							
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
				
			VolleyRequest.addJsonObjectRequest(c,webjson);
			
			break;
			
	    case R.id.text_by_mistake:
	    	
	    	reason = text_no_need.getText().toString();
	    	
	    	
	    	webjson=new WebJsonObjectRequest(Method.GET, cancel_order, new JSONObject(),new Listener<JSONObject>() 
					{
						@Override
						public void onResponse(JSONObject response) 
						{
							// TODO Auto-generated method stub
							
							try
							{
							JSONObject responseObj =(JSONObject)response;
							String message = responseObj.getString("message");
							//Toast.makeText(c, message, Toast.LENGTH_LONG).show();
							
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
				
			VolleyRequest.addJsonObjectRequest(c,webjson);
			
			break;
	    	
	    case R.id.text_cancel:
	    	Toast.makeText(c, "Dialog Dismissed", Toast.LENGTH_LONG).show();
	      dismiss();
	      break;
	    /*case R.id.camera_image:
	    	Utilities.captureImageFromCamera(c);
	      break;
	    	
	    case R.id.gallery_image:
	    	Utilities.showImageFromGallery(c);
	      break;
	    default:
	      break;
	    }
	  }
	    dismiss();*/
	  }
	  }
	}
