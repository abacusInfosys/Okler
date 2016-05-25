package com.okler.android;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
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
