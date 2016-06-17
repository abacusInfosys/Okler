package com.okler.diagnostics;

import java.security.DigestInputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.okler.android.BaseActivity;
import com.okler.android.NewSignIn;
import com.okler.android.SplashActivity;
import com.okleruser.R;

import com.okler.android.SplashActivity;
import com.okler.customviews.CustomViewLabsForSelectedPckg;
import com.okler.customviews.CustomViewLabsForSelectedTest;
import com.okler.databeans.AddressDataBean;
import com.okler.databeans.DiagnoLabsDataBean;
import com.okler.databeans.DiagnoOrderDataBean;
import com.okler.databeans.DiagnoPackageDataBean;
import com.okler.databeans.TestDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.UIUtils;
import com.okler.utils.UserStatusEnum;
import com.okler.utils.Utilities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DiagnoLabsAvailable extends BaseActivity implements
		Response.Listener, Response.ErrorListener {

	private Button schedule_pickBtn, toolBarCount;
	private Toolbar toolBar;
	private ImageView imgBack;
	private View bottomBarLayout;
	private String[] receivedTestIdOrPckId;
	private String receivedPckgId;
	private String ConvertTestIdOrPckId, getLabUrl, calledFrom, getLabPckUrl;
	private Context context;
	Activity activity;
	boolean isCalled;
	RelativeLayout back_layout;
	LinearLayout progressLinLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagno_labs_available);
		context = this;
		activity = this;
		bottomBarLayout = findViewById(R.id.bottombar);
		handleMapping(bottomBarLayout);
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		toolBarCount = (Button) findViewById(R.id.toolBarCount);
		toolBarCount.setVisibility(View.GONE);
		setSupportActionBar(toolBar);
		final ActionBar ab = getSupportActionBar();
		// ab.setDisplayHomeAsUpEnabled(true);
		toolBar.setBackgroundResource(R.drawable.custom_view_grad_diagno);
		progressLinLayout = (LinearLayout)findViewById(R.id.progressLinLayout);
		/*back_layout = (RelativeLayout)toolBar.findViewById(R.id.back_layout);
		back_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
		imgBack = (ImageView) toolBar.findViewById(R.id.toolbar_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(imgBack.getWindowToken(), 0);
			}
		});*/
		UIUtils.setBackClick(toolBar, activity);
		Utilities.setTitleText(toolBar, "Diagnostic Test [2/5]");
		calledFrom = getIntent().getStringExtra("callFromKey");
		if (calledFrom.equals("FromTest")) {
			receivedTestIdOrPckId = getIntent().getStringArrayExtra("testId");
			for (int i = 0; i < receivedTestIdOrPckId.length; i++) {
				if (i == 0) {
					ConvertTestIdOrPckId = receivedTestIdOrPckId[i];
				} else {
					ConvertTestIdOrPckId = ConvertTestIdOrPckId + ","
							+ receivedTestIdOrPckId[i];
				}
			}
			System.out.println("ConvertTestIdOrPckId" + ConvertTestIdOrPckId);
			// webserviceCall
			getLabUrl = getString(R.string.getLabsTest1) + ConvertTestIdOrPckId
					+ getString(R.string.getLabsTest2);
			getLabsFor(getLabUrl);

		} else {
			receivedPckgId = getIntent().getStringExtra("pckgId");
			// webserviceCall
			getLabPckUrl = getString(R.string.getLabsForPackage1)
					+ receivedPckgId + getString(R.string.getLabsForPackage2);
			getLabsFor(getLabPckUrl);

		}

	}

	private void getLabsFor(String url) {
		// TODO Auto-generated method stub
		WebJsonObjectRequest webobj = new WebJsonObjectRequest(Method.GET, url,
				new JSONObject(), this, this);
		if(VolleyRequest.addJsonObjectRequest(context, webobj))
			showProgress(true);
		else
			showProgress(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.diagno_labs_available, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DiagnoLabsDataBean togetBean = new DiagnoLabsDataBean();
		DiagnoOrderDataBean diagnoOdr = new DiagnoOrderDataBean();
		diagnoOdr.setSelectedLab(togetBean);

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

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		String err = String.valueOf(error);
		System.out.println("Error" + err);
		showProgress(false);
	}

	@Override
	public void onResponse(Object response) {
		// TODO Auto-generated method stub
		if (calledFrom.equals("FromTest")) {
			try {
				displayLabs(response);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showProgress(false);
			}
		} else {
			displayLabsForPck(response);
		}
	}

	private void displayLabs(Object response) throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jobj = (JSONObject) response;
		JSONObject recommended_package_detail;
		JSONArray resultArray, innerresultarrayLabcnt;// ,gallery_jarray;
		DiagnoLabsDataBean labBean = null;
		TestDataBean testBean = new TestDataBean();
		DiagnoPackageDataBean pckBean = new DiagnoPackageDataBean();
		ArrayList<DiagnoLabsDataBean> finallist = null;
		ArrayList<TestDataBean> testList;

		String message = jobj.getString("message");
		if (message.equals("No data found.")) {
			// labBean= new DiagnoLabsDataBean();
			TextView nodata = (TextView) findViewById(R.id.Nodata);
			nodata.setVisibility(View.VISIBLE);
			nodata.setText("No Data Found");

		} else {
			try {
				String imagePath = jobj.getString("lab_image_url");
				// main array
				resultArray = jobj.getJSONArray("result");
				/*
				 * if(resultArray.equals("")){
				 * Toast.makeText(context,"No Lab found"
				 * ,Toast.LENGTH_LONG).show(); }else{
				 */
				finallist = new ArrayList<DiagnoLabsDataBean>();
				for (int i = 0; i < resultArray.length(); i++) {
					// we get labs
					testList = new ArrayList<TestDataBean>();
					labBean = new DiagnoLabsDataBean();
					innerresultarrayLabcnt = resultArray.getJSONArray(i);
					System.out.println("LabCount"
							+ innerresultarrayLabcnt.length());
					for (int j = 0; j < innerresultarrayLabcnt.length(); j++) {
						// we get inner-object for each test
						System.out.println("testCount"
								+ innerresultarrayLabcnt.length());
						JSONObject testwiseObj = innerresultarrayLabcnt
								.getJSONObject(j);
						// System.out.println("testCount"+testwiseObj.length());

						testBean = new TestDataBean();
						labBean.setLabId(Integer.parseInt(testwiseObj
								.getString("lab_id")));
						labBean.setLab_name(testwiseObj.getString("lab_name"));
						labBean.setLabDescrption(testwiseObj
								.getString("lab_description"));
						String log1 = testwiseObj.getString("lab_image");
						if (log1.equals("null")) {
							labBean.setLab_Log_image(log1);
						} else {
							String log = imagePath
									+ (testwiseObj.getString("lab_image"));
							labBean.setLab_Log_image(log);
						}
						String award_image = imagePath
								+ (testwiseObj.getString("lab_awards"));
						labBean.setLab_award_image(award_image);
						labBean.setLab_image_path(imagePath);
						// to get labgalleryImages
						/*
						 * JSONArray
						 * gallery_jarray=testwiseObj.getJSONArray("lab_gallery"
						 * );
						 * 
						 * for(int y=0;y<gallery_jarray.length();y++){
						 * gallery_Image
						 * [y]=imagePath+gallery_jarray.getJSONObject
						 * (y).getString(""); }
						 */
						String gallerImage = testwiseObj
								.getString("lab_gallery");
						if (gallerImage.equals("null")
								|| gallerImage.equals("")) {
							labBean.setLab_gallery_image(null);
						} else {
							int len = testwiseObj.getString("lab_gallery")
									.length();
							String gallery_Image = (testwiseObj
									.getString("lab_gallery")).substring(1,
									len - 1);
							String ar[] = gallery_Image.split(",");
							int count = ar.length;
							String[] arrayLabImage = new String[count];
							for (int q = 0; q < count; q++) {
								int size = ar[q].length();
								arrayLabImage[q] = ar[q].substring(1, size - 1);
								System.out.println("arrayLabImage"
										+ arrayLabImage);
							}

							labBean.setLab_gallery_image(arrayLabImage);
						}
						testBean.setMarketPrice(Float.parseFloat(testwiseObj
								.getString("mrp")));
						int lenght = (testwiseObj.getString("okler_discount"))
								.length();
						String discount = (testwiseObj
								.getString("okler_discount"));
						System.out.println("discount" + discount);
						if (discount.equals("null")) {
							discount = "0";
							testBean.setYouSave(Float.parseFloat(discount));
						} else {
							testBean.setYouSave(Float.parseFloat(discount));
						}

						testBean.setOklerTestPrice(Float.parseFloat(testwiseObj
								.getString("okler_amount")));
						testBean.setTestId(Integer.parseInt(testwiseObj
								.getString("entity_id")));
						testBean.setTestname(testwiseObj
								.getString("OklerTestName"));
						testBean.setDescription(testwiseObj
								.getString("testDescription"));
						String manadatoryInfo = testwiseObj
								.getString("mandatoryInfo");
						if (manadatoryInfo.equals("null")) {
							testBean.setMandatoryInfo(null);
						} else {
							testBean.setMandatoryInfo(testwiseObj
									.getString("mandatoryInfo"));
						}
						// get recommended package for each test
						recommended_package_detail = testwiseObj
								.getJSONObject("recommended_package_details");
						if (recommended_package_detail.equals(null)
								|| recommended_package_detail.equals("")) {
							pckBean.setPackage_name("Not Available");

						} else {

							JSONArray pacakgeDetailArray = recommended_package_detail
									.getJSONArray("packageDetails");
							if (pacakgeDetailArray.equals("")
									|| (pacakgeDetailArray.length() == 0)) {
								// pckBean.setPackage_name("Not Available");
								// break;
							} else {

								JSONObject pckObj = pacakgeDetailArray
										.getJSONObject(0);
								if (pckObj.length() == 0 || pckObj.equals("")) {
									break;

								} else {
									pckBean.setPacakageId(Integer
											.parseInt(pckObj
													.getString("package_id")));
									String pck_name = pckObj
											.getString("package_name");
									pckBean.setPackage_name(pck_name);
									String pck_descp = pckObj
											.getString("package_description");
									pckBean.setDescription(pck_descp);

								}
								JSONObject pckAmountRelated = pacakgeDetailArray
										.getJSONObject(1);
								if (pckAmountRelated.length() == 0
										|| pckAmountRelated.equals("")) {
									break;
								} else {

									pckBean.setPack_mrp(Float
											.parseFloat(pckAmountRelated
													.getString("mrp")));
									pckBean.setPack_oklerPrice(Float
											.parseFloat(pckAmountRelated
													.getString("okler_amount")));
									pckBean.setPack_you_save(Integer.parseInt(pckAmountRelated
											.getString("okler_discount")));
									testBean.setPckBean(pckBean);
								}
							}

						}

						testList.add(testBean);
					}

					labBean.setTestBean(testList);
					finallist.add(labBean);

				}

			} catch (Exception e) {
				e.printStackTrace();
				showProgress(false);
			}
			if (finallist.size() > 0) {
				addLabsTestCustView(finallist);
				showProgress(false);
			} else {
				showProgress(false);
				Toast.makeText(context,
						"Please select test to get related Lab info",
						Toast.LENGTH_LONG).show();
			}

		}

	}

	private void addLabsTestCustView(
			final ArrayList<DiagnoLabsDataBean> finallist) {
		// TODO Auto-generated method stub
		int labCount = finallist.size();
		View[] views = new View[labCount];
		View[] seperatorViews = new View[labCount];
		ImageLoader imgLoader;
		TestDataBean testBean;
		DiagnoLabsDataBean labBean;
		RelativeLayout TestLayoutforOnClick, PckayoutforOnClick;
		// rupeeSymbol = getResources().getString(R.string.curr_rupee);
		for (int i = 0; i < labCount; i++) {

			labBean = new DiagnoLabsDataBean();
			RelativeLayout mainParentLayout = (RelativeLayout) findViewById(R.id.labCustViewLayout);
			LinearLayout labLinearlayout = (LinearLayout) mainParentLayout
					.findViewById(R.id.labLayout);
			views[i] = getLayoutInflater().inflate(R.layout.row_lab_info, null);
			// views[i].setTag(i+100);
			NetworkImageView logImage = (NetworkImageView) views[i]
					.findViewById(R.id.imgLabLogo);
			imgLoader = VolleyRequest.getInstance(context).getImageLoader();
			TextView labname = (TextView) views[i].findViewById(R.id.tvLabName);
			TextView labId = (TextView) views[i].findViewById(R.id.labId);
			String url = finallist.get(i).getLab_Log_image();
			logImage.setImageUrl(url, imgLoader);
			final ImageView tickImage = (ImageView) views[i]
					.findViewById(R.id.tickImage);
			int labID = finallist.get(i).getLabId();
			labname.setText(finallist.get(i).getLab_name());
			tickImage.setTag(R.string.labid, labID);
			tickImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DiagnoLabsDataBean togetBean = new DiagnoLabsDataBean();
					DiagnoOrderDataBean diagnoOdr = new DiagnoOrderDataBean();
					String labId = String.valueOf(v.getTag(R.string.labid));
					for (int i = 0; i < finallist.size(); i++) {
						String tempLabId = String.valueOf(finallist.get(i)
								.getLabId());
						if (labId.equals(tempLabId)) {
							togetBean = finallist.get(i);
							diagnoOdr.setSelectedLab(togetBean);
							Okler.getInstance().setDiagnoOrderDataBean(
									diagnoOdr);
							break;
						}
					}
					if (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN
							|| (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_FB)
							|| (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
						UsersDataBean ubean = Utilities
								.getCurrentUserFromSharedPref(activity);
						ArrayList<AddressDataBean> paList = new ArrayList<AddressDataBean>();
						paList = ubean.getPatAddList();
						if (paList.size() <= 0)
							getPatAdresses();
						else
							isCalled = true;
						if (isCalled) {
							if (paList.size() <= 0) {
								Intent intent = new Intent(
										getApplicationContext(),
										EnterPatientInfoActivity.class);
								startActivity(intent);
							} else {
								Intent intent = new Intent(
										getApplicationContext(),
										SelectPatientInfoActivity.class);
								startActivity(intent);
							}
						}
					} else {
						Intent in = new Intent(context, NewSignIn.class);
						context.startActivity(in);
					}
				}
			});
			labLinearlayout.addView(views[i]);

			int TotalTestinLab = (finallist.get(i).getTestBean()).size();
			if (TotalTestinLab == 0) {

			} else {
				// LinearLayout
				// custTestInLabLinLayout=(LinearLayout)mainParentLayout.findViewById(R.id.labtestOrpckgCustViewLayout);
				ArrayList<TestDataBean> templist = finallist.get(i)
						.getTestBean();
				CustomViewLabsForSelectedTest[] custViewTestsArrInLab = new CustomViewLabsForSelectedTest[templist
						.size()];
				for (int l = 0; l < TotalTestinLab; l++) {
					// testBean=new TestDataBean();
					custViewTestsArrInLab[l] = new CustomViewLabsForSelectedTest(
							context);
					// for test
					TextView testName = (TextView) custViewTestsArrInLab[l]
							.findViewById(R.id.testValue);
					testName.setText(templist.get(l).getTestname());
					int testID = templist.get(l).getTestId();
					TextView testMrp = (TextView) custViewTestsArrInLab[l]
							.findViewById(R.id.tv_MrpValue);
					testMrp.setText(templist.get(l).getMarketPrice() + "");
					TextView testYouSave = (TextView) custViewTestsArrInLab[l]
							.findViewById(R.id.tv_youSaveValue);
					testYouSave.setText(templist.get(l).getYouSave() + "%");
					Button testOklerPrice = (Button) custViewTestsArrInLab[l]
							.findViewById(R.id.okler_Amount);
					testOklerPrice.setText("Okler Price :"
							+ templist.get(l).getOklerTestPrice());
					TestLayoutforOnClick = (RelativeLayout) custViewTestsArrInLab[l]
							.findViewById(R.id.testRelParent);
					TestLayoutforOnClick.setTag(R.string.labid, labID);
					TestLayoutforOnClick.setTag(R.string.testid, testID);
					TestLayoutforOnClick
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									DiagnoLabsDataBean togetBean = new DiagnoLabsDataBean();
									Boolean flag = true;
									String labId = String.valueOf(v
											.getTag(R.string.labid));

									String test = String.valueOf(v
											.getTag(R.string.testid));
									System.out.println("labId" + labId);
									for (int i = 0; i < finallist.size(); i++) {
										String tempLabId = String
												.valueOf(finallist.get(i)
														.getLabId());
										if (labId.equals(tempLabId)) {
											togetBean = finallist.get(i);
										}
									}
									Gson gson = new Gson();
									String sendDetails = gson.toJson(togetBean);
									System.out.println("sendDetails"
											+ sendDetails);
									Intent i = new Intent(
											getApplicationContext(),
											DiagnoLabTestPkgActivity.class);
									i.putExtra("clickedItem", sendDetails);
									i.putExtra("flag", flag);
									i.putExtra("testid", test);
									startActivity(i);
								}
							});
					// for recommended package
					DiagnoPackageDataBean pckBean = new DiagnoPackageDataBean();
					pckBean = templist.get(l).getPckBean();
					int rec_packID = pckBean.getPacakageId();
					if (rec_packID == 0) {
						// String rec_packName=pckBean.getPackage_name();

						TextView packName = (TextView) custViewTestsArrInLab[l]
								.findViewById(R.id.pkgName);
						packName.setText("Not Available");
						TextView pckMrp = (TextView) custViewTestsArrInLab[l]
								.findViewById(R.id.tv_MrpValuePkg);
						pckMrp.setVisibility(View.GONE);
						TextView pckYouSave = (TextView) custViewTestsArrInLab[l]
								.findViewById(R.id.tv_youSaveValuePkg);
						pckYouSave.setVisibility(View.GONE);
						Button pckOklerPrice = (Button) custViewTestsArrInLab[l]
								.findViewById(R.id.okler_AmountPkg);
						pckOklerPrice.setVisibility(View.GONE);
						TextView titlemrp = (TextView) custViewTestsArrInLab[l]
								.findViewById(R.id.tv_MrpTagPkg);
						titlemrp.setVisibility(View.GONE);
						TextView titleYouSave = (TextView) custViewTestsArrInLab[l]
								.findViewById(R.id.tv_youSavePkg);
						titleYouSave.setVisibility(View.GONE);

					} else {
						TextView packName = (TextView) custViewTestsArrInLab[l]
								.findViewById(R.id.pkgName);
						packName.setText(pckBean.getPackage_name());
						/*
						 * TextView
						 * pckId=(TextView)custViewTestsArrInLab[l].findViewById
						 * (R.id.rec_pckId);
						 * pckId.setText(pckBean.getPacakageId());
						 */
						int pck_ID = pckBean.getPacakageId();
						TextView pckMrp = (TextView) custViewTestsArrInLab[l]
								.findViewById(R.id.tv_MrpValuePkg);
						pckMrp.setText(pckBean.getPack_mrp() + "");

						System.out.println("value of index" + l);
						TextView pckYouSave = (TextView) custViewTestsArrInLab[l]
								.findViewById(R.id.tv_youSaveValuePkg);
						// pckYouSave.setText(pckBean.getPack_you_save());
						pckYouSave.setText("10%");
						Button pckOklerPrice = (Button) custViewTestsArrInLab[l]
								.findViewById(R.id.okler_AmountPkg);
						pckOklerPrice.setText("Okler Price :"
								+ Float.parseFloat(pckBean.getPack_oklerPrice()
										+ ""));
						PckayoutforOnClick = (RelativeLayout) custViewTestsArrInLab[l]
								.findViewById(R.id.pckgRelParent);
						PckayoutforOnClick.setTag(R.string.labid, labID);
						PckayoutforOnClick.setTag(R.string.testid, testID);
						PckayoutforOnClick
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										DiagnoLabsDataBean togetBean = new DiagnoLabsDataBean();
										Boolean flag = false;
										String labId = String.valueOf(v
												.getTag(R.string.labid));

										String test = String.valueOf(v
												.getTag(R.string.testid));
										System.out.println("labId" + labId);
										for (int i = 0; i < finallist.size(); i++) {
											String tempLabId = String
													.valueOf(finallist.get(i)
															.getLabId());
											if (labId.equals(tempLabId)) {
												togetBean = finallist.get(i);
											}
										}
										Gson gson = new Gson();
										String sendDetails = gson
												.toJson(togetBean);
										System.out.println("sendDetails"
												+ sendDetails);
										Intent intent = new Intent(
												getApplicationContext(),
												DiagnoLabTestPkgActivity.class);
										intent.putExtra("clickedItem",
												sendDetails);
										intent.putExtra("flag", flag);
										intent.putExtra("testid", test);
										startActivity(intent);
										// System.out.println("labId"+labId);
									}
								});
					}
					labLinearlayout.addView(custViewTestsArrInLab[l]);
				}
			}
			seperatorViews[i] = getLayoutInflater().inflate(
					R.layout.separator_1dp, null);
			labLinearlayout.addView(seperatorViews[i]);
		}
		showProgress(false);
	}

	private void displayLabsForPck(Object response) {
		// TODO Auto-generated method stub
		JSONObject jobj = (JSONObject) response;
		JSONArray jresultArray, jLabArray;
		JSONObject jobjLab;
		String lab_Image_Url;
		ArrayList<DiagnoLabsDataBean> finallist = new ArrayList<DiagnoLabsDataBean>();
		DiagnoLabsDataBean labBean = null;
		DiagnoPackageDataBean pckBean = new DiagnoPackageDataBean();
		try {
			jresultArray = jobj.getJSONArray("result");
			lab_Image_Url = jobj.getString("lab_image_url");
			for (int i = 0; i < jresultArray.length(); i++) {
				jLabArray = jresultArray.getJSONArray(i);

				jobjLab = jLabArray.getJSONObject(0);
				labBean = new DiagnoLabsDataBean();

				labBean.setLabId(Integer.parseInt(jobjLab.getString("lab_id")));

				labBean.setLab_name(jobjLab.getString("lab_name"));

				labBean.setLabDescrption(jobjLab.getString("lab_description"));
				// get lab log
				String log1 = jobjLab.getString("lab_image");
				if (log1.equals("null")) {
					labBean.setLab_Log_image(log1);
				} else {
					String log = lab_Image_Url
							+ (jobjLab.getString("lab_image"));
					labBean.setLab_Log_image(log);
				}

				String award_image = lab_Image_Url
						+ (jobjLab.getString("lab_awards"));
				labBean.setLab_award_image(award_image);

				labBean.setLab_image_path(lab_Image_Url);

				String gallerImage = jobjLab.getString("lab_gallery");
				if (gallerImage.equals("null") || gallerImage.equals("")) {
					labBean.setLab_gallery_image(null);
				} else {
					int len = jobjLab.getString("lab_gallery").length();
					String gallery_Image = (jobjLab.getString("lab_gallery"))
							.substring(1, len - 1);
					String ar[] = gallery_Image.split(",");
					int count = ar.length;
					String[] arrayLabImage = new String[count];
					for (int q = 0; q < count; q++) {
						int size = ar[q].length();
						arrayLabImage[q] = ar[q].substring(1, size - 1);
						System.out.println("arrayLabImage" + arrayLabImage);
					}
					labBean.setLab_gallery_image(arrayLabImage);
				}

				pckBean = new DiagnoPackageDataBean();

				pckBean.setPackage_name(jobjLab.getString("OklerPackageName"));
				pckBean.setDescription(jobjLab.getString("packageDescription"));
				pckBean.setPacakageId(Integer.parseInt(jobjLab
						.getString("entity_id")));
				pckBean.setPack_mrp(Float.parseFloat(jobjLab.getString("mrp")));
				pckBean.setPack_oklerPrice(Float.parseFloat(jobjLab
						.getString("okler_amount")));
				String youSaveValue = jobjLab.getString("okler_discount");
				if (youSaveValue.equals("null")) {
					pckBean.setPack_you_save(0);
				} else {
					int len = jobjLab.getString("okler_discount").length();
					// String
					// youSave=(jobjLab.getString("okler_discount")).substring(0,len-1);
					String youSave = (jobjLab.getString("okler_discount"));
					pckBean.setPack_you_save(Integer.parseInt(youSave));
				}
				labBean.setPackageBean(pckBean);

				finallist.add(labBean);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showProgress(false);
		}
		if (finallist.size() > 0) {
			addLabsPckCustView(finallist);
		} else {
			showProgress(false);
			Toast.makeText(context,
					"Please select test to get related Lab info",
					Toast.LENGTH_LONG).show();
		}

	}

	private void addLabsPckCustView(
			final ArrayList<DiagnoLabsDataBean> finallist) {
		// TODO Auto-generated method stub
		int labCount = finallist.size();
		View[] views = new View[labCount];
		ImageLoader imgLoader;
		DiagnoLabsDataBean labBean;
		DiagnoPackageDataBean pckBean;
		CustomViewLabsForSelectedPckg[] custViewPackage = new CustomViewLabsForSelectedPckg[labCount];
		for (int i = 0; i < labCount; i++) {
			labBean = new DiagnoLabsDataBean();
			RelativeLayout mainParentLayout = (RelativeLayout) findViewById(R.id.labCustViewLayout);
			LinearLayout labLinearlayout = (LinearLayout) mainParentLayout
					.findViewById(R.id.labLayout);
			views[i] = getLayoutInflater().inflate(R.layout.row_lab_info, null);
			// views[i].setTag(i+100);
			NetworkImageView logImage = (NetworkImageView) views[i]
					.findViewById(R.id.imgLabLogo);
			imgLoader = VolleyRequest.getInstance(context).getImageLoader();
			TextView labname = (TextView) views[i].findViewById(R.id.tvLabName);
			TextView labId = (TextView) views[i].findViewById(R.id.labId);
			String url = finallist.get(i).getLab_Log_image();
			logImage.setImageUrl(url, imgLoader);
			final ImageView tickImage = (ImageView) views[i]
					.findViewById(R.id.tickImage);
			int labID = finallist.get(i).getLabId();
			labname.setText(finallist.get(i).getLab_name());
			tickImage.setTag(R.string.labid, labID);
			tickImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DiagnoLabsDataBean togetBean = new DiagnoLabsDataBean();
					String labId = String.valueOf(v.getTag(R.string.labid));
					// ImageView tickImage=(ImageView)
					// v.findViewById(R.id.tickImage);
					// tickImage.setImageDrawable(getResources().getDrawable(R.drawable.red_tick_icon));
					for (int i = 0; i < finallist.size(); i++) {
						String tempLabId = String.valueOf(finallist.get(i)
								.getLabId());
						if (labId.equals(tempLabId)) {
							togetBean = finallist.get(i);
							DiagnoOrderDataBean obean = new DiagnoOrderDataBean();
							obean.setSelectedLab(togetBean);
							Okler.getInstance().setDiagnoOrderDataBean(obean);
							break;
						}
					}

					if (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN
							|| (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_FB)
							|| (Utilities.getUserStatusFromSharedPref(activity) == UserStatusEnum.LOGGED_IN_GOOGLE)) {
						UsersDataBean ubean = Utilities
								.getCurrentUserFromSharedPref(activity);
						ArrayList<AddressDataBean> paList = new ArrayList<AddressDataBean>();
						paList = ubean.getPatAddList();
						if (paList.size() <= 0)
							getPatAdresses();
						else
							isCalled = true;
						if (isCalled) {
							if (paList.size() <= 0) {
								Intent intent = new Intent(
										getApplicationContext(),
										EnterPatientInfoActivity.class);
								startActivity(intent);
							} else {
								Intent intent = new Intent(
										getApplicationContext(),
										SelectPatientInfoActivity.class);
								startActivity(intent);
							}
						}
					} else {
						Intent in = new Intent(context, NewSignIn.class);
						context.startActivity(in);
					}
				}
			});
			labLinearlayout.addView(views[i]);

			// custview for package
			pckBean = new DiagnoPackageDataBean();
			custViewPackage[i] = new CustomViewLabsForSelectedPckg(context);
			LinearLayout mainLayout = (LinearLayout) custViewPackage[i]
					.findViewById(R.id.setPckInfo);
			TextView packageName = (TextView) mainLayout
					.findViewById(R.id.pckValue);
			TextView mrpValue = (TextView) mainLayout
					.findViewById(R.id.tv_MrpValue);
			TextView youSaveValue = (TextView) mainLayout
					.findViewById(R.id.tv_youSaveValue);
			Button oklerPrice1 = (Button) mainLayout
					.findViewById(R.id.okler_Amount);

			pckBean = finallist.get(i).getPackageBean();
			packageName.setText(pckBean.getPackage_name());
			mrpValue.setText(pckBean.getPack_mrp() + "");
			youSaveValue.setText(pckBean.getPack_you_save() + "%");
			oklerPrice1.setText("Okler Price :" + pckBean.getPack_oklerPrice());

			mainLayout.setTag(R.string.labid, labID);
			mainLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DiagnoLabsDataBean togetBean = new DiagnoLabsDataBean();
					Boolean flag = false;
					String labId = String.valueOf(v.getTag(R.string.labid));
					System.out.println("labId" + labId);
					for (int i = 0; i < finallist.size(); i++) {
						String tempLabId = String.valueOf(finallist.get(i)
								.getLabId());
						if (labId.equals(tempLabId)) {
							togetBean = finallist.get(i);
							DiagnoOrderDataBean odBean = new DiagnoOrderDataBean();
							odBean.setSelectedLab(togetBean);
							Okler.getInstance().setDiagnoOrderDataBean(odBean);

						}
					}
					Gson gson = new Gson();
					String sendDetails = gson.toJson(togetBean);
					System.out.println("sendDetails" + sendDetails);
					Intent intent = new Intent(getApplicationContext(),
							DiagnoLabTestPkgActivity.class);
					intent.putExtra("clickedItem", sendDetails);
					intent.putExtra("flag", flag);
					intent.putExtra("key", "DiagnoPck");
					startActivity(intent);
					// System.out.println("labId"+labId);

				}
			});

			labLinearlayout.addView(custViewPackage[i]);

		}
		showProgress(false);
	}

	public void getPatAdresses() {
		final UsersDataBean ubean = Utilities
				.getCurrentUserFromSharedPref(activity);
		int userId = ubean.getId();

		String pAddrUrl = getString(R.string.GetPatientsAllAddr) + userId;
		WebJsonObjectRequest pjson = new WebJsonObjectRequest(Method.GET,
				pAddrUrl, new JSONObject(),
				new Response.Listener<JSONObject>() {

					AddressDataBean adbean = new AddressDataBean();
					JSONObject jobj = new JSONObject();
					JSONObject jobj2;
					ArrayList<AddressDataBean> PaList = new ArrayList<AddressDataBean>();

					@Override
					public void onResponse(JSONObject response) {
						try {
							JSONArray jarr = response.getJSONArray("result");
							int length = jarr.length();
							PaList = new ArrayList<AddressDataBean>();
							for (int i = 0; i < length; i++) {
								// jobj = new JSONObject();
								jobj = jarr.getJSONObject(i);
								adbean = new AddressDataBean();
								adbean.setPat_id(Integer.parseInt(jobj
										.optString("pat_id")));
								adbean.setFirstname(jobj.optString("firstname"));
								adbean.setLastname(jobj.optString("middlename"));
								adbean.setDob(jobj.optString("dob"));
								adbean.setRelationId(jobj
										.optString("relationid"));
								adbean.setGenderId(jobj.optString("gender"));
								adbean.setPhone(jobj.optString("mobileno"));
								jobj2 = new JSONObject();
								if ((jobj.getString("residential_address") != null)
										&& (jobj.getString("residential_address") != "false")) {
									jobj2 = jobj
											.getJSONObject("residential_address");
									adbean.setAddress1(jobj2.optString("addr1"));
									adbean.setAddress2(jobj2.optString("addr2"));
									adbean.setLandmark(jobj2
											.optString("land_mark"));
									adbean.setZip(jobj2.optString("pincode"));
									adbean.setCity_id(jobj2
											.optString("city_id"));
									adbean.setCity(jobj2.optString("city_name"));
									adbean.setGender(jobj
											.optString("userGender"));
									adbean.setRelation(jobj
											.optString("relation_name"));
								}
								PaList.add(adbean);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ubean.setPatAddList(PaList);
						Utilities.writeCurrentUserToSharedPref(activity, ubean);
						isCalled = true;

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						// Utilities.handleVollyError(error, ack);
						Log.e("ERROR", "" + error.getStackTrace());
						isCalled = true;
					}
				});
		VolleyRequest.addJsonObjectRequest(activity, pjson);

		// patient address call ends here
	}

	private void showProgress(boolean paramBoolean)
	 {
	   if (paramBoolean)
	   {
	     this.progressLinLayout.setVisibility(View.VISIBLE);
	     return;
	   }
	   this.progressLinLayout.setVisibility(View.INVISIBLE);
	 }
}
