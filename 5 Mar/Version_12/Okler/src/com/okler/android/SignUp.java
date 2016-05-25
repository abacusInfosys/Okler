
package com.okler.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.okler.databeans.PrescriptionImagesDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.dialogs.CameraGalleryDialog;
import com.okler.dialogs.VerifyPhoneNumDialog;
import com.okler.utils.CameraGalleryImageInfo;
import com.okler.utils.MediPhysioTextValidations;
import com.okler.utils.Okler;
import com.okler.utils.SocialLoginEnum;
import com.okler.utils.SocialMediaUtils;
import com.okler.utils.TextValidations;
import com.okler.utils.UIUtils;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends BaseActivity {

	Button btnSignup;
	EditText et_name,et_surName,et_phonenumber,et_emailId,et_password;
	String firstName,lastName,phoneNumber,emailId,password;
	boolean flag;
	ImageView cameraImage;
	  private static final int GALLERY_PICTURE = 100;
	    private static final int CAMERA_PICTURE = 101;
		  private static final int RC_SIGN_IN = 100000;
	//private String phoneNumber="9876543210";
	Context context;
	private EditText editTextToValidate;
    public Uri SelectedImage;
	//public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	UsersDataBean userBean;
	TextValidations text ;
	RelativeLayout alreadyHaveAcc;
	View bottomBarLayout;
	Toolbar toolBar;
	Button notifCount;
	ImageView imgBack;
	Activity actr;
	LoginButton facebook_Login;
	ImageView google_login;
	SocialMediaUtils scUtils;
	CallbackManager callbackManager;
	Button fbNew;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	private boolean mIntentInProgress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scUtils = new SocialMediaUtils(this,false);
		SocialMediaUtils.initFacebookSDK(this);
		callbackManager = CallbackManager.Factory.create();
		setContentView(R.layout.activity_sign_up);
		toolBar = (Toolbar)findViewById(R.id.toolbar);
		facebook_Login = (LoginButton) findViewById(R.id.facebook_Login);
		fbNew = (Button)findViewById(R.id.fbNewBtn);
		google_login = (ImageView) findViewById(R.id.google_Login);
	//	setSupportActionBar(toolBar);
	//	ActionBar ab = getSupportActionBar();
		bottomBarLayout = findViewById(R.id.bottombar);
		actr = this;
		  
	//	handleMapping(bottomBarLayout);
	//	imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	//	toolBar.setBackgroundResource(UIUtils.getToolBarDrawable(Okler.getInstance().getBookingType()));
	//	imgBack.setOnClickListener(new OnClickListener() {
	//			
	//		@Override
	//		public void onClick(View v) {
	//			// TODO Auto-generated method stub
	//		finish();	
	//		}
	//	});
		
		// for google login
		
				google_login.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						if(!SocialMediaUtils.mGoogleApiClient.isConnected())
							 SocialMediaUtils.mGoogleApiClient.connect();
						SocialLoginEnum scLogin = SocialLoginEnum.SIGNUP;
						Utilities.writeSignInSignUpStatus(actr, scLogin);
					}
				});
				
				
				fbNew.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub					
						
						SocialMediaUtils.setFacebookCallback(callbackManager, actr, facebook_Login);
						facebook_Login.performClick();
						SocialLoginEnum scLogin = SocialLoginEnum.SIGNUP;
						Utilities.writeSignInSignUpStatus(actr, scLogin);
					}
				});
		
		btnSignup = (Button) findViewById(R.id.signup_button);
		context=getApplicationContext();
		userBean=new UsersDataBean();
		et_name=(EditText) findViewById(R.id.et_first);
		et_surName=(EditText) findViewById(R.id.et_surname);
		et_phonenumber=(EditText) findViewById(R.id.et_mobile);
		et_emailId=(EditText) findViewById(R.id.et_emailAdd);
		et_password=(EditText) findViewById(R.id.et_password);
		cameraImage=(ImageView) findViewById(R.id.itemIamge);
		TextView tv_elseLogin = (TextView) findViewById(R.id.tv_elseLogin);
		tv_elseLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SignUp.this, NewSignIn.class);
				startActivity(i);
			}
		});
		cameraImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CameraGalleryDialog cdd=new CameraGalleryDialog(SignUp.this);
				cdd.show();
			}
		});
