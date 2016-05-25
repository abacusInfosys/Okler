package com.okler.android;



import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import com.okler.databeans.CartDataBean;
import com.okler.databeans.PrescriptionImagesDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.utils.CameraGalleryImageInfo;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_med_up_presc);
		toolBar = (Toolbar)findViewById(R.id.toolbar);
		notifCount = (Button) toolBar.findViewById(R.id.notif_count);
		setSupportActionBar(toolBar);
		ack = this;
		check = getIntent().getIntExtra("Check", 11);
		ActionBar ab = getSupportActionBar();
		checkout_Tv = (TextView)findViewById(R.id.checkout_Tv);
		//ab.setDisplayHomeAsUpEnabled(true);
		Utilities.setTitleText(toolBar, getString(R.string.allopathy));
			//ab.setTitle(R.string.title_activity_allopathy);
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_medicine);
		
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
			//newIntent.putExtra("isMedPres",isMedPres);
			Bitmap iBitmap = imgInfo.getImgBitmap();
			/*presImgs.setPrescImages(iBitmap);
			presImgs.setTag(1);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			iBitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream); 
			byte[] byte_arr = stream.toByteArray();*/			
		    String	base64string = Utilities.convertImageToBase64(iBitmap); //Base64.encodeToString(byte_arr,Base64.DEFAULT);
		    presImgs.setPrescImages(iBitmap);
		    presImgs.setBase64ConvrtedImg(base64string);
		    String imgPa = imgInfo.getUri();
		    presImgs.setImgUri(imgPa);
			Okler.getInstance().getPrescriptionsDataBeans().getPresImages().add(presImgs);
			//newIntent.putExtra("flag",flag);//******* 24112015
			startActivity(newIntent);
		}
		
		else if(resultCode == RESULT_CANCELED)
		{
			//Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
		} 
		else 
		{
			// failed to capture image
		//	Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		/*getMenuInflater().inflate(R.menu.allopathy, menu);
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
			   ArrayList<ProductDataBean>pdList = cbean.getProdList();
			   // pdList = odbean.getProdList();
			   int maincount = pdList.size(); 
			   
			   if(maincount>9){
			    notifCount.setText(""+maincount);
			    }
			    else{
			    	notifCount.setText("0"+maincount);
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
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(notifCount, ack);
	}
}
