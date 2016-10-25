package com.okler.adapters;

import java.util.ArrayList;

import com.okler.adapters.DiagnoPackageAdapter.ViewHolder;
import com.okler.databeans.DiseaseDataBean;
import com.okler.utils.Okler;
import com.okleruser.R;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DiagnoDiseaseAdapter extends RecyclerView.Adapter<DiagnoDiseaseAdapter.ViewHolder>{

	Activity ack;
	ArrayList<DiseaseDataBean> disList;
	RecyclerView rcView;
	public DiagnoDiseaseAdapter(Activity ack1,ArrayList<DiseaseDataBean> disList1,RecyclerView rcView1){
		this.ack=ack1;
		this.disList = disList1;
		this.rcView = rcView1;
	}
	
	public class ViewHolder extends RecyclerView.ViewHolder{
		public ImageView chkBox;
		public TextView disName;
		public RelativeLayout disMain;
		public ViewHolder(View itemView) {
			super(itemView);
			chkBox = (ImageView) itemView
					.findViewById(R.id.checkBox_diagno_test);
			disName=(TextView) itemView.findViewById(R.id.tvName);
			disMain = (RelativeLayout)itemView.findViewById(R.id.testMain);
		}
		
	}
	
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return disList.size();
	}
	
	@Override
	public void onBindViewHolder(final ViewHolder v, int position) {
		v.disName.setText(disList.get(position).getDiseaseName());
		v.chkBox.setTag(position);
		v.disMain.setTag(position);
		
		int disId = Okler.getInstance().getDiagnoOrder().getDiseaseId();
		int disId2 = disList.get(position).getDiseaseId();
		
		if(disId==disId2){
			v.chkBox.setBackgroundResource(R.drawable.checkbox_selected);
		}else{
			v.chkBox.setBackgroundResource(R.drawable.checkbox_unselected);
		}
		
		v.disMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v1) {
				int tag = Integer.parseInt(v1.getTag().toString());
				/*if(v.chkBox.getBackground().getConstantState()==ack.getResources().getDrawable(R.drawable.checkbox_selected).getConstantState()){
					v.chkBox.setBackgroundResource(R.drawable.checkbox_unselected);
					Okler.getInstance().getDiagnoOrder().setDiseaseId(0);
				}else{*/
					for(int i = 0; i<rcView.getChildCount();i++){
						View rcRelv = rcView.getChildAt(i);
						RelativeLayout rcRel = (RelativeLayout)rcRelv;
						View chkImgv = rcRel.getChildAt(1);
						ImageView chkImg = (ImageView)chkImgv;
						chkImg.setBackgroundResource(R.drawable.checkbox_unselected);
					}
					v.chkBox.setBackgroundResource(R.drawable.checkbox_selected);
					Okler.getInstance().getDiagnoOrder().setDiseaseId(disList.get(tag).getDiseaseId());	
				//}
				
				
				
			}
		});
		
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(ack).inflate(R.layout.row_diagno_home_test,arg0,false);
	     
		//ViewHolder vh = new ViewHolder(v);
	     
	     return new ViewHolder(v);
	}
}
