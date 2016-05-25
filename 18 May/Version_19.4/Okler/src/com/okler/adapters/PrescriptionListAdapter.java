package com.okler.adapters;


import java.lang.reflect.Array;
import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.okler.adapters.DiagnoTestAdapter.viewHolder;
import com.okler.android.PrescriptionList;
import com.okleruser.R;
import com.okler.databeans.PrescriptionImagesDataBean;
import com.okler.databeans.PrescriptionsDataBean;
import com.okler.dialogs.LargePresImageDialog;
import com.okler.network.VolleyRequest;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PrescriptionListAdapter extends BaseAdapter {
	
	private static LayoutInflater inflater=null;
	Activity context;
	ArrayList<PrescriptionsDataBean> prescHistory;
//	String photoURl ="http://183.82.110.105:8081/oklerdevv2/uploads/prescription/";
	ImageLoader imgLoader;
	public PrescriptionListAdapter(PrescriptionList prescriptionList,ArrayList<PrescriptionsDataBean> prescHistory)
	{
		this.prescHistory = prescHistory;
		context = prescriptionList;
		inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imgLoader = VolleyRequest.getInstance(context).getImageLoader();
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return prescHistory.size();
	}

	@Override
	public Object getItem(int positopn) {
		// TODO Auto-generated method stub
		return positopn;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public class Holder
    {
        TextView pName,pSName,dSName,prescIDVal,status,statusVal,dName,prescID;
        NetworkImageView img1,img2,img3;//,singleImg;
       
    }
	

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        View rowView;
        if(convertView==null){
			holder=new Holder();
			convertView = inflater.inflate(R.layout.row_prescriptions_list, null);
			convertView.setTag(holder);	
        }else{
			holder=(Holder) convertView.getTag();
		}	
                    
             holder.img1=(NetworkImageView) convertView.findViewById(R.id.img1);
             holder.img1.setTag(position);
             holder.img2=(NetworkImageView) convertView.findViewById(R.id.img2);
             holder.img3=(NetworkImageView) convertView.findViewById(R.id.img3);
        //     holder.singleImg=(NetworkImageView) convertView.findViewById(R.id.singleImg);
         
             PrescriptionsDataBean curPresHistoryObj = prescHistory.get(position);
             ArrayList<PrescriptionImagesDataBean> imagesInPresc = curPresHistoryObj.getPresImages();
             String fName = imagesInPresc.get(0).getPresUrl();
        	 holder.img1.setImageUrl(fName, imgLoader);
        	 holder.img2.setImageUrl(fName, imgLoader);
        	 holder.img3.setImageUrl(fName, imgLoader);  
            int len = imagesInPresc.size();
            if(len == 2 )
            {            	
            		fName = imagesInPresc.get(1).getPresUrl();
            		holder.img2.setImageUrl(fName, imgLoader);
               	 	holder.img1.setImageUrl(fName, imgLoader);
            //   	 holder.singleImg.setVisibility(View.INVISIBLE);
            }
            else if(len ==3)
            {
            	fName = imagesInPresc.get(1).getPresUrl();
        		holder.img2.setImageUrl(fName, imgLoader);
        		fName = imagesInPresc.get(2).getPresUrl();
           	 	holder.img1.setImageUrl(fName, imgLoader);  	
      //     	 holder.singleImg.setVisibility(View.INVISIBLE);
            }
            holder.dName = (TextView)convertView.findViewById(R.id.dName);
            holder.pName = (TextView)convertView.findViewById(R.id.pName);
            holder.pSName = (TextView)convertView.findViewById(R.id.pSName);
            holder.dSName = (TextView)convertView.findViewById(R.id.dSName);
            holder.prescID = (TextView)convertView.findViewById(R.id.prescIDVal);
            holder.statusVal = (TextView)convertView.findViewById(R.id.statusVal);
            
            
            String doc = prescHistory.get(position).getDocName();            
            if(prescHistory.get(position).getDocSirname()!= null)
            	doc+=" "+prescHistory.get(position).getDocSirname();
            
            String patient = prescHistory.get(position).getPatientName();
          /*  if(prescHistory.get(position).getPatientSirName()!= null)
            	patient+=" "+ prescHistory.get(position).getPatientSirName();*/
            String presID = prescHistory.get(position).getPresId();
            String statusV = prescHistory.get(position).getPrescStatus();
            if(patient == null|| patient.equals("null") || patient.equals(""))
            {
            	holder.pName.setText("");
            }
            else
            {
            	holder.pName.setText(patient);
            }
            if(doc == null || doc.equals("null ") || doc.equals(""))
            {
            	holder.dName.setText("");
            }
            else
            {
            	holder.dName.setText(doc);
            }
            
            //holder.dName.setText(doc);
            holder.dSName.setText("");
            //holder.pName.setText(patient);
            holder.pSName.setText("");
            holder.prescID.setText(presID);
            holder.statusVal.setText(statusV);
          
      
             convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	
            	ImageView temp =(ImageView) v.findViewById(R.id.img1);
            	 int position = Integer.parseInt(temp.getTag().toString());
            	 LargePresImageDialog largePres = new LargePresImageDialog(context,position,prescHistory);
             	largePres.show();
            }
        });

        return convertView;
	}
}
