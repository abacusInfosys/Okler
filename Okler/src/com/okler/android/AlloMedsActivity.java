package com.okler.android;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.okler.adapters.AlloSubstituesAdapter;
import com.okler.customviews.CustomViewProdInfo;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.ConstituentsDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

public class AlloMedsActivity extends BaseActivity {

	private View bottomBarLayout;
	private Toolbar toolBar;
	ImageView imgBack,minus_units,plus_units,edit_pincode,add_Button_indi,minusButton_indi,
				buyIV,shareBtn,addToCart,sliderIndicatorRight,sliderIndicatorDown,down_arrow,
				right_arrow;
	Activity ack;
	String medId,serverUrl,constituentsUrl,getProdGenSubsUrl,pincode,pincodeUrl,pinurlpart1,
			pinurlpart2,unit;
	UsersDataBean ubean = new UsersDataBean();
	int userId,prodId,cityId;
	SpannableString med_info;
	TextView tv_med_info,item_name,brand_name,presc_req,tv_MrpValue,tv_youSaveValue
	,okler_Amount;
	NetworkImageView item_image;
	ImageLoader imgloader;
	ArrayList<ConstituentsDataBean> constiList = new ArrayList<ConstituentsDataBean>();
	LinearLayout med_info_linLay,check_pin_LL;
	SlidingUpPanelLayout mLayout;
	 private static final String TAG = "AlloMedsActivity";
	ArrayList<ProductDataBean> subProdList;
	//pArrList2;
	AlloSubstituesAdapter subsAdap;
	ListView lv;
	TextView indi_title,details,pincodeTv,availableTv,shipstoTv,change,sliderHeader;
	RelativeLayout title_RL,indi_details_RL,available_LL,unitLayout,med_info_heading_layout,
	parentImageFav;
	EditText qty_val,pincodeEt;
	RelativeLayout minus_units_rl,plus_units_rl;
	Button check;
	RelativeLayout back_layout;
	ImageView image_favourite;
	CartDataBean mainbean;
	boolean addCart = false,isAddedToCart = false,buy = false;
	Button notifCount;
	ProductDataBean hsBean,prodBean;
	ArrayList<ProductDataBean> favs = new ArrayList<ProductDataBean>();
	boolean isFav = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allo_meds);
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		ack=this;
		setSupportActionBar(toolBar);
		medId = getIntent().getStringExtra("MedId");
		prodBean = new ProductDataBean();
		prodBean = UIUtils.getProdBean(medId);
		toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(
				Okler.getInstance().getBookingType()));
		Utilities.setTitleText(toolBar, "Allopathy");
		bottomBarLayout = findViewById(R.id.bottombar);
		subProdList = new ArrayList<ProductDataBean>();
		handleMapping(bottomBarLayout);
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		userId = ubean.getId();
		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgloader = VolleyRequest.getInstance(getApplicationContext())
				.getImageLoader();
		image_favourite = (ImageView) findViewById(R.id.image_favourite);
		parentImageFav = (RelativeLayout)findViewById(R.id.parentImageFav);
		serverUrl = getString(R.string.serverUrl);
		UIUtils.setBackClick(toolBar, ack);
		med_info = new SpannableString("Medicine Info");
		med_info.setSpan(new UnderlineSpan(), 0, med_info.length(), 0);
		tv_med_info = (TextView)findViewById(R.id.tv_med_info);
		tv_med_info.setText(med_info);
		item_name = (TextView)findViewById(R.id.item_name);
		brand_name = (TextView)findViewById(R.id.brand_name);
		presc_req = (TextView)findViewById(R.id.presc_req);
		tv_MrpValue = (TextView)findViewById(R.id.tv_MrpValue);
		tv_youSaveValue = (TextView)findViewById(R.id.tv_youSaveValue);
		okler_Amount = (TextView)findViewById(R.id.okler_Amount);
		item_image = (NetworkImageView)findViewById(R.id.item_image);
		med_info_linLay = (LinearLayout)findViewById(R.id.med_info_linLay);
		minus_units = (ImageView)findViewById(R.id.minus_units);
		plus_units = (ImageView)findViewById(R.id.plus_units);
		qty_val = (EditText)findViewById(R.id.qty_val);
		minus_units_rl = (RelativeLayout)findViewById(R.id.minus_units_rl);
		plus_units_rl = (RelativeLayout)findViewById(R.id.plus_units_rl);
		check = (Button) findViewById(R.id.button_check);
		pincodeEt = (EditText) findViewById(R.id.enter_pincode_Et);
		pincodeTv = (TextView) findViewById(R.id.pincodeTv);
		check_pin_LL = (LinearLayout) findViewById(R.id.check_pin_LL);
		edit_pincode = (ImageView) findViewById(R.id.edit_pincode);
		available_LL = (RelativeLayout) findViewById(R.id.available_LL);
		unitLayout = (RelativeLayout) findViewById(R.id.unitLayout);
		availableTv = (TextView) findViewById(R.id.availableTv);
		shipstoTv = (TextView) findViewById(R.id.shipstoTv);
		buyIV = (ImageView) findViewById(R.id.buyIV);
		change = (TextView) findViewById(R.id.changeTv);
		notifCount = (Button) toolBar.findViewById(R.id.notif_count);
		addToCart = (ImageView) findViewById(R.id.cartIV);
		med_info_heading_layout = (RelativeLayout)findViewById(R.id.med_info_heading_layout);
		down_arrow = (ImageView)findViewById(R.id.down_arrow);
		right_arrow = (ImageView)findViewById(R.id.right_arrow);
		
		try {
			JSONObject medobj = new JSONObject(medId);
			item_name.setText(medobj.optString("prodName"));
			String company = medobj.optString("company");
			if (company.equals("") || company.equals("null")
					|| company.equals(null) || company == null
					|| company.length() == 0) {
				brand_name.setVisibility(View.GONE);
			} else {
				brand_name.setText(company);
			}
			int presc_check = medobj.getInt("presc_needed");
			if (presc_check == 0) {
				presc_req.setVisibility(View.GONE);
			}
			float mrp = Float.parseFloat(medobj.optString("mrp"));
			tv_MrpValue.setText("" + mrp);
			tv_youSaveValue
			.setText((String.valueOf(medobj.getInt("discount")) + "%"));
			String part1 = okler_Amount.getText().toString();
			float ok_price = Float.parseFloat(medobj.optString("oklerPrice"));
			String full = part1 + ok_price;
			okler_Amount.setText(full);
			String second = medobj.optString("imgUrl");
			String First = medobj.optString("clipArtUrl");
			String photo_url = First + second;
			item_image.setImageUrl(photo_url, imgloader);
			prodId = medobj.optInt("prodId");
			parentImageFav.setTag(prodId);
			constituentsUrl = serverUrl+getString(R.string.getProdsConstituents)+prodId;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		lv = (ListView) findViewById(R.id.list);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductDataBean pbean = new ProductDataBean();
                pbean = subProdList.get(position);
                String prod_id = String.valueOf(pbean.getProdId());
               // String name = pbean.getProdName();
                //Toast.makeText(ack, "id "+prod_id+" name "+name, 2000).show();
                getProductInfoById(prod_id);
            }
        });
		
		String cityid = ubean.getUserCityId();
		if(cityid==null||cityid.equals(""))
			cityid="-1";
        cityId=Integer.parseInt(cityid);
		
        getProdGenSubsUrl = serverUrl+getString(R.string.getProdGenericSubsPart1)+prodId
        		+getString(R.string.getProdGenericSubsPart2)+cityId;
        
        getProdSubstitutes(getProdGenSubsUrl);
        setUnits();
        checkAvailability();
        addToCart();
        getConstituents(constituentsUrl);
		share(prodId);
		buyNow();

		med_info_heading_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//int a = med_info_linLay.getVisibility();
				if(med_info_linLay.getVisibility()==8){
					med_info_linLay.setVisibility(View.VISIBLE);
					down_arrow.setVisibility(View.VISIBLE);
					right_arrow.setVisibility(View.GONE);
				}else{
					med_info_linLay.setVisibility(View.GONE);
					down_arrow.setVisibility(View.GONE);
					right_arrow.setVisibility(View.VISIBLE);
				}
			}
		});
        
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        sliderIndicatorRight = (ImageView) findViewById(R.id.sliderIndicatorRight);
        sliderIndicatorDown = (ImageView)findViewById(R.id.sliderIndicatorDown);
        mLayout.setAnchorPoint((float) 0.5);
        mLayout.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                if(newState == PanelState.COLLAPSED){
                	sliderIndicatorRight.setVisibility(View.VISIBLE);
                	sliderIndicatorDown.setVisibility(View.GONE);
                }else{
                	sliderIndicatorRight.setVisibility(View.GONE);
                	sliderIndicatorDown.setVisibility(View.VISIBLE);
                }
            }
        });
        mLayout.setFadeOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(PanelState.COLLAPSED);
            }
        });
		
        sliderHeader = (TextView) findViewById(R.id.sliderHeader);

        parentImageFav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				UIUtils.addRemoveFavourites(v, ack, prodBean);
			}
		});
       }

	public void getConstituents(String url){
		
		WebJsonObjectRequest constijson = new WebJsonObjectRequest(Method.GET,
				url, new JSONObject(), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				JSONObject resultObj = new JSONObject();
				resultObj = response.optJSONObject("result");
				JSONArray constiArray = new JSONArray();
				constiArray = resultObj.optJSONArray("constituents");
				constiList = new ArrayList<ConstituentsDataBean>();
				ConstituentsDataBean cntbean;
				JSONObject constiObj;
				for (int i = 0; i < constiArray.length(); i++) {
					cntbean = new ConstituentsDataBean();
					constiObj = new JSONObject();
					constiObj = constiArray.optJSONObject(i);
					cntbean.setConstituent_name(constiObj.optString("constituent_name"));
					cntbean.setIndication(constiObj.optString("indication"));
					cntbean.setContra_indication(constiObj.optString("contra_indication"));
					cntbean.setCaution(constiObj.optString("caution"));
					cntbean.setSide_effect(constiObj.optString("side_effect"));
					constiList.add(cntbean);
				}
				constiList.trimToSize();
				setConstituentsUI();
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("ERROR", String.valueOf(error.getStackTrace()));
			}
		});
		VolleyRequest.addJsonObjectRequest(ack, constijson);
	}
	
	public void setConstituentsUI(){
		CustomViewProdInfo customConstiView[] = new 
				CustomViewProdInfo[constiList.size()];
		ConstituentsDataBean cntbean;
		for (int i = 0; i < constiList.size(); i++) {
			customConstiView[i] = new CustomViewProdInfo(ack);
			
			cntbean = new ConstituentsDataBean();
			cntbean = constiList.get(i);
			indi_title = (TextView)customConstiView[i].
					findViewById(R.id.indi_title);
			
			add_Button_indi = (ImageView)customConstiView[i].
					findViewById(R.id.add_Button_indi);
			int t = i+100;
			add_Button_indi.setTag(""+t);
			minusButton_indi = (ImageView)customConstiView[i].
					findViewById(R.id.minusButton_indi);
			int t2 = i+500;
			minusButton_indi.setTag(""+t2);
			details = (TextView)customConstiView[i].
					findViewById(R.id.details);
			int t3 = i+1000;
			details.setTag(""+t3);
			title_RL = (RelativeLayout)customConstiView[i].
					findViewById(R.id.title_RL);
			title_RL.setTag(""+i);
			indi_details_RL = (RelativeLayout)customConstiView[i].
					findViewById(R.id.indi_details_RL);
			int t4 = i+1500;
			indi_details_RL.setTag(""+t4);
			indi_title.setText(cntbean.getConstituent_name());
			details.setText("\n"+"Indication : "+cntbean.getIndication()+"\n\n"+ "Contra Indication : " +cntbean.getContra_indication()+"\n\n"+
							"Caution : "+cntbean.getCaution()+"\n\n"+ "Side Effect :"+
							cntbean.getSide_effect()+"\n");
			
			med_info_linLay.addView(customConstiView[i]);
			
			title_RL.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int id = Integer.parseInt(String.valueOf(v.getTag()));
					View v2 = (CustomViewProdInfo) med_info_linLay.getChildAt(id);
					ImageView add_btn = (ImageView)v2.findViewById(R.id.add_Button_indi);
					ImageView minus_btn = (ImageView)v2.findViewById(R.id.minusButton_indi);
					RelativeLayout details = (RelativeLayout)v2.findViewById(R.id.indi_details_RL);
					if(add_btn.getVisibility()==0){
						add_btn.setVisibility(View.GONE);
						minus_btn.setVisibility(View.VISIBLE);
						details.setVisibility(View.VISIBLE);
						int len = med_info_linLay.getChildCount();
						for (int j = 0; j < len; j++) {
							if(j!=id){
							View v3 = (CustomViewProdInfo)med_info_linLay.getChildAt(j);
							RelativeLayout details2 = (RelativeLayout)v3.findViewById(R.id.indi_details_RL);
							ImageView add_btn2 = (ImageView)v3.findViewById(R.id.add_Button_indi);
							ImageView minus_btn2 = (ImageView)v3.findViewById(R.id.minusButton_indi);
							add_btn2.setVisibility(View.VISIBLE);
							minus_btn2.setVisibility(View.GONE);
							details2.setVisibility(View.GONE);
							}
						}
					}else{
						add_btn.setVisibility(View.VISIBLE);
						minus_btn.setVisibility(View.GONE);
						details.setVisibility(View.GONE);
					}
				}
			});
		}
	}
	
	public void getProdSubstitutes(String url){
	
		WebJsonObjectRequest subsjson = new WebJsonObjectRequest(Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try{
				JSONArray resultArray = response.optJSONArray("result");
				JSONObject jobj1;
				subProdList = new ArrayList<ProductDataBean>();
				ProductDataBean pbean;
				if(resultArray==null)
					resultArray= new JSONArray();
				for (int i = 0; i < resultArray.length(); i++) {
					pbean = new ProductDataBean();
					jobj1= new JSONObject();
					jobj1 = resultArray.optJSONObject(i);
					pbean.setProdId(Integer.parseInt(jobj1.optString("id")));
					pbean.setProdName(jobj1.optString("name"));
					pbean.setMrp(Float.parseFloat(jobj1.optString("price")));
					pbean.setOklerPrice(Float.parseFloat(jobj1.optString("saleprice")));
					pbean.setDiscount(Float.parseFloat(jobj1.optString("discount")));
					pbean.setWeight(jobj1.optString("weight"));
					pbean.setPresc_needed(Integer.parseInt(jobj1.optString("prescription_need")));
					pbean.setProdType(Integer.parseInt(jobj1.optString("pro_type")));				
					
				subProdList.add(pbean);
				}
				subProdList.trimToSize();
				int subSize = subProdList.size();
				if(subSize>0)
				sliderHeader.setText(subSize+" "+"Generic Substitue (s) Available");
				else
				sliderHeader.setText("No Generic Substitute (s) Available");	
				subsAdap = new AlloSubstituesAdapter(ack, subProdList);
		        lv.setAdapter(subsAdap);
				}catch(Exception e){
					Log.e("ERROR", String.valueOf(e.getStackTrace()));
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("ERROR", String.valueOf(error.getStackTrace()));
			}
		});
		VolleyRequest.addJsonObjectRequest(ack, subsjson);
	}
	
	public void setUnits(){
		plus_units_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(qty_val.getText().toString().equals("")){
					qty_val.setText("00");
				}
				int units=0;
				units = Integer.parseInt(qty_val.getText().toString());
				if(units>=99){
					Toast.makeText(ack, "Maximum Limit is 99", 2000).show();
				}else{
				units++;
				}
				if(units<=9){
					qty_val.setText("0"+units);
				}else{
					qty_val.setText(""+units);
				}
			}
		});
		
		minus_units_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(qty_val.getText().toString().equals("")){
					qty_val.setText("00");
				}
				int units = 0;
				units = Integer.parseInt(qty_val.getText().toString());
				if(units<=0){
				}else{
					units--;
				}
				if(units<=9){
					qty_val.setText("0"+units);
				}else{
					qty_val.setText(""+units);
				}
			}
		});
	}
	
	public void checkAvailability(){
		
		pinurlpart1 = getString(R.string.getPinUrlPart1);
		pinurlpart2 = getString(R.string.getPinUrlPart2);
		check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (pincodeEt.getText().toString().trim().length() > 6
						|| pincodeEt.getText().toString().trim().length() < 6) {
					Toast.makeText(getApplicationContext(),
							"Please Enter Correct Pincode", Toast.LENGTH_SHORT)
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
									// resp, Toast.LENGTH_SHORT).show();
									JSONObject pinobj = (JSONObject) response;
									try {
										resp = pinobj.getString("message");
										if (resp.equals("Pincode Not Available")) {
											pincodeTv.setText(pincode);
											if(check_pin_LL.getVisibility()!=8)
												check_pin_LL.setVisibility(View.GONE);
											if(available_LL.getVisibility()!=0){
												available_LL.setVisibility(View.VISIBLE);
												availableTv.setText("Not Available");
											}
											if(shipstoTv.getVisibility()!=8)
												shipstoTv.setVisibility(View.GONE);
											buyIV.setEnabled(false);
										} else {
											pincodeTv.setText(pincode);
											if(check_pin_LL.getVisibility()!=8)
											check_pin_LL.setVisibility(View.GONE);
											if(available_LL.getVisibility()!=0){
											available_LL.setVisibility(View.VISIBLE);
											availableTv.setText("Available");
											}
											if(shipstoTv.getVisibility()!=0)
											shipstoTv.setVisibility(View.VISIBLE);
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
				
				if(check_pin_LL.getVisibility()!=0)
					check_pin_LL.setVisibility(View.VISIBLE);
				
				if(available_LL.getVisibility()!=8)
					available_LL.setVisibility(View.GONE);
				}
		});
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
	
	public void addToCart() {
		addToCart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mainbean = Okler.getInstance().getMainCart();
				isAddedToCart = false;
				for (int i = 0; i < mainbean.getProdList().size(); i++) {
					int id = mainbean.getProdList().get(i).getProdId();
					if (id == prodId) {
						Toast.makeText(ack,
								"This Product is already in your Cart.",
								Toast.LENGTH_SHORT).show();
						isAddedToCart = true;
						break;
					}
				}
				if (!isAddedToCart) {
					if (availableTv.getText().toString().equals("Available")
							&& available_LL.getVisibility() == View.VISIBLE) {
						unit = qty_val.getText().toString();
						if (unit.equals(""))
							unit = "00";
						if (unit.length() > 2) {
							qty_val.requestFocus();
							qty_val.setText("");
							Toast.makeText(ack, "Maximum limit is 99",
									Toast.LENGTH_SHORT).show();
						} else {
							int units = Integer.parseInt(unit);
							if (units <= 0) {
								Toast.makeText(ack,
										"Please Select atleast one unit",
										Toast.LENGTH_SHORT).show();
								qty_val.requestFocus();
							} else {
								if (Utilities
										.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN
										|| (Utilities
												.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_FB)
										|| (Utilities
												.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
									addCartWebService();
								} else {
									Intent intent = new Intent(ack,
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
						Toast.makeText(ack,
								"Product not available at your PinCode.",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	public void addCartWebService() {
		addCart = false;
		UsersDataBean ubean = new UsersDataBean();
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		mainbean.setUbean(ubean);
				prodBean.setUnits(
								Integer.parseInt(qty_val.getText().toString()));
				mainbean.getProdList().add(prodBean);

		String addToCartUrl, add1, add2, add3;
		add1 = "http://183.82.110.105:8081/oklerapi/products/usercart/savecart?product=";
		add2 = "&cust_id=";
		add3 = "&quantity=";
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
								UIUtils.setCartCount(notifCount, ack);
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
		
					}
				});
		VolleyRequest.addJsonObjectRequest(getApplicationContext(), addjson);
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
	
	public void buyNow() {

		buyIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (availableTv.getText().toString().equals("Available")
						&& available_LL.getVisibility() == View.VISIBLE) {
					String unit = qty_val.getText().toString();
					if (unit.equals(""))
						unit = "00";
					if (unit.length() > 2) {
						qty_val.setText("");
						qty_val.requestFocus();
						Toast.makeText(ack, "Maximum limit is 99",
								Toast.LENGTH_SHORT).show();
					} else {
						int units = Integer.parseInt(unit);
						if (units <= 0) {
							Toast.makeText(ack,
									"Please Select atleast one unit",
									Toast.LENGTH_SHORT).show();
							qty_val.requestFocus();
						}
						else {
							if (Utilities.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN
									|| (Utilities
											.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_FB)
									|| (Utilities
											.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
								buy();
							} else {
								Intent intent = new Intent(ack,
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
					Toast.makeText(ack,
							"Product not available at your PinCode.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public void buy() {
		CartDataBean odBean = new CartDataBean();
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
		odBean.setCurUBean(ubean);
		prodBean.setUnits(
					Integer.parseInt(qty_val.getText().toString()));		
		odBean.getProdList().add(prodBean);
				Okler.getInstance().setSingleCart(odBean);
		Intent intent = new Intent(getApplicationContext(),
				ProductCheckoutListView.class);
		intent.putExtra("Check", 1);
		startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		UIUtils.setCartCount(notifCount, ack);
		if (Utilities.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN
				|| (Utilities.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_FB)
				|| (Utilities.getUserStatusFromSharedPref(ack) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
			if (addCart) {
				addCart = false;
				addCartWebService();
			}
			if (buy) {
				buy = false;
				buy();
			}
			favs = Utilities.getFavourites(ack);
			 //favs = Okler.getInstance().getFavourites();
			 favs.trimToSize();
				if(favs.size() != 0)
				{
					for(int i = 0; i<favs.size(); i++)
					{
						ProductDataBean pbean = favs.get(i);
						int prodID = pbean.getProdId();
						if(prodID == prodId)
						{
							image_favourite
							.setBackgroundResource(R.drawable.fav_filled_heart_icon);
							isFav = true;
						}
					}
				}
				else
				{
					image_favourite
					.setBackgroundResource(R.drawable.favourites);
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
		getProdUrl = part1 + pid + part2 + "0" + part3;

	WebJsonObjectRequest prodjson = new WebJsonObjectRequest(Method.GET,
				getProdUrl, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						JSONObject jobj = new JSONObject();
						JSONObject jobj2 = new JSONObject();
						JSONObject jobj3 = new JSONObject();
						JSONArray jArray = new JSONArray();

						try {
							jobj = response.optJSONObject("result");
							int totalCount = jobj.optInt("total_count");
							if(totalCount<=0)
							{
								Toast.makeText(
										ack, "Product Details are not available",
										2500).show();
							}
							else
							{
							jobj2 = jobj.optJSONObject("0");

							hsBean = new ProductDataBean();
							hsBean.setProdName(jobj2.optString("name"));
							if (jobj2.has("type_of_packing")) {
							}
							hsBean.setDesc(jobj2.optString("description"));
							hsBean.setProdType(0);
							if (jobj2.has("type_of_packing")) {
								hsBean.setPacking_size(jobj2.optString("type_of_packing"));
							}
							
							hsBean.setMrp(Float.parseFloat(jobj2.optString("price")));
							hsBean.setOklerPrice(Float.parseFloat(jobj2
									.optString("saleprice")));
							hsBean.setDiscount(Float.parseFloat(jobj2.optString("discount")));
							if (jobj2.has("side_effects")) {
								hsBean.setSide_effect(jobj2.optString("side_effects"));
							}
							if (jobj2.has("tax")) {
								hsBean.setTax(Float.parseFloat(jobj2.optString("tax")));
							}
							if (jobj2.has("Contraindications")) {
								hsBean.setContraIndi(jobj2.optString("Contraindications"));
							}
							hsBean.setProdId(Integer.valueOf(jobj2
									.optString("id")));
							if (jobj2.has("prescription_need")) {
								if (jobj2.optString("prescription_need").equals("null")
										|| jobj2.optString("prescription_need")
												.equals(null)
										|| jobj2.optString("prescription_need").equals("")) {
									hsBean.setPresc_needed(0);
								} else {
									hsBean.setPresc_needed(Integer.parseInt(jobj2
											.optString("prescription_need")));
								}
							}
							String jimg = jobj2.optString("images");
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
							String image_name = "";
							try {
								image_name = URLEncoder.encode(url2, "UTF-8");
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							hsBean.setImgUrl(image_name);
							JSONObject uobj = new JSONObject();
							uobj = response.getJSONObject("product_image_url");
							hsBean.setMediumUrl(uobj
									.optString("productimage_url_medium"));
							hsBean.setSmallUrl(uobj
									.optString("productimage_url_small"));
							hsBean.setThumbUrl(uobj
									.optString("productimage_url_thumbnail"));
							hsBean.setClipArtUrl(uobj
									.optString("productimage_clipArt_images"));
							if (jobj2.has("indications")) {
								hsBean.setIndications(jobj2.optString("indications"));
							}
							hsBean.setDosage(jobj2.optString("dosage"));
							hsBean.setCompany(jobj2.optString("company_name"));
							if (jobj2.has("generic_val")) {
								hsBean.setGeneric_name(jobj2.optString("generic_val"));
							}
							if (jobj2.has("constituents_details")) {
								jArray = jobj2.getJSONArray("constituents_details");
									jobj3 = jArray.optJSONObject(0);
									if(jobj3!=null){
									if (jobj3.has("contra_indication")) {
										hsBean.setContraIndi(jobj3
												.optString("contra_indication"));
									}
									if (jobj3.has("caution")) {
										hsBean.setCaution(jobj3.optString("caution"));
									}
									if (jobj3.has("side_effects")) {
										hsBean.setSide_effect(jobj3.optString("side_effects"));
									}
									if (jobj3.has("weight")) {
										hsBean.setWeight(jobj3.optString("weight"));
									}
									}
							}
							Gson gson = new Gson();
							String hsbean = gson.toJson(hsBean);
							Intent intent =  new Intent(ack,
											AlloMedsActivity.class);
							intent.putExtra("MedId", hsbean);
							startActivity(intent);
							finish();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
		VolleyRequest.addJsonObjectRequest(ack, prodjson);
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
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
}
