package com.okler.databeans;

import java.util.ArrayList;

public class CartDataBean {
	UsersDataBean ubean,curUBean;
	float totalPrice;
	ArrayList<ProductDataBean> ProdList = new ArrayList<ProductDataBean>();
	String presc_id;
	float coupon_disc;
	String cCode;
	String ship_charge; 
	
	
	
	public String getShip_charge() {
		return ship_charge;
	}

	public void setShip_charge(String ship_charge) {
		this.ship_charge = ship_charge;
	}

	public String getcCode() {
		return cCode;
	}

	public void setcCode(String cCode) {
		this.cCode = cCode;
	}

	public float getCoupon_disc() {
		return coupon_disc;
	}

	public void setCoupon_disc(float coupon_disc) {
		this.coupon_disc = coupon_disc;
	}

	public String getPresc_id() {
		return presc_id;
	}

	public void setPresc_id(String presc_id) {
		this.presc_id = presc_id;
	}

	public UsersDataBean getCurUBean() {
		return curUBean;
	}

	public void setCurUBean(UsersDataBean curUBean) {
		this.curUBean = curUBean;
	}
	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public UsersDataBean getUbean() {
		return ubean;
	}

	public void setUbean(UsersDataBean ubean) {
		this.ubean = ubean;
	}

	

	public ArrayList<ProductDataBean> getProdList() {
		return ProdList;
	}

	public void setProdList(ArrayList<ProductDataBean> prodList) {
		ProdList = prodList;
	}
	
}
