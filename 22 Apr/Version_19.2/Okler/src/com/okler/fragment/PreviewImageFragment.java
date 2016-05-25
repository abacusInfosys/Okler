package com.okler.fragment;

import com.okleruser.R;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PreviewImageFragment extends Fragment {
	
	View view;
	
	ImageView thimbnail, imageGallery;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		view = inflater.inflate(R.layout.fragment_preview_image, container, false);	
		
		imageGallery = new ImageView(getActivity());
		
		
		/*thimbnail = (ImageView) view.findViewById(R.id.thumbnail_image);
		
		thimbnail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				CameraDialog cdd=new CameraDialog(getActivity());
				cdd.show();
				
			}
		}); */
		return view;
	}

}
