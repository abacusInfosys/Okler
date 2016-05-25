package com.okler.dialogs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.Utilities;

public class PhysiomedDialog extends Dialog implements
		android.view.View.OnClickListener {
	public Activity c;
	public Dialog d;

	private EditText editTextToValidate;
	TextView text_yes;
	TextValidations text;
	EditText edt_reason;
	ArrayList<OrdersDataBean> odtBeanArr;
	ListView myOrderList;
	String order_id, custId, cancel_order;
	int bookingOrderType;

	int uid;
	String customer_name;
	String email;
	String reason;

	public PhysiomedDialog(Activity a, String cid, String oid, int bookType) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
		custId = cid;
		order_id = oid;
		bookingOrderType = bookType;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_cancel_order_physiomed);
		text_yes = (TextView) findViewById(R.id.text_yes);
		edt_reason = (EditText) findViewById(R.id.edt_reason);

		text_yes.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		boolean flag = validatePassword();
		if (flag == true) {

			UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(c);
			uid = ubean.getId();
			customer_name = ubean.getFname();
			email = ubean.getEmail();
			reason = edt_reason.getText().toString();

			String cancel_order;
			/*
			 * if(bookingOrderType == 2 || bookingOrderType == 4) {
			 */
			cancel_order = c.getString(R.string.serverUrl)+c.getString(R.string.cancel_service)+"id="
					+ order_id
					+ c.getString(R.string.cancel_order_diagno_custId)
					+ uid
					+ c.getString(R.string.service_id)
					+ bookingOrderType + "&notes=" + reason;
			/*
			 * } else cancel_order =
			 * "http://183.82.110.105:8081/oklerapi/order/delete?order_id="
			 * +order_id+"&cust_id="+uid+"&notes="+reason;
			 */
			WebJsonObjectRequest webjson = new WebJsonObjectRequest(Method.GET,
					cancel_order, new JSONObject(), new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub

							try {
								JSONObject responseObj = (JSONObject) response;
								String message = responseObj
										.getString("message");
								String strToCompare;
								// if(bookingOrderType == 2 || bookingOrderType
								// == 4)
								strToCompare = "Your service has been cancelled";
								/*
								 * else strToCompare =
								 * "Your order has been cancelled";
								 */
								if (message.equals(strToCompare)) {
									// salutation=Mr&cust_id=1&customer_name=Mark&email=testmail@gmail.com&order_number=1043&reason=aaaa&required_type=4
									customer_name = URLEncoder.encode(
											customer_name, "UTF-8");
									reason = URLEncoder.encode(reason, "UTF-8");

									String service_cancel = c.getResources()
											.getString(R.string.serverUrl)
											+ c.getResources().getString(
													R.string.service_cancel)
											+ "salutation="
											+ "&cust_id="
											+ uid
											+ "&customer_name="
											+ customer_name
											+ "&email="
											+ email
											+ "&order_number="
											+ order_id
											+ "&reason="
											+ reason
											+ "&&required_type="
											+ bookingOrderType;

									WebJsonObjectRequest webObjReq = new WebJsonObjectRequest(
											Method.GET,
											service_cancel,
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

													// Log.i("Error", new
													// String(error.networkResponse.data));

													Log.i("email",
															"mail not sent... ");

												}

											}, true);

									VolleyRequest.addJsonObjectRequest(c,
											webObjReq);

									// Toast.makeText(c,
									// "Your order is cancelled.",
									// Toast.LENGTH_LONG).show();
									MyOrderMS_History.disableCancel();
								}
								Toast.makeText(c, message, Toast.LENGTH_LONG)
										.show();

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
							Log.e("Cancel orer", "order cant be cancelled");
						}
					}, true);

			if (VolleyRequest.addJsonObjectRequest(c, webjson))
				Log.e("WS called", "ddd");
			else
				Log.e("WS not called", "ddd gg");

			dismiss();
		}// button

	}

	public boolean validatePassword() {
		this.editTextToValidate = edt_reason;
		text = new TextValidations(editTextToValidate);
		return text.validateText("Please Enter reason");
	}
}
