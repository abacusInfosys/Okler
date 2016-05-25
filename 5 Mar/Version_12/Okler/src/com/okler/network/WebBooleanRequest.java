package com.okler.network;

import java.io.UnsupportedEncodingException;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class WebBooleanRequest extends Request<Boolean> {

	 private final Listener<Boolean> listener;
	    
	    private final Response.ErrorListener mErrorListener;
	  public WebBooleanRequest(String url, int method,
	            Listener<Boolean> listener, ErrorListener errorListener) {
			super(method, url+"&timestamp="+System.currentTimeMillis(), errorListener);
			// this.clazz = clazz;
		  //      this.headers = headers;
		        this.listener = listener;
		        this.mErrorListener = errorListener;
			// TODO Auto-generated constructor stub
		}
	@Override
	protected Response<Boolean> parseNetworkResponse(NetworkResponse response) {
		 Boolean parsed;
	        try {
	            parsed = Boolean.valueOf(new String(response.data, HttpHeaderParser.parseCharset(response.headers)));
	        } catch (UnsupportedEncodingException e) {
	            parsed = Boolean.valueOf(new String(response.data));
	        }
	        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
	}
	@Override
	protected void deliverResponse(Boolean response) {
		 listener.onResponse(response);
		
	}
	
	 @Override
	    protected VolleyError parseNetworkError(VolleyError volleyError) {
	        return super.parseNetworkError(volleyError);
	    }
	 
	 @Override
	    public void deliverError(VolleyError error) {
	        mErrorListener.onErrorResponse(error);
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
}
