package com.okler.adapters;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.okler.adapters.DiseaseAdapter.viewHolder;
import com.okler.databeans.ProductDataBean;
import com.okleruser.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AlloSubstituesAdapter extends BaseAdapter{
	
	Context context;
	ArrayList<ProductDataBean> subList;
	LayoutInflater inflater;
	/*This is testing*/
	public AlloSubstituesAdapter(Context context2,ArrayList<ProductDataBean> subList2) {
		this.context = context2;
		this.subList = subList2;
		inflater = LayoutInflater.from(context2);
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return subList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return subList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class ViewHolder{
		TextView subtsName,oklerPrValueSub,youSavePercSub;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.custom_view_prod_subst_layout, null);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.subtsName = (TextView)convertView.findViewById(R.id.subtsName);
		holder.oklerPrValueSub = (TextView)convertView.findViewById(R.id.oklerPrValueSub);
		holder.youSavePercSub = (TextView)convertView.findViewById(R.id.youSavePercSub);
		String name = subList.get(position).getProdName();
		holder.subtsName.setText(name);
		holder.oklerPrValueSub.setText("Rs. "+subList.get(position).getOklerPrice());
		holder.youSavePercSub.setText(subList.get(position).getDiscount()+"%");
		
		return convertView;
	}

}
