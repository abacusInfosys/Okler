package com.okler.android;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.okler.customviews.CustomViewOrderProdDetails;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.OrdersDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.dialogs.CameraGalleryDialog;
import com.okler.dialogs.CancelDialog;
import com.okler.dialogs.DialogCancellation;

import com.okler.dialogs.OrderCancellationDialog;
import com.okler.network.VolleyRequest;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyOrderMed_Hs_Details extends BaseActivity {
	
	static Button cancel;
	
	Toolbar toolBar;
	View bottomBarLayout;
	
	View view1, view2;
	TextView txt_addr_title, txt_addr_title1, addr_tv, addr_tv1;
	ImageView edit_red_pencil,edit_red_pencil1;
	NetworkImageView item_image;
	TextView item_name, item_description, tv_mrpvalue, tv_youSaveValue, amount_value_tv, okler_disc_value_tv, shipping_charg_value_tv, tax_value_tv, net_pay_value_tv;
	Button okler_amount, image_cart;
	CustomViewOrderProdDetails customProd[] = new CustomViewOrderProdDetails[50];
	LinearLayout custview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order_med__hs__details);
		
		TextView title_mycart = (TextView) findViewById(R.id.title_mycart);
		title_mycart.setText("SUMMARY");
		View view = (View) findViewById(R.id.address_title);
		TextView title_address = (TextView) view.findViewById(R.id.title_mycart);
		title_address.setText("ADDRESS");
		
		custview= (LinearLayout)findViewById(R.id.custview);
		view1 = (View) findViewById(R.id.address_delivery_detail);
		txt_addr_title = (TextView) view1.findViewById(R.id.addr_title_tv);
		edit_red_pencil = (ImageView) view1.findViewById(R.id.edit_red_pencil);
		addr_tv = (TextView) view1.findViewById(R.id.addr_tv);
		edit_red_pencil.setVisibility(View.GONE);
		txt_addr_title.setText("Delivery Address");
		view2 = (View) findViewById(R.id.address_billing_detail);
		txt_addr_title1 = (TextView) view2.findViewById(R.id.addr_title_tv);
		edit_red_pencil1 = (ImageView) view2.findViewById(R.id.edit_red_pencil);
		addr_tv1 = (TextView) view2.findViewById(R.id.addr_tv);
		edit_red_pencil1.setVisibility(View.GONE);
		txt_addr_title1.setText("Billing Address");
		
		View amount_view = (View) findViewById(R.id.amountLayout);
		
		amount_value_tv = (TextView) amount_view.findViewById(R.id.amount_value_tv);
		okler_disc_value_tv = (TextView) amount_view.findViewById(R.id.okler_disc_value_tv);
		shipping_charg_value_tv = (TextView) amount_view.findViewById(R.id.shipping_charg_value_tv);
		tax_value_tv = (TextView) amount_view.findViewById(R.id.tax_value_tv);
		net_pay_value_tv = (TextView) amount_view.findViewById(R.id.net_pay_value_tv);
		
		
		
		
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		toolBar.setBackgroundColor(Color.BLUE);
	       setSupportActionBar(toolBar);
	       
	       bottomBarLayout = findViewById(R.id.bottombar);
			handleMapping(bottomBarLayout);
	       
	       ImageView imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}

	       });
	       
	       Utilities.setTitleText(toolBar, "My Orders");
		
		item_image = (NetworkImageView) findViewById(R.id.item_image);
		
		item_name = (TextView) findViewById(R.id.item_name);
		item_description = (TextView) findViewById(R.id.item_description);
		tv_mrpvalue = (TextView) findViewById(R.id.tv_MrpValue);
		tv_youSaveValue = (TextView) findViewById(R.id.tv_youSaveValue);
		okler_amount = (Button) findViewById(R.id.okler_Amount);
		
		
		cancel = (Button) findViewById(R.id.cancelButton);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				DialogCancellation cdd=new DialogCancellation(MyOrderMed_Hs_Details.this);
				cdd.show();
				
			}
		});
		
		//image_cart = (Button) (findViewById(R.id.image_cart));
		//image_cart.setVisibility(View.GONE);
		
		int intentValue = getIntent().getIntExtra("intent_value", 0);
		switch (intentValue) {
		case 1:
			int pos = getIntent().getIntExtra("position", -1);
			
			ArrayList<OrdersDataBean> uorder = new ArrayList<OrdersDataBean>();
			uorder = Okler.getInstance().getUsersOrders();
			OrdersDataBean obean = new OrdersDataBean();
			obean = uorder.get(pos);
			ArrayList<ProductDataBean> prodArr = new ArrayList<ProductDataBean>();		
			prodArr = obean.getProd_list();
			ArrayList<AddressDataBean> addarr = new ArrayList<AddressDataBean>();
			addarr = obean.getAddbean();
			if(prodArr.size()<=0){
				cancel.setVisibility(View.GONE);
			}
			float mrp=0.0f,disc=0.0f,ship=0.0f,tax=0.0f,net_pay=0.0f,okler_pr=0.0f;
			int length = prodArr.size();
			ImageLoader imgLoader = VolleyRequest.getInstance(this).getImageLoader();
			for (int i = 0; i < length; i++) {
				ProductDataBean pbean = new ProductDataBean();
				pbean = prodArr.get(i);
				customProd[i] = new CustomViewOrderProdDetails(getApplicationContext());
				TextView unit_tv_value = (TextView)customProd[i].findViewById(R.id.unit_tv_value);
				TextView status_tv_value = (TextView)customProd[i].findViewById(R.id.status_tv_value);
				NetworkImageView item_image = (NetworkImageView)customProd[i].findViewById(R.id.item_image);
				TextView item_name = (TextView)customProd[i].findViewById(R.id.item_name);
				TextView item_description = (TextView)customProd[i].findViewById(R.id.item_description);
				TextView tv_MrpValue = (TextView)customProd[i].findViewById(R.id.tv_MrpValue);
				TextView tv_youSaveValue = (TextView)customProd[i].findViewById(R.id.tv_youSaveValue);
				LinearLayout prescriptionRL = (LinearLayout)customProd[i].findViewById(R.id.prescriptionRL);
				Button okler_Amount = (Button)customProd[i].findViewById(R.id.okler_Amount);
				Button image_cart = (Button)customProd[i].findViewById(R.id.image_cart);
				image_cart.setVisibility(View.GONE);
				
				item_name.setText(pbean.getProdName());
				if(pbean.getUnits()<9){
				unit_tv_value.setText("0"+pbean.getUnits());
				}else{
				unit_tv_value.setText(""+pbean.getUnits());	
				}
				String order_status = obean.getOrderStatus();
				if(order_status.equals("Cancelled")){
					cancel.setVisibility(View.GONE);
				}
				status_tv_value.setText(obean.getOrderStatus());
				if(obean.getOrderStatus().equals("Awaiting Prescription")){
					status_tv_value.setTextColor(getResources().getColor(R.color.Red));
				}else{
					status_tv_value.setTextColor(getResources().getColor(R.color.Black));
				}
				String desc="";
				if(pbean.getProdType()==0){
					desc = pbean.getGeneric_name()+"\n"+pbean.getCompany();
				}else{
					desc = pbean.getDesc();
				}
				item_description.setText(desc);
				tv_MrpValue.setText("Rs."+pbean.getMrp());
				okler_Amount.setText("OKLER AMOUNT"+"\n"+"Rs."+pbean.getOklerPrice());
				tv_youSaveValue.setText("Rs."+(pbean.getMrp()-pbean.getOklerPrice()));
				if(pbean.getPresc_needed()==1){
					prescriptionRL.setVisibility(View.VISIBLE);
				}
				//String First= "http://183.82.110.105:8081/oklerdevv2/uploads/images/medium/";
				String First="";
				if(pbean.getProdType()==0)
				First = prodArr.get(i).getClipArtUrl();
				else
				First= prodArr.get(i).getThumbUrl();
				String Second = prodArr.get(i).getImgUrl();
		 		String photo_url=First+Second;
		 		item_image.setImageUrl(photo_url, imgLoader);
				
				custview.addView(customProd[i]);
			mrp = mrp + (pbean.getMrp()*pbean.getUnits());
			okler_pr = okler_pr+ (pbean.getOklerPrice()*pbean.getUnits());
			tax = tax + (pbean.getTax()*pbean.getUnits());
			
			}
			disc = mrp - okler_pr;
			ship = Float.parseFloat(obean.getShipping_charges());
			net_pay = okler_pr+tax+ship;
			
			amount_value_tv.setText("Rs."+mrp);
			okler_disc_value_tv.setText("Rs."+disc);
			shipping_charg_value_tv.setText("Rs."+ship);
			tax_value_tv.setText("Rs."+tax);
			net_pay_value_tv.setText("Rs."+net_pay);
			
			
			AddressDataBean abean = new AddressDataBean();
			abean = obean.getAddressDataBean();
			
			String ship_addr = abean.getShip_fname()+ " " + abean.getShip_lname()+"\n"+abean.getShip_add1()+","
					+abean.getShip_add2()+"."+"\n"+abean.getShip_city()+" - "+abean.getShip_zip()+""+abean.getShip_state()
					+"."+ "\n" + "Preferred Delivery Time: "+ abean.getPreferred_del_time();
			addr_tv.setText(ship_addr);
			String bill_addr = abean.getBill_fname()+ " " + abean.getBill_lname()+"\n"+abean.getBill_add1()+","
					+abean.getBill_add2()+"."+"\n"+abean.getBill_city()+" - "+abean.getBill_zip()+""+abean.getBill_state()
					+".";
			addr_tv1.setText(bill_addr);
			
			
			/*for(int i = 0;i< prodArr.size(); i++)
			{
				item_name.setText(prodArr.get(i).getProdName());
				item_description.setText(prodArr.get(i).getDesc());
				tv_mrpvalue.setText(""+prodArr.get(i).getMrp());
				tv_youSaveValue.setText(""+prodArr.get(i).getDiscount());
				okler_amount.setText("Okler amount Rs"+ prodArr.get(i).getOklerPrice());
				
				addr_tv.setText(addarr.get(i).getFirstname()+ " "+addarr.get(i).getLastname()+"\n"+addarr.get(i).getShip_add1()+addarr.get(i).getShip_add2()+"\n"+addarr.get(i).getShip_city()+addarr.get(i).getShip_state()+addarr.get(i).getShip_zip());
				
				addr_tv1.setText(addarr.get(i).getFirstname()+ " "+addarr.get(i).getLastname()+"\n"+addarr.get(i).getBill_add1()+addarr.get(i).getBill_add2()+"\n"+addarr.get(i).getBill_city()+addarr.get(i).getBill_state()+addarr.get(i).getBill_zip());
				
				String cancel_order = obean.getOrderStatus();
				if(cancel_order.equals("Cancelled"))
				{
					cancel.setVisibility(View.GONE);
				}
				String First= "http://183.82.110.105:8081/oklerdevv2/uploads/images/medium/";
		 		String Second = prodArr.get(i).getImgUrl();
		 		String photo_url=First+Second;
		 		
		 		item_image.setImageUrl(photo_url, imgLoader);
		 		amount_value_tv.setText("Rs."+prodArr.get(i).getMrp());
		 		okler_disc_value_tv.setText("Rs."+prodArr.get(i).getDiscount());
		 		net_pay_value_tv.setText("Rs."+ prodArr.get(i).getOklerPrice());
			}*/
			
			break;

		default:
			break;
		}
		
		
		
		JSONObject prodBean = null;
		String pbean = getIntent().getStringExtra("pbean");
		try {
			prodBean = new JSONObject("pbean");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_order_med__hs__details, menu);
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
	
	public static void disableCancel()
	{
		cancel.setVisibility(View.GONE);
	}
}
