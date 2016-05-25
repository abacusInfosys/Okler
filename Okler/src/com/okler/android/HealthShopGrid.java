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
import com.okler.adapters.HealthShopSingleItemAdapter;
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
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class HealthShopGrid extends BaseActivity  implements Response.Listener,Response.ErrorListener,SearchView.OnQueryTextListener,OnScrollListener {
	private View bottomBarLayout;
	private GridView gridView;
    private Context context;    
    private ArrayList prgmName;
    private Toolbar toolBar;
    private Button notifCount;
    BrandsDataBean brndHS = null;
    SearchView searchView;
    private NetworkImageView iv_image;
    ImageLoader mImageLoader;
    LinearLayout progressLinLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    LinearLayout linParent;
    ArrayList<CategoriesDataBean> cats;
    ArrayList<SubCategoriesDataBean> subCats,selectedCatSubCats;
    ArrayList<String> priceRanges;
    Button toolBarCount;
    boolean response2 ;
    int lastBrandsPage = 1;
    int cnt=0;
    HealthShopGridAdapter adapter;
    int prodtype = 1,totalResultsFromWebService=0;
    int pageCount=0;
    String cate_id, brand_id;
    ScrollView scrT;
    ArrayList<ProductDataBean> finallist;
    private DrawerLayout mDrawerLayout;
    ArrayList<BrandsDataBean> brandsData;
    HealthShopListAdapter listAdapter;
    HealthShopSingleItemAdapter singleAdapter;
    boolean isGrid=true,isList=false,isSingleList=false,ifNoProductsReturned=false;
    ImageView filter;
   public static String [] prgmNameList={"1 kg weight cuff","1 kg weight cuff","1 kg weight cuff","1 kg weight cuff","1 kg weight cuff","1 kg weight cuff"};
   public static int [] prgmImages={R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera};
   ArrayList<BrandsDataBean>   bbean = new ArrayList<BrandsDataBean>();
   Activity act;
   TextView no_item,nodata;
   ImageView imgBack,imageBrandleft;
   RelativeLayout ListRL;
   ImageView list,singleList,grid;
   ListView listView;
   String searchText="",priceRangeVal="",priceSortVal="",brandIdVal="",catIdVal="";
   String FromWhere,getAllMedsUrl,pageNo="",medUrl1,medUrl2,medUrl3,medUrl4,medUrl5,medUrl6,priceRange,alphaMedUrl1,alphaMedUrl2;
   String categoriesUrl;
   Button viewMore;
   int totalBrands;
   boolean isCleared=false,isBrandsCalled=false;  
   
   @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_shop_grid);
		   toolBar=(Toolbar) findViewById(R.id.toolbar);
	       context=getApplicationContext();
	       mImageLoader = VolleyRequest.getInstance(context).getImageLoader();
	       final ActionBar ab=getSupportActionBar();
	       setSupportActionBar(toolBar);
	       act = this;
	     //  ab.setDisplayHomeAsUpEnabled(true);
	       toolBar.setBackgroundResource(R.drawable.custom_view_grad_healthshop);
	       Utilities.setTitleText(toolBar, "Health Shop");
	       listView= (ListView)findViewById(R.id.listView);
	       nodata=(TextView) findViewById(R.id.noresult);
	       imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       cats = new ArrayList<CategoriesDataBean>();
	       subCats = new ArrayList<SubCategoriesDataBean>();
	       selectedCatSubCats = new ArrayList<SubCategoriesDataBean>();
	       cats = Okler.getInstance().getCategoriesBean();
	        subCats = Okler.getInstance().getSubCategories();
	        
	        categoriesUrl = getString(R.string.get_categories);
	        
	        if(cats.size()<=0){
	        	UIUtils.setCategories(this,categoriesUrl);
	        }
	        if(cats.size()>0 && subCats.size()<=0){
	        	UIUtils.setSubcategories(act);
	        }
	        /*if(brandsData.size()<=0){
	        	setHomeoBrands();
	        }*/
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	       categoriesUrl = getString(R.string.get_categories);
	       notifCount = (Button)toolBar.findViewById(R.id.toolBarCount);
	       progressLinLayout = (LinearLayout)findViewById(R.id.progressLinLayout);
	      bottomBarLayout = findViewById(R.id.bottombar);
	       handleMapping(bottomBarLayout);
	       brandsData = new ArrayList<BrandsDataBean>();
	       brandsData = Okler.getInstance().getHsBrands();
	       no_item = (TextView) findViewById(R.id.no_item);
	       
	       toolBarCount = (Button)toolBar.findViewById(R.id.toolBarCount);
	       getAllMedsUrl = setUrl("0", "", "", "1", "", "", "", "", "", "", ""); //medUrl1+medUrl2+medUrl3+medUrl4+prodtype+medUrl5+medUrl6;
	       
		   finallist = new ArrayList<ProductDataBean>();		
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
	       gridView=(GridView)findViewById(R.id.gridView1);    
	       gridView.setOnItemClickListener(new OnItemClickListener() {

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
	       
	   listView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ArrayList<ProductDataBean> databean=Okler.getInstance().getProdList();
			ProductDataBean	databean1=databean.get(position);
				Gson gson=new Gson();
				String hsbean=gson.toJson(databean1);
				Intent intent=new Intent(context,ProductDetailsActivity.class);
				intent.putExtra("MedId",hsbean);
				startActivity(intent);
			}
	});    
	       gridView.setOnScrollListener(this);
	       listView.setOnScrollListener(this);
	       RelativeLayout parentSearchLayout=(RelativeLayout) toolBar.findViewById(R.id.parentSearchLayout);
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
	       
	       ListRL = (RelativeLayout)parentSearchLayout.findViewById(R.id.ListRL);
	       list =(ImageView) ListRL.findViewById(R.id.listingIcon);
	       singleList = (ImageView) ListRL.findViewById(R.id.singleIcon);
	       grid = (ImageView)ListRL.findViewById(R.id.gridIcon);
	       linParent = (LinearLayout)findViewById(R.id.sliderManuParentLinear);
	       
	       list.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				list.setVisibility(View.GONE);
				singleList.setVisibility(View.VISIBLE);
				grid.setVisibility(View.GONE);
				gridView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				listView.setAdapter(listAdapter);
				
			}
		});
	       
	    singleList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				list.setVisibility(View.GONE);
				singleList.setVisibility(View.GONE);
				grid.setVisibility(View.VISIBLE);
				gridView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				listView.setAdapter(singleAdapter);
				
			}
		});
	    
	    grid.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				list.setVisibility(View.VISIBLE);
				singleList.setVisibility(View.GONE);
				grid.setVisibility(View.GONE);
				gridView.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				
			}
		});
	       
	       
	       ImageView overflow =(ImageView) toolBar.findViewById(R.id.overflowIcon);
	       overflow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cats.size()<=0){
				UIUtils.setCategories(act, categoriesUrl);
				}
				else{
				   setCategoriesInRightMenu();
			       setSubCategories(cats.get(0).getCatId());
		//	Toast.makeText(getApplicationContext(), "Overflow", Toast.LENGTH_LONG).show();	
			
			       if(mDrawerLayout.isDrawerOpen(Gravity.END))
					{
						mDrawerLayout.closeDrawer(Gravity.END);
					}else{
						mDrawerLayout.openDrawer(Gravity.END);
					}
				}
			}
		});
	       
	       mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	       drawerSetUp();
	       
	       
	        selectedCatSubCats = new ArrayList<SubCategoriesDataBean>();
	        
	        priceRanges = new ArrayList<String>();
	        scrT = (ScrollView)findViewById(R.id.scrT);
	       //Set Categories in right menu
	       setCategoriesInRightMenu();
	       if(cats.size()!=0)
	    	   setSubCategories(cats.get(0).getCatId());
	       else
	       {
	    	   
	    	   UIUtils.setCategories(context, categoriesUrl);
	       }
	       mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	  
	      /* WebJsonObjectRequest getMedsJson = new WebJsonObjectRequest(Method.GET, getAllMedsUrl, new JSONObject(), this, this);
			if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), getMedsJson))
				showProgress(true);
			else
				showProgress(false);*/
			adapter = new HealthShopGridAdapter(act, finallist,toolBarCount);
			gridView.setAdapter(adapter);
			listAdapter = new HealthShopListAdapter(act, finallist, toolBarCount);
			
			singleAdapter = new HealthShopSingleItemAdapter(act, finallist, toolBarCount);
			
	}
