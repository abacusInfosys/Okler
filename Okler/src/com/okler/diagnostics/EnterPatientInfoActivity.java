package com.okler.diagnostics;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.okler.android.BaseActivity;
import com.okleruser.R;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EnterPatientInfoActivity extends BaseActivity {
	ImageView imgBack;
	Toolbar toolBar;
	View bottomBarLayout;
	Button btn_sced_pickup;
	EditText nameTv, surnameTv, addr1Tv, addr2Tv, landmarkTv, pincodeTv,
			mobileNoTv;
	TextView dobTv;
	Spinner relationSpinner, genderSpinner;
	AutoCompleteTextView cityTv, stateTv;
	ArrayList<String> relation = new ArrayList<String>();
	ArrayList<String> relation_id = new ArrayList<String>();
	ArrayList<String> cities = new ArrayList<String>();
	ArrayList<String> city_id = new ArrayList<String>();
	int relation_id1;
	String adId;
	int content;
	Activity ack;
	ArrayList<AddressDataBean> paList = new ArrayList<AddressDataBean>();
	ArrayList<String> citi_ids = new ArrayList<String>();
	UsersDataBean ubean;
	ArrayAdapter<String> adapter, adapter1;
	ArrayList<String> states = new ArrayList<String>();
	String[] gender = new String[] { "Gender", "Male", "Female" };
	int state_id, pat_id;
	String city;
	String cityId;
	int UserId;
	Calendar newCalendar;
	int rid;
	int rel_id;
	int gender_id;
	boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_patient_info);
		ack = this;
		Okler.getInstance().getStates().clear();
		/* Okler.getInstance().setBookingType(9); */
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		relation.add("Relation");
		paList = new ArrayList<AddressDataBean>();
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		UserId = ubean.getId();
		paList = ubean.getPatAddList();

		nameTv = (EditText) findViewById(R.id.nameTv);
		surnameTv = (EditText) findViewById(R.id.surnameTv);
		dobTv = (TextView) findViewById(R.id.dobTv);
		addr1Tv = (EditText) findViewById(R.id.addr1Tv);
		addr2Tv = (EditText) findViewById(R.id.addr2Tv);
		landmarkTv = (EditText) findViewById(R.id.landmarkTv);
		pincodeTv = (EditText) findViewById(R.id.pincodeTv);
		mobileNoTv = (EditText) findViewById(R.id.mobileNoTv);
		cityTv = (AutoCompleteTextView) findViewById(R.id.cityTv);
		relationSpinner = (Spinner) findViewById(R.id.relationSpinner);
		genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
		stateTv = (AutoCompleteTextView) findViewById(R.id.stateTv);

		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Utilities.setTitleText(toolBar, "Diagnostic Test [3/5]");
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		btn_sced_pickup = (Button) findViewById(R.id.btn_sced_pickup);

		// setData();
		btn_sced_pickup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cities = Okler.getInstance().getCities();
				// Gitesh 13feb
				String addpatientpart1 = getString(R.string.AddNewPatientUrl);
				String fname = "", lname = "", dob = "", relationId = "", gender = "", mobileNo = "", addr1 = "", addr2 = "", land_mark = "", pincode = "", city_Id = "", state = "";
				TextValidations tval;
				UIUtils uiutils;
				flag = false;
				tval = new TextValidations(nameTv);
				if (tval.ValidateMinimumChars("Please Enter a Valid name", 3)) {
					flag = false;
					fname = nameTv.getText().toString();
					tval = new TextValidations(surnameTv);
					if (tval.ValidateMinimumChars(
							"Please Enter a Valid Surname", 3)) {
						flag = false;
						lname = surnameTv.getText().toString();
						// tval = new TextValidations(dobTv);
						if (dobTv.getText().toString().trim().equals("")
								|| (dobTv.getText().toString().trim() == "")) {
							Toast.makeText(ack, "Please Enter Your Birthdate",
									Toast.LENGTH_LONG).show();
						} else {
							flag = false;
							dob = dobTv.getText().toString();
							tval = new TextValidations(addr1Tv);
							if (tval.validateText("Please Enter Address")) {
								flag = false;
								addr1 = addr1Tv.getText().toString();
								tval = new TextValidations(addr2Tv);
								if (tval.validateText("Please Enter Address")) {
									flag = false;
									addr2 = addr2Tv.getText().toString();
									tval = new TextValidations(landmarkTv);
									if (tval.validateText("Please Enter Landmark")) {
										flag = false;
										land_mark = landmarkTv.getText()
												.toString();
										tval = new TextValidations(mobileNoTv);
										if (tval.validateMobile("Enter Validate Mobile No")) {
											flag = false;
											mobileNo = mobileNoTv.getText()
													.toString();
											tval = new TextValidations(
													pincodeTv);
											if (tval.validatePinCode("Please Enter a valid Pincode")) {
												flag = false;
												pincode = pincodeTv.getText()
														.toString();
												UIUtils uiutils1 = new UIUtils();
												if (uiutils1
														.validateSpinner(
																"Please check Relation",
																"Relation",
																relationSpinner
																		.getSelectedItem()
																		.toString()
																		.trim(),
																ack)) {
													/*
													 * flag = false; relationId
													 * = String.valueOf(
													 * relationSpinner
													 * .getSelectedItemPosition
													 * ()+1);
													 */
													String relation_name = (String) relationSpinner
															.getSelectedItem();
													relation = Okler
															.getInstance()
															.getRelationList();
													if (!(relation.size() <= 0)) {
														rid = relation
																.indexOf(relation_name);
													}

													relation_id = Okler
															.getInstance()
															.getRelationIdList();
													if (!(relation_id.size() <= 0))
														rel_id = (Integer
																.parseInt(relation_id
																		.get(rid - 1)));
													int r = rel_id;
													UIUtils uiUtils2 = new UIUtils();
													if (uiUtils2
															.validateSpinner(
																	"Please check Gender",
																	"Gender",
																	genderSpinner
																			.getSelectedItem()
																			.toString()
																			.trim(),
																	ack)) {
														gender = String
																.valueOf(genderSpinner
																		.getSelectedItem());
														if (gender
																.equals("Male")) {
															gender_id = 1;
														} else
															gender_id = 2;
														flag = false;
														uiutils = new UIUtils(
																stateTv);
														String st = stateTv
																.getText()
																.toString();
														if (uiutils
																.validateState(
																		"Enter Valid State",
																		st, ack)) {
															flag = false;
															uiutils = new UIUtils(
																	cityTv);
															String ct = cityTv
																	.getText()
																	.toString();
															if (uiutils
																	.validateCity(
																			"Enter Valid City",
																			ct,
																			ack)) {
																int cid = cities
																		.indexOf(city);
																city_id = Okler
																		.getInstance()
																		.getCiti_ids();
																if (!(city_id
																		.size() <= 0))
																	cityId = String
																			.valueOf(city_id
																					.get(cid));
																city_Id = cityId;
																flag = true;
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}

				if (flag) {

					String Url = addpatientpart1 + UserId + "&pat_id=" + pat_id
							+ "&firstname=" + fname + "&surname=" + lname
							+ "&dob=" + dob + "&relationid" + rel_id
							+ "&gender=" + gender_id + "&mobileno=" + mobileNo
							+ "&addr1=" + addr1 + "&addr2=" + addr2
							+ "&land_mark=" + land_mark + "&pincode=" + pincode
							+ "&city_id=" + city_Id;

					WebJsonObjectRequest addjson = new WebJsonObjectRequest(
							Method.GET, Url, new JSONObject(),
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									JSONObject jobj = (JSONObject) response;

									if (jobj.has("message")) {
										String msg = jobj.optString("message");
										if (msg.equals("Add user Patient Address")) {
											try {
												JSONObject jobj2 = jobj
														.getJSONObject("result");
												if (jobj2
														.optString("message")
														.equals("inserted successfully")
														|| jobj2.optString(
																"message")
																.equals("updated successfully")) {
													// Toast.makeText(ack,
													// "Address Added to Databse.",
													// Toast.LENGTH_LONG).show();
													pat_id = jobj2
															.optInt("pat_id");
													AddressDataBean abean = new AddressDataBean();
													abean.setFirstname(nameTv
															.getText()
															.toString());
													abean.setLastname(surnameTv
															.getText()
															.toString());
													abean.setDob(dobTv
															.getText()
															.toString());
													abean.setAddress1(addr1Tv
															.getText()
															.toString());
													abean.setAddress2(addr2Tv
															.getText()
															.toString());
													abean.setLandmark(landmarkTv
															.getText()
															.toString());
													abean.setZip(pincodeTv
															.getText()
															.toString());
													abean.setPhone(mobileNoTv
															.getText()
															.toString());
													abean.setState(stateTv
															.getText()
															.toString());
													abean.setCity(cityTv
															.getText()
															.toString());
													abean.setRelation(relationSpinner
															.getSelectedItem()
															.toString());
													abean.setRelationId(String
															.valueOf(rel_id));
													abean.setGender(genderSpinner
															.getSelectedItem()
															.toString());

													abean.setGenderId(String
															.valueOf(genderSpinner
																	.getSelectedItemPosition() + 1));
													abean.setState_id(String
															.valueOf(state_id));
													abean.setPat_id(pat_id);
													// abean.setSelected(true);
													// cities =
													// Okler.getInstance().getCities();
													String ct = cityTv
															.getText()
															.toString();
													abean.setCity(ct);
													abean.setCity_id(cityId);
													abean.setSelected(true);
													/*
													 * Log.e("City",cities.size()
													 * +"");
													 * if(!(cities.size()<=0)){
													 * for (int i = 0; i <
													 * cities.size(); i++) {
													 * Log.e("City",
													 * cities.get(i)+" "+ct); }
													 * int cid=
													 * cities.indexOf(ct);
													 * if(cid==-1){
													 * 
													 * }else{ cityId =
													 * city_id.get(cid); String
													 * cd = cityId;
													 * Log.e("City", cd); } }
													 */
													// abean.setCity_id(cityId);

													paList.add(abean);
													ubean.setAddDatabean(paList);
													Utilities
															.writeCurrentUserToSharedPref(
																	ack, ubean);
													Intent intent = new Intent(
															getApplicationContext(),
															DiagnoSchedulePickup.class);
													startActivity(intent);
												}
											} catch (JSONException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

										} else {
											Toast.makeText(
													ack,
													"Some error ocurred while saving address",
													Toast.LENGTH_LONG).show();
										}
									} else {
										Toast.makeText(
												ack,
												"Some error ocurred while saving address",
												Toast.LENGTH_LONG).show();
									}
								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									// TODO Auto-generated method stub
									error.printStackTrace();
								}
							});

					VolleyRequest.addJsonObjectRequest(ack, addjson);
					// end
				} else {
					Toast.makeText(ack, "Please Enter Valid Address",
							Toast.LENGTH_LONG).show();
				}
			}

		});

		dobTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DatePickerDialog fromDatePickerDialog;
				final SimpleDateFormat dateFormatter;
				dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
				newCalendar = Calendar.getInstance();
				newCalendar.set(Calendar.HOUR, 00);
				newCalendar.set(Calendar.MINUTE, 00);
				newCalendar.set(Calendar.SECOND, 00);
				newCalendar.set(Calendar.MILLISECOND, 00);
				dobTv.setText("");
				fromDatePickerDialog = new DatePickerDialog(ack,
						new OnDateSetListener() {

							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								Calendar newDate = Calendar.getInstance();
								newDate.set(year, monthOfYear, dayOfMonth);
								newDate.set(Calendar.HOUR, 00);
								newDate.set(Calendar.MINUTE, 00);
								newDate.set(Calendar.SECOND, 00);
								newDate.set(Calendar.MILLISECOND, 00);
								if ((newDate.compareTo(newCalendar) == -1)
										|| (newDate.compareTo(newCalendar) == 0)) {
									dobTv.setText(dateFormatter.format(newDate
											.getTime()));
								} else {
									dobTv.setText("");
								}
							}
						}, newCalendar.get(Calendar.YEAR), newCalendar
								.get(Calendar.MONTH), newCalendar
								.get(Calendar.DAY_OF_MONTH));
				fromDatePickerDialog.show();

			}
		});

		getRelation();

		getState();
		getCity();

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, gender);
		genderSpinner.setAdapter(adapter);

		cityTv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				city = cityTv.getText().toString();

			}
		});

	}

	// Gitesh 13feb
	private void setData() {
		AddressDataBean abean = new AddressDataBean();
		if (paList.size() > 0) {
			abean = paList.get(0);
			nameTv.setText(abean.getFirstname());
			surnameTv.setText(abean.getLastname());
			dobTv.setText(abean.getDob());
			addr1Tv.setText(abean.getAddress1());
			addr2Tv.setText(abean.getAddress2());
			landmarkTv.setText(abean.getLandmark());
			pincodeTv.setText(abean.getZip());
			mobileNoTv.setText(abean.getPhone());
			cityTv.setText(abean.getCity());
			pat_id = abean.getPat_id();
		}
	}

	// end
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.enter_patient_info, menu);
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

	public void getRelation() {
		String relationUrl = getResources().getString(R.string.relationUrl);
		// web call for relation
		WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET,
				relationUrl, new JSONObject(), new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						String a = String.valueOf(response);
						// Toast.makeText(getApplicationContext(), a,
						// Toast.LENGTH_LONG).show();
						try {
							JSONObject responseObj = (JSONObject) response;
							JSONArray doctorsArr = responseObj
									.getJSONArray("result");
							// docCount=responseObj.getInt("TotalCount");
							for (int i = 0; i < doctorsArr.length(); i++) {
								try {
									JSONObject docObj = (JSONObject) doctorsArr
											.get(i);
									relation.add(docObj.getString("relation"));
									relation_id.add(String.valueOf(docObj
											.getInt("relation_id")));
									Log.i("tag", "json object" + docObj);
								} catch (JSONException e) {
									// TODO: handle exception
									Log.e("JSON Exception", e.getMessage());
								}
							}
						} catch (JSONException jsonEx) {
							Log.e("Exception json", jsonEx.getStackTrace()
									.toString());
							// Toast.makeText(getApplicationContext(),
							// String.valueOf(jsonEx),
							// Toast.LENGTH_LONG).show();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

						Log.i("error", String.valueOf(error));

					}
				}, true);

		VolleyRequest.addJsonObjectRequest(ack, webjson);

		adapter = new ArrayAdapter<String>(ack,
				android.R.layout.simple_dropdown_item_1line, relation);
		relationSpinner.setAdapter(adapter);
	}

	public void getCity() {
		cities.clear();
		citi_ids.clear();

		String cityUrl = getResources().getString(R.string.cityUrl)
				+ "state_id=" + state_id;

		WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET,
				cityUrl, new JSONObject(), new Listener<JSONObject>() {
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
									cities.add(docObj.getString("city_name"));
									city_id.add(String.valueOf(docObj
											.getInt("id")));
									Log.i("tag", "json object" + docObj);
								} catch (JSONException e) {
									// TODO: handle exception
									Log.e("JSON Exception", e.getMessage());
								}
							}
							Okler.getInstance().setCities(cities);
							Okler.getInstance().setCiti_ids(city_id);
						} catch (JSONException jsonEx) {
							Log.e("Exception json", jsonEx.getStackTrace()
									.toString());
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				}, true);

		VolleyRequest.addJsonObjectRequest(getApplicationContext(), webjson);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, cities) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text = (TextView) view
						.findViewById(android.R.id.text1);
				text.setTextColor(Color.BLACK);
				return view;
			}
		};
		cityTv.setAdapter(adapter);
		adapter1 = new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_dropdown_item_1line, states) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text = (TextView) view
						.findViewById(android.R.id.text1);
				text.setTextColor(Color.BLACK);
				return view;
			}
		};
		stateTv.setAdapter(adapter1);

		stateTv.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				int id = 0;

				String state = stateTv.getText().toString();

				id = states.indexOf(state);

				Log.i("id_tag", "id is:" + id);

				state_id = 1268 + id;
				getCity();
			}
		});

		cityTv.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				city = cityTv.getText().toString();
				String c = city;
				Log.e("City", city);
				Log.d("City", city);
			}
		});
	}

	public void getState() {
		WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET,
				getResources().getString(R.string.stateUrl), new JSONObject(),
				new Listener<JSONObject>() {
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
									states.add(docObj.getString("state_name"));
									Log.i("tag", "json object" + docObj);
								} catch (JSONException e) {
									// TODO: handle exception
									Log.e("JSON Exception", e.getMessage());
								}
							}
							Okler.getInstance().setStates(states);
						} catch (JSONException jsonEx) {
							Log.e("Exception json", jsonEx.getStackTrace()
									.toString());
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				}, true);

		VolleyRequest.addJsonObjectRequest(getApplicationContext(), webjson);
	}
}
