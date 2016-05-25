package com.okler.customviews;

import com.okleruser.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class CustomViewProdMyCart extends LinearLayout{
	public CustomViewProdMyCart(Context context) {
		super(context);
		initializeViews(context);
	}

	public CustomViewProdMyCart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }
	
	public CustomViewProdMyCart(Context context, AttributeSet attrs, int defStyle) {
	      super(context, attrs, defStyle);
	    }
	private void initializeViews(Context context) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View CustomViewProdMyCart = inflater.inflate(R.layout.row_product_checkout_list, this);
	}
}
