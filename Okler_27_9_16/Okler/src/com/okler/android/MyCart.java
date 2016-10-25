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
import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MyCart extends BaseActivity implements Response.Listener<JSONObject>,
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
		progressLinLayout = (LinearLayout) findViewById(R.id.progressLinLayout);
		imgloader = VolleyRequest.getInstance(getApplicationContext())
				.getImageLoader();
		mtitle_mycart.setText("MEDICINES");
		htitle_mycart.setText("HEALTHSHOP");
		UIUtils.setBackClick(toolBar, ack);

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
						Utilities.writeCartToSharedPref(ack, mainCart);
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
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);

		getMyCartUrl = getMyCartUrl + ubean.getId();
		WebJsonObjectRequest cartjson = new WebJsonObjectRequest(Method.GET,
				getMyCartUrl, new JSONObject(), this, this);
		VolleyRequest.clearCache();
		
		if(cartjson.getCacheEntry() != null)
		if(cartjson.getCacheEntry().data != null)
		{
			Log.e("Cache data", "Cache entry data"+cartjson.getCacheEntry().data.toString());
		}
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
		if(size==0){
			med_LL.setVisibility(View.VISIBLE);
			mtitle_mycart.setText("There are no items in your cart");
			mtitle_mycart.setVisibility(View.VISIBLE);
		}else{
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
			unitshs = (EditText) rowViewhs[i].findViewById(R.id.unitValue);
			int t2 = i + 50000;
			unitshs.setTag(t2 + "");
			unitshs.addTextChangedListener(new GenericTextWatcher(unitshs));
			deleteImagehs = (ImageView) rowViewhs[i]
					.findViewById(R.id.deleteImage);
			deleteImagehs.setTag("" + i);
			image_favouritehs = (ImageView) rowViewhs[i]
					.findViewById(R.id.image_favourite);
			image_favouritehs.setVisibility(View.GONE);
			/*image_carths = (Button) rowViewhs[i].findViewById(R.id.image_cart);
			image_carths.setVisibility(View.GONE);*/
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
			mrphs.setText(""  + pdBean.getMrp());
			you_savehs.setText(pdBean.getDiscount() + "%");
			okler_pricehs.setText(okler_pricehs.getText().toString()
					+ "\n" + pdBean.getOklerPrice());
			int u = pdBean.getUnits();
			if (u > 9) {
				unitshs.setText("" + pdBean.getUnits());
			} else {
				unitshs.setText("0" + pdBean.getUnits());
			}
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
					id = Integer.parseInt(i);
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
										e.printStackTrace();
									}
								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
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
						String i = String.valueOf(v.getTag());
						int id = Integer.parseInt(i);
						id = id - 100;
						View vv = (CustomViewProdMyCart)hsList.getChildAt(id);
						int id1 = id + 50000;
						EditText edd1 = (EditText)vv.findViewWithTag(""+id1);
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
								Utilities.writeCartToSharedPref(ack, mainCart);
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
						String i = String.valueOf(v.getTag());
						int id = Integer.parseInt(i);
						id = id - 100;
						int id1 = id + 50000;
						View vv = (CustomViewProdMyCart)hsList.getChildAt(id);
						
						EditText edd2 = (EditText)vv.findViewWithTag(""+id1);
						Utilities.writeToLogFIle("Cart: Hs: id1" + id1);
						if (edd2.getText().toString().trim().equals("")) {
							edd2.setText("00");
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
								unit++;
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
								if (unit > 9) {
									edd2.setText("" + unit);
								} else {
									edd2.setText("0" + unit);
								}
								edd2.setLayoutParams(params4);
								edd2.setBackgroundColor(getResources()
										.getColor(R.color.White));
								mainCart.setProdList(prodList);
								Okler.getInstance().setMainCart(mainCart);
								Utilities.writeCartToSharedPref(ack, mainCart);
								isPlusClicked=true;
								setAmount();
							}
						}
					}
				});
				red_minus_Imagehs.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String i = String.valueOf(v.getTag());
						int id = Integer.parseInt(i);
						id = id - 1000;
						int id1 = id + 50000;
						View vv = (CustomViewProdMyCart)hsList.getChildAt(id);
						EditText edd3 = (EditText)vv.findViewWithTag(""+id1);
						if (edd3.getText().toString().trim().equals("")) {
							edd3.setText("00");
						}
						String uni = edd3.getText().toString();
						if (uni.length() > 2) {
							edd3.setText("");
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						} else {
							int unit = Integer.parseInt(edd3.getText()
									.toString());
							if (unit <= 1) {
								unit = 1;
							} else {
								unit--;
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
							int i1 = prodList.size();
							Utilities.writeToLogFIle("Cart: Hs: size" + i1);
							if (unit > 9) {
								edd3.setText("" + unit);
							} else {
								edd3.setText("0" + unit);
							}
							edd3.setLayoutParams(params4);
							edd3.setBackgroundColor(getResources().getColor(
									R.color.White));
							mainCart.setProdList(prodList);
							Okler.getInstance().setMainCart(mainCart);
							Utilities.writeCartToSharedPref(ack, mainCart);
							isPlusClicked=true;
							setAmount();
						}
					}
				});

				red_minus_ImageRlhs.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String i = String.valueOf(v.getTag());
						int id = Integer.parseInt(i);
						id = id - 1000;
						int id1 = id + 50000;
						View vv = (CustomViewProdMyCart)hsList.getChildAt(id);
						EditText edd4 = (EditText)vv.findViewWithTag(""+id1);
						if (edd4.getText().toString().trim().equals("")) {
							edd4.setText("00");
						}
						String uni = edd4.getText().toString();
						if (uni.length() > 2) {
							edd4.setText("");
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						} else {
							int unit = Integer.parseInt(edd4.getText()
									.toString());
							if (unit <= 1) {
								unit = 1;
							} else {
								unit--;
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
							if (unit > 9) {
								edd4.setText("" + unit);
							} else {
								edd4.setText("0" + unit);
							}
							edd4.setLayoutParams(params4);
							edd4.setBackgroundColor(getResources().getColor(
									R.color.White));
							mainCart.setProdList(prodList);
							Okler.getInstance().setMainCart(mainCart);
							Utilities.writeCartToSharedPref(ack, mainCart);
							isPlusClicked=true;
							setAmount();
						}
					}
				});
			} catch (Exception e) {
				Utilities.writeToLogFIle("Exception Cart HS "
						+ e.getStackTrace());
			}
		}
	}
	}

	private void setMeds() {
		setPdList = new ArrayList<ProductDataBean>();
		ProductDataBean pdBean = new ProductDataBean();
		int size = prodList.size();
		if(size==0){
			med_LL.setVisibility(View.VISIBLE);
			mtitle_mycart.setText("There are no items in your cart");
			mtitle_mycart.setVisibility(View.VISIBLE);
		}else{
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
			units = (EditText) rowView[i].findViewById(R.id.unitValue);
			int tg = i + 500;
			units.setTag("" + tg);
			units.addTextChangedListener(new GenericTextWatcher(units));
			deleteImage = (ImageView) rowView[i].findViewById(R.id.deleteImage);
			deleteImage.setTag("" + i);
			image_favourite = (ImageView) rowView[i]
					.findViewById(R.id.image_favourite);
			image_favourite.setVisibility(View.GONE);
			/*image_cart = (Button) rowView[i].findViewById(R.id.image_cart);
			image_cart.setVisibility(View.GONE);*/

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
					+ "\n" + pdBean.getOklerPrice());
			int uni = pdBean.getUnits();
			if (uni > 9)
				units.setText("" + pdBean.getUnits());
			else
				units.setText("0" + pdBean.getUnits());
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
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}

								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
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

				red_plus_Image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String i = String.valueOf(v.getTag());
						int id = Integer.parseInt(i);
						id = id - 100;
						int id1 = id + 500;
						View vv = (CustomViewProdMyCart)medList.getChildAt(id);
						EditText edd5 = (EditText)vv.findViewWithTag(""+id1);
						if (edd5.getText().toString().trim().equals("")) {
							edd5.setText("00");
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
								unit++;
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
								if (unit > 9) {
									edd5.setText("" + unit);
								} else {
									edd5.setText("0" + unit);
								}
								edd5.setLayoutParams(params4);
								edd5.setBackgroundColor(getResources()
										.getColor(R.color.White));
								int i1 = prodList.size();
								Utilities.writeToLogFIle("Cart: Meds: size"
										+ i1);
								mainCart.setProdList(prodList);
								Okler.getInstance().setMainCart(mainCart);
								Utilities.writeCartToSharedPref(ack, mainCart);
								isPlusClicked=true;
								setAmount();
							}
						}
					}
				});
				red_plus_ImageRl.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String i = String.valueOf(v.getTag());
						int id = Integer.parseInt(i);
						id = id - 100;
						int id1 = id + 500;
						View vv = (CustomViewProdMyCart)medList.getChildAt(id);
						EditText edd6 = (EditText)vv.findViewWithTag(""+id1);
						if (edd6.getText().toString().trim().equals("")) {
							edd6.setText("00");
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
								unit++;
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
								if (unit > 9) {
									edd6.setText("" + unit);
								} else {
									edd6.setText("0" + unit);
								}
								edd6.setLayoutParams(params4);
								edd6.setBackgroundColor(getResources()
										.getColor(R.color.White));
								mainCart.setProdList(prodList);
								Okler.getInstance().setMainCart(mainCart);
								Utilities.writeCartToSharedPref(ack, mainCart);
								isPlusClicked=true;
								setAmount();
							}
						}
					}
				});

				red_minus_Image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String i = String.valueOf(v.getTag());
						int id = Integer.parseInt(i);
						id = id - 1000;
						int id1 = id + 500;
						View vv = (CustomViewProdMyCart)medList.getChildAt(id);
						EditText edd7 = (EditText)vv.findViewWithTag(""+id1);
						if (edd7.getText().toString().trim().equals("")) {
							edd7.setText("00");
						}
						String uni = edd7.getText().toString();
						if (uni.length() > 2) {
							edd7.setText("");
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						} else {
							int unit = Integer.parseInt(edd7.getText()
									.toString());
							if (unit <= 1) {
								unit = 1;
							} else {
								unit--;
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
							int i1 = prodList.size();
							Utilities.writeToLogFIle("Cart: Meds: size" + i1);
							if (unit > 9) {
								edd7.setText("" + unit);
							} else {
								edd7.setText("0" + unit);
							}
							edd7.setLayoutParams(params4);
							edd7.setBackgroundColor(getResources().getColor(
									R.color.White));
							mainCart.setProdList(prodList);
							Okler.getInstance().setMainCart(mainCart);
							Utilities.writeCartToSharedPref(ack, mainCart);
							isPlusClicked=true;
							setAmount();
						}
					}
				});

				red_minus_ImageRl.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String i = String.valueOf(v.getTag());
						int id = Integer.parseInt(i);
						id = id - 1000;
						int id1 = id + 500;
						View vv = (CustomViewProdMyCart)medList.getChildAt(id);
						EditText edd8 = (EditText)vv.findViewWithTag(""+id1);
						if (edd8.getText().toString().trim().equals("")) {
							edd8.setText("00");
						}
						String uni = edd8.getText().toString();
						if (uni.length() > 2) {
							edd8.setText("");
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						} else {
							int unit = Integer.parseInt(edd8.getText()
									.toString());
							if (unit <= 1) {
								unit = 1;
							} else {
								unit--;
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
							if (unit > 9) {
								edd8.setText("" + unit);
							} else {
								edd8.setText("0" + unit);
							}
							edd8.setLayoutParams(params4);
							edd8.setBackgroundColor(getResources().getColor(
									R.color.White));
							mainCart.setProdList(prodList);
							Okler.getInstance().setMainCart(mainCart);
							Utilities.writeCartToSharedPref(ack, mainCart);
							isPlusClicked=true;
							setAmount();
						}
					}
				});
			} catch (Exception e) {
				Utilities.writeToLogFIle("Exception Cart Med "
						+ e.getStackTrace());
			}
		}
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

	@Override
	public void onErrorResponse(VolleyError error) {
		Log.e("Error",String.valueOf(error));
		showProgress(false);
	}

	@Override
	public void onResponse(JSONObject response) {
		
		mainCart = new CartDataBean();
		showProgress(false);
		JSONObject jobj = response;
		try {
			String msg = jobj.getString("message");
			prodList = new ArrayList<ProductDataBean>();
			if (msg.equals("User cart is Empty.")) {
				mtitle_mycart.setText("There are no items in your cart");
				hs_LL.setVisibility(View.INVISIBLE);
			}else{
			JSONArray jarr = jobj.getJSONArray("result");
			int length = jarr.length();
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
					pdbean.setGeneric_name(jobj1.optString("generic_val"));
				}
				pdbean.setProdType(Integer.parseInt(jobj1.getString("pro_type")));
				prodList.add(pdbean);
			}
		}
			mainCart.setProdList(prodList);
			Okler.getInstance().setMainCart(mainCart);
			Utilities.writeCartToSharedPref(ack, mainCart);
			int count = prodList.size();
			if (count > 9) {
				toolBarCount.setText("" + count);
			} else {
				toolBarCount.setText("0" + count);
			}
			setAmount();
			setMeds();
			setHS();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void setAmount() {
		try {
			int length = prodList.size();
			float mrp = 0.00f, okler_pr = 0.00f, disco = 0.00f, tax = 0.00f, shipping = 0.00f, net_pay = 0.00f;
			if (length <= 0) {
				amount_value_tv.setText("Rs." + "00");
				okler_disc_value_tv.setText("Rs." + "00");
				shipping_charg_value_tv.setText("Rs." + "00");
				tax_value_tv.setText("Rs." + "00");
				net_pay_value_tv.setText("Rs." + "00");
			} else {
				ProductDataBean pbean = new ProductDataBean();
				for (int i = 0; i < length; i++) {
					pbean = prodList.get(i);
					mrp = mrp + (pbean.getMrp() * pbean.getUnits());
										okler_pr = okler_pr
							+ (pbean.getOklerPrice() * pbean.getUnits());
					tax = tax + (pbean.getTax() * pbean.getUnits());
				}
				disco = (mrp - okler_pr);
				net_pay = okler_pr + tax;
				if (net_pay <= 499 && net_pay>0) {
					shipping = 50;
					net_pay = net_pay + shipping;
				}
				amount_value_tv.setText("Rs." + mrp);
				okler_disc_value_tv.setText("Rs." + disco);
				if(net_pay<=0)
					shipping=0;
				shipping_charg_value_tv.setText("Rs." + shipping);
				tax_value_tv.setText("Rs." + tax);
				net_pay_value_tv.setText("Rs." + net_pay);
			}
		} catch (Exception e) {
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
