package com.okler.network;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;

public class WebJsonObjectRequest extends JsonObjectRequest{
	public WebJsonObjectRequest(int method, String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		super(method, url+"&timestamp="+System.currentTimeMillis(), jsonRequest, listener, errorListener);
		/*super(method, url, jsonRequest, listener, errorListener);*/
		// TODO Auto-generated constructor stub
	}


	@Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("Cache-Control", "private");
        return headers;
    }
 
    @Override
    public RetryPolicy getRetryPolicy() {
        // here you can write a custom retry policy
        return new DefaultRetryPolicy(3000, 0, 2);
        
    }
    
    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
    	// TODO Auto-generated method stub
    	return super.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 2));
    }
    
    public WebJsonObjectRequest(int method, String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener,boolean flag) {
		super(method, url, jsonRequest, listener, errorListener);
		// TODO Auto-generated constructor stub
	
    }
}
