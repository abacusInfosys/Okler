package com.okler.adapters;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.okler.adapters.DiagnoTestAdapter.viewHolder;
import com.okler.adapters.TestRcAdapter.ViewHolder;
import com.okleruser.R;
import com.okler.databeans.DiagnoPackageDataBean;
import com.okler.databeans.DiseaseDataBean;
import com.okler.databeans.TestDataBean;
import com.okler.databeans.diagnobean.DiagnoPackagesDataBean;
import com.okler.databeans.diagnobean.DiagnoTestDataBean;
import com.okler.diagno.DiagnoTestPkgHome;
import com.okler.utils.Okler;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DiagnoPackageAdapter extends RecyclerView.Adapter<DiagnoPackageAdapter.ViewHolder>{
	Activity ack;
	ArrayList<DiagnoPackagesDataBean> pkgList,selPkgsList;
	RecyclerView rcview;
	
	public DiagnoPackageAdapter(Activity ack1,ArrayList<DiagnoPackagesDataBean> pkgList1,RecyclerView rcview1){
		this.ack = ack1;
		this.pkgList = pkgList1;
		this.rcview = rcview1;
		
		
	}
	
	public class ViewHolder extends RecyclerView.ViewHolder{
		public ImageView chkBox;
		public TextView pkgName;
		public RelativeLayout pkgMain;
		public ViewHolder(View itemView) {
			super(itemView);
			chkBox = (ImageView) itemView
					.findViewById(R.id.checkBox_diagno_test);
			pkgName=(TextView) itemView.findViewById(R.id.tvName);
			pkgMain = (RelativeLayout)itemView.findViewById(R.id.testMain);
		}
		
	}
	
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return pkgList.size();
	}
	
	@Override
	public void onBindViewHolder(final ViewHolder v, int position) {
		v.pkgName.setText(pkgList.get(position).getPackageName());
		v.pkgMain.setTag(position);
		v.chkBox.setTag(position);	
	int pid = pkgList.get(position).getPacakageId();
	selPkgsList = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs();
	for (int i = 0; i < selPkgsList.size(); i++) {
		int pid2 = selPkgsList.get(i).getPacakageId();
		if(pid==pid2){
			v.chkBox.setBackgroundResource(R.drawable.checkbox_selected);
			break;
		}else{
			v.chkBox.setBackgroundResource(R.drawable.checkbox_unselected);
		}
		int size = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().size();
		if (size == 0) {
			DiagnoTestPkgHome.setNextDisabled();
		}
		if (size > 0) {
			DiagnoTestPkgHome.setNextEnabled();						
		}
		DiagnoTestPkgHome.setSelectedPackage(size);
			
	}
	
	v.pkgMain.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v1) {
			int tag = Integer.parseInt(v1.getTag().toString());
			
			if(v.chkBox.getBackground().getConstantState()==ack.getResources().getDrawable(R.drawable.checkbox_selected).getConstantState()){
				v.chkBox.setBackgroundResource(R.drawable.checkbox_unselected);
				
				int id =pkgList.get(tag).getPacakageId();

				for(int i =0;i<Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().size();i++){
					int id2 = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().get(i).getPacakageId();
					if(id==id2){
						Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().remove(i);
						break;
					}
				}
				//Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().remove(tag);
				//Toast.makeText(ack, "deselected", 100).show();				
				}else{
					boolean isRemoved = false;
					int tid2 = pkgList.get(tag).getPacakageId();
					for (int i = 0; i < Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().size(); i++) {
						int tid = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().get(i).getPacakageId();
						if(tid2==tid){
							Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().remove(i);
							v.chkBox.setBackgroundResource(R.drawable.checkbox_unselected);
							isRemoved=true;
							break;
						
					}
					}	
					if(!isRemoved){
					for (int i = 0; i < Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().size(); i++){
						int tid = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().get(i).getPacakageId();
						if(tid2!=tid){
						v.chkBox.setBackgroundResource(R.drawable.checkbox_selected);
						Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().add(pkgList.get(tag));
						break;
					}
					}
					if(Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().size()==0){
						v.chkBox.setBackgroundResource(R.drawable.checkbox_selected);
						Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().add(pkgList.get(tag));
					}
				//Toast.makeText(ack, "selected", 100).show();
			}
					
			
			
			/*if(v.chkBox.isChecked()){
				v.chkBox.setChecked(false);
				
			}else{
				v.chkBox.setChecked(true);
			}*/
				}
			int size = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().size();
			if (size == 0) {
				DiagnoTestPkgHome.setNextDisabled();
			}
			if (size > 0) {
				DiagnoTestPkgHome.setNextEnabled();						
			}
			DiagnoTestPkgHome.setSelectedPackage(size);
			
		}
	});
	
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(ack).inflate(R.layout.row_diagno_home_test,arg0,false);
	     
		//ViewHolder vh = new ViewHolder(v);
	     
	     return new ViewHolder(v);
	}
