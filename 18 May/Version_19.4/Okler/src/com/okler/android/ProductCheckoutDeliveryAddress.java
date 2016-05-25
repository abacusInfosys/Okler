package com.okler.android;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.okler.customviews.CustomViewAddrDiagno;
import com.okler.customviews.CustomViewAddress;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.DateTimeKeyListener;

import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okleruser.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.LinearLayout;
public class ProductCheckoutDeliveryAddress extends BaseActivity {
	Button notifCount;
	Toolbar toolBar;
	TextView proceed,textView1,text_name,text_address,selectAddress;
	RelativeLayout addAddressLayout;
	CartDataBean cdbean;
	View bottomBarLayout;
	ImageView progressInScreen,imgBack,editImage,radioButtonImage,deleteImage;
	CheckBox chkbx_addr;
	ArrayList<AddressDataBean> adList;
	UsersDataBean ubean;
	LinearLayout layout,deliveryAddress;
	View view1;
	int content,check;
	LinearLayout progressLinLayout;
	RelativeLayout.LayoutParams editparams;
	RelativeLayout.LayoutParams deleteparams,radioparams;
	LinearLayout.LayoutParams linparams;
	CustomViewAddress[] cvaddr = new CustomViewAddress[500];
	int userId,id;
	Activity ack;
	AddressDataBean adbean;
	ArrayList<AddressDataBean> aList;
	boolean isSelected=false,isCheckedAddr=false;
	int id2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_checkout_delivery_address);
		
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		notifCount = (Button) toolBar.findViewById(R.id.notif_count);
		selectAddress = (TextView)findViewById(R.id.selectAddress);
		check = getIntent().getIntExtra("Check", 11);
		if(check==1){
			cdbean = Okler.getInstance().getSingleCart();
		}else{
			cdbean = Okler.getInstance().getMainCart();
		}
		ack = this;
		progressLinLayout = (LinearLayout)findViewById(R.id.progressLinLayout);
		setSupportActionBar(toolBar);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		final ActionBar ab=getSupportActionBar();
		//ab.setDisplayHomeAsUpEnabled(true);
		textView1 = (TextView)findViewById(R.id.textView1);
		progressInScreen = (ImageView)findViewById(R.id.progressInScreen);
		if(Okler.getInstance().getBookingType()==0){
			textView1.setBackgroundColor(getResources().getColor(R.color.Brightyellow));
			progressInScreen.setImageResource(R.drawable.md_process_user_details_image);
			//ab.setTitle(R.string.title_activity_allopathy);
			Utilities.setTitleText(toolBar, getString(R.string.allopathy));
		}else if(Okler.getInstance().getBookingType()==3){
		//	progressInScreen.setImageResource(R.drawable.md_process_user_details_image);
			textView1.setBackgroundColor(getResources().getColor(R.color.Brightyellow));
			//ab.setTitle(R.string.title_activity_ayurvedic);
			Utilities.setTitleText(toolBar, getString(R.string.ayurvedic));
		}else if(Okler.getInstance().getBookingType()==4){
		//	progressInScreen.setImageResource(R.drawable.md_process_user_details_image);
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
		
		addAddressLayout=(RelativeLayout) findViewById(R.id.addAddressLayout);
		layout = (LinearLayout) findViewById(R.id.centerAddressLayout);
		addAddressLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),ProductCheckoutNewAddress.class);
				intent.putExtra("Check", check);
				startActivity(intent);
			}
		});

		proceed =(TextView) findViewById(R.id.tv_proceed);
		//proceed.setEnabled(false);
		proceed.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isCheckedAddr){
				Intent intent = null;
				CartDataBean cbean = new CartDataBean();
				if(check==1){
				cbean = Okler.getInstance().getSingleCart();
				}
				else{
				cbean = Okler.getInstance().getMainCart();	
				}
				ArrayList<ProductDataBean> prodList1 = cbean.getProdList();
				int prescNeeded = 0;
				int length = prodList1.size();
				for (int i = 0; i < length; i++) {
					prescNeeded = prodList1.get(i).getPresc_needed();
					if(prescNeeded==1){
						break;
					}
				}
				if(prescNeeded==1){
					intent = new Intent(getApplicationContext(),AlloPrescActivity.class);
					intent.putExtra("Check", check);
					startActivity(intent);
				}else{
				intent = new Intent(getApplicationContext(),ProductCheckoutSummary.class);
				intent.putExtra("Check", check);
				startActivity(intent);
				}
			}else{
				Toast.makeText(ack, "Please select a address", Toast.LENGTH_SHORT).show();
			}
			}
		});
		 imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	    
	   getAllAddress();   
	   // setUi();
	      

		//toolBar.setBackgroundResource(R.drawable.custom_view_grad_healthshop);
	}
	public void getAllAddress(){
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		userId = ubean.getId();
	String get_addr = getString(R.string.get_user_address)+userId;
	
WebJsonObjectRequest adjson = new WebJsonObjectRequest(Method.GET, get_addr, new JSONObject(), new Response.Listener<JSONObject>() {

	@Override
	public void onResponse(JSONObject response) {
		JSONObject responseObj =(JSONObject)response;
		JSONObject result;
		try {
			String res = responseObj.optString("result");
			if(res.equals("")){
				layout.removeAllViews();
				selectAddress.setText("You don't have any address."+"\n"+"Please add a new address.");
				showProgress(false);
			}else{
			result = responseObj.getJSONObject("result");
			
			if(!result.isNull("user_Addr")){
				JSONArray jarr2 = result.getJSONArray("user_Addr");
				JSONObject jobj2;
				int length = jarr2.length();
				aList = new ArrayList<AddressDataBean>();
				for (int i = 0; i < length; i++) {
					adbean = new AddressDataBean();
					jobj2 = new JSONObject();
					int a = i+1;
					if(a==length){
						break;
					}
					jobj2 = jarr2.getJSONObject(a);
					//jobj2 = jarr2.getJSONObject(i+1);
					
					adbean.setFirstname(jobj2.optString("customer_name"));
					adbean.setLastname(jobj2.optString("surname"));
					adbean.setAddress1(jobj2.optString("addr1"));
					adbean.setAddress2(jobj2.optString("addr2"));
					adbean.setZip(jobj2.optString("zip"));
					adbean.setCountry_id(jobj2.optString("country_id"));
					adbean.setAddr_id(jobj2.optString("addr_id"));
					adbean.setCity(jobj2.optString("city_name"));
					adbean.setCity_id(jobj2.optString("city_id"));
					adbean.setState_id(jobj2.optString("state_id"));
					adbean.setState(jobj2.optString("state_name"));
					adbean.setCountry(jobj2.optString("country_name"));
					adbean.setDefault_billing(jobj2.optInt("default_billing"));
					adbean.setDefault_shipping(jobj2.optInt("default_shiping"));
					adbean.setPreferred_del_time(jobj2.optString("delivery_time"));
					//adbean.setPreferred_del_time(jobj2.optString(name));
					AddressDataBean ab = new AddressDataBean();
					ab = adbean;
					aList.add(adbean);
			}
				ubean.setAddDatabean(aList);
				Utilities.writeCurrentUserToSharedPref(ack, ubean);
				setUi();
			}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showProgress(false);
		}
		
		
	}
}, new Response.ErrorListener() {

	@Override
	public void onErrorResponse(VolleyError error) {
		
		showProgress(false);
	}
});	

	/*VolleyRequest.remove(get_addr);
	VolleyRequest.clearCache();*/
	if(VolleyRequest.addJsonObjectRequest(ack, adjson)){
		showProgress(true);
	}else{
		showProgress(false);
	}

//address call ends here

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
	public void setUi(){
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
	    adList = aList; //ubean.getAddDatabean();
	    int length = adList.size();
	    if(length<=0){
	    	selectAddress.setText("You don't have any address."+"\n"+"Please add a new address.");
	    	showProgress(false);
	    }else{
	    editparams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		editparams.setMargins(0, 5, 0, 0);
		deleteparams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		deleteparams.setMargins(0, 200, 0, 0);
		layout.removeAllViews();
	    for (int i = 0; i < length; i++) {
	    	AddressDataBean add = adList.get(i);
	    	cvaddr[i]=new CustomViewAddress(getApplicationContext());
	    	chkbx_addr = (CheckBox)cvaddr[i].findViewById(R.id.chkbx_addr);
	    	chkbx_addr.setTag(""+i);
	    	//radioButtonImage = (ImageView) cvaddr[i].findViewById(R.id.radioButtonImage);
	    	int t=i+1000;
	    	//radioButtonImage.setTag(""+t);
	    	//radioButtonImage.setTag(R.string.FlagRdImage,0);
	    	
	    	text_address = (TextView) cvaddr[i].findViewById(R.id.multiLineText);
	    	editImage = (ImageView) cvaddr[i].findViewById(R.id.editImage);
	    	int t1 = i+100;
	    	editImage.setTag(""+t1);
	    	deleteImage = (ImageView) cvaddr[i].findViewById(R.id.deleteImage);
	    	int t2 = i+500;
	    	deleteImage.setTag(""+t2);
	    	text_name = (TextView) cvaddr[i].findViewById(R.id.titleDeliveryAddress);
	    	
	    	cvaddr[i].setTag(""+i);
			
			//if(add.getDefault_shipping()!=0){
			String text = add.getFirstname() + " " + add.getLastname();
			text_name.setText(text);
			String text1 =  add.getAddress1() + "\n" + add.getAddress2() + "\n" + add.getCity() + "," + add.getState() + "-" + add.getZip() + "\n" +"Preferred Delivery Time: " +add.getPreferred_del_time();
			text_address.setText(text1);
			layout.addView(cvaddr[i]);
		//	Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
			//}
			
			chkbx_addr.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
					AddressDataBean add;
					String tag = String.valueOf(buttonView.getTag());
					int id = Integer.parseInt(tag);
					
					
					int id1;
					for (int j = 0; j < adList.size(); j++) {
						 add = adList.get(j);
						add.setSelected(false);
					CustomViewAddress c = (CustomViewAddress)layout.getChildAt(j);
					CheckBox ch = (CheckBox) c.findViewById(R.id.chkbx_addr);
					id1 = Integer.parseInt(String.valueOf(ch.getTag()));
					if(id1!=id){
						ch.setChecked(false);
						//isSelected=true;
					}
						
						}
					
					add = adList.get(id);
					add.setSelected(true);
					ubean.setAddDatabean(adList);
					Utilities.writeCurrentUserToSharedPref(ack, ubean);
					
					isCheckedAddr=true;
						//proceed.setEnabled(true);
						//isSelected=true;
					}else{
						//proceed.setEnabled(false);
						//isSelected=false;
						isCheckedAddr=false;
					}
					
					
				}
			});
			
			editImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					int viewid = Integer.parseInt(String.valueOf(v.getTag()));
					viewid = viewid-100;
					AddressDataBean add = adList.get(viewid);
					
					Intent in = new Intent(getApplicationContext(), ProductCheckoutNewAddress.class);
					Gson gson = new Gson();
					String aBean = gson.toJson(add);
					content = 2;
					in.putExtra("int value", content);					
					in.putExtra("addressbean", aBean);
					startActivity(in);
					
				}
			});
			
			deleteImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					id = Integer.parseInt(String.valueOf(v.getTag()));
					id = id-500;
					AddressDataBean adbean = adList.get(id);
					int defBill = adbean.getDefault_billing();
					if(defBill>=1){
						Toast.makeText(ack, "You cannot delete default billing address", Toast.LENGTH_LONG).show();
					}else{
					String addrId = adbean.getAddr_id();
					String del1,del2,delUserAddUrl;
					ubean = Utilities.getCurrentUserFromSharedPref(ack);
					userId = ubean.getId();
					del1 = getString(R.string.DeleteUserAddPart1);
					del2 = getString(R.string.DeleteUserAddPart2);
					delUserAddUrl = del1+userId+del2+addrId;
				
					WebJsonObjectRequest dajson = new WebJsonObjectRequest(Method.GET, delUserAddUrl, new JSONObject(), new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					
					
					String msg = response.optString("message");
					if(msg.equals("deleted success from user Address")){
						Toast.makeText(ack, "Address successfully deleted.", Toast.LENGTH_LONG).show();
						//setUi();
						 
						adList.remove(id);
						ubean.setAddDatabean(adList);
						Utilities.writeCurrentUserToSharedPref(ack, ubean);
						getAllAddress();
					}else {
						Toast.makeText(ack, "Some error ocurred while deleting Address.", Toast.LENGTH_LONG).show();
						 getAllAddress();
						//setUi();
					}
					
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					
					
				}
			});		
					
			VolleyRequest.addJsonObjectRequest(ack,dajson);
				}
				}
			});
		}
	    
	    showProgress(false);
	    }
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(notifCount, ack);
	}
}
