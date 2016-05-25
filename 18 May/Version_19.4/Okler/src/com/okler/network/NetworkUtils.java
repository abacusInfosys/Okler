package com.okler.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	 public static boolean checkNetworkAvailable(Context paramContext)
	  {
	    NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
	    if (localNetworkInfo == null) {
	    	return false;
	    }
	    while ((!localNetworkInfo.isConnected()) || (!localNetworkInfo.isAvailable())) {
	      return false;
	    }
	    return true;
	  }
}
