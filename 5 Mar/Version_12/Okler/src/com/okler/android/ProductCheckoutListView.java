package com.okler.android;

import java.util.ArrayList;

import com.okler.adapters.CheckoutListViewAdapter;
import com.okler.adapters.HealthShopListAdapter;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okleruser.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductCheckoutListView extends BaseActivity {
	Button notifCount;
	Toolbar toolBar;
	static TextView next;
	static TextView totalAmountValue;
	static TextView count;
	static TextView emptyText;
	static ListView productList;
	RelativeLayout checkoutCount;
	View bottomBarLayout;
	static CartDataBean odbean;
	static CartDataBean odbean2;
	ArrayList<ProductDataBean> pdList;
	static ArrayList<ProductDataBean> pdSendList;
	ImageView imgBack;
	int prodtype,prodcount=0,maincount=0;
	static CheckoutListViewAdapter clistViewAdapter;
	static RelativeLayout amountLayout_chList;
	static float total = 0.00f;
	static int check;
	static Activity ack;
	static boolean isEmpty = false;
	    public static String [] prgmNameList={"1 kg weight cuff","1 kg weight cuff","1 kg weight cuff","1 kg weight cuff","1 kg weight cuff","1 kg weight cuff"};
	    public static int [] prgmImages={R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera};    
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_product_checkout_list_view);
			ack = this;
			check = getIntent().getIntExtra("Check", 1);
			toolBar=(Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolBar);
			notifCount = (Button) toolBar.findViewById(R.id.notif_count);
			final ActionBar ab=getSupportActionBar();
			bottomBarLayout = findViewById(R.id.bottombar);
			handleMapping(bottomBarLayout);
			//ab.setDisplayHomeAsUpEnabled(true);//11_01_2016 Gitesh
			checkoutCount = (RelativeLayout)findViewById(R.id.checkoutCount);
			if(Okler.getInstance().getBookingType()==0){
			//	ab.setTitle(R.string.title_activity_allopathy);
				prodtype =0;
				checkoutCount.setBackgroundColor(getResources().getColor(R.color.Brightyellow));
				Utilities.setTitleText(toolBar, getString(R.string.allopathy));
			}else if(Okler.getInstance().getBookingType()==3){
				//ab.setTitle(R.string.title_activity_ayurvedic);
				prodtype =3;
				checkoutCount.setBackgroundColor(getResources().getColor(R.color.Brightyellow));
				Utilities.setTitleText(toolBar, getString(R.string.ayurvedic));
			}else if(Okler.getInstance().getBookingType()==4){
				//ab.setTitle(R.string.title_activity_homeopathy);
				prodtype =4;
				checkoutCount.setBackgroundColor(getResources().getColor(R.color.Brightyellow));
				Utilities.setTitleText(toolBar, getString(R.string.homeopathy));
			}else{
				//ab.setTitle(R.string.title_activity_health_shop_grid);
				prodtype =1; 
				Utilities.setTitleText(toolBar, "Health Shop");
			}
			toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler.getInstance().getBookingType()));
			if(check==0){
				toolBar.setBackgroundColor(Color.BLUE);
				checkoutCount.setBackgroundColor(Color.BLUE);
				Utilities.setTitleText(toolBar, "Cart");
			}
			
			imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
		       imgBack.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				finish();	
				}
			});
		    amountLayout_chList = (RelativeLayout)findViewById(R.id.amountLayout_chList);   
		    emptyText = (TextView)findViewById(R.id.emptyText); 
			next =(TextView) findViewById(R.id.next);
			next.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					if(isEmpty){
						Intent intent = new Intent(ProductCheckoutListView.this,ServiceCategoryActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}else{
					CartDataBean cbean = new CartDataBean();
					if(check==1){
					cbean = Okler.getInstance().getSingleCart();
					}
					else{
					cbean = Okler.getInstance().getMainCart();	
					}
					int a = cbean.getProdList().get(0).getUnits();
					if (Utilities.getUserStatusFromSharedPref(ProductCheckoutListView.this) == UserStatusEnum.LOGGED_IN)
					{
					if(a!=0){	
					if(total>=0){	
					UsersDataBean ubean = new UsersDataBean();
				    ubean = Utilities.getCurrentUserFromSharedPref(ack);
					ArrayList<AddressDataBean> adList = new ArrayList<AddressDataBean>();
					adList = ubean.getAddDatabean();
					if(adList.size()<=0){
				    Intent intent = new Intent(getApplicationContext(),ProductCheckoutUserDetail.class);
					intent.putExtra("Check", check);
					startActivity(intent);
					}else{
						Intent intent = new Intent(getApplicationContext(),ProductCheckoutDeliveryAddress.class);
						intent.putExtra("Check", check);
						startActivity(intent);
					}
					}else{
						Toast.makeText(getApplicationContext(), "Unable to proceed with your order", Toast.LENGTH_LONG).show();
						Intent intent = new Intent(ProductCheckoutListView.this,ServiceCategoryActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						Okler.getInstance().setBookingType(100);
						startActivity(intent);
					}
					}else{
						Toast.makeText(ack, "Please Select atleast one unit", Toast.LENGTH_LONG).show();
					}
					}
					else
					{
						Intent in = new Intent(ProductCheckoutListView.this, NewSignIn.class);
						
						startActivity(in);
						finish();	
					}
					}
				}
			});
			
			totalAmountValue = (TextView)findViewById(R.id.totalAmountValue);
			count = (TextView)findViewById(R.id.count);
			if(check == 1){
			odbean = Okler.getInstance().getSingleCart();
			}
			else{
				odbean = Okler.getInstance().getMainCart();
			}
			productList = (ListView)findViewById(R.id.productList);
			
			//pdList.get(0).getProdType();
			
			
			//maincount = pdList.size();
			pdSendList = new ArrayList<ProductDataBean>();
			pdSendList = odbean.getProdList();
			//for (int i = 0; i < pdSendList.size(); i++) {
				//if(pdSendList.get(i).getProdType()== prodtype)
				//{
				//prodcount++;	
				//pdSendList.add(pdList.get(i));
				//}
			if(!(pdSendList.size()<=0)){
				
			
			int count1 = pdSendList.size();
				if(count1< 9){
					
				count.setText("0"+count1);
				}else{
					count.setText(""+count1);
				}	
			}
			//}
			setUi();
			
			
			/*for (int i = 0; i < prodcount; i++) {
				float price = pdList.get(i).getOklerPrice();
				int units = pdList.get(i).getUnits();
				
				
				total = total + (price*units);
			}*/
			//totalAmountValue.setText(totalOrderValue());
			//totalAmountValue.setText(""+total);
			//Toast.makeText(getApplicationContext(), pdList.get(0).getProdName(), Toast.LENGTH_SHORT).show();
			//toolBar.setBackgroundResource(R.drawable.custom_view_grad_physio);
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.product_checkout_list_view, menu);
		 /*MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.product_checkout_list_view, menu);
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
			   ArrayList<ProductDataBean>pdList = new ArrayList<ProductDataBean>();
			   pdList = cbean.getProdList();
			   // pdList = odbean.getProdList();
			   if(!(pdList.size()<=0)){
			   
			   int maincount = pdList.size(); 
			   
			   if(maincount>9){
			    notifCount.setText(""+maincount);
			    }
			    else{
			    	notifCount.setText("0"+maincount);
			    }
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
	public static void totalOrderValue(){
		ArrayList<ProductDataBean> prodL = new ArrayList<ProductDataBean>();
		if(check ==1){
		odbean2 = Okler.getInstance().getSingleCart();
		}else
		{
			odbean2 = Okler.getInstance().getMainCart();
		}
		prodL = odbean2.getProdList();
		/*
		int prodcount = prodL.size();
		
		
		for (int i = 0; i < prodcount; i++) {
			float price = prodL.get(i).getOklerPrice();
			int units = prodL.get(i).getUnits();
			
			
			total = total + (price*units);
			
		}
		odbean2.setTotalPrice(total);
		if(check ==1){
			Okler.getInstance().setSingleCart(odbean2);
			}else
			{
				Okler.getInstance().setMainCart(odbean2);
			}*/
		int length = prodL.size(),units;
		float mrp = 0.00f,okler_pr=0.00f,disco=0.00f,tax = 0.00f,shipping=0.00f,net_pay=0.00f;
		
		if(length <=0){
			totalAmountValue.setText("00");
		}else{
		ProductDataBean pbean = new ProductDataBean();
		for (int i = 0; i < length; i++) {

			pbean = prodL.get(i);
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
		totalAmountValue.setText("Rs."+okler_pr
				);
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		CartDataBean cbean = new CartDataBean();
		if(check==1){
		cbean = Okler.getInstance().getSingleCart();
		}
		else{
		cbean = Okler.getInstance().getMainCart();	
		}
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		CartDataBean cbean = new CartDataBean();
		if(check==1){
		cbean = Okler.getInstance().getSingleCart();
		}
		else{
		cbean = Okler.getInstance().getMainCart();	
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		CartDataBean cbean = new CartDataBean();
		if(check==1){
		cbean = Okler.getInstance().getSingleCart();
		}
		else{
		cbean = Okler.getInstance().getMainCart();	
		}
	}
	public static void setUi(){
		/*ProductCheckoutListView pl = new ProductCheckoutListView();
		pl.setView();
	}*/
	//public void setView(){
		if(check == 1){
			odbean = Okler.getInstance().getSingleCart();
			}
			else{
				odbean = Okler.getInstance().getMainCart();
			}
		pdSendList = new ArrayList<ProductDataBean>();
		pdSendList = odbean.getProdList();
		if(pdSendList.size()<=0){
			productList.setVisibility(View.GONE);
			emptyText.setVisibility(View.VISIBLE);
			amountLayout_chList.setVisibility(View.INVISIBLE);
			count.setText("00");
			next.setText("CONTINUE TO PRODUCTS");
			isEmpty = true;
			//totalOrderValue();
		}else{
			//emptyText.setVisibility(View.GONE);
			
			clistViewAdapter = new CheckoutListViewAdapter(ack, pdSendList,true,check);
			productList.setAdapter(clistViewAdapter);
			totalOrderValue();
			isEmpty = false;
		}
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(notifCount, ack);
	}
	/*public static void setEmptyUi(){
		
	}*/
}
