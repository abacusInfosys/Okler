package com.okler.diagno;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.okler.adapters.DiagnoDiseaseAdapter;
import com.okler.android.BaseActivity;
import com.okleruser.R;
import com.okler.android.SearchHistoryActivity;
import com.okler.databeans.DiseaseDataBean;
import com.okler.enums.DiagnoOrderType;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class DiagnoDiseaseHome extends BaseActivity implements Response.Listener<JSONObject>,Response.ErrorListener,SearchView.OnQueryTextListener
{
	private boolean isOnScrollProgress=false,isSearching=false,isSearchClicked=false,isFirstWebCall=false;
 	private String[] alpha = {"#","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private ListView listAlpha;
	private View bottomBarLayout;
	private Toolbar toolBar;
	private TextView currentPosition,lastClick,text,search_history;
	private ArrayAdapter<String> adapter;
	private String getAllDiagnoDiseaseUrl,searchText="",queryText="";
	SearchView searchView;
	private ArrayList<DiseaseDataBean> finallist = new ArrayList<DiseaseDataBean>();
	int pagecount=0,totaldiseaseCount=0,currPageNo=0,lastPage=0;
	LinearLayout progressLinLayout;
	String searchURL;
	TextView no_items;
	ImageView overflowIcon;
	RelativeLayout back_layout;
	Activity ack;
	int diseaseId,lastChecked;
	Button nextButn;
	DiagnoDiseaseAdapter disAdap;
	RecyclerView lvTests2;
	LinearLayoutManager linearManager;
	int lastItemInScreen,visibleItemCount,totalItemCount;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagno_disease_home);
		toolBar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		ack = this;
		search_history = (TextView)findViewById(R.id.search_history);
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		no_items = (TextView) findViewById(R.id.no_items);
		Utilities.setTitleText(toolBar, "Diagnostic Test");
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		currentPosition = (TextView)findViewById(R.id.currentPosition_Diagno_dis);
		progressLinLayout = (LinearLayout)findViewById(R.id.progressLinLayout);
		overflowIcon = (ImageView) findViewById(R.id.overflowIcon);
		overflowIcon.setVisibility(View.INVISIBLE);
		nextButn = (Button)findViewById(R.id.nextButn);
		lvTests2 = (RecyclerView)findViewById(R.id.lvTests2);
		disAdap = new DiagnoDiseaseAdapter(ack, finallist, lvTests2);
		linearManager = new LinearLayoutManager(ack);
		lvTests2.setLayoutManager(linearManager);
		lvTests2.setHasFixedSize(true);
		lvTests2.setAdapter(disAdap);
		UIUtils.setBackClick(toolBar, ack);
		
		lvTests2.addOnScrollListener(new RecyclerView.OnScrollListener() {
			
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				// TODO Auto-generated method stub
				super.onScrolled(recyclerView, dx, dy);
				visibleItemCount = recyclerView.getChildCount();
				totalItemCount = linearManager.getItemCount();
				lastItemInScreen  = linearManager.findLastVisibleItemPosition()+1;
				
				if(isOnScrollProgress)
					return;
				else{
					if ((lastItemInScreen == totalItemCount) && (totaldiseaseCount != finallist.size())){
						isOnScrollProgress=true;
						currPageNo++;
						Log.e("Web service called", currPageNo+" times scroll");
						getAllDiagnoDiseaseUrl = getString(R.string.getDiagnoDisease)+getString(R.string.getDiagnoDisease1)+searchText+getString(R.string.getDiagnoDisease2)+currPageNo;
						getDiseaseWebCall(getAllDiagnoDiseaseUrl);
					}
				}
			}
		});
	
		//Adapter for alphabetical List
		adapter = new ArrayAdapter<String>(this,
	              android.R.layout.simple_list_item_1, android.R.id.text1, alpha){
			public View getView(int position, View convertView, android.view.ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
  		        TextView text = (TextView) view.findViewById(android.R.id.text1);
  		        text.setTextColor(Color.BLACK);
  		        return view;
  		   
			}	
			};
		
		listAlpha= (ListView)findViewById(R.id.alphabetsScrView) ;
		listAlpha.setAdapter(adapter);
		setAlphaScrollListener();
		listAlpha.setOnItemClickListener(new OnItemClickListener() {
				@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					String abc = ((TextView)view).getText().toString();
					isSearchClicked=false;
					searchView.setQuery("", false);
					text = (TextView)view.findViewById(android.R.id.text1);
					text.setTextColor(getResources().getColor(R.color.Red));
					finallist.clear();
					totaldiseaseCount=0;
					currPageNo=0;
					isFirstWebCall=true;
					if(lastClick!=null){
						if(lastClick!=text)
						lastClick.setTextColor(getResources().getColor(R.color.Black));
					}
					
					lastClick = text;
						if(abc.equals("#")){
							searchText="";
							getAllDiagnoDiseaseUrl = getString(R.string.getDiagnoDisease)+getString(R.string.getDiagnoDisease1)+getString(R.string.getDiagnoDisease2)+"0";
							getDiseaseWebCall(getAllDiagnoDiseaseUrl);
							currentPosition.setText(abc);
						}else{
							searchText=abc;
							getAllDiagnoDiseaseUrl = getString(R.string.getDiagnoDisease)+getString(R.string.getDiagnoDisease1)+abc+getString(R.string.getDiagnoDisease2)+"0";
								getDiseaseWebCall(getAllDiagnoDiseaseUrl);
								currentPosition.setText(abc);
							 }					
				}
			});
		searchView=(SearchView)toolBar.findViewById(R.id.menu_search);
		searchView.setOnQueryTextListener(this);
		searchView.setQueryHint("Search");
		setUpSearchView();

		nextButn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Okler.getInstance().getDiagnoOrder().getDiseaseId()!=0){
					Intent intent = new Intent(getApplicationContext(),DiagnoTestPkgHome.class);	
					Okler.getInstance().getDiagnoOrder().setOrderType(DiagnoOrderType.TEST);
					intent.putExtra("DiseaseId",diseaseId);
					startActivity(intent);
				}else{
					Toast.makeText(ack, "Please select atleast one disease.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		if (Utilities.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN
				|| (Utilities
						.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_FB)
				|| (Utilities
						.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
			search_history.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DiagnoDiseaseHome.this,SearchHistoryActivity.class);
				intent.putExtra("term_type_value", 8);
				startActivity(intent);
				
			}
		});
		}else{
			search_history.setVisibility(View.INVISIBLE);
		}
	
		//lvDieseases.setOnScrollListener(this);
		
		
	}
	
	private void setUpSearchView() {
		// TODO Auto-generated method stub
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint("Search Diseases");		
		
	}
	
		private void getDiseaseWebCall(String url) {
			Log.e("Web service called", currPageNo+" times fun");
		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, url, new JSONObject(), this, this);
		if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson))
		{
       		showProgress(true);

		}
		else
		{
			showProgress(false);

		}
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
			String err=String.valueOf(error);
			Toast.makeText(getApplicationContext(),"Time-Out", Toast.LENGTH_LONG).show();
			Log.e("JsonErr", err);
			showProgress(false);
	}

		@Override
		public void onResponse(JSONObject response) {
			getDisease(response);
			showProgress(false);
			}
		private void getDisease(Object response) {
			JSONObject jobj = (JSONObject)response;
			JSONObject jobj2 = new JSONObject();
			JSONArray jarr = new JSONArray();
			DiseaseDataBean diseasebean = null;
			if(isOnScrollProgress){
				isOnScrollProgress=false;
			}
			if(isSearching){
				isSearching=false;
			}
			try {
				totaldiseaseCount = jobj.getInt("disease_mapping_count");
					if(jobj.has("message")){
					String msg=jobj.getString("message");
					if(msg.equals("No data found.")){
						lvTests2.setVisibility(View.INVISIBLE);
						no_items.setVisibility(View.VISIBLE);
						}
					}else{
				jarr = jobj.getJSONArray("result");
				for (int i = 0; i < jarr.length(); i++) {
					jobj2 = jarr.getJSONObject(i);
					diseasebean = new DiseaseDataBean();
					diseasebean.setDiseaseId(Integer.parseInt(jobj2.getString("id")));
					String name=jobj2.getString("name");
					if(name.equals("")){
						String emptyString="disease not available";
						diseasebean.setDiseaseName(emptyString);
					}else{
						diseasebean.setDiseaseName(name);
					}
					finallist.add(diseasebean);
					if(!isFirstWebCall)
						disAdap.notifyItemInserted(finallist.size());
				}
				}
				if(isFirstWebCall){
					isFirstWebCall=false;
					disAdap.notifyDataSetChanged();
				}
					if(finallist.size() == 0)
					{
						lvTests2.setVisibility(View.INVISIBLE);
						no_items.setVisibility(View.VISIBLE);
					}
					else
					{
						lvTests2.setVisibility(View.VISIBLE);
						no_items.setVisibility(View.GONE);
					}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}

		@Override
		public boolean onQueryTextSubmit(String query) {
			// TODO Auto-generated method stub
			 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
			    try {
					searchText = URLEncoder.encode(query, "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    finallist.clear();	
				currPageNo=0;
				lastPage=0;
				totaldiseaseCount=0;
				isSearching=true;
				isFirstWebCall=true;
				getAllDiagnoDiseaseUrl = getString(R.string.getDiagnoDisease)+getString(R.string.getDiagnoDisease1)+searchText+getString(R.string.getDiagnoDisease2)+currPageNo;
				isSearchClicked=true;
				if (Utilities.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN
						|| (Utilities
								.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_FB)
						|| (Utilities
								.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
				UIUtils.addToSearchHistory(searchText, ack, 8);
				}
				getDiseaseWebCall(getAllDiagnoDiseaseUrl);
			return false;
		}
		@Override
		public boolean onQueryTextChange(String newText) {
			
			
			if(newText.length()==0 && isSearchClicked){
				currentPosition.setText("#");
				searchText="";
				finallist.clear();	
				currPageNo=0;
				lastPage=0;
				totaldiseaseCount=0;
				isSearching=true;
				isFirstWebCall=true;
				Log.e("Web service called", currPageNo+" times search "+searchText);
				getAllDiagnoDiseaseUrl = getString(R.string.getDiagnoDisease)+getString(R.string.getDiagnoDisease1)+searchText+getString(R.string.getDiagnoDisease2)+currPageNo;
				isSearchClicked=false;
				getDiseaseWebCall(getAllDiagnoDiseaseUrl);
			}

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
		 
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		finallist.clear();
		disAdap.notifyDataSetChanged();
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
		currPageNo=0;
		totaldiseaseCount=0;
		getAllDiagnoDiseaseUrl = getString(R.string.getDiagnoDisease)+getString(R.string.getDiagnoDisease1)+searchText+getString(R.string.getDiagnoDisease2)+"0";
		isFirstWebCall=true;
		getDiseaseWebCall(getAllDiagnoDiseaseUrl);
		
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
		 listAlpha.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					
					String current = currentPosition.getText().toString();
					for (int i = 0; i < listAlpha.getChildCount(); i++) {
						TextView tv = (TextView)listAlpha.getChildAt(i);
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

