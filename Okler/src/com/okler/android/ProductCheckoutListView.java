package com.okler.android;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.okler.adapters.CheckoutListViewAdapter;
import com.okler.adapters.HealthShopListAdapter;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okleruser.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductCheckoutListView extends BaseActivity {
	Button notifCount;
	Toolbar toolBar;
	static Button next;
	static TextView totalAmountValue;
	static TextView count;
	static TextView emptyText;
	static ListView productList;
	RelativeLayout checkoutCount;
	View bottomBarLayout;
	static CartDataBean odbean;
	static CartDataBean odbean2;
	ArrayList<ProductDataBean> pdList;
	static ArrayList<ProductDataBean> pdSendList;
	ImageView imgBack;
	int prodtype, prodcount = 0, maincount = 0;
	static CheckoutListViewAdapter clistViewAdapter;
	static RelativeLayout amountLayout_chList;
	static float total = 0.00f;
	static int check;
	static Activity ack;
	static boolean isEmpty = false;
	public static String[] prgmNameList = { "1 kg weight cuff",
			"1 kg weight cuff", "1 kg weight cuff", "1 kg weight cuff",
			"1 kg weight cuff", "1 kg weight cuff" };
	public static int[] prgmImages = { R.drawable.camera, R.drawable.camera,
			R.drawable.camera, R.drawable.camera, R.drawable.camera,
			R.drawable.camera, R.drawable.camera };
	
	UsersDataBean ubean;
	LinearLayout progressLinLayout;
	RelativeLayout back_layout;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_checkout_list_view);
		ack = this;
		check = getIntent().getIntExtra("Check", 1);
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		notifCount = (Button) toolBar.findViewById(R.id.notif_count);
		final ActionBar ab = getSupportActionBar();
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		progressLinLayout = (LinearLayout) findViewById(R.id.progressLinLayout);
		
		showProgress(true);
		getAllAddress();
		
		// ab.setDisplayHomeAsUpEnabled(true);//11_01_2016 Gitesh
		checkoutCount = (RelativeLayout) findViewById(R.id.checkoutCount);
		if (Okler.getInstance().getBookingType() == 0) {
			// ab.setTitle(R.string.title_activity_allopathy);
			prodtype = 0;
			checkoutCount.setBackgroundColor(getResources().getColor(
					R.color.Brightyellow));
			Utilities.setTitleText(toolBar, getString(R.string.allopathy));
		} else if (Okler.getInstance().getBookingType() == 3) {
			// ab.setTitle(R.string.title_activity_ayurvedic);
			prodtype = 3;
			checkoutCount.setBackgroundColor(getResources().getColor(
					R.color.Brightyellow));
			Utilities.setTitleText(toolBar, getString(R.string.ayurvedic));
		} else if (Okler.getInstance().getBookingType() == 4) {
			// ab.setTitle(R.string.title_activity_homeopathy);
			prodtype = 4;
			checkoutCount.setBackgroundColor(getResources().getColor(
					R.color.Brightyellow));
			Utilities.setTitleText(toolBar, getString(R.string.homeopathy));
		} else {
			// ab.setTitle(R.string.title_activity_health_shop_grid);
			prodtype = 1;
			Utilities.setTitleText(toolBar, "Health Shop");
		}
		toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler
				.getInstance().getBookingType()));
		if (check == 0) {
			toolBar.setBackgroundColor(Color.BLUE);
			checkoutCount.setBackgroundColor(Color.BLUE);
			Utilities.setTitleText(toolBar, "Cart");
		}

		/*back_layout = (RelativeLayout)toolBar.findViewById(R.id.back_layout);
		back_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});*/
		UIUtils.setBackClick(toolBar, ack);
		
		amountLayout_chList = (RelativeLayout) findViewById(R.id.amountLayout_chList);
		emptyText = (TextView) findViewById(R.id.emptyText);
		next = (Button) findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isEmpty) {
					Intent intent = new Intent(ProductCheckoutListView.this,
							ServiceCategoryActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				} else {
					CartDataBean cbean = new CartDataBean();
					if (check == 1) {
						cbean = Okler.getInstance().getSingleCart();
					} else {
						cbean = Okler.getInstance().getMainCart();
					}
					int a = cbean.getProdList().get(0).getUnits();
					if (Utilities
							.getUserStatusFromSharedPref(ProductCheckoutListView.this) == UserStatusEnum.LOGGED_IN
							|| (Utilities
									.getUserStatusFromSharedPref(ProductCheckoutListView.this) == UserStatusEnum.LOGGED_IN_FB)
							|| (Utilities
									.getUserStatusFromSharedPref(ProductCheckoutListView.this) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
						if (a != 0) {
							if (total >= 0) {
								UsersDataBean ubean = new UsersDataBean();
								ubean = Utilities
										.getCurrentUserFromSharedPref(ack);
								ArrayList<AddressDataBean> adList = new ArrayList<AddressDataBean>();
								adList = ubean.getAddDatabean();
								if (adList.size() <= 0) {
									Intent intent = new Intent(
											getApplicationContext(),
											ProductCheckoutUserDetail.class);
									intent.putExtra("Check", check);
									startActivity(intent);
								} else {
									Intent intent = new Intent(
											getApplicationContext(),
											ProductCheckoutDeliveryAddress.class);
									intent.putExtra("Check", check);
									startActivity(intent);
								}
							} else {
								Toast.makeText(getApplicationContext(),
										"Unable to proceed with your order",
										Toast.LENGTH_LONG).show();
								Intent intent = new Intent(
										ProductCheckoutListView.this,
										ServiceCategoryActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								Okler.getInstance().setBookingType(100);
								startActivity(intent);
							}
						} else {
							Toast.makeText(ack,
									"Please Select atleast one unit",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Intent in = new Intent(ProductCheckoutListView.this,
								NewSignIn.class);

						startActivity(in);
						finish();
					}
				}
			}
		});

		totalAmountValue = (TextView) findViewById(R.id.totalAmountValue);
		count = (TextView) findViewById(R.id.count);
		if (check == 1) {
			odbean = Okler.getInstance().getSingleCart();
		} else {
			odbean = Okler.getInstance().getMainCart();
		}
		productList = (ListView) findViewById(R.id.productList);

		// pdList.get(0).getProdType();

		// maincount = pdList.size();
		pdSendList = new ArrayList<ProductDataBean>();
		pdSendList = odbean.getProdList();

		if (!(pdSendList.size() <= 0)) {

			int count1 = pdSendList.size();
			if (count1 < 9) {

				count.setText("0" + count1);
			} else {
				count.setText("" + count1);
			}
		}
		// }
		setUi();

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

	public static void totalOrderValue() {
		ArrayList<ProductDataBean> prodL = new ArrayList<ProductDataBean>();
		if (check == 1) {
			odbean2 = Okler.getInstance().getSingleCart();
		} else {
			odbean2 = Okler.getInstance().getMainCart();
		}
		prodL = odbean2.getProdList();

		int length = prodL.size(), units;
		float mrp = 0.00f, okler_pr = 0.00f, disco = 0.00f, tax = 0.00f, shipping = 0.00f, net_pay = 0.00f;

		if (length <= 0) {
			totalAmountValue.setText("00");
		} else {
			ProductDataBean pbean = new ProductDataBean();
			for (int i = 0; i < length; i++) {

				pbean = prodL.get(i);
				units = pbean.getUnits();
				float mrp1 = pbean.getMrp();
				mrp = mrp + (pbean.getMrp() * pbean.getUnits());
				float ok = pbean.getOklerPrice();
				okler_pr = okler_pr
						+ (pbean.getOklerPrice() * pbean.getUnits());

				float tx = pbean.getTax();
				tax = tax + (pbean.getTax() * pbean.getUnits());
			}
			disco = (mrp - okler_pr);
			net_pay = okler_pr + tax;
			if (net_pay < 499) {
				shipping = 50;
				net_pay = net_pay + shipping;
			}
			totalAmountValue.setText("Rs." + okler_pr);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		CartDataBean cbean = new CartDataBean();
		if (check == 1) {
			cbean = Okler.getInstance().getSingleCart();
		} else {
			cbean = Okler.getInstance().getMainCart();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		CartDataBean cbean = new CartDataBean();
		if (check == 1) {
			cbean = Okler.getInstance().getSingleCart();
		} else {
			cbean = Okler.getInstance().getMainCart();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		CartDataBean cbean = new CartDataBean();
		if (check == 1) {
			cbean = Okler.getInstance().getSingleCart();
		} else {
			cbean = Okler.getInstance().getMainCart();
		}
	}

	public static void setUi() {
		/*
		 * ProductCheckoutListView pl = new ProductCheckoutListView();
		 * pl.setView(); }
		 */
		// public void setView(){
		if (check == 1) {
			odbean = Okler.getInstance().getSingleCart();
		} else {
			odbean = Okler.getInstance().getMainCart();
		}
		pdSendList = new ArrayList<ProductDataBean>();
		pdSendList = odbean.getProdList();
		if (pdSendList.size() <= 0) {
			productList.setVisibility(View.GONE);
			emptyText.setVisibility(View.VISIBLE);
			amountLayout_chList.setVisibility(View.INVISIBLE);
			count.setText("00");
			next.setText("CONTINUE TO PRODUCTS");
			isEmpty = true;
			// totalOrderValue();
		} else {
			// emptyText.setVisibility(View.GONE);

			clistViewAdapter = new CheckoutListViewAdapter(ack, pdSendList,
					true, check);
			productList.setAdapter(clistViewAdapter);
			totalOrderValue();
			isEmpty = false;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(notifCount, ack);
	}
	/*
	 * public static void setEmptyUi(){
	 * 
	 * }
	 */
	
	public void getAllAddress(){
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		int userId = ubean.getId();
	String get_addr = getString(R.string.get_user_address)+userId;
	
WebJsonObjectRequest adjson = new WebJsonObjectRequest(Method.GET, get_addr, new JSONObject(), new Response.Listener<JSONObject>() {

	@Override
	public void onResponse(JSONObject response) {
		JSONObject responseObj =(JSONObject)response;
		JSONObject result;
		try {
			
			result = responseObj.getJSONObject("result");
			
			if(!result.isNull("user_Addr")){
				JSONArray jarr2 = result.getJSONArray("user_Addr");
				JSONObject jobj2;
				int length = jarr2.length();
				ArrayList<AddressDataBean>aList = new ArrayList<AddressDataBean>();
				for (int i = 0; i < length; i++) {
					AddressDataBean adbean = new AddressDataBean();
					jobj2 = new JSONObject();
					int a = i+1;
					if(a==length){
						showProgress(false);
						next.setEnabled(true);
						break;
					}
					jobj2 = jarr2.getJSONObject(a);
					//jobj2 = jarr2.getJSONObject(i);
					
					adbean.setFirstname(jobj2.optString("customer_name"));
					adbean.setLastname(jobj2.optString("surname"));
					adbean.setAddress1(jobj2.optString("addr1"));
					adbean.setAddress2(jobj2.optString("addr2"));
					adbean.setZip(jobj2.optString("zip"));
					adbean.setCountry_id(jobj2.optString("country_id"));
					adbean.setAddr_id(jobj2.optString("addr_id"));
					adbean.setCity(jobj2.optString("city_name"));
					adbean.setState(jobj2.optString("state_name"));
					adbean.setCity_id(jobj2.optString("city_id"));
					adbean.setState_id(jobj2.optString("state_id"));
					adbean.setCountry(jobj2.optString("country_name"));
					adbean.setDefault_billing(jobj2.optInt("default_billing"));
					adbean.setDefault_shipping(jobj2.optInt("default_shiping"));
					adbean.setPreferred_del_time(jobj2.optString("delivery_time"));
					//adbean.setPreferred_del_time(jobj2.optString(name));
					AddressDataBean ab = new AddressDataBean();
					ab = adbean;
					aList.add(adbean);
					showProgress(false);
					next.setEnabled(true);
			}
				//showProgress(false);
				ubean.setAddDatabean(aList);
				Utilities.writeCurrentUserToSharedPref(ack, ubean);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showProgress(false);
			next.setEnabled(true);
		}
		
		
	}
}, new Response.ErrorListener() {

	@Override
	public void onErrorResponse(VolleyError error) {
		Log.e("ERROR", ""+error.getStackTrace());
		showProgress(false);
		next.setEnabled(true);
		//Utilities.handleVollyError(error, ack);
	}
});	
	VolleyRequest.addJsonObjectRequest(ack, adjson);
//address call ends here

	}
	
	private void showProgress(boolean paramBoolean) {
		if (paramBoolean) {
			this.progressLinLayout.setVisibility(View.VISIBLE);
			return;
		}
		this.progressLinLayout.setVisibility(View.INVISIBLE);
	}
	
}
