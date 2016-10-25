package com.okler.dialogs;

import org.json.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.okler.android.SearchHistoryActivity;
import com.okler.databeans.UsersDataBean;
import com.okler.interfaces.ClearHistoryCallback;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

public class ClearHistoryDialog extends DialogFragment{
	Activity ack;
	int term_type_value;
	Button button_clear;
	RadioButton prod_type,allHIstory;
	String clearHistoryUrl="",serverUrl="",clearPart1="",term_type="",term_Value="";
	UsersDataBean ubean;
	int userId; 
	ClearHistoryCallback clHistory;
	public ClearHistoryDialog(Activity ack1,int term_type_value1) {
		this.ack = ack1;
		this.term_type_value = term_type_value1;
		clHistory = (ClearHistoryCallback)ack1;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(ack);
		View view = ack.getLayoutInflater().inflate(R.layout.dialog_clear_history, null);
		button_clear = (Button)view.findViewById(R.id.button_clear);
		prod_type = (RadioButton)view.findViewById(R.id.prod_type);
		allHIstory = (RadioButton)view.findViewById(R.id.allHIstory);
		serverUrl = ack.getString(R.string.serverUrl);
		clearPart1 = ack.getString(R.string.clearSearchHistoryPart1);
		term_type = ack.getString(R.string.term_type);
		ubean = Utilities.getCurrentUserFromSharedPref(ack);
		userId = ubean.getId();
		prod_type.setChecked(true);
		
		if(term_type_value==1){
			button_clear.setBackgroundColor(ack.getResources().getColor(R.color.ModerateMagenta));
			prod_type.setText("Diagnostic-Test");
		}
		if(term_type_value==2){
			button_clear.setBackgroundColor(ack.getResources().getColor(R.color.ModerateMagenta));
			prod_type.setText("Diagnostic-Package");
		}
			
		if(term_type_value==3){
			button_clear.setBackgroundColor(ack.getResources().getColor(R.color.YellowGreen));
			prod_type.setText("Ayurvedic");
		}
		
		if(term_type_value==4){
			button_clear.setBackgroundColor(ack.getResources().getColor(R.color.YellowGreen));
			prod_type.setText("Allopathy");
		}
		if(term_type_value==5){
			button_clear.setBackgroundColor(ack.getResources().getColor(R.color.YellowGreen));
			prod_type.setText("Homeopathy");
		}
		if(term_type_value==6){
			button_clear.setBackgroundColor(Color.parseColor("#FCAB12"));
			prod_type.setText("Healthproduct");
		}
		if(term_type_value==8){
			button_clear.setBackgroundColor(ack.getResources().getColor(R.color.ModerateMagenta));
			prod_type.setText("Diagnostic-Disease");
		}
		
		button_clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(prod_type.isChecked()){
					term_Value = ""+term_type_value;
				}else{
					term_Value="";
				}
				clearHistoryUrl = serverUrl+clearPart1+userId+term_type+term_Value;
				
				WebJsonObjectRequest clearJson = new WebJsonObjectRequest(Method.GET, clearHistoryUrl, new JSONObject(), new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						
						Utilities.writeToLogFIle("Clear History "+response.optString("message"));
						Intent intent = new Intent(ack,SearchHistoryActivity.class);
						intent.putExtra("term_type_value", term_type_value);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						ack.startActivity(intent);
						clHistory.disableClearHistory();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Utilities.writeToLogFIle("Clear History in Errorrespose "+error.toString());
						Intent intent = new Intent(ack,SearchHistoryActivity.class);
						intent.putExtra("term_type_value", term_type_value);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						ack.startActivity(intent);
					}
				});
				VolleyRequest.addJsonObjectRequest(ack, clearJson);
			}
		});
		
		dialog.setView(view);
		
		
		
		return dialog.show();
	}
}
