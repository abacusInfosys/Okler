package com.okler.android;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.android.volley.Request.Method;
import com.okler.adapters.DiseaseAdapter;
import com.okler.adapters.ProductsAdapter;
import com.okler.databeans.DiseaseDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchByMedActivity extends BaseActivity implements Response.Listener,Response.ErrorListener{
	ArrayList<ProductDataBean> med_products,finallist,amed_list;
	boolean isAlpha =false;
 	String[] alpha = {"#","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	ProductsAdapter med_prodAdap,med_prodAdap2;
	DiseaseAdapter dadap;
	ListView lvMeds;
	View bottomBarLayout;
	Toolbar toolBar;
	Button notifCount;
	String FromWhere,getAllMedsUrl,pageNo="",medUrl1,medUrl2,medUrl3,medUrl4,medUrl5,medUrl6,priceRange,alphaMedUrl1,alphaMedUrl2;
	int diseaseId,mappingId,page,prodId,prodtype,BrandId;
	ImageView imgBack;
	TextView currentPosition;
	ProductDataBean apdBean,pdBean,updBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_by_med);
		FromWhere = getIntent().getStringExtra("FromWhere");
		diseaseId = getIntent().getIntExtra("DiseaseId", 0);
		
		medUrl1 = getString(R.string.getMedsUrlPageNo1);
		medUrl2 = getString(R.string.getMedsUrlStartWith2);
		medUrl3 = getString(R.string.getMedsUrlProdId3);
		medUrl4 = getString(R.string.getMedsUrlProdType4);
		medUrl5 = getString(R.string.getMedsUrlPriceRange5);
		medUrl6 = getString(R.string.getMedsUrlBrandId6);
		currentPosition = (TextView)findViewById(R.id.currentPosition);
		
		toolBar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		ActionBar ab = getSupportActionBar();
		//ab.setDisplayHomeAsUpEnabled(true);
		if(Okler.getInstance().getBookingType()==0){
			//ab.setTitle(R.string.title_activity_allopathy);
			
			
					//getString(R.string.getAllMedsAlloUrl);
			prodtype =0;
			mappingId = 4;
			getAllMedsUrl = medUrl1+medUrl2+medUrl3+medUrl4+prodtype+medUrl5+medUrl6;
			alphaMedUrl2 = medUrl3+medUrl4+prodtype+medUrl5+medUrl6;
			Utilities.setTitleText(toolBar, getString(R.string.allopathy));
		}else if(Okler.getInstance().getBookingType()==3){
			//ab.setTitle(R.string.title_activity_ayurvedic);
			//getAllMedsUrl = getString(R.string.getAllMedsAyurUrl);
			prodtype =3;
			mappingId =3;
			getAllMedsUrl = medUrl1+medUrl2+medUrl3+medUrl4+prodtype+medUrl5+medUrl6;
			alphaMedUrl2 = medUrl3+medUrl4+prodtype+medUrl5+medUrl6;
			Utilities.setTitleText(toolBar, getString(R.string.ayurvedic));
		}else{
			//ab.setTitle(R.string.title_activity_homeopathy);
			//getAllMedsUrl = getString(R.string.getAllMedsHomeoUrl);
			prodtype =4;
			mappingId =5;
			getAllMedsUrl = medUrl1+medUrl2+medUrl3+medUrl4+prodtype+medUrl5+medUrl6;
			alphaMedUrl2 = medUrl3+medUrl4+prodtype+medUrl5+medUrl6;
			Utilities.setTitleText(toolBar, getString(R.string.homeopathy));
		}
		if(FromWhere.equals("searchByDiseases")){
			//
			getMedsForDisease();
		}
		else if (FromWhere.equals("searchByMedi")){
			getAllMeds();
		}else{
			getAllMeds();
		}
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
		toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler.getInstance().getBookingType()));
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		
		 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
              android.R.layout.simple_list_item_1, android.R.id.text1, alpha);

		ListView lv1 = (ListView)findViewById(R.id.alphabetsScrView) ;
		lv1.setAdapter(adapter);
		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//int sel = view.getId();
				String abc = ((TextView)view).getText().toString();
				currentPosition.setText(abc);
				if(abc.equals("#")){
					getAllMeds();
				}else{
				alphaMedUrl1 = medUrl1+medUrl2+abc+alphaMedUrl2;
				
				WebJsonObjectRequest alJson = new WebJsonObjectRequest(Method.GET, alphaMedUrl1, new JSONObject(), new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						String resp = String.valueOf(response);
						//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
						JSONObject ajobj = (JSONObject)response;
						JSONObject ajobj1 = new JSONObject();
						JSONObject ajobj2 = new JSONObject();
						JSONObject ajobj3 = new JSONObject();
						JSONArray ajArray = new JSONArray();
						amed_list = new ArrayList<ProductDataBean>();
						apdBean = null;
						try {
							ajobj1 = ajobj.getJSONObject("result");
							int alen = ajobj1.length();
						//	Toast.makeText(getApplicationContext(), "arrlen "+len, Toast.LENGTH_LONG).show();
						
							for (int ai = 0; ai < ((ajobj1.length())-3); ai++) {
								apdBean = new ProductDataBean();
								ajobj2 = ajobj1.getJSONObject(""+ai);
								apdBean.setProdId(Integer.parseInt(ajobj2.getString("id")));
								apdBean.setProdName(ajobj2.getString("name"));
								apdBean.setDesc(ajobj2.getString("description"));
								apdBean.setSide_effect(ajobj2.getString("side_effects"));
								apdBean.setMrp(Float.parseFloat(ajobj2.getString("price")));
								apdBean.setOklerPrice(Float.parseFloat(ajobj2.getString("saleprice")));
								apdBean.setDiscount(Float.parseFloat(ajobj2.getString("discount")));
								apdBean.setWeight(ajobj2.getString("weight"));
								
								String ajimg = ajobj2.getString("images");
								String aurl2;
								if(ajimg.equals(null)||ajimg.equals("null")||ajimg.equals("")){
									aurl2 = "drawable://" + R.drawable.no_image_found;
								}else{
								String aj1[] =ajimg.split(",");
								String aj2=aj1[0];
								String acolon = ":";
								String aj3[]=aj2.split(acolon);
								String aurl = aj3[2];
								String aurl1 = aurl.substring(1);
								int alength = aurl.length();
								aurl2 = aurl.substring(1, (alength-1));
								}
								//JSONObject jimg2 = jimg.getJSONObject("");
								//JSONObject jimg3 = jobj2.getJSONObject("images");
								//pdBean.setImgUrl(jobj2.getJSONObject("images").getJSONObject("").getString("filename"));
								apdBean.setImgUrl(aurl2);
								if(ajobj2.getString("prescription_need").equals("null")||ajobj2.getString("prescription_need").equals(null)||ajobj2.getString("prescription_need").equals(""))
								{
								apdBean.setPresc_needed(0);	
								}else{
								apdBean.setPresc_needed(Integer.parseInt(ajobj2.getString("prescription_need")));
								}
								apdBean.setIndications(ajobj2.getString("indications"));
								apdBean.setDosage(ajobj2.getString("dosage"));
								apdBean.setCompany(ajobj2.getString("company_name"));
								//pdBean.setContraIndi(jobj2.getString(name));
								//pdBean.setCaution(jobj2.getString(name));
								//pdBean.setGeneric_name(jobj2.getString(name));//need string from webservice
								if(ajobj2.has("constituents_details")){
									if(ajArray.length()<25){
										
									}else{
								ajArray = ajobj2.getJSONArray("constituents_details");
								ajobj3 = ajArray.getJSONObject(0);
								if(ajobj3.has("contra_indication")){
									apdBean.setContraIndi(ajobj3.getString("contra_indication"));
								}
								if(ajobj3.has("caution")){
									apdBean.setCaution(ajobj3.getString("caution"));
								}
									}
								}
								amed_list.add(apdBean);
							}
							
							med_prodAdap2 = new ProductsAdapter(getApplicationContext(), amed_list);
							//med_prodAdap = new ProductsAdapter(getApplicationContext(), amed_list);
							//med_prodAdap.notifyDataSetChanged();
							lvMeds.setAdapter(med_prodAdap2);
							isAlpha = true;
							isMedAdap("med_prodAdap2");
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							String resp2 = String.valueOf(e);
						//	Toast.makeText(getApplicationContext(), resp2, Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}
						
						Okler.getInstance().setPdbean(apdBean);
						Okler.getInstance().setProdList(amed_list);
					}

					
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						
						
					}
				});
				VolleyRequest.addJsonObjectRequest(getApplicationContext(), alJson);
				
			}
			}
		});
		
		lvMeds =(ListView) findViewById(R.id.lvMeds);
		lvMeds.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ProductDataBean pdbean2 = new ProductDataBean();
			if(isAlpha){
				pdbean2 = (ProductDataBean)med_prodAdap2.getItem(position);
				int pid1 = pdbean2.getProdId();
				String pname2 = pdbean2.getProdName();
			//	Toast.makeText(getApplicationContext(), pname2+" id = "+pid1, Toast.LENGTH_LONG).show();
				Okler.getInstance().setProdList(med_products);
			}
			else{
			pdbean2 = (ProductDataBean)med_prodAdap.getItem(position);
			int pid = pdbean2.getProdId();
			String pname = pdbean2.getProdName();
			//Toast.makeText(getApplicationContext(), pname+" id = "+pid, Toast.LENGTH_LONG).show();
			Okler.getInstance().setProdList(finallist);
			}
			Gson gson = new Gson();
			String pbean = gson.toJson(pdbean2);
					//String.valueOf(pdbean2);
			Intent intent = new Intent(getApplicationContext(),ProductDetailsActivity.class);	
			
			intent.putExtra("MedId", pbean);
			startActivity(intent);
			}
		});
		
		
	}
	public Boolean isMedAdap(String adap) {
		
		if(adap.equals("med_prodAdap")){
		return  true;
		}
		else
			return false;
	}
	private void getMedsForDisease() {
		String getMedsForDisMappingId,mainUrl,getMedsForDisDiD,getMedsForDisPage,getMedsForDisLastPart;
		getMedsForDisMappingId =getString(R.string.getMedsForDisMappingId);
		getMedsForDisDiD = getString(R.string.getMedsForDisDiD);
		getMedsForDisPage = getString(R.string.getMedsForDisPage);
		getMedsForDisLastPart = getString(R.string.getMedsForDisLastPart);
		mainUrl = getMedsForDisMappingId+mappingId+getMedsForDisDiD+diseaseId+getMedsForDisPage+pageNo+getMedsForDisLastPart;
		WebJsonObjectRequest getMedsJson = new WebJsonObjectRequest(Method.GET, mainUrl, new JSONObject(), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				String resp = String.valueOf(response);
				//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
				JSONObject jobj = (JSONObject)response;
				JSONObject jobj2 = new JSONObject();
				JSONObject jobj3 = new JSONObject();
				JSONArray jArray = new JSONArray();
				ArrayList<ProductDataBean> med_list = new ArrayList<ProductDataBean>();
				updBean = null;
				try {
					jArray = jobj.getJSONArray("result");
					int len = jArray.length();
				//	Toast.makeText(getApplicationContext(), "arrlen "+len, Toast.LENGTH_LONG).show();
				
					for (int i = 0; i < jArray.length(); i++) {
						updBean = new ProductDataBean();
						jobj2 = jArray.getJSONObject(i);
						updBean.setProdId(Integer.parseInt(jobj2.getString("id")));
						updBean.setProdName(jobj2.getString("name"));
						updBean.setDesc(jobj2.getString("description"));
						updBean.setSide_effect(jobj2.getString("side_effects"));
						updBean.setMrp(Float.parseFloat(jobj2.getString("price")));
						updBean.setOklerPrice(Float.parseFloat(jobj2.getString("saleprice")));
						updBean.setDiscount(Float.parseFloat(jobj2.getString("discount")));
						updBean.setWeight(jobj2.getString("weight"));
						
						String jimg = jobj2.getString("images");
						String url2;
						if(jimg.equals(null)||jimg.equals("null")||jimg.equals("")){
							url2 = "drawable://" + R.drawable.no_image_found;
						}else{
						String j1[] =jimg.split(",");
						String j2=j1[0];
						String colon = ":";
						String j3[]=j2.split(colon);
						String url = j3[2];
						String url1 = url.substring(1);
						int length = url.length();
						url2 = url.substring(1, (length-1));
						}
						//JSONObject jimg2 = jimg.getJSONObject("");
						//JSONObject jimg3 = jobj2.getJSONObject("images");
						//pdBean.setImgUrl(jobj2.getJSONObject("images").getJSONObject("").getString("filename"));
						updBean.setImgUrl(url2);
						if(jobj2.getString("prescription_need").equals("null")||jobj2.getString("prescription_need").equals(null)||jobj2.getString("prescription_need").equals(""))
						{
						updBean.setPresc_needed(0);	
						}else{
						updBean.setPresc_needed(Integer.parseInt(jobj2.getString("prescription_need")));
						}
						updBean.setIndications(jobj2.getString("indications"));
						updBean.setDosage(jobj2.getString("dosage"));
						updBean.setCompany(jobj2.getString("company_name"));
						//updBean.setContraIndi(jobj2.getString(name));
						//updBean.setCaution(jobj2.getString(name));
						
						//updBean.setGeneric_name(jobj2.getString(name));//need string from webservice
						if(jobj2.has("constituents_details")){
							if(jArray.length()<25){
								
							}else{
						jArray = jobj2.getJSONArray("constituents_details");
						jobj3 = jArray.getJSONObject(0);
						if(jobj3.has("contra_indication")){
							updBean.setContraIndi(jobj3.getString("contra_indication"));
						}
						if(jobj3.has("caution")){
							updBean.setCaution(jobj3.getString("caution"));
						}
							}
						}
						updBean.setProdType(prodtype);
						med_list.add(updBean);
					}
					med_prodAdap = new ProductsAdapter(getApplicationContext(), med_list);
					lvMeds.setAdapter(med_prodAdap);
					isAlpha = false;
					isMedAdap("med_prodAdap");
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Okler.getInstance().setPdbean(updBean);
				Okler.getInstance().setProdList(med_list);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				
			}
		});
		VolleyRequest.addJsonObjectRequest(getApplicationContext(), getMedsJson);
		
	}

	private void getAllMeds() {
		
		
		
		WebJsonObjectRequest getMedsJson = new WebJsonObjectRequest(Method.GET, getAllMedsUrl, new JSONObject(), this, this);
		VolleyRequest.addJsonObjectRequest(getApplicationContext(), getMedsJson);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_by_med, menu);
		MenuItem item = menu.findItem(R.id.action_settings);
		item.setActionView(R.layout.feed_update_count);
	    //
		 View count = item.getActionView();
		    notifCount = (Button) count.findViewById(R.id.notif_count);
		    notifCount.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Toast.makeText(getApplicationContext(), "btn clicked", Toast.LENGTH_LONG).show();
					Intent i = new Intent(getApplicationContext(), Physiotherapy.class);
					startActivity(i);
				}
			});
		    notifCount.setText(String.valueOf(1));
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
		//Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onResponse(Object response) {
		String resp = String.valueOf(response);
		//Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG).show();
		JSONObject jobj = (JSONObject)response;
		JSONObject jobj1 = new JSONObject();
		try {
			jobj1 = jobj.getJSONObject("result");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject jobj2 = new JSONObject();
		JSONObject jobj3 = new JSONObject();
		JSONArray jArray = new JSONArray();
		pdBean = null;
		int lenobj = jobj1.length();
	//	Toast.makeText(getApplicationContext(), "Obj len = "+lenobj, Toast.LENGTH_LONG).show();
		finallist = new ArrayList<ProductDataBean>();
		
		
		try {
			for (int i = 0; i < (lenobj-3); i++) {
			//jArray = jobj.getJSONArray("");
			jobj2 = jobj1.getJSONObject(i+"");
			int len = jobj2.length();
			pdBean = new ProductDataBean();
			pdBean.setProdId(Integer.parseInt(jobj2.getString("id")));
			pdBean.setProdName(jobj2.getString("name"));
			if(jobj2.has("type_of_packing")){
			pdBean.setPacking_size(jobj2.getString("type_of_packing"));
			}
			pdBean.setDesc(jobj2.getString("description"));
			if(jobj2.has("side_effects")){
			pdBean.setSide_effect(jobj2.getString("side_effects"));
			}
			pdBean.setMrp(Float.parseFloat(jobj2.getString("price")));
			pdBean.setOklerPrice(Float.parseFloat(jobj2.getString("saleprice")));
			pdBean.setDiscount(Float.parseFloat(jobj2.getString("discount")));
			if(jobj2.has("weight")){
			pdBean.setWeight(jobj2.getString("weight"));
			}
			String jimg = jobj2.getString("images");
			String url2;
			if(jimg.equals(null)||jimg.equals("null")||jimg.equals("")){
				url2 = "drawable://" + R.drawable.no_image_found;
			}else{
			String j1[] =jimg.split(",");
			String j2=j1[0];
			String colon = ":";
			String j3[]=j2.split(colon);
			String url = j3[2];
			//String url1 = url.substring(1);
			int length = url.length();
			url2 = url.substring(1, (length-1));
			}
			//JSONObject jimg2 = jimg.getJSONObject("");
			//JSONObject jimg3 = jobj2.getJSONObject("images");
			//pdBean.setImgUrl(jobj2.getJSONObject("images").getJSONObject("").getString("filename"));
			pdBean.setImgUrl(url2);
			if(jobj2.has("prescription_need")){
			if(jobj2.getString("prescription_need").equals("null")||jobj2.getString("prescription_need").equals(null)||jobj2.getString("prescription_need").equals(""))
			{
			pdBean.setPresc_needed(0);	
			}else{
			pdBean.setPresc_needed(Integer.parseInt(jobj2.getString("prescription_need")));
			}
			}
			if(jobj2.has("indications")){
			pdBean.setIndications(jobj2.getString("indications"));
			}
			pdBean.setDosage(jobj2.getString("dosage"));
			pdBean.setCompany(jobj2.getString("company_name"));
			
			//pdBean.setGeneric_name(jobj2.getString(name));//need string from webservice
			if(jobj2.has("constituents_details")){
				if(jArray.length()<25){
					
				}else{
			jArray = jobj2.getJSONArray("constituents_details");
			jobj3 = jArray.getJSONObject(0);
			if(jobj3.has("contra_indication")){
				pdBean.setContraIndi(jobj3.getString("contra_indication"));
			}
			if(jobj3.has("caution")){
				pdBean.setCaution(jobj3.getString("caution"));
			}
				}
			}
			pdBean.setProdType(prodtype);
			finallist.add(pdBean);
			
			
		//	Toast.makeText(getApplicationContext(), "array len = "+len, Toast.LENGTH_LONG).show();
			}
			med_prodAdap = new ProductsAdapter(getApplicationContext(), finallist);
			lvMeds.setAdapter(med_prodAdap);
			isAlpha = false;
			//isMedAdap("med_prodAdap");
			
			} catch (JSONException e) {
			String er = String.valueOf(e);
		//	Toast.makeText(getApplicationContext(), er, Toast.LENGTH_LONG).show();
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
	//Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
	}
	
}