btnSignup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag=validateFirstName();
				if(flag==true)
				{
					firstName=et_name.getText().toString();
					userBean.setFname(firstName);					
					flag=validateSurnmae();
					if(flag==true)
					{
						lastName=et_surName.getText().toString();
						userBean.setLname(lastName);
						System.out.println("lastname"+lastName);						
						flag=validateEmail();
						if(flag==true)
						{
							emailId=et_emailId.getText().toString();
							userBean.setEmail(emailId);
							flag=validatePhoneNo();
							if(flag==true)
							{
								phoneNumber=et_phonenumber.getText().toString();
								userBean.setPhone(phoneNumber);
								System.out.println("phone"+phoneNumber);
								flag=validatePassword();
								if(flag==true)
								{
									password=et_password.getText().toString();
									userBean.setPassword(password);
									System.out.println("password"+password);
									Okler.getInstance().setuDataBean(userBean);
									Gson gson=new Gson();
									new VerifyPhoneNumDialog(actr,phoneNumber).show(getFragmentManager(),"");
									/*System.out.println("oklerInstance"+(Okler.getInstance().getuDataBean()));
									nameValuePairs.add(new BasicNameValuePair("name", userBean.getFname()));
									nameValuePairs.add(new BasicNameValuePair("lastname", userBean.getLname()));
									nameValuePairs.add(new BasicNameValuePair("phone", userBean.getPhone()));
									nameValuePairs.add(new BasicNameValuePair("email", userBean.getEmail()));
									nameValuePairs.add(new BasicNameValuePair("image", userBean.getUser_image()));
									nameValuePairs.add(new BasicNameValuePair("password", userBean.getPassword()));
									
									String json=gson.toJson(Okler.getInstance().getuDataBean());
									System.out.println("JSON"+json);
									new SignUpAsyncTask().execute("");
*/									
									
								}	
							}			
								
						}
						
										
					}
					else
						{
							
							Toast.makeText(actr, "All fields are mandatory", Toast.LENGTH_LONG).show();
						}
				
				}
			}
		});
			
		
		
		alreadyHaveAcc=(RelativeLayout) findViewById(R.id.alreadyAccountLayout);
		alreadyHaveAcc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Intent startIntent=new Intent(SignUp.this,NewSignIn.class);
			startActivity(startIntent);
			}
		});
	}
	public boolean validateFirstName()
	{
		this.editTextToValidate = et_name;
		text = new TextValidations(editTextToValidate);
		if(editTextToValidate.length()<3)
		{
			editTextToValidate.setError("Please enter at least 3 letters");
			return false;
		}
		
		return text.validateText("Please Enter First name");
	}
	
	public boolean validateSurnmae()
	{
		this.editTextToValidate = et_surName;
		text = new TextValidations(editTextToValidate);
		if(editTextToValidate.length()<3)
		{
			editTextToValidate.setError("Please enter at least 3 letters");
			return false;
		}
		
		return text.validateText("Please Enter surname");
	}
	
	public boolean validateEmail()
	{
		this.editTextToValidate = et_emailId;
		text = new TextValidations(editTextToValidate);
		
		return text.ValidateEmailId();
	}
	
	public boolean validatePhoneNo()
	{
		this.editTextToValidate = et_phonenumber;
		if(editTextToValidate.length()<10 ||editTextToValidate.length()>10)
		{
			editTextToValidate.setError("Please enter 10 digit mobile number");
			return false;
		}
		
		long mobile_no = Long.parseLong(et_phonenumber.getText().toString());
		if(mobile_no == 0)
		{
			editTextToValidate.setError("Please enter your mobile number");
			return false;
		}
		
			return true;
	}
	public boolean validatePassword()
	{
		this.editTextToValidate = et_password;
		text = new TextValidations(editTextToValidate);
		return text.validateText("Please Enter password");
	}
	public boolean validate(){
		boolean flag; 
		return false;
		
	}
	

	/**
	 * Receiving activity result method will be called after closing the camera
	 * */
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if(requestCode == 100 || requestCode == 101)
			{
				if(resultCode == RESULT_OK)
				{
				
				CameraGalleryImageInfo imgInfo = Utilities.getImageInfo(requestCode, resultCode, this, data);
				//UsersDataBean ImgBean = new UsersDataBean();
				Bitmap image = imgInfo.getImgBitmap();
				
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				image.compress(Bitmap.CompressFormat.JPEG, 30, stream); 
				byte[] byte_arr = stream.toByteArray();			
			    String	base64string = Base64.encodeToString(byte_arr,Base64.DEFAULT);
			    //pImgBean.setPrescImages(iBitmap);
			    userBean.setUser_image(base64string);
			    cameraImage.setImageBitmap(image);
			    //pImgBean.setBase64ConvrtedImg(base64string);
				//Okler.getInstance().getPrescriptionsDataBeans().getPresImages().add(pImgBean);
		//		Intent newIntent = new Intent(this,PrescriptionPreview.class);
		//		newIntent.putExtra("imageFilePath", imgInfo.getFilePath());
		//		newIntent.putExtra("imgFileName",imgInfo.getFileName());
				//newIntent.putExtra("flag",flag);//******* 24112015
		//		startActivity(newIntent);
				}
				
				if(resultCode == RESULT_CANCELED)
				{
					/*Toast.makeText(getApplicationContext(),
							"User cancelled image capture", Toast.LENGTH_SHORT)
							.show();*/
				}
			}
			else if (requestCode == RC_SIGN_IN || requestCode == 34464) 
			  {
				  //For google
			    mIntentInProgress = false;
			    if (!SocialMediaUtils.mGoogleApiClient.isConnected()) {
			     SocialMediaUtils.mGoogleApiClient.connect();
			    }
			  }
			  else
			  {
				  //For FB
				  callbackManager.onActivityResult(requestCode, resultCode, data);			
			  }
		
		}

	/*public void open(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyPic-"+ System.currentTimeMillis() + ".jpg");
		SelectedImage = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, SelectedImage);
		startActivityForResult(intent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
 }  */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
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
	public class SignUpAsyncTask extends AsyncTask<String,Object,Object>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url=getString(R.string.registerUser);
			String Result = String.valueOf(Utilities.RegisterNewUser(url,nameValuePairs));
			return Result;
		}
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				JSONObject json=new JSONObject(result.toString());
				String messageReceived=json.getString("message");
				if(messageReceived.equals("User registration successful."))
				{
					new VerifyPhoneNumDialog(actr,phoneNumber).show(getFragmentManager(),"");
					//Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();
					
					int uid = json.getInt("user_id");
					String userimage = json.optString("profile_url");
					userBean=Okler.getInstance().getuDataBean();	
					userBean.setId(uid);
					userBean.setUser_image(userimage);
					Utilities.writeCurrentUserToSharedPref(actr,userBean);
					/*Intent success=new Intent(SignUp.this,ServiceCategoryActivity.class);
					startActivity(success);*/
				}else{
			
					Toast.makeText(getApplicationContext(),messageReceived,Toast.LENGTH_LONG).show();
				}
					
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("IN Post execute"+result);		
		}
	}
}