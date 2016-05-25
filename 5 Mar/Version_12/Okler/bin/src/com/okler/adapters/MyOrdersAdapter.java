package com.okler.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug.IntToString;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.okleruser.R;
import com.okler.databeans.OrdersDataBean;
import com.okler.databeans.ProductDataBean;


public class MyOrdersAdapter extends BaseAdapter {
	Context con;
	ArrayList<OrdersDataBean> prods;
	LayoutInflater inflater;
	public MyOrdersAdapter(Context context, ArrayList<OrdersDataBean> prodDataBean) {
	
		prods = prodDataBean;	
    	inflater=LayoutInflater.from(context);
    	
    }
	@Override
	public int getCount() {
	//	Log.i("prodds size", toString().valueOf(prods.size()));
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
		TextView id,price,orderStatus,date;				
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder; 
		
		if(convertView==null){

		
			holder=new viewHolder();
			convertView=inflater.inflate(R.layout.row_my_order_home,null);
			//holder.imgRed=(ImageView) convertView.findViewById(R.id.imgSelection);
			holder.id=(TextView) convertView.findViewById(R.id.orderId);
			holder.orderStatus=(TextView) convertView.findViewById(R.id.OrderStatus);
			holder.price = (TextView) convertView.findViewById(R.id.Price);
			holder.date = (TextView) convertView.findViewById(R.id.Date);
			
		
			
			convertView.setTag(holder);	
		}else{
			Log.i("nullvale", prods.get(position).getOrderId());
			holder=(viewHolder) convertView.getTag();
		}	
		holder.id.setText(prods.get(position).getOrderId());
		holder.price.setText(prods.get(position).getPrice());
		holder.orderStatus.setText(prods.get(position).getOrderStatus());
		holder.date.setText(prods.get(position).getDate());
	
		

		return convertView;	
	
	}
}
