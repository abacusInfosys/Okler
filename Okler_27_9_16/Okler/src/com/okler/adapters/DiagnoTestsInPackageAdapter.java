package com.okler.adapters;

import java.util.ArrayList;

import com.okler.databeans.diagnobean.DiagnoTestDataBean;
import com.okleruser.R;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DiagnoTestsInPackageAdapter extends RecyclerView.Adapter<DiagnoTestsInPackageAdapter.ViewHolder> {
    private ArrayList<DiagnoTestDataBean> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView testNameTxt;
            

            public ViewHolder(View v) {
                    super(v);
                    testNameTxt = (TextView) v.findViewById(R.id.testNameInPkg);
                   
            }
    }
    
    public DiagnoTestsInPackageAdapter(ArrayList<DiagnoTestDataBean> myDataset) {
        mDataset = myDataset;
}

    public DiagnoTestsInPackageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
    // create a new view
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tests_in_packages, parent, false);
    // set the view's size, margins, paddings and layout parameters
    ViewHolder vh = new ViewHolder(v);
    return vh;
}
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final String name = mDataset.get(position).getTestname();
           
            holder.testNameTxt.setText(name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
            return mDataset.size();
    }
}
