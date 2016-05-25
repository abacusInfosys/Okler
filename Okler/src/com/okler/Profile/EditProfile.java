package com.okler.Profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.okler.android.BaseActivity;
import com.okler.android.MyAccount;
import com.okler.android.OtpConfirmationActivity;
import com.okler.android.PrescriptionPreview;
import com.okler.android.ServiceCategoryActivity;
import com.okler.android.SignUp;
import com.okler.android.OtpConfirmationActivity.ResetpassAsyncTask;
import com.okler.databeans.PrescriptionImagesDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.dialogs.CameraGalleryDialog;
import com.okler.network.VolleyRequest;
import com.okler.utils.CameraGalleryImageInfo;
import com.okler.utils.Okler;
import com.okler.utils.RoundedImageView;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;
import com.okleruser.R.color;
import com.okleruser.R.id;
import com.okleruser.R.layout;

public class EditProfile extends BaseActivity implements OnClickListener {

	Toolbar toolBar;
	View bottomBarLayout;
	ImageView imgBack, imagepencil;

	TextView txtdob, textemail, textphone;
	EditText fname, lname;
	Calendar newDate, now;
	Button changePass, deactivate, submit;
	RoundedImageView profile_photo;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	UsersDataBean userBean;
	Bitmap iBitmap;
	ImageLoader imgLoader;
	String base64string;
	int uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);

		now = Calendar.getInstance();
		now.set(Calendar.HOUR, 00);
		now.set(Calendar.MINUTE, 00);
		now.set(Calendar.SECOND, 00);

		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);

		toolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		final ActionBar ab = getSupportActionBar();
		// ab.setDisplayHomeAsUpEnabled(true);
		toolBar.setBackgroundColor(getResources().getColor(R.color.Blue));

		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);

		textemail = (TextView) findViewById(R.id.tv_mail_value);
		textphone = (TextView) findViewById(R.id.tv_phone_value);
		fname = (EditText) findViewById(R.id.edtTxt_fname);
		lname = (EditText) findViewById(R.id.edtTxt_lname);
		txtdob = (TextView) findViewById(R.id.txtdob);
		imagepencil = (ImageView) findViewById(R.id.edit_pencil);
		profile_photo = (RoundedImageView) findViewById(R.id.NetworkImageView01);

		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(this);
		int userid = ubean.getId();
		String phone = ubean.getPhone();
		String email = ubean.getEmail();
		String firstname = ubean.getFname();
		String surname = ubean.getLname();
		String dob = ubean.getDob();

		if (dob.equals("null")) {
			txtdob.setHint("Date of Birth");
		} else {
			txtdob.setText(dob);
		}

		textemail.setText(email);
		textphone.setText(phone);
		fname.setText(firstname);
		lname.setText(surname);

		imgLoader = VolleyRequest.getInstance(this).getImageLoader();

		String First = ubean.getUserAvatarUrl();
		// "http://183.82.110.105:8081/oklerdevv2/uploads/user_avatar/";
		String Second = ubean.getUser_image();
		String photo_url = First + Second;

		profile_photo.setImageUrl(photo_url, imgLoader);

		UsersDataBean userbean = Utilities
				.getCurrentUserFromSharedPref(EditProfile.this);
		uid = userbean.getId();

		submit = (Button) findViewById(R.id.btnsubmit);
		submit.setTransformationMethod(null);
		changePass = (Button) findViewById(R.id.button_change_password);
		changePass.setTransformationMethod(null);
		deactivate = (Button) findViewById(R.id.button_sign_out);
		deactivate.setTransformationMethod(null);

		imagepencil.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				CameraGalleryDialog cameragallary = new CameraGalleryDialog(
						EditProfile.this);
				cameragallary.show();

			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String name = fname.getText().toString();
				String lastname = lname.getText().toString();
				String dob = txtdob.getText().toString();

				Gson gson = new Gson();
				nameValuePairs.add(new BasicNameValuePair("user_id", String
						.valueOf(uid)));
				nameValuePairs.add(new BasicNameValuePair("name", name));
				nameValuePairs
						.add(new BasicNameValuePair("lastname", lastname));
				nameValuePairs.add(new BasicNameValuePair("dob", dob));
				nameValuePairs.add(new BasicNameValuePair("imgtype", "jpeg"));
				nameValuePairs
						.add(new BasicNameValuePair("image", base64string));

				new EditInfo().execute("");

			}
		});
		changePass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent in = new Intent(getApplicationContext(),
						ProfileChangePassword.class);
				startActivity(in);
			}
		});
		deactivate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		txtdob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DatePickerDialog fromDatePickerDialog;
				DatePickerDialog toDatePickerDialog;
				final SimpleDateFormat dateFormatter;
				dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
				Calendar newCalendar = Calendar.getInstance();

				txtdob.setText("");
				fromDatePickerDialog = new DatePickerDialog(EditProfile.this,
						new OnDateSetListener() {

							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								newDate = Calendar.getInstance();
								newDate.set(year, monthOfYear, dayOfMonth);
								newDate.set(Calendar.HOUR, 00);
								newDate.set(Calendar.MINUTE, 00);
								newDate.set(Calendar.SECOND, 00);
								newDate.set(Calendar.MILLISECOND, 00);
								int a = newDate.compareTo(now);
								if ((newDate.compareTo(now) == 1)
										|| (newDate.compareTo(now) == 0)) {

									txtdob.setText("");
								} else {

									txtdob.setText(dateFormatter.format(newDate
											.getTime()));
								}
							}
						}, newCalendar.get(Calendar.YEAR), newCalendar
								.get(Calendar.MONTH), newCalendar
								.get(Calendar.DAY_OF_MONTH));
				fromDatePickerDialog.show();
			}
		});

		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Utilities.setTitleText(toolBar, "My Profile");
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);

		if (arg1 == RESULT_OK) {

			CameraGalleryImageInfo imgInfo = Utilities.getImageInfo(arg0, arg1,
					this, arg2);
			iBitmap = imgInfo.getImgBitmap();
			base64string = Utilities.convertImageToBase64(iBitmap);

			AlertDialog alertDialog = new AlertDialog.Builder(EditProfile.this)
					.create();
			alertDialog
					.setMessage("Do you want to keep this as your profile picture?");

			alertDialog.setButton("Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});

			alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					nameValuePairs.add(new BasicNameValuePair("user_id", String
							.valueOf(uid)));
					nameValuePairs
							.add(new BasicNameValuePair("imgtype", "jpeg"));
					nameValuePairs.add(new BasicNameValuePair("image",
							base64string));

					new EditInfo().execute("");

				}
			});

			alertDialog.show();

		}

	}

	/*
	 * @Override protected void onResume() { // TODO Auto-generated method stub
	 * super.onResume();
	 * 
	 * profile_photo.setImageBitmap(iBitmap);
	 * 
	 * 
	 * }
	 */

	public class EditInfo extends AsyncTask<String, Object, Object> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			System.out.println("in preexecute");
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = getString(R.string.registerUser);
			String Result = String.valueOf(Utilities.RegisterNewUser(url,
					nameValuePairs));
			return Result;

		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				System.out.println("in postexecute");
				JSONObject json = new JSONObject(result.toString());
				String messageReceived = json.getString("message");
				String respstatus = json.getString("status");
				JSONArray usrDet = json.getJSONArray("userdetails");
				JSONObject uDetObj = usrDet.getJSONObject(0);
				System.out.println("status" + respstatus);
				if (messageReceived.equals("User Update successful."))
				// if (respstatus == "1")
				{
					profile_photo.setImageBitmap(iBitmap);

					userBean = Utilities
							.getCurrentUserFromSharedPref(EditProfile.this);
					String fname = uDetObj.optString("firstname");
					String lname = uDetObj.optString("lastname");
					String dob = uDetObj.optString("dob");
					String user_image = uDetObj.optString("user_image");
					userBean.setFname(fname);
					userBean.setLname(lname);
					userBean.setDob(dob);
					userBean.setUser_image(user_image);
					Log.i("Fname", "Fname is" + fname + "User Bean fname:"
							+ userBean.getFname());
					Log.i("lname", "lname is" + lname + "User Bean lname:"
							+ userBean.getLname());
					Utilities.writeUserStatusEnumToSharedPref(EditProfile.this,
							UserStatusEnum.LOGGED_IN);
					Utilities.writeCurrentUserToSharedPref(EditProfile.this,
							userBean);
					Toast.makeText(getApplicationContext(),
							"Profile Updated Successfully", Toast.LENGTH_LONG)
							.show();

					Intent in = new Intent(EditProfile.this, MyAccount.class);
					in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(in);

				} else {

					Toast.makeText(getApplicationContext(), messageReceived,
							Toast.LENGTH_LONG).show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
