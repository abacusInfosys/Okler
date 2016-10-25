package com.okler.adapters;

import java.util.ArrayList;

import junit.framework.Test;

import com.okleruser.R;
import com.okler.databeans.TestDataBean;
import com.okler.databeans.diagnobean.DiagnoTestDataBean;
import com.okler.diagno.DiagnoTestPkgHome;
import com.okler.enums.DiagnoOrderType;
import com.okler.utils.Okler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DiagnoTestAdapter extends BaseAdapter implements
		OnCheckedChangeListener {

	Context context;
	ArrayList<DiagnoTestDataBean> testDB;
	int count;
	private ArrayList<DiagnoTestDataBean> arrayListObj = new ArrayList<DiagnoTestDataBean>();
	private static LayoutInflater inflater = null;
	ArrayList<DiagnoTestDataBean> selected_tests = new ArrayList<DiagnoTestDataBean>();
	DiagnoOrderType orderType;

	public DiagnoTestAdapter(Context context,
			ArrayList<DiagnoTestDataBean> testDataBean) {
		testDB = testDataBean;
		inflater = LayoutInflater.from(context);
		this.context = context;
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

	public class viewHolder {
		TextView TestName;
		CheckBox chkBox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder;
		if (convertView == null) {
			holder = new viewHolder();
			convertView = inflater.inflate(R.layout.row_diagno_home_test, null);
			holder.chkBox = (CheckBox) convertView
					.findViewById(R.id.checkBox_diagno_test);
			holder.TestName = (TextView) convertView.findViewById(R.id.tvName);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		selected_tests = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests();
		holder.TestName.setText(testDB.get(position).getTestname());
		holder.chkBox.setTag(position);
		//holder.chkBox.setOnCheckedChangeListener(null);
		//Toast.makeText(context, position+" "+testDB.get(position).getCkIsChecked().toString(), Toast.LENGTH_SHORT).show();
		holder.chkBox.setChecked(testDB.get(position).getCkIsChecked());
		holder.chkBox
				.setOnCheckedChangeListener((OnCheckedChangeListener) this);
		for(int i = 0; i<selected_tests.size();i++){
			if(selected_tests.get(i).getTestId() == testDB.get(position).getTestId()){
				holder.chkBox.setChecked(true);
				//testDB.get(position).setCkIsChecked(true);
			}
		}
		/*for(int i=0;i<selected_tests.size();i++)
		{
			
			if(selected_tests.get(i).getTestId() == testDB.get(position).getTestId())
			{
				//holder.checkBox.setChecked(testReceived.get(position).isCkIsChecked());
				//holder.checkBox.setChecked(testReceived.get(position).isCkIsChecked());
				int tag = Integer.parseInt(holder.chkBox.getTag().toString());
				
				if(tag == position)
				{
					holder.chkBox.setChecked(true);
					//holder.checkBox.performClick();
				}
			}
		}*/
		/*holder.chkBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				View v1 =(View) v.getParent();
				int position = Integer.parseInt(v.getTag().toString());
				DiagnoTestPkgHome.selectTestPkg(v1, position);
				
			}
		});*/
		return convertView;
	}

	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		//Okler.getInstance().getDiagnoOrder().getLabTestDataBean().setCurrentTests(arrayListObj);
		//	DiagnoTestPkgHome.selectTestPkg(view, position);
		if (Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().size() > 0) {
			DiagnoTestPkgHome.setNextEnabled();
		}else
			DiagnoTestPkgHome.setNextDisabled();
		DiagnoTestPkgHome.setSelectedTest(Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().size());
		
		// TODO Auto-generated method stub
		/*int positionchk = (int) buttonView.getTag();
		orderType = Okler.getInstance().getDiagnoOrder().getOrderType();
		if(orderType == DiagnoOrderType.TEST)
		{
			
		}
		DiagnoTestDataBean testbean;
		System.out.println("positionchk" + positionchk);
		if (isChecked) {
			count++;

			testbean = new DiagnoTestDataBean();
			testDB.get(positionchk).setCkIsChecked(true);
			testbean.setTestname(testDB.get(positionchk).getTestname()
					.toString());
			testbean.setTestId(testDB.get(positionchk).getTestId());
			arrayListObj.add(testbean);
			Okler.getInstance().getDiagnoOrder().getLabTestDataBean().setCurrentTests(arrayListObj);

			if (count > 0) {
				DiagnoTestPkgHome.setNextEnabled();
			}

		} else {
			count--;

			int testId = testDB.get(positionchk).getTestId();
			for (int i = 0; i < arrayListObj.size(); i++) {
				if (arrayListObj.get(i).getTestId() == testId) {
					arrayListObj.remove(i);
					break;
				}
			}
			testDB.get(positionchk).setCkIsChecked(false);
			Okler.getInstance().getDiagnoOrder().getLabTestDataBean().setCurrentTests(arrayListObj);
			if (count == 0) {
				DiagnoTestPkgHome.setNextDisabled();
			}

		}
			DiagnoTestPkgHome.setSelectedTest(count);*/
	}

}
