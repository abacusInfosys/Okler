package com.okler.android;

import com.okler.diagnostics.DiagnosticsActivityHome;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okleruser.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;



public class ServiceCategoriesFragment extends Fragment {
	View view;
	LinearLayout layoutUploadPresc, layoutPharmacy, layoutDignisticService, 
	layoutMedicalService, layoutPhysiotherapy, layoutHealthShop;
	Context context;
	Intent intent;
	UIUtils uiUtils;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_service_category, container, false);
		context = getActivity();
		layoutUploadPresc = (LinearLayout) view.findViewById(R.id.upload_presc_layout);
		layoutPharmacy = (LinearLayout) view.findViewById(R.id.pharmacy_layout);
		layoutDignisticService = (LinearLayout) view.findViewById(R.id.dignostic_service_layout);
		layoutMedicalService = (LinearLayout) view.findViewById(R.id.medical_service_layout);
		layoutPhysiotherapy = (LinearLayout) view.findViewById(R.id.physiotherapy_layout);
		layoutHealthShop = (LinearLayout) view.findViewById(R.id.health_shop_layout);
		uiUtils = new UIUtils();
		setUploadPresListener();
		setPharmacyListener();
		setDiagnosticsListener();
		setMediListener();
		setPhysioListener();
		setHealthShopListener();
		return view;
	}
	private void setUploadPresListener() {
		layoutUploadPresc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context, UploadPrescription.class);
				startActivity(intent);
			}
		});
	}
	
	private void setPharmacyListener() {
		layoutPharmacy.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(context, "Pharmacy activity shown", Toast.LENGTH_LONG).show();
				intent = new Intent(context, MedicineHomeActivity.class);//12_01_2016 Gitesh
				startActivity(intent);//12_01_2016 Gitesh
			}
		});
	}
	private void setDiagnosticsListener() {
		layoutDignisticService.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	Toast.makeText(context, "Diagnosis activity shown", Toast.LENGTH_LONG).show();
				intent = new Intent(context, DiagnosticsActivityHome.class);
				int ab = uiUtils.getBookingType("Diagnostic");
				Okler.getInstance().setBookingType(ab);
				startActivity(intent);
			}
		});
	}

	private void setMediListener()
	{
			layoutMedicalService.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context, MedicalServices.class);
				startActivity(intent);
			}
		});
	}
		
	private void setPhysioListener()
	{
		layoutPhysiotherapy.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context, Physiotherapy.class);
				startActivity(intent);
			}
		});
	}
	private void setHealthShopListener() {
		layoutHealthShop.setOnClickListener(new OnClickListener() {			
		@Override
		public void onClick(View v) {
		//	Toast.makeText(context, "Health shop activity shown", Toast.LENGTH_LONG).show();
			intent = new Intent(context, HealthShopGrid.class);
			uiUtils = new UIUtils();
			int ab = uiUtils.getBookingType("Healthshop");
			Okler.getInstance().setBookingType(ab);
			int a = Okler.getInstance().getBookingType();
		//	Toast.makeText(context, ""+a, Toast.LENGTH_LONG).show();
			startActivity(intent);
		}
	});
}

}
