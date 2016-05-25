package com.okler.customviews;

import com.okleruser.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class CustomViewLabsForSelectedPckg extends RelativeLayout {

	public CustomViewLabsForSelectedPckg(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initializeViews(context);
	}
	public CustomViewLabsForSelectedPckg(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initializeViews(context);
	}
	
	public CustomViewLabsForSelectedPckg(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	
	private void initializeViews(Context context) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View CustomViewSelectedLabs = inflater.inflate(R.layout.custom_view_selected_pck, this);
	
	}
}
