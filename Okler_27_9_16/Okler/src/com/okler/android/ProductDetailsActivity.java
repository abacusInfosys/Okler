package com.okler.android;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import java.io.UnsupportedEncodingException;
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
import com.okler.customviews.CustomViewProdInfo;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.JustViewedProdsDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.dialogs.ZoomimageDialogFragment;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

	   Bitmap bitmap;
	 
	// variable section for medicines
	RelativeLayout parentImageFav;
	TextView viewMoreTv, viewLessTv,desc_heading, indi_heading, 
			cIndi_heading, caution_heading, sEff_heading, weight_heading,
			dosage_heading, pincodeTv, availableTv,	shipstoTv;
	RelativeLayout desc_details_RL, indi_details_RL, contra_indi_details_RL,
			caution_details_RL, side_eff_details_RL, weight_details_RL,
			dosage_Details_RL, available_LL, unitLayout, RL_For_Prod_Info;// 13_01_2016 Gitesh
	ImageView image_favourite, add_button_desc, minus_button_desc, add_indi,
			minus_indi, add_contra_indi, minus_contra_indi, add_caution,
			minus_caution, add_side_eff, minus_side_eff, add_weight,
			minus_weight, add_dosage, minus_dosage, edit_pincode;// 13_01_2016
																	// Gitesh
	CustomViewProdInfo customViewDesc, customViewIndi, customViewCIndi,
			customViewCaution, customViewSEff, customViewWeight,
			customViewDosage;
	LinearLayout desc_Main_RL, check_pin_LL, substitutesLL,
			SubMainLL1, SubMainLL2, SubMainLL3;
	Button check;
	EditText pincodeEt, unitValue;
	String pincodeUrl, pinurlpart1, pinurlpart2, pincode,photo_url="";
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
	SpannableString viewMore, viewLess, mrp, change_pin;
	LinearLayout.LayoutParams params;
	ImageView imgBack, buyIV;
	int i1;
	ProductDataBean hsBean,prodBean;
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
	boolean addCart = false,buy = false,isAddedToCart = false;
	String unit;
	int units;
	String First;
	CartDataBean mainbean;
	String prod_id_recent_prod = "", prod_id_similar_prod = "";
	JustViewedProdsDataBean jbean;
	ArrayList<ProductDataBean> favs = new ArrayList<ProductDataBean>();
	boolean isFav;
	NetworkImageView item_image;
	ImageLoader imgloader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_details);
		context = getApplicationContext();
		medId = getIntent().getStringExtra("MedId");
		prodBean = UIUtils.getProdBean(medId);
		item_image = (NetworkImageView) findViewById(R.id.item_image);
		
		
		item_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ZoomimageDialogFragment zoom=new ZoomimageDialogFragment(bitmap);
				zoom.show(getFragmentManager(), "");
				
			}
		});
	
		
		curList = getIntent().getIntExtra("MedList", 100);
		activity = this;
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		notifCount = (Button) toolBar.findViewById(R.id.notif_count);
		ubean = Utilities.getCurrentUserFromSharedPref(activity);
		userId = ubean.getId();
		txt_item = (TextView) findViewById(R.id.no_items);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		imgloader = VolleyRequest.getInstance(getApplicationContext())
				.getImageLoader();
		ActionBar ab = getSupportActionBar();
		image_favourite = (ImageView) findViewById(R.id.image_favourite);
		parentImageFav = (RelativeLayout)findViewById(R.id.parentImageFav);
		UIUtils.setBackClick(toolBar, activity);
		unitValue = (EditText) findViewById(R.id.unitValue);
		unitValue.requestFocus();
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
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
		if (Okler.getInstance().getBookingType() == 0) {
			mainProductUI();
			alloUI();
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

		parentImageFav.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UIUtils.addRemoveFavourites(v, activity, prodBean);
			}
		});
	}

	private void mainProductHsUI() {

		TextView item_name, brand_name, presc_req, tv_MrpValue, tv_youSaveValue, okler_Amount;
		
		
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
		TextView mrpTv = (TextView) findViewById(R.id.tv_MrpValue);
		mrp = new SpannableString(mrpTv.getText().toString());
		mrp.setSpan(new StrikethroughSpan(), 0, mrp.length(), 0);
		mrpTv.setText(mrp);
		TextView change = (TextView) findViewById(R.id.changeTv);
		change.setText(change_pin);
		addToCart = (ImageView) findViewById(R.id.cartIV);
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
			parentImageFav.setTag(prodId);
			if (Okler.getInstance().getBookingType() == 0) {
				First = json1.optString("clipArtUrl");
			} else {
				First = json1.optString("mediumUrl");
			}
			second = json1.getString("imgUrl");
			Log.e("URL", First + second);

		} catch (JSONException e) {
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
											buyIV.setEnabled(true);
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									Log.e("Error", String.valueOf(error));
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

		
		
		photo_url = First + second;       
		
		item_image.setImageUrl(photo_url, imgloader);
		
		imgloader.get(photo_url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
            	
            	bitmap =imgloader.get(photo_url, ImageLoader.getImageListener(item_image,
                        R.drawable.tempcuff, android.R.drawable
                                .ic_dialog_alert)).getBitmap();
            	
                /*Intent intent = new Intent(getApplicationContext(),NotificationReceiverActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, 0);


                Notification noti = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Mail Notification")
                        .setContentText("5 new unread messages")
                        .setSmallIcon(R.drawable.bgd)
                        .setStyle(new Notification.BigPictureStyle().bigPicture(bit))
                        .setAutoCancel(true)
                        .setContentIntent(pIntent).build();


                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                noti.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(0,noti);*/

                Log.i("LOG>>>>>>>>>>>>>>>>>>>","inside the on response");
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
            	//bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                Log.i("LOG>>>>>>>>>>>>>>>>>>>","inside the errror response");


            }
        });
		
		/*Bitmap bit=Bitmap.createBitmap(item_image.getWidth(), item_image.getHeight(), Bitmap.Config.ARGB_8888);
		 canvas=new Canvas(bit);
*/		UIUtils.addRemoveUnits(unitLayout, activity);
	}

	private void healthShopFunction() {
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
	}

	private void displayrecentProds() {
		jbean = new JustViewedProdsDataBean();
		jbean = Utilities.getjustViewdProdsFromSharedPref(activity);
		ArrayList<ProductDataBean> recendProds = new ArrayList<ProductDataBean>();
		recendProds = jbean.getJustViewedProdsList();
		recendProds.trimToSize();
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

	private void share(final int prodId) {
		shareBtn = (ImageView) findViewById(R.id.shareIV);
		shareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						String prodName = prodBean.getProdName();
						shareProd(prodName);
			}
		});
	}

	private void keyFeature() {
		TextView keyDetail;
		minus_button_Hskey = (ImageView) customViewKeyFeature
				.findViewById(R.id.minusButton_indi);
		Hskey_details_RL = (RelativeLayout) customViewKeyFeature
				.findViewById(R.id.indi_details_RL);
		TextView Hskey_heading = (TextView) customViewKeyFeature
				.findViewById(R.id.indi_title);
		Hskey_heading.setText("KEY FEATURES");
		keyDetail = (TextView) customViewKeyFeature.findViewById(R.id.details);
		RelativeLayout title_RL = (RelativeLayout) customViewKeyFeature
				.findViewById(R.id.title_RL);
		desc_Main_RL.addView(customViewKeyFeature);
		customViewKeyFeature.setLayoutParams(params);
		customViewKeyFeature.requestLayout();
		try {
			JSONObject jsonAllo = new JSONObject(medId);
			String feature = jsonAllo.getString("key_feature");
			keyDetail.setText(Html.fromHtml(feature));
		} catch (Exception e) {
		}
		add_button_Hskey = (ImageView) customViewKeyFeature
				.findViewById(R.id.add_Button_indi);
		title_RL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (add_button_Hskey.getVisibility() == 0) {
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
				} else {
					add_button_Hskey.setVisibility(View.VISIBLE);
					minus_button_Hskey.setVisibility(View.GONE);
					Hskey_details_RL.setVisibility(View.GONE);
				}

			}
		});
	}

	private void Descrption() {
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
		RelativeLayout title_RL = (RelativeLayout) customViewDescption
				.findViewById(R.id.title_RL);
		descDetail = (TextView) customViewDescption.findViewById(R.id.details);
		desc_Main_RL.addView(customViewDescption);
		customViewDescption.setLayoutParams(params);
		customViewDescption.requestLayout();
		try {
			JSONObject jsonAllo = new JSONObject(medId);
			descDetail.setText(jsonAllo.getString("desc"));
		} catch (Exception e) {
		}
		title_RL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (add_button_HSdesc.getVisibility() == 0) {
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
				} else {
					add_button_HSdesc.setVisibility(View.VISIBLE);
					minus_button_HSdesc.setVisibility(View.GONE);
					HSdesc_details_RL.setVisibility(View.GONE);
				}
			}
		});
	}

	private void specification() {
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
		RelativeLayout title_RL = (RelativeLayout) customViewSpeci
				.findViewById(R.id.title_RL);
		customViewSpeci.setLayoutParams(params);
		customViewSpeci.requestLayout();
		try {
			JSONObject jsonAllo = new JSONObject(medId);
			specDetail.setText(jsonAllo.getString("specfic"));
		} catch (Exception e) {
		}
		title_RL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (add_button_HSspec.getVisibility() == 0) {
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
				} else {
					add_button_HSspec.setVisibility(View.VISIBLE);
					minus_button_HSspec.setVisibility(View.GONE);
					HSspec_details_RL.setVisibility(View.GONE);
				}

			}
		});

	}

	private void brandInfo() {
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
		RelativeLayout title_RL = (RelativeLayout) customViewBInfo
				.findViewById(R.id.title_RL);
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
				if (add_button_HSbrand.getVisibility() == 0) {
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
				} else {
					add_button_HSbrand.setVisibility(View.VISIBLE);
					minus_button_HSbrand.setVisibility(View.GONE);
					HSbrand_details_RL.setVisibility(View.GONE);
				}

			}
		});

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
		RelativeLayout title_RL = (RelativeLayout) customViewWarrant
				.findViewById(R.id.title_RL);
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
				if (add_button_HSwar.getVisibility() == 0) {
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
				} else {
					add_button_HSwar.setVisibility(View.VISIBLE);
					minus_button_HSwar.setVisibility(View.GONE);
					HSwar_details_RL.setVisibility(View.GONE);
				}

			}
		});

	}
	private void displaySimilarProd() {
		final LinearLayout innerLayout = (LinearLayout) findViewById(R.id.innerLayout1);
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
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
		VolleyRequest.addJsonObjectRequest(getApplicationContext(), addjson);
	}

	private void addCustRelatedProducts(LinearLayout innerLayout, String name,
			String imageName2, String pid) {
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
			views[cnt].setTag(pid);
			innerLayout.addView(views[cnt]);
			views[cnt].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
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
		RelativeLayout desctitle_RL = (RelativeLayout) customViewDesc
				.findViewById(R.id.title_RL);
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
		RelativeLayout indititle_RL = (RelativeLayout) customViewIndi
				.findViewById(R.id.title_RL);
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
		RelativeLayout cindititle_RL = (RelativeLayout) customViewCIndi
				.findViewById(R.id.title_RL);
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
		RelativeLayout cautiontitle_RL = (RelativeLayout) customViewCaution
				.findViewById(R.id.title_RL);
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
		RelativeLayout sEfftitle_RL = (RelativeLayout) customViewSEff
				.findViewById(R.id.title_RL);
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
		RelativeLayout weighttitle_RL = (RelativeLayout) customViewWeight
				.findViewById(R.id.title_RL);
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
		RelativeLayout dosagetitle_RL = (RelativeLayout) customViewDosage
				.findViewById(R.id.title_RL);
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
			e.printStackTrace();
		}

		desctitle_RL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (add_button_desc.getVisibility() == 0) {
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
				} else {
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
				}

			}
		});

		indititle_RL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (add_indi.getVisibility() == 0) {
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
				} else {
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
				}

			}
		});

		cindititle_RL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (add_contra_indi.getVisibility() == 0) {
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
				} else {
					add_contra_indi.setVisibility(View.VISIBLE);
					minus_contra_indi.setVisibility(View.GONE);
					contra_indi_details_RL.setVisibility(View.GONE);
				}

			}
		});

		cautiontitle_RL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (add_caution.getVisibility() == 0) {
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
				} else {
					add_caution.setVisibility(View.VISIBLE);
					minus_caution.setVisibility(View.GONE);
					caution_details_RL.setVisibility(View.GONE);
				}

			}
		});

		sEfftitle_RL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (add_side_eff.getVisibility() == 0) {
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
				} else {
					add_side_eff.setVisibility(View.VISIBLE);
					minus_side_eff.setVisibility(View.GONE);
					side_eff_details_RL.setVisibility(View.GONE);
				}

			}
		});

		weighttitle_RL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (add_weight.getVisibility() == 0) {
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
				} else {
					add_weight.setVisibility(View.VISIBLE);
					minus_weight.setVisibility(View.GONE);
					weight_details_RL.setVisibility(View.GONE);
				}

			}
		});

		dosagetitle_RL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (add_dosage.getVisibility() == 0) {
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
				} else {
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);
				}

			}
		});

	}

	private void homeoUI() {
		TextView descdetailsHome, indidetailsHome, cindidetailsHome, cautiondetailsHome, side_effdetailsHome, dosagedetailsHome;
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
		RelativeLayout desctitle_RL = (RelativeLayout) customViewDesc
				.findViewById(R.id.title_RL);
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
		RelativeLayout sEff_title_RL = (RelativeLayout) customViewSEff
				.findViewById(R.id.title_RL);
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
		RelativeLayout dosage_title_RL = (RelativeLayout) customViewDosage
				.findViewById(R.id.title_RL);
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
			e.printStackTrace();
		}
		desctitle_RL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (add_button_desc.getVisibility() == 0) {
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
				} else {
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
				if (add_side_eff.getVisibility() == 0) {
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
				} else {
					add_side_eff.setVisibility(View.VISIBLE);
					minus_side_eff.setVisibility(View.GONE);
					side_eff_details_RL.setVisibility(View.GONE);
				}

			}
		});

		dosage_title_RL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (add_dosage.getVisibility() == 0) {
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
				} else {
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);
				}

			}
		});

	}

	private void ayurUI() {
		TextView descdetailsAyur, indidetailsAyur, cindidetailsAyur, cautiondetailsAyur, weightdetailsAyur, dosagedetailsAyur;

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
		RelativeLayout desctitle_RL = (RelativeLayout) customViewDesc
				.findViewById(R.id.title_RL);
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
		RelativeLayout indititle_RL = (RelativeLayout) customViewIndi
				.findViewById(R.id.title_RL);
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
		RelativeLayout dosagetitle_RL = (RelativeLayout) customViewDosage
				.findViewById(R.id.title_RL);
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
		RelativeLayout weighttitle_RL = (RelativeLayout) customViewWeight
				.findViewById(R.id.title_RL);
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
			weightdetailsAyur.setText(jsonAyur.optString("weight"));
			dosagedetailsAyur.setText(Html.fromHtml(jsonAyur
					.optString("dosage")));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		desctitle_RL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (add_button_desc.getVisibility() == 0) {
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
				} else {
					add_button_desc.setVisibility(View.VISIBLE);
					minus_button_desc.setVisibility(View.GONE);
					desc_details_RL.setVisibility(View.GONE);
				}

			}
		});

		// add minus buttons for indicator
		indititle_RL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (add_indi.getVisibility() == 0) {
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

				} else {
					add_indi.setVisibility(View.VISIBLE);
					minus_indi.setVisibility(View.GONE);
					indi_details_RL.setVisibility(View.GONE);
				}

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
				if (add_weight.getVisibility() == 0) {
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
				} else {
					add_weight.setVisibility(View.VISIBLE);
					minus_weight.setVisibility(View.GONE);
					weight_details_RL.setVisibility(View.GONE);
				}

			}
		});

		dosagetitle_RL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (add_dosage.getVisibility() == 0) {
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
				} else {
					add_dosage.setVisibility(View.VISIBLE);
					minus_dosage.setVisibility(View.GONE);
					dosage_Details_RL.setVisibility(View.GONE);
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	public void setCartQuantity() {
		CartDataBean cbean = Okler.getInstance().getMainCart();
		ArrayList<ProductDataBean> pdList = cbean.getProdList();
		pdList.trimToSize();
		int maincount = pdList.size();
		if (maincount > 9) {
			notifCount.setText("" + maincount);
		} else {
			notifCount.setText("0" + maincount);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		TextView mrpTv = (TextView) findViewById(R.id.tv_MrpValue);
		mrp = new SpannableString(mrpTv.getText().toString());
		mrp.setSpan(new StrikethroughSpan(), 0, mrp.length(), 0);
		mrpTv.setText(mrp);
		TextView change = (TextView) findViewById(R.id.changeTv);
		change.setText(change_pin);
		addToCart = (ImageView) findViewById(R.id.cartIV);

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
			parentImageFav.setTag(prodId);
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
									Log.e("Error", String.valueOf(error));

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
		String photo_url = First + second;
		item_image.setImageUrl(photo_url, imgloader);

		UIUtils.addRemoveUnits(unitLayout, activity);
	}

	public void buyNow() {

		buyIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

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
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Get "
				+ prodName + " From Okler.");
		String shareMessage = "Get "
				+ prodName
				+ " From Okler."
				+ "\n"
				+ "Download Okler today. Visit https://play.google.com/store/apps/details?id=com.okleruser&hl=en";
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
		startActivity(Intent.createChooser(shareIntent, "Share Okler Via"));
	}

	public void addCartWebService() {
		addCart = false;
		UsersDataBean ubean = new UsersDataBean();
		ubean = Utilities.getCurrentUserFromSharedPref(activity);
		mainbean.setUbean(ubean);
				prodBean.setUnits(
						Integer.parseInt(unitValue.getText().toString()));
				mainbean.getProdList().add(prodBean);

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
	}

	public void buy() {
		CartDataBean odBean = new CartDataBean();
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(activity);
		odBean.setCurUBean(ubean);
				prodBean.setUnits(
						Integer.parseInt(unitValue.getText().toString()));
				odBean.getProdList().add(prodBean);
				Okler.getInstance().setSingleCart(odBean);
			//	break;
			//}
		//}
		Intent intent = new Intent(getApplicationContext(),
				ProductCheckoutListView.class);
		intent.putExtra("Check", 1);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
			favs = Utilities.getFavourites(activity);
			//favs = Okler.getInstance().getFavourites();
			favs.trimToSize();
			if (favs.size() != 0) {
				for (int i = 0; i < favs.size(); i++) {
					ProductDataBean pbean = favs.get(i);
					int prodID = pbean.getProdId();
					if (prodID == prodId) {
						image_favourite
								.setBackgroundResource(R.drawable.fav_filled_heart_icon);
						isFav = true;
					}
				}
			} else {
				image_favourite.setBackgroundResource(R.drawable.favourites);
				isFav = false;
			}

		} else {
		}
	}

	public void getProductInfoById(String pid) {
		String part1, part2, part3, getProdUrl;

		part1 = getString(R.string.getProdInfoByIdUrlPart1);
		part2 = getString(R.string.getProdInfoByIdUrlPart2);
		part3 = getString(R.string.getProdInfoByIdUrlPart3);
		getProdUrl = part1 + pid + part2 + "1" + part3;
		WebJsonObjectRequest prodjson = new WebJsonObjectRequest(Method.GET,
				getProdUrl, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						JSONObject jobj = new JSONObject();
						JSONObject jobj2 = new JSONObject();
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
	}
		
}
