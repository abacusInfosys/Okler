package com.okler.android;

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

public class MyCart extends BaseActivity implements Response.Listener,Response.ErrorListener{
	private View bottomBarLayout;
	private Toolbar toolBar;
	private Context context;
	ImageView imgBack;
	TextView mtitle_mycart,htitle_mycart;
	LinearLayout med_LL,hs_LL;
	String getMyCartUrl;
	CartDataBean mainCart;
	ArrayList<ProductDataBean> prodList;
	ProductDataBean pdbean;
	CheckoutListViewAdapter clistViewAdapter;
	LinearLayout medList,hsList;
	LinearLayout progressLinLayout;
	Button toolBarCount;
	ScrollView mainScroll;
	CustomViewProdMyCart[] rowView = new CustomViewProdMyCart[100];
	ImageView deleteImage,image_favourite,red_plus_Image,red_minus_Image;
	TextView item_name,item_desc,mrp,you_save,tv_prescriptionRequired,amount_value_tv,okler_disc_value_tv,shipping_charg_value_tv,tax_value_tv,net_pay_value_tv;
	Button okler_price,image_cart;
	EditText units;
	LinearLayout forMycartScreen;
	int id;
	NetworkImageView item_img,item_img1;
	Button submit;
	Activity ack;
	ImageLoader imgloader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_cart);
		ack = this;
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		prodList = new ArrayList<ProductDataBean>();
		toolBar.setBackgroundColor(Color.BLUE);
	       context=getApplicationContext();
	       setSupportActionBar(toolBar);
	       final ActionBar ab=getSupportActionBar();
	      // ab.setDisplayHomeAsUpEnabled(true);
	       submit = (Button)findViewById(R.id.submit);
	       amount_value_tv = (TextView)findViewById(R.id.amount_value_tv);
	       okler_disc_value_tv = (TextView)findViewById(R.id.okler_disc_value_tv);
	       shipping_charg_value_tv = (TextView)findViewById(R.id.shipping_charg_value_tv);
	       tax_value_tv = (TextView)findViewById(R.id.tax_value_tv);
	       net_pay_value_tv = (TextView)findViewById(R.id.net_pay_value_tv);
	       imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       toolBarCount = (Button)toolBar.findViewById(R.id.toolBarCount);
	       med_LL = (LinearLayout)findViewById(R.id.med_LL);
	       mtitle_mycart = (TextView)med_LL.findViewById(R.id.title_mycart);
	       hs_LL = (LinearLayout)findViewById(R.id.hs_LL);
	       htitle_mycart = (TextView)hs_LL.findViewById(R.id.title_mycart);
	       medList = (LinearLayout)findViewById(R.id.medList);
	       hsList = (LinearLayout)findViewById(R.id.hsList);
	       mainScroll = (ScrollView)findViewById(R.id.scrollViewMyCartMenu);
	       //setTouchListenerScrViews();
	       progressLinLayout = (LinearLayout)findViewById(R.id.progressLinLayout);	
	       imgloader = VolleyRequest.getInstance(getApplicationContext()).getImageLoader();
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
				if(!(prodList.size()<=0)){
				mainCart.setProdList(prodList);
				Okler.getInstance().setMainCart(mainCart);
				Intent intent = new Intent(getApplicationContext(),ProductCheckoutDeliveryAddress.class);
				intent.putExtra("Check", 0);
				startActivity(intent);
				}else{
					Toast.makeText(ack, "Please add some products in your cart", Toast.LENGTH_LONG).show();
				}
				
			}
		});
	       
	}
	
	public void getMyCart(){
		getMyCartUrl = getString(R.string.getUserCart);
				//getString(R.string.getMyCartUrl);
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
		
	       getMyCartUrl = getMyCartUrl+ubean.getId();
	    ArrayList<AddressDataBean> arr = ubean.getAddDatabean(); 
	    
	       /*mainCart.setProdList(prodList);
			Okler.getInstance().setMainCart(mainCart);
			int count = prodList.size();
			if(count>9){
			toolBarCount.setText(""+count);
			}else{
				toolBarCount.setText("0"+count);
			}*/
		
	       WebJsonObjectRequest cartjson = new WebJsonObjectRequest(Method.GET, getMyCartUrl, new JSONObject(), this , this);
	       //WebJsonObjectRequest cartjson = new WebJsonObjectRequest(Method.GET, getMyCartUrl, new JSONObject(), new Response.Listener<JSONObject>() {

			
	       if(VolleyRequest.addJsonObjectRequest(context, cartjson))
	       {
	   		showProgress(true);		
	   		}
	   	else{
	   		showProgress(false);
	   		
	   	}
	
	}
	private void showProgress(boolean paramBoolean)
	  {
	    if (paramBoolean)
	    {
	      this.progressLinLayout.setVisibility(View.VISIBLE);
	      return;
	    }
	    this.progressLinLayout.setVisibility(View.INVISIBLE);
	  }
	
	
	private void setHS() {
		ArrayList<ProductDataBean> setPdList = new ArrayList<ProductDataBean>();
		ProductDataBean pdBean = new ProductDataBean();
		int size = prodList.size();
		 RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
                 LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
         params3.setMargins(0, 7, 0, 0);
		for (int i = 0; i < size; i++) {
			pdBean = prodList.get(i);
			if(pdBean.getProdType()==1){
				setPdList.add(pdBean);
			}
		}
		
			int size1 = setPdList.size();
			if(size1 <=0){
				hs_LL.setVisibility(View.GONE);
			}
		for (int i = 0; i < size1; i++) {
			pdBean = setPdList.get(i);
			rowView[i] = new CustomViewProdMyCart(getApplicationContext());
			item_img=(NetworkImageView) rowView[i].findViewById(R.id.item_image);
            item_name=(TextView) rowView[i].findViewById(R.id.item_name);
            item_desc=(TextView)rowView[i].findViewById(R.id.item_description);
            mrp = (TextView)rowView[i].findViewById(R.id.tv_MrpValue);
            you_save = (TextView)rowView[i].findViewById(R.id.tv_youSaveValue);
            okler_price = (Button)rowView[i].findViewById(R.id.okler_Amount);
            units = (EditText)rowView[i].findViewById(R.id.unitValue);
            units.setId(i+5000);
            deleteImage=(ImageView)rowView[i].findViewById(R.id.deleteImage);
            deleteImage.setTag(""+i);
            image_favourite=(ImageView)rowView[i].findViewById(R.id.image_favourite);
            image_favourite.setVisibility(View.GONE);
            image_cart = (Button)rowView[i].findViewById(R.id.image_cart);
            image_cart.setVisibility(View.GONE);
            red_plus_Image = (ImageView)rowView[i].findViewById(R.id.red_plus_Image);
            int t = i+100;
            red_plus_Image.setTag(t+"");
            red_plus_Image.setLayoutParams(params3);
            red_minus_Image = (ImageView)rowView[i].findViewById(R.id.red_minus_Image);
            int t1 = i+1000;
            red_minus_Image.setTag(t1+"");
            
            
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.RIGHT_OF, units.getId());
                red_plus_Image.setLayoutParams(params);
           RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
           params2.setMargins(0, 0, 0, 8);  
           red_minus_Image.setLayoutParams(params2);
          
           
           //units.setLayoutParams(params2);
            /*int pre = pdBean.getPresc_needed();
            if(pre == 1){
            forMycartScreen = (LinearLayout)rowView[i].findViewById(R.id.forMycartScreen);
            forMycartScreen.setVisibility(View.VISIBLE);
            }*/
            
            item_name.setText(pdBean.getProdName());
            String desc = pdBean.getGeneric_name()+"\n"+pdBean.getCompany();
            item_desc.setText(desc);
            mrp.setText(""+pdBean.getMrp());
            you_save.setText(pdBean.getDiscount()+"%");
            okler_price.setText(okler_price.getText().toString()+pdBean.getOklerPrice());
            int u = pdBean.getUnits();
            if(u>9){
            units.setText(""+pdBean.getUnits());
            }else{
            units.setText("0"+pdBean.getUnits());	
            }
            
            //String First= "http://183.82.110.105:8081/oklerdevv2/uploads/images/medium/";
            String First= pdBean.getThumbUrl();
            String second = pdBean.getImgUrl();
			String photo_url=First+second;
       	 
       	 item_img.setImageUrl(photo_url, imgloader);
            hsList.addView(rowView[i]);
            
            int count = prodList.size();
			if(count>9){
			toolBarCount.setText(""+count);
			}else{
				toolBarCount.setText("0"+count);
			}
			
            deleteImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String i = String.valueOf(v.getTag());
			//		Toast.makeText(getApplicationContext(), "onClick", Toast.LENGTH_SHORT).show();
					id = Integer.parseInt(i);
					//temp id
					UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
					int userId = ubean.getId();
					ProductDataBean pbean = new ProductDataBean();
					pbean = prodList.get(id);
					int prodId1 = pbean.getProdId();
			String remove1,remove2,deleteFromCartUrl;
			remove1 = "https://www.okler.com/api/products/usercart/deletecart?cust_id=";
			remove2 = "&product_id=";
			deleteFromCartUrl = remove1+userId+remove2+prodId1;
			WebJsonObjectRequest deljson = new WebJsonObjectRequest(Method.GET, deleteFromCartUrl, new JSONObject(), new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
			//		Toast.makeText(getApplicationContext(), "onResponse", Toast.LENGTH_SHORT).show();
						JSONObject jobj = (JSONObject)response;
						try {
							showProgress(false);
							String msg = jobj.getString("message");
							if(msg.equals("item in your cart are deleted successfully.")){
								prodList.remove(id);
								int i1 = prodList.size();
								mainCart.setProdList(prodList);
								medList.removeAllViews();
								hsList.removeAllViews();
								getMyCart();
								setAmount();
							}
							else{
								Toast.makeText(getApplicationContext(), "Some Error Ocurred."+"\n"+"Item not Deleted.", Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
						//	Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
						
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
				//	Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_SHORT).show();	
					showProgress(false);
				}
			});	
			if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), deljson))
			{
		   		showProgress(true);		
		   		}
		   	else{
		   		showProgress(false);
		   		
		   	}
			
				}
			});
            
            red_plus_Image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String i = String.valueOf(v.getTag());
					int id = Integer.parseInt(i);
					id = id -100;
					int id1 = id+5000;
					ViewParent ll = v.getParent();
					EditText edd = (EditText)findViewById(id1);
					if(edd.getText().toString().trim().equals("")){
						edd.setText("00");
					}
					int unit = Integer.parseInt(edd.getText().toString());
					unit++;
					prodList.get(id).setUnits(unit);
					if(unit>9){
						edd.setText(""+unit);
						}else{
							edd.setText("0"+unit);
						}
					int i1 = prodList.size();
					mainCart.setProdList(prodList);
					Okler.getInstance().setMainCart(mainCart);
					//medList.removeAllViews();
					//hsList.removeAllViews();
					//setMeds();
					//setHS();
					setAmount();
					
				}
			});   
	            
	         red_minus_Image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String i = String.valueOf(v.getTag());
					int id = Integer.parseInt(i);
					id = id -1000;
					int id1 = id+5000;
					ViewParent ll = v.getParent();
					EditText edd = (EditText)findViewById(id1);
					if(edd.getText().toString().trim().equals("")){
						edd.setText("00");
					}
					int unit = Integer.parseInt(edd.getText().toString());
					if(unit<=1){
						unit=1;
					}else{
					unit--;
					}
					prodList.get(id).setUnits(unit);
					int i1 = prodList.size();
					if(unit>9){
						edd.setText(""+unit);
						}else{
							edd.setText("0"+unit);
						}
					mainCart.setProdList(prodList);
					Okler.getInstance().setMainCart(mainCart);
					//medList.removeAllViews();
					//hsList.removeAllViews();
					//setMeds();
					//setHS();
					setAmount();
				}
			});
		       
         
			
			
		}
		
	}

	private void setMeds() {
		ArrayList<ProductDataBean> setPdList = new ArrayList<ProductDataBean>();
		ProductDataBean pdBean = new ProductDataBean();
		int size = prodList.size();
		
		for (int i = 0; i < size; i++) {
			pdBean = prodList.get(i);
			if(pdBean.getProdType()!=1){
				setPdList.add(pdBean);
			}
		}
			int size2 = setPdList.size();
			
			if(size2<=0){
				med_LL.setVisibility(View.GONE);
			}
		for (int i = 0; i < size2; i++) {
			pdBean = setPdList.get(i);
			rowView[i] = new CustomViewProdMyCart(getApplicationContext());
			item_img1=(NetworkImageView) rowView[i].findViewById(R.id.item_image);
            item_name=(TextView) rowView[i].findViewById(R.id.item_name);
            item_desc=(TextView)rowView[i].findViewById(R.id.item_description);
            mrp = (TextView)rowView[i].findViewById(R.id.tv_MrpValue);
            you_save = (TextView)rowView[i].findViewById(R.id.tv_youSaveValue);
            okler_price = (Button)rowView[i].findViewById(R.id.okler_Amount);
            units = (EditText)rowView[i].findViewById(R.id.unitValue);
            units.setId(i+500);
           
            deleteImage=(ImageView)rowView[i].findViewById(R.id.deleteImage);
            deleteImage.setTag(""+i);
            image_favourite=(ImageView)rowView[i].findViewById(R.id.image_favourite);
            image_favourite.setVisibility(View.GONE);
            image_cart = (Button)rowView[i].findViewById(R.id.image_cart);
            image_cart.setVisibility(View.GONE);
         
            red_plus_Image = (ImageView)rowView[i].findViewById(R.id.red_plus_Image);
            int t = i+100;
            red_plus_Image.setTag(t+"");
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.RIGHT_OF, units.getId());
                red_plus_Image.setLayoutParams(params);
            red_minus_Image = (ImageView)rowView[i].findViewById(R.id.red_minus_Image);
            int t1 = i+1000;
            red_minus_Image.setTag(t1+"");
            /*int pre = pdBean.getPresc_needed();
            if(pre == 1){
            forMycartScreen = (LinearLayout)rowView[i].findViewById(R.id.forMycartScreen);
            forMycartScreen.setVisibility(View.VISIBLE);
            }*/
            
            item_name.setText(pdBean.getProdName());
            String desc = pdBean.getGeneric_name()+"\n"+pdBean.getCompany();
            item_desc.setText(desc);
            mrp.setText(""+pdBean.getMrp());
            you_save.setText(pdBean.getDiscount()+"%");
            okler_price.setText(okler_price.getText().toString()+pdBean.getOklerPrice());
            units.setText(""+pdBean.getUnits());
            //String First= "http://183.82.110.105:8081/oklerdevv2/uploads/images/medium/";
            String First= pdBean.getThumbUrl();
			String second = pdBean.getImgUrl();
			String photo_url=First+second;
       	 	item_img1.setImageUrl(photo_url, imgloader);
            medList.addView(rowView[i]);
            
            int count = prodList.size();
			if(count>9){
			toolBarCount.setText(""+count);
			}else{
				toolBarCount.setText("0"+count);
			}
            
		    deleteImage.setOnClickListener(new OnClickListener() {
				
						@Override
						public void onClick(View v) {
							String i = String.valueOf(v.getTag());
							id = Integer.parseInt(i);
							UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
							int userId = ubean.getId();
							ProductDataBean pbean = new ProductDataBean();
							
							pbean = prodList.get(id);
							int prodId1 = pbean.getProdId();
					String remove1,remove2,deleteFromCartUrl;
					remove1 = "https://www.okler.com/api/products/usercart/deletecart?cust_id=";
					remove2 = "&product_id=";
					deleteFromCartUrl = remove1+userId+remove2+prodId1;
					WebJsonObjectRequest deljson = new WebJsonObjectRequest(Method.GET, deleteFromCartUrl, new JSONObject(), new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							showProgress(false);
								JSONObject jobj = (JSONObject)response;
								try {
									String msg = jobj.getString("message");
									if(msg.equals("item in your cart are deleted successfully.")){
										prodList.remove(id);
										int i1 = prodList.size();
										mainCart.setProdList(prodList);
										medList.removeAllViews();
										hsList.removeAllViews();
										if(i1<=0){
											setAmount();
											toolBarCount.setText("00");
										}else{
											getMyCart();
											setAmount();
										}
										
									}
									else{
									//	Toast.makeText(getApplicationContext(), "Some Error Ocurred."+"\n"+"Item not Deleted.", Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
						//	Toast.makeText(getApplicationContext(), "Error prod deleted", Toast.LENGTH_SHORT).show();	
							showProgress(false);
						}
					
					});		
						
					if(VolleyRequest.addJsonObjectRequest(getApplicationContext(), deljson))
					{
				   		showProgress(true);		
				   		}
				   	else{
				   		showProgress(false);
				   		
				   	}
							/*setMeds();
							setHS();*/
						}
					});
		            
		    
	         red_plus_Image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String i = String.valueOf(v.getTag());
					int id = Integer.parseInt(i);
					id = id -100;
					int id1 = id+500;
					ViewParent ll = v.getParent();
					EditText edd = (EditText)findViewById(id1);
					if(edd.getText().toString().trim().equals("")){
						edd.setText("00");
					}
					int unit = Integer.parseInt(edd.getText().toString());
					unit++;
					prodList.get(id).setUnits(unit);
					if(unit>9){
						edd.setText(""+unit);
						}else{
							edd.setText("0"+unit);
						}
					int i1 = prodList.size();
					mainCart.setProdList(prodList);
					Okler.getInstance().setMainCart(mainCart);
					//medList.removeAllViews();
					//hsList.removeAllViews();
					//setMeds();
					//setHS();
					setAmount();
					
				}
			});   
	            
	         red_minus_Image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String i = String.valueOf(v.getTag());
					int id = Integer.parseInt(i);
					id = id -1000;
					int id1 = id+500;
					ViewParent ll = v.getParent();
					EditText edd = (EditText)findViewById(id1);
					if(edd.getText().toString().trim().equals("")){
						edd.setText("00");
					}
					int unit = Integer.parseInt(edd.getText().toString());
					if(unit<=1){
						unit=1;
					}else{
					unit--;
					}
					prodList.get(id).setUnits(unit);
					int i1 = prodList.size();
					if(unit>9){
						edd.setText(""+unit);
						}else{
							edd.setText("0"+unit);
						}
					mainCart.setProdList(prodList);
					Okler.getInstance().setMainCart(mainCart);
					//medList.removeAllViews();
					//hsList.removeAllViews();
					//setMeds();
					//setHS();
					setAmount();
				}
			});
		       
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
	//	Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
		showProgress(false);
	}

	@Override
	public void onResponse(Object response) {
		String err = String.valueOf(response);
	//	Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
		mainCart = new CartDataBean();
		showProgress(false);
		JSONObject jobj = (JSONObject)response;
		try {
			String msg = jobj.getString("message");
			if(msg.equals("User cart is Empty.")){
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
				pdbean.setCart_item_id(Integer.parseInt(jobj1.getString("cart_item_id")));
				pdbean.setMrp(Float.parseFloat(jobj1.getString("price")));
				pdbean.setOklerPrice(Float.parseFloat(jobj1.getString("saleprice")));
				pdbean.setDiscount(Float.parseFloat(jobj1.getString("discount")));
				String ajimg = jobj1.getString("images");
				String aurl2="";
				if(ajimg.equals(null)){
					aurl2 = "drawable://" + R.drawable.no_image_found;
				}else{
					if(ajimg.contains(",")){
				String aj1[] =ajimg.split(",");
				String aj2=aj1[0];
				String acolon = ":";
				String aj3[]=aj2.split(acolon);
				String aurl = aj3[2];
				String aurl1 = aurl.substring(1);
				int alength = aurl.length();
				aurl2 = aurl.substring(1, (alength-1));}
					else{
						aurl2 = ajimg;
					}
				}
				pdbean.setImgUrl(aurl2);
				
				JSONObject uobj= new JSONObject();	
				uobj = jobj.getJSONObject("productimage_url");
				pdbean.setMediumUrl(uobj.optString("productimage_url_medium"));
				pdbean.setSmallUrl(uobj.optString("productimage_url_small"));
				pdbean.setThumbUrl(uobj.optString("productimage_url_thumbnail"));
				if(jobj1.getString("prescription_need").equals("null")||jobj1.getString("prescription_need").equals(null)||jobj1.getString("prescription_need").equals(""))
				{
				pdbean.setPresc_needed(0);	
				}else{
				pdbean.setPresc_needed(Integer.parseInt(jobj1.getString("prescription_need")));
				}
				pdbean.setCompany(jobj1.optString("company_name"));
				if(jobj1.has("generic_val")){
				pdbean.setGeneric_name(jobj1.optString("generic_val"));//need string from webservice
				}
				pdbean.setProdType(Integer.parseInt(jobj1.getString("pro_type")));
				
				prodList.add(pdbean);
				
			}
			
			mainCart.setProdList(prodList);
			Okler.getInstance().setMainCart(mainCart);
			int count = prodList.size();
			if(count>9){
			toolBarCount.setText(""+count);
			}else{
				toolBarCount.setText("0"+count);
			}
			
			setAmount();
			setMeds();
		       setHS();
			
			/*clistViewAdapter = new CheckoutListViewAdapter(getApplicationContext(), prodList,true);
			medList.setAdapter(clistViewAdapter);*/
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	private void setTouchListenerScrViews()
	{
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
	
	public void setAmount(){
		int length = prodList.size(),units;
		float mrp = 0.00f,okler_pr=0.00f,disco=0.00f,tax = 0.00f,shipping=0.00f,net_pay=0.00f;
		
		if(length <=0){
			amount_value_tv.setText("00");
			okler_disc_value_tv.setText("00");
			shipping_charg_value_tv.setText("00");
			tax_value_tv.setText("00");
			net_pay_value_tv.setText("00");
		}else{
		ProductDataBean pbean = new ProductDataBean();
		for (int i = 0; i < length; i++) {
			pbean = prodList.get(i);
			units = pbean.getUnits();
			float mrp1 = pbean.getMrp();
			mrp = mrp + (pbean.getMrp()*pbean.getUnits());
			float ok = pbean.getOklerPrice();
			okler_pr = okler_pr + (pbean.getOklerPrice()*pbean.getUnits());
			
			
			float tx= pbean.getTax();
			tax = tax + (pbean.getTax()*pbean.getUnits());
		}
		disco = (mrp - okler_pr);
		net_pay = okler_pr+tax;
		if(net_pay<499){
			shipping = 50;
			net_pay = net_pay+shipping;
		}
		amount_value_tv.setText("Rs."+mrp);
		okler_disc_value_tv.setText("Rs."+disco);
		shipping_charg_value_tv.setText("Rs."+shipping);
		tax_value_tv.setText("Rs."+tax);
		net_pay_value_tv.setText("Rs."+net_pay);
		}
		
		
	}
}
