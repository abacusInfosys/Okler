package com.okler.databeans;


public class DiagnoOrderDataBean {
	DiagnoLabsDataBean selectedLab = new DiagnoLabsDataBean();
	UsersDataBean userBean=new UsersDataBean();
	String ap_date,ap_time,ap_at,orderId,status,pickupType;
	public String getPickupType() {
		return pickupType;
	}
	public void setPickupType(String pickupType) {
		this.pickupType = pickupType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	int slot_id;
	String amount,oklerDiscount,couponDiscount,tax,netPayable;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getOklerDiscount() {
		return oklerDiscount;
	}
	public void setOklerDiscount(String oklerDiscount) {
		this.oklerDiscount = oklerDiscount;
	}
	public String getCouponDiscount() {
		return couponDiscount;
	}
	public void setCouponDiscount(String couponDiscount) {
		this.couponDiscount = couponDiscount;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getNetPayable() {
		return netPayable;
	}
	public void setNetPayable(String netPayable) {
		this.netPayable = netPayable;
	}
	public int getSlot_id() {
		return slot_id;
	}
	public void setSlot_id(int slot_id) {
		this.slot_id = slot_id; 
	}
	public String getAp_date() {
		return ap_date;
	}
	public void setAp_date(String ap_date) {
		this.ap_date = ap_date;
	}
	public String getAp_time() {
		return ap_time;
	}
	public void setAp_time(String ap_time) {
		this.ap_time = ap_time;
	}
	public String getAp_at() {
		return ap_at;
	}
	public void setAp_at(String ap_at) {
		this.ap_at = ap_at;
	}
	public DiagnoLabsDataBean getSelectedLab() {
		return selectedLab;
	}
	public void setSelectedLab(DiagnoLabsDataBean selectedLab) {
		this.selectedLab = selectedLab;
	}
	public UsersDataBean getUserBean() {
		return userBean;
	}
	public void setUserBean(UsersDataBean userBean) {
		this.userBean = userBean;
	}
	
}
