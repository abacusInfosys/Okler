package com.okler.adapters;

import com.okleruser.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CustomPagerAdapter extends PagerAdapter {
	 
    Context mContext;
    LayoutInflater mLayoutInflater;
 
    int[] mResources = {
            R.drawable.intro_upload_prescrpition,
            R.drawable.intro_medicine_screen,
            R.drawable.intro_diagnostic_services,
            R.drawable.intro_medical_services,
            R.drawable.intro_physiotherapy,
            R.drawable.intro_health_shop
    };
    
    public CustomPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    @Override
    public int getCount() {
        return mResources.length;
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
 
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
 
        ImageView imageView = (ImageView) itemView.findViewById(R.id.pagerViewItem);
        imageView.setImageResource(mResources[position]);
 
        container.addView(itemView);
 
        return itemView;
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}