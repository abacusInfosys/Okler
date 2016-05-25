package com.okler.diagnostics;

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
import com.okler.adapters.DiagnoTestAdapter;
import com.okler.adapters.DiseaseAdapter;
import com.okler.android.BaseActivity;
import com.okler.android.Physiotherapy;
import com.okleruser.R;
import com.okler.android.SearchByMedActivity;

import com.okler.databeans.DiseaseDataBean;
import com.okler.databeans.TestDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class DiagnoDiseaseHome extends BaseActivity implements Response.Listener,Response.ErrorListener,SearchView.OnQueryTextListener,OnScrollListener{
	private ArrayList<DiseaseDataBean> dieseases;
	private boolean isAlpha =false,loadingInProgress=false;
 	private String[] alpha = {"#","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private DiseaseAdapter dieseasesAdap;
	private ListView lvDieseases,listAlpha;
	private View bottomBarLayout;
	private Toolbar toolBar;
	private ImageView imgBack;
	private TextView currentPosition,lastClick,text;
	private ArrayAdapter<String> adapter;
	private String getAllDiagnoDiseaseUrl,searchText="";
	private Button notifCount,nxtButton;
	private CheckBox chk;
	SearchView searchView;
	private ArrayList<DiseaseDataBean> arrayListObj=new ArrayList<DiseaseDataBean>();
	private ArrayList<DiseaseDataBean> finallist = new ArrayList<DiseaseDataBean>();
	int pagecount=0,totaldiseaseCount=0,currPageNo=0,lastPage=0;
	LinearLayout progressLinLayout;
	String searchURL;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagno_disease_home);
		toolBar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		ActionBar ab = getSupportActionBar();
		//ab.setDisplayHomeAsUpEnabled(true);
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		Utilities.setTitleText(toolBar, "Diagnostic Test");
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		currentPosition = (TextView)findViewById(R.id.currentPosition_Diagno_dis);
		progressLinLayout = (LinearLayout)findViewById(R.id.progressLinLayout);
		//Back navigation
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
		//webservice call for all disease on default screen
		getAllDiagnoDiseaseUrl = getString(R.string.getDiagnoDisease)+getString(R.string.getDiagnoDisease1)+getString(R.string.getDiagnoDisease2)+"0";
		getDiseaseWebCall(getAllDiagnoDiseaseUrl);
	
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
		listAlpha.setOnItemClickListener(new OnItemClickListener() {
				@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					String abc = ((TextView)view).getText().toString();
				
					text = (TextView)view.findViewById(android.R.id.text1);
					text.setTextColor(getResources().getColor(R.color.Red));
					finallist.clear();
					totaldiseaseCount=0;
					currPageNo=0;
					if(lastClick!=null){
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
		//FIRST Disease List
		lvDieseases =(ListView) findViewById(R.id.lvDiagnoDiseases);
		lvDieseases.setChoiceMode(lvDieseases.CHOICE_MODE_SINGLE);
		dieseasesAdap = new DiseaseAdapter(getApplicationContext(), finallist,true);
		lvDieseases.setAdapter(dieseasesAdap);
		lvDieseases.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				DiseaseDataBean dbean2 = (DiseaseDataBean)dieseasesAdap.getItem(position);
				dbean2.setCkIsChecked(true);
				arrayListObj.add(dbean2);
				Okler.getInstance().setListSelectedDisease(arrayListObj);
				int diseaseId=dbean2.getDiseaseId();
				Intent intent = new Intent(getApplicationContext(),DiagnoTestsHome.class);	
				intent.putExtra("DiseaseId",diseaseId);
				startActivity(intent);
		
				/*int did = dbean2.getDiseaseId();
				Toast.makeText(getApplicationContext(), "id = "+did, Toast.LENGTH_LONG).show();
				
				Intent intent = new Intent(getApplicationContext(),DiagnoTestsHome.class);	
				//intent.putExtra("DiseaseId", did);
				startActivity(intent);
*/
				// TODO Auto-generated method stub
				//int positionchk=(int) buttonView.getTag();
				/*chk=(CheckBox) view.findViewById(R.id.checkBox_diagno_test);
				chk.performClick();
				DiseaseDataBean dbean2 = (DiseaseDataBean)dieseasesAdap.getItem(position);
				//System.out.println("positionchk"+positionchk);
				if(chk.isChecked()){
				dbean2.setCkIsChecked(true);
				arrayListObj.add(dbean2);
				Okler.getInstance().setListSelectedDisease(arrayListObj);
				}else{
					int diseaseId=dbean2.getDiseaseId();
					for(int i=0;i<arrayListObj.size();i++){
						if(arrayListObj.get(i).getDiseaseId()==diseaseId){
							arrayListObj.remove(i);
							break;
						}
					}
					dbean2.setCkIsChecked(false);
					Okler.getInstance().setListSelectedDisease(arrayListObj);
				}
		*/	
			
			}
		});
		
		
	/*
		nxtButton=(Button) findViewById(R.id.nextButn);
		nxtButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<DiseaseDataBean> selectedDisease;
				selectedDisease=Okler.getInstance().getListSelectedDisease();
				int diseaseId=selectedDisease.get(0).getDiseaseId();
				Intent intent = new Intent(getApplicationContext(),DiagnoTestsHome.class);	
				intent.putExtra("DiseaseId",diseaseId);
				startActivity(intent);
		
			}
		});
		
		*/
		lvDieseases.setOnScrollListener(this);
	}
		private void getDiseaseWebCall(String url) {
	
		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, url, new JSONObject(), this, this);
		if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson))
		{
       		showProgress(true);
		//	Toast.makeText(getApplicationContext(), "value searching", Toast.LENGTH_LONG).show();
		}
		else
		{
			showProgress(false);
		//	Toast.makeText(getApplicationContext(), "value not found", Toast.LENGTH_LONG).show();
		}
	}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	/*		getMenuInflater().inflate(R.menu.search_by_diseases, menu);
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
					Intent i = new Intent(getApplicationContext(), Physiotherapy.class);
					startActivity(i);
				}
			});
		    notifCount.setText(String.valueOf(55));*/
		return true;
	}
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			
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
			String err=String.valueOf(error);
			Toast.makeText(getApplicationContext(),"Time-Out", Toast.LENGTH_LONG).show();
			Log.e("JsonErr", err);
			showProgress(false);
	}

		@Override
		public void onResponse(Object response) {
			String Resp = response.toString();
			getDisease(response);
			showProgress(false);
			}
		private void getDisease(Object response) {
			JSONObject jobj = (JSONObject)response;
			JSONObject jobj2 = new JSONObject();
			JSONArray jarr = new JSONArray();
			DiseaseDataBean diseasebean = null;
			try {
				totaldiseaseCount = jobj.getInt("disease_mapping_count");
					if(jobj.has("message")){
					String msg=jobj.getString("message");
					if(msg.equals("No data found.")){
						diseasebean = new DiseaseDataBean();
						String emptyString="No data found.";
						diseasebean.setDiseaseName(emptyString);
						finallist.add(diseasebean);
				
						}
					}else{
				jarr = jobj.getJSONArray("result");
				int len = jarr.length();
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
				}
				}
				
dieseasesAdap.notifyDataSetChanged();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			Log.d("In on scrol"," executed onscroll");
		/*	if((lastPage+1) == currPageNo || currPageNo == 0)
			{		*/	
				int lastItemInScreen=firstVisibleItem+visibleItemCount;
				if((lastItemInScreen==totalItemCount)&&(totaldiseaseCount!=finallist.size())){
					currPageNo++;
					Log.e("Web service called", currPageNo+" times");
				//	String str = searchURL+getString(R.string.getdiseasePagePart)+(pagecount);
					
					getAllDiagnoDiseaseUrl = getString(R.string.getDiagnoDisease)+getString(R.string.getDiagnoDisease1)+searchText+getString(R.string.getDiagnoDisease2)+currPageNo;
					
					WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, getAllDiagnoDiseaseUrl, new JSONObject(), new Listener<JSONObject>() {			
						@Override
						public void onResponse(JSONObject response) {
							
							// TODO Auto-generated method stub
							getDisease(response);
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
			//	pagecount=0;
				//tests.clear();
				finallist.clear();	
				currPageNo=0;
				lastPage=0;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//	searchURL = setSearchURL(searchText)+getString(R.string.getdiseasePagePart)+(pagecount);	
				
	getAllDiagnoDiseaseUrl = getString(R.string.getDiagnoDisease)+getString(R.string.getDiagnoDisease1)+searchText+getString(R.string.getDiagnoDisease2)+currPageNo;
			//	getTestWebCall(getAllDiagnoTest);
				
		 //	isOngoingSearch = true;
		
			WebJsonObjectRequest getTestsJson = new WebJsonObjectRequest(Method.GET, getAllDiagnoDiseaseUrl, new JSONObject(), new Listener<JSONObject>() {			
				@Override
				public void onResponse(JSONObject response) {
					
					// TODO Auto-generated method stub
					getDisease(response);
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

