package com.okler.diagnostics;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.okler.adapters.DiagnoTestAdapter;
import com.okler.adapters.DiseaseAdapter;
import com.okler.android.BaseActivity;
import com.okleruser.R;
import com.okler.android.SearchByMedActivity;

import com.okler.databeans.DiseaseDataBean;
import com.okler.databeans.TestDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;

import android.R.integer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.cardemulation.OffHostApduService;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class DiagnoTestsHome extends BaseActivity implements Response.Listener,Response.ErrorListener, SearchView.OnQueryTextListener,OnScrollListener{

	ArrayList<TestDataBean> tests;
	DiagnoTestAdapter testAdp;
	ListView lvTestsDiseases;
	Toolbar toolBar;
	Button nextBtn;
	Context context;
	ImageView imgBack;
	View bottomBarLayout;
	private int diseaseIdReceived;
	private ListView lv1,testList;
	TextView lastClick,text;
	SearchView searchView;
	private Boolean flagCallfromDiagnoDisease=false;
	LinearLayout progressLinLayout;
	private String getAllDiagnoTest,getAlphabeticallyTest;
	private ArrayList<TestDataBean> arrayListObj=new ArrayList<TestDataBean>();
	ArrayList<TestDataBean> finallist = new ArrayList<TestDataBean>();
	String searchText="",searchURL;
	boolean loadingInProgress=false;
	int pagecount=0,totalTestsCount=0,currPageNo=0,lastPage=0;
	
	
	private String[] alpha = {"#","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagno_tests);
		context = getApplicationContext();
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		final ActionBar ab=getSupportActionBar();
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		//ab.setDisplayHomeAsUpEnabled(true);
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	    imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(imgBack.getWindowToken(), 0);
			}
		});
	    Utilities.setTitleText(toolBar, "Diagnostic Test [1/5]");
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		diseaseIdReceived=getIntent().getIntExtra("DiseaseId",0);
		Okler.getInstance().setBookingType(20);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	              android.R.layout.simple_list_item_1, android.R.id.text1, alpha){
			@Override
			public View getView(int position, View convertView, android.view.ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
	  		        TextView text = (TextView) view.findViewById(android.R.id.text1);
	  		        text.setTextColor(Color.BLACK);
	  		        return view;
	  		   
				}
			};
		

		 lv1 = (ListView)findViewById(R.id.alphabetsScrView) ;
		 lv1.setAdapter(adapter);
		 if(diseaseIdReceived==0){
			 //For diagnotest home 
			getAllDiagnoTest = getString(R.string.getDiagnoTestSearch)+
					getString(R.string.getDiagnoTestPage)+pagecount+
					getString(R.string.getDiagnoTestOrderBy)+
					getString(R.string.getDiagnoTestTestId);
			getTestWebCall(getAllDiagnoTest);
			}else{
				//only when intent received from Diagnostic Disease
				getAllDiagnoTest = getString(R.string.getDiagnoTestforD1)+diseaseIdReceived+getString(R.string.getDiagnoTestforD2)+getString(R.string.getDiagnoTestforD3)+pagecount;
				getTestWebCall(getAllDiagnoTest);
				flagCallfromDiagnoDisease=true;
			}
			lv1.setOnItemClickListener(new OnItemClickListener() {
				@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//ImageView imgView = (ImageView) view.findViewById(R.id.imgSelection);
				//imgView.setImageResource(R.drawable.right_red_tick);
					finallist.clear();
					testAdp.notifyDataSetChanged();
					currPageNo=0;
					String abc = ((TextView)view).getText().toString();
					text = (TextView)view.findViewById(android.R.id.text1);
					text.setTextColor(getResources().getColor(R.color.Red));
					if(lastClick!=null){
						lastClick.setTextColor(getResources().getColor(R.color.Black));
					}
					
					lastClick = text;
					if(abc.equals("#")){
						searchText="";
						if(flagCallfromDiagnoDisease==false){
							getAllDiagnoTest = getString(R.string.getDiagnoTestSearch)+
									getString(R.string.getDiagnoTestPage)+"0"+
									getString(R.string.getDiagnoTestOrderBy)+
									getString(R.string.getDiagnoTestTestId);
						getTestWebCall(getAllDiagnoTest);
						}else{
							//only when intent received from Diagnostic Disease
							getAllDiagnoTest = getString(R.string.getDiagnoTestforD1)+diseaseIdReceived+getString(R.string.getDiagnoTestforD2)+getString(R.string.getDiagnoTestforD3)+"0";
							getTestWebCall(getAllDiagnoTest);
						}
					}else{
						searchText=abc;
						if(flagCallfromDiagnoDisease==false){
							
						getAllDiagnoTest = getString(R.string.getDiagnoTestSearch)+abc+
								getString(R.string.getDiagnoTestPage)+"0"+
								getString(R.string.getDiagnoTestOrderBy)+
								getString(R.string.getDiagnoTestTestId);
						getTestWebCall(getAllDiagnoTest);
						}
						else{
							
							//only when intent received from Diagnostic Disease
							getAllDiagnoTest = getString(R.string.getDiagnoTestforD1)+diseaseIdReceived+getString(R.string.getDiagnoTestforD2)+searchText+getString(R.string.getDiagnoTestforD3)+"0";
						getTestWebCall(getAllDiagnoTest);
						}
					}
			}
			});
			
		
			/*testList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					TestDataBean dbean2; 
					CheckBox chk=(CheckBox) view.findViewById(R.id.checkBox_diagno_test);
					chk.performClick();
					dbean2= (TestDataBean)testAdp.getItem(position);
					if(chk.isSelected()){
						chk.setSelected(false);
						dbean2.setCkIsChecked(true);
						arrayListObj.add(dbean2);
						Okler.getInstance().setListSelectedDiagnoTest(arrayListObj);
					}else{
						dbean2.setCkIsChecked(false);
						//chk.setSelected(true);
						arrayListObj.remove(dbean2);
						Okler.getInstance().setListSelectedDiagnoTest(arrayListObj);
					}
					
					
					/*ImageView imgView = (ImageView) view.findViewById(R.id.imgSelection);
					imgView.setImageResource(R.drawable.right_red_tick);
					
					//int testid =dbean2.getTestId();
					
					
					//Toast.makeText(getApplicationContext(), "id = "+testid, Toast.LENGTH_LONG).show();
					
					
				
				}
			});  */
	
		nextBtn = (Button)findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<TestDataBean> dataBeanobj=Okler.getInstance().getListSelectedDiagnoTest();
				String[] testId=new String[dataBeanobj.size()];
				//need to uncomment this as for all test_Id we were not getting lab details; 
				for(int i=0;i<dataBeanobj.size();i++){
					String a=String.valueOf(dataBeanobj.get(i).getTestId()); 
					testId[i]=a;
				}
				//testId[0]="1";
				//testId[1]="5";
				Intent startDiagnoLabs = new Intent(context, DiagnoLabsAvailable.class);
				startDiagnoLabs.putExtra("testId",testId);
				startDiagnoLabs.putExtra("callFromKey","FromTest");
				startActivity(startDiagnoLabs);
			}
		});
		
		searchView=(SearchView)toolBar.findViewById(R.id.menu_search);
		searchView.setOnQueryTextListener(this);
		searchView.setQueryHint("Search");	
			searchView.setIconifiedByDefault(false);	
		progressLinLayout = (LinearLayout)findViewById(R.id.progressLinLayout);
		testAdp = new DiagnoTestAdapter(getApplicationContext(), finallist);
		testList=(ListView) findViewById(R.id.lvTests);
		testList.setOnScrollListener(this);
		testList.setAdapter(testAdp);
	}
	protected void getTestWebCall(String url) {
		// TODO Auto-generated method stub
		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, url, new JSONObject(), this, this);
		if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson))
		{
       		
			//Toast.makeText(getApplicationContext(), "value searching", Toast.LENGTH_LONG).show();
			
		}
		else
		{
			//Toast.makeText(getApplicationContext(), "value not found", Toast.LENGTH_LONG).show();
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.diagno_tests, menu);
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
	//	Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
		Log.e("JsonErr", err);
		showProgress(false);
		loadingInProgress = false;
	}
	
	@Override
	public void onResponse(Object response) {
		// TODO Auto-generated method stub
		String Resp = response.toString();
		/*Toast.makeText(getApplicationContext(), Resp, Toast.LENGTH_LONG).show();*/
		getTest(response);
		showProgress(false);
		loadingInProgress = false;
	}
	private void getTest(Object response) {
		if(!flagCallfromDiagnoDisease){
		// TODO Auto-generated method stub
		JSONObject jobj = (JSONObject)response;
		JSONObject jobj2 = new JSONObject();
		JSONObject resultObj = new JSONObject();
		TestDataBean testbean = null;
		TextView no_data=(TextView) findViewById(R.id.no_data_found);
		try {
			lastPage = jobj.getInt("cur_page");		
			resultObj = jobj.getJSONObject("result");			
			int len = resultObj.length();
			if(flagCallfromDiagnoDisease == false)
				totalTestsCount = resultObj.getInt("test_count");
			else
				totalTestsCount = jobj.getInt("records_count");
			if(totalTestsCount==0){
				testList.setVisibility(View.GONE);
				no_data.setVisibility(View.VISIBLE);
				no_data.setText("Test not available");
			}else{
				testList.setVisibility(View.VISIBLE);
				no_data.setVisibility(View.GONE);
		int	length=(resultObj.length())-1;
			for (int i = 0; i<length ; i++) {
				jobj2 = resultObj.getJSONObject(i+"");
				testbean = new TestDataBean();
				testbean.setTestId(Integer.parseInt(jobj2.getString("test_id")));
				testbean.setTestname(jobj2.getString("test_name"));
				//testbean.setTestcount(Integer.parseInt(jobj2.getString("count")));				
			//	finallist.add((lastPage*10+i),testbean);
			finallist.add(testbean);
			}
			}
		testAdp.notifyDataSetChanged();
	//		
			pagecount++;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}
		
	}else{
		//when call from disease 
		JSONObject jobj = (JSONObject)response;
		JSONArray resultObj = new JSONArray();
		JSONObject jobj2 = new JSONObject();
		TestDataBean testbean = null;
		TextView no_data=(TextView) findViewById(R.id.no_data_found);
		try {
			lastPage = jobj.getInt("cur_page");
			//resultObj=jobj.getJSONArray("result");
			
			totalTestsCount=jobj.getInt("records_count");
			if(totalTestsCount==0){
				testList.setVisibility(View.GONE);
				no_data.setVisibility(View.VISIBLE);
				no_data.setText("Test not available");
			}else{
				resultObj=jobj.getJSONArray("result");
				int len = resultObj.length();
				testList.setVisibility(View.VISIBLE);
				no_data.setVisibility(View.GONE);
				for(int i=0;i<len;i++){
					jobj2=resultObj.getJSONObject(i);
					testbean = new TestDataBean();
					testbean.setTestId(Integer.parseInt(jobj2.getString("test_id")));
					testbean.setTestname(jobj2.getString("test_name"));
					finallist.add(testbean);
				}
				
			}
			testAdp.notifyDataSetChanged();
			pagecount++;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
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
	/*	if((lastPage+1) == currPageNo || currPageNo == 0)
		{		*/	
			int lastItemInScreen=firstVisibleItem+visibleItemCount;
			if((lastItemInScreen==totalItemCount)&&(totalTestsCount!=finallist.size())){
				currPageNo++;
				Log.e("Web service called", currPageNo+" times");
			//	String str = searchURL+getString(R.string.getdiseasePagePart)+(pagecount);
				if(flagCallfromDiagnoDisease == false)
				searchURL = getString(R.string.getDiagnoTestSearch)+searchText+
						getString(R.string.getDiagnoTestPage)+currPageNo+
						getString(R.string.getDiagnoTestOrderBy)+
						getString(R.string.getDiagnoTestTestId);
				else
					searchURL = getString(R.string.getDiagnoTestforD1)+diseaseIdReceived+getString(R.string.getDiagnoTestforD2)+searchText+getString(R.string.getDiagnoTestforD3)+currPageNo;
				
				
				WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, searchURL, new JSONObject(), new Listener<JSONObject>() {			
					@Override
					public void onResponse(JSONObject response) {
						
						// TODO Auto-generated method stub
						getTest(response);
					//	pagecount++;
						showProgress(false);
						loadingInProgress = false;
				//		isOngoingSearch=false;
					}
				}, new ErrorListener() {
			
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.e("Error ", "Some error occured"+error);
						showProgress(false);
						loadingInProgress = false;
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
		//	}		
	//	}
		
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
			
			searchText = URLEncoder.encode(newText, "utf-8");
			pagecount=0;
			//tests.clear();
			finallist.clear();	
			currPageNo=0;
			lastPage=0;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	searchURL = setSearchURL(searchText)+getString(R.string.getdiseasePagePart)+(pagecount);	
		if(flagCallfromDiagnoDisease==false){			
			searchURL = getString(R.string.getDiagnoTestSearch)+searchText+
					getString(R.string.getDiagnoTestPage)+"0"+
					getString(R.string.getDiagnoTestOrderBy)+
					getString(R.string.getDiagnoTestTestId);
		//	getTestWebCall(getAllDiagnoTest);
			}
			else{
				//only when intent received from Diagnostic Disease
				searchURL = getString(R.string.getDiagnoTestforD1)+diseaseIdReceived+getString(R.string.getDiagnoTestforD2)+searchText;
			//getTestWebCall(getAllDiagnoTestForDisease);
			}
	 //	isOngoingSearch = true;
	
		WebJsonObjectRequest getTestsJson = new WebJsonObjectRequest(Method.GET, searchURL, new JSONObject(), new Listener<JSONObject>() {			
			@Override
			public void onResponse(JSONObject response) {
				
				// TODO Auto-generated method stub
				getTest(response);
			//	pagecount++;
				showProgress(false);
				loadingInProgress = false;
		//		isOngoingSearch=false;
			}
		}, new ErrorListener() {
	
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Log.e("Error ", "Some error occured"+error);
				showProgress(false);
				loadingInProgress = false;
			//	isOngoingSearch=false;
			}
		});
		if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), getTestsJson))
		{
			loadingInProgress= true;
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

