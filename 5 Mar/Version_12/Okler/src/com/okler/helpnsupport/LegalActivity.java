package com.okler.helpnsupport;

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
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Utilities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

public class LegalActivity extends BaseActivity {
	View bottomBarLayout;
	Toolbar toolBar;
	ImageView imgBack;
	String dynamicURL,legalURL;
	WebView legal_webView ;
	int in;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_legal);
		toolBar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		
		legal_webView = (WebView) findViewById(R.id.legal_webView);
		
		/* Get Dynamic URLS*/
		dynamicURL = getString(R.string.get_faq_term_dynamic_url);
		GetDynamicURLS();
		
		in = getIntent().getIntExtra("t&c", -1);
		if(in == 1)
		{			
			Utilities.setTitleText(toolBar, "Terms & Conditions");
		}
		else
		{
			Utilities.setTitleText(toolBar, "Legal");
		}
		
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
		
		toolBar.setBackgroundColor(getResources().getColor(R.color.Blue));
	}
	
	private void GetDynamicURLS()
	{
		  WebJsonObjectRequest webjson=new WebJsonObjectRequest(Method.GET, dynamicURL, new JSONObject(),new Listener<JSONObject>() 
					{
						@Override
						public void onResponse(JSONObject response) 
						{
							
								try {
									JSONObject results = response.getJSONObject("result");
									if(in == 1)
									{
										legalURL = results.getString("TermsConditions");
										legal_webView.loadUrl(legalURL);
									}
									else
									{
										legalURL = results.getString("Legal");
										legal_webView.loadUrl(legalURL);
										
									}										
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
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
		
	VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.legal, menu);
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
}
