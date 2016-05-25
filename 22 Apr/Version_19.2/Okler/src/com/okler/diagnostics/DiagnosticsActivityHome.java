package com.okler.diagnostics;

import com.okler.android.BaseActivity;
import com.okleruser.R;
import com.okler.android.UploadPrescription;
import com.okler.utils.Utilities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class DiagnosticsActivityHome extends BaseActivity {

	View bottomBarLayout;
	Toolbar toolBar;
	ImageView diagnoTestIv,diagnoPackageIv,diagnoDiseaseIv,diagnoUploadPres;
	Context context;
	ImageView imgBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagnostics);
		context = this;
		toolBar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		final ActionBar ab=getSupportActionBar();
		//ab.setDisplayHomeAsUpEnabled(true);
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	      Utilities.setTitleText(toolBar, "Diagnostic Test");
		diagnoTestIv =(ImageView) findViewById(R.id.diagnoTestIv);
		diagnoPackageIv = (ImageView)findViewById(R.id.diagnoPackageIv);
		diagnoDiseaseIv = (ImageView)findViewById(R.id.diagnoDiseaseIv);
		diagnoUploadPres=(ImageView) findViewById(R.id.diagnoUpPrescIv);
		diagnoTestIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent startDiagnoTest = new Intent(context, DiagnoTestsHome.class);
			startActivity(startDiagnoTest);				
			}
		});
		
		diagnoPackageIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent startDPack = new Intent(context,DiagnoPackageHome.class);
				startActivity(startDPack);
				
			}
		});
		diagnoDiseaseIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent startDiseases = new Intent(context,DiagnoDiseaseHome.class);
				startActivity(startDiseases);
				
			}
		});
		diagnoUploadPres.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent startUpPresc=new Intent(context,UploadPrescription.class);
				startUpPresc.putExtra("isMedPres", false);
				startActivity(startUpPresc);
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.diagnostics, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
