package com.okler.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.okler.databeans.GroupDataBean;
import com.okler.databeans.ProductDataBean;
import com.okleruser.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class GroupExpandableListAdapter extends BaseExpandableListAdapter {
	Context con;
	HashMap<GroupDataBean, List<ProductDataBean>> Glist;
	GroupDataBean gbean = new GroupDataBean();
	ArrayList<GroupDataBean> gdList;

	public GroupExpandableListAdapter(Context context,
			HashMap<GroupDataBean, List<ProductDataBean>> Glist2,
			ArrayList<GroupDataBean> gdList2) {
		// TODO Auto-generated constructor stub
		this.con = context;
		this.Glist = Glist2;
		this.gdList = gdList2;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return Glist.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return Glist.get(gdList.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return Glist.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub

		return Glist.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ExpandableListView elv = (ExpandableListView) parent;
		elv.expandGroup(groupPosition);
		elv.setGroupIndicator(null);
		elv.setChildIndicator(null);
		elv.setChildDivider(con.getResources().getDrawable(R.color.White));
		elv.setDivider(con.getResources().getDrawable(R.color.White));
		elv.setDividerHeight(2);
		if(gdList.size()>0){
		GroupDataBean gb = gdList.get(groupPosition);
		// (GroupDataBean)getGroup(groupPosition);

		String header = gb.getGroup_name();
		String idtag = gb.getGroup_id();
		// (String)getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.con
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_group_header, null);

		}
		TextView thead = (TextView) convertView.findViewById(R.id.groupHeader);
		thead.setText(header);
		thead.setTag(idtag);
		return convertView;
		}else{
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) this.con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_group_header, null);

			}
			
			return convertView;			
		}
		
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(gdList.size()>0){
		ProductDataBean pbean = Glist.get(gdList.get(groupPosition)).get(
				childPosition);
		String name = pbean.getProdName();
		int id = pbean.getProdId();

		// String text = (String)getChild(groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) con
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_group_item, null);
		}
		TextView listItem = (TextView) convertView.findViewById(R.id.listItem);
		listItem.setText(name);
		listItem.setTag("" + id);
		// TODO Auto-generated method stub
		return convertView;
		}else{
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_group_item, null);
			}
			
			return convertView;
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
