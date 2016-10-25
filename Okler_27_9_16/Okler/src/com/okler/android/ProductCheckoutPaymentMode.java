package com.okler.android;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;
import android.support.v7.widget.Toolbar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductCheckoutPaymentMode extends BaseActivity {
	private View bottomBarLayout;
	private Toolbar toolBar;
	private Button processButton;
	private String orderId;
	TextView textView1;
	ImageView progressInScreen;
	CartDataBean cdbean;
	ImageView imgBack;
	UsersDataBean ubean, defaultUbean;
	AddressDataBean adbean;
	ArrayList<AddressDataBean> adList;
	ArrayList<NameValuePair> nvPair = new ArrayList<NameValuePair>();
	//ArrayList<NameValuePair> nvProds = new ArrayList<NameValuePair>();
	//ArrayList<NameValuePair> nvProdsq = new ArrayList<NameValuePair>();
	ArrayList<ProductDataBean> pdList = new ArrayList<ProductDataBean>();
	String bill_city, bill_add1, bill_add2, billFname, bill_zip, payment_info,
			shipFname, shipCity, fname, emailId, salutation1, ship_add2,
			ship_add1, ship_zip, bill_state, ship_state, ship_charges;
	Button notifCount;
	int check;
	Activity ack;
	boolean isDialog = false;
	int userId;
	RelativeLayout back_layout;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_checkout_payment_mode);
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		notifCount = (Button) toolBar.findViewById(R.id.notif_count);
		setSupportActionBar(toolBar);
		ack = this;
		check = getIntent().getIntExtra("Check", 11);
		textView1 = (TextView) findViewById(R.id.textView1);
		progressInScreen = (ImageView) findViewById(R.id.progressInScreen);
		if (Okler.getInstance().getBookingType() == 0) {
			progressInScreen.setImageResource(R.drawable.md_process_payment);
			textView1.setBackgroundColor(getResources().getColor(
					R.color.Brightyellow));
			Utilities.setTitleText(toolBar, getString(R.string.allopathy));
		} else if (Okler.getInstance().getBookingType() == 3) {
			textView1.setBackgroundColor(getResources().getColor(
					R.color.Brightyellow));
			Utilities.setTitleText(toolBar, getString(R.string.ayurvedic));
		} else if (Okler.getInstance().getBookingType() == 4) {
			textView1.setBackgroundColor(getResources().getColor(
					R.color.Brightyellow));
			Utilities.setTitleText(toolBar, getString(R.string.homeopathy));
		} else {
			Utilities.setTitleText(toolBar, "Health Shop");
		}
		toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler
				.getInstance().getBookingType()));
		if (check == 0) {
			toolBar.setBackgroundColor(Color.BLUE);
			textView1.setBackgroundColor(Color.BLUE);
			Utilities.setTitleText(toolBar, "Cart");
		}
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		UIUtils.setBackClick(toolBar, ack);
		cdbean = Utilities.getCartDataBean(ack);
		pdList = cdbean.getProdList();
		pdList.trimToSize();
		processButton = (Button) findViewById(R.id.payment_placeorder);
		processButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				processButton.setEnabled(false);
				int[] prodid = new int[50];
				int[] quantity = new int[50];
				payment_info = "Cash On Delivery";

				String cart_id = "";
				String shipLname = "", ship_salutation = "", bill_salutation = "", bill_lastname = "", del_time = "", b_land = "", s_land = "";
				ubean = Utilities.getCurrentUserFromSharedPref(ack);
				adList = ubean.getAddDatabean();
				adList.trimToSize();
				int length = adList.size();
				for (int i = 0; i < length; i++) {
					adbean = adList.get(i);
					
					if (adbean.isSelected()) {
						shipFname = adbean.getFirstname();
						shipLname = adbean.getLastname();
						shipCity = adbean.getCity_id();
						ship_salutation = adbean.getShipSalut();
						del_time = adbean.getPreferred_del_time();
						ship_add2 = adbean.getAddress2();
						ship_add1 = adbean.getAddress1();
						ship_zip = "" + adbean.getZip();
						ship_state = adbean.getState_id();
						s_land = adbean.getLandmark();
					}
					if (adbean.getDefault_billing() != 0) {
						bill_city = adbean.getCity_id();
						bill_add1 = adbean.getAddress1();
						bill_add2 = adbean.getAddress2();
						bill_salutation = adbean.getBillSalut();
						billFname = adbean.getFirstname();
						bill_lastname = adbean.getLastname();
						bill_zip = "" + adbean.getZip();
						bill_state = adbean.getState_id();
						b_land = adbean.getLandmark();
					}else{
						bill_city=shipCity;
						bill_add1 = ship_add1;
						bill_add2 = ship_add2;
						bill_salutation = ship_salutation;
						billFname = shipFname;
						bill_lastname = shipLname;
						bill_zip = ship_zip;
						bill_state = ship_state;
						b_land = s_land;
					}
				}
				userId = ubean.getId();
				fname = ubean.getFname();
				emailId = ubean.getEmail();
				salutation1 = ubean.getSalutation();
				float subTotal = cdbean.getSubTotal();
				nvPair.add(new BasicNameValuePair("total", ""
						+ cdbean.getTotalPrice()));
				nvPair.add(new BasicNameValuePair("totalmrpprice", ""+cdbean.getTotalMrp()));
				nvPair.add(new BasicNameValuePair("payment_info", payment_info));
				nvPair.add(new BasicNameValuePair("ccode", cdbean.getcCode()));
				nvPair.add(new BasicNameValuePair("subtotal", ""+subTotal));
				nvPair.add(new BasicNameValuePair("tax", "" + cdbean.getTax()));
				nvPair.add(new BasicNameValuePair("city_select", shipCity));
				nvPair.add(new BasicNameValuePair("ship_salutation",
						ship_salutation));
				nvPair.add(new BasicNameValuePair("bill_salutation",
						bill_salutation));
				nvPair.add(new BasicNameValuePair("bill_landmark", b_land));
				nvPair.add(new BasicNameValuePair("ship_landmark", s_land));

				nvPair.add(new BasicNameValuePair("ship_country", "Indian"));
				nvPair.add(new BasicNameValuePair("bill_email", ubean
						.getEmail()));
				// nvPair.add(new BasicNameValuePair("bill_comment", ""));
				nvPair.add(new BasicNameValuePair("bill_city", bill_city));
				nvPair.add(new BasicNameValuePair("bill_phone", ubean
						.getPhone()));
				nvPair.add(new BasicNameValuePair("ship_phone", ubean
						.getPhone()));
				nvPair.add(new BasicNameValuePair("ship_email", ubean
						.getEmail()));
				nvPair.add(new BasicNameValuePair("bill_email", ubean
						.getEmail()));
				nvPair.add(new BasicNameValuePair("phone", ubean.getPhone()));
				nvPair.add(new BasicNameValuePair("email", ubean.getEmail()));
				nvPair.add(new BasicNameValuePair("bill_firstname", billFname));
				nvPair.add(new BasicNameValuePair("bill_lastname",
						bill_lastname));

				nvPair.add(new BasicNameValuePair("bill_address1", bill_add1));
				nvPair.add(new BasicNameValuePair("bill_address2", bill_add2));
				nvPair.add(new BasicNameValuePair("ship_firstname", shipFname));//
				nvPair.add(new BasicNameValuePair("ship_lastname", shipLname));//
				nvPair.add(new BasicNameValuePair("lastname", shipLname));//
				nvPair.add(new BasicNameValuePair("ship_city", shipCity));
				nvPair.add(new BasicNameValuePair("delivery_slots_timings",
						del_time));
				nvPair.add(new BasicNameValuePair("firstname", billFname));
				nvPair.add(new BasicNameValuePair("cust_id", "" + userId));//
				nvPair.add(new BasicNameValuePair("bill_zip", bill_zip));
				nvPair.add(new BasicNameValuePair("ship_address2", ship_add2));
				nvPair.add(new BasicNameValuePair("ship_address1", ship_add1));
				nvPair.add(new BasicNameValuePair("coupon_discount", ""
						+ cdbean.getCoupon_disc()));
				nvPair.add(new BasicNameValuePair("bill_country", "Indian"));
				nvPair.add(new BasicNameValuePair("ship_zip", ship_zip));
				nvPair.add(new BasicNameValuePair("presc_id", cdbean
						.getPresc_id()));
				nvPair.add(new BasicNameValuePair("ship_state", ship_state));
				nvPair.add(new BasicNameValuePair("bill_state", bill_state));
				nvPair.add(new BasicNameValuePair("shipping_charges", ""
						+ cdbean.getShip_charge()));
				for (int i = 0; i < pdList.size(); i++) {
					ProductDataBean pdBean = pdList.get(i);
					prodid[i] = pdBean.getProdId();
					quantity[i] = pdBean.getUnits();
					cart_id = pdBean.getCart_id();
					if(cart_id == null||cart_id.equals("null"))
					{
						cart_id = "";
					}
					String prodPara = "products[" + i + "][product_id]";
					String qtyPara = "products[" + i + "][quantity]";
					nvPair.add(new BasicNameValuePair(prodPara, "" + prodid[i]));
					nvPair.add(new BasicNameValuePair(qtyPara, "" + quantity[i]));

				}
				nvPair.add(new BasicNameValuePair("cart_id", cart_id));
				nvPair.trimToSize();
				String str = "";// gson.toJson(postParams);
				new PlaceOrderAsyncTask().execute(str);

			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (isDialog) {
			Intent intent = new Intent(ProductCheckoutPaymentMode.this,
					ServiceCategoryActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class PlaceOrderAsyncTask extends AsyncTask<String, Object, Object> {

		String placeOrderUrl = getString(R.string.placeOrderUrl);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(String... params) {
			Object result = Utilities.RegisterNewUser(placeOrderUrl, nvPair);
			return result;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result!=null){
			String resu = result.toString();
			if(resu==null)
				resu="";
			if (resu.contains("Order placed successfully.")) {
				String s1[] = resu.split(",");
				String s2 = s1[3];
				String s3[] = s2.split(":");
				orderId = s3[1];

				if (check == 1) {
					CartDataBean c = new CartDataBean();
					Okler.getInstance().setSingleCart(c);
					Utilities.writeCartToSharedPref(ack, null);
				} else {
					CartDataBean c = new CartDataBean();
					Okler.getInstance().setMainCart(c);
					//Setting shared pref cart to null. This is to make cart empty.
					Utilities.writeCartToSharedPref(ack, null);
				}
				isDialog = true;
				String part1, part2, part3, part4, Url, part5;
				part1 = getString(R.string.orderConfirmCallBackUrl);
				part2 = "&cust_id=";
				part3 = "&customer_name=";
				part4 = "&email=";
				part5 = "&order_id=";
				try {
					fname = URLEncoder.encode(fname, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				Url = part1 + salutation1 + part2 + userId + part3 + fname
						+ part4 + emailId + part5 + orderId;
				WebJsonObjectRequest mailjson = new WebJsonObjectRequest(
						Method.GET, Url, new JSONObject(),
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								Log.e("order mail", "mail sent");
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								Log.e("error", error.getStackTrace().toString());
								Log.e("order mail", "mail not sent");
							}
						}, true);
				VolleyRequest.addJsonObjectRequest(ack, mailjson);
				Intent in = new Intent(ProductCheckoutPaymentMode.this,
						MyOrderHome.class);
				in.putExtra("value", 1);
				in.putExtra("fromOrder", true);
				startActivity(in);
				finish();
			} else {
				Toast.makeText(getApplicationContext(),
						"Some error occured while placing order",
						Toast.LENGTH_LONG).show();
				processButton.setEnabled(true);
			}
		}else{
			Toast.makeText(getApplicationContext(),
					"Some error occured while placing order",
					Toast.LENGTH_LONG).show();
			processButton.setEnabled(true);
		}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(notifCount, ack);
	}
}
