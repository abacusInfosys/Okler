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
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.okler.adapters.HealthShopGridAdapter;
import com.okler.adapters.HealthShopListAdapter;
import com.okler.databeans.BrandsDataBean;
import com.okler.databeans.CategoriesDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.SubCategoriesDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class HealthShopListView extends BaseActivity implements SearchView.OnQueryTextListener,OnScrollListener,Response.Listener,Response.ErrorListener {

	ListView listview1;
	Context context;    
    ArrayList prgmName;    
    private Button notifCount;
    public static String [] prgmNameList={"1 kg weight cuff","1 kg weight cuff","1 kg weight cuff","1 kg weight cuff","1 kg weight cuff","1 kg weight cuff"};
    public static int [] prgmImages={R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera};    
    View bottomBarLayout;
	ImageLoader mImageLoader;
	Toolbar toolBar;
	ImageView imgBack,imageBrandleft;
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	LinearLayout linParent;
    ArrayList<CategoriesDataBean> cats;
    ArrayList<SubCategoriesDataBean> subCats,selectedCatSubCats;
    ArrayList<String> priceRanges;
    ArrayList<BrandsDataBean> brandsData;
    int cnt=0;
    ScrollView scrT;
    ImageView filter;
    SearchView searchView;
    BrandsDataBean brndHS = null;	  
  //  boolean isOngoingSearch;
    int lastBrandsPage = 1;
    ArrayList<BrandsDataBean>hsBrands = new ArrayList<BrandsDataBean>();
  ArrayList<BrandsDataBean>   bbean = new ArrayList<BrandsDataBean>();
  Activity act;
  boolean response2 ;
  String medUrl1,medUrl2,medUrl3,medUrl4,medUrl5,medUrl6,priceRange,getAllMedsUrl;
  int prodtype = 1,totalResultsFromWebService=0;
  ArrayList<ProductDataBean> finallist;//,searchList;
  String cate_id, brand_id;
  int pageCount=0;
  String searchText="",priceRangeVal="",priceSortVal="",brandIdVal="",catIdVal="";
  LinearLayout progressLinLayout;
  HealthShopListAdapter adapter;
  Button toolBarCount;
  TextView no_item;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_shop_list_view);
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		notifCount = (Button)toolBar.findViewById(R.id.toolBarCount);
	       context=getApplicationContext();
	       mImageLoader = VolleyRequest.getInstance(context).getImageLoader();
	       setSupportActionBar(toolBar);
	       no_item = (TextView) findViewById(R.id.no_item);
	       final ActionBar ab=getSupportActionBar();
	       act=this;
	    //   ab.setDisplayHomeAsUpEnabled(true);
	       toolBar.setBackgroundResource(R.drawable.custom_view_grad_healthshop);
	       imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       progressLinLayout = (LinearLayout)findViewById(R.id.progressLinLayout);
	       act = this;
	       finallist = new ArrayList<ProductDataBean>();		
		
		   totalResultsFromWebService = getIntent().getIntExtra("totalResultsFromWebService", 0);
		   searchText= getIntent().getStringExtra("searchText");
		 
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(act,ServiceCategoryActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			    imm.hideSoftInputFromWindow(imgBack.getWindowToken(), 0);

				
				
	//finish();	
			}
		});
	    	getAllMedsUrl = setUrl("0", "", "", "1", "", "", "", "", "", "", "");
	      Utilities.setTitleText(toolBar, "Health Shop");
	      filter = (ImageView)toolBar.findViewById(R.id.filterIcon);
	       filter.setVisibility(View.GONE);
	       filter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setFiltersInRightMenu();
				mDrawerLayout.openDrawer(Gravity.END);
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			}
		});
	      ImageView listingIcon = (ImageView)toolBar.findViewById(R.id.listingIcon);
	      listingIcon.setImageResource(R.drawable.list_icon);
		    listingIcon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent startListing = new Intent(getApplicationContext(), HealthShopSingleItemDetail.class);
					startListing.putExtra("totalResultsFromWebService", totalResultsFromWebService);
					startActivity(startListing);
				}
			});	    
		    
		    
		    ImageView overflow =(ImageView) toolBar.findViewById(R.id.overflowIcon);
		       overflow.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "Overflow", Toast.LENGTH_LONG).show();
					setCategoriesInRightMenu();
					if(mDrawerLayout.isDrawerOpen(Gravity.END))
					{
						mDrawerLayout.closeDrawer(Gravity.END);;
					}else{
					mDrawerLayout.openDrawer(Gravity.END);
					}
				}
			});
		       
		       mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		       drawerSetUp();
	       bottomBarLayout = findViewById(R.id.bottombar);
	       handleMapping(bottomBarLayout);
	       FrameLayout frame=(FrameLayout) toolBar.findViewById(R.id.frame_layout);
	       RelativeLayout parentSearchLayout=(RelativeLayout) frame.findViewById(R.id.parentSearchLayout);
	       parentSearchLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchView.setIconifiedByDefault(false);
				searchView.requestFocus();
			}
		});
	       searchView=(SearchView)parentSearchLayout.findViewById(R.id.menu_search);
	      setUpSearchView();
	       searchView.setOnQueryTextListener(this);
	       searchView.setQueryHint("Search");
	       if(!searchText.isEmpty())
		   {
			  // isOngoingSearch = true;
			 //  searchList = Okler.getInstance().getProdList();
			  // searchPageCount = searchList.size()/10;
		   }

		listview1 = (ListView) findViewById(R.id.listView);
		finallist =Okler.getInstance().getProdList();
		
		
		listview1.setOnScrollListener(this);
		listview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ArrayList<ProductDataBean> databean=Okler.getInstance().getProdList();
				ProductDataBean	databean1=databean.get(position);
					Gson gson=new Gson();
					String hsbean=gson.toJson(databean1);
					Intent intent=new Intent(context,ProductDetailsActivity.class);
					intent.putExtra("MedId",hsbean);
					startActivity(intent);
			}
		});
		linParent = (LinearLayout)findViewById(R.id.sliderManuParentLinear);
		
		   cats = Okler.getInstance().getCategoriesBean();
	        subCats = Okler.getInstance().getSubCategories();
	        brandsData = Okler.getInstance().getHsBrands();
	        selectedCatSubCats = new ArrayList<SubCategoriesDataBean>();
	        scrT = (ScrollView)findViewById(R.id.scrT);
	       //Set Categories in right menu
	       
	       if(cats.size()!=0)
	       {
	    	   setCategoriesInRightMenu();
	    	   setSubCategories(cats.get(0).getCatId());
	       }
	       
	       mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	       toolBarCount = (Button)toolBar.findViewById(R.id.toolBarCount);
	       toolBarCount.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				Intent i = new Intent(getApplicationContext(), MyCart.class);
				startActivity(i);
			}
		});
	 //      setNextBrands(lastBrandsPage);
		   adapter = new HealthShopListAdapter(act, Okler.getInstance().getProdList(),toolBarCount);
		//   searchAdapter = new HealthShopListAdapter(act, Okler.getInstance().getProdList(),toolBarCount);
		   listview1.setAdapter(adapter);
	      
	       
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		// TODO Auto-generated method stub
		Intent i=new Intent(act,ServiceCategoryActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		
//finish();	
	
	}
	private void setUpSearchView() {
		// TODO Auto-generated method stub
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint("Search");		
		
	}
	private void setCategoriesInRightMenu()
	{	
		try
		{
		ImageLoader imgLoader;
		LinearLayout rightMenuCats = (LinearLayout)findViewById(R.id.rightMenuCats);
		rightMenuCats.removeAllViews();
		final View[] views = new View[cats.size()];
		final int size = cats.size();
		//View[] separatorView = new View[cats.size()]; 
		for( cnt = 0; cnt< cats.size();cnt++)
		{
			views[cnt] = getLayoutInflater().inflate(R.layout.include_category_1, null);
			views[cnt].setTag(cats.get(cnt).getCatId());			
			//separatorView[cnt] = getLayoutInflater().inflate(R.layout.separator_1dp, null);			
			TextView txtCategoryName =(TextView) views[cnt].findViewById(R.id.mainCategory);
			NetworkImageView  imgCategoryIcon = (NetworkImageView)views[cnt].findViewById(R.id.rightItem);
			imgLoader = VolleyRequest.getInstance(context).getImageLoader();
			String url=cats.get(cnt).getImagePath();
			txtCategoryName.setText(cats.get(cnt).getCategoryName());
			imgCategoryIcon.setImageUrl(url, imgLoader);
			rightMenuCats.addView(views[cnt]);
			//rightMenuCats.addView(separatorView[cnt]);
		}
		views[0].setBackgroundColor(getResources().getColor(R.color.LightGreyWhite));
		for(cnt=0;cnt< cats.size();cnt++)
		{
			views[cnt].setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setSubCategories(v.getTag().toString());
					//Make others background color white and selected category color as grey				
					
					for(int tempCnt = 0 ;tempCnt < size;tempCnt++)
					{
						if(!views[tempCnt].getTag().equals(v.getTag()))
							views[tempCnt].setBackgroundResource(R.color.White);
						else
							views[tempCnt].setBackgroundResource(R.color.LightGreyWhite);
					}
				}
			});
		}
		}catch(Exception ex)
		{
			Log.e("Error", ex.getStackTrace().toString());
		}
	}

	
	private void setSubCategories(String catId)
	{
		try
		{
		populateSubCategories(catId);
		LinearLayout rightMenuSubCats = (LinearLayout)findViewById(R.id.rightMenuSubcats);
		rightMenuSubCats.removeAllViews();
		View[] views = new View[selectedCatSubCats.size()];
		View[] separatorView = new View[selectedCatSubCats.size()]; 
		for(int cnt = 0; cnt< selectedCatSubCats.size();cnt++)
		{
			views[cnt] = getLayoutInflater().inflate(R.layout.include_subcategory, null);			
			separatorView[cnt] = getLayoutInflater().inflate(R.layout.separator_1dp, null);
			views[cnt].setTag(cnt);
			views[cnt].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					filter.setVisibility(View.VISIBLE);
					// TODO Auto-generated method stub
					mDrawerLayout.closeDrawer(Gravity.END);
					
					TextView txtSubCategoryName1 =(TextView) v.findViewById(R.id.subCategory);
					
					String txt_name = txtSubCategoryName1.getText().toString();
					//Toast.makeText(getApplicationContext(), txt_name, Toast.LENGTH_LONG).show();
					
					int name = (int) v.getTag();
					//Toast.makeText(getApplicationContext(), "tag:"+name, Toast.LENGTH_LONG).show();
					for (int i = 0; i < selectedCatSubCats.size(); i++) 
					{
						if(txt_name == (selectedCatSubCats.get(i).getSubCateName()))
						{
							cate_id =  (selectedCatSubCats.get(i).getSubCateId());
							catIdVal = cate_id;
							brandIdVal="";
							priceRangeVal="";
							priceSortVal="";
							//Toast.makeText(getApplicationContext(), cate_id, Toast.LENGTH_LONG).show();
							getAllMedsUrl = setUrl("0", "", "", "1", "", "", "", cate_id, "", "", "");
							//String cate_url = "https://www.okler.com/api/products/products/productsbyprotype?page=&search=&prod_id=&pro_type=1&order_by=desc&price_range=&brand_id=&subcat_id="+cate_id+"&sort_price=asc";
							finallist.clear();
							//searchList.clear();
							searchText="";
							pageCount=0;
							WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, getAllMedsUrl, new JSONObject(), new Listener<JSONObject>() 
							{
								@Override
								public void onResponse(JSONObject response) 
								{
					
									ProductDataBean hsBean;
					showProgress(false);
									String resp = String.valueOf(response);
									//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
									JSONObject jobj = (JSONObject)response;
									JSONObject jobj1 = new JSONObject();
									try {
											jobj1 = jobj.getJSONObject("result");
											totalResultsFromWebService = jobj1.getInt("total_count");
											if(totalResultsFromWebService == 0)
											{
												no_item.setVisibility(View.VISIBLE);														
											}
											else
											{
												processResponse(jobj1,jobj);
												no_item.setVisibility(View.GONE);
											}
											
										} 	
									catch (JSONException e1) 
										{
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
								/*	JSONObject jobj2 = new JSONObject();
									JSONObject jobj3 = new JSONObject();
									JSONArray jArray = new JSONArray();
									hsBean = null;
									int lenobj = jobj1.length();
								//	Toast.makeText(getApplicationContext(), "Obj len = "+lenobj, Toast.LENGTH_LONG).show();
									finallist = new ArrayList<ProductDataBean>();
					
									try {
											for (int i = 0; i < lenobj-3 ; i++) 
											{
												//jArray = jobj.getJSONArray("");
												jobj2 = jobj1.getJSONObject(i+"");
												int len = jobj2.length();
												hsBean = new ProductDataBean();
												hsBean.setProdName(jobj2.getString("name"));
											
												hsBean.setDesc(jobj2.getString("description"));
						
												hsBean.setMrp(jobj2.getInt("price"));
												hsBean.setOklerPrice(jobj2.getInt("saleprice"));
												hsBean.setDiscount(jobj2.getInt("discount"));
												String jimg = jobj2.getString("images");
												String url2;
												if(jimg.equals(null)||jimg.equals("null")||jimg.equals(""))
												{
													url2 = "drawable://" + R.drawable.tempcuff;
												}
												else
												{
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
												hsBean.setImgUrl(url2);
							
												finallist.add(hsBean);
						
					//	Toast.makeText(getApplicationContext(), "array len = "+len, Toast.LENGTH_LONG).show();
											}
											
											JSONArray array = jobj1.getJSONArray("range");
											int length = array.length();				
											ArrayList<String> array1 = new ArrayList<String>();				
											for(int j = 0; j<length-3; j ++)
											{
												array1.add((String) array.get(j));
											}
											
											Okler.getInstance().setPriceRanges(array1);
											
											
											Okler.getInstance().setProdList(finallist);
											 = new HealthShopGridAdapter(act, finallist);
											listview1.setAdapter(healthshop);
						
						//isMedAdap("med_prodAdap");
									} 
									catch (JSONException e) 
									{
										String er = String.valueOf(e);
									//	Toast.makeText(getApplicationContext(), er, Toast.LENGTH_LONG).show();
										e.printStackTrace();
									}*/
							
							}},
				new Response.ErrorListener() 
				{

					@Override
					public void onErrorResponse(VolleyError error) 
					{
						// TODO Auto-generated method stub
						showProgress(false);
					}
				}
	);
		
	if(VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson))
		showProgress(true);
	else
		showProgress(false);

						}
						
				/*	setFiltersInRightMenu();
					linParent.setVisibility(View.GONE);
					sliderManuParentLinear1.setVisibility(View.VISIBLE);*/
				}
				
				
				
				}
				
			});
			TextView txtSubCategoryName =(TextView) views[cnt].findViewById(R.id.subCategory);			
			txtSubCategoryName.setText(selectedCatSubCats.get(cnt).getSubCateName());		
			rightMenuSubCats.addView(views[cnt]);
			rightMenuSubCats.addView(separatorView[cnt]);
		 }
		}catch(Exception ex)
		{
			Log.e("AA", ex.getStackTrace().toString());
		}
	
		
			/*	if(response2)
				{*/
				
				
				
				/*}
				else
				 Toast.makeText(getApplicationContext(), "Some error occurred while fetching brands", Toast.LENGTH_LONG).show();*/
			
	}
	
	private void setNextBrands(int pageNum)
	{
		
		 response2 = false;
			
				//final int tempJ = j;
				response2 = false;
				String brandsUrl = "https://www.okler.com/api/products/products/AllBrands?medicine_type_id=1&page="+pageNum+"&search=";
				WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, brandsUrl, new JSONObject(),	new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) 
					{
						showProgress(false);
						response2 = true;
						try {
							JSONArray resultsArr = response.getJSONArray("result");
							 bbean.clear();
							for(int j1 = 0 ;j1< resultsArr.length();j1++)
							{
								brndHS = new BrandsDataBean();
								JSONObject subCates = resultsArr.getJSONObject(j1);
								brndHS.setBrandId(subCates.getString("id"));
								brndHS.setBrandName(subCates.getString("brand_name"));
								brandsData.add(brndHS);
							
							}
						//	brandsData.addAll(bbean);
							//Okler.getInstance().setHsBrands(hsBrands);
							Okler.getInstance().setHsBrands(brandsData);
							populateBrands();
							lastBrandsPage++;
							/*if (tempJ == pageLength)
							{
								populateBrands();
							}*/
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
						response2 = false;
						showProgress(false);
							Log.e("error", ""+error.getMessage());
							
						}					
				});
				if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson))
					showProgress(true);
				else
					showProgress(false);
				
		//	}
	}
	private void populateSubCategories(String catId)
	{
		selectedCatSubCats = new ArrayList<SubCategoriesDataBean>();
		//Get number of subcategories for given category
		for(int cntSubCat = 0 ; cntSubCat <subCats.size();cntSubCat++)
		{
			if(subCats.get(cntSubCat).getCateId().equals(catId))
			{
				selectedCatSubCats.add(subCats.get(cntSubCat));	
			}
		}
	}
	private void setFiltersInRightMenu()
	{
		try
		{
		LinearLayout rightMenuCats = (LinearLayout)findViewById(R.id.rightMenuCats);
		rightMenuCats.removeAllViews();
		final View[] views = new View[3];
		final int size = cats.size();
		//View[] separatorView = new View[cats.size()]; 
		for( cnt = 0; cnt< 3;cnt++)
		{
			views[cnt] = getLayoutInflater().inflate(R.layout.include_category_2, null);
			views[cnt].setTag(cnt);			
			//separatorView[cnt] = getLayoutInflater().inflate(R.layout.separator_1dp, null);			
			TextView txtCategoryName =(TextView) views[cnt].findViewById(R.id.subCategory);
			ImageView imgCategoryIcon = (ImageView)views[cnt].findViewById(R.id.rightItem);
			String str="";
			switch(cnt)
			{
			case 0 : str = "BRAND";
			break;
			case 1 : str = "PRICE RANGE";
			break;
			case 2: str = "PRICE";
					break;
			}
			txtCategoryName.setText(str);		
			rightMenuCats.addView(views[cnt]);
			//rightMenuCats.addView(separatorView[cnt]);
		}
		views[0].setBackgroundColor(getResources().getColor(R.color.LightGreyWhite));
		for(cnt=0;cnt< 3;cnt++)
		{
			views[cnt].setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setFilterSubCategories(v.getTag().toString());
					//Make others background color white and selected category color as grey
					
					
					for(int tempCnt = 0 ;tempCnt < 3;tempCnt++)
					{
						if(!views[tempCnt].getTag().equals(v.getTag()))
							views[tempCnt].setBackgroundResource(R.color.White);
						else
							views[tempCnt].setBackgroundResource(R.color.LightGreyWhite);
					}
				}
			});
		}
		setFilterSubCategories("0");
		}catch(Exception ex)
		{
			Log.e("AA", ex.getStackTrace().toString());
		}
	}
	
	private void setFilterSubCategories(String catId)
	{
		switch(catId)
		{
		case "0": // populate Brands
			populateBrands();
			break;
		case "1": //populate price ranges
			populatePriceRanges();
			break;
		case "2"://populate Price
			populatePrices();
			break;
		}
		
	/*	populateSubCategories(catId);
		LinearLayout rightMenuSubCats = (LinearLayout)findViewById(R.id.rightMenuSubcats);		
		rightMenuSubCats.removeAllViews();
		View[] views = new View[3];
		View[] separatorView = new View[3]; 
		for(int cnt = 0; cnt<3;cnt++)
		{
			views[cnt] = getLayoutInflater().inflate(R.layout.include_category_2, null);			
			separatorView[cnt] = getLayoutInflater().inflate(R.layout.separator_1dp, null);
			views[cnt].setTag(cnt);
			TextView txtSubCategoryName =(TextView) views[cnt].findViewById(R.id.subCategory);			
			txtSubCategoryName.setText("Brnd1");		
			rightMenuSubCats.addView(views[cnt]);
			rightMenuSubCats.addView(separatorView[cnt]);
		}*/
	}
	
	private void populateBrands()
	{
		try{
		LinearLayout rightMenuSubCats = (LinearLayout)findViewById(R.id.rightMenuSubcats);		
		rightMenuSubCats.removeAllViews();
		View[] views = new View[brandsData.size()];
		View[] separatorView = new View[brandsData.size()]; 
		for(int cnt = 0; cnt< brandsData.size();cnt++)
		{
			views[cnt] = getLayoutInflater().inflate(R.layout.include_category_2, null);			
			separatorView[cnt] = getLayoutInflater().inflate(R.layout.separator_1dp, null);
			imageBrandleft=(ImageView)views[cnt].findViewById(R.id.rightItem);
			imageBrandleft.setImageDrawable(getResources().getDrawable(R.drawable.right_grey_tick));
			views[cnt].setTag(brandsData.get(cnt).getBrandId());
			views[cnt].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					TextView txtSubCategoryName2 =(TextView) v.findViewById(R.id.subCategory);
					imageBrandleft=(ImageView)v.findViewById(R.id.rightItem);
					imageBrandleft.setImageDrawable(getResources().getDrawable(R.drawable.right_grey_tick));
					
					String txt_name = txtSubCategoryName2.getText().toString();
					//Toast.makeText(getApplicationContext(), txt_name, Toast.LENGTH_LONG).show();
					
					String name = (String) v.getTag();
					//Toast.makeText(getApplicationContext(), "tag:"+name, Toast.LENGTH_LONG).show();
					for (int i = 0; i < brandsData.size(); i++) 
					{
						if(txt_name == (brandsData.get(i).getBrandName()))
						{
							brand_id =  (brandsData.get(i).getBrandId());
						//	Toast.makeText(getApplicationContext(), brand_id, Toast.LENGTH_LONG).show();
							
							getAllMedsUrl = setUrl("0", "", "", "1", "", brand_id, "", cate_id, "", "", "");
							finallist.clear();
						//	searchList.clear();
						//	searchPageCount=0;
							pageCount=0;
							brandIdVal=brand_id;
							priceRangeVal="";
							priceSortVal="";
							catIdVal="";
							searchText="";
					//		String brand_url = "https://www.okler.com/api/products/products/productsbyprotype?page=&search=&prod_id=&pro_type=1&order_by=desc&price_range=&brand_id="+brand_id+"&subcat_id="+ cate_id+"&sort_price=asc";
							
							WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, getAllMedsUrl, new JSONObject(), new Listener<JSONObject>() 
									{
										@Override
										public void onResponse(JSONObject response) 
										{			showProgress(false);					
											ProductDataBean hsBean;							
											String resp = String.valueOf(response);
											//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
											JSONObject jobj = (JSONObject)response;
											JSONObject jobj1 = new JSONObject();
											try {
													jobj1 = jobj.getJSONObject("result");
													totalResultsFromWebService = jobj1.getInt("total_count");
													if(totalResultsFromWebService == 0)
													{
														no_item.setVisibility(View.VISIBLE);
													}
													else
													{
														no_item.setVisibility(View.GONE);
														processResponse(jobj1,jobj);
													}
												} 	
											catch (JSONException e1) 
												{
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}
									/*		JSONObject jobj2 = new JSONObject();
											JSONObject jobj3 = new JSONObject();
											JSONArray jArray = new JSONArray();
											hsBean = null;
											int lenobj = jobj1.length();
										//	Toast.makeText(getApplicationContext(), "Obj len = "+lenobj, Toast.LENGTH_LONG).show();
											finallist = new ArrayList<ProductDataBean>();
							
											try {
													for (int i = 0; i < lenobj-3 ; i++) 
													{
														//jArray = jobj.getJSONArray("");
														jobj2 = jobj1.getJSONObject(i+"");
														int len = jobj2.length();
														hsBean = new ProductDataBean();
														hsBean.setProdName(jobj2.getString("name"));
													
														hsBean.setDesc(jobj2.getString("description"));
								
														hsBean.setMrp(jobj2.getInt("price"));
														hsBean.setOklerPrice(jobj2.getInt("saleprice"));
														hsBean.setDiscount(jobj2.getInt("discount"));
														String jimg = jobj2.getString("images");
														String url2;
														if(jimg.equals(null)||jimg.equals("null")||jimg.equals(""))
														{
															url2 = "drawable://" + R.drawable.tempcuff;
														}
														else
														{
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
														hsBean.setImgUrl(url2);
									
														finallist.add(hsBean);
								
								
								
								
							//	Toast.makeText(getApplicationContext(), "array len = "+len, Toast.LENGTH_LONG).show();
													}
													Okler.getInstance().setProdList(finallist);
													HealthShopGridAdapter healthshop = new HealthShopGridAdapter(act, finallist);
													listview1.setAdapter(healthshop);
								
								//isMedAdap("med_prodAdap");
											} 
											catch (JSONException e) 
											{
												String er = String.valueOf(e);
											//	Toast.makeText(getApplicationContext(), er, Toast.LENGTH_LONG).show();
												e.printStackTrace();
											}
									*/
									}},
						new Response.ErrorListener() 
						{

							@Override
							public void onErrorResponse(VolleyError error) 
							{
								// TODO Auto-generated method stub
								showProgress(false);
							}
						}
			);
				
			if(VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson))
				showProgress(true);
			else
				showProgress(false);
							
						}
						
					
					
					}
					mDrawerLayout.closeDrawer(Gravity.END);
					
				/*	setFiltersInRightMenu();
					linParent.setVisibility(View.GONE);
					sliderManuParentLinear1.setVisibility(View.VISIBLE);*/
				}
			});
			TextView txtSubCategoryName =(TextView) views[cnt].findViewById(R.id.subCategory);			
			txtSubCategoryName.setText(brandsData.get(cnt).getBrandName());		
			rightMenuSubCats.addView(views[cnt]);
			rightMenuSubCats.addView(separatorView[cnt]);
		}
		Button viewMore = new Button(this);
		viewMore.setWidth(LayoutParams.FILL_PARENT);
		viewMore.setHeight(LayoutParams.WRAP_CONTENT);
		viewMore.setText("View More");
		viewMore.setBackgroundResource(R.color.LightGrey);
		rightMenuSubCats.addView(viewMore);
		
		viewMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setNextBrands(lastBrandsPage);
			}
		});
		}catch(Exception ex)
		{
			Log.e("AA", ex.getStackTrace().toString());
		}
	}
	
	private void populatePriceRanges()
	{
		try{
		LinearLayout rightMenuSubCats = (LinearLayout)findViewById(R.id.rightMenuSubcats);		
		rightMenuSubCats.removeAllViews();
		
		ArrayList<String> prices = new ArrayList<String>();
		prices = Okler.getInstance().getPriceRanges();
		
		View[] views = new View[prices.size()];
		View[] separatorView = new View[prices.size()]; 
		for(int cnt = 0; cnt< prices.size();cnt++)
		{
			views[cnt] = getLayoutInflater().inflate(R.layout.include_category_1, null);			
			separatorView[cnt] = getLayoutInflater().inflate(R.layout.separator_1dp, null);
			views[cnt].setTag(cnt);
			imageBrandleft=(ImageView)views[cnt].findViewById(R.id.rightItem);
			imageBrandleft.setImageDrawable(getResources().getDrawable(R.drawable.right_grey_tick));
			views[cnt].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					TextView txtSubCategoryName =(TextView) v.findViewById(R.id.mainCategory);
					imageBrandleft=(ImageView)v.findViewById(R.id.rightItem);

					imageBrandleft.setImageDrawable(getResources().getDrawable(R.drawable.right_red_tick));
					String range = txtSubCategoryName.getText().toString();
					getAllMedsUrl = setUrl("0", "", "", "1", range, "", "", cate_id, "", "", "");
					finallist.clear();
					//searchList.clear();
					//searchPageCount=0;
					pageCount=0;
					brandIdVal="";
					priceRangeVal=range;
					priceSortVal="";
					catIdVal="";
					searchText="";
				//	String pRange = "https://www.okler.com/api/products/products/productsbyprotype?page=&search=&prod_id=&pro_type=1&order_by=desc&price_range="+range+"&brand_id=&subcat_id="+cate_id+"&sort_price=asc";
					
					WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, getAllMedsUrl, new JSONObject(), new Listener<JSONObject>() 
							{
								@Override
								public void onResponse(JSONObject response) 
								{
					
									ProductDataBean hsBean;
									showProgress(false);
									String resp = String.valueOf(response);
									//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
									JSONObject jobj = (JSONObject)response;
									JSONObject jobj1 = new JSONObject();
									try {
											jobj1 = jobj.getJSONObject("result");
											totalResultsFromWebService = jobj1.getInt("total_count");
											if(totalResultsFromWebService == 0)
											{
												no_item.setVisibility(View.VISIBLE);
											}
											else
											{
												no_item.setVisibility(View.GONE);
												processResponse(jobj1,jobj);
											}
											
										} 	
									catch (JSONException e1) 
										{
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
							/*		JSONObject jobj2 = new JSONObject();
									JSONObject jobj3 = new JSONObject();
									JSONArray jArray = new JSONArray();
									hsBean = null;
									int lenobj = jobj1.length();
								//	Toast.makeText(getApplicationContext(), "Obj len = "+lenobj, Toast.LENGTH_LONG).show();
									finallist = new ArrayList<ProductDataBean>();
					
									try {
											for (int i = 0; i < lenobj-3 ; i++) 
											{
												//jArray = jobj.getJSONArray("");
												jobj2 = jobj1.getJSONObject(i+"");
												int len = jobj2.length();
												hsBean = new ProductDataBean();
												hsBean.setProdName(jobj2.getString("name"));
											
												hsBean.setDesc(jobj2.getString("description"));
						
												hsBean.setMrp(jobj2.getInt("price"));
												hsBean.setOklerPrice(jobj2.getInt("saleprice"));
												hsBean.setDiscount(jobj2.getInt("discount"));
												String jimg = jobj2.getString("images");
												String url2;
												if(jimg.equals(null)||jimg.equals("null")||jimg.equals(""))
												{
													url2 = "drawable://" + R.drawable.tempcuff;
												}
												else
												{
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
												hsBean.setImgUrl(url2);
							
												finallist.add(hsBean);
						
						
						
						
					//	Toast.makeText(getApplicationContext(), "array len = "+len, Toast.LENGTH_LONG).show();
											}
											Okler.getInstance().setProdList(finallist);
											HealthShopGridAdapter healthshop = new HealthShopGridAdapter(act, finallist);
											listview1.setAdapter(healthshop);
						
						//isMedAdap("med_prodAdap");
									} 
									catch (JSONException e) 
									{
										String er = String.valueOf(e);
									//	Toast.makeText(getApplicationContext(), er, Toast.LENGTH_LONG).show();
										e.printStackTrace();
									}*/
							
							}},
				new Response.ErrorListener() 
				{

					@Override
					public void onErrorResponse(VolleyError error) 
					{
						// TODO Auto-generated method stub
						showProgress(false);
					}
				}
	);
		
	if(VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson))
		showProgress(true);
	else
		showProgress(false);
	}
			});
			
			//mDrawerLayout.closeDrawer(Gravity.END);
					
			
			TextView txtSubCategoryName =(TextView) views[cnt].findViewById(R.id.mainCategory);			
			txtSubCategoryName.setText(prices.get(cnt));		
			rightMenuSubCats.addView(views[cnt]);
			rightMenuSubCats.addView(separatorView[cnt]);
		}
		}catch(Exception ex)
		{
			Log.e("AA", ex.getStackTrace().toString());
		}
	}
	
	private void populatePrices()
	{
		try
		{
		LinearLayout rightMenuSubCats = (LinearLayout)findViewById(R.id.rightMenuSubcats);		
		rightMenuSubCats.removeAllViews();
		View[] views = new View[2];
		View[] separatorView = new View[2]; 
		for(int cnt = 0; cnt<2;cnt++)
		{
			views[cnt] = getLayoutInflater().inflate(R.layout.include_category_2, null);			
			separatorView[cnt] = getLayoutInflater().inflate(R.layout.separator_1dp, null);
			views[cnt].setTag(cnt);
			
			views[cnt].setOnClickListener(new OnClickListener() {			
			
				
				@Override
				public void onClick(View v) 
				{
					
					String id1 = "", id = "";
					
					// TODO Auto-generated method stub
					TextView txtSubCategoryName2 =(TextView) v.findViewById(R.id.subCategory);
					
					String txt_name = txtSubCategoryName2.getText().toString();
					ImageView imageOnLeft=(ImageView) findViewById(R.id.rightItem);
					if(txt_name == "Price - Low to High")
					{
						imageOnLeft.setImageDrawable(getResources().getDrawable(R.drawable.grey_up_arrow_icon));
						String asc = "asc";
				//		String price_url = "https://www.okler.com/api/products/products/productsbyprotype?page=&search=&prod_id=&pro_type=1&order_by=desc&price_range=&brand_id=&subcat_id="+cate_id+"&sort_price=asc";
						getAllMedsUrl = setUrl("0", "", "", "1", "", "", "", cate_id, asc, "", "");
						finallist.clear();
					//	searchList.clear();
					//	searchPageCount=0;
						pageCount=0;
						brandIdVal="";
						priceRangeVal="";
						priceSortVal="";
						catIdVal="";
						searchText="";
						WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, getAllMedsUrl, new JSONObject(), new Listener<JSONObject>() 
								{
									@Override
									public void onResponse(JSONObject response) 
									{
										showProgress(false);
										ProductDataBean hsBean;
						
										String resp = String.valueOf(response);
										//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
										JSONObject jobj = (JSONObject)response;
										JSONObject jobj1 = new JSONObject();
										try {
												jobj1 = jobj.getJSONObject("result");
												totalResultsFromWebService = jobj1.getInt("total_count");
												if(totalResultsFromWebService == 0)
												{
													no_item.setVisibility(View.VISIBLE);
												}
												else
												{
													no_item.setVisibility(View.GONE);
													processResponse(jobj1,jobj);
												}
												
											} 	
										catch (JSONException e1) 
											{
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
									/*	JSONObject jobj2 = new JSONObject();
										JSONObject jobj3 = new JSONObject();
										JSONArray jArray = new JSONArray();
										hsBean = null;
										int lenobj = jobj1.length();
								//		Toast.makeText(getApplicationContext(), "Obj len = "+lenobj, Toast.LENGTH_LONG).show();
										finallist = new ArrayList<ProductDataBean>();
						
										try {
												for (int i = 0; i < lenobj-3 ; i++) 
												{
													//jArray = jobj.getJSONArray("");
													jobj2 = jobj1.getJSONObject(i+"");
													int len = jobj2.length();
													hsBean = new ProductDataBean();
													hsBean.setProdName(jobj2.getString("name"));
												
													hsBean.setDesc(jobj2.getString("description"));
							
													hsBean.setMrp(jobj2.getInt("price"));
													hsBean.setOklerPrice(jobj2.getInt("saleprice"));
													hsBean.setDiscount(jobj2.getInt("discount"));
													String jimg = jobj2.getString("images");
													String url2;
													if(jimg.equals(null)||jimg.equals("null")||jimg.equals(""))
													{
														url2 = "drawable://" + R.drawable.tempcuff;
													}
													else
													{
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
													hsBean.setImgUrl(url2);
								
													finallist.add(hsBean);
							
							
							
							
						//	Toast.makeText(getApplicationContext(), "array len = "+len, Toast.LENGTH_LONG).show();
												}
												Okler.getInstance().setProdList(finallist);
												HealthShopGridAdapter healthshop = new HealthShopGridAdapter(act, finallist);
												listview1.setAdapter(healthshop);
							
							//isMedAdap("med_prodAdap");
										} 
										catch (JSONException e) 
										{
											String er = String.valueOf(e);
									//		Toast.makeText(getApplicationContext(), er, Toast.LENGTH_LONG).show();
											e.printStackTrace();
										}*/
								
								}},
					new Response.ErrorListener() 
					{

						@Override
						public void onErrorResponse(VolleyError error) 
						{
							// TODO Auto-generated method stub
							showProgress(false);
						}
					}
		);
			
		if(VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson))
			showProgress(true);
		else
			showProgress(false);

					}//if
					
					if(txt_name == "Price - High to Low")
					{
						String desc = "desc";
						getAllMedsUrl = setUrl("0", "", "", "1", "", "", "", cate_id, desc, "", "");
						finallist.clear();
					//	searchList.clear();
					//	searchPageCount=0;
						pageCount=0;
						brandIdVal="";
						priceRangeVal="";
						priceSortVal="";
						catIdVal="";
						searchText="";
					//	String brand_url = "https://www.okler.com/api/products/products/productsbyprotype?page=&search=&prod_id=&pro_type=1&order_by=desc&price_range=&brand_id=&subcat_id="+cate_id+"&sort_price="+desc;
						imageOnLeft.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow_grey_icon));
						WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, getAllMedsUrl, new JSONObject(), new Listener<JSONObject>() 
								{
									@Override
									public void onResponse(JSONObject response) 
									{
										showProgress(false);
										ProductDataBean hsBean;
						
										String resp = String.valueOf(response);
										//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
										JSONObject jobj = (JSONObject)response;
										JSONObject jobj1 = new JSONObject();
										try {
												jobj1 = jobj.getJSONObject("result");
												totalResultsFromWebService = jobj1.getInt("total_count");
												if(totalResultsFromWebService == 0)
												{
													no_item.setVisibility(View.VISIBLE);
												}
												else
												{
													no_item.setVisibility(View.GONE);
													processResponse(jobj1,jobj);
												}
												
											} 	
										catch (JSONException e1) 
											{
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
									/*	JSONObject jobj2 = new JSONObject();
										JSONObject jobj3 = new JSONObject();
										JSONArray jArray = new JSONArray();
										hsBean = null;
										int lenobj = jobj1.length();
									//	Toast.makeText(getApplicationContext(), "Obj len = "+lenobj, Toast.LENGTH_LONG).show();
										finallist = new ArrayList<ProductDataBean>();
						
										try {
												for (int i = 0; i < lenobj-3 ; i++) 
												{
													//jArray = jobj.getJSONArray("");
													jobj2 = jobj1.getJSONObject(i+"");
													int len = jobj2.length();
													hsBean = new ProductDataBean();
													hsBean.setProdName(jobj2.getString("name"));
												
													hsBean.setDesc(jobj2.getString("description"));
							
													hsBean.setMrp(jobj2.getInt("price"));
													hsBean.setOklerPrice(jobj2.getInt("saleprice"));
													hsBean.setDiscount(jobj2.getInt("discount"));
													String jimg = jobj2.getString("images");
													String url2;
													if(jimg.equals(null)||jimg.equals("null")||jimg.equals(""))
													{
														url2 = "drawable://" + R.drawable.tempcuff;
													}
													else
													{
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
													hsBean.setImgUrl(url2);
								
													finallist.add(hsBean);
							
							
							
							
						//	Toast.makeText(getApplicationContext(), "array len = "+len, Toast.LENGTH_LONG).show();
												}
												Okler.getInstance().setProdList(finallist);
												HealthShopGridAdapter healthshop = new HealthShopGridAdapter(act, finallist);
												listview1.setAdapter(healthshop);
							
							//isMedAdap("med_prodAdap");
										} 
										catch (JSONException e) 
										{
											String er = String.valueOf(e);
										//	Toast.makeText(getApplicationContext(), er, Toast.LENGTH_LONG).show();
											e.printStackTrace();
										}
								*/
								}},
					new Response.ErrorListener() 
					{

						@Override
						public void onErrorResponse(VolleyError error) 
						{
							// TODO Auto-generated method stub
							showProgress(false);
						}
					}
		);
			
		if(VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson))
			showProgress(true);
		else
			showProgress(false);
						

					}// end if hight to low
				}// onclick
			
			}); // end listener
			
			//mDrawerLayout.closeDrawer(Gravity.END);
			
			TextView txtSubCategoryName =(TextView) views[cnt].findViewById(R.id.subCategory);
			ImageView imageOnLeft=(ImageView)views[cnt].findViewById(R.id.rightItem);
			String str="";
			if(cnt == 0){
				str = "Price - Low to High";
				imageOnLeft.setImageDrawable(getResources().getDrawable(R.drawable.grey_up_arrow_icon));
				txtSubCategoryName.setText(str);
			}else{ 
				str = "Price - High to Low";
				imageOnLeft.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow_grey_icon));
				txtSubCategoryName.setText(str);
			}
				
			rightMenuSubCats.addView(views[cnt]);
			rightMenuSubCats.addView(separatorView[cnt]);
	}
		}catch(Exception ex)
		{
			Log.e("AA", ex.getStackTrace().toString());
		}
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.health_shop_list_view, menu);
	/*	MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.health_shop_list_view, menu);
		MenuItem item = menu.findItem(R.id.action_settings);
		item.setActionView(R.layout.feed_update_count);
	    //
		 View count = item.getActionView();
		    notifCount = (Button) count.findViewById(R.id.notif_count);
		    notifCount.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "btn clicked", Toast.LENGTH_LONG).show();
					Intent i = new Intent(getApplicationContext(),HealthShopSingleItemDetail.class);
					startActivity(i);
				}
			});
		    notifCount.setText(String.valueOf(5));*/
		return true;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(notifCount,act);
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
	
	private void drawerSetUp()
	{
		  mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
	                toolBar, //nav menu toggle icon
	                R.string.app_name, // nav drawer open - description for accessibility
	                R.string.app_name // nav drawer close - description for accessibility
	        ){
	            public void onDrawerClosed(View view) {
	                //getSupportActionBar().setTitle(mTitle);
	                // calling onPrepareOptionsMenu() to show action bar icons
	                invalidateOptionsMenu();
	                //		Toast.makeText(getApplicationContext(), "Drawer closed", Toast.LENGTH_LONG).show();	
	                		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	            }
	 
	            public void onDrawerOpened(View drawerView) {
	            //	Toast.makeText(getApplicationContext(), "Drawer open", Toast.LENGTH_LONG).show();	
	            	mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
	            }
	        };
	        mDrawerLayout.setDrawerListener(mDrawerToggle);
	        mDrawerToggle.setDrawerIndicatorEnabled(false); //disable "hamburger to arrow" drawable
	        //mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
	    //    mDrawerToggle.setDrawerIndicatorEnabled(true);
	}
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(linParent);
    //    menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
	
	 /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
      
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
		return false;
	}
	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		String verr=String.valueOf(error);
	System.out.println("VolleyError error");
	showProgress(false);
	}

	@Override
	public void onResponse(Object response) 
	{
		showProgress(false);
		ProductDataBean hsBean;
			String resp = String.valueOf(response);
			//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
			JSONObject jobj = (JSONObject)response;
			JSONObject jobj1 = new JSONObject();
			
			try {
				jobj1 = jobj.getJSONObject("result");
				totalResultsFromWebService = jobj1.getInt("total_count");
				processResponse(jobj1,jobj);
				JSONArray array = jobj1.getJSONArray("range");
				int length = array.length();				
				ArrayList<String> array1 = new ArrayList<String>();				
				for(int j = 0; j<length-3; j ++)
				{
					array1.add((String) array.get(j));
				}						
				Okler.getInstance().setPriceRanges(array1);	
			} catch (JSONException e1) {
			// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
				
	}
	private void processResponse(JSONObject jobj1,JSONObject jobj) throws JSONException
	{
		ProductDataBean hsBean;
	
		JSONObject jobj2 = new JSONObject();
		JSONObject jobj3 = new JSONObject();
		JSONArray jArray = new JSONArray();
		hsBean = null;
		int lenobj = jobj1.length();
		ArrayList<ProductDataBean> currResults = new ArrayList<ProductDataBean>();
	//	Toast.makeText(getApplicationContext(), "Obj len = "+lenobj, Toast.LENGTH_LONG).show();
	//	finallist = new ArrayList<ProductDataBean>();

		
				for (int i = 0; i < lenobj-3 ; i++) 
				{
					//jArray = jobj.getJSONArray("");
					jobj2 = jobj1.getJSONObject(i+"");
					int len = jobj2.length();
					hsBean = new ProductDataBean();
					hsBean.setProdName(jobj2.getString("name"));
				
					hsBean.setDesc(jobj2.getString("description"));

					hsBean.setMrp(jobj2.getInt("price"));
					hsBean.setOklerPrice(jobj2.getInt("saleprice"));
					hsBean.setDiscount(jobj2.getInt("discount"));
					String jimg = jobj2.getString("images");
					String url2;
					if(jimg.equals(null)||jimg.equals("null")||jimg.equals(""))
					{
						url2 = "drawable://" + R.drawable.tempcuff;
					}
					else
					{
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
					hsBean.setImgUrl(url2);
					JSONObject uobj= new JSONObject();	
					uobj = jobj.getJSONObject("product_image_url");
					hsBean.setMediumUrl(uobj.optString("productimage_url_medium"));
					hsBean.setSmallUrl(uobj.optString("productimage_url_small"));
					hsBean.setThumbUrl(uobj.optString("productimage_url_thumbnail"));
					currResults.add(hsBean);
				//	finallist.add(hsBean);
					
					
			/*		HealthShopGridAdapter healthshop = new HealthShopGridAdapter(act, finallist);
					listview1.setAdapter(healthshop);*/

//	Toast.makeText(getApplicationContext(), "array len = "+len, Toast.LENGTH_LONG).show();
				}
			/*	if(isOngoingSearch)
				{
					searchList.addAll(currResults);// = finallist;
				//	isOngoingSearch = false;
					searchAdapter = new HealthShopListAdapter(act, searchList,toolBarCount);
					listview1.setAdapter(searchAdapter);
					Okler.getInstance().setProdList(searchList);
		
				}
				else
				{*/
					finallist.addAll(currResults);
					adapter = new HealthShopListAdapter(act, finallist,toolBarCount);
					listview1.setAdapter(adapter);	
					Okler.getInstance().setProdList(finallist);
			//	}
		
	}
	@Override
	public boolean onQueryTextChange(String inputText) {
		// TODO Auto-generated method stub
	//	searchPageCount = 0;
	//	searchList.clear();
		finallist.clear();
		pageCount=0;
		searchText=inputText;
		try {
			inputText = URLEncoder.encode(searchText, "utf-8");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	String url = setSearchURL(searchText);	
	 //	isOngoingSearch = true;
	 	if(TextUtils.isEmpty(searchText)){
			System.out.println("text is empty");
	//		isOngoingSearch=false;	
			finallist.clear();
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
		}
	 	getAllMedsUrl = setUrl(pageCount+"", inputText, "", ""+prodtype, priceRangeVal, brandIdVal, "", catIdVal, priceSortVal, "", "");
		WebJsonObjectRequest getMedsJson = new WebJsonObjectRequest(Method.GET, getAllMedsUrl, new JSONObject(), new Listener<JSONObject>() {			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				try {
					showProgress(false);
					JSONObject jobj = (JSONObject)response;
					JSONObject jobj1 = new JSONObject();	
					
					jobj1 = jobj.getJSONObject("result");
					totalResultsFromWebService = jobj1.getInt("total_count");
					if(totalResultsFromWebService == 0)
					{
						no_item.setVisibility(View.VISIBLE);
					}
					else
					{
						no_item.setVisibility(View.GONE);
						processResponse(jobj1,jobj);
					}
					
				//	processResponse(response);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//isOngoingSearch=false;
				
			}
		}, new ErrorListener() {
	
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				showProgress(false);
				Log.e("Error ", "Some error occured"+error);
				//isOngoingSearch=false;
			}
		});
		if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), getMedsJson))
			showProgress(true);
		else
			showProgress(false);
		return true;
	}
	private String setSearchURL(String searchText)
	{
		String url = getString(R.string.getHealthShopProds)+"&"+getString(R.string.getHealthShopProdsSearch)+searchText+"&"+
				getString(R.string.getHealthShopProdsProdId)+"&"+
				getString(R.string.getHealthShopProType)+"1"+"&"+getString(R.string.getHealthShopOrder)+"&"+
		getString(R.string.getHealthShoppriceRange)+"&"+getString(R.string.getHealthShoppriceBrandId)+"&"+
		getString(R.string.getHealthShoppriceSubcatId)+"&"+getString(R.string.getHealthShoppriceSortPrice)+"&"+
		getString(R.string.getHealthShoppincode)+"&"+getString(R.string.getHealthShopcityid);
		return url;		
		
	}
	
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int lastItemInScreen=firstVisibleItem+visibleItemCount;
		/*if(isOngoingSearch)
		{			
			/*if(searchPageCount == 0 && is1Called)				
				return;
			else
				if(searchPageCount == 0 && !is1Called)
					is1Called = true;*/
		/*	if(searchList.size() > totalResultsFromWebService )
				return;
			if((lastItemInScreen==totalItemCount)&&(totalResultsFromWebService!=searchList.size())){
				//String str = searchURL+getString(R.string.getdiseasePagePart)+(searchPageCount);
				String str = setUrl((++searchPageCount)+"", searchText, "", ""+prodtype, priceRangeVal, brandIdVal, "", catIdVal, priceSortVal, "", ""); 
				WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, str, new JSONObject(), this, this);
				if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), webjson))
				{
					//searchPageCount++;
					showProgress(true);					
				}
				else
				{
					showProgress(false);
				}
				
			}
		}
		else
		{*/
	/*		if(pageCount == 0 && is1Called)				
				return;
			else
				if(pageCount == 0 && !is1Called)
					is1Called = true;*/
			if(finallist.size() > totalResultsFromWebService )
				return;
			if((lastItemInScreen==totalItemCount)&&(totalResultsFromWebService!=finallist.size())){	
				String str = setUrl((++pageCount)+"", searchText, "", ""+prodtype, priceRangeVal, brandIdVal, "", catIdVal, priceSortVal, "", ""); //normalURL+getString(R.string.getdiseasePagePart)+(pageCount);
				
				WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, str, new JSONObject(), this, this);
				if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), webjson))
				{
					//pageCount++;
					showProgress(true);
				}
				else
				{
					showProgress(false);
				}			
			}	
		//}
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
	 private String setUrl(String pageNum,String search,String prodId,String proType,String priceRange, String brandId,String orderBy,String subCatId,String sortBy,String pinCode,String cityId)
	 {
		 String URL="";
		 medUrl1 = getString(R.string.getMedsUrlPageNo1)+pageNum;
			medUrl2 = getString(R.string.getMedsUrlStartWith2)+search;
			medUrl3 = getString(R.string.getMedsUrlProdId3)+prodId;
			medUrl4 = getString(R.string.getMedsUrlProdType4)+proType;
			String medUrl17 = getString(R.string.getMedsUrlOrderBy5)+orderBy;
			medUrl5 = getString(R.string.getMedsUrlPriceRange6)+priceRange;
			medUrl6 = getString(R.string.getMedsUrlBrandId6)+brandId;
			String medUrl18 = getString(R.string.getMedsUrlSubCatId7)+subCatId;
			String medUrl19 = getString(R.string.getMedsUrlsortprice8)+sortBy;
			String medUrl20 = getString(R.string.getMedsUrlPinCode)+pinCode;
			String medUrl21 = getString(R.string.getMedsUrlCityId)+cityId;
			URL = medUrl1+medUrl2+medUrl3+medUrl4+medUrl17+medUrl5+medUrl6+medUrl18+medUrl19+medUrl20+medUrl21;
			return URL;
	 }
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
  
}
