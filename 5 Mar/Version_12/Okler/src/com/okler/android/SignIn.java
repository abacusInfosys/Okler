package com.okler.android;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.utils.Okler;
import com.okleruser.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SignIn extends BaseActivity  implements OnClickListener{
	View bottomBarLayout;
	Toolbar toolBar;
	Button notifCount;
	TextView txtSignup;
	Button btnSignup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		txtSignup = (TextView) findViewById(R.id.signup_text);
		btnSignup = (Button) findViewById(R.id.signup_button);
		
		txtSignup.setOnClickListener(this);
		btnSignup.setOnClickListener(this);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.product_checkout_delivery_address, menu);
		MenuItem item = menu.findItem(R.id.action_settings);
		item.setActionView(R.layout.feed_update_count);
	    //
		 View count = item.getActionView();
		    notifCount = (Button) count.findViewById(R.id.notif_count);
		    notifCount.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				//	Toast.makeText(getApplicationContext(), "btn clicked", Toast.LENGTH_LONG).show();
					Intent i = new Intent(getApplicationContext(), MyCart.class);
					startActivity(i);
				}
			});
		    CartDataBean cbean = Okler.getInstance().getMainCart();
			   ArrayList<ProductDataBean>pdList = cbean.getProdList();
			   // pdList = odbean.getProdList();
			   int maincount = pdList.size(); 
			   
			   if(maincount>9){
			    notifCount.setText(""+maincount);
			    }
			    else{
			    	notifCount.setText("0"+maincount);
			    }
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
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Intent intent;
		
		if(v == txtSignup)
		{
			intent = new Intent(getApplicationContext(), SignUp.class);
			startActivity(intent);
		}
		
		if(v == btnSignup)
		{
			intent = new Intent(getApplicationContext(), SignUp.class);
			startActivity(intent);
		}
		
	}
}
