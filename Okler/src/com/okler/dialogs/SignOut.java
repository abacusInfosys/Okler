package com.okler.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.okler.android.ProductCheckoutSummary;
import com.okleruser.R;
import com.okler.android.ServiceCategoryActivity;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.utils.Okler;
import com.okler.utils.SocialMediaUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;

public class SignOut extends DialogFragment {

	public SignOut() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final Activity currActivity = getActivity();

		AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_signout_confirm, null);
		dlg.setView(view)

				// Set Dialog Title
				.setTitle("Confirm Logout")

				// Positive button
				.setPositiveButton("Logout",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Remove the custom view from linear layout

								UserStatusEnum usrStatus = Utilities
										.getUserStatusFromSharedPref(currActivity);

								switch (usrStatus.ordinal()) {
								case 1: // log out from our own system
									Utilities.writeUserStatusToSharedPref(
											currActivity,
											UserStatusEnum.LOGGED_OUT);
									UsersDataBean ubean = new UsersDataBean();
									Utilities.writeCurrentUserToSharedPref(
											currActivity, ubean);
									Okler.getInstance().getMainCart()
											.getProdList().clear();
								case 2:// google log out
									SocialMediaUtils.signOutOfGoogle();
									// After successful logout- write to shared
									// pref value
									Utilities.writeUserStatusToSharedPref(
											currActivity,
											UserStatusEnum.LOGGED_OUT);
									ubean = new UsersDataBean();
									Utilities.writeCurrentUserToSharedPref(
											currActivity, ubean);
									Okler.getInstance().getMainCart()
											.getProdList().clear();
									break;
								case 3:// fb log out
									SocialMediaUtils.signOutOfFB(currActivity);
									Utilities.writeUserStatusToSharedPref(
											currActivity,
											UserStatusEnum.LOGGED_OUT);
									ubean = new UsersDataBean();
									Utilities.writeCurrentUserToSharedPref(
											currActivity, ubean);
									Okler.getInstance().getMainCart()
											.getProdList().clear();
									// final SocialMediaUtils scUtils = new
									// SocialMediaUtils(currActivity,false);
									// scUtils.signOutOfFB(currActivity);
									break;
								}

								Toast.makeText(
										getActivity().getApplicationContext(),
										"You have successfully logged Out",
										Toast.LENGTH_LONG).show();
								Intent newActivity = new Intent(getActivity(),
										ServiceCategoryActivity.class);
								// newActivity.putExtra("openDrawer", true);
								newActivity
										.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								getActivity().finish();
								startActivity(newActivity);
								dismiss();
								// Need to rewrite otp status because if user
								// logs out and tried to Sign Up again, otp must
								// be verified again
								Utilities.writeStringToSharedPref(getActivity()
										.getApplicationContext(), "otpStatus",
										"NotVerfied");
							}
						})

				// Negative Button
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Only dismiss the dialog
								dismiss();
							}
						}).create();

		return dlg.create();
	}
}