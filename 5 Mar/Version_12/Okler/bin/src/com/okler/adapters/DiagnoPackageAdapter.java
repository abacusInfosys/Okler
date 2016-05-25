package com.okler.adapters;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.okler.adapters.DiagnoTestAdapter.viewHolder;
import com.okleruser.R;
import com.okler.databeans.DiagnoPackageDataBean;
import com.okler.databeans.DiseaseDataBean;
import com.okler.databeans.TestDataBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DiagnoPackageAdapter extends BaseAdapter{

	Context con;
	ArrayList<DiagnoPackageDataBean> dPList;
	LayoutInflater inflater = null;

	public DiagnoPackageAdapter(Context applicationContext,
			ArrayList<DiagnoPackageDataBean> finallist) {
		// TODO Auto-generated constructor stub
		con = applicationContext;
		dPList = finallist;	
    	inflater=LayoutInflater.from(con);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dPList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dPList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public class viewHolder{
		//ImageView select_packege_img;
		TextView package_name_TV,testList,testTitle,sched_pick;
		LinearLayout parent_layout;
		//Button sched_pick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder; 
		if(convertView==null){
			holder=new viewHolder();
			convertView=inflater.inflate(R.layout.row_digno_package_layout,null);
				
				holder.package_name_TV = (TextView)convertView.findViewById(R.id.package_name_TV);
				holder.testList = (TextView)convertView.findViewById(R.id.testList);
				//holder.parent_layout=(LinearLayout) convertView.findViewById(R.id.LL_for_buttons);
				holder.sched_pick=(TextView)convertView.findViewById(R.id.sched_pick);
				holder.testTitle=(TextView) convertView.findViewById(R.id.tv_testTitle);
				convertView.setTag(holder);
		}else{
			holder=(viewHolder) convertView.getTag();
		}	
		//	holder.TestName.setText(testDB.get(position).getTestname());
		
		String packageName=dPList.get(position).getPackage_name();
		if(packageName.equals("Package Not Available")){
			holder.package_name_TV.setText(packageName);
			holder.testList.setVisibility(View.GONE);
			holder.sched_pick.setVisibility(View.GONE);
			holder.testTitle.setVisibility(View.GONE);
		}else{
			holder.package_name_TV.setText(packageName);
			ArrayList<TestDataBean> test=new ArrayList<TestDataBean>();
			test=dPList.get(position).getTestArrayList();
			String testAvailable = null;
				for(int i=0;i<test.size();i++){
					if(i==0){
					testAvailable=test.get(i).getTestname();	
					}else{
					testAvailable=testAvailable+","+test.get(i).getTestname();
					}
				
				}
			holder.testList.setText(testAvailable);
		}
		return convertView;
	}

}
