package com.okler.diagno;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.okler.adapters.DiagnoPackageAdapter;
import com.okler.adapters.DiagnoTestAdapter;
import com.okler.adapters.TestRcAdapter;
import com.okler.android.BaseActivity;
import com.okler.android.SearchHistoryActivity;
import com.okler.databeans.diagnobean.DiagnoLabTestDataBean;
import com.okler.databeans.diagnobean.DiagnoPackagesDataBean;
import com.okler.databeans.diagnobean.DiagnoTestDataBean;
import com.okler.enums.DiagnoOrderType;
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
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.*;
import com.android.volley.Request.Method;
import android.widget.SearchView;

public class DiagnoTestPkgHome extends BaseActivity implements Response.Listener<JSONObject>,
Response.ErrorListener, SearchView.OnQueryTextListener
//,OnScrollListener {
{
	Context context;
	Activity ack;
	Toolbar toolBar;
	View bottomBarLayout;
	ImageView imgBack,overflowIcon;
	TextView text,lastClick,currentPosition,search_history;
	private static TextView noOfSelectedTest;
	private String[] alpha = { "#", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };
	ListView lv1;
	

	ArrayAdapter<String> adapter;
	String getAllDiagnoTest="",searchText="",searchURL="",getAllPackages="",queryText="";

	int count=0,totalTestsCount = 0, lastPage = 0,diseaseIdReceived=0,currPageNo=0, totalPkgCount;
	boolean flagCallfromDiagnoDisease=false,loadingInProgress=false, flagNoPckFound, isScrolling, isSearching
			,isTestsPkgsByLabs=false,isFirstTimeCall=false;
	static ArrayList<DiagnoTestDataBean> finallist;
	static ArrayList<DiagnoPackagesDataBean> finallist1;
	RecyclerView lvTests2;
	DiagnoTestAdapter testAdp;
	DiagnoPackageAdapter pkgAdp;
	static Button nextBtn;
	SearchView searchView;
	LinearLayout progressLinLayout;
	DiagnoLabTestDataBean labTestBean;
	static DiagnoOrderType orderType;
	TextView no_data;
	LinearLayoutManager linearManager;
	TestRcAdapter tAdap;
	boolean isSearchClicked=false;
	int lastItemInScreen,visibleItemCount,totalItemCount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagno_test_pkg_home);
		context = getApplicationContext();
		ack = this;
		findViews();	
		
		linearManager = new LinearLayoutManager(ack);
		lvTests2.setLayoutManager(linearManager);
		lvTests2.setHasFixedSize(true);
		setSupportActionBar(toolBar);
		handleMapping(bottomBarLayout);
		no_data = (TextView) findViewById(R.id.no_data_found);
		search_history = (TextView)findViewById(R.id.search_history);
		overflowIcon.setVisibility(View.INVISIBLE);
		UIUtils.setBackClick(toolBar, ack);
		Utilities.setTitleText(toolBar, "Diagnostic Test [1/5]");
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);		
		
		Okler.getInstance().setBookingType(9);
		adapter = setAlphabetsAdapter();		
		lv1.setAdapter(adapter);
		setAlphaScrollListener();
		
		orderType = Okler.getInstance().getDiagnoOrder().getOrderType();
		diseaseIdReceived = Okler.getInstance().getDiagnoOrder().getDiseaseId(); 
		if(orderType.equals(DiagnoOrderType.TEST)){
			finallist = new ArrayList<DiagnoTestDataBean>();
			tAdap = new TestRcAdapter(ack, finallist,lvTests2);
			lvTests2.setAdapter(tAdap);
		}
		else{
			finallist1 = new ArrayList<DiagnoPackagesDataBean>();
			pkgAdp = new DiagnoPackageAdapter(ack, finallist1,lvTests2);
			lvTests2.setAdapter(pkgAdp);
		}
				isTestsPkgsByLabs = getIntent().getBooleanExtra("isTestsPkgsByLabs", false);
				
		{
			
		}
		 
		if (Utilities.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN
				|| (Utilities
						.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_FB)
				|| (Utilities
						.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
		search_history.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DiagnoTestPkgHome.this,SearchHistoryActivity.class);
				if(orderType.equals(DiagnoOrderType.TEST))
					intent.putExtra("term_type_value", 1);
				else
					intent.putExtra("term_type_value", 2);
				intent.putExtra("DiseaseId", getIntent().getIntExtra("DiseaseId", 0));
				intent.putExtra("isTestsPkgsByLabs", getIntent().getBooleanExtra("isTestsPkgsByLabs", false));
				intent.putExtra("isFromSummary", getIntent().getBooleanExtra("isFromSummary", false));
				startActivity(intent);
			}
		});
		}else{
			search_history.setVisibility(View.INVISIBLE);
		}
		 setOnclickListenerOnMainList();
		setOnClickListenerOnAlphabets();
		setNextBtnListener();
		setupSearchView();
		lvTests2.addOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
					visibleItemCount = recyclerView.getChildCount();
					totalItemCount = linearManager.getItemCount();
					lastItemInScreen  = linearManager.findLastVisibleItemPosition()+1;
					
					if(isScrolling)
						return;
					else{
						
					if(orderType.equals(DiagnoOrderType.TEST)){
						Log.e("TP", "last "+lastItemInScreen+" total "+totalItemCount+" totaltest "+totalTestsCount+" final "+finallist.size());
					if ((lastItemInScreen == totalItemCount) && (totalTestsCount != finallist.size())) 
					{
						isScrolling=true;
						currPageNo++;
						searchURL = setUrlDiagnoDiseaseTest(searchText, currPageNo, "asc", "");
						getTestWebCall(getAllDiagnoTest);
					}
					}
					else{
						Log.e("TP", "last "+lastItemInScreen+" total "+totalItemCount+" totalpkg "+totalPkgCount+" final1 "+finallist1.size());
						if ((lastItemInScreen == totalItemCount) && (totalPkgCount != finallist1.size())) 
						{
							isScrolling=true;
							currPageNo++;
							getAllPackages = setPackageUrl("", currPageNo,"asc","");
							getPackageWebCall(getAllPackages);
						}
					}
					}
				}
		});
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
					Log.e("JsonErr", err);
				showProgress(false);
				loadingInProgress = false;

	}

	@Override
	public void onResponse(JSONObject response) {
		// TODO Auto-generated method stub
		getTest(response);
		showProgress(false);
		loadingInProgress = false;

	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
		try {

			searchText = URLEncoder.encode(query, "utf-8");
			currPageNo = 0;
			isFirstTimeCall=true;
			lastPage = 0;
			isSearchClicked=true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(orderType.equals(DiagnoOrderType.TEST)){
			finallist.clear();
			totalTestsCount=0;
			if (flagCallfromDiagnoDisease == false) {
				searchURL = setUrlDiagnoDiseaseTest(searchText, 0, "asc", "");
			} else {
				searchURL = setUrlDiagnoTest(diseaseIdReceived, searchText, 0,"");
			}
			getTestWebCall(searchURL);
			if (Utilities.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN
					|| (Utilities
							.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_FB)
					|| (Utilities
							.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
			UIUtils.addToSearchHistory(searchText, ack, 1);
			}
		}
		else{
			finallist1.clear();
			totalPkgCount=0;
			getAllPackages = setPackageUrl(searchText, currPageNo,"asc","");
			isFirstTimeCall=true;
			getPackageWebCall(getAllPackages);
			if (Utilities.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN
					|| (Utilities
							.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_FB)
					|| (Utilities
							.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
			UIUtils.addToSearchHistory(searchText, ack, 2);
			}
		}
		
		
		return false;

	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if(newText.equals("")&&isSearchClicked){
		try {
			currentPosition.setText("#");
			searchText = URLEncoder.encode(newText, "utf-8");
			currPageNo = 0;
			isFirstTimeCall=true;
			lastPage = 0;
			isSearchClicked=false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(orderType.equals(DiagnoOrderType.TEST)){
			finallist.clear();
			if (flagCallfromDiagnoDisease == false) {
				searchURL = setUrlDiagnoDiseaseTest(searchText, 0, "asc", "");
			} else {
				searchURL = setUrlDiagnoTest(diseaseIdReceived, searchText, 0,"");
			}
			getTestWebCall(searchURL);
		}
		else{
			finallist1.clear();
			getAllPackages = setPackageUrl(searchText, currPageNo,"asc","");
			isFirstTimeCall=true;
			getPackageWebCall(getAllPackages);
		}
		}

		return true;

	}
	
	public void findViews()
	{
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		overflowIcon = (ImageView) findViewById(R.id.overflowIcon);
		noOfSelectedTest = (TextView) findViewById(R.id.tvTitle);
		lvTests2 = (RecyclerView)findViewById(R.id.lvTests2);
		bottomBarLayout = findViewById(R.id.bottombar);
		lv1 = (ListView) findViewById(R.id.alphabetsScrView);
		nextBtn = (Button) findViewById(R.id.nextBtn);
		progressLinLayout = (LinearLayout) findViewById(R.id.progressLinLayout);
		currentPosition = (TextView)findViewById(R.id.currentPosition);
	}
	
	public ArrayAdapter<String> setAlphabetsAdapter()
	{
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, alpha) {
			@Override
			public View getView(int position, View convertView,
					android.view.ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text = (TextView) view
						.findViewById(android.R.id.text1);
				text.setTextColor(Color.BLACK);
				return view;

			}
		};
		
		return adapter;
	}
	
	public void setOnClickListenerOnAlphabets()
	{
		lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				isFirstTimeCall=true;
				isSearchClicked=false;
				searchView.setQuery("", false);
				if(orderType.equals(DiagnoOrderType.TEST))
				{
					finallist.clear();
					tAdap.notifyDataSetChanged();
					currPageNo = 0;
					totalTestsCount=0;
					String abc = ((TextView) view).getText().toString();
					text = (TextView) view.findViewById(android.R.id.text1);
					text.setTextColor(getResources().getColor(R.color.Red));
					if (lastClick != null) {
						if(lastClick!=text)
						lastClick.setTextColor(getResources().getColor(
								R.color.Black));
					}

					lastClick = text;
					if (abc.equals("#")) {
						currentPosition.setText("#");
						searchText = "";
						if (flagCallfromDiagnoDisease == false) {
							getAllDiagnoTest = setUrlDiagnoDiseaseTest("",0,"asc","");
							getTestWebCall(getAllDiagnoTest);
						} else {
							getAllDiagnoTest = setUrlDiagnoTest(diseaseIdReceived, "", 0,"");
							getTestWebCall(getAllDiagnoTest);
						}
					} else {
						searchText = abc;
						currentPosition.setText(abc);
						if (flagCallfromDiagnoDisease == false) {
							getAllDiagnoTest = setUrlDiagnoDiseaseTest(abc,0,"asc","");
							getTestWebCall(getAllDiagnoTest);
						} else {

							getAllDiagnoTest = setUrlDiagnoTest(diseaseIdReceived, searchText, 0,"");
							getTestWebCall(getAllDiagnoTest);
						}
					}
				}
				if(orderType.equals(DiagnoOrderType.PACKAGE))
				{
					finallist1.clear();
					pkgAdp.notifyDataSetChanged();
					currPageNo = 0;
					totalPkgCount=0;
					String abc = ((TextView) view).getText().toString();
					text = (TextView) view.findViewById(android.R.id.text1);
					text.setTextColor(getResources().getColor(R.color.Red));
					if (lastClick != null) {
						if(lastClick!=text)
						lastClick.setTextColor(getResources().getColor(
								R.color.Black));
					}

					lastClick = text;
					if (abc.equals("#")) 
					{
						currentPosition.setText("#");
						searchText = "";
							getAllPackages = setPackageUrl("",0,"asc","");
							getPackageWebCall(getAllPackages);
						
					} 
					else 
					{
						searchText = abc;
						currentPosition.setText(abc);
						getAllPackages = setPackageUrl(abc,0,"asc","");
						getPackageWebCall(getAllPackages);
					}
				}
				
			}
		});
	}
	
	public void setNextBtnListener()
	{
		
		nextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<DiagnoTestDataBean> dataBeanobj = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests();
				String[] testId = new String[dataBeanobj.size()];
				for (int i = 0; i < dataBeanobj.size(); i++) {
					String a = String.valueOf(dataBeanobj.get(i).getTestId());
					testId[i] = a;
				}
				Intent startDiagnoLabs = new Intent(context,
						DiagnoLabAvailable.class);
				startDiagnoLabs.putExtra("testId", testId);
				startDiagnoLabs.putExtra("callFromKey", "FromTest");
				startDiagnoLabs.putExtra("isFromSummary", getIntent().getBooleanExtra("isFromSummary", false));
				startActivity(startDiagnoLabs);
				//}
			}
		});

	}
	
	public void setupSearchView()
	{
		searchView = (SearchView) toolBar.findViewById(R.id.menu_search);
		searchView.setOnQueryTextListener(this);
		if(orderType.equals(DiagnoOrderType.TEST))
			searchView.setQueryHint("Search Tests");
		else
			searchView.setQueryHint("Search Packages");
		searchView.setIconifiedByDefault(false);

	}
	
	public void setOnclickListenerOnMainList()
	{
	}
	
	
	protected void getTestWebCall(String url) {
		// TODO Auto-generated method stub
		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, url,
				new JSONObject(), this, this);
		if (VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson)) {
			Log.d("Tag", "value searching");
			showProgress(true);
		}

	}
	
	protected void getPackageWebCall(String url) {
		// TODO Auto-generated method stub
		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, url, new JSONObject(),new Response.Listener<JSONObject>() 
				{

				@Override
				public void onResponse(JSONObject response) 
				{	
					getPackage(response);
				}				
			},new Response.ErrorListener() 
			{
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("email", "mail not sent... ");						
				}	
				});
				if(VolleyRequest.addJsonObjectRequest(getApplicationContext(),wjson))
					showProgress(true);
		}
	
	
	private void getTest(Object response) {
		
		JSONObject jobj = (JSONObject) response;		
		JSONObject jobj2 = new JSONObject();
		DiagnoTestDataBean testbean = null;
		int length=0;
		ArrayList<DiagnoTestDataBean> selectedTestList = new ArrayList<DiagnoTestDataBean>();
		selectedTestList = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentTests();
		if(isScrolling)
			isScrolling=false;
		if (!flagCallfromDiagnoDisease) {
			JSONObject resultObj = new JSONObject();
			try {
				lastPage = jobj.getInt("cur_page");
				resultObj = jobj.getJSONObject("result");
				
				if (flagCallfromDiagnoDisease == false)
					totalTestsCount = resultObj.getInt("test_count");
				else
					totalTestsCount = jobj.getInt("records_count");
				if (totalTestsCount == 0) {
					lvTests2.setVisibility(View.GONE);
					no_data.setVisibility(View.VISIBLE);
					showProgress(false);
				} else {
					lvTests2.setVisibility(View.VISIBLE);
					no_data.setVisibility(View.GONE);
					length = (resultObj.length()) - 1;
					for (int i = 0; i < length; i++) {
						jobj2 = resultObj.getJSONObject(i + "");
						testbean = new DiagnoTestDataBean();
						testbean.setTestId(Integer.parseInt(jobj2
								.getString("test_id")));
						testbean.setTestname(jobj2.getString("test_name"));
						if(selectedTestList.size()>0){
							for (int j = 0; j < selectedTestList.size(); j++) {
								int id = selectedTestList.get(j).getTestId();
								int id2 = testbean.getTestId();
								if(id == id2){
									testbean.setCkIsChecked(true);
									break;
								}
							}
						}
						finallist.add(testbean);
						if(!isFirstTimeCall)
						tAdap.notifyItemInserted(finallist.size());
					}
				}
				if(isFirstTimeCall){
					isFirstTimeCall=false;
					tAdap.notifyDataSetChanged();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else {
			JSONArray resultObj = new JSONArray();
			int len=0;
			try {
				lastPage = jobj.getInt("cur_page");
				totalTestsCount = jobj.getInt("records_count");
				if (totalTestsCount == 0) {
					lvTests2.setVisibility(View.GONE);
					no_data.setVisibility(View.VISIBLE);
					no_data.setText("Test not available");
					showProgress(false);
				} else {
					resultObj = jobj.getJSONArray("result");
					len = resultObj.length();
					lvTests2.setVisibility(View.VISIBLE);
					no_data.setVisibility(View.GONE);
					for (int i = 0; i < len; i++) {
						jobj2 = resultObj.getJSONObject(i);
						testbean = new DiagnoTestDataBean();
						testbean.setTestId(Integer.parseInt(jobj2
								.getString("test_id")));
						testbean.setTestname(jobj2.getString("test_name"));
						finallist.add(testbean);
						if(!isFirstTimeCall)
						tAdap.notifyItemInserted(finallist.size());
					}
					
					
				}
				if(isFirstTimeCall){
					isFirstTimeCall=false;
				tAdap.notifyDataSetChanged();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				showProgress(false);

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

	public static void setNextEnabled() {
		nextBtn.setEnabled(true);
	}

	public static void setNextDisabled() {
		nextBtn.setEnabled(false);
	}
	
	public static void setSelectedTest(int count)
	{
		if(count == 0)
		{
			noOfSelectedTest.setText("SELECT TEST");
		}
		else
		{
			noOfSelectedTest.setText(""+count+" "+"TEST SELECTED");
		}	
	}
	
	public static void setSelectedPackage(int count)
	{
		if(count == 0)
		{
			noOfSelectedTest.setText("SELECT PACKAGE");
		}
		else
		{
			noOfSelectedTest.setText(""+count+" "+"PACKAGE SELECTED");
		}	
	}
	
	public String setUrlDiagnoDiseaseTest(String searchTxt, int pageNum, String orderBy, String testId)
	{
		if(isTestsPkgsByLabs){
			int labid = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentLab().getLabId();
			getAllDiagnoTest = getString(R.string.serverUrl)
					+getString(R.string.getTestsByLabs)
					+searchTxt
					+ getString(R.string.getDiagnoTestPage)
					+ pageNum
					+ getString(R.string.getDiagnoTestOrderBy)
					+ orderBy
					+ getString(R.string.getDiagnoTestTestId)
					+ testId
					+getString(R.string.getLabsTest2)
					+labid;
		}else{
	
			getAllDiagnoTest = getString(R.string.getDiagnoTestSearch)
				+ searchTxt
				+ getString(R.string.getDiagnoTestPage)
				+ pageNum
				+ getString(R.string.getDiagnoTestOrderBy)
				+ orderBy
				+ getString(R.string.getDiagnoTestTestId)
				+ testId;
		
		}
		return getAllDiagnoTest;
	}
	
	public String setUrlDiagnoTest(int diseaseId,String searchTxt, int pageNum,String testId)
	{
		if(isTestsPkgsByLabs){
			int labid = Okler.getInstance().getDiagnoOrder().getLabTestDataBean().getCurrentLab().getLabId();
			getAllDiagnoTest = getString(R.string.serverUrl)
					+getString(R.string.getTestsByLabs)
					+searchTxt
					+ getString(R.string.getDiagnoTestPage)
					+ pageNum
					+ getString(R.string.getDiagnoTestOrderBy)
					+ "asc"
					+ getString(R.string.getDiagnoTestTestId)
					+ testId
					+getString(R.string.getLabsTest2)
					+labid
					+getString(R.string.getMedsForDisDiD)
					+diseaseId;
			flagCallfromDiagnoDisease=false;
		}else{
			getAllDiagnoTest = getString(R.string.getDiagnoTestforD1)
				+ diseaseId
				+ getString(R.string.getDiagnoTestforD2)
				+ searchTxt
				+ getString(R.string.getDiagnoTestforD3)
				+ pageNum;
			flagCallfromDiagnoDisease = true;
		}
		return getAllDiagnoTest;
	}
	
	public String setPackageUrl(String searchText2, int currPageNo2,String orderBy,String pkgId)
	{	
		if(isTestsPkgsByLabs){
			int labid = Okler.getInstance().getDiagnoOrder().getLabPkgDataBean().getCurrentLab().getLabId();
			getAllPackages = getString(R.string.serverUrl)
					+getString(R.string.getPkgsByLabs)
					+searchText2
					+getString(R.string.getDiagnoPackagePage)
					+currPageNo2
					+getString(R.string.getDiagnoPackageOrderBy)
					+orderBy
					+getString(R.string.getDiagnoPackagePkgId)
					+pkgId
					+getString(R.string.getLabsTest2)
					+labid;
					
		}else{
		getAllPackages =  getString(R.string.getDiagnoPackageSearch)+searchText2
				+getString(R.string.getDiagnoPackagePage)+currPageNo2
				+getString(R.string.getDiagnoPackageOrderBy)+orderBy
				+getString(R.string.getDiagnoPackagePkgId)+pkgId;
		}
		return getAllPackages;
	}
	
	private void getPackage(Object response) {
		JSONObject jobj = (JSONObject)response;
		JSONObject pck_jobj = new JSONObject();
		JSONArray resultjarr = new JSONArray();
		DiagnoPackagesDataBean pckbean = new DiagnoPackagesDataBean();
		if(isScrolling)
			isScrolling = false;
		if(isSearching)
			isSearching=false;
		try {
			totalPkgCount = jobj.getInt("packages_count");
			
			String messageString=jobj.getString("message");
			if(messageString.equals("No data found.")){
				this.lvTests2.setVisibility(View.GONE);
				no_data.setVisibility(View.VISIBLE);
				showProgress(false);
				flagNoPckFound=true;//2922016***bhagyashri
			}else{
				resultjarr=jobj.getJSONArray("result");
				int length=resultjarr.length();
				this.lvTests2.setVisibility(View.VISIBLE);
				no_data.setVisibility(View.GONE);
			System.out.println("length of result or number of pckg"+length);
			for(int i=0;i<resultjarr.length();i++){
				pck_jobj=resultjarr.getJSONObject(i);
				pckbean=new DiagnoPackagesDataBean();
				pckbean.setPacakageId(Integer.parseInt(pck_jobj.getString("package_id")));
				pckbean.setPackageName(pck_jobj.getString("package_name"));
				JSONObject pkgDetails = pck_jobj.optJSONObject("package_details");
				JSONObject pkgTestDetails = pkgDetails.optJSONObject("packageTestDetails");
				JSONArray testArr = pkgTestDetails.optJSONArray("ifAnyTests");
				ArrayList<DiagnoTestDataBean> testListInPkg = new ArrayList<DiagnoTestDataBean>();
				DiagnoTestDataBean dtBean;
				for (int j = 0; j < testArr.length(); j++) {
					dtBean = new DiagnoTestDataBean();
					JSONObject currTest = testArr.optJSONObject(j);
					try{
					dtBean.setTestId(Integer.parseInt(currTest.optString("test_id")));
					dtBean.setTestname(currTest.optString("test_name"));
					
					testListInPkg.add(dtBean);
					}catch(Exception e){
						Log.e("TP", pck_jobj.getString("package_id")+" "+pck_jobj.getString("package_id"));
						Log.e("TP", e.toString());
					}
				}
				pckbean.setTestList(testListInPkg);
				finallist1.add(pckbean);
				if(!isFirstTimeCall)
				pkgAdp.notifyItemInserted(finallist1.size());
			}
			
			
			}
			if(isFirstTimeCall){
				isFirstTimeCall=false;
			pkgAdp.notifyDataSetChanged();
			}
			showProgress(false);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showProgress(false);

		}

	}

@Override
	protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	if(searchText.isEmpty())
		searchText = getIntent().getStringExtra("searchText");
		if(searchText==null||searchText.equals("null"))
			searchText="";
		else{
			try {
				queryText = searchText;
				searchText = URLEncoder.encode(searchText, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		searchView.setQuery(queryText, false);
		isSearching = true;
		isSearchClicked=true;
		}
	if(orderType.equals(DiagnoOrderType.TEST))
	{
		if(diseaseIdReceived == 0)
		{
			finallist.clear();
			tAdap.notifyDataSetChanged();
			noOfSelectedTest.setText("SELECT TEST");
			currPageNo=0;
			getAllDiagnoTest = setUrlDiagnoDiseaseTest(searchText, currPageNo, "asc", "");
			isFirstTimeCall=true;
			getTestWebCall(getAllDiagnoTest);
		}
		else
		{
			finallist.clear();
			tAdap.notifyDataSetChanged();
			currPageNo=0;
			getAllDiagnoTest =	setUrlDiagnoTest(diseaseIdReceived, searchText, currPageNo,"");
			isFirstTimeCall=true;
			getTestWebCall(getAllDiagnoTest);
			
		}
		
	}
	else if(orderType.equals(DiagnoOrderType.PACKAGE))
	{
		finallist1.clear();
		pkgAdp.notifyDataSetChanged();
		currPageNo=0;
		
		noOfSelectedTest.setText("SELECT PACKAGE");
		getAllPackages = setPackageUrl(searchText, currPageNo,"asc","");
		isFirstTimeCall=true;
		getPackageWebCall(getAllPackages);
	}
	for(int i = 0;i<alpha.length;i++){
		if(searchText.equals(alpha[i])){
			currentPosition.setText(searchText);
			break;
		}else{
			if(searchText.equals("")){
				currentPosition.setText("#");
				break;
			}
		}
	}
}

 	public void setAlphaScrollListener(){
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
 }
}
