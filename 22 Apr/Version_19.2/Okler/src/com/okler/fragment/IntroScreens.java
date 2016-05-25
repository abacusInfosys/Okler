package com.okler.fragment;

import java.util.Timer;
import java.util.TimerTask;

import com.okler.adapters.CustomPagerAdapter;
import com.okleruser.R;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class IntroScreens extends Fragment {
	CustomPagerAdapter mCustomPagerAdapter;
	ViewPager mViewPager;
	Timer swipeTimer;
	int currentPage=0,NUM_PAGES=6;
	Runnable Update;
	Handler handler;
	ListView homeHeadersList;
	
	public IntroScreens()
	{
		
	}
	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		  // Inflate the layout for this fragment
	        View view = inflater.inflate(R.layout.fragment_intro_screens, container);
	        
	        setViewPager(view);
	       return view;
	       }        
	        
	 
	 public void setViewPager(View view)
	 {
	      mCustomPagerAdapter = new CustomPagerAdapter(getActivity());
	        
	        mViewPager = (ViewPager)view.findViewById(R.id.viewPager);
	        mViewPager.setAdapter(mCustomPagerAdapter);
	        
	      //Bind the title indicator to the adapter
	        /*CirclePageIndicator titleIndicator = (CirclePageIndicator)view.findViewById(R.id.indicator);
	        titleIndicator.setViewPager(mViewPager);*/
	        
	        handler = new Handler();

	        Update= new Runnable() {
	            public void run() {
	                if (currentPage == NUM_PAGES) {
	                    currentPage = 0;
	                }
	                mViewPager.setCurrentItem(currentPage++, true);
	            }
	        };

	        swipeTimer = new Timer();
	        swipeTimer.schedule(new TimerTask() {

	            public void run() {
	                handler.post(Update);
	            }
	        }, 1000, 4000);
	 }

}
