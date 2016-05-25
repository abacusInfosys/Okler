package com.okler.databeans;

import android.graphics.Bitmap;

public class PrescriptionImagesDataBean {
	Bitmap prescImages;
	int tag;
	String presUrl;
	String imgType;
	String base64ConvrtedImg;
	String imgUri;
	public String getImgUri() {
		return imgUri;
	}

	public void setImgUri(String imgUri) {
		this.imgUri = imgUri;
	}

	public String getBase64ConvrtedImg() {
		return base64ConvrtedImg;
	}

	public void setBase64ConvrtedImg(String base64ConvrtedImg) {
		this.base64ConvrtedImg = base64ConvrtedImg;
	}

	public String getImgType() {
		return imgType;
	}

	public void setImgType(String imgType) {
		this.imgType = imgType;
	}

	public int getTag() {
	return tag;
	}

	public void setTag(int tag) {
	this.tag = tag;
	}

	public Bitmap getPrescImages() {
		return prescImages;
	}

	public void setPrescImages(Bitmap prescImages) {
		this.prescImages = prescImages;
	}
	
	public String getPresUrl() {
		return presUrl;
	}
	public void setPresUrl(String presUrl) {
		this.presUrl = presUrl;
	}
}
