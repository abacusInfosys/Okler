package com.okler.android;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import com.okler.network.VolleyRequest;
import com.okler.adapters.DiseaseAdapter;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.DiseaseDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchByDiseasesActivity extends BaseActivity implements Response.Listener,Response.ErrorListener,SearchView.OnQueryTextListener,OnScrollListener{
	
	boolean isAlpha =false;
 	String[] alpha = {"#","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	String currURL;
	DiseaseAdapter dieseasesAdap;//,dieseasesAdap2,searchAdp;
	ArrayList<DiseaseDataBean> dieseases = new ArrayList<DiseaseDataBean>();
	ListView lvDieseases;
	View bottomBarLayout;
	Toolbar toolBar;
	Button notifCount;
	ImageView imgBack;
	TextView currentPosition, text, lastClick;
	String getAllDiseasesAllo,getAllDiseasesHome,getAllDiseasesAyur;
	SearchView searchView;
	Button btnCart;
	LinearLayout progressLinLayout;
	int currBookingType,searchPageCount=1,pageCount=1,totaldiseasesCount=-1,totaldiseasesSearchCount=-1;
	boolean isOngoingSearch;
	String searchURL,normalURL;
	boolean is1Called=false;
	TextView no_items;
	ImageView overflowIcon;
	int curPageNum = 0;
	Activity act;
	boolean onScrollinProg = false;
	String searchTxt="";
	String abc="#";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_by_diseases);
		toolBar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		no_items = (TextView) findViewById(R.id.no_items);
		ActionBar ab = getSupportActionBar();
		progressLinLayout = (LinearLayout)findViewById(R.id.progressLinLayout);
		overflowIcon = (ImageView) findViewById(R.id.overflowIcon);
		overflowIcon.setVisibility(View.INVISIBLE);
		lvDieseases =(ListView) findViewById(R.id.lvDiseases);
		dieseases = new ArrayList<DiseaseDataBean>();
		dieseasesAdap = new DiseaseAdapter(getApplicationContext(), dieseases,false);
	    lvDieseases.setAdapter(dieseasesAdap);
	    act = this;
		//ab.setDisplayHomeAsUpEnabled(true);
		currBookingType =Okler.getInstance().getBookingType(); 
		if(currBookingType==0){
			//ab.setTitle(R.string.title_activity_allopathy);
			Utilities.setTitleText(toolBar, getString(R.string.allopathy));
			getAllDiseasesAllo = getString(R.string.getDiseasesUrlAllo);
			normalURL = getAllDiseasesAllo;
			callWebService(getAllDiseasesAllo);
		}else if(currBookingType==3){
			//ab.setTitle(R.string.title_activity_ayurvedic);
			Utilities.setTitleText(toolBar, getString(R.string.ayurvedic));
			getAllDiseasesAyur = getString(R.string.getDiseasesUrlAyur);
			normalURL = getAllDiseasesAyur;
			callWebService(getAllDiseasesAyur);
		}else{
			//ab.setTitle(R.string.title_activity_homeopathy);
			
			Utilities.setTitleText(toolBar, getString(R.string.homeopathy));
			getAllDiseasesHome = getString(R.string.getDiseasesUrlHome);
			normalURL = getAllDiseasesHome;
			callWebService(getAllDiseasesHome);
		}
		
		currentPosition = (TextView)findViewById(R.id.currentPosition);
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
		searchView=(SearchView)toolBar.findViewById(R.id.menu_search);
		btnCart = (Button)toolBar.findViewById(R.id.toolBarCount);	    
		toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler.getInstance().getBookingType()));
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		setUpSearchView();
		
		
//		searchDiseases = new ArrayList<DiseaseDataBean>();

	
		/*for(int i = 0; i < 1; i++)
		{
			DiseaseDataBean dObj = new DiseaseDataBean();
			dObj.setDiseaseId(i);
			dObj.setDiseaseName("Disease Name "+i);
			dieseases.add(dObj);
		}*/
		//dieseasesAdap = new DiseaseAdapter(getApplicationContext(), dieseases);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	              android.R.layout.simple_list_item_1, android.R.id.text1, alpha);

			ListView lv1 = (ListView)findViewById(R.id.alphabetsScrView) ;
			lv1.setAdapter(adapter);
			lv1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					isAlpha=true;
					dieseases.clear();
					dieseasesAdap.notifyDataSetChanged();
					abc = ((TextView)view).getText().toString();
					
					text = (TextView)view.findViewById(android.R.id.text1);
					text.setTextColor(getResources().getColor(R.color.Red));
					if(lastClick!=null){
						lastClick.setTextColor(getResources().getColor(R.color.Black));
					}
					lastClick = text;
					currentPosition.setText(abc);
					//dieseases.clear();
					//dieseasesAdap.notifyDataSetChanged();
					//searchDiseases.clear();
					is1Called=false;
					searchPageCount=0;
					pageCount=0;
					
				if(abc.equals("#")){					
					if(Okler.getInstance().getBookingType()==0){
						dieseases.clear();
						dieseasesAdap.notifyDataSetChanged();
						totaldiseasesCount=0;
						pageCount=0;
						getAllDiseasesAllo = getString(R.string.getDiseasesUrlAllo);
						searchTxt="";
						callWebService(getAllDiseasesAllo);
						pageCount++;
						
						
					}else if(Okler.getInstance().getBookingType()==3){
						totaldiseasesCount=0;
						pageCount=0;
						getAllDiseasesAyur = getString(R.string.getDiseasesUrlAyur);
						searchTxt="";
						callWebService(getAllDiseasesAyur);
						pageCount++;
						
					}else{
						totaldiseasesCount=0;
						pageCount=0;
						getAllDiseasesHome = getString(R.string.getDiseasesUrlHome);
						searchTxt="";
						callWebService(getAllDiseasesHome);
						pageCount++;
						
					}
				}
				else{
					if(Okler.getInstance().getBookingType()==0){
						String getAllDiseasesAllo1 = getString(R.string.getDiseasesUrlAllo);
						String part2 = getString(R.string.getMedsUrlStartWith2);
						getAllDiseasesAllo = getAllDiseasesAllo1+part2+abc;
						totaldiseasesCount=0;
						pageCount=0;
						searchTxt=abc;
						callWebService(getAllDiseasesAllo);
						pageCount++;
					}else if(Okler.getInstance().getBookingType()==3){
						String getAllDiseasesAyur1 = getString(R.string.getDiseasesUrlAyur);
						String part2 = getString(R.string.getMedsUrlStartWith2);
						getAllDiseasesAyur = getAllDiseasesAyur1+part2+abc;
						totaldiseasesCount=0;
						pageCount=0;
						searchTxt=abc;
						callWebService(getAllDiseasesAyur);
						pageCount++;
					}else{
						String getAllDiseasesHome1 = getString(R.string.getDiseasesUrlHome);
						String part2 = getString(R.string.getMedsUrlStartWith2);
						getAllDiseasesHome = getAllDiseasesHome1+part2+abc;
						totaldiseasesCount=0;
						pageCount=0;
						searchTxt=abc;
						callWebService(getAllDiseasesHome);
						pageCount++;
					}				
					
				  }
				}
			});		
		
	
		lvDieseases.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				DiseaseDataBean dbean2 = (DiseaseDataBean)dieseasesAdap.getItem(position);
				int did = dbean2.getDiseaseId();
		//		Toast.makeText(getApplicationContext(), "id = "+did, Toast.LENGTH_LONG).show();
				
				Intent intent = new Intent(getApplicationContext(),SearchByMed1Activity.class);	
				intent.putExtra("DiseaseId", did);
				intent.putExtra("FromWhere", "searchByDiseases");
				startActivity(intent);
			
			}
		});
		lvDieseases.setOnScrollListener(this);
		//lvDieseases.setAdapter(dieseasesAdap);
	}
	private void setUpSearchView() {
		// TODO Auto-generated method stub
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint("Search");		
	}
	private void callWebService(String getAllDiseasesStr) {
	    String str = getAllDiseasesStr+getString(R.string.getdiseasePagePart)+"0";
		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, str, new JSONObject(), this, this);
		//lvDieseases.setOnScrollListener(this);
		if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson))
		{
			showProgress(true);
		}else
		{
			showProgress(false);
			
		}
			
				
	}

