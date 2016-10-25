package com.okler.dialogs;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.okleruser.R;

public class VersionUpdateDialog  extends DialogFragment {
	
	Button okbutton;
	Activity actCntx;
	static boolean otpReceivedFlag = false;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	ArrayList<NameValuePair> resetPassNameValPair = new ArrayList<NameValuePair>();
	boolean isFromNewSignIn = false;
	EditText ed_phoneNumberED;
	TextView viewPhoneNumber, tv_secondLine, tv_firstLine;

	public VersionUpdateDialog() {
	}

	public VersionUpdateDialog(Activity context) {

		actCntx = context;
		
		//isFromNewSignIn = isFromNewSignin;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_updated_version, null);
		builder.setView(view);
		final AlertDialog alert = builder.create();
		okbutton = (Button) view.findViewById(R.id.btn_update);
		

		okbutton.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(Intent.ACTION_VIEW);
				Uri Uri = null;
				intent.setData(Uri.parse("market://details?id=com.okleruser"));// 11_01_2016
																				// Gitesh
				if (!MyStartActivity(intent)) {
					// Market (Google play) app seems not installed, let's try to
					// open a webbrowser
					intent.setData(Uri
							.parse("https://play.google.com/store/apps/details?id=com.okleruser"));// 11_01_2016
																									// Gitesh
					if (!MyStartActivity(intent)) {
						// Well if this also fails, we have run out of options,
						// inform the user.
						// Toast.makeText(this,
						// "Could not open Android market, please install the market app.",
						// Toast.LENGTH_SHORT).show();
					}
				}
				
				//Toast.makeText(actCntx, "Application Updated ... ", Toast.LENGTH_SHORT).show();
			}

		});
		alert.setCancelable(false);

		return alert;

	}
	
	private boolean MyStartActivity(Intent aIntent) {
		try {
			startActivity(aIntent);
			
			return true;
		} catch (ActivityNotFoundException e) {
			return false;
		}
	}
}
