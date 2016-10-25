package com.okler.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.okler.android.MedicalServices;
import com.okler.android.Physiotherapy;
import com.okleruser.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;

import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Utilities;


public class AdditionalInfo  extends Fragment {
View view;
	
	public static WebJsonObjectRequest webjson;
	
	static ArrayList<String> service = new ArrayList<String>();
	static ArrayList<String> serviceId = new ArrayList<String>();
	ArrayList<String> relation = new ArrayList<String>();
	ArrayList<String> relationId = new ArrayList<String>();
	AutoCompleteTextView edt_state;
	static BaseAdapter adapter,adapterRel;
	EditText edtTxt_pincode,edtTxt_weight;
	String s1;
	String service_id,rel_id;
	Spinner spinnerRelation, spinnerService;
	private static int webCall;
	private static Activity act;
	String rel_name;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		//return inflater.inflate(R.layout.fragment_info, container);
		
		view = inflater.inflate(R.layout.fragment_additional_info, container, false);
		spinnerRelation = (Spinner) view.findViewById(R.id.relation_spinner);
		spinnerService = (Spinner) view.findViewById(R.id.service_spinner);
		edtTxt_pincode = (EditText) view.findViewById(R.id.edtTxt_pincode);
		edtTxt_weight = (EditText) view.findViewById(R.id.edtTxt_weight);
		relation.add("Relation");
		spinnerRelation.setSelection(1);
		
		service.add("Service Required For");
		act = getActivity();
		
	//	new PhysiotherapyAsyncTask().execute;
		
		// web call for relation
		webjson=new WebJsonObjectRequest(Method.GET, getResources().getString(R.string.relationUrl), new JSONObject(),new Listener<JSONObject>() 
				{
					@Override
					public void onResponse(JSONObject response) 
					{
						// TODO Auto-generated method stub
						
						try
						{
						JSONObject responseObj =(JSONObject)response;
						JSONArray doctorsArr = responseObj.getJSONArray("result");
						//docCount=responseObj.getInt("TotalCount");
						for(int i = 0 ; i < doctorsArr.length();i++)
						{
							try
							{
								JSONObject docObj =(JSONObject) doctorsArr.get(i);
								relation.add(docObj.getString("relation"));
								relationId.add(docObj.optString("relation_id"));
								if(docObj.getString("relation").equals("myself"))
								{
									rel_name=docObj.optString("relation");
									rel_id=docObj.optString("relation_id");
								}
								Log.i("tag", "json object" + docObj);
								}catch (JSONException e) {
									// TODO: handle exception
									Log.e("JSON Exception", e.getMessage());
								}
						 }
						ArrayList<String> r = new ArrayList<String>();
						r=relationId;
						adapterRel.notifyDataSetChanged();
						}catch(JSONException jsonEx)
						{
							Log.e("Exception json", jsonEx.getStackTrace().toString());
						}
				
					}}, 
					new Response.ErrorListener() 
					{

						@Override
						public void onErrorResponse(VolleyError error) 
						{
							// TODO Auto-generated method stub
				
						}
					}
					,true);
			
		VolleyRequest.addJsonObjectRequest(getActivity(),webjson);
		
		adapterRel = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, relation);
		spinnerRelation.setAdapter(adapterRel);
		
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, service);
		spinnerService.setAdapter(adapter);

		spinnerRelation.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				int id1 = relation.indexOf(spinnerRelation.getSelectedItem().toString());
				ArrayList<String> r = new ArrayList<String>();
				r=relationId;
				if(id1 != 0)
				{
					rel_id = relationId.get(id1-1); 
					rel_name = spinnerRelation.getSelectedItem().toString();
				/*	String rel_d = rel_id;
					String relation_myself=rel_name;*/
										
				}
				Physiotherapy.getRelid(rel_id,rel_name);
				MedicalServices.getRid(rel_id,rel_name);
				/*else
				{
					
				//	rel_id=rel_myself;
					Physiotherapy.getRelid(rel_id,rel_myself);
				}*/
				

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinnerService.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				int id1 = service.indexOf(spinnerService.getSelectedItem().toString());
				if(id1 != 0 )
				{
					service_id = serviceId.get(id1-1);
					MedicalServices.getSid(service_id);
					Physiotherapy.getSevid(service_id);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		AdditionalInfo.webCallServiceRequired(2);
		return view;
	}
	public static void webCallServiceRequired(int param1) {
		
		service.clear();
		service.add("Service Required For");
		serviceId.clear();
		
		webCall = param1;
		
			String serviceurl = act.getResources().getString(R.string.serviceUrl)+webCall;
			
			WebJsonObjectRequest webObjReq=new WebJsonObjectRequest(Method.GET, serviceurl, new JSONObject(),new Response.Listener<JSONObject>() 
					{

						@Override
						public void onResponse(JSONObject response) 
						{

							// TODO Auto-generated method stub
							
							try
							{
							JSONObject responseObj =(JSONObject)response;
							JSONArray doctorsArr = responseObj.getJSONArray("result");
							//docCount=responseObj.getInt("TotalCount");
							for(int i = 0 ; i < doctorsArr.length();i++)
							{
								try
								{
									JSONObject docObj =(JSONObject) doctorsArr.get(i);
									service.add(docObj.getString("name"));
									serviceId.add(docObj.getString("id"));
									Log.i("tag", "json object" + docObj);
									}catch (JSONException e) {
										// TODO: handle exception
										Log.e("JSON Exception", e.getMessage());
									}
							 }
							adapter.notifyDataSetChanged();
							}catch(JSONException jsonEx)
							{
								Log.e("Exception json", jsonEx.getStackTrace().toString());
							}
						
						}

						
					},new Response.ErrorListener() 
					{

						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e("Error","On error msg"+error);
					}
						
					}, true);
					
					VolleyRequest.addJsonObjectRequest(act,webObjReq);		
		
	}

}
