package com.okler.android;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductCheckoutNewAddress extends BaseActivity {
	private Toolbar toolBar;
	private Context context;
	private ImageLoader mImageLoader;
	View bottomBarLayout;
	private Button notifCount, save;
	ImageView imgBack;
	EditText nameTV, surnameTV, addr1TV, addr2TV, pincodeTV,
			et_prefferedDelTime;
	AutoCompleteTextView edtTxt_state, cityTV;
	ArrayList<String> cities = new ArrayList<String>();
	ArrayList<String> city_ids = new ArrayList<String>();

	ArrayAdapter<String> adapter, adapter2;
	CartDataBean cdbean;
	ArrayList<String> states = new ArrayList<String>();
	int content, check, state_id;
	Activity ack;
	String adId, cityUrl;
	WebJsonObjectRequest webjson = null;
	String city_id = "";
	String city = "";
	boolean flag = false;
	String add_address = "";
	int user_id;
	AddressDataBean adbean;
	UsersDataBean ubean;
	ArrayList<AddressDataBean> aList;
	LinearLayout headingLL;
	CheckBox defaultBill, defaultShip;
	int add_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_checkout_new_address);
		ack = this;
		check = getIntent().getIntExtra("Check", 11);
		content = getIntent().getIntExtra("int value", 1);
		toolBar = (Toolbar) findViewById(R.id.toolbar);

		Okler.getInstance().getStates().clear();
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		context = getApplicationContext();
		mImageLoader = VolleyRequest.getInstance(context).getImageLoader();
		setSupportActionBar(toolBar);
		final ActionBar ab = getSupportActionBar();

		if (check == 1) {
			toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler
					.getInstance().getBookingType()));
			cdbean = Okler.getInstance().getSingleCart();
		} else {
			toolBar.setBackgroundColor(getResources().getColor(R.color.Blue));
			cdbean = Okler.getInstance().getMainCart();
		}
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		nameTV = (EditText) findViewById(R.id.et_firstName);
		surnameTV = (EditText) findViewById(R.id.et_surName);
		addr1TV = (EditText) findViewById(R.id.et_address);
		addr2TV = (EditText) findViewById(R.id.et_addressLine2);
		edtTxt_state = (AutoCompleteTextView) findViewById(R.id.edtTxt_state);
		cityTV = (AutoCompleteTextView) findViewById(R.id.edtTxt_city);
		et_prefferedDelTime = (EditText) findViewById(R.id.et_prefferedDelTime);
		pincodeTV = (EditText) findViewById(R.id.et_pincode);
		headingLL = (LinearLayout) findViewById(R.id.headingLL);
		headingLL.setVisibility(View.GONE);
		defaultBill = (CheckBox) findViewById(R.id.defaultShip);
		defaultShip = (CheckBox) findViewById(R.id.defaultShip);

		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(imgBack.getWindowToken(), 0);
			}
		});
		Utilities.setTitleText(toolBar, "New Address");
		save = (Button) findViewById(R.id.saveButton);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				editAddress();

			}
		});
		JSONObject addbean = null;
		if (content == 2) {

			String add = getIntent().getStringExtra("addressbean");
			try {
				addbean = new JSONObject(add);
				nameTV.setText(addbean.optString("firstname"));
				surnameTV.setText(addbean.optString("lastname"));
				addr1TV.setText(addbean.optString("address1"));
				addr2TV.setText(addbean.optString("address2"));
				et_prefferedDelTime.setText(addbean
						.optString("preferred_del_time"));
				pincodeTV.setText(addbean.optString("zip"));
				edtTxt_state.setText(addbean.optString("state"));
				cityTV.setText(addbean.optString("city"));
				adId = addbean.optString("addr_id");
				city_id = addbean.optString("city_id");
				String ss = addbean.optString("state_id");
				if (ss.equals("")) {
					state_id = 0;
				} else {
					state_id = Integer.parseInt(addbean.optString("state_id"));
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		getStates();
		states = Okler.getInstance().getStates();
		adapter = new ArrayAdapter<String>(getApplicationContext(),
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
		edtTxt_state.setAdapter(adapter);
		edtTxt_state.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				getCity(edtTxt_state);
			}
		});

		cityTV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				city = cityTV.getText().toString();

			}
		});

	}

	public void editAddress() {
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		aList = new ArrayList<AddressDataBean>();
		aList = ubean.getAddDatabean();
		// int user_id = ubean.getId();
		user_id = ubean.getId();
		String name = "", name1 = "", email1 = "", pref_del_time = "", pref_del_time1 = "", surname = "", surname1 = "", dob = "", add1 = "", add2 = "", email = "", address1 = "", address2 = "", landmark1 = "", country_id = "", relation = "", zone_id = "", gender = "", landmark = "", pincode = "", mobile = "", city = "", state = "";
		flag = false;
		TextValidations tval;
		UIUtils uiUtils;
		adbean = new AddressDataBean();
		tval = new TextValidations(nameTV);
		if (tval.ValidateMinimumChars("Enter a Valid Name", 3)) {
			name = nameTV.getText().toString();
			adbean.setFirstname(name);
			tval = new TextValidations(surnameTV);
			if (tval.ValidateMinimumChars("Enter a Valid Surname", 3)) {
				surname = surnameTV.getText().toString();
				adbean.setLastname(name);
				tval = new TextValidations(addr1TV);
				if (tval.validateText("Please Enter Address")) {
					add1 = addr1TV.getText().toString();
					adbean.setAddress1(add1);
					tval = new TextValidations(addr2TV);
					if (tval.validateText("Please Enter Address")) {
						add2 = addr2TV.getText().toString();
						adbean.setAddress2(add2);
						uiUtils = new UIUtils(edtTxt_state);
						String state1 = edtTxt_state.getText().toString();
						if (uiUtils.validateState("Please Enter a Valid State",
								state1, ack)) {
							state = "" + state_id;
							adbean.setState(state1);
							adbean.setState_id(String.valueOf(state_id));
							tval = new TextValidations(pincodeTV);
							if (tval.validatePinCode("Enter a valid Pin Code")) {
								pincode = pincodeTV.getText().toString();
								adbean.setZip(pincode);
								uiUtils = new UIUtils(cityTV);
								String city1 = cityTV.getText().toString();
								if (uiUtils.validateCity("Enter a Valid City",
										city1, ack)) {
									if (!(cities.size() <= 0)) {
										int cid = cities.indexOf(city1);
										adbean.setCity(city1);
										if (!(city_ids.size() <= 0))
											city_id = city_ids.get(cid);
										adbean.setCity_id(city_id);
										city = city_id;
									}
									tval = new TextValidations(
											et_prefferedDelTime);
									if (tval.validateText("Please Enter Preferred Time")) {
										adbean.setPreferred_del_time(et_prefferedDelTime
												.getText().toString());
										pref_del_time = et_prefferedDelTime
												.getText().toString();
										flag = true;
									}
								}
							}
						}
					}
				}
			}
		}

		if (flag) {
			landmark = "";
			country_id = "99";
			zone_id = "";
			try {
				address1 = URLEncoder.encode(add1, "UTF-8");
				address2 = URLEncoder.encode(add2, "UTF-8");
				landmark1 = URLEncoder.encode(landmark, "UTF-8");
				name1 = URLEncoder.encode(name, "UTF-8");
				surname1 = URLEncoder.encode(surname, "UTF-8");
				pref_del_time1 = URLEncoder.encode(pref_del_time, "UTF-8");
				// email1 = URLEncoder.encode(email,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mobile = ubean.getPhone();
			email = ubean.getEmail();
			if (content == 2) {
				add_address = getResources().getString(
						R.string.add_patient_address)
						+ "addr_id="
						+ adId
						+ "&customer_id="
						+ user_id
						+ "&company="
						+ ""
						+ "&customer_name="
						+ name1
						+ "&surname="
						+ surname1
						+ "&email="
						+ email
						+ "&mobileNo="
						+ mobile
						+ "&addr1="
						+ address1
						+ "&addr2="
						+ address2
						+ "&landmark="
						+ landmark1
						+ "&zip="
						+ pincode
						+ "&country_id="
						+ country_id
						+ "&zone_id="
						+ zone_id
						+ "&state_id="
						+ state
						+ "&city_id="
						+ city
						+ "&delivery_time="
						+ pref_del_time1;

			} else {
				add_address = getResources().getString(
						R.string.add_patient_address)
						+ "&customer_id="
						+ user_id
						+ "&company="
						+ ""
						+ "&customer_name="
						+ name1
						+ "&surname="
						+ surname1
						+ "&email="
						+ email
						+ "&mobileNo="
						+ mobile
						+ "&addr1="
						+ address1
						+ "&addr2="
						+ address2
						+ "&landmark="
						+ landmark1
						+ "&zip="
						+ pincode
						+ "&country_id="
						+ country_id
						+ "&zone_id="
						+ zone_id
						+ "&state_id="
						+ state
						+ "&city_id="
						+ city
						+ "&delivery_time="
						+ pref_del_time1;
			}
			WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET,
					add_address, new JSONObject(), new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub

							try {
								JSONObject responseObj = (JSONObject) response;
								JSONObject doctorsArr = responseObj
										.getJSONObject("result");

								String messaqge = doctorsArr
										.getString("message");
								if ((messaqge.equals("inserted successfully"))) {

									add_id = Integer.parseInt(doctorsArr
											.getString("addr_id"));
									String u1, u2, u3, setDefaultAddUrl = "";

									u1 = getString(R.string.DefaultBillShipPart1);
									u2 = getString(R.string.DefaultBillShipPart2);
									u3 = getString(R.string.DefaultBillShipPart3);
									if (defaultBill.isChecked()) {
										setDefaultAddUrl = u1 + user_id + u2
												+ u3 + add_id;
									} else if (defaultShip.isChecked()) {
										setDefaultAddUrl = u1 + user_id + u2
												+ add_id + u3;
									} else {
										aList.add(adbean);
										ubean.setAddDatabean(aList);
										Utilities.writeCurrentUserToSharedPref(
												ack, ubean);
										Intent intent = new Intent(
												getApplicationContext(),
												ProductCheckoutDeliveryAddress.class);
										intent.putExtra("Check", check);
										startActivity(intent);

									}
									adbean.setAddr_id(String.valueOf(add_id));
									// adbean.setDefault_shiping(default_shiping);
									if (!(setDefaultAddUrl.equals(""))) {
										if (defaultShip.isChecked()
												|| defaultShip.isChecked()) {
											WebJsonObjectRequest adjson = new WebJsonObjectRequest(
													Method.GET,
													setDefaultAddUrl,
													new JSONObject(),
													new Response.Listener<JSONObject>() {

														@Override
														public void onResponse(
																JSONObject response) {
															/* if(flag){ */
															// Utilities.writeCurrentUserToSharedPref(ack,
															// ubean);
															if (defaultBill
																	.isChecked()) {
																adbean.setDefault_billing(add_id);
															} else {
																adbean.setDefault_shipping(add_id);
															}
															aList.add(adbean);
															ubean.setAddDatabean(aList);
															Utilities
																	.writeCurrentUserToSharedPref(
																			ack,
																			ubean);
															Intent intent = new Intent(
																	getApplicationContext(),
																	ProductCheckoutDeliveryAddress.class);
															intent.putExtra(
																	"Check",
																	check);
															startActivity(intent);
															// }
														}
													},
													new Response.ErrorListener() {

														@Override
														public void onErrorResponse(
																VolleyError error) {
															// TODO
															// Auto-generated
															// method stub
															Log.e("ERROR",
																	String.valueOf(error));
														}
													});
											VolleyRequest.addJsonObjectRequest(
													ack, adjson);
										}
									}
								} else {
									if (content == 2) {
										adbean.setDefault_shipping(Integer
												.parseInt(adId));
										aList.add(adbean);
										ubean.setAddDatabean(aList);
										Utilities.writeCurrentUserToSharedPref(
												ack, ubean);
									}
									Intent intent = new Intent(
											getApplicationContext(),
											ProductCheckoutDeliveryAddress.class);
									intent.putExtra("Check", check);
									startActivity(intent);
								}
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
					});

			VolleyRequest
					.addJsonObjectRequest(getApplicationContext(), webjson);
		} else {
			Toast.makeText(ack, "Please Enter Valid Address", Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

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

	public void getCity(EditText edt) {

		int id = 0;
		cities.clear();
		city_ids.clear();
		String state = edt.getText().toString();
		states = Okler.getInstance().getStates();
		id = states.indexOf(state);
		Log.i("id_tag", "id is:" + id);
		state_id = 1268 + id;
		cityUrl = getResources().getString(R.string.cityUrl) + "state_id="
				+ state_id;
		webjson = new WebJsonObjectRequest(Method.GET, cityUrl,
				new JSONObject(), new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONObject responseObj = (JSONObject) response;
							JSONArray doctorsArr = responseObj
									.getJSONArray("result");
							for (int i = 0; i < doctorsArr.length(); i++) {
								try {
									JSONObject docObj = (JSONObject) doctorsArr
											.get(i);
									cities.add(docObj.getString("city_name"));
									city_ids.add(docObj.optString("id"));
									Log.i("tag", "json object" + docObj);
								} catch (JSONException e) {
									// TODO: handle exception
									Log.e("JSON Exception", e.getMessage());
								}
							}
							Okler.getInstance().setCities(cities);
							Okler.getInstance().setCiti_ids(city_ids);
							adapter2 = new ArrayAdapter<String>(
									ack,
									android.R.layout.simple_dropdown_item_1line,
									cities)
							// adapter = new ArrayAdapter<String>(this,
							// android.R.layout.simple_dropdown_item_1line,
							// cities)
							{
								@Override
								public View getView(int position,
										View convertView, ViewGroup parent) {
									View view = super.getView(position,
											convertView, parent);
									TextView text = (TextView) view
											.findViewById(android.R.id.text1);
									text.setTextColor(Color.BLACK);
									return view;
								}
							};
							cityTV.setAdapter(adapter2);
							adapter2.notifyDataSetChanged();
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

		VolleyRequest.addJsonObjectRequest(getApplicationContext(), webjson);

		Log.i("city url", cityUrl);

	}

	public void getStates() {
		// web call for states
		String ste = getResources().getString(R.string.stateUrl);
		webjson = new WebJsonObjectRequest(Method.GET, ste, new JSONObject(),
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
							if (content == 2) {
								city = cityTV.getText().toString();
								getCity(edtTxt_state);
							}
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

		VolleyRequest.addJsonObjectRequest(getApplicationContext(), webjson);
	}
}