/*	Context con;
	ArrayList<DiagnoPackagesDataBean> dPList;
	int count;
	private ArrayList<DiagnoPackagesDataBean> arrayListObj = new ArrayList<DiagnoPackagesDataBean>();
	ArrayList<DiagnoPackagesDataBean> selected_packages = new ArrayList<DiagnoPackagesDataBean>();
	LayoutInflater inflater = null;

	public DiagnoPackageAdapter(Context applicationContext,
			ArrayList<DiagnoPackagesDataBean> finallist1) {
		// TODO Auto-generated constructor stub
		con = applicationContext;
		dPList = finallist1;
		inflater = LayoutInflater.from(con);
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

	public class viewHolder {
		// ImageView select_packege_img;
		TextView package_name_TV, testList, testTitle, sched_pick;
		CheckBox chkBox;
		LinearLayout parent_layout;
		// Button sched_pick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder;
		if (convertView == null) {
			holder = new viewHolder();
			convertView = inflater.inflate(R.layout.row_diagno_home_test,
					null);

			holder.package_name_TV = (TextView) convertView
					.findViewById(R.id.tvName);
			holder.testList = (TextView) convertView
					.findViewById(R.id.testList);
			holder.chkBox = (CheckBox) convertView
					.findViewById(R.id.checkBox_diagno_test);
			// holder.parent_layout=(LinearLayout)
			// convertView.findViewById(R.id.LL_for_buttons);
			holder.sched_pick = (TextView) convertView
					.findViewById(R.id.sched_pick);
			holder.testTitle = (TextView) convertView
					.findViewById(R.id.tv_testTitle);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		selected_packages = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs();
		holder.package_name_TV.setText(dPList.get(position).getPackageName());
		holder.chkBox.setTag(position);
		//holder.chkBox.setOnCheckedChangeListener(null);
		holder.chkBox.setChecked(dPList.get(position).getCkIsChecked());
		holder.chkBox.setOnCheckedChangeListener((OnCheckedChangeListener) this);
		
		for(int i=0;i<selected_packages.size();i++)
		{
			
			if(selected_packages.get(i).getPacakageId() == dPList.get(position).getPacakageId())
			{
				holder.chkBox.setChecked(true);
				//holder.checkBox.setChecked(testReceived.get(position).isCkIsChecked());
				//holder.checkBox.setChecked(testReceived.get(position).isCkIsChecked());
				int tag = Integer.parseInt(holder.chkBox.getTag().toString());
				
				if(tag == position)
				{
					
					//holder.checkBox.performClick();
				}
			}
		}
		
		holder.chkBox
				.setOnCheckedChangeListener((OnCheckedChangeListener) this);
		
		return convertView;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
		//int positionchk = (int) buttonView.getTag();
		//DiagnoPackagesDataBean testbean;
		//System.out.println("positionchk" + positionchk);
		int size = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().size();
		if (size > 0) {
			DiagnoTestPkgHome.setNextEnabled();
		}
		if (size == 0) {
			DiagnoTestPkgHome.setNextDisabled();
		}
			DiagnoTestPkgHome.setSelectedPackage(size);
		//if (isChecked) {
			count++;

			testbean = new DiagnoPackagesDataBean();
			dPList.get(positionchk).setCkIsChecked(true);
			testbean.setPackageName(dPList.get(positionchk).getPackageName()
					.toString());
			testbean.setPacakageId(dPList.get(positionchk).getPacakageId());
			arrayListObj.add(testbean);
			Okler.getInstance().getDiagnoOrder().getLabTestDataBean().setCurrentPkgs(arrayListObj);

			

	//	} else {
			count--;

			int testId = dPList.get(positionchk).getPacakageId();
			for (int i = 0; i < arrayListObj.size(); i++) {
				if (arrayListObj.get(i).getPacakageId() == testId) {
					arrayListObj.remove(i);
					break;
				}
			}
			dPList.get(positionchk).setCkIsChecked(false);
			Okler.getInstance().getDiagnoOrder().getLabTestDataBean().setCurrentPkgs(arrayListObj);
			

	//	}
		
		
	}*/

}
