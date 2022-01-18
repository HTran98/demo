package com.h3phonestore.dto;

import java.beans.Transient;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HeadPhoneDetailDto {
	private long headPhoneId;

	private String headphoneUsageTime;
	
	private String chargingBoxUsageTime;
	
	private String chargingPort;
	
	private String audioTechnology;
	
	private String compatible;
	
	private String connectApplication;
	
	private String utilities;
	
	private String connectSameTime;
	
	private String connectTechnology;
	
	private String control;
	
	private String color;
	
	private String imageOverView;
	
	private String imageUnder;
	
	private String imageSide;
	
	private String imageOther;
	
	private int prices;
	
	private int pricesImport;
	
	private int quantityImport;
	
	private int quantityExport;
	
	private float discount;
	
	private float vote;
	
	private String headPhoneName;
	
	private int available;

	private String createdBy;

	Date createdDate;

	private String updatedBy;

	Date updatedDate;

	boolean deleteFlag;
	
	private ProductDto product;
	
	private int newQuantityImport;
	
	private MultipartFile[] multipartFiles;
	
	@Transient
	public String imageOverviewUrl() {
		return "/src/main/resources/static/images/imagesProduct/" + product.getProductName() + "/" + imageOverView;
	}
	@Transient
	public String imageUnderUrl() {
		return "/src/main/resources/static/images/imagesProduct/" + product.getProductName() + "/" + imageUnder;
	}
	@Transient
	public String imageSideUrl() {
		return "/src/main/resources/static/images/imagesProduct/" + product.getProductName() + "/" + imageSide;
	}
	@Transient
	public String imageOtherUrl() {
		return "/src/main/resources/static/images/imagesProduct/" + product.getProductName() + "/" + imageOther;
	}
}
