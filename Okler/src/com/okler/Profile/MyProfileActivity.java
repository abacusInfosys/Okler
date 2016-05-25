package com.okler.Profile;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.okler.android.BaseActivity;
import com.okler.android.MyAccount;
import com.okleruser.R;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.dialogs.SignOut;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.RoundedImageView;
import com.okler.utils.Utilities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfileActivity extends BaseActivity implements
		Response.Listener, Response.ErrorListener {

	Button btnSave, btnCancel;
	Toolbar toolBar;
	View bottomBarLayout;
	ImageView imgBack;
	int id;
	String fname, lname, email, phone;
	int user_id;
	RoundedImageView profile_image;
	ImageLoader imgLoader;

	ImageView editPencilBlack;

	TextView bill_address, delilvery_address, text_view_bill_add,
			text_view_delivery_add;

	ArrayList<AddressDataBean> address = new ArrayList<AddressDataBean>();

	UsersDataBean currentUser = new UsersDataBean();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_my_profile);

		ImageView pencil = (ImageView) findViewById(R.id.edit_red_pencil);
		pencil.setVisibility(View.GONE);
		TextView bill = (TextView) findViewById(R.id.addr_title_tv);
		bill.setText("Billing Address");

		profile_image = (RoundedImageView) findViewById(R.id.profile_photo);

		View view = (View) findViewById(R.id.delivery_address);
		ImageView pencil1 = (ImageView) view.findViewById(R.id.edit_red_pencil);
		pencil1.setVisibility(View.GONE);
		TextView delivery = (TextView) view.findViewById(R.id.addr_title_tv);
		delivery.setText("Shipping Address");

		editPencilBlack = (ImageView) findViewById(R.id.editPencilBlack);
		editPencilBlack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(getApplicationContext(),
						EditProfile.class);
				startActivity(intent);

			}
		});

		delilvery_address = (TextView) view.findViewById(R.id.addr_tv);

		bill_address = (TextView) findViewById(R.id.addr_tv);

		Button btn_change_pass = (Button) findViewById(R.id.button_change_password);
		btn_change_pass.setTransformationMethod(null);
		btn_change_pass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent in = new Intent(MyProfileActivity.this,
						ProfileChangePassword.class);
				startActivity(in);

			}
		});

		Button button_sign_out = (Button) findViewById(R.id.button_sign_out);
		button_sign_out.setTransformationMethod(null);
		button_sign_out.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				SignOut signout = new SignOut();
				signout.show(getSupportFragmentManager(), "Sign Out");

			}
		});

		text_view_bill_add = (TextView) findViewById(R.id.text_view_billing_add);
		text_view_bill_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent in = new Intent(MyProfileActivity.this,
						ProfileAddress.class);
				startActivity(in);
			}
		});

		text_view_delivery_add = (TextView) findViewById(R.id.text_view_delivery_add);
		text_view_delivery_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent in = new Intent(MyProfileActivity.this,
						ProfileAddress.class);
				startActivity(in);
			}
		});

		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);

		toolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		final ActionBar ab = getSupportActionBar();
		// ab.setDisplayHomeAsUpEnabled(true);

		toolBar.setBackgroundColor(getResources().getColor(R.color.Blue));

		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Utilities.setTitleText(toolBar, "My Profile");

		currentUser = Utilities.getCurrentUserFromSharedPref(this);

		setFields();

		user_id = currentUser.getId();

		String address_url = getResources()
				.getString(R.string.get_user_address) + "" + user_id;
		WebJsonObjectRequest webjson1 = new WebJsonObjectRequest(Method.GET,
				address_url, new JSONObject(), this, this);
		VolleyRequest.addJsonObjectRequest(this, webjson1);
	}

	public void setFields() {
		TextView txt_name, txt_location, txt_ph_email, dobTv, text_location;

		txt_name = (TextView) findViewById(R.id.text_name);
		// txt_location = (TextView) findViewById(R.id.text_location);
		txt_ph_email = (TextView) findViewById(R.id.text_phone_email);

		// UsersDataBean udBeans = Okler.getInstance().getuDataBean();

		String salutation = currentUser.getSalutation();

		if (salutation == null ||salutation.equals("null")) {
			txt_name.setText(currentUser.getFname() + " "
					+ currentUser.getLname());
		} else {
			txt_name.setText(salutation + " " + currentUser.getFname() + " "
					+ currentUser.getLname());
		}

		// txt_location.setText(udBeans.getState_name());
		txt_ph_email.setText(currentUser.getPhone() + "/"
				+ currentUser.getEmail());

		imgLoader = VolleyRequest.getInstance(this).getImageLoader();

		String First = currentUser.getUserAvatarUrl();
		// "http://183.82.110.105:8081/oklerdevv2/uploads/user_avatar/";
		String Second = currentUser.getUser_image();
		String photo_url = First + Second;

		profile_image.setImageUrl(photo_url, imgLoader);

		/*
		 * dobTv = (TextView)findViewById(R.id.dobTv);
		 * dobTv.setText(currentUser.getDob());
		 */
		text_location = (TextView) findViewById(R.id.text_location);
		if (!(currentUser.getAddDatabean().size() <= 0))
			text_location.setText(currentUser.getAddDatabean().get(0).getCity()
					+ ", " + "India");
		else
			text_location.setText("" + "India");
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResponse(Object response) {
		// TODO Auto-generated method stub

		AddressDataBean adDatabean = null;

		try {
			JSONObject responseObj = (JSONObject) response;
			JSONObject result = responseObj.getJSONObject("result");
			JSONArray doctorsArr = result.getJSONArray("user_Addr");

			Log.i("adress", "array is :" + doctorsArr);
			// Toast.makeText(getApplicationContext(), "array" + doctorsArr,
			// Toast.LENGTH_LONG).show();
			int length = doctorsArr.length();

			for (int i = 0; i < doctorsArr.length(); i++) {
				int a = i + 1;
				if (a == length) {
					break;
				}
				JSONObject docObj = doctorsArr.getJSONObject(a);
				adDatabean = new AddressDataBean();

				// JSONObject docObj =(JSONObject) jobj.getJSONObject(""+i);
				adDatabean.setPat_id(docObj.optInt("pat_id"));
				adDatabean.setFirstname(docObj.optString("customer_name"));
				adDatabean.setLastname(docObj.optString("surname"));
				adDatabean.setAddress1(docObj.optString("addr1"));
				adDatabean.setAddress2(docObj.optString("addr2"));
				if (docObj.has("state_name") == true) {
					adDatabean.setState(docObj.optString("state_name"));
				}
				adDatabean.setCity(docObj.optString("city_name"));
				adDatabean.setState_id(docObj.optString("state_id"));
				adDatabean.setZip(docObj.optString("zip"));
				adDatabean.setCountry(docObj.optString("country_name"));
				adDatabean.setDefault_billing(Integer.parseInt(docObj
						.optString("default_billing")));
				adDatabean.setDefault_shipping(Integer.parseInt(docObj
						.optString("default_shiping")));
				if (docObj.has("landmark") == true) {
					adDatabean.setLandmark(docObj.optString("landmark"));
				}
				adDatabean.setCountry_id(docObj.optString("country_id"));
				adDatabean.setDob(docObj.optString("dob"));
				adDatabean.setZone_id(docObj.optString("zone_id"));
				adDatabean.setCity_id(docObj.optString("city_id"));
				adDatabean.setGenderId(docObj.optString("gender"));
				adDatabean.setAddr_id(docObj.optString("addr_id"));
				adDatabean.setPhone(docObj.optString("mobileNo"));
				adDatabean.setRelationId(docObj.optString("relationid"));

				Log.i("tag", "json object" + docObj);

				address.add(adDatabean);
			}
			currentUser = Okler.getInstance().getuDataBean();

			currentUser.setAddDatabean(address);

			Okler.getInstance().setuDataBean(currentUser);

			checkForDefaultBillingAndShipping();
		}

		catch (Exception e) {
			// TODO: handle exception
			Log.i("exce", String.valueOf(e));
		}
	}

	public void checkForDefaultBillingAndShipping() {
		for (int i = 0; i < address.size(); i++) {
			AddressDataBean add = address.get(i);
			if (add.getDefault_billing() != 0) {
				String text = add.getFirstname() + " " + add.getLastname()
						+ "\n" + add.getAddress1() + "\n" + add.getAddress2()
						+ "\n" + add.getLandmark() + "\n" + add.getCity() + ","
						+ add.getState() + "-" + add.getZip() + "\n"
						+ add.getCountry();
				bill_address.setText(text);
			}
			if (add.getDefault_shipping() != 0) {
				String text = add.getFirstname() + " " + add.getLastname()
						+ "\n" + add.getAddress1() + "\n" + add.getAddress2()
						+ "\n" + add.getLandmark() + "\n" + add.getCity() + ","
						+ add.getState() + "-" + add.getZip() + "\n"
						+ add.getCountry();
				delilvery_address.setText(text);
			}
		}
	}
}
