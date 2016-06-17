package com.okler.android;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.google.gson.Gson;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.okler.adapters.GroupExpandableListAdapter;
import com.okler.databeans.BrandsDataBean;
import com.okler.databeans.GroupDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class GroupByMedsActivity extends BaseActivity implements
		OnScrollListener, SearchView.OnQueryTextListener {

	String Url = "", serverUrl;
	ArrayList<ProductDataBean> ProdList = new ArrayList<ProductDataBean>();
	HashMap<GroupDataBean, List<ProductDataBean>> Glist;
	ArrayList<GroupDataBean> gdList = new ArrayList<GroupDataBean>();
	GroupDataBean gbean = new GroupDataBean();
	ExpandableListView medListExp;
	GroupExpandableListAdapter gAdap;
	ActionBarDrawerToggle mDrawerToggle;
	Activity ack;
	ProductDataBean hsBean;
	int pageNo, cur_pageNo, totalrecordsfromwebservice, page_count, prodtype,
			mappingId;
	boolean isONScrollProg = false, isBrandsCalled = false,
			isSearching = false;
	String[] alpha = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
			"X", "Y", "Z" };
	String abc = "", FromWhere = "", brand_id = "",lastClickText="";
	ListView lv1;
	ArrayAdapter<String> adapter;
	TextView no_items,currentPosition,lastClick;
	LinearLayout progressLinLayout,relParentSearchMed,rightMenuSubcatsMeds;
	Toolbar toolBar;
	Button btnCart, viewMore;
	SearchView searchView;
	View bottomBarLayout;
	ImageView overflow, imgBack;
	ArrayList<BrandsDataBean> brandsData;
	DrawerLayout mDrawerLayout;
	int diseaseId, lastBrandsPage, totalBrands;
	BrandsDataBean brndHS;
	RelativeLayout back_layout;
	
	WebJsonObjectRequest wjson;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_by_meds);
		FromWhere = getIntent().getStringExtra("FromWhere");
		diseaseId = getIntent().getIntExtra("DiseaseId", 0);
		serverUrl = getString(R.string.serverUrl);
		ack = this;
		Glist = new HashMap<GroupDataBean, List<ProductDataBean>>();
		gdList = new ArrayList<GroupDataBean>();
		medListExp = (ExpandableListView) findViewById(R.id.medListExp);
		gAdap = new GroupExpandableListAdapter(ack, Glist, gdList);
		medListExp.setAdapter(gAdap);
		no_items = (TextView) findViewById(R.id.no_items);
		progressLinLayout = (LinearLayout) findViewById(R.id.progressLinLayout);
		currentPosition = (TextView) findViewById(R.id.currentPosition);
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		btnCart = (Button) toolBar.findViewById(R.id.toolBarCount);
		searchView = (SearchView) toolBar.findViewById(R.id.menu_search);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		relParentSearchMed = (LinearLayout) findViewById(R.id.sliderManuParentLinear);
		rightMenuSubcatsMeds = (LinearLayout) findViewById(R.id.rightMenuSubcatsMeds);
		overflow = (ImageView) toolBar.findViewById(R.id.overflowIcon);
		setSupportActionBar(toolBar);
		ActionBar ab = getSupportActionBar();
		toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler
				.getInstance().getBookingType()));
		brandsData = new ArrayList<BrandsDataBean>();
		setTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerSetUp();
		setToolBarListeners();
		setUpSearchView();
				 
		
		
		

		medListExp.setOnScrollListener(this);

		medListExp.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				// Toast.makeText(ack,
				// parent+" "+v+" "+groupPosition+" "+childPosition+" "+id,
				// 5000).show();
				Log.e("TP",
						"parent" + parent.toString() + " " + "View "
								+ v.toString() + " " + "grppsn "
								+ groupPosition + " chpsn" + childPosition
								+ " id " + id);
				ProductDataBean pbeab = Glist.get(gdList.get(groupPosition))
						.get(childPosition);
				// Toast.makeText(ack,
				// "name = "+pbeab.getProdName()+" \n "+"id = "+pbeab.getProdId(),
				// 4000).show();
				getProductInfoById("" + pbeab.getProdId());
				return true;
			}
		});
		
		medListExp.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// TODO Auto-generated method stub
				return true;
			}
		});

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, alpha) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text = (TextView) view
						.findViewById(android.R.id.text1);

				text.setTextColor(Color.BLACK);
				return view;
			}
		};

		lv1 = (ListView) findViewById(R.id.alphabetsScrView);
		lv1.setAdapter(adapter);

		 lv1.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				
				String current = currentPosition.getText().toString();
				for (int i = 0; i < lv1.getChildCount(); i++) {
					TextView tv = (TextView)lv1.getChildAt(i);
					String text = tv.getText().toString();
					if(current.equals(text)){
						tv.setTextColor(Color.RED);
					}else{
						tv.setTextColor(Color.BLACK);
					}
							
				}
				
			}
		});
		setAlphaClick();
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.group_by_test, menu);
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		String bookingType = getIntent().getStringExtra("bookingType");
		
		Okler.getInstance().setBookingType(UIUtils.getBookingType(bookingType));
		
		//int type = Okler.getInstance().getBookingType();
		
		if (Okler.getInstance().getBookingType() == 0) {
			brandsData = Okler.getInstance().getAlloBrands();
			populateBrands();
		} else if (Okler.getInstance().getBookingType() == 4) {
			brandsData = Okler.getInstance().getHomeoBrands();
			populateBrands();
		}
		if (Okler.getInstance().getBookingType() == 3)
			overflow.setVisibility(View.INVISIBLE);
		
		pageNo= 0;
		cur_pageNo=0;
		totalrecordsfromwebservice=0;
		page_count=0;
		Glist.clear();
		gdList.clear();
		ProdList.clear();
		gAdap.notifyDataSetChanged();
		setUrl();
		callWebService(Url,false);
		currentPosition.setText("#");
		UIUtils.setCartCount(btnCart, ack);

	}

	public void processResponse(JSONObject response) {

		try {

			if (isONScrollProg)
				isONScrollProg = false;
			if (isSearching) {
				Glist.clear();
				gdList.clear();
				ProdList.clear();
				gAdap.notifyDataSetChanged();
			}
			JSONArray jarr = response.optJSONArray("result");
			JSONObject jobj, jobj2;
			JSONArray jarr2;

			cur_pageNo = response.optInt("cur_page");
			totalrecordsfromwebservice = response.optInt("records_count");
			page_count = response.optInt("page_count");
			int length = jarr.length();
			if (length <= 0) {
				no_items.setVisibility(View.VISIBLE);
				medListExp.setVisibility(View.INVISIBLE);
			} else {
				no_items.setVisibility(View.GONE);
				medListExp.setVisibility(View.VISIBLE);
			}
			for (int i = 0; i < length; i++) {
				gbean = new GroupDataBean();
				jobj = new JSONObject();
				jobj = jarr.getJSONObject(i);
				gbean.setGroup_id(jobj.optString("group_id"));
				gbean.setGroup_code(jobj.optString("group_code"));
				gbean.setGroup_name(jobj.optString("group_name"));
				jarr2 = jobj.optJSONArray("product_details");
				if (jarr2 == null) {
					jarr2 = new JSONArray();

				}
				ProdList = new ArrayList<ProductDataBean>();
				for (int j = 0; j < jarr2.length(); j++) {
					ProductDataBean pbean = new ProductDataBean();
					jobj2 = new JSONObject();
					jobj2 = jarr2.optJSONObject(j);
					String id = jobj2.optString("id");
					if (!(id.equals("200mg 5 Tablets)")))
						pbean.setProdId(Integer.parseInt(jobj2.optString("id")));
					pbean.setProdName(jobj2.optString("product_name"));
					ProdList.add(pbean);
				}
				Okler.getInstance().setProdList(ProdList);
				gdList.add(gbean);
				Glist.put(gbean, ProdList);

				// gbean.setProdList(ProdList);
			}

			gAdap.notifyDataSetChanged();
	setAlphaClick();
			showProgress(false);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	setAlphaClick();
			showProgress(false);
		}

	}

	public void setUrl() {
		if (FromWhere.equals("searchByDiseases")) {
			Url = disUrl(serverUrl, mappingId, diseaseId, "asc", abc, pageNo,
					brand_id);
		} else if (FromWhere.equals("searchByMedi")) {
			Url = MedUrl(serverUrl, prodtype, "asc", pageNo, abc, brand_id);
		}
	}

	public void setTitle() {
		if (Okler.getInstance().getBookingType() == 0) {
			prodtype = 0;
			mappingId = 4;
			Utilities.setTitleText(toolBar, getString(R.string.allopathy));
		} else if (Okler.getInstance().getBookingType() == 3) {
			prodtype = 3;
			mappingId = 3;
			Utilities.setTitleText(toolBar, getString(R.string.ayurvedic));
		} else {
			prodtype = 4;
			mappingId = 5;
			Utilities.setTitleText(toolBar, getString(R.string.homeopathy));
		}

	}

	private void drawerSetUp() {
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolBar, // nav
																				// menu
																				// toggle
																				// icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				// getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
				// Toast.makeText(getApplicationContext(), "Drawer closed",
				// Toast.LENGTH_LONG).show();
				mDrawerLayout
						.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			}

			public void onDrawerOpened(View drawerView) {
				// Toast.makeText(getApplicationContext(), "Drawer open",
				// Toast.LENGTH_LONG).show();
				mDrawerLayout
						.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.setDrawerIndicatorEnabled(false); // disable
														// "hamburger to arrow"
														// drawable
		// mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
		// mDrawerToggle.setDrawerIndicatorEnabled(true);
	}

	private void setUpSearchView() {
		// TODO Auto-generated method stub
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint("Search");
	}

	private void setToolBarListeners() {
		
		/*back_layout = (RelativeLayout)toolBar.findViewById(R.id.back_layout);
		back_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
		
		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});*/
		UIUtils.setBackClick(toolBar, ack);
		
		overflow = (ImageView) toolBar.findViewById(R.id.overflowIcon);
		overflow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
					mDrawerLayout.closeDrawer(Gravity.END);

				} else {
					mDrawerLayout.openDrawer(Gravity.END);
				}
			}
		});
	}

	public String MedUrl(String serverUrl, int pro_type, String order_by,
			int pageNo, String search, String brand_id) {

		String url = serverUrl
				+ "products/products/getproductsbygroup?pro_type=" + pro_type
				+ "&order_by=" + order_by + "&page=" + pageNo + "&search="
				+ search + "&brand_id=" + brand_id;

		return url;
	}

	public String disUrl(String serverUrl, int map_id, int disId,
			String order_by, String search, int pageNo, String brand_id) {
		String url = serverUrl
				+ "diseasemapping/getgrouprecordsbydiseases?mapping_typeid="
				+ map_id + "&disease_id=" + disId + "&order_by=" + order_by
				+ "&search=" + search + "&page=" + pageNo + "&brand_id="
				+ brand_id;

		return url;
	}

	private void populateBrands() {
		// if(lastBrandsPage != 1)

		if (brandsData.size() <= 0) {
			lastBrandsPage = 0;
			setNextBrands(0);
			return;
		}
		while (rightMenuSubcatsMeds.getChildCount() > 1) {
			rightMenuSubcatsMeds.removeViewAt(1);
		}

		View[] views = new View[brandsData.size()];
		View[] separatorView = new View[brandsData.size()];
		for (int cnt = 0; cnt < brandsData.size(); cnt++) {
			views[cnt] = getLayoutInflater().inflate(
					R.layout.include_category_2, null);
			views[cnt].setBackgroundColor(Color.WHITE);
			separatorView[cnt] = getLayoutInflater().inflate(
					R.layout.separator_1dp, null);
			views[cnt].setTag(brandsData.get(cnt).getBrandId());
			views[cnt].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TextView txtSubCategoryName = (TextView) v
							.findViewById(R.id.subCategory);
					int length = brandsData.size();
					pageNo = 0;
					cur_pageNo = 0;
					totalrecordsfromwebservice = 0;
					page_count = 0;
					Glist.clear();
					gdList.clear();
					ProdList.clear();
					for (int i = 0; i < length; i++) {

						if (brandsData.get(i).getBrandId()
								.equals(v.getTag().toString())) {
							Glist.clear();
							gdList.clear();
							ProdList.clear();

							abc = "";
							brand_id = brandsData.get(i).getBrandId();
							setUrl();
							callWebService(Url,false);

						}
					}

					pageNo = 1;
					mDrawerLayout.closeDrawer(Gravity.END);
				}
			});
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			params.setMargins(0, 1, 0, 1);
			views[cnt].setLayoutParams(params);
			TextView txtSubCategoryName = (TextView) views[cnt]
					.findViewById(R.id.subCategory);
			ImageView imgView = (ImageView) views[cnt]
					.findViewById(R.id.rightItem);
			imgView.setImageResource(R.drawable.right_arrow_brand_icon);
			txtSubCategoryName.setText(brandsData.get(cnt).getBrandName());

			txtSubCategoryName.setTypeface(Typeface.DEFAULT);
			rightMenuSubcatsMeds.addView(views[cnt]);
			rightMenuSubcatsMeds.addView(separatorView[cnt]);
		}
		if (Okler.getInstance().getBookingType() == 0) {
			viewMore = new Button(this);
			viewMore.setWidth(LayoutParams.FILL_PARENT);
			viewMore.setHeight(LayoutParams.WRAP_CONTENT);
			viewMore.setText("View More");
			viewMore.setBackgroundResource(R.color.LightGrey);
			if (!isBrandsCalled)
				rightMenuSubcatsMeds.addView(viewMore);
			else {
				if (totalBrands > brandsData.size())
					rightMenuSubcatsMeds.addView(viewMore);
			}
			viewMore.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setNextBrands(lastBrandsPage);
				}
			});
		}
	}

	public void setAlphaClick(){
		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				removeAlphaClick();
				abc = ((TextView) view).getText().toString();
				currentPosition.setText(abc);	
				if (abc.equals("#"))
					abc = "";
				TextView text = (TextView)view.findViewById(android.R.id.text1);
				text.setTextColor(getResources().getColor(R.color.Red));
				if (lastClick != null){
				lastClickText = lastClick.getText().toString();
				Log.e("TP", "last = "+ lastClickText+" abc = "+abc);
				if((lastClickText.equals(abc)))
					lastClick.setTextColor(getResources().getColor(
							R.color.Red));
				else
					lastClick.setTextColor(getResources().getColor(
							R.color.Black));
				
				}
				lastClick = text;
				
				pageNo = 0;
				cur_pageNo = 0;
				totalrecordsfromwebservice = 0;
				page_count = 0;
				Glist.clear();
				gdList.clear();
				ProdList.clear();
				brand_id = "";
				gAdap.notifyDataSetChanged();
				setUrl();
				callWebService(Url,false);

			}
		});
	}
	
	public void removeAlphaClick(){
		lv1.setOnItemClickListener(null);
	}
	
	private void setNextBrands(int lastBrandPage) {
		String brandsUrl;
		if (Okler.getInstance().getBookingType() == 0)
			brandsUrl = getString(R.string.get_brands_allo1)
					+ getString(R.string.get_brands_allo2) + lastBrandPage
					+ getString(R.string.get_brands_allo3);
		else
			brandsUrl = getString(R.string.get_brands_homeo1)
					+ getString(R.string.get_brands_allo2) + lastBrandPage
					+ getString(R.string.get_brands_allo3);

		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET,
				brandsUrl, new JSONObject(),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							isBrandsCalled = true;
							JSONArray resultsArr = response
									.getJSONArray("result");
							totalBrands = response.optInt("total_brand_count");
							for (int j = 0; j < resultsArr.length(); j++) {
								brndHS = new BrandsDataBean();
								JSONObject subCates = resultsArr
										.getJSONObject(j);
								brndHS.setBrandId(subCates.optString("id"));
								brndHS.setBrandName(subCates
										.optString("brand_name"));
								brandsData.add(brndHS);

							}
							populateBrands();
							lastBrandsPage++;
							if (Okler.getInstance().getBookingType() == 0)
								Okler.getInstance().setAlloBrands(brandsData);
							else
								Okler.getInstance().setHomeoBrands(brandsData);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						// Toast.makeText(getApplicationContext(),
						// "In on error repsonse", Toast.LENGTH_LONG).show();
					}

				});
		VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson);

	}

	public void getProductInfoById(String pid) {
		String part1, part2, part3, getProdUrl;
		showProgress(true);
		part1 = getString(R.string.getProdInfoByIdUrlPart1);
		part2 = getString(R.string.getProdInfoByIdUrlPart2);
		part3 = getString(R.string.getProdInfoByIdUrlPart3);
		getProdUrl = part1 + pid + part2 + prodtype + part3;
		// getProdUrl = part1+pid+part2+"0"+part3;
		WebJsonObjectRequest prodjson = new WebJsonObjectRequest(Method.GET,
				getProdUrl, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						JSONObject jobj = new JSONObject();
						JSONObject jobj2 = new JSONObject();
						JSONObject jobj3 = new JSONObject();
						JSONArray jArray = new JSONArray();

						try {
							jobj = response.optJSONObject("result");
							int totalCount = jobj.optInt("total_count");
							if(totalCount<=0)
							{
								Toast.makeText(
										ack, "Product Details are not available",
										2500).show();
							}
							else
							{
							jobj2 = jobj.optJSONObject("0");

							hsBean = new ProductDataBean();
							hsBean.setProdName(jobj2.optString("name"));
							hsBean.setDesc(jobj2.optString("description"));
							hsBean.setProdType(prodtype);
							if (jobj2.has("type_of_packing")) {
								hsBean.setPacking_size(jobj2.optString("type_of_packing"));
							}
							
							hsBean.setMrp(Float.parseFloat(jobj2.optString("price")));
							hsBean.setOklerPrice(Float.parseFloat(jobj2
									.optString("saleprice")));
							hsBean.setDiscount(Float.parseFloat(jobj2.optString("discount")));
							if (jobj2.has("side_effects")) {
								hsBean.setSide_effect(jobj2.optString("side_effects"));
							}
							if (jobj2.has("tax")) {
								hsBean.setTax(Float.parseFloat(jobj2.optString("tax")));
							}
							if (jobj2.has("Contraindications")) {
								hsBean.setContraIndi(jobj2.optString("Contraindications"));
							}
							hsBean.setProdId(Integer.valueOf(jobj2
									.optString("id")));
							if (jobj2.has("prescription_need")) {
								if (jobj2.optString("prescription_need").equals("null")
										|| jobj2.optString("prescription_need")
												.equals(null)
										|| jobj2.optString("prescription_need").equals("")) {
									hsBean.setPresc_needed(0);
								} else {
									hsBean.setPresc_needed(Integer.parseInt(jobj2
											.optString("prescription_need")));
								}
							}
							String jimg = jobj2.optString("images");
							String url2;
							if (jimg.equals(null) || jimg.equals("null")
									|| jimg.equals("")) {
								url2 = "drawable://" + R.drawable.tempcuff;
							} else {
								String j1[] = jimg.split(",");
								String j2 = j1[0];
								String colon = ":";
								String j3[] = j2.split(colon);
								String url = j3[2];
								// String url1 = url.substring(1);
								int length = url.length();
								url2 = url.substring(1, (length - 1));
							}
							String image_name = "";
							try {
								image_name = URLEncoder.encode(url2, "UTF-8");
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								showProgress(false);
								e.printStackTrace();
							}
							// JSONObject jimg2 = jimg.getJSONObject("");
							// JSONObject jimg3 = jobj2.getJSONObject("images");
							// pdBean.setImgUrl(jobj2.getJSONObject("images").getJSONObject("").optString("filename"));
							hsBean.setImgUrl(image_name);
							JSONObject uobj = new JSONObject();
							uobj = response.getJSONObject("product_image_url");
							hsBean.setMediumUrl(uobj
									.optString("productimage_url_medium"));
							hsBean.setSmallUrl(uobj
									.optString("productimage_url_small"));
							hsBean.setThumbUrl(uobj
									.optString("productimage_url_thumbnail"));
							hsBean.setClipArtUrl(uobj
									.optString("productimage_clipArt_images"));
							
							if (jobj2.has("indications")) {
								hsBean.setIndications(jobj2.optString("indications"));
							}
							hsBean.setDosage(jobj2.optString("dosage"));
							hsBean.setCompany(jobj2.optString("company_name"));
							if (jobj2.has("generic_val")) {
								hsBean.setGeneric_name(jobj2.optString("generic_val"));// need
																						// string
																						// from
																						// webservice
								// Log.e("URL", jobj2.optString("generic_val"));
							}
							
							if (jobj2.has("constituents_details")) {
								jArray = jobj2.getJSONArray("constituents_details");
								int len = jArray.length();
									
									jobj3 = jArray.optJSONObject(0);
									if(jobj3!=null){
									if (jobj3.has("contra_indication")) {
										hsBean.setContraIndi(jobj3
												.optString("contra_indication"));
									}
									if (jobj3.has("caution")) {
										hsBean.setCaution(jobj3.optString("caution"));
									}
									if (jobj3.has("side_effects")) {
										hsBean.setSide_effect(jobj3.optString("side_effects"));
									}
									if (jobj3.has("weight")) {
										hsBean.setWeight(jobj3.optString("weight"));
									}
									}
							}
							

							ArrayList<ProductDataBean> hList = new ArrayList<ProductDataBean>();
							hList.add(hsBean);
							Okler.getInstance().setProdList(hList);
							Gson gson = new Gson();
							String hsbean = gson.toJson(hsBean);
							Intent intent = null;
							if(Okler.getInstance().getBookingType()==0)
									intent = new Intent(ack,
											AlloMedsActivity.class);
									
							else
									intent = new Intent(ack,
									ProductDetailsActivity.class);
							intent.putExtra("MedId", hsbean);
							startActivity(intent);
							showProgress(false);
							}
							// finish();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							showProgress(false);
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						showProgress(false);
					}
				});

		VolleyRequest.addJsonObjectRequest(ack, prodjson);
		// return hsBean;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(relParentSearchMed);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		int lastItemInScreen = firstVisibleItem + visibleItemCount;
		if ((lastItemInScreen == totalItemCount)
				&& totalrecordsfromwebservice != Glist.size()) {
			if (pageNo > (totalrecordsfromwebservice / 10))
				return;
			if (!isONScrollProg) {
				isONScrollProg = true;
				// Url = MedUrl(serverUrl, 0, "asc", pageNo, abc);
				// Url = disUrl(serverUrl, 4, diseaseId, "asc", abc, pageNo);
				pageNo++;
				setUrl();
				callWebService(Url,false);

				Log.e("TP", "" + pageNo);
			}

		}

	}

	private void showProgress(boolean paramBoolean) {
		if (paramBoolean) {
			this.progressLinLayout.setVisibility(View.VISIBLE);
			return;
		}
		this.progressLinLayout.setVisibility(View.INVISIBLE);
	}

	public void callWebService(String Url,boolean isSearch) {
		if(wjson!=null)
			wjson.cancel();
		wjson = new WebJsonObjectRequest(Method.GET, Url,
				new JSONObject(), new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub

						processResponse(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						showProgress(false);
						setAlphaClick();
					}
				});
		if(isSearch)
		{
			String str = "search";
			wjson.setTag(str);
			wjson.setSearch(true);
		}
		if (VolleyRequest.addJsonObjectRequest(ack, wjson)) {
			showProgress(true);
		} else {
			showProgress(false);
		}
		
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub

		try {
			if(wjson!=null)
				wjson.cancel();
			abc = URLEncoder.encode(newText, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(abc.length()>=3){
		pageNo = 0;
		cur_pageNo = 0;
		totalrecordsfromwebservice = 0;
		page_count = 0;
		/*
		 * Glist.clear(); gdList.clear(); ProdList.clear();
		 */
		setUrl();
		isSearching = true;
		callWebService(Url,true);
		}
		else if(abc.length()==0){
			setUrl();
			callWebService(Url,true);
		}
		else
		{
			pageNo= 0;
			cur_pageNo=0;
			totalrecordsfromwebservice=0;
			page_count=0;
			Glist.clear();
			gdList.clear();
			ProdList.clear();
			gAdap.notifyDataSetChanged();
			if(no_items.getVisibility()==0){
			no_items.setVisibility(View.GONE);
			medListExp.setVisibility(View.VISIBLE);
			}
		}
	if(newText.equals(""))
		{
			currentPosition.setText("#");
			currentPosition.setTextColor(getResources().getColor(R.color.Red));
		}
		return false;
	}

}
