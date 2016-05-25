package com.okler.Profile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.okler.android.BaseActivity;

import com.okleruser.R;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.Utilities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileChangePassword extends BaseActivity {
	
	Button btnSave, btnCancel;
	Toolbar toolBar;
	View bottomBarLayout;
	ImageView imgBack;
	TextView textemail, textphone;
	EditText edtTxt_old_password, edtTxt_new_password, edtTxt_retype_password;
	int userid;
	boolean flag;
	private EditText editTextToValidate;
	TextValidations text ;
	String old_pass, new_pass, retype_pass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profile_change_password);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		
		textemail = (TextView) findViewById(R.id.text_email);
		textphone = (TextView) findViewById(R.id.text_phone);		
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		final ActionBar ab=getSupportActionBar();
		//ab.setDisplayHomeAsUpEnabled(true);
		
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(this);
	    userid = ubean.getId();
	  	String phone = ubean.getPhone();
	  	String email = ubean.getEmail();
	  	
	  	textemail.setText(email);
	  	textphone.setText(phone);
		
		edtTxt_old_password = (EditText) findViewById(R.id.edtTxt_old_password);
		edtTxt_new_password = (EditText) findViewById(R.id.edtTxt_new_password);
		edtTxt_retype_password = (EditText) findViewById(R.id.edtTxt_retype_password);
		
		btnCancel = (Button) findViewById(R.id.button_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
				
			}
		});
		btnSave = (Button) findViewById(R.id.button_save);
		
		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				
				/*flag=validateOldPassword();
				if(flag==true){
					old_pass = edtTxt_old_password.getText().toString();
				
						}
				flag=validateNewPassword();
				if(flag==true){
					new_pass = edtTxt_new_password.getText().toString();
				}
				flag=validateRetypePassword();
				if(flag==true){
					retype_pass = edtTxt_retype_password.getText().toString();
					}
				else
				{
					Toast.makeText(getApplicationContext(), "Password do not match", Toast.LENGTH_LONG).show();
				}
				
				if(flag == false)
				{
				}
				else
				{*/
				
				TextValidations tval;
				flag=false;
					tval = new TextValidations(edtTxt_old_password);
				if(tval.validateText("Please Enter old Password")){
					flag=false;
					tval = new TextValidations(edtTxt_new_password);
				if(tval.ValidateMinimumChars("Please Enter atleast 6 characters", 6)){
					flag=false;
					tval= new TextValidations(edtTxt_retype_password);
				if(tval.validateText("Please Retype Old Password")){
					flag=false;
					old_pass = edtTxt_old_password.getText().toString();
					new_pass = edtTxt_new_password.getText().toString();
					retype_pass = edtTxt_retype_password.getText().toString();
					if(!new_pass.equals(retype_pass)){
						edtTxt_retype_password.setText("");
						edtTxt_retype_password.setError("Retyped Password should be equal new Password");
						flag=false;
					}else{
						if(old_pass.equals(new_pass)||old_pass.equals(retype_pass)){
							edtTxt_new_password.setText("");
							edtTxt_retype_password.setText("");
							edtTxt_new_password.setError("Old and New Password should not be same");
							edtTxt_new_password.requestFocus();
							flag=false;
						}else{
						flag=true;
						}
					}
				}
				}
				}
				/*else{
					flag=true;
				}
				
				*
				*/
				if(flag){
				String newPwd1="",oldPwd1="",p="";
				
				try {
					p = URLEncoder.encode("","UTF-8");
					if(new_pass==null)
						new_pass="";
					newPwd1 = URLEncoder.encode(new_pass,"UTF-8");
					if(old_pass==null)
						old_pass="";
					oldPwd1 = URLEncoder.encode(old_pass,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(flag){
				String change_pass = "https://www.okler.com/api/users/changepassword?user_id="+userid+"&newpwd="+newPwd1+"&oldpwd="+oldPwd1;
				Log.e("URL", change_pass);
				WebJsonObjectRequest webjson=new WebJsonObjectRequest(Method.GET, change_pass, new JSONObject(),new Listener<JSONObject>() 
						{
							@Override
							public void onResponse(JSONObject response) 
							{
								// TODO Auto-generated method stub
								
								try
								{
								JSONObject responseObj =(JSONObject)response;
								String result = responseObj.getString("result");
								String message = responseObj.getString("message");
								Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
								if(message.equals("password changed successfully"))
								{
									finish();
								}
								
								}catch(JSONException jsonEx)
								{
									Log.e("Exception json", jsonEx.getStackTrace().toString());
								}
						
							}}, 
							new Response.ErrorListener() 
							{

								@Override
								public void onErrorResponse(VolleyError error) 
								{
									Log.i("error", String.valueOf(error));
									// TODO Auto-generated method stub
						
								}
							}
				);
			
		VolleyRequest.addJsonObjectRequest(getApplicationContext(),webjson);
				}
				else{
					
				}
			}
			//}
			}});
		
		toolBar.setBackgroundColor(Color.BLUE);
		
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	      Utilities.setTitleText(toolBar, "Change Password");
	      
	      
	  	
	}
	
	public boolean validateOldPassword()
	{
		this.editTextToValidate = edtTxt_old_password;
		text = new TextValidations(editTextToValidate);
		return text.validateText("Please Enter password");
	}
	
	public boolean validateNewPassword()
	{
		this.editTextToValidate = edtTxt_new_password;
		text = new TextValidations(editTextToValidate);
		return text.validateText("Please Enter password");
	}
	
	public boolean validateRetypePassword()
	{
		this.editTextToValidate = edtTxt_retype_password;
		text = new TextValidations(editTextToValidate);
		if(new_pass.equals(retype_pass))
		{
			return true;
		}
		else
		{
			 text.validateText("Password do not match");
			 return false;
		}
		
	}

}
