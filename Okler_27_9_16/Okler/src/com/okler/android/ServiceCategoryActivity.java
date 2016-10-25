package com.okler.android;

import java.util.ArrayList;

import com.okler.databeans.NavDrawerItems;
import com.okler.diagno.DiagnosticsActivityHome;
import com.okler.helpnsupport.ContactUsActivity;
import com.okler.helpnsupport.FaqActivity;
import com.okler.helpnsupport.LegalActivity;
import com.okler.helpnsupport.SupportHelpActivity;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;
import com.okleruser.R;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ServiceCategoryActivity extends BaseActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private Toolbar toolbar;
	Button toolBarCount;
	// nav drawer title
	private CharSequence mDrawerTitle;
	Button notifCount;
	// used to store app title
	private CharSequence mTitle;
	Activity ack;
	// slide menu items
	private String[] navMenuTitles;

	private ArrayList<com.okler.databeans.NavDrawerItems> navDrawerItems;
	private com.okler.adapters.NavDrawerListAdapter adapter;
	RelativeLayout relParent;
	View bottomBarLayout;
	/*LinearLayout layoutUploadPresc, layoutPharmacy, layoutDignisticService,
			layoutMedicalService, layoutPhysiotherapy, layoutHealthShop;*/
	Intent intent;
	ImageView imgUp,imgMeds,imgDiagno,imgMedSer,imgPhysio,imgHealth;
	Context context;
	UIUtils uiUtils;
	ImageButton btnHome;
	TextView versiontext;
	PackageInfo info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_category);
		ack = this;
		// nav drawer icons from resources
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		relParent = (RelativeLayout) findViewById(R.id.sliderManuParent);
		mDrawerList = (ListView) relParent.findViewById(R.id.list_slidermenu);

		versiontext = (TextView) findViewById(R.id.versiontext);

		/*
		 * PackageManager manager = this.getPackageManager();
		 * 
		 * try { info = manager.getPackageInfo( this.getPackageName(), 0); }
		 * catch (NameNotFoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * float versioncode = info.versionCode;
		 */
		versiontext.setText("Version :22.1");

		/*
		 * final ActionBar ab=getSupportActionBar();
		 * ab.setDisplayHomeAsUpEnabled(false);
		 */
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolBarCount = (Button) toolbar.findViewById(R.id.toolBarCount);

		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navDrawerItems = new ArrayList<com.okler.databeans.NavDrawerItems>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new com.okler.databeans.NavDrawerItems(
				navMenuTitles[0], R.drawable.home_icon));
		// offers
		/*
		 * navDrawerItems.add(new
		 * com.okler.databeans.NavDrawerItems(navMenuTitles[1],
		 * R.drawable.notification_icon));
		 */
		// Rate Us
		navDrawerItems.add(new com.okler.databeans.NavDrawerItems(
				navMenuTitles[1], R.drawable.black_like_thumb_icon));
		// Share The App
		navDrawerItems.add(new com.okler.databeans.NavDrawerItems(
				navMenuTitles[2], R.drawable.black_share_icon));
		// Support/Help
		navDrawerItems.add(new com.okler.databeans.NavDrawerItems(
				navMenuTitles[3], R.drawable.black_fan_icon));
		// Faqs
		navDrawerItems.add(new com.okler.databeans.NavDrawerItems(
				navMenuTitles[4], R.drawable.black_question_icon));
		// Legal
		navDrawerItems.add(new com.okler.databeans.NavDrawerItems(
				navMenuTitles[5], R.drawable.security_icon));
		// Contact Us
		navDrawerItems.add(new com.okler.databeans.NavDrawerItems(
				navMenuTitles[6], R.drawable.black_telephone_icon));
		
		navDrawerItems.add(new com.okler.databeans.NavDrawerItems(
				navMenuTitles[7],R.drawable.terms_conditions));

		// setting the nav drawer list adapter
		adapter = new com.okler.adapters.NavDrawerListAdapter(
				getApplicationContext(), navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, // nav
																				// menu
																				// toggle
																				// icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				// getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				// getSupportActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.setDrawerIndicatorEnabled(true);
		// getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}

		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Load correct fragment as per position
				displayView(position);
			}
		});

		context = getApplicationContext();
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);

		RelativeLayout rl = (RelativeLayout) bottomBarLayout
				.findViewById(R.id.homeRL);
		ImageButton btnHome = (ImageButton) rl.findViewById(R.id.btnHome);
		LinearLayout active_home = (LinearLayout) rl
				.findViewById(R.id.active_home);
		active_home.setVisibility(View.VISIBLE);
		btnHome.setEnabled(false);
		rl.setEnabled(false);

		/*layoutUploadPresc = (LinearLayout) findViewById(R.id.upload_presc_layout);
		layoutPharmacy = (LinearLayout) findViewById(R.id.pharmacy_layout);
		layoutDignisticService = (LinearLayout) findViewById(R.id.dignostic_service_layout);
		layoutMedicalService = (LinearLayout) findViewById(R.id.medical_service_layout);
		layoutPhysiotherapy = (LinearLayout) findViewById(R.id.physiotherapy_layout);
		layoutHealthShop = (LinearLayout) findViewById(R.id.health_shop_layout);*/
		imgUp = (ImageView)findViewById(R.id.imgUp);
		imgMeds = (ImageView)findViewById(R.id.imgMeds);
		imgDiagno = (ImageView)findViewById(R.id.imgDiagno);
		imgMedSer = (ImageView)findViewById(R.id.imgMedSer);
		imgPhysio = (ImageView)findViewById(R.id.imgPhysio);
		imgHealth = (ImageView)findViewById(R.id.imgHealth);
		
		
		setUploadPresListener();
		setPharmacyListener();
		setDiagnosticsListener();
		setMediListener();
		setPhysioListener();
		setHealthShopListener();

	}

	/**
	 * Displaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments

		switch (position) {
		case 0:// Home
			mDrawerLayout.closeDrawer(Gravity.LEFT);
			break;
		/*
		 * case 1://Offers Intent strtOffers = new Intent(this,
		 * OffersActivity.class); startActivity(strtOffers); break;
		 */

		case 1:// Rate Us
			Intent intent = new Intent(Intent.ACTION_VIEW);
			Uri Uri = null;
			intent.setData(Uri.parse("market://details?id=com.okleruser"));// 11_01_2016
																			// Gitesh
			if (!MyStartActivity(intent)) {
				// Market (Google play) app seems not installed, let's try to
				// open a webbrowser
				intent.setData(Uri
						.parse("https://play.google.com/store/apps/details?id=com.okleruser"));// 11_01_2016
																								// Gitesh
				if (!MyStartActivity(intent)) {
					// Well if this also fails, we have run out of options,
					// inform the user.
					// Toast.makeText(this,
					// "Could not open Android market, please install the market app.",
					// Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case 2:// Share the app
			shareApp();
			break;
		case 3: // Support/Help
			if (Utilities
					.getUserStatusFromSharedPref(ServiceCategoryActivity.this) ==
							UserStatusEnum.LOGGED_IN
					|| (Utilities
							.getUserStatusFromSharedPref(ServiceCategoryActivity.this) ==
							UserStatusEnum.LOGGED_IN_FB)
					|| (Utilities
							.getUserStatusFromSharedPref(ServiceCategoryActivity.this) ==
							UserStatusEnum.LOGGED_IN_GOOGLE)) {
				intent = new Intent(ServiceCategoryActivity.this,
						SupportHelpActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(ServiceCategoryActivity.this,
						NewSignIn.class);
				int ab = UIUtils.getBookingType("helpSupport");
				Okler.getInstance().setBookingType(ab);
				startActivity(intent);
			}
			/*
			 * Intent strtsupport = new Intent(this, SupportHelpActivity.class);
			 * startActivity(strtsupport);
			 */
			break;
		case 4: // Faqs
			Intent strtfag = new Intent(this, FaqActivity.class);
			startActivity(strtfag);
			break;
		case 5: // Legal
			Intent strtlegal = new Intent(this, LegalActivity.class);
			startActivity(strtlegal);
			break;
		case 6:
			Intent strtContact = new Intent(this, ContactUsActivity.class);
			startActivity(strtContact);
			break;
			
		case 7:
			Intent strtTerms=new Intent(this,LegalActivity.class);
			int in = 1;
			strtTerms.putExtra("t&c", in);
			startActivity(strtTerms);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		int id = item.getItemId();
		/*
		 * if (id == R.id.action_settings) { return true; }
		 */
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(relParent);
		// menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return true;
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		// getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	// 18_12_2015 Gitesh start
	private boolean MyStartActivity(Intent aIntent) {
		try {
			startActivity(aIntent);
			return true;
		} catch (ActivityNotFoundException e) {
			return false;
		}
	}

	// 18_12_2015 Gitesh ends

	private void shareApp() {
		// create the send intent
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

		// set the type
		shareIntent.setType("text/plain");

		// add a subject
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Hello From Okler");

		// build the body of the message to be shared
		String shareMessage = "Download Okler today. "
				+ "Visit https://play.google.com/store/apps/details?id=com.okleruser&hl=en";

		// add the message
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);

		// start the chooser for sharing
		startActivity(Intent.createChooser(shareIntent, "Share Okler Via"));
	}

	private void setUploadPresListener() {
		
		imgUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Utilities
						.getUserStatusFromSharedPref(ServiceCategoryActivity.this) ==
								UserStatusEnum.LOGGED_IN
						|| (Utilities
								.getUserStatusFromSharedPref(ServiceCategoryActivity.this) ==
								UserStatusEnum.LOGGED_IN_FB)
						|| (Utilities
								.getUserStatusFromSharedPref(ServiceCategoryActivity.this) ==
								UserStatusEnum.LOGGED_IN_GOOGLE)) {
					intent = new Intent(context, UploadPrescription.class);
					intent.putExtra("isMedPres", true);
					int ab = UIUtils.getBookingType("UploadPresc");
					Okler.getInstance().setBookingType(ab);
					startActivity(intent);
				} else {
					Intent in = new Intent(ServiceCategoryActivity.this,
							NewSignIn.class);
					int ab = UIUtils.getBookingType("UploadPresc");
					Okler.getInstance().setBookingType(ab);
					startActivity(in);
					// finish();
				}

			}
		});
	}

	private void setPharmacyListener() {
		
		imgMeds.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(),
				// "Pharmacy activity shown", Toast.LENGTH_LONG).show();
				intent = new Intent(context, MedicineHomeActivity.class);// 12_01_2016
				Okler.getInstance().setBookingType(0);															// Gitesh
				startActivity(intent);// 12_01_2016 Gitesh
			}
		});
	}

	private void setDiagnosticsListener() {
		
		imgDiagno.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(),
				// "Diagnosis activity shown", Toast.LENGTH_LONG).show();
				intent = new Intent(context, DiagnosticsActivityHome.class);
				int ab = uiUtils.getBookingType("Diagnostic");
				Okler.getInstance().setBookingType(ab);
				startActivity(intent);
			}
		});
	}

	private void setMediListener() {
		
		imgMedSer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Utilities
						.getUserStatusFromSharedPref(ServiceCategoryActivity.this) == 
								UserStatusEnum.LOGGED_IN
						|| (Utilities
								.getUserStatusFromSharedPref(ServiceCategoryActivity.this) ==
								UserStatusEnum.LOGGED_IN_FB)
						|| (Utilities
								.getUserStatusFromSharedPref(ServiceCategoryActivity.this) ==
								UserStatusEnum.LOGGED_IN_GOOGLE)) {
					intent = new Intent(context, MedicalServices.class);
					startActivity(intent);
				} else {
					intent = new Intent(context, NewSignIn.class);
					int ab = uiUtils.getBookingType("medical_services");
					Okler.getInstance().setBookingType(ab);
					startActivity(intent);
				}
			}
		});
	}

	private void setPhysioListener() {
		
		imgPhysio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Utilities
						.getUserStatusFromSharedPref(ServiceCategoryActivity.this) == 
								UserStatusEnum.LOGGED_IN
						|| (Utilities
								.getUserStatusFromSharedPref(ServiceCategoryActivity.this) == 
								UserStatusEnum.LOGGED_IN_FB)
						|| (Utilities
								.getUserStatusFromSharedPref(ServiceCategoryActivity.this) == 
								UserStatusEnum.LOGGED_IN_GOOGLE)) {
					intent = new Intent(context, Physiotherapy.class);
					startActivity(intent);
				} else {
					intent = new Intent(context, NewSignIn.class);
					int ab = uiUtils.getBookingType("physiotherapy");
					Okler.getInstance().setBookingType(ab);
					startActivity(intent);
				}
			}
		});
	}

	private void setHealthShopListener() {
		
		imgHealth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(getApplicationContext(),
				// "Health shop activity shown", Toast.LENGTH_LONG).show();
				intent = new Intent(context, HealthShopGrid.class);
				int in = 0;
				intent.putExtra("name", 0);
				intent.putExtra("currentView", "Grid");
				uiUtils = new UIUtils();
				int ab = uiUtils.getBookingType("Healthshop");
				Okler.getInstance().setBookingType(ab);
				int a = Okler.getInstance().getBookingType();
				// Toast.makeText(getApplicationContext(), ""+a,
				// Toast.LENGTH_LONG).show();
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIUtils.setCartCount(toolBarCount, ack);
		toolBarCount.setText(toolBarCount.getText().toString() + "  ");
	}
}
