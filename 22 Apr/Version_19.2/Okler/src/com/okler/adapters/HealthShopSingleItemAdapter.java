package com.okler.adapters;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.okler.android.BaseActivity;
import com.okler.android.FavouritesActivity;
import com.okler.android.HealthShopListView;
import com.okler.android.NewSignIn;
import com.okler.android.ProductDetailsActivity;
import com.okleruser.R;


import com.okler.android.HealthShopSingleItemDetail;
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
import android.content.Intent;
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

public class HealthShopSingleItemAdapter extends BaseAdapter {

	Activity context;
    ArrayList<ProductDataBean> prods;
      private static LayoutInflater inflater=null;
      Activity c;
      
      ImageLoader imgLoader;
      ArrayList<ProductDataBean> array = new ArrayList<ProductDataBean>();
Button tbCount;
    public HealthShopSingleItemAdapter(Activity con, ArrayList<ProductDataBean> prodDataBean,Button tbCnt) {
		// TODO Auto-generated constructor stub
    	context = con;
		prods = prodDataBean;	
    	inflater=LayoutInflater.from(context);
    	tbCount = tbCnt;
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

    public class Holder
    {
    	TextView tv, textMrpValue, item_description, youSaveValue;
        Button okler_price,image_cart;
        NetworkImageView img;
        ImageView image_favourite, image_favourite_filled;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.row_healthshop_single, null);
        holder.tv=(TextView) rowView.findViewById(R.id.item_name);
        holder.img=(NetworkImageView) rowView.findViewById(R.id.item_image);
        holder.textMrpValue = (TextView) rowView.findViewById(R.id.tv_MrpValue);
        holder.okler_price = (Button) rowView.findViewById(R.id.okler_Amount);
        holder.item_description = (TextView) rowView.findViewById(R.id.item_description);
        holder.youSaveValue = (TextView) rowView.findViewById(R.id.tv_youSaveValue);
        holder.image_favourite = (ImageView) rowView.findViewById(R.id.image_favourite);
        holder.image_favourite.setVisibility(View.INVISIBLE);
        holder.image_favourite_filled = (ImageView) rowView.findViewById(R.id.image_favourite_filled);
        holder.image_favourite_filled.setVisibility(View.INVISIBLE);
        holder.image_favourite.setTag(prods.get(position).getProdId());
        holder.image_cart = (Button)rowView.findViewById(R.id.image_cart);
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
    holder.textMrpValue.setText((""+prods.get(position).getMrp()));
    holder.okler_price.setText("  "+"OKLER PRICE RS:"+ prods.get(position).getOklerPrice());
    holder.item_description.setText(prods.get(position).getDesc());
    holder.youSaveValue.setText(prods.get(position).getDiscount() + "%");
    
    
    imgLoader = VolleyRequest.getInstance(context).getImageLoader();
    
    
	
	//String First= "http://183.82.110.105:8081/oklerdevv2/uploads/images/medium/";
    String First = prods.get(position).getMediumUrl();
    String Second = prods.get(position).getImgUrl();
	String photo_url=First+Second;
	
	holder.img.setImageUrl(photo_url, imgLoader);
	
	
	holder.image_favourite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int pid = 0;
				holder.image_favourite.setVisibility(View.GONE);
 				holder.image_favourite_filled.setVisibility(View.VISIBLE);
				
			//ProductDataBean pbean = Okler.getInstance().getProdList();
				
				UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(context);
				int id = ubean.getId();
				
				int viewid = (int) v.getTag();
				for(int i = 0; i<prods.size(); i++)
				{
					if(prods.get(i).getProdId() == viewid)
 					{
 						
 						array.add(prods.get(i));
 						Okler.getInstance().setFavourites(array);
 						pid = viewid;
 					}
				}
				
				//Utilities.writeFavouritesToSharedPref(context, Okler.getInstance().getFavourites());
				
				ArrayList<ProductDataBean> array1 = new ArrayList<ProductDataBean>();
				
				array1 = Okler.getInstance().getFavourites();
				for(int j = 0; j<array1.size();j++)
				{
					pid = array1.get(j).getProdId();
				/*String proName = array1.get(j).getProdName();
				Toast.makeText(context, proName, Toast.LENGTH_LONG).show();*/
				}
			//	Toast.makeText(context, "okler" + Okler.getInstance().getFavourites(), Toast.LENGTH_LONG).show();
				
				
				if (Utilities.getUserStatusFromSharedPref(context) == UserStatusEnum.LOGGED_IN ||
						(Utilities.getUserStatusFromSharedPref(context) == UserStatusEnum.LOGGED_IN_FB) ||
						(Utilities.getUserStatusFromSharedPref(context) == UserStatusEnum.LOGGED_IN_GOOGLE))
 				{
 					
 					String add_fav = "https://www.okler.com/api/products/products/addfav?"+"cust_id="+id+"&prod_id="+pid;
 	 		    	
 	 		    	
 	 		    	WebJsonObjectRequest webjson=new WebJsonObjectRequest(Method.GET, add_fav, new JSONObject(),new Listener<JSONObject>() 
 	 						{
 	 							@Override
 	 							public void onResponse(JSONObject response) 
 	 							{
 	 								// TODO Auto-generated method stub
 	 								
 	 								try
 	 								{
 	 								JSONObject responseObj =(JSONObject)response;
 	 								String message = responseObj.getString("message");
 	 								Toast.makeText(context, message, Toast.LENGTH_LONG).show();	
 	 								if(message.equals("Product Successfully Added To Your Favorites"))
 	 								{
 	 									Intent intent = new Intent(context, FavouritesActivity.class);
 	 									context.startActivity(intent);
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
 	 									// TODO Auto-generated method stub
 	 									
 	 									Toast.makeText(context, "Failed to add product to favorites", Toast.LENGTH_LONG).show();
 	 						
 	 								}
 	 							}
 	 				);
 	 					
 	 				VolleyRequest.addJsonObjectRequest(context,webjson);
 				}
				else
				{
					Okler.getInstance().setFavourites(array);
					Intent in = new Intent(context, NewSignIn.class);
					context.startActivity(in);
				}
			}
		});
	
	
	
	
	
	

