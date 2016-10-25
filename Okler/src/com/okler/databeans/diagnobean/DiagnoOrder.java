package com.okler.databeans.diagnobean;

import com.okler.databeans.AddressDataBean;
import com.okler.enums.DiagnoOrderType;

public class DiagnoOrder {
	float mrp;
	float oklerPrice;
	float youSaveRs;
	int youSave;
	float couponCode;
	float couponDiscountAmt;
	String apptDt;
	int diseaseId;
	int appTimeSlotId;
	String appTime;
	String status;
	String orderId;
	String pickupType;
	AddressDataBean patientAddr;
	float tax;
	float netPayable;
	DiagnoLabTestDataBean labTestDataBean = new DiagnoLabTestDataBean();
	DiagnoLabPackageDataBean labPkgDataBean = new DiagnoLabPackageDataBean();
    DiagnoOrderType orderType;
    
    
    
	public int getDiseaseId() {
		return diseaseId;
	}
	public void setDiseaseId(int diseaseId) {
		this.diseaseId = diseaseId;
	}
	public float getYouSaveRs() {
		return youSaveRs;
	}
	public void setYouSaveRs(float youSaveRs) {
		this.youSaveRs = youSaveRs;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPickupType() {
		return pickupType;
	}
	public void setPickupType(String pickupType) {
		this.pickupType = pickupType;
	}
	public String getAppTime() {
		return appTime;
	}
	public void setAppTime(String appTime) {
		this.appTime = appTime;
	}
	public float getMrp() {
		return mrp;
	}
	public void setMrp(float mrp) {
		this.mrp = mrp;
	}
	public float getOklerPrice() {
		return oklerPrice;
	}
	public void setOklerPrice(float oklerPrice) {
		this.oklerPrice = oklerPrice;
	}
	public int getYouSave() {
		return youSave;
	}
	public void setYouSave(int youSave) {
		this.youSave = youSave;
	}
	public float getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(float couponCode) {
		this.couponCode = couponCode;
	}
	public float getCouponDiscountAmt() {
		return couponDiscountAmt;
	}
	public void setCouponDiscountAmt(float couponDiscountAmt) {
		this.couponDiscountAmt = couponDiscountAmt;
	}
	public String getApptDt() {
		return apptDt;
	}
	public void setApptDt(String apptDt) {
		this.apptDt = apptDt;
	}
	public int getAppTimeSlotId() {
		return appTimeSlotId;
	}
	public void setAppTimeSlotId(int appTimeSlotId) {
		this.appTimeSlotId = appTimeSlotId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public AddressDataBean getPatientAddr() {
		return patientAddr;
	}
	public void setPatientAddr(AddressDataBean patientAddr) {
		this.patientAddr = patientAddr;
	}
	public float getTax() {
		return tax;
	}
	public void setTax(float tax) {
		this.tax = tax;
	}
	public float getNetPayable() {
		return netPayable;
	}
	public void setNetPayable(float netPayable) {
		this.netPayable = netPayable;
	}
	public DiagnoLabTestDataBean getLabTestDataBean() {
		return labTestDataBean;
	}
	public void setLabTestDataBean(DiagnoLabTestDataBean labTestDataBean) {
		this.labTestDataBean = labTestDataBean;
	}
	public DiagnoLabPackageDataBean getLabPkgDataBean() {
		return labPkgDataBean;
	}
	public void setLabPkgDataBean(DiagnoLabPackageDataBean labPkgDataBean) {
		this.labPkgDataBean = labPkgDataBean;
	}
	public DiagnoOrderType getOrderType() {
		return orderType;
	}
	public void setOrderType(DiagnoOrderType orderType) {
		this.orderType = orderType;
	}
}
