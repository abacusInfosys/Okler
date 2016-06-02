package com.okler.dialogs;

import com.okler.adapters.OrderCancelAdapter;
import com.okleruser.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class OrderCancellationDialog extends DialogFragment {
	String names[] = { "A", "B", "C", "D" };
	Context context = getActivity();
	RelativeLayout cancel;
	String orderId = "OK123456789";

	/*
	 * public OrderCancellationDialog() { // TODO Auto-generated constructor
	 * stub names[] ={"A","B","C","D"}; }
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_cancel_order, null);
		builder.setView(view).setTitle("Cancellation Request");
		final AlertDialog alert = builder.create();
		ListView lv = (ListView) view.findViewById(R.id.reasonToCancel);
		OrderCancelAdapter adapter = new OrderCancelAdapter(context, names);
		lv.setAdapter(adapter);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alert.dismiss();
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				new MyOrderCancelConfirmationDialog(context, orderId);
			}
		});
		alert.show();
		return alert;
	}
}
