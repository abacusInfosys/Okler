package com.okler.adapters;

import java.util.ArrayList;
import junit.framework.Test;
import com.okleruser.R;
import com.okler.databeans.TestDataBean;
import com.okler.diagnostics.DiagnoTestsHome;
import com.okler.utils.Okler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DiagnoTestAdapter extends BaseAdapter implements OnCheckedChangeListener {
	
	 
	Context context;
    ArrayList<TestDataBean> testDB;
    int count;
    private ArrayList<TestDataBean> arrayListObj=new ArrayList<TestDataBean>();
    private static LayoutInflater inflater=null;
    public DiagnoTestAdapter(Context context, ArrayList<TestDataBean> testDataBean) {
    	testDB = testDataBean;	
    	inflater=LayoutInflater.from(context);
    }
      
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return testDB.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return testDB.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public class viewHolder{
		TextView TestName;
		CheckBox chkBox;		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder; 
		if(convertView==null){
			holder=new viewHolder();
			convertView=inflater.inflate(R.layout.row_diagno_home_test,null);
			holder.chkBox= (CheckBox) convertView.findViewById(R.id.checkBox_diagno_test);
			holder.TestName=(TextView) convertView.findViewById(R.id.tvName);
			convertView.setTag(holder);	
		}else{
			holder=(viewHolder) convertView.getTag();
		}	
			holder.TestName.setText(testDB.get(position).getTestname());
			holder.chkBox.setTag(position);
			holder.chkBox.setOnCheckedChangeListener(null);
			holder.chkBox.setChecked(testDB.get(position).getCkIsChecked());
			holder.chkBox.setOnCheckedChangeListener((OnCheckedChangeListener)this);
	
		return convertView;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		int positionchk=(int) buttonView.getTag();
		TestDataBean testbean;
		System.out.println("positionchk"+positionchk);
		if(isChecked){
			count++;
			
			 testbean=new TestDataBean();
			testDB.get(positionchk).setCkIsChecked(true);
			testbean.setTestname(testDB.get(positionchk).getTestname().toString());
			testbean.setTestId(testDB.get(positionchk).getTestId());
			arrayListObj.add(testbean);
			Okler.getInstance().setListSelectedDiagnoTest(arrayListObj);
			
			if(count>0)
			{
				DiagnoTestsHome.setNextEnabled();
			}
			
		}else{
			count--;
			
			int testId=testDB.get(positionchk).getTestId();
			for(int i=0;i<arrayListObj.size();i++){
				if(arrayListObj.get(i).getTestId()==testId){
					arrayListObj.remove(i);
					break;
				}
			}
			testDB.get(positionchk).setCkIsChecked(false);
			Okler.getInstance().setListSelectedDiagnoTest(arrayListObj);
			if(count==0)
			{
			DiagnoTestsHome.setNextDisanled();
			}
			
		}
	}
	
}
