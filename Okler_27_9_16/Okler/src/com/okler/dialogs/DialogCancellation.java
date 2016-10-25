package com.okler.dialogs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.okler.android.MyOrderMed_Hs_Details;
import com.okleruser.R;
import com.okler.databeans.OrdersDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;

public class DialogCancellation extends Dialog implements
		android.view.View.OnClickListener {

	int id;
	String customer_name;
	String email;
	String order_id;
	String order_date;
	String reason;

	public Activity c;
	public Dialog d;
	String cancel_order;
	String uOrder="";

	TextView text_no_need, text_too_late, text_bought_myself, text_by_mistake,
			text_cancel;

	public DialogCancellation(Activity a,String uOrder) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
		this.uOrder=uOrder;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_reason_for_cancel);
		text_no_need = (TextView) findViewById(R.id.text_no_need);
		text_too_late = (TextView) findViewById(R.id.text_too_late);
		text_bought_myself = (TextView) findViewById(R.id.text_bought_myself);
		text_by_mistake = (TextView) findViewById(R.id.text_by_mistake);
		text_cancel = (TextView) findViewById(R.id.text_cancel);
		text_no_need.setOnClickListener(this);
		text_too_late.setOnClickListener(this);
		text_bought_myself.setOnClickListener(this);
		text_by_mistake.setOnClickListener(this);
		text_cancel.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		// ArrayList<OrdersDataBean> arr_odbean =
		// Okler.getInstance().getUsersOrders();
		OrdersDataBean obean = new OrdersDataBean();

		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(c);
		id = ubean.getId();
		customer_name = ubean.getFname();
		email = ubean.getEmail();

		int position = Okler.getInstance().getPostion();
		Gson gson=new Gson();
		obean = gson.fromJson(uOrder,OrdersDataBean.class);
				//Okler.getInstance().getUsersOrders().get(position);
		order_id = obean.getOrderId();
		order_date = obean.getDate();

		String cancel = c.getString(R.string.serverUrl)+c.getString(R.string.cancel_order)+c.getString(R.string.orderId)
				+ order_id + c.getString(R.string.cancel_order_diagno_custId) + id + c.getString(R.string.notes);
		WebJsonObjectRequest webjson;

		switch (v.getId()) {
		case R.id.text_no_need:

			// Toast.makeText(c, "text_no_need", Toast.LENGTH_LONG).show();
			reason = text_no_need.getText().toString();
			try {
				reason = URLEncoder.encode(reason, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String cancel_order;
			cancel_order = c.getResources().getString(R.string.serverUrl)
					+ c.getResources().getString(R.string.cancel_order)
					+ "order_id=" + order_id + "&cust_id=" + id + "&notes="
					+ reason;
			Log.i("delete url", cancel_order);
			webjson = new WebJsonObjectRequest(Method.GET, cancel_order,
					new JSONObject(), new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub

							try {
								JSONObject responseObj = (JSONObject) response;
								String message = responseObj
										.getString("message");
								if (message
										.equals("Your order has been cancelled")) {
									customer_name = URLEncoder.encode(
											customer_name, "UTF-8");

									String order_cancel = c.getResources()
											.getString(R.string.serverUrl)
											+ c.getResources().getString(
													R.string.order_cancel)
											+ "salutation="
											+ "&cust_id="
											+ id
											+ "&customer_name="
											+ customer_name
											+ "&email="
											+ email
											+ "&order_number="
											+ order_id
											+ "&reason="
											+ reason
											+ "&order_date=" + order_date;
									WebJsonObjectRequest webObjReq = new WebJsonObjectRequest(
											Method.GET,
											order_cancel,
											new JSONObject(),
											new Response.Listener<JSONObject>() {

												@Override
												public void onResponse(
														JSONObject response) {
													Log.i("email",
															"mail sent... ");

												}

											}, new Response.ErrorListener() {

												@Override
												public void onErrorResponse(
														VolleyError error) {

													Log.i("email",
															"mail not sent... ");

												}

											});

									VolleyRequest.addJsonObjectRequest(c,
											webObjReq);

									MyOrderMed_Hs_Details.disableCancel();
								}
								// Toast.makeText(c, message,
								// Toast.LENGTH_LONG).show();
							} catch (JSONException
									| UnsupportedEncodingException jsonEx) {
								Log.e("Exception json", jsonEx.getStackTrace()
										.toString());
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub

						}
					});

			VolleyRequest.addJsonObjectRequest(c, webjson);
			dismiss();

			break;
		case R.id.text_too_late:

			reason = text_too_late.getText().toString();
			try {
				reason = URLEncoder.encode(reason, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cancel_order = c.getResources().getString(R.string.serverUrl)
					+ c.getResources().getString(R.string.cancel_order)
					+ "order_id=" + order_id + "&cust_id=" + id + "&notes="
					+ reason;
			webjson = new WebJsonObjectRequest(Method.GET, cancel_order,
					new JSONObject(), new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub

							try {
								JSONObject responseObj = (JSONObject) response;
								String message = responseObj
										.getString("message");
								if (message
										.equals("Your order has been cancelled")) {
									customer_name = URLEncoder.encode(
											customer_name, "UTF-8");

									String order_cancel = c.getResources()
											.getString(R.string.serverUrl)
											+ c.getResources().getString(
													R.string.order_cancel)
											+ "salutation="
											+ "&cust_id="
											+ id
											+ "&customer_name="
											+ customer_name
											+ "&email="
											+ email
											+ "&order_number="
											+ order_id
											+ "&reason="
											+ reason
											+ "&order_date=" + order_date;

									WebJsonObjectRequest webObjReq = new WebJsonObjectRequest(
											Method.GET,
											order_cancel,
											new JSONObject(),
											new Response.Listener<JSONObject>() {

												@Override
												public void onResponse(
														JSONObject response) {
													Log.i("email",
															"mail sent... ");

												}

											}, new Response.ErrorListener() {

												@Override
												public void onErrorResponse(
														VolleyError error) {

													Log.i("email",
															"mail not sent... ");

												}

											});

									VolleyRequest.addJsonObjectRequest(c,
											webObjReq);
									MyOrderMed_Hs_Details.disableCancel();
								}
								// Toast.makeText(c, message,
								// Toast.LENGTH_LONG).show();

							} catch (JSONException jsonEx) {
								Log.e("Exception json", jsonEx.getStackTrace()
										.toString());
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub

						}
					});

			VolleyRequest.addJsonObjectRequest(c, webjson);
			dismiss();
			break;

		case R.id.text_bought_myself:

			reason = text_bought_myself.getText().toString();
			try {
				reason = URLEncoder.encode(reason, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cancel_order = c.getResources().getString(R.string.serverUrl)
					+ c.getResources().getString(R.string.cancel_order)
					+ "order_id=" + order_id + "&cust_id=" + id + "&notes="
					+ reason;
			webjson = new WebJsonObjectRequest(Method.GET, cancel_order,
					new JSONObject(), new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub

							try {
								JSONObject responseObj = (JSONObject) response;
								String message = responseObj
										.getString("message");
								if (message
										.equals("Your order has been cancelled")) {
									customer_name = URLEncoder.encode(
											customer_name, "UTF-8");

									String order_cancel = c.getResources()
											.getString(R.string.serverUrl)
											+ c.getResources().getString(
													R.string.order_cancel)
											+ "salutation="
											+ "&cust_id="
											+ id
											+ "&customer_name="
											+ customer_name
											+ "&email="
											+ email
											+ "&order_number="
											+ order_id
											+ "&reason="
											+ reason
											+ "&order_date=" + order_date;

									WebJsonObjectRequest webObjReq = new WebJsonObjectRequest(
											Method.GET,
											order_cancel,
											new JSONObject(),
											new Response.Listener<JSONObject>() {

												@Override
												public void onResponse(
														JSONObject response) {
													Log.i("email",
															"mail sent... ");

												}

											}, new Response.ErrorListener() {

												@Override
												public void onErrorResponse(
														VolleyError error) {

													Log.i("email",
															"mail not sent... ");

												}

											});

									VolleyRequest.addJsonObjectRequest(c,
											webObjReq);

									MyOrderMed_Hs_Details.disableCancel();
								}
								// Toast.makeText(c, message,
								// Toast.LENGTH_LONG).show();

							} catch (JSONException jsonEx) {
								Log.e("Exception json", jsonEx.getStackTrace()
										.toString());
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub

						}
					});

			VolleyRequest.addJsonObjectRequest(c, webjson);
			dismiss();
			// c.finish();
			break;

		case R.id.text_by_mistake:

			reason = text_by_mistake.getText().toString();
			try {
				reason = URLEncoder.encode(reason, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cancel_order = c.getResources().getString(R.string.serverUrl)
					+ c.getResources().getString(R.string.cancel_order)
					+ "order_id=" + order_id + "&cust_id=" + id + "&notes="
					+ reason;
			webjson = new WebJsonObjectRequest(Method.GET, cancel_order,
					new JSONObject(), new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub

							try {
								JSONObject responseObj = (JSONObject) response;
								String message = responseObj
										.getString("message");
								if (message
										.equals("Your order has been cancelled")) {
									customer_name = URLEncoder.encode(
											customer_name, "UTF-8");

									String order_cancel = c.getResources()
											.getString(R.string.serverUrl)
											+ c.getResources().getString(
													R.string.order_cancel)
											+ "salutation="
											+ "&cust_id="
											+ id
											+ "&customer_name="
											+ customer_name
											+ "&email="
											+ email
											+ "&order_number="
											+ order_id
											+ "&reason="
											+ reason
											+ "&order_date=" + order_date;

									WebJsonObjectRequest webObjReq = new WebJsonObjectRequest(
											Method.GET,
											order_cancel,
											new JSONObject(),
											new Response.Listener<JSONObject>() {

												@Override
												public void onResponse(
														JSONObject response) {
													Log.i("email",
															"mail sent... ");

												}

											}, new Response.ErrorListener() {

												@Override
												public void onErrorResponse(
														VolleyError error) {

													Log.i("email",
															"mail not sent... ");

												}

											});

									VolleyRequest.addJsonObjectRequest(c,
											webObjReq);

									MyOrderMed_Hs_Details.disableCancel();
								}
								// Toast.makeText(c, message,
								// Toast.LENGTH_LONG).show();

							} catch (JSONException jsonEx) {
								Log.e("Exception json", jsonEx.getStackTrace()
										.toString());
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub

						}
					});

			VolleyRequest.addJsonObjectRequest(c, webjson);
			dismiss();
			break;

		case R.id.text_cancel:
			// Toast.makeText(c, "Dialog Dismissed", Toast.LENGTH_LONG).show();
			dismiss();
			break;
		/*
		 * case R.id.camera_image: Utilities.captureImageFromCamera(c); break;
		 * 
		 * case R.id.gallery_image: Utilities.showImageFromGallery(c); break;
		 * default: break; } } dismiss();
		 */
		}
	}
}
