package com.okler.dialogs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.okler.android.MedicalServices;
import com.okler.android.MyAccount;
import com.okler.android.MyCart;
import com.okler.android.NewSignIn;
import com.okler.android.NotificationActivity;
import com.okler.android.Physiotherapy;
import com.okler.android.ProductDetailsActivity;
import com.okler.android.ServiceCategoryActivity;
import com.okler.android.UploadPrescription;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.diagnostics.EnterPatientInfoActivity;
import com.okler.diagnostics.SelectPatientInfoActivity;
import com.okler.helpnsupport.SupportHelpActivity;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePasswordDialog extends DialogFragment implements android.view.View.OnClickListener {
	
	
	public Activity c;
	public Dialog d;
	EditText edtTxt_old_password, edtTxt_new_password, edtTxt_retype_password;
	int userid;
	boolean flag;
	private EditText editTextToValidate;
	TextValidations text;
	String old_pass, new_pass, retype_pass;
	Button btn_save;
	LinearLayout progressLinLayout;

	public ChangePasswordDialog(Activity a) {
		
		// TODO Auto-generated constructor stub
		
		this.c = a;
	}
	
	/*@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_change_password);
		
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(c);
		userid = ubean.getId();
		String phone = ubean.getPhone();
		String email = ubean.getEmail();
		edtTxt_old_password = (EditText) findViewById(R.id.edtTxt_old_password);
		edtTxt_new_password = (EditText) findViewById(R.id.edtTxt_new_password);
		edtTxt_retype_password = (EditText) findViewById(R.id.edtTxt_retype_password);
		
		btn_save = (Button) findViewById(R.id.button_save);
		
		btn_save.setOnClickListener(this);		
	}*/

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder build = new AlertDialog.Builder(c);
		View view = c.getLayoutInflater().inflate(R.layout.dialog_change_password, null);
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(c);
		userid = ubean.getId();
		String phone = ubean.getPhone();
		String email = ubean.getEmail();
		edtTxt_old_password = (EditText) view.findViewById(R.id.edtTxt_old_password);
		edtTxt_new_password = (EditText) view.findViewById(R.id.edtTxt_new_password);
		edtTxt_retype_password = (EditText) view.findViewById(R.id.edtTxt_retype_password);
		btn_save = (Button) view.findViewById(R.id.button_save);
		progressLinLayout = (LinearLayout)view.findViewById(R.id.progressLinLayout);
		btn_save.setOnClickListener(this);		
		build.setView(view);
		return build.show();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		

		// TODO Auto-generated method stub

		TextValidations tval;
		flag = false;
		tval = new TextValidations(edtTxt_old_password);
		if (tval.validatePassword("Please enter old password")) {
			flag = false;
			tval = new TextValidations(edtTxt_new_password);
			if (tval.validatePassword(
					"Please enter new password")) {
				flag = false;
				tval = new TextValidations(edtTxt_retype_password);
				if (tval.validatePassword("Please retype new password")) {
					flag = false;
					old_pass = edtTxt_old_password.getText().toString();
					new_pass = edtTxt_new_password.getText().toString();
					retype_pass = edtTxt_retype_password.getText()
							.toString();
					if (!new_pass.equals(retype_pass)) {
						edtTxt_retype_password.setText("");
						edtTxt_retype_password
								.setError("Password mismatch");
						flag = false;
					} else {
						if (old_pass.equals(new_pass)
								|| old_pass.equals(retype_pass)) {
							edtTxt_new_password.setText("");
							edtTxt_retype_password.setText("");
							edtTxt_new_password
									.setError("Old and new password should not be equal");
							edtTxt_new_password.requestFocus();
							flag = false;
						} else {
							flag = true;
						}
					}
				}
			}
		}

		if (flag) {
			showProgress(true);
			String newPwd1 = "", oldPwd1 = "", p = "";

			try {
				p = URLEncoder.encode("", "UTF-8");
				if (new_pass == null)
					new_pass = "";
				newPwd1 = URLEncoder.encode(new_pass, "UTF-8");
				if (old_pass == null)
					old_pass = "";
				oldPwd1 = URLEncoder.encode(old_pass, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (flag) {
				String change_pass = c.getString(R.string.change_password_url)
						+ userid
						+ "&newpwd="
						+ newPwd1
						+ "&oldpwd="
						+ oldPwd1;
				Log.e("URL", change_pass);
				WebJsonObjectRequest webjson = new WebJsonObjectRequest(
						Method.GET, change_pass, new JSONObject(),
						new Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								// TODO Auto-generated method stub

								try {
									JSONObject responseObj = (JSONObject) response;
									String result = responseObj
											.getString("result");
									String message = responseObj
											.getString("message");
									/*Toast.makeText(
											c,
											message, Toast.LENGTH_LONG)
											.show();*/
									if (message
											.equals("password changed successfully")) {
										Utilities.writeUserStatusToSharedPref(c,
												UserStatusEnum.LOGGED_IN);
										showProgress(false);
										Toast.makeText(c,
												"Logged in Successfully", Toast.LENGTH_LONG).show();
									navigation();	
										dismiss();
									}else if(message.equals("Old password does not match. Please provide the correct password.")){
										Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
									}

								} catch (JSONException jsonEx) {
									showProgress(false);
									Log.e("Exception json", jsonEx
											.getStackTrace().toString());
								}

							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(
									VolleyError error) {
								showProgress(false);
								Log.i("error", String.valueOf(error));
								// TODO Auto-generated method stub

							}
						});

				VolleyRequest.addJsonObjectRequest(
						c, webjson);
			} else {

			}
		}
		// }
	
		
	}
	
	public boolean validateOldPassword() {
		this.editTextToValidate = edtTxt_old_password;
		text = new TextValidations(editTextToValidate);
		return text.validateText("Please enter password");
	}

	public boolean validateNewPassword() {
		this.editTextToValidate = edtTxt_new_password;
		text = new TextValidations(editTextToValidate);
		return text.validateText("Please enter password");
	}

	public boolean validateRetypePassword() {
		this.editTextToValidate = edtTxt_retype_password;
		text = new TextValidations(editTextToValidate);
		if (new_pass.equals(retype_pass)) {
			return true;
		} else {
			text.validateText("password mismatch");
			return false;
		}

	}
	
	private void navigation() {
		switch (Okler.getInstance().getBookingType()) {
		case 0: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			/*
			 * Intent success=new
			 * Intent(NewSignIn.this,ProductCheckoutListView.class);
			 * startActivity(success);
			 */
			c.finish();
			break;
		}
		case 1: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			/*
			 * Intent success=new
			 * Intent(NewSignIn.this,ProductCheckoutListView.class);
			 * startActivity(success);
			 */
			c.finish();
			break;

		}
		case 3: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			/*
			 * Intent success=new
			 * Intent(NewSignIn.this,ProductCheckoutListView.class);
			 * startActivity(success);
			 */
			c.finish();
			break;

		}
		case 4: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			/*
			 * Intent success=new
			 * Intent(NewSignIn.this,ProductCheckoutListView.class);
			 * startActivity(success);
			 */
			c.finish();
			break;

		}
		case 9: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(c);
			ArrayList<AddressDataBean> paList = new ArrayList<AddressDataBean>();
			paList = ubean.getPatAddList();
			if (paList.size() <= 0) {
				Intent intent = new Intent(c,
						EnterPatientInfoActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(c,
						SelectPatientInfoActivity.class);
				startActivity(intent);
			}
			break;

		}
		case 11: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(c,
					UploadPrescription.class);
			startActivity(success);
			c.finish();
			break;
		}
		case 12: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(c, MyAccount.class);
			startActivity(success);
			c.finish();
			break;
		}
		case 100: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(c,
					ServiceCategoryActivity.class);
			Okler.getInstance().setBookingType(100);
			startActivity(success);
			c.finish();
			break;
		}
		case 13: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(c, Physiotherapy.class);
			Okler.getInstance().setBookingType(100);
			startActivity(success);
			c.finish();
			break;
		}
		case 14: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(c, MedicalServices.class);
			Okler.getInstance().setBookingType(100);
			startActivity(success);
			c.finish();
			break;
		}

		case 18: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(c,
					SupportHelpActivity.class);
			Okler.getInstance().setBookingType(100);
			startActivity(success);
			c.finish();
			break;
		}

		case 19: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(c, MyCart.class);
			Okler.getInstance().setBookingType(100);
			startActivity(success);
			c.finish();
			break;
		}
		case 20: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(c);
			ArrayList<AddressDataBean> paList = new ArrayList<AddressDataBean>();
			paList = ubean.getPatAddList();
			if (paList.size() <= 0) {
				Intent intent = new Intent(c,
						EnterPatientInfoActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(c,
						SelectPatientInfoActivity.class);
				startActivity(intent);
			}
			break;
		}
		case 21: {
			Log.i("bookin type",
					toString().valueOf(Okler.getInstance().getBookingType()));
			Intent success = new Intent(c,
					NotificationActivity.class);
			Okler.getInstance().setBookingType(100);
			startActivity(success);
			c.finish();
			break;
		}
		case 22: {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			Uri Uri = null;
			intent.setData(Uri.parse("market://details?id=com.okleruser"));// 11_01_2016
																			// Gitesh
			if (!MyStartActivity(intent)) {
				// Market (Google play) app seems not installed, let's try to
				// open a webbrowser
				intent.setData(Uri
						.parse("https://play.google.com/store/apps/details?id=com.okleruser"));// 11_01_2016
																								// Gitesh
				if (!MyStartActivity(intent)) {
					// Well if this also fails, we have run out of options,
					// inform the user.
					// Toast.makeText(this,
					// "Could not open Android market, please install the market app.",
					// Toast.LENGTH_SHORT).show();
				}
			}
			c.finish();
			/*
			 * Log.i("bookin type",toString().valueOf(Okler.getInstance().
			 * getBookingType())); Intent success=new
			 * Intent(NewSignIn.this,NotificationActivity.class);
			 * Okler.getInstance().setBookingType(100); startActivity(success);
			 * finish();
			 */
			break;
		}
		case 23: {
			Intent startUpPresc = new Intent(c,UploadPrescription.class);
			startUpPresc.putExtra("isMedPres", false);
			Okler.getInstance().setBookingType(UIUtils.getBookingType("Diagnostic"));
			startActivity(startUpPresc);
			c.finish();
			break;
		}
		case 24:
		{
			Intent startFav = new Intent(c,ProductDetailsActivity.class);
			startFav.putExtra("isMedPres", false);
			Okler.getInstance().setBookingType(UIUtils.getBookingType("Favorites"));
			startActivity(startFav);
			c.finish();
		}
		

		}

	}
	
	private boolean MyStartActivity(Intent aIntent) {
		try {
			startActivity(aIntent);
			return true;
		} catch (ActivityNotFoundException e) {
			return false;
		}
	}
	public void showProgress(boolean paramBoolean) {
		if (paramBoolean) {
			progressLinLayout.setVisibility(View.VISIBLE);
			return;
		}
		progressLinLayout.setVisibility(View.INVISIBLE);
	}
}
