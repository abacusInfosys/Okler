package com.okler.adapters;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.okleruser.R;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HealthShopSingleItemAdapter extends BaseAdapter {

	Activity context;
    ArrayList<ProductDataBean> prods;
      private static LayoutInflater inflater=null;
      ImageLoader imgLoader;
    
      ArrayList<ProductDataBean> favs = new ArrayList<ProductDataBean>();
      int pid;
      int id;

      Button tbCount;
    public HealthShopSingleItemAdapter(Activity con, ArrayList<ProductDataBean> prodDataBean,Button tbCnt) {
		// TODO Auto-generated constructor stub
    	context = con;
		prods = prodDataBean;
		prods.trimToSize();
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
        ImageView image_favourite;
        RelativeLayout parentImageFav;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;

		rowView = inflater.inflate(R.layout.row_healthshop_single, null);
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
		holder.parentImageFav = (RelativeLayout)rowView
				.findViewById(R.id.parentImageFav);
		favs = Utilities.getFavourites(context);
		//favs = Okler.getInstance().getFavourites();
		favs.trimToSize();
		if(favs.size() != 0)
		{
			for(int i = 0; i<favs.size(); i++)
			{
				ProductDataBean pbean = favs.get(i);
				int prodID = pbean.getProdId();
				if(prodID == prods.get(position).getProdId())
				{
					holder.image_favourite.setBackgroundResource(R.drawable.fav_filled_heart_icon);
				}
			}
		}

		holder.image_favourite.setTag(prods.get(position).getProdId());
		holder.parentImageFav.setTag(prods.get(position).getProdId());
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
    holder.textMrpValue.setText((""+prods.get(position).getMrp()));
    holder.okler_price.setText("  "+"OKLER PRICE RS:"+ prods.get(position).getOklerPrice());
    holder.item_description.setText(prods.get(position).getDesc());
    holder.youSaveValue.setText(prods.get(position).getDiscount() + "%");

    imgLoader = VolleyRequest.getInstance(context).getImageLoader();
    String First = prods.get(position).getMediumUrl();
    String Second = prods.get(position).getImgUrl();
	String photo_url=First+Second;
	holder.img.setImageUrl(photo_url, imgLoader);
	
	holder.parentImageFav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ProductDataBean prod = new ProductDataBean();
				int viewid = (int) v.getTag();
				for(int i=0;i<prods.size();i++){
					if(viewid==prods.get(i).getProdId()){
						prod = prods.get(i);
						break;
					}
				}
				UIUtils.addRemoveFavourites(v, context, prod);
			}
		});
		
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
		add1 = context.getString(R.string.save_cart_url);
		add2 = context.getString(R.string.cust_id);
		add3 = context.getString(R.string.quantity);
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
		   pdList.trimToSize();
		   int maincount = pdList.size(); 
		   
		   if(maincount>9){
			   tbCount.setText(""+maincount);
		    }
		    else{
		    	tbCount.setText("0"+maincount);
		    }
	}
}
