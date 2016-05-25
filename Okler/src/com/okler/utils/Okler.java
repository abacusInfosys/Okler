package com.okler.utils;

import java.util.ArrayList;
import java.util.Hashtable;

import com.okler.databeans.BrandsDataBean;
import com.okler.databeans.CategoriesDataBean;
import com.okler.databeans.DiagnoLabsDataBean;
import com.okler.databeans.DiagnoOrderDataBean;
import com.okler.databeans.DiseaseDataBean;
import com.okler.databeans.CartDataBean;

import com.okler.databeans.FavouritesDataBean;
import com.okler.databeans.OrdersDataBean;
import com.okler.databeans.PhysioAndMedicalBean;
import com.okler.databeans.PrescriptionsDataBean;
import com.okler.databeans.ProductDataBean;
import com.okler.databeans.SubCategoriesDataBean;
import com.okler.databeans.TestDataBean;
import com.okler.databeans.UsersDataBean;

import android.app.Application;
import android.graphics.Bitmap;

public class Okler extends Application {
	private static Okler mContext;
	private PhysioAndMedicalBean physioMedBean;

	public ArrayList<DiagnoOrderDataBean> getUserDiagnoOrders() {
		return userDiagnoOrders;
	}

	public void setUserDiagnoOrders(
			ArrayList<DiagnoOrderDataBean> userDiagnoOrders) {
		this.userDiagnoOrders = userDiagnoOrders;
	}

	int bookingType;
	ProductDataBean pdbean;
	ArrayList<ProductDataBean> prodList, subProdList;
	CartDataBean singleCart, mainCart, localCart;
	ArrayList<OrdersDataBean> usersOrders;
	FavouritesDataBean favDataBean;
	int postion;
	ArrayList<String> cities, relationList, relationIdList;
	ArrayList<String> states;
	ArrayList<String> citi_ids;
	DiagnoOrderDataBean diagnoOrderDataBean;
	ArrayList<DiagnoLabsDataBean> diagLabAddressList, diagLabAddrListNear;
	ArrayList<ProductDataBean> favourites;
	ArrayList<DiagnoOrderDataBean> userDiagnoOrders;

	public ArrayList<String> getRelationList() {
		return relationList;
	}

	public void setRelationList(ArrayList<String> relationList) {
		this.relationList = relationList;
	}

	public ArrayList<String> getRelationIdList() {
		return relationIdList;
	}

	public void setRelationIdList(ArrayList<String> relationIdList) {
		this.relationIdList = relationIdList;
	}

	/**
	 * @return the localCart
	 */
	public CartDataBean getLocalCart() {
		return localCart;
	}

	/**
	 * @param localCart
	 *            the localCart to set
	 */
	public void setLocalCart(CartDataBean localCart) {
		this.localCart = localCart;
	}

	public DiagnoOrderDataBean getDiagnoOrderDataBean() {
		return diagnoOrderDataBean;
	}

	public void setDiagnoOrderDataBean(DiagnoOrderDataBean diagnoOrderDataBean) {
		this.diagnoOrderDataBean = diagnoOrderDataBean;
	}

	public ArrayList<DiagnoLabsDataBean> getDiagLabAddrListNear() {
		return diagLabAddrListNear;
	}

	public void setDiagLabAddrListNear(
			ArrayList<DiagnoLabsDataBean> diagLabAddrListNear) {
		this.diagLabAddrListNear = diagLabAddrListNear;
	}

	public ArrayList<DiagnoLabsDataBean> getDiagLabAddressList() {
		return diagLabAddressList;
	}

	public void setDiagLabAddressList(
			ArrayList<DiagnoLabsDataBean> diagLabAddressList) {
		this.diagLabAddressList = diagLabAddressList;
	}

	public ArrayList<String> getCiti_ids() {
		return citi_ids;
	}

	public void setCiti_ids(ArrayList<String> citi_ids) {
		this.citi_ids = citi_ids;
	}

	public ArrayList<String> getCities() {
		return cities;
	}

	public void setCities(ArrayList<String> cities) {
		this.cities = cities;
	}

	public ArrayList<String> getStates() {
		return states;
	}

	public void setStates(ArrayList<String> states) {
		this.states = states;
	}

	public ArrayList<OrdersDataBean> getUsersOrders() {
		return usersOrders;
	}

	public void setUsersOrders(ArrayList<OrdersDataBean> usersOrders) {
		this.usersOrders = usersOrders;
	}

	public CartDataBean getSingleCart() {
		return singleCart;
	}

	public void setSingleCart(CartDataBean singleCart) {
		this.singleCart = singleCart;
	}

	public CartDataBean getMainCart() {
		return mainCart;
	}

	public void setMainCart(CartDataBean mainCart) {
		this.mainCart = mainCart;
	}

	public ArrayList<ProductDataBean> getFavourites() {
		return favourites;
	}

	public void setFavourites(ArrayList<ProductDataBean> favourites) {
		this.favourites = favourites;
	}

	PrescriptionsDataBean prescriptionsDataBeans;
	ArrayList<CategoriesDataBean> categoriesBean;
	ArrayList<SubCategoriesDataBean> subCategories;
	ArrayList<BrandsDataBean> alloBrands;
	ArrayList<BrandsDataBean> homeoBrands;
	ArrayList<BrandsDataBean> hsBrands;
	ArrayList<String> priceRanges;
	private ArrayList<TestDataBean> listSelectedDiagnoTest = null;
	private ArrayList<DiseaseDataBean> listSelectedDisease = null;
	/* History variables */

	ArrayList<PrescriptionsDataBean> prescHistory;

