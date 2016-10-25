package com.okler.databeans.diagnobean;

import java.util.ArrayList;

public class DiagnoPackagesDataBean {
	private String packageName,reportTime;
	private boolean fastingRequired;
	private int pacakageId;
	private float packMrp;
	private float packOklerPrice;
	private int packYouSave;
	private boolean ckIsChecked = false;
	private boolean home_pickup_applicable=false;
	private float youSaveRs;
	ArrayList<DiagnoTestDataBean> testList = new ArrayList<DiagnoTestDataBean>();
	
	
	
	public ArrayList<DiagnoTestDataBean> getTestList() {
		return testList;
	}
	public void setTestList(ArrayList<DiagnoTestDataBean> testList) {
		this.testList = testList;
	}
	public float getYouSaveRs() {
		return youSaveRs;
	}
	public void setYouSaveRs(float youSaveRs) {
		this.youSaveRs = youSaveRs;
	}
	
	
	public boolean isHome_pickup_applicable() {
		return home_pickup_applicable;
	}
	public void setHome_pickup_applicable(boolean home_pickup_applicable) {
		this.home_pickup_applicable = home_pickup_applicable;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public boolean isFastingRequired() {
		return fastingRequired;
	}
	public void setFastingRequired(boolean fastingRequired) {
		this.fastingRequired = fastingRequired;
	}
	public int getPacakageId() {
		return pacakageId;
	}
	public void setPacakageId(int pacakageId) {
		this.pacakageId = pacakageId;
	}
	public float getPackMrp() {
		return packMrp;
	}
	public void setPackMrp(float packMrp) {
		this.packMrp = packMrp;
	}
	public float getPackOklerPrice() {
		return packOklerPrice;
	}
	public void setPackOklerPrice(float packOklerPrice) {
		this.packOklerPrice = packOklerPrice;
	}
	public int getPackYouSave() {
		return packYouSave;
	}
	public void setPackYouSave(int packYouSave) {
		this.packYouSave = packYouSave;
	}
	
	public Boolean getCkIsChecked() {
		return ckIsChecked;
	}
	public void setCkIsChecked(Boolean ckIsChecked) {
		this.ckIsChecked = ckIsChecked;
	}
}
