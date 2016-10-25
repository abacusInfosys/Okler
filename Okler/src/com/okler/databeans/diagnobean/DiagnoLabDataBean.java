package com.okler.databeans.diagnobean;

public class DiagnoLabDataBean {
	 private int labId;
	 private String lab_name;
	 private String lab_logo_image_url;
	 private String lab_logo_image_path;
	 private String lab_award;
	 private boolean is_recommended_package_available=false;
	 
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
	public String getLab_logo_image_url() {
		return lab_logo_image_url;
	}
	public void setLab_logo_image_url(String lab_logo_image_url) {
		this.lab_logo_image_url = lab_logo_image_url;
	}
	public String getLab_logo_image_path() {
		return lab_logo_image_path;
	}
	public void setLab_logo_image_path(String lab_logo_image_path) {
		this.lab_logo_image_path = lab_logo_image_path;
	}
	public String getLab_award() {
		return lab_award;
	}
	public void setLab_award(String lab_award) {
		this.lab_award = lab_award;
	}
	public boolean is_recommended_package_available() {
		return is_recommended_package_available;
	}
	public void set_recommended_package_available(boolean is_recommended_package_available) {
		this.is_recommended_package_available = is_recommended_package_available;
	}
	 
}
