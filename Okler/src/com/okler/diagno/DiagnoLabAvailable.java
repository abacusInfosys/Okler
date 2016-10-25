package com.okler.diagno;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.okler.adapters.DiagnoLabsAdapter;
import com.okler.android.BaseActivity;
import com.okler.databeans.diagnobean.DiagnoLabBranchDataBean;
import com.okler.databeans.diagnobean.DiagnoLabPackageDataBean;
import com.okler.databeans.diagnobean.DiagnoLabTestDataBean;
import com.okler.databeans.diagnobean.DiagnoPackagesDataBean;
import com.okler.databeans.diagnobean.DiagnoTestDataBean;
import com.okler.enums.DiagnoOrderType;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DiagnoLabAvailable extends BaseActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

	RecyclerView rcview;
	RecyclerView.LayoutManager linearManager;
	String getLabsUrl;
	Activity ack;
	String testIds,pkgIds;
	int labId=0,cityId=0;
	ArrayList<DiagnoTestDataBean> diagnoTestList;
	ArrayList<DiagnoPackagesDataBean> diagnoPkgList;
	DiagnoLabsAdapter dLabAdap;
	ArrayList<DiagnoLabTestDataBean> labTestList = new ArrayList<DiagnoLabTestDataBean>();
	ArrayList<DiagnoLabPackageDataBean> labPkgList = new ArrayList<DiagnoLabPackageDataBean>();
	ArrayList<DiagnoTestDataBean> testList = new ArrayList<DiagnoTestDataBean>();
	ArrayList<DiagnoPackagesDataBean> pkgList = new ArrayList<DiagnoPackagesDataBean>();
	float mrpTotal,mrp,okAmt,okAmtTotal,youSave;
	int youSavePerc;
	Toolbar toolBar;
	View bottomBarLayout;
	boolean is_rec_pk_avail=false,isFromSummary=false;
	DiagnoOrderType orderType;
	TextView no_data,txt_labs_available;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagno_lab_available);
		ack = this;
		findViews();
		setSupportActionBar(toolBar);
		UIUtils.setBackClick(toolBar, ack);
		Utilities.setTitleText(toolBar, "Diagnostic Test [2/5]");
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		handleMapping(bottomBarLayout);
		linearManager = new LinearLayoutManager(ack);
		rcview.setLayoutManager(linearManager);
		diagnoTestList = new ArrayList<DiagnoTestDataBean>();
		diagnoPkgList = new ArrayList<DiagnoPackagesDataBean>();
		orderType = Okler.getInstance().getDiagnoOrder().getOrderType();
		isFromSummary = getIntent().getBooleanExtra("isFromSummary", false);
		if(orderType.equals(DiagnoOrderType.TEST)){
			txt_labs_available.setText("LABS AVAILABLE FOR ALL SELECTED TESTS");
			if(Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests()!=null)	
				diagnoTestList =  Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests();
			for (int i = 0; i < diagnoTestList.size(); i++) {
				if(i==0){
					testIds = diagnoTestList.get(i).getTestId()+"";
				}
				else{
					testIds = testIds + "," + diagnoTestList.get(i).getTestId();
				}
			}
			if(isFromSummary){
				labId = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentLab().getLabId();
			}
	 		getLabsUrl = getString(R.string.getLabsTest1)+testIds+getString(R.string.getLabsTest2)+labId+getString(R.string.getLabsTest3)+cityId;
		}
		if(orderType.equals(DiagnoOrderType.PACKAGE)){
			txt_labs_available.setText("LABS AVAILABLE FOR ALL SELECTED PACKAGES");
			if(Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs()!=null)	
				diagnoPkgList = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs();
			for(int i = 0;i<diagnoPkgList.size();i++){
				if(i==0){
					pkgIds = diagnoPkgList.get(i).getPacakageId()+"";
				}else{
					pkgIds = pkgIds + "," + diagnoPkgList.get(i).getPacakageId();
				}
			}
			if(isFromSummary){
				labId = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentLab().getLabId();
			}
			getLabsUrl = getString(R.string.getLabsForPackage1)+pkgIds+getString(R.string.getLabsTest2)+labId+getString(R.string.getLabsTest3)+cityId;
		}
		WebJsonObjectRequest getLabsJson = new WebJsonObjectRequest(Method.GET, getLabsUrl, new JSONObject(), this, this);
		VolleyRequest.addJsonObjectRequest(ack, getLabsJson);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void findViews(){
		rcview = (RecyclerView)findViewById(R.id.rcView);
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		no_data = (TextView)findViewById(R.id.no_data);
		bottomBarLayout = findViewById(R.id.bottombar);
		txt_labs_available = (TextView)findViewById(R.id.txt_labs_available);
	}


	@Override
	public void onErrorResponse(VolleyError error) {
		Log.e("Error", error.toString());
	}


	@Override
	public void onResponse(JSONObject response) {
		Log.e("Response", response.toString());
		JSONObject resp = (JSONObject)response;
		processResponse(resp);
	}
	
	public void processResponse(JSONObject resp){
		JSONArray result = resp.optJSONArray("result");
		DiagnoLabTestDataBean dLabTBean = new DiagnoLabTestDataBean();
		DiagnoLabPackageDataBean dLabPBean = new DiagnoLabPackageDataBean();
		 
		DiagnoLabBranchDataBean branchBean = new DiagnoLabBranchDataBean();
		JSONArray labArray;
		if(result!=null){
			no_data.setVisibility(View.GONE);
			rcview.setVisibility(View.VISIBLE);
			labTestList = new ArrayList<DiagnoLabTestDataBean>();
			labPkgList = new ArrayList<DiagnoLabPackageDataBean>();
		for (int i = 0; i < result.length(); i++) {
			dLabTBean = new DiagnoLabTestDataBean();
			dLabPBean = new DiagnoLabPackageDataBean();
			labArray = new JSONArray();
			labArray = result.optJSONArray(i);
			
		for (int j = 0; j < labArray.length(); j++) {
			branchBean = new DiagnoLabBranchDataBean();
			JSONObject job1  = labArray.optJSONObject(0);
			branchBean.setLabId(Integer.parseInt(job1.optString("lab_id")));
			branchBean.setCityId(Integer.parseInt(job1.optString("lab_city_id")));
			branchBean.setLab_name(job1.optString("lab_name"));
			branchBean.setStateId(Integer.parseInt(job1.optString("state")));
			branchBean.setLab_logo_image_path(job1.optString("lab_image"));
			branchBean.setAddr1(job1.optString("lab_address"));
			branchBean.setLab_logo_image_url(resp.optString("lab_image_url"));
			branchBean.setCity(job1.optString("lab_city_name"));
			branchBean.setState(job1.optString("lab_state_name"));
			JSONArray awardArray = job1.optJSONArray("accreditation_list");
			String award = "" ;
			//= "\""+awardArray+"\"";
			for (int l = 0; l < awardArray.length(); l++) {
				if(l == 0)
					award=awardArray.optString(l);
				else
				award = award+"    "+awardArray.optString(l);
			}
			if(award== null)
				award="";
			branchBean.setLab_award(award);
		}
		dLabTBean.setCurrentLab(branchBean);
		dLabPBean.setCurrentLab(branchBean);
		testList = new  ArrayList<DiagnoTestDataBean>();
		pkgList = new ArrayList<DiagnoPackagesDataBean>();
		DiagnoTestDataBean dBean = new DiagnoTestDataBean();
		DiagnoPackagesDataBean dpBean = new DiagnoPackagesDataBean(); 
		mrpTotal = 0.0f;
		okAmtTotal = 0.0f;
		youSavePerc=0;
		is_rec_pk_avail=false;
		for (int j = 0; j < labArray.length(); j++) {
			JSONObject job1  = labArray.optJSONObject(j);
			if(orderType.equals(DiagnoOrderType.TEST)){
					dBean = new DiagnoTestDataBean();
					dBean.setTestId(Integer.parseInt(job1.optString("entity_id")));
					dBean.setTestname(job1.optString("OklerTestName"));
					mrp = Float.parseFloat(job1.optString("mrp"));
					dBean.setMarketPrice(mrp);
					dBean.setYouSave(Integer.parseInt(job1.optString("okler_discount")));
					okAmt = Float.parseFloat(job1.optString("okler_amount"));
					dBean.setOklerTestPrice(okAmt);
					dBean.setReportTime(job1.optString("results_time"));
					String fasting_req = job1.optString("fastingRequired");
					if(fasting_req.equals("1"))
						dBean.setFastingRequired(true);
					else
						dBean.setFastingRequired(false);	
					String home_pickup = job1.optString("home_pickup_appicable");
					if(home_pickup.equals("1"))
						dBean.setHome_pickup_applicable(true);
					else
						dBean.setHome_pickup_applicable(false);
					if(!is_rec_pk_avail){
						is_rec_pk_avail = job1.optBoolean("recommended_package_isavailable");
					}
					testList.add(dBean);
			}
			if(orderType.equals(DiagnoOrderType.PACKAGE)){
				int count = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getPkgCount();
				for(int pkgIndex = 0 ;pkgIndex < count ; pkgIndex ++)
				{
					if(Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().get(pkgIndex).getPacakageId() == Integer.parseInt(job1.optString("entity_id")))
					{
						dpBean = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs().get(pkgIndex);
						mrp = Float.parseFloat(job1.optString("mrp"));
						dpBean.setPackMrp(mrp);
						dpBean.setPackYouSave(Integer.parseInt(job1.optString("okler_discount")));
						okAmt = Float.parseFloat(job1.optString("okler_amount"));
						dpBean.setPackOklerPrice(okAmt);
						dpBean.setReportTime(job1.optString("results_time"));
						String fasting_req = job1.optString("fastingRequired");
						if(fasting_req.equals("1"))
							dpBean.setFastingRequired(true);
						else
							dpBean.setFastingRequired(false);	
						String home_pickup = job1.optString("home_pickup_appicable");
						if(home_pickup.equals("1"))
							dpBean.setHome_pickup_applicable(true);
						else
							dpBean.setHome_pickup_applicable(false);
					}
				}
				pkgList.add(dpBean);
			}
			mrpTotal = mrpTotal + mrp;
			okAmtTotal = okAmtTotal + okAmt;
		}
		youSave=0.0f;
		youSave=mrpTotal-okAmtTotal;
		youSavePerc = (int)((youSave/mrpTotal)*100);
		if(orderType.equals(DiagnoOrderType.TEST)){
			dLabTBean.setCurrentTests(testList);
			dLabTBean.getCurrentLab().set_recommended_package_available(is_rec_pk_avail);
			dLabTBean.setMrp(mrpTotal);
			dLabTBean.setOkler_price(okAmtTotal);
			dLabTBean.setYouSave(youSavePerc);
			dLabTBean.setYouSaveRs(youSave);
			labTestList.add(dLabTBean);
			labPkgList.clear();
		}
		if(orderType.equals(DiagnoOrderType.PACKAGE)){
			dLabPBean.setCurrentPkgs(pkgList);
			dLabPBean.setMrp(mrpTotal);
			dLabPBean.setOkler_price(okAmtTotal);
			dLabPBean.setYouSave(youSavePerc);
			dLabPBean.setYouSaveRs(youSave);
			labPkgList.add(dLabPBean);
			labTestList.clear();
		}
		}
		dLabAdap = new DiagnoLabsAdapter(ack,labTestList,labPkgList,orderType,isFromSummary);
		rcview.setAdapter(dLabAdap);
		}
		else{
			no_data.setVisibility(View.VISIBLE);
			rcview.setVisibility(View.GONE);
		}
	}
}
