package com.okler.android;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.okler.customviews.CustomViewProdInfo;
import com.okler.customviews.CustomViewProdSubst;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.JustViewedProdsDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetailsActivity extends BaseActivity {

	// variable section for medicines

	Button gen_check;
	EditText gen_pincodeEt;
	LinearLayout gen_check_pin_LL;
	ImageView gen_edit_pincode, gen_add_button_desc, gen_minus_button_desc,
			gen_add_caution, gen_add_contra_indi, gen_add_dosage, gen_add_indi,
			gen_add_side_eff, gen_add_weight, gen_minus_caution,
			gen_minus_contra_indi, gen_minus_dosage, gen_minus_indi,
			gen_minus_side_eff, gen_minus_weight;
	RelativeLayout gen_available_LL, gen_unitLayout, gen_desc_details_RL,
			gen_indi_details_RL, gen_caution_details_RL,
			gen_contra_indi_details_RL, gen_side_eff_details_RL,
			gen_weight_details_RL, gen_dosage_Details_RL;
	TextView gen_desc_heading, gen_indi_heading, gen_caution_heading,
			gen_cIndi_heading, gen_sEff_heading, gen_weight_heading,
			gen_dosage_heading;
	TextView viewMoreTv, viewLessTv, gen_viewMoreTv, gen_viewLessTv,
			desc_heading, indi_heading, cIndi_heading, caution_heading,
			sEff_heading, weight_heading, dosage_heading, pincodeTv,
			gen_pincodeTv, availableTv, shipstoTv, gen_availableTv,
			gen_shipstoTv;
	RelativeLayout desc_details_RL, indi_details_RL, contra_indi_details_RL,
			caution_details_RL, side_eff_details_RL, weight_details_RL,
			dosage_Details_RL, available_LL, unitLayout, gen_enter_pin_LL,
			RL_For_Prod_Info;// 13_01_2016 Gitesh
	ImageView image_favourite, add_button_desc, minus_button_desc, add_indi,
			minus_indi, add_contra_indi, minus_contra_indi, add_caution,
			minus_caution, add_side_eff, minus_side_eff, add_weight,
			minus_weight, add_dosage, minus_dosage, edit_pincode;// 13_01_2016
																	// Gitesh
	CustomViewProdInfo customViewDesc, customViewIndi, customViewCIndi,
			customViewCaution, customViewSEff, customViewWeight,
			customViewDosage, gen_customViewDesc, gen_customViewIndi,
			gen_customViewCIndi, gen_customViewCaution, gen_customViewSEff,
			gen_customViewWeight, gen_customViewDosage;
	CustomViewProdSubst customViewProdSubst1, customViewProdSubst2,
			customViewProdSubst3;
	LinearLayout gen_desc_Main_RL, desc_Main_RL, check_pin_LL, substitutesLL,
			SubMainLL1, SubMainLL2, SubMainLL3;
	Button check;
	EditText pincodeEt, unitValue, gen_unitValue;
	View genView;
	String pincodeUrl, pinurlpart1, pinurlpart2, pincode, gen_pincode;
	int prodId, userId;
	String addToFav1, addToFav2, addToFavUrl;
	// end of variable section for medicines
	String medId, getMedSubUrl1, getMedSubUrl;
	Toolbar toolBar;
	Button notifCount;
	View bottomBarLayout;
	ScrollView scrView;
	HorizontalScrollView horScrView;
	static ImageView addToCart, genAddToCart;
	SpannableString viewMore, viewLess, mrp, gen_mrp, change_pin;
	LinearLayout.LayoutParams params;
	ImageView imgBack, buyIV;
	ArrayList<ProductDataBean> ListForSubs = new ArrayList<ProductDataBean>();
	ArrayList<ProductDataBean> pArrList2;
	int i1;
	ProductDataBean hsBean;
	int curList;
	ImageView shareBtn;
	UsersDataBean ubean;
	// for healthshop
	ImageView add_button_Hskey, minus_button_Hskey, add_button_HSdesc,
			minus_button_HSdesc, add_button_HSspec, minus_button_HSspec,
			add_button_HSbrand, minus_button_HSbrand, minus_button_HSwar,
			add_button_HSwar;
	CustomViewProdInfo customViewKeyFeature, customViewDescption,
			customViewSpeci, customViewBInfo, customViewWarrant;
	RelativeLayout Hskey_details_RL, HSdesc_details_RL, HSspec_details_RL,
			HSbrand_details_RL, HSwar_details_RL;
	Context context;
	Activity activity;
	JSONArray relatedPro;
	TextView txt_item;
	boolean addCart = false, buy = false, gen_addCart = false, gen_buy = false,
			isAddedToCart = false, gen_isAddedToCart = false;
	String unit;
	int units;
	String First;
	CartDataBean mainbean;
	String prod_id_recent_prod = "", prod_id_similar_prod = "";
	JustViewedProdsDataBean jbean;
	RelativeLayout back_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_details);
		context = getApplicationContext();
		medId = getIntent().getStringExtra("MedId");
		curList = getIntent().getIntExtra("MedList", 100);
		Gson gson = new Gson();
		ProductDataBean pdbean = new ProductDataBean();
		activity = this;
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		notifCount = (Button) toolBar.findViewById(R.id.notif_count);
		ubean = Utilities.getCurrentUserFromSharedPref(activity);
		userId = ubean.getId();
		/*
		 * if(curList==100){ curList = 100; }
		 */

		txt_item = (TextView) findViewById(R.id.no_items);
		if (curList == 22) {
			pArrList2 = Okler.getInstance().getSubProdList();
		} else {
			pArrList2 = Okler.getInstance().getProdList();
		}
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);

		ActionBar ab = getSupportActionBar();
		// ab.setDisplayHomeAsUpEnabled(true);
		image_favourite = (ImageView) findViewById(R.id.image_favourite);
		image_favourite.setVisibility(View.INVISIBLE);
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
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(imgBack.getWindowToken(), 0);
			}
		});*/

		UIUtils.setBackClick(toolBar, activity);
		unitValue = (EditText) findViewById(R.id.unitValue);
		unitValue.requestFocus();
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 0, 10);
		viewMore = new SpannableString("View Details");
		viewMore.setSpan(new UnderlineSpan(), 0, viewMore.length(), 0);
		viewLess = new SpannableString(getString(R.string.viewLess));
		viewLess.setSpan(new UnderlineSpan(), 0, viewLess.length(), 0);
		change_pin = new SpannableString(getString(R.string.change));
		change_pin.setSpan(new UnderlineSpan(), 0, change_pin.length(), 0);
		scrView = (ScrollView) findViewById(R.id.scrollViewProdDetails);
		buyIV = (ImageView) findViewById(R.id.buyIV);
		// viewLess.setSpan(new StrikethroughSpan(), 0, viewLess.length(), 0);

		if (Okler.getInstance().getBookingType() == 0) {

			mainProductUI();
			alloUI();
			availableSubstitutes();
			// topGenProductUI();
			Utilities.setTitleText(toolBar, getString(R.string.allopathy));
		} else if (Okler.getInstance().getBookingType() == 3) {
			mainProductUI();
			ayurUI();
			Utilities.setTitleText(toolBar, getString(R.string.ayurvedic));
		} else if (Okler.getInstance().getBookingType() == 4) {
			mainProductUI();
			homeoUI();
			Utilities.setTitleText(toolBar, getString(R.string.homeopathy));
		} else {
			ab.setTitle(R.string.title_activity_health_shop_grid);
			Utilities.setTitleText(toolBar, "Health Shop");
			mainProductHsUI();
			healthShopFunction();

		}
		toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler
				.getInstance().getBookingType()));
		buyNow();
		addToCart();
		share(prodId);

		addFavourite();
		// horScrView =
		// (HorizontalScrollView)findViewById(R.id.prodsHprScrView);
		// setTouchListenerScrViews();
	}

	private void mainProductHsUI() {

		TextView item_name, brand_name, presc_req, tv_MrpValue, tv_youSaveValue, okler_Amount;
		ImageLoader imgloader;
		NetworkImageView item_image = (NetworkImageView) findViewById(R.id.item_image);
		HorizontalScrollView similarProdView, justViewProd;
		RelativeLayout horiScrollMainLayout;
		String second = "";

		/* horizontl scrollView related */
		horiScrollMainLayout = (RelativeLayout) findViewById(R.id.horiScrollMainLayout);
		horiScrollMainLayout.setVisibility(View.VISIBLE);

		similarProdView = (HorizontalScrollView) findViewById(R.id.prodHSScrView);
		setTouchListenerScrViews(similarProdView);

		justViewProd = (HorizontalScrollView) findViewById(R.id.justViewedScrView);
		setTouchListenerScrViews(justViewProd);

		item_name = (TextView) findViewById(R.id.item_name);
		brand_name = (TextView) findViewById(R.id.brand_name);
		brand_name.setVisibility(View.GONE);
		presc_req = (TextView) findViewById(R.id.presc_req);
		presc_req.setVisibility(View.GONE);
		tv_MrpValue = (TextView) findViewById(R.id.tv_MrpValue);
		tv_youSaveValue = (TextView) findViewById(R.id.tv_youSaveValue);
		okler_Amount = (TextView) findViewById(R.id.okler_Amount);

		viewMoreTv = (TextView) findViewById(R.id.viewMoreTv);
		viewMoreTv.setText(viewMore);
		desc_Main_RL = (LinearLayout) findViewById(R.id.desc_Main_RL);
		viewLessTv = (TextView) findViewById(R.id.viewLessTv);
		viewLessTv.setText(viewLess);
		RelativeLayout relativeHiddenLayout = (RelativeLayout) findViewById(R.id.relativeHiddenLayout);
		relativeHiddenLayout.setVisibility(View.VISIBLE);
		// Button cartToHide = (Button)findViewById(R.id.image_cart);
		// cartToHide.setVisibility(View.GONE);
		TextView mrpTv = (TextView) findViewById(R.id.tv_MrpValue);
		mrp = new SpannableString(mrpTv.getText().toString());
		mrp.setSpan(new StrikethroughSpan(), 0, mrp.length(), 0);
		mrpTv.setText(mrp);
		TextView change = (TextView) findViewById(R.id.changeTv);
		change.setText(change_pin);
		addToCart = (ImageView) findViewById(R.id.cartIV);

		// ArrayList<ProductDataBean> prodListAllo =
		// Okler.getInstance().getProdList();
		check = (Button) findViewById(R.id.button_check);
		pincodeEt = (EditText) findViewById(R.id.enter_pincode_Et);
		pincodeTv = (TextView) findViewById(R.id.pincodeTv);
		check_pin_LL = (LinearLayout) findViewById(R.id.check_pin_LL);
		edit_pincode = (ImageView) findViewById(R.id.edit_pincode);
		available_LL = (RelativeLayout) findViewById(R.id.available_LL);
		unitLayout = (RelativeLayout) findViewById(R.id.unitLayout);
		availableTv = (TextView) findViewById(R.id.availableTv);
		shipstoTv = (TextView) findViewById(R.id.shipstoTv);

		viewMoreTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewMoreTv.setVisibility(View.GONE);
				viewLessTv.setVisibility(View.VISIBLE);
				desc_Main_RL.setVisibility(View.VISIBLE);

			}
		});
		viewLessTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewMoreTv.setVisibility(View.VISIBLE);
				viewLessTv.setVisibility(View.GONE);
				desc_Main_RL.setVisibility(View.GONE);
			}
		});
		// add minus buttons for descriptions
		try {
			JSONObject json1 = new JSONObject(medId);
			// Toast.makeText(getApplicationContext(), medId,
			// Toast.LENGTH_LONG).show();

			item_name.setText(json1.getString("prodName"));

			Log.e("HSOBJECT", medId);
			float mrp = Float.parseFloat(json1.getString("mrp"));
			tv_MrpValue.setText("" + mrp);
			tv_youSaveValue
					.setText((String.valueOf(json1.getInt("discount")) + "%"));
			String part1 = okler_Amount.getText().toString();
			float ok_price = Float.parseFloat(json1.getString("oklerPrice"));
			String full = part1 + ok_price;
			okler_Amount.setText(full);
			prodId = json1.getInt("prodId");
			if (Okler.getInstance().getBookingType() == 0) {
				First = json1.optString("clipArtUrl");
			} else {
				First = json1.optString("mediumUrl");
			}

			second = json1.getString("imgUrl");
			Log.e("URL", First + second);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		pinurlpart1 = getString(R.string.getPinUrlPart1);
		pinurlpart2 = getString(R.string.getPinUrlPart2);

		check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pincodeEt.getText().toString().trim().length() > 6
						|| pincodeEt.getText().toString().trim().length() < 6) {

					Toast.makeText(getApplicationContext(),
							"Please Enter Correct Pincode", Toast.LENGTH_LONG)
							.show();
				} else {
					pincode = pincodeEt.getText().toString().trim();

					pincodeUrl = pinurlpart1 + prodId + pinurlpart2 + pincode;
					WebJsonObjectRequest pinjson = new WebJsonObjectRequest(
							Method.GET, pincodeUrl, new JSONObject(),
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									String resp = String.valueOf(response);
									// Toast.makeText(getApplicationContext(),
									// resp, Toast.LENGTH_LONG).show();
									JSONObject pinobj = (JSONObject) response;
									try {
										resp = pinobj.getString("message");
										if (resp.equals("Pincode Not Available")) {
											pincodeTv.setText(pincode);
											check_pin_LL
													.setVisibility(View.GONE);
											available_LL
													.setVisibility(View.VISIBLE);
											availableTv
													.setText("Not Available");
											shipstoTv.setVisibility(View.GONE);
											buyIV.setEnabled(false);
										} else {
											pincodeTv.setText(pincode);
											check_pin_LL
													.setVisibility(View.GONE);
											available_LL
													.setVisibility(View.VISIBLE);
											availableTv.setText("Available");
											shipstoTv
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
									String err = String.valueOf(error);
									// Toast.makeText(getApplicationContext(),
									// err, Toast.LENGTH_LONG).show();

								}
							});
					VolleyRequest.addJsonObjectRequest(getApplicationContext(),
							pinjson);

				}
			}
		});

		change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pincodeEt.setText("");
				pincodeEt.requestFocus();
				check_pin_LL.setVisibility(View.VISIBLE);
				available_LL.setVisibility(View.GONE);

			}
		});

		imgloader = VolleyRequest.getInstance(getApplicationContext())
				.getImageLoader();
		// String First=
		// "http://183.82.110.105:8081/oklerdevv2/uploads/images/medium/";
		// First= pdBean.getThumbUrl();

		String photo_url = First + second;
		item_image.setImageUrl(photo_url, imgloader);

		UIUtils.addRemoveUnits(unitLayout, activity);
	}

	private void healthShopFunction() {
		// TODO Auto-generated method stub
		// writeJustViewdProdAndGet();

		customViewKeyFeature = new CustomViewProdInfo(this);
		keyFeature();
		customViewDescption = new CustomViewProdInfo(this);
		Descrption();
		customViewSpeci = new CustomViewProdInfo(this);
		specification();
		customViewBInfo = new CustomViewProdInfo(this);
		brandInfo();
		customViewWarrant = new CustomViewProdInfo(this);
		wrrant();
		displayrecentProds();
		displaySimilarProd();
		// displayJustViewedImage();
	}

	private void displayrecentProds() {
		jbean = new JustViewedProdsDataBean();
		jbean = Utilities.getjustViewdProdsFromSharedPref(activity);
		ArrayList<ProductDataBean> recendProds = new ArrayList<ProductDataBean>();
		recendProds = jbean.getJustViewedProdsList();
		// recendProds.clear();
		int count = recendProds.size();
		ProductDataBean pdataBean = new ProductDataBean();
		pdataBean = Utilities.getProductdataBean(activity, medId);
		int curid = pdataBean.getProdId();
		int cmpid;
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				ProductDataBean pbean = new ProductDataBean();
				pbean = recendProds.get(i);
				cmpid = pbean.getProdId();
				if (cmpid == curid) {
					recendProds.remove(i);
					break;
				}
			}
		}
		recendProds.add(pdataBean);
		jbean.setJustViewedProdsList(recendProds);
		Utilities.writejustViewdProdsToSharedPref(activity, jbean);
		count = recendProds.size();
		String photo_url = null;
		String prodname = null;
		LinearLayout innerLayout = (LinearLayout) findViewById(R.id.innerLayout2);
		View[] views = new View[count];
		int j = 0;
		for (int i = count - 2; i > 0; i--) {
			ProductDataBean pbean = new ProductDataBean();
			pbean = recendProds.get(i);
			j++;
			prod_id_recent_prod = String.valueOf(pbean.getProdId());
			prodname = pbean.getProdName();
			String imageUrl = pbean.getImgUrl();
			photo_url = First + imageUrl;
			views[j] = getLayoutInflater().inflate(
					R.layout.include_hs_related_products, null);
			// ImageLoader imgloader =
			// VolleyRequest.getInstance(getApplicationContext()).getImageLoader();
			TextView imageName = (TextView) views[j]
					.findViewById(R.id.relatedImageName);
			imageName.setText(prodname);
			NetworkImageView imgCategoryIcon = (NetworkImageView) views[j]
					.findViewById(R.id.relatedImage1);
			ImageLoader imgloader = VolleyRequest.getInstance(
					getApplicationContext()).getImageLoader();
			imgCategoryIcon.setImageUrl(photo_url, imgloader);
			views[j].setTag(prod_id_recent_prod);
			innerLayout.addView(views[j]);
			views[j].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String pid = String.valueOf(v.getTag());
					getProductInfoById(pid);
				}
			});
		}

	}

	private void writeJustViewdProdAndGet() {
		// TODO Auto-generated method stub

		// SharedPreferences
		// share=Utilities.getSharedPrefs(context.getApplicationContext());
		ProductDataBean pdataBean = Utilities.getProductdataBean(activity,
				medId);
		int totalCount = (Utilities.getTotalCount(activity)) + 1;
		if (totalCount == 0) {
			totalCount = 0;
			Utilities.writejustViewProdToSharedPref(activity, pdataBean,
					totalCount);
		} else {
			Utilities.writejustViewProdToSharedPref(activity, pdataBean,
					totalCount);
		}
		Utilities.writeTotalCount(activity, "totalRelatedProduct", totalCount);

	}

	private void share(final int prodId) {
		shareBtn = (ImageView) findViewById(R.id.shareIV);
		shareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<ProductDataBean> pArrList = Okler.getInstance()
						.getProdList();

				for (int i = 0; i < pArrList.size(); i++) {
					int id = pArrList.get(i).getProdId();
					if (id == prodId) {
						String prodName = pArrList.get(i).getProdName();
						shareProd(prodName);
					}

				}
			}
		});
	}

	private void keyFeature() {
		// TODO Auto-generated method stub
		TextView keyDetail;

		minus_button_Hskey = (ImageView) customViewKeyFeature
				.findViewById(R.id.minusButton_indi);
		Hskey_details_RL = (RelativeLayout) customViewKeyFeature
				.findViewById(R.id.indi_details_RL);
		TextView Hskey_heading = (TextView) customViewKeyFeature
				.findViewById(R.id.indi_title);
		Hskey_heading.setText("KEY FEATURES");
		keyDetail = (TextView) customViewKeyFeature.findViewById(R.id.details);
		RelativeLayout title_RL = (RelativeLayout)customViewKeyFeature.
				findViewById(R.id.title_RL);
		desc_Main_RL.addView(customViewKeyFeature);
		customViewKeyFeature.setLayoutParams(params);
		customViewKeyFeature.requestLayout();
		try {
			JSONObject jsonAllo = new JSONObject(medId);
			String feature = jsonAllo.getString("key_feature");
			keyDetail.setText(Html.fromHtml(feature));
		} catch (Exception e) {
			// TODO: handle exception
		}
		add_button_Hskey = (ImageView) customViewKeyFeature
				.findViewById(R.id.add_Button_indi);
		
		title_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_button_Hskey.getVisibility()==0){
					add_button_Hskey.setVisibility(View.GONE);
					minus_button_Hskey.setVisibility(View.VISIBLE);
					Hskey_details_RL.setVisibility(View.VISIBLE);

				add_button_HSdesc.setVisibility(View.VISIBLE);
				add_button_HSspec.setVisibility(View.VISIBLE);
				add_button_HSbrand.setVisibility(View.VISIBLE);
				add_button_HSwar.setVisibility(View.VISIBLE);

				minus_button_HSdesc.setVisibility(View.GONE);
				minus_button_HSspec.setVisibility(View.GONE);
				minus_button_HSbrand.setVisibility(View.GONE);
				minus_button_HSwar.setVisibility(View.GONE);

					HSdesc_details_RL.setVisibility(View.GONE);
					HSspec_details_RL.setVisibility(View.GONE);
					HSbrand_details_RL.setVisibility(View.GONE);
					HSwar_details_RL.setVisibility(View.GONE);
				}else{
					add_button_Hskey.setVisibility(View.VISIBLE);
					minus_button_Hskey.setVisibility(View.GONE);
					Hskey_details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		/*add_button_Hskey.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_button_Hskey.setVisibility(View.GONE);
				minus_button_Hskey.setVisibility(View.VISIBLE);
				Hskey_details_RL.setVisibility(View.VISIBLE);

				add_button_HSdesc.setVisibility(View.VISIBLE);
				add_button_HSspec.setVisibility(View.VISIBLE);
				add_button_HSbrand.setVisibility(View.VISIBLE);
				add_button_HSwar.setVisibility(View.VISIBLE);

				minus_button_HSdesc.setVisibility(View.GONE);
				minus_button_HSspec.setVisibility(View.GONE);
				minus_button_HSbrand.setVisibility(View.GONE);
				minus_button_HSwar.setVisibility(View.GONE);

				HSdesc_details_RL.setVisibility(View.GONE);
				HSspec_details_RL.setVisibility(View.GONE);
				HSbrand_details_RL.setVisibility(View.GONE);
				HSwar_details_RL.setVisibility(View.GONE);

			}
		});
		minus_button_Hskey.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_button_Hskey.setVisibility(View.VISIBLE);
				minus_button_Hskey.setVisibility(View.GONE);
				Hskey_details_RL.setVisibility(View.GONE);

			}
		});*/
	}

	private void Descrption() {
		// TODO Auto-generated method stub
		TextView descDetail;
		add_button_HSdesc = (ImageView) customViewDescption
				.findViewById(R.id.add_Button_indi);
		minus_button_HSdesc = (ImageView) customViewDescption
				.findViewById(R.id.minusButton_indi);
		HSdesc_details_RL = (RelativeLayout) customViewDescption
				.findViewById(R.id.indi_details_RL);
		TextView HSdesc_heading = (TextView) customViewDescption
				.findViewById(R.id.indi_title);
		HSdesc_heading.setText("DESCRIPTION");
		RelativeLayout title_RL = (RelativeLayout)customViewDescption.
				findViewById(R.id.title_RL);
		descDetail = (TextView) customViewDescption.findViewById(R.id.details);
		desc_Main_RL.addView(customViewDescption);
		customViewDescption.setLayoutParams(params);
		customViewDescption.requestLayout();
		try {
			JSONObject jsonAllo = new JSONObject(medId);
			descDetail.setText(jsonAllo.getString("desc"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		title_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_button_HSdesc.getVisibility()==0){
					add_button_HSdesc.setVisibility(View.GONE);
					minus_button_HSdesc.setVisibility(View.VISIBLE);
					HSdesc_details_RL.setVisibility(View.VISIBLE);

				add_button_Hskey.setVisibility(View.VISIBLE);
				add_button_HSspec.setVisibility(View.VISIBLE);
				add_button_HSbrand.setVisibility(View.VISIBLE);
				add_button_HSwar.setVisibility(View.VISIBLE);

					minus_button_Hskey.setVisibility(View.GONE);
					minus_button_HSspec.setVisibility(View.GONE);
					minus_button_HSbrand.setVisibility(View.GONE);
					minus_button_HSwar.setVisibility(View.GONE);

					Hskey_details_RL.setVisibility(View.GONE);
					HSspec_details_RL.setVisibility(View.GONE);
					HSbrand_details_RL.setVisibility(View.GONE);
					HSwar_details_RL.setVisibility(View.GONE);
				}else{
					add_button_HSdesc.setVisibility(View.VISIBLE);
					minus_button_HSdesc.setVisibility(View.GONE);
					HSdesc_details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		
		/*add_button_HSdesc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_button_HSdesc.setVisibility(View.GONE);
				minus_button_HSdesc.setVisibility(View.VISIBLE);
				HSdesc_details_RL.setVisibility(View.VISIBLE);

				add_button_Hskey.setVisibility(View.VISIBLE);
				add_button_HSspec.setVisibility(View.VISIBLE);
				add_button_HSbrand.setVisibility(View.VISIBLE);
				add_button_HSwar.setVisibility(View.VISIBLE);

				minus_button_Hskey.setVisibility(View.GONE);
				minus_button_HSspec.setVisibility(View.GONE);
				minus_button_HSbrand.setVisibility(View.GONE);
				minus_button_HSwar.setVisibility(View.GONE);

				Hskey_details_RL.setVisibility(View.GONE);
				HSspec_details_RL.setVisibility(View.GONE);
				HSbrand_details_RL.setVisibility(View.GONE);
				HSwar_details_RL.setVisibility(View.GONE);

			}
		});
		minus_button_HSdesc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_button_HSdesc.setVisibility(View.VISIBLE);
				minus_button_HSdesc.setVisibility(View.GONE);
				HSdesc_details_RL.setVisibility(View.GONE);

			}
		});*/

	}

	private void specification() {
		// TODO Auto-generated method stub
		TextView specDetail;
		add_button_HSspec = (ImageView) customViewSpeci
				.findViewById(R.id.add_Button_indi);
		minus_button_HSspec = (ImageView) customViewSpeci
				.findViewById(R.id.minusButton_indi);
		HSspec_details_RL = (RelativeLayout) customViewSpeci
				.findViewById(R.id.indi_details_RL);
		TextView HSspec_heading = (TextView) customViewSpeci
				.findViewById(R.id.indi_title);
		HSspec_heading.setText("SPECIFICATION");
		specDetail = (TextView) customViewSpeci.findViewById(R.id.details);
		desc_Main_RL.addView(customViewSpeci);
		RelativeLayout title_RL = (RelativeLayout)customViewSpeci.
				findViewById(R.id.title_RL);
		customViewSpeci.setLayoutParams(params);
		customViewSpeci.requestLayout();
		try {
			JSONObject jsonAllo = new JSONObject(medId);
			specDetail.setText(jsonAllo.getString("specfic"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		title_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_button_HSspec.getVisibility()==0){
					add_button_HSspec.setVisibility(View.GONE);
					minus_button_HSspec.setVisibility(View.VISIBLE);
					HSspec_details_RL.setVisibility(View.VISIBLE);

				add_button_Hskey.setVisibility(View.VISIBLE);
				add_button_HSdesc.setVisibility(View.VISIBLE);
				add_button_HSbrand.setVisibility(View.VISIBLE);
				add_button_HSwar.setVisibility(View.VISIBLE);

				minus_button_Hskey.setVisibility(View.GONE);
				minus_button_HSdesc.setVisibility(View.GONE);
				minus_button_HSbrand.setVisibility(View.GONE);
				minus_button_HSwar.setVisibility(View.GONE);

					Hskey_details_RL.setVisibility(View.GONE);
					HSdesc_details_RL.setVisibility(View.GONE);
					HSbrand_details_RL.setVisibility(View.GONE);
					HSwar_details_RL.setVisibility(View.GONE);
				}else{
					add_button_HSspec.setVisibility(View.VISIBLE);
					minus_button_HSspec.setVisibility(View.GONE);
					HSspec_details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		/*add_button_HSspec.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_button_HSspec.setVisibility(View.GONE);
				minus_button_HSspec.setVisibility(View.VISIBLE);
				HSspec_details_RL.setVisibility(View.VISIBLE);

				add_button_Hskey.setVisibility(View.VISIBLE);
				add_button_HSdesc.setVisibility(View.VISIBLE);
				add_button_HSbrand.setVisibility(View.VISIBLE);
				add_button_HSwar.setVisibility(View.VISIBLE);

				minus_button_Hskey.setVisibility(View.GONE);
				minus_button_HSdesc.setVisibility(View.GONE);
				minus_button_HSbrand.setVisibility(View.GONE);
				minus_button_HSwar.setVisibility(View.GONE);

				Hskey_details_RL.setVisibility(View.GONE);
				HSdesc_details_RL.setVisibility(View.GONE);
				HSbrand_details_RL.setVisibility(View.GONE);
				HSwar_details_RL.setVisibility(View.GONE);

			}
		});
		minus_button_HSspec.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_button_HSspec.setVisibility(View.VISIBLE);
				minus_button_HSspec.setVisibility(View.GONE);
				HSspec_details_RL.setVisibility(View.GONE);

			}
		});*/

	}

	private void brandInfo() {
		// TODO Auto-generated method stub
		TextView bInfoDetail;
		add_button_HSbrand = (ImageView) customViewBInfo
				.findViewById(R.id.add_Button_indi);
		minus_button_HSbrand = (ImageView) customViewBInfo
				.findViewById(R.id.minusButton_indi);
		HSbrand_details_RL = (RelativeLayout) customViewBInfo
				.findViewById(R.id.indi_details_RL);
		TextView HSbrand_heading = (TextView) customViewBInfo
				.findViewById(R.id.indi_title);
		HSbrand_heading.setText("BRAND INFO");
		RelativeLayout title_RL = (RelativeLayout)customViewBInfo.
				findViewById(R.id.title_RL);
		bInfoDetail = (TextView) customViewBInfo.findViewById(R.id.details);
		desc_Main_RL.addView(customViewBInfo);
		customViewBInfo.setLayoutParams(params);
		customViewBInfo.requestLayout();
		try {
			JSONObject jsonAllo = new JSONObject(medId);
			bInfoDetail.setText(jsonAllo.getString("brandInfo"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		title_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_button_HSbrand.getVisibility()==0){
					add_button_HSbrand.setVisibility(View.GONE);
					minus_button_HSbrand.setVisibility(View.VISIBLE);
					HSbrand_details_RL.setVisibility(View.VISIBLE);

					add_button_Hskey.setVisibility(View.VISIBLE);
					add_button_HSdesc.setVisibility(View.VISIBLE);
					add_button_HSspec.setVisibility(View.VISIBLE);
					add_button_HSwar.setVisibility(View.VISIBLE);

					minus_button_Hskey.setVisibility(View.GONE);
					minus_button_HSdesc.setVisibility(View.GONE);
					minus_button_HSspec.setVisibility(View.GONE);
					minus_button_HSwar.setVisibility(View.GONE);

					Hskey_details_RL.setVisibility(View.GONE);
					HSdesc_details_RL.setVisibility(View.GONE);
					HSspec_details_RL.setVisibility(View.GONE);
					HSwar_details_RL.setVisibility(View.GONE);
				}else{
					add_button_HSbrand.setVisibility(View.VISIBLE);
					minus_button_HSbrand.setVisibility(View.GONE);
					HSbrand_details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		/*add_button_HSbrand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_button_HSbrand.setVisibility(View.GONE);
				minus_button_HSbrand.setVisibility(View.VISIBLE);
				HSbrand_details_RL.setVisibility(View.VISIBLE);

				add_button_Hskey.setVisibility(View.VISIBLE);
				add_button_HSdesc.setVisibility(View.VISIBLE);
				add_button_HSspec.setVisibility(View.VISIBLE);
				add_button_HSwar.setVisibility(View.VISIBLE);

				minus_button_Hskey.setVisibility(View.GONE);
				minus_button_HSdesc.setVisibility(View.GONE);
				minus_button_HSspec.setVisibility(View.GONE);
				minus_button_HSwar.setVisibility(View.GONE);

				Hskey_details_RL.setVisibility(View.GONE);
				HSdesc_details_RL.setVisibility(View.GONE);
				HSspec_details_RL.setVisibility(View.GONE);
				HSwar_details_RL.setVisibility(View.GONE);

			}
		});
		minus_button_HSbrand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_button_HSbrand.setVisibility(View.VISIBLE);
				minus_button_HSbrand.setVisibility(View.GONE);
				HSbrand_details_RL.setVisibility(View.GONE);

			}
		});*/

	}

	private void wrrant() {
		// TODO Auto-generated method stub
		TextView warrantDetail;
		add_button_HSwar = (ImageView) customViewWarrant
				.findViewById(R.id.add_Button_indi);
		minus_button_HSwar = (ImageView) customViewWarrant
				.findViewById(R.id.minusButton_indi);
		HSwar_details_RL = (RelativeLayout) customViewWarrant
				.findViewById(R.id.indi_details_RL);
		TextView HSwar_heading = (TextView) customViewWarrant
				.findViewById(R.id.indi_title);
		HSwar_heading.setText("WARRANTY");
		RelativeLayout title_RL = (RelativeLayout)customViewWarrant.
				findViewById(R.id.title_RL);
		warrantDetail = (TextView) customViewWarrant.findViewById(R.id.details);
		desc_Main_RL.addView(customViewWarrant);
		customViewWarrant.setLayoutParams(params);
		customViewWarrant.requestLayout();
		try {
			JSONObject jsonAllo = new JSONObject(medId);
			warrantDetail.setText(jsonAllo.getString("warranty"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		title_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_button_HSwar.getVisibility()==0){
					add_button_HSwar.setVisibility(View.GONE);
					minus_button_HSwar.setVisibility(View.VISIBLE);
					HSwar_details_RL.setVisibility(View.VISIBLE);

				add_button_Hskey.setVisibility(View.VISIBLE);
				add_button_HSdesc.setVisibility(View.VISIBLE);
				add_button_HSspec.setVisibility(View.VISIBLE);
				add_button_HSbrand.setVisibility(View.VISIBLE);

				minus_button_Hskey.setVisibility(View.GONE);
				minus_button_HSdesc.setVisibility(View.GONE);
				minus_button_HSspec.setVisibility(View.GONE);
				minus_button_HSbrand.setVisibility(View.GONE);

					Hskey_details_RL.setVisibility(View.GONE);
					HSdesc_details_RL.setVisibility(View.GONE);
					HSspec_details_RL.setVisibility(View.GONE);
					HSbrand_details_RL.setVisibility(View.GONE);
				}else{
					add_button_HSwar.setVisibility(View.VISIBLE);
					minus_button_HSwar.setVisibility(View.GONE);
					HSwar_details_RL.setVisibility(View.GONE);
				}
				
				
			}
		});
		/*add_button_HSwar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_button_HSwar.setVisibility(View.GONE);
				minus_button_HSwar.setVisibility(View.VISIBLE);
				HSwar_details_RL.setVisibility(View.VISIBLE);

				add_button_Hskey.setVisibility(View.VISIBLE);
				add_button_HSdesc.setVisibility(View.VISIBLE);
				add_button_HSspec.setVisibility(View.VISIBLE);
				add_button_HSbrand.setVisibility(View.VISIBLE);

				minus_button_Hskey.setVisibility(View.GONE);
				minus_button_HSdesc.setVisibility(View.GONE);
				minus_button_HSspec.setVisibility(View.GONE);
				minus_button_HSbrand.setVisibility(View.GONE);

				Hskey_details_RL.setVisibility(View.GONE);
				HSdesc_details_RL.setVisibility(View.GONE);
				HSspec_details_RL.setVisibility(View.GONE);
				HSbrand_details_RL.setVisibility(View.GONE);

			}
		});
		minus_button_HSwar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_button_HSwar.setVisibility(View.VISIBLE);
				minus_button_HSwar.setVisibility(View.GONE);
				HSwar_details_RL.setVisibility(View.GONE);
			}
		});*/

	}

	private void displayJustViewedImage() {
		// TODO Auto-generated method stub
		String photo_url = null;
		String prodname = null;
		int prodCount = Utilities.getTotalCount(activity);

		LinearLayout innerLayout = (LinearLayout) findViewById(R.id.innerLayout2);
		View[] views = new View[prodCount];
		for (int cnt = 0; cnt < prodCount; cnt++) {
			String getProd = Utilities.getjustViewProd(activity, cnt + 1);
			try {
				JSONObject jsonAllo = new JSONObject(getProd);
				prod_id_recent_prod = String.valueOf(jsonAllo
						.getString("prodId"));
				// prod_id_list.add(prod_id_recent_prod);
				String p = prod_id_recent_prod;
				prodname = jsonAllo.getString("prodName");
				String imageUrl = String.valueOf(jsonAllo.getString("imgUrl"));
				// First=
				// "http://183.82.110.105:8081/oklerdevv2/uploads/images/medium/";
				photo_url = First + imageUrl;
			} catch (Exception e) {
				// TODO: handle exception
			}
			views[cnt] = getLayoutInflater().inflate(
					R.layout.include_hs_related_products, null);
			// ImageView
			// jsutViewd=(ImageView)views[cnt].findViewById(R.id.relatedImage1);
			ImageLoader imgloader = VolleyRequest.getInstance(
					getApplicationContext()).getImageLoader();
			TextView imageName = (TextView) views[cnt]
					.findViewById(R.id.relatedImageName);
			imageName.setText(prodname);
			URL url;
			NetworkImageView imgCategoryIcon = (NetworkImageView) views[cnt]
					.findViewById(R.id.relatedImage1);
			imgloader = VolleyRequest.getInstance(getApplicationContext())
					.getImageLoader();
			imgCategoryIcon.setImageUrl(photo_url, imgloader);

			views[cnt].setTag(prod_id_recent_prod);
			innerLayout.addView(views[cnt]);
			views[cnt].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String pid = String.valueOf(v.getTag());
					getProductInfoById(pid);

				}
			});

		}
	}

	private void displaySimilarProd() {
		final LinearLayout innerLayout = (LinearLayout) findViewById(R.id.innerLayout1);
		// TODO Auto-generated method stub
		String prod_id = null, prodname;
		try {
			JSONObject jsonAllo = new JSONObject(medId);
			prodname = jsonAllo.getString("prodName");
		} catch (Exception e) {
			// TODO: handle exception
		}
		// get related product details..
		String getRelProduct = getString(R.string.relatedProdsHs) + prodId;

		WebJsonObjectRequest addjson = new WebJsonObjectRequest(Method.GET,
				getRelProduct, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						JSONObject jboj = (JSONObject) response;
						relatedPro = new JSONArray();
						try {
							String msg = jboj.optString("message");
							if (msg.equals("No data found.")) {
								txt_item.setVisibility(View.VISIBLE);
								txt_item.setText("No items to show");
							} else {
								JSONArray jarr = new JSONArray();
								jarr = jboj.getJSONArray("result");
								for (int i = 0; i < jarr.length(); i++) {
									/*
									 * if(relatedPro.equals(null)){
									 * Toast.makeText
									 * (context,"relatedProduct array is null"
									 * ,Toast.LENGTH_LONG); }else{
									 */
									JSONObject jObject = jarr.getJSONObject(i);
									prod_id_similar_prod = jObject
											.optString("id");
									String name = jObject.optString("name");
									String jimg = jObject.optString("images");
									String url2;
									if (jimg.equals(null)) {
										url2 = "drawable://"
												+ R.drawable.no_image_found;
									} else {
										if (jimg.contains(",")) {
											String arr1[] = jimg.split(":");
											String sec = arr1[2];
											String arr2[] = sec.split("\"");
											url2 = arr2[1];
										} else {
											url2 = jimg;
										}
									}
									String image_name = "";
									try {
										image_name = URLEncoder.encode(url2,
												"UTF-8");
									} catch (UnsupportedEncodingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									addCustRelatedProducts(innerLayout, name,
											image_name, prod_id_similar_prod);
									// }
								}
							}

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							// Toast.makeText(getApplicationContext(),
							// String.valueOf(e), Toast.LENGTH_LONG).show();

						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

						// Toast.makeText(getApplicationContext(),
						// String.valueOf(error), Toast.LENGTH_LONG).show();

					}
				});
		VolleyRequest.addJsonObjectRequest(getApplicationContext(), addjson);

	}

	private void addCustRelatedProducts(LinearLayout innerLayout, String name,
			String imageName2, String pid) {

		// TODO Auto-generated method stub
		// String
		// Url="http://183.82.110.105:8081/oklerdevv2/uploads/images/medium/";
		String photo_url = First + imageName2;
		String prodname = name;

		View[] views = new View[1];
		for (int cnt = 0; cnt < 1; cnt++) {
			views[cnt] = getLayoutInflater().inflate(
					R.layout.include_hs_related_products, null);
			NetworkImageView jsutViewd = (NetworkImageView) views[cnt]
					.findViewById(R.id.relatedImage1);
			ImageLoader imgloader = VolleyRequest.getInstance(
					getApplicationContext()).getImageLoader();
			TextView imageName = (TextView) views[cnt]
					.findViewById(R.id.relatedImageName);
			imageName.setText(prodname);
			jsutViewd.setImageUrl(photo_url, imgloader);
			URL url;
			views[cnt].setTag(pid);
			innerLayout.addView(views[cnt]);

			views[cnt].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// ProductDataBean pbean =
					String id = String.valueOf(v.getTag());
					getProductInfoById(id);
				}
			});
		}

	}

	private void alloUI() {

		TextView descdetailsAllo, indidetailsAllo, cindidetailsAllo, cautiondetailsAllo, side_effdetailsAllo, weightdetailsAllo, dosagedetailsAllo;
		TextView item_description = (TextView) findViewById(R.id.gen_name);
		item_description.setVisibility(View.VISIBLE);

		customViewDesc = new CustomViewProdInfo(this);
		customViewIndi = new CustomViewProdInfo(this);
		customViewCIndi = new CustomViewProdInfo(this);
		customViewCaution = new CustomViewProdInfo(this);
		customViewSEff = new CustomViewProdInfo(this);
		customViewWeight = new CustomViewProdInfo(this);
		customViewDosage = new CustomViewProdInfo(this);

		add_button_desc = (ImageView) customViewDesc
				.findViewById(R.id.add_Button_indi);
		minus_button_desc = (ImageView) customViewDesc
				.findViewById(R.id.minusButton_indi);
		desc_details_RL = (RelativeLayout) customViewDesc
				.findViewById(R.id.indi_details_RL);
		desc_heading = (TextView) customViewDesc.findViewById(R.id.indi_title);
		desc_heading.setText(getString(R.string.description));
		RelativeLayout desctitle_RL = (RelativeLayout)customViewDesc.
				findViewById(R.id.title_RL);
		descdetailsAllo = (TextView) customViewDesc.findViewById(R.id.details);
		desc_Main_RL.addView(customViewDesc);
		customViewDesc.setLayoutParams(params);
		customViewDesc.requestLayout();

		add_indi = (ImageView) customViewIndi
				.findViewById(R.id.add_Button_indi);
		minus_indi = (ImageView) customViewIndi
				.findViewById(R.id.minusButton_indi);
		indi_details_RL = (RelativeLayout) customViewIndi
				.findViewById(R.id.indi_details_RL);
		indi_heading = (TextView) customViewIndi.findViewById(R.id.indi_title);
		indi_heading.setText(getString(R.string.indication));
		RelativeLayout indititle_RL = (RelativeLayout)customViewIndi.
				findViewById(R.id.title_RL);
		indidetailsAllo = (TextView) customViewIndi.findViewById(R.id.details);
		desc_Main_RL.addView(customViewIndi);
		customViewIndi.setLayoutParams(params);
		customViewIndi.requestLayout();

		add_contra_indi = (ImageView) customViewCIndi
				.findViewById(R.id.add_Button_indi);
		minus_contra_indi = (ImageView) customViewCIndi
				.findViewById(R.id.minusButton_indi);
		contra_indi_details_RL = (RelativeLayout) customViewCIndi
				.findViewById(R.id.indi_details_RL);
		cIndi_heading = (TextView) customViewCIndi
				.findViewById(R.id.indi_title);
		cIndi_heading.setText(getString(R.string.contra_indication));
		RelativeLayout cindititle_RL = (RelativeLayout)customViewCIndi.
				findViewById(R.id.title_RL);
		cindidetailsAllo = (TextView) customViewCIndi
				.findViewById(R.id.details);
		desc_Main_RL.addView(customViewCIndi);
		customViewCIndi.setLayoutParams(params);
		customViewCIndi.requestLayout();

		add_caution = (ImageView) customViewCaution
				.findViewById(R.id.add_Button_indi);
		minus_caution = (ImageView) customViewCaution
				.findViewById(R.id.minusButton_indi);
		caution_details_RL = (RelativeLayout) customViewCaution
				.findViewById(R.id.indi_details_RL);
		caution_heading = (TextView) customViewCaution
				.findViewById(R.id.indi_title);
		caution_heading.setText(getString(R.string.caution));
		RelativeLayout cautiontitle_RL = (RelativeLayout)customViewCaution.
				findViewById(R.id.title_RL);
		cautiondetailsAllo = (TextView) customViewCaution
				.findViewById(R.id.details);
		desc_Main_RL.addView(customViewCaution);
		customViewCaution.setLayoutParams(params);
		customViewCaution.requestLayout();

		add_side_eff = (ImageView) customViewSEff
				.findViewById(R.id.add_Button_indi);// 13_01_2016 Gitesh
		minus_side_eff = (ImageView) customViewSEff
				.findViewById(R.id.minusButton_indi);// 13_01_2016 Gitesh
		side_eff_details_RL = (RelativeLayout) customViewSEff
				.findViewById(R.id.indi_details_RL);// 13_01_2016 Gitesh
		sEff_heading = (TextView) customViewSEff.findViewById(R.id.indi_title);
		sEff_heading.setText(getString(R.string.side_effect));
		RelativeLayout sEfftitle_RL = (RelativeLayout)customViewSEff.
				findViewById(R.id.title_RL);
		side_effdetailsAllo = (TextView) customViewSEff
				.findViewById(R.id.details);
		desc_Main_RL.addView(customViewSEff);
		customViewSEff.setLayoutParams(params);
		customViewSEff.requestLayout();

		add_weight = (ImageView) customViewWeight
				.findViewById(R.id.add_Button_indi);
		minus_weight = (ImageView) customViewWeight
				.findViewById(R.id.minusButton_indi);
		weight_details_RL = (RelativeLayout) customViewWeight
				.findViewById(R.id.indi_details_RL);// 13_01_2016 Gitesh
		weight_heading = (TextView) customViewWeight
				.findViewById(R.id.indi_title);
		weight_heading.setText(getString(R.string.weight));
		RelativeLayout weighttitle_RL = (RelativeLayout)customViewWeight.
				findViewById(R.id.title_RL);
		weightdetailsAllo = (TextView) customViewWeight
				.findViewById(R.id.details);
		desc_Main_RL.addView(customViewWeight);
		customViewWeight.setLayoutParams(params);
		customViewWeight.requestLayout();

		add_dosage = (ImageView) customViewDosage
				.findViewById(R.id.add_Button_indi);
		minus_dosage = (ImageView) customViewDosage
				.findViewById(R.id.minusButton_indi);
		dosage_Details_RL = (RelativeLayout) customViewDosage
				.findViewById(R.id.indi_details_RL);// 13_01_2016 Gitesh
		dosage_heading = (TextView) customViewDosage
				.findViewById(R.id.indi_title);
		dosage_heading.setText(getString(R.string.dosage));
		RelativeLayout dosagetitle_RL = (RelativeLayout)customViewDosage.
				findViewById(R.id.title_RL);
		dosagedetailsAllo = (TextView) customViewDosage
				.findViewById(R.id.details);
		desc_Main_RL.addView(customViewDosage);
		customViewDosage.setLayoutParams(params);
		customViewDosage.requestLayout();

		try {
			JSONObject jsonAllo = new JSONObject(medId);
			descdetailsAllo.setText(jsonAllo.optString("desc"));
			indidetailsAllo.setText(jsonAllo.optString("indications"));
			cindidetailsAllo.setText(jsonAllo.optString("contraIndi"));
			cautiondetailsAllo.setText(jsonAllo.optString("caution"));
			side_effdetailsAllo.setText(jsonAllo.optString("side_effect"));
			weightdetailsAllo.setText(jsonAllo.optString("weight"));
			dosagedetailsAllo.setText(Html.fromHtml(jsonAllo
					.optString("dosage")));
			String generic_name = jsonAllo.optString("generic_name");
			if (generic_name.equals("") || generic_name.equals("null")
					|| generic_name.equals(null) || generic_name == null
					|| generic_name.length() == 0) {
				item_description.setVisibility(View.GONE);
			} else {
				item_description.setText(generic_name);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		desctitle_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_button_desc.getVisibility()==0){
					add_button_desc.setVisibility(View.GONE);
					minus_button_desc.setVisibility(View.VISIBLE);
					desc_details_RL.setVisibility(View.VISIBLE);
					add_caution.setVisibility(View.VISIBLE);
					minus_caution.setVisibility(View.GONE);
					caution_details_RL.setVisibility(View.GONE);
					add_contra_indi.setVisibility(View.VISIBLE);
					minus_contra_indi.setVisibility(View.GONE);
					contra_indi_details_RL.setVisibility(View.GONE);
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
					add_side_eff.setVisibility(View.VISIBLE);
					minus_side_eff.setVisibility(View.GONE);
					side_eff_details_RL.setVisibility(View.GONE);
					add_weight.setVisibility(View.VISIBLE);
					minus_weight.setVisibility(View.GONE);
					weight_details_RL.setVisibility(View.GONE);
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);
				}else{
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		
		/*add_button_desc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_button_desc.setVisibility(View.GONE);
				minus_button_desc.setVisibility(View.VISIBLE);
				desc_details_RL.setVisibility(View.VISIBLE);
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);
				add_side_eff.setVisibility(View.VISIBLE);
				minus_side_eff.setVisibility(View.GONE);
				side_eff_details_RL.setVisibility(View.GONE);
				add_weight.setVisibility(View.VISIBLE);
				minus_weight.setVisibility(View.GONE);
				weight_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);
			}
		});
		minus_button_desc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);

			}
		});*/

		// add minus buttons for indicator

		indititle_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_indi.getVisibility()==0){
					add_indi.setVisibility(View.GONE);
					minus_indi.setVisibility(View.VISIBLE);
					indi_details_RL.setVisibility(View.VISIBLE);
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
					add_contra_indi.setVisibility(View.VISIBLE);
					minus_contra_indi.setVisibility(View.GONE);
					contra_indi_details_RL.setVisibility(View.GONE);
					add_caution.setVisibility(View.VISIBLE);
					minus_caution.setVisibility(View.GONE);
					caution_details_RL.setVisibility(View.GONE);
					add_side_eff.setVisibility(View.VISIBLE);
					minus_side_eff.setVisibility(View.GONE);
					side_eff_details_RL.setVisibility(View.GONE);
					add_weight.setVisibility(View.VISIBLE);
					minus_weight.setVisibility(View.GONE);
					weight_details_RL.setVisibility(View.GONE);
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);
				}else{
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		/*add_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_indi.setVisibility(View.GONE);
				minus_indi.setVisibility(View.VISIBLE);
				indi_details_RL.setVisibility(View.VISIBLE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);
				add_side_eff.setVisibility(View.VISIBLE);
				minus_side_eff.setVisibility(View.GONE);
				side_eff_details_RL.setVisibility(View.GONE);
				add_weight.setVisibility(View.VISIBLE);
				minus_weight.setVisibility(View.GONE);
				weight_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);

			}
		});
		minus_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);

			}
		});
*/
		// add minus buttons for contrA
		
		cindititle_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_contra_indi.getVisibility()==0){
					add_contra_indi.setVisibility(View.GONE);
					minus_contra_indi.setVisibility(View.VISIBLE);
					contra_indi_details_RL.setVisibility(View.VISIBLE);
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
					add_caution.setVisibility(View.VISIBLE);
					minus_caution.setVisibility(View.GONE);
					caution_details_RL.setVisibility(View.GONE);
					add_side_eff.setVisibility(View.VISIBLE);
					minus_side_eff.setVisibility(View.GONE);
					side_eff_details_RL.setVisibility(View.GONE);
					add_weight.setVisibility(View.VISIBLE);
					minus_weight.setVisibility(View.GONE);
					weight_details_RL.setVisibility(View.GONE);
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);
				}else{
					add_contra_indi.setVisibility(View.VISIBLE);
					minus_contra_indi.setVisibility(View.GONE);
					contra_indi_details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		/*add_contra_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_contra_indi.setVisibility(View.GONE);
				minus_contra_indi.setVisibility(View.VISIBLE);
				contra_indi_details_RL.setVisibility(View.VISIBLE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);
				add_side_eff.setVisibility(View.VISIBLE);
				minus_side_eff.setVisibility(View.GONE);
				side_eff_details_RL.setVisibility(View.GONE);
				add_weight.setVisibility(View.VISIBLE);
				minus_weight.setVisibility(View.GONE);
				weight_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);

			}
		});
		minus_contra_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
			}
		});
*/
		// add minus buttons for cautions
		cautiontitle_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_caution.getVisibility()==0){
					add_caution.setVisibility(View.GONE);
					minus_caution.setVisibility(View.VISIBLE);
					caution_details_RL.setVisibility(View.VISIBLE);
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
					add_contra_indi.setVisibility(View.VISIBLE);
					minus_contra_indi.setVisibility(View.GONE);
					contra_indi_details_RL.setVisibility(View.GONE);
					add_side_eff.setVisibility(View.VISIBLE);
					minus_side_eff.setVisibility(View.GONE);
					side_eff_details_RL.setVisibility(View.GONE);
					add_weight.setVisibility(View.VISIBLE);
					minus_weight.setVisibility(View.GONE);
					weight_details_RL.setVisibility(View.GONE);
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);
				}else{
					add_caution.setVisibility(View.VISIBLE);
					minus_caution.setVisibility(View.GONE);
					caution_details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		/*add_caution.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_caution.setVisibility(View.GONE);
				minus_caution.setVisibility(View.VISIBLE);
				caution_details_RL.setVisibility(View.VISIBLE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
				add_side_eff.setVisibility(View.VISIBLE);
				minus_side_eff.setVisibility(View.GONE);
				side_eff_details_RL.setVisibility(View.GONE);
				add_weight.setVisibility(View.VISIBLE);
				minus_weight.setVisibility(View.GONE);
				weight_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);

			}

		});
		minus_caution.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);

			}
		});*/
		// 13_01_2016 Gitesh start
		// add minus buttons for side effects
		sEfftitle_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_side_eff.getVisibility()==0){
					add_caution.setVisibility(View.VISIBLE);
					minus_caution.setVisibility(View.GONE);
					caution_details_RL.setVisibility(View.GONE);
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
					add_contra_indi.setVisibility(View.VISIBLE);
					minus_contra_indi.setVisibility(View.GONE);
					contra_indi_details_RL.setVisibility(View.GONE);
					add_side_eff.setVisibility(View.GONE);
					minus_side_eff.setVisibility(View.VISIBLE);
					side_eff_details_RL.setVisibility(View.VISIBLE);
					add_weight.setVisibility(View.VISIBLE);
					minus_weight.setVisibility(View.GONE);
					weight_details_RL.setVisibility(View.GONE);
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);
				}else{
					add_side_eff.setVisibility(View.VISIBLE);
					minus_side_eff.setVisibility(View.GONE);
					side_eff_details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		/*add_side_eff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
				add_side_eff.setVisibility(View.GONE);
				minus_side_eff.setVisibility(View.VISIBLE);
				side_eff_details_RL.setVisibility(View.VISIBLE);
				add_weight.setVisibility(View.VISIBLE);
				minus_weight.setVisibility(View.GONE);
				weight_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);
			}
		});
		minus_side_eff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_side_eff.setVisibility(View.VISIBLE);
				minus_side_eff.setVisibility(View.GONE);
				side_eff_details_RL.setVisibility(View.GONE);
			}
		});*/
		// 13_01_2016 Gitesh end
		weighttitle_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_weight.getVisibility()==0){
					add_caution.setVisibility(View.VISIBLE);
					minus_caution.setVisibility(View.GONE);
					caution_details_RL.setVisibility(View.GONE);
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
					add_contra_indi.setVisibility(View.VISIBLE);
					minus_contra_indi.setVisibility(View.GONE);
					contra_indi_details_RL.setVisibility(View.GONE);
					add_side_eff.setVisibility(View.VISIBLE);
					minus_side_eff.setVisibility(View.GONE);
					side_eff_details_RL.setVisibility(View.GONE);
					add_weight.setVisibility(View.GONE);
					minus_weight.setVisibility(View.VISIBLE);
					weight_details_RL.setVisibility(View.VISIBLE);
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);
				}else{
					add_weight.setVisibility(View.VISIBLE);
					minus_weight.setVisibility(View.GONE);
					weight_details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		/*add_weight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
				add_side_eff.setVisibility(View.VISIBLE);
				minus_side_eff.setVisibility(View.GONE);
				side_eff_details_RL.setVisibility(View.GONE);
				add_weight.setVisibility(View.GONE);
				minus_weight.setVisibility(View.VISIBLE);
				weight_details_RL.setVisibility(View.VISIBLE);
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);

			}
		});
		minus_weight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_weight.setVisibility(View.VISIBLE);
				minus_weight.setVisibility(View.GONE);
				weight_details_RL.setVisibility(View.GONE);

			}
		});*/
		
		dosagetitle_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_dosage.getVisibility()==0){
					add_caution.setVisibility(View.VISIBLE);
					minus_caution.setVisibility(View.GONE);
					caution_details_RL.setVisibility(View.GONE);
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
					add_contra_indi.setVisibility(View.VISIBLE);
					minus_contra_indi.setVisibility(View.GONE);
					contra_indi_details_RL.setVisibility(View.GONE);
					add_side_eff.setVisibility(View.VISIBLE);
					minus_side_eff.setVisibility(View.GONE);
					side_eff_details_RL.setVisibility(View.GONE);
					add_weight.setVisibility(View.VISIBLE);
					minus_weight.setVisibility(View.GONE);
					weight_details_RL.setVisibility(View.GONE);
					add_dosage.setVisibility(View.GONE);
					minus_dosage.setVisibility(View.VISIBLE);
					dosage_Details_RL.setVisibility(View.VISIBLE);
					add_weight.setVisibility(View.VISIBLE);
					minus_weight.setVisibility(View.GONE);
					weight_details_RL.setVisibility(View.GONE);
					add_dosage.setVisibility(View.GONE);
					minus_dosage.setVisibility(View.VISIBLE);
					dosage_Details_RL.setVisibility(View.VISIBLE);
				}else{
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);	
				}
				
			}
		});
		/*add_dosage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
				add_side_eff.setVisibility(View.VISIBLE);
				minus_side_eff.setVisibility(View.GONE);
				side_eff_details_RL.setVisibility(View.GONE);
				add_weight.setVisibility(View.VISIBLE);
				minus_weight.setVisibility(View.GONE);
				weight_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.GONE);
				minus_dosage.setVisibility(View.VISIBLE);
				dosage_Details_RL.setVisibility(View.VISIBLE);
				add_weight.setVisibility(View.VISIBLE);
				minus_weight.setVisibility(View.GONE);
				weight_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.GONE);
				minus_dosage.setVisibility(View.VISIBLE);
				dosage_Details_RL.setVisibility(View.VISIBLE);

			}
		});
		minus_dosage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);
			}
		});*/

	}

	private void homeoUI() {
		TextView descdetailsHome, indidetailsHome, cindidetailsHome, cautiondetailsHome, side_effdetailsHome, weightdetailsHome, dosagedetailsHome;

		desc_Main_RL = (LinearLayout) findViewById(R.id.desc_Main_RL);

		TextView item_description = (TextView) findViewById(R.id.item_description);
		item_description.setVisibility(View.GONE);
		RelativeLayout relativeHiddenLayout = (RelativeLayout) findViewById(R.id.relativeHiddenLayout);
		relativeHiddenLayout.setVisibility(View.GONE);

		customViewDesc = new CustomViewProdInfo(this);
		customViewSEff = new CustomViewProdInfo(this);
		customViewCIndi = new CustomViewProdInfo(this);
		customViewDosage = new CustomViewProdInfo(this);
		customViewIndi = new CustomViewProdInfo(this);
		customViewCaution = new CustomViewProdInfo(this);

		add_button_desc = (ImageView) customViewDesc
				.findViewById(R.id.add_Button_indi);
		minus_button_desc = (ImageView) customViewDesc
				.findViewById(R.id.minusButton_indi);
		desc_details_RL = (RelativeLayout) customViewDesc
				.findViewById(R.id.indi_details_RL);
		desc_heading = (TextView) customViewDesc.findViewById(R.id.indi_title);
		desc_heading.setText(getString(R.string.description));
		RelativeLayout desctitle_RL = (RelativeLayout)customViewDesc.findViewById(R.id.title_RL);
		descdetailsHome = (TextView) customViewDesc.findViewById(R.id.details);
		desc_Main_RL.addView(customViewDesc);
		customViewDesc.setLayoutParams(params);
		customViewDesc.requestLayout();

		add_side_eff = (ImageView) customViewSEff
				.findViewById(R.id.add_Button_indi);// 13_01_2016 Gitesh
		minus_side_eff = (ImageView) customViewSEff
				.findViewById(R.id.minusButton_indi);// 13_01_2016 Gitesh
		side_eff_details_RL = (RelativeLayout) customViewSEff
				.findViewById(R.id.indi_details_RL);// 13_01_2016 Gitesh
		sEff_heading = (TextView) customViewSEff.findViewById(R.id.indi_title);
		sEff_heading.setText(getString(R.string.side_effect));
		RelativeLayout sEff_title_RL = (RelativeLayout)customViewSEff.
				findViewById(R.id.title_RL);
		side_effdetailsHome = (TextView) customViewSEff
				.findViewById(R.id.details);
		desc_Main_RL.addView(customViewSEff);
		customViewSEff.setLayoutParams(params);
		customViewSEff.requestLayout();

		add_contra_indi = (ImageView) customViewCIndi
				.findViewById(R.id.add_Button_indi);
		minus_contra_indi = (ImageView) customViewCIndi
				.findViewById(R.id.minusButton_indi);
		contra_indi_details_RL = (RelativeLayout) customViewCIndi
				.findViewById(R.id.indi_details_RL);
		cIndi_heading = (TextView) customViewCIndi
				.findViewById(R.id.indi_title);
		cIndi_heading.setText(getString(R.string.contra_indication));
		cindidetailsHome = (TextView) customViewCIndi
				.findViewById(R.id.details);
		// desc_Main_RL.addView(customViewCIndi);
		customViewCIndi.setLayoutParams(params);
		customViewCIndi.requestLayout();

		add_dosage = (ImageView) customViewDosage
				.findViewById(R.id.add_Button_indi);
		minus_dosage = (ImageView) customViewDosage
				.findViewById(R.id.minusButton_indi);
		dosage_Details_RL = (RelativeLayout) customViewDosage
				.findViewById(R.id.indi_details_RL);// 13_01_2016 Gitesh
		dosage_heading = (TextView) customViewDosage
				.findViewById(R.id.indi_title);
		dosage_heading.setText(getString(R.string.dosage));
		RelativeLayout dosage_title_RL = (RelativeLayout)customViewDosage.
				findViewById(R.id.title_RL);
		dosagedetailsHome = (TextView) customViewDosage
				.findViewById(R.id.details);
		desc_Main_RL.addView(customViewDosage);
		customViewDosage.setLayoutParams(params);
		customViewDosage.requestLayout();

		add_indi = (ImageView) customViewIndi
				.findViewById(R.id.add_Button_indi);
		minus_indi = (ImageView) customViewIndi
				.findViewById(R.id.minusButton_indi);
		indi_details_RL = (RelativeLayout) customViewIndi
				.findViewById(R.id.indi_details_RL);
		indi_heading = (TextView) customViewIndi.findViewById(R.id.indi_title);
		indi_heading.setText(getString(R.string.interactions));
		indidetailsHome = (TextView) customViewCIndi.findViewById(R.id.details);
		// desc_Main_RL.addView(customViewIndi);
		customViewIndi.setLayoutParams(params);
		customViewIndi.requestLayout();

		add_caution = (ImageView) customViewCaution
				.findViewById(R.id.add_Button_indi);
		minus_caution = (ImageView) customViewCaution
				.findViewById(R.id.minusButton_indi);
		caution_details_RL = (RelativeLayout) customViewCaution
				.findViewById(R.id.indi_details_RL);
		caution_heading = (TextView) customViewCaution
				.findViewById(R.id.indi_title);
		caution_heading.setText(getString(R.string.diet_restriction));
		cautiondetailsHome = (TextView) customViewCaution
				.findViewById(R.id.details);
		// desc_Main_RL.addView(customViewCaution);
		customViewCaution.setLayoutParams(params);
		customViewCaution.requestLayout();

		try {
			JSONObject jsonHome = new JSONObject(medId);
			descdetailsHome.setText(jsonHome.optString("desc"));
			indidetailsHome.setText(jsonHome.optString("indications"));
			cindidetailsHome.setText(jsonHome.optString("contraIndi"));
			cautiondetailsHome.setText(jsonHome.optString("caution"));
			side_effdetailsHome.setText(jsonHome.optString("side_effect"));
			dosagedetailsHome.setText(Html.fromHtml(jsonHome
					.optString("dosage")));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		desctitle_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_button_desc.getVisibility()==0){
					add_button_desc.setVisibility(View.GONE);
					minus_button_desc.setVisibility(View.VISIBLE);
					desc_details_RL.setVisibility(View.VISIBLE);
					add_caution.setVisibility(View.VISIBLE);
					minus_caution.setVisibility(View.GONE);
					caution_details_RL.setVisibility(View.GONE);
					add_contra_indi.setVisibility(View.VISIBLE);
					minus_contra_indi.setVisibility(View.GONE);
					contra_indi_details_RL.setVisibility(View.GONE);
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
					add_side_eff.setVisibility(View.VISIBLE);
					minus_side_eff.setVisibility(View.GONE);
					side_eff_details_RL.setVisibility(View.GONE);
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);
				}else{
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
				}
				
			}
		});

		// add minus buttons for indicator

		add_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_indi.setVisibility(View.GONE);
				minus_indi.setVisibility(View.VISIBLE);
				indi_details_RL.setVisibility(View.VISIBLE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);
				add_side_eff.setVisibility(View.VISIBLE);
				minus_side_eff.setVisibility(View.GONE);
				side_eff_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);

			}
		});
		minus_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);

			}
		});

		// add minus buttons for contrA
		add_contra_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_contra_indi.setVisibility(View.GONE);
				minus_contra_indi.setVisibility(View.VISIBLE);
				contra_indi_details_RL.setVisibility(View.VISIBLE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);
				add_side_eff.setVisibility(View.VISIBLE);
				minus_side_eff.setVisibility(View.GONE);
				side_eff_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);

			}
		});
		minus_contra_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
			}
		});

		// add minus buttons for cautions
		add_caution.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_caution.setVisibility(View.GONE);
				minus_caution.setVisibility(View.VISIBLE);
				caution_details_RL.setVisibility(View.VISIBLE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
				add_side_eff.setVisibility(View.VISIBLE);
				minus_side_eff.setVisibility(View.GONE);
				side_eff_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);

			}

		});
		minus_caution.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);

			}
		});
		// 13_01_2016 Gitesh start
		// add minus buttons for side effects
		
		sEff_title_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(add_side_eff.getVisibility()==0){
					add_caution.setVisibility(View.VISIBLE);
					minus_caution.setVisibility(View.GONE);
					caution_details_RL.setVisibility(View.GONE);
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
					add_contra_indi.setVisibility(View.VISIBLE);
					minus_contra_indi.setVisibility(View.GONE);
					contra_indi_details_RL.setVisibility(View.GONE);
					add_side_eff.setVisibility(View.GONE);
					minus_side_eff.setVisibility(View.VISIBLE);
					side_eff_details_RL.setVisibility(View.VISIBLE);
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);
				}
				else{
					add_side_eff.setVisibility(View.VISIBLE);
					minus_side_eff.setVisibility(View.GONE);
					side_eff_details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		
		/*add_side_eff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
				add_side_eff.setVisibility(View.GONE);
				minus_side_eff.setVisibility(View.VISIBLE);
				side_eff_details_RL.setVisibility(View.VISIBLE);
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);
			}
		});
		minus_side_eff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_side_eff.setVisibility(View.VISIBLE);
				minus_side_eff.setVisibility(View.GONE);
				side_eff_details_RL.setVisibility(View.GONE);
			}
		});
*/
		
		dosage_title_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_dosage.getVisibility()==0){
					add_caution.setVisibility(View.VISIBLE);
					minus_caution.setVisibility(View.GONE);
					caution_details_RL.setVisibility(View.GONE);
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
					add_contra_indi.setVisibility(View.VISIBLE);
					minus_contra_indi.setVisibility(View.GONE);
					contra_indi_details_RL.setVisibility(View.GONE);
					add_side_eff.setVisibility(View.VISIBLE);
					minus_side_eff.setVisibility(View.GONE);
					side_eff_details_RL.setVisibility(View.GONE);
					add_dosage.setVisibility(View.GONE);
					minus_dosage.setVisibility(View.VISIBLE);
					dosage_Details_RL.setVisibility(View.VISIBLE);
					add_dosage.setVisibility(View.GONE);
					minus_dosage.setVisibility(View.VISIBLE);
					dosage_Details_RL.setVisibility(View.VISIBLE);
				}else{
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		
		/*add_dosage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
				add_side_eff.setVisibility(View.VISIBLE);
				minus_side_eff.setVisibility(View.GONE);
				side_eff_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.GONE);
				minus_dosage.setVisibility(View.VISIBLE);
				dosage_Details_RL.setVisibility(View.VISIBLE);
				add_dosage.setVisibility(View.GONE);
				minus_dosage.setVisibility(View.VISIBLE);
				dosage_Details_RL.setVisibility(View.VISIBLE);

			}
		});
		minus_dosage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);
			}
		});*/
	}

	private void ayurUI() {
		TextView descdetailsAyur, indidetailsAyur, cindidetailsAyur, cautiondetailsAyur, side_effdetailsAyur, weightdetailsAyur, dosagedetailsAyur;

		desc_Main_RL = (LinearLayout) findViewById(R.id.desc_Main_RL);
		TextView item_description = (TextView) findViewById(R.id.item_description);
		item_description.setVisibility(View.GONE);
		RelativeLayout relativeHiddenLayout = (RelativeLayout) findViewById(R.id.relativeHiddenLayout);
		relativeHiddenLayout.setVisibility(View.GONE);

		customViewDesc = new CustomViewProdInfo(this);
		customViewCaution = new CustomViewProdInfo(this);
		customViewIndi = new CustomViewProdInfo(this);
		customViewDosage = new CustomViewProdInfo(this);
		customViewWeight = new CustomViewProdInfo(this);
		customViewCIndi = new CustomViewProdInfo(this);

		add_button_desc = (ImageView) customViewDesc
				.findViewById(R.id.add_Button_indi);
		minus_button_desc = (ImageView) customViewDesc
				.findViewById(R.id.minusButton_indi);
		desc_details_RL = (RelativeLayout) customViewDesc
				.findViewById(R.id.indi_details_RL);
		desc_heading = (TextView) customViewDesc.findViewById(R.id.indi_title);
		desc_heading.setText(getString(R.string.description));
		RelativeLayout desctitle_RL = (RelativeLayout)customViewDesc.
				findViewById(R.id.title_RL);
		descdetailsAyur = (TextView) customViewDesc.findViewById(R.id.details);
		desc_Main_RL.addView(customViewDesc);
		customViewDesc.setLayoutParams(params);
		customViewDesc.requestLayout();

		add_caution = (ImageView) customViewCaution
				.findViewById(R.id.add_Button_indi);
		minus_caution = (ImageView) customViewCaution
				.findViewById(R.id.minusButton_indi);
		caution_details_RL = (RelativeLayout) customViewCaution
				.findViewById(R.id.indi_details_RL);
		caution_heading = (TextView) customViewCaution
				.findViewById(R.id.indi_title);
		caution_heading.setText(getString(R.string.composition));
		cautiondetailsAyur = (TextView) customViewCaution
				.findViewById(R.id.details);
		// desc_Main_RL.addView(customViewCaution);
		customViewCaution.setLayoutParams(params);
		customViewCaution.requestLayout();

		add_indi = (ImageView) customViewIndi
				.findViewById(R.id.add_Button_indi);
		minus_indi = (ImageView) customViewIndi
				.findViewById(R.id.minusButton_indi);
		indi_details_RL = (RelativeLayout) customViewIndi
				.findViewById(R.id.indi_details_RL);
		indi_heading = (TextView) customViewIndi.findViewById(R.id.indi_title);
		indi_heading.setText(getString(R.string.indication));
		RelativeLayout indititle_RL = (RelativeLayout)customViewIndi.
				findViewById(R.id.title_RL);
		indidetailsAyur = (TextView) customViewIndi.findViewById(R.id.details);
		desc_Main_RL.addView(customViewIndi);
		customViewIndi.setLayoutParams(params);
		customViewIndi.requestLayout();

		add_dosage = (ImageView) customViewDosage
				.findViewById(R.id.add_Button_indi);
		minus_dosage = (ImageView) customViewDosage
				.findViewById(R.id.minusButton_indi);
		dosage_Details_RL = (RelativeLayout) customViewDosage
				.findViewById(R.id.indi_details_RL);// 13_01_2016 Gitesh
		dosage_heading = (TextView) customViewDosage
				.findViewById(R.id.indi_title);
		dosage_heading.setText(getString(R.string.dosage));
		RelativeLayout dosagetitle_RL = (RelativeLayout)customViewDosage.
				findViewById(R.id.title_RL);
		dosagedetailsAyur = (TextView) customViewDosage
				.findViewById(R.id.details);
		desc_Main_RL.addView(customViewDosage);
		customViewDosage.setLayoutParams(params);
		customViewDosage.requestLayout();

		add_weight = (ImageView) customViewWeight
				.findViewById(R.id.add_Button_indi);
		minus_weight = (ImageView) customViewWeight
				.findViewById(R.id.minusButton_indi);
		weight_details_RL = (RelativeLayout) customViewWeight
				.findViewById(R.id.indi_details_RL);// 13_01_2016 Gitesh
		weight_heading = (TextView) customViewWeight
				.findViewById(R.id.indi_title);
		weight_heading.setText(getString(R.string.packing_size));
		RelativeLayout weighttitle_RL = (RelativeLayout)customViewWeight.
				findViewById(R.id.title_RL);
		weightdetailsAyur = (TextView) customViewWeight
				.findViewById(R.id.details);
		desc_Main_RL.addView(customViewWeight);
		customViewWeight.setLayoutParams(params);
		customViewWeight.requestLayout();

		add_contra_indi = (ImageView) customViewCIndi
				.findViewById(R.id.add_Button_indi);
		minus_contra_indi = (ImageView) customViewCIndi
				.findViewById(R.id.minusButton_indi);
		contra_indi_details_RL = (RelativeLayout) customViewCIndi
				.findViewById(R.id.indi_details_RL);
		cIndi_heading = (TextView) customViewCIndi
				.findViewById(R.id.indi_title);
		cIndi_heading.setText(getString(R.string.diet_restriction));
		cindidetailsAyur = (TextView) customViewCIndi
				.findViewById(R.id.details);
		// desc_Main_RL.addView(customViewCIndi);
		customViewCIndi.setLayoutParams(params);
		customViewCIndi.requestLayout();

		try {
			JSONObject jsonAyur = new JSONObject(medId);
			descdetailsAyur.setText(jsonAyur.optString("desc"));
			indidetailsAyur.setText(jsonAyur.optString("indications"));
			cindidetailsAyur.setText(jsonAyur.optString("contraIndi"));
			cautiondetailsAyur.setText(jsonAyur.optString("caution"));
			// side_effdetailsAyur.setText(jsonAyur.optString("side_effect"));
			weightdetailsAyur.setText(jsonAyur.optString("weight"));
			dosagedetailsAyur.setText(Html.fromHtml(jsonAyur
					.optString("dosage")));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		desctitle_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_button_desc.getVisibility()==0){
					add_button_desc.setVisibility(View.GONE);
					minus_button_desc.setVisibility(View.VISIBLE);
					desc_details_RL.setVisibility(View.VISIBLE);
					add_caution.setVisibility(View.VISIBLE);
					minus_caution.setVisibility(View.GONE);
					caution_details_RL.setVisibility(View.GONE);
					add_contra_indi.setVisibility(View.VISIBLE);
					minus_contra_indi.setVisibility(View.GONE);
					contra_indi_details_RL.setVisibility(View.GONE);
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
					add_weight.setVisibility(View.VISIBLE);
					minus_weight.setVisibility(View.GONE);
					weight_details_RL.setVisibility(View.GONE);
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);
				}else{
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		/*add_button_desc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_button_desc.setVisibility(View.GONE);
				minus_button_desc.setVisibility(View.VISIBLE);
				desc_details_RL.setVisibility(View.VISIBLE);
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);
				add_weight.setVisibility(View.VISIBLE);
				minus_weight.setVisibility(View.GONE);
				weight_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);
			}
		});
		minus_button_desc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);

			}
		});*/

		// add minus buttons for indicator
		indititle_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_indi.getVisibility()==0){
					add_indi.setVisibility(View.GONE);
					minus_indi.setVisibility(View.VISIBLE);
					indi_details_RL.setVisibility(View.VISIBLE);
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
					add_contra_indi.setVisibility(View.VISIBLE);
					minus_contra_indi.setVisibility(View.GONE);
					contra_indi_details_RL.setVisibility(View.GONE);
					add_caution.setVisibility(View.VISIBLE);
					minus_caution.setVisibility(View.GONE);
					caution_details_RL.setVisibility(View.GONE);
					add_weight.setVisibility(View.VISIBLE);
					minus_weight.setVisibility(View.GONE);
					weight_details_RL.setVisibility(View.GONE);
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);

				}else{
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		/*
		add_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_indi.setVisibility(View.GONE);
				minus_indi.setVisibility(View.VISIBLE);
				indi_details_RL.setVisibility(View.VISIBLE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);
				add_weight.setVisibility(View.VISIBLE);
				minus_weight.setVisibility(View.GONE);
				weight_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);

			}
		});
		minus_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);

			}
		});*/

		// add minus buttons for contrA
		add_contra_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_contra_indi.setVisibility(View.GONE);
				minus_contra_indi.setVisibility(View.VISIBLE);
				contra_indi_details_RL.setVisibility(View.VISIBLE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);
				add_weight.setVisibility(View.VISIBLE);
				minus_weight.setVisibility(View.GONE);
				weight_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);

			}
		});
		minus_contra_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
			}
		});

		// add minus buttons for cautions
		add_caution.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_caution.setVisibility(View.GONE);
				minus_caution.setVisibility(View.VISIBLE);
				caution_details_RL.setVisibility(View.VISIBLE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
				add_weight.setVisibility(View.VISIBLE);
				minus_weight.setVisibility(View.GONE);
				weight_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);

			}

		});
		minus_caution.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);

			}
		});
		// 13_01_2016 Gitesh end
		weighttitle_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_weight.getVisibility()==0){
					add_caution.setVisibility(View.VISIBLE);
					minus_caution.setVisibility(View.GONE);
					caution_details_RL.setVisibility(View.GONE);
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
					add_contra_indi.setVisibility(View.VISIBLE);
					minus_contra_indi.setVisibility(View.GONE);
					contra_indi_details_RL.setVisibility(View.GONE);
					add_weight.setVisibility(View.GONE);
					minus_weight.setVisibility(View.VISIBLE);
					weight_details_RL.setVisibility(View.VISIBLE);
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);
				}else{
					add_weight.setVisibility(View.VISIBLE);
					minus_weight.setVisibility(View.GONE);
					weight_details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		/*add_weight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
				add_weight.setVisibility(View.GONE);
				minus_weight.setVisibility(View.VISIBLE);
				weight_details_RL.setVisibility(View.VISIBLE);
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);

			}
		});
		minus_weight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_weight.setVisibility(View.VISIBLE);
				minus_weight.setVisibility(View.GONE);
				weight_details_RL.setVisibility(View.GONE);

			}
		});*/
		
		dosagetitle_RL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(add_dosage.getVisibility()==0){
					add_caution.setVisibility(View.VISIBLE);
					minus_caution.setVisibility(View.GONE);
					caution_details_RL.setVisibility(View.GONE);
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
					add_contra_indi.setVisibility(View.VISIBLE);
					minus_contra_indi.setVisibility(View.GONE);
					contra_indi_details_RL.setVisibility(View.GONE);
					add_weight.setVisibility(View.VISIBLE);
					minus_weight.setVisibility(View.GONE);
					weight_details_RL.setVisibility(View.GONE);
					add_dosage.setVisibility(View.GONE);
					minus_dosage.setVisibility(View.VISIBLE);
					dosage_Details_RL.setVisibility(View.VISIBLE);
					add_weight.setVisibility(View.VISIBLE);
					minus_weight.setVisibility(View.GONE);
					weight_details_RL.setVisibility(View.GONE);
					add_dosage.setVisibility(View.GONE);
					minus_dosage.setVisibility(View.VISIBLE);
					dosage_Details_RL.setVisibility(View.VISIBLE);
				}else{
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);
				}
				
			}
		});
		/*add_dosage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_caution.setVisibility(View.VISIBLE);
				minus_caution.setVisibility(View.GONE);
				caution_details_RL.setVisibility(View.GONE);
				add_button_desc.setVisibility(View.VISIBLE);
				minus_button_desc.setVisibility(View.GONE);
				desc_details_RL.setVisibility(View.GONE);
				add_indi.setVisibility(View.VISIBLE);
				minus_indi.setVisibility(View.GONE);
				indi_details_RL.setVisibility(View.GONE);
				add_contra_indi.setVisibility(View.VISIBLE);
				minus_contra_indi.setVisibility(View.GONE);
				contra_indi_details_RL.setVisibility(View.GONE);
				add_weight.setVisibility(View.VISIBLE);
				minus_weight.setVisibility(View.GONE);
				weight_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.GONE);
				minus_dosage.setVisibility(View.VISIBLE);
				dosage_Details_RL.setVisibility(View.VISIBLE);
				add_weight.setVisibility(View.VISIBLE);
				minus_weight.setVisibility(View.GONE);
				weight_details_RL.setVisibility(View.GONE);
				add_dosage.setVisibility(View.GONE);
				minus_dosage.setVisibility(View.VISIBLE);
				dosage_Details_RL.setVisibility(View.VISIBLE);

			}
		});
		minus_dosage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				add_dosage.setVisibility(View.VISIBLE);
				minus_dosage.setVisibility(View.GONE);
				dosage_Details_RL.setVisibility(View.GONE);
			}
		});*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	public void setCartQuantity() {
		CartDataBean cbean = Okler.getInstance().getMainCart();
		ArrayList<ProductDataBean> pdList = cbean.getProdList();
		// pdList = odbean.getProdList();
		int maincount = pdList.size();

		if (maincount > 9) {
			notifCount.setText("" + maincount);
		} else {
			notifCount.setText("0" + maincount);
		}
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

	public void topGenProductUI() {

		genView = findViewById(R.id.top_gen_info);
		genView.setVisibility(View.VISIBLE);
		gen_viewMoreTv = (TextView) genView.findViewById(R.id.viewMoreTv);
		gen_viewLessTv = (TextView) genView.findViewById(R.id.viewLessTv);
		gen_desc_Main_RL = (LinearLayout) genView
				.findViewById(R.id.desc_Main_RL);
		gen_viewLessTv.setText(viewLess);
		gen_viewMoreTv.setText(viewMore);
		TextView gen_mrpTv = (TextView) genView.findViewById(R.id.tv_MrpValue);
		gen_mrp = new SpannableString(gen_mrpTv.getText().toString());
		gen_mrp.setSpan(new StrikethroughSpan(), 0, gen_mrp.length(), 0);
		gen_mrpTv.setText(gen_mrp);
		NetworkImageView item_image = (NetworkImageView) genView
				.findViewById(R.id.item_image);
		item_image.setVisibility(View.GONE);
		View black_separator_item = genView
				.findViewById(R.id.black_separator_item);
		black_separator_item.setVisibility(View.GONE);
		ImageView image_favourite = (ImageView) genView
				.findViewById(R.id.image_favourite);
		image_favourite.setVisibility(View.GONE);
		TextView gen_change = (TextView) genView.findViewById(R.id.changeTv);
		gen_change.setText(change_pin);
		RelativeLayout top_gen_heading_RL, gre_separator_RL, red_separator_RL, avail_subs_heading_RL;

		top_gen_heading_RL = (RelativeLayout) findViewById(R.id.top_gen_heading_RL);
		top_gen_heading_RL.setVisibility(View.VISIBLE);
		gre_separator_RL = (RelativeLayout) findViewById(R.id.gre_separator_RL);
		gre_separator_RL.setVisibility(View.VISIBLE);
		avail_subs_heading_RL = (RelativeLayout) findViewById(R.id.avail_subs_heading_RL);
		avail_subs_heading_RL.setVisibility(View.VISIBLE);
		red_separator_RL = (RelativeLayout) findViewById(R.id.red_separator_RL);
		red_separator_RL.setVisibility(View.VISIBLE);

		// RL_For_Prod_Info =
		// (RelativeLayout)genView.findViewById(R.id.RL_For_Prod_Info);
		genView.setBackgroundColor(getResources().getColor(
				R.color.LightGreyWhite));
		RelativeLayout relativeHiddenLayout = (RelativeLayout) genView
				.findViewById(R.id.relativeHiddenLayout);
		relativeHiddenLayout.setVisibility(View.VISIBLE);
		RelativeLayout health_shop_greed_main_layout = (RelativeLayout) genView
				.findViewById(R.id.health_shop_greed_main_layout);
		health_shop_greed_main_layout.setBackgroundColor(getResources()
				.getColor(R.color.LightGreyWhite));
		// Button cartToHide = (Button)genView.findViewById(R.id.image_cart);
		// cartToHide.setVisibility(View.GONE);
		LinearLayout check_pin_LL2 = (LinearLayout) genView
				.findViewById(R.id.check_pin_LL);
		check_pin_LL2.setBackgroundColor(getResources().getColor(
				R.color.LightGreyWhite));
		gen_pincodeEt = (EditText) genView.findViewById(R.id.enter_pincode_Et);
		// gen_pincodeEt.setBackground(R.color.LightGreyWhite);
		// gen_pincodeEt.setBackgroundResource(R.color.LightGreyWhite);
		gen_pincodeEt.setBackgroundColor(getResources().getColor(
				R.color.LightGreyWhite));
		gen_enter_pin_LL = (RelativeLayout) genView
				.findViewById(R.id.enter_pin_LL);
		// gen_enter_pin_LL.setBackgroundColor(getResources().getColor(R.color.White));
		gen_availableTv = (TextView) genView.findViewById(R.id.availableTv);
		gen_shipstoTv = (TextView) genView.findViewById(R.id.shipstoTv);
		gen_unitValue = (EditText) genView.findViewById(R.id.unitValue);
		ImageView gen_buyIV = (ImageView) genView.findViewById(R.id.buyIV);
		genAddToCart = (ImageView) genView.findViewById(R.id.cartIV);
		ImageView gen_share = (ImageView) genView.findViewById(R.id.shareIV);
		genAddToCart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// int unit = 0;
				if (gen_availableTv.getText().toString().equals("Available")
						&& gen_available_LL.getVisibility() == View.VISIBLE) {
					String unit = gen_unitValue.getText().toString();
					if (unit.equals(""))
						unit = "00";
					if (unit.length() > 2) {
						gen_unitValue.setText("");
						gen_unitValue.requestFocus();
						Toast.makeText(activity, "Maximum limit is 99",
								Toast.LENGTH_SHORT).show();
					} else {
						int units = Integer.parseInt(unit);
						if (units <= 0) {
							Toast.makeText(activity,
									"Please Select atleast one unit",
									Toast.LENGTH_LONG).show();
							gen_unitValue.requestFocus();
						} else {
							if (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN
									|| (Utilities
											.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_FB)
									|| (Utilities
											.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_GOOGLE)) {

								genAddCartWebService();
							} else {
								Intent intent = new Intent(activity,
										NewSignIn.class);
								gen_addCart = true;
								startActivity(intent);
							}

						}
					}
				} else if (gen_check_pin_LL.getVisibility() == View.VISIBLE) {
					gen_pincodeEt
							.setError("Please Check availability at your PinCode.");
					gen_pincodeEt.requestFocus();
					// Toast.makeText(activity,
					// "Please Check availability at your PinCode.",
					// Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(activity,
							"Product not available at your PinCode.",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		gen_buyIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CartDataBean odBean = new CartDataBean();
				// temp code for id

				if (gen_availableTv.getText().toString().equals("Available")
						&& gen_available_LL.getVisibility() == View.VISIBLE) {
					String unit = gen_unitValue.getText().toString();
					if (unit.equals(""))
						unit = "00";
					if (unit.length() > 2) {
						gen_unitValue.setText("");
						gen_unitValue.requestFocus();
						Toast.makeText(activity, "Maximum limit is 99",
								Toast.LENGTH_SHORT).show();
					} else {
						int units = Integer.parseInt(unit);
						if (units <= 0) {
							Toast.makeText(activity,
									"Please Select atleast one unit",
									Toast.LENGTH_LONG).show();
							gen_unitValue.requestFocus();
						} else {
							if (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN
									|| (Utilities
											.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_FB)
									|| (Utilities
											.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
								genBuy();
							} else {
								Intent intent = new Intent(activity,
										NewSignIn.class);
								gen_buy = true;
								startActivity(intent);
							}

						}
					}
				} else if (gen_check_pin_LL.getVisibility() == View.VISIBLE) {
					gen_pincodeEt
							.setError("Please Check availability at your PinCode.");
					gen_pincodeEt.requestFocus();
					// Toast.makeText(activity,
					// "Please Check availability at your PinCode.",
					// Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(activity,
							"Product not available at your PinCode.",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		gen_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareProd(ListForSubs.get(0).getProdName());
			}
		});

		TextView gen_descdetailsAllo, gen_indidetailsAllo, gen_cindidetailsAllo, gen_cautiondetailsAllo, gen_side_effdetailsAllo, gen_weightdetailsAllo, gen_dosagedetailsAllo;

		gen_customViewDesc = new CustomViewProdInfo(this);
		gen_customViewIndi = new CustomViewProdInfo(this);
		gen_customViewCIndi = new CustomViewProdInfo(this);
		gen_customViewCaution = new CustomViewProdInfo(this);
		gen_customViewSEff = new CustomViewProdInfo(this);
		gen_customViewWeight = new CustomViewProdInfo(this);
		gen_customViewDosage = new CustomViewProdInfo(this);

		gen_check = (Button) genView.findViewById(R.id.button_check);

		gen_pincodeTv = (TextView) genView.findViewById(R.id.pincodeTv);
		gen_check_pin_LL = (LinearLayout) genView
				.findViewById(R.id.check_pin_LL);
		gen_edit_pincode = (ImageView) genView.findViewById(R.id.edit_pincode);
		gen_available_LL = (RelativeLayout) genView
				.findViewById(R.id.available_LL);
		gen_unitLayout = (RelativeLayout) genView.findViewById(R.id.unitLayout);
		gen_available_LL.setBackgroundColor(getResources().getColor(
				R.color.LightGreyWhite));

		gen_add_button_desc = (ImageView) gen_customViewDesc
				.findViewById(R.id.add_Button_indi);
		gen_minus_button_desc = (ImageView) gen_customViewDesc
				.findViewById(R.id.minusButton_indi);
		gen_desc_details_RL = (RelativeLayout) gen_customViewDesc
				.findViewById(R.id.indi_details_RL);
		gen_desc_heading = (TextView) gen_customViewDesc
				.findViewById(R.id.indi_title);
		gen_desc_heading.setText(getString(R.string.description));
		gen_descdetailsAllo = (TextView) gen_customViewDesc
				.findViewById(R.id.details);
		gen_desc_Main_RL.addView(gen_customViewDesc);

		gen_customViewDesc.setLayoutParams(params);
		gen_customViewDesc.requestLayout();

		gen_add_indi = (ImageView) gen_customViewIndi
				.findViewById(R.id.add_Button_indi);
		gen_minus_indi = (ImageView) gen_customViewIndi
				.findViewById(R.id.minusButton_indi);
		gen_indi_details_RL = (RelativeLayout) gen_customViewIndi
				.findViewById(R.id.indi_details_RL);
		gen_indi_heading = (TextView) gen_customViewIndi
				.findViewById(R.id.indi_title);
		gen_indi_heading.setText(getString(R.string.indication));
		gen_indidetailsAllo = (TextView) gen_customViewIndi
				.findViewById(R.id.details);
		gen_desc_Main_RL.addView(gen_customViewIndi);
		gen_customViewIndi.setLayoutParams(params);
		gen_customViewIndi.requestLayout();

		gen_add_contra_indi = (ImageView) gen_customViewCIndi
				.findViewById(R.id.add_Button_indi);
		gen_minus_contra_indi = (ImageView) gen_customViewCIndi
				.findViewById(R.id.minusButton_indi);
		gen_contra_indi_details_RL = (RelativeLayout) gen_customViewCIndi
				.findViewById(R.id.indi_details_RL);
		gen_cIndi_heading = (TextView) gen_customViewCIndi
				.findViewById(R.id.indi_title);
		gen_cIndi_heading.setText(getString(R.string.contra_indication));
		gen_cindidetailsAllo = (TextView) gen_customViewCIndi
				.findViewById(R.id.details);
		gen_desc_Main_RL.addView(gen_customViewCIndi);
		gen_customViewCIndi.setLayoutParams(params);
		gen_customViewCIndi.requestLayout();

		gen_add_caution = (ImageView) gen_customViewCaution
				.findViewById(R.id.add_Button_indi);
		gen_minus_caution = (ImageView) gen_customViewCaution
				.findViewById(R.id.minusButton_indi);
		gen_caution_details_RL = (RelativeLayout) gen_customViewCaution
				.findViewById(R.id.indi_details_RL);
		gen_caution_heading = (TextView) gen_customViewCaution
				.findViewById(R.id.indi_title);
		gen_caution_heading.setText(getString(R.string.caution));
		gen_cautiondetailsAllo = (TextView) gen_customViewCaution
				.findViewById(R.id.details);
		gen_desc_Main_RL.addView(gen_customViewCaution);
		gen_customViewCaution.setLayoutParams(params);
		gen_customViewCaution.requestLayout();

		gen_add_side_eff = (ImageView) gen_customViewSEff
				.findViewById(R.id.add_Button_indi);// 13_01_2016 Gitesh
		gen_minus_side_eff = (ImageView) gen_customViewSEff
				.findViewById(R.id.minusButton_indi);// 13_01_2016 Gitesh
		gen_side_eff_details_RL = (RelativeLayout) gen_customViewSEff
				.findViewById(R.id.indi_details_RL);// 13_01_2016 Gitesh
		gen_sEff_heading = (TextView) gen_customViewSEff
				.findViewById(R.id.indi_title);
		gen_sEff_heading.setText(getString(R.string.side_effect));
		gen_side_effdetailsAllo = (TextView) gen_customViewSEff
				.findViewById(R.id.details);
		gen_desc_Main_RL.addView(gen_customViewSEff);
		gen_customViewSEff.setLayoutParams(params);
		gen_customViewSEff.requestLayout();

		gen_add_weight = (ImageView) gen_customViewWeight
				.findViewById(R.id.add_Button_indi);
		gen_minus_weight = (ImageView) gen_customViewWeight
				.findViewById(R.id.minusButton_indi);
		gen_weight_details_RL = (RelativeLayout) gen_customViewWeight
				.findViewById(R.id.indi_details_RL);// 13_01_2016 Gitesh
		gen_weight_heading = (TextView) gen_customViewWeight
				.findViewById(R.id.indi_title);
		gen_weight_heading.setText(getString(R.string.weight));
		gen_weightdetailsAllo = (TextView) gen_customViewWeight
				.findViewById(R.id.details);
		gen_desc_Main_RL.addView(gen_customViewWeight);
		gen_customViewWeight.setLayoutParams(params);
		gen_customViewWeight.requestLayout();

		gen_add_dosage = (ImageView) gen_customViewDosage
				.findViewById(R.id.add_Button_indi);
		gen_minus_dosage = (ImageView) gen_customViewDosage
				.findViewById(R.id.minusButton_indi);
		gen_dosage_Details_RL = (RelativeLayout) gen_customViewDosage
				.findViewById(R.id.indi_details_RL);// 13_01_2016 Gitesh
		gen_dosage_heading = (TextView) gen_customViewDosage
				.findViewById(R.id.indi_title);
		gen_dosage_heading.setText(getString(R.string.dosage));
		gen_dosagedetailsAllo = (TextView) gen_customViewDosage
				.findViewById(R.id.details);
		gen_desc_Main_RL.addView(gen_customViewDosage);
		gen_customViewDosage.setLayoutParams(params);
		gen_customViewDosage.requestLayout();

		// try {
		// JSONObject jsonAllo = new JSONObject(medId);
		gen_descdetailsAllo.setText(ListForSubs.get(0).getDesc());// (jsonAllo.getString("desc"));
		gen_indidetailsAllo.setText(ListForSubs.get(0).getIndications());// (jsonAllo.getString("indications"));
		gen_cindidetailsAllo.setText(ListForSubs.get(0).getContraIndi());// (jsonAllo.getString("contraIndi"));
		gen_cautiondetailsAllo.setText(ListForSubs.get(0).getCaution());// (jsonAllo.getString("caution"));
		gen_side_effdetailsAllo.setText(ListForSubs.get(0).getSide_effect());// (jsonAllo.getString("side_effect"));
		gen_weightdetailsAllo.setText(ListForSubs.get(0).getWeight());// (jsonAllo.getString("weight"));
		gen_dosagedetailsAllo.setText(Html.fromHtml(ListForSubs.get(0)
				.getDosage()));// (jsonAllo.getString("dosage"));
		TextView gen_item_description = (TextView) genView
				.findViewById(R.id.gen_name);
		TextView gen_item_description2 = (TextView) genView
				.findViewById(R.id.item_description);
		gen_item_description2.setVisibility(View.INVISIBLE);
		gen_item_description.setVisibility(View.VISIBLE);
		String generic_name = "";
		generic_name = ListForSubs.get(0).getGeneric_name();// jsonAllo.optString("generic_name");
		if (generic_name == null || generic_name.equals("")
				|| generic_name.equals("null") || generic_name.equals(null)
				|| generic_name.length() == 0) {
			gen_item_description.setVisibility(View.GONE);
		} else {
			gen_item_description.setText(generic_name);
		}

		// } catch (JSONException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		TextView item_name = (TextView) genView.findViewById(R.id.item_name);
		TextView brand_name = (TextView) genView.findViewById(R.id.brand_name);
		TextView presc_req = (TextView) genView.findViewById(R.id.presc_req);
		TextView tv_MrpValue = (TextView) genView
				.findViewById(R.id.tv_MrpValue);
		TextView tv_youSaveValue = (TextView) genView
				.findViewById(R.id.tv_youSaveValue);
		TextView okler_Amount = (TextView) genView
				.findViewById(R.id.okler_Amount);

		item_name.setText(ListForSubs.get(0).getProdName());
		String company = ListForSubs.get(0).getCompany();
		if (company.equals("") || company.equals("null")
				|| company.equals(null) || company == null
				|| company.length() == 0) {
			brand_name.setVisibility(View.GONE);
		} else {
			brand_name.setText(company);
		}
		int presc = ListForSubs.get(0).getPresc_needed();
		if (presc == 0) {
			presc_req.setVisibility(View.GONE);
		}
		tv_MrpValue.setText(String.valueOf(ListForSubs.get(0).getMrp()));
		float disc = ListForSubs.get(0).getDiscount();
		tv_youSaveValue.setText(disc + "%");
		String part1 = okler_Amount.getText().toString();
		float amount = ListForSubs.get(0).getOklerPrice();
		okler_Amount.setText(part1 + amount);

		gen_viewMoreTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_viewMoreTv.setVisibility(View.GONE);
				gen_viewLessTv.setVisibility(View.VISIBLE);
				gen_desc_Main_RL.setVisibility(View.VISIBLE);

			}
		});
		gen_viewLessTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_viewMoreTv.setVisibility(View.VISIBLE);
				gen_viewLessTv.setVisibility(View.GONE);
				gen_desc_Main_RL.setVisibility(View.GONE);
			}
		});
		// add minus buttons for descriptions

		gen_add_button_desc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_add_button_desc.setVisibility(View.GONE);
				gen_minus_button_desc.setVisibility(View.VISIBLE);
				gen_desc_details_RL.setVisibility(View.VISIBLE);
				gen_add_caution.setVisibility(View.VISIBLE);
				gen_minus_caution.setVisibility(View.GONE);
				gen_caution_details_RL.setVisibility(View.GONE);
				gen_add_contra_indi.setVisibility(View.VISIBLE);
				gen_minus_contra_indi.setVisibility(View.GONE);
				gen_contra_indi_details_RL.setVisibility(View.GONE);
				gen_add_indi.setVisibility(View.VISIBLE);
				gen_minus_indi.setVisibility(View.GONE);
				gen_indi_details_RL.setVisibility(View.GONE);
				gen_add_side_eff.setVisibility(View.VISIBLE);
				gen_minus_side_eff.setVisibility(View.GONE);
				gen_side_eff_details_RL.setVisibility(View.GONE);
				gen_add_weight.setVisibility(View.VISIBLE);
				gen_minus_weight.setVisibility(View.GONE);
				gen_weight_details_RL.setVisibility(View.GONE);
				gen_add_dosage.setVisibility(View.VISIBLE);
				gen_minus_dosage.setVisibility(View.GONE);
				gen_dosage_Details_RL.setVisibility(View.GONE);
			}
		});
		gen_minus_button_desc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_add_button_desc.setVisibility(View.VISIBLE);
				gen_minus_button_desc.setVisibility(View.GONE);
				gen_desc_details_RL.setVisibility(View.GONE);

			}
		});

		// add minus buttons for indicator

		gen_add_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_add_indi.setVisibility(View.GONE);
				gen_minus_indi.setVisibility(View.VISIBLE);
				gen_indi_details_RL.setVisibility(View.VISIBLE);
				gen_add_button_desc.setVisibility(View.VISIBLE);
				gen_minus_button_desc.setVisibility(View.GONE);
				gen_desc_details_RL.setVisibility(View.GONE);
				gen_add_contra_indi.setVisibility(View.VISIBLE);
				gen_minus_contra_indi.setVisibility(View.GONE);
				gen_contra_indi_details_RL.setVisibility(View.GONE);
				gen_add_caution.setVisibility(View.VISIBLE);
				gen_minus_caution.setVisibility(View.GONE);
				gen_caution_details_RL.setVisibility(View.GONE);
				gen_add_side_eff.setVisibility(View.VISIBLE);
				gen_minus_side_eff.setVisibility(View.GONE);
				gen_side_eff_details_RL.setVisibility(View.GONE);
				gen_add_weight.setVisibility(View.VISIBLE);
				gen_minus_weight.setVisibility(View.GONE);
				gen_weight_details_RL.setVisibility(View.GONE);
				gen_add_dosage.setVisibility(View.VISIBLE);
				gen_minus_dosage.setVisibility(View.GONE);
				gen_dosage_Details_RL.setVisibility(View.GONE);

			}
		});
		gen_minus_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_add_indi.setVisibility(View.VISIBLE);
				gen_minus_indi.setVisibility(View.GONE);
				gen_indi_details_RL.setVisibility(View.GONE);

			}
		});

		// add minus buttons for contrA
		gen_add_contra_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_add_contra_indi.setVisibility(View.GONE);
				gen_minus_contra_indi.setVisibility(View.VISIBLE);
				gen_contra_indi_details_RL.setVisibility(View.VISIBLE);
				gen_add_button_desc.setVisibility(View.VISIBLE);
				gen_minus_button_desc.setVisibility(View.GONE);
				gen_desc_details_RL.setVisibility(View.GONE);
				gen_add_indi.setVisibility(View.VISIBLE);
				gen_minus_indi.setVisibility(View.GONE);
				gen_indi_details_RL.setVisibility(View.GONE);
				gen_add_caution.setVisibility(View.VISIBLE);
				gen_minus_caution.setVisibility(View.GONE);
				gen_caution_details_RL.setVisibility(View.GONE);
				gen_add_side_eff.setVisibility(View.VISIBLE);
				gen_minus_side_eff.setVisibility(View.GONE);
				gen_side_eff_details_RL.setVisibility(View.GONE);
				gen_add_weight.setVisibility(View.VISIBLE);
				gen_minus_weight.setVisibility(View.GONE);
				gen_weight_details_RL.setVisibility(View.GONE);
				gen_add_dosage.setVisibility(View.VISIBLE);
				gen_minus_dosage.setVisibility(View.GONE);
				gen_dosage_Details_RL.setVisibility(View.GONE);

			}
		});
		gen_minus_contra_indi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_add_contra_indi.setVisibility(View.VISIBLE);
				gen_minus_contra_indi.setVisibility(View.GONE);
				gen_contra_indi_details_RL.setVisibility(View.GONE);
			}
		});

		// add minus buttons for cautions
		gen_add_caution.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_add_caution.setVisibility(View.GONE);
				gen_minus_caution.setVisibility(View.VISIBLE);
				gen_caution_details_RL.setVisibility(View.VISIBLE);
				gen_add_button_desc.setVisibility(View.VISIBLE);
				gen_minus_button_desc.setVisibility(View.GONE);
				gen_desc_details_RL.setVisibility(View.GONE);
				gen_add_indi.setVisibility(View.VISIBLE);
				gen_minus_indi.setVisibility(View.GONE);
				gen_indi_details_RL.setVisibility(View.GONE);
				gen_add_contra_indi.setVisibility(View.VISIBLE);
				gen_minus_contra_indi.setVisibility(View.GONE);
				gen_contra_indi_details_RL.setVisibility(View.GONE);
				gen_add_side_eff.setVisibility(View.VISIBLE);
				gen_minus_side_eff.setVisibility(View.GONE);
				gen_side_eff_details_RL.setVisibility(View.GONE);
				gen_add_weight.setVisibility(View.VISIBLE);
				gen_minus_weight.setVisibility(View.GONE);
				gen_weight_details_RL.setVisibility(View.GONE);
				gen_add_dosage.setVisibility(View.VISIBLE);
				gen_minus_dosage.setVisibility(View.GONE);
				gen_dosage_Details_RL.setVisibility(View.GONE);

			}

		});
		gen_minus_caution.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_add_caution.setVisibility(View.VISIBLE);
				gen_minus_caution.setVisibility(View.GONE);
				gen_caution_details_RL.setVisibility(View.GONE);

			}
		});
		// 13_01_2016 Gitesh start
		// add minus buttons for side effects
		gen_add_side_eff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_add_caution.setVisibility(View.VISIBLE);
				gen_minus_caution.setVisibility(View.GONE);
				gen_caution_details_RL.setVisibility(View.GONE);
				gen_add_button_desc.setVisibility(View.VISIBLE);
				gen_minus_button_desc.setVisibility(View.GONE);
				gen_desc_details_RL.setVisibility(View.GONE);
				gen_add_indi.setVisibility(View.VISIBLE);
				gen_minus_indi.setVisibility(View.GONE);
				gen_indi_details_RL.setVisibility(View.GONE);
				gen_add_contra_indi.setVisibility(View.VISIBLE);
				gen_minus_contra_indi.setVisibility(View.GONE);
				gen_contra_indi_details_RL.setVisibility(View.GONE);
				gen_add_side_eff.setVisibility(View.GONE);
				gen_minus_side_eff.setVisibility(View.VISIBLE);
				gen_side_eff_details_RL.setVisibility(View.VISIBLE);
				gen_add_weight.setVisibility(View.VISIBLE);
				gen_minus_weight.setVisibility(View.GONE);
				gen_weight_details_RL.setVisibility(View.GONE);
				gen_add_dosage.setVisibility(View.VISIBLE);
				gen_minus_dosage.setVisibility(View.GONE);
				gen_dosage_Details_RL.setVisibility(View.GONE);
			}
		});
		gen_minus_side_eff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_add_side_eff.setVisibility(View.VISIBLE);
				gen_minus_side_eff.setVisibility(View.GONE);
				gen_side_eff_details_RL.setVisibility(View.GONE);
			}
		});
		// 13_01_2016 Gitesh end
		gen_add_weight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_add_caution.setVisibility(View.VISIBLE);
				gen_minus_caution.setVisibility(View.GONE);
				gen_caution_details_RL.setVisibility(View.GONE);
				gen_add_button_desc.setVisibility(View.VISIBLE);
				gen_minus_button_desc.setVisibility(View.GONE);
				gen_desc_details_RL.setVisibility(View.GONE);
				gen_add_indi.setVisibility(View.VISIBLE);
				gen_minus_indi.setVisibility(View.GONE);
				gen_indi_details_RL.setVisibility(View.GONE);
				gen_add_contra_indi.setVisibility(View.VISIBLE);
				gen_minus_contra_indi.setVisibility(View.GONE);
				gen_contra_indi_details_RL.setVisibility(View.GONE);
				gen_add_side_eff.setVisibility(View.VISIBLE);
				gen_minus_side_eff.setVisibility(View.GONE);
				gen_side_eff_details_RL.setVisibility(View.GONE);
				gen_add_weight.setVisibility(View.GONE);
				gen_minus_weight.setVisibility(View.VISIBLE);
				gen_weight_details_RL.setVisibility(View.VISIBLE);
				gen_add_dosage.setVisibility(View.VISIBLE);
				gen_minus_dosage.setVisibility(View.GONE);
				gen_dosage_Details_RL.setVisibility(View.GONE);

			}
		});
		gen_minus_weight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_add_weight.setVisibility(View.VISIBLE);
				gen_minus_weight.setVisibility(View.GONE);
				gen_weight_details_RL.setVisibility(View.GONE);

			}
		});
		gen_add_dosage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_add_caution.setVisibility(View.VISIBLE);
				gen_minus_caution.setVisibility(View.GONE);
				gen_caution_details_RL.setVisibility(View.GONE);
				gen_add_button_desc.setVisibility(View.VISIBLE);
				gen_minus_button_desc.setVisibility(View.GONE);
				gen_desc_details_RL.setVisibility(View.GONE);
				gen_add_indi.setVisibility(View.VISIBLE);
				gen_minus_indi.setVisibility(View.GONE);
				gen_indi_details_RL.setVisibility(View.GONE);
				gen_add_contra_indi.setVisibility(View.VISIBLE);
				gen_minus_contra_indi.setVisibility(View.GONE);
				gen_contra_indi_details_RL.setVisibility(View.GONE);
				gen_add_side_eff.setVisibility(View.VISIBLE);
				gen_minus_side_eff.setVisibility(View.GONE);
				gen_side_eff_details_RL.setVisibility(View.GONE);
				gen_add_weight.setVisibility(View.VISIBLE);
				gen_minus_weight.setVisibility(View.GONE);
				gen_weight_details_RL.setVisibility(View.GONE);
				gen_add_dosage.setVisibility(View.GONE);
				gen_minus_dosage.setVisibility(View.VISIBLE);
				gen_dosage_Details_RL.setVisibility(View.VISIBLE);
				gen_add_weight.setVisibility(View.VISIBLE);
				gen_minus_weight.setVisibility(View.GONE);
				gen_weight_details_RL.setVisibility(View.GONE);
				gen_add_dosage.setVisibility(View.GONE);
				gen_minus_dosage.setVisibility(View.VISIBLE);
				gen_dosage_Details_RL.setVisibility(View.VISIBLE);

			}
		});
		gen_minus_dosage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gen_add_dosage.setVisibility(View.VISIBLE);
				gen_minus_dosage.setVisibility(View.GONE);
				gen_dosage_Details_RL.setVisibility(View.GONE);
			}
		});

		pinurlpart1 = getString(R.string.getPinUrlPart1);
		pinurlpart2 = getString(R.string.getPinUrlPart2);
		gen_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (gen_pincodeEt.getText().toString().trim().length() > 6
						|| gen_pincodeEt.getText().toString().trim().length() < 6) {
					Toast.makeText(getApplicationContext(),
							"Please Enter Correct Pincode", Toast.LENGTH_LONG)
							.show();
				} else {
					gen_pincode = gen_pincodeEt.getText().toString().trim();
					pincodeUrl = pinurlpart1 + prodId + pinurlpart2
							+ gen_pincode;
					WebJsonObjectRequest gen_pinjson = new WebJsonObjectRequest(
							Method.GET, pincodeUrl, new JSONObject(),
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									String resp = String.valueOf(response);
									// Toast.makeText(getApplicationContext(),
									// resp, Toast.LENGTH_LONG).show();
									JSONObject gen_pinobj = (JSONObject) response;
									try {
										resp = gen_pinobj.getString("message");
										if (resp.equals("Pincode Not Available")) {
											gen_pincodeTv
													.setVisibility(View.VISIBLE);
											gen_pincodeTv.setText(gen_pincode);
											gen_check_pin_LL
													.setVisibility(View.GONE);
											gen_available_LL
													.setVisibility(View.VISIBLE);
											gen_availableTv
													.setText("Not Available");
											gen_shipstoTv
													.setVisibility(View.GONE);

										} else {
											gen_pincodeTv.setText(gen_pincode);
											gen_check_pin_LL
													.setVisibility(View.GONE);
											gen_available_LL
													.setVisibility(View.VISIBLE);
											gen_availableTv
													.setText("Available");
											gen_shipstoTv
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
									String err = String.valueOf(error);
									// Toast.makeText(getApplicationContext(),
									// err, Toast.LENGTH_LONG).show();

								}
							});
					if (VolleyRequest.addJsonObjectRequest(
							getApplicationContext(), gen_pinjson)) {

						// Toast.makeText(getApplicationContext(),
						// "value searching", Toast.LENGTH_LONG).show();
					} else {
						// Toast.makeText(getApplicationContext(),
						// "value not found", Toast.LENGTH_LONG).show();
					}

				}
			}
		});

		gen_change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gen_pincodeEt.setText("");
				gen_pincodeEt.requestFocus();
				gen_check_pin_LL.setVisibility(View.VISIBLE);
				gen_available_LL.setVisibility(View.GONE);

			}
		});
		UIUtils.addRemoveUnits(gen_unitLayout, activity);

	}

	public void genBuy() {
		CartDataBean odBean = new CartDataBean();
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(activity);
		odBean.setUbean(ubean);
		ListForSubs.get(0).setUnits(
				Integer.parseInt(gen_unitValue.getText().toString()));

		odBean.getProdList().add(ListForSubs.get(0));
		Okler.getInstance().setSingleCart(odBean);
		// ArrayList<OrderDataBean> orderList = new ArrayList<OrderDataBean>();
		// orderList.add(odBean);
		// odBean.setProdList();
		// Okler.getInstance().setOrderList(orderList);
		// Toast.makeText(getApplicationContext(), "general",
		// Toast.LENGTH_SHORT).show();
		// }
		// }
		Intent intent = new Intent(getApplicationContext(),
				ProductCheckoutListView.class);
		intent.putExtra("Check", 1);
		startActivity(intent);
	}

	public void genAddCartWebService() {
		CartDataBean mainbean = Okler.getInstance().getMainCart();
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(activity);
		mainbean.setUbean(ubean);
		int uni = Integer.parseInt(gen_unitValue.getText().toString());
		int pid = ListForSubs.get(0).getProdId();
		mainbean.getProdList().add(ListForSubs.get(0));
		Okler.getInstance().setMainCart(mainbean);
		String addToCartUrl, add1, add2, add3;
		add1 = getString(R.string.save_cart_url);
		add2 = getString(R.string.cancel_order_diagno_custId);
		add3 = getString(R.string.quantity);
		int userId = ubean.getId();
		addToCartUrl = add1 + pid + add2 + userId + add3 + uni;

		WebJsonObjectRequest addjson = new WebJsonObjectRequest(Method.GET,
				addToCartUrl, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						JSONObject jboj = (JSONObject) response;
						try {
							String msg = jboj.getString("message");
							if (msg.equals("save the user cart items.")) {
								Toast.makeText(getApplicationContext(),
										"Added to cart Successfully",
										Toast.LENGTH_SHORT).show();
								UIUtils.setCartCount(notifCount, activity);
								// genAddToCart.setEnabled(false);
							} else {
								Toast.makeText(getApplicationContext(),
										"Some error ocured while adding.",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							Toast.makeText(getApplicationContext(),
									"Some error ocured while adding.",
									Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				});
		VolleyRequest.addJsonObjectRequest(getApplicationContext(), addjson);
		//setCartQuantity();
	}

	public void mainProductUI() {

		TextView item_name, brand_name, presc_req, tv_MrpValue, tv_youSaveValue, okler_Amount;
		ImageLoader imgloader;
		NetworkImageView item_image = (NetworkImageView) findViewById(R.id.item_image);
		String second = "";

		item_name = (TextView) findViewById(R.id.item_name);
		brand_name = (TextView) findViewById(R.id.brand_name);
		presc_req = (TextView) findViewById(R.id.presc_req);
		tv_MrpValue = (TextView) findViewById(R.id.tv_MrpValue);
		tv_youSaveValue = (TextView) findViewById(R.id.tv_youSaveValue);
		okler_Amount = (TextView) findViewById(R.id.okler_Amount);

		viewMoreTv = (TextView) findViewById(R.id.viewMoreTv);
		viewMoreTv.setText(viewMore);
		desc_Main_RL = (LinearLayout) findViewById(R.id.desc_Main_RL);
		viewLessTv = (TextView) findViewById(R.id.viewLessTv);
		viewLessTv.setText(viewLess);
		RelativeLayout relativeHiddenLayout = (RelativeLayout) findViewById(R.id.relativeHiddenLayout);
		relativeHiddenLayout.setVisibility(View.VISIBLE);
		// Button cartToHide = (Button)findViewById(R.id.image_cart);
		// cartToHide.setVisibility(View.GONE);
		TextView mrpTv = (TextView) findViewById(R.id.tv_MrpValue);
		mrp = new SpannableString(mrpTv.getText().toString());
		mrp.setSpan(new StrikethroughSpan(), 0, mrp.length(), 0);
		mrpTv.setText(mrp);
		TextView change = (TextView) findViewById(R.id.changeTv);
		change.setText(change_pin);
		addToCart = (ImageView) findViewById(R.id.cartIV);

		// ArrayList<ProductDataBean> prodListAllo =
		// Okler.getInstance().getProdList();

		check = (Button) findViewById(R.id.button_check);
		pincodeEt = (EditText) findViewById(R.id.enter_pincode_Et);
		pincodeTv = (TextView) findViewById(R.id.pincodeTv);
		check_pin_LL = (LinearLayout) findViewById(R.id.check_pin_LL);
		edit_pincode = (ImageView) findViewById(R.id.edit_pincode);
		available_LL = (RelativeLayout) findViewById(R.id.available_LL);
		unitLayout = (RelativeLayout) findViewById(R.id.unitLayout);
		availableTv = (TextView) findViewById(R.id.availableTv);
		shipstoTv = (TextView) findViewById(R.id.shipstoTv);

		viewMoreTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewMoreTv.setVisibility(View.GONE);
				viewLessTv.setVisibility(View.VISIBLE);
				desc_Main_RL.setVisibility(View.VISIBLE);

			}
		});
		viewLessTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewMoreTv.setVisibility(View.VISIBLE);
				viewLessTv.setVisibility(View.GONE);
				desc_Main_RL.setVisibility(View.GONE);
			}
		});

		try {
			JSONObject json1 = new JSONObject(medId);

			item_name.setText(json1.optString("prodName"));
			String company = json1.optString("company");
			if (company.equals("") || company.equals("null")
					|| company.equals(null) || company == null
					|| company.length() == 0) {
				brand_name.setVisibility(View.GONE);
			} else {
				brand_name.setText(company);
			}
			int presc_check = json1.getInt("presc_needed");
			if (presc_check == 0) {
				presc_req.setVisibility(View.GONE);
			}
			float mrp = Float.parseFloat(json1.optString("mrp"));
			tv_MrpValue.setText("" + mrp);
			tv_youSaveValue
					.setText((String.valueOf(json1.getInt("discount")) + "%"));
			String part1 = okler_Amount.getText().toString();
			float ok_price = Float.parseFloat(json1.optString("oklerPrice"));
			String full = part1 + ok_price;
			okler_Amount.setText(full);
			prodId = json1.getInt("prodId");
			second = json1.optString("imgUrl");
			if (Okler.getInstance().getBookingType() == 0) {
				First = json1.optString("clipArtUrl");
			} else {
				First = json1.optString("mediumUrl");
			}
			Log.e("URL", First + second);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		pinurlpart1 = getString(R.string.getPinUrlPart1);
		pinurlpart2 = getString(R.string.getPinUrlPart2);

		check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pincodeEt.getText().toString().trim().length() > 6
						|| pincodeEt.getText().toString().trim().length() < 6) {
					Toast.makeText(getApplicationContext(),
							"Please Enter Correct Pincode", Toast.LENGTH_LONG)
							.show();
				} else {
					pincode = pincodeEt.getText().toString().trim();

					pincodeUrl = pinurlpart1 + prodId + pinurlpart2 + pincode;
					WebJsonObjectRequest pinjson = new WebJsonObjectRequest(
							Method.GET, pincodeUrl, new JSONObject(),
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									String resp = String.valueOf(response);

									JSONObject pinobj = (JSONObject) response;
									try {
										resp = pinobj.getString("message");
										if (resp.equals("Pincode Not Available")) {
											pincodeTv.setText(pincode);
											check_pin_LL
													.setVisibility(View.GONE);
											available_LL
													.setVisibility(View.VISIBLE);
											availableTv
													.setText("Not Available");
											shipstoTv.setVisibility(View.GONE);

										} else {
											pincodeTv.setText(pincode);
											check_pin_LL
													.setVisibility(View.GONE);
											available_LL
													.setVisibility(View.VISIBLE);
											availableTv.setText("Available");
											shipstoTv
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
									String err = String.valueOf(error);

								}
							});
					VolleyRequest.addJsonObjectRequest(getApplicationContext(),
							pinjson);

				}
			}
		});

		change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pincodeEt.setText("");
				pincodeEt.requestFocus();
				check_pin_LL.setVisibility(View.VISIBLE);
				available_LL.setVisibility(View.GONE);

			}
		});

		imgloader = VolleyRequest.getInstance(getApplicationContext())
				.getImageLoader();
		// String First=
		// "http://183.82.110.105:8081/oklerdevv2/uploads/images/medium/";

		String photo_url = First + second;
		item_image.setImageUrl(photo_url, imgloader);

		UIUtils.addRemoveUnits(unitLayout, activity);
	}

	public void availableSubstitutes() {
		substitutesLL = (LinearLayout) findViewById(R.id.substitutesLL);
		substitutesLL.setVisibility(View.VISIBLE);
		getMedSubUrl1 = getString(R.string.getMedSubUrl);
		// getMedSubUrl = getMedSubUrl1+8568;
		getMedSubUrl = getMedSubUrl1 + prodId;

		WebJsonObjectRequest subjson = new WebJsonObjectRequest(Method.GET,
				getMedSubUrl, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						String resp = String.valueOf(response);

						JSONObject subjobj = (JSONObject) response;
						JSONObject jboj2 = new JSONObject();
						ProductDataBean subpdBean = null;
						JSONArray subarr = new JSONArray();
						JSONObject subobj2 = new JSONObject();

						try {
							jboj2 = subjobj.getJSONObject("result");
							subarr = jboj2.getJSONArray("substitutes_products");
							int len = subarr.length();
							if (len <= 0) {
								genView = findViewById(R.id.top_gen_info);
								genView.setVisibility(View.GONE);
							} else {
								CustomViewProdSubst[] customViewProdSubst = new CustomViewProdSubst[500];
								for (i1 = 0; i1 < subarr.length(); i1++) {
									customViewProdSubst[i1] = new CustomViewProdSubst(
											getApplicationContext());
									// customVi1ewProdSubst[i]=null;
									subobj2 = subarr.getJSONObject(i1);
									subpdBean = new ProductDataBean();
									subpdBean.setProdId(Integer
											.parseInt(subobj2.optString("id")));
									subpdBean.setProdName(subobj2
											.optString("name"));
									subpdBean.setDesc(subobj2
											.optString("description"));
									subpdBean.setSide_effect(subobj2
											.optString("side_effects"));
									subpdBean.setMrp(Float.parseFloat(subobj2
											.optString("price")));
									subpdBean.setOklerPrice(Float
											.parseFloat(subobj2
													.optString("saleprice")));
									subpdBean.setDiscount(Float
											.parseFloat(subobj2
													.optString("discount")));
									subpdBean.setWeight(subobj2
											.optString("weight"));
									subpdBean.setPresc_needed(Integer.parseInt(subobj2
											.optString("prescription_need")));
									subpdBean.setCompany(subobj2
											.optString("company_name"));
									subpdBean.setIndications(subobj2
											.optString("indications"));
									subpdBean.setDosage(subobj2
											.optString("dosage"));
									subpdBean.setTax(Float.parseFloat(subobj2
											.optString("tax")));
									subpdBean.setPacking_size(subobj2
											.optString("type_of_packing"));
									String img = "";
									if (subobj2.optString("images")
											.equals(null)) {
										img = "";
									} else {
										img = subobj2.optString("images");
									}
									subpdBean.setImgUrl(img);

									subpdBean.setProdType(0);
									// subpdBean.setContraIndi(subobj2.getString(name));
									// subpdBean.setCaution(subobj2.getString(name));
									if (subobj2.has("generic_val")) {
										if (1 == 1) {
										}
										subpdBean.setGeneric_name(subobj2
												.optString("generic_val"));
									}
									SubMainLL1 = (LinearLayout) customViewProdSubst[i1]
											.findViewById(R.id.SubMainLL);
									TextView subtsName = (TextView) customViewProdSubst[i1]
											.findViewById(R.id.subtsName);
									subtsName.setText(subpdBean.getProdName());
									//TextView mrpValueSub = (TextView) customViewProdSubst[i1]
										//	.findViewById(R.id.mrpValueSub);
									//mrpValueSub.setText("" + subpdBean.getMrp());
									TextView oklerPrValueSub = (TextView) customViewProdSubst[i1]
											.findViewById(R.id.oklerPrValueSub);
									oklerPrValueSub.setText(""
											+ subpdBean.getOklerPrice());
									TextView youSavePercSub = (TextView) customViewProdSubst[i1]
											.findViewById(R.id.youSavePercSub);
									youSavePercSub.setText(subpdBean
											.getDiscount() + "%");
									customViewProdSubst[i1]
											.setBackgroundColor(getResources()
													.getColor(
															R.color.LightGreyWhite));
									customViewProdSubst[i1]
											.setLayoutParams(params);
									customViewProdSubst[i1].requestLayout();
									customViewProdSubst[i1].setTag("" + i1);
									substitutesLL
											.addView(customViewProdSubst[i1]);
									ListForSubs.add(subpdBean);
									customViewProdSubst[i1]
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													int a = Integer.parseInt(String
															.valueOf(v.getTag()));
													ProductDataBean pdbean = new ProductDataBean();
													pdbean = ListForSubs.get(a);
													// Toast.makeText(getApplicationContext(),
													// pdbean.getProdName(),
													// Toast.LENGTH_LONG).show();
													Gson gson = new Gson();
													String pbean = gson
															.toJson(pdbean);
													Intent intent = new Intent(
															getApplicationContext(),
															ProductDetailsActivity.class);
													intent.putExtra("MedId",
															pbean);
													intent.putExtra("MedList",
															22);
													finish();
													startActivity(intent);

												}
											});

								}
								Okler.getInstance().setSubProdList(ListForSubs);
								topGenProductUI();
							}
							// Toast.makeText(getApplicationContext(),
							// ""+subarr.length(), Toast.LENGTH_LONG).show();
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						String err = String.valueOf(error);
						// Toast.makeText(getApplicationContext(), err,
						// Toast.LENGTH_LONG).show();

					}
				});
		VolleyRequest.addJsonObjectRequest(getApplicationContext(), subjson);
	}

	public void buyNow() {

		buyIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// ubean.setId(2);
				// ubean.getId();
				if (availableTv.getText().toString().equals("Available")
						&& available_LL.getVisibility() == View.VISIBLE) {
					String unit = unitValue.getText().toString();
					if (unit.equals(""))
						unit = "00";
					if (unit.length() > 2) {
						unitValue.setText("");
						unitValue.requestFocus();
						Toast.makeText(activity, "Maximum limit is 99",
								Toast.LENGTH_SHORT).show();
					} else {
						int units = Integer.parseInt(unit);
						if (units <= 0) {
							Toast.makeText(activity,
									"Please Select atleast one unit",
									Toast.LENGTH_LONG).show();
							unitValue.requestFocus();
						}

						else {
							UserStatusEnum u = Utilities
									.getUserStatusFromSharedPref(activity);
							if (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN
									|| (Utilities
											.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_FB)
									|| (Utilities
											.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
								buy();
							} else {
								Intent intent = new Intent(activity,
										NewSignIn.class);
								buy = true;
								startActivity(intent);
							}

						}
					}
				} else if (check_pin_LL.getVisibility() == View.VISIBLE) {
					pincodeEt
							.setError("Please Check availability at your PinCode.");
					pincodeEt.requestFocus();
					// Toast.makeText(activity,
					// "Please Check availability at your PinCode.",
					// Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(activity,
							"Product not available at your PinCode.",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public void addToCart() {

		addToCart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * if(isAddedToCart){ Toast.makeText(activity,
				 * "This Product is already in your Cart.",
				 * Toast.LENGTH_LONG).show(); }else{
				 */

				mainbean = Okler.getInstance().getMainCart();
				boolean flagCart = false;
				for (int i = 0; i < mainbean.getProdList().size(); i++) {
					int id = mainbean.getProdList().get(i).getProdId();
					if (id == prodId) {
						Toast.makeText(activity,
								"This Product is already in your Cart.",
								Toast.LENGTH_LONG).show();
						isAddedToCart = true;
						flagCart = true;
						break;
					}
				}
				if (!flagCart)
					isAddedToCart = false;

				if (!isAddedToCart) {
					// int unit = 0;
					if (availableTv.getText().toString().equals("Available")
							&& available_LL.getVisibility() == View.VISIBLE) {
						unit = unitValue.getText().toString();
						if (unit.equals(""))
							unit = "00";
						if (unit.length() > 2) {
							unitValue.requestFocus();
							unitValue.setText("");
							Toast.makeText(activity, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						} else {
							int units = Integer.parseInt(unit);
							if (units <= 0) {
								Toast.makeText(activity,
										"Please Select atleast one unit",
										Toast.LENGTH_LONG).show();
								unitValue.requestFocus();
							} else {
								if (Utilities
										.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN
										|| (Utilities
												.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_FB)
										|| (Utilities
												.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_GOOGLE)) {

									addCartWebService();
								} else {
									Intent intent = new Intent(activity,
											NewSignIn.class);
									addCart = true;
									startActivity(intent);
								}
							}
						}
					} else if (check_pin_LL.getVisibility() == View.VISIBLE) {
						pincodeEt
								.setError("Please Check availability at your PinCode.");
						pincodeEt.requestFocus();
						// Toast.makeText(activity,
						// "Please Check availability at your PinCode.",
						// Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(activity,
								"Product not available at your PinCode.",
								Toast.LENGTH_LONG).show();

					}
				}
			}
		});

	}

	private void setTouchListenerScrViews(HorizontalScrollView horScrView) {
		horScrView.setOnTouchListener(new ListView.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					// Disallow ScrollView to intercept touch events.
					scrView.requestDisallowInterceptTouchEvent(true);
					break;

				case MotionEvent.ACTION_UP:
					// Allow ScrollView to intercept touch events.
					scrView.requestDisallowInterceptTouchEvent(false);
					break;
				}

				// Handle HorizontalScrollView touch events.
				v.onTouchEvent(event);
				return true;
			}
		});
	}

	private void shareProd(String prodName) {
		// create the send intent
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

		// set the type
		shareIntent.setType("text/plain");

		// add a subject
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Get "
				+ prodName + " From Okler.");

		// build the body of the message to be shared
		String shareMessage = "Get "
				+ prodName
				+ " From Okler."
				+ "\n"
				+ "Download Okler today. Visit https://play.google.com/store/apps/details?id=com.okleruser&hl=en";

		// add the message
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);

		// start the chooser for sharing
		startActivity(Intent.createChooser(shareIntent, "Share Okler Via"));
	}

	public void addFavourite() {
		addToFav1 = getString(R.string.AddToFavouriteUrlPart1);
		addToFav2 = getString(R.string.AddToFavouriteUrlPart2);
		addToFavUrl = addToFav1 + userId + addToFav2 + prodId;
		// Toast.makeText(getApplicationContext(), "fun",
		// Toast.LENGTH_LONG).show();
		image_favourite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(getApplicationContext(), "onClick",
				// Toast.LENGTH_LONG).show();
				if (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN
						|| (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_FB)
						|| (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
					WebJsonObjectRequest json = new WebJsonObjectRequest(
							Method.GET, addToFavUrl, new JSONObject(),
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {

									JSONObject jobj = (JSONObject) response;
									String msg = jobj.optString("message");
									if (msg.equals("Added success from favourites")) {
										Toast.makeText(
												getApplicationContext(),
												"Added to your favourites successfully",
												Toast.LENGTH_SHORT).show();
										for (int i = 0; i < pArrList2.size(); i++) {
											int id = pArrList2.get(i)
													.getProdId();
											if (id == prodId) {
												ProductDataBean pbean = pArrList2
														.get(i);
												ArrayList<ProductDataBean> favList = Okler
														.getInstance()
														.getFavourites();
												favList.add(pbean);
												Okler.getInstance()
														.setFavourites(favList);
												image_favourite
														.setImageResource(R.drawable.fav_filled_heart_icon);
												break;
											}
										}
									}

								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {

									// Toast.makeText(getApplicationContext(),
									// String.valueOf(error),
									// Toast.LENGTH_LONG).show();
								}
							});

					VolleyRequest.addJsonObjectRequest(getApplicationContext(),
							json);
				} else {
					// Toast.makeText(getApplicationContext(), "else",
					// Toast.LENGTH_LONG).show();
					Intent in = new Intent(activity, NewSignIn.class);
					startActivity(in);
				}
			}
		});
	}

	public void addCartWebService() {
		addCart = false;
		UsersDataBean ubean = new UsersDataBean();
		ubean = Utilities.getCurrentUserFromSharedPref(activity);
		mainbean.setUbean(ubean);

		for (int i = 0; i < pArrList2.size(); i++) {
			int id = pArrList2.get(i).getProdId();
			if (id == prodId) {
				pArrList2.get(i).setUnits(
						Integer.parseInt(unitValue.getText().toString()));
				units = Integer.parseInt(unitValue.getText().toString());
				mainbean.getProdList().add(pArrList2.get(i));
				break;
			}
		}

		String addToCartUrl, add1, add2, add3;
		add1 = getString(R.string.save_cart_url);
		add2 = getString(R.string.cancel_order_diagno_custId);
		add3 = getString(R.string.quantity);
		int userId = ubean.getId();
		addToCartUrl = add1 + prodId + add2 + userId + add3 + unit;

		WebJsonObjectRequest addjson = new WebJsonObjectRequest(Method.GET,
				addToCartUrl, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						JSONObject jboj = (JSONObject) response;
						try {
							String msg = jboj.getString("message");
							if (msg.equals("save the user cart items.")) {
								Toast.makeText(getApplicationContext(),
										"Added to cart Successfully",
										Toast.LENGTH_SHORT).show();
								// addToCart.setEnabled(false);
								isAddedToCart = true;
								Okler.getInstance().setMainCart(mainbean);
								UIUtils.setCartCount(notifCount, activity);
							} else {
								Toast.makeText(getApplicationContext(),
										"Some error ocured while adding.",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							Toast.makeText(getApplicationContext(),
									"Some error ocured while adding.",
									Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				});
		VolleyRequest.addJsonObjectRequest(getApplicationContext(), addjson);
	//	setCartQuantity();
	}

	public void buy() {
		CartDataBean odBean = new CartDataBean();
		// temp code for id
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(activity);
		odBean.setCurUBean(ubean);
		for (int i = 0; i < pArrList2.size(); i++) {
			int id = pArrList2.get(i).getProdId();
			if (id == prodId) {
				pArrList2.get(i).setUnits(
						Integer.parseInt(unitValue.getText().toString()));
				odBean.getProdList().add(pArrList2.get(i));
				Okler.getInstance().setSingleCart(odBean);
				break;
			}
		}
		Intent intent = new Intent(getApplicationContext(),
				ProductCheckoutListView.class);
		intent.putExtra("Check", 1);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// ProductDetailsActivity.addToCart.setEnabled(true);
		// ProductDetailsActivity.genAddToCart.setEnabled(true);
		UIUtils.setCartCount(notifCount, activity);
		if (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN
				|| (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_FB)
				|| (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
			if (addCart) {
				addCart = false;
				addCartWebService();
			}
			if (buy) {
				buy = false;
				buy();
			}
			if (gen_addCart) {
				gen_addCart = false;
				genAddCartWebService();
			}
			if (gen_buy) {
				gen_buy = false;
				genBuy();
			}
		} else {
			/*
			 * Intent intent = new Intent(activity,NewSignIn.class); buy=true;
			 * startActivity(intent);
			 */
		}
	}

	public void getProductInfoById(String pid) {
		String part1, part2, part3, getProdUrl;

		part1 = getString(R.string.getProdInfoByIdUrlPart1);
		part2 = getString(R.string.getProdInfoByIdUrlPart2);
		part3 = getString(R.string.getProdInfoByIdUrlPart3);
		getProdUrl = part1 + pid + part2 + "1" + part3;
		// getProdUrl = part1+pid+part2+"0"+part3;
		WebJsonObjectRequest prodjson = new WebJsonObjectRequest(Method.GET,
				getProdUrl, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						JSONObject jobj = new JSONObject();
						JSONObject jobj2 = new JSONObject();
						// JSONObject jobj3 = new JSONObject();
						// JSONArray jArray = new JSONArray();

						try {
							jobj = response.getJSONObject("result");
							jobj2 = jobj.getJSONObject("0");

							hsBean = new ProductDataBean();
							hsBean.setProdName(jobj2.getString("name"));
							if (jobj2.has("type_of_packing")) {
							}
							hsBean.setDesc(jobj2.getString("description"));
							hsBean.setProdId(Integer.valueOf(jobj2
									.getString("id")));
							hsBean.setKey_feature("key_features");
							hsBean.setBrandInfo("brandinfo");
							hsBean.setWarranty("warranty");
							hsBean.setSpecfic("specifications");
							hsBean.setMrp(jobj2.getInt("price"));
							hsBean.setOklerPrice(jobj2.getInt("saleprice"));
							hsBean.setDiscount(jobj2.getInt("discount"));
							String jimg = jobj2.getString("images");
							String url2;
							if (jimg.equals(null) || jimg.equals("null")
									|| jimg.equals("")) {
								url2 = "drawable://" + R.drawable.tempcuff;
							} else {
								String j1[] = jimg.split(",");
								String j2 = j1[0];
								String colon = ":";
								String j3[] = j2.split(colon);
								String url = j3[2];
								// String url1 = url.substring(1);
								int length = url.length();
								url2 = url.substring(1, (length - 1));
							}

							hsBean.setImgUrl(url2);
							JSONObject uobj = new JSONObject();
							uobj = response.getJSONObject("product_image_url");
							hsBean.setMediumUrl(uobj
									.optString("productimage_url_medium"));
							hsBean.setSmallUrl(uobj
									.optString("productimage_url_small"));
							hsBean.setThumbUrl(uobj
									.optString("productimage_url_thumbnail"));

							ArrayList<ProductDataBean> hList = new ArrayList<ProductDataBean>();
							hList.add(hsBean);
							Okler.getInstance().setProdList(hList);
							Gson gson = new Gson();
							String hsbean = gson.toJson(hsBean);
							Intent intent = new Intent(context,
									ProductDetailsActivity.class);
							intent.putExtra("MedId", hsbean);
							startActivity(intent);
							finish();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				});

		VolleyRequest.addJsonObjectRequest(activity, prodjson);
		// return hsBean;
	}
}
