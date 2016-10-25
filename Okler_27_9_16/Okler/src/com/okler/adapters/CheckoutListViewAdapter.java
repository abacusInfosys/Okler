package com.okler.adapters;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.okler.android.ProductCheckoutListView;
import com.okleruser.R;
import com.okler.databeans.CartDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.UsersDataBean;
import com.okler.network.VolleyRequest;
import com.okler.network.WebJsonObjectRequest;
import com.okler.utils.Okler;
import com.okler.utils.Utilities;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CheckoutListViewAdapter extends BaseAdapter {

	String[] result;
	Context context;
	Activity ack;
	int[] imageId;
	private static LayoutInflater inflater = null;
	boolean isCheckout, isUnit = false;
	ArrayList<ProductDataBean> pdList;
	CartDataBean odbean;

	ImageLoader imgloader;
	int check;

	public CheckoutListViewAdapter(Context con,
			ArrayList<ProductDataBean> ordList, boolean isCheckout2, int check1) {
		context = con;
		ack = (Activity) con;
		pdList = ordList;
		isCheckout = isCheckout2;
		check = check1;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pdList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pdList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class Holder {
		TextView item_name, item_desc, mrp, you_save;
		Button okler_price;
		//image_cart;
		EditText units;
		ImageView deleteImage, image_favourite, red_plus_Image,
				red_minus_Image;
		NetworkImageView item_img;
		RelativeLayout red_plus_ImageRl, red_minus_ImageRl;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (check == 1) {
			odbean = Okler.getInstance().getSingleCart();
		} else {
			odbean = Okler.getInstance().getMainCart();
		}
		final Holder holder = new Holder();
		View rowView;

		rowView = inflater.inflate(R.layout.row_product_checkout_list, null);
		holder.item_img = (NetworkImageView) rowView
				.findViewById(R.id.item_image);
		holder.item_name = (TextView) rowView.findViewById(R.id.item_name);
		holder.item_desc = (TextView) rowView
				.findViewById(R.id.item_description);
		holder.mrp = (TextView) rowView.findViewById(R.id.tv_MrpValue);
		holder.you_save = (TextView) rowView.findViewById(R.id.tv_youSaveValue);
		holder.okler_price = (Button) rowView.findViewById(R.id.okler_Amount);
		holder.units = (EditText) rowView.findViewById(R.id.unitValue);
		holder.deleteImage = (ImageView) rowView.findViewById(R.id.deleteImage);
		holder.deleteImage.setVisibility(View.GONE);
		holder.image_favourite = (ImageView) rowView
				.findViewById(R.id.image_favourite);
		holder.image_favourite.setVisibility(View.GONE);
		/*holder.image_cart = (Button) rowView.findViewById(R.id.image_cart);
		holder.image_cart.setVisibility(View.GONE);*/
		holder.red_plus_Image = (ImageView) rowView
				.findViewById(R.id.red_plus_Image);
		holder.red_minus_Image = (ImageView) rowView
				.findViewById(R.id.red_minus_Image);
		holder.red_plus_ImageRl = (RelativeLayout) rowView
				.findViewById(R.id.red_minus_ImageRl);
		holder.red_minus_ImageRl = (RelativeLayout) rowView
				.findViewById(R.id.red_minus_ImageRl);
		holder.item_name.setText(pdList.get(position).getProdName());
		String company = pdList.get(position).getCompany();
		String str = "" + pdList.get(position).getMrp();
		SpannableString strikethroughMRP = new SpannableString(str);
		strikethroughMRP.setSpan(new StrikethroughSpan(), 0,
				strikethroughMRP.length(), 0);
		String First;
		if (Okler.getInstance().getBookingType() == 0) {
			First = pdList.get(position).getClipArtUrl();
		} else {
			First = pdList.get(position).getThumbUrl();
		}
		String second = pdList.get(position).getImgUrl();
		String photo_url = First + second;
		imgloader = VolleyRequest.getInstance(context).getImageLoader();
		holder.item_img.setImageUrl(photo_url, imgloader);
		if (Okler.getInstance().getBookingType() == 0) {
			if (company == null || company.equals("null") || company.equals("")) {
				company = "";
			}
			holder.item_desc.setText(company);
		} else {
			String desc = pdList.get(position).getDesc();
			if (desc == null || desc.equals("null"))
				desc="";
			holder.item_desc.setText(desc);
		}
		holder.mrp.setText(strikethroughMRP);
		holder.you_save.setText(pdList.get(position).getDiscount() + "%");
		holder.okler_price.setText(holder.okler_price.getText().toString()
				+ pdList.get(position).getOklerPrice());
		int unitvl = pdList.get(position).getUnits();
		if (unitvl > 9) {
			holder.units.setText("" + pdList.get(position).getUnits());
		} else {
			holder.units.setText("0" + pdList.get(position).getUnits());
		}
		holder.units.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String s2 = s.toString();
				if (s2.equals("") || s2 == "") {
					pdList.get(position).setUnits(0);
					isUnit = false;
				} else if (s2.length() > 2) {
					holder.units.setText("");
					holder.units.requestFocus();
					holder.units.setError("Maximum limit is 99");
				} else {
					int s3 = Integer.parseInt(s2.toString());
					if (s3 <= 0) {
						pdList.get(position).setUnits(0);
						isUnit = false;
					} else {
						isUnit = true;
					}
				}
			}
		});
		holder.red_plus_Image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (holder.units.getText().toString().trim().equals("")) {
					holder.units.setText("00");
				}
				String uni = holder.units.getText().toString();
				if (uni.length() > 2) {
					holder.units.setText("");
					Toast.makeText(ack, "Maximum limit is 99",
							Toast.LENGTH_SHORT).show();
				} else {
					int unit = Integer.parseInt(holder.units.getText()
							.toString());
					if (unit >= 99) {
						Toast.makeText(ack, "Maximum limit is 99",
								Toast.LENGTH_SHORT).show();
					} else {
						holder.units.setError(null);
						unit = unit + 1;
						if (unit > 9) {
							holder.units.setText("" + unit);
						} else {
							holder.units.setText("0" + unit);
						}
						pdList.get(position).setUnits(unit);
						odbean.setProdList(pdList);
						Okler.getInstance().setSingleCart(odbean);
						Utilities.writeCartToSharedPref(ack, odbean);
						ProductCheckoutListView.totalOrderValue();
					}
				}
			}
		});
		holder.red_plus_ImageRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (holder.units.getText().toString().trim().equals("")) {
					holder.units.setText("00");
				}
				String uni = holder.units.getText().toString();
				if (uni.length() > 2) {
					holder.units.setText("");
					Toast.makeText(ack, "Maximum limit is 99",
							Toast.LENGTH_SHORT).show();
				} else {
					int unit = Integer.parseInt(holder.units.getText()
							.toString());
					if (unit >= 99) {
						Toast.makeText(ack, "Maximum limit is 99",
								Toast.LENGTH_SHORT).show();
					} else {
						holder.units.setError(null);
						unit = unit + 1;
						if (unit > 9) {
							holder.units.setText("" + unit);
						} else {
							holder.units.setText("0" + unit);
						}
						pdList.get(position).setUnits(unit);
						odbean.setProdList(pdList);
						Okler.getInstance().setSingleCart(odbean);
						Utilities.writeCartToSharedPref(ack, odbean);
						ProductCheckoutListView.totalOrderValue();
					}
				}
			}
		});

		holder.red_minus_Image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (holder.units.getText().toString().trim().equals("")) {
					holder.units.setText("01");
				}
				String uni = holder.units.getText().toString();
				if (uni.length() > 2) {
					holder.units.setText("");
					Toast.makeText(ack, "Maximum limit is 99",
							Toast.LENGTH_SHORT).show();
				} else {
					int unit = Integer.parseInt(holder.units.getText()
							.toString());
					if (unit > 1) {
						holder.units.setError(null);
						unit = unit - 1;
					} else {
					}
					if (unit > 9) {
						holder.units.setText("" + unit);
					} else {
						holder.units.setText("0" + unit);
					}
					pdList.get(position).setUnits(unit);
					odbean.setProdList(pdList);
					Okler.getInstance().setSingleCart(odbean);
					Utilities.writeCartToSharedPref(ack, odbean);
					ProductCheckoutListView.totalOrderValue();
				}
			}
		});
		holder.red_minus_ImageRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (holder.units.getText().toString().trim().equals("")) {
					holder.units.setText("01");
				}
				String uni = holder.units.getText().toString();
				if (uni.length() > 2) {
					holder.units.setText("");
					Toast.makeText(ack, "Maximum limit is 99",
							Toast.LENGTH_SHORT).show();
				} else {
					int unit = Integer.parseInt(holder.units.getText()
							.toString());
					if (unit > 1) {
						holder.units.setError(null);
						unit = unit - 1;
					} else {

					}
					if (unit > 9) {
						holder.units.setText("" + unit);
					} else {
						holder.units.setText("0" + unit);
					}
					pdList.get(position).setUnits(unit);
					odbean.setProdList(pdList);
					Okler.getInstance().setSingleCart(odbean);
					Utilities.writeCartToSharedPref(ack, odbean);
					ProductCheckoutListView.totalOrderValue();
				}
			}
		});
		holder.deleteImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (check == 1) {
					pdList.remove(position);
					odbean.setProdList(pdList);
					Okler.getInstance().setSingleCart(odbean);
					Utilities.writeCartToSharedPref(ack, odbean);
					ProductCheckoutListView.setUi();
				} else {
					int pid = pdList.get(position).getProdId();
					deleteFromCart(pid, position);
				}
			}
		});

		if (!isCheckout) {

		}

		return rowView;
	}

	public void deleteFromCart(int prodId, final int position) {
		UsersDataBean ubean = Utilities.getCurrentUserFromSharedPref(ack);
		int userId = ubean.getId();
		String remove1, remove2, deleteFromCartUrl;
		remove1 = ack.getString(R.string.delete_cart_url);
		remove2 = ack.getString(R.string.product_id);
		deleteFromCartUrl = remove1 + userId + remove2 + prodId;
		WebJsonObjectRequest deljson = new WebJsonObjectRequest(Method.GET,
				deleteFromCartUrl, new JSONObject(),
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						JSONObject jobj = (JSONObject) response;
						try {
							String msg = jobj.getString("message");
							if (msg.equals("item in your cart are deleted successfully.")) {
								pdList.remove(position);
								odbean.setProdList(pdList);
								Okler.getInstance().setMainCart(odbean);
								ProductCheckoutListView.setUi();
							} else {
								Toast.makeText(
										ack,
										"Some Error Ocurred." + "\n"
												+ "Item not Deleted.",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
		VolleyRequest.addJsonObjectRequest(ack, deljson);
	}
}