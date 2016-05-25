package com.okler.dialogs;

import com.okleruser.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MyOrderCancelConfirmationDialog extends DialogFragment{
	Context receivedContext;
	RelativeLayout okLayout;
	public MyOrderCancelConfirmationDialog(Context context,String orderId) {
		// TODO Auto-generated constructor stub
	this.receivedContext=context;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
		View view=getActivity().getLayoutInflater().inflate(R.layout.dialog_my_order,null);
		builder.setView(view);
		final AlertDialog alert = builder.create();
		
		okLayout=(RelativeLayout) view.findViewById(R.id.tv_ok);
		okLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alert.dismiss();
			}
		});
		alert.show();
		return alert;
	}
}
