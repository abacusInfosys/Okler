package com.okler.dialogs;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.okler.adapters.MyOrdersAdapter;
import com.okler.android.MedicalServices;

import com.okler.android.MyOrderMS_History;
import com.okler.android.MyOrderMed_Hs_Details;
import com.okleruser.R;
import com.okler.databeans.OrdersDataBean;
import com.okler.databeans.PhysioAndMedicalBean;
import com.okler.databeans.UsersDataBean;
import com.okler.diagnostics.DiagnoOrderSummary;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.Utilities;

public class DiagnoOrderCancellation extends Dialog implements android.view.View.OnClickListener {
	public Activity c;
	  public Dialog d;
	 
	  private EditText editTextToValidate;
	  TextView text_yes,textTitle;
	  TextValidations text ;
	  EditText edt_reason;
	  ArrayList<OrdersDataBean> odtBeanArr;
	  ListView myOrderList;
	  String order_id,custId, cancel_order;
	  TextView cancelBtn;
	  public DiagnoOrderCancellation(Activity a,String cid,String oid) {
			super(a);
			// TODO Auto-generated constructor stub
			this.c = a;
			custId = cid;
			order_id = oid;
			
		}
	  
	  
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.dialog_cancel_order_physiomed);
	    text_yes = (TextView) findViewById(R.id.text_yes);
	    edt_reason = (EditText) findViewById(R.id.edt_reason);	    
	    text_yes.setOnClickListener(this);	    
	    textTitle = (TextView)findViewById(R.id.textView1);
	    textTitle.setText(c.getString(R.string.diagnoOrdercancelDlgTitle));
	  }

	  @Override
	  public void onClick(View v) {
	    	
	    	boolean flag = validatePassword();
	    	if(flag == true)
	    	{
	    	
	    	UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(c);
	    	int uid = ubean.getId();
	    	String reason = edt_reason.getText().toString();
	    	
	    /*	int position = Okler.getInstance().getPostion();
			  OrdersDataBean obean = Okler.getInstance().getUsersOrders().get(position);
			  String order_id = obean.getOrderId();*/
	    	
	    	String cancel_order_diagno_id;
	    	/*if(bookingOrderType == 2 || bookingOrderType == 4)
	    	{*/
	    	cancel_order_diagno_id =c.getString(R.string.cancel_order_diagno_id)+order_id+
	    			c.getString(R.string.cancel_order_diagno_custId)+uid+
	    			c.getString(R.string.cancel_order_diagno_reason)+reason;
	   /* 	}
	    	else
	    		cancel_order = "https://www.okler.com/api/order/delete?order_id="+order_id+"&cust_id="+uid+"&notes="+reason;
	    */	
	    	WebJsonObjectRequest webjson=new WebJsonObjectRequest(Method.GET, cancel_order_diagno_id, new JSONObject(),new Listener<JSONObject>() 
					{
						@Override
						public void onResponse(JSONObject response) 
						{
							// TODO Auto-generated method stub
							
							try
							{
							JSONObject responseObj =(JSONObject)response;
							String message = responseObj.getString("message");
							String strToCompare;
			//				if(bookingOrderType == 2 || bookingOrderType == 4)
								strToCompare="deletion process success at lab booking";
				/*			else
								strToCompare = "Your order has been cancelled";*/
							if(message.equals(strToCompare))
							{
								Toast.makeText(c, "Your order is cancelled.", Toast.LENGTH_LONG).show();
								DiagnoOrderSummary.btnDisable();
							}
							//Toast.makeText(c, message, Toast.LENGTH_LONG).show();
							
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
								Log.e("Cancel orer", "order cant be cancelled");	
							}
						}
			);
				
			if(VolleyRequest.addJsonObjectRequest(c,webjson))
				Log.e("WS called", "ddd");
			else
				Log.e("WS not called", "ddd gg");
			
		/*	UsersDataBean ubean1 = Utilities.getCurrentUserFromSharedPref(c);
		       int cust_id= ubean.getId();
		       String myOrderUrl = "";
		       WebJsonObjectRequest alJson = new WebJsonObjectRequest(Method.GET, myOrderUrl, new JSONObject(), new Response.Listener<JSONObject>() {
		    		
					@Override
					public void onResponse(JSONObject response) {
						
						JSONObject ajobj = (JSONObject)response;
						JSONArray ajobj1 = new JSONArray();
						JSONObject ajobj2 = new JSONObject();
						JSONObject ajobj3 = new JSONObject();
						JSONArray ajArray = new JSONArray();		
						try {							
							if(ajobj.getString("message").equals("No data found."))
							{
								Toast.makeText(c, "You dont have any order ...", Toast.LENGTH_LONG).show();
							}
							else
							{
								ajobj1 = ajobj.getJSONArray("result");
						
						
							odtBeanArr = new ArrayList<OrdersDataBean>();
							for (int ai = 0; ai < ((ajobj1.length())); ai++) 
							{
								Log.i("in loop", "in loop");
								
								ajobj2 = ajobj1.getJSONObject(ai);
								
								OrdersDataBean odtBean = new OrdersDataBean();
								
								odtBean = new OrdersDataBean();
							    odtBean.setDate(ajobj2.optString("start_date"));
							    odtBean.setOrderId(ajobj2.optString("booking_id"));
							    odtBean.setOrderStatus(ajobj2.optString("booking_status"));
							    
							    //   odtBean.setPrice(ajobj2.optString("total")); 
							       
							       odtBeanArr.add(odtBean);
							}
							}					
						}
						catch(Exception e) {
							// TODO Auto-generated catch block
							String resp2 = String.valueOf(e);
						//	Toast.makeText(getApplicationContext(), resp2, Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}
				
						
						 MyOrdersAdapter ordAdapter = new MyOrdersAdapter(c,odtBeanArr);
						
					       myOrderList.setAdapter(ordAdapter);
					       
						Okler.getInstance().setUsersOrders(odtBeanArr);
						
					}
		       }, new Response.ErrorListener() {
		    	   @Override
					public void onErrorResponse(VolleyError error) {
						Log.e("aa", "mmm");
					}
				});
		       
		       
		       //put that response in odtBean
		      
		   	if(VolleyRequest.addJsonObjectRequest(c, alJson))
			{
	       		
				Toast.makeText(c, "got response", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(c, "value not found", Toast.LENGTH_LONG).show();
			}
			*/
	      dismiss();
	    	}// button
	      
	     
	    	
	  }
	  
		public boolean validatePassword()
		{
			this.editTextToValidate = edt_reason;
			text = new TextValidations(editTextToValidate);
			return text.validateText("Please Enter reason");
		}
}