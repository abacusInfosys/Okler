package com.okler.dialogs;
import com.okler.android.HealthShopGrid;
import com.okleruser.R;
import com.okler.android.ServiceCategoryActivity;
import com.okler.android.ServicesCategories;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
public class OrderPlacedConfirmationDialog extends DialogFragment {
	private TextView orderId;

	/* (non-Javadoc)
	 * @see android.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	private String orderIdReceived;
	Context receivedContext;
	
	public OrderPlacedConfirmationDialog(Context context, String orderId) {
		// TODO Auto-generated constructor stub
		this.orderIdReceived=orderId;
		this.receivedContext=context;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view=getActivity().getLayoutInflater().inflate(R.layout.dialog_order_confirm, null);
		builder.setView(view);
		//.setTitle("OKLER");
		
		
		/*.setPositiveButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
                //do things 
        	   Intent startHome = new Intent(receivedContext, ServicesCategories.class);
        	   startActivity(startHome);
        	   dismiss(); 
           }*/
   
		final AlertDialog alert = builder.create();
		orderId=(TextView) view.findViewById(R.id.orderIdValue);
		orderId.setText(orderIdReceived);
		Button next=(Button) view.findViewById(R.id.new_order_button);
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent startHome = new Intent(receivedContext, ServiceCategoryActivity.class);
	        	 startHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
				 startActivity(startHome);
	        	   alert.dismiss();
			}
		});
		alert.show();
		alert.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Intent startHome = new Intent(receivedContext, ServiceCategoryActivity.class);
	        	 startHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
				 startActivity(startHome);
			}
		});
		/*alert.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Intent startHome = new Intent(receivedContext, ServiceCategoryActivity.class);
	        	 startHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
				 startActivity(startHome);
			}
		});*/
		return alert;
	}
	
	

}
