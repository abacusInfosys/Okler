package com.okler.android;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageRequest;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.BrandsDataBean;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.CategoriesDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.SubCategoriesDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.dialogs.CheckInternetDialog;
import com.okler.dialogs.ErrorDialog;
import com.okler.dialogs.VersionUpdateDialog;
import com.okler.network.NetworkUtils;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.SocialMediaUtils;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class SplashActivity extends BaseActivity {

	// Splash screen timer
    private static int SPLASH_TIME_OUT = 10000;
    String categoriesUrl,subCategoriesUrl,brandsUrl,priceRangeUrl;
    int brandMappingId;
    ArrayList<CategoriesDataBean> categoriesList;
    ArrayList<SubCategoriesDataBean> subCatsList;
	ArrayList<BrandsDataBean> alloBrands;
	ArrayList<BrandsDataBean> homeoBrands;
	ArrayList<String> priceRanges;
	ArrayList<BrandsDataBean> hsBrands;
	CategoriesDataBean cat;
	SubCategoriesDataBean subCat;
	BrandsDataBean brndHS;
	ImageRequest irs;
	int userId;
	UsersDataBean ubean;
	ArrayList<AddressDataBean> aList,paList;
	AddressDataBean adbean;
	CartDataBean mainCart;
	ArrayList<ProductDataBean> prodList;
	ArrayList<ProductDataBean> myfav;
	ProductDataBean pdbean;
	Activity ack;
	float currentVersion;
	float onlineVersion;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);	
		ack = this;
		categoriesUrl = getString(R.string.get_categories);
		subCategoriesUrl = getString(R.string.get_sub_categories);
		categoriesList = new ArrayList<CategoriesDataBean>();
		subCatsList = new ArrayList<SubCategoriesDataBean>();
		alloBrands = new ArrayList<BrandsDataBean>();
		homeoBrands = new ArrayList<BrandsDataBean>();
		hsBrands = new ArrayList<BrandsDataBean>();
		priceRanges = new ArrayList<String>();
		ubean = new UsersDataBean();
		aList = new ArrayList<AddressDataBean>();
		adbean = new AddressDataBean();
		mainCart = new CartDataBean();
		paList = new ArrayList<AddressDataBean>();
		prodList = new ArrayList<ProductDataBean>();
		pdbean = new ProductDataBean();
		SocialMediaUtils scMUtils = new SocialMediaUtils(this,false);
		scMUtils.initGoogleClient(this);
		SocialMediaUtils.initFacebookSDK(this);
		
		
		Okler.getInstance().setBookingType(100);
		
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
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
	

	
	private void setAlloBrands()
	{
		brandsUrl = getString(R.string.get_brands_allo1)+getString(R.string.get_brands_allo2)+getString(R.string.get_brands_allo3);
		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, brandsUrl, new JSONObject(), 	new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) 
			{
				try {
					JSONArray resultsArr = response.getJSONArray("result");
					for(int j = 0 ;j< resultsArr.length();j++)
					{
						brndHS = new BrandsDataBean();
						JSONObject subCates = resultsArr.getJSONObject(j);
						brndHS.setBrandId(subCates.getString("id"));
						brndHS.setBrandName(subCates.getString("company_name"));
						alloBrands.add(brndHS);					
					}
					Okler.getInstance().setAlloBrands(alloBrands);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
				Log.e("ERROR", ""+error.getStackTrace());
					//Utilities.handleVollyError(error, ack);
					
				//	Toast.makeText(getApplicationContext(), "In on error repsonse", Toast.LENGTH_LONG).show();
				}		
			
		});
		VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson);
		
	}
	
	private void setHomeoBrands()
	{
		brandsUrl = getString(R.string.get_brands_homeo1)+getString(R.string.get_brands_allo2)+getString(R.string.get_brands_allo3);
		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, brandsUrl, new JSONObject(),	new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) 
			{
				try {
					JSONArray resultsArr = response.getJSONArray("result");
					for(int j = 0 ;j< resultsArr.length();j++)
					{
						brndHS = new BrandsDataBean();
						JSONObject subCates = resultsArr.getJSONObject(j);
						brndHS.setBrandId(subCates.getString("id"));
						brndHS.setBrandName(subCates.getString("company_name"));
						homeoBrands.add(brndHS);					
					}
					Okler.getInstance().setHomeoBrands(homeoBrands);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Log.e("ERROR", ""+error.getStackTrace());
					//Utilities.handleVollyError(error, ack);
				}					
		});
		VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson);
		
	}
	

	private void setHSBrands()
	{
		brandsUrl = getString(R.string.get_healthshop_brand);
		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, brandsUrl, new JSONObject(),	new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) 
			{
				try {
					JSONArray resultsArr = response.getJSONArray("result");
					for(int j = 0 ;j< resultsArr.length();j++)
					{
						brndHS = new BrandsDataBean();
						JSONObject subCates = resultsArr.getJSONObject(j);
						brndHS.setBrandId(subCates.getString("id"));
						brndHS.setBrandName(subCates.getString("company_name"));
						hsBrands.add(brndHS);
					
					}
					Okler.getInstance().setHsBrands(hsBrands);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
				
					//Utilities.handleVollyError(error, ack);
					Log.e("ERROR", ""+error.getStackTrace());
				}					
		});
		VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson);
		
	}
	
	public void setUserData(){
		if (Utilities.getUserStatusFromSharedPref(SplashActivity.this) == UserStatusEnum.LOGGED_IN ||
				(Utilities.getUserStatusFromSharedPref(SplashActivity.this) == UserStatusEnum.LOGGED_IN_FB) ||
				(Utilities.getUserStatusFromSharedPref(SplashActivity.this) == UserStatusEnum.LOGGED_IN_GOOGLE)){
			getAllAddress();
			getPatAdresses();
			getUserCart();
			getAllFavourites();
		}
	}
	
	public void getUserCart(){
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		userId = ubean.getId();		

String getMyCartUrl = getString(R.string.getUserCart)+userId;

WebJsonObjectRequest cartjson = new WebJsonObjectRequest(Method.GET, getMyCartUrl, new JSONObject(), new Response.Listener<JSONObject>() {

	@Override
	public void onResponse(JSONObject response) {
		mainCart = new CartDataBean();
		
		JSONObject jobj = (JSONObject)response;
		
			String msg = jobj.optString("message");
			if("User cart is Empty.".equals(msg))
				return;
			
			JSONArray jarr = jobj.optJSONArray("result");
			int length = jarr.length();
			prodList = new ArrayList<ProductDataBean>();
			
			
			for (int i = 0; i < length; i++) {
				JSONObject jobj1 = jarr.optJSONObject(i);
				pdbean = new ProductDataBean();
				pdbean.setCart_id(jobj1.optString("cart_id"));
				pdbean.setCart_num(jobj1.optString("cart_number"));
				pdbean.setProdId(Integer.parseInt(jobj1.optString("id")));
				pdbean.setTax(Float.parseFloat(jobj1.optString("tax")));
				pdbean.setPresc_id(jobj1.optString("prescription_id"));
				pdbean.setProdName(jobj1.optString("name"));
				pdbean.setDesc(jobj1.optString("description"));
				pdbean.setWeight(jobj1.optString("weight"));
				pdbean.setPacking_size(jobj1.optString("type_of_packing"));
				pdbean.setSide_effect(jobj1.optString("side_effects"));
				pdbean.setUnits(Integer.parseInt(jobj1.optString("quantity")));
				pdbean.setCart_item_id(Integer.parseInt(jobj1.optString("cart_item_id")));
				pdbean.setMrp(Float.parseFloat(jobj1.optString("price")));
				pdbean.setOklerPrice(Float.parseFloat(jobj1.optString("saleprice")));
				pdbean.setDiscount(Float.parseFloat(jobj1.optString("discount")));
				String ajimg = jobj1.optString("images");
				String aurl2="";
				if(ajimg.equals(null)){
					aurl2 = "drawable://" + R.drawable.no_image_found;
				}else{
					if(ajimg.contains(",")){
				String aj1[] =ajimg.split(",");
				String aj2=aj1[0];
				String acolon = ":";
				String aj3[]=aj2.split(acolon);
				String aurl = aj3[2];
				String aurl1 = aurl.substring(1);
				int alength = aurl.length();
				aurl2 = aurl.substring(1, (alength-1));}
					else{
						aurl2 = ajimg;
					}
				}
				pdbean.setImgUrl(aurl2);
				if(jobj1.optString("prescription_need").equals("null")||jobj1.optString("prescription_need").equals(null)||jobj1.optString("prescription_need").equals(""))
				{
				pdbean.setPresc_needed(0);	
				}else{
				pdbean.setPresc_needed(Integer.parseInt(jobj1.optString("prescription_need")));
				}
				pdbean.setCompany(jobj1.optString("company_name"));
				if(jobj1.has("generic_val")){
				pdbean.setGeneric_name(jobj1.optString("generic_val"));//need string from webservice
				}
				pdbean.setProdType(Integer.parseInt(jobj1.optString("pro_type")));
				
				prodList.add(pdbean);
				
			}
			
			mainCart.setProdList(prodList);
			Okler.getInstance().setMainCart(mainCart);
			
		
		
	}
}, new Response.ErrorListener() {

	@Override
	public void onErrorResponse(VolleyError error) {
		Log.e("ERROR", ""+error.getStackTrace());
		//Utilities.handleVollyError(error, ack);
	}
});
VolleyRequest.addJsonObjectRequest(ack, cartjson);
	}
	public void getPatAdresses(){
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		userId = ubean.getId();	
			
String pAddrUrl = getString(R.string.GetPatientsAllAddr)+userId;
WebJsonObjectRequest pjson = new WebJsonObjectRequest(Method.GET, pAddrUrl, new JSONObject(), new Response.Listener<JSONObject>() {
	
	AddressDataBean adbean = new AddressDataBean();
	JSONObject jobj = new JSONObject();
	JSONObject jobj2;
	@Override
	public void onResponse(JSONObject response) {
		try {
			JSONArray jarr = response.getJSONArray("result");
			int length = jarr.length();
			paList = new ArrayList<AddressDataBean>();
			for (int i = 0; i < length; i++) {
				//jobj = new JSONObject();
				jobj = jarr.getJSONObject(i);
				adbean = new AddressDataBean();
				adbean.setPat_id(Integer.parseInt(jobj.optString("pat_id")));
				adbean.setFirstname(jobj.optString("firstname"));
				adbean.setLastname(jobj.optString("middlename"));
				adbean.setDob(jobj.optString("dob"));
				adbean.setRelationId(jobj.optString("relationid"));
				adbean.setGenderId(jobj.optString("gender"));
				adbean.setPhone(jobj.optString("mobileno"));
				jobj2 = new JSONObject();
				if((jobj.getString("residential_address")!=null)&&(jobj.getString("residential_address")!="false")){
				jobj2 = jobj.getJSONObject("residential_address");
				adbean.setAddress1(jobj2.optString("addr1"));
				adbean.setAddress2(jobj2.optString("addr2"));
				adbean.setLandmark(jobj2.optString("land_mark"));
				adbean.setZip(jobj2.optString("pincode"));
				adbean.setCity_id(jobj2.optString("city_id"));
				adbean.setCity(jobj2.optString("city_name"));
				adbean.setState(jobj2.optString("state_name"));
				adbean.setGender(jobj.optString("userGender"));
				adbean.setRelation(jobj.optString("relation_name"));
				}
				paList.add(adbean);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ubean.setPatAddList(paList);
		Utilities.writeCurrentUserToSharedPref(ack, ubean);
		
	}
}, new Response.ErrorListener() {

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		//Utilities.handleVollyError(error, ack);
		Log.e("ERROR", ""+error.getStackTrace());
	}
});	
VolleyRequest.addJsonObjectRequest(ack, pjson);

//patient address call ends here
	}
	public void getAllAddress(){
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		userId = ubean.getId();
	String get_addr = getString(R.string.get_user_address)+userId;
	
WebJsonObjectRequest adjson = new WebJsonObjectRequest(Method.GET, get_addr, new JSONObject(), new Response.Listener<JSONObject>() {

	@Override
	public void onResponse(JSONObject response) {
		JSONObject responseObj =(JSONObject)response;
		JSONObject result;
		try {
			
			result = responseObj.getJSONObject("result");
			
			if(!result.isNull("user_Addr")){
				JSONArray jarr2 = result.getJSONArray("user_Addr");
				JSONObject jobj2;
				int length = jarr2.length();
				aList = new ArrayList<AddressDataBean>();
				for (int i = 0; i < length; i++) {
					adbean = new AddressDataBean();
					jobj2 = new JSONObject();
					int a = i+1;
					if(a==length){
						break;
					}
					jobj2 = jarr2.getJSONObject(a);
					//jobj2 = jarr2.getJSONObject(i);
					
					adbean.setFirstname(jobj2.optString("customer_name"));
					adbean.setLastname(jobj2.optString("surname"));
					adbean.setAddress1(jobj2.optString("addr1"));
					adbean.setAddress2(jobj2.optString("addr2"));
					adbean.setZip(jobj2.optString("zip"));
					adbean.setCountry_id(jobj2.optString("country_id"));
					adbean.setAddr_id(jobj2.optString("addr_id"));
					adbean.setCity(jobj2.optString("city_name"));
					adbean.setState(jobj2.optString("state_name"));
					adbean.setCity_id(jobj2.optString("city_id"));
					adbean.setState_id(jobj2.optString("state_id"));
					adbean.setCountry(jobj2.optString("country_name"));
					adbean.setDefault_billing(jobj2.optInt("default_billing"));
					adbean.setDefault_shipping(jobj2.optInt("default_shiping"));
					adbean.setPreferred_del_time(jobj2.optString("delivery_time"));
					//adbean.setPreferred_del_time(jobj2.optString(name));
					AddressDataBean ab = new AddressDataBean();
					ab = adbean;
					aList.add(adbean);
			}
				//showProgress(false);
				ubean.setAddDatabean(aList);
				Utilities.writeCurrentUserToSharedPref(ack, ubean);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
	}
}, new Response.ErrorListener() {

	@Override
	public void onErrorResponse(VolleyError error) {
		Log.e("ERROR", ""+error.getStackTrace());
		//Utilities.handleVollyError(error, ack);
	}
});	
	VolleyRequest.addJsonObjectRequest(ack, adjson);
//address call ends here

	}
	
	public void isUserExists(){
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
		if(ubean==null)
			ubean = new UsersDataBean();
		String phone = ubean.getPhone();
		String isUserExist = getString(R.string.is_user_exists_url)
				+ phone+getString(R.string.mail);
		WebJsonObjectRequest isExist = new WebJsonObjectRequest(Method.GET, isUserExist, new JSONObject(), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				JSONArray jarr = response.optJSONArray("result");
				if(jarr!=null){
				JSONObject job = jarr.optJSONObject(0);
				UsersDataBean ubean2 = Utilities.getCurrentUserFromSharedPref(ack);
				if(ubean2==null){
					ubean2 = new UsersDataBean();
				}
				ubean2.setApprove_status(Integer.parseInt(job.optString("approved_status")));
				ubean2.setId(Integer.parseInt(job.optString("id")));
				ubean2.setPhone(job.optString("phone"));
			Utilities.writeCurrentUserToSharedPref(ack, ubean2);
			}
			
			}
			
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				
			}
		});
		VolleyRequest.addJsonObjectRequest(ack, isExist);
		}
	public void getAllFavourites()
	{
		
			if (Utilities.getUserStatusFromSharedPref(this) == UserStatusEnum.LOGGED_IN ||
					(Utilities.getUserStatusFromSharedPref(this) == UserStatusEnum.LOGGED_IN_FB) ||
					(Utilities.getUserStatusFromSharedPref(this) == UserStatusEnum.LOGGED_IN_GOOGLE))
			{
				UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(this);
				int uid = ubean.getId();
	    	  String get_fav = getString(R.string.get_fav_url)+uid;
	 	     
	 	     
	 	     WebJsonObjectRequest webjson= new WebJsonObjectRequest(Method.GET, get_fav, new JSONObject(),new Response.Listener<JSONObject>() 
	 					{
	 						@Override
	 						public void onResponse(JSONObject response) 
	 						{
	 							// TODO Auto-generated method stub
	 							
	 							ProductDataBean pbean;
	 							
	 							try
	 							{
	 							JSONObject responseObj =(JSONObject)response;
	 							String result = responseObj.getString("result");
	 					//		Toast.makeText(getApplicationContext(), "result" + result, Toast.LENGTH_LONG).show();
	 							
	 							JSONArray doctorsArr = responseObj.getJSONArray("result");
	 							//docCount=responseObj.getInt("TotalCount");
	 							myfav = new ArrayList<ProductDataBean>();
	 							for(int i = 0 ; i < doctorsArr.length();i++)
	 							{
	 								pbean = new ProductDataBean();
	 								try
	 								{
	 									JSONObject docObj =(JSONObject) doctorsArr.get(i);
	 									pbean.setProdName(docObj.getString("name"));
	 									pbean.setProdId(docObj.getInt("id"));
	 									pbean.setDesc(docObj.getString("description"));
	 									pbean.setMrp(docObj.getInt("price"));
	 									pbean.setOklerPrice(docObj.getInt("saleprice"));
	 									Float discount = Float.parseFloat(docObj.getString("discount"));
	 									pbean.setDiscount(discount);
	 									
	 									String jimg = docObj.getString("images");
	 									String url2;
	 									if(jimg.equals(null)||jimg.equals("null")||jimg.equals("")){
	 										url2 = "drawable://" + R.drawable.tempcuff;
	 									}else{
	 									String j1[] =jimg.split(",");
	 									String j2=j1[0];
	 									String colon = ":";
	 									String j3[]=j2.split(colon);
	 									String url = j3[2];
	 									//String url1 = url.substring(1);
	 									int length = url.length();
	 									url2 = url.substring(1, (length-1));
	 									}
	 									//JSONObject jimg2 = jimg.getJSONObject("");
	 									//JSONObject jimg3 = jobj2.getJSONObject("images");
	 									//pdBean.setImgUrl(jobj2.getJSONObject("images").getJSONObject("").getString("filename"));
	 									pbean.setImgUrl(url2);
	 									
	 									Log.i("tag", "json object" + docObj);
	 									}catch (JSONException e) {
	 										// TODO: handle exception
	 										Log.e("JSON Exception", e.getMessage());
	 									}
	 								
	 								myfav.add(pbean);
	 								
	 							 }
	 								Utilities.writeFavouritesToSharedPref(ack, myfav);
	 								//Okler.getInstance().setFavourites(myfav);
	 								
	 							}catch(JSONException jsonEx)
	 							{
	 								Log.e("Exception json", jsonEx.getStackTrace().toString());
	 							}
	 					
	 						}}, 
	 						new Response.ErrorListener() 
	 						{

	 							@Override
	 							public void onErrorResponse(VolleyError error) 
	 							{
	 								// TODO Auto-generated method stub
	 					
	 							}
	 						}
	 			);
	 		
	 	VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
			}
	      
	      {
	    	  ProductDataBean pbean = new ProductDataBean();
	    	  //pbean = Utilities.getFavouritesFromSharedPref(this);
	    	  Log.i("product", ""+pbean);
	      }
	}
		public void webCallForUpdateVersion()
	{
		
		
		String getVersionCode;
		String user = getString(R.string.getVersionCode1);
		String version = getString(R.string.getVersionCode2);
		
		try {
			currentVersion = Float.parseFloat(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			version = URLEncoder.encode(version, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getVersionCode = getString(R.string.serverUrl)+user+version;
		
		WebJsonObjectRequest isExist = new WebJsonObjectRequest(Method.GET, getVersionCode, new JSONObject(), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				JSONObject jarr = response.optJSONObject("result");
				try {
					onlineVersion = Float.parseFloat(jarr.getString("current_version"));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.e("jarr", ""+jarr);
				
				//if (onlineVersion == 0 ) {
	            if (currentVersion < onlineVersion) 
	            {
	            	VersionUpdateDialog versionDialog = new VersionUpdateDialog(SplashActivity.this);
	            	versionDialog.setCancelable(false);
	            	versionDialog.show(ack.getFragmentManager(),"");
	            }
	            else
	            {
	            	
	             	isUserExists();
	            	setUserData();
	            	//SetCategories
	            	UIUtils.setCategories(SplashActivity.this,categoriesUrl);
	            	//SetSubcategories
	           
	            	//SetHomeoBrands
	            	setHomeoBrands();
	            	 //	SetAlloBrands
	            	setAlloBrands();
	            	setHSBrands();
	            	
	            	UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(SplashActivity.this);
	                   // 	Toast.makeText(ack, ""+ubean.getApprove_status(), Toast.LENGTH_SHORT).show();
	                        	if (Utilities.getUserStatusFromSharedPref(SplashActivity.this) == UserStatusEnum.LOGGED_IN||Utilities.getUserStatusFromSharedPref(SplashActivity.this) == UserStatusEnum.LOGGED_IN_FB||Utilities.getUserStatusFromSharedPref(SplashActivity.this) == UserStatusEnum.LOGGED_IN_GOOGLE)
	                			{	
	                        		if(ubean.getApprove_status() == 0)
	                            	{
	                                		Intent intent = new Intent(SplashActivity.this, OtpConfirmationActivity.class);
	                                		startActivity(intent);
	                                		finish();
	                                }
	                        		else
	                        		{
	                        			Intent startAct = new Intent(getApplicationContext(),ServiceCategoryActivity.class);
	                            		startActivity(startAct);
	                            		finish();
	                        		}
	                			}
	                        	else
	                        	{	
	            	            	Intent startAct = new Intent(getApplicationContext(),IntroScreen.class);
	            	        		startActivity(startAct);
	            	        		finish();
	                        	}
	            }
				//}
	        
			
			}
			
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				//Log.e("Error", new String(error.networkResponse.data));
				ErrorDialog versionDialog = new ErrorDialog(SplashActivity.this);
            	versionDialog.setCancelable(false);
            	versionDialog.show(ack.getFragmentManager(),"");
				Log.e("error", error.toString());
				
			}
		}, true);
		VolleyRequest.addJsonObjectRequest(ack, isExist);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
		
		try {
			if (NetworkUtils.checkNetworkAvailable(SplashActivity.this)) {
				
				webCallForUpdateVersion();
			} else
			{
				new Handler().postDelayed(new Runnable() {			 
	            @Override
	            public void run() {
	                // This method will be executed once the timer is over
	                // Start your app main activity
	            	//if user status is log in fb / log in google, then make user login again
	            	
	            	UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(SplashActivity.this);
	           // 	Toast.makeText(ack, ""+ubean.getApprove_status(), Toast.LENGTH_SHORT).show();
	                	if (Utilities.getUserStatusFromSharedPref(SplashActivity.this) == UserStatusEnum.LOGGED_IN||Utilities.getUserStatusFromSharedPref(SplashActivity.this) == UserStatusEnum.LOGGED_IN_FB||Utilities.getUserStatusFromSharedPref(SplashActivity.this) == UserStatusEnum.LOGGED_IN_GOOGLE)
	        			{	
	                		if(ubean.getApprove_status() == 0)
	                    	{
	                        		Intent intent = new Intent(SplashActivity.this, OtpConfirmationActivity.class);
	                        		startActivity(intent);
	                        		finish();
	                        }
	                		else
	                		{
	                			Intent startAct = new Intent(getApplicationContext(),ServiceCategoryActivity.class);
	                    		startActivity(startAct);
	                    		finish();
	                		}
	        			}
	                	else
	                	{	
	    	            	Intent startAct = new Intent(getApplicationContext(),IntroScreen.class);
	    	        		startActivity(startAct);
	    	        		finish();
	                	}
	            }
	        }, SPLASH_TIME_OUT);
	    	isUserExists();
	    	setUserData();
	    	//SetCategories
	    	UIUtils.setCategories(this,categoriesUrl);
	    	//SetSubcategories
	   
	    	//SetHomeoBrands
	    	setHomeoBrands();
	    	 //	SetAlloBrands
	    	setAlloBrands();
	    	setHSBrands();
	    	getAllFavourites();
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		
	}
}
