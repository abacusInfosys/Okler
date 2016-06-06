package com.okler.android;

import java.util.ArrayList;

import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class NotificationActivity extends BaseActivity{
	
	View bottomBarLayout;
    Toolbar toolBar;
    ImageView imgBack;
    ImageButton btnNotif;
    LinearLayout active_notif;
    Activity ack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		ack = this;
   		bottomBarLayout = findViewById(R.id.bottombar);
   		handleMapping(bottomBarLayout);
   		toolBar=(Toolbar) findViewById(R.id.toolbar);
   		setSupportActionBar(toolBar);
   		toolBar.setBackgroundColor(getResources().getColor(R.color.Blue));
   		
   		RelativeLayout rl = (RelativeLayout)bottomBarLayout.findViewById(R.id.notifRL);
		btnNotif =(ImageButton) rl.findViewById(R.id.btnNotif);
		LinearLayout active_notif = (LinearLayout) rl.findViewById(R.id.active_notification);
		active_notif.setVisibility(View.VISIBLE);
		btnNotif.setEnabled(false);
		rl.setEnabled(false);
   		
   		/*imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
   	       imgBack.setOnClickListener(new OnClickListener() 
   	       {
   			
   			@Override
   			public void onClick(View v) 
   			{
   				// TODO Auto-generated method stub
   			finish();	
   			}
   		});*/
		UIUtils.setBackClick(toolBar, ack);
   	      Utilities.setTitleText(toolBar, "Notifications");
   		
		
	}

}
