package com.okler.databeans;

import java.util.ArrayList;

public class OrdersDataBean {
 String  orderId,price,orderStatus,date,shipping_charges;
 
 ArrayList<AddressDataBean> addbean = new ArrayList<AddressDataBean>();
 AddressDataBean addressDataBean = new AddressDataBean();
 /*public String getProduct_id() {
	return product_id;
}

public void setProduct_id(String product_id) {
	this.product_id = product_id;
}*/

ArrayList<ProductDataBean> prod_list = new ArrayList<ProductDataBean>();
		


		public AddressDataBean getAddressDataBean() {
	return addressDataBean;
}

public void setAddressDataBean(AddressDataBean addressDataBean) {
	this.addressDataBean = addressDataBean;
}

		public String getShipping_charges() {
	return shipping_charges;
}

public void setShipping_charges(String shipping_charges) {
	this.shipping_charges = shipping_charges;
}

		public ArrayList<AddressDataBean> getAddbean() {
	return addbean;
}

public void setAddbean(ArrayList<AddressDataBean> addbean) {
	this.addbean = addbean;
}

		public ArrayList<ProductDataBean> getProd_list() 
		{
			return prod_list;
		}

		public void setProd_list(ArrayList<ProductDataBean> prod_list) 
		{
			this.prod_list = prod_list;
		}

		public String getOrderId() {
			return orderId;
		}
		
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		
		public String getPrice() {
			return price;
		}
		
		public void setPrice(String price) {
			this.price = price;
		}
		
		public String getOrderStatus() {
			return orderStatus;
		}
		
		public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}
		
		public String getDate() {
			return date;
		}
		
		public void setDate(String date) {
			this.date = date;
		}
		 
}
