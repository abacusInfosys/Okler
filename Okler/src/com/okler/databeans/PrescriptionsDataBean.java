package com.okler.databeans;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class PrescriptionsDataBean {
	String patientName,patientSirName,docName,docSirname,mobileNumber,presType,presId,prescStatus;
	ArrayList<PrescriptionImagesDataBean> presImages;
	public String getPrescStatus() {
		return prescStatus;
	}
	public void setPrescStatus(String prescStatus) {
		this.prescStatus = prescStatus;
	}
	public String getPresId() {
		return presId;
	}
	public void setPresId(String presId) {
		this.presId = presId;
	}
	
	public String getPresType() {
		return presType;
	}
	public void setPresType(String presType) {
		this.presType = presType;
	}
	
	public PrescriptionsDataBean() {
		// TODO Auto-generated constructor stub
		presImages = new ArrayList<PrescriptionImagesDataBean>();
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPatientSirName() {
		return patientSirName;
	}
	public void setPatientSirName(String patientSirName) {
		this.patientSirName = patientSirName;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getDocSirname() {
		return docSirname;
	}
	public void setDocSirname(String docSirname) {
		this.docSirname = docSirname;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public ArrayList<PrescriptionImagesDataBean> getPresImages() {
		return presImages;
	}
	public void setPresImages(ArrayList<PrescriptionImagesDataBean> presImages) {
		this.presImages = presImages;
	}
}
