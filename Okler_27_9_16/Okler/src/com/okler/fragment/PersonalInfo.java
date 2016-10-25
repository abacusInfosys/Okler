package com.okler.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.okler.android.MedicalServices;
import com.okler.android.Physiotherapy;
import com.okleruser.R;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class PersonalInfo extends Fragment implements OnClickListener {

	View view;
	EditText edt_firstname, edt_surname, edt_email, edt_phoneno;
	TextView edt_From, edt_To;
	Calendar calendar;
	static ArrayList<String> citi_ids = new ArrayList<String>();
	int year, month, day;
	boolean flag=false;
	static AutoCompleteTextView edt_city;
	static AutoCompleteTextView edt_state;

	// PhysiotherapyDatabean physioDataBean;

	// String[10][10] states1 = new String();

	static ArrayAdapter<String> adapter;

	static ArrayList<String> cities = new ArrayList<String>();
	static ArrayList<String> states = new ArrayList<String>();
	static ArrayList<String> state_ids = new ArrayList<String>();
	static String state_id;
	Calendar newDate, now, date;
	static String cityUrl;
	static String cityId;
	static String city;
	static String st;
	static WebJsonObjectRequest webjson;

	public PersonalInfo() {

	}

	/*
	 * @Override public void onCreate(Bundle savedInstanceState) { // TODO
	 * Auto-generated method stub super.onCreate(savedInstanceState); }
	 */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// return inflater.inflate(R.layout.fragment_info, container);
		view = inflater.inflate(R.layout.fragment_personal_info, null);
		now = Calendar.getInstance();
		now.set(Calendar.HOUR, 00);
		now.set(Calendar.MINUTE, 00);
		now.set(Calendar.SECOND, 00);
		now.set(Calendar.MILLISECOND, 00);

		edt_city = (AutoCompleteTextView) view.findViewById(R.id.edtTxt_city);
		edt_city.setTag(false);
		edt_state = (AutoCompleteTextView) view.findViewById(R.id.edtTxt_state);
		;

		edt_From = (TextView) view.findViewById(R.id.txtFrom);
		edt_To = (TextView) view.findViewById(R.id.txtTo);

		edt_From.setOnClickListener(this);
		edt_To.setOnClickListener(this);
		String Url = getString(R.string.stateUrl);
		states.clear();
		// web call for states
		getStates();
		/*adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line, cities);
		edt_city.setAdapter(adapter);*/

		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line, states);
		edt_state.setAdapter(adapter);
		
		edt_state.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				int id1 = 0;
				String state = edt_state.getText().toString();
				id1 = states.indexOf(state);
				Log.i("id_tag", "id is:" + id);
				state_id = state_ids.get(id1);

				Log.i("id_tag", "id is:" + id1);

				st = String.valueOf(state_id);
				String st1 = String.valueOf(state_id);
				cityUrl = getResources().getString(R.string.cityUrl)
						+ "state_id=" + state_id;

				webjson = new WebJsonObjectRequest(Method.GET, cityUrl,
						new JSONObject(), new Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								// TODO Auto-generated method stub

								try {
									JSONObject responseObj = (JSONObject) response;
									JSONArray doctorsArr = responseObj
											.getJSONArray("result");
									// docCount=responseObj.getInt("TotalCount");
									for (int i = 0; i < doctorsArr.length(); i++) {
										try {
											JSONObject docObj = (JSONObject) doctorsArr
													.get(i);
											cities.add(docObj
													.getString("city_name"));
											citi_ids.add(docObj.optString("id"));
											Log.i("tag", "json object" + docObj);
										} catch (JSONException e) {
											// TODO: handle exception
											Log.e("JSON Exception",
													e.getMessage());
										}
									}
									Okler.getInstance().setCities(cities);
									Okler.getInstance().setCiti_ids(citi_ids);
									
									adapter = new ArrayAdapter<String>(getActivity(),
											android.R.layout.simple_dropdown_item_1line, cities);
									edt_city.setAdapter(adapter);
								} catch (JSONException jsonEx) {
									Log.e("Exception json", jsonEx
											.getStackTrace().toString());
								}

							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								// TODO Auto-generated method stub

							}
						});
				cities.clear();
				VolleyRequest.addJsonObjectRequest(getActivity(), webjson);

				Log.i("city url", cityUrl);
			}
		});
		edt_city.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView text = (TextView) view
						.findViewById(android.R.id.text1);
				city = text.getText().toString();
				if (!(cities.size() <= 0)) {
					int cid = cities.indexOf(city);
					if (!(citi_ids.size() <= 0)) {
						cityId = citi_ids.get(cid);
					}
				}
				
				flag=true;
				edt_city.setTag(true);
				MedicalServices.stateCityIds(st, cityId);
				Physiotherapy.stateCityIds(st, cityId);
				
			}
			
			
			
			 
		});
		
		edt_city.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				flag=false;
				edt_city.setTag(false);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

		
			
		});
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		DatePickerDialog fromDatePickerDialog;
		DatePickerDialog toDatePickerDialog;
		final SimpleDateFormat dateFormatter;
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		Calendar newCalendar = Calendar.getInstance();

		if (v == edt_From) {
			edt_From.setText("");
			fromDatePickerDialog = new DatePickerDialog(getActivity(),
					new OnDateSetListener() {

						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							edt_From.setError(null);
							
							if (edt_To.getText().toString() == "") {
								newDate = Calendar.getInstance();
								newDate.set(year, monthOfYear, dayOfMonth);
								newDate.set(Calendar.HOUR, 00);
								newDate.set(Calendar.MINUTE, 00);
								newDate.set(Calendar.SECOND, 00);
								newDate.set(Calendar.MILLISECOND, 00);
								int a = newDate.compareTo(now);
								if ((newDate.compareTo(now) == 1)
										|| (newDate.compareTo(now) == 0)) {

									edt_From.setText(dateFormatter
											.format(newDate.getTime()));
								} else {
									edt_From.setText("");
								}
							} else {
								date = Calendar.getInstance();
								String date1 = edt_To.getText().toString();
								try {
									date.setTime(dateFormatter.parse(date1));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								newDate = Calendar.getInstance();
								newDate.set(year, monthOfYear, dayOfMonth);
								newDate.set(Calendar.HOUR, 00);
								newDate.set(Calendar.MINUTE, 00);
								newDate.set(Calendar.SECOND, 00);
								newDate.set(Calendar.MILLISECOND, 00);
								int a = newDate.compareTo(now);
								if ((newDate.compareTo(now) == 1)
										|| (newDate.compareTo(now) == 0)) {

									edt_From.setText(dateFormatter
											.format(newDate.getTime()));
								} else {
									edt_From.setText("");
								}
								/*if (newDate.compareTo(date) == 1) {
									edt_From.setText("");
									 edt_From.setText(dateFormatter.format(newDate.getTime()));
								}*/
								String s=edt_From.getText().toString();
								String e=edt_To.getText().toString();
								try {
									if (dateFormatter.parse(s).before( dateFormatter.parse(e)))
									{
										edt_From.setText(dateFormatter
												.format(newDate.getTime()));
										
									}
									else if(dateFormatter.parse(s).equals(dateFormatter.parse(e)))
									{
										edt_From.setText(dateFormatter
												.format(newDate.getTime()));
									}
									else
									{
										edt_From.setText("");
									   edt_From.setError("Select start date before end date");
									   
									}
								} catch (ParseException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}	

							}
						}
					}, newCalendar.get(Calendar.YEAR),
					newCalendar.get(Calendar.MONTH),
					newCalendar.get(Calendar.DAY_OF_MONTH));
			fromDatePickerDialog.show();
		}

		if (v == edt_To) {
			edt_To.setText("");
			fromDatePickerDialog = new DatePickerDialog(getActivity(),
					new OnDateSetListener() {

						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							edt_To.setError(null);
							if (edt_From.getText().toString() == "") {
								newDate = Calendar.getInstance();
								newDate.set(year, monthOfYear, dayOfMonth);
								newDate.set(Calendar.HOUR, 00);
								newDate.set(Calendar.MINUTE, 00);
								newDate.set(Calendar.SECOND, 00);
								newDate.set(Calendar.MILLISECOND, 00);
								int a = newDate.compareTo(now);
								if ((newDate.compareTo(now) == 1)
										|| (newDate.compareTo(now) == 0)) {

									edt_To.setText(dateFormatter.format(newDate
											.getTime()));
								} else {
									edt_To.setText("");
								}
							} else {
								date = Calendar.getInstance();
								String date1 = edt_From.getText().toString();
								try {
									date.setTime(dateFormatter.parse(date1));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								newDate = Calendar.getInstance();
								newDate.set(year, monthOfYear, dayOfMonth);
								newDate.set(Calendar.HOUR, 00);
								newDate.set(Calendar.MINUTE, 00);
								newDate.set(Calendar.SECOND, 00);
								newDate.set(Calendar.MILLISECOND, 00);
								int a = newDate.compareTo(now);
								if ((newDate.compareTo(date) == 1)
										|| (newDate.compareTo(date) == 0)) {

									edt_To.setText(dateFormatter.format(newDate
											.getTime()));
								} else {
									edt_To.setText("");
								}
								/*
								 * if(newDate.compareTo(date)==-1) {
								 * edt_To.setText("");
								 * //edt_From.setText(dateFormatter
								 * .format(newDate.getTime())); }
								 */
							}
						}
					}, newCalendar.get(Calendar.YEAR),
					newCalendar.get(Calendar.MONTH),
					newCalendar.get(Calendar.DAY_OF_MONTH));
			fromDatePickerDialog.show();
		}
	}
	public void getStates() 
	{
		// web call for states
		webjson = new WebJsonObjectRequest(Method.GET, getResources()
				.getString(R.string.stateUrl), new JSONObject(),
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						states.clear();
						state_ids.clear();
						try {
							JSONObject responseObj = (JSONObject) response;
							JSONArray doctorsArr = responseObj
									.getJSONArray("result");
							// docCount=responseObj.getInt("TotalCount");
							for (int i = 0; i < doctorsArr.length(); i++) {
								try {
									JSONObject docObj = (JSONObject) doctorsArr
											.get(i);
									states.add(docObj.getString("state_name"));
									state_ids.add(docObj.getString("id"));
									Log.i("tag", "json object" + docObj);
								} catch (JSONException e) {
									// TODO: handle exception
									Log.e("JSON Exception", e.getMessage());
								}
							}
							Okler.getInstance().setStates(states);
							/*
							 * if(content==2){ city =
							 * cityTV.getText().toString(); getCity(stateTv); }
							 */
						} catch (JSONException jsonEx) {
							Log.e("Exception json", jsonEx.getStackTrace()
									.toString());
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.e("ERROR", String.valueOf(error));
					}
				}, true);

		VolleyRequest.addJsonObjectRequest(getActivity(), webjson);
		
		
	}
}
