package com.okler.databeans;

public class AddressDataBean {

	String firstname;
	String lastname;
	String address1;
	String address2;
	String city;
	String zip;
	String country;
	String state;
	String dob;
	String country_id;
	String zone_id;
	String gender;
	String preferred_del_time;
	String landmark;
	int pat_id;
	int default_billing;
	int default_shipping;
	String city_id;
	boolean isSelected;
	String phone;
	String relation;
	String relationId;
	String genderId, ship_add1, ship_add2, ship_city, ship_state, ship_zip,
			bill_add1, bill_add2, bill_city, bill_state, bill_zip;
	String ship_fname, ship_lname, bill_fname, bill_lname, shipSalut = "",
			billSalut = "";
	
	public String getShipSalut() {
		return shipSalut;
	}

	public void setShipSalut(String shipSalut) {
		this.shipSalut = shipSalut;
	}

	public String getBillSalut() {
		return billSalut;
	}

	public void setBillSalut(String billSalut) {
		this.billSalut = billSalut;
	}

	public int getDefault_shipping() {
		return default_shipping;
	}

	public void setDefault_shipping(int default_shipping) {
		this.default_shipping = default_shipping;
	}

	public String getShip_fname() {
		return ship_fname;
	}

	public void setShip_fname(String ship_fname) {
		this.ship_fname = ship_fname;
	}

	public String getShip_lname() {
		return ship_lname;
	}

	public void setShip_lname(String ship_lname) {
		this.ship_lname = ship_lname;
	}

	public String getBill_fname() {
		return bill_fname;
	}

	public void setBill_fname(String bill_fname) {
		this.bill_fname = bill_fname;
	}

	public String getBill_lname() {
		return bill_lname;
	}

	public void setBill_lname(String bill_lname) {
		this.bill_lname = bill_lname;
	}

	public String getShip_add1() {
		return ship_add1;
	}

	public void setShip_add1(String ship_add1) {
		this.ship_add1 = ship_add1;
	}

	public String getShip_add2() {
		return ship_add2;
	}

	public void setShip_add2(String ship_add2) {
		this.ship_add2 = ship_add2;
	}

	public String getShip_city() {
		return ship_city;
	}

	public void setShip_city(String ship_city) {
		this.ship_city = ship_city;
	}

	public String getShip_state() {
		return ship_state;
	}

	public void setShip_state(String ship_state) {
		this.ship_state = ship_state;
	}

	public String getShip_zip() {
		return ship_zip;
	}

	public void setShip_zip(String ship_zip) {
		this.ship_zip = ship_zip;
	}

	public String getBill_add1() {
		return bill_add1;
	}

	public void setBill_add1(String bill_add1) {
		this.bill_add1 = bill_add1;
	}

	public String getBill_add2() {
		return bill_add2;
	}

	public void setBill_add2(String bill_add2) {
		this.bill_add2 = bill_add2;
	}

	public String getBill_city() {
		return bill_city;
	}

	public void setBill_city(String bill_city) {
		this.bill_city = bill_city;
	}

	public String getBill_state() {
		return bill_state;
	}

	public void setBill_state(String bill_state) {
		this.bill_state = bill_state;
	}

	public String getBill_zip() {
		return bill_zip;
	}

	public void setBill_zip(String bill_zip) {
		this.bill_zip = bill_zip;
	}

	public String getGenderId() {
		return genderId;
	}

	public void setGenderId(String genderId) {
		this.genderId = genderId;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(String addr_id) {
		this.addr_id = addr_id;
	}

	String addr_id;

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getPreferred_del_time() {
		return preferred_del_time;
	}

	public void setPreferred_del_time(String preferred_del_time) {
		this.preferred_del_time = preferred_del_time;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getCountry_id() {
		return country_id;
	}

	public void setCountry_id(String country_id) {
		this.country_id = country_id;
	}

	public String getZone_id() {
		return zone_id;
	}

	public void setZone_id(String zone_id) {
		this.zone_id = zone_id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	String state_id;

	public String getState_id() {
		return state_id;
	}

	public void setState_id(String state_id) {
		this.state_id = state_id;
	}

	public String getLandmark() {
		return landmark;
	}

	public int getPat_id() {
		return pat_id;
	}

	public void setPat_id(int pat_id) {
		this.pat_id = pat_id;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	// getter

	public String getLastname() {
		return lastname;
	}

	public String getAddress1() {
		return address1;
	}

	public String getAddress2() {
		return address2;
	}

	public String getCity() {
		return city;
	}

	public String getZip() {
		return zip;
	}

	public String getCountry() {
		return country;
	}

	public int getDefault_billing() {
		return default_billing;
	}

	public String getState() {
		return state;
	}

	// setter

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setDefault_billing(int default_billing) {
		this.default_billing = default_billing;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setId(int int1) {
		// TODO Auto-generated method stub

	}

}