/*	private void codeForAyur() {
		normalURL = getAllDiseasesAyur+getString(R.string.getdiseasePagePart)+"0";
		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, normalURL, new JSONObject(), this, this);
		if (VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson))
		{
			showProgress(true);
		}else
		{
			showProgress(false);
			
		}
		
		
	}

	private void codeForAllo() {
		normalURL = getAllDiseasesAllo+getString(R.string.getdiseasePagePart)+"0";
		WebJsonObjectRequest wjson = new WebJsonObjectRequest(Method.GET, normalURL, new JSONObject(), this, this);
		if (VolleyRequest.addJsonObjectRequest(getApplicationContext(), wjson))
		{
			showProgress(true);
		}else
		{
			showProgress(false);
			
		}
		
	}*/

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(btnCart,act);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.search_by_diseases, menu);
		/*MenuItem item = menu.findItem(R.id.action_settings);
		item.setActionView(R.layout.feed_update_count);
	    //
		 View count = item.getActionView();
		    notifCount = (Button) count.findViewById(R.id.notif_count);
		    notifCount.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "btn clicked", Toast.LENGTH_LONG).show();
					Intent i = new Intent(getApplicationContext(), MyCart.class);
					startActivity(i);
				}
			});*/
		    /*CartDataBean cbean = Okler.getInstance().getMainCart();
			   ArrayList<ProductDataBean>pdList = cbean.getProdList();
			   // pdList = odbean.getProdList();
			   int maincount = pdList.size(); 
			   
			   if(maincount>9){
				   btnCart.setText(""+maincount);
			    }
			    else{
			    	btnCart.setText("0"+maincount);
			    }
			   btnCart.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(getApplicationContext(), MyCart.class);
						startActivity(i);
					}
				});*/
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
		String err = String.valueOf(error);
		
		showProgress(false);
	//	Toast.makeText(getBaseContext(), "Error in connecting to Server", Toast.LENGTH_LONG).show();
	//	lvDieseases.setOnScrollListener(this);
		Log.e("JsonErr", err);
	}

	@Override
	public void onResponse(Object response) {
		//if(isAlpha){
		//isAlpha=false;
	//	dieseases.clear();
		//dieseasesAdap.notifyDataSetChanged();
		//}
		
		processResponse(response);		
	}

	private void processResponse(Object response)
	{
		String Resp = response.toString();
		//Toast.makeText(getApplicationContext(), Resp, Toast.LENGTH_LONG).show();
		JSONObject jobj = (JSONObject)response;
	
		JSONObject jobj2 = new JSONObject();
		JSONArray jarr = new JSONArray();
		//int len = jarr.length();
		//Toast.makeText(getApplicationContext(), "JArr len= "+len, Toast.LENGTH_LONG).show();
		DiseaseDataBean dbean = null;
		showProgress(false);
		if(onScrollinProg)
			onScrollinProg = false;
		try {
			int totalcnt = jobj.getInt("disease_mapping_count");
			jarr = jobj.getJSONArray("result");
			int len = jarr.length();
			curPageNum = jobj.getInt("cur_page");
			pageCount=curPageNum+1;
		/*	if(len == 0)
			{
				no_items.setVisibility(View.VISIBLE);
			}
			else
			{
				no_items.setVisibility(View.GONE);*/
				
				ArrayList<DiseaseDataBean> finallist = new ArrayList<DiseaseDataBean>();
				/*			Toast.makeText(getApplicationContext(), "JArr len= "+len, Toast.LENGTH_LONG).show();
							jobj2 = jarr.getJSONObject(1);
							String Resp2 = jobj2.toString();
							Toast.makeText(getApplicationContext(), Resp2, Toast.LENGTH_LONG).show();
							//Toast.makeText(getApplicationContext(), "JArr content= "+jarr, Toast.LENGTH_LONG).show();*/
							for (int i = 0; i < jarr.length(); i++) {
								jobj2 = jarr.getJSONObject(i);
								dbean = new DiseaseDataBean();
								dbean.setDiseaseId(Integer.parseInt(jobj2.getString("id")));
								dbean.setDiseaseName(jobj2.getString("name"));
								dbean.setCount(Integer.parseInt(jobj2.getString("count")));
								finallist.add(dbean);
							}
							/*if(isOngoingSearch)
							{
								searchDiseases.addAll(finallist);
								searchAdp.notifyDataSetChanged();
								if(finallist.size() == 0)
									no_items.setVisibility(View.VISIBLE);
								else
									no_items.setVisibility(View.GONE);
							//	searchAdp = new DiseaseAdapter(getApplicationContext(),searchDiseases ,false);
								//lvDieseases.setAdapter(searchAdp);
								totaldiseasesSearchCount = totalcnt;
							//	searchAdp.notifyDataSetChanged();
							}
							else 
							{*/	
							if(!(abc.equals("#"))){
								isAlpha=false;
								dieseases.clear();
								dieseasesAdap.notifyDataSetChanged();
							}
								
							dieseases.addAll(finallist);
							    totaldiseasesCount = totalcnt;
							    dieseasesAdap.notifyDataSetChanged();
								if(dieseases.size() == 0)
								{
									lvDieseases.setVisibility(View.INVISIBLE);
									no_items.setVisibility(View.VISIBLE);
								}
								else
								{
									lvDieseases.setVisibility(View.VISIBLE);
									no_items.setVisibility(View.GONE);
								}
							  //  dieseasesAdap = new DiseaseAdapter(getApplicationContext(),dieseases ,false);
								//lvDieseases.setAdapter(dieseasesAdap);
							//	lvDieseases.setOnScrollListener(this);
							//}
			//}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
		return false;
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
	public boolean onQueryTextChange(String searchText) {
		
		try {
			
			searchText = URLEncoder.encode(searchText, "utf-8");
			searchTxt = searchText;
		/*	searchDiseases.clear();
			is1Called=false;
			searchPageCount=0;*/
			pageCount=0;
			dieseases.clear();
			dieseasesAdap.notifyDataSetChanged();
			totaldiseasesCount=0;
			/*if(TextUtils.isEmpty(searchText)){
				System.out.println("text is empty");
				isOngoingSearch=false;	
				
		/*		dieseasesAdap = new DiseaseAdapter(getApplicationContext(),dieseases ,false);
				lvDieseases.setAdapter(dieseasesAdap);
				pageCount=1;*/
		/*	}
			else
			{
				isOngoingSearch=true;
				searchAdp = new DiseaseAdapter(getApplicationContext(),searchDiseases ,false);
				lvDieseases.setAdapter(searchAdp);
			/*	if(lv.getAdapter().equals(testAdapter))
					testAdapter.notifyDataSetChanged();*/
			//}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currURL = normalURL+getString(R.string.getdiseaseSearchPart)+searchText+getString(R.string.getdiseasePagePart)+(pageCount);	
		
	 //	isOngoingSearch = true;
		WebJsonObjectRequest getMedsJson = new WebJsonObjectRequest(Method.GET, currURL, new JSONObject(), new Listener<JSONObject>() {			
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				processResponse(response);
			//	pageCount++;
		//		isOngoingSearch=false;
			}
		}, new ErrorListener() {
	
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Log.e("Error ", "Some error occured"+error);
				//Log.i("Error", new String(error.networkResponse.data));
				isOngoingSearch=false;
				showProgress(false);
			}
		});
		
	//	lvDieseases.setOnScrollListener(this);
		if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), getMedsJson))
			showProgress(true);
		else
			showProgress(false);
		pageCount=1;
		return true;
	}
	
	private String setSearchURL(String searchTxt)
	{
		String url;
		if(currBookingType==0){
			url = getString(R.string.getDiseasesUrlAllo)+getString(R.string.getdiseaseSearchPart)+searchTxt;			
		}else if(currBookingType==3){
			url = getString(R.string.getDiseasesUrlAyur)+getString(R.string.getdiseaseSearchPart)+searchTxt;			
		}else{
			url = getString(R.string.getDiseasesUrlHome)+getString(R.string.getdiseaseSearchPart)+searchTxt;		
		}
		return url;		
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int lastItemInScreen=firstVisibleItem+visibleItemCount;
		Log.d("On scroll called","on scr called aa");
	//	Toast.makeText(act, "on scr called"+pageCount, Toast.LENGTH_LONG).show();
	//	onScrollinProg = true;
	//	lvDieseases.setOnScrollListener(null);
		/*if(isOngoingSearch)
		{			
			if(searchPageCount == 0 && is1Called)				
				return;
			else
				if(searchPageCount == 0 && !is1Called)
					is1Called = true;
			if(searchDiseases.size() > totaldiseasesSearchCount )
				return;
			if((lastItemInScreen==totalItemCount)&&(totaldiseasesSearchCount!=searchDiseases.size())){
				String str = searchURL+getString(R.string.getdiseasePagePart)+(searchPageCount);
				WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, str, new JSONObject(), this, this);
				if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), webjson))
				{
					showProgress(true);
					searchPageCount++;
				}
				else
				{
					showProgress(false);
				}
				
			}
		}
		else
		{
			if(pageCount == 0 && is1Called)				
				return;
			else
				if(pageCount == 0 && !is1Called)
					is1Called = true;*/
		
			if(dieseases.size() > totaldiseasesCount )
				return;
			if(pageCount > (totaldiseasesCount/10) )
				return;
			if((lastItemInScreen==totalItemCount)&&(totaldiseasesCount!=dieseases.size())){	
				Log.d("On scroll called","we service called");				
					
				String str = normalURL+getString(R.string.getdiseaseSearchPart)+searchTxt+getString(R.string.getdiseasePagePart)+(pageCount);
			//	lvDieseases.setOnScrollListener(this);
				
				if(!onScrollinProg || pageCount <3 )
				{
					//Toast.makeText(act, "web service called"+pageCount, Toast.LENGTH_LONG).show();
					WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET, str, new JSONObject(), this, this);
					if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), webjson))
					{
						onScrollinProg=true;
						pageCount++;
						showProgress(true);
					}
					else
					{
						showProgress(false);
					}		
				}
			}	
	//	}
		
	}
}