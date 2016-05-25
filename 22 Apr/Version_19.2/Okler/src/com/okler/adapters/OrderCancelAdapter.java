package com.okler.adapters;

import com.okleruser.R;

import android.R.layout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderCancelAdapter extends BaseAdapter{
	Context adaContext;
	String[] receivedReasons;
	LayoutInflater minflater;
	public OrderCancelAdapter(Context context, String[] names) {
		// TODO Auto-generated constructor stub
		this.adaContext=context;
		this.receivedReasons=names;
		this.minflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return receivedReasons.length;
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
	public class ViewHolder{
		TextView reasons;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=minflater.inflate(R.layout.row_cancel_order_reason,null);
			holder.reasons=(TextView) convertView.findViewById(R.id.listItemReason);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.reasons.setText(receivedReasons[position]);
		return convertView;
	}

}
