package com.okler.android;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.okleruser.R;

public class IntroScreen extends BaseActivity implements OnClickListener {

	TextView txtSkip;
	Button btnSignUp, btnLogIn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro_screen);
		
		txtSkip = (TextView) findViewById(R.id.skip_textView);
        btnSignUp = (Button) findViewById(R.id.Signup_button);
        btnLogIn = (Button) findViewById(R.id.login_button);
        
        txtSkip.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);
        calculateHashKey("com.okleruser");
	}

	private void calculateHashKey(String yourPackageName) {
	    try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                yourPackageName,
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:",
	                    Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            Log.i("KeyHash:",
	                    Base64.encodeToString(md.digest(), Base64.DEFAULT));
	           // Toast.makeText(getApplicationContext(), Base64.encodeToString(md.digest(), Base64.DEFAULT), Toast.LENGTH_LONG).show();
	        }
	    } catch (NameNotFoundException e) {
	        e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intro_screen, menu);
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
		
		if(v == txtSkip)
		{
			intent = new Intent(this, ServiceCategoryActivity.class);
			startActivity(intent);
			finish();
		}
		
		if(v == btnSignUp)
		{
			intent = new Intent(this, SignUp.class);
			startActivity(intent);
			finish();
		}
		
		if(v == btnLogIn)
		{
			intent = new Intent(this, NewSignIn.class);
			startActivity(intent);
			finish();
		}
		
	}
}
