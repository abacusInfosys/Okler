package com.okler.android;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.*;
import com.okler.network.VolleyRequest;
import com.okler.adapters.DiseaseAdapter;
import com.okler.databeans.DiseaseDataBean;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchByDiseasesActivity extends BaseActivity implements
		Response.Listener<JSONObject>, Response.ErrorListener,
		SearchView.OnQueryTextListener, OnScrollListener {

	boolean isAlpha = false;
	String[] alpha = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
			"X", "Y", "Z" };
	String currURL;
	DiseaseAdapter dieseasesAdap;// ,dieseasesAdap2,searchAdp;
	ArrayList<DiseaseDataBean> dieseases = new ArrayList<DiseaseDataBean>();
	ListView lvDieseases,lv1;
	View bottomBarLayout;
	Toolbar toolBar;
	Button notifCount;
	ImageView imgBack;
	TextView currentPosition, text, lastClick,search_history;
	String getAllDiseasesAllo, getAllDiseasesHome, getAllDiseasesAyur;
	SearchView searchView;
	Button btnCart;
	LinearLayout progressLinLayout;
	int currBookingType, searchPageCount = 1, pageCount = 1,
			totaldiseasesCount = -1, totaldiseasesSearchCount = -1;
	boolean isOngoingSearch,isSeachClicked=false;
	String searchURL, normalURL;
	boolean is1Called = false;
	TextView no_items;
	ImageView overflowIcon;
	int curPageNum = 0;
	Activity act;
	boolean onScrollinProg = false;
	String searchTxt = "";
	String abc = "#";
	RelativeLayout back_layout;
	String bookingtype="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_by_diseases);
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		no_items = (TextView) findViewById(R.id.no_items);
		progressLinLayout = (LinearLayout) findViewById(R.id.progressLinLayout);
		overflowIcon = (ImageView) findViewById(R.id.overflowIcon);
		overflowIcon.setVisibility(View.INVISIBLE);
		lvDieseases = (ListView) findViewById(R.id.lvDiseases);
		dieseases = new ArrayList<DiseaseDataBean>();
		dieseasesAdap = new DiseaseAdapter(getApplicationContext(), dieseases);
		lvDieseases.setAdapter(dieseasesAdap);
		act = this;
		currBookingType = Okler.getInstance().getBookingType();
		if (currBookingType == 0) {
			Utilities.setTitleText(toolBar, getString(R.string.allopathy));
			getAllDiseasesAllo = getString(R.string.getDiseasesUrlAllo);
			normalURL = getAllDiseasesAllo;
			callWebService(getAllDiseasesAllo);
		} else if (currBookingType == 3) {
			Utilities.setTitleText(toolBar, getString(R.string.ayurvedic));
			getAllDiseasesAyur = getString(R.string.getDiseasesUrlAyur);
			normalURL = getAllDiseasesAyur;
			callWebService(getAllDiseasesAyur);
		} else {
			Utilities.setTitleText(toolBar, getString(R.string.homeopathy));
			getAllDiseasesHome = getString(R.string.getDiseasesUrlHome);
			normalURL = getAllDiseasesHome;
			callWebService(getAllDiseasesHome);
		}
		bookingtype = getIntent().getStringExtra("bookingType");
		currentPosition = (TextView) findViewById(R.id.currentPosition);
		UIUtils.setBackClick(toolBar, act);
		searchView = (SearchView) toolBar.findViewById(R.id.menu_search);
		btnCart = (Button) toolBar.findViewById(R.id.toolBarCount);
		search_history = (TextView)findViewById(R.id.search_history);
		toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler
				.getInstance().getBookingType()));
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		setUpSearchView();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, alpha);

		lv1 = (ListView) findViewById(R.id.alphabetsScrView);
		lv1.setAdapter(adapter);
		
		setItemClickOnAlphabetList();

		 lv1.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
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
		 
		lvDieseases.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				DiseaseDataBean dbean2 = (DiseaseDataBean) dieseasesAdap
						.getItem(position);
				int did = dbean2.getDiseaseId();
				Intent intent = new Intent(getApplicationContext(),
						GroupByMedsActivity.class);
				intent.putExtra("bookingType", bookingtype);
				intent.putExtra("DiseaseId", did);
				intent.putExtra("FromWhere", "searchByDiseases");
				startActivity(intent);

			}
		});
		lvDieseases.setOnScrollListener(this);
		search_history.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SearchByDiseasesActivity.this,SearchHistoryActivity.class);
				startActivity(intent);
				
			}
		});
	}

	private void setItemClickOnAlphabetList()
	{

		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				isAlpha = true;
				isSeachClicked=false;
				searchView.setQuery("", false);
				lv1.setOnItemClickListener(null);
				dieseases.clear();
				dieseasesAdap.notifyDataSetChanged();
				abc = ((TextView) view).getText().toString();

				text = (TextView) view.findViewById(android.R.id.text1);
				text.setTextColor(getResources().getColor(R.color.Red));
				if (lastClick != null) {
					lastClick.setTextColor(getResources().getColor(
							R.color.Black));
				}
				lastClick = text;
				currentPosition.setText(abc);

				is1Called = false;
				searchPageCount = 0;
				pageCount = 0;

				if (abc.equals("#")) {
					if (Okler.getInstance().getBookingType() == 0) {
						dieseases.clear();
						dieseasesAdap.notifyDataSetChanged();
						totaldiseasesCount = 0;
						pageCount = 0;
						getAllDiseasesAllo = getString(R.string.getDiseasesUrlAllo);
						searchTxt = "";
						callWebService(getAllDiseasesAllo);
						pageCount++;

					} else if (Okler.getInstance().getBookingType() == 3) {
						dieseases.clear();
						dieseasesAdap.notifyDataSetChanged();
						totaldiseasesCount = 0;
						pageCount = 0;
						getAllDiseasesAyur = getString(R.string.getDiseasesUrlAyur);
						searchTxt = "";
						callWebService(getAllDiseasesAyur);
						pageCount++;

					} else {
						dieseases.clear();
						dieseasesAdap.notifyDataSetChanged();
						totaldiseasesCount = 0;
						pageCount = 0;
						getAllDiseasesHome = getString(R.string.getDiseasesUrlHome);
						searchTxt = "";
						callWebService(getAllDiseasesHome);
						pageCount++;

					}
				} else {
					if (Okler.getInstance().getBookingType() == 0) {
						dieseases.clear();
						dieseasesAdap.notifyDataSetChanged();
						String getAllDiseasesAllo1 = getString(R.string.getDiseasesUrlAllo);
						String part2 = getString(R.string.getMedsUrlStartWith2);
						getAllDiseasesAllo = getAllDiseasesAllo1 + part2 + abc;
						totaldiseasesCount = 0;
						pageCount = 0;
						searchTxt = abc;
						callWebService(getAllDiseasesAllo);
						pageCount++;
					} else if (Okler.getInstance().getBookingType() == 3) {
						dieseases.clear();
						dieseasesAdap.notifyDataSetChanged();
						String getAllDiseasesAyur1 = getString(R.string.getDiseasesUrlAyur);
						String part2 = getString(R.string.getMedsUrlStartWith2);
						getAllDiseasesAyur = getAllDiseasesAyur1 + part2 + abc;
						totaldiseasesCount = 0;
						pageCount = 0;
						searchTxt = abc;
						callWebService(getAllDiseasesAyur);
						pageCount++;
					} else {
						dieseases.clear();
						dieseasesAdap.notifyDataSetChanged();
						String getAllDiseasesHome1 = getString(R.string.getDiseasesUrlHome);
						String part2 = getString(R.string.getMedsUrlStartWith2);
						getAllDiseasesHome = getAllDiseasesHome1 + part2 + abc;
						totaldiseasesCount = 0;
						pageCount = 0;
						searchTxt = abc;
						callWebService(getAllDiseasesHome);
						pageCount++;
					}
				}
				isAlpha=true;
				dieseases.clear();
				dieseasesAdap.notifyDataSetChanged();
			}
		});
	}
	private void setUpSearchView() {
		// TODO Auto-generated method stub
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint("Search");
	}

	private void callWebService(String getAllDiseasesStr) {
		String str = getAllDiseasesStr + getString(R.string.getdiseasePagePart)
				+ "0";
		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, str,
				new JSONObject(), this, this);
		if (VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson)) {
			showProgress(true);
		} else {
			showProgress(false);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(btnCart, act);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		String err = String.valueOf(error);
		setItemClickOnAlphabetList();
		showProgress(false);
		Log.e("JsonErr", err);
	}

	@Override
	public void onResponse(JSONObject response) {
		setItemClickOnAlphabetList();
		processResponse(response);
	}

	private void processResponse(Object response) {
		JSONObject jobj = (JSONObject) response;
		JSONObject jobj2 = new JSONObject();
		JSONArray jarr = new JSONArray();
		DiseaseDataBean dbean = null;
		showProgress(false);
		if (onScrollinProg)
			onScrollinProg = false;
		try {
			int totalcnt = jobj.getInt("disease_mapping_count");
			jarr = jobj.getJSONArray("result");
			curPageNum = jobj.getInt("cur_page");
			pageCount = curPageNum + 1;

			ArrayList<DiseaseDataBean> finallist = new ArrayList<DiseaseDataBean>();
			for (int i = 0; i < jarr.length(); i++) {
				jobj2 = jarr.getJSONObject(i);
				dbean = new DiseaseDataBean();
				dbean.setDiseaseId(Integer.parseInt(jobj2.getString("id")));
				dbean.setDiseaseName(jobj2.getString("name"));
				dbean.setCount(Integer.parseInt(jobj2.getString("count")));
				finallist.add(dbean);
			}
			if(isAlpha){
				isAlpha=false;
			dieseases.clear();
			dieseasesAdap.notifyDataSetChanged();
			}
			dieseases.addAll(finallist);
			dieseases.trimToSize();
			totaldiseasesCount = totalcnt;
			dieseasesAdap.notifyDataSetChanged();
			if (dieseases.size() == 0) {
				lvDieseases.setVisibility(View.INVISIBLE);
				no_items.setVisibility(View.VISIBLE);
			} else {
				lvDieseases.setVisibility(View.VISIBLE);
				no_items.setVisibility(View.GONE);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
		searchTxt = query;
		try {
			searchTxt = URLEncoder.encode(searchTxt, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isSeachClicked=true;
		callSearchWebService();
		
		
		return false;
	}

	private void showProgress(boolean paramBoolean) {
		if (paramBoolean) {
			this.progressLinLayout.setVisibility(View.VISIBLE);
			return;
		}
		this.progressLinLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onQueryTextChange(String searchText) {
		
		
		 if(searchText.length()==0 && isSeachClicked){
				searchTxt = "";
				isSeachClicked=false;
				callSearchWebService();
		 }
		return true;
	}

	public void callSearchWebService(){
		pageCount = 0;
		dieseases.clear();
		dieseasesAdap.notifyDataSetChanged();
		totaldiseasesCount = 0;
		
	
	currURL = normalURL + getString(R.string.getdiseaseSearchPart)
			+ searchTxt + getString(R.string.getdiseasePagePart)
			+ (pageCount);

		WebJsonObjectRequest getMedsJson = new WebJsonObjectRequest(Method.GET,
				currURL, new JSONObject(), new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						processResponse(response);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.e("Error ", "Some error occured" + error);
						isOngoingSearch = false;
						showProgress(false);
					}
				});
	if (VolleyRequest.addJsonObjectRequest(getApplicationContext(),
			getMedsJson))
		showProgress(true);
	else
		showProgress(false);
	pageCount = 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int lastItemInScreen = firstVisibleItem + visibleItemCount;
		Log.d("On scroll called", "on scr called aa");
		if (dieseases.size() > totaldiseasesCount)
			return;
		if (pageCount > (totaldiseasesCount / 10))
			return;
		if ((lastItemInScreen == totalItemCount)
				&& (totaldiseasesCount != dieseases.size())) {
			Log.d("On scroll called", "we service called");

			String str = normalURL + getString(R.string.getdiseaseSearchPart)
					+ searchTxt + getString(R.string.getdiseasePagePart)
					+ (pageCount);
			if (!onScrollinProg || pageCount < 3) {
				WebJsonObjectRequest webjson = new WebJsonObjectRequest(
						Method.GET, str, new JSONObject(), this, this);
				if (VolleyRequest.addJsonObjectRequest(getApplicationContext(),
						webjson)) {
					onScrollinProg = true;
					pageCount++;
					showProgress(true);
				} else {
					showProgress(false);
				}
			}
		}
	}
}
