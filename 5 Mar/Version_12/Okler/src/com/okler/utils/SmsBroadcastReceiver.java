package com.okler.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsBroadcastReceiver extends BroadcastReceiver{

	// Get the object of SmsManager
	final SmsManager sms = SmsManager.getDefault();
	@Override
	public void onReceive(Context context, Intent intent) {		
		 // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        try {            
            if (bundle != null) {                 
                final Object[] pdusObj = (Object[]) bundle.get("pdus");                 
                for (int i = 0; i < pdusObj.length; i++) {                     
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();                     
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody(); 
                    String otp=Utilities.getOtpFromSharedPref(context);
                    boolean flag =message.contains(otp);
                    System.out.println("otpflag"+flag);
                    if(flag==true){
                    	Utilities.writeStringToSharedPref(context,"otpStatus","Verfied");
                    }else{
                    	Utilities.writeStringToSharedPref(context,"otpStatus","NotVerfied");	
                    }
                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);            
 
                   // Show Toast - change this later to 
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();                     
                } // end for loop
              } // bundle is null
 
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
             
        }
	}


}
