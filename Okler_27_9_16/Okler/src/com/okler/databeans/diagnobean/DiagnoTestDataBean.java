package com.okler.databeans.diagnobean;

public class DiagnoTestDataBean {

	private int testId;
	private String testname;
	private float marketPrice;
	private float oklerTestPrice;
	private String reportTime;
	private int youSave;
	private boolean ckIsChecked = false;
	private boolean fastingRequired = false; 
	private boolean home_pickup_applicable=false;
	
	public boolean isHome_pickup_applicable() {
		return home_pickup_applicable;
	}
	public void setHome_pickup_applicable(boolean home_pickup_applicable) {
		this.home_pickup_applicable = home_pickup_applicable;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public int getTestId() {
		return testId;
	}
	public void setTestId(int testId) {
		this.testId = testId;
	}
	public String getTestname() {
		return testname;
	}
	public void setTestname(String testname) {
		this.testname = testname;
	}
	public float getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(float marketPrice) {
		this.marketPrice = marketPrice;
	}
	public float getOklerTestPrice() {
		return oklerTestPrice;
	}
	public void setOklerTestPrice(float oklerTestPrice) {
		this.oklerTestPrice = oklerTestPrice;
	}
	public float getYouSave() {
		return youSave;
	}
	public void setYouSave(int youSave) {
		this.youSave = youSave;
	}
	public Boolean getCkIsChecked() {
		return ckIsChecked;
	}
	public void setCkIsChecked(Boolean ckIsChecked) {
		this.ckIsChecked = ckIsChecked;
	}
	public boolean isFastingRequired() {
		return fastingRequired;
	}
	public void setFastingRequired(boolean fastingRequired) {
		this.fastingRequired = fastingRequired;
	}
	
}
