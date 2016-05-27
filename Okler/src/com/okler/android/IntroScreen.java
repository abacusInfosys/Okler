package com.okler.android;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.okleruser.R;

public class IntroScreen extends BaseActivity implements OnClickListener {

	TextView txtSkip;
	Button btnSignUp, btnLogIn;
	ViewFlipper flip;
	float initialx;
	private boolean isStarted = false;
	private static final int DELAY_MSG = 42;
	private static final int DELAY = 4000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro_screen);
		
		txtSkip = (TextView) findViewById(R.id.skip_textView);
		btnSignUp = (Button) findViewById(R.id.Signup_button);
		btnLogIn = (Button) findViewById(R.id.login_button);
		//diff check 1
		flip = (ViewFlipper) findViewById(R.id.flipper);
		flip.setInAnimation(this, R.anim.right_enter);
		flip.setOutAnimation(this, R.anim.left_out);
		//commit check
		//diff check
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
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			initialx = event.getX();
			break;

		case MotionEvent.ACTION_UP:
			float finalx = event.getX();
			if(initialx>finalx){
				if(flip.getDisplayedChild()==6)
					break;
				flip.showNext();
				stopFlipper();
				runFlipper();
				
			}else{
				if(flip.getDisplayedChild()==0)
					break;
				flip.setOutAnimation(this,R.anim.right_out);
				flip.setInAnimation(this,R.anim.left_enter);
				
				flip.showPrevious();
				
				flip.setInAnimation(this, R.anim.right_enter);
				flip.setOutAnimation(this, R.anim.left_out);
				
				stopFlipper();
				runFlipper();
			}
			break;
		default:
			break;
		}
		return false;
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.intro_screen, menu);
		return true;
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		stopFlipper();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		runFlipper();
		//flip.startFlipping();
		
	}
	private void runFlipper() {
		if (isStarted == false) {
			Message msg = mHandler.obtainMessage(42);
			mHandler.sendMessageDelayed(msg, 4000);
			isStarted = true;
		}
	}
	
	private void stopFlipper() {
		mHandler.removeMessages(42);
		isStarted = false;
	}
	
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 42) {
				Log.d("ImageFlipper", "Next picture...");
				flip.showNext();
				msg = obtainMessage(42);
				sendMessageDelayed(msg, 4000);
			}
		}
	};
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
