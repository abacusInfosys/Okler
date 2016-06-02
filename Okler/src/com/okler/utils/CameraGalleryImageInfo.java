package com.okler.utils;

import android.graphics.Bitmap;

public class CameraGalleryImageInfo {
	private String fileName,filePath;	
	private Bitmap imgBitmap;
	private String uri;
	public CameraGalleryImageInfo()
	{
		fileName = null;
		filePath = null;
		setImgBitmap(null);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Bitmap getImgBitmap() {
		return imgBitmap;
	}

	public void setImgBitmap(Bitmap imgBitmap) {
		this.imgBitmap = imgBitmap;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}	
	
}
