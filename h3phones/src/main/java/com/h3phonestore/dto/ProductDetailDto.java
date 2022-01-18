package com.h3phonestore.dto;

import java.beans.Transient;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDetailDto {
	private long productDetailId;

	private String screen;
	private String resolution;
	private String operatingSystem;
	private String rearCamera;
	private String frontCamera;
	private String chips;
	private String ram;
	private String internalMemory;
	private String sim;
	private String rechargeableBatteries;
	private String connector;
	private String headphoneJack;
	private String mobileNetwork;
	private String bluetooth;
	private String wifi;
	private String gps;
	private int prices;
	private int pricesImport;
	private String color;
	private String imageOverview;
	private String imageUnder;
	private String imageSide;
	private String imageOther;
	private int quantityImport;
	private int quantityExport;
    private float discount;
	private float vote;
    private String productDetailName;
    private int available;
	private String createdBy;
    private int newQuantityImport;
	Date createdDate;

	private String updatedBy;

	Date updatedDate;

	boolean deleteFlag;

	private ProductDto productInfo;

	private MultipartFile[] multipartFiles;
	
	@Transient
	public String imageOverviewUrl() {
		return "/src/main/resources/static/images/imagesProduct/" + productInfo.getProductName() + "/" + imageOverview;
	}
	@Transient
	public String imageUnderUrl() {
		return "/src/main/resources/static/images/imagesProduct/" + productInfo.getProductName() + "/" + imageUnder;
	}
	@Transient
	public String imageSideUrl() {
		return "/src/main/resources/static/images/imagesProduct/" + productInfo.getProductName() + "/" + imageSide;
	}
	@Transient
	public String imageOtherUrl() {
		return "/src/main/resources/static/images/imagesProduct/" + productInfo.getProductName() + "/" + imageOther;
	}
}
