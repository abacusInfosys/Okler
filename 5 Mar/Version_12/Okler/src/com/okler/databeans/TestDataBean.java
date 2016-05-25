package com.okler.databeans;



public class TestDataBean {

	private int testId;
	private int testcount;
	private String testname;
	private float marketPrice;
	private float oklerTestPrice;
	private float youSave;
	private String description;
	private String mandatoryInfo;
	private Boolean ckIsChecked=false;
	 private DiagnoPackageDataBean pckBean=new DiagnoPackageDataBean();;
	 
	private float tax;
	
	
	public float getTax() {
		return tax;
	}
	public void setTax(float tax) {
		this.tax = tax;
	}
	public float getOklerTestPrice() {
		return oklerTestPrice;
	}
	public void setOklerTestPrice(float oklerTestPrice) {
		this.oklerTestPrice = oklerTestPrice;
	}
	 public String getMandatoryInfo() {
		return mandatoryInfo;
	}
	 public void setMandatoryInfo(String mandatoryInfo) {
		this.mandatoryInfo = mandatoryInfo;
	}
	 public String getDescription() {
		return description;
	}
	 public void setDescription(String description) {
		this.description = description;
	}
	 public DiagnoPackageDataBean getPckBean() {
		return pckBean;
	}
	 public void setPckBean(DiagnoPackageDataBean pckBean) {
		this.pckBean = pckBean;
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
	
	public float getYouSave() {
		return youSave;
	}
	public void setYouSave(float youSave) {
		this.youSave = youSave;
	}
	public Boolean getCkIsChecked() {
		return ckIsChecked;
	}
	public void setCkIsChecked(Boolean ckIsChecked) {
		this.ckIsChecked = ckIsChecked;
	}
	public int getTestcount() {
		return testcount;
	}
	public void setTestcount(int testcount) {
		this.testcount = testcount;
	}
}
