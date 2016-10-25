package com.okler.databeans.diagnobean;

import java.util.ArrayList;

public class DiagnoLabTestDataBean {
	DiagnoLabBranchDataBean currentLab = new DiagnoLabBranchDataBean();
	ArrayList<DiagnoTestDataBean> currentTests = new ArrayList<DiagnoTestDataBean>();
	float mrp;
	float okler_price;
	int youSave;
	ArrayList<DiagnoPackagesDataBean> recommendedPkgs;
	int testCount;
	private float youSaveRs;
	
	
	public float getYouSaveRs() {
		return youSaveRs;
	}
	public void setYouSaveRs(float youSaveRs) {
		this.youSaveRs = youSaveRs;
	}
	public DiagnoLabBranchDataBean getCurrentLab() {
		return currentLab;
	}
	public void setCurrentLab(DiagnoLabBranchDataBean currentLab) {
		this.currentLab = currentLab;
	}
	public ArrayList<DiagnoTestDataBean> getCurrentTests() {
		return currentTests;
	}
	public void setCurrentTests(ArrayList<DiagnoTestDataBean> currentTests) {
		this.currentTests = currentTests;
	}
	public float getMrp() {
		return mrp;
	}
	public void setMrp(float mrp) {
		this.mrp = mrp;
	}
	public float getOkler_price() {
		return okler_price;
	}
	public void setOkler_price(float okler_price) {
		this.okler_price = okler_price;
	}
	public int getYouSave() {
		return youSave;
	}
	public void setYouSave(int youSave) {
		this.youSave = youSave;
	}
	public ArrayList<DiagnoPackagesDataBean> getCurrentPkgs() {
		return recommendedPkgs;
	}
	public void setCurrentPkgs(ArrayList<DiagnoPackagesDataBean> recommendedPkgs) {
		this.recommendedPkgs = recommendedPkgs;
	}
	public int getTestCount() {
		return currentTests.size();
	}
	public void setTestCount(int testCount) {
		this.testCount = testCount;
	}
}
