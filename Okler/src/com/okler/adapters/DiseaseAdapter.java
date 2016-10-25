package com.okler.adapters;

import java.util.ArrayList;
import com.okleruser.R;
import com.okler.databeans.DiseaseDataBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DiseaseAdapter extends BaseAdapter{

	Context con;
	ArrayList<DiseaseDataBean> dieseases = new ArrayList<DiseaseDataBean>();
	LayoutInflater inflater;
	public DiseaseAdapter(Context context, ArrayList<DiseaseDataBean> diseaseDataBean) {
		con = context;
		dieseases = diseaseDataBean;	
    	inflater=LayoutInflater.from(context);
    }
	@Override
	public int getCount() {
		return dieseases.size();
	}

	@Override
	public Object getItem(int position) {
		return dieseases.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	public class viewHolder{
		TextView DieseaseName;
				
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder; 				
		
				if(convertView==null){
					holder=new viewHolder();
				convertView=inflater.inflate(R.layout.row_single_disease,null);
				holder.DieseaseName=(TextView) convertView.findViewById(R.id.tvName);
				convertView.setTag(holder);
				}else{
					holder=(viewHolder) convertView.getTag();
				}
				String dname=dieseases.get(position).getDiseaseName();
				holder.DieseaseName.setText(dname);
		return convertView;	
	}

}
