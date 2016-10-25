package com.okler.dialogs;

import com.okler.dialogs.*;
import com.okler.utils.TouchImageView;
import com.okleruser.R;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class ZoomimageDialogFragment extends DialogFragment{
	TouchImageView healthshopimage;
	ProgressBar progressLinLayout;
	Bitmap bitmap;
	public ZoomimageDialogFragment(Bitmap bitmap)
	{
		this.bitmap=bitmap;
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //showProgress(true);
   //     setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        
	}
	
	@Override 
	public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        d.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.Transparent));
        //d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if (d!=null){
        	int width = ViewGroup.LayoutParams.MATCH_PARENT;
        	int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }
	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 	
	        View root = inflater.inflate(R.layout.dialog_zoom_healthshop, container, false);
	       
			
	        healthshopimage=(TouchImageView)root.findViewById(R.id.healthshopimage);
	        healthshopimage.setImageBitmap(bitmap);
	        
	        healthshopimage.setOnTouchImageViewListener(new com.okler.utils.TouchImageView.OnTouchImageViewListener() {
				
				@Override
				public void onMove() {
					// TODO Auto-generated method stub
					PointF point = healthshopimage.getScrollPosition();
					RectF rect = healthshopimage.getZoomedRect();
					float currentZoom = healthshopimage.getCurrentZoom();
					boolean isZoomed = healthshopimage.isZoomed();
					
				}
				
			});
	        return root;
	 }
		
		
	
}
