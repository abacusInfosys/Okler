package com.okler.android;


import java.io.ByteArrayOutputStream;

import com.okler.databeans.PrescriptionImagesDataBean;
import com.okler.databeans.PrescriptionsDataBean;
import com.okler.utils.CameraGalleryImageInfo;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class UploadPrescription extends BaseActivity {
	
	String[] pres_type = 	new String[]{"Select Type of Prescription", "Diagnostics", "Medicines"};
	Spinner spinnerPrescriptionType;	
	View bottomBarLayout;
	Toolbar toolBar;
	Button notifCount;	
	static int mNotifCount = 0;
	ImageButton btnUpload;
	RelativeLayout relUpDisable;
	ImageView imgBack;
	PrescriptionImagesDataBean presImgs;
	PrescriptionsDataBean prsDtBean;
	boolean isMedPres ;
	public static boolean changetab = false;
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_prescription);
	
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		Utilities.writeToLogFIle(" In Upload Prescription Start");
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		final ActionBar ab=getSupportActionBar();
	//	ab.setDisplayHomeAsUpEnabled(true);
		spinnerPrescriptionType = (Spinner) findViewById(R.id.prescription_type_spinner);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, pres_type);
		spinnerPrescriptionType.setAdapter(adapter1);
		isMedPres = getIntent().getBooleanExtra("isMedPres", true);
		Utilities.writeToLogFIle(" In Upload Prescription "+String.valueOf(isMedPres));
		if(isMedPres)
			{toolBar.setBackgroundResource(R.drawable.custom_view_grad_upload_pesc);
			Utilities.writeToLogFIle(" In Upload Prescription ");
			
			}else{
				toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
				spinnerPrescriptionType.setSelection(1);
				Utilities.writeToLogFIle(" In Diagno Upload Prescription");
			}
			imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(imgBack.getWindowToken(), 0);

			}
		});
	      Utilities.setTitleText(toolBar, "Upload Prescriptions [1/3]");

	      Utilities.writeToLogFIle(" In Upload Prescription 2");
		
		RelativeLayout rl = (RelativeLayout)bottomBarLayout.findViewById(R.id.uploadPresRL);
		btnUpload =(ImageButton) rl.findViewById(R.id.btnUploadPres);
		LinearLayout active_upload = (LinearLayout) rl.findViewById(R.id.active_upload);
		active_upload.setVisibility(View.VISIBLE);
		btnUpload.setEnabled(false);
		rl.setEnabled(false);
		
		presImgs = new PrescriptionImagesDataBean();
		prsDtBean = new PrescriptionsDataBean();
		Okler.getInstance().setPrescriptionsDataBeans(prsDtBean);
		Utilities.writeToLogFIle(" In Upload Prescription 3");
		/*if(isMedPres)
		 spinnerPrescriptionType.setSelection(2);
		else
			spinnerPrescriptionType.setSelection(1);*/
	//	spinnerPrescriptionType.setEnabled(false);
		
		spinnerPrescriptionType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				if(spinnerPrescriptionType.getSelectedItem().toString() == "Medicines")
				{
					changetab = false;
					isMedPres = true;
				
				}
				if(spinnerPrescriptionType.getSelectedItem().toString() == "Diagnostics")
				{
					changetab = true;
					isMedPres = false;
					//PrescriptionList.visibleDiagnostic();
				}
				Utilities.writeToLogFIle("presc Type = "+spinnerPrescriptionType.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		    return super.onCreateOptionsMenu(menu);

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
	

/**
 * Receiving activity result method will be called after closing the camera
 * */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Utilities.writeToLogFIle("UpPresc: requestCode= "+requestCode+" resultCode= "+resultCode+" data= "+data);
		try
		{
		if(resultCode == RESULT_OK)
		{			
			Utilities.writeToLogFIle(" UpPresc: resultcode=resultOk "+resultCode);
			CameraGalleryImageInfo imgInfo = Utilities.getImageInfo(requestCode, resultCode, this, data);
			Utilities.writeToLogFIle("UpPresc: imgInfo"+imgInfo);
			Intent newIntent = new Intent(this,PrescriptionPreview.class);
			Utilities.writeToLogFIle("UpPresc: newIntent");
			newIntent.putExtra("imageFilePath", imgInfo.getFilePath());
			Utilities.writeToLogFIle("UpPresc: imgInfo.getFileName() "+imgInfo.getFilePath());
			newIntent.putExtra("imgFileName",imgInfo.getFileName());
			Utilities.writeToLogFIle("UpPresc: imgInfo.getFileName() "+imgInfo.getFileName());
			newIntent.putExtra("isMedPres",isMedPres);
			Utilities.writeToLogFIle("UpPresc: ismedpresc = "+String.valueOf(isMedPres));
			Utilities.writeToLogFIle("upPresc: "+imgInfo.getFilePath()+"  name "+imgInfo.getFileName());
			Bitmap iBitmap = imgInfo.getImgBitmap();
			Utilities.writeToLogFIle("UpPresc: ibitmap "+iBitmap);
			Utilities.writeToLogFIle(" In Upload Prescription 5");
				
		    String	base64string = Utilities.convertImageToBase64(iBitmap); //Base64.encodeToString(byte_arr,Base64.DEFAULT);
		    Utilities.writeToLogFIle("UpPresc: base64 "+base64string);
		    presImgs.setPrescImages(iBitmap);
		    Utilities.writeToLogFIle("UpPresc: presImgs.setPrescImages(iBitmap);");
		    presImgs.setBase64ConvrtedImg(base64string);
		    Utilities.writeToLogFIle("UpPresc: presImgs.setBase64ConvrtedImg(base64string);");
		    String imgPa = imgInfo.getUri();
		    Utilities.writeToLogFIle("UpPresc:  ImgUri "+imgPa);
		    presImgs.setImgUri(imgPa);
		    Utilities.writeToLogFIle("UpPresc: presImgs.setImgUri(imgPa);");
			Okler.getInstance().getPrescriptionsDataBeans().getPresImages().add(presImgs);
			
			Utilities.writeToLogFIle("UpPresc: Okler.getInstance().getPrescriptionsDataBeans().getPresImages().add(presImgs);");
			//newIntent.putExtra("flag",flag);//******* 24112015
			startActivity(newIntent);
		}
		
		else if(resultCode == RESULT_CANCELED)
		{
			Utilities.writeToLogFIle("UpPresc:  Canceled" );
			//Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
		} 
		else if(resultCode == -1){
			
			Utilities.writeToLogFIle("UpPresc:  Gallery Failed "+resultCode);
		}
		else 
		{
			Utilities.writeToLogFIle("UpPresc:  "+resultCode);
			// failed to capture image
		//	Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
		}
		}catch(Exception ex)
		{
			Utilities.writeToLogFIle("Got exception "+ex.getStackTrace());
		}
	}
}
