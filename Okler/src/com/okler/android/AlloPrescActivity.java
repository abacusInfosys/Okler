package com.okler.android;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import com.okler.databeans.PrescriptionImagesDataBean;
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
		Utilities.writeToLogFIle("Value of check in on create allo presc act is:"+check);
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
		Utilities.writeToLogFIle("In AlloPrescActi. OnActivityresult");
		Intent newIntent = new Intent(this,AlloUpPrescActivity.class);
		boolean flag = false;
		if(data == null){ Utilities.writeToLogFIle("Data is null");}
		else{
			Utilities.writeToLogFIle("Data is not null");
			Bundle extras = data.getExtras(); 
			/* Get the returned image from extra*/ 
	        if(extras != null){
	        	Utilities.writeToLogFIle("Extras is not null");
	        	Bitmap bmpt = (Bitmap) extras.get("data");
	        	if(bmpt!=null){
	        		
	        		CameraGalleryImageInfo imgInfo = Utilities.getImageInfo(requestCode, resultCode, this, data);
	    			Utilities.writeToLogFIle("In AlloPrescActi. OnActivityresult . image info");
	    			
	    			newIntent.putExtra("imageFilePath", imgInfo.getFilePath());
	    			newIntent.putExtra("imgFileName",imgInfo.getFileName());
	    			newIntent.putExtra("Check", check);
	    			Utilities.writeToLogFIle("Value of check in allo presc acr is:"+check);
	        		Utilities.writeToLogFIle("Bitmap is not null");
					  String base64string = Utilities.convertImageToBase64(bmpt); //Base64.encodeToString(byte_arr,Base64.DEFAULT);
			    Utilities.writeToLogFIle("In AlloPrescActi. OnActivityresult . base 64 encrupted key is"+base64string);
			    Utilities.writeToLogFIle("In AlloPrescActi. OnActivityresult . base 64"+base64string);
			    presImgs.setPrescImages(bmpt);
			    Utilities.writeToLogFIle("after Set presc image");
			    presImgs.setBase64ConvrtedImg(base64string);
			    Utilities.writeToLogFIle("after Set base 64 converted");
		/*	    String imgPa = imgInfo.getUri();
			    Utilities.writeToLogFIle("String imagpA"+imgPa);
			    presImgs.setImgUri(imgPa);*/
			    flag = true;
			    Utilities.writeToLogFIle("After set image uri");
				Okler.getInstance().getPrescriptionsDataBeans().getPresImages().add(presImgs);
				//newIntent.putExtra("flag",flag);//******* 24112015
				startActivity(newIntent);
				}
	        	else
	        		Utilities.writeToLogFIle("Bitmap is null in data");
	        }
		}
		
		if(resultCode == RESULT_OK && flag == false)
		{
			Utilities.writeToLogFIle("In AlloPrescActi. OnActivityresult . result ok");
			
			CameraGalleryImageInfo imgInfo = Utilities.getImageInfo(requestCode, resultCode, this, data);
			Utilities.writeToLogFIle("In AlloPrescActi. OnActivityresult . image info");
			
			newIntent.putExtra("imageFilePath", imgInfo.getFilePath());
			newIntent.putExtra("imgFileName",imgInfo.getFileName());
			newIntent.putExtra("Check", check);
			Utilities.writeToLogFIle("In AlloPrescActi. OnActivityresult . File path"+imgInfo.getFilePath());
			Utilities.writeToLogFIle("In AlloPrescActi. OnActivityresult . File path"+imgInfo.getFileName());
			//newIntent.putExtra("isMedPres",isMedPres);
			Bitmap iBitmap = imgInfo.getImgBitmap();
			if(iBitmap != null)
			{
			    String	base64string = Utilities.convertImageToBase64(iBitmap); //Base64.encodeToString(byte_arr,Base64.DEFAULT);
			    Utilities.writeToLogFIle("In AlloPrescActi. OnActivityresult . base 64"+base64string);
			    presImgs.setPrescImages(iBitmap);
			    Utilities.writeToLogFIle("after Set presc image");
			    presImgs.setBase64ConvrtedImg(base64string);
			    Utilities.writeToLogFIle("after Set base 64 converted");
			    String imgPa = imgInfo.getUri();
			    Utilities.writeToLogFIle("String imagpA"+imgPa);
			    presImgs.setImgUri(imgPa);
			    Utilities.writeToLogFIle("After set image uri");
				Okler.getInstance().getPrescriptionsDataBeans().getPresImages().add(presImgs);
				//newIntent.putExtra("flag",flag);//******* 24112015
				startActivity(newIntent);
			}
			else
			{
				Utilities.writeToLogFIle("Else: ibitmap is null");
			}
		}	
		else
		{
			Utilities.writeToLogFIle("In Else. Result not ok. Result code is:"+ resultCode);
		}
		
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(notifCount, ack);
	}
}
