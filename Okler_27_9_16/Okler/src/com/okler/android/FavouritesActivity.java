package com.okler.android;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;
import android.support.v7.widget.Toolbar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FavouritesActivity extends BaseActivity {
	
	Toolbar toolBar;
	View bottomBarLayout;
	ImageView imgBack;
	View view1;
	LinearLayout layout;
	
	int uid, pid;
	int viewid;
	TextView clearButton;
	
	ArrayList<ProductDataBean> myfav = new ArrayList<ProductDataBean>();
	ProductDataBean add = new ProductDataBean();
	Activity ack;
	int protype;
	String bookingtype,First;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favourites);
		ack = this;
		layout = (LinearLayout) findViewById(R.id.layout_address);
		clearButton = (Button) findViewById(R.id.clearButton);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		toolBar.setBackgroundColor(getResources().getColor(R.color.Blue800));
		UIUtils.setBackClick(toolBar, ack);
	      Utilities.setTitleText(toolBar, "Favourites");
	      UsersDataBean ubean =Utilities.getCurrentUserFromSharedPref(this);
	      uid = ubean.getId();
	}
	
	public void setFields()
	{
		layout.removeAllViews();
		for(int i = 0; i< myfav.size(); i++)
		{	
			add = myfav.get(i);
			LayoutInflater inflator = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
			view1 = inflator.inflate(R.layout.custom_favourites, null);
			view1.setTag(i);
			pid = add.getProdId();
			UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(FavouritesActivity.this);
		    uid = ubean.getId();

		    TextView text_name = (TextView) view1.findViewById(R.id.item_name);
			TextView item_description = (TextView) view1.findViewById(R.id.item_description);
			TextView tv_MrpValue = (TextView) view1.findViewById(R.id.tv_MrpValue);
			TextView tv_youSaveValue = (TextView) view1.findViewById(R.id.tv_youSaveValue);
			Button okler_Amount = (Button) view1.findViewById(R.id.okler_Amount);
			RelativeLayout deleteImageRl = (RelativeLayout)view1.findViewById(R.id.deleteImageRl);
			Button image_cart = (Button) view1.findViewById(R.id.image_cart);
			NetworkImageView item_image = (NetworkImageView) view1.findViewById(R.id.item_image);
			image_cart.setVisibility(View.GONE);
			item_description.setTag(i);
			ImageLoader imgLoader = VolleyRequest.getInstance(getApplicationContext()).getImageLoader();
	 		if(myfav.get(i).getProdType()==0)
	 			First=myfav.get(i).getClipArtUrl();
	 		else
	 		First= myfav.get(i).getThumbUrl();
	 		String Second = myfav.get(i).getImgUrl();
	 		String photo_url=First+Second;
	 		item_image.setImageUrl(photo_url, imgLoader); 
			deleteImageRl.setTag(i);
			text_name.setText(add.getProdName());
			item_description.setText(add.getDesc());
			tv_MrpValue.setText(""+add.getMrp());
			tv_youSaveValue.setText(""+add.getDiscount()+"%");
			okler_Amount.setText("Okler Price:"+ add.getOklerPrice());
			layout.addView(view1);			
			LinearLayout separator = new LinearLayout(getApplicationContext());				
			LinearLayout.LayoutParams params1 = new LinearLayout.
					LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
			params1.setMargins(0, 0, 0, 0);
			separator.setLayoutParams(params1);
			separator.requestLayout();
			separator.setBackgroundColor(getResources().getColor(R.color.Black));
			layout.addView(separator);
			deleteImageRl.setOnClickListener(new OnClickListener() 
			{	
				@Override
				public void onClick(View v) 
				{
					viewid = Integer.parseInt(v.getTag().toString());
					ProductDataBean pbean = new ProductDataBean();
					pbean = myfav.get(viewid);
					pid = pbean.getProdId();
					final AlertDialog alertDialog = new AlertDialog.Builder(FavouritesActivity.this)
					.create();
			alertDialog.setTitle("Alert");
			alertDialog
					.setMessage("Are you sure, you want to remove this product from your favourites?");
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							final String delete_fav = getString(R.string.delete_fav_url) +uid+getString(R.string.getMedsUrlProdId3)+pid;
												         
							        	 WebJsonObjectRequest webjson=new WebJsonObjectRequest(Method.GET, delete_fav, new JSONObject(),new Listener<JSONObject>() 
													{
														@Override
														public void onResponse(JSONObject response) 
														{
															try
															{
															JSONObject responseObj =(JSONObject)response;
															String message = responseObj.getString("message"); 
															if(message != "deleted failed from favourites")
															{
																layout.removeAllViews();
																webCallFavourite();
															}
															}catch(JSONException jsonEx)
															{
																Log.e("Exception json", jsonEx.getStackTrace().toString());
															}
														}}, 
														new Response.ErrorListener() 
														{

												@Override
												public void onErrorResponse(VolleyError error) 
												{
													Log.i("error", String.valueOf(error));
												}
											}
								);
						VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
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
			});
			view1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				redirect(v);
				}
			});
			item_description.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					redirect(v);
				}
			});
		}
		
		clearButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final AlertDialog alertDialog = new AlertDialog.Builder(FavouritesActivity.this)
				.create();
		alertDialog.setTitle("Alert");
		alertDialog
				.setMessage("Are you sure, you want to remove all the products from favourites?");
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						String clear_fav = getString(R.string.clear_all_fav_url)+uid;
						
						WebJsonObjectRequest webjson=new WebJsonObjectRequest(Method.GET, clear_fav, new JSONObject(),new Listener<JSONObject>() 
								{
									@Override
									public void onResponse(JSONObject response) 
									{
										try
										{
										JSONObject responseObj =(JSONObject)response;
										String message = responseObj.getString("message");
										if(message.equals("All deleted success from favourites"))
										{
											layout.removeAllViews();
											ArrayList<ProductDataBean> pbean = new ArrayList<ProductDataBean>();
											Utilities.writeFavouritesToSharedPref(ack, pbean);
											clearButton.setEnabled(false);
										}
										}catch(JSONException jsonEx)
										{
											Log.e("Exception json", jsonEx.getStackTrace().toString());
										}
									}}, 
									new Response.ErrorListener() 
									{
								@Override
								public void onErrorResponse(VolleyError error) 
								{
								}
							}
				);
				VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
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
		});
	}

	public void redirect(View v){
		Intent in = null;
		viewid = Integer.parseInt(v.getTag().toString());
		ProductDataBean pbean = new ProductDataBean();
		pbean = myfav.get(viewid);
		Gson gson=new Gson();
		String hsbean=gson.toJson(pbean);
		Okler.getInstance().setBookingType(pbean.getProdType());
		Utilities.writeFavouritesToSharedPref(ack, myfav);
		//Okler.getInstance().setFavourites(myfav);
		if(pbean.getProdType()==0)
		{
			in = new Intent(FavouritesActivity.this,AlloMedsActivity.class);
		}
		else
		{
			in = new Intent(FavouritesActivity.this,ProductDetailsActivity.class);
		}
		in.putExtra("MedId", hsbean);
		startActivity(in);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void webCallFavourite()
	{
		myfav.clear();
		if (Utilities.getUserStatusFromSharedPref(FavouritesActivity.this) == UserStatusEnum.LOGGED_IN ||
				(Utilities.getUserStatusFromSharedPref(FavouritesActivity.this) == UserStatusEnum.LOGGED_IN_FB) ||
				(Utilities.getUserStatusFromSharedPref(FavouritesActivity.this) == UserStatusEnum.LOGGED_IN_GOOGLE))
		{
    	  String get_fav = getString(R.string.get_fav_url)+uid;
 	     
 	     
 	     WebJsonObjectRequest webjson=new WebJsonObjectRequest(Method.GET, get_fav, new JSONObject(),new Listener<JSONObject>() 
 					{
 						@Override
 						public void onResponse(JSONObject response) 
 						{
 							ProductDataBean pbean;
 							try
 							{
 							JSONObject responseObj =(JSONObject)response;
 							if(responseObj.getString("result") == "")
 							{
 								finish();
 							}
 							else
 							{
 								JSONArray doctorsArr = responseObj.optJSONArray("result");
 								JSONObject imageUrlObj = responseObj.optJSONObject("product_image_url");
 								if(doctorsArr == null)
 								{
 									myfav.clear();
 									Toast toast =Toast.makeText(FavouritesActivity.this, "No Favourites items to show", Toast.LENGTH_LONG);

 									toast.show();
 									clearButton.setEnabled(false);
 								}
 								else
 								{
 									myfav = new ArrayList<ProductDataBean>();
 	 								for(int i = 0 ; i < doctorsArr.length();i++)
 	 								{
 	 									pbean = new ProductDataBean();
 	 									try
 	 									{
 	 										JSONObject docObj =(JSONObject) doctorsArr.get(i);
 	 										pbean.setProdName(docObj.optString("name"));
 	 										pbean.setProdId(docObj.optInt("id"));
 	 										pbean.setDesc(docObj.optString("description"));
 	 										pbean.setMrp(docObj.optInt("price"));
 	 										pbean.setOklerPrice(docObj.optInt("saleprice"));
 	 										Float discount = Float.parseFloat(docObj.optString("discount"));
 	 										pbean.setDiscount(discount);
 	 										pbean.setProdType(docObj.optInt("pro_type"));
 	 										String jimg = docObj.optString("images");
 	 										String url2;
 	 										if(jimg.equals(null)||jimg.equals("null")||jimg.equals("")){
 	 											url2 = "drawable://" + R.drawable.tempcuff;
 	 										}else{
 	 											String j1[] =jimg.split(",");
 	 											String j2=j1[0];
 	 											String colon = ":";
 	 											String j3[]=j2.split(colon);
 	 											String url = j3[2];
 	 										int length = url.length();
 	 										url2 = url.substring(1, (length-1));
 	 										}
 	 									pbean.setImgUrl(url2);
 	 									pbean.setSmallUrl(imageUrlObj.optString("productimage_url_small"));
 	 									pbean.setMediumUrl(imageUrlObj.optString("productimage_url_medium"));
 	 									pbean.setThumbUrl(imageUrlObj.optString("productimage_url_thumbnail"));
 	 									pbean.setClipArtUrl(imageUrlObj.optString("productimage_clipArt_images"));
 	 									Log.i("tag", "json object" + docObj);
 	 									}catch (JSONException e) {
 	 										Log.e("JSON Exception", e.getMessage());
 	 									} 
 	 								
 	 								myfav.add(pbean);
 	 								
 	 							 }
 								}
 								myfav.trimToSize();
 								Utilities.writeFavouritesToSharedPref(ack, myfav);
 								//Okler.getInstance().setFavourites(myfav);
 							}
 								
 								setFields();
 							}catch(JSONException jsonEx)
 							{
 								Log.e("Exception json", jsonEx.getStackTrace().toString());
 							}
 					
 						}}, 
 						new Response.ErrorListener() 
 						{

 							@Override
 							public void onErrorResponse(VolleyError error) 
 							{
 							}
 						}
 			);
 		
 	VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
		}
      {
    	  ProductDataBean pbean = new ProductDataBean();
    	  Log.i("product", ""+pbean);
      }	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		webCallFavourite();
	}
}