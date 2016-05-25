package com.okler.android;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.okler.adapters.HealthShopGridAdapter;
import com.okler.databeans.CategoriesDataBean;
import com.okler.databeans.SubCategoriesDataBean;
import com.okler.network.VolleyRequest;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;
import com.okleruser.R;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HealthShopGrid extends BaseActivity {
	private View bottomBarLayout;
	private GridView gridView;
    private Context context;    
    private ArrayList prgmName;
    private Toolbar toolBar;
    private Button notifCount;
    private NetworkImageView iv_image;
    ImageLoader mImageLoader;
       public static String [] prgmNameList={"1 kg weight cuff","1 kg weight cuff","1 kg weight cuff","1 kg weight cuff","1 kg weight cuff","1 kg weight cuff"};
   public static int [] prgmImages={R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera,R.drawable.camera};
   ImageView imgBack;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_shop_grid);
		   gridView=(GridView)findViewById(R.id.gridView1);    
	   //    gridView.setAdapter(new HealthShopGridAdapter(this, prgmNameList, prgmImages));
	       toolBar=(Toolbar) findViewById(R.id.toolbar);
	       context=getApplicationContext();
	       mImageLoader = VolleyRequest.getInstance(context).getImageLoader();
	       final ActionBar ab=getSupportActionBar();
	     //  ab.setDisplayHomeAsUpEnabled(true);
	       toolBar.setBackgroundResource(R.drawable.custom_view_grad_healthshop);
	       imgBack = (ImageView)toolBar.findViewById(R.id.toolbar_back);
	       imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	      Utilities.setTitleText(toolBar, "Health Shop");
	    Button btn =(Button)  toolBar.findViewById(R.id.toolBarCount);
	    btn.setVisibility(View.GONE);
	    RelativeLayout bottomRelLayo = (RelativeLayout)toolBar.findViewById(R.id.bottomRelLayo);
	    ImageView listingIcon = (ImageView)bottomRelLayo.findViewById(R.id.listingIcon);
	    listingIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent startListing = new Intent(getApplicationContext(), HealthShopListView.class);
				startActivity(startListing);
			}
		});
	       bottomBarLayout = findViewById(R.id.bottombar);
	       handleMapping(bottomBarLayout);
	       
	     
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.menu.health_shop_grid, menu);
				MenuInflater inflater = getMenuInflater();
			    inflater.inflate(R.menu.health_shop_grid, menu);
				MenuItem item = menu.findItem(R.id.action_settings);
				item.setActionView(R.layout.feed_update_count);
			    //
				 View count = item.getActionView();
				    notifCount = (Button) count.findViewById(R.id.notif_count);
				    notifCount.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
						//	Toast.makeText(getApplicationContext(), "btn clicked", Toast.LENGTH_LONG).show();
							Intent i = new Intent(getApplicationContext(),HealthShopListView .class);
							startActivity(i);
						}
					});
				    notifCount.setText(String.valueOf(5));
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
