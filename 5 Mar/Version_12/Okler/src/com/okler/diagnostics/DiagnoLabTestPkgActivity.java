package com.okler.diagnostics;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
//import com.android.volley.toolbox.NetworkImageView;
import com.okler.Profile.ProfileNewAddress;
import com.okler.android.BaseActivity;
import com.okler.android.NewSignIn;
import com.okleruser.R;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.DiagnoLabsDataBean;
import com.okler.databeans.DiagnoPackageDataBean;
import com.okler.databeans.TestDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DiagnoLabTestPkgActivity extends BaseActivity {

	Button schedule_pickBtn;
	ImageView imgBack;
	Toolbar toolBar;
	View bottomBarLayout;
	private String receivedLabDetail;
	private Boolean flag;
	ImageLoader imageLoader;
	ImageLoader imageLoader2;
	ImageLoader imageLoader3;
	DiagnoLabsDataBean receivedData;
	TestDataBean testBean;
	Context context;
	ScrollView scroll_view;
	ArrayList<TestDataBean> testBean1 = new ArrayList<TestDataBean>();
	Activity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagno_lab_test_pkg);
		
		RelativeLayout mainParentLayout=(RelativeLayout) findViewById(R.id.briefAbtLab);	  
		ImageLoader imgLoader;
		activity = this;
		
		context=this;
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		DiagnoLabsDataBean receivedData= new DiagnoLabsDataBean();
		 toolBar=(Toolbar) findViewById(R.id.toolbar);
		 receivedLabDetail=getIntent().getStringExtra("clickedItem");
		 String testid = getIntent().getStringExtra("testid");
		 String key = getIntent().getStringExtra("key");
		 flag=getIntent().getBooleanExtra("flag",true);
		 imageLoader=VolleyRequest.getInstance(context).getImageLoader();
		 DiagnoPackageDataBean pckdetails = new DiagnoPackageDataBean();
		 ImageView lab_log=(ImageView) findViewById(R.id.labLogo);
		 Log.i("json strng received",receivedLabDetail);
		 scroll_view=(ScrollView) findViewById(R.id.mainScrollview);
		 
		 JSONObject mainObject;
		try {
			Log.i("Where am I", "in json object");
			DiagnoPackageDataBean packageBean = new DiagnoPackageDataBean();
			testBean= new TestDataBean();
			mainObject = new JSONObject(receivedLabDetail);
			JSONArray testObject = new JSONArray();
			receivedData.setLabId(mainObject.getInt("labId"));
			receivedData.setLab_name(mainObject.optString("lab_name"));
			
			receivedData.setLabDescrption(mainObject.optString("labDescrption"));
			receivedData.setLab_Log_image(mainObject.optString("lab_Log_image"));
			receivedData.setLab_image_path(mainObject.optString("lab_image_path"));
			receivedData.setLab_award_image(mainObject.optString("lab_award_image"));
			receivedData.setLab_address(mainObject.optString("lab_address"));
			receivedData.setLab_city_id(mainObject.optString("lab_city"));
			receivedData.setLab_state(mainObject.optString("lab_state"));
			receivedData.setLab_pinCode(mainObject.optString("lab_pinCode"));
			receivedData.setLab_city_branch_id(mainObject.optString("lab_city_branch_id"));
			//receivedData.setLab_Log_image(mainObject.optString("lab_Log_image"));
			String path_url=mainObject.getString("lab_image_path");
			JSONArray j_lab_array=mainObject.getJSONArray("lab_gallery_image");
			if(j_lab_array.equals(null)){
				
				receivedData.setLab_gallery_image(null);
			}else{
			int length=j_lab_array.length();
			String[] arrayLabImage=new String[length];
			
			for(int i=0;i<length;i++){
				arrayLabImage[i]=path_url+j_lab_array.getString(i);
			}
			receivedData.setLab_gallery_image(arrayLabImage);
				}
			
			if ( (key != null )  )
			{
				
			
				
				if (key.equals("DiagnoPck") )
				{
					JSONObject packpack = (JSONObject) mainObject.getJSONObject("packageBean");
					pckdetails.setDescription(packpack.getString("description"));
					pckdetails.setPackage_name(packpack.getString("package_name"));
					pckdetails.setPack_mrp(Float.parseFloat(packpack.getString("pack_mrp")));
					pckdetails.setPack_oklerPrice(Float.parseFloat(packpack.getString("pack_oklerPrice")));
					pckdetails.setPack_you_save(Integer.parseInt(packpack.getString("pack_you_save")));
					
					testBean.setPckBean(pckdetails);
				}
				
				else
				{
			
				
					testObject = mainObject.getJSONArray("testBean");
					Log.i("Where am I size of test ",toString().valueOf(testObject.length()));
					
					for(int i=0;i<testObject.length();i++){
					
						JSONObject testwiseObj=testObject.getJSONObject(i);
						String testIdInArray = toString().valueOf(testwiseObj.optString("testId"));
						
						
						if (testIdInArray.equals(testid))
							{
							testBean.setTestname(testwiseObj.optString("testname"));
							testBean.setMarketPrice(Float.parseFloat(testwiseObj.optString("marketPrice")));
							
							testBean.setOklerTestPrice(Float.parseFloat(testwiseObj.optString("oklerTestPrice")));
							testBean.setYouSave(Float.parseFloat(testwiseObj.optString("youSave")));
							testBean.setDescription(testwiseObj.optString("description"));
							testBean.setMandatoryInfo(testwiseObj.optString("mandatoryInfo"));
							JSONObject packagedetails=testwiseObj.getJSONObject("pckBean");
							pckdetails.setDescription(packagedetails.getString("description"));
							pckdetails.setPackage_name(packagedetails.getString("package_name"));
							pckdetails.setPack_mrp(Float.parseFloat(packagedetails.getString("pack_mrp")));
							pckdetails.setPack_oklerPrice(Float.parseFloat(packagedetails.getString("pack_oklerPrice")));
							pckdetails.setPack_you_save(Integer.parseInt(packagedetails.getString("pack_you_save")));
						
							testBean.setPckBean(pckdetails);
							
							}
						
						}
				}
			}
				else
				{
				
					{
				
					
						testObject = mainObject.getJSONArray("testBean");
						Log.i("Where am I size of test ",toString().valueOf(testObject.length()));
						
						for(int i=0;i<testObject.length();i++){
						
							JSONObject testwiseObj=testObject.getJSONObject(i);
							String testIdInArray = toString().valueOf(testwiseObj.optString("testId"));
							
							
							if (testIdInArray.equals(testid))
								{
								testBean.setTestname(testwiseObj.optString("testname"));
								testBean.setMarketPrice(Float.parseFloat(testwiseObj.optString("marketPrice")));
								
								testBean.setOklerTestPrice(Float.parseFloat(testwiseObj.optString("oklerTestPrice")));
								testBean.setYouSave(Float.parseFloat(testwiseObj.optString("youSave")));
								testBean.setDescription(testwiseObj.optString("description"));
								testBean.setMandatoryInfo(testwiseObj.optString("mandatoryInfo"));
								JSONObject packagedetails=testwiseObj.getJSONObject("pckBean");
								pckdetails.setDescription(packagedetails.getString("description"));
								pckdetails.setPackage_name(packagedetails.getString("package_name"));
								pckdetails.setPack_mrp(Float.parseFloat(packagedetails.getString("pack_mrp")));
								pckdetails.setPack_oklerPrice(Float.parseFloat(packagedetails.getString("pack_oklerPrice")));
								pckdetails.setPack_you_save(Integer.parseInt(packagedetails.getString("pack_you_save")));
							
								testBean.setPckBean(pckdetails);
								
								}
							
							}
				}
		}	
			
			
			/**/
			}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
	//get logo image
			NetworkImageView logImage=(NetworkImageView) findViewById(R.id.labLogo);
    		imgLoader = VolleyRequest.getInstance(context).getImageLoader();
    		
    		String url=receivedData.getLab_Log_image();
    		
    		logImage.setImageUrl(url, imgLoader);
			
			//get Awards image
			NetworkImageView awardImage=(NetworkImageView) findViewById(R.id.accredition);
			imageLoader2 = VolleyRequest.getInstance(context).getImageLoader();
    		
    		 url=receivedData.getLab_award_image();
    		 awardImage.setImageUrl(url, imgLoader);
    		
    		//get gallery image
    		
