package com.okler.dialogs;

import com.okler.android.ProductCheckoutSummary;
import com.okleruser.R;
import com.okler.databeans.CartDataBean;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ConfirmPrescDialog extends DialogFragment {

	TextView msg;
	String prescId;
	boolean flgPending;
	Context con;
	int check;

	public ConfirmPrescDialog(Context context, String pId, boolean flag) {
		this.prescId = pId;
		this.con = context;
		flgPending = flag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		check = getActivity().getIntent().getIntExtra("Check", 11);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_med_presc, null);
		msg = (TextView) view.findViewById(R.id.prIddlg);
		msg.setText("Prescription ID: " + prescId);
		
		if(flgPending == false)
		{
			builder.setView(view)
			.setTitle("You cannot upload pending/rejected prescription")
			.setPositiveButton("Ok", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					dismiss();
				}
			}).create();
		}
		else
		{
			builder.setView(view)
			.setTitle("Do you want to Upload this Prescripion?")
			.setPositiveButton("Upload", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (check == 1) {
							CartDataBean cbean = Utilities.getCartDataBean(getActivity()); /*Okler.getInstance()
									.getSingleCart();*/
							cbean.setPresc_id(prescId);
							Okler.getInstance().setSingleCart(cbean);
							Utilities.writeCartToSharedPref(getActivity(), cbean);
						} else {
							CartDataBean cbean = Utilities.getCartDataBean(getActivity()); /*Okler.getInstance()
									.getMainCart();*/
							cbean.setPresc_id(prescId);
							Okler.getInstance().setMainCart(cbean);
							Utilities.writeCartToSharedPref(getActivity(), cbean);
						}
						Intent intent = new Intent(con,
								ProductCheckoutSummary.class);
						intent.putExtra("Check", check);
						startActivity(intent);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Only dismiss the dialog
								dismiss();
							}
						}).create();
		}
		return builder.create();
	}
}
