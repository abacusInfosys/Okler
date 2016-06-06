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

public class WebStringRequest extends Request<String> {

	private final Listener<String> listener;

	private final Response.ErrorListener mErrorListener;

	public WebStringRequest(String url, int method, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url + "&timestamp=" + System.currentTimeMillis(),
				errorListener);
		// this.clazz = clazz;
		// this.headers = headers;
		this.listener = listener;
		this.mErrorListener = errorListener;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		return Response.success(parsed,
				HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	protected void deliverResponse(String response) {
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

	public WebStringRequest(String url, int method, Listener<String> listener,
			ErrorListener errorListener, boolean flag) {
		super(method, url, errorListener);
		// this.clazz = clazz;
		// this.headers = headers;
		this.listener = listener;
		this.mErrorListener = errorListener;
		// TODO Auto-generated constructor stub
	}
}
