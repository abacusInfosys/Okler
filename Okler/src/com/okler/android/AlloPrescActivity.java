package com.okler.android;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.support.v7.widget.Toolbar;
import com.okler.databeans.PrescriptionImagesDataBean;
import com.okler.databeans.UsersDataBean;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AlloPrescActivity extends BaseActivity {
	View bottomBarLayout;
	Toolbar toolBar;
	Button notifCount;
	TextView checkout_Tv,upload_later,choose_from_existing;
	ImageView imgBack;
	int check;
	PrescriptionImagesDataBean presImgs;
	Activity ack;
	ArrayList<NameValuePair>nameValuePair=new ArrayList<NameValuePair>();
	UsersDataBean ubean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_med_up_presc);
		toolBar = (Toolbar)findViewById(R.id.toolbar);
		notifCount = (Button) toolBar.findViewById(R.id.notif_count);
		setSupportActionBar(toolBar);
		ack = this;
		check = getIntent().getIntExtra("Check", 11);
		checkout_Tv = (TextView)findViewById(R.id.checkout_Tv);
		Utilities.setTitleText(toolBar, getString(R.string.allopathy));
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_medicine);
		ubean=new UsersDataBean();
		ubean=Utilities.getCurrentUserFromSharedPref(ack);
		if(check==0){
			toolBar.setBackgroundColor(Color.BLUE);
			checkout_Tv.setBackgroundColor(Color.BLUE);
			Utilities.setTitleText(toolBar, "Cart");
		}
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		choose_from_existing = (TextView)findViewById(R.id.choose_from_existing);
		choose_from_existing.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getApplicationContext(),PrescriptionList.class);
				intent.putExtra("Check", check);
				intent.putExtra("isParentMyAccount", true);
				startActivity(intent);
				
			}
		});
		presImgs = new PrescriptionImagesDataBean();
		upload_later = (TextView)findViewById(R.id.upload_later);
		upload_later.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),ProductCheckoutSummary.class);
				intent.putExtra("Check", check);
				startActivity(intent);
				nameValuePair.add(new BasicNameValuePair("user_id",""+ubean.getId()));
				new UploadLaterAsyncTask().execute("");
			}
		});
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			finish();	
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK)
		{			
			CameraGalleryImageInfo imgInfo = Utilities.getImageInfo(requestCode, resultCode, this, data);
			Intent newIntent = new Intent(this,AlloUpPrescActivity.class);
			newIntent.putExtra("imageFilePath", imgInfo.getFilePath());
			newIntent.putExtra("imgFileName",imgInfo.getFileName());
			newIntent.putExtra("Check", check);
			Bitmap iBitmap = imgInfo.getImgBitmap();
	if(iBitmap != null)
			{
		    String	base64string = Utilities.convertImageToBase64(iBitmap);
		    presImgs.setPrescImages(iBitmap);
		    presImgs.setBase64ConvrtedImg(base64string);
		    String imgPa = imgInfo.getUri();
		    presImgs.setImgUri(imgPa);
			Okler.getInstance().getPrescriptionsDataBeans().getPresImages().add(presImgs);
			startActivity(newIntent);
			}else{
				Toast.makeText(ack, "Unable to locate image file.", Toast.LENGTH_SHORT).show();
			}
		}	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onResume() {
		super.onResume();
		UIUtils.setCartCount(notifCount, ack);
	}
	public class UploadLaterAsyncTask extends AsyncTask<String,Object,Object>
	{
		@Override
		protected Object doInBackground(String... params) {
			String url=getString(R.string.serverUrl)+getString(R.string.addPrescLater);	
			String result=String.valueOf(Utilities.RegisterNewUser(url,nameValuePair));
			try
			{
				JSONObject obj=new JSONObject(result);
				int id=obj.getInt("pre_upid");
				Utilities.writeIntToSharedPref(ack,"pre_upid",id);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}