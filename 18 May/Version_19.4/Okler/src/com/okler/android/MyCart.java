package com.okler.android;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.Request.Method;
import com.okler.adapters.CheckoutListViewAdapter;
import com.okler.customviews.CustomViewProdMyCart;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MyCart extends BaseActivity implements Response.Listener,
		Response.ErrorListener {
	private View bottomBarLayout;
	private Toolbar toolBar;
	private Context context;
	ImageView imgBack;
	TextView mtitle_mycart, htitle_mycart;
	LinearLayout med_LL, hs_LL;
	String getMyCartUrl;
	CartDataBean mainCart;
	ArrayList<ProductDataBean> prodList;
	ProductDataBean pdbean;
	CheckoutListViewAdapter clistViewAdapter;
	LinearLayout medList, hsList;
	LinearLayout progressLinLayout;
	Button toolBarCount;
	ScrollView mainScroll;
	CustomViewProdMyCart[] rowView = new CustomViewProdMyCart[100];
	CustomViewProdMyCart[] rowViewhs = new CustomViewProdMyCart[100];
	ImageView deleteImage, image_favourite, deleteImagehs, image_favouritehs,
			red_plus_Image, red_minus_Image, red_plus_Imagehs,
			red_minus_Imagehs;
	TextView item_name, item_desc, mrp, you_save, item_namehs, item_deschs,
			mrphs, you_savehs, tv_prescriptionRequired, amount_value_tv,
			okler_disc_value_tv, shipping_charg_value_tv, tax_value_tv,
			net_pay_value_tv;
	Button okler_price, okler_pricehs, image_cart, image_carths;
	EditText units, unitshs;
	LinearLayout forMycartScreen;
	int id, mid;
	NetworkImageView item_img, item_imghs, item_img1;
	Button submit;
	Activity ack;
	ImageLoader imgloader;
	int prodId1, prodId2;
	boolean flag = false,isPlusClicked=false;
	ArrayList<ProductDataBean> setPdLisths, setPdList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_cart);
		ack = this;
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		prodList = new ArrayList<ProductDataBean>();
		toolBar.setBackgroundColor(Color.BLUE);
		context = getApplicationContext();
		setSupportActionBar(toolBar);
		final ActionBar ab = getSupportActionBar();
		// ab.setDisplayHomeAsUpEnabled(true);
		submit = (Button) findViewById(R.id.submit);
		amount_value_tv = (TextView) findViewById(R.id.amount_value_tv);
		okler_disc_value_tv = (TextView) findViewById(R.id.okler_disc_value_tv);
		shipping_charg_value_tv = (TextView) findViewById(R.id.shipping_charg_value_tv);
		tax_value_tv = (TextView) findViewById(R.id.tax_value_tv);
		net_pay_value_tv = (TextView) findViewById(R.id.net_pay_value_tv);
		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		toolBarCount = (Button) toolBar.findViewById(R.id.toolBarCount);
		med_LL = (LinearLayout) findViewById(R.id.med_LL);
		mtitle_mycart = (TextView) med_LL.findViewById(R.id.title_mycart);
		hs_LL = (LinearLayout) findViewById(R.id.hs_LL);
		htitle_mycart = (TextView) hs_LL.findViewById(R.id.title_mycart);
		medList = (LinearLayout) findViewById(R.id.medList);
		hsList = (LinearLayout) findViewById(R.id.hsList);
		mainScroll = (ScrollView) findViewById(R.id.scrollViewMyCartMenu);
		// setTouchListenerScrViews();
		progressLinLayout = (LinearLayout) findViewById(R.id.progressLinLayout);
		imgloader = VolleyRequest.getInstance(getApplicationContext())
				.getImageLoader();
		mtitle_mycart.setText("MEDICINES");
		htitle_mycart.setText("HEALTHSHOP");
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		Utilities.setTitleText(toolBar, "Cart");

		getMyCart();

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!(prodList.size() <= 0)) {
					int count1 = medList.getChildCount();
					View[] va = new View[count1];
					int id = 500;
					for (int i = 0; i < count1; i++) {
						va[i] = medList.getChildAt(i);
						EditText ed = (EditText) va[i].findViewWithTag(""+id);
						String edtext = ed.getText().toString();
						if (edtext == null || edtext.equals("")
								|| edtext.equals("00") || edtext.equals("null"))
							edtext = "00";
						if (edtext.length() > 2) {
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
							flag = false;
							break;
						} else {
							int unit = Integer.parseInt(edtext);
							if (unit <= 0) {
								Toast.makeText(ack,
										"Please Select atleast one unit",
										Toast.LENGTH_LONG).show();
								ed.requestFocus();
								flag = false;
								break;
							}

						}
						// Toast.makeText(ack, edtext,
						// Toast.LENGTH_SHORT).show();
						id++;
						flag = true;
					}
					if (count1 == 0)
						flag = true;
					if (flag) {
						int count2 = hsList.getChildCount();
						View va2[] = new View[count2];
						int id2 = 50000;
						for (int j = 0; j < count2; j++) {
							flag = false;
							va2[j] = hsList.getChildAt(j);
							EditText ed2 = (EditText) va2[j].findViewWithTag(""+id2);
							String edtext2 = ed2.getText().toString();
							if (edtext2 == null || edtext2.equals("")
									|| edtext2.equals("00")
									|| edtext2.equals("null"))
								edtext2 = "00";
							if (edtext2.length() > 2) {
								Toast.makeText(ack, "Maximum limit is 99",
										Toast.LENGTH_SHORT).show();
								flag = false;
								break;
							} else {
								int unit2 = Integer.parseInt(edtext2);
								if (unit2 <= 0) {
									Toast.makeText(ack,
											"Please Select atleast one unit",
											Toast.LENGTH_LONG).show();
									ed2.requestFocus();
									flag = false;
									break;
								}

							}
							id2++;
							flag = true;
						}
					}
					if (flag) {
						mainCart.setProdList(prodList);
						Okler.getInstance().setMainCart(mainCart);
						Intent intent = new Intent(getApplicationContext(),
								ProductCheckoutDeliveryAddress.class);
						intent.putExtra("Check", 0);
						startActivity(intent);
					}
				} else {
					Toast.makeText(ack,
							"Please add some products in your cart",
							Toast.LENGTH_LONG).show();
				}

			}
		});

	}

	public void getMyCart() {
		getMyCartUrl = getString(R.string.getUserCart);
		// getString(R.string.getMyCartUrl);
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);

		getMyCartUrl = getMyCartUrl + ubean.getId();
		ArrayList<AddressDataBean> arr = ubean.getAddDatabean();

		WebJsonObjectRequest cartjson = new WebJsonObjectRequest(Method.GET,
				getMyCartUrl, new JSONObject(), this, this);
		VolleyRequest.clearCache();
		
		if(cartjson.getCacheEntry() != null)
		if(cartjson.getCacheEntry().data != null)
		{
			Log.e("Cache data", "Cache entry data"+cartjson.getCacheEntry().data.toString());
		}
		// WebJsonObjectRequest cartjson = new WebJsonObjectRequest(Method.GET,
		// getMyCartUrl, new JSONObject(), new Response.Listener<JSONObject>() {
		cartjson.setShouldCache(false);
		if (VolleyRequest.addJsonObjectRequest(context, cartjson)) {
			showProgress(true);
		} else {
			showProgress(false);

		}

	}

	private void showProgress(boolean paramBoolean) {
		if (paramBoolean) {
			this.progressLinLayout.setVisibility(View.VISIBLE);
			return;
		}
		this.progressLinLayout.setVisibility(View.INVISIBLE);
	}

	private void setHS() {
		setPdLisths = new ArrayList<ProductDataBean>();
		ProductDataBean pdBean = new ProductDataBean();
		int size = prodList.size();
		RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params3.setMargins(0, 7, 0, 0);
		for (int i = 0; i < size; i++) {
			pdBean = prodList.get(i);
			if (pdBean.getProdType() == 1) {
				setPdLisths.add(pdBean);
			}
		}

		int size1 = setPdLisths.size();
		if (size1 <= 0) {
			hs_LL.setVisibility(View.GONE);
		}
		for (int i = 0; i < size1; i++) {
			pdBean = setPdLisths.get(i);
			rowViewhs[i] = new CustomViewProdMyCart(getApplicationContext());
			item_imghs = (NetworkImageView) rowViewhs[i]
					.findViewById(R.id.item_image);
			item_namehs = (TextView) rowViewhs[i].findViewById(R.id.item_name);
			item_deschs = (TextView) rowViewhs[i]
					.findViewById(R.id.item_description);
			mrphs = (TextView) rowViewhs[i].findViewById(R.id.tv_MrpValue);
			you_savehs = (TextView) rowViewhs[i]
					.findViewById(R.id.tv_youSaveValue);
			okler_pricehs = (Button) rowViewhs[i]
					.findViewById(R.id.okler_Amount);
			RelativeLayout unitValueRlhs = (RelativeLayout) rowViewhs[i]
					.findViewById(R.id.unitValueRl);
			unitshs = (EditText) rowViewhs[i].findViewById(R.id.unitValue);
			int t2 = i + 50000;
			unitshs.setTag(t2 + "");
			//unitshs.setId(i + 50000);
			unitshs.addTextChangedListener(new GenericTextWatcher(unitshs));
			// myTextWatcher(unitshs);
			deleteImagehs = (ImageView) rowViewhs[i]
					.findViewById(R.id.deleteImage);
			deleteImagehs.setTag("" + i);
			image_favouritehs = (ImageView) rowViewhs[i]
					.findViewById(R.id.image_favourite);
			image_favouritehs.setVisibility(View.GONE);
			image_carths = (Button) rowViewhs[i].findViewById(R.id.image_cart);
			image_carths.setVisibility(View.GONE);
			red_plus_Imagehs = (ImageView) rowViewhs[i]
					.findViewById(R.id.red_plus_Image);
			int t = i + 100;
			red_plus_Imagehs.setTag(t + "");
			red_plus_Imagehs.setLayoutParams(params3);
			red_minus_Imagehs = (ImageView) rowViewhs[i]
					.findViewById(R.id.red_minus_Image);
			int t1 = i + 1000;
			red_minus_Imagehs.setTag(t1 + "");
			RelativeLayout red_minus_ImageRlhs = (RelativeLayout) rowViewhs[i]
					.findViewById(R.id.red_minus_ImageRl);
			red_minus_ImageRlhs.setTag(t1 + "");
			RelativeLayout red_plus_ImageRlhs = (RelativeLayout) rowViewhs[i]
					.findViewById(R.id.red_plus_ImageRl);
			red_plus_ImageRlhs.setTag(t + "");

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.RIGHT_OF, unitshs.getId());
			red_plus_Imagehs.setLayoutParams(params);
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params2.setMargins(0, 0, 0, 8);
			red_minus_Imagehs.setLayoutParams(params2);
			final RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params4.setMargins(5, 0, 5, 0);
			params4.addRule(RelativeLayout.CENTER_IN_PARENT);

			item_namehs.setText(pdBean.getProdName());
			String desc = pdBean.getDesc();
			item_deschs.setText(desc);
			mrphs.setText("" + pdBean.getMrp());
			you_savehs.setText(pdBean.getDiscount() + "%");
			okler_pricehs.setText(okler_pricehs.getText().toString()
					+ pdBean.getOklerPrice());
			int u = pdBean.getUnits();
			if (u > 9) {
				unitshs.setText("" + pdBean.getUnits());
			} else {
				unitshs.setText("0" + pdBean.getUnits());
			}

			// String First=
			// "http://183.82.110.105:8081/oklerdevv2/uploads/images/medium/";
			String First = pdBean.getThumbUrl();
			String second = pdBean.getImgUrl();
			String photo_url = First + second;

			item_imghs.setImageUrl(photo_url, imgloader);
			hsList.addView(rowViewhs[i]);

			int count = prodList.size();
			if (count > 9) {
				toolBarCount.setText("" + count);
			} else {
				toolBarCount.setText("0" + count);
			}

			deleteImagehs.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String i = String.valueOf(v.getTag());
					// Toast.makeText(getApplicationContext(), "onClick",
					// Toast.LENGTH_SHORT).show();
					id = Integer.parseInt(i);
					// temp id
					UsersDataBean ubean = Utilities
							.getCurrentUserFromSharedPref(ack);
					int userId = ubean.getId();
					ProductDataBean pbean = new ProductDataBean();
					pbean = setPdLisths.get(id);
					prodId1 = pbean.getProdId();
					String remove1, remove2, deleteFromCartUrl;
					remove1 = getString(R.string.delete_cart_url);
					remove2 = getString(R.string.product_id);
					deleteFromCartUrl = remove1 + userId + remove2 + prodId1;
					WebJsonObjectRequest deljson = new WebJsonObjectRequest(
							Method.GET, deleteFromCartUrl, new JSONObject(),
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									// Toast.makeText(getApplicationContext(),
									// "onResponse", Toast.LENGTH_SHORT).show();
									JSONObject jobj = (JSONObject) response;
									try {
										int id = 0;
										showProgress(false);
										String msg = jobj.getString("message");
										if (msg.equals("item in your cart are deleted successfully.")) {
											for (int i = 0; i < prodList.size(); i++) {
												ProductDataBean pbean = new ProductDataBean();
												pbean = prodList.get(i);
												int prodId = pbean.getProdId();
												if (prodId == prodId1) {
													id = i;
													break;
												}
											}

											prodList.remove(id);
											int i1 = prodList.size();
											mainCart.setProdList(prodList);
											medList.removeAllViews();
											hsList.removeAllViews();
											getMyCart();
											setAmount();
										} else {
											Toast.makeText(
													getApplicationContext(),
													"Some Error Ocurred."
															+ "\n"
															+ "Item not Deleted.",
													Toast.LENGTH_SHORT).show();
										}
									} catch (JSONException e) {
										// Toast.makeText(getApplicationContext(),
										// String.valueOf(e),
										// Toast.LENGTH_SHORT).show();
										e.printStackTrace();
									}

								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									// Toast.makeText(getApplicationContext(),
									// String.valueOf(error),
									// Toast.LENGTH_SHORT).show();
									showProgress(false);
								}
							});
					if (VolleyRequest.addJsonObjectRequest(
							getApplicationContext(), deljson)) {
						showProgress(true);
					} else {
						showProgress(false);

					}

				}
			});
			try {

				red_plus_Imagehs.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Utilities.writeToLogFIle("Cart: Hs: redplusimagehs");
						String i = String.valueOf(v.getTag());
						Utilities.writeToLogFIle("Cart: Hs: i" + i);
						int id = Integer.parseInt(i);
						Utilities.writeToLogFIle("Cart: Hs: id" + id);
						id = id - 100;
						Utilities.writeToLogFIle("Cart: Hs: id" + id);
						View vv = (CustomViewProdMyCart)hsList.getChildAt(id);
						int id1 = id + 50000;
						EditText edd1 = (EditText)vv.findViewWithTag(""+id1);
						
						
						Utilities.writeToLogFIle("Cart: Hs: id1" + id1);
						ViewParent ll = v.getParent();
						ViewParent l2 = ll.getParent();
						RelativeLayout rl = (RelativeLayout) l2;
						Utilities.writeToLogFIle("Cart: Hs: ll");
						//EditText edd1 = (EditText) rl.findViewById(id1);
						//setEditTextMaxLength(2, edd1);
						//edd1.setId(id1);
						Utilities.writeToLogFIle("Cart: Hs: edd1 "
								+ edd1.getText().toString());
						if (edd1.getText().toString().trim().equals("")) {
							Utilities.writeToLogFIle("Cart: Hs: edd2 "
									+ edd1.getText().toString());
							edd1.setText("00");
							Utilities.writeToLogFIle("Cart: Hs: edd3 "
									+ edd1.getText().toString());
						}
						String uni = edd1.getText().toString();
						if (uni.length() > 2) {
							edd1.setText("");
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						} else {
							int unit = Integer.parseInt(edd1.getText()
									.toString());
							if (unit >= 99) {
								Toast.makeText(ack, "Maximum limit is 99",
										Toast.LENGTH_SHORT).show();
							} else {

								Utilities.writeToLogFIle("Cart: Hs: unit "
										+ unit);
								unit++;
								Utilities.writeToLogFIle("Cart: Hs: unit++"
										+ unit);
								int prodid = setPdLisths.get(id).getProdId();
								for (int j = 0; j < prodList.size(); j++) {
									ProductDataBean pbean = new ProductDataBean();
									pbean = prodList.get(j);
									int pid = pbean.getProdId();
									if (pid == prodid) {
										prodList.get(j).setUnits(unit);
										break;
									}
								}

								Utilities
										.writeToLogFIle("Cart: Hs: prodlist setUnit");
								if (unit > 9) {
									edd1.setText("" + unit);
								} else {
									edd1.setText("0" + unit);
								}
								//edd1.addTextChangedListener(new GenericTextWatcher(
								//		edd1));
								Utilities
										.writeToLogFIle("Cart: Hs: after settext"
												+ unit);
								edd1.setLayoutParams(params4);
								Utilities.writeToLogFIle("Cart: Hs: params4");
								edd1.setBackgroundColor(getResources()
										.getColor(R.color.White));
								Utilities.writeToLogFIle("Cart: Hs: white");
								int i1 = prodList.size();
								Utilities.writeToLogFIle("Cart: Hs: size" + i1);
								mainCart.setProdList(prodList);

								Okler.getInstance().setMainCart(mainCart);
								Utilities.writeToLogFIle("Cart: Hs: instance");
								// medList.removeAllViews();
								// hsList.removeAllViews();
								// setMeds();
								// setHS();
								isPlusClicked=true;
								 setAmount();
								Utilities.writeToLogFIle("Cart: Hs: amount");
							}
						}
					}
				});

				red_plus_ImageRlhs.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Utilities.writeToLogFIle("Cart: Hs: redplusimageRLhs");
						String i = String.valueOf(v.getTag());
						Utilities.writeToLogFIle("Cart: Hs: i" + i);
						int id = Integer.parseInt(i);
						Utilities.writeToLogFIle("Cart: Hs: id " + id);
						id = id - 100;
						Utilities.writeToLogFIle("Cart: Hs: id2 " + id);
						int id1 = id + 50000;
						View vv = (CustomViewProdMyCart)hsList.getChildAt(id);
						
						EditText edd2 = (EditText)vv.findViewWithTag(""+id1);
						Utilities.writeToLogFIle("Cart: Hs: id1" + id1);
						ViewParent ll = v.getParent();
						ViewParent l2 = ll.getParent();
						RelativeLayout rl = (RelativeLayout) l2;
						Utilities.writeToLogFIle("Cart: Hs: ll");
						//EditText edd2 = (EditText) rl.findViewById(id1);
						//edd2.setId(id1);
						//setEditTextMaxLength(2, edd2);
						Utilities.writeToLogFIle("Cart: Hs: edd1"
								+ edd2.getText().toString());
						if (edd2.getText().toString().trim().equals("")) {
							Utilities.writeToLogFIle("Cart: Hs: edd2"
									+ edd2.getText().toString());
							edd2.setText("00");
							Utilities.writeToLogFIle("Cart: Hs: edd2"
									+ edd2.getText().toString());
						}
						String uni = edd2.getText().toString();
						if (uni.length() > 2) {
							edd2.setText("");
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						} else {
							int unit = Integer.parseInt(edd2.getText()
									.toString());
							if (unit >= 99) {
								Toast.makeText(ack, "Maximum limit is 99",
										Toast.LENGTH_SHORT).show();
							} else {
								Utilities.writeToLogFIle("Cart: Hs: unit"
										+ unit);
								unit++;
								Utilities.writeToLogFIle("Cart: Hs: unit++"
										+ unit);
								int prodid = setPdLisths.get(id).getProdId();
								for (int j = 0; j < prodList.size(); j++) {
									ProductDataBean pbean = new ProductDataBean();
									pbean = prodList.get(j);
									int pid = pbean.getProdId();
									if (pid == prodid) {
										prodList.get(j).setUnits(unit);
										break;
									}
								}
								Utilities
										.writeToLogFIle("Cart: Hs: prodlistsetunit");
								if (unit > 9) {
									edd2.setText("" + unit);
								} else {
									edd2.setText("0" + unit);
								}
								//edd2.addTextChangedListener(new GenericTextWatcher(
								//		edd2));
								Utilities
										.writeToLogFIle("Cart: Hs: after set text");
								edd2.setLayoutParams(params4);
								Utilities.writeToLogFIle("Cart: Hs: params");
								edd2.setBackgroundColor(getResources()
										.getColor(R.color.White));
								Utilities.writeToLogFIle("Cart: Hs: white");
								int i1 = prodList.size();
								Utilities.writeToLogFIle("Cart: Hs: size" + i1);
								mainCart.setProdList(prodList);
								Utilities.writeToLogFIle("Cart: Hs: setlist");
								Okler.getInstance().setMainCart(mainCart);
								// medList.removeAllViews();
								// hsList.removeAllViews();
								// setMeds();
								// setHS();
								isPlusClicked=true;
								setAmount();
								Utilities.writeToLogFIle("Cart: Hs: amount");
							}
						}
					}
				});
				red_minus_Imagehs.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Utilities.writeToLogFIle("Cart: Hs: redminusImage");
						String i = String.valueOf(v.getTag());
						Utilities.writeToLogFIle("Cart: Hs: i" + i);
						int id = Integer.parseInt(i);
						Utilities.writeToLogFIle("Cart: Hs: id" + id);
						id = id - 1000;
						Utilities.writeToLogFIle("Cart: Hs: id2" + id);
						int id1 = id + 50000;
						View vv = (CustomViewProdMyCart)hsList.getChildAt(id);
						EditText edd3 = (EditText)vv.findViewWithTag(""+id1);
						Utilities.writeToLogFIle("Cart: Hs: id1" + id1);
						ViewParent ll = v.getParent();
						ViewParent l2 = ll.getParent();
						RelativeLayout rl = (RelativeLayout) l2;
						Utilities.writeToLogFIle("Cart: Hs: ll");
						
						//EditText edd3 = (EditText) rl.findViewById(id1);
						//edd3.setId(id1);
						//setEditTextMaxLength(2, edd3);
						Utilities.writeToLogFIle("Cart: Hs: edd1"
								+ edd3.getText().toString());
						if (edd3.getText().toString().trim().equals("")) {
							Utilities.writeToLogFIle("Cart: Hs: edd2"
									+ edd3.getText().toString());
							edd3.setText("00");
							Utilities.writeToLogFIle("Cart: Hs: edd3"
									+ edd3.getText().toString());
						}
						String uni = edd3.getText().toString();
						if (uni.length() > 2) {
							edd3.setText("");
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						} else {
							int unit = Integer.parseInt(edd3.getText()
									.toString());
							Utilities.writeToLogFIle("Cart: Hs: unit" + unit);
							if (unit <= 1) {
								unit = 1;
								Utilities.writeToLogFIle("Cart: Hs: unit"
										+ unit);
							} else {
								unit--;
								Utilities.writeToLogFIle("Cart: Hs: unit"
										+ unit);
							}
							int prodid = setPdLisths.get(id).getProdId();
							for (int j = 0; j < prodList.size(); j++) {
								ProductDataBean pbean = new ProductDataBean();
								pbean = prodList.get(j);
								int pid = pbean.getProdId();
								if (pid == prodid) {
									prodList.get(j).setUnits(unit);
									break;
								}
							}
							Utilities
									.writeToLogFIle("Cart: Hs: prodlistsetunits");
							int i1 = prodList.size();
							Utilities.writeToLogFIle("Cart: Hs: size" + i1);
							if (unit > 9) {
								edd3.setText("" + unit);
							} else {
								edd3.setText("0" + unit);
							}
							//edd3.addTextChangedListener(new GenericTextWatcher(
							//		edd3));
							Utilities.writeToLogFIle("Cart: Hs: aftersetText");
							edd3.setLayoutParams(params4);
							Utilities.writeToLogFIle("Cart: Hs: params");
							edd3.setBackgroundColor(getResources().getColor(
									R.color.White));
							Utilities.writeToLogFIle("Cart: Hs: white");
							mainCart.setProdList(prodList);
							Utilities.writeToLogFIle("Cart: Hs: setlist");
							Okler.getInstance().setMainCart(mainCart);
							Utilities.writeToLogFIle("Cart: Hs: instance");
							// medList.removeAllViews();
							// hsList.removeAllViews();
							// setMeds();
							// setHS();
							isPlusClicked=true;
							setAmount();
							Utilities.writeToLogFIle("Cart: Hs: amount");
						}
					}
				});

				red_minus_ImageRlhs.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Utilities.writeToLogFIle("Cart: Hs: redminusImageRLhs");
						String i = String.valueOf(v.getTag());
						Utilities.writeToLogFIle("Cart: Hs: i" + i);
						int id = Integer.parseInt(i);
						Utilities.writeToLogFIle("Cart: Hs: id" + id);
						id = id - 1000;
						Utilities.writeToLogFIle("Cart: Hs: id2" + id);
						int id1 = id + 50000;
						View vv = (CustomViewProdMyCart)hsList.getChildAt(id);
						EditText edd4 = (EditText)vv.findViewWithTag(""+id1);
						Utilities.writeToLogFIle("Cart: Hs: id1" + id1);
						ViewParent ll = v.getParent();
						ViewParent l2 = ll.getParent();
						RelativeLayout rl = (RelativeLayout) l2;
						Utilities.writeToLogFIle("Cart: Hs: ll");
						//EditText edd4 = (EditText) rl.findViewById(id1);
						//edd4.setId(id1);
						//setEditTextMaxLength(2, edd4);
						Utilities.writeToLogFIle("Cart: Hs: edd1"
								+ edd4.getText().toString());
						if (edd4.getText().toString().trim().equals("")) {
							Utilities.writeToLogFIle("Cart: Hs: edd2"
									+ edd4.getText().toString());
							edd4.setText("00");
							Utilities.writeToLogFIle("Cart: Hs: edd3"
									+ edd4.getText().toString());
						}
						String uni = edd4.getText().toString();
						if (uni.length() > 2) {
							edd4.setText("");
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						} else {
							int unit = Integer.parseInt(edd4.getText()
									.toString());
							Utilities.writeToLogFIle("Cart: Hs: unit" + unit);
							if (unit <= 1) {
								unit = 1;
								Utilities.writeToLogFIle("Cart: Hs: unit"
										+ unit);
							} else {
								unit--;
								Utilities.writeToLogFIle("Cart: Hs: unit"
										+ unit);
							}
							// myTextWatcher(edd4);
							int prodid = setPdLisths.get(id).getProdId();
							for (int j = 0; j < prodList.size(); j++) {
								ProductDataBean pbean = new ProductDataBean();
								pbean = prodList.get(j);
								int pid = pbean.getProdId();
								if (pid == prodid) {
									prodList.get(j).setUnits(unit);
									break;
								}
							}
							Utilities
									.writeToLogFIle("Cart: Hs: prodlistsetunits");
							int i1 = prodList.size();
							Utilities.writeToLogFIle("Cart: Hs: size" + i1);
							if (unit > 9) {
								edd4.setText("" + unit);
							} else {
								edd4.setText("0" + unit);
							}
							//edd4.addTextChangedListener(new GenericTextWatcher(
							//		edd4));
							Utilities.writeToLogFIle("Cart: Hs: aftersetText");
							edd4.setLayoutParams(params4);
							Utilities.writeToLogFIle("Cart: Hs: params");
							edd4.setBackgroundColor(getResources().getColor(
									R.color.White));
							Utilities.writeToLogFIle("Cart: Hs: white");
							mainCart.setProdList(prodList);
							Utilities.writeToLogFIle("Cart: Hs: setprodlist");
							Okler.getInstance().setMainCart(mainCart);
							Utilities.writeToLogFIle("Cart: Hs: instance");
							// medList.removeAllViews();
							// hsList.removeAllViews();
							// setMeds();
							// setHS();
							isPlusClicked=true;
							setAmount();
							Utilities.writeToLogFIle("Cart: Hs: amount");
						}
					}
				});
			} catch (Exception e) {
				Utilities.writeToLogFIle("Exception Cart HS "
						+ e.getStackTrace());
				// TODO: handle exception
			}

		}

	}

	private void setMeds() {
		setPdList = new ArrayList<ProductDataBean>();
		ProductDataBean pdBean = new ProductDataBean();
		int size = prodList.size();

		for (int i = 0; i < size; i++) {
			pdBean = prodList.get(i);
			if (pdBean.getProdType() != 1) {
				setPdList.add(pdBean);
			}
		}
		int size2 = setPdList.size();

		if (size2 <= 0) {
			med_LL.setVisibility(View.GONE);
		}
		for (int i = 0; i < size2; i++) {
			pdBean = setPdList.get(i);
			rowView[i] = new CustomViewProdMyCart(getApplicationContext());
			item_img1 = (NetworkImageView) rowView[i]
					.findViewById(R.id.item_image);
			item_name = (TextView) rowView[i].findViewById(R.id.item_name);
			item_desc = (TextView) rowView[i]
					.findViewById(R.id.item_description);
			mrp = (TextView) rowView[i].findViewById(R.id.tv_MrpValue);
			you_save = (TextView) rowView[i].findViewById(R.id.tv_youSaveValue);
			okler_price = (Button) rowView[i].findViewById(R.id.okler_Amount);
			RelativeLayout unitValueRl = (RelativeLayout) rowView[i]
					.findViewById(R.id.unitValueRl);

			units = (EditText) rowView[i].findViewById(R.id.unitValue);
			int tg = i + 500;
			units.setTag("" + tg);
			//units.setId(i + 500);
			units.addTextChangedListener(new GenericTextWatcher(units));
			deleteImage = (ImageView) rowView[i].findViewById(R.id.deleteImage);
			deleteImage.setTag("" + i);
			image_favourite = (ImageView) rowView[i]
					.findViewById(R.id.image_favourite);
			image_favourite.setVisibility(View.GONE);
			image_cart = (Button) rowView[i].findViewById(R.id.image_cart);
			image_cart.setVisibility(View.GONE);

			red_plus_Image = (ImageView) rowView[i]
					.findViewById(R.id.red_plus_Image);
			int t = i + 100;
			red_plus_Image.setTag(t + "");
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.RIGHT_OF, units.getId());
			red_plus_Image.setLayoutParams(params);
			red_minus_Image = (ImageView) rowView[i]
					.findViewById(R.id.red_minus_Image);
			int t1 = i + 1000;
			red_minus_Image.setTag(t1 + "");
			RelativeLayout red_minus_ImageRl = (RelativeLayout) rowView[i]
					.findViewById(R.id.red_minus_ImageRl);
			red_minus_ImageRl.setTag(t1 + "");
			RelativeLayout red_plus_ImageRl = (RelativeLayout) rowView[i]
					.findViewById(R.id.red_plus_ImageRl);
			red_plus_ImageRl.setTag(t + "");

			final RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params4.setMargins(5, 0, 5, 0);
			params4.addRule(RelativeLayout.CENTER_IN_PARENT);

			item_name.setText(pdBean.getProdName());
			int pro_type = pdBean.getProdType();
			String desc = "";
			if (pro_type == 0) {
				String two = pdBean.getCompany();
				if (two == null || two.equals("null") || two.equals("")) {
					two = "";
				}
				desc = two;
			} else
				desc = pdBean.getDesc();
			if (desc == null || desc.equals("null"))
				desc="";
				item_desc.setText(desc);
			mrp.setText("" + pdBean.getMrp());
			you_save.setText(pdBean.getDiscount() + "%");
			okler_price.setText(okler_price.getText().toString()
					+ pdBean.getOklerPrice());
			int uni = pdBean.getUnits();
			if (uni > 9)
				units.setText("" + pdBean.getUnits());
			else
				units.setText("0" + pdBean.getUnits());
			// String First=
			// "http://183.82.110.105:8081/oklerdevv2/uploads/images/medium/";
			String First = "";
			if (pdBean.getProdType() == 0)
				First = pdBean.getClipArtUrl();
			else
				First = pdBean.getThumbUrl();
			String second = pdBean.getImgUrl();
			String photo_url = First + second;
			item_img1.setImageUrl(photo_url, imgloader);
			medList.addView(rowView[i]);

			int count = prodList.size();
			if (count > 9) {
				toolBarCount.setText("" + count);
			} else {
				toolBarCount.setText("0" + count);
			}

			deleteImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String i = String.valueOf(v.getTag());
					mid = Integer.parseInt(i);
					UsersDataBean ubean = Utilities
							.getCurrentUserFromSharedPref(ack);
					int userId = ubean.getId();
					ProductDataBean pbean = new ProductDataBean();

					pbean = setPdList.get(mid);
					prodId2 = pbean.getProdId();
					String remove1, remove2, deleteFromCartUrl;
					remove1 = getString(R.string.delete_cart_url);
					remove2 = getString(R.string.product_id);
					deleteFromCartUrl = remove1 + userId + remove2 + prodId2;
					WebJsonObjectRequest deljson = new WebJsonObjectRequest(
							Method.GET, deleteFromCartUrl, new JSONObject(),
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									showProgress(false);
									int id = 0;
									JSONObject jobj = (JSONObject) response;
									try {
										String msg = jobj.getString("message");
										if (msg.equals("item in your cart are deleted successfully.")) {

											for (int i = 0; i < prodList.size(); i++) {
												ProductDataBean pbean = new ProductDataBean();
												pbean = prodList.get(i);
												int prodId = pbean.getProdId();
												if (prodId == prodId2) {
													id = i;
													break;
												}
											}
											prodList.remove(id);
											int i1 = prodList.size();
											mainCart.setProdList(prodList);
											medList.removeAllViews();
											hsList.removeAllViews();
											if (i1 <= 0) {
												setAmount();
												toolBarCount.setText("00");
											} else {
												getMyCart();
												setAmount();
											}

										} else {
											// Toast.makeText(getApplicationContext(),
											// "Some Error Ocurred."+"\n"+"Item not Deleted.",
											// Toast.LENGTH_SHORT).show();
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									// Toast.makeText(getApplicationContext(),
									// "Error prod deleted",
									// Toast.LENGTH_SHORT).show();
									showProgress(false);
								}

							});

					if (VolleyRequest.addJsonObjectRequest(
							getApplicationContext(), deljson)) {
						showProgress(true);
					} else {
						showProgress(false);

					}
					/*
					 * setMeds(); setHS();
					 */
				}
			});
			try {

				red_plus_Image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Utilities.writeToLogFIle("Cart: Meds: redPlusImage");
						String i = String.valueOf(v.getTag());
						Utilities.writeToLogFIle("Cart: Meds: i" + i);
						int id = Integer.parseInt(i);
						Utilities.writeToLogFIle("Cart: Meds: id" + id);
						id = id - 100;
						Utilities.writeToLogFIle("Cart: Meds: id2" + id);
						int id1 = id + 500;
						View vv = (CustomViewProdMyCart)medList.getChildAt(id);
						EditText edd5 = (EditText)vv.findViewWithTag(""+id1);
						Utilities.writeToLogFIle("Cart: Meds: id1" + id1);
						ViewParent ll = v.getParent();
						ViewParent l2 = ll.getParent();
						RelativeLayout rl = (RelativeLayout) l2;
						View v1 = v.getRootView();
						Utilities.writeToLogFIle("Cart: Meds: ll");
						//EditText edd5 = (EditText) rl.findViewById(id1);
						//edd5.setId(id1);
						//setEditTextMaxLength(2, edd5);
						Utilities.writeToLogFIle("Cart: Meds: edd1"
								+ edd5.getText().toString());
						if (edd5.getText().toString().trim().equals("")) {
							Utilities.writeToLogFIle("Cart: Meds: edd2"
									+ edd5.getText().toString());
							edd5.setText("00");
							Utilities.writeToLogFIle("Cart: Meds: edd3"
									+ edd5.getText().toString());
						}
						String uni = edd5.getText().toString();
						if (uni.length() > 2) {
							edd5.setText("");
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						} else {
							int unit = Integer.parseInt(edd5.getText()
									.toString());
							if (unit >= 99) {
								Toast.makeText(ack, "Maximum limit is 99",
										Toast.LENGTH_SHORT).show();
							} else {

								Utilities.writeToLogFIle("Cart: Meds: unit"
										+ unit);
								unit++;
								Utilities.writeToLogFIle("Cart: Meds: unit++"
										+ unit);
								int prodid = setPdList.get(id).getProdId();
								for (int j = 0; j < prodList.size(); j++) {
									ProductDataBean pbean = new ProductDataBean();
									pbean = prodList.get(j);
									int pid = pbean.getProdId();
									if (pid == prodid) {
										prodList.get(j).setUnits(unit);
										break;
									}
								}
								Utilities
										.writeToLogFIle("Cart: Meds: prodlistsetunits");
								if (unit > 9) {
									edd5.setText("" + unit);
								} else {
									edd5.setText("0" + unit);
								}
								//edd5.addTextChangedListener(new GenericTextWatcher(
								//		edd5));
								Utilities
										.writeToLogFIle("Cart: Meds: aftersettext");
								edd5.setLayoutParams(params4);
								Utilities.writeToLogFIle("Cart: Meds: params");
								edd5.setBackgroundColor(getResources()
										.getColor(R.color.White));
								Utilities.writeToLogFIle("Cart: Meds: white");
								// edd.setGravity(Gravity.CENTER_HORIZONTAL);
								int i1 = prodList.size();
								Utilities.writeToLogFIle("Cart: Meds: size"
										+ i1);
								mainCart.setProdList(prodList);
								Utilities.writeToLogFIle("Cart: Meds: setlist");
								Okler.getInstance().setMainCart(mainCart);
								Utilities
										.writeToLogFIle("Cart: Meds: instance");
								// medList.removeAllViews();
								// hsList.removeAllViews();
								// setMeds();
								// setHS();
								isPlusClicked=true;
								setAmount();
								Utilities.writeToLogFIle("Cart: Meds: amount");
							}
						}
					}
				});
				red_plus_ImageRl.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Utilities.writeToLogFIle("Cart: Meds: redplusimageRL");
						String i = String.valueOf(v.getTag());
						Utilities.writeToLogFIle("Cart: Meds: i" + i);
						int id = Integer.parseInt(i);
						Utilities.writeToLogFIle("Cart: Meds: id" + id);
						id = id - 100;
						Utilities.writeToLogFIle("Cart: Meds: id2" + id);
						int id1 = id + 500;
						View vv = (CustomViewProdMyCart)medList.getChildAt(id);
						EditText edd6 = (EditText)vv.findViewWithTag(""+id1);
						Utilities.writeToLogFIle("Cart: Meds: id1" + id1);
						ViewParent ll = v.getParent();
						ViewParent l2 = ll.getParent();

						RelativeLayout rl = (RelativeLayout) l2;
						View v1 = v.getRootView();
						Utilities.writeToLogFIle("Cart: Meds: ll");
						//EditText edd6 = (EditText) rl.findViewById(id1);
						//edd6.setId(id1);
						//setEditTextMaxLength(2, edd6);
						Utilities.writeToLogFIle("Cart: Meds: edd1"
								+ edd6.getText().toString());
						if (edd6.getText().toString().trim().equals("")) {
							Utilities.writeToLogFIle("Cart: Meds: edd2"
									+ edd6.getText().toString());
							edd6.setText("00");
							Utilities.writeToLogFIle("Cart: Meds: edd3"
									+ edd6.getText().toString());
						}
						String uni = edd6.getText().toString();
						if (uni.length() > 2) {
							edd6.setText("");
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						} else {
							int unit = Integer.parseInt(edd6.getText()
									.toString());
							if (unit >= 99) {
								Toast.makeText(ack, "Maximum limit is 99",
										Toast.LENGTH_SHORT).show();
							} else {

								Utilities.writeToLogFIle("Cart: Meds: unit"
										+ unit);
								unit++;
								Utilities.writeToLogFIle("Cart: Meds: unit++"
										+ unit);
								int prodid = setPdList.get(id).getProdId();
								for (int j = 0; j < prodList.size(); j++) {
									ProductDataBean pbean = new ProductDataBean();
									pbean = prodList.get(j);
									int pid = pbean.getProdId();
									if (pid == prodid) {
										prodList.get(j).setUnits(unit);
										break;
									}
								}
								Utilities
										.writeToLogFIle("Cart: Meds: setunits");
								if (unit > 9) {
									edd6.setText("" + unit);
								} else {
									edd6.setText("0" + unit);
								}
								//edd6.addTextChangedListener(new GenericTextWatcher(
								//		edd6));
								Utilities
										.writeToLogFIle("Cart: Meds: aftersettext");
								edd6.setLayoutParams(params4);
								Utilities.writeToLogFIle("Cart: Meds: params");
								edd6.setBackgroundColor(getResources()
										.getColor(R.color.White));
								Utilities.writeToLogFIle("Cart: Meds: white");
								int i1 = prodList.size();
								Utilities.writeToLogFIle("Cart: Meds: size"
										+ i1);
								mainCart.setProdList(prodList);
								Utilities.writeToLogFIle("Cart: Meds: setlist");
								Okler.getInstance().setMainCart(mainCart);
								Utilities
										.writeToLogFIle("Cart: Meds: instance");
								// medList.removeAllViews();
								// hsList.removeAllViews();
								// setMeds();
								// setHS();
								isPlusClicked=true;
								setAmount();
								Utilities.writeToLogFIle("Cart: Meds: amount");
							}
						}
					}
				});

				red_minus_Image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Utilities.writeToLogFIle("Cart: Meds: redminusimage");
						String i = String.valueOf(v.getTag());
						Utilities.writeToLogFIle("Cart: Meds: i" + i);
						int id = Integer.parseInt(i);
						Utilities.writeToLogFIle("Cart: Meds: id" + id);
						id = id - 1000;
						Utilities.writeToLogFIle("Cart: Meds: id2" + id);
						int id1 = id + 500;
						View vv = (CustomViewProdMyCart)medList.getChildAt(id);
						EditText edd7 = (EditText)vv.findViewWithTag(""+id1);
						Utilities.writeToLogFIle("Cart: Meds: id1" + id1);
						ViewParent ll = v.getParent();
						ViewParent l2 = ll.getParent();
						RelativeLayout rl = (RelativeLayout) l2;
						Utilities.writeToLogFIle("Cart: Meds: ll");
						//EditText edd7 = (EditText) rl.findViewById(id1);
						//edd7.setId(id1);
						//setEditTextMaxLength(2, edd7);
						Utilities.writeToLogFIle("Cart: Meds: edd1"
								+ edd7.getText().toString());
						if (edd7.getText().toString().trim().equals("")) {
							Utilities.writeToLogFIle("Cart: Meds: edd2"
									+ edd7.getText().toString());
							edd7.setText("00");
							Utilities.writeToLogFIle("Cart: Meds: edd3"
									+ edd7.getText().toString());
						}
						String uni = edd7.getText().toString();
						if (uni.length() > 2) {
							edd7.setText("");
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						} else {
							int unit = Integer.parseInt(edd7.getText()
									.toString());
							Utilities.writeToLogFIle("Cart: Meds: unit" + unit);
							if (unit <= 1) {
								unit = 1;
								Utilities.writeToLogFIle("Cart: Meds: unit"
										+ unit);
							} else {
								unit--;
								Utilities.writeToLogFIle("Cart: Meds: unit--"
										+ unit);
							}
							int prodid = setPdList.get(id).getProdId();
							for (int j = 0; j < prodList.size(); j++) {
								ProductDataBean pbean = new ProductDataBean();
								pbean = prodList.get(j);
								int pid = pbean.getProdId();
								if (pid == prodid) {
									prodList.get(j).setUnits(unit);
									break;
								}
							}
							Utilities.writeToLogFIle("Cart: Meds: setunits");
							int i1 = prodList.size();
							Utilities.writeToLogFIle("Cart: Meds: size" + i1);
							if (unit > 9) {
								edd7.setText("" + unit);
							} else {
								edd7.setText("0" + unit);
							}
							//edd7.addTextChangedListener(new GenericTextWatcher(
							//		edd7));
							Utilities
									.writeToLogFIle("Cart: Meds: aftersettext");
							edd7.setLayoutParams(params4);
							Utilities.writeToLogFIle("Cart: Meds: params");
							edd7.setBackgroundColor(getResources().getColor(
									R.color.White));
							Utilities.writeToLogFIle("Cart: Meds: white");
							mainCart.setProdList(prodList);
							Utilities.writeToLogFIle("Cart: Meds: setlist");
							Okler.getInstance().setMainCart(mainCart);
							Utilities.writeToLogFIle("Cart: Meds: instance");
							// medList.removeAllViews();
							// hsList.removeAllViews();
							// setMeds();
							// setHS();
							isPlusClicked=true;
							setAmount();
							Utilities.writeToLogFIle("Cart: Meds: amount");
						}
					}
				});

				red_minus_ImageRl.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Utilities.writeToLogFIle("Cart: Meds: redminusimageRL");
						String i = String.valueOf(v.getTag());
						Utilities.writeToLogFIle("Cart: Meds: i" + i);
						int id = Integer.parseInt(i);
						Utilities.writeToLogFIle("Cart: Meds: id" + id);
						id = id - 1000;
						Utilities.writeToLogFIle("Cart: Meds: id2" + id);
						int id1 = id + 500;
						View vv = (CustomViewProdMyCart)medList.getChildAt(id);
						EditText edd8 = (EditText)vv.findViewWithTag(""+id1);
						Utilities.writeToLogFIle("Cart: Meds: id1" + id1);
						ViewParent ll = v.getParent();
						ViewParent l2 = ll.getParent();
						RelativeLayout rl = (RelativeLayout) l2;
						Utilities.writeToLogFIle("Cart: Meds: ll");
						//EditText edd8 = (EditText) rl.findViewById(id1);
						//edd8.setId(id1);
						//setEditTextMaxLength(2, edd8);
						Utilities.writeToLogFIle("Cart: Meds: edd1"
								+ edd8.getText().toString());
						if (edd8.getText().toString().trim().equals("")) {
							Utilities.writeToLogFIle("Cart: Meds: edd2"
									+ edd8.getText().toString());
							edd8.setText("00");
							Utilities.writeToLogFIle("Cart: Meds: edd3"
									+ edd8.getText().toString());
						}
						String uni = edd8.getText().toString();
						if (uni.length() > 2) {
							edd8.setText("");
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						} else {
							int unit = Integer.parseInt(edd8.getText()
									.toString());
							Utilities.writeToLogFIle("Cart: Meds: unit" + unit);
							if (unit <= 1) {
								unit = 1;
								Utilities.writeToLogFIle("Cart: Meds: unit"
										+ unit);
							} else {
								unit--;
								Utilities.writeToLogFIle("Cart: Meds: unit--"
										+ unit);
							}
							int prodid = setPdList.get(id).getProdId();
							for (int j = 0; j < prodList.size(); j++) {
								ProductDataBean pbean = new ProductDataBean();
								pbean = prodList.get(j);
								int pid = pbean.getProdId();
								if (pid == prodid) {
									prodList.get(j).setUnits(unit);
									break;
								}
							}
							Utilities.writeToLogFIle("Cart: Meds:setunits ");
							int i1 = prodList.size();
							Utilities.writeToLogFIle("Cart: Meds: size" + i1);
							if (unit > 9) {
								edd8.setText("" + unit);
							} else {
								edd8.setText("0" + unit);
							}
							//edd8.addTextChangedListener(new GenericTextWatcher(
								//	edd8));
							Utilities
									.writeToLogFIle("Cart: Meds: aftersettext");
							edd8.setLayoutParams(params4);
							Utilities.writeToLogFIle("Cart: Meds: params");
							edd8.setBackgroundColor(getResources().getColor(
									R.color.White));
							Utilities.writeToLogFIle("Cart: Meds: white");
							mainCart.setProdList(prodList);
							Utilities.writeToLogFIle("Cart: Meds: setlist");
							Okler.getInstance().setMainCart(mainCart);
							Utilities.writeToLogFIle("Cart: Meds: instance");
							// medList.removeAllViews();
							// hsList.removeAllViews();
							// setMeds();
							// setHS();
							isPlusClicked=true;
							setAmount();
							Utilities.writeToLogFIle("Cart: Meds: amount");
						}
					}
				});
			} catch (Exception e) {
				Utilities.writeToLogFIle("Exception Cart Med "
						+ e.getStackTrace());
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_cart, menu);
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
	public void onErrorResponse(VolleyError error) {
		String err = String.valueOf(error);
		// Toast.makeText(getApplicationContext(), err,
		// Toast.LENGTH_LONG).show();
		showProgress(false);
	}

	@Override
	public void onResponse(Object response) {
		String err = String.valueOf(response);
		// Toast.makeText(getApplicationContext(), err,
		// Toast.LENGTH_LONG).show();
		mainCart = new CartDataBean();
		showProgress(false);
		JSONObject jobj = (JSONObject) response;
		try {
			String msg = jobj.getString("message");
			if (msg.equals("User cart is Empty.")) {
				mtitle_mycart.setText("There are no items in your cart");
				hs_LL.setVisibility(View.INVISIBLE);
			}
			JSONArray jarr = jobj.getJSONArray("result");
			int length = jarr.length();

			prodList = new ArrayList<ProductDataBean>();

			for (int i = 0; i < length; i++) {
				JSONObject jobj1 = jarr.getJSONObject(i);
				pdbean = new ProductDataBean();
				pdbean.setCart_id(jobj1.getString("cart_id"));
				pdbean.setCart_num(jobj1.getString("cart_number"));
				pdbean.setProdId(Integer.parseInt(jobj1.getString("id")));
				pdbean.setTax(Float.parseFloat(jobj1.getString("tax")));
				pdbean.setPresc_id(jobj1.getString("prescription_id"));
				pdbean.setProdName(jobj1.getString("name"));
				pdbean.setDesc(jobj1.optString("description"));
				pdbean.setWeight(jobj1.optString("weight"));
				pdbean.setPacking_size(jobj1.optString("type_of_packing"));
				pdbean.setSide_effect(jobj1.optString("side_effects"));
				pdbean.setUnits(Integer.parseInt(jobj1.getString("quantity")));
				pdbean.setCart_item_id(Integer.parseInt(jobj1
						.getString("cart_item_id")));
				pdbean.setMrp(Float.parseFloat(jobj1.getString("price")));
				pdbean.setOklerPrice(Float.parseFloat(jobj1
						.getString("saleprice")));
				pdbean.setDiscount(Float.parseFloat(jobj1.getString("discount")));
				pdbean.setProdType(Integer.parseInt(jobj1.optString("pro_type")));
				String ajimg = jobj1.getString("images");
				String aurl2 = "";
				if (ajimg.equals(null)) {
					aurl2 = "drawable://" + R.drawable.no_image_found;
				} else {
					if (ajimg.contains(",")) {
						String arr1[] = ajimg.split(":");
						String sec = arr1[2];
						String arr2[] = sec.split("\"");
						aurl2 = arr2[1];

					} else {
						aurl2 = ajimg;
					}
				}
				String image_name = "";
				try {
					image_name = URLEncoder.encode(aurl2, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pdbean.setImgUrl(image_name);

				JSONObject uobj = new JSONObject();
				uobj = jobj.getJSONObject("productimage_url");
				pdbean.setMediumUrl(uobj.optString("productimage_url_medium"));
				pdbean.setSmallUrl(uobj.optString("productimage_url_small"));
				pdbean.setThumbUrl(uobj.optString("productimage_url_thumbnail"));
				pdbean.setClipArtUrl(uobj
						.optString("productimage_clipArt_images"));
				if (jobj1.getString("prescription_need").equals("null")
						|| jobj1.getString("prescription_need").equals(null)
						|| jobj1.getString("prescription_need").equals("")) {
					pdbean.setPresc_needed(0);
				} else {
					pdbean.setPresc_needed(Integer.parseInt(jobj1
							.getString("prescription_need")));
				}
				pdbean.setCompany(jobj1.optString("companyName"));
				if (jobj1.has("generic_val")) {
					pdbean.setGeneric_name(jobj1.optString("generic_val"));// need
																			// string
																			// from
																			// webservice
				}
				pdbean.setProdType(Integer.parseInt(jobj1.getString("pro_type")));

				prodList.add(pdbean);

			}

			mainCart.setProdList(prodList);
			Okler.getInstance().setMainCart(mainCart);
			int count = prodList.size();
			if (count > 9) {
				toolBarCount.setText("" + count);
			} else {
				toolBarCount.setText("0" + count);
			}

			setAmount();
			setMeds();
			setHS();

			/*
			 * clistViewAdapter = new
			 * CheckoutListViewAdapter(getApplicationContext(), prodList,true);
			 * medList.setAdapter(clistViewAdapter);
			 */

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setTouchListenerScrViews() {
		medList.setOnTouchListener(new ListView.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					// Disallow ScrollView to intercept touch events.
					mainScroll.requestDisallowInterceptTouchEvent(true);
					break;

				case MotionEvent.ACTION_UP:
					// Allow ScrollView to intercept touch events.
					mainScroll.requestDisallowInterceptTouchEvent(false);
					break;
				}

				// Handle HorizontalScrollView touch events.
				v.onTouchEvent(event);
				return true;
			}
		});
	}

	public void setAmount() {
		try {
			Utilities.writeToLogFIle("Cart: SetAmount: ");
			int length = prodList.size(), units;
			Utilities.writeToLogFIle("Cart: SetAmount: size" + length);
			float mrp = 0.00f, okler_pr = 0.00f, disco = 0.00f, tax = 0.00f, shipping = 0.00f, net_pay = 0.00f;
			Utilities.writeToLogFIle("Cart: SetAmount: declare");
			if (length <= 0) {
				Utilities.writeToLogFIle("Cart: SetAmount: length" + length);
				amount_value_tv.setText("Rs." + "00");
				okler_disc_value_tv.setText("Rs." + "00");
				shipping_charg_value_tv.setText("Rs." + "00");
				tax_value_tv.setText("Rs." + "00");
				net_pay_value_tv.setText("Rs." + "00");
			} else {
				Utilities.writeToLogFIle("Cart: SetAmount: else");
				ProductDataBean pbean = new ProductDataBean();
				Utilities.writeToLogFIle("Cart: SetAmount: before for");
				for (int i = 0; i < length; i++) {
					pbean = prodList.get(i);
					Utilities.writeToLogFIle("Cart: SetAmount: pbean "
							+ String.valueOf(pbean));
					units = pbean.getUnits();
					Utilities.writeToLogFIle("Cart: SetAmount: units" + units);
					float mrp1 = pbean.getMrp();
					Utilities.writeToLogFIle("Cart: SetAmount: mrp1 " + mrp1);
					mrp = mrp + (pbean.getMrp() * pbean.getUnits());
					Utilities.writeToLogFIle("Cart: SetAmount: mrp" + mrp);
					float ok = pbean.getOklerPrice();
					Utilities.writeToLogFIle("Cart: SetAmount: ok" + ok);
					okler_pr = okler_pr
							+ (pbean.getOklerPrice() * pbean.getUnits());
					Utilities.writeToLogFIle("Cart: SetAmount: okler_pr"
							+ okler_pr);

					float tx = pbean.getTax();
					Utilities.writeToLogFIle("Cart: SetAmount: tx" + tx);
					tax = tax + (pbean.getTax() * pbean.getUnits());
					Utilities.writeToLogFIle("Cart: SetAmount: tax" + tax);
				}
				disco = (mrp - okler_pr);
				Utilities.writeToLogFIle("Cart: SetAmount: disco" + disco);
				net_pay = okler_pr + tax;
				Utilities.writeToLogFIle("Cart: SetAmount: net_pay" + net_pay);
				if (net_pay <= 499 && net_pay>0) {
					Utilities.writeToLogFIle("Cart: SetAmount: in if ship");
					shipping = 50;
					net_pay = net_pay + shipping;
					Utilities.writeToLogFIle("Cart: SetAmount: net_pay ship "
							+ net_pay);
				}
				amount_value_tv.setText("Rs." + mrp);
				Utilities.writeToLogFIle("Cart: SetAmount: amount tv");
				okler_disc_value_tv.setText("Rs." + disco);
				Utilities.writeToLogFIle("Cart: SetAmount: disc tv");
				if(net_pay<=0)
					shipping=0;
				shipping_charg_value_tv.setText("Rs." + shipping);
				Utilities.writeToLogFIle("Cart: SetAmount: ship tv");
				tax_value_tv.setText("Rs." + tax);
				Utilities.writeToLogFIle("Cart: SetAmount: tax tv");
				net_pay_value_tv.setText("Rs." + net_pay);
				Utilities.writeToLogFIle("Cart: SetAmount: net pay tv");
			}
		} catch (Exception e) {
			// TODO: handle exception
			Utilities.writeToLogFIle("Cart setAmount " + e.getStackTrace());
		}

	}

	public void setEditTextMaxLength(int length, EditText edt_text) {
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(length);
		edt_text.setFilters(FilterArray);
	}

	public class GenericTextWatcher implements TextWatcher {

		private View view;

		private GenericTextWatcher(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence charSequence, int i, int i1,
				int i2) {
		}

		public void onTextChanged(CharSequence charSequence, int i, int i1,
				int i2) {
		}

		public void afterTextChanged(Editable editable) {
			if(isPlusClicked){
				isPlusClicked=false;
			}else{
			String text = editable.toString();
			int id = Integer.parseInt(String.valueOf(view.getTag()));
			// Toast.makeText(ack, id+" "+text, 1000).show();

			if (id < 5000) {
				id = id - 500;
				int pd_id = setPdList.get(id).getProdId();
				for (int i = 0; i < prodList.size(); i++) {
					ProductDataBean pbean = new ProductDataBean();
					pbean = prodList.get(i);
					int prod_id = pbean.getProdId();
					if (prod_id == pd_id) {
						if (text.equals("")) {
							text = "0";
							EditText et = (EditText) view;
					//		et.setText(text);
							return;
						}
						prodList.get(i).setUnits(Integer.parseInt(text));
						break;
					}
				}
			} else {
				id = id - 50000;
				int pd_id = setPdLisths.get(id).getProdId();
				for (int i = 0; i < prodList.size(); i++) {
					ProductDataBean pbean = new ProductDataBean();
					pbean = prodList.get(i);
					int prod_id = pbean.getProdId();
					if (prod_id == pd_id) {
						if (text.equals("")) {
							text = "0";
							EditText et = (EditText) view;
							//et.setText(text);
							return;
						}
						prodList.get(i).setUnits(Integer.parseInt(text));
						break;
					}
				}
		
			}
		setAmount();
		}
		}
	}
}
