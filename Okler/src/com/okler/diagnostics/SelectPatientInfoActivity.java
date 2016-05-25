package com.okler.diagnostics;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.google.gson.Gson;
import com.okler.Profile.ProfileNewAddress;
import com.okler.android.BaseActivity;
import com.okler.android.ProductCheckoutNewAddress;
import com.okleruser.R;
import com.okler.customviews.CustomViewAddrDiagno;
import com.okler.customviews.CustomViewAddress;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Utilities;

import android.R.layout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelectPatientInfoActivity extends BaseActivity {

	ImageView imgBack;
	Toolbar toolBar;
	View bottomBarLayout;
	Button btn_sced_pickup;
	CustomViewAddrDiagno customAdd[] = new CustomViewAddrDiagno[50];
	UsersDataBean ubean;
	ArrayList<AddressDataBean> aList = new ArrayList<AddressDataBean>();
	Activity ack;
	int content, userId;
	LinearLayout LL_for_custom_addr;
	ImageView radioButtonImage2;
	ArrayList<AddressDataBean> paList = new ArrayList<AddressDataBean>();
	View addMore;
	boolean isCheckedAddr = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_patient_info);
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		ack = this;
		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		LL_for_custom_addr = (LinearLayout) findViewById(R.id.LL_for_custom_addr);
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		userId = ubean.getId();
		paList = ubean.getPatAddList();
		addMore = findViewById(R.id.include_for_add);

		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(imgBack.getWindowToken(), 0);
			}
		});
		Utilities.setTitleText(toolBar, "Diagnostic Test [3/5]");
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		btn_sced_pickup = (Button) findViewById(R.id.btn_sced_pickup);
		// btn_sced_pickup.setEnabled(false);
		btn_sced_pickup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isCheckedAddr) {
					Intent intent = new Intent(getApplicationContext(),
							DiagnoSchedulePickup.class);
					startActivity(intent);
				} else {
					Toast.makeText(ack, "Please select a Address",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		addMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Toast.makeText(ack, "Clicked add more",
				// Toast.LENGTH_LONG).show();
				Intent intent = new Intent(SelectPatientInfoActivity.this,
						ProfileNewAddress.class);
				intent.putExtra("int value", 1);
				startActivity(intent);
			}
		});

		setView();

	}

	private void setView() {
		String AddrUrl = getString(R.string.GetPatientsAllAddr) + userId;
		WebJsonObjectRequest pjson = new WebJsonObjectRequest(Method.GET,
				AddrUrl, new JSONObject(), new Response.Listener<JSONObject>() {

					AddressDataBean adbean = new AddressDataBean();
					JSONObject jobj = new JSONObject();
					JSONObject jobj2;

					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray jarr = response.getJSONArray("result");
							int length = jarr.length();
							paList = new ArrayList<AddressDataBean>();
							for (int i = 0; i < length; i++) {
								// jobj = new JSONObject();
								jobj = jarr.getJSONObject(i);
								adbean = new AddressDataBean();
								adbean.setPat_id(Integer.parseInt(jobj
										.optString("pat_id")));
								adbean.setFirstname(jobj.optString("firstname"));
								adbean.setLastname(jobj.optString("middlename"));
								adbean.setDob(jobj.optString("dob"));
								adbean.setRelationId(jobj
										.optString("relationid"));
								adbean.setGenderId(jobj.optString("gender"));
								adbean.setPhone(jobj.optString("mobileno"));
								jobj2 = new JSONObject();
								String j = jobj
										.optString("residential_address");
								if ((j.equals("null")) || (j.equals("false"))) {
											continue;
								} else {

									jobj2 = jobj
											.getJSONObject("residential_address");
									adbean.setAddress1(jobj2.optString("addr1"));
									adbean.setAddress2(jobj2.optString("addr2"));
									adbean.setLandmark(jobj2
											.optString("land_mark"));
									adbean.setZip(jobj2.optString("pincode"));
									adbean.setCity_id(jobj2
											.optString("city_id"));
									adbean.setCity(jobj2.optString("city_name"));
									adbean.setState(jobj2.optString("state_name"));
									adbean.setGender(jobj
											.optString("userGender"));
									adbean.setRelation(jobj
											.optString("relation_name"));
								}
								paList.add(adbean);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ubean.setPatAddList(paList);
						Utilities.writeCurrentUserToSharedPref(ack, ubean);
						setUi();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						setUi();
					}
				});
		VolleyRequest.addJsonObjectRequest(ack, pjson);
	}

	public void setUi() {
		int length = paList.size();
		AddressDataBean abean;
		for (int i = 0; i < length; i++) {
			customAdd[i] = new CustomViewAddrDiagno(ack);
			abean = new AddressDataBean();
			abean = paList.get(i);
			CheckBox chkbx_diagno_addr = (CheckBox) customAdd[i]
					.findViewById(R.id.chkbx_diagno_addr);
			chkbx_diagno_addr.setTag("" + i);
			// ImageView radioButtonImage =
			// (ImageView)customAdd[i].findViewById(R.id.radioButtonImage);
			int t = i + 1000;

			int tt = i + 1500;
			// radioButtonImage2.setTag(""+tt);
			TextView titleDeliveryAddress = (TextView) customAdd[i]
					.findViewById(R.id.titleDeliveryAddress);
			TextView multiLineText = (TextView) customAdd[i]
					.findViewById(R.id.multiLineText);
			ImageView editImage = (ImageView) customAdd[i]
					.findViewById(R.id.editImage);
			int t2 = i + 100;
			editImage.setTag("" + t2);
			ImageView deleteImage = (ImageView) customAdd[i]
					.findViewById(R.id.deleteImage);
			int t1 = i + 500;
			deleteImage.setTag("" + t1);
			int viewid = Integer.parseInt(String.valueOf(deleteImage.getTag()));
			titleDeliveryAddress.setText(abean.getFirstname() + " "
					+ abean.getLastname());
			String addr = abean.getDob() + "\n";
			String rel = abean.getRelation();
			if (!(rel.equals("null")))
				addr = addr + rel + "\n";
			if(abean.getState() == null || abean.getState().equals("null") || abean.getState().equals(""))
			{
				addr = addr + abean.getGender() + "\n" + abean.getAddress1() + "\n"
						+ abean.getAddress2() + "\n" + abean.getCity() + " - "
						+ abean.getZip() + "\n" + getString(R.string.country_code) + abean.getPhone();
			}
			else
			{
				addr = addr + abean.getGender() + "\n" + abean.getAddress1() + "\n"
						+ abean.getAddress2() + "\n" + abean.getCity() + " - "
						+ abean.getZip() + "\n" + abean.getState() + "\n" + getString(R.string.country_code) + abean.getPhone();
			}
			multiLineText.setText(addr);
			LL_for_custom_addr.addView(customAdd[i]);

			chkbx_diagno_addr
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							AddressDataBean add = new AddressDataBean();
							if (isChecked) {
								int id = Integer.parseInt(String
										.valueOf(buttonView.getTag()));

								int id1;

								for (int j = 0; j < paList.size(); j++) {
									add = paList.get(j);
									add.setSelected(false);
									CustomViewAddrDiagno cad = (CustomViewAddrDiagno) LL_for_custom_addr
											.getChildAt(j);
									CheckBox ch = (CheckBox) cad
											.findViewById(R.id.chkbx_diagno_addr);
									id1 = Integer.parseInt(String.valueOf(ch
											.getTag()));
									if (id1 != id) {
										ch.setChecked(false);
										// isSelected=true;
									}
									// radioButtonImage2.setVisibility(View.GONE);
								}
								add = paList.get(id);
								add.setSelected(true);
								// paList.add(add);
								ubean.setAddDatabean(paList);
								Utilities.writeCurrentUserToSharedPref(ack,
										ubean);
								isCheckedAddr = true;
								// btn_sced_pickup.setEnabled(true);
							} else {
								isCheckedAddr = false;
								// btn_sced_pickup.setEnabled(false);
							}

						}
					});

			editImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					int viewid = Integer.parseInt(String.valueOf(v.getTag()));
					viewid = viewid - 100;
					AddressDataBean add = paList.get(viewid);

					Intent in = new Intent(getApplicationContext(),
							ProfileNewAddress.class);
					Gson gson = new Gson();
					String aBean = gson.toJson(add);
					content = 2;
					in.putExtra("int value", content);
					in.putExtra("addressbean", aBean);
					startActivity(in);

				}
			});

			deleteImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int id = Integer.parseInt(String.valueOf(v.getTag()));
					id = id - 500;
					AddressDataBean adbean = paList.get(id);
					String addrId = adbean.getAddr_id();
					String del1, del2, delUserAddUrl;
					int pat_id;
					UsersDataBean ubean = Utilities
							.getCurrentUserFromSharedPref(ack);
					userId = ubean.getId();
					del1 = getString(R.string.DeletePatientUrlPart1);
					del2 = getString(R.string.DeletePatientUrlPart2);
					pat_id = adbean.getPat_id();
					delUserAddUrl = del1 + userId + del2 + pat_id;

					WebJsonObjectRequest dajson = new WebJsonObjectRequest(
							Method.GET, delUserAddUrl, new JSONObject(),
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {

									String msg = response.optString("message");
									if (msg.equals("deleted success from User Patient Address")) {
										Toast.makeText(
												ack,
												"Address successfully deleted.",
												Toast.LENGTH_LONG).show();
										LL_for_custom_addr.removeAllViews();
										setView();
									} else {
										Toast.makeText(
												ack,
												"Some error ocurred while deleting Address.",
												Toast.LENGTH_LONG).show();
										LL_for_custom_addr.removeAllViews();
										setView();
									}

								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									Toast.makeText(
											ack,
											"Some error ocurred while deleting Address.",
											Toast.LENGTH_LONG).show();
									LL_for_custom_addr.removeAllViews();
									setView();
								}
							});
					VolleyRequest.addJsonObjectRequest(ack, dajson);
				}
			});

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_patient_info, menu);
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
}
