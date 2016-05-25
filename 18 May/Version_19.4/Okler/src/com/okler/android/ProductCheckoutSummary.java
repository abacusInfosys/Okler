package com.okler.android;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.okler.customviews.CustomViewProdCheckoutSummary;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.helpnsupport.LegalActivity;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductCheckoutSummary extends BaseActivity {
	View bottomBarLayout;
	Toolbar toolBar;
	Button notifCount;
	RelativeLayout customViewRL1, billing_RL, delivery_RL, customViewRL2,
			customViewRL3, coupon_disc_RL, checkout_quant_RL;
	LinearLayout parentForCustomView;
	CustomViewProdCheckoutSummary[] prodSummary = new CustomViewProdCheckoutSummary[500];
	Button apply_button;
	TextView coupon_appl_text, net_pay_value_tv, amount_value_tv,
			okler_disc_value_tv, coupon_disc_value_tv, shipping_charg_value_tv,
			tax_value_tv;
	TextView next, baddr_title_tv, baddr_tv, addr_title_tv, addr_tv,
			firstnameValueTv, surnameValueTv, mobileValueTv, emailValueTv;
	ImageView checkout_progress_summary_Iv;
	ImageView imgBack, bedit_red_pencil, edit_red_pencil;
	CartDataBean odbean;
	ArrayList<ProductDataBean> pdList;
	float redAmt = 0.00f, amount = 0.00f, okler_disc = 0.00f,
			shipping_charg = 0.00f, taxes = 0.00f, net_pay = 0.00f,
			coupon_disc = 0.00f;
	boolean flagbill = false, flagship = false;
	int check;
	EditText coupon_code;
	ImageLoader imgloader;
	String couponCodeUrl;
	TextView terms_n_cons_tv;
	Activity ack;
	ImageView blueuntick, bluetick, trackinImage;
	Button checkout_count;
	String ccCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_checkout_summary);
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		notifCount = (Button) toolBar.findViewById(R.id.notif_count);
		setSupportActionBar(toolBar);
		ActionBar ab = getSupportActionBar();
		ack = this;
		// ab.setDisplayHomeAsUpEnabled(true);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		check = getIntent().getIntExtra("Check", 11);
		if (check == 1) {
			odbean = Okler.getInstance().getSingleCart();
		} else {
			odbean = Okler.getInstance().getMainCart();
		}
		pdList = new ArrayList<ProductDataBean>();
		checkout_quant_RL = (RelativeLayout) findViewById(R.id.checkout_quant_RL);
		parentForCustomView = (LinearLayout) findViewById(R.id.LL_for_prods_custom_view);
		billing_RL = (RelativeLayout) findViewById(R.id.billing_RL);
		baddr_title_tv = (TextView) billing_RL.findViewById(R.id.addr_title_tv);
		baddr_tv = (TextView) billing_RL.findViewById(R.id.addr_tv);
		bedit_red_pencil = (ImageView) billing_RL
				.findViewById(R.id.edit_red_pencil);
		bedit_red_pencil.setVisibility(View.GONE);
		delivery_RL = (RelativeLayout) findViewById(R.id.delivery_RL);
		addr_title_tv = (TextView) delivery_RL.findViewById(R.id.addr_title_tv);
		addr_tv = (TextView) delivery_RL.findViewById(R.id.addr_tv);
		edit_red_pencil = (ImageView) delivery_RL
				.findViewById(R.id.edit_red_pencil);
		edit_red_pencil.setVisibility(View.GONE);
		firstnameValueTv = (TextView) findViewById(R.id.firstnameValueTv);
		surnameValueTv = (TextView) findViewById(R.id.surnameValueTv);
		mobileValueTv = (TextView) findViewById(R.id.mobileValueTv);
		emailValueTv = (TextView) findViewById(R.id.emailValueTv);
		amount_value_tv = (TextView) findViewById(R.id.amount_value_tv);
		okler_disc_value_tv = (TextView) findViewById(R.id.okler_disc_value_tv);
		coupon_disc_value_tv = (TextView) findViewById(R.id.coupon_disc_value_tv);
		apply_button = (Button) findViewById(R.id.apply_button);
		coupon_disc_RL = (RelativeLayout) findViewById(R.id.coupon_disc_RL);
		coupon_appl_text = (TextView) findViewById(R.id.coupon_appl_text);
		net_pay_value_tv = (TextView) findViewById(R.id.net_pay_value_tv);
		shipping_charg_value_tv = (TextView) findViewById(R.id.shipping_charg_value_tv);
		tax_value_tv = (TextView) findViewById(R.id.tax_value_tv);
		coupon_code = (EditText) findViewById(R.id.coupon_code);
		terms_n_cons_tv = (TextView) findViewById(R.id.terms_n_cons_tv);
		checkout_count = (Button) findViewById(R.id.checkout_count);
		blueuntick = (ImageView) findViewById(R.id.blueunTick);
		bluetick = (ImageView) findViewById(R.id.blueTick);

		blueuntick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				blueuntick.setVisibility(View.GONE);
				bluetick.setVisibility(View.VISIBLE);

			}
		});
		bluetick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				bluetick.setVisibility(View.GONE);
				blueuntick.setVisibility(View.VISIBLE);

			}
		});

		terms_n_cons_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(ProductCheckoutSummary.this,
						LegalActivity.class);
				int in = 1;
				i.putExtra("t&c", in);
				startActivity(i);
			}
		});

		imgloader = VolleyRequest.getInstance(getApplicationContext())
				.getImageLoader();
		next = (TextView) findViewById(R.id.proceed_to_pay);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (bluetick.getVisibility() == View.GONE) {
					Toast.makeText(getApplicationContext(),
							"Please accept Terms and Conditions",
							Toast.LENGTH_LONG).show();
				} else {
					if (amount >= 0 || net_pay >= 0) {
						odbean.setProdList(pdList);
						odbean.setTotalPrice(net_pay);
						odbean.setShip_charge("" + shipping_charg);
						odbean.setCoupon_disc(redAmt);
						odbean.setTax(taxes);
						odbean.setcCode(ccCode);
						if (check == 1) {
							Okler.getInstance().setSingleCart(odbean);
						} else {
							Okler.getInstance().setMainCart(odbean);
						}
						Intent intent = new Intent(getApplicationContext(),
								ProductCheckoutPaymentMode.class);
						intent.putExtra("Check", check);
						startActivity(intent);
					} else {
						Toast.makeText(getApplicationContext(),
								"Unable to proceed with your order",
								Toast.LENGTH_LONG).show();
						Intent intent = new Intent(ProductCheckoutSummary.this,
								ServiceCategoryActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						Okler.getInstance().setBookingType(100);
						startActivity(intent);
					}
				}
			}

		});
		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		// code to be copied for checkout summary for action bar and etc
		checkout_progress_summary_Iv = (ImageView) findViewById(R.id.checkout_progress_summary_Iv);
		if (Okler.getInstance().getBookingType() == 0) {
			// ab.setTitle(R.string.title_activity_allopathy);
			Utilities.setTitleText(toolBar, getString(R.string.allopathy));
			checkout_progress_summary_Iv
					.setImageResource(R.drawable.md_process_summary_image);
			checkout_quant_RL.setBackgroundColor(getResources().getColor(
					R.color.Brightyellow));

		} else if (Okler.getInstance().getBookingType() == 3) {
			// checkout_progress_summary_Iv.setImageResource(R.drawable.md_process_summary_image);
			checkout_quant_RL.setBackgroundColor(getResources().getColor(
					R.color.Brightyellow));

			Utilities.setTitleText(toolBar, getString(R.string.ayurvedic));
			// ab.setTitle(R.string.title_activity_ayurvedic);
		} else if (Okler.getInstance().getBookingType() == 4) {
			// checkout_progress_summary_Iv.setImageResource(R.drawable.md_process_summary_image);
			checkout_quant_RL.setBackgroundColor(getResources().getColor(
					R.color.Brightyellow));
			// ab.setTitle(R.string.title_activity_homeopathy);
			Utilities.setTitleText(toolBar, getString(R.string.homeopathy));

		} else {
			ab.setTitle(R.string.title_activity_health_shop_grid);
			checkout_quant_RL.setBackgroundColor(Color.parseColor("#FCAB12"));
			trackinImage = (ImageView) findViewById(R.id.checkout_progress_summary_Iv);
			trackinImage.setImageDrawable(getResources().getDrawable(
					R.drawable.hs_process_summary));
			Utilities.setTitleText(toolBar, "Health Shop");
		}
		toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler
				.getInstance().getBookingType()));
		if (check == 0) {
			toolBar.setBackgroundColor(Color.BLUE);
			checkout_quant_RL.setBackgroundColor(Color.BLUE);
			Utilities.setTitleText(toolBar, "Cart");

		}
		// end of code to be copied

		apply_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String cCode = coupon_code.getText().toString();
				// float amt =
				// Float.parseFloat(amount_value_tv.getText().toString());
				couponCodeUrl = getString(R.string.couponCodeUrlPart1) + cCode
						+ getString(R.string.couponCodeUrlPart2) + "product"
						+ getString(R.string.couponCodeUrlPart3) + amount;

				WebJsonObjectRequest cjson = new WebJsonObjectRequest(
						Method.GET, couponCodeUrl, new JSONObject(),
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								Log.d("Response", String.valueOf(response));
								JSONObject jobj = (JSONObject) response;
								try {
									if (jobj.getString("message").equals(
											"Coupon Valid")) {
										redAmt = Float.parseFloat(jobj
												.getString("reduction_amount"));
										// float net =
										// Float.parseFloat(net_pay_value_tv.getText().toString());

										if (net_pay > redAmt) {
											net_pay = net_pay - redAmt;
											// net_pay = net;
											ccCode = coupon_code.getText()
													.toString();
											;
											coupon_disc_RL
													.setVisibility(View.VISIBLE);
											net_pay_value_tv.setText("Rs."
													+ net_pay);
											coupon_disc_value_tv.setText("Rs."
													+ redAmt);
											coupon_appl_text
													.setText("The coupon has been applied to your bill.");
											coupon_appl_text
													.setVisibility(View.VISIBLE);
										} else {
											coupon_appl_text
													.setText("Invalid Coupon");
											coupon_appl_text
													.setVisibility(View.VISIBLE);
										}
									} else {
										coupon_appl_text
												.setText("Invalid Coupon");
										coupon_appl_text
												.setVisibility(View.VISIBLE);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								Log.d("Error", String.valueOf(error));
								coupon_appl_text.setText("Invalid Coupon");
								coupon_appl_text.setVisibility(View.VISIBLE);
							}
						});

				VolleyRequest.addJsonObjectRequest(getApplicationContext(),
						cjson);
				/*
				 * coupon_disc_RL.setVisibility(View.VISIBLE);
				 * coupon_appl_text.setVisibility(View.VISIBLE);
				 * net_pay_value_tv.setText("RS. 14.20");
				 */
			}
		});
		pdList = odbean.getProdList();
		int size = pdList.size();
		if (size > 9) {
			checkout_count.setText("" + size);
		} else {
			checkout_count.setText("0" + size);
		}
		String First;
		for (int i = 0; i < size; i++) {
			prodSummary[i] = new CustomViewProdCheckoutSummary(
					getApplicationContext());
			customViewRL1 = (RelativeLayout) prodSummary[i]
					.findViewById(R.id.RL_for_custom_view_prod_checkout_summary);
			NetworkImageView item_image = (NetworkImageView) prodSummary[i]
					.findViewById(R.id.item_image);
			TextView item_name = (TextView) prodSummary[i]
					.findViewById(R.id.item_name);
			TextView item_description = (TextView) prodSummary[i]
					.findViewById(R.id.item_description);
			TextView tv_MrpValue = (TextView) prodSummary[i]
					.findViewById(R.id.tv_MrpValue);
			TextView tv_youSaveValue = (TextView) prodSummary[i]
					.findViewById(R.id.tv_youSaveValue);
			Button okler_Amount = (Button) prodSummary[i]
					.findViewById(R.id.okler_Amount);
			ProductDataBean pdbean1 = new ProductDataBean();
			pdbean1 = pdList.get(i);
			item_name.setText(pdbean1.getProdName());
			String desc = "";
			if (Okler.getInstance().getBookingType() == 0) {
				String two = pdbean1.getCompany();
				if (two == null || two.equals("null") || two.equals("")) {
					two = "";
				}
				desc = two;
				
			} else {
				desc = pdbean1.getDesc();
				if (desc == null || desc.equals("null"))
					desc="";
				
			}
			item_description.setText(desc);
			tv_MrpValue.setText(pdbean1.getMrp() + "");
			tv_youSaveValue.setText(pdbean1.getDiscount() + "%");
			okler_Amount.setText(okler_Amount.getText().toString()
					+ pdbean1.getOklerPrice());
			// First=
			// "http://183.82.110.105:8081/oklerdevv2/uploads/images/medium/";
			First = "";// pdList.get(i).getThumbUrl();
			if (Okler.getInstance().getBookingType() == 0) {
				First = pdList.get(i).getClipArtUrl();
			} else {
				First = pdList.get(i).getThumbUrl();
			}
			String second = pdList.get(i).getImgUrl();
			String photo_url = First + second;

			item_image.setImageUrl(photo_url, imgloader);

			parentForCustomView.addView(prodSummary[i]);
			amount = amount + (pdbean1.getMrp() * pdbean1.getUnits());

			net_pay = net_pay + (pdbean1.getOklerPrice() * pdbean1.getUnits());
			taxes = taxes + (pdbean1.getTax() * pdbean1.getUnits());
		}
		okler_disc = (amount - net_pay);
		shipping_charg = 0;
		// taxes = 0;
		coupon_disc = 0;
		net_pay = net_pay + taxes - coupon_disc;
		if (net_pay < 500 && net_pay > 0) {
			shipping_charg = 50;
		}

		net_pay = net_pay + shipping_charg;
		amount_value_tv.setText("Rs." + amount);
		okler_disc_value_tv.setText("Rs." + okler_disc);
		coupon_disc_value_tv.setText("Rs." + coupon_disc);
		shipping_charg_value_tv.setText("Rs." + shipping_charg);
		tax_value_tv.setText("Rs." + taxes);
		net_pay_value_tv.setText("Rs." + net_pay);

		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
		ArrayList<AddressDataBean> aList = ubean.getAddDatabean();
		for (int i = 0; i < aList.size(); i++) {
			AddressDataBean abean = new AddressDataBean();
			abean = aList.get(i);
			if (abean.getDefault_billing() != 0) {
				/* if(flagbill==false){ */
				baddr_title_tv.setText("Billing Address");
				String addr = aList.get(i).getFirstname() + " "
						+ aList.get(i).getLastname() + "\n"
						+ aList.get(i).getAddress1() + ", "
						+ aList.get(i).getAddress2() + ", " + "\n"
						+ aList.get(i).getCity() + " - "
						+ aList.get(i).getZip() + ", "
						+ aList.get(i).getState() + ".";
				baddr_tv.setText(addr);
				/*
				 * flagbill=true; } else{
				 * 
				 * }
				 */
			}
			if (abean.isSelected()) {
				/* if(flagship==false){ */
				addr_title_tv.setText("Delivery Address");
				String addr = aList.get(i).getFirstname() + " "
						+ aList.get(i).getLastname() + "\n"
						+ aList.get(i).getAddress1() + ", "
						+ aList.get(i).getAddress2() + ", " + "\n"
						+ aList.get(i).getCity() + " - "
						+ aList.get(i).getZip() + ", "
						+ aList.get(i).getState() + "." + "\n"
						+ "Preferred Delivery Time: "
						+ aList.get(i).getPreferred_del_time();
				addr_tv.setText(addr);
				/*
				 * flagbill=true; } else{
				 * 
				 * }
				 */
			}
		}
		UsersDataBean ubean2 = new UsersDataBean();
		ubean2 = odbean.getCurUBean();
		if(ubean2==null)
			ubean2=ubean;
		firstnameValueTv.setText(ubean2.getFname());
		surnameValueTv.setText(ubean2.getLname());
		mobileValueTv.setText(ubean2.getPhone());
		emailValueTv.setText(ubean2.getEmail());

	}// end of onCreate

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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(notifCount, ack);
	}
}
