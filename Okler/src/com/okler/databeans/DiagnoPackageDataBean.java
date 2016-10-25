package com.okler.databeans;

import java.util.ArrayList;

public class DiagnoPackageDataBean {
	private int totalPacakgeCount;
	private String package_name;
	private String description;
	private int pacakageId;
	private float pack_mrp;
	private float pack_oklerPrice;
	private int pack_you_save;
	private String mandatoryInfo;
	public String getMandatoryInfo() {
		return mandatoryInfo;
	}
	public void setMandatoryInfo(String mandatoryInfo) {
		this.mandatoryInfo = mandatoryInfo;
	}
	public float getPack_mrp() {
		return pack_mrp;
	}
	public float getPack_oklerPrice() {
		return pack_oklerPrice;
	}
	public int getPack_you_save() {
		return pack_you_save;
	}
	public void setPack_mrp(float pack_mrp) {
		this.pack_mrp = pack_mrp;
	}
	public void setPack_oklerPrice(float pack_oklerPrice) {
		this.pack_oklerPrice = pack_oklerPrice;
	}
	public void setPack_you_save(int pack_you_save) {
		this.pack_you_save = pack_you_save;
	}
	
	private ArrayList<TestDataBean> testArrayList; 
	
	public int getTotalPacakgeCount() {
		return totalPacakgeCount;
	}
	public void setTotalPacakgeCount(int totalPacakgeCount) {
		this.totalPacakgeCount = totalPacakgeCount;
	}
	public String getPackage_name() {
		return package_name;
	}
	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}
	public int getPacakageId() {
		return pacakageId;
	}
	public void setPacakageId(int pacakageId) {
		this.pacakageId = pacakageId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public ArrayList<TestDataBean> getTestArrayList() {
		return testArrayList;
	}
	public void setTestArrayList(ArrayList<TestDataBean> testArrayList) {
		this.testArrayList = testArrayList;
	}
}



