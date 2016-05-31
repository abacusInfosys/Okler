package com.okler.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.JustViewedProdsDataBean;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.UserStatusEnum;
import com.okler.*;
import com.okler.android.PrescriptionDelivery;
import com.okleruser.R;
import com.okler.databeans.PrescriptionsDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;

public class Utilities {
	
	  private static final int ID_GALLERY = 1;
	    private static final int ID_CAMERA = 2;
	    private static final int GALLERY_PICTURE = 100;
	    private static final int CAMERA_PICTURE = 101;
	    static File photoFile = null;
	    static Intent pictureActionIntent;
	    static Typeface typeFace;
	//create image to internal storage
		public static File createImageFile() throws IOException {
		    // Create an image file name
			Utilities.writeToLogFIle("createImageFile() ");
		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		    Utilities.writeToLogFIle("createImageFile() timestamp "+timeStamp);
		    String imageFileName = "JPEG_" + timeStamp + "_";
 Utilities.writeToLogFIle("createImageFile() imagefilename "+imageFileName);
 

		    File storageDir = Environment.getExternalStoragePublicDirectory(
		            Environment.DIRECTORY_PICTURES);
					Utilities.writeToLogFIle("createImageFile() storageDir "+ storageDir);
 //Remove below code later. Added to test redmi upload crash
 /*File storageDir2 = new File("/sdcard/Okler");
		    Utilities.writeToLogFIle("createImageFile() Storagedir2 "+storageDir2);
		    boolean isCreated = storageDir2.mkdirs();
		    Utilities.writeToLogFIle("createImageFile() mkdirs "+storageDir2);
		    Utilities.writeToLogFIle("createImageFile() isCreated = "+isCreated);*/
		    File image = File.createTempFile(
		        imageFileName,  /* prefix */
		        ".jpg",         /* suffix */
		        storageDir      /* directory */
//storageDir2  
		    );
		    Utilities.writeToLogFIle("createImageFile() image "+image);
		    // Save a file: path for use with ACTION_VIEW intents
		  //  mCurrentPhotoPath = image.getAbsolutePath();
		    return image;
		}
	
	//create image to internal storage
		public static File createImageFile(Context context) {
		    // Create an image file name
			File image = null;
			try{
			Utilities.writeToLogFIle("createImageFile() ");
		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		    Utilities.writeToLogFIle("createImageFile() timestamp "+timeStamp);
		    String imageFileName = "JPEG_" + timeStamp + "_";
 Utilities.writeToLogFIle("createImageFile() imagefilename "+imageFileName);
 

		    File storageDir = context.getFilesDir();
		    		//Environment.getExternalStoragePublicDirectory(
		            //Environment.DIRECTORY_PICTURES);
					//Utilities.writeToLogFIle("createImageFile() storageDir "+ storageDir);
 //Remove below code later. Added to test redmi upload crash
 /*File storageDir2 = new File("/sdcard/Okler");
		    Utilities.writeToLogFIle("createImageFile() Storagedir2 "+storageDir2);
		    boolean isCreated = storageDir2.mkdirs();
		    Utilities.writeToLogFIle("createImageFile() mkdirs "+storageDir2);
		    Utilities.writeToLogFIle("createImageFile() isCreated = "+isCreated);*/
		    image = File.createTempFile(
		        imageFileName,  /* prefix */
		        ".jpg",         /* suffix */
		        storageDir      /* directory */
//storageDir2  
		    );
		    Utilities.writeToLogFIle("createImageFile() image "+image);
		    // Save a file: path for use with ACTION_VIEW intents
		  //  mCurrentPhotoPath = image.getAbsolutePath();
		    
			}catch(Exception e){
				Utilities.writeToLogFIle("captureImageFromCamera(Fragment exception"+String.valueOf(e.getStackTrace()));
	        	Utilities.writeToLogFIle("captureImageFromCamera(Fragment exception"+e.getStackTrace().toString());
	        	Utilities.writeToLogFIle("captureImageFromCamera(Fragment exception"+e.toString());
	        	Utilities.writeToLogFIle("Message"+e.getLocalizedMessage());
			}
			return image;
		   
		}	
		public static String saveToInternalSorage(Bitmap bitmapImage,Context context,String dirPath,String fileName){
	        ContextWrapper cw = new ContextWrapper(context);
	         // path to /data/data/yourapp/app_data/dirPath
	        File directory = cw.getDir(dirPath, Context.MODE_PRIVATE);
	        // Create imageDir
	        File mypath=new File(directory,fileName);

	        FileOutputStream fos = null;
	        try {           
	            fos = new FileOutputStream(mypath);
	       // Use the compress method on the BitMap object to write image to the OutputStream
	            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
	            fos.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return directory.getAbsolutePath();
	    }
		
		//Retrieve image bitmap from internal storage
		public static Bitmap getImageBitmapFromStorage(String filePath,String fileName)
		{
			Bitmap imgBitmap = null;
		   //File f=new File(filePath);//, fileName);
		    imgBitmap = Utilities.decodeSampledBitmapFromFile(filePath, 100,100);	   
		    return imgBitmap;
		}
		
		public static int calculateInSampleSize(
	            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;

	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }

	    return inSampleSize;
	 }
		
