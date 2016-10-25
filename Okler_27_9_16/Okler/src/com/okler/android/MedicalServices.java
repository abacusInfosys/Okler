package com.okler.android;

import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.google.gson.JsonArray;
import com.okler.databeans.PhysioAndMedicalBean;
import com.okler.databeans.UsersDataBean;
import com.okler.fragment.AdditionalInfo;
import com.okler.fragment.PersonalInfo;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.network.WebStringRequest;
import com.okler.utils.MediPhysioTextValidations;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MedicalServices extends BaseActivity {

	EditText edt_firstname, edt_surname, edt_email, edt_phoneno, edt_address,
			edt_pincode, edt_weight;
	AutoCompleteTextView edt_state, edt_city;
	Spinner spinner_relation, spinner_service;
	TextView txtcity, txtstate, txtrelation, txtservice, edt_From, edt_To;
	LinearLayout footer1, footer2;
	Activity currActivity;
	Button btnAddInfo, btnAppointment;
	android.app.FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	View bottomBarLayout;
	Toolbar toolBar;
	static String rel_myself="";
	TextView btn_nurse, btn_attendant;
	LinearLayout progressLinLayout;
	String From, to, relation1, service1;
	int user_type = 2;
	int currStatus = 0;
	View view, view1;
	ImageView imgBack;
	public static String id;
	static String state1;
	static String city1;
	static String rel_id;
	static String sev_id;
	ArrayList<String> service = new ArrayList<String>();
	ArrayList<String> serviceId = new ArrayList<String>();
	ArrayList<String> relation = new ArrayList<String>();
	ArrayList<String> relationId = new ArrayList<String>();
	public static int web_param;
	Activity act;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medical_services);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		currActivity = this;
		progressLinLayout = (LinearLayout) findViewById(R.id.progressLinLayout);
	web_param = 2;
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		act = this;
		setSupportActionBar(toolBar);
		final ActionBar ab = getSupportActionBar();
		// ab.setDisplayHomeAsUpEnabled(true);
		ArrayList<String> cities = new ArrayList<String>();
		ArrayList<String> states = new ArrayList<String>();
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_medi_services);
		/*imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(imgBack.getWindowToken(), 0);

			}
		});*/
		UIUtils.setBackClick(toolBar, currActivity);
			Utilities.setTitleText(toolBar, "Medical Services");
		
		footer1 = (LinearLayout) findViewById(R.id.layout_footer1);
		footer2 = (LinearLayout) findViewById(R.id.layout_footer2);

		// values from fragment personal info
		view = findViewById(R.id.fragment_info);
		edt_firstname = (EditText) view.findViewById(R.id.edtTet_firstname);
		edt_surname = (EditText) view.findViewById(R.id.edtTxt_surname);
		edt_email = (EditText) view.findViewById(R.id.edtTxt_email);
		edt_phoneno = (EditText) view.findViewById(R.id.edtTxt_phonenumber);
		edt_state = (AutoCompleteTextView) view.findViewById(R.id.edtTxt_state);
		edt_city = (AutoCompleteTextView) view.findViewById(R.id.edtTxt_city);
		edt_From = (TextView) view.findViewById(R.id.txtFrom);
		edt_To = (TextView) view.findViewById(R.id.txtTo);
		txtcity = (TextView) view.findViewById(R.id.txt_city_id);
		txtstate = (TextView) view.findViewById(R.id.txt_state_id);

		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_addinfo);

		View view = findViewById(R.id.fragment_info);
		View frgLayout = layout.findViewById(R.id.fragment_add_info);
		LinearLayout frgLayout1 = (LinearLayout) frgLayout
				.findViewById(R.id.frag_add_info);
		edt_address = (EditText) frgLayout.findViewById(R.id.edtTxt_address);
		edt_pincode = (EditText) frgLayout.findViewById(R.id.edtTxt_pincode);
		spinner_relation = (Spinner) frgLayout
				.findViewById(R.id.relation_spinner);
		spinner_service = (Spinner) frgLayout
				.findViewById(R.id.service_spinner);
		edt_weight = (EditText) frgLayout.findViewById(R.id.edtTxt_weight);
		txtrelation = (TextView) frgLayout.findViewById(R.id.txt_relationid);
		txtservice = (TextView) frgLayout.findViewById(R.id.txt_serviceid);

		btnAddInfo = (Button) findViewById(R.id.addinfo_btn);
		btnAppointment = (Button) findViewById(R.id.apppointmant_btn);
		bottomBarLayout = findViewById(R.id.bottombar);

		btn_nurse = (TextView) findViewById(R.id.nurse_button);
		btn_attendant = (TextView) findViewById(R.id.attendant_button);

		btn_nurse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				edt_firstname.setText("");
				edt_surname.setText("");
				edt_email.setText("");
				edt_phoneno.setText("");
				edt_From.setText("");
				edt_To.setText("");
				edt_address.setText("");
				edt_pincode.setText("");
				edt_weight.setText("");
				edt_state.setText("");
				edt_city.setText("");

				edt_firstname.setFocusable(true);

				footer2.setVisibility(View.INVISIBLE);
				footer1.setVisibility(View.VISIBLE);
				user_type = 2;
				web_param = 2;
				AdditionalInfo.webCallServiceRequired(web_param);
			}
		});
		btn_attendant.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				edt_firstname.setText("");
				edt_surname.setText("");
				edt_email.setText("");
				edt_phoneno.setText("");
				edt_From.setText("");
				edt_To.setText("");
				edt_address.setText("");
				edt_pincode.setText("");
				edt_weight.setText("");
				edt_state.setText("");
				edt_city.setText("");

				edt_firstname.setFocusable(true);

				footer1.setVisibility(View.INVISIBLE);
				footer2.setVisibility(View.VISIBLE);
				user_type = 4;
				web_param = 4;
				AdditionalInfo.webCallServiceRequired(web_param);
				//PersonalInfo.getStates(act);
			}
		});

		handleMapping(bottomBarLayout);
		Configuration config = getResources().getConfiguration();

		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		btnAddInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

	
				LinearLayout layout = (LinearLayout) findViewById(R.id.layout_addinfo);
				if (currStatus == 0) {
					// btnAddInfo.setEnabled(false);
					Drawable icon = getApplicationContext().getResources()
							.getDrawable(R.drawable.minus);
					btnAddInfo.setCompoundDrawablesWithIntrinsicBounds(null,
							null, icon, null);
					currStatus = 1;
					layout.setVisibility(View.VISIBLE);
				} else {

					Drawable icon = getApplicationContext().getResources()
							.getDrawable(R.drawable.add);
					btnAddInfo.setCompoundDrawablesWithIntrinsicBounds(null,
							null, icon, null);
					currStatus = 0;
					layout.setVisibility(View.GONE);
				}

			}
		});

		btnAppointment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				showDialog();
			}
		});
	}

	public void showDialog() {
		boolean yes;
		boolean flag = true;
		boolean f=(boolean) edt_city.getTag();
		MediPhysioTextValidations medical = new MediPhysioTextValidations(view);
		yes = medical.validateFirstName();
		if (yes) {
			// flag = false;
			yes = medical.validateSurnmae();
			if (yes) {
				// flag = false;
				yes = medical.validateEmail();
				if (yes) {
					// flag = false;
					yes = medical.validatePhoneNo();
					if (yes) {
						// flag = false;
						yes = medical.validateState();
						if (yes) {
							// flag = false;
							yes = medical.validateCity();
							if (yes) {
								String pin = edt_pincode.getText().toString();
								if (pin.equals("") || pin.length() == 0) {
									yes = true;
								} else {
									int len = pin.length();
									if (len < 6 || len > 6) {
										edt_pincode
												.setError("Enter Valid Pincode");
										yes = false;
									}
									/*long pinVal = Long.parseLong(pin);
									if (pinVal == 0) {
										edt_pincode
												.setError("Enter Valid pincode");
										yes = false;
									}*/
								}
								if (yes) {
									String weight = edt_weight.getText()
											.toString();
									if (weight.equals("")
											|| weight.length() == 0) {
										yes = true;
									} else {
										if(weight.length()>3)
										{
											edt_weight.setError("Enter Valid Weight");
											yes=false;
										}										
									}
									if (yes) {
										String from = edt_From.getText()
												.toString();
										if (from.equals("")
												|| from.length() == 0) {
											yes = false;
											edt_From.setError("Please Select a correct starting date");
										} else {

											yes = true;
										}
										if (yes) {
											String to = edt_To.getText()
													.toString();
											if (to.equals("")
													|| to.length() == 0) {
												yes = false;
												edt_To.setError("Please Select a correct ending date");
											} else {
												yes = true;
											}
											if (yes) {
												if(f==false)
													Toast.makeText(getApplicationContext(),"Please enter valid city",Toast.LENGTH_LONG).show();
													else
												callWebService();
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

	public void callWebService() {
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(this);
		int uid = ubean.getId();
		relation = Okler.getInstance().getRelationList();
		relationId = Okler.getInstance().getRelationIdList();
		PhysioAndMedicalBean mdDatabean = new PhysioAndMedicalBean();
		String fname = edt_firstname.getText().toString();
		String sname = edt_surname.getText().toString();
		String email = edt_email.getText().toString();
		String phoneNumber = edt_phoneno.getText().toString();
		String state = edt_state.getText().toString();
		String city = edt_city.getText().toString();
		From = edt_From.getText().toString();
		to = edt_To.getText().toString();
		txtcity.setText(city1);
		txtstate.setText(state1);
		String city_id = txtcity.getText().toString();
		String state_id = txtstate.getText().toString();
		String relation_id = txtrelation.getText().toString();
		String service_id = "";
		service_id = txtservice.getText().toString();

		String address = edt_address.getText().toString();
		/*try {
			address = URLEncoder.encode(address, "utf-8");

		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		String pincode = edt_pincode.getText().toString();
		relation1 = spinner_relation.getSelectedItem().toString();
		if(relation1.equalsIgnoreCase("Relation"))
   			relation1= rel_myself;
		service1 = spinner_service.getSelectedItem().toString();
		String weight = edt_weight.getText().toString();

		
		mdDatabean.setFirstname(fname);
		mdDatabean.setsurname(sname);
		mdDatabean.setphoneno(phoneNumber);
		mdDatabean.setemail(email);
		mdDatabean.setState(state);
		mdDatabean.setCity(city);
		mdDatabean.setPincode(pincode);
		mdDatabean.setfrom(From);
		mdDatabean.setto(to);
		mdDatabean.setAddress(address);
		mdDatabean.setRelation(relation1);
		mdDatabean.setService(service1);
		mdDatabean.setUserType(user_type);
		mdDatabean.setCustId("" + uid);
		Okler.getInstance().setPhysioMedBean(mdDatabean);

		/*
		 * String url =
		 * getResources().getString(R.string.bookAppointment)+"chetan";
		 * Log.i("book url", url);
		 */
		SimpleDateFormat orgFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dtOrg;
		String formatedFromdt = From;
		try {
			dtOrg = orgFormat.parse(From);
			orgFormat = new SimpleDateFormat("yyyy-MM-dd");
			formatedFromdt = orgFormat.format(dtOrg);
			Log.e("Str", "fbbfv" + formatedFromdt);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fname =  URLEncoder.encode(fname, "UTF-8");
			sname =  URLEncoder.encode(sname, "UTF-8");
			state = URLEncoder.encode(state,"UTF-8");
			city = URLEncoder.encode(city,"UTF-8");
			address = URLEncoder.encode(address,"UTF-8");
			relation1 = URLEncoder.encode(relation1,"UTF-8");
			service1 = URLEncoder.encode(service1,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String book_appointment_url = getResources().getString(
				R.string.bookAppointment)
				+ "user_id="
				+ uid
				+ "&email_id="
				+ email
				+ "&first_name="
				+ fname
				+ "&last_name="
				+ sname
				+ "&mobile="
				+ phoneNumber
				+ "&address="
				+ address
				+ "&city="
				+ city_id
				+ "&state="
				+ state_id
				+ "&pincode="
				+ pincode
				+ "&relation="
				+ rel_id
				+ "&required_type="
				+ user_type
				+ "&required_for="
				+ sev_id
				+ "&patient_kgs="
				+ weight
				+ "&start_date="
				+ formatedFromdt
				+ "&end_date=" + to;

		Log.e("final url", book_appointment_url);

		WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET,
				book_appointment_url, new JSONObject(),
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub

						try {
							JSONObject responseObj = (JSONObject) response;
							JSONObject result = responseObj
									.getJSONObject("result");
							id = result.getString("id");
							String message = responseObj.getString("message");
							Okler.getInstance().getPhysioMedBean()
									.setOrder_id(id);
							showProgress(false);

							UsersDataBean ubean = Utilities
									.getCurrentUserFromSharedPref(MedicalServices.this);
							int cust_id = ubean.getId();
							String customer_name = ubean.getFname();
							String email = ubean.getEmail();
							String salutation = ubean.getSalutation();
							if(salutation==null)
								salutation="";
							if(salutation.equals("null"))
								salutation="";

							if (message
									.equals("Nursing service booking inserted successfully")
									|| message
											.equals("Trained service booking inserted successfully")) {
								String rel, serv;
								if (relation1.equals("Relation")) {
									rel = rel_myself;
								} else {
									rel = relation1;
								}

								if (service1.equals("Service Required For")) {
									serv = "";
								} else {
									serv = service1;
								}

								customer_name = URLEncoder.encode(
										customer_name, "UTF-8");
								salutation = URLEncoder.encode(
										salutation, "UTF-8");
								rel = URLEncoder.encode(
										rel, "UTF-8");
								serv = URLEncoder.encode(
										serv, "UTF-8");

								// cust_id=1&salutation=Mr&customer_name=Mark&email=testmail@gmail.com&required_type=3&appointment_date=2016/03/03&appointment_end_date=2016/03/13&relation=4&reason=test%20reason&booking_id=1

								String order_medical_email = getResources()
										.getString(R.string.serverUrl)
										+ getResources().getString(
												R.string.send_email_order_url)
										+ "cust_id="
										+ cust_id
										+ "&salutation="
										+salutation
										+ "&customer_name="
										+ customer_name
										+ "&email="
										+ email
										+ "&required_type="
										+ user_type
										+ "&appointment_date="
										+ From
										+ "&appointment_end_date="
										+ to
										+ "&relation="
										+ rel
										+ "&reason="
										+ serv + "&booking_id=" + id;

								WebJsonObjectRequest webObjReq = new WebJsonObjectRequest(
										Method.GET, order_medical_email,
										new JSONObject(),
										new Response.Listener<JSONObject>() {

											@Override
											public void onResponse(
													JSONObject response) {
												Log.i("email", "mail sent... ");

											}

										}, new Response.ErrorListener() {

											@Override
											public void onErrorResponse(
													VolleyError error) {

												Log.i("email",
														"mail not sent... ");

											}

										}, true);

								VolleyRequest.addJsonObjectRequest(
										getApplicationContext(), webObjReq);
							}

							Intent in = new Intent(MedicalServices.this,
									MyOrderMS_History.class);
							in.putExtra("value", 1);
							in.putExtra("orderId", id);
							startActivity(in);
							/*
							 * String message =
							 * responseObj.getString("message");
							 * if(message.equals
							 * ("Your order has been cancelled")) {
							 * MyOrderMed_Hs_Details.disableCancel(); }
							 */
							// Toast.makeText(c, message,
							// Toast.LENGTH_LONG).show();
						} catch (JSONException jsonEx) {
							Log.e("Exception json", jsonEx.getStackTrace()
									.toString());
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						showProgress(false);
						Toast.makeText(getBaseContext(),
								"Error in connecting to Server",
								Toast.LENGTH_LONG).show();
						Log.e("Exception json", "" + error);
					}
				}, true);

		if (VolleyRequest.addJsonObjectRequest(this, webjson)) {
			showProgress(true);
		} else {
			showProgress(false);

		}
	}

	public static void stateCityIds(String stateId, String cityId) {
		state1 = stateId;
		city1 = cityId;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

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

	private void showProgress(boolean paramBoolean) {
		if (paramBoolean) {
			this.progressLinLayout.setVisibility(View.VISIBLE);
			return;
		}
		this.progressLinLayout.setVisibility(View.INVISIBLE);
	}

	public static void getRid(String rId,String rId_myself) {
		rel_id = rId;
		 rel_myself = rId_myself;
	}

	public static void getSid(String sId) {
		sev_id = sId;

	}

}