	public PrescriptionsDataBean getPrescriptionsDataBeans() {
		return prescriptionsDataBeans;
	}

	public void setPrescriptionsDataBeans(
			PrescriptionsDataBean prescriptionsDataBeans) {
		this.prescriptionsDataBeans = prescriptionsDataBeans;
	}

	UsersDataBean uDataBean;

	public ArrayList<ProductDataBean> getSubProdList() {
		return subProdList;
	}

	public void setSubProdList(ArrayList<ProductDataBean> subProdList) {
		this.subProdList = subProdList;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = this;
		setPhysioMedBean(new PhysioAndMedicalBean());
		prescriptionsDataBeans = new PrescriptionsDataBean();
		physioMedBean = new PhysioAndMedicalBean();
		pdbean = new ProductDataBean();
		prodList = new ArrayList<ProductDataBean>();
		singleCart = new CartDataBean();
		mainCart = new CartDataBean();
		subProdList = new ArrayList<ProductDataBean>();
		uDataBean = new UsersDataBean();
		categoriesBean = new ArrayList<CategoriesDataBean>();
		subCategories = new ArrayList<SubCategoriesDataBean>();
		alloBrands = new ArrayList<BrandsDataBean>();
		homeoBrands = new ArrayList<BrandsDataBean>();
		hsBrands = new ArrayList<BrandsDataBean>();
		priceRanges = new ArrayList<String>();
		prescHistory = new ArrayList<PrescriptionsDataBean>();
		usersOrders = new ArrayList<OrdersDataBean>();
		favourites = new ArrayList<ProductDataBean>();
		favDataBean = new FavouritesDataBean();
		cities = new ArrayList<String>();
		states = new ArrayList<String>();
		citi_ids = new ArrayList<String>();
		listSelectedDiagnoTest = new ArrayList<TestDataBean>();
		diagLabAddressList = new ArrayList<DiagnoLabsDataBean>();
		diagLabAddrListNear = new ArrayList<DiagnoLabsDataBean>();
		diagnoOrderDataBean = new DiagnoOrderDataBean();
		favourites = new ArrayList<ProductDataBean>();
		localCart = new CartDataBean();
		relationList = new ArrayList<String>();
		relationIdList = new ArrayList<String>();
	}

	public FavouritesDataBean getFavDataBean() {
		return favDataBean;
	}

	public void setFavDataBean(FavouritesDataBean favDataBean) {
		this.favDataBean = favDataBean;
	}

	public ArrayList<PrescriptionsDataBean> getPrescHistory() {
		return prescHistory;
	}

	public void setPrescHistory(ArrayList<PrescriptionsDataBean> prescHistory) {
		this.prescHistory = prescHistory;
	}

	public ArrayList<BrandsDataBean> getHsBrands() {
		return hsBrands;
	}

	public void setHsBrands(ArrayList<BrandsDataBean> hsBrands) {
		this.hsBrands = hsBrands;
	}

	public ArrayList<CategoriesDataBean> getCategoriesBean() {
		return categoriesBean;
	}

	public void setCategoriesBean(ArrayList<CategoriesDataBean> categoriesBean) {
		this.categoriesBean = categoriesBean;
	}

	public ArrayList<SubCategoriesDataBean> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(ArrayList<SubCategoriesDataBean> subCategories) {
		this.subCategories = subCategories;
	}

	public ArrayList<BrandsDataBean> getAlloBrands() {
		return alloBrands;
	}

	public void setAlloBrands(ArrayList<BrandsDataBean> alloBrands) {
		this.alloBrands = alloBrands;
	}

	public ArrayList<BrandsDataBean> getHomeoBrands() {
		return homeoBrands;
	}

	public void setHomeoBrands(ArrayList<BrandsDataBean> homeoBrands) {
		this.homeoBrands = homeoBrands;
	}

	public ArrayList<String> getPriceRanges() {
		return priceRanges;
	}

	public void setPriceRanges(ArrayList<String> priceRanges) {
		this.priceRanges = priceRanges;
	}

	public ArrayList<ProductDataBean> getProdList() {
		return prodList;
	}

	public void setProdList(ArrayList<ProductDataBean> prodList) {
		this.prodList = prodList;
	}

	public static Okler getInstance() {
		return mContext;
	}

	public int getBookingType() {
		return bookingType;
	}

	public void setBookingType(int bookingType) {
		this.bookingType = bookingType;
	}

	public int getPostion() {
		return postion;
	}

	public void setPostion(int postion) {
		this.postion = postion;
	}

	public ProductDataBean getPdbean() {
		return pdbean;
	}

	public void setPdbean(ProductDataBean pdbean) {
		this.pdbean = pdbean;
	}

	public PhysioAndMedicalBean getPhysioMedBean() {
		return physioMedBean;
	}

	public void setPhysioMedBean(PhysioAndMedicalBean physioMedBean) {
		this.physioMedBean = physioMedBean;
	}

	public UsersDataBean getuDataBean() {
		return uDataBean;
	}

	public void setuDataBean(UsersDataBean uDataBean) {
		this.uDataBean = uDataBean;
	}

	public void setListSelectedDiagnoTest(
			ArrayList<TestDataBean> listSelectedDiagnoTest) {
		this.listSelectedDiagnoTest = listSelectedDiagnoTest;
	}

	public ArrayList<TestDataBean> getListSelectedDiagnoTest() {
		return listSelectedDiagnoTest;
	}

	public ArrayList<DiseaseDataBean> getListSelectedDisease() {
		return listSelectedDisease;
	}

	public void setListSelectedDisease(
			ArrayList<DiseaseDataBean> listSelectedDisease) {
		this.listSelectedDisease = listSelectedDisease;
	}

}
