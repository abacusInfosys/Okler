package com.okler.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.okler.android.MyCart;
import com.okler.android.NewSignIn;
import com.okleruser.R;
import com.okler.android.SplashActivity;
import com.okler.databeans.BrandsDataBean;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.CategoriesDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.SubCategoriesDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UIUtils {
	static String value;
	static int intvalue;
	AutoCompleteTextView autoTextToValidate;
	Spinner spinner;
	static CategoriesDataBean cat;
	static ArrayList<CategoriesDataBean> categoriesList;
	static ArrayList<SubCategoriesDataBean> subCatsList;
	static SubCategoriesDataBean subCat;
	static Context cntx;
	static ArrayList<UsersDataBean> citiesList = new ArrayList<UsersDataBean>();
	static int totalPages;
	static int totalWebServiceResults;
	static boolean isCityFound=false;
	static ArrayList<ProductDataBean> favs;
	static boolean isAddedAlready =false;

	// 15_01_2016 Gitesh start
	public UIUtils() {
	}

	public UIUtils(AutoCompleteTextView autoTextToValidate) {
		this.autoTextToValidate = autoTextToValidate;
		autoTextToValidate.addTextChangedListener(watch);

	}

	public UIUtils(Spinner spinner) {
		this.spinner = spinner;
		// spinneraddTextChangedListener(watch);

	}

	public boolean validateSpinner(String errorMessage, String texttoCompare,
			String selectedText, Context context) {
		if (!(selectedText.equals(texttoCompare))) {
			return true;
		} else {
			Toast.makeText(context, "Please Select " + texttoCompare,
					Toast.LENGTH_LONG).show();
			return false;
		}
	}

	public boolean validateState(String errorMessage, String state,
			Context context) {
		String inputText = autoTextToValidate.getText().toString();
		if (inputText == null || inputText.equals("") || inputText == "") {
			autoTextToValidate.setError(errorMessage);
			autoTextToValidate.requestFocus();

			return false;
		} else {
			ArrayList<String> states = Okler.getInstance().getStates();
			for (int i = 0; i < states.size(); i++) {
				if (state.equals(states.get(i))) {
					return true;

				}

			}
			Toast.makeText(context, "Please Enter Valid State",
					Toast.LENGTH_LONG).show();
		}
		return false;

	}

	public boolean validateCity(String errorMessage, String city,
			Context context) {
		String inputText = autoTextToValidate.getText().toString();
		if (inputText == null || inputText.equals("") || inputText == "") {
			autoTextToValidate.setError(errorMessage);
			autoTextToValidate.requestFocus();
			return false;
		} else {
			ArrayList<String> cities = Okler.getInstance().getCities();
			for (int i = 0; i < cities.size(); i++) {
				if (city.equals(cities.get(i))) {
					return true;

				}

			}
			Toast.makeText(context, "Please Enter Valid City",
					Toast.LENGTH_LONG).show();
		}
		return false;

	}

	public static RelativeLayout addRemoveUnits(RelativeLayout unitLayout,
			final Activity ack) {
		ImageView red_plus_image = (ImageView) unitLayout
				.findViewById(R.id.red_plus_Image);
		ImageView red_minus_image = (ImageView) unitLayout
				.findViewById(R.id.red_minus_Image);
		final EditText unit_value = (EditText) unitLayout
				.findViewById(R.id.unitValue);
		RelativeLayout red_minus_ImageRl = (RelativeLayout) unitLayout
				.findViewById(R.id.red_minus_ImageRl);
		RelativeLayout red_plus_ImageRl = (RelativeLayout) unitLayout
				.findViewById(R.id.red_plus_ImageRl);

		if (unit_value.getText().toString().trim().equals("")) {
			unit_value.setText("01");
		} else {
			red_plus_image.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (unit_value.getText().toString().trim().equals("")) {
						unit_value.setText("00");
					}
					value = unit_value.getText().toString().trim();
					if (value.length() > 2) {
						unit_value.setText("");
						Toast.makeText(ack, "Maximum limit is 99",
								Toast.LENGTH_SHORT).show();
					} else {
						intvalue = Integer.parseInt(value);
						if (intvalue >= 99)
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						else {
							intvalue++;
							value = String.valueOf(intvalue);
							if (intvalue > 9) {
								unit_value.setText(value);
							} else {
								unit_value.setText("0" + value);
							}
						}
					}
				}
			});

			red_plus_ImageRl.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (unit_value.getText().toString().trim().equals("")) {
						unit_value.setText("00");
					}
					value = unit_value.getText().toString().trim();
					if (value.length() > 2) {
						unit_value.setText("");
						Toast.makeText(ack, "Maximum limit is 99",
								Toast.LENGTH_SHORT).show();
					} else {
						intvalue = Integer.parseInt(value);
						if (intvalue >= 99)
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						else {
							intvalue++;
							value = String.valueOf(intvalue);
							if (intvalue > 9) {
								unit_value.setText(value);
							} else {
								unit_value.setText("0" + value);
							}
						}
					}
				}
			});

			red_minus_image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (unit_value.getText().toString().trim().equals("")) {
						unit_value.setText("01");
					}
					value = unit_value.getText().toString().trim();
					if (value.length() > 2) {
						unit_value.setText("");
						Toast.makeText(ack, "Maximum limit is 99",
								Toast.LENGTH_SHORT).show();
					} else {
						intvalue = Integer.parseInt(value);

						if (intvalue <= 0)
							intvalue = 0;
						else
							intvalue--;
						value = String.valueOf(intvalue);
						if (intvalue > 9) {
							unit_value.setText(value);
						} else {
							unit_value.setText("0" + value);
						}
					}
				}
			});

			red_minus_ImageRl.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (unit_value.getText().toString().trim().equals("")) {
						unit_value.setText("01");
					}
					value = unit_value.getText().toString().trim();
					if (value.length() > 2) {
						unit_value.setText("");
						Toast.makeText(ack, "Maximum limit is 99",
								Toast.LENGTH_SHORT).show();
					} else {
						intvalue = Integer.parseInt(value);

						if (intvalue <= 0)
							intvalue = 0;
						else
							intvalue--;
						value = String.valueOf(intvalue);
						if (intvalue > 9) {
							unit_value.setText(value);
						} else {
							unit_value.setText("0" + value);
						}
					}
				}
			});
		}

		return unitLayout;
	}

	// 15_01_2016 Gitesh ends

	public static int getBookingType(String name) {
		switch (name) {
		case "Allopathy":
			return 0;
		case "Ayurvedic":
			return 3;
		case "Homeopathy":
			return 4;
		case "Healthshop":
			return 1;
		case "Diagnostic":
			return 9;
		case "UploadPresc":
			return 11;
		case "helpSupport":
			return 18;
		case "mycart":
			return 19;
		case "notifications":
			return 21;
		case "rateus":
			return 22;
		case "DiagnoPresc":
			return 23;

		default:
			return 100;

		}
	}

	public static int getToolBarDrawable(int id) {
		switch (id) {
		case 0:
			return R.drawable.custom_view_grad_medicine;
		case 1:
			return R.drawable.custom_view_grad_healthshop;
		case 3:
			return R.drawable.custom_view_grad_medicine;
		case 4:
			return R.drawable.custom_view_grad_medicine;
		case 9:
			return R.drawable.custom_view_grad_diagno;
		case 11:
			return R.drawable.custom_view_grad_upload_pesc;
		case 23:
			return R.drawable.custom_view_grad_diagno;
		default:
			return R.drawable.custom_view_grad_healthshop;
		}
	}

	public static void setCartCount(Button btn, final Activity ack) {
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Utilities.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN
						|| (Utilities.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_FB)
						|| (Utilities.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
					Intent i = new Intent(ack, MyCart.class);
					ack.startActivity(i);
				} else {
					Intent i = new Intent(ack, NewSignIn.class);
					ack.startActivity(i);
				}
			}
		});
		CartDataBean cbean = Okler.getInstance().getMainCart();
		ArrayList<ProductDataBean> pdList = cbean.getProdList();
		// pdList = odbean.getProdList();
		int maincount = pdList.size();

		if (maincount > 9) {
			btn.setText("" + maincount);
		} else {
			btn.setText("0" + maincount);
		}
	}

	public static void populateBrands(Activity context,
			LinearLayout rightMenuSubCats,
			ArrayList<BrandsDataBean> brandsData, boolean isMedBrands) {
		// LinearLayout rightMenuSubCats =
		// (LinearLayout)findViewById(R.id.rightMenuSubcats);
		if (!isMedBrands)
			rightMenuSubCats.removeAllViews();
		View[] views = new View[brandsData.size()];
		View[] separatorView = new View[brandsData.size()];
		for (int cnt = 0; cnt < brandsData.size(); cnt++) {
			views[cnt] = context.getLayoutInflater().inflate(
					R.layout.include_category_2, null);
			separatorView[cnt] = context.getLayoutInflater().inflate(
					R.layout.separator_1dp, null);
			views[cnt].setTag(brandsData.get(cnt).getBrandId());
			views[cnt].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// mDrawerLayout.closeDrawer(Gravity.END);

					/*
					 * setFiltersInRightMenu();
					 * linParent.setVisibility(View.GONE);
					 * sliderManuParentLinear1.setVisibility(View.VISIBLE);
					 */
				}
			});
			TextView txtSubCategoryName = (TextView) views[cnt]
					.findViewById(R.id.subCategory);
			txtSubCategoryName.setText(brandsData.get(cnt).getBrandName());
			rightMenuSubCats.addView(views[cnt]);
			rightMenuSubCats.addView(separatorView[cnt]);
		}
	}

	public TextWatcher watch = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int a, int b, int c) {

			autoTextToValidate.setError(null);

		}
	};

	public static void setCategories(Context context, String categoriesUrl) {
		cntx = context;
		categoriesList = new ArrayList<CategoriesDataBean>();

		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET,
				categoriesUrl, new JSONObject(),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray resultsArr = response
									.getJSONArray("result");
							String urlPath = response
									.getString("category_image_url");
							for (int i = 0; i < resultsArr.length(); i++) {
								cat = new CategoriesDataBean();
								JSONObject catObj = (JSONObject) resultsArr
										.get(i);
								cat.setCatId(catObj.getString("id"));
								cat.setCategoryName(catObj.getString("name"));
								cat.setImgName(catObj.getString("image"));
								String imageUrl = urlPath
										+ catObj.getString("image");
								cat.setImagePath(imageUrl);
								/*
								 * irs = new ImageRequest(imageUrl, new
								 * Response.Listener<Bitmap>() {
								 * 
								 * @Override public void onResponse(Bitmap
								 * response) { // callback Bitmap tempB =
								 * response;
								 * cat.setImgBase64(Utilities.convertImageToBase64
								 * (tempB)); } }, 100, 100, null, null);
								 */
								categoriesList.add(cat);
							}
							Okler.getInstance().setCategoriesBean(
									categoriesList);
							setSubcategories(cntx);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						Log.e("Error", "er" + error.getMessage());
					}

				}, true);
		if (VolleyRequest.addJsonObjectRequest(context, wjson)) {
			// Toast.makeText(getApplicationContext(), "value searching",
			// Toast.LENGTH_LONG).show();
		} else {
			// Toast.makeText(getApplicationContext(), "value not found",
			// Toast.LENGTH_LONG).show();
		}
	}

	public static void setSubcategories(Context context) {
		String subCategoriesUrl;
		subCatsList = new ArrayList<SubCategoriesDataBean>();
		for (int i = 0; i < categoriesList.size(); i++) {
			subCategoriesUrl = context.getString(R.string.get_sub_categories)
					+ categoriesList.get(i).getCatId();
			WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET,
					subCategoriesUrl, new JSONObject(),
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								JSONArray resultsArr = response
										.getJSONArray("result");
								for (int j = 0; j < resultsArr.length(); j++) {
									subCat = new SubCategoriesDataBean();
									JSONObject subCates = resultsArr
											.getJSONObject(j);
									subCat.setCateId(subCates
											.getString("cat_id"));
									subCat.setCateName(subCates
											.getString("name"));
									subCat.setSubCateId(subCates
											.getString("id"));
									subCat.setSubCateName(subCates
											.getString("subcategory"));
									subCatsList.add(subCat);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Okler.getInstance().setSubCategories(subCatsList);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e("Error", "er" + error.getMessage());

						}

					});
			if (VolleyRequest.addJsonObjectRequest(context, wjson)) {

				// Toast.makeText(getApplicationContext(), "value searching",
				// Toast.LENGTH_LONG).show();
			} else {
				// Toast.makeText(getApplicationContext(), "value not found",
				// Toast.LENGTH_LONG).show();
			}
		}

	}
	
	public static void setUserLocationToSharedPrefs(double longi,double latti,Activity ack){
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
		Geocoder geocoder;
    	List<Address> addresses = new ArrayList<Address>();
    	geocoder = new Geocoder(ack, Locale.getDefault());

    	try {
			addresses = geocoder.getFromLocation(latti, longi, 1);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
    	
    	String city,country;
    	if(addresses.size()>0){
    	city = addresses.get(0).getLocality();
    	country = addresses.get(0).getCountryName();
    	
    	ubean.setUserCity(city);
    	ubean.setUserCountry(country);
    	int pageNo =1;
    	//getCityId(ack,pageNo,city);
    	
    	
    	
    	}
    	else{
    		ubean.setUserCity("");
        	ubean.setUserCountry("");	
    	}
    	
    	Utilities.writeCurrentUserToSharedPref(ack, ubean);
	}
	
	public static void getCityId(Activity ack,int pageNo,String city){
		String url = ack.getString(R.string.serverUrl)+ack.getString(R.string.getAllCitiesPart1)+pageNo+ack.getString(R.string.getProdsByGroup4)+city;
		boolean returnvalue;
		returnvalue = getCitiesWebService(url,city,ack,pageNo);
		
		for(int i=0;i<totalPages;i++){
		if(!returnvalue && (totalPages+1)>pageNo){
			pageNo++;
			Log.e("TP pageNo", ""+pageNo);
			url = ack.getString(R.string.serverUrl)+ack.getString(R.string.getAllCitiesPart1)+pageNo+ack.getString(R.string.getProdsByGroup4)+city;
			returnvalue = getCitiesWebService(url,city,ack,pageNo);
		}
		}
	}
	public static boolean getCitiesWebService(String getCityUrl,final String city,final Activity ack,final int pageNo){
		
		
		
		WebJsonObjectRequest cityjson = new WebJsonObjectRequest(Method.GET, getCityUrl, new JSONObject(), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				
				if(response.optInt("cities_count")>0){
					
					JSONArray resArr = response.optJSONArray("result");
					
					UsersDataBean cityBean;
					for (int i = 0; i < resArr.length(); i++) {
						
						JSONObject jobj = resArr.optJSONObject(i);
						cityBean = new UsersDataBean();
						
						cityBean.setUserCity(jobj.optString("city_name"));
						cityBean.setUserCityId(jobj.optString("city_id"));
						cityBean.setUserCountryId(jobj.optString("country_id"));
						cityBean.setUserCountry(jobj.optString("country_name"));
						citiesList.add(cityBean);
					}
					
				totalWebServiceResults = response.optInt("cities_count");
				totalPages = response.optInt("page_count");
				
				}
				for (int i = 0; i < citiesList.size(); i++) {
					
					String tp = city+" == "+citiesList.get(i).getUserCity();
					//Log.e("TP", city+" == "+citiesList.get(i).getUserCity());
					if(city.equals(citiesList.get(i).getUserCity())){
						UsersDataBean ubBean = Utilities.getCurrentUserFromSharedPref(ack);
						ubBean.setUserCity(citiesList.get(i).getUserCity());
						ubBean.setUserCityId(citiesList.get(i).getUserCityId());
						ubBean.setUserCountry(citiesList.get(i).getUserCountry());
						ubBean.setUserCountryId(citiesList.get(i).getUserCountryId());
						Utilities.writeCurrentUserToSharedPref(ack, ubBean);
						isCityFound=true;
						break;
						
					}
					
				}
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				
			}
		});
		
		VolleyRequest.addJsonObjectRequest(ack, cityjson);
		
		return isCityFound;
	}
	public static void setBackClick(Toolbar toolBar,final Activity ack)
    {
		final RelativeLayout back_layout;
		final ImageView imgBack;
		back_layout = (RelativeLayout)toolBar.findViewById(R.id.back_layout);
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
		
		back_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			ack.finish();	
			InputMethodManager imm = (InputMethodManager) 
					ack.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(back_layout.getWindowToken(), 0);
			}
		});
		
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			ack.finish();	
			InputMethodManager imm = (InputMethodManager) 
					ack.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(imgBack.getWindowToken(), 0);
			}
		});
    }
	
	public static void addToSearchHistory(String search,Activity ack,int term_type_id){
		String term_type="",serverUrl="",term="",addToSearchUrl="",addToSearchUrlPart1="";
		UsersDataBean ubBean = Utilities.getCurrentUserFromSharedPref(ack);
		int userId=0;
		userId = ubBean.getId();
		term_type = ack.getString(R.string.term_type);
		final String search1 = search;
		try {
			search = URLEncoder.encode(search,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		term = ack.getString(R.string.term);
		addToSearchUrlPart1= ack.getString(R.string.addToSearchHistoryPart1);
		serverUrl = ack.getString(R.string.serverUrl);		
		addToSearchUrl = serverUrl+addToSearchUrlPart1+userId+term_type+term_type_id+term+search;
	
		WebJsonObjectRequest addToHisJson = new WebJsonObjectRequest(Method.GET, addToSearchUrl, new JSONObject(), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				
				if(!(response.has("error"))){
					Utilities.writeToLogFIle("added to Search History "+search1+" result "+response.optString("message"));
				}
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Utilities.writeToLogFIle("Not added to Search History "+search1+" onError "+error.toString());
				//Log.e("ERROR", new String(error.networkResponse.data));
				
			}
		},true);
		
		VolleyRequest.addJsonObjectRequest(ack, addToHisJson);
	}
	
	public static String getDayOfWeek(int day){
		String dayOfWeek="";
		switch (day) {
		case 1:
			return "SUN";
			
		case 2:
			return "MON";
			
		case 3:
			return "TUE";
			
		case 4:
			return "WED";
			
		case 5:
			return "THU";
			
		case 6:
			return "FRI";
			
		case 7:
			return "SAT";		
			
		default:
			return "";
			
		}
		
	}
	
	public static String getMonthOfYear(int month){
		String monthOfYear="";
		switch (month) {
		case 0:
			return "Jan";
		case 1:
			return "Feb";
		case 2:
			return "Mar";
		case 3:
			return "Apr";
		case 4:
			return "May";
		case 5:
			return "Jun";
		case 6:
			return "Jul";
		case 7:
			return "Aug";
		case 8:
			return "Sep";
		case 9:
			return "Oct";
		case 10:
			return "Nov";
		case 11:
			return "Dec";
		default:
			return "";
		
		}
	}
	
	public static void addRemoveFavourites(View v,final Activity context,final ProductDataBean prods){
		favs = new ArrayList<ProductDataBean>();
		isAddedAlready =false;
		if (Utilities.getUserStatusFromSharedPref(context) == UserStatusEnum.LOGGED_IN ||
				(Utilities.getUserStatusFromSharedPref(context) == UserStatusEnum.LOGGED_IN_FB) ||
				(Utilities.getUserStatusFromSharedPref(context) == UserStatusEnum.LOGGED_IN_GOOGLE))
			{
		final int viewid = (int) v.getTag();
		//Toast.makeText(context, ""+viewid, 100).show();
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(context);
		final int userId = ubean.getId();
		//favs = new ArrayList<ProductDataBean>();
		favs = Utilities.getFavourites(context);
		//favs = Okler.getInstance().getFavourites();
		final RelativeLayout rl = (RelativeLayout)v;
	for(int i=0;i<favs.size();i++){
		if(viewid==favs.get(i).getProdId()){
			isAddedAlready=true;
			break;
		}
	}
	if(isAddedAlready){
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle("Alert");
		alertDialog.setMessage("Are you sure, you want to remove this product from your favourites?");
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				final String delete_fav = context.getString(R.string.delete_fav_url) +userId+context.getString(R.string.getMedsUrlProdId3)+viewid;
				//	Toast.makeText(getApplicationContext(), "id"+ viewid, Toast.LENGTH_LONG).show();
									         
				        	 WebJsonObjectRequest webjson=new WebJsonObjectRequest(Method.GET, delete_fav, new JSONObject(),new Listener<JSONObject>() 
										{
											@Override
											public void onResponse(JSONObject response) 
											{
												// TODO Auto-generated method stub
												
												
												JSONObject responseObj =(JSONObject)response;
												String result = responseObj.optString("result");
												String message = responseObj.optString("message"); 
									//			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
												
										//		Toast.makeText(getApplicationContext(), "result" + result, Toast.LENGTH_LONG).show();
												
												if(message.equals("deleted success from favourites"))
												{
													//array1 = Okler.getInstance().getFavourites();
													favs = Utilities.getFavourites(context);
													//favs = Okler.getInstance().getFavourites();
													for(int i = 0; i<favs.size(); i++)
													{
														if(viewid==favs.get(i).getProdId()){
															favs.remove(i);
															Utilities.writeFavouritesToSharedPref(context, favs);
															//Okler.getInstance().setFavourites(favs);
															ImageView image_favourite = (ImageView) rl.findViewById(R.id.image_favourite);	
					 	 									image_favourite.setBackgroundResource(R.drawable.favourites);
					 	 									Toast.makeText(context, "Product Successfully Removed From Your Favourites", Toast.LENGTH_SHORT).show();
					 	 									isAddedAlready=false;
														}
														
													}
												}else{
													Toast.makeText(context, "Failed To Remove Product From Your Favourites", Toast.LENGTH_SHORT).show();
												}
												
												
										
											}}, 
											new Response.ErrorListener() 
											{

												@Override
												public void onErrorResponse(VolleyError error) 
												{
													Log.i("error", String.valueOf(error));
													Toast.makeText(context, "Failed To Remove Product From Your Favourites", Toast.LENGTH_SHORT).show();
										
												}
											}
								);
							
						VolleyRequest.addJsonObjectRequest(context,webjson);
				
			}
		});
		
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				dialog.dismiss();
				
			}
		});
		
		alertDialog.show();
	}else{
			String add_fav = context.getString(R.string.add_fav_url)
					+ context.getString(R.string.cust_id) + userId + context.getString(R.string.getMedsUrlProdId3)+viewid;
		    	
		    	
		    	WebJsonObjectRequest webjson=new WebJsonObjectRequest(Method.GET, add_fav, new JSONObject(),new Listener<JSONObject>() 
						{
							@Override
							public void onResponse(JSONObject response) 
							{
								// TODO Auto-generated method stub
								
								
								JSONObject responseObj =(JSONObject)response;
								String message = responseObj.optString("message");
									
								if(message.equals("Product Successfully Added To Your Favorites"))
								{
									favs = Utilities.getFavourites(context);
									//favs = Okler.getInstance().getFavourites();
									//for(int i = 0;i<prods.size();i++){
										//if(prods.get(i).getProdId()==viewid){
											favs.add(prods);
										//}
									//}
									Utilities.writeFavouritesToSharedPref(context, favs);		
									//Okler.getInstance().setFavourites(favs);
								ImageView image_favourite = (ImageView) rl.findViewById(R.id.image_favourite);	
									image_favourite.setBackgroundResource(R.drawable.fav_filled_heart_icon);
									Toast.makeText(context, message, Toast.LENGTH_LONG).show();
									//holder.image_favourite.setEnabled(false);
									//holder.image_favourite_filled.setVisibility(View.VISIBLE);
									//holder.image_favourite_filled.setEnabled(true);
								}else if(message.equals("Product Already Exists In Your Favorites")){
									Toast.makeText(context, message, Toast.LENGTH_LONG).show();
								}
								else{
									Toast.makeText(context, "Failed To Add Product To Your Favorites", Toast.LENGTH_LONG).show();
								}
								
						
							}}, 
							new Response.ErrorListener() 
							{

								@Override
								public void onErrorResponse(VolleyError error) 
								{
									
									Toast.makeText(context, "Failed To Add Product To Your Favorites", Toast.LENGTH_LONG).show();
						
								}
							}
				);
					
				VolleyRequest.addJsonObjectRequest(context,webjson);
			
	}
	}else{
		
			Intent in = new Intent(context, NewSignIn.class);
			context.startActivity(in);
		}
	}
	
	public static ProductDataBean getProdBean(String json){
		ProductDataBean pBean=new ProductDataBean();
		try {
			JSONObject job = new JSONObject(json);
			pBean.setProdId(job.optInt("prodId"));
			pBean.setPresc_needed(job.optInt("presc_needed"));
			pBean.setUnits(job.optInt("units"));
			pBean.setProdType(job.optInt("prodType"));
			pBean.setProdName(job.optString("prodName"));
			pBean.setDesc(job.optString("desc"));
			pBean.setSide_effect(job.optString("side_effect"));
			pBean.setCompany(job.optString("company"));
			pBean.setDosage(job.optString("dosage"));
			pBean.setImgUrl(job.optString("imgUrl"));
			pBean.setIndications(job.optString("indications"));
			pBean.setContraIndi(job.optString("contraIndi"));
			pBean.setCaution(job.optString("caution"));
			pBean.setWeight(job.optString("weight"));
			pBean.setGeneric_name(job.optString("generic_name"));
			pBean.setInteracions(job.optString("interacions"));
			pBean.setDiet_rest(job.optString("diet_rest"));
			pBean.setComposition(job.optString("composition"));
			pBean.setPacking_size(job.optString("packing_size"));
			pBean.setKey_feature(job.optString("key_feature"));
			pBean.setSpecfic(job.optString("specfic"));
			pBean.setBrandInfo(job.optString("brandInfo"));
			pBean.setWarranty(job.optString("warranty"));
			pBean.setMrp(Float.parseFloat(job.opt("mrp").toString()));
			pBean.setOklerPrice(Float.parseFloat(job.opt("oklerPrice").toString()));
			pBean.setDiscount(Float.parseFloat(job.opt("discount").toString()));
			pBean.setCart_item_id(job.optInt("cart_item_id"));
			pBean.setTax(Float.parseFloat(job.opt("tax").toString()));
			pBean.setPresc_id(job.optString("presc_id"));
			pBean.setIs_presc_upped(job.optInt("is_presc_upped"));
			pBean.setCart_id(job.optString("cart_id"));
			pBean.setCart_num(job.optString("cart_num"));
			pBean.setFavourite(job.optBoolean("isFavourite"));
			pBean.setMediumUrl(job.optString("mediumUrl"));
			pBean.setThumbUrl(job.optString("thumbUrl"));
			pBean.setSmallUrl(job.optString("SmallUrl"));
			pBean.setClipArtUrl(job.optString("clipArtUrl"));
			
			return pBean;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return new ProductDataBean();
		}
		
	}
}
