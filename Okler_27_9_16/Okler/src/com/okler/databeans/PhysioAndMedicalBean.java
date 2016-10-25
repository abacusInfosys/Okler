package com.okler.databeans;

public class PhysioAndMedicalBean {
	String firstname;
	String surname;
	String email;
	String phoneno;
	String From;
	String To;
	String address;
	String pincode;
	String weight;
	String state;
	String city;
	String relation;
	String service;
	String order_id;
	String booking_status,booking_id;
	String custId;
	String service_required_for;
	public String getService_required_for() {
		return service_required_for;
	}
	public void setService_required_for(String service_required_for) {
		this.service_required_for = service_required_for;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}

	public void setBooking_status(String booking_status) {
		this.booking_status = booking_status;
	}
	
	public String getBooking_status()
	{
		return booking_status;
	}

	public String getBooking_id() {
		return booking_id;
	}

	public void setBooking_id(String booking_id) {
		this.booking_id = booking_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	int user_type;
	
	//getter methods
	
	public String getfirstname()
	{
		return firstname;
	}
	
	public String getsurname()
	{
		return surname;
	}
	
	public String getemail()
	{
		return email;
	}
	
	public String getphoneno()
	{
		return phoneno;
	}
	public String getfrom()
	{
		return From;
	}
	
	public String getto()
	{
		return To;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public String getPincode()
	{
		return pincode;
	}
	
	public String getWeight()
	{
		return weight;
	}
	
	public String getState()
	{
		return state;
	}
	public String getCity()
	{
		return city;
	}
	
	public String getRelation()
	{
		return relation;
	}
	
	public String getService()
	{
		return service;
	}
	
	public int getUsertype()
	{
		return user_type;
	}
	
	//setter methods
	
		public void setFirstname(String fname)
		{
			this.firstname = fname;
		}
		
		public void setsurname(String sname)
		{
			this.surname = sname;
		}
		
		public void setemail(String email)
		{
			this.email = email;
		}
		
		public void setphoneno(String phoneNumber)
		{
			this.phoneno = phoneNumber;
		}
		public void setfrom(String From)
		{
			this.From = From;
		}
		
		public void setto(String to)
		{
			this.To = to;
		}
		
		public void setAddress(String address)
		{
			this.address = address;
		}
		
		public void setPincode(String pincode)
		{
			this.pincode = pincode;
		}
		
		public void setWeight(String weight)
		{
			this.weight = weight;
		}
		
		public void setState(String state)
		{
			this.state = state;
		}
		public void setCity(String city)
		{
			this.city = city;
		}
		
		public void setRelation(String relation)
		{
			this.relation = relation;
		}
		
		public void setService(String service)
		{
			this.service = service;
		}
		
		public void setUserType(int user_type)
		{
			this.user_type = user_type;
		}
}
