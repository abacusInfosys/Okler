package com.okler.dialogs;

import com.okleruser.R;
import com.okler.utils.Utilities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class CameraGalleryDialog extends Dialog implements
		android.view.View.OnClickListener {

	public Activity c;
	public Dialog d;
	public Button cancel;

	ImageView camera, gallery;

	public CameraGalleryDialog(Activity a) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_camera_gallery);
		cancel = (Button) findViewById(R.id.cancelbutton);
		camera = (ImageView) findViewById(R.id.camera_image);
		gallery = (ImageView) findViewById(R.id.gallery_image);
		cancel.setOnClickListener(this);
		camera.setOnClickListener(this);
		gallery.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancelbutton:
			dismiss();
			break;
		case R.id.camera_image:
			Utilities.captureImageFromCamera(c);
			break;

		case R.id.gallery_image:
			Utilities.showImageFromGallery(c);
			break;
		default:
			break;
		}
		dismiss();
	}
}
