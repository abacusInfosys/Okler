package com.okler.databeans;

import java.util.ArrayList;

public class GroupDataBean {

	String group_id;
	String group_code;
	String group_name;
	ArrayList<ProductDataBean> prodList = new ArrayList<ProductDataBean>();
	
	
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getGroup_code() {
		return group_code;
	}
	public void setGroup_code(String group_code) {
		this.group_code = group_code;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public ArrayList<ProductDataBean> getProdList() {
		return prodList;
	}
	public void setProdList(ArrayList<ProductDataBean> prodList) {
		this.prodList = prodList;
	}
	
	
}
