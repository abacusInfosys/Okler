package com.okler.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.okler.adapters.MyOrdersAdapter.viewHolder;
import com.okleruser.R;
import com.okler.databeans.DiagnoOrderDataBean;
import com.okler.databeans.OrdersDataBean;
import com.okler.databeans.diagnobean.DiagnoOrder;

public class MyOrderDiagnoOrderAdapter extends BaseAdapter {
	Context con;
	ArrayList<DiagnoOrder> prods;
	LayoutInflater inflater;

	public MyOrderDiagnoOrderAdapter(Context context,
			ArrayList<DiagnoOrder> diagnoDataBean) {

		prods = diagnoDataBean;
		inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// Log.i("prodds size", toString().valueOf(prods.size()));
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

	public class viewHolder {
		TextView id, price, orderStatus, date;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder;

		if (convertView == null) {

			holder = new viewHolder();
			convertView = inflater.inflate(R.layout.row_my_order_home, null);
			holder.id = (TextView) convertView.findViewById(R.id.orderId);
			holder.orderStatus = (TextView) convertView
					.findViewById(R.id.OrderStatus);
			holder.price = (TextView) convertView.findViewById(R.id.Price);
			holder.date = (TextView) convertView.findViewById(R.id.Date);

			convertView.setTag(holder);
		} else {
			Log.i("nullvale", prods.get(position).getOrderId()+"");
			holder = (viewHolder) convertView.getTag();
		}
		holder.id.setText(prods.get(position).getOrderId());
		holder.price.setText(prods.get(position).getNetPayable()+"");
		holder.orderStatus.setText(prods.get(position).getStatus());
		holder.date.setText(prods.get(position).getApptDt());

		return convertView;

	}
}
