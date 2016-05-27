package com.okler.android;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.google.gson.Gson;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.PrescriptionImagesDataBean;
import com.okler.databeans.PrescriptionsDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.dialogs.CameraGalleryDialog;
import com.okler.utils.CameraGalleryImageInfo;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AlloUpPrescActivity extends BaseActivity {

	String imageFilePath, imageFileName;
	View bottomBarLayout;
	Toolbar toolBar;
	Bitmap imgBitmap;
	Button notifCount;
	ImageView imgBack, imgPreview;
	Button upload, btnCancel, btnUpload;
	LinearLayout layout;
	PrescriptionsDataBean prescriptionsDataBean;
	String[] imagesBase64, imageTypes;
	String uploadPrescrUrl;
	CartDataBean cdbean;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	TextView checkout_Tv, choose_from_existing;
	int check;
	int totalCnt;
	Activity ack;
	UsersDataBean ubean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allo_up_presc);
		prescriptionsDataBean = Okler.getInstance().getPrescriptionsDataBeans();
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		notifCount = (Button) toolBar.findViewById(R.id.notif_count);
		check = getIntent().getIntExtra("Check", 11);
		setSupportActionBar(toolBar);
		ack = this;
		ActionBar ab = getSupportActionBar();
		ubean = new UsersDataBean();
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		// ab.setDisplayHomeAsUpEnabled(true);
		Utilities.setTitleText(toolBar, getString(R.string.allopathy));
		// ab.setTitle(R.string.title_activity_allopathy);
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_medicine);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		checkout_Tv = (TextView) findViewById(R.id.checkout_Tv);
/*Testing*/
		uploadPrescrUrl = getString(R.string.upload_pres);
		choose_from_existing = (TextView) findViewById(R.id.choose_from_existing);
		choose_from_existing.setVisibility(View.INVISIBLE);

		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Okler.getInstance().getPrescriptionsDataBeans().getPresImages()
						.clear();
				finish();
			}
		});
		if (check == 0) {
			toolBar.setBackgroundColor(Color.BLUE);
			checkout_Tv.setBackgroundColor(Color.BLUE);
			Utilities.setTitleText(toolBar, "Cart");
		}
		View view = findViewById(R.id.frag_preview);

		imgPreview = (ImageView) view.findViewById(R.id.selected_image);
		// thumbnail = (ImageView) view.findViewById(R.id.thumbnail_image);

		upload = (Button) findViewById(R.id.upload);

		if (getIntent().getExtras().get("imageFilePath") != null) {
			imageFilePath = getIntent().getExtras().get("imageFilePath")
					.toString();
			if (getIntent().getExtras().get("imgFileName") != null) {
				imageFileName = getIntent().getExtras().get("imgFileName")
						.toString();
				imageFilePath = imageFilePath + "/" + imageFileName;// 06_01_2016
																	// Gitesh
			}
		}
		// flag =getIntent().getBooleanExtra("flag",false);
		// Create a bundle and put bitmap in compressed format in a bundle
		Bundle bundle = new Bundle();
		imageFileName = null;
		imgBitmap = Utilities.getImageBitmapFromStorage(imageFilePath,
				imageFileName);
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		imgBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bs);
		bundle.putByteArray("image", bs.toByteArray());

		imgPreview.setImageBitmap(imgBitmap);

		upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int size = prescriptionsDataBean.getPresImages().size();
				imagesBase64 = new String[prescriptionsDataBean.getPresImages()
						.size()];
				imageTypes = new String[prescriptionsDataBean.getPresImages()
						.size()];
				for (int i = 0; i < size; i++) {
					imagesBase64[i] = prescriptionsDataBean.getPresImages()
							.get(i).getBase64ConvrtedImg();
					imageTypes[i] = "jpeg";
				}
				Gson gson = new Gson();
				String base64 = gson.toJson(imagesBase64);
				String imgTypes = gson.toJson(imageTypes);
				// to change user id later on
				// nameValuePairs.add(new BasicNameValuePair("patient_name",
				// prescriptionsDataBean.getPatientName()));
				nameValuePairs
						.add(new BasicNameValuePair("required_type", "1"));
				nameValuePairs.add(new BasicNameValuePair("phoneno", ubean
						.getPhone().toString()));
				for (int imgCount = 0; imgCount < prescriptionsDataBean
						.getPresImages().size(); imgCount++) {
					String base641 = imagesBase64[imgCount];
					String tp = imageTypes[imgCount];
					nameValuePairs.add(new BasicNameValuePair("image["
							+ imgCount + "]", imagesBase64[imgCount]));
					nameValuePairs.add(new BasicNameValuePair("imgtype["
							+ imgCount + "]", imageTypes[imgCount]));
				}

				nameValuePairs.add(new BasicNameValuePair("user_id", ""
						+ ubean.getId()));
				nameValuePairs.add(new BasicNameValuePair("service_type", "2"));

				String str = "";// gson.toJson(postParams);
				new UploadPrescAsyncTask().execute(str);
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Okler.getInstance().getPrescriptionsDataBeans().getPresImages().clear();
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(notifCount, ack);
		layout = (LinearLayout) findViewById(R.id.image_layout);
		totalCnt = Okler.getInstance().getPrescriptionsDataBeans()
				.getPresImages().size();
		ImageView[] imageViews = new ImageView[totalCnt + 1];
		layout.removeAllViews();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150, 150);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp2.height = 100;
		lp2.width = 100;
		lp2.gravity = Gravity.CENTER_VERTICAL;
		lp.setMargins(5, 5, 5, 5);

		for (int i = 0; i < totalCnt; i++) {
			imageViews[i] = new ImageView(getApplicationContext());
			imageViews[i].setImageBitmap(Okler.getInstance()
					.getPrescriptionsDataBeans().getPresImages().get(i)
					.getPrescImages());
			layout.addView(imageViews[i]);
			imageViews[i].setLayoutParams(lp);
		}
		if (totalCnt <= 0) {

		} else {
			imgPreview.setImageBitmap(Okler.getInstance()
					.getPrescriptionsDataBeans().getPresImages()
					.get(totalCnt - 1).getPrescImages());
		}
		imageViews[totalCnt] = new ImageView(getApplicationContext());
		imageViews[totalCnt].setImageResource(R.drawable.camera);
		imageViews[totalCnt].setLayoutParams(lp2);
		layout.addView(imageViews[totalCnt]);

		imageViews[totalCnt].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (totalCnt < 4) {
					CameraGalleryDialog cdd = new CameraGalleryDialog(ack);
					cdd.show();
				} else {
					Toast.makeText(getApplicationContext(),
							"You can only add 4 prescriptions at a time",
							Toast.LENGTH_LONG).show();
				}

			}
		});
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			CameraGalleryImageInfo imgInfo = Utilities.getImageInfo(
					requestCode, resultCode, this, data);
			PrescriptionImagesDataBean pImgBean = new PrescriptionImagesDataBean();
			Bitmap iBitmap = imgInfo.getImgBitmap();

			String base64string = Utilities.convertImageToBase64(iBitmap);
			pImgBean.setPrescImages(iBitmap);
			pImgBean.setBase64ConvrtedImg(base64string);
			String imgPa = imgInfo.getUri();
			pImgBean.setImgUri(imgPa);
			int size = Okler.getInstance().getPrescriptionsDataBeans()
					.getPresImages().size();
			boolean imgFound = false;
			if (size != 0) {
				for (int c = 0; c < size; c++) {
					String oldImgPath = Okler.getInstance()
							.getPrescriptionsDataBeans().getPresImages().get(c)
							.getImgUri();
					if (oldImgPath.equals(pImgBean.getImgUri()))
						imgFound = true;
				}
			}

			if (!imgFound)
				Okler.getInstance().getPrescriptionsDataBeans().getPresImages()
						.add(pImgBean);
		}
	}

	public class UploadPrescAsyncTask extends AsyncTask<String, Object, Object> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(String... params) {
			try {
				InputStream inputStream = null;
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(uploadPrescrUrl);
				// StringEntity stringEntity = new StringEntity(params[0]);
				// httpPost.setEntity(stringEntity);
				ArrayList<NameValuePair> n = nameValuePairs;
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				// httpPost.setHeader("Accept","application/Json");
				httpPost.addHeader("content-type",
						"application/x-www-form-urlencoded");
				HttpResponse httpResponse = httpClient.execute(httpPost);
				inputStream = httpResponse.getEntity().getContent();

				if (inputStream != null) {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(inputStream));
					String line = "";
					String result = "";
					while ((line = bufferedReader.readLine()) != null) {
						result += line;
					}

					Log.e("ERRORISIS", "IS: " + result);
					inputStream.close();
					return result;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String resu = result.toString();
			if (resu.contains("Prescription added successfully")) {
				Toast.makeText(getApplicationContext(),
						"Prescription is uploaded", Toast.LENGTH_LONG).show();
				cdbean = Okler.getInstance().getSingleCart();
				String s1[] = resu.split(",");
				String s2 = s1[3];
				String s3[] = s2.split(":");
				String ss = s3[1].substring(0, s3[1].length() - 1);
				String presc_id = ss;
				cdbean.setPresc_id(presc_id);
				Okler.getInstance().setSingleCart(cdbean);
				Intent in = new Intent(AlloUpPrescActivity.this,
						ProductCheckoutSummary.class);
				in.putExtra("Check", check);
				startActivity(in);
			} else {
				Toast.makeText(getApplicationContext(),
						"Some error occured while uploading prescription",
						Toast.LENGTH_LONG).show();
			}
		}

	}

}
