package com.okler.adapters;


import com.okler.Profile.ProfileAddress;
import com.okleruser.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ProfileAddressAdapter extends BaseAdapter {
	
	private static LayoutInflater inflater=null;
	Context context;
    
    public ProfileAddressAdapter(ProfileAddress profileAddress)    
    {
		// TODO Auto-generated constructor stub
    	
    	this.context=profileAddress;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View rowView;

        rowView = inflater.inflate(R.layout.row_profile_address, null);
		return rowView;
	}
}
