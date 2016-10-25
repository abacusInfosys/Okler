package com.okler.databeans;

public class DiseaseDataBean {

	int dieseaseId;
	String dieseaseName;
	int count;
	private Boolean ckIsChecked=false;
	public int getDiseaseId() {
		return dieseaseId;
	}
	public void setDiseaseId(int dieseaseId) {
		this.dieseaseId = dieseaseId;
	}
	public String getDiseaseName() {
		return dieseaseName;
	}
	public void setDiseaseName(String dieseaseName) {
		this.dieseaseName = dieseaseName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Boolean getCkIsChecked() {
		return ckIsChecked;
	}
	public void setCkIsChecked(Boolean ckIsChecked) {
		this.ckIsChecked = ckIsChecked;
	}
}
