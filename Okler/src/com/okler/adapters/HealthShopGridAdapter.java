package com.okler.adapters;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.okler.android.BaseActivity;
import com.okler.android.FavouritesActivity;
import com.okler.android.NewSignIn;
import com.okler.android.ProductDetailsActivity;
import com.okleruser.R;

import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HealthShopGridAdapter extends BaseAdapter {

	Context context;
	ArrayList<ProductDataBean> prods;
	Activity c;
	private static LayoutInflater inflater = null;

	ImageLoader imgLoader;
	Button cartBtn;
	ArrayList<ProductDataBean> array = new ArrayList<ProductDataBean>();
	ProductDataBean pbean = new ProductDataBean();
	ArrayList<ProductDataBean> array1 = new ArrayList<ProductDataBean>();
	
	int pid = 0;
	int userid;


	public HealthShopGridAdapter(Activity con,
			ArrayList<ProductDataBean> prodDataBean, Button cart) {
		// TODO Auto-generated constructor stub

		// context = con;
		c = con;
		context = c.getApplicationContext();
		prods = prodDataBean;
		inflater = LayoutInflater.from(context);
		cartBtn = cart;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return prods.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class Holder {
		TextView tv, textMrpValue, item_description, youSaveValue;
		Button okler_price, image_cart;
		NetworkImageView img;
		ImageView image_favourite, image_favourite_filled;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Holder holder = new Holder();
		View rowView;

		rowView = inflater.inflate(R.layout.row_healthshop_grid, null);
		holder.tv = (TextView) rowView.findViewById(R.id.item_name);
		holder.img = (NetworkImageView) rowView.findViewById(R.id.item_image);
		holder.textMrpValue = (TextView) rowView.findViewById(R.id.tv_MrpValue);
		holder.okler_price = (Button) rowView.findViewById(R.id.okler_Amount);
		holder.item_description = (TextView) rowView
				.findViewById(R.id.item_description);
		holder.youSaveValue = (TextView) rowView
				.findViewById(R.id.tv_youSaveValue);
		holder.image_favourite = (ImageView) rowView
				.findViewById(R.id.image_favourite);
		holder.image_favourite_filled = (ImageView) rowView
				.findViewById(R.id.image_favourite_filled);
		//holder.image_favourite.setVisibility(View.VISIBLE);

		array1 = Okler.getInstance().getFavourites();
		
		if(array1.size() != 0)
		{
			for(int i = 0; i<array1.size(); i++)
			{
				ProductDataBean pbean = array1.get(i);
				int prodID = pbean.getProdId();
				if(prodID == prods.get(position).getProdId())
				{
					holder.image_favourite_filled.setVisibility(View.VISIBLE);
				}
			}
		}
		
		
		
		//holder.image_favourite.setVisibility(View.INVISIBLE);
		holder.image_favourite.setTag(prods.get(position).getProdId());
		holder.image_favourite_filled.setTag(prods.get(position).getProdId());
		
		
		holder.image_cart = (Button) rowView.findViewById(R.id.image_cart);
		holder.image_cart.setTag(prods.get(position).getProdId());
		holder.image_cart.setVisibility(View.GONE);
		holder.image_cart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addToCart(holder.image_cart);
			}
		});

		holder.tv.setText(prods.get(position).getProdName());
		holder.textMrpValue.setText(("" + prods.get(position).getMrp()));
		holder.okler_price.setText("  " + "OKLER PRICE RS:"
				+ prods.get(position).getOklerPrice());
		holder.item_description.setText(prods.get(position).getDesc());
		holder.youSaveValue.setText(prods.get(position).getDiscount() + "%");

		imgLoader = VolleyRequest.getInstance(context).getImageLoader();

		// String First=
		// "http://183.82.110.105:8081/oklerdevv2/uploads/images/medium/";
		String First = prods.get(position).getThumbUrl();
		String Second = prods.get(position).getImgUrl();
		String photo_url = First + Second;

		holder.img.setImageUrl(photo_url, imgLoader);
		final int id;
		holder.image_favourite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				pid = 0;
				

				// ProductDataBean pbean = Okler.getInstance().getProdList();

				UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(c);
				userid = ubean.getId();

				int viewid = (int) v.getTag();
				pid = viewid;
					

				

				if (Utilities.getUserStatusFromSharedPref(c) == UserStatusEnum.LOGGED_IN
						|| (Utilities.getUserStatusFromSharedPref(c) == UserStatusEnum.LOGGED_IN_FB)
						|| (Utilities.getUserStatusFromSharedPref(c) == UserStatusEnum.LOGGED_IN_GOOGLE)) {

					String add_fav = context.getString(R.string.add_fav_url)
							+ context.getString(R.string.cust_id) + userid + context.getString(R.string.getMedsUrlProdId3) + pid;

					WebJsonObjectRequest webjson = new WebJsonObjectRequest(
							Method.GET, add_fav, new JSONObject(),
							new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									// TODO Auto-generated method stub

									try {
										JSONObject responseObj = (JSONObject) response;
										String message = responseObj
												.getString("message");
										Toast.makeText(context, message,
												Toast.LENGTH_LONG).show();
										if (message
												.equals("Product Successfully Added To Your Favorites")) {
											
											array1.add(prods.get(position));
											Okler.getInstance().setFavourites(array1);
											
											holder.image_favourite.setVisibility(View.GONE);
											holder.image_favourite.setEnabled(false);
											holder.image_favourite_filled.setVisibility(View.VISIBLE);
											holder.image_favourite_filled.setEnabled(true);
										}

									} catch (JSONException jsonEx) {
										Log.e("Exception json", jsonEx
												.getStackTrace().toString());
									}

								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									// TODO Auto-generated method stub

									Toast.makeText(
											c,
											"Failed to add product to favorites",
											Toast.LENGTH_LONG).show();

								}
							});

					VolleyRequest.addJsonObjectRequest(context, webjson);
				}

				else {

					Intent in = new Intent(c, NewSignIn.class);
					c.startActivity(in);
				}

			}
		});
		
		holder.image_favourite_filled.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

				// TODO Auto-generated method stub

				pid = 0;
				

				// ProductDataBean pbean = Okler.getInstance().getProdList();

				UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(c);
				userid = ubean.getId();

				int viewid = (int) v.getTag();
				/*for (int i = 0; i < prods.size(); i++) {
					if (prods.get(i).getProdId() == viewid) {

						array.add(prods.get(i));
						Okler.getInstance().setFavourites(array);*/
						pid = viewid;
					/*}
				}
*/
				

				if (Utilities.getUserStatusFromSharedPref(c) == UserStatusEnum.LOGGED_IN
						|| (Utilities.getUserStatusFromSharedPref(c) == UserStatusEnum.LOGGED_IN_FB)
						|| (Utilities.getUserStatusFromSharedPref(c) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
					
					
					AlertDialog alertDialog = new AlertDialog.Builder(c).create();
					alertDialog.setTitle("Alert");
					alertDialog.setMessage("Are you sure, you want to remove this product from your favourites?");
					alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
							final String delete_fav = context.getString(R.string.delete_fav_url) +userid+context.getString(R.string.getMedsUrlProdId3)+pid;
							//	Toast.makeText(getApplicationContext(), "id"+ viewid, Toast.LENGTH_LONG).show();
												         
							        	 WebJsonObjectRequest webjson=new WebJsonObjectRequest(Method.GET, delete_fav, new JSONObject(),new Listener<JSONObject>() 
													{
														@Override
														public void onResponse(JSONObject response) 
														{
															// TODO Auto-generated method stub
															
															try
															{
															JSONObject responseObj =(JSONObject)response;
															String result = responseObj.getString("result");
															String message = responseObj.getString("message"); 
												//			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
															
													//		Toast.makeText(getApplicationContext(), "result" + result, Toast.LENGTH_LONG).show();
															
															if(message != "deleted failed from favourites")
															{
																array1 = Okler.getInstance().getFavourites();
																for(int i = 0; i<array1.size(); i++)
																{
																	ProductDataBean pbean = array1.get(i);
																	int prodID = pbean.getProdId();
																	if(prodID == prods.get(position).getProdId())
																	{
																		array1.remove(i);
																		holder.image_favourite_filled.setVisibility(View.GONE);
																		holder.image_favourite_filled.setEnabled(false);
																		holder.image_favourite.setVisibility(View.VISIBLE);
																		holder.image_favourite.setEnabled(true);
																		Okler.getInstance().setFavourites(array1);
																		break;
																	}
																}
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
																// TODO Auto-generated method stub
													
															}
														}
											);
										
									VolleyRequest.addJsonObjectRequest(context,webjson);
							
						}
					});
					
					alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
							dialog.dismiss();
							
						}
					});
					
					alertDialog.show();
				         }     

				else {

					Intent in = new Intent(c, NewSignIn.class);
					c.startActivity(in);
				}				
			}
		});
		
		

		return rowView;

	}

	public void addToCart(final Button addToCart) {

		CartDataBean mainbean = Okler.getInstance().getMainCart();
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(c);
		int unit = 0;
		int prodId = Integer.parseInt(addToCart.getTag().toString());
		mainbean.setUbean(ubean);
		for (int i = 0; i < prods.size(); i++) {
			int id = prods.get(i).getProdId();
			if (id == prodId) {
				prods.get(i).setUnits(1);
				unit = 1;
				mainbean.getProdList().add(prods.get(i));
				break;
			}
		}
		Okler.getInstance().setMainCart(mainbean);
		String addToCartUrl, add1, add2, add3;
		add1 = context.getString(R.string.save_cart_url);
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
								Toast.makeText(context,
										"Added to cart Successfully",
										Toast.LENGTH_SHORT).show();
								addToCart.setEnabled(false);
							} else {
								Toast.makeText(context,
										"Some error ocured while adding.",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							Toast.makeText(context,
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
		VolleyRequest.addJsonObjectRequest(context, addjson);
		setCartQuantity();

	}

	public void setCartQuantity() {
		CartDataBean cbean = Okler.getInstance().getMainCart();
		ArrayList<ProductDataBean> pdList = cbean.getProdList();
		// pdList = odbean.getProdList();
		int maincount = pdList.size();

		if (maincount > 9) {
			cartBtn.setText("" + maincount);
		} else {
			cartBtn.setText("0" + maincount);
		}
	}
}
