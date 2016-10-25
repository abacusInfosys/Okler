package com.okler.android;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.CameraGalleryImageInfo;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;
import com.okleruser.R;
import com.okler.databeans.PrescriptionImagesDataBean;
import com.okler.databeans.PrescriptionsDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.dialogs.CameraGalleryDialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PrescriptionPreview extends BaseActivity {
	
	String imageFilePath,imageFileName;
	Bitmap imgBitmap;
	Toolbar toolBar;
	Button btnNext;
	boolean flag;

	private ImageView imgPreview, thumbnail;	
	RelativeLayout btnUpload, btnCancel;
	
	ImageView image, image2;
	
	View bottomBarLayout;
	int totalCnt;
	LinearLayout layout,titleLayout;
	ImageView imgBack;
	boolean isMedPres;
	LinearLayout progressLinLayout;
	Activity act;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	PrescriptionsDataBean prescriptionsDataBean;
	int required_type=0;
	String uploadPrescrUrl;
	RelativeLayout back_layout;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_prescription_preview);		
		layout = (LinearLayout) findViewById(R.id.image_layout);	
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		act=this;
		//Utilities.writeToLogFIle("PrescPrev: 1");
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		progressLinLayout = (LinearLayout)findViewById(R.id.progressLinLayout);
		setSupportActionBar(toolBar);
		final ActionBar ab=getSupportActionBar();
		//ab.setDisplayHomeAsUpEnabled(true);
		isMedPres = getIntent().getBooleanExtra("isMedPres", true);
		uploadPrescrUrl = getString(R.string.upload_pres);
		if(Okler.getInstance().getBookingType() == 9){
			titleLayout=(LinearLayout) findViewById(R.id.title_layout);
			toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
			titleLayout.setBackgroundColor(Color.parseColor("#c054ca"));
		//		Utilities.writeToLogFIle("PrescPrev: Diagno");
		}else{
			toolBar.setBackgroundResource(R.drawable.custom_view_grad_upload_pesc);
	//		Utilities.writeToLogFIle("PrescPrev: Med");
		}
		back_layout = (RelativeLayout)toolBar.findViewById(R.id.back_layout);
		back_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Okler.getInstance().getPrescriptionsDataBeans().getPresImages().clear();
			finish();	
			}
		});
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       imgBack.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Okler.getInstance().getPrescriptionsDataBeans().getPresImages().clear();
				
			finish();	
			}
		});
	      Utilities.setTitleText(toolBar, "Upload Prescriptions [2/3]");		
		View view = findViewById(R.id.fragment1);
		
		imgPreview = (ImageView) view.findViewById(R.id.selected_image);
		//thumbnail = (ImageView) view.findViewById(R.id.thumbnail_image);
		
		btnCancel = (RelativeLayout) findViewById(R.id.btn_cancel);
		btnUpload = (RelativeLayout) findViewById(R.id.btn_upload);
		if(Okler.getInstance().getBookingType() == 9){
			
			btnCancel.setBackgroundColor(getResources().getColor(R.color.ModerateMagenta)); 
			btnUpload.setBackgroundColor(getResources().getColor(R.color.Darkmoderateviolet));
		}
	//	Utilities.writeToLogFIle("PrescPrev: 2");
		if(getIntent().getExtras().get("imageFilePath") != null)
    	{
    		imageFilePath =  getIntent().getExtras().get("imageFilePath").toString();
    		if(getIntent().getExtras().get("imgFileName") != null)
    		{
    			imageFileName = getIntent().getExtras().get("imgFileName").toString();
    			imageFilePath = imageFilePath+"/"+imageFileName;//06_01_2016 Gitesh
    			Utilities.writeToLogFIle("PrescPrev:  imgPath "+ imageFilePath);
    		}
    		
    	} 			
		
		flag =getIntent().getBooleanExtra("flag",false);
		//Create a bundle and put bitmap in compressed format in a bundle 
		Bundle bundle = new Bundle();
		imageFileName = null;
		imgBitmap = Utilities.getImageBitmapFromStorage(imageFilePath, imageFileName);
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		imgBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bs);
		bundle.putByteArray("image", bs.toByteArray());		
		
	//	Utilities.writeToLogFIle("PrescPrev: 3");
		imgPreview.setImageBitmap(imgBitmap);
		
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(totalCnt>1){
				Okler.getInstance().getPrescriptionsDataBeans().getPresImages().remove(totalCnt-1);
				onResume();
				}else{
					finish();
					Okler.getInstance().getPrescriptionsDataBeans().getPresImages().clear();
					//Toast.makeText(getApplicationContext(), "Atleast One Prescription is needed.", Toast.LENGTH_LONG).show();
				}
			//	Utilities.writeToLogFIle("PrescPrev:  Cancle Clicked");
			}
		});
	//	Utilities.writeToLogFIle("PrescPrev: 4");
		btnUpload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isMedPres)
				{
					// Call prescription activity
					prescriptionsDataBean = Okler.getInstance().getPrescriptionsDataBeans();
					nameValuePairs = Utilities.getPrescriptionNameValPairFromBean(act, prescriptionsDataBean, isMedPres, required_type);
					String str ="";// gson.toJson(postParams);
				//	Utilities.writeToLogFIle("PrescPrev: upclicked diagno");
					new UploadPrescAsyncTask().execute(str);
					showProgress(true);
				}
				else
				{
					Intent in = new Intent(PrescriptionPreview.this, PrescriptionDelivery.class);
					in.putExtra("isMedPres",isMedPres);
					startActivity(in);
				//	Utilities.writeToLogFIle("PrescPrev:  upclicked med");
				}
				
			//	Utilities.writeToLogFIle("PrescPrev:  upclicked");
				
			}
		});	
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	//	Utilities.writeToLogFIle("PrescPrev: onBackPressed()");
		Okler.getInstance().getPrescriptionsDataBeans().getPresImages().clear();
		finish();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	//	Utilities.writeToLogFIle("PrescPrev: onResume()");
		layout = (LinearLayout) findViewById(R.id.image_layout);
		totalCnt = Okler.getInstance().getPrescriptionsDataBeans().getPresImages().size();
		ImageView[] imageViews = new ImageView[totalCnt+1];
		layout.removeAllViews();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150,150);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		lp2.height=100;
		lp2.width=100;
		lp2.gravity=Gravity.CENTER_VERTICAL;
		lp.setMargins(5, 5, 5, 5);
	//	Utilities.writeToLogFIle("PrescPrev: 6");
		for(int  i =0; i < totalCnt; i++)
		{
			imageViews[i] = new ImageView(getApplicationContext());
			imageViews[i].setImageBitmap(Okler.getInstance().getPrescriptionsDataBeans().getPresImages().get(i).getPrescImages());
			layout.addView(imageViews[i]);			
			imageViews[i].setLayoutParams(lp);
		}
		if(totalCnt<=0){
			
		}else
		{
		imgPreview.setImageBitmap(Okler.getInstance().getPrescriptionsDataBeans().getPresImages().get(totalCnt-1).getPrescImages());
		}
		imageViews[totalCnt] = new ImageView(getApplicationContext());
		imageViews[totalCnt].setImageResource(R.drawable.camera);
		imageViews[totalCnt].setLayoutParams(lp2);
		//imageViews[totalCnt].setLayoutParams(lp);
		layout.addView(imageViews[totalCnt]);
	//	Utilities.writeToLogFIle("PrescPrev: 8");
	
		
		imageViews[totalCnt].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(totalCnt<4){
				CameraGalleryDialog cdd=new CameraGalleryDialog(PrescriptionPreview.this);
				cdd.show();
				}else{
					Toast.makeText(getApplicationContext(), "You can only add 4 prescriptions at a time", Toast.LENGTH_LONG).show();
				}
				
			}
		});
	//	Utilities.writeToLogFIle("PrescPrev: 9");
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
	
	/**
	 * Receiving activity result method will be called after closing the camera
	 * */
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			try
			{
	//		Utilities.writeToLogFIle("PrescPrev: onactivityresult");
		//	Utilities.writeToLogFIle("UpPresc: requestCode= "+requestCode+" resultCode= "+resultCode+" data= "+data);
			if(resultCode == RESULT_OK)
			{
		//		Utilities.writeToLogFIle("PrescPrev: resultCode == RESULT_OK "+resultCode);
			CameraGalleryImageInfo imgInfo = Utilities.getImageInfo(requestCode, resultCode, this, data);
		//	Utilities.writeToLogFIle("PrescPrev: CameraGalleryImageInfo imgInfo = Utilities.getImageInfo(requestCode, resultCode, this, data); "+imgInfo);
			PrescriptionImagesDataBean pImgBean = new PrescriptionImagesDataBean();
		//	Utilities.writeToLogFIle("PrescPrev: PrescriptionImagesDataBean pImgBean = new PrescriptionImagesDataBean();");
			Bitmap iBitmap = imgInfo.getImgBitmap();
			if(iBitmap!=null){
			Utilities.writeToLogFIle("PrescPrev: Bitmap iBitmap = imgInfo.getImgBitmap(); "+ iBitmap);
		    String	base64string = Utilities.convertImageToBase64(iBitmap);
		  //  Utilities.writeToLogFIle("PrescPrev: String	base64string = Utilities.convertImageToBase64(iBitmap); "+base64string);
		    pImgBean.setPrescImages(iBitmap);
		  //  Utilities.writeToLogFIle("PrescPrev: pImgBean.setPrescImages(iBitmap);");
		    pImgBean.setBase64ConvrtedImg(base64string);
		  //  Utilities.writeToLogFIle("PrescPrev: pImgBean.setBase64ConvrtedImg(base64string);");
		    String imgPa = imgInfo.getUri();
		    Utilities.writeToLogFIle("PrescPrev: ImgUri "+imgPa);
		    pImgBean.setImgUri(imgPa);
		    Utilities.writeToLogFIle("PrescPrev: pImgBean.setImgUri(imgPa);");
		    int size = Okler.getInstance().getPrescriptionsDataBeans().getPresImages().size();
		    Utilities.writeToLogFIle("PrescPrev: int size = Okler.getInstance().getPrescriptionsDataBeans().getPresImages().size(); "+size);
		    boolean imgFound = false;
		    Utilities.writeToLogFIle("PrescPrev: boolean imgFound = false;");
		    if(size != 0)
		    {
		    	for(int c = 0; c < size; c++)
		    	{
		    		//Utilities.writeToLogFIle("PrescPrev: in for "+c);
		    		String oldImgPath = Okler.getInstance().getPrescriptionsDataBeans().getPresImages().get(c).getImgUri();
		    		//Utilities.writeToLogFIle("PrescPrev: String oldImgPath = Okler.getInstance().getPrescriptionsDataBeans().getPresImages().get(c).getImgUri(); "+oldImgPath  );
		    		if(oldImgPath.equals(pImgBean.getImgUri())){
		    			Utilities.writeToLogFIle("PrescPrev: if(oldImgPath.equals(pImgBean.getImgUri())) "+"imgFound"+String.valueOf(imgFound));
		    			imgFound = true;
		    		}
		    	}		    	
		    }

		    if(!imgFound)
		    	//Utilities.writeToLogFIle("PrescPrev: if(!imgFound)");
		    	Okler.getInstance().getPrescriptionsDataBeans().getPresImages().add(pImgBean);
		    	//Utilities.writeToLogFIle("PrescPrev: Okler.getInstance().getPrescriptionsDataBeans().getPresImages().add(pImgBean);");
			}else{
				Toast.makeText(act, "Unable to locate image file.", Toast.LENGTH_SHORT).show();
			}
			}
			else if(resultCode == -1){
				Utilities.writeToLogFIle("PrescPrev:  Gallery Crash "+resultCode);
			}
			}catch(Exception ex)
			{
				Utilities.writeToLogFIle("Exception occurred"+ex.getMessage());
			}
	
		}
		
		private void showProgress(boolean paramBoolean)
		  {
		    if (paramBoolean)
		    {
		      this.progressLinLayout.setVisibility(View.VISIBLE);
		      return;
		    }
		    this.progressLinLayout.setVisibility(View.INVISIBLE);
		  }
		
		public class UploadPrescAsyncTask extends AsyncTask<String, Object, Object>{
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}
			
			@Override
			protected Object doInBackground(String... params) {
				try
				{
					InputStream inputStream= null;
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(uploadPrescrUrl);
				//	StringEntity stringEntity = new StringEntity(params[0]);
				//	httpPost.setEntity(stringEntity);
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					for(int i = 0;i<nameValuePairs.size();i++)
					Utilities.writeToLogFIle("PrescPrev: nvpairs "+nameValuePairs.get(i).getName()+" "+nameValuePairs.get(i).getValue());
				//	httpPost.setHeader("Accept","application/Json");
					httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
					HttpResponse httpResponse = httpClient.execute(httpPost);
					inputStream=httpResponse.getEntity().getContent();
					
					if(inputStream!=null){
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
						String line="";
						String result="";
						while ((line = bufferedReader.readLine()) != null) {
							result+=line;
							}
						
						Log.e("ERRORISIS","IS: "+result);
						Utilities.writeToLogFIle("PrescPrev: upResult = "+result);
						inputStream.close();
						return result;
					}
				}catch (UnsupportedEncodingException e) {	
					Utilities.writeToLogFIle("PrescPrev: upResult UnsupportedEncodingException = "+e.getStackTrace());
					e.printStackTrace();
					return null;
				} catch (ClientProtocolException e) {				
					e.printStackTrace();
					Utilities.writeToLogFIle("PrescPrev: upResult ClientProtocolException = "+e.getStackTrace());
					return null;
					
				} catch (IOException e) {				
					e.printStackTrace();
					Utilities.writeToLogFIle("PrescPrev: upResult IOException = "+e.getStackTrace());
					return null;
				} catch (Exception e) {
					// TODO: handle exception
					Utilities.writeToLogFIle("PrescPrev: upResult Exception = "+e.getStackTrace());
					Utilities.writeToLogFIle("PrescPrev:  ");
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Object result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				String resu = String.valueOf(result);
				//String resu = result.toString();
				showProgress(false);
				if(resu.contains("result\":\"true"))
				{
					Toast.makeText(getApplicationContext(), "Prescription is uploaded", Toast.LENGTH_LONG).show();	
					
					String s1[] = resu.split(",");
					String s2 = s1[3];
					String s3[] = s2.split(":");
					//String ss = s3[1].substring(0, s3[1].length()-1);
					String presc_id= s3[1];
					Utilities.writeToLogFIle("PrescPrev: Uploaded success Pid = "+presc_id);
					UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(PrescriptionPreview.this);
					int cust_id = ubean.getId();
					String customer_name = ubean.getFname();
					String email = ubean.getEmail();
					String salutation = ubean.getSalutation();
					PrescriptionsDataBean presbean = Okler.getInstance().getPrescriptionsDataBeans();
					//String doctor_name = presbean.getDocName();
					String patient_name = presbean.getPatientName();
					
					try {
						customer_name = URLEncoder.encode(customer_name,"UTF-8");
						//doctor_name = URLEncoder.encode(doctor_name,"UTF-8");
						patient_name = URLEncoder.encode(patient_name, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String upload_pres_email = getResources().getString(R.string.serverUrl)+getResources().getString(R.string.upload_pres_email)+"salutation="+salutation+"&cust_id="+cust_id+"&customer_name="+customer_name+"&email="+email+"&presc_id="+presc_id+"&doctor_name="+""+"&patient_name="+patient_name;

					WebJsonObjectRequest webObjReq=new WebJsonObjectRequest(Method.GET, upload_pres_email, new JSONObject(),new Response.Listener<JSONObject>() 
							{

								@Override
								public void onResponse(JSONObject response) 
								{
									Utilities.writeToLogFIle("In on response of mail callback");
									Log.e("mail", "mail sent");
								}

								
							},new Response.ErrorListener() 
							{

								@Override
								public void onErrorResponse(VolleyError error) {
									
									Utilities.writeToLogFIle("In on error response of mail callback"+error.getLocalizedMessage());
									Log.e("mail", "mail sent");
									//Log.e("error", new String(error.networkResponse.data));
							}
								
							});
							
							VolleyRequest.addJsonObjectRequest(act,webObjReq);
				
							Intent in = new Intent(PrescriptionPreview.this, PrescriptionList.class);
							in.putExtra("isMedPres", isMedPres);
								in.putExtra("Check", 11);
							startActivity(in);
					
					
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Some error occured while uploading prescription", Toast.LENGTH_LONG).show();
					Utilities.writeToLogFIle("PrescPrev: Upload Fails");
				}
				
			}
			
		}
}