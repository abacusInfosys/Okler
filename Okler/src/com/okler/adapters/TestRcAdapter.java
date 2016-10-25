package com.okler.adapters;

import java.util.ArrayList;

import com.okler.adapters.DiagnoLabsAdapter.ViewHolder;
import com.okler.databeans.diagnobean.DiagnoTestDataBean;
import com.okler.diagno.DiagnoTestPkgHome;
import com.okler.interfaces.OnLoadMoreListener;
import com.okler.utils.Okler;
import com.okleruser.R;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TestRcAdapter extends RecyclerView.Adapter<TestRcAdapter.ViewHolder>{
	Activity ack;
	ArrayList<DiagnoTestDataBean> testList = new ArrayList<DiagnoTestDataBean>();
	ArrayList<DiagnoTestDataBean> selTestList = new ArrayList<DiagnoTestDataBean>();
	RecyclerView rcview;
	//OnLoadMoreListener onLoadMoreListener;
	
	public TestRcAdapter(Activity ack1,ArrayList<DiagnoTestDataBean> testList1,RecyclerView rcview1){
		this.ack = ack1;
		this.testList = testList1;
		this.rcview = rcview1;
		
	}
	public class ViewHolder extends RecyclerView.ViewHolder{
		public ImageView chkBox;
		public TextView testName;
		public RelativeLayout testMain;
		public ViewHolder(View itemView) {
			super(itemView);
			chkBox = (ImageView) itemView
					.findViewById(R.id.checkBox_diagno_test);
			testName=(TextView) itemView.findViewById(R.id.tvName);
			testMain = (RelativeLayout)itemView.findViewById(R.id.testMain);
		}
		
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return testList.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder v, int position) {
		//v.setIsRecyclable(false);
		//Toast.makeText(ack, "inOnBind", 100).show();
		v.testName.setText(testList.get(position).getTestname());
		v.testMain.setTag(position);
		v.chkBox.setTag(position);
		int tid = testList.get(position).getTestId();
		selTestList = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests();
		for(int i =0;i<selTestList.size();i++){
			int tid2 = selTestList.get(i).getTestId();
			if(tid==tid2){
				//v.g
				
				//if(v.chkBox.getBackground().getConstantState()==ack.getResources().getDrawable(R.drawable.checkbox_unselected).getConstantState()){
				v.chkBox.setBackgroundResource(R.drawable.checkbox_selected);
				//Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().add(testList.get(position));
				//Toast.makeText(ack, v.chkBox.getTag().toString(),100).show();
				//}
				break;
			}else{
				v.chkBox.setBackgroundResource(R.drawable.checkbox_unselected);
			}
			
		}
		int size = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().size();
		if (size == 0) {
			DiagnoTestPkgHome.setNextDisabled();
		}
		if (size > 0) {
			DiagnoTestPkgHome.setNextEnabled();						
		}
		DiagnoTestPkgHome.setSelectedTest(size);
		
		/*if(v.chkBox.getBackground().getConstantState()==ack.getResources().getDrawable(R.drawable.checkbox_selected).getConstantState()){
			int l = Integer.parseInt(v.chkBox.getTag().toString());
			Toast.makeText(ack, testList.get(l).getTestname()+" "+Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().size(), 200).show();
		}*/
		
		v.testMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v1) {
				//Toast.makeText(ack, "clicked", 200).show();
				int tag = Integer.parseInt(v1.getTag().toString());
				
				if(v.chkBox.getBackground().getConstantState()==ack.getResources().getDrawable(R.drawable.checkbox_selected).getConstantState()){
					v.chkBox.setBackgroundResource(R.drawable.checkbox_unselected);
					
					int id =testList.get(tag).getTestId();
	
					for(int i =0;i<Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().size();i++){
						int id2 = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().get(i).getTestId();
						if(id==id2){
							Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().remove(i);
							break;
						}
					}
					//Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().remove(tag);
					//Toast.makeText(ack, "deselected", 100).show();				
					}else{
						boolean isRemoved = false;
						int tid2 = testList.get(tag).getTestId();
						for (int i = 0; i < Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().size(); i++) {
							int tid = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().get(i).getTestId();
							if(tid2==tid){
								Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().remove(i);
								v.chkBox.setBackgroundResource(R.drawable.checkbox_unselected);
								isRemoved=true;
								break;
							
						}
						}	
						if(!isRemoved){
						for (int i = 0; i < Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().size(); i++){
							int tid = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().get(i).getTestId();
							if(tid2!=tid){
							v.chkBox.setBackgroundResource(R.drawable.checkbox_selected);
							Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().add(testList.get(tag));
							break;
						}
						}
						if(Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().size()==0){
							v.chkBox.setBackgroundResource(R.drawable.checkbox_selected);
							Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().add(testList.get(tag));
						}
					//Toast.makeText(ack, "selected", 100).show();
				}
						
				
				
				/*if(v.chkBox.isChecked()){
					v.chkBox.setChecked(false);
					
				}else{
					v.chkBox.setChecked(true);
				}*/
					}
				int size = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests().size();
				if (size == 0) {
					DiagnoTestPkgHome.setNextDisabled();
				}
				if (size > 0) {
					DiagnoTestPkgHome.setNextEnabled();						
				}
				DiagnoTestPkgHome.setSelectedTest(size);
			}
		});
		/*Log.e("TP1", testList.size()+" == "+position+" == "+rcview.getChildCount());
		if((testList.size()-1)==position)
			
			rcview.scrollToPosition(rcview.getChildCount()-10);*/
	}

	/*public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		  this.onLoadMoreListener = onLoadMoreListener;
		 }*/
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(ack).inflate(R.layout.row_diagno_home_test,arg0,false);
	     
		//ViewHolder vh = new ViewHolder(v);
	     
	     return new ViewHolder(v);
	}

	
}
