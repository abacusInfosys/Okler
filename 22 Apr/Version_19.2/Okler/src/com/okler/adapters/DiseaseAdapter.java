package com.okler.adapters;

import java.util.ArrayList;

import com.okler.adapters.DiagnoTestAdapter.viewHolder;
import com.okleruser.R;
import com.okler.databeans.DiseaseDataBean;
import com.okler.databeans.TestDataBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DiseaseAdapter extends BaseAdapter{

	Context con;
	ArrayList<DiseaseDataBean> dieseases = new ArrayList<DiseaseDataBean>();
	LayoutInflater inflater;
	Boolean isDig;
	public DiseaseAdapter(Context context, ArrayList<DiseaseDataBean> diseaseDataBean,Boolean isDigno) {
		con = context;
		dieseases = diseaseDataBean;	
    	inflater=LayoutInflater.from(context);
    	isDig = isDigno;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dieseases.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dieseases.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public class viewHolder{
		/*ImageView imgRed;*/
		CheckBox chkBox;
		TextView DieseaseName;
				
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder; 
		
			
			
			//holder.imgRed=(ImageView) convertView.findViewById(R.id.imgSelection);
			
			//holder.imgRed=(ImageView) convertView.findViewById(R.id.imgSelection);
			
			if(isDig){
				if(convertView==null){
					holder=new viewHolder();
				convertView=inflater.inflate(R.layout.row_diagno_home_disease,null);
				holder.DieseaseName=(TextView) convertView.findViewById(R.id.tvName);
				//holder.chkBox= (CheckBox) convertView.findViewById(R.id.checkBox_diagno_test);
				convertView.setTag(holder);
				}else{
					holder=(viewHolder) convertView.getTag();
				}
				String dname=dieseases.get(position).getDiseaseName();
				holder.DieseaseName.setText(dname);
				/*if(dname.equals("No data found.")){
				holder.chkBox.setVisibility(View.GONE);	
				}else{
				holder.chkBox.setTag(position);
				holder.chkBox.setOnCheckedChangeListener(null);
				holder.chkBox.setChecked(dieseases.get(position).getCkIsChecked());
				holder.chkBox.setOnCheckedChangeListener((OnCheckedChangeListener)this);
				}*/
			}
			else{
				if(convertView==null){
					holder=new viewHolder();
				convertView=inflater.inflate(R.layout.row_single_disease,null);			
				convertView.setTag(holder);
				}else{
					holder=(viewHolder) convertView.getTag();
				}
				holder.DieseaseName=(TextView) convertView.findViewById(R.id.tvName);
				String name = dieseases.get(position).getDiseaseName();
				String start = " (";
				String count = String.valueOf(dieseases.get(position).getCount());
				String end = ")";
				String finalname= name+start+count+end;
				holder.DieseaseName.setText(finalname);
				
			}
		
			
			//holder.DieseaseName.setText(dieseases.get(position).getDiseaseName());			
		return convertView;	
		
	}

}
