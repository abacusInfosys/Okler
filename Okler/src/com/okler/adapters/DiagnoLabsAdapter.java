package com.okler.adapters;



import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.okler.databeans.DiagnoPackageDataBean;
import com.okler.databeans.diagnobean.DiagnoLabBranchDataBean;
import com.okler.databeans.diagnobean.DiagnoLabPackageDataBean;
import com.okler.databeans.diagnobean.DiagnoLabTestDataBean;
import com.okler.databeans.diagnobean.DiagnoPackagesDataBean;
import com.okler.databeans.diagnobean.DiagnoTestDataBean;
import com.okler.diagno.DiagnoLabPickup;
import com.okler.diagno.DiagnoTestOrderList;
import com.okler.diagnostics.DiagnoOrderSummary;
import com.okler.enums.DiagnoOrderType;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okleruser.R;
import com.okleruser.R.string;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DiagnoLabsAdapter extends RecyclerView.Adapter<DiagnoLabsAdapter.ViewHolder>{
	Activity ack;
	ArrayList<DiagnoLabTestDataBean> dLabTestList = new ArrayList<DiagnoLabTestDataBean>();
	ArrayList<DiagnoLabPackageDataBean> dLabPkgList = new ArrayList<DiagnoLabPackageDataBean>();
	ImageLoader imgloader;
	ArrayList<DiagnoTestDataBean> testlist = new ArrayList<DiagnoTestDataBean>();
	ArrayList<DiagnoTestDataBean> testListPkg = new ArrayList<DiagnoTestDataBean>();
	ArrayList<DiagnoPackagesDataBean> pkgList = new ArrayList<DiagnoPackagesDataBean>();
	float mrp = 0.0f;
	SpannableString mrpStr;
	DiagnoOrderType orderType;
	String testIds,getRecPkgUrl;
	DiagnoLabPackageDataBean diagnoLPDataBean;
	DiagnoLabTestDataBean dLabTestBean;
	ArrayList<DiagnoPackagesDataBean> dpList;
	DiagnoPackagesDataBean dpBean;
	DiagnoTestDataBean dtBean;
	boolean isFromSummary=false;
	
	public DiagnoLabsAdapter(Activity ack1,ArrayList<DiagnoLabTestDataBean> dLabTestList1
			,ArrayList<DiagnoLabPackageDataBean> dLabPkgList1,DiagnoOrderType orderType1,boolean isFromSummary1){
		this.ack = ack1;
		this.dLabTestList = dLabTestList1;
		this.dLabPkgList = dLabPkgList1;
		this.orderType = orderType1;
		this.isFromSummary = isFromSummary1;
	}
	
	public class ViewHolder extends RecyclerView.ViewHolder {
		public TextView txtlabname,txtcredetion,txt_fasting,mrpValue,
		youSaveValue,okPriceValue,selectedTestCount,selectedTest;
		public NetworkImageView lab_photo;
		public ImageView recPkg;
		public RelativeLayout mainLayout;
		public ViewHolder(View itemView) {
			super(itemView);
			
			txtlabname = (TextView)itemView.findViewById(R.id.txtlabname);
			lab_photo = (NetworkImageView)itemView.findViewById(R.id.lab_photo);
			txtcredetion = (TextView)itemView.findViewById(R.id.txtcredetion);
			txt_fasting = (TextView)itemView.findViewById(R.id.txt_fasting);
			mrpValue = (TextView)itemView.findViewById(R.id.mrpValue);
			youSaveValue = (TextView)itemView.findViewById(R.id.youSaveValue);
			okPriceValue = (TextView)itemView.findViewById(R.id.okPriceValue);
			selectedTestCount = (TextView)itemView.findViewById(R.id.selectedTestCount);
			recPkg = (ImageView)itemView.findViewById(R.id.recPkg);
			mainLayout = (RelativeLayout)itemView.findViewById(R.id.mainLayout);
			selectedTest = (TextView)itemView.findViewById(R.id.selectedTest);
		}

	}

	@Override
	public int getItemCount() {
		if(orderType.equals(DiagnoOrderType.TEST))
			return dLabTestList.size();
		else
			return dLabPkgList.size();	
	}

	@Override
	public void onBindViewHolder(ViewHolder v, int position) {
		if(orderType.equals(DiagnoOrderType.TEST)){
		v.txtlabname.setText(dLabTestList.get(position).getCurrentLab().getLab_name());
		v.txtcredetion.setText(dLabTestList.get(position).getCurrentLab().getLab_award());
		imgloader = VolleyRequest.getInstance(ack).getImageLoader();
		v.lab_photo.setImageUrl(dLabTestList.get(position).getCurrentLab().getLab_logo_image_url()
				+dLabTestList.get(position).getCurrentLab().getLab_logo_image_path(), imgloader);
		mrpStr = new SpannableString(""+dLabTestList.get(position).getMrp());
		mrpStr.setSpan(new StrikethroughSpan(), 0, mrpStr.length(), 0);
		v.mrpValue.setText(mrpStr);
		v.okPriceValue.setText(""+dLabTestList.get(position).getOkler_price());
		v.youSaveValue.setText(dLabTestList.get(position).getYouSave()+" %");
		v.selectedTestCount.setText(dLabTestList.get(position).getTestCount()+"");
		
		
		//if(dLabTestList.get(position).getCurrentLab().is_recommended_package_available())
			v.recPkg.setVisibility(View.VISIBLE);
		
		testlist = dLabTestList.get(position).getCurrentTests();
		for (int i = 0; i < testlist.size(); i++) {
			if(testlist.get(i).isFastingRequired()){
				v.txt_fasting.setVisibility(View.VISIBLE);
				break;
			}
		}
		}
		if(orderType.equals(DiagnoOrderType.PACKAGE)){
			v.txtlabname.setText(dLabPkgList.get(position).getCurrentLab().getLab_name());
			v.txtcredetion.setText(dLabPkgList.get(position).getCurrentLab().getLab_award());
			imgloader = VolleyRequest.getInstance(ack).getImageLoader();
			v.lab_photo.setImageUrl(dLabPkgList.get(position).getCurrentLab().getLab_logo_image_url()+
					dLabPkgList.get(position).getCurrentLab().getLab_logo_image_path(), imgloader);
			v.mrpValue.setText(""+dLabPkgList.get(position).getMrp());
			v.okPriceValue.setText(""+dLabPkgList.get(position).getOkler_price());
			v.youSaveValue.setText(dLabPkgList.get(position).getYouSave()+" %");
			v.selectedTestCount.setText(dLabPkgList.get(position).getPkgCount()+"");
			v.selectedTest.setText("selected package(s)");
			pkgList = dLabPkgList .get(position).getCurrentPkgs();
			for (int i = 0; i < pkgList.size(); i++) {
				if(pkgList.get(i).isFastingRequired())
					v.txt_fasting.setVisibility(View.VISIBLE);
					break;
			}
		}
		v.recPkg.setTag(position);
		v.mainLayout.setTag(position);
		v.recPkg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int id = Integer.parseInt(String.valueOf(v.getTag()));
				//Okler.getInstance().getDiagnoOrder().setLabTestDataBean(dLabTestList.get(id));
				dLabTestBean = new DiagnoLabTestDataBean();
				dLabTestBean = dLabTestList.get(id);
				testlist = new ArrayList<DiagnoTestDataBean>();
				testlist = dLabTestBean.getCurrentTests();
				for (int i = 0; i < testlist.size(); i++) {
					if(i==0){
						testIds = testlist.get(i).getTestId()+"";
					}
					else{
						testIds = testIds + "," + testlist.get(i).getTestId();
					}
				}
				int pkgId = dLabTestBean.getCurrentLab().getLabId();
				getRecPkgUrl = ack.getString(R.string.serverUrl)+ack.getString(R.string.getRecPkgsPart1)+testIds+ack.getString(R.string.getLabsTest2)+pkgId;
				String url = getRecPkgUrl;
				WebJsonObjectRequest getRecPkgJson = new WebJsonObjectRequest(Method.GET, getRecPkgUrl, new JSONObject(), new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						diagnoLPDataBean = new DiagnoLabPackageDataBean();
						DiagnoLabBranchDataBean labBean = new DiagnoLabBranchDataBean();
						labBean = dLabTestBean.getCurrentLab();
						diagnoLPDataBean.setCurrentLab(labBean);
						dpList = new ArrayList<DiagnoPackagesDataBean>();
						JSONArray jarr1 = response.optJSONArray("result");
						if(jarr1!=null){
						JSONObject job1 = new JSONObject();
						float mrp = 0.0f,okPrice =0.0f,yousaveRs = 0.0f;
						int youSave = 0;
						for (int i = 0; i < jarr1.length(); i++) {
							
						
							dpBean = new DiagnoPackagesDataBean();
							job1 = jarr1.optJSONObject(i);
							dpBean.setPacakageId(Integer.parseInt(job1.optString("package_id")));
							dpBean.setPackageName(job1.optString("package_name"));
							dpBean.setReportTime(job1.optString("results_time"));
							mrp = Float.parseFloat(job1.optString("mrp"));
							dpBean.setPackMrp(mrp);
							okPrice = Float.parseFloat(job1.optString("okler_amount"));
							dpBean.setPackOklerPrice(okPrice);
							youSave = Integer.parseInt(job1.optString("okler_discount"));
							dpBean.setPackYouSave(youSave);
							yousaveRs = mrp - okPrice;
							dpBean.setYouSaveRs(yousaveRs);
							JSONObject pkgDetails = job1.optJSONObject("package_details");
							JSONObject pkgTestDetails = pkgDetails.optJSONObject("packageTestDetails");
							JSONArray testArr = pkgTestDetails.optJSONArray("ifAnyTests");
							testListPkg = new ArrayList<DiagnoTestDataBean>();
							
							for (int j = 0; j < testArr.length(); j++) {
								dtBean = new DiagnoTestDataBean();
								JSONObject testObj = testArr.optJSONObject(j);
								dtBean.setTestId(Integer.parseInt(testObj.optString("test_id")));
								dtBean.setTestname(testObj.optString("test_name"));
								testListPkg.add(dtBean);
							}
							testListPkg.trimToSize();
							dpBean.setTestList(testListPkg);
							dpList.add(dpBean);
							
							
						}
						dpList.trimToSize();
						diagnoLPDataBean.setCurrentPkgs(dpList);
						DiagnoLabPackageDataBean dpBean = diagnoLPDataBean;
						diagnoLPDataBean.setMrp(mrp);
						diagnoLPDataBean.setOkler_price(okPrice);
						diagnoLPDataBean.setYouSave(youSave);
						diagnoLPDataBean.setYouSaveRs(yousaveRs);
						Okler.getInstance().getDiagnoOrder().setLabTestDataBean(new DiagnoLabTestDataBean());
						Okler.getInstance().getDiagnoOrder().setLabPkgDataBean(diagnoLPDataBean);
						Okler.getInstance().getDiagnoOrder().setOrderType(DiagnoOrderType.PACKAGE);
						Intent intent = new Intent(ack,DiagnoTestOrderList.class);
						intent.putExtra("isFromTest", true);
						ack.startActivity(intent);
						}else{
							Toast.makeText(ack, "No recommended package.", Toast.LENGTH_SHORT).show();
						}
						
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("Error", error.toString());
						Toast.makeText(ack, "Some error occurred while getting results, please try again.", Toast.LENGTH_SHORT).show();
					}
				});
				VolleyRequest.addJsonObjectRequest(ack, getRecPkgJson);
				
				/*Intent intent = new Intent(ack,DiagnoTestOrderList.class);
				//intent.putExtra("isRecPkg", true);
				ack.startActivity(intent);*/
			}
		});
		
		v.mainLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int id = Integer.parseInt(String.valueOf(v.getTag()));
				if(orderType.equals(DiagnoOrderType.TEST)){
					Okler.getInstance().getDiagnoOrder().setLabTestDataBean(dLabTestList.get(id));	
				}
				if(orderType.equals(DiagnoOrderType.PACKAGE)){
					DiagnoLabPackageDataBean d = dLabPkgList.get(id);
					
					Okler.getInstance().getDiagnoOrder().setLabPkgDataBean(dLabPkgList.get(id));
				}
				Intent intent = null;
				if(isFromSummary){
					intent = new Intent(ack,DiagnoOrderSummary.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				}
				else
					intent = new Intent(ack,DiagnoTestOrderList.class);
				ack.startActivity(intent);
				
				
			}
		});
		
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(ack).inflate(R.layout.row_labs_available,arg0,false);
	     ViewHolder vh = new ViewHolder(v);
	     return vh;
	}

}
