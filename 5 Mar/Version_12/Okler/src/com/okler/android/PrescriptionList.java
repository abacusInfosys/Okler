package com.okler.android;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageRequest;
import com.okler.adapters.MedPrescListAdapter;
import com.okler.adapters.PrescriptionListAdapter;
import com.okler.databeans.CategoriesDataBean;
import com.okler.databeans.PrescriptionImagesDataBean;
import com.okler.databeans.PrescriptionsDataBean;
import com.okler.dialogs.ConfirmPrescDialog;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PrescriptionList extends BaseActivity implements OnScrollListener {
	
    public static int [] prgmImages={R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera};    

	String prescriptionURL;
	View bottomBarLayout;
	Toolbar toolBar;
	static LinearLayout footer1, footer2,medTitle,diagnoTitle;
	TextView btn_medicines, btn_dignostics;
	ListView listview1;	
	ImageView imgBack;
	String getPresc;
	LinearLayout tabbar_layout;
	Button conti,notifcount;
	int check;
	boolean  flag = false; 
	ArrayList<PrescriptionsDataBean> prescHistory, medPrescHistory,diagnoPrescHistory;
	PrescriptionsDataBean currPresc;
	MedPrescListAdapter adap;
	boolean isMedPres;
	LinearLayout progressLinLayout;
	PrescriptionListAdapter prescLstAdp;
	int pageNumber=0,totalResultsFromWebService=0;
	PrescriptionList pct;
	boolean isParentMyAccount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_prescription_list);
		check = getIntent().getIntExtra("Check", 11);
		isParentMyAccount = getIntent().getBooleanExtra("isParentMyAccount", false);
		footer1 = (LinearLayout) findViewById(R.id.layout_footer1);
		footer2 = (LinearLayout) findViewById(R.id.layout_footer2);
		diagnoTitle = (LinearLayout) findViewById(R.id.diagnoTitle);
		medTitle = (LinearLayout)findViewById(R.id.medTitle);
		btn_medicines = (TextView) findViewById(R.id.medicines_button);
		btn_dignostics = (TextView) findViewById(R.id.dignostics_button);
		tabbar_layout = (LinearLayout)findViewById(R.id.tabbar_layout);
		conti = (Button)findViewById(R.id.conti);
		pct = this;
		isMedPres = getIntent().getBooleanExtra("isMedPres", true);
		// 
		//if(UploadPrescription.changetab == false)
		if(isMedPres)
		{
			footer1.setVisibility(View.VISIBLE);
			footer2.setVisibility(View.INVISIBLE);
		}
		else
		//if(UploadPrescription.changetab == true)
		{
			footer1.setVisibility(View.INVISIBLE);
			footer2.setVisibility(View.VISIBLE);
		}
		
		listview1 = (ListView) findViewById(R.id.imagesPrescHistory);
		progressLinLayout = (LinearLayout)findViewById(R.id.progressLinLayout);
		
		conti.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Okler.getInstance().getBookingType()==0){
					Intent intent = new Intent(PrescriptionList.this,ProductCheckoutSummary.class);
					intent.putExtra("Check", check);
					startActivity(intent);
				}else{
					Intent intent = new Intent(PrescriptionList.this,UploadPrescription.class);
					startActivity(intent);
				}
			}
		});
		
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		notifcount = (Button)toolBar.findViewById(R.id.notif_count);
		notifcount.setVisibility(View.GONE);
		setSupportActionBar(toolBar);
		final ActionBar ab=getSupportActionBar();
		//ab.setDisplayHomeAsUpEnabled(true);
			toolBar.setBackgroundResource(R.drawable.custom_view_grad_upload_pesc);
			
			if(Okler.getInstance().getBookingType()==0){
				toolBar.setBackgroundResource(R.drawable.custom_view_grad_medicine);
				tabbar_layout.setBackgroundColor(getResources().getColor(R.color.YellowGreen));
				conti.setText("CONTINUE");
				flag = true;
				footer1.setBackgroundColor(getResources().getColor(R.color.Black));
				footer2.setBackgroundColor(getResources().getColor(R.color.Black));
		}
	if(Okler.getInstance().getBookingType()==9){
		tabbar_layout.setBackgroundColor(Color.parseColor("#c054ca"));
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		//btn_medicines.setBackgroundColor(Color.parseColor("#c054ca"));
			//btn_dignostics.setBackgroundColor(Color.parseColor("#c054ca"));
			footer1.setBackgroundColor(Color.parseColor("#562E86"));
			footer2.setBackgroundColor(Color.parseColor("#562E86"));
			}		
		
		
		
		
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isParentMyAccount)
				{
					Intent intent = new Intent(getApplicationContext(), UploadPrescription.class);
					 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
				finish();	
			}
		});
	      Utilities.setTitleText(toolBar, "Upload Prescription");	
	      if(check == 0){
	    	toolBar.setBackgroundColor(Color.BLUE);
	    	Utilities.setTitleText(toolBar, "Cart");
	    	tabbar_layout.setBackgroundColor(getResources().getColor(R.color.Blue));
	    	footer1.setBackgroundColor(getResources().getColor(R.color.DarkBlue));
			footer2.setBackgroundColor(getResources().getColor(R.color.DarkBlue));
	      }
		btn_medicines.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				footer2.setVisibility(View.INVISIBLE);
				footer1.setVisibility(View.VISIBLE);
	//			pageNumber=0;
				prescLstAdp = new PrescriptionListAdapter(pct, medPrescHistory);
				listview1.setAdapter(prescLstAdp);
				//listview1.setOnScrollListener(null);
				setListView(false);	
			}
		});
		btn_dignostics.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				footer1.setVisibility(View.INVISIBLE);
				footer2.setVisibility(View.VISIBLE);	
				showProgress(true);
			//	pageNumber=0;
				prescLstAdp = new PrescriptionListAdapter(pct, diagnoPrescHistory);
				listview1.setAdapter(prescLstAdp);
			//	listview1.setOnScrollListener(null);
				setListView(true);
			}
		});
	/*	diagnoTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
			}
		});
		medTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
			}
		});*/
		
		getPresc = getString(R.string.get_prescriptions);
		getPresc = getPresc + Utilities.getCurrentUserFromSharedPref(PrescriptionList.this).getId();
		prescHistory = new ArrayList<PrescriptionsDataBean>();
		medPrescHistory = new ArrayList<PrescriptionsDataBean>();
		diagnoPrescHistory = new ArrayList<PrescriptionsDataBean>(); 
		if(isMedPres)
			prescLstAdp = new PrescriptionListAdapter(this, medPrescHistory);
		else
			prescLstAdp = new PrescriptionListAdapter(this, diagnoPrescHistory);
		listview1.setAdapter(prescLstAdp);
		currPresc = new PrescriptionsDataBean();
		//Get Prescriptions
		getPrescriptions(pageNumber);	
		listview1.setOnScrollListener(this);
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(Okler.getInstance().getBookingType()==0){
			notifcount.setVisibility(View.VISIBLE);
			UIUtils.setCartCount(notifcount, pct);
		}
	}
	public void getPrescriptions(int pageNum)
	{
		String prescURL = getPresc + getString(R.string.getdiseasePagePart)+pageNum;
		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, prescURL, new JSONObject(), 
				new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) 
			{
				try {
					showProgress(false);
					JSONObject resultsObj = response.getJSONObject("result");
					totalResultsFromWebService = resultsObj.getInt("total_count");
					prescriptionURL = resultsObj.getString("prescription_url");
					//prescriptionURL = prescriptionURL.replace("/", "");
					int length = resultsObj.length();
					for(int i = 0 ;i< length-2;i++)
					{
						currPresc = new PrescriptionsDataBean();
						JSONObject curPrescrJSon= resultsObj.getJSONObject(""+i);
						
						currPresc.setPresId(curPrescrJSon.getString("id"));
						currPresc.setDocName(curPrescrJSon.getString("doc_name"));
						currPresc.setDocSirname(curPrescrJSon.getString("doctorlname"));
						currPresc.setPatientName(curPrescrJSon.getString("patient_name"));
						currPresc.setPrescStatus(curPrescrJSon.getString("status"));
						
						String prescrImages = curPrescrJSon.getString("prescription_name");
						String str = prescrImages.substring(1,prescrImages.length()-1);
						if(str.equals("0"))
							continue;
						String[] imgs = str.split(",");
						ArrayList<PrescriptionImagesDataBean> prsArr = new ArrayList<PrescriptionImagesDataBean>();
						for(int imgCnt = 0 ; imgCnt < imgs.length;imgCnt++ )
						{
							PrescriptionImagesDataBean pImages = new PrescriptionImagesDataBean();
							String stt = imgs[imgCnt].substring(1,(imgs[imgCnt].length()-1));
							
							String strr = prescriptionURL+stt;
							pImages.setPresUrl(strr);
							prsArr.add(pImages);
						}
						currPresc.setPresImages(prsArr);
						if(curPrescrJSon.getString("service_type").equals("1"))
							diagnoPrescHistory.add(currPresc);							
						else
							medPrescHistory.add(currPresc);
						prescHistory.add(currPresc);
				//	Okler.getInstance().setCategoriesBean(categoriesList);
					}
					//Set UI
					if(!isMedPres)
						setListView(true);
					else
						setListView(false);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
				
					Log.e("Error", "er"+error.getMessage());
					showProgress(false);
				}		
			
		});
		if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson))
			showProgress(true);
		else
			showProgress(false);
	}
	
	 private void showProgress(boolean paramBoolean)
	  {
	    if (paramBoolean)
	    {
	      this.progressLinLayout.setVisibility(View.VISIBLE);
	      return;
	    }
	    this.progressLinLayout.setVisibility(View.INVISIBLE);
	  }
	 
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(!isParentMyAccount)
		{
			Intent intent = new Intent(getApplicationContext(), UploadPrescription.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		finish();	
	}
	
	public void setListView(boolean isDiagno)
	{
		if(flag){
			adap = new MedPrescListAdapter(this, medPrescHistory);
			listview1.setAdapter(adap);
			listview1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					PrescriptionsDataBean pbean = new PrescriptionsDataBean();
					pbean = (PrescriptionsDataBean)adap.getItem(position);
					String idd = pbean.getPresId();
					ConfirmPrescDialog cdlg = new ConfirmPrescDialog(getApplicationContext(), idd);
					cdlg.show(getFragmentManager(), "DO you want to select this Prescripion?");
				}
			});
		}else
		{
			prescLstAdp.notifyDataSetChanged();
			
			}
		showProgress(false);
		/*if(isDiagno)
			
			listview1.setAdapter(new PrescriptionListAdapter(this, diagnoPrescHistory));
		else
			listview1.setAdapter(new PrescriptionListAdapter(this, medPrescHistory));*/
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if(((totalResultsFromWebService/10)+1) == pageNumber)
			return;
		int lastItemInScreen=firstVisibleItem+visibleItemCount;
		int size;
		if(isMedPres)
			size = medPrescHistory.size();
		else
			size = diagnoPrescHistory.size();
		
		if((lastItemInScreen==totalItemCount)&&(totalResultsFromWebService!=size))
			{	pageNumber++;
				getPrescriptions(pageNumber);
			}
	 }

}