//			NetworkImageView galleryImage=(NetworkImageView) findViewById(R.id.galley);
//			imageLoader3 = VolleyRequest.getInstance(context).getImageLoader();
    		
  //  		 url=receivedData.getLab_gallery_image();
   // 		galleryImage.setImageUrl(url, imgLoader);
    	HorizontalScrollView horiView=(HorizontalScrollView) findViewById(R.id.galleryHSScrView);
   	      setTouchListenerScrViews(horiView);
   	      
    		String[] galArray=receivedData.getLab_gallery_image();
    		if(galArray!=null){
    			if(galArray.length>0){
    			setgalleryImages(horiView,galArray);
    			}
    			
    		}else{
    			TextView noImage=(TextView) findViewById(R.id.noImagetv);
    			noImage.setVisibility(View.VISIBLE);
    			noImage.setText("Image not Available");
    		
    		}
			TextView testPkgName = (TextView)findViewById(R.id.testPkgName);
			
			
			TextView testPkgDesc = (TextView)findViewById(R.id.testPkgDesc);
			
			
			TextView mandateValue = (TextView)findViewById(R.id.mandateValue);
			
			
			TextView tv_MrpValue = (TextView)findViewById(R.id.tv_MrpValue);
			
			
			TextView okler_Amount = (TextView)findViewById(R.id.okler_Amount);
		
			TextView youSaveValue = (TextView)findViewById(R.id.tv_youSaveValue);
			
			 
				TextView info_abt_lab = (TextView)findViewById(R.id.info_abt_lab2);
				info_abt_lab.setText(receivedData.getLabDescrption());
				
				 if(flag){
	    			 testPkgName.setText(testBean.getTestname());
	    			 testPkgDesc.setText(Html.fromHtml(testBean.getDescription()));
	    			 mandateValue.setText(testBean.getMandatoryInfo());
	    			 tv_MrpValue.setText(toString().valueOf(testBean.getMarketPrice()));
	    				okler_Amount.setText(toString().valueOf(testBean.getOklerTestPrice()));
	    				youSaveValue.setText(toString().valueOf(testBean.getYouSave()));
	    		 }else{
	    			 testPkgName.setText(testBean.getPckBean().getPackage_name());
	    			 testPkgDesc.setText(Html.fromHtml(testBean.getPckBean().getDescription()));
	    			 //mandateValue.setText(testBean.getMandatoryInfo());
	    			 tv_MrpValue.setText(toString().valueOf(testBean.getPckBean().getPack_mrp()));
	    				okler_Amount.setText("Okler Price :"+toString().valueOf(testBean.getPckBean().getPack_oklerPrice()));
	    				youSaveValue.setText(toString().valueOf(testBean.getPckBean().getPack_you_save()));
	    		 }
				
				
				
			
		 
	
		 
		 
		 
		 
		 
		schedule_pickBtn = (Button)findViewById(R.id.schedule_pickBtn);
		schedule_pickBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN)
 				{
				UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(activity);
				ArrayList<AddressDataBean> paList = new ArrayList<AddressDataBean>();
				paList = ubean.getPatAddList();
				if(paList.size()<=0){
				Intent intent = new Intent(getApplicationContext(),EnterPatientInfoActivity.class);
				startActivity(intent);
				}else{
					Intent intent = new Intent(getApplicationContext(),SelectPatientInfoActivity.class);
					startActivity(intent);
				}
 				}else{
 					Intent in = new Intent(context, NewSignIn.class);
					context.startActivity(in);
 				}
				
			}
		});
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	       toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
	      Utilities.setTitleText(toolBar, "Diagnostic Test [2/5]");
	}
	private void setgalleryImages(HorizontalScrollView horiView,
			String[] galArray) {
		// TODO Auto-generated method stub
		LinearLayout innerLayout=(LinearLayout) horiView.findViewById(R.id.innerLayout1);
		int count=galArray.length;
		View[] views = new View[count];
		for(int cnt = 0; cnt<count;cnt++)
		{
			addImageView(innerLayout,galArray[cnt]);
			
			
		}}
	private void addImageView(LinearLayout innerLayout, String galArray) {
		// TODO Auto-generated method stub
		
		NetworkImageView imageIngallery=new NetworkImageView(this);
		ImageLoader imageLoader2; 
		URL url;
			try {
				
				url = new URL(galArray);
				imageLoader2 = VolleyRequest.getInstance(context).getImageLoader();
				LinearLayout.LayoutParams lp= new LayoutParams(150,150);
				lp.setMargins(5,5,5,5);
				imageIngallery.setLayoutParams(lp);
				/*Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());*/
				imageIngallery.setImageUrl(galArray, imageLoader2);
				innerLayout.addView(imageIngallery);
			} catch (IOException e) {
		// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}
	private void setTouchListenerScrViews(HorizontalScrollView horScrView)
	{
		horScrView.setOnTouchListener(new ListView.OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	                int action = event.getAction();
	                switch (action) {
	                case MotionEvent.ACTION_DOWN:
	                    // Disallow ScrollView to intercept touch events.
	                   scroll_view.requestDisallowInterceptTouchEvent(true);
	                    break;

	                case MotionEvent.ACTION_UP:
	                    // Allow ScrollView to intercept touch events.
	                	scroll_view.requestDisallowInterceptTouchEvent(false);
	                    break;
	                }

	                // Handle HorizontalScrollView touch events.
	                v.onTouchEvent(event);
	                return true;
	            }
	        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.diagno_lab_test_pkg, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
}
