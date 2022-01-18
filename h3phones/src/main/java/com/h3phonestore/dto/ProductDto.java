package com.h3phonestore.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {
	private long productId;

	private String productName;

	private int productType;

	private String createdBy;
	
	private String brandNameProduct;

	Date createdDate;

	private String updatedBy;

	Date updatedDate;

	boolean deleteFlag;
	
	String brandNames;

	private BrandDto brandInfo;
	
	private int sales;
	
	private int revenue;


	private List<ProductDetailDto> productDetails;
}
