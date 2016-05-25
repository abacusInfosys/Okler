package com.okler.databeans;

public class ProductDataBean {
	int prodId,presc_needed,units,prodType;
	String product_id,prodName,desc,side_effect,company,dosage,imgUrl,indications,contraIndi,caution,weight,generic_name,interacions,diet_rest,composition,packing_size,	key_feature,
			specfic,
			brandInfo,
			warranty;
	float mrp,oklerPrice,discount;
	int cart_item_id;
	float tax;
	String presc_id;
	int is_presc_upped;
	String cart_id;
	String cart_num;
	boolean isFavourite;
	String mediumUrl,thumbUrl,SmallUrl,clipArtUrl;
	
	
	
	public String getClipArtUrl() {
		return clipArtUrl;
	}
	public void setClipArtUrl(String clipArtUrl) {
		this.clipArtUrl = clipArtUrl;
	}
	public String getMediumUrl() {
		return mediumUrl;
	}
	public void setMediumUrl(String mediumUrl) {
		this.mediumUrl = mediumUrl;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}
	public String getSmallUrl() {
		return SmallUrl;
	}
	public void setSmallUrl(String smallUrl) {
		SmallUrl = smallUrl;
	}
	public boolean isFavourite() {
		return isFavourite;
	}
	public void setFavourite(boolean isFavourite) {
		this.isFavourite = isFavourite;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getWarranty() {
		return warranty;
	}
	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}
	public String getBrandInfo() {
		return brandInfo;
	}
	public void setBrandInfo(String brandInfo) {
		this.brandInfo = brandInfo;
	}
	public String getSpecfic() {
		return specfic;
	}
	public void setSpecfic(String specfic) {
		this.specfic = specfic;
	}
	public String getKey_feature() {
		return key_feature;
	}
	public void setKey_feature(String key_feature) {
		this.key_feature = key_feature;
	}
	public String getCart_id() {
		return cart_id;
	}

	public void setCart_id(String cart_id) {
		this.cart_id = cart_id;
	}

	public String getCart_num() {
		return cart_num;
	}

	public void setCart_num(String cart_num) {
		this.cart_num = cart_num;
	}
	
	public int getCart_item_id() {
		return cart_item_id;
	}
	public void setCart_item_id(int cart_item_id) {
		this.cart_item_id = cart_item_id;
	}
	public float getTax() {
		return tax;
	}
	public void setTax(float tax) {
		this.tax = tax;
	}
	public String getPresc_id() {
		return presc_id;
	}
	public void setPresc_id(String presc_id) {
		this.presc_id = presc_id;
	}
	public int getIs_presc_upped() {
		return is_presc_upped;
	}
	public void setIs_presc_upped(int is_presc_upped) {
		this.is_presc_upped = is_presc_upped;
	}
	public int getProdType() {
		return prodType;
	}
	public void setProdType(int prodType) {
		this.prodType = prodType;
	}
	public int getUnits() {
		return units;
	}
	public void setUnits(int units) {
		this.units = units;
	}
	public String getInteracions() {
		return interacions;
	}
	public void setInteracions(String interacions) {
		this.interacions = interacions;
	}
	public String getDiet_rest() {
		return diet_rest;
	}
	public void setDiet_rest(String diet_rest) {
		this.diet_rest = diet_rest;
	}
	public String getComposition() {
		return composition;
	}
	public void setComposition(String composition) {
		this.composition = composition;
	}
	public String getPacking_size() {
		return packing_size;
	}
	public void setPacking_size(String packing_size) {
		this.packing_size = packing_size;
	}
	public int getPresc_needed() {
		return presc_needed;
	}
	
	
	
	public String getGeneric_name() {
		return generic_name;
	}
	public void setGeneric_name(String generic_name) {
		this.generic_name = generic_name;
	}
	public int getProdId() {
		return prodId;
	}
	public void setProdId(int prodId) {
		this.prodId = prodId;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	
	public float getDiscount(){
		return discount;
	}
	public void setDiscount(float discount){
		this.discount = discount;
	}
	public String getDesc(){
		return desc;
	}
	public void setDesc(String desc){
		this.desc =desc;
	}
	public String getSideEff(){
		return side_effect;
	}
	public void setSideEff(String side_effect){
		this.side_effect = side_effect;
	}
	public String getCompany(){
		return company;
	}
	public void setCompany(String company){
		this.company = company;
	}
	public String getDosage(){
		return dosage;
	}
	public void setDosage(String dosage){
		this.dosage = dosage;
	}
	public String getImgUrl(){
		return imgUrl;
	}
	public void setImgUrl(String imgUrl){
		this.imgUrl = imgUrl;
	}
	public String getIndications(){
		return indications;
	}
	public void setIndications(String indications){
		this.indications = indications;
	}
	public String getContraIndi(){
		return contraIndi;
	}
	public void setContraIndi(String contraIndi){
		this.contraIndi = contraIndi;
	}
	public String getCaution(){
		return caution;
	}
	public void setCaution(String caution){
		this.caution =caution;
	}
	public String getSide_effect() {
		return side_effect;
	}
	public void setSide_effect(String side_effect) {
		this.side_effect = side_effect;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
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
	public int isPresc_needed() {
		return presc_needed;
	}
	public void setPresc_needed(int presc_needed) {
		this.presc_needed = presc_needed;
	}
	
}
