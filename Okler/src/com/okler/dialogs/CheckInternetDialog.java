package com.okler.dialogs;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.okleruser.R;

public class CheckInternetDialog  extends DialogFragment {
	
	Button exitbutton, settingbutton;
	Activity actCntx;
	static boolean otpReceivedFlag = false;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	ArrayList<NameValuePair> resetPassNameValPair = new ArrayList<NameValuePair>();
	boolean isFromNewSignIn = false;
	EditText ed_phoneNumberED;
	TextView viewPhoneNumber, tv_secondLine, tv_firstLine;

	public CheckInternetDialog() {
	}

	public CheckInternetDialog(Activity context) {
		actCntx = context;
		//isFromNewSignIn = isFromNewSignin;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_check_internet, null);
		builder.setView(view);
		final AlertDialog alert = builder.create();
		exitbutton = (Button) view.findViewById(R.id.btn_exit);
		settingbutton = (Button) view.findViewById(R.id.btn_settings);
		exitbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.exit(0);
			}
		});
		
		settingbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				settingbutton.setText("Retry");
				
				actCntx.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
				
			}
		});
		alert.setCancelable(false);
		return alert;
	}
}
