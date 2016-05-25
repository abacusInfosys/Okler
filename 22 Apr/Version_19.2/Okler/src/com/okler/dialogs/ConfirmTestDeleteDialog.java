package com.okler.dialogs;


import java.util.ArrayList;

import com.okleruser.R;
import com.okler.databeans.DiagnoLabsDataBean;
import com.okler.databeans.DiagnoOrderDataBean;
import com.okler.databeans.TestDataBean;
import com.okler.diagnostics.DiagnoOrderSummary;
import com.okler.utils.Okler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ConfirmTestDeleteDialog extends DialogFragment{
			Context con;
			int id;
			LinearLayout parentLayout;
			DiagnoOrderDataBean dobean = new DiagnoOrderDataBean();
			DiagnoLabsDataBean labbean = new DiagnoLabsDataBean();
			ArrayList<TestDataBean> tList = new ArrayList<TestDataBean>();
			DiagnoOrderSummary dsum = new DiagnoOrderSummary();
			
			String pickupType;
		public ConfirmTestDeleteDialog(Context context,int gotid,LinearLayout linLayout,String pick){
			this.con = context;
			this.id = gotid;
			this.parentLayout = linLayout;
			this.pickupType = pick;
		}
		
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
			
		AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_confirm_test_delete, null);
		dlg.setView(view)
		.setTitle("Confirm Delete")
		.setPositiveButton("Delete", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int tagV = id;
				
				dobean = Okler.getInstance().getDiagnoOrderDataBean();
				labbean = dobean.getSelectedLab();
				tList = labbean.getTestBean();
				tList.remove(id);
			//	Toast.makeText(con, "reached", Toast.LENGTH_LONG).show();
				
				labbean.setTestBean(tList);
				dobean.setSelectedLab(labbean);
				Okler.getInstance().setDiagnoOrderDataBean(dobean);
				//parentLayout.removeAllViews();
				//dsum.setTestUi();
				//int count1 =parentLayout.getChildCount();
				//View vtoRem = parentLayout.findViewWithTag(""+tagV);
				//parentLayout.removeView(vtoRem);
				
				//int count2 = parentLayout.getChildCount();
				dismiss();
				
			}
		})
		
		.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
				
			}
		}).create();
	
		
		
		return dlg.create();
	}	
}
