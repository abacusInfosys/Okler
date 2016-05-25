package com.okler.android;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.okler.Profile.MyProfileActivity;
import com.okler.Profile.ProfileChangePassword;
import com.okler.databeans.UsersDataBean;
import com.okler.dialogs.SignOut;
import com.okler.network.VolleyRequest;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyAccount extends BaseActivity {
	
	
	Button btnSave, btnCancel,signOut;
	Toolbar toolBar;
	View bottomBarLayout;
	ImageView imgBack,imgExpand, imgCalculator, imgReminder, coll_service, coll_reminder, col_calc;
	Button btn_change_pass;
	TextView txtMedicine, txtDignostic_order, txt_medical, txt_physiotherapy, txt_favourites,txt_Uploaded_prescriptions;
	RelativeLayout myOrderRl;
	NetworkImageView profile_image;
	
	
	LinearLayout text_myprofile;
	
	LinearLayout layout;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		/*DrawingView dv = new DrawingView(this);
        setContentView(dv);*/
		
		setContentView(R.layout.activity_my_account);
		
		profile_image = (NetworkImageView) findViewById(R.id.profile_photo);
		
		UsersDataBean currentUser = Utilities.getCurrentUserFromSharedPref(this);
		
		ImageLoader imgLoader = VolleyRequest.getInstance(this).getImageLoader();
		
		String First= currentUser.getUserAvatarUrl();
				//"http://183.82.110.105:8081/oklerdevv2/uploads/user_avatar/";
		String Second = currentUser.getUser_image();
		String photo_url=First+Second;
		
		profile_image.setImageUrl(photo_url, imgLoader);
		
		imgExpand = (ImageView) findViewById(R.id.expand_services);
		imgCalculator = (ImageView) findViewById(R.id.expand_calculator);
		imgReminder = (ImageView) findViewById(R.id.expand_reminder);
		
		coll_service = (ImageView) findViewById(R.id.Collapse_services);
		coll_reminder = (ImageView) findViewById(R.id.Collapse_reminder);
		col_calc = (ImageView) findViewById(R.id.Collapse_calculator);
		
		txtMedicine = (TextView) findViewById(R.id.Medicine_order);
		txtDignostic_order = (TextView) findViewById(R.id.Dignostic_order);
		txt_medical = (TextView) findViewById(R.id.medical_services);
		txt_physiotherapy = (TextView) findViewById(R.id.Physiotherapy_service);
		txt_Uploaded_prescriptions = (TextView)findViewById(R.id.MyUploaded_prescriptions);
		txt_favourites = (TextView) findViewById(R.id.text_favourites);
		myOrderRl = (RelativeLayout) findViewById(R.id.myOrderRl);
		
		layout = (LinearLayout) findViewById(R.id.layout_services);
		myOrderRl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int visi = layout.getVisibility();
				
				if(visi == 0)
				{
					layout.setVisibility(View.GONE);
					imgExpand.setVisibility(View.VISIBLE);
					coll_service.setVisibility(View.GONE);
				}
				else
				{
					layout.setVisibility(View.VISIBLE);
					imgExpand.setVisibility(View.GONE);
					coll_service.setVisibility(View.VISIBLE);
				}
				
				
			}
		});
		txt_favourites.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent in = new Intent(MyAccount.this, FavouritesActivity.class);
				startActivity(in);
				
			}
		});
		
		text_myprofile = (LinearLayout) findViewById(R.id.linearMyProfile);
		text_myprofile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(getApplicationContext(), MyProfileActivity.class);
				startActivity(i);
				
			}
		});
		
		
		txtMedicine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent in = new Intent(MyAccount.this, MyOrderHome.class);
				in.putExtra("value", 1);
				startActivity(in);
				
			}
		});
		
		txtDignostic_order.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent in = new Intent(MyAccount.this, MyOrderHome.class);
				in.putExtra("value", 2);
				startActivity(in);
				
			}
		});
		
	txt_Uploaded_prescriptions.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent in = new Intent(MyAccount.this, PrescriptionList.class);		
				in.putExtra("isMedPres", true);
				in.putExtra("isParentMyAccount", true);
				startActivity(in);				
			}
		});
	
		txt_medical.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent in = new Intent(MyAccount.this, MyOrderHome.class);
				in.putExtra("value", 3);
				startActivity(in);
				
			}
		});
		
		txt_physiotherapy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent in = new Intent(MyAccount.this, MyOrderHome.class);
				in.putExtra("value", 4);
				startActivity(in);
				
			}
		});
		
		
		
		btn_change_pass = (Button) findViewById(R.id.button_change_password);
		btn_change_pass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent in = new Intent(MyAccount.this, ProfileChangePassword.class);
				startActivity(in);
				
				
			}
		});
		
		coll_service.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				imgExpand.setVisibility(View.VISIBLE);
				coll_service.setVisibility(View.GONE);
				
				LinearLayout layout = (LinearLayout) findViewById(R.id.layout_services);
				layout.setVisibility(View.GONE);
				
			}
		});
			
		imgExpand.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				LinearLayout layout = (LinearLayout) findViewById(R.id.layout_services);
				layout.setVisibility(View.VISIBLE);
				
				imgExpand.setVisibility(View.GONE);
				coll_service.setVisibility(View.VISIBLE);
			}
		});
		
		col_calc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				imgCalculator.setVisibility(View.VISIBLE);
				col_calc.setVisibility(View.GONE);
				
				LinearLayout layout = (LinearLayout) findViewById(R.id.layout_calculator);
				layout.setVisibility(View.GONE);
			}
		});
		
		imgCalculator.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				LinearLayout layout = (LinearLayout) findViewById(R.id.layout_calculator);
				layout.setVisibility(View.VISIBLE);
				
				
				
				imgCalculator.setVisibility(View.GONE);
				col_calc.setVisibility(View.VISIBLE);
			}
		});
		
		
		coll_reminder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				imgReminder.setVisibility(View.VISIBLE);
				coll_reminder.setVisibility(View.GONE);
				
				LinearLayout layout = (LinearLayout) findViewById(R.id.layout_reminder);
				layout.setVisibility(View.GONE);
				
			}
		});
		
		
		imgReminder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				LinearLayout layout = (LinearLayout) findViewById(R.id.layout_reminder);
				layout.setVisibility(View.VISIBLE);
				
				imgReminder.setVisibility(View.GONE);
				coll_reminder.setVisibility(View.VISIBLE);
			}
		});
		
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		
		RelativeLayout rl = (RelativeLayout)bottomBarLayout.findViewById(R.id.manageProfRL);
		ImageButton btnprofile =(ImageButton) rl.findViewById(R.id.btnManageProfile);
		LinearLayout active_profile = (LinearLayout) rl.findViewById(R.id.active_profile);
		active_profile.setVisibility(View.VISIBLE);
		btnprofile.setEnabled(false);
		rl.setEnabled(false);
		
		
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		final ActionBar ab=getSupportActionBar();
		//ab.setDisplayHomeAsUpEnabled(true);
		
		toolBar.setBackgroundColor(Color.BLUE);
		
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	      Utilities.setTitleText(toolBar, "My Account");
	      signOut = (Button)findViewById(R.id.button_sign_out);
	      signOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			SignOut signout = new SignOut();
			signout.show(getSupportFragmentManager(),"Sign Out");
				
			}
		});
	}

}
