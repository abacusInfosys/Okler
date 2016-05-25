package com.okler.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.okler.utils.CameraGalleryImageInfo;
import com.okler.utils.Okler;
import com.okler.utils.TextValidations;
import com.okler.utils.Utilities;

import com.okleruser.R;
import com.okler.databeans.PrescriptionsDataBean;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CameraGallery extends Fragment implements OnClickListener{
	LinearLayout cameraLayout, galleryLayout;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int RESULT_OK = 1;
	private static final int RESULT_CANCELED = 2;


	// directory name to store captured images and videos
	private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
	protected static final int SELECT_IMAGE = 1;
	protected static final int CAPTURE_IMAGE = 2;

	private Uri fileUri; // file url to store image/video
	
	static File mediaFile;

	private ImageView imgPreview;
	String parent;
	public Bitmap bitmap;
	Activity parentAct;
	private String prescType,patientName, docName, mobNum;
public PrescriptionsDataBean pdtBean;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		parentAct = getActivity();
		parent = String.valueOf(parentAct.getComponentName());
		View view = inflater.inflate(R.layout.fragment_camera_gallery, null);
		Okler.getInstance().getPrescriptionsDataBeans().getPresImages().clear();
		cameraLayout = (LinearLayout) view.findViewById(R.id.layout_camera);
	    galleryLayout = (LinearLayout) view.findViewById(R.id.layout_gallery);
		
	    cameraLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Validate Patient Info
				if(parent.equals("ComponentInfo{com.okler.android/com.okler.android.UploadPrescription}")){
				if(ValidatePatientData())
				{
					
					Utilities.captureImageFromCamera(getActivity());
				}	
				}
				else{
					Utilities.captureImageFromCamera(getActivity());
				}
			}
		});
	    galleryLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Validate Patient Info
				if(parent.equals("ComponentInfo{com.okler.android/com.okler.android.UploadPrescription}")){
				if(ValidatePatientData())
				{
					Utilities.showImageFromGallery(getActivity());
				}
				}
				else{
					Utilities.showImageFromGallery(getActivity());
				}
				
			}
		});	    
		return view;	
	}

@Override
public void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);

	// save file url in bundle as it will be null on screen orientation
	// changes
	outState.putParcelable("file_uri", fileUri);
}

protected void onRestoreInstanceState(Bundle savedInstanceState) {
	super.onSaveInstanceState(savedInstanceState);

	// get the file url
	fileUri = savedInstanceState.getParcelable("file_uri");
}

private boolean ValidatePatientData()
{
	
	//Verify Spinner
	Spinner presType = (Spinner)parentAct.findViewById(R.id.prescription_type_spinner);
		if(presType.getSelectedItemId() == 0)
		{		
			TextView errorText = (TextView)presType.getSelectedView(); 
			errorText.setError("Select Prescription Type");	
			Toast.makeText(parentAct, "Select Prescription Type", Toast.LENGTH_LONG).show();
			return false;
		}		
		prescType = presType.getSelectedItem().toString();
		//Verify Patient Name
		EditText upload_edit_patientname = (EditText)parentAct.findViewById(R.id.upload_edit_patientname);
		TextValidations txtValidation = new TextValidations(upload_edit_patientname);
		if(!txtValidation.ValidateMinimumChars("Enter Patient's Name",3))
			return false;
		
		EditText upload_edit_patientsirname = (EditText)parentAct.findViewById(R.id.upload_edit_surname);
		patientName = upload_edit_patientname.getText().toString() +" "+ upload_edit_patientsirname.getText().toString();
		
		//Verify Doc Name
		EditText upload_edit_docname = (EditText)parentAct.findViewById(R.id.upload_edit_doctorname);
		txtValidation = new TextValidations(upload_edit_docname);
		if(!txtValidation.ValidateMinimumChars("Enter Doctor's Name",3))
			return false;
		
		EditText upload_edit_docsirname = (EditText)parentAct.findViewById(R.id.upload_edit_docsurname);
		docName = upload_edit_docname.getText().toString() +" "+ upload_edit_docsirname.getText().toString();
		
		//Verify Mobile Number
		EditText upload_mob_number = (EditText)parentAct.findViewById(R.id.uploaa_edit_mobileno);
		txtValidation = new TextValidations(upload_mob_number);
		if(!txtValidation.validateMobile("Enter Correct Mobile Number"))
			return false;
		
		mobNum = upload_mob_number.getText().toString();
		Okler.getInstance().getPrescriptionsDataBeans().setDocName(docName);
		Okler.getInstance().getPrescriptionsDataBeans().setPatientName(patientName);
		Okler.getInstance().getPrescriptionsDataBeans().setMobileNumber(mobNum);
		Okler.getInstance().getPrescriptionsDataBeans().setPresType(prescType);
		return true;
	}

/*
 * Display image from a path to ImageView
 */
/*private void previewCapturedImage() {
	try {
		imgPreview.setVisibility(View.VISIBLE);

		// bimatp factory
		BitmapFactory.Options options = new BitmapFactory.Options();

		// downsizing image as it throws OutOfMemory Exception for larger
		// images
		options.inSampleSize = 8;

		final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
				options);

		imgPreview.setImageBitmap(bitmap);
	} catch (NullPointerException e) {
		e.printStackTrace();
	}
}*/

/*
 * Creating file uri to store image/video
 */
public Uri getOutputMediaFileUri(int type) {
	return Uri.fromFile(getOutputMediaFile(type));
}


private static File getOutputMediaFile(int type) {

	// External sdcard location
	File mediaStorageDir = new File(
			Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
			IMAGE_DIRECTORY_NAME);

	// Create the storage directory if it does not exist
	if (!mediaStorageDir.exists()) {
		if (!mediaStorageDir.mkdirs()) {
			Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
					+ IMAGE_DIRECTORY_NAME + " directory");
			return null;
		}
	}

	// Create a media file name
	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
			Locale.getDefault()).format(new Date());
	
	if (type == MEDIA_TYPE_IMAGE) 
	{
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");
	} 
	else 
	{
		return null;
	}

	return mediaFile;
}

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
}

}
