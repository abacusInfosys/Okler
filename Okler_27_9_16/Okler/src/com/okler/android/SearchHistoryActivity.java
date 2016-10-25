package com.okler.android;

import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.okler.databeans.UsersDataBean;
import com.okler.diagno.DiagnoDiseaseHome;
import com.okler.diagno.DiagnoTestPkgHome;
import com.okler.dialogs.ClearHistoryDialog;
import com.okler.interfaces.ClearHistoryCallback;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class SearchHistoryActivity extends BaseActivity implements ClearHistoryCallback {
	
	Toolbar toolBar;
	Activity ack;
	ListView historyList;
	ArrayList<String> listHistory = new ArrayList<String>();
	String serverUrl,getSearchPart1,term_type,getAllHistoryUrl,message="";
	int userId,term_type_value;
	UsersDataBean ubean;
	ArrayAdapter<String> adapter;
	TextView clean_history;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_history);
		ack = this;
		toolBar = (Toolbar)findViewById(R.id.toolbar);
		term_type_value=getIntent().getIntExtra("term_type_value", 0);
		historyList = (ListView)findViewById(R.id.historyList);
		serverUrl= getString(R.string.serverUrl);
		getSearchPart1 = getString(R.string.getSearchHistoryPart1);
		term_type = getString(R.string.term_type);
		clean_history = (TextView)findViewById(R.id.clean_history);
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		userId = ubean.getId();
		
		UIUtils.setBackClick(toolBar, ack);
		Utilities.setTitleText(toolBar, "Search History");
		toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler.getInstance().getBookingType()));
		getAllHistory();
		
		clean_history.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cleanHistory();
			}
		});
	}

	public void getAllHistory(){
		
		getAllHistoryUrl=serverUrl+getSearchPart1+userId+term_type+term_type_value;
		
		WebJsonObjectRequest getHisJson = new WebJsonObjectRequest(Method.GET, getAllHistoryUrl, new JSONObject(), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				JSONObject jobj1 = new JSONObject();
				JSONArray jarr1 = new JSONArray();
				message=response.optString("message");
				if(!(message.equals("No data found."))){
					jarr1 = response.optJSONArray("result");
					 listHistory = new ArrayList<String>();
				for (int i = 0; i < jarr1.length(); i++) {
					jobj1 = new JSONObject();
					jobj1 = jarr1.optJSONObject(i);
					listHistory.add(jobj1.optString("term"));
				}
				listHistory.trimToSize();
					setUi();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("ERROR", String.valueOf(error));
				Log.e("ERROR", error.getStackTrace().toString());
			}
		});
		VolleyRequest.addJsonObjectRequest(ack, getHisJson);
	}
	
	public void cleanHistory(){
		ClearHistoryDialog cdlg = new ClearHistoryDialog(ack,term_type_value);
		cdlg.show(getFragmentManager(), "");
	}
	public void setUi(){
		adapter = new ArrayAdapter<String>(ack,
				android.R.layout.simple_list_item_1, android.R.id.text1, listHistory) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text = (TextView) view
						.findViewById(android.R.id.text1);
				text.setTextColor(Color.BLACK);
				return view;
			}
		};
		
		historyList.setAdapter(adapter);
		
		historyList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String term = listHistory.get(position);
				Intent intent =null;
				
				if(term_type_value==1||term_type_value==2){
					intent = new Intent(SearchHistoryActivity.this,DiagnoTestPkgHome.class);
					intent.putExtra("DiseaseId", getIntent().getIntExtra("DiseaseId", 0));
					intent.putExtra("isTestsPkgsByLabs", getIntent().getBooleanExtra("isTestsPkgsByLabs", false));
					intent.putExtra("isFromSummary", getIntent().getBooleanExtra("isFromSummary", false));
				}
				if((term_type_value==3)||(term_type_value==4)||(term_type_value==5)){
					intent = new Intent(SearchHistoryActivity.this,GroupByMedsActivity.class);
					intent.putExtra("bookingType", getIntent().getStringExtra("bookingType"));
					intent.putExtra("FromWhere", getIntent().getStringExtra("FromWhere"));
					intent.putExtra("DiseaseId", getIntent().getIntExtra("DiseaseId", 0));
				}
				
				if(term_type_value==6){
					intent = new Intent(SearchHistoryActivity.this,HealthShopGrid.class);
					intent.putExtra("currentView", getIntent().getStringExtra("currentView"));
				}
				if(term_type_value==8){
					intent = new Intent(SearchHistoryActivity.this,DiagnoDiseaseHome.class);
				}
				intent.putExtra("searchText", term);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				
				
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
	public void disableClearHistory() {
		// TODO Auto-generated method stub
		clean_history.setEnabled(false);
	}
}