  /*       rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	ArrayList<ProductDataBean> prodBean = new ArrayList<ProductDataBean>();
            	Intent intent = new Intent(context, ProductDetailsActivity.class);
            	prodBean = Okler.getInstance().getProdList();
            	//intent.putStringArrayListExtra("medId", prodBean);
            	context.startActivity(intent);
             //   Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });
*/
        return rowView;
    }
    
    public void addToCart(final Button addToCart){
		

		CartDataBean mainbean = Okler.getInstance().getMainCart();			
	    UsersDataBean	ubean = Utilities.getCurrentUserFromSharedPref(context);
		int unit = 0;
		int prodId = Integer.parseInt(addToCart.getTag().toString());
		mainbean.setUbean(ubean);
		for (int i = 0; i < prods.size(); i++) {
			int id = prods.get(i).getProdId();
			if(id == prodId){
				prods.get(i).setUnits(1);
				unit = 1;
				mainbean.getProdList().add(prods.get(i));
				break;
			}
		}
		Okler.getInstance().setMainCart(mainbean);
		String addToCartUrl,add1,add2,add3;
		add1 = "https://www.okler.com/api/products/usercart/savecart?product=";
		add2 = "&cust_id=";
		add3 = "&quantity=";
		int userId = ubean.getId();
		addToCartUrl = add1+prodId+add2+userId+add3+unit; 
		
		WebJsonObjectRequest addjson = new WebJsonObjectRequest(Method.GET, addToCartUrl, new JSONObject(), new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					JSONObject jboj = (JSONObject)response;
					try {
					String msg = jboj.getString("message");
					if(msg.equals("save the user cart items.")){
						Toast.makeText(context, "Added to cart Successfully", Toast.LENGTH_SHORT).show();	
						addToCart.setEnabled(false);
					}else{
						Toast.makeText(context, "Some error ocured while adding.", Toast.LENGTH_SHORT).show();
					}
					} catch (JSONException e) {
						Toast.makeText(context, "Some error ocured while adding.", Toast.LENGTH_SHORT).show();
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
	public void setCartQuantity(){
		CartDataBean cbean = Okler.getInstance().getMainCart();
		   ArrayList<ProductDataBean>pdList = cbean.getProdList();
		   // pdList = odbean.getProdList();
		   int maincount = pdList.size(); 
		   
		   if(maincount>9){
			   tbCount.setText(""+maincount);
		    }
		    else{
		    	tbCount.setText("0"+maincount);
		    }
	}
}
