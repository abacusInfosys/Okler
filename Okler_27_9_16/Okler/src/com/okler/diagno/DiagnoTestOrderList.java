package com.okler.diagno;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.okler.adapters.DiagnoTestsInPackageAdapter;
import com.okler.android.BaseActivity;
import com.okler.android.NewSignIn;
import com.okler.customviews.CustomViewTests;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.databeans.diagnobean.DiagnoLabPackageDataBean;
import com.okler.databeans.diagnobean.DiagnoLabTestDataBean;
import com.okler.databeans.diagnobean.DiagnoPackagesDataBean;
import com.okler.databeans.diagnobean.DiagnoTestDataBean;
import com.okler.diagnostics.EnterPatientInfoActivity;
import com.okler.diagnostics.SelectPatientInfoActivity;
import com.okler.enums.DiagnoOrderType;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;

public class DiagnoTestOrderList extends BaseActivity{
	
	Toolbar toolBar;
	View bottomBarLayout,view1;
	String base64string,test_id, test_name, report_time,testIds,getRecPkgUrl;
	int uid,testCount, id, packageCount;
	Activity ack;
	TextView text_add_test,txt_discount,text_fasting, txt_next,txt_mrp_value, 
	txt_discvalue, txt_youpayvalue,txtcredetion, txt_labs_available,text_name, text_report_time, 
	text_pricevalue, txtlabname,txt_testheading;
	LinearLayout layout,separator;
	DiagnoTestDataBean test;
	DiagnoPackagesDataBean sel_package;
	boolean isCalled,isFromTest=false;
	ImageView deleteImage, deletePackageImage, rightArrowImage, toolbar_back;
	NetworkImageView lab_photo;
	ArrayList<DiagnoTestDataBean> selectedTest = new ArrayList<DiagnoTestDataBean>();
	ArrayList<DiagnoPackagesDataBean> selectedPackages = new ArrayList<DiagnoPackagesDataBean>();
	DiagnoLabTestDataBean diagnoLabTest;
	DiagnoLabPackageDataBean diagnoLPDataBean;
	DiagnoOrderType orderType;
	ImageLoader imgloader;
	RelativeLayout back_layout,rightarrowlayout;
	DiagnoTestsInPackageAdapter diagnoTestInPkgAdp;
	ArrayList<DiagnoTestDataBean> tests;
	RelativeLayout testsInPkgs;
	RecyclerView testInPackagesGrdView;
	float mrp=0.0f, youSave=0.0f, netPay=0.0f;
	int youSavePerc;
	Button btn_next;
	ArrayList<AddressDataBean> PaList;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.test_list_activity);
		layout = (LinearLayout) findViewById(R.id.layout_tests);
		text_fasting = (TextView) findViewById(R.id.txt_fasting);
		ack = this;
		isFromTest = getIntent().getBooleanExtra("isFromTest", false);
		orderType = Okler.getInstance().getDiagnoOrder().getOrderType();
		tests = new ArrayList<DiagnoTestDataBean>();
		diagnoTestInPkgAdp = new DiagnoTestsInPackageAdapter(tests);
		txt_mrp_value = (TextView) findViewById(R.id.text_mrp_value);
		txt_discvalue = (TextView) findViewById(R.id.txt_dicvalue);
		txt_youpayvalue = (TextView) findViewById(R.id.txt_youpayvalue);
		txt_labs_available = (TextView) findViewById(R.id.txt_labs_available);
		text_add_test = (TextView) findViewById(R.id.txt_add_more_tests);
		txtlabname = (TextView) findViewById(R.id.txtlabname);
		txtcredetion = (TextView) findViewById(R.id.txtcredetion);
		txt_discount = (TextView)findViewById(R.id.txt_discount);
		bottomBarLayout = findViewById(R.id.bottombar);
		txt_testheading = (TextView)findViewById(R.id.txt_testheading);
		lab_photo = (NetworkImageView)findViewById(R.id.lab_photo);
		handleMapping(bottomBarLayout);

		toolBar = (Toolbar) findViewById(R.id.toolbar);
		back_layout = (RelativeLayout)toolBar.findViewById(R.id.back_layout);
		toolbar_back = (ImageView)toolBar.findViewById(R.id.toolbar_back);
		setSupportActionBar(toolBar);
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		back_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
				
			}
		});
		
		toolbar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
				
			}
		});
		Utilities.setTitleText(toolBar, "Diagnostic Test[2/5]");
		
		btn_next = (Button) findViewById(R.id.txt_next);
		
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(netPay>0){
				if (Utilities.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN
						|| (Utilities.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_FB)
						|| (Utilities.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
					UsersDataBean ubean = Utilities
							.getCurrentUserFromSharedPref(ack);
					ArrayList<AddressDataBean> paList = new ArrayList<AddressDataBean>();
					paList = ubean.getPatAddList();
					paList.trimToSize();
					if (paList.size() <= 0)
						getPatAdresses();
					else
						isCalled = true;
					if (isCalled) {
						if (paList.size() <= 0) {
							Intent intent = new Intent(
									getApplicationContext(),
									EnterPatientInfoActivity.class);
							startActivity(intent);
						} else {
							Intent intent = new Intent(
									getApplicationContext(),
									SelectPatientInfoActivity.class);
							startActivity(intent);
						}
					}
				} else {
					Intent in = new Intent(ack, NewSignIn.class);
					ack.startActivity(in);
				}
				}else{
					Toast.makeText(ack, "Please add atleast one test/package", Toast.LENGTH_SHORT).show();
				}
			}
		});
			setTestPkgUi();

		
			
		
		text_add_test.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectedTest = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests();
				selectedTest.trimToSize();
				Intent in = new Intent(DiagnoTestOrderList.this, DiagnoTestPkgHome.class);
				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				in.putExtra("isTestsPkgsByLabs", true);
				startActivity(in);
				finish();
				
			}
		});
		
		for(int i =0;i<testCount;i++)
		{
			test = selectedTest.get(i);
			if(test.isFastingRequired() == true)
			{
				text_fasting.setVisibility(View.VISIBLE);
				break;
			}
		}		
	}
	
	private void setTestPkgUi(){
		if(orderType.equals(DiagnoOrderType.PACKAGE))
		{
			txt_labs_available.setText("LABS AVAILABLE FOR ALL SELECTED PACKAGES");
			text_add_test.setText("ADD MORE PACKAGES");
			txt_testheading.setText("PACKAGE NAME");
			selectedPackages = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs();
			selectedPackages.trimToSize();
			packageCount = selectedPackages.size();
			setUIPackage();
		}
		
		if(orderType.equals(DiagnoOrderType.TEST))
		{
			txt_labs_available.setText("LABS AVAILABLE FOR ALL SELECTED TESTS");
			text_add_test.setText("ADD MORE TESTS");
			txt_testheading.setText("TEST NAME");
			selectedTest = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests();
			selectedTest.trimToSize();
			testCount = selectedTest.size();
			setUITests();
		}
	}
	private void setUITests()
	{
			layout.removeAllViews();
			testCount = selectedTest.size();
			CustomViewTests [] view1 = new CustomViewTests[testCount];
			for(int i = 0 ; i<testCount ; i++)
			{
				view1[i]=new CustomViewTests(ack);
				test = selectedTest.get(i);
				view1[i].setId(test.getTestId());
				text_name = (TextView) view1[i].findViewById(R.id.txt_testname);
				text_report_time = (TextView) view1[i].findViewById(R.id.txt_report_time);
				text_pricevalue = (TextView) view1[i].findViewById(R.id.txt_pricevalue);
				text_report_time = (TextView) view1[i].findViewById(R.id.txt_report_time);
				deleteImage = (ImageView) view1[i].findViewById(R.id.img_delete);
				deletePackageImage = (ImageView) view1[i].findViewById(R.id.img_delete_pkg);
				deletePackageImage.setVisibility(View.GONE);
				rightArrowImage = (ImageView) view1[i].findViewById(R.id.img_right_arrow_pkg);
				rightArrowImage.setVisibility(View.GONE);
				deleteImage.setId(test.getTestId());
				deleteImage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final int viewid = v.getId();
						final AlertDialog alertDialog = new AlertDialog.Builder(ack).create();
				alertDialog.setTitle("Alert");
				alertDialog.setMessage("Are you sure, you want to delete the selected test");
				alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
								for(int i=0; i<selectedTest.size(); i++)
								{
									if(viewid == selectedTest.get(i).getTestId())
									{
										selectedTest.remove(i);
										setUITests();
									}
								}
								Okler.getInstance().getDiagnoOrder().getLabTestDataBean().setCurrentTests(selectedTest);
								Toast.makeText(ack, "Test deleted successfully", Toast.LENGTH_SHORT).show();
								mrp=0.0f;
								youSave=0.0f;
								youSavePerc=0;
								
								calculateTestPrice();
							}
						});

				alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});

				alertDialog.show();
					}
				});	
						text_name.setText(test.getTestname());
						
						text_pricevalue.setText("Rs."+test.getOklerTestPrice());
						text_report_time.setText("Report time:" +test.getReportTime());
						layout.addView(view1[i]);
						
						
			}	
			
			DiagnoLabTestDataBean diagnoLTDataBean = new DiagnoLabTestDataBean();
			diagnoLTDataBean = Okler.getInstance().getDiagnoOrder().getLabTestDataBean();
			imgloader = VolleyRequest.getInstance(getApplicationContext()).getImageLoader();
			String part1 = diagnoLTDataBean.getCurrentLab().getLab_logo_image_url();
			String part2 = diagnoLTDataBean.getCurrentLab().getLab_logo_image_path();
			lab_photo.setImageUrl(part1+part2, imgloader);
			txtlabname.setText(""+diagnoLTDataBean.getCurrentLab().getLab_name());
			txtcredetion.setText(""+diagnoLTDataBean.getCurrentLab().getLab_award());
			txt_mrp_value.setText("Rs."+diagnoLTDataBean.getMrp());
			txt_discount.setText(txt_discount.getText().toString()+diagnoLTDataBean.getYouSave()+"%)");
			txt_discvalue.setText("Rs."+diagnoLTDataBean.getYouSaveRs());
			netPay = diagnoLTDataBean.getOkler_price();
			txt_youpayvalue.setText("Rs."+diagnoLTDataBean.getOkler_price());
	}
	
	public void getPatAdresses() {
		final UsersDataBean ubean = Utilities 
				.getCurrentUserFromSharedPref(ack);
		int userId = ubean.getId();

		String pAddrUrl = getString(R.string.GetPatientsAllAddr) + userId;
		WebJsonObjectRequest pjson = new WebJsonObjectRequest(Method.GET,
				pAddrUrl, new JSONObject(),
				new Response.Listener<JSONObject>() {

					

					@Override
					public void onResponse(JSONObject response) {
						
						try {
							AddressDataBean adbean = new AddressDataBean();
							JSONObject jobj = new JSONObject();
							JSONObject jobj2;
							PaList = new ArrayList<AddressDataBean>();
							JSONArray jarr = response.getJSONArray("result");
							int length = jarr.length();
							PaList = new ArrayList<AddressDataBean>();
							for (int i = 0; i < length; i++) {
								jobj = jarr.optJSONObject(i);
								adbean = new AddressDataBean();
								adbean.setPat_id(Integer.parseInt(jobj
										.optString("pat_id")));
								adbean.setFirstname(jobj.optString("firstname"));
								adbean.setLastname(jobj.optString("middlename"));
								adbean.setDob(jobj.optString("dob"));
								adbean.setRelationId(jobj
										.optString("relationid"));
								adbean.setGenderId(jobj.optString("gender"));
								adbean.setPhone(jobj.optString("mobileno"));
								jobj2 = new JSONObject();
								String j = jobj
										.optString("residential_address");
								if ((j.equals("null")) || (j.equals("false"))) {
											continue;
								} else {
									jobj2 = jobj
											.optJSONObject("residential_address");
									adbean.setAddress1(jobj2.optString("addr1"));
									adbean.setAddress2(jobj2.optString("addr2"));
									adbean.setLandmark(jobj2
											.optString("land_mark"));
									adbean.setZip(jobj2.optString("pincode"));
									adbean.setCity_id(jobj2
											.optString("city_id"));
									adbean.setCity(jobj2.optString("city_name"));
									adbean.setGender(jobj
											.optString("userGender"));
									adbean.setRelation(jobj
											.optString("relation_name"));
								}
								PaList.add(adbean);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
						PaList.trimToSize();
						ubean.setPatAddList(PaList);
						Utilities.writeCurrentUserToSharedPref(ack, ubean);
						isCalled = true;

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("ERROR", "" + error.getStackTrace());
						isCalled = true;
					}
				});
		VolleyRequest.addJsonObjectRequest(ack, pjson);
	}
	
	public void setUIPackage()
	{
		layout.removeAllViews();
		packageCount = selectedPackages.size();
		CustomViewTests [] view1 = new CustomViewTests[packageCount];
		for(int i = 0 ; i<packageCount ; i++)
		{
			view1[i]=new CustomViewTests(ack);
			sel_package = selectedPackages.get(i);
			view1[i].setId(sel_package.getPacakageId());
			text_name = (TextView) view1[i].findViewById(R.id.txt_testname);
			text_report_time = (TextView) view1[i].findViewById(R.id.txt_report_time);
			text_pricevalue = (TextView) view1[i].findViewById(R.id.txt_pricevalue);
			text_report_time = (TextView) view1[i].findViewById(R.id.txt_report_time);
			deleteImage = (ImageView) view1[i].findViewById(R.id.img_delete);
			deleteImage.setVisibility(View.GONE);
			deletePackageImage = (ImageView) view1[i].findViewById(R.id.img_delete_pkg);
			deletePackageImage.setVisibility(View.VISIBLE);
			rightArrowImage = (ImageView) view1[i].findViewById(R.id.img_right_arrow_pkg);
			rightArrowImage.setVisibility(View.VISIBLE);
			rightarrowlayout = (RelativeLayout)view1[i].findViewById(R.id.rightarrowlayout);
			testInPackagesGrdView = (RecyclerView)view1[i].findViewById(R.id.testInPackagesGrdView);
			GridLayoutManager grdLManager = new GridLayoutManager(ack,2);
			testInPackagesGrdView.setLayoutManager(grdLManager);
			deletePackageImage.setId(sel_package.getPacakageId());
			rightarrowlayout.setId(i+100);
			testInPackagesGrdView.setId(i+10000);
			testInPackagesGrdView.setAdapter(diagnoTestInPkgAdp);
			deletePackageImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final int viewid = v.getId();
					final AlertDialog alertDialog = new AlertDialog.Builder(ack).create();
			alertDialog.setTitle("Alert");
			alertDialog.setMessage("Are you sure, you want to delete the selected package");
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							
							for(int i=0; i<selectedPackages.size(); i++)
							{
								if(viewid == selectedPackages.get(i).getPacakageId())
								{
									selectedPackages.remove(i);
									setUIPackage();
								}
							}
							Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().setCurrentPkgs(selectedPackages);
							Toast.makeText(ack, "Package deleted successfully", Toast.LENGTH_SHORT).show();
							mrp=0.0f;
							youSave=0.0f;
							youSavePerc=0;
							
							calculatePackagePrice();
						}
					});

			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			alertDialog.show();
				}
			});	
		rightarrowlayout.setTag(R.string.isArrowClicked, "NotClicked");
		rightArrowImage.setBackgroundResource(R.drawable.right_arrow_pkg);
		rightarrowlayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				showTestsInPkgs(v);
			}});
					text_name.setText(sel_package.getPackageName());
					text_pricevalue.setText("Rs."+sel_package.getPackOklerPrice());
					text_report_time.setText("Report time:" +sel_package.getReportTime());
					layout.addView(view1[i]);
		}	
		diagnoLPDataBean = new DiagnoLabPackageDataBean();
		diagnoLPDataBean = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean();
		imgloader = VolleyRequest.getInstance(getApplicationContext()).getImageLoader();
		String part1 = diagnoLPDataBean.getCurrentLab().getLab_logo_image_url();
		String part2 = diagnoLPDataBean.getCurrentLab().getLab_logo_image_path();
		lab_photo.setImageUrl(part1+part2, imgloader);
		txtlabname.setText(""+diagnoLPDataBean.getCurrentLab().getLab_name());
		txtcredetion.setText(""+diagnoLPDataBean.getCurrentLab().getLab_award());
		txt_mrp_value.setText("Rs."+diagnoLPDataBean.getMrp());
		txt_discount.setText(txt_discount.getText().toString()+diagnoLPDataBean.getYouSave()+"%)");
		txt_discvalue.setText("Rs."+diagnoLPDataBean.getYouSaveRs());
		netPay = diagnoLPDataBean.getOkler_price();
		txt_youpayvalue.setText("Rs."+diagnoLPDataBean.getOkler_price());
	}
		
		private void showTestsInPkgs(View v)
		{
			String tag = v.getTag(R.string.isArrowClicked).toString();
			int clickedViewId = v.getId();
			View customView =  layout.findViewById(selectedPackages.get(clickedViewId-100).getPacakageId());
			ImageView arr ;
			RelativeLayout rl = (RelativeLayout)v;
			View v2 = rl.getChildAt(0);
			arr = (ImageView)v2;
			RelativeLayout relLL = (RelativeLayout) customView.findViewById(R.id.testsInPkgs);
			int a =(clickedViewId-100)+10000;
			android.support.v7.widget.RecyclerView clickedGrdView =(android.support.v7.widget.RecyclerView) relLL.findViewById(a);
			if(tag.equals("NotClicked"))
			{
				relLL.setVisibility(View.VISIBLE);
				arr.setBackgroundResource(R.drawable.down_arrow_pkg);
				tests = selectedPackages.get(clickedViewId-100).getTestList();
				tests.trimToSize();
				if(tests.size() == 0)
				{
					Toast.makeText(ack, "Some error occurred", Toast.LENGTH_SHORT).show();
					return;
				}
				
				diagnoTestInPkgAdp = new DiagnoTestsInPackageAdapter(tests);
				clickedGrdView.setAdapter(diagnoTestInPkgAdp);
				v.setTag(R.string.isArrowClicked,"Clicked");
			}
			else if(tag.equals("Clicked"))
			{
				arr.setBackgroundResource(R.drawable.right_arrow_pkg);
				v.setTag(R.string.isArrowClicked,"NotClicked");
				relLL.setVisibility(View.GONE);
			}
		}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(isFromTest){
		Okler.getInstance().getDiagnoOrder().setOrderType(DiagnoOrderType.TEST);
		}
	}
	
	public void calculateTestPrice()
	{
		mrp=0.0f;
		youSave=0.0f;
		netPay=0.0f;
		ArrayList<DiagnoTestDataBean> dpBean = new ArrayList<DiagnoTestDataBean>();
		dpBean = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests();
		
		if(dpBean.size() == 0)
		{
			txt_mrp_value.setText(""+0.0);
			txt_discvalue.setText(""+0.0f);
			txt_youpayvalue.setText(""+0.0f);
		}
		
		for(int i=0; i<dpBean.size();i++)
		{
			 mrp = mrp+dpBean.get(i).getMarketPrice();
			 youSave = dpBean.get(i).getMarketPrice() - dpBean.get(i).getOklerTestPrice();
			 netPay = netPay +dpBean.get(i).getOklerTestPrice();
		}
		txt_discvalue.setText(""+youSave);
		 txt_mrp_value.setText(""+mrp);
		 txt_youpayvalue.setText(""+netPay);
		youSavePerc = (int)((youSave/mrp)*100);
		txt_discount.setText("Okler Discount("+"-"+youSavePerc+"%)");
	}
	
	public void calculatePackagePrice()
	{
		mrp=0.0f;
		youSave=0.0f;
		netPay=0.0f;
		ArrayList<DiagnoPackagesDataBean> dpBean = new ArrayList<DiagnoPackagesDataBean>();
		dpBean = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentPkgs();
		
		if(dpBean.size() == 0)
		{
			txt_mrp_value.setText(""+0.0);
			txt_discvalue.setText(""+0.0f);
			txt_youpayvalue.setText(""+0.0f);
		}
		
		for(int i=0; i<dpBean.size();i++)
		{
			 mrp = mrp+dpBean.get(i).getPackMrp();
			 youSave = dpBean.get(i).getPackMrp() - dpBean.get(i).getPackOklerPrice();
			 netPay = netPay +dpBean.get(i).getPackOklerPrice();
			 
		}
		txt_discvalue.setText(""+youSave);
		 txt_mrp_value.setText(""+mrp);
		 txt_youpayvalue.setText(""+netPay);
		youSavePerc = (int)((youSave/mrp)*100);
		txt_discount.setText("Okler Discount("+"-"+youSavePerc+"%)");
	}
}

