package com.okler.dialogs;

import java.util.ArrayList;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.okleruser.R;
import com.okler.databeans.PrescriptionImagesDataBean;
import com.okler.databeans.PrescriptionsDataBean;
import com.okler.network.VolleyRequest;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.view.*;

public class LargePresImageDialog extends Dialog implements
		android.view.View.OnClickListener {

	int position;
	ArrayList<PrescriptionsDataBean> presHistory;
	ImageView leftArr, rightArr, crossArr;
	NetworkImageView prsImage;
	PrescriptionsDataBean currbean;
	ArrayList<PrescriptionImagesDataBean> currImagesBean;
	int currPos = 0, currImgNumber = 0;
	ImageLoader imageLoader;
	RelativeLayout leftArrowImgRl, rightArrowImgRl;
	Activity a;
	ProgressBar progressLinLayout;

	public LargePresImageDialog(Activity a, int pos,
			ArrayList<PrescriptionsDataBean> hist) {
		super(a);
		position = pos;
		presHistory = hist;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_large_pres_image);
		progressLinLayout=(ProgressBar)findViewById(R.id.progressLinLayout);
		showProgress(true);
		leftArr = (ImageView) findViewById(R.id.leftArrowImg);
		leftArrowImgRl = (RelativeLayout) findViewById(R.id.leftArrowImgRl);
		rightArrowImgRl = (RelativeLayout) findViewById(R.id.rightArrowImgRl);
		prsImage = (NetworkImageView) findViewById(R.id.presImageView);
		rightArr = (ImageView) findViewById(R.id.rightArrowImg);
		//crossArr = (ImageView) findViewById(R.id.deleteImg);
		imageLoader = VolleyRequest.getInstance(a).getImageLoader();
		currbean = presHistory.get(position);
		currImagesBean = currbean.getPresImages();
		leftArr.setVisibility(View.INVISIBLE);
		leftArrowImgRl.setVisibility(View.INVISIBLE);
		if (currImagesBean.size() == 1) {
			rightArr.setVisibility(View.INVISIBLE);
			rightArrowImgRl.setVisibility(View.INVISIBLE);
		}

		// Default
		String s = currImagesBean.get(currImgNumber).getPresUrl();
		prsImage.setImageUrl(currImagesBean.get(currImgNumber).getPresUrl(),
				imageLoader);
		long a = prsImage.getDrawingTime();
		// prsImage.setImageUrl("http://183.82.110.105:8081/oklerdevv2/uploads/prescription/OKLPIDIMG_1456220834796_0.jpeg",
		// imageLoader);
		prsImage.setErrorImageResId(R.drawable.no_image_found);
		
		rightArr.setOnClickListener(this);
		leftArr.setOnClickListener(this);
		//crossArr.setOnClickListener(this);
		leftArrowImgRl.setOnClickListener(this);
		rightArrowImgRl.setOnClickListener(this);
		/*imageLoader.get(currImagesBean.get(currImgNumber).getPresUrl(), new ImageListener() {
			
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onResponse(ImageContainer response, boolean isImmediate) {
				// TODO Auto-generated method stub
				if(response!=null)
				{
					Bitmap bitmap=response.getBitmap();
					if(bitmap!=null)
					{
						showProgress(false);
						prsImage.setImageUrl(currImagesBean.get(currImgNumber).getPresUrl(),
								imageLoader);
						
					}
				}
				
			}
		});
		*/
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.leftArrowImg:

			currImgNumber--;
			prsImage.setImageUrl(
					currImagesBean.get(currImgNumber).getPresUrl(), imageLoader);
			if (currImgNumber == 0) {
				leftArr.setVisibility(View.INVISIBLE);
				leftArrowImgRl.setVisibility(View.INVISIBLE);
				rightArr.setVisibility(View.VISIBLE);
				rightArrowImgRl.setVisibility(View.VISIBLE);
			} else {
				leftArr.setVisibility(View.VISIBLE);
				leftArrowImgRl.setVisibility(View.VISIBLE);
				rightArr.setVisibility(View.VISIBLE);
				rightArrowImgRl.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.leftArrowImgRl:

			currImgNumber--;
			prsImage.setImageUrl(
					currImagesBean.get(currImgNumber).getPresUrl(), imageLoader);
			if (currImgNumber == 0) {
				leftArr.setVisibility(View.INVISIBLE);
				leftArrowImgRl.setVisibility(View.INVISIBLE);
				rightArr.setVisibility(View.VISIBLE);
				rightArrowImgRl.setVisibility(View.VISIBLE);
			} else {
				leftArr.setVisibility(View.VISIBLE);
				leftArrowImgRl.setVisibility(View.VISIBLE);
				rightArr.setVisibility(View.VISIBLE);
				rightArrowImgRl.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.rightArrowImg:
			currImgNumber++;
			prsImage.setImageUrl(
					currImagesBean.get(currImgNumber).getPresUrl(), imageLoader);
			leftArr.setVisibility(View.VISIBLE);
			leftArrowImgRl.setVisibility(View.VISIBLE);
			if ((currImagesBean.size() - 1) == currImgNumber) {
				rightArr.setVisibility(View.INVISIBLE);
				rightArrowImgRl.setVisibility(View.INVISIBLE);
			}
			break;

		case R.id.rightArrowImgRl:
			currImgNumber++;
			prsImage.setImageUrl(
					currImagesBean.get(currImgNumber).getPresUrl(), imageLoader);
			leftArr.setVisibility(View.VISIBLE);
			leftArrowImgRl.setVisibility(View.VISIBLE);
			if ((currImagesBean.size() - 1) == currImgNumber) {
				rightArr.setVisibility(View.INVISIBLE);
				rightArrowImgRl.setVisibility(View.INVISIBLE);
			}
			break;
		/*case R.id.deleteImg:
			dismiss();
			break;
*/
		}
	}
	
	private void showProgress(boolean paramBoolean) {
		
		
		if (paramBoolean) {
			this.progressLinLayout.setVisibility(View.VISIBLE);
			return;
		}
		this.progressLinLayout.setVisibility(View.INVISIBLE);
	}
	
}
