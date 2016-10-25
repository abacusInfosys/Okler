package com.okler.customviews;

import java.text.AttributedCharacterIterator.Attribute;

import com.okleruser.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class CustomViewProdInfo extends RelativeLayout{

	public CustomViewProdInfo(Context context) {
		super(context);
		initializeViews(context);
	}

	public CustomViewProdInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }
	
	public CustomViewProdInfo(Context context, AttributeSet attrs, int defStyle) {
	      super(context, attrs, defStyle);
	    }
	private void initializeViews(Context context) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View CustomViewProdInfo = inflater.inflate(R.layout.custom_view_prod_info_layout, this);
	}

}
