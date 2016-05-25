package com.okler.diagnostics;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.okler.adapters.DiagnoPackageAdapter;
import com.okler.adapters.DiseaseAdapter;
import com.okler.android.BaseActivity;
import com.okler.android.Physiotherapy;
import com.okleruser.R;
import com.okler.databeans.DiagnoPackageDataBean;
import com.okler.databeans.DiseaseDataBean;
import com.okler.databeans.TestDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class DiagnoPackageHome extends BaseActivity implements Response.Listener,Response.ErrorListener,SearchView.OnQueryTextListener,OnScrollListener{
	private View bottomBarLayout;
	private Toolbar toolBar;
	private Button notifCount;
	private ArrayList<DiagnoPackageDataBean> packages;
	private DiagnoPackageAdapter dpadap;
	private ListView lvPackages,listAlpha;
	private ImageView imgBack;
	private TextView currentPosition,lastClick,text;
	private ArrayAdapter<String> adapter;
	private String getAllDiagnoPackageUrl;
	Boolean flag;
	ArrayList<DiagnoPackageDataBean> finallist = new ArrayList<DiagnoPackageDataBean>();
	int currPageNo=0,totalPkgCount=0;
	String searchText="";
	TextView no_items;
	LinearLayout progressLinLayout;
	SearchView searchView;
	private String[] alpha = {"#","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	boolean onScrollinProg = false;
	boolean flagNoPckFound=false;
	ImageView overflowIcon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagno_package_home);
		toolBar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		ActionBar ab = getSupportActionBar();
		//ab.setDisplayHomeAsUpEnabled(true);
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		bottomBarLayout = findViewById(R.id.bottombar);
		
		handleMapping(bottomBarLayout);
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
		overflowIcon = (ImageView) findViewById(R.id.overflowIcon);
		overflowIcon.setVisibility(View.INVISIBLE);
	    imgBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(imgBack.getWindowToken(), 0);

			}
		});
	    Utilities.setTitleText(toolBar, "Diagnostic  [1/5]");
	    Okler.getInstance().setBookingType(9);
	    progressLinLayout = (LinearLayout)findViewById(R.id.progressLinLayout);
	    currentPosition = (TextView)findViewById(R.id.currentPosition_Diagno_Pac);   
	    //Adapter for alphabetical List
	    flag=true;
	    getAllDiagnoPackageUrl = getString(R.string.getDiagnoPackageSearch)+getString(R.string.getDiagnoPackagePage)+
	    		getString(R.string.getDiagnoPackageOrderBy)+getString(R.string.getDiagnoPackagePkgId);
		getPackageWebCall(getAllDiagnoPackageUrl);
	     adapter = new ArrayAdapter<String>(this,
		              android.R.layout.simple_list_item_1, android.R.id.text1, alpha){
	    	 @Override
	    	 public View getView(int position, View convertView, android.view.ViewGroup parent) {
	    		 View view = super.getView(position, convertView, parent);
	  		        TextView text = (TextView) view.findViewById(android.R.id.text1);
	  		        text.setTextColor(Color.BLACK);
	  		        return view;
	    	 	}
	    	 };
	    	 no_items = (TextView) findViewById(R.id.no_items);
	     listAlpha= (ListView)findViewById(R.id.alphabetsScrViewPac) ;
	     listAlpha.setAdapter(adapter);
	     listAlpha.setOnItemClickListener(new OnItemClickListener() {
			@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
				String abc = ((TextView)view).getText().toString();
				text = (TextView)view.findViewById(android.R.id.text1);
				text.setTextColor(getResources().getColor(R.color.Red));
				finallist.clear();
				totalPkgCount=0;
				currPageNo=0;
				if(lastClick!=null){
					lastClick.setTextColor(getResources().getColor(R.color.Black));
				}
				
				lastClick = text;
				if(abc.equals("#")){
					flag=true;
					searchText="";
						getAllDiagnoPackageUrl =  getString(R.string.getDiagnoPackageSearch)+getString(R.string.getDiagnoPackagePage)+
					    		getString(R.string.getDiagnoPackageOrderBy)+getString(R.string.getDiagnoPackagePkgId);
						getPackageWebCall(getAllDiagnoPackageUrl);
						currentPosition.setText(abc);
					}else{
						flag=false;
						searchText=abc;
						String searchCriteriaWise =  getString(R.string.getDiagnoPackageSearch)+abc+getString(R.string.getDiagnoPackagePage)+
					    		getString(R.string.getDiagnoPackageOrderBy)+getString(R.string.getDiagnoPackagePkgId);
						getPackageWebCall(searchCriteriaWise);
						currentPosition.setText(abc);
						 }
				
			}
		});
	    //List of package 
	   lvPackages = (ListView)findViewById(R.id.lvPackages);
	   lvPackages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//ImageView imgv = (ImageView)view.findViewById(R.id.select_package_img);
				//imgv.setImageResource(R.drawable.right_red_tick);
			//	Toast.makeText(getApplicationContext(), "selected",	Toast.LENGTH_SHORT).show();
				DiagnoPackageDataBean itemclicked=(DiagnoPackageDataBean) dpadap.getItem(position);
				
				Intent intent = new Intent(getApplicationContext(),DiagnoLabsAvailable.class);
				String packageId=String.valueOf(itemclicked.getPacakageId());
				// packageId="2";//need to change later now as labs are not found for all package value of package id id kept fixed; 
				System.out.println("packageId"+packageId);
				intent.putExtra("pckgId",packageId);
				intent.putExtra("callFromKey","FromPacakge");
				startActivity(intent);
			}
			
	   });
		lvPackages.setAdapter(dpadap);
		searchView=(SearchView)toolBar.findViewById(R.id.menu_search);
		searchView.setOnQueryTextListener(this);
		searchView.setIconifiedByDefault(false);
		searchView.setQueryHint("Search");
		lvPackages.setOnScrollListener(this);
		dpadap= new DiagnoPackageAdapter(getApplicationContext(), finallist);
		lvPackages.setAdapter(dpadap);
	}

	public void getPackageWebCall(String url) {
		// TODO Auto-generated method stub
		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, url, new JSONObject(), this, this);
		if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson))
		{
       		
		//	Toast.makeText(getApplicationContext(), "value searching", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(getApplicationContext(), "value not found", Toast.LENGTH_LONG).show();
		}
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.diagno_package_home, menu);
		MenuItem item = menu.findItem(R.id.action_settings);
		
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
		// TODO Auto-generated method stub
		String err=String.valueOf(error);
		Toast.makeText(getApplicationContext(),"Time-Out", Toast.LENGTH_LONG).show();
		Log.e("JsonErr", err);

	}

	@Override
	public void onResponse(Object response) {
		// TODO Auto-generated method stub
		String Resp = response.toString();
		getPackage(response);
	
	}

	private void getPackage(Object response) {
		// TODO Auto-generated method stub
		
		
		JSONObject jobj = (JSONObject)response;
		JSONObject pck_jobj = new JSONObject();
		JSONObject pckDetailObj=new JSONObject();
		JSONObject pcktestDetailsObj=new JSONObject();
		JSONArray testArrayIPck=new JSONArray();
		JSONArray resultjarr = new JSONArray();
		JSONObject testObj=new JSONObject();
		DiagnoPackageDataBean pckbean = new DiagnoPackageDataBean();
		TestDataBean testBean=new TestDataBean();
		ArrayList<TestDataBean> testList=new ArrayList<TestDataBean>();
		if(onScrollinProg)
			onScrollinProg = false;
		try {
			totalPkgCount = jobj.getInt("packages_count");
			
			String messageString=jobj.getString("message");
			//String messageString="No data found.";
			if(messageString.equals("No data found.")){
				//pckbean.setPackage_name("Package Not Available");
				//finallist.add(pckbean);
				lvPackages.setVisibility(View.INVISIBLE);
				no_items.setVisibility(View.VISIBLE);
				flagNoPckFound=true;//2922016***bhagyashri
			}else{
				resultjarr=jobj.getJSONArray("result");
				int length=resultjarr.length();
				//Gson gson=new Gson();
				//String resultString=gson.toJson(resultjarr);
				
			System.out.println("length of result or number of pckg"+length);
			for(int i=0;i<resultjarr.length();i++){
				pck_jobj=resultjarr.getJSONObject(i);
				pckbean=new DiagnoPackageDataBean();
				pckbean.setPacakageId(Integer.parseInt(pck_jobj.getString("package_id")));
				pckbean.setPackage_name(pck_jobj.getString("package_name"));
				pckbean.setDescription(pck_jobj.getString("package_description"));
				pckbean.setMandatoryInfo(pck_jobj.getString("mandatory_information"));
				
				pckDetailObj=pck_jobj.getJSONObject("package_details");
				pcktestDetailsObj=pckDetailObj.getJSONObject("packageTestDetails");
				
				testArrayIPck=pcktestDetailsObj.getJSONArray("ifAnyTests");
				int numberOfTest=testArrayIPck.length();
				System.out.println("numberOfTest"+numberOfTest);
				testList=new ArrayList<TestDataBean>();
				for(int k=0;k<numberOfTest;k++){
					testObj=testArrayIPck.getJSONObject(k);
				
					testBean=new TestDataBean();					
					testBean.setTestId(Integer.parseInt(testObj.getString("test_id")));
					testBean.setTestname(testObj.getString("test_name"));
					testList.add(testBean);
				}
				pckbean.setTestArrayList(testList);
				finallist.add(pckbean);
			}
			
			
			}
			
			dpadap.notifyDataSetChanged();	
			if(finallist.size() == 0)
			{
				lvPackages.setVisibility(View.INVISIBLE);
				no_items.setVisibility(View.VISIBLE);
			}
			else
			{
				lvPackages.setVisibility(View.VISIBLE);
				no_items.setVisibility(View.GONE);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		Log.d("In on scrol"," executed onscroll");
			//if((lastPage+1) == currPageNo || currPageNo == 0)
		if(!onScrollinProg || currPageNo <3 )
			{			
				int lastItemInScreen=firstVisibleItem+visibleItemCount;
				if((lastItemInScreen==totalItemCount)&&(totalPkgCount!=finallist.size())){
					currPageNo++;
					Log.e("Web service called", currPageNo+" times");
				//	String str = searchURL+getString(R.string.getdiseasePagePart)+(pagecount);
					
					getAllDiagnoPackageUrl =  getString(R.string.getDiagnoPackageSearch)+searchText+getString(R.string.getDiagnoPackagePage)+currPageNo+
				    		getString(R.string.getDiagnoPackageOrderBy)+getString(R.string.getDiagnoPackagePkgId);
					if(!flagNoPckFound){
					WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, getAllDiagnoPackageUrl, new JSONObject(), new Listener<JSONObject>() {			
						@Override
						public void onResponse(JSONObject response) {
							
							// TODO Auto-generated method stub
							getPackage(response);
						//	pagecount++;
							showProgress(false);
						//	loadingInProgress = false;
					//		isOngoingSearch=false;
						}
					}, new ErrorListener() {
				
						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub
							Log.e("Error ", "Some error occured"+error);
							showProgress(false);
						//	loadingInProgress = false;
						//	isOngoingSearch=false;
						}
					});
				/*	if(!loadingInProgress)
					{*/
						if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), webjson))
						{
							
							showProgress(true);						
						//	pagecount++;
						}
						else
						{
							showProgress(false);
						}
					}
			}
			}
		
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		try {
			totalPkgCount=0;
		searchText = URLEncoder.encode(newText, "utf-8");
	//	pagecount=0;
		//tests.clear();
		finallist.clear();	
		currPageNo=0;
	//	lastPage=0;
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
//	searchURL = setSearchURL(searchText)+getString(R.string.getdiseasePagePart)+(pagecount);	
		
		getAllDiagnoPackageUrl = getString(R.string.getDiagnoPackageSearch)+searchText+ getString(R.string.getDiagnoPackagePage)+currPageNo+
				getString(R.string.getDiagnoPackageOrderBy)+getString(R.string.getDiagnoPackagePkgId);
	//	getTestWebCall(getAllDiagnoTest);
		
 //	isOngoingSearch = true;

	WebJsonObjectRequest getPkgJson = new WebJsonObjectRequest(Method.GET, getAllDiagnoPackageUrl, new JSONObject(), new Listener<JSONObject>() {			
		@Override
		public void onResponse(JSONObject response) {
			
			// TODO Auto-generated method stub
			getPackage(response);
		//	pagecount++;
			showProgress(false);
		//	loadingInProgress = false;
	//		isOngoingSearch=false;
		}
	}, new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			// TODO Auto-generated method stub
			Log.e("Error ", "Some error occured"+error);
			showProgress(false);
			//loadingInProgress = false;
		//	isOngoingSearch=false;
		}
	});
	if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), getPkgJson))
	{
	//	loadingInProgress= true;
		showProgress(true);
	}
	else
		showProgress(false);
	
	return true;
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
}
