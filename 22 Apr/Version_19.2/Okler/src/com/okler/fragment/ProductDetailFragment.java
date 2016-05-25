package com.okler.fragment;

import com.okleruser.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class ProductDetailFragment extends Fragment {
	View view;
	RelativeLayout relativetempLayout;
	Button cartToHide;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.row_healthshop_grid, container,false);
		relativetempLayout = (RelativeLayout)view.findViewById(R.id.relativetempLayout);
		relativetempLayout.setVisibility(View.VISIBLE);
		cartToHide = (Button)view.findViewById(R.id.image_cart);
		cartToHide.setVisibility(View.GONE);
		return view;
	}
}