@Override
	protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	 WebJsonObjectRequest getMedsJson = new WebJsonObjectRequest(Method.GET, getAllMedsUrl, new JSONObject(), this, this);
		if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), getMedsJson))
			showProgress(true);
		else
			showProgress(false);
	UIUtils.setCartCount(toolBarCount,act);
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
				return true;

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
			imgCategoryIcon.setImageDrawable(getResources().getDrawable(R.drawable.loud_speaker_icon));
			break;
			case 1 : str = "PRICE RANGE";
			imgCategoryIcon.setImageDrawable(getResources().getDrawable(R.drawable.by_price_range_icon));
			break;
			case 2: str = "PRICE";
			imgCategoryIcon.setImageDrawable(getResources().getDrawable(R.drawable.rupee_icon));
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
			Log.d("Exe", ex.getStackTrace().toString());
		}
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
					ImageView imageOnLeft=(ImageView) findViewById(R.id.rightItem);
					String txt_name = txtSubCategoryName2.getText().toString();
					
					if(txt_name == "Price - Low to High")
					{	
						//imageOnLeft.setImageDrawable(getResources().getDrawable(R.drawable.grey_up_arrow_icon));
						String asc = "asc";
						priceRangeVal="";
						priceSortVal=asc;
						brandIdVal="";
						searchText="";
						finallist.clear();
					pageCount=0;
					//	String price_url = "https://www.okler.com/api/products/products/productsbyprotype?page=&search=&prod_id=&pro_type=1&order_by=desc&price_range=&brand_id=&subcat_id="+cate_id+"&sort_price="+asc;
						getAllMedsUrl = setUrl("0", "", "", "1", "", "", "", cate_id, asc, "", "");
						WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, getAllMedsUrl, new JSONObject(), new Listener<JSONObject>() 
								{
									@Override
									public void onResponse(JSONObject response) 
									{
										isCleared=true;
										searchView.setQuery("", false);
									//	ProductDataBean hsBean;
										showProgress(false);
										String resp = String.valueOf(response);
										//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
										JSONObject jobj = (JSONObject)response;
										JSONObject jobj1 = new JSONObject();
										try {
												jobj1 = jobj.getJSONObject("result");
												totalResultsFromWebService = jobj1.optInt("total_count");
												totalResultsFromWebService = jobj1.optInt("total_count");
												if(totalResultsFromWebService == 0)
												{
													no_item.setVisibility(View.VISIBLE);														
												}
												else
												{
													processResponse(jobj1,jobj);
													setPriceRange(jobj1);
													no_item.setVisibility(View.GONE);
												}
												
											} 	
										catch (JSONException e1) 
											{
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}									
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
						//imageOnLeft.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow_grey_icon));
						String desc = "desc";
						searchText="";
						priceRangeVal="";
						priceSortVal=desc;
						brandIdVal="";
						finallist.clear();
					pageCount=0;
					//	String brand_url = "https://www.okler.com/api/products/products/productsbyprotype?page=&search=&prod_id=&pro_type=1&order_by=desc&price_range=&brand_id=&subcat_id="+cate_id+"&sort_price="+desc;
						getAllMedsUrl = setUrl("0", "", "", "1", "", "", "", cate_id, desc, "", "");
						WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, getAllMedsUrl, new JSONObject(), new Listener<JSONObject>() 
								{
									@Override
									public void onResponse(JSONObject response) 
									{	
										isCleared=true;
										searchView.setQuery("", false);
										showProgress(false);
										String resp = String.valueOf(response);
										//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
										JSONObject jobj = (JSONObject)response;
										JSONObject jobj1 = new JSONObject();
										try {
												jobj1 = jobj.getJSONObject("result");
												totalResultsFromWebService = jobj1.optInt("total_count");
												totalResultsFromWebService = jobj1.optInt("total_count");
												if(totalResultsFromWebService == 0)
												{
													no_item.setVisibility(View.VISIBLE);														
												}
												else
												{
													processResponse(jobj1,jobj);
													setPriceRange(jobj1);
													no_item.setVisibility(View.GONE);
												}
												
											} 	
										catch (JSONException e1) 
											{
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
										
								
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
					
					mDrawerLayout.closeDrawer(Gravity.END);		
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
			if(!ifNoProductsReturned){		
			rightMenuSubCats.addView(views[cnt]);
			rightMenuSubCats.addView(separatorView[cnt]);
			}
	}
		}catch(Exception ex)
		{
			Log.e("ex", ex.getStackTrace().toString());
		}
   }
	
	private void populateBrands()
	{
		try
		{
		LinearLayout rightMenuSubCats = (LinearLayout)findViewById(R.id.rightMenuSubcats);		
		rightMenuSubCats.removeAllViews();
		
		if(brandsData.size() == 0 && !ifNoProductsReturned)
		{
			lastBrandsPage=0;
			setNextBrands(0);
			//ifNoProductsReturned=false;
			return;
		}
		//setNextBrands(1);
		View[] views = new View[brandsData.size()];
		View[] separatorView = new View[brandsData.size()]; 
		
		for(int cnt = 0; cnt< brandsData.size();cnt++)
		{
			views[cnt] = getLayoutInflater().inflate(R.layout.include_category_2, null);			
			separatorView[cnt] = getLayoutInflater().inflate(R.layout.separator_1dp, null);
			views[cnt].setTag(brandsData.get(cnt).getBrandId());
			imageBrandleft=(ImageView)views[cnt].findViewById(R.id.rightItem);
			imageBrandleft.setImageDrawable(getResources().getDrawable(R.drawable.right_grey_tick));
			TextView txtSubCategoryName =(TextView) views[cnt].findViewById(R.id.subCategory);			
			txtSubCategoryName.setText(brandsData.get(cnt).getBrandName());	
			String brnId = brandsData.get(cnt).getBrandId();
			if(brnId.equals(brand_id))
				imageBrandleft.setImageDrawable(getResources().getDrawable(R.drawable.right_red_tick));
			
			views[cnt].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					imageBrandleft=(ImageView)v.findViewById(R.id.rightItem);
					TextView txtSubCategoryName2 =(TextView) v.findViewById(R.id.subCategory);
					imageBrandleft.setImageDrawable(getResources().getDrawable(R.drawable.right_red_tick));
					
					String txt_name = txtSubCategoryName2.getText().toString();
				//	Toast.makeText(getApplicationContext(), txt_name, Toast.LENGTH_LONG).show();
					
					String name = (String) v.getTag();
				//	Toast.makeText(getApplicationContext(), "tag:"+name, Toast.LENGTH_LONG).show();
					for (int i = 0; i < brandsData.size(); i++) 
					{
						if(txt_name == (brandsData.get(i).getBrandName()))
						{
							String id = String.valueOf(v.getTag());
							if(id.equals(brand_id)){
								imageBrandleft.setImageDrawable(getResources().getDrawable(R.drawable.right_grey_tick));
								brand_id = "";
							}else{		
							brand_id =  (brandsData.get(i).getBrandId());
							}
						//	Toast.makeText(getApplicationContext(), brand_id, Toast.LENGTH_LONG).show();
							brandIdVal=brand_id;
							priceRangeVal="";
							priceSortVal="";
							finallist.clear();
							searchText="";
							pageCount=0;
						//	String brand_url = "https://www.okler.com/api/products/products/productsbyprotype?page=&search=&prod_id=&pro_type=1&order_by=desc&price_range=&brand_id="+brand_id+"&subcat_id="+ cate_id+"&sort_price=asc";
						 	getAllMedsUrl = setUrl("0", "", "", "1", "", brand_id, "", cate_id, "", "", ""); 
							
							WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, getAllMedsUrl, new JSONObject(), new Listener<JSONObject>() 
									{
										@Override
										public void onResponse(JSONObject response) 
										{	
											isCleared = true;
											searchView.setQuery("", false);
											showProgress(false);					
											ProductDataBean hsBean;							
											String resp = String.valueOf(response);
											//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
											JSONObject jobj = (JSONObject)response;
											JSONObject jobj1 = new JSONObject();
											try {
													jobj1 = jobj.getJSONObject("result");
													totalResultsFromWebService = jobj1.optInt("total_count");
													if(totalResultsFromWebService == 0)
													{
														no_item.setVisibility(View.VISIBLE);
														Okler.getInstance().getPriceRanges().clear();
														Okler.getInstance().getHsBrands().clear();
														ifNoProductsReturned=true;
													}
													else
													{
														processResponse(jobj1,jobj);
														setPriceRange(jobj1);
														no_item.setVisibility(View.GONE);
														ifNoProductsReturned=false;
													}
													
												} 	
											catch (JSONException e1) 
												{
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}				
									
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
			
			rightMenuSubCats.addView(views[cnt]);
			rightMenuSubCats.addView(separatorView[cnt]);
		}
		viewMore = new Button(this);
		viewMore.setWidth(LayoutParams.FILL_PARENT);
		viewMore.setHeight(LayoutParams.WRAP_CONTENT);
		viewMore.setText("View More");
		viewMore.setBackgroundResource(R.color.LightGrey);
		int t = totalBrands;
		int t2 = brandsData.size();
		Log.e("Values", "total = "+t+"size = "+t2);
		if(brandsData.size()>0){
		if(!isBrandsCalled)
			rightMenuSubCats.addView(viewMore);	
		else{
		if(totalBrands>brandsData.size()){
			
			rightMenuSubCats.addView(viewMore);	
		}
		}
		}
		//isBrandsCalled=false;
		viewMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("Values", "lastBrandsPage "+lastBrandsPage);
				setNextBrands(lastBrandsPage);
			}
		});
		}catch(Exception ex)
		{
			Log.d("AA", "Exception occured"+ex.getStackTrace());
		}
	}
	
	private void populatePriceRanges()
	{
		try
		{
		LinearLayout rightMenuSubCats = (LinearLayout)findViewById(R.id.rightMenuSubcats);		
		rightMenuSubCats.removeAllViews();
		
		ArrayList<String> prices = new ArrayList<String>();
		prices = Okler.getInstance().getPriceRanges();
		
		View[] views = new View[prices.size()];
		View[] separatorView = new View[prices.size()]; 
		for(int cnt = 0; cnt< prices.size();cnt++)
		{
			views[cnt] = getLayoutInflater().inflate(R.layout.include_category_2, null);			
			separatorView[cnt] = getLayoutInflater().inflate(R.layout.separator_1dp, null);
			views[cnt].setTag(cnt);
			imageBrandleft=(ImageView)views[cnt].findViewById(R.id.rightItem);
			imageBrandleft.setImageDrawable(getResources().getDrawable(R.drawable.right_grey_tick));
			TextView txtSubCategoryName =(TextView) views[cnt].findViewById(R.id.subCategory);			
			txtSubCategoryName.setText(prices.get(cnt));
			String priceRg = prices.get(cnt);
			if(priceRg.equals(priceRangeVal))
				imageBrandleft.setImageDrawable(getResources().getDrawable(R.drawable.right_red_tick));

			views[cnt].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					TextView txtSubCategoryName =(TextView) v.findViewById(R.id.subCategory);
					imageBrandleft=(ImageView)v.findViewById(R.id.rightItem);

					imageBrandleft.setImageDrawable(getResources().getDrawable(R.drawable.right_red_tick));
					String range = txtSubCategoryName.getText().toString();
					
					if(range.equals(priceRangeVal)){
					imageBrandleft.setImageDrawable(getResources().getDrawable(R.drawable.right_grey_tick));
					priceRangeVal="";
					}else{
						priceRangeVal = range;
					}
					brandIdVal="";
					priceSortVal="";
					finallist.clear();
					searchText="";
					pageCount=0;
				//	String pRange = "https://www.okler.com/api/products/products/productsbyprotype?page=&search=&prod_id=&pro_type=1&order_by=desc&price_range="+range+"&brand_id=&subcat_id="+cate_id+"&sort_price=asc";
					getAllMedsUrl = setUrl("0", "", "", "1", priceRangeVal, "", "", cate_id, "", "", "");
					WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, getAllMedsUrl, new JSONObject(), new Listener<JSONObject>() 
							{
								@Override
								public void onResponse(JSONObject response) 
								{			
									isCleared=true;
									searchView.setQuery("", false);
									showProgress(false);
									String resp = String.valueOf(response);
									//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
									JSONObject jobj = (JSONObject)response;
									JSONObject jobj1 = new JSONObject();
									try {
											jobj1 = jobj.getJSONObject("result");
											totalResultsFromWebService = jobj1.optInt("total_count");
											if(totalResultsFromWebService == 0)
											{
												no_item.setVisibility(View.VISIBLE);														
											}
											else
											{
												processResponse(jobj1,jobj);
												setPriceRange(jobj1);
												no_item.setVisibility(View.GONE);
											}
											
										} 	
									catch (JSONException e1) 
										{
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}							
						
							
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
	
		//bhagyashri--1822016
		mDrawerLayout.closeDrawer(Gravity.END);
	}			
				
			});
			//mDrawerLayout.closeDrawer(Gravity.END);
			
					
			rightMenuSubCats.addView(views[cnt]);
			rightMenuSubCats.addView(separatorView[cnt]);
		}
		}catch(Exception ex)
		{
			Log.d("AA", "Exception occured"+ex.getStackTrace());	
		}
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
	
	private void processResponse(JSONObject jobj1,JSONObject jobj) throws JSONException
	{
		ProductDataBean hsBean;	
		
		JSONObject jobj2 = new JSONObject();
		JSONObject jobj3 = new JSONObject();
		JSONArray jArray = new JSONArray();
		hsBean = null;
		int lenobj = jobj1.length();
		ifNoProductsReturned = false;
		nodata.setVisibility(View.GONE);
	//	Toast.makeText(getApplicationContext(), "Obj len = "+lenobj, Toast.LENGTH_LONG).show();
	ArrayList<ProductDataBean> currResults = new ArrayList<ProductDataBean>();
	
			for (int i = 0; i < lenobj-3 ; i++) {
				//jArray = jobj.getJSONArray("");
				jobj2 = jobj1.getJSONObject(i+"");
				int len = jobj2.length();
				hsBean = new ProductDataBean();
				hsBean.setProdName(jobj2.optString("name"));
			if(jobj2.has("type_of_packing")){
			}
			hsBean.setDesc(jobj2.optString("description"));
				hsBean.setProdId(Integer.valueOf(jobj2.optString("id")));
				hsBean.setKey_feature(jobj2.optString("key_features"));
				hsBean.setBrandInfo(jobj2.optString("brandinfo"));
				hsBean.setWarranty(jobj2.optString("warranty"));
				hsBean.setSpecfic(jobj2.optString("specifications"));
			hsBean.setMrp(jobj2.optInt("price"));
			hsBean.setTax(Float.parseFloat(jobj2.optString("tax")));
			hsBean.setOklerPrice(jobj2.optInt("saleprice"));
			hsBean.setDiscount(jobj2.optInt("discount"));
			String jimg = jobj2.optString("images");
			String url2="";
			if(jimg.equals(null)||jimg.equals("null")||jimg.equals("")){
				url2 = "drawable://" + R.drawable.tempcuff;
			}else{
				String arr1[] = jimg.split(":");
		        String sec = arr1[2];
		        String arr2[] = sec.split("\"");
		        url2 = arr2[1];	
			}
			String image_name="";
			try {
				 image_name = URLEncoder.encode(url2, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			hsBean.setImgUrl(image_name);	
			JSONObject uobj= new JSONObject();	
			uobj = jobj.getJSONObject("product_image_url");
			hsBean.setMediumUrl(uobj.optString("productimage_url_medium"));
			hsBean.setSmallUrl(uobj.optString("productimage_url_small"));
			hsBean.setThumbUrl(uobj.optString("productimage_url_thumbnail"));
			//array1 = Okler.getInstance().getPriceRanges();					
		//	finallist.add(hsBean);			
			currResults.add(hsBean);
		//	Toast.makeText(getApplicationContext(), "array len = "+len, Toast.LENGTH_LONG).show();
			}
			finallist.addAll(currResults);
			adapter.notifyDataSetChanged();
			listAdapter.notifyDataSetChanged();
			singleAdapter.notifyDataSetChanged();
			Okler.getInstance().setProdList(finallist);
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
	
	private void setNextBrands(int pageNum)
	{
		
		 response2 = false;
			
				//final int tempJ = j;
				response2 = false;
				String brandsUrl = getString(R.string.get_healthshop_brand)+getString(R.string.getDiagnoDisease2)+pageNum+getString(R.string.getDiagnoDisease1);
				WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, brandsUrl, new JSONObject(),	new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) 
					{
						showProgress(false);
						isBrandsCalled=true;
						response2 = true;
						try {
							JSONArray resultsArr = response.getJSONArray("result");
							totalBrands = response.optInt("total_brand_count");
							 bbean.clear();
							for(int j1 = 0 ;j1< resultsArr.length();j1++)
							{
								brndHS = new BrandsDataBean();
								JSONObject subCates = resultsArr.getJSONObject(j1);
								brndHS.setBrandId(subCates.optString("id"));
								brndHS.setBrandName(subCates.optString("brand_name"));
								brandsData.add(brndHS);
							
							}
						//	brandsData.addAll(bbean);
							//Okler.getInstance().setHsBrands(hsBrands);
							Okler.getInstance().setHsBrands(brandsData);
							populateBrands();
							lastBrandsPage++;
							/*if (tempJ == pageLength)
							{
								
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
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		int lastItemInScreen=firstVisibleItem+visibleItemCount;		
		/*if(searchText.equals("")){
			searchText= getIntent().getStringExtra("searchText");	
		}*/
			if(finallist.size() > totalResultsFromWebService )
				return;
			if(pageCount > (totalResultsFromWebService/10) )
				return;
			if((lastItemInScreen==totalItemCount)&&(totalResultsFromWebService!=finallist.size())){	
				/*if(searchText.equals("")){
					searchText= getIntent().getStringExtra("searchText");	
				}*/
				if(searchText.length()>2||searchText.length()==0){
				String str = setUrl((++pageCount)+"", searchText, "", ""+prodtype, priceRangeVal, brandIdVal, "", catIdVal, priceSortVal, "", ""); //normalURL+getString(R.string.getdiseasePagePart)+(pageCount);
				Log.e("URL","Hsnew"+" "+ searchText);
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
			}
	}
	@Override
	public boolean onQueryTextSubmit(String query) {
		
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
	return false;
	}
	@Override
	public boolean onQueryTextChange(String newText) {
		if(isCleared){
			isCleared=false;
		}
		else{
		
		pageCount = 0;
		finallist.clear();
		//gridView.setAdapter(searchAdapter);
				try {
			searchText = URLEncoder.encode(newText, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 //	String url = setSearchURL(searchText);	
			if(searchText.length()>2){
				callSearchWebService();
			}
			else if(searchText.length()==0){
				searchText="";
				callSearchWebService();
			}else{
				finallist.clear();
				adapter.notifyDataSetChanged();
				listAdapter.notifyDataSetChanged();
				singleAdapter.notifyDataSetChanged();
				Okler.getInstance().setProdList(finallist);
				/*if(nodata.getVisibility()==0){*/
				nodata.setVisibility(View.INVISIBLE);
				//gridView.setVisibility(View.VISIBLE);
				//}
			}
	}
		return true;
	}
	
	public void callSearchWebService(){
		getAllMedsUrl = setUrl(pageCount+"", searchText, "", ""+prodtype, priceRangeVal, brandIdVal, "", catIdVal, priceSortVal, "", "");
		WebJsonObjectRequest getMedsJson = new WebJsonObjectRequest(Method.GET, getAllMedsUrl, new JSONObject(), new Listener<JSONObject>() {			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				try {
					showProgress(false);
					JSONObject jobj = (JSONObject)response;
					JSONObject jobj1 = new JSONObject();		
					
					jobj1 = jobj.getJSONObject("result");
					totalResultsFromWebService = jobj1.optInt("total_count");
					
					if(totalResultsFromWebService==0){
						
						nodata.setVisibility(View.VISIBLE);
						//gridView.setVisibility(View.GONE);
						
					}else{
						nodata.setVisibility(View.GONE);
						//gridView.setVisibility(View.VISIBLE);
					processResponse(jobj1,jobj);
					setPriceRange(jobj1);
					}
				//	processResponse(response);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}, new ErrorListener() {
	
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				showProgress(false);
				Log.e("Error ", "Some error occured"+error);
				
			}
		});
		if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), getMedsJson))
			showProgress(true);
		else
			showProgress(false);
	}
	
	@Override
	public void onErrorResponse(VolleyError error) {
		String verr=String.valueOf(error);
		System.out.println("VolleyError error");
		showProgress(false);
	}
	@Override
	public void onResponse(Object response) {
		showProgress(false);
		ProductDataBean hsBean;
			String resp = String.valueOf(response);
			//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
			JSONObject jobj = (JSONObject)response;
			JSONObject jobj1 = new JSONObject();
			
			try {
				jobj1 = jobj.getJSONObject("result");
				totalResultsFromWebService = jobj1.optInt("total_count");
				processResponse(jobj1,jobj);
				setPriceRange(jobj1);
			} catch (JSONException e1) {
			// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
		
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
		NetworkImageView imgCategoryIcon =(NetworkImageView) views[cnt].findViewById(R.id.rightItem);
		imgLoader = VolleyRequest.getInstance(context).getImageLoader();
		String url=cats.get(cnt).getImagePath();
		txtCategoryName.setText(cats.get(cnt).getCategoryName());
		//String tempUrl="http://us.cdn1.123rf.com/168nwm/hasloo/hasloo1403/hasloo140300120/27003223-macro-shoot-of-medical-person-for-health-insurance-or-hospital.jpg";
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
			Log.d("Exception", ex.getStackTrace().toString());
		}
	}
	
	public void setPriceRange(JSONObject jobj1){
		JSONArray array = new JSONArray();
		ArrayList<String> array1 = new ArrayList<String>();
		try {
			array = jobj1.getJSONArray("range");
			int length = array.length();				
							
			for(int j = 0; j<length-3; j ++)
			{
				array1.add((String) array.get(j));
			}					
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		Okler.getInstance().setPriceRanges(array1);	
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
				//	Toast.makeText(getApplicationContext(), txt_name, Toast.LENGTH_LONG).show();
					
					int name = (int) v.getTag();
				//	Toast.makeText(getApplicationContext(), "tag:"+name, Toast.LENGTH_LONG).show();
					for (int i = 0; i < selectedCatSubCats.size(); i++) 
					{
						if(txt_name == (selectedCatSubCats.get(i).getSubCateName()))
						{
							cate_id =  (selectedCatSubCats.get(i).getSubCateId());
							catIdVal = cate_id;
					//		Toast.makeText(getApplicationContext(), cate_id, Toast.LENGTH_LONG).show();
							searchText="";
							pageCount=0;
							//String cate_url = "https://www.okler.com/api/products/products/productsbyprotype?page=&search=&prod_id=&pro_type=1&order_by=desc&price_range=&brand_id=&subcat_id="+cate_id+"&sort_price=asc";
							String cate_url = setUrl("0", "", "", ""+prodtype, "", "", "", cate_id, "", "", "");
							finallist.clear();
						
							WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, cate_url, new JSONObject(), new Listener<JSONObject>() 
							{
								@Override
								public void onResponse(JSONObject response) 
								{				
									isCleared=true;
									searchView.setQuery("", false);
									showProgress(false);
									String resp = String.valueOf(response);
									//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
									JSONObject jobj = (JSONObject)response;
									JSONObject jobj1 = new JSONObject();
									try {
											jobj1 = jobj.getJSONObject("result");
											totalResultsFromWebService = jobj1.optInt("total_count");
											if(totalResultsFromWebService == 0)
											{
												no_item.setVisibility(View.VISIBLE);														
											}
											else
											{
												processResponse(jobj1,jobj);
												setPriceRange(jobj1);
												no_item.setVisibility(View.GONE);
											}
											
										} 	
									catch (JSONException e1) 
										{
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
							
											JSONArray array;
											try {
												array = jobj1.getJSONArray("range");
												int length = array.length();				
												ArrayList<String> array1 = new ArrayList<String>();				
												for(int j = 0; j<length-3; j ++)
												{
													array1.add((String) array.get(j));
												}
												
												Okler.getInstance().setPriceRanges(array1);	
	
									//		Okler.getInstance().setProdList(finallist);
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}										
							
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
				
				
				
				}
				
			});
			TextView txtSubCategoryName =(TextView) views[cnt].findViewById(R.id.subCategory);			
			txtSubCategoryName.setText(selectedCatSubCats.get(cnt).getSubCateName());		
			rightMenuSubCats.addView(views[cnt]);
			rightMenuSubCats.addView(separatorView[cnt]);
		 }
		
		}catch(Exception ex)
		{
			Log.e("erer", ex.getStackTrace().toString());
		}
		
	
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
	                	//	Toast.makeText(getApplicationContext(), "Drawer closed", Toast.LENGTH_LONG).show();	
	                		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	            }
	 
	            public void onDrawerOpened(View drawerView) {
	            	//Toast.makeText(getApplicationContext(), "Drawer open", Toast.LENGTH_LONG).show();	
	            	mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
	            }
	        };
	        mDrawerLayout.setDrawerListener(mDrawerToggle);
	        mDrawerToggle.setDrawerIndicatorEnabled(false); //disable "hamburger to arrow" drawable
	        //mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
	    //    mDrawerToggle.setDrawerIndicatorEnabled(true);
	}
}
