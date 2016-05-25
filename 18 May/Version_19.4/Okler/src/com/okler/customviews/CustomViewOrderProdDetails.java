package com.okler.customviews;

import com.okleruser.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class CustomViewOrderProdDetails extends LinearLayout{
	
	public CustomViewOrderProdDetails(Context context) {
		super(context);
		initializeViews(context);
	}

	public CustomViewOrderProdDetails(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }
	
	public CustomViewOrderProdDetails(Context context, AttributeSet attrs, int defStyle) {
	      super(context, attrs, defStyle);
	    }
	private void initializeViews(Context context) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View CustomViewOrderProdDetails = inflater.inflate(R.layout.include_order_prod_details, this);
	}
	

}
