package com.okler.customviews;

import com.okleruser.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class CustomViewProdCheckoutSummary extends RelativeLayout{
	public CustomViewProdCheckoutSummary(Context context) {
		super(context);
		initializeViews(context);
	}

	public CustomViewProdCheckoutSummary(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }
	
	public CustomViewProdCheckoutSummary(Context context, AttributeSet attrs, int defStyle) {
	      super(context, attrs, defStyle);
	    }
	private void initializeViews(Context context) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View CustomViewProdCheckoutSummary = inflater.inflate(R.layout.custom_view_prod_checkout_summary, this);
	}
}
