package com.okler.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


public class VolleyRequest {
	private static VolleyRequest instance;
	  public static final int MAX_RETRIES = 0;
	  public static final int TIMEOUT_VOLLEY = 15000;
	  private static RequestQueue mRequestQue; 
	 public ImageLoader mImageLoader;
	  
	  public static VolleyRequest getInstance(Context context) {
	        if (instance == null) {
	            instance = new VolleyRequest(context);
	        }
	        return instance;
	    }
	  private VolleyRequest(Context context)
	  {
		  if (mRequestQue == null) {
		      mRequestQue = Volley.newRequestQueue(context.getApplicationContext());
		    }
	  }
	  public static boolean addJsonArrayRequest(Context paramContext, WebJSonArrayReqest jsonArrayRequest)
	  {	   
	    try
	    {
	      if (NetworkUtils.checkNetworkAvailable(paramContext))
	      {
	       getRequestQueue(paramContext).add(jsonArrayRequest);	      
	        return true;
	      }	  
	      else
	    	  Toast.makeText(paramContext, "Check network connection", Toast.LENGTH_LONG).show();
	      return false;
	    }
	    catch (Exception localException)
	    {
	      localException.printStackTrace();
	      return false;
	    }
	  }
	  
	  
	  
	  
	  public static boolean addJsonObjectRequest(Context paramContext, WebJsonObjectRequest jsonObjRequest)
	  {	   
	    try
	    {
	      if (NetworkUtils.checkNetworkAvailable(paramContext))
	      {
	    //	  getRequestQueue(paramContext).getCache().get("https://www.okler.com/api/products/usercart/cartitems?cust_id=1285").serverDate;
	        getRequestQueue(paramContext).add(jsonObjRequest);	      
	        return true;
	      }
	      else
	    	  Toast.makeText(paramContext, "Check network connection", Toast.LENGTH_LONG).show();	     
	    }
	    catch (Exception localException)
	    {
	      localException.printStackTrace();	     
	    }
	    return false;
	  }
	  
	  public static boolean addBooleanRequest(Context paramContext, WebBooleanRequest boolRequest)
	  {	   
	    try
	    {
	      if (NetworkUtils.checkNetworkAvailable(paramContext))
	      {
	        getRequestQueue(paramContext).add(boolRequest);	      
	        return true;
	      }
	      else
	    	  Toast.makeText(paramContext, "Check network connection", Toast.LENGTH_LONG).show();	     
	    }
	    catch (Exception localException)
	    {
	      localException.printStackTrace();	     
	    }
	    return false;
	  }
	  
	  public static Boolean addStringRequest(Context paramContext, WebStringRequest boolRequest)
	  {	   
	    try
	    {
	      if (NetworkUtils.checkNetworkAvailable(paramContext))
	      {
	        getRequestQueue(paramContext).add(boolRequest);	      
	        return true;
	      }
	      else
	    	  Toast.makeText(paramContext, "Check network connection", Toast.LENGTH_LONG).show();	     
	    }
	    catch (Exception localException)
	    {
	      localException.printStackTrace();	     
	    }
	    return false;
	  }
	  
	  private void initImageLoader()
	  {
		  mImageLoader = new ImageLoader(mRequestQue,
            new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap>
                        cache = new LruCache<String, Bitmap>(20);

                @Override
                public Bitmap getBitmap(String url) {
                    return cache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url, bitmap);
                }
            });
	  }
	  
	  public static void cancelRequest(WebJsonObjectRequest wjor)
	  {
	    if (wjor != null) {
	    	wjor.cancel();
	    }
	  }
	  
	  public static void cancelRequest(WebJSonArrayReqest wjar)
	  {
	    if (wjar != null) {
	    	wjar.cancel();
	    }
	  }
	  
	  public static void cancelRequest(WebBooleanRequest boolObj)
	  {
	    if (boolObj != null) {
	    	boolObj.cancel();
	    }
	  }
	  
	  public static void clearCache()
	  {
	    if (mRequestQue != null) {
	      mRequestQue.getCache().clear();
	    }
	  }
	  
	  private static RequestQueue getRequestQueue(Context paramContext)
	  {
	     if (mRequestQue == null) {
		      mRequestQue = Volley.newRequestQueue(paramContext.getApplicationContext());
		    }
	    return mRequestQue;
	  }  
	  
	  public ImageLoader getImageLoader()
	  {
		  initImageLoader();
		  return mImageLoader;
	  }
}
