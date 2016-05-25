package com.okler.databeans;

import java.util.ArrayList;

public class UsersDataBean {
	
	String fname;
	String lname;
	String street_address;
	 float curr_balance;
    String state_name;
    String dob;
    String email;
    int age;
    String gender;
    int height;
    int weight;
    int latitude;
    int longitude;
    String user_image;
    String phone;
    int available_status;
    String blood_group;
    int id;
    String password;
    ArrayList<AddressDataBean> addDatabean = new ArrayList<AddressDataBean>();
    ArrayList<AddressDataBean> patAddList = new ArrayList<AddressDataBean>();
    String userName;
    String refCode;
    String company;
    String UserAvatarUrl;
    String salutation;
    ArrayList<ProductDataBean> recentProdList = new ArrayList<ProductDataBean>();
    
    
    public ArrayList<ProductDataBean> getRecentProdList() {
		return recentProdList;
	}
	public void setRecentProdList(ArrayList<ProductDataBean> recentProdList) {
		this.recentProdList = recentProdList;
	}
	public String getSalutation() {
		return salutation;
	}
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}
	public String getUserAvatarUrl() {
		return UserAvatarUrl;
	}
	public void setUserAvatarUrl(String userAvatarUrl) {
		UserAvatarUrl = userAvatarUrl;
	}
	public ArrayList<AddressDataBean> getPatAddList() {
		return patAddList;
	}
	public void setPatAddList(ArrayList<AddressDataBean> patAddList) {
		this.patAddList = patAddList;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	public String getRefCode() {
		return refCode;
	}
	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}
	public float getCurr_balance() {
		return curr_balance;
	}
	public void setCurr_balance(float curr_balance) {
		this.curr_balance = curr_balance;
	}
	public String getStreet_address() {
		return street_address;
	}
	public void setStreet_address(String street_address) {
		this.street_address = street_address;
	}
	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public int getAvailable_status() {
		return available_status;
	}

	public void setAvailable_status(int available_status) {
		this.available_status = available_status;
	}

	public String getBlood_group() {
		return blood_group;
	}

	public void setBlood_group(String blood_group) {
		this.blood_group = blood_group;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UsersDataBean()
    {
    	addDatabean = new ArrayList<AddressDataBean>();
    }
    
    // getter
 

	public ArrayList<AddressDataBean> getAddDatabean() {
		return addDatabean;
	}

	public void setAddDatabean(ArrayList<AddressDataBean> address) {
		this.addDatabean = address;
	}

	public int getId()
    {
    	return id;
    }
    
    public String getFname()
    {
    	return fname;
    }
    
    public String getLname()
    {
    	return lname;
    }
    
    public String getEmail()
    {
    	return email;
    }
    
    public String getPhone()
    {
    	return phone;
    }   
    
   
    
    public String getPassword() {
		return password;
	}
// getter
    
    public void setId(int id)
    {
    	this.id = id;
    }
    
    public void setFname(String fname)
    {
    	this.fname = fname;
    }
    
    public void setLname(String lname)
    {
    	this.lname = lname;
    }
    
    public void setEmail(String email)
    {
    	this.email = email;
    }
    
    public void setPhone(String phone)
    {
    	this.phone = phone;
    }   
    public void setPassword(String password) {
		this.password = password;
	}
    public String getUser_image() {
		return user_image;
	}
    public void setUser_image(String user_image) {
		this.user_image = user_image;
	}
}
