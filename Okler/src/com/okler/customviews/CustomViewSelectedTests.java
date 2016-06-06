package com.okler.customviews;

import com.okleruser.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class CustomViewSelectedTests extends RelativeLayout{
	public CustomViewSelectedTests(Context context) {
		super(context);
		initializeViews(context);
	}

	public CustomViewSelectedTests(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }
	
	public CustomViewSelectedTests(Context context, AttributeSet attrs, int defStyle) {
	      super(context, attrs, defStyle);
	    }
	private void initializeViews(Context context) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View CustomViewSelectedTests = inflater.inflate(R.layout.custom_view_selected_tests, this);
	}
}
