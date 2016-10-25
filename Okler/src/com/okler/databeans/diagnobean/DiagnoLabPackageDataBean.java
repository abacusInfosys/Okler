package com.okler.databeans.diagnobean;

import java.util.ArrayList;

public class DiagnoLabPackageDataBean {
	DiagnoLabBranchDataBean currentLab = new DiagnoLabBranchDataBean();
	ArrayList<DiagnoPackagesDataBean> currentPkgs = new ArrayList<DiagnoPackagesDataBean>();
	float mrp;
	float okler_price;
	int youSave;	
	int pkgCount;
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
	public ArrayList<DiagnoPackagesDataBean> getCurrentPkgs() {
		return currentPkgs;
	}
	public void setCurrentPkgs(ArrayList<DiagnoPackagesDataBean> currentPkgs) {
		this.currentPkgs = currentPkgs;
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
	public int getPkgCount() {
		return currentPkgs.size();
	}
	public void setPkgCount(int pkgCount) {
		this.pkgCount = pkgCount;
	}
	
}
