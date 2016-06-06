package com.okler.adapters;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.okler.adapters.PrescriptionListAdapter.Holder;
import com.okler.android.PrescriptionList;
import com.okleruser.R;
import com.okler.databeans.PrescriptionImagesDataBean;
import com.okler.databeans.PrescriptionsDataBean;
import com.okler.dialogs.ConfirmPrescDialog;
import com.okler.network.VolleyRequest;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MedPrescListAdapter extends BaseAdapter{
	private static LayoutInflater inflater=null;
	Activity context;
	ArrayList<PrescriptionsDataBean> medPrescHistory;
	ImageLoader imgLoader;
	String presId = "";
	public MedPrescListAdapter(PrescriptionList prescriptionList,ArrayList<PrescriptionsDataBean> prescHistory)
	{
		this.medPrescHistory = prescHistory;
		context = prescriptionList;
		inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imgLoader = VolleyRequest.getInstance(context).getImageLoader();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return medPrescHistory.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return medPrescHistory.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public class Holder{
		ImageView select;
		NetworkImageView img;
		TextView name,docName,prescId,statusTv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Holder holder = new Holder();
        View rowView;
        
        if(convertView==null){
			holder=new Holder();
			convertView = inflater.inflate(R.layout.custom_view_med_presc, null);
			convertView.setTag(holder);	
        }else{
        	
        	
        }
        holder.img = (NetworkImageView)convertView.findViewById(R.id.img1);
        holder.select = (ImageView)convertView.findViewById(R.id.select);
        holder.name = (TextView)convertView.findViewById(R.id.nameTv);
        holder.docName = (TextView)convertView.findViewById(R.id.docNameTv);
        holder.prescId = (TextView)convertView.findViewById(R.id.prescValue);
        holder.statusTv = (TextView)convertView.findViewById(R.id.statusTv);
        PrescriptionsDataBean prsbean = medPrescHistory.get(position);
        ArrayList<PrescriptionImagesDataBean> imagesInPresc = prsbean.getPresImages();
        String fName = imagesInPresc.get(0).getPresUrl();
        holder.img.setImageUrl(fName, imgLoader);
        String name = medPrescHistory.get(position).getPatientName()/*+" "+medPrescHistory.get(position).getPatientSirName()*/;
        if(name == null|| name == "null" || name == "")
        {
        	holder.name.setText("");
        }
        else
        {
        	holder.name.setText(name);
        }
        String docName= medPrescHistory.get(position).getDocName()/*+" "+medPrescHistory.get(position).getDocSirname()*/;
        if(docName == null || docName == "null" || docName == "" )
        {
        	holder.docName.setText("");
        }
        else
        {
        	holder.docName.setText(docName);
        }
        holder.prescId.setText(medPrescHistory.get(position).getPresId());
        presId = holder.prescId.getText().toString();
        
        holder.statusTv.setText(medPrescHistory.get(position).getPrescStatus());
     
		return convertView;
	}
}
