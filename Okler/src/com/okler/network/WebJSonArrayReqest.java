package com.okler.network;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;

public class WebJSonArrayReqest  extends JsonArrayRequest{
	public WebJSonArrayReqest(int method, String url, JSONArray jsonRequest,
			Listener<JSONArray> listener, ErrorListener errorListener) {
		super(method, url+"&timestamp="+System.currentTimeMillis(), jsonRequest, listener, errorListener);
		// TODO Auto-generated constructor stub
	}
	 @Override
	    public Map<String, String> getHeaders() throws AuthFailureError {
	        HashMap<String, String> headers = new HashMap<String, String>();
	        headers.put("Content-Type", "application/json; charset=utf-8");
	        return headers;
	    }
	 
	    @Override
	    public RetryPolicy getRetryPolicy() {
	        // here you can write a custom retry policy
	    	return new DefaultRetryPolicy(3000, 2, 2);
	    }
	    @Override
	    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
	    	// TODO Auto-generated method stub
	    	return super.setRetryPolicy(new DefaultRetryPolicy(3000, 2, 2));
	    }
	    public WebJSonArrayReqest(int method, String url, JSONArray jsonRequest,
				Listener<JSONArray> listener, ErrorListener errorListener,boolean flag) {
			super(method, url, jsonRequest, listener, errorListener);
			// TODO Auto-generated constructor stub
		}
}
