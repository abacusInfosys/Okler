package com.okler.Profile;

import java.util.ArrayList;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.okler.adapters.ProfileAddressAdapter;
import com.okler.android.BaseActivity;
import com.okleruser.R;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileAddress extends BaseActivity {

	Toolbar toolBar;
	View bottomBarLayout;
	ImageView imgBack;

	TextView text_name, text_address, text_indi_title, text_count;
	RelativeLayout add_button;
	ImageView radioButtonImage, editImage, deleteImage;

	TextView selectAddress;
	Activity act;
	AddressDataBean add;
	View view1;
	int content;
	int length, id;
	static int count = 0;
	LinearLayout layout;
	LinearLayout separator;
	CheckBox chkbx_addr;
	ArrayList<AddressDataBean> addre = new ArrayList<AddressDataBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_profile_address);
		Okler.getInstance().getStates().clear();
		layout = (LinearLayout) findViewById(R.id.layout_address);

		act = this;

		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);

		toolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		final ActionBar ab = getSupportActionBar();
		// ab.setDisplayHomeAsUpEnabled(true);

		selectAddress = (TextView) findViewById(R.id.selectAddress);

		text_indi_title = (TextView) findViewById(R.id.indi_title);
		text_indi_title.setText("ADD A NEW ADDRESS");

		add_button = (RelativeLayout) findViewById(R.id.add_more_Adress_custView);
		add_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent in = new Intent(getApplicationContext(),
						ProfileNewAddress.class);
				content = 1;
				in.putExtra("int value", content);
				startActivity(in);

			}
		});

		toolBar.setBackgroundColor(getResources().getColor(R.color.Blue));

		UsersDataBean udatabean = Okler.getInstance().getuDataBean();
		addre = udatabean.getAddDatabean();

		length = addre.size();

		if (length == 0) {
			selectAddress.setText("You don't have any Saved Addresses");
		}

		// /View view = (View) new View(getApplicationContext());

		for (int i = 0; i < addre.size(); i++) {
			add = addre.get(i);
			LayoutInflater inflator = (LayoutInflater) getApplicationContext()
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			view1 = inflator.inflate(
					R.layout.include_address_layout_with_deletebutton, null);
			view1.setId(i);

			text_name = (TextView) view1
					.findViewById(R.id.titleDeliveryAddress);
			text_address = (TextView) view1.findViewById(R.id.multiLineText);
			chkbx_addr = (CheckBox) view1.findViewById(R.id.chkbx_addr);
			chkbx_addr.setVisibility(View.INVISIBLE);
			// radioButtonImage = (ImageView)
			// view1.findViewById(R.id.radioButtonImage);
			// text_count = (TextView) view1.findViewById(R.id.text_count);
			// radioButtonImage.setVisibility(View.INVISIBLE);
			RelativeLayout.LayoutParams editparams = new RelativeLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			editparams.setMargins(0, 5, 0, 0);

			editImage = (ImageView) view1.findViewById(R.id.editImage);
			editImage.setLayoutParams(editparams);
			editImage.setId(i);
			deleteImage = (ImageView) view1.findViewById(R.id.deleteImage);
			RelativeLayout.LayoutParams deleteparams = new RelativeLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			deleteparams.setMargins(0, 200, 0, 0);
			deleteImage.setLayoutParams(deleteparams);
			deleteImage.setId(i + 1000);

			// text_count.setText(count);

			editImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					int viewid = v.getId();
					// Toast.makeText(getApplicationContext(), "id"+ viewid,
					// Toast.LENGTH_LONG).show();
					/* int id = ((UsersDataBean) editImage.getParent()).getId(); */

					// int id =
					// Integer.parseInt(text_count.getText().toString());
					AddressDataBean add = ProfileAddress.this.addre.get(viewid);

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
					// TODO Auto-generated method stub

					int viewid = v.getId();
					id = viewid - 1000;
					UsersDataBean udatabean = null;
					udatabean = Utilities
							.getCurrentUserFromSharedPref(ProfileAddress.this);

					AddressDataBean addbean = addre.get(id);
					int pat_id = udatabean.getId();
					int uid = udatabean.getId();
					String addr_id = addbean.getAddr_id();
					int default_billing = addbean.getDefault_billing();
					if(default_billing>=1){
					Toast toast =	Toast.makeText(act, "You cannot delete default billing address", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					}else{
					final String deleteUrl = getResources().getString(R.string.delete_user_address) + "customer_id=" + uid + "&addr_id=" +addr_id; 
										
					
					
					// Toast.makeText(getApplicationContext(), "id"+ viewid,
					// Toast.LENGTH_LONG).show();

					final AlertDialog alertDialog = new AlertDialog.Builder(act)
							.create();
					alertDialog.setTitle("Alert");
					alertDialog
							.setMessage("Are you sure, you want to delete the address");

					alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									WebJsonObjectRequest webjson = new WebJsonObjectRequest(
											Method.GET, deleteUrl,
											new JSONObject(),
											new Listener<JSONObject>() {
												@Override
												public void onResponse(
														JSONObject response) {
													// TODO Auto-generated
													// method stub

													try {
														JSONObject responseObj = (JSONObject) response;
														String result = responseObj
																.getString("result");
														if (result
																.equals("true")) {
															Toast.makeText(
																	act,
																	"Address deleted successfully",
																	Toast.LENGTH_LONG)
																	.show();
															length = length - 1;
															selectAddress
																	.setText(length
																			+ " "
																			+ "Saved Addresses");
															View view1 = (View) findViewById(id);
															layout.removeView(view1);
															int sepId = id + 20000;
															View v2 = (View) findViewById(sepId);
															layout.removeView(v2);
															addre.remove(id);

															Intent in = new Intent(
																	ProfileAddress.this,
																	ProfileAddress.class);
															startActivity(in);
															finish();
														}

													} catch (JSONException jsonEx) {
														Log.e("Exception json",
																jsonEx.getStackTrace()
																		.toString());
													}

												}
											}, new Response.ErrorListener() {

												@Override
												public void onErrorResponse(
														VolleyError error) {
													// TODO Auto-generated
													// method stub

												}
											});

									VolleyRequest.addJsonObjectRequest(act,
											webjson);
								}
							});

					alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});

					alertDialog.show();
}
				}
			});

			String text = add.getFirstname() + " " + add.getLastname();
			text_name.setText(text);
			String text1 = add.getAddress1() + "\n" + add.getAddress2() + "\n"
					+ add.getCity() + "," + add.getState() + "-" + add.getZip()
					+ "\n" + add.getCountry();
			text_address.setText(text1);
			layout.addView(view1);

			separator = new LinearLayout(getApplicationContext());
			int t = i + 20000;
			separator.setId(t);
			LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 1);
			params1.setMargins(0, 30, 0, 30);

			separator.setLayoutParams(params1);
			separator.requestLayout();
			separator
					.setBackgroundColor(getResources().getColor(R.color.Black));
			layout.addView(separator);
			count++;

		}

		selectAddress.setText(length + " " + "Saved Addresses");

		/*imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});*/
		UIUtils.setBackClick(toolBar, act);
		Utilities.setTitleText(toolBar, "Address");
	}

}
