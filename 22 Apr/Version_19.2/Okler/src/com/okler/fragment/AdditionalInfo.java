package com.okler.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.okler.android.MedicalServices;
import com.okler.android.Physiotherapy;
import com.okleruser.R;

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


public class AdditionalInfo  extends Fragment implements Response.Listener, Response.ErrorListener{
View view;
	
	WebJsonObjectRequest webjson;
	
	ArrayList<String> service = new ArrayList<String>();
	ArrayList<String> serviceId = new ArrayList<String>();
	ArrayList<String> relation = new ArrayList<String>();
	ArrayList<String> relationId = new ArrayList<String>();
	AutoCompleteTextView edt_state;
	BaseAdapter adapter,adapterRel;
	EditText edtTxt_pincode,edtTxt_weight;
	String s1;
	String service_id,rel_id;
	Spinner spinnerRelation, spinnerService;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		//return inflater.inflate(R.layout.fragment_info, container);
		
		view = inflater.inflate(R.layout.fragment_additional_info, container, false);
		spinnerRelation = (Spinner) view.findViewById(R.id.relation_spinner);
		spinnerService = (Spinner) view.findViewById(R.id.service_spinner);
		edtTxt_pincode = (EditText)view.findViewById(R.id.edtTxt_pincode);
		edtTxt_weight = (EditText)view.findViewById(R.id.edtTxt_weight);
		relation.add("Relation");
		service.add("Service Required For");
	//	new PhysiotherapyAsyncTask().execute;
		
		// web call for service 
		webjson = new WebJsonObjectRequest(com.android.volley.Request.Method.GET, getResources().getString(R.string.serviceUrl), new JSONObject(), this, this);
		VolleyRequest.addJsonObjectRequest(getActivity(), webjson);
		
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
	
		/*spinnerRelation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				
			}
		});*/
		
		spinnerRelation.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				int id1 = relation.indexOf(spinnerRelation.getSelectedItem().toString());
				ArrayList<String> r = new ArrayList<String>();
				r=relationId;
				if(id1 != 0)
				{
					rel_id = relationId.get(id1-1); 
					String rel_d = rel_id;
					MedicalServices.getRid(rel_id);
					Physiotherapy.getRelid(rel_id);
				}
				
				
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
		/*spinnerService.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
			}
		});*/
		/*edtTxt_pincode.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				s1 = s.toString();
				if(s.equals("")||s.length()==0||s.toString()=="")
				{
					//edtTxt_pincode.setText("0");
					s1="0";
				}				
				int len = s.length();
				if(len<6||len>6)
				{
						//edtTxt_pincode.setText("");
						edtTxt_pincode.setError("Enter Valid Pincode");
				}
				long mobile_no = Long.parseLong(s1);
				if(mobile_no == 0)
				{
					edtTxt_pincode.setError("Enter Valid pincode");
				}
			}
		});*/
		
		/*edtTxt_weight.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				if(s.equals("")||s.length()==0||s.toString()=="")
				{
					edtTxt_weight.setText("0");
				}
				
				long mobile_no = Long.parseLong(edtTxt_weight.getText().toString());
				if(mobile_no == 0)
				{
					edtTxt_weight.setError("Enter Valid Weight");
				}
				
				else
				{
					int text = Integer.parseInt(s.toString());
					if(text<=0){
					//edtTxt_pincode.setText("");
					edtTxt_weight.setError("Enter Valid Weight");
				}
				}
			}
		});*/
		return view;
	}
	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		
		Log.e("Error","On error msg"+error);
		
	}
	@Override
	public void onResponse(Object response) 
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
	
/*	public class PhysiotherapyAsyncTask extends AsyncTask<String, Object, Object>
	{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try
			{
				InputStream inputStream= null;
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(Update_User_Info);
				StringEntity stringEntity = new StringEntity(params[0]);
				httpPost.setEntity(stringEntity);
				
				httpPost.setHeader("Accept","application/Json");
				httpPost.addHeader("content-type", "application/json");
				HttpResponse httpResponse = httpClient.execute(httpPost);
				inputStream=httpResponse.getEntity().getContent();
				
				if(inputStream!=null){
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					String line="";
					String result="";
					while ((line = bufferedReader.readLine()) != null) {
						result+=line;
						}
					Log.e("ERRORISIS","IS: "+result);
					inputStream.close();
					return result;
				}
			}catch (UnsupportedEncodingException e) {				
				e.printStackTrace();
				return null;
			} catch (ClientProtocolException e) {				
				e.printStackTrace();
				return null;
			} catch (IOException e) {				
				e.printStackTrace();
				return null;
			} 
			
			return null;
		}
		
	}*/
}
