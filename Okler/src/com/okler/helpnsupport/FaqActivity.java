package com.okler.helpnsupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.okler.android.BaseActivity;
import com.okleruser.R;
import com.okler.databeans.ProductDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FaqActivity extends BaseActivity {
	View bottomBarLayout;
	Toolbar toolBar;
	ImageView imgBack;
	String dynamicURL, faqURL;
	WebView faq_webView;
	int progress;
	LinearLayout progressLinLayout;
	Activity activity;
	ProgressBar progBar;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faq);
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		activity = this;
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		progressLinLayout = (LinearLayout) findViewById(R.id.progressLinLayout);

		faq_webView = (WebView) findViewById(R.id.faq_webView);

		faq_webView.getSettings().setJavaScriptEnabled(true);
		faq_webView.setWebChromeClient(new MyWEbClient());
		progBar = (ProgressBar) findViewById(R.id.progBar);
		progBar.setMax(100);

		/* Get Dynamic URLS */
		dynamicURL = getString(R.string.get_faq_term_dynamic_url);
		GetDynamicURLS();

		/*imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});*/
		UIUtils.setBackClick(toolBar, activity);
		toolBar.setBackgroundColor(getResources().getColor(R.color.Blue));
		Utilities.setTitleText(toolBar, "FAQ");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.faq, menu);
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

	private void showProgress(boolean paramBoolean) {
		if (paramBoolean) {
			this.progressLinLayout.setVisibility(View.VISIBLE);
			return;
		}
		this.progressLinLayout.setVisibility(View.INVISIBLE);
	}

	private void GetDynamicURLS() {
		WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET,
				dynamicURL, new JSONObject(), new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						try {
							JSONObject results = response
									.getJSONObject("result");
							faqURL = results.getString("Faq");
							faq_webView.loadUrl(faqURL);
							FaqActivity.this.progBar.setProgress(0);
							// progress = faq_webView.getProgress();
							/*
							 * if(progress<100){ showProgress(true); }else{
							 * showProgress(false); }
							 */
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							// showProgress(false);
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						// showProgress(false);
						// Utilities.handleVollyError(error, activity);
						Log.i("error", "" + error.getStackTrace());
						// Log.e("a", "Error");
					}
				}, true);

		VolleyRequest.addJsonObjectRequest(getApplicationContext(), webjson);
	}

	private class MyWEbClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			FaqActivity.this.setValue(newProgress);
			// if(newProgress>50)
			// progBar.setVisibility(View.GONE);
			// Toast.makeText(activity, ""+newProgress, 300).show();
			super.onProgressChanged(view, newProgress);
		}

	}

	public void setValue(int progress) {
		this.progBar.setProgress(progress);
		if (progress > 50)
			this.progBar.setVisibility(View.GONE);
		// Toast.makeText(activity, ""+progress, 300).show();
	}
}