		public static Bitmap decodeSampledBitmapFromBytes(byte[] bytes, int reqWidth, int reqHeight) {

		    // First decode with inJustDecodeBounds=true to check dimensions
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeByteArray(bytes,0, bytes.length,options);

		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,options);
		}
		
		public static Bitmap decodeSampledBitmapFromFile(String filePath,int reqWidth, int reqHeight)
		{
			   // First decode with inJustDecodeBounds=true to check dimensions
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(filePath, options);

		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    return BitmapFactory.decodeFile(filePath, options);
		}
		
		public static void showImageFromGallery(Fragment c)
		{
			 pictureActionIntent = new Intent(
	               Intent.ACTION_GET_CONTENT, null);
	       pictureActionIntent.setType("image/*");
	       pictureActionIntent.putExtra("return-data", true);
	       c.startActivityForResult(pictureActionIntent,GALLERY_PICTURE);
		}
	  
	  	public static void captureImageFromCamera(Fragment context)
		{
	  		Utilities.writeToLogFIle("");
	  		Utilities.writeToLogFIle("captureImageFromCamera(Fragment "+context.toString());
			    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    Utilities.writeToLogFIle("captureImageFromCamera(Fragment "+takePictureIntent.toString());
			    // Ensure that there's a camera activity to handle the intent
			    if (takePictureIntent.resolveActivity(context.getActivity().getPackageManager()) != null) {
			    	Utilities.writeToLogFIle("captureImageFromCamera(Fragment intent!=null");
			        // Create the File where the photo should go
			      //  File photoFile = null;
			        try {
					//Commented for testing upload crash issue on Asus
			            photoFile =Utilities.createImageFile();
//						photoFile =Utilities.createImageFile(context.getActivity().getApplicationContext());
			            Utilities.writeToLogFIle("captureImageFromCamera(Fragment "+photoFile);
			        } catch (Exception ex) {
			            // Error occurred while creating the File
			        	Utilities.writeToLogFIle("captureImageFromCamera(Fragment exception"+String.valueOf(ex.getStackTrace()));
			        	Utilities.writeToLogFIle("captureImageFromCamera(Fragment exception"+ex.getStackTrace().toString());
  	Utilities.writeToLogFIle("captureImageFromCamera(Fragment exception"+ex.toString());
			           Log.d("Image File Exc", ex.getStackTrace().toString());
			        }
			        // Continue only if the File was successfully created
			        if (photoFile != null) {
			        	Utilities.writeToLogFIle("captureImageFromCamera(Fragment photofile!=null");
			        	Utilities.writeToLogFIle("captureImageFromCamera(Fragment photofile = "+photoFile);
			        	Utilities.writeToLogFIle("Your android build info is:"+android.os.Build.BRAND + "/" + android.os.Build.PRODUCT + "/"
			                    + android.os.Build.DEVICE);
			            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
			                    Uri.fromFile(photoFile));
			            Utilities.writeToLogFIle("captureImageFromCamera(Fragment putextra");
			           context.startActivityForResult(takePictureIntent, CAMERA_PICTURE);
			           Utilities.writeToLogFIle("captureImageFromCamera(Fragment startactivity "+takePictureIntent.toString());
			        }
			    }
		}
	  	
	  	 //This is for activity
	  	public static void showImageFromGallery(Activity c)
		{
			 pictureActionIntent = new Intent(
	               Intent.ACTION_GET_CONTENT, null);
	       pictureActionIntent.setType("image/*");
	       pictureActionIntent.putExtra("return-data", true);
	       c.startActivityForResult(pictureActionIntent,GALLERY_PICTURE);
		}
	  
	  	//This is for activity
	  	public static void captureImageFromCamera(Activity context)
		{
	  			Utilities.writeToLogFIle("captureImageFromCamera(Activity "+context.toString());
			    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    Utilities.writeToLogFIle("captureImageFromCamera "+takePictureIntent.toString());
			    // Ensure that there's a camera activity to handle the intent
			    if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
			        // Create the File where the photo should go	
			    	Utilities.writeToLogFIle("captureImageFromCamera in if");
			        try {
			        	//commented to debug upload crash on Asus
			            photoFile =Utilities.createImageFile();
				//		  photoFile =Utilities.createImageFile(context.getApplicationContext());
			            Utilities.writeToLogFIle("captureImageFromCamera photofile "+photoFile);
			        } catch (Exception ex) {
			            // Error occurred while creating the File
			        	Utilities.writeToLogFIle("captureImageFromCamera exception"+String.valueOf(ex.getStackTrace()));
			        	Utilities.writeToLogFIle("captureImageFromCamera exception"+ex.getStackTrace().toString());
			        	Log.d("Image File Exc", ex.getStackTrace().toString());
			        }
			        // Continue only if the File was successfully created
			        if (photoFile != null) {
			        	Utilities.writeToLogFIle("captureImageFromCamera photofile!=null");
			        	Utilities.writeToLogFIle("captureImageFromCamera photofile ="+photoFile);
			            
			        	Utilities.writeToLogFIle("Your android build info is:"+android.os.Build.BRAND + "/" + android.os.Build.PRODUCT + "/"
			                    + android.os.Build.DEVICE);
			        	Uri newUri = Uri.fromFile(photoFile);
			        	Utilities.writeToLogFIle("New URI is:"+newUri);
			        	/*Temporary commenting put extra.. to cross check*/
			        	Utilities.writeToLogFIle("Commented extra_output to check culprit");
			        	/*takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
			            		newUri);*/
			            Utilities.writeToLogFIle("captureImageFromCamera putextra");
			           context.startActivityForResult(takePictureIntent, CAMERA_PICTURE);
			           Utilities.writeToLogFIle("captureImageFromCamera startactivity"+takePictureIntent.toString());
			        }
			    }
		}
	  	
		//This method will return object of type - CameraGalleryImageInfo.
	  	//Info will be name and path of the file
	  	public static CameraGalleryImageInfo getImageInfo(int requestCode,int resultCode,Activity context,Intent data)
	  	{
	  		CameraGalleryImageInfo camGalImgInfo = new CameraGalleryImageInfo();
	  		Bitmap thumbnailImg = null;
	  		Uri selectedImageUri = null;
			String imageFilePath = null,fileName = null;
			Calendar c;
			int seconds;
			if (resultCode == Activity.RESULT_OK) {
				if (requestCode == CAMERA_PICTURE) {				
					try {
						Utilities.writeToLogFIle("Before URI:");
						Utilities.writeToLogFIle("Before URI: photofile "+photoFile);
						
						/**************Commenting temporarily to identifyy redn=mi crash*************/
						/*Uri fileUri= Uri.fromFile(photoFile);
						Utilities.writeToLogFIle("URI:"+fileUri);
						imageFilePath=fileUri.getPath();
						Utilities.writeToLogFIle("URI image file path:"+imageFilePath);
						camGalImgInfo.setUri(imageFilePath);
						thumbnailImg=Utilities.decodeSampledBitmapFromFile(imageFilePath, 100, 100);
						Utilities.writeToLogFIle("URI image thumbnail:"+thumbnailImg);
						if(thumbnailImg == null)
							Utilities.writeToLogFIle("URI image thumbnail is null");
						thumbnailImg = rotateImageIfRequired(imageFilePath,thumbnailImg);
						Utilities.writeToLogFIle("URI image thumbnail after rotation");*/
						/**************Commenting temporarily to identifyy redn=mi crash*************/
						
						/**************Uncommenting temporarily to identifyy redn=mi crash*************/
						thumbnailImg = (Bitmap) data.getExtras().get("data"); 
						c = Calendar.getInstance(); 
						seconds = c.get(Calendar.SECOND);
						fileName = seconds+context.getResources().getString(R.string.jpeg_extension);
						imageFilePath = Utilities.saveToInternalSorage(thumbnailImg, context, context.getResources().getString(R.string.PrescriptionImgeasDirPath),fileName );
						
					}catch (Exception e) {
						e.printStackTrace();   
						Utilities.writeToLogFIle("URI thumbnail"+e.getStackTrace());
					} 							
				}			
				else if(requestCode == GALLERY_PICTURE)
				{
					 	selectedImageUri = data.getData();
					 	Utilities.writeToLogFIle("selectedImageUri = "+selectedImageUri);
						camGalImgInfo.setUri(selectedImageUri.toString());
						
					/*	String[] projection = { MediaColumns.DATA };
						CursorLoader cursorLoader = new CursorLoader(context,selectedImageUri, projection, null, null,
						null);
						Cursor cursor =cursorLoader.loadInBackground();
						int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
						cursor.moveToFirst();
						  String imgDecodableString = cursor.getString(column_index);
					//	- See more at: http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample#sthash.Hy7V3hEZ.dpuf
							*/
		                // Get the cursor
						String imgDecodableString="";
						
							
						/*	 final int takeFlags = data.getFlags()
				                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
				                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				    // Check for the freshest data.
							 context.getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);
							  String[] selPathSeg = selectedImageUri.getLastPathSegment().split(":");
							  String id = selPathSeg[0];
								String[] filePathColumn = { MediaStore.Images.Media.DATA };
								 Uri uri = getUri();
								  Cursor imageCursor = context.getContentResolver().query(uri, filePathColumn,
			                                MediaStore.Images.Media._ID + "=" + id, null, null);

								    if (imageCursor.moveToFirst()) {
								    	imgDecodableString = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
								    }
								    Log.e("path",imgDecodableString );
				
					*/
							//String imgDecodableString = getAbsolutePath(context,selectedImageUri);
							String pp = selectedImageUri.getPath();
							File f = new File(pp);
							Utilities.writeToLogFIle("file f "+f);
							 imgDecodableString = f.getAbsoluteFile().toString();
							 Utilities.writeToLogFIle("imgdecodestring "+imgDecodableString);
							   thumbnailImg = Utilities.decodeSampledBitmapFromFile(imgDecodableString, 100, 100);
		                boolean flg = false;
		                if(thumbnailImg == null)
		                {
		                	String[] filePathColumn = { MediaStore.Images.Media.DATA };
		                	for(int i = 0;i<filePathColumn.length;i++)
		                	Utilities.writeToLogFIle("filepathcolumn "+filePathColumn[i]);
			                Cursor cursor = context.getContentResolver().query(selectedImageUri,
			                        filePathColumn, null, null, null);
			                // Move to first row
			                cursor.moveToFirst();	 
			                imgDecodableString = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
			                Utilities.writeToLogFIle("thumb=null imgDecodestring "+imgDecodableString);
			                cursor.close();
			                flg = true;
		                }
		                if(flg)
		                	thumbnailImg = Utilities.decodeSampledBitmapFromFile(imgDecodableString, 100, 100);
		                if(thumbnailImg == null)
		                {
		                	imgDecodableString = getPath(context,selectedImageUri);
		                	Utilities.writeToLogFIle("selectedImgUri "+imgDecodableString);
		                	thumbnailImg = Utilities.decodeSampledBitmapFromFile(imgDecodableString, 100, 100);
		                }
		                
		            	c = Calendar.getInstance(); 
						seconds = c.get(Calendar.SECOND);
		                fileName = seconds+context.getResources().getString(R.string.jpeg_extension);
		                imageFilePath = Utilities.saveToInternalSorage(thumbnailImg, context, context.getResources().getString(R.string.PrescriptionImgeasDirPath),fileName );
		        }
				Utilities.writeToLogFIle("Before ser file name"+fileName);
				camGalImgInfo.setFileName(fileName);
				Utilities.writeToLogFIle("Before ser file path"+imageFilePath);
				camGalImgInfo.setFilePath(imageFilePath);
				Utilities.writeToLogFIle("image "+imageFilePath+fileName+"\n"+thumbnailImg);
				camGalImgInfo.setImgBitmap(thumbnailImg);
			}
	  		return camGalImgInfo;
	  	}
	  	
	 // By using this method get the Uri of Internal/External Storage for Media
	  	private static Uri getUri() {
	  		Utilities.writeToLogFIle("getUri()");
	  	    String state = Environment.getExternalStorageState();
	  	    if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
	  	        return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

	  	  Utilities.writeToLogFIle("internal_uri "+MediaStore.Images.Media.INTERNAL_CONTENT_URI);
	  	Utilities.writeToLogFIle("external_uri "+MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	  	
	  	    return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	  	}
	  	
	  	public static String getAbsolutePath(Activity curAct, Uri uri) {
	  		Utilities.writeToLogFIle("getAbsolutePath()");
	        String[] projection = { MediaColumns.DATA };
	        @SuppressWarnings("deprecation")
	        Cursor cursor = curAct.managedQuery(uri, projection, null, null, null);
	        if (cursor != null) {
	            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
	            cursor.moveToFirst();
	            Utilities.writeToLogFIle("column index"+cursor.getString(column_index));
	            return cursor.getString(column_index);
	        } else
	            return null;
	    }
	  	
	  	private static Bitmap rotateImageIfRequired(String photoPath,Bitmap bitmap)
	  	{
	  		Bitmap bb = bitmap;
	  		ExifInterface ei=null;
			try {
				ei = new ExifInterface(photoPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  		int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

	  		switch(orientation) {
	  		    case ExifInterface.ORIENTATION_ROTATE_90:
	  		        bb= rotateImage(bitmap, 90);
	  		        break;
	  		    case ExifInterface.ORIENTATION_ROTATE_180:
	  		        bb = rotateImage(bitmap, 180);
	  		        
	  		        break;
	  		    // etc.
	  		}
	  		return bb;
	  	}
	  	
	  	private static Bitmap rotateImage(Bitmap source, float angle) {
	  	    Bitmap retVal;

	  	    Matrix matrix = new Matrix();
	  	    matrix.postRotate(angle);
	  	    retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	  	    return retVal;
	  	}
		public static View getSeparatorView(Context context)
	  	{
	  		View view = new View(context);
	  		view.setBackgroundColor(context.getResources().getColor(R.color.separatorViewColor));
	  		LayoutParams params = new LayoutParams();
	  		params.width= LayoutParams.FILL_PARENT;
	  		params.height = 5;
	  		view.setLayoutParams(params);
	  		return view;
	  	}
		
		public static View getBlackSeparatorView(Context context)
	  	{
	  		View view = new View(context);
	  		view.setBackgroundColor(context.getResources().getColor(R.color.Black));
	  		LayoutParams params = new LayoutParams();
	  		params.width= LayoutParams.FILL_PARENT;
	  		params.height = 1;
	  		view.setLayoutParams(params);
	  		return view;
	  	}
		
		  public static Typeface getTypeFace(Context mContext){
		        if(typeFace==null){
		        	typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/ARIALN.TTF");
		        }
		        return typeFace;
		    }
		  
		  private static SharedPreferences getSharedPrefs(Context context)
		    {		    	
		    	SharedPreferences sharedPref = context.getSharedPreferences(
		    	      context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		    	return sharedPref;
		    }
		    
		    private static void writeIntToSharedPref(Activity activity,String key,int value)
		    {
		    	SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext());
		    	SharedPreferences.Editor editor = sharedPref.edit();
		    	editor.putInt(key, value);
		    	editor.commit();
		    }
		    
		    private static void writeIntToSharedPref(Context activity,String key,int value)
		    {
		    	SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext());
		    	SharedPreferences.Editor editor = sharedPref.edit();
		    	editor.putInt(key, value);
		    	editor.commit();
		    }
		    
		    public static String getjustViewProd(Activity activity,int i){
		    	SharedPreferences sharePref=getSharedPrefs(activity.getApplicationContext());
				   String justviewprod=sharePref.getString("currentProd"+i,"NoProd");
				return justviewprod;
		    	
		    }
		   public static void writeTotalCount(Activity activity,String key,int value){			   
			   writeIntToSharedPref(activity,"totalRelatedProduct",value);
			
			}
		   public static int getTotalCount(Activity activity)
		   {
			   SharedPreferences sharePref=getSharedPrefs(activity);
			   int totalNumProd=sharePref.getInt("totalRelatedProduct",0);
			return totalNumProd;
			   
		   }
		    
		    public static void writeStringToSharedPref(Context context,String key,String value)
		    {
		    	SharedPreferences sharedPref = getSharedPrefs(context);
		    	SharedPreferences.Editor editor = sharedPref.edit();
		    	editor.putString(key, value);
		    	editor.commit();
		    }

		    
		    public static void writeBoolToSharedPref(Activity activity,String key,Boolean value)
		    {
		    	SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext());
		    	SharedPreferences.Editor editor = sharedPref.edit();
		    	editor.putBoolean(key, value);
		    	editor.commit();
		    }
		    
		     private static void showProgress(boolean paramBoolean,LinearLayout mProgress)
			  {
			    if (paramBoolean)
			    {
			      mProgress.setVisibility(View.VISIBLE);
			      return;
			    }
			    mProgress.setVisibility(View.INVISIBLE);
			  }
		    
		    public static String convertImageToBase64(Bitmap bitmap)
			{
		    	String ss="";
		    	try
		    	{
		    	 Utilities.writeToLogFIle("convertImageToBase64 line 1"+bitmap);
				 ByteArrayOutputStream stream=new ByteArrayOutputStream();
				 Utilities.writeToLogFIle("convertImageToBase64 line 2"+stream);
				    int heightbefore=bitmap.getHeight();
				    Utilities.writeToLogFIle("convertImageToBase64 line 3"+heightbefore);
				   // System.out.println("Bitmap height"+heightbefore);
				    int widthbefore=bitmap.getWidth();
				    Utilities.writeToLogFIle("convertImageToBase64 line 4"+widthbefore);
				    //System.out.println("bitmap width"+widthbefore);
			        bitmap.compress(Bitmap.CompressFormat.JPEG,90, stream);
			        Utilities.writeToLogFIle("convertImageToBase64 line 5"+bitmap);
				    int height=bitmap.getHeight();
				    Utilities.writeToLogFIle("convertImageToBase64 line 6"+height);
				    //System.out.println("Bitmap height"+height);
				    int width=bitmap.getWidth();
				    Utilities.writeToLogFIle("convertImageToBase64 line 7"+width);
				    //System.out.println("bitmap width"+width);
				    byte[] image=stream.toByteArray();
				    Utilities.writeToLogFIle("convertImageToBase64 line 8"+image);
				    String imag=Base64.encodeToString(image, Base64.DEFAULT);
				    Utilities.writeToLogFIle("convertImageToBase64 line 9"+imag);
				    return imag;
		    	}catch(Exception ex)
		    	{
		    		Utilities.writeToLogFIle("Exception occureed:"+String.valueOf(ex.getStackTrace()));
		    		Utilities.writeToLogFIle("Exception occureed:"+ex.getStackTrace().toString());
		    		Utilities.writeToLogFIle("Exception occureed:"+ex.getMessage());
		    	}
		    	return ss;
			}
		    
		    public static void setTitleText(Toolbar toolBar,String textToSet)
		    {
		    	 TextView title =(TextView) toolBar.findViewById(R.id.toolbar_title);
			       title.setText(textToSet);
		    }
		    
		    public static Object RegisterNewUser(String url,ArrayList<NameValuePair> nameValuePair){
		    	try
				{
					InputStream inputStream= null;
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(url);
				//	StringEntity stringEntity = new StringEntity(params[0]);
				//	httpPost.setEntity(stringEntity);
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
				//	httpPost.setHeader("Accept","text/plain");
					httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
					HttpResponse httpResponse = httpClient.execute(httpPost);
					inputStream=httpResponse.getEntity().getContent();
					
					if(inputStream!=null){
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
						String line="";
						String result="";
						while ((line = bufferedReader.readLine()) != null) {
							result+=line;
							}
						
						Log.e("ERRORISIS","IS: "+result);
						inputStream.close();
						return result;
					}
				}catch (UnsupportedEncodingException e) {				
					e.printStackTrace();
					return null;
				} catch (ClientProtocolException e) {				
					e.printStackTrace();
					return null;
				} catch (IOException e) {				
					e.printStackTrace();
					return null;
				} 
				return null;
		    }

		    public static UsersDataBean makeUserObject(Activity activity)
		    {
		    	UsersDataBean myUser = new UsersDataBean();
		    	myUser.setFname(getloggedInUserNameFromSharedPref(activity));
		    	myUser.setEmail(getloggedInUserEmailFromSharedPref(activity));
		    	myUser.setPhone(getPhoneNumberOfUSer(activity));
		    	myUser.setId(getUserIdOfUser(activity));
		    	myUser.setUser_image(getLoggedInUserPhotoUrl(activity));
		    	myUser.setPassword(getloggedInUserPasswordFromSharedPref(activity));
		    	return myUser;
		    }
		    
		  //23_12_2015 Gitesh starts
		    public static void writeCurrentUserToSharedPref(Activity activity,UsersDataBean CurrUser)
		    {
		    	Gson gson = new Gson();
		    	  String json = gson.toJson(CurrUser);
		    	writeStringToSharedPref(activity, "currentUser", json);
		    }
		    public static void writejustViewdProdsToSharedPref(Activity activity,JustViewedProdsDataBean jbean){
		    	Gson gson = new Gson();
		    	  String json = gson.toJson(jbean);
		    	writeStringToSharedPref(activity, "JustViewed", json);
		    }
		   
		    public static void writejustViewProdToSharedPref(Activity activity,ProductDataBean CurrProd,int i)
		    {
		    	Gson gson = new Gson();
		    	  String json = gson.toJson(CurrProd);
		    	writeStringToSharedPref(activity, "currentProd"+i, json);
		    }
		    
		    public  static UsersDataBean getCurrentUserFromSharedPref(Activity activity){
		    	Gson fgson = new Gson();
		    	String curUser;
		    	UsersDataBean ubin = new UsersDataBean();
		    	SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext());
		    	curUser=sharedPref.getString("currentUser", "default");
		    	try{
		  		    	ubin=fgson.fromJson(curUser, UsersDataBean.class);
		  	    	}
		    	catch(Exception e){
		    		e.printStackTrace();
		    	}
		    	return ubin;		    	
		    }
		    
		    public static JustViewedProdsDataBean getjustViewdProdsFromSharedPref(Activity activity){
		    	Gson fgson = new Gson();
		    	String justVd;
		    	JustViewedProdsDataBean jbean = new JustViewedProdsDataBean();
		    	SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext());
		    	justVd=sharedPref.getString("JustViewed", "default");
		    	try{
		    		jbean=fgson.fromJson(justVd, JustViewedProdsDataBean.class);
		  	    	}
		    	catch(Exception e){
		    		e.printStackTrace();
		    	}
		    	return jbean;		    	
		    }

		    public  static ProductDataBean getProductdataBean(Activity activity,String prodDetail){
		    	Gson fgson = new Gson();
		    	
		    	ProductDataBean pdataBean = new ProductDataBean();
		    	//SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext());
		    	try{
		    		pdataBean=fgson.fromJson(prodDetail,ProductDataBean.class);
		  	    	}
		    	catch(Exception e){
		    		e.printStackTrace();
		    	}
		    	return pdataBean;		    	
		    }
		    public static UserStatusEnum getUserStatusFromSharedPref(Activity activity)
		    {
		    	UserStatusEnum userStatus = UserStatusEnum.NOT_REGISTERED;
		    	SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext()); //activity.getSharedPreferences(name, mode).getPreferences(Context.MODE_PRIVATE);
		    	int defaultValue = 0;
		    	int loginStatus = sharedPref.getInt(activity.getString(R.string.user_login_status), defaultValue);
		    	
		    	switch(loginStatus)
		    	{
		    		case 0:userStatus = UserStatusEnum.NOT_REGISTERED;
		    				break;
		    		case 1:userStatus = UserStatusEnum.LOGGED_IN;
		    				break;
		    		case 2:userStatus = UserStatusEnum.LOGGED_IN_GOOGLE;
		    				break;
		    		case 3:userStatus = UserStatusEnum.LOGGED_IN_FB;
		    				break;
		    		case 4: userStatus = UserStatusEnum.LOGGED_OUT;
		    				break;		    		
		    	}
		    	return userStatus;
		    }
		    
		    public static SocialLoginEnum getActivityName(Activity activity)
		    {
		    	SocialLoginEnum userStatus = SocialLoginEnum.SIGNUP;
		    	SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext()); //activity.getSharedPreferences(name, mode).getPreferences(Context.MODE_PRIVATE);
		    	int defaultValue = 0;
		    	int loginStatus = sharedPref.getInt(activity.getString(R.string.sign_in_up_activity), defaultValue);
		    	
		    	switch(loginStatus)
		    	{
		    		case 0:userStatus = SocialLoginEnum.SIGNUP;
		    				break;
		    		case 1:userStatus = SocialLoginEnum.SIGNIN;
		    				break;		    				    		
		    	}
		    	return userStatus;
		    }
		    
		    public static void writeUserStatusToSharedPref(Activity activity, UserStatusEnum userStatus)
		    {
		    	writeIntToSharedPref(activity, activity.getString(R.string.user_login_status), userStatus.ordinal());
		    }
		    
		    public static void writeUserStatusToSharedPref(Context cntx, UserStatusEnum userStatus)
		    {
		    	writeIntToSharedPref(cntx, cntx.getString(R.string.user_login_status), userStatus.ordinal());
		    }
		    public static void writeSignInSignUpStatus(Activity activity, SocialLoginEnum signinUpStatus)
		    {
		    	writeIntToSharedPref(activity, activity.getString(R.string.sign_in_up_activity), signinUpStatus.ordinal());
		    }
		    
		    
		    public static String getloggedInUserNameFromSharedPref(Activity activity)
		    {
		    	String uName;
		    	SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext());
		    	uName = sharedPref.getString("currentUserName", "guestUser");
		    	return uName;
		    }
		    public static String getloggedInUserEmailFromSharedPref(Activity activity)
		    {
		    	String uEmail;
		    	SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext());
		    	uEmail = sharedPref.getString("currentUserEmailId", "guestUserEmailId");
		    	return uEmail;
		    }
		    public static String getPhoneNumberOfUSer(Activity activity)
		    {
		    	String phoneNum;
		    	SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext());
		    	phoneNum = sharedPref.getString("currentUserPhoneNum", "noPhoneNum");
		    	return phoneNum;
		    }
		    public static int getUserIdOfUser(Activity activity)
		    {
		    	int userId=-1;
		    	SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext());
		    	userId = sharedPref.getInt("currentUserId", -1);
		    	return userId;
		    }
		    public static String getLoggedInUserPhotoUrl(Activity activity)
		    {
		    	String photoUrl;
		    	SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext());
		    	photoUrl = sharedPref.getString("currentUserPhotoUrl", "NoPhoto");
		    	return photoUrl;
		    }
		    
		    public static String getloggedInUserPasswordFromSharedPref(Activity activity)
		    {
		    	String password;
		    	SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext());
		    	password = sharedPref.getString("currentUserPassword", "NoPassword");
		    	return password;
		    }
		    public static String getOtpFromSharedPref(Context context)
		    {
		    	String otp;
		    	SharedPreferences sharedPref = getSharedPrefs(context.getApplicationContext());
		    	otp= sharedPref.getString("otp", "default");
		    	return otp;
		    }
		    public static String getOtpStatusFromSharedPref(Context context)
		    {
		    	String otpStatus;
		    	SharedPreferences sharedPref = getSharedPrefs(context.getApplicationContext());
		    	otpStatus= sharedPref.getString("otpStatus", "default");
		    	return otpStatus;
		    }
 public static void writeOtpToSharedPreferences(Activity activity,String otp)
		    {
		    	writeStringToSharedPref(activity, "otp", otp);
		    }
		    
		    public static void writeUserStatusEnumToSharedPref(Activity activity, UserStatusEnum userStatus)
		    {
		    	writeIntToSharedPref(activity, activity.getString(R.string.user_login_status), userStatus.ordinal());
		    }

		    public static UserStatusEnum getUserStatusEnumFromSharedPref(Activity activity)
		    {
		    	UserStatusEnum userStatus = UserStatusEnum.NOT_REGISTERED;
		    	SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext()); //activity.getSharedPreferences(name, mode).getPreferences(Context.MODE_PRIVATE);
		    	int defaultValue = 0;
		    	int loginStatus = sharedPref.getInt(activity.getString(R.string.user_login_status), defaultValue);
		    	
		    	switch(loginStatus)
		    	{
		    		case 0:userStatus = UserStatusEnum.NOT_REGISTERED;
		    				break;
		    		case 1:userStatus = UserStatusEnum.LOGGED_IN;
		    				break;
		    		case 2:userStatus = UserStatusEnum.LOGGED_IN_GOOGLE;
		    				break;
		    		case 3:userStatus = UserStatusEnum.LOGGED_IN_FB;
		    				break;
		    		case 4: userStatus = UserStatusEnum.LOGGED_OUT;
		    				break;		    		
		    	}
		    	return userStatus;
		    }
		    
		    public static void writecurrentUserNameToSharedPref(Activity activity,String userName)
		    {
		    	writeStringToSharedPref(activity, "currentUserName", userName);
		    }
		    
		    public static void writecurrentUserEmailToSharedPref(Activity activity,String emailId)
		    {
		    	writeStringToSharedPref(activity, "currentUserEmailId", emailId);
		    }
		    
		    public static boolean getLaunchFromStatusSharedPref(Activity activity)
		    {
		    	boolean retValue; 
		    	SharedPreferences sharedPref = getSharedPrefs(activity.getApplicationContext());
		    	retValue = sharedPref.getBoolean("launchFromSplash", true);
		    	return retValue;
		    }
		    
		    public static void startGetUserInfo(String url, final Activity activity,LinearLayout mProgress)
			 {
				 WebJsonObjectRequest webobjReq=new WebJsonObjectRequest(Method.GET, url, new JSONObject(),new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub
							setHLInstanceAndWriteSharedPrefFromWebService(activity, response);					
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub
							Toast.makeText(activity, "Some error while retriving user info", Toast.LENGTH_LONG).show();
						}}
					);				
						if(com.okler.network.VolleyRequest.addJsonObjectRequest(activity,webobjReq))
						{
							if(mProgress!= null)
								showProgress(true,mProgress);
						}
						else
						{
							if(mProgress!= null)
								showProgress(false,mProgress);
						}
			 }
		    
		    public static void setHLInstanceAndWriteSharedPrefFromWebService(Activity activity, JSONObject response)
			 {
				 
				 String name,emailId,password,phoneNum,photoUrl;
				 int usId;
				 ArrayList<AddressDataBean> userAddr = new ArrayList<AddressDataBean>();
				 try
				 {
					 name = response.getString("Name");
					 emailId = response.getString("EmailId");
					 password = response.getString("Password");
					 phoneNum = response.getString("PhoneNo");
					 photoUrl = response.getString("PhotoURL");
					 usId = Integer.parseInt(response.getString("UserId"));
					 JSONArray adrFromWS = response.getJSONArray("AddressList");
				     //userAddr = setAddrFromUser(adrFromWS,usId);
					 //Set Addresses From web service
					 UsersDataBean udb = new UsersDataBean();
					 udb.setEmail(emailId);
					 udb.setFname(name);
					 udb.setPassword(password);
					 udb.setPhone(phoneNum);
					 //udb.setPhotoUrl(photoUrl);
					 udb.setId(usId);
					 udb.setAddDatabean(userAddr);
					 
					 Okler.getInstance().setuDataBean(udb);
					 Utilities.writeCurrentUserToSharedPref(activity, udb);
					 
					 
				 }catch(Exception ex)
				 {
					 
				 }
			 }
		    
		    public static String getPath(final Context context, final Uri uri) {

		        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		        // DocumentProvider
		        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
		            // ExternalStorageProvider
		        	Utilities.writeToLogFIle("isKitKat && DocumentsContract.isDocumentUri("+context+", "+uri+")");
		        	
		            if (isExternalStorageDocument(uri)) {
		                final String docId = DocumentsContract.getDocumentId(uri);
		                final String[] split = docId.split(":");
		                final String type = split[0];
		                Utilities.writeToLogFIle(" : to  / type "+type);
		                if ("primary".equalsIgnoreCase(type)) {
		                    return Environment.getExternalStorageDirectory() + "/" + split[1];
		                }

		                // TODO handle non-primary volumes
		            }
		            // DownloadsProvider
		            else if (isDownloadsDocument(uri)) {

		                final String id = DocumentsContract.getDocumentId(uri);
		                final Uri contentUri = ContentUris.withAppendedId(
		                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
		                Utilities.writeToLogFIle("content://downloads/public_downloads");
		                
		                return getDataColumn(context, contentUri, null, null);
		            }
		            // MediaProvider
		            else if (isMediaDocument(uri)) {
		                final String docId = DocumentsContract.getDocumentId(uri);
		                final String[] split = docId.split(":");
		                final String type = split[0];

		                Uri contentUri = null;
		                if ("image".equals(type)) {
		                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		                } else if ("video".equals(type)) {
		                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		                } else if ("audio".equals(type)) {
		                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		                }

		                final String selection = "_id=?";
		                final String[] selectionArgs = new String[] {
		                        split[1]
		                };
		                Utilities.writeToLogFIle("context "+context+"contentUri "+contentUri+"selection"+selection+"selectionArgs"+selectionArgs);
		                
		                return getDataColumn(context, contentUri, selection, selectionArgs);
		            }
		        }
		        // MediaStore (and general)
		        else if ("content".equalsIgnoreCase(uri.getScheme())) {

		            // Return the remote address
		            if (isGooglePhotosUri(uri)){
		            	Utilities.writeToLogFIle("isGooglePhotosUri("+uri+")");
		                return uri.getLastPathSegment();
		            }
		            Utilities.writeToLogFIle("isgoogle context"+context+" uri"+uri);
		            return getDataColumn(context, uri, null, null);
		        }
		        // File
		        else if ("file".equalsIgnoreCase(uri.getScheme())) {
		        	Utilities.writeToLogFIle("isgoogle "+uri.getPath());
		            return uri.getPath();
		        }

		        return null;
		    }

		    /**
		     * Get the value of the data column for this Uri. This is useful for
		     * MediaStore Uris, and other file-based ContentProviders.
		     *
		     * @param context The context.
		     * @param uri The Uri to query.
		     * @param selection (Optional) Filter used in the query.
		     * @param selectionArgs (Optional) Selection arguments used in the query.
		     * @return The value of the _data column, which is typically a file path.
		     */
		    public static String getDataColumn(Context context, Uri uri, String selection,
		            String[] selectionArgs) {
		    	Utilities.writeToLogFIle("");
		    	Utilities.writeToLogFIle("Context "+context+"Uri "+uri+"selection"+selection);
		    	for(int i=0;i<selectionArgs.length;i++){
		    		Utilities.writeToLogFIle("selectionargs "+selectionArgs[i]);
		    	}
		        Cursor cursor = null;
		        final String column = "_data";
		        final String[] projection = {
		                column
		        };
		        Utilities.writeToLogFIle("column "+column);
		        for(int i=0;i<projection.length;i++)
		        Utilities.writeToLogFIle("projection "+projection[i]);
		        try {
		            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
		                    null);
		            if (cursor != null && cursor.moveToFirst()) {
		                final int index = cursor.getColumnIndexOrThrow(column);
		                Utilities.writeToLogFIle("index "+cursor.getString(index));
		                return cursor.getString(index);
		            }
		        } finally {
		            if (cursor != null)
		                cursor.close();
		        }
		        Utilities.writeToLogFIle("null");
		        return null;
		    }


		    /**
		     * @param uri The Uri to check.
		     * @return Whether the Uri authority is ExternalStorageProvider.
		     */
		    public static boolean isExternalStorageDocument(Uri uri) {
		        return "com.android.externalstorage.documents".equals(uri.getAuthority());
		    }

		    /**
		     * @param uri The Uri to check.
		     * @return Whether the Uri authority is DownloadsProvider.
		     */
		    public static boolean isDownloadsDocument(Uri uri) {
		        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
		    }

		    /**
		     * @param uri The Uri to check.
		     * @return Whether the Uri authority is MediaProvider.
		     */
		    public static boolean isMediaDocument(Uri uri) {
		        return "com.android.providers.media.documents".equals(uri.getAuthority());
		    }

		    /**
		     * @param uri The Uri to check.
		     * @return Whether the Uri authority is Google Photos.
		     */
		    public static boolean isGooglePhotosUri(Uri uri) {
		        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
		    }
		    
		    public static 	ArrayList<NameValuePair> getPrescriptionNameValPairFromBean(Activity curActivity, PrescriptionsDataBean prescriptionsDataBean,boolean isMedPres,int required_type)
		    {
		    	Utilities.writeToLogFIle("In Utiities nvpairs");
		    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    	String[]	imagesBase64 = new String[prescriptionsDataBean.getPresImages().size()];
				String[] imageTypes =  new String[prescriptionsDataBean.getPresImages().size()];
					
				for(int i = 0 ; i < prescriptionsDataBean.getPresImages().size() ; i++)
				{
					imagesBase64[i] = prescriptionsDataBean.getPresImages().get(i).getBase64ConvrtedImg();
					imageTypes[i] = "jpeg";
				}
				UsersDataBean ubean = new UsersDataBean();
			
				ubean.setId(Utilities.getCurrentUserFromSharedPref(curActivity).getId());	
				
				
			/*	Log.d("get doc name", prescriptionsDataBean.getDocName());
				Log.d("get patient name", prescriptionsDataBean.getPatientName());
				Log.d("get pres type", prescriptionsDataBean.getPresType());
				Log.d("get mob number", prescriptionsDataBean.getMobileNumber());*/
			
				Gson gson = new Gson();
				String base64 = gson.toJson(imagesBase64);
				String imgTypes = gson.toJson(imageTypes);
				//to change user id later on
				nameValuePairs.add(new BasicNameValuePair("patient_name", prescriptionsDataBean.getPatientName()));		
				nameValuePairs.add(new BasicNameValuePair("doc_name", prescriptionsDataBean.getDocName()));	
				nameValuePairs.add(new BasicNameValuePair("required_type", ""+required_type));			
				nameValuePairs.add(new BasicNameValuePair("phoneno", prescriptionsDataBean.getMobileNumber()));			
				for(int imgCount = 0 ; imgCount < prescriptionsDataBean.getPresImages().size(); imgCount ++)
				{
					nameValuePairs.add(new BasicNameValuePair("image["+imgCount+"]", imagesBase64[imgCount]));
					nameValuePairs.add(new BasicNameValuePair("imgtype["+imgCount+"]", imageTypes[imgCount]));
				}
				
				nameValuePairs.add(new BasicNameValuePair("user_id", ""+ubean.getId()));
			
				if(isMedPres)
					nameValuePairs.add(new BasicNameValuePair("service_type", "2"));					
				else
					nameValuePairs.add(new BasicNameValuePair("service_type", "1"));		
				for(int i=0;i<nameValuePairs.size();i++)
				Utilities.writeToLogFIle("utilities nvpairs "+nameValuePairs.get(i).getName()+"  "+nameValuePairs.get(i).getValue());
				return nameValuePairs;
		    }
		    
		    public static void writeUserTosharedPrefFromJSON(Activity activity, JSONArray arr)
		    {
		    	UsersDataBean udb = new UsersDataBean();
		    	JSONObject obj;
 		    	try {
					obj = arr.getJSONObject(0);
					udb.setId(obj.getInt("id"));
					udb.setFname(obj.getString("firstname"));
					udb.setLname(obj.getString("lastname"));
					udb.setEmail(obj.getString("email"));
					udb.setUser_image(obj.optString("user_image"));
					udb.setPhone(obj.optString("phone"));
					writeCurrentUserToSharedPref(activity, udb);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    
		    public static void writeToLogFIle(String lineToWrite)
			{
				  try {
					   DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				        File myFile = new File("/sdcard/OklerLog.txt");
				        if(!myFile.exists())
				        	myFile.createNewFile();
				        FileOutputStream fOut = new FileOutputStream(myFile,true);
				        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
				        Calendar cal = Calendar.getInstance();
				 	   
				        myOutWriter.append(dateFormat.format(cal.getTime())+" "+lineToWrite+"\n");
				        myOutWriter.close();
				        fOut.close();
				        } catch (Exception e) {
				              Log.e("ERRR", "Could not create file",e);
				        } 
			}
		    
}
