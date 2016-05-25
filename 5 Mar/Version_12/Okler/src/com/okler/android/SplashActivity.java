package com.okler.android;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.http.protocol.ResponseConnControl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageRequest;
import com.google.gson.JsonArray;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.BrandsDataBean;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.CategoriesDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.SubCategoriesDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.SocialMediaUtils;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
	ProductDataBean pdbean;
	Activity ack;
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
		//Depending on the user status, launch wither MainActivity Or MainActivityyLoggedIn - To Do
    	new Handler().postDelayed(new Runnable() {			 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */	 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
            	//if user status is log in fb / log in google, then make user login again
            	if (Utilities.getUserStatusFromSharedPref(SplashActivity.this) == UserStatusEnum.LOGGED_IN)
    			{
            		Intent startAct = new Intent(getApplicationContext(),ServiceCategoryActivity.class);
            		startActivity(startAct);
            		finish();
    			}
            	else
            	{
	            	Intent startAct = new Intent(getApplicationContext(),IntroScreen.class);
	        		startActivity(startAct);
	        		finish();
            	}
            }
        }, SPLASH_TIME_OUT);
    	
    	setUserData();
    	//SetCategories
    	UIUtils.setCategories(this,categoriesUrl);
    	//SetSubcategories
   
    	//SetHomeoBrands
    	setHomeoBrands();
    	 //	SetAlloBrands
    	setAlloBrands();
    	setHSBrands();
    	
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
						brndHS.setBrandName(subCates.getString("brand_name"));
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
						brndHS.setBrandName(subCates.getString("brand_name"));
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
						brndHS.setBrandName(subCates.getString("brand_name"));
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
				
					Log.e("error", ""+error.getMessage());
				}					
		});
		VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson);
		
	}
	
	public void setUserData(){
		if (Utilities.getUserStatusFromSharedPref(SplashActivity.this) == UserStatusEnum.LOGGED_IN){
			getAllAddress();
			getPatAdresses();
			getUserCart();
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
		try {
			String msg = jobj.getString("message");
			JSONArray jarr = jobj.getJSONArray("result");
			int length = jarr.length();
			prodList = new ArrayList<ProductDataBean>();
			
			
			for (int i = 0; i < length; i++) {
				JSONObject jobj1 = jarr.getJSONObject(i);
				pdbean = new ProductDataBean();
				pdbean.setCart_id(jobj1.getString("cart_id"));
				pdbean.setCart_num(jobj1.getString("cart_number"));
				pdbean.setProdId(Integer.parseInt(jobj1.getString("id")));
				pdbean.setTax(Float.parseFloat(jobj1.getString("tax")));
				pdbean.setPresc_id(jobj1.getString("prescription_id"));
				pdbean.setProdName(jobj1.getString("name"));
				pdbean.setDesc(jobj1.optString("description"));
				pdbean.setWeight(jobj1.optString("weight"));
				pdbean.setPacking_size(jobj1.optString("type_of_packing"));
				pdbean.setSide_effect(jobj1.optString("side_effects"));
				pdbean.setUnits(Integer.parseInt(jobj1.getString("quantity")));
				pdbean.setCart_item_id(Integer.parseInt(jobj1.getString("cart_item_id")));
				pdbean.setMrp(Float.parseFloat(jobj1.getString("price")));
				pdbean.setOklerPrice(Float.parseFloat(jobj1.getString("saleprice")));
				pdbean.setDiscount(Float.parseFloat(jobj1.getString("discount")));
				String ajimg = jobj1.getString("images");
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
				if(jobj1.getString("prescription_need").equals("null")||jobj1.getString("prescription_need").equals(null)||jobj1.getString("prescription_need").equals(""))
				{
				pdbean.setPresc_needed(0);	
				}else{
				pdbean.setPresc_needed(Integer.parseInt(jobj1.getString("prescription_need")));
				}
				pdbean.setCompany(jobj1.optString("company_name"));
				if(jobj1.has("generic_val")){
				pdbean.setGeneric_name(jobj1.optString("generic_val"));//need string from webservice
				}
				pdbean.setProdType(Integer.parseInt(jobj1.getString("pro_type")));
				
				prodList.add(pdbean);
				
			}
			
			mainCart.setProdList(prodList);
			Okler.getInstance().setMainCart(mainCart);
			
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}, new Response.ErrorListener() {

	@Override
	public void onErrorResponse(VolleyError error) {
		
		
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
		
		
	}
});	
	VolleyRequest.addJsonObjectRequest(ack, adjson);
//address call ends here

	}
}
