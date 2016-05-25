package com.okler.android;

import com.okler.helpnsupport.SupportHelpActivity;
import com.okler.network.NetworkUtils;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity implements OnClickListener {

	protected ImageButton btnHome,btnNotif,btnUploadPres,btnManageProfi,btnQues;
	protected RelativeLayout homeRL,notifRL,uploadPresRL,manageProfRL,questionsRL;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_base);
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	protected void handleMapping(View rootView)
	{
		btnHome = (ImageButton) rootView.findViewById(R.id.btnHome);
		btnNotif = (ImageButton) rootView.findViewById(R.id.btnNotif);
		btnUploadPres = (ImageButton)rootView.findViewById(R.id.btnUploadPres);
		btnManageProfi = (ImageButton)rootView.findViewById(R.id.btnManageProfile);
		btnQues = (ImageButton)rootView.findViewById(R.id.btnQuestions);
		
		homeRL = (RelativeLayout)rootView.findViewById(R.id.homeRL);
		notifRL = (RelativeLayout)rootView.findViewById(R.id.notifRL);
		uploadPresRL = (RelativeLayout)rootView.findViewById(R.id.uploadPresRL);
		manageProfRL = (RelativeLayout)rootView.findViewById(R.id.manageProfRL);
		questionsRL = (RelativeLayout)rootView.findViewById(R.id.questionsRL);
		
		homeRL.setOnClickListener(this);
		notifRL.setOnClickListener(this);
		uploadPresRL.setOnClickListener(this);
		manageProfRL.setOnClickListener(this);
		questionsRL.setOnClickListener(this);
		
		
		btnHome.setOnClickListener(this);
		btnNotif.setOnClickListener(this);
		btnUploadPres.setOnClickListener(this);
		btnManageProfi.setOnClickListener(this);
		btnQues.setOnClickListener(this);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base, menu);
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
		//Toast.makeText(getApplicationContext(), "button clicked", Toast.LENGTH_LONG).show();
		int id = v.getId();
		Intent intent;
		UserStatusEnum curStatus = Utilities.getUserStatusFromSharedPref(this);
		switch(id)
		{
		case R.id.btnHome:
			case	R.id.homeRL: intent = new Intent(this,ServiceCategoryActivity.class);
									startActivity(intent);
									break;
			case R.id.btnUploadPres:						
			case R.id.uploadPresRL:
				if ((Utilities.getUserStatusFromSharedPref(BaseActivity.this) == UserStatusEnum.LOGGED_IN) ||
					(Utilities.getUserStatusFromSharedPref(BaseActivity.this) == UserStatusEnum.LOGGED_IN_FB) ||
					(Utilities.getUserStatusFromSharedPref(BaseActivity.this) == UserStatusEnum.LOGGED_IN_GOOGLE)){
				intent = new Intent(this,UploadPrescription.class);
				intent.putExtra("isMedPres", true);
				int abc = UIUtils.getBookingType("UploadPresc");
				Okler.getInstance().setBookingType(abc);
				startActivity(intent);
				break;
				}else{
					Intent in = new Intent(BaseActivity.this, NewSignIn.class);	
					int ab = UIUtils.getBookingType("UploadPresc");
					Okler.getInstance().setBookingType(ab);
					startActivity(in);
					finish();
					break;
				}					
									
				// startUploadPrescOrSignIn(curStatus);
	case R.id.btnQuestions:								
	case R.id.questionsRL:
				if (Utilities.getUserStatusFromSharedPref(BaseActivity.this) == UserStatusEnum.LOGGED_IN ||
						(Utilities.getUserStatusFromSharedPref(BaseActivity.this) == UserStatusEnum.LOGGED_IN_FB) ||
						(Utilities.getUserStatusFromSharedPref(BaseActivity.this) == UserStatusEnum.LOGGED_IN_GOOGLE))
	 				{
					intent = new Intent(BaseActivity.this, SupportHelpActivity.class);
					startActivity(intent);
	 				}
				else
				{
					intent = new Intent(BaseActivity.this, NewSignIn.class);
					int ab = UIUtils.getBookingType("helpSupport");
					Okler.getInstance().setBookingType(ab);
					startActivity(intent);
				}
				break;
	case R.id.btnManageProfile:	
			case R.id.manageProfRL:Okler.getInstance().setBookingType(12);
	//		Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
			checkInternet(curStatus);
				
			break;				
	case R.id.btnNotif:
	case R.id.notifRL:
	intent = new Intent(BaseActivity.this, NotificationActivity.class);
			startActivity(intent);
break;
					
			
		}
	}
	
	private void checkInternet(UserStatusEnum curStatus)
	{
		if(!NetworkUtils.checkNetworkAvailable(this))
		{
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setMessage("Check your internet connection");
			dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		         @Override
		         public void onClick(DialogInterface arg0, int arg1) {
		        	 
		        	 //startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
		        	 finish();
		        	 System.exit(0);
		         }
		      });
			
			AlertDialog alertDialog = dialog.create();
		      alertDialog.show();
		}
		else
			startMyAccountOrSignIn(curStatus);
	}
	private void startMyAccountOrSignIn(UserStatusEnum curStatus )
	{
		Intent intent;
		switch(curStatus.ordinal())
		{
		case 0:
		case 5:intent = new Intent(this,NewSignIn.class);startActivity(intent);break;
		case 1:
		case 2:
		case 3:
		case 4:intent = new Intent(this,MyAccount.class);
		startActivity(intent);break;
		}	
	}
	
/*	private void startUploadPrescOrSignIn(UserStatusEnum curStatus )
	{
		Intent intent;
		switch(curStatus.ordinal())
		{
		case 0:
		case 5:intent = new Intent(this,NewSignIn.class);startActivity(intent);break;
		case 1:
		case 2:
		case 3:
		case 4:intent = new Intent(this,UploadPrescription.class);
		startActivity(intent);break;
		}	
	}	*/
}
