package com.okler.android;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.CDATASection;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.JsonObject;
import com.okler.android.PrescriptionDelivery.UploadPrescAsyncTask;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.dialogs.OrderPlacedConfirmationDialog;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductCheckoutPaymentMode extends BaseActivity {
	private View bottomBarLayout;
	private Toolbar toolBar;
	private Context context;
	private Button processButton;
	private String orderId;
	TextView textView1;
	ImageView progressInScreen;
	CartDataBean cdbean;
	ImageView imgBack;
	UsersDataBean ubean,defaultUbean;
	AddressDataBean adbean;
	ArrayList<AddressDataBean> adList;
	ArrayList<NameValuePair> nvPair = new ArrayList<NameValuePair>();
	ArrayList<NameValuePair> nvProds = new ArrayList<NameValuePair>();
	ArrayList<NameValuePair> nvProdsq = new ArrayList<NameValuePair>();
	ArrayList<ProductDataBean> pdList = new ArrayList<ProductDataBean>();
	String bill_city,bill_add1,bill_add2,billFname,bill_zip,payment_info,shipFname,shipCity,fname,emailId,salutation1,	
	ship_add2,ship_add1,ship_zip,bill_state,ship_state,ship_charges;
	Button notifCount;
	int check;
	Activity ack;
	boolean isDialog=false;
	int userId;
	
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_product_checkout_payment_mode);
			toolBar=(Toolbar) findViewById(R.id.toolbar);
			notifCount = (Button) toolBar.findViewById(R.id.notif_count);
		       context=getApplicationContext();
		       setSupportActionBar(toolBar);
		       ack = this;
		       check = getIntent().getIntExtra("Check", 11);
		       final ActionBar ab=getSupportActionBar();
		      // ab.setDisplayHomeAsUpEnabled(true);
		       textView1 = (TextView)findViewById(R.id.textView1);
		       progressInScreen = (ImageView)findViewById(R.id.progressInScreen);
		       if(Okler.getInstance().getBookingType()==0){
		    	   progressInScreen.setImageResource(R.drawable.md_process_payment);
		    	   textView1.setBackgroundColor(getResources().getColor(R.color.Brightyellow));
				//	ab.setTitle(R.string.title_activity_allopathy);
		    	   Utilities.setTitleText(toolBar, getString(R.string.allopathy));
				}else if(Okler.getInstance().getBookingType()==3){
				//	progressInScreen.setImageResource(R.drawable.md_process_payment);
					textView1.setBackgroundColor(getResources().getColor(R.color.Brightyellow));
					//ab.setTitle(R.string.title_activity_ayurvedic);
					Utilities.setTitleText(toolBar, getString(R.string.ayurvedic));
				}else if(Okler.getInstance().getBookingType()==4){
					//progressInScreen.setImageResource(R.drawable.md_process_payment);
					textView1.setBackgroundColor(getResources().getColor(R.color.Brightyellow));
					//ab.setTitle(R.string.title_activity_homeopathy);
					Utilities.setTitleText(toolBar, getString(R.string.homeopathy));
				}else{
					//ab.setTitle(R.string.title_activity_health_shop_grid);
					 Utilities.setTitleText(toolBar, "Health Shop");
					
				}
		       toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler.getInstance().getBookingType()));
		       if(check==0){
		    	   toolBar.setBackgroundColor(Color.BLUE);
		    	   textView1.setBackgroundColor(Color.BLUE);
		    	   Utilities.setTitleText(toolBar, "Cart");
		       }
				
		       //toolBar.setBackgroundResource(R.drawable.custom_view_grad_healthshop);
		       bottomBarLayout = findViewById(R.id.bottombar);
		       handleMapping(bottomBarLayout);
		       imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
		       imgBack.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				finish();	
				}
			});
		       if(check == 1){
		       cdbean = Okler.getInstance().getSingleCart();
		       }else{
		    	   cdbean = Okler.getInstance().getMainCart();
		       }
		       pdList = cdbean.getProdList();
		       processButton=(Button) findViewById(R.id.payment_placeorder);
		       processButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					processButton.setEnabled(false);
					// TODO Auto-generated method stub
					int[] prodid = new int[50];
					int[] quantity= new int[50];
					String one = "";
					//ProductDataBean pdBean = pdList.get(0);
					//pdList.get(0).setProdId(123);
					payment_info = "Cash On Delivery";
					
					/*int prodid1 = pdBean.getProdId();
					 int quantity1 = pdBean.getUnits();
					 one = one +"(product_id="+prodid1+"&quantity="+quantity1+"))";*/
				//	String prod = one.substring(0, one.length()-1);
				//	prod = prod+")";
					
					//String bill_phone = "1234567890";
					String cart_id = "";
					String shipLname="",ship_salutation="",bill_salutation="",bill_lastname="",del_time="",b_land="",s_land="";
					ubean = Utilities.getCurrentUserFromSharedPref(ack);
					adList = ubean.getAddDatabean();
					int length = adList.size();
					for (int i = 0; i < length; i++) {
						adbean = adList.get(i);
						if(adbean.getDefault_billing()!=0){
						bill_city = adbean.getCity_id();
						bill_add1 = adbean.getAddress1();
						bill_add2 = adbean.getAddress2();
						bill_salutation = adbean.getBillSalut();
						billFname = adbean.getFirstname();
						bill_lastname = adbean.getLastname();
						bill_zip = ""+adbean.getZip();
						bill_state = adbean.getState_id();
						b_land = adbean.getLandmark();
						}
						if(adbean.isSelected()){
							shipFname = adbean.getFirstname();
							shipLname = adbean.getLastname();
							shipCity = adbean.getCity_id();
							ship_salutation = adbean.getShipSalut();
							del_time= adbean.getPreferred_del_time();
							ship_add2 = adbean.getAddress2();
							ship_add1 = adbean.getAddress1();
							ship_zip = ""+adbean.getZip();
							ship_state = adbean.getState_id();
							s_land = adbean.getLandmark();
						}
					}
					//defaultUbean = Utilities.getCurrentUserFromSharedPref(ack);
					userId = ubean.getId();
					fname = ubean.getFname();
					emailId = ubean.getEmail();
					salutation1 = ubean.getSalutation();
					String subtotal="";
					
					nvPair.add(new BasicNameValuePair("total", ""+cdbean.getTotalPrice()));//
					nvPair.add(new BasicNameValuePair("payment_info", payment_info));//
					nvPair.add(new BasicNameValuePair("ccode", cdbean.getcCode()));
					nvPair.add(new BasicNameValuePair("subtotal", subtotal));//
					nvPair.add(new BasicNameValuePair("tax", ""+cdbean.getTax()));//
					nvPair.add(new BasicNameValuePair("city_select", shipCity));
					nvPair.add(new BasicNameValuePair("ship_salutation", ship_salutation));
					nvPair.add(new BasicNameValuePair("bill_salutation", bill_salutation));
					nvPair.add(new BasicNameValuePair("bill_landmark", b_land));
					nvPair.add(new BasicNameValuePair("ship_landmark", s_land));
					
					//nvPair.add(new BasicNameValuePair("", ""));
					//nvPair.add(new BasicNameValuePair("bill_zone", ""));
					//nvPair.add(new BasicNameValuePair("Testuuhih8", ""));
					nvPair.add(new BasicNameValuePair("ship_country", "Indian"));
					nvPair.add(new BasicNameValuePair("bill_email", ubean.getEmail()));
					//nvPair.add(new BasicNameValuePair("bill_comment", ""));
					nvPair.add(new BasicNameValuePair("bill_city", bill_city));
					nvPair.add(new BasicNameValuePair("bill_phone", ubean.getPhone()));
					nvPair.add(new BasicNameValuePair("ship_phone", ubean.getPhone()));
					nvPair.add(new BasicNameValuePair("ship_email", ubean.getEmail()));
					nvPair.add(new BasicNameValuePair("bill_email", ubean.getEmail()));
					nvPair.add(new BasicNameValuePair("phone", ubean.getPhone()));
					nvPair.add(new BasicNameValuePair("email", ubean.getEmail()));
					nvPair.add(new BasicNameValuePair("bill_firstname", billFname));
					nvPair.add(new BasicNameValuePair("bill_lastname", bill_lastname));
					
					nvPair.add(new BasicNameValuePair("bill_address1", bill_add1));
					nvPair.add(new BasicNameValuePair("bill_address2", bill_add2));
					nvPair.add(new BasicNameValuePair("ship_firstname", shipFname));//
					nvPair.add(new BasicNameValuePair("ship_lastname", shipLname));//
					nvPair.add(new BasicNameValuePair("lastname", shipLname));//
					nvPair.add(new BasicNameValuePair("ship_city", shipCity));
					nvPair.add(new BasicNameValuePair("delivery_slots_timings", del_time));
					//bill_firstName
					nvPair.add(new BasicNameValuePair("firstname", billFname));
					//nvPair.add(new BasicNameValuePair("ship_zone", ""));
					//nvPair.add(new BasicNameValuePair("Testuuhih8", ""));
					nvPair.add(new BasicNameValuePair("cust_id", ""+userId));//
					nvPair.add(new BasicNameValuePair("bill_zip", bill_zip));
					//nvPair.add(new BasicNameValuePair("ship_phone", ubean.getPhone()));
					nvPair.add(new BasicNameValuePair("ship_address2", ship_add2));
					nvPair.add(new BasicNameValuePair("ship_address1", ship_add1));
					nvPair.add(new BasicNameValuePair("coupon_discount", ""+cdbean.getCoupon_disc()));
					nvPair.add(new BasicNameValuePair("bill_country", "Indian"));
					//nvPair.add(new BasicNameValuePair("notes", ""));
					nvPair.add(new BasicNameValuePair("ship_zip", ship_zip));
					//nvPair.add(new BasicNameValuePair("products", "array(product_id=925&quantity=1),(product_id=1489&quantity=1),(product_id=1504&quantity=1))"));
					//nvPair.add(new BasicNameValuePair("products", one));
					nvPair.add(new BasicNameValuePair("presc_id", cdbean.getPresc_id()));
					nvPair.add(new BasicNameValuePair("ship_state", ship_state));
					nvPair.add(new BasicNameValuePair("bill_state", bill_state));
					nvPair.add(new BasicNameValuePair("shipping_charges", ""+cdbean.getShip_charge()));
					for (int i = 0; i < pdList.size(); i++) {
						ProductDataBean pdBean = pdList.get(i);
						 prodid[i] = pdBean.getProdId();
						 quantity[i] = pdBean.getUnits();
						 cart_id = pdBean.getCart_id();
						 String prodPara  = "products["+i+"][product_id]";
						 String qtyPara = "products["+i+"][quantity]";// +prodid[i]+"&Product["+i+"][quantity]="+quantity[i];
						 nvPair.add(new BasicNameValuePair(prodPara,""+prodid[i]));
						 nvPair.add(new BasicNameValuePair(qtyPara,""+quantity[i]));
						 
					}
					nvPair.add(new BasicNameValuePair("cart_id", cart_id));
					/*nvProds.add(new BasicNameValuePair("product_id", ""+925));
					nvProds.add(new BasicNameValuePair("product_id", ""+1489));
					nvProds.add(new BasicNameValuePair("product_id", ""+1504));
					nvProds.add(new BasicNameValuePair("quantity", "1"));
					nvProdsq.add(new BasicNameValuePair("quantity", "2"));
					nvProdsq.add(new BasicNameValuePair("quantity", "3"));*/
					//nvPair.add(new BasicNameValuePair("products[0]", "product_id=925&quantity=1"));
					//nvPair.add(new BasicNameValuePair("products[1]", "product_id=1489&quantity=1"));
					//nvPair.add(new BasicNameValuePair("products[2]", "product_id=1504&quantity=1"));
					//temp
					//nvPair.add(new BasicNameValuePair("vendor_id", "409"));
					
					
					//nvPair.add(new BasicNameValuePair("", ""));
					String str ="";// gson.toJson(postParams);
					new PlaceOrderAsyncTask().execute(str);
					
				}
			});

		}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(isDialog){
			Intent intent = new Intent(ProductCheckoutPaymentMode.this,ServiceCategoryActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
				/*getMenuInflater().inflate(R.menu.allopathy, menu);
				MenuItem item = menu.findItem(R.id.action_settings);
				item.setActionView(R.layout.feed_update_count);
			    //
				 View count = item.getActionView();
				    notifCount = (Button) count.findViewById(R.id.notif_count);
				    notifCount.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
						//	Toast.makeText(getApplicationContext(), "btn clicked", Toast.LENGTH_LONG).show();
							Intent i = new Intent(getApplicationContext(), MyCart.class);
							startActivity(i);
						}
					});
					CartDataBean cbean = Okler.getInstance().getMainCart();
					   ArrayList<ProductDataBean>pdList = cbean.getProdList();
					   // pdList = odbean.getProdList();
					   int maincount = pdList.size(); 
					   
					   if(maincount>9){
					    notifCount.setText(""+maincount);
					    }
					    else{
					    	notifCount.setText("0"+maincount);
					    }*/
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
	
	public class PlaceOrderAsyncTask extends AsyncTask<String, Object, Object>{

			String placeOrderUrl = getString(R.string.placeOrderUrl);
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(String... params) {
			Object result = Utilities.RegisterNewUser(placeOrderUrl, nvPair);
			return result;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String resu = result.toString();
			if(resu.contains("Order placed successfully."))
			{
				String s1[] = resu.split(",");
				String s2 = s1[3];
				String s3[] = s2.split(":");
				orderId = s3[1];		
				
				if(check == 1){
					CartDataBean c = new CartDataBean();
				      Okler.getInstance().setSingleCart(c);
				       }else{
				    	   CartDataBean c = new CartDataBean();
				    	   Okler.getInstance().setMainCart(c);
				       }
				
			//	Toast.makeText(getApplicationContext(), resu, Toast.LENGTH_LONG).show();
				/*OrderPlacedConfirmationDialog od= new OrderPlacedConfirmationDialog(context,orderId);
				od.show(getSupportFragmentManager(),"OKLER");*/
				
				isDialog=true;
			String part1,part2,part3,part4,Url,part5;
			part1 = getString(R.string.orderConfirmCallBackUrl);
			part2="&cust_id=";
			part3="&customer_name=";
			part4="&email=";
			part5="&order_id=";
			try {
				fname = URLEncoder.encode(fname, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Url= part1+salutation1+part2+userId
					+part3+fname+part4+emailId+part5+orderId;
			WebJsonObjectRequest mailjson = new WebJsonObjectRequest(Method.GET, Url, new JSONObject(), new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					
					Log.e("order mail", "mail sent");
					
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					// TODO Auto-generated method stub
					
					Log.e("error", error.getStackTrace().toString());
					//Log.e("Error", new String(error.networkResponse.data));
					Log.e("order mail", "mail not sent");
					
				}
			}, true);
			VolleyRequest.addJsonObjectRequest(ack, mailjson);
				Intent in = new Intent(ProductCheckoutPaymentMode.this, MyOrderHome.class);
				in.putExtra("value",1);
				in.putExtra("fromOrder", true);
				startActivity(in);
				finish();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Some error occured while placing order", Toast.LENGTH_LONG).show();
				processButton.setEnabled(true);
			}
		}
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(notifCount, ack);
	}
}
