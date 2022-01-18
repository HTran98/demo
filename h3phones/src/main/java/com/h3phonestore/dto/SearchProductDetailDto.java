package com.h3phonestore.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductDetailDto {
	private long brandId;
	private String brandName;
	private long productDetailId;
	private String productId;
	private String color;
	private String ram;
	private String internalMemory;
	private ArrayList<BrandDto> listBrand;
	private ArrayList<InternalMemoryDto> listInternalMemory;

}
