package com.okler.android;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.android.volley.Request.Method;
import com.okler.adapters.ProductsAdapter;
import com.okler.databeans.BrandsDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.helpnsupport.SupportHelpActivity;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchByMed1Activity extends BaseActivity implements
		Response.Listener, Response.ErrorListener,
		SearchView.OnQueryTextListener, OnScrollListener {
	ArrayList<ProductDataBean> finallist; // ,amed_list,med_list,search_list;

	String[] alpha = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
			"X", "Y", "Z" };
	ProductsAdapter med_prodAdap;
	ListView lvMeds;
	View bottomBarLayout;
	Toolbar toolBar;
	Button notifCount;
	String FromWhere, getAllMedsUrl, getAllMedsByDisease;
	int diseaseId, mappingId, pageNo, prodId, prodtype, BrandId, priceRange;
	ImageView imgBack;
	TextView currentPosition, lastClick, text;
	ProductDataBean apdBean, pdBean, updBean;
	Button btnCart;
	String abc = "";
	int lastBrandsPage = 1;
	boolean isFromDisease = false;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	ImageView overflow;
	LinearLayout relParentSearchMed, rightMenuSubcatsMeds;
	ArrayList<BrandsDataBean> brandsData;
	String brand_id = "";
	SearchView searchView;
	LinearLayout progressLinLayout;
	boolean onScrollinProg = false;
	Context context;
	BrandsDataBean brndHS = null;
	int totalResultsFromWebService = 0, lastPageCount = 0;
	String searchText;
	Activity ack;
	TextView no_items;
	boolean testingNwError = false;
	/* Changes for footer */
	ImageButton btnHome, btnNotif, btnUploadPres, btnManageProfi, btnQues;
	RelativeLayout homeRL, notifRL, uploadPresRL, manageProfRL, questionsRL;
	WebJsonObjectRequest getMedsJson;
	boolean isResponse = false;
	int totalBrands;
	Button viewMore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_by_med1);
		FromWhere = getIntent().getStringExtra("FromWhere");
		diseaseId = getIntent().getIntExtra("DiseaseId", 0);
		ack = this;
		context = getApplicationContext();

		no_items = (TextView) findViewById(R.id.no_items);
		progressLinLayout = (LinearLayout) findViewById(R.id.progressLinLayout);
		currentPosition = (TextView) findViewById(R.id.currentPosition);
		finallist = new ArrayList<ProductDataBean>();
		/*
		 * search_list = new ArrayList<ProductDataBean>(); med_list = new
		 * ArrayList<ProductDataBean>();
		 */
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		btnCart = (Button) toolBar.findViewById(R.id.toolBarCount);
		searchView = (SearchView) toolBar.findViewById(R.id.menu_search);
		progressLinLayout = (LinearLayout) findViewById(R.id.progressLinLayout);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		relParentSearchMed = (LinearLayout) findViewById(R.id.sliderManuParentLinear);
		rightMenuSubcatsMeds = (LinearLayout) findViewById(R.id.rightMenuSubcatsMeds);
		overflow = (ImageView) toolBar.findViewById(R.id.overflowIcon);
		setSupportActionBar(toolBar);
		ActionBar ab = getSupportActionBar();
		// ab.setDisplayHomeAsUpEnabled(true);
		toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler
				.getInstance().getBookingType()));
		brandsData = new ArrayList<BrandsDataBean>();
		setURLAndTitle();
		setToolBarListeners();
		setUpSearchView();
		// Set all fields to empty
		pageNo = 0;
		searchText = "";
		brand_id = "";

		if (FromWhere.equals("searchByDiseases")) {
			getMedsForDisease();
			isFromDisease = true;
		} else if (FromWhere.equals("searchByMedi")) {
			getAllMeds();
		}

		if (Okler.getInstance().getBookingType() == 0) {
			brandsData = Okler.getInstance().getAlloBrands();
			populateBrands();
		} else if (Okler.getInstance().getBookingType() == 4) {
			brandsData = Okler.getInstance().getHomeoBrands();
			populateBrands();
		}
		if (Okler.getInstance().getBookingType() == 3)
			overflow.setVisibility(View.INVISIBLE);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
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

		ListView lv1 = (ListView) findViewById(R.id.alphabetsScrView);
		lv1.setAdapter(adapter);
		lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// int sel = view.getId();
				// amed_list = new ArrayList<ProductDataBean>();
				finallist.clear();
				abc = ((TextView) view).getText().toString();
				text = (TextView) view.findViewById(android.R.id.text1);
				text.setTextColor(getResources().getColor(R.color.Red));
				if (lastClick != null)
					lastClick.setTextColor(getResources().getColor(
							R.color.Black));

				lastClick = text;
				currentPosition.setText(abc);
				pageNo = 0;
				searchText = "";
				brand_id = "";
				if (abc.equals("#")) {
					searchText = "";
					if (isFromDisease) {
						// Call disease web service
						getMedsForDisease();
					} else {
						// call med web service
						getAllMeds();
					}
				} else {
					searchText = abc;
					if (FromWhere.equals("searchByDiseases")) {
						// Call disease web service
						getMedsForDisease();
					} else {
						// call med web service
						getAllMeds();
					}
				}
				pageNo = 1;
			}
		});

		lvMeds = (ListView) findViewById(R.id.lvMeds);
		med_prodAdap = new ProductsAdapter(context, finallist);
		lvMeds.setAdapter(med_prodAdap);
		pageNo++;
		lvMeds.setOnScrollListener(this);
		lvMeds.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showProdDetails(position);
			}
		});
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerSetUp();
		// setFooterIcons();
	}

	private void startMyAccountOrSignIn(UserStatusEnum curStatus) {
		Intent intent;
		switch (curStatus.ordinal()) {
		case 0:
		case 5:
			intent = new Intent(this, NewSignIn.class);
			startActivity(intent);
			break;
		case 1:
		case 2:
		case 3:
		case 4:
			intent = new Intent(this, MyAccount.class);
			startActivity(intent);
			break;
		}
	}

	private void showProdDetails(int position) {
		// TODO Auto-generated method stub
		ProductDataBean pdbean2 = new ProductDataBean();
		pdbean2 = (ProductDataBean) med_prodAdap.getItem(position);
		Gson gson = new Gson();
		String pbean = gson.toJson(pdbean2);
		// String.valueOf(pdbean2);
		Intent intent = new Intent(getApplicationContext(),
				ProductDetailsActivity.class);
		intent.putExtra("MedId", pbean);
		startActivity(intent);
	}

	private void setToolBarListeners() {
		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
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

	private void populateBrands() {
		// if(lastBrandsPage != 1)

		if (brandsData.size() <= 0) {
			lastBrandsPage = 0;
			setNextBrands(0);
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
					for (int i = 0; i < length; i++) {

						if (brandsData.get(i).getBrandId()
								.equals(v.getTag().toString())) {
							finallist.clear();
							searchText = "";
							brand_id = brandsData.get(i).getBrandId();
							if (FromWhere.equals("searchByDiseases")) { //

								getMedsForDisease();

							} else if (FromWhere.equals("searchByMedi")) {
								if (Okler.getInstance().getBookingType() == 3) {

									brand_id = "";

								}
								// getAllMedsUrl = setMedsURL(0, "", "",
								// prodtype, "", brand_id, "", "asc");
								getAllMeds();
							}
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
			if (totalBrands > brandsData.size())
				rightMenuSubcatsMeds.addView(viewMore);

			viewMore.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setNextBrands(lastBrandsPage);
				}
			});
		}
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
							JSONArray resultsArr = response
									.getJSONArray("result");
							totalBrands = response.optInt("total_brand_count");
							for (int j = 0; j < resultsArr.length(); j++) {
								brndHS = new BrandsDataBean();
								JSONObject subCates = resultsArr
										.getJSONObject(j);
								brndHS.setBrandId(subCates.getString("id"));
								brndHS.setBrandName(subCates
										.getString("brand_name"));
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

	public void setURLAndTitle() {
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
		getAllMedsUrl = setMedsURL(0, "", "", prodtype, "", brand_id, "", "");
	}

	private String setMedsURL(int pageNo, String startWith, String prodId,
			int prodType, String priceRange, String brand, String subCatId,
			String sort) {
		String tempURl;
		tempURl = getString(R.string.getMedsUrlPageNo1) + pageNo
				+ getString(R.string.getMedsUrlStartWith2) + startWith
				+ getString(R.string.getMedsUrlProdId3) + prodId
				+ getString(R.string.getMedsUrlProdType4) + prodtype
				+ getString(R.string.getMedsUrlPriceRange5) + priceRange
				+ getString(R.string.getMedsUrlBrandId6) + brand
				+ "&subcat_id=" + subCatId + "&sort_price=" + sort;
		return tempURl;
	}

	private String setDiseaseMedsURL(int pageNo, int diseaseId,
			String mappingId, String recId, String brand, String sort,
			String searchText) {
		String tempUrl;
		tempUrl = getString(R.string.getMedsForDiease) + diseaseId
				+ getString(R.string.getMedsDiseaseMappingType) + mappingId
				+ getString(R.string.getMedsDiseaseRec) + recId
				+ getString(R.string.getMedsDiseaseBrand) + brand
				+ getString(R.string.getMedsDiseasePage) + pageNo
				+ getString(R.string.getMedsDiseaseOrderBy) + sort
				+ getString(R.string.getMedsDiseaseSearch) + searchText;
		return tempUrl;

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

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(relParentSearchMed);
		// menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

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

	private void setUpSearchView() {
		// TODO Auto-generated method stub
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint("Search");
	}

	private void getMedsForDisease() {
		getAllMedsByDisease = setDiseaseMedsURL(pageNo, diseaseId, ""
				+ mappingId, "", brand_id, "asc", searchText);
		getMedsJson = new WebJsonObjectRequest(Method.GET, getAllMedsByDisease,
				new JSONObject(), new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						showProgress(false);
						processDiseaseURLResponse(response);
						Okler.getInstance().setPdbean(updBean);
						Okler.getInstance().setProdList(finallist);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						showProgress(false);
					}
				});
		if (VolleyRequest.addJsonObjectRequest(getApplicationContext(),
				getMedsJson))
			showProgress(true);
		else
			showProgress(false);

	}

	private void processDiseaseURLResponse(JSONObject response) {
		String resp = String.valueOf(response);

		JSONObject jobj = (JSONObject) response;
		JSONObject jobj2 = new JSONObject();
		JSONObject jobj3 = new JSONObject();
		JSONObject resultsObj = new JSONObject();
		updBean = new ProductDataBean();
		try {
			if (onScrollinProg)
				onScrollinProg = false;
			if (jobj.get("result").equals("")) {
				lvMeds.setVisibility(View.INVISIBLE);
				no_items.setVisibility(View.VISIBLE);

			} else {
				resultsObj = jobj.getJSONObject("result");
				totalResultsFromWebService = resultsObj.optInt("total_count");
				int lenobj = resultsObj.length();
				int len = lenobj - 1;

				for (int i = 0; i < len; i++) {
					updBean = new ProductDataBean();
					jobj2 = resultsObj.getJSONObject(i + "");
					updBean.setProdId(Integer.parseInt(jobj2.optString("id")));
					updBean.setProdName(jobj2.optString("name"));
					updBean.setDesc(jobj2.optString("description"));
					updBean.setSide_effect(jobj2.optString("side_effects"));
					updBean.setMrp(Float.parseFloat(jobj2.optString("price")));
					updBean.setOklerPrice(Float.parseFloat(jobj2
							.optString("saleprice")));
					updBean.setDiscount(Float.parseFloat(jobj2
							.optString("discount")));
					updBean.setWeight(jobj2.optString("weight"));
					updBean.setProdType(prodtype);
					updBean.setTax(Float.parseFloat(jobj2.optString("tax")));
					updBean.setPacking_size(jobj2.optString("type_of_packing"));
					String jimg = jobj2.optString("images");
					String url2;
					if (jimg.equals(null)) {
						url2 = "drawable://" + R.drawable.no_image_found;
					} else {
						if (jimg.contains(",")) {
							String arr1[] = jimg.split(":");
							String sec = arr1[2];
							String arr2[] = sec.split("\"");
							url2 = arr2[1];
						} else {
							url2 = jimg;
						}
					}
					String image_name = "";
					try {
						image_name = URLEncoder.encode(url2, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					updBean.setImgUrl(image_name);

					if (jobj.has("product_image_url")) {
						JSONObject uobj = new JSONObject();
						uobj = jobj.getJSONObject("product_image_url");
						updBean.setMediumUrl(uobj
								.optString("productimage_url_medium"));
						updBean.setSmallUrl(uobj
								.optString("productimage_url_small"));
						updBean.setThumbUrl(uobj
								.optString("productimage_url_thumbnail"));
					}
					if (jobj2.optString("prescription_need").equals("null")
							|| jobj2.optString("prescription_need")
									.equals(null)
							|| jobj2.optString("prescription_need").equals("")) {
						updBean.setPresc_needed(0);
					} else {
						updBean.setPresc_needed(Integer.parseInt(jobj2
								.optString("prescription_need")));
					}
					updBean.setIndications(jobj2.optString("indications"));
					updBean.setDosage(jobj2.optString("dosage"));
					updBean.setCompany(jobj2.optString("company_name"));
					if (jobj2.has("generic_val")) {
						updBean.setGeneric_name(jobj2.optString("generic_val"));// need
																				// string
																				// from
																				// webservice
					}
					if (jobj2.has("constituents_details")) {
						JSONArray constiArr = jobj2
								.getJSONArray("constituents_details");
						String str = constiArr.optString(0);
						if (!str.isEmpty()) {
							jobj3 = constiArr.getJSONObject(0);
							if (jobj3.has("contra_indication")) {
								updBean.setContraIndi(jobj3
										.optString("contra_indication"));
							}
							if (jobj3.has("caution")) {
								updBean.setCaution(jobj3.optString("caution"));
							}
						}
					}
					updBean.setProdType(prodtype);
					finallist.add(updBean);
				}

			}

			med_prodAdap.notifyDataSetChanged();
			if (finallist.size() <= 0) {
				lvMeds.setVisibility(View.INVISIBLE);
				no_items.setVisibility(View.VISIBLE);

				/*
				 * Toast.makeText(ack, "No Data Available", Toast.LENGTH_LONG)
				 * .show();
				 */
			} else {
				lvMeds.setVisibility(View.VISIBLE);
				no_items.setVisibility(View.GONE);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getAllMeds() {
		String newURL = setMedsURL(pageNo, searchText, "", prodtype, "",
				brand_id, "", "");
		getMedsJson = new WebJsonObjectRequest(Method.GET, newURL,
				new JSONObject(), new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						finallist.clear();
						processResponse(response);
						showProgress(false);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						Toast.makeText(getApplicationContext(),
								String.valueOf(error), Toast.LENGTH_SHORT)
								.show();
						showProgress(false);
						try {
							// Log.i("Error", new
							// String(error.networkResponse.data));
						} catch (Exception ex) {
						}

					}
				});
		getMedsJson.setShouldCache(false);
		if (VolleyRequest.addJsonObjectRequest(getApplicationContext(),
				getMedsJson))
			showProgress(true);
		else
			showProgress(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
	public void onErrorResponse(VolleyError error) {
		Log.e("sds", "fdf" + getAllMedsUrl);
		showProgress(false);
	}

	@Override
	public void onResponse(Object response) {
		showProgress(false);
		if (isFromDisease)
			processDiseaseURLResponse((JSONObject) response);
		else
			processResponse(response);

	}

	public void processResponse(Object response) {
		ArrayList<ProductDataBean> tempProds = new ArrayList<ProductDataBean>();
		String resp = String.valueOf(response);
		// Toast.makeText(getApplicationContext(), resp,
		// Toast.LENGTH_LONG).show();
		JSONObject jobj = (JSONObject) response;
		JSONObject jobj1 = new JSONObject();
		try {
			jobj1 = jobj.getJSONObject("result");
			totalResultsFromWebService = jobj1.getInt("total_count");
		} catch (JSONException e1) {

			e1.printStackTrace();
		}
		JSONObject jobj2 = new JSONObject();
		JSONObject jobj3 = new JSONObject();
		JSONArray jArray = new JSONArray();
		pdBean = null;
		if (onScrollinProg)
			onScrollinProg = false;
		int lenobj = jobj1.length();

		try {
			int lenth = lenobj - 3;
			for (int i = 0; i < lenth; i++) {

				jobj2 = jobj1.getJSONObject(i + "");
				int len = jobj2.length();
				pdBean = new ProductDataBean();
				pdBean.setProdId(Integer.parseInt(jobj2.optString("id")));
				pdBean.setProdName(jobj2.optString("name"));
				pdBean.setProdType(prodtype);
				if (jobj2.has("type_of_packing")) {
					pdBean.setPacking_size(jobj2.optString("type_of_packing"));
				}
				pdBean.setDesc(jobj2.optString("description"));
				if (jobj2.has("side_effects")) {
					pdBean.setSide_effect(jobj2.optString("side_effects"));
				}
				pdBean.setMrp(Float.parseFloat(jobj2.optString("price")));
				pdBean.setOklerPrice(Float.parseFloat(jobj2
						.optString("saleprice")));
				pdBean.setDiscount(Float.parseFloat(jobj2.optString("discount")));
				if (jobj2.has("weight")) {
					pdBean.setWeight(jobj2.optString("weight"));
				}
				if (jobj2.has("tax")) {
					pdBean.setTax(Float.parseFloat(jobj2.optString("tax")));
				}
				// pdBean.setPacking_size(jobj2.optString("type_of_packing"));
				String jimg = jobj2.optString("images");
				String url2;
				if (jimg.equals(null)) {
					url2 = "drawable://" + R.drawable.no_image_found;
				} else {
					if (jimg.contains(",")) {
						String arr1[] = jimg.split(":");
						String sec = arr1[2];
						String arr2[] = sec.split("\"");
						url2 = arr2[1];

					} else {
						url2 = jimg;
					}
				}

				String image_name = "";
				try {
					image_name = URLEncoder.encode(url2, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pdBean.setImgUrl(image_name);

				if (jobj.has("product_image_url")) {
					JSONObject uobj = new JSONObject();
					uobj = jobj.getJSONObject("product_image_url");
					pdBean.setMediumUrl(uobj
							.optString("productimage_url_medium"));
					pdBean.setSmallUrl(uobj.optString("productimage_url_small"));
					pdBean.setThumbUrl(uobj
							.optString("productimage_url_thumbnail"));
					pdBean.setClipArtUrl(uobj
							.optString("productimage_clipArt_images"));
				}
				if (jobj2.has("prescription_need")) {
					if (jobj2.optString("prescription_need").equals("null")
							|| jobj2.optString("prescription_need")
									.equals(null)
							|| jobj2.optString("prescription_need").equals("")) {
						pdBean.setPresc_needed(0);
					} else {
						pdBean.setPresc_needed(Integer.parseInt(jobj2
								.optString("prescription_need")));
					}
				}
				if (jobj2.has("indications")) {
					pdBean.setIndications(jobj2.optString("indications"));
				}
				pdBean.setDosage(jobj2.optString("dosage"));
				pdBean.setCompany(jobj2.optString("company_name"));
				if (jobj2.has("generic_val")) {
					pdBean.setGeneric_name(jobj2.optString("generic_val"));// need
																			// string
																			// from
																			// webservice
					// Log.e("URL", jobj2.optString("generic_val"));
				}
				if (jobj2.has("constituents_details")) {
					jArray = jobj2.getJSONArray("constituents_details");
					if (jArray.length() < 25) {

					} else {
						
						jobj3 = jArray.getJSONObject(0);
						if (jobj3.has("contra_indication")) {
							pdBean.setContraIndi(jobj3
									.optString("contra_indication"));
						}
						if (jobj3.has("caution")) {
							pdBean.setCaution(jobj3.optString("caution"));
						}
					}
				}
				pdBean.setProdType(prodtype);
				tempProds.add(pdBean);
				finallist.add(pdBean);

			}
			med_prodAdap.notifyDataSetChanged();
			if (finallist.size() <= 0) {
				lvMeds.setVisibility(View.INVISIBLE);
				no_items.setVisibility(View.VISIBLE);

				/*
				 * Toast.makeText(ack, "No Data Available", Toast.LENGTH_LONG)
				 * .show();
				 */
			} else {
				lvMeds.setVisibility(View.VISIBLE);
				no_items.setVisibility(View.GONE);
			}
		} catch (JSONException e) {
			String er = String.valueOf(e);
			// Toast.makeText(getApplicationContext(), er,
			// Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		Okler.getInstance().setPdbean(pdBean);
		Okler.getInstance().setProdList(finallist);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ArrayList<ProductDataBean> plist = Okler.getInstance().getProdList();
		// Toast.makeText(getApplicationContext(), "",
		// Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		Log.d("aa", "Clicked");
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
		return false;
	}

	@Override
	public boolean onQueryTextChange(String searchTxt) {
		// TODO Auto-generated method stub
		String newURL;
		// lastSearchText=searchText;
		pageNo = 0;
		finallist.clear();
		try {
			searchText = URLEncoder.encode(searchTxt, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (isFromDisease)
			newURL = setDiseaseMedsURL(pageNo, diseaseId, "" + mappingId, "",
					brand_id, "asc", searchText);
		else
			newURL = setMedsURL(pageNo, searchText, "", prodtype, "", brand_id,
					"", "");
		if (getMedsJson != null)
			getMedsJson.cancel();
		getMedsJson = new WebJsonObjectRequest(Method.GET, newURL,
				new JSONObject(), new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						showProgress(false);
						if (isFromDisease)
							processDiseaseURLResponse(response);
						else
							processResponse(response);

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.e("Error ", "Some error occured" + error);
						showProgress(false);
					}
				});

		if (VolleyRequest.addJsonObjectRequest(getApplicationContext(),
				getMedsJson))
			showProgress(true);
		else
			showProgress(false);
		pageNo = 1;
		return true;
	}

	private void showProgress(boolean paramBoolean) {
		if (paramBoolean) {
			this.progressLinLayout.setVisibility(View.VISIBLE);
			return;
		}
		this.progressLinLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(btnCart, ack);

	}

	public enum currList {
		FINAL_LIST, MED_LIST, AMED_LIST, SEARCH_LIST
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int lastItemInScreen = firstVisibleItem + visibleItemCount;

		String newURL;

		// TODO Auto-generated method stub
		if ((lastItemInScreen == totalItemCount)
				&& (totalResultsFromWebService != finallist.size())) {
			if (pageNo > (totalResultsFromWebService / 10))
				return;
			if (!onScrollinProg || pageNo < 3) {
				if (isFromDisease)
					newURL = setDiseaseMedsURL(pageNo, diseaseId, ""
							+ mappingId, "", brand_id, "asc", searchText);
				else
					newURL = setMedsURL(pageNo, searchText, "", prodtype, "",
							brand_id, "", "");
				WebJsonObjectRequest webjson = new WebJsonObjectRequest(
						Method.GET, newURL, new JSONObject(), this, this);
				if (VolleyRequest.addJsonObjectRequest(getApplicationContext(),
						webjson)) {
					onScrollinProg = true;
					showProgress(true);
					pageNo++;
				} else {
					showProgress(false);
				}
			}

		}
	}
}
