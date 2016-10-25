package com.okler.databeans;

import java.util.ArrayList;



public class DiagnoLabsDataBean {
 private int labId;
 private String lab_name;
 private String labDescrption;
 private String lab_Log_image;
 private String lab_image_path;
 private String lab_award_image;
 private String[] lab_gallery_image;
 private ArrayList<TestDataBean> testBean=new ArrayList<TestDataBean>();

 DiagnoPackageDataBean packageBean;//when it is search lab by package..
 private String lab_address,lab_city,lab_state,lab_pinCode;
 private ArrayList<TestDataBean> selectedTest;
 private ArrayList<DiagnoPackageDataBean> selectedPckg; 
  public DiagnoPackageDataBean getPackageBean() {
  	return packageBean;
  }
  public void setPackageBean(DiagnoPackageDataBean packageBean) {
	this.packageBean = packageBean;
}
 private String lab_city_branch_id;
	 private String lab_city_id,lab_state_id;
 
	 private String lab_Log_url;
	 private boolean isSelected;
	 
 
 
	 public String getLab_city_id() {
		return lab_city_id;
	}
	public void setLab_city_id(String lab_city_id) {
		this.lab_city_id = lab_city_id;
	}
	public String getLab_state_id() {
		return lab_state_id;
	}
	public void setLab_state_id(String lab_state_id) {
		this.lab_state_id = lab_state_id;
	}
	public String getLab_city_branch_id() {
	return lab_city_branch_id;
}
public void setLab_city_branch_id(String lab_city_branch_id) {
	this.lab_city_branch_id = lab_city_branch_id;
}
 
 public ArrayList<TestDataBean> getTestBean() {
	return testBean;
}
 public void setTestBean(ArrayList<TestDataBean> testBean) {
	this.testBean = testBean;
}
 public ArrayList<DiagnoPackageDataBean> getSelectedPckg() {
	return selectedPckg;
}
 public void setSelectedPckg(ArrayList<DiagnoPackageDataBean> selectedPckg) {
	this.selectedPckg = selectedPckg;
}
 public ArrayList<TestDataBean> getSelectedTest() {
	return selectedTest;
}
 public void setSelectedTest(ArrayList<TestDataBean> selectedTest) {
	this.selectedTest = selectedTest;
}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getLab_Log_url() {
		return lab_Log_url;
	}
	public void setLab_Log_url(String lab_Log_url) {
		this.lab_Log_url = lab_Log_url;
	}
	public String getLab_city() {
		return lab_city;
	}
	public void setLab_city(String lab_city) {
		this.lab_city = lab_city;
	}
	public String getLab_state() {
		return lab_state;
	}
	public void setLab_state(String lab_state) {
		this.lab_state = lab_state;
	}
	public String getLab_pinCode() {
		return lab_pinCode;
	}
	public void setLab_pinCode(String lab_pinCode) {
		this.lab_pinCode = lab_pinCode;
	}
	public String getLab_address() {
		return lab_address;
	}
	public void setLab_address(String lab_address) {
		this.lab_address = lab_address;
	}
 

 public int getLabId() {
	return labId;
}
 public void setLabId(int labId) {
	this.labId = labId;
}
 
 public String getLab_name() {
	return lab_name;
}
 public void setLab_name(String lab_name) {
	this.lab_name = lab_name;
}
 
 public String getLabDescrption() {
	return labDescrption;
}
 public void setLabDescrption(String labDescrption) {
	this.labDescrption = labDescrption;
}
 
public String getLab_Log_image() {
	return lab_Log_image;
}
 public void setLab_Log_image(String lab_Log_image) {
	this.lab_Log_image = lab_Log_image;
}
 
 public String getLab_image_path() {
	return lab_image_path;
}
 public void setLab_image_path(String lab_image_path) {
	this.lab_image_path = lab_image_path;
}
 
 public String getLab_award_image() {
	return lab_award_image;
}
 public void setLab_award_image(String lab_award_image) {
	this.lab_award_image = lab_award_image;
}
 
 public String[] getLab_gallery_image() {
	return lab_gallery_image;
}
 public void setLab_gallery_image(String[] lab_gallery_image) {
	this.lab_gallery_image = lab_gallery_image;
}
 
}
 