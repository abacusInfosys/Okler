package com.okler.adapters;

import java.util.ArrayList;


import com.okleruser.R;
import com.okler.databeans.DiseaseDataBean;
import com.okler.databeans.ProductDataBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProductsAdapter extends BaseAdapter{
	Context con;
	ArrayList<ProductDataBean> prods;
	LayoutInflater inflater;
	
	public ProductsAdapter(Context context, ArrayList<ProductDataBean> prodDataBean) {
		con = context;
		prods = prodDataBean;	
    	inflater=LayoutInflater.from(context);
    	
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return prods.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return prods.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public class viewHolder{
		TextView MedName;
				
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder; 
		if(convertView==null){
			holder=new viewHolder();
			
			convertView=inflater.inflate(R.layout.row_single_disease,null);
			//holder.imgRed=(ImageView) convertView.findViewById(R.id.imgSelection);
			holder.MedName=(TextView) convertView.findViewById(R.id.tvName);
			convertView.setTag(holder);	
		}else{
			holder=(viewHolder) convertView.getTag();
		}	
			holder.MedName.setText(prods.get(position).getProdName());			
		return convertView;	
	}
}
