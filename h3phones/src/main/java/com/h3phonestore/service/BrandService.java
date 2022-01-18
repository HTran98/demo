package com.h3phonestore.service;

import java.util.List;

import com.h3phonestore.dto.BrandDto;

public interface BrandService {

	List<BrandDto> getAll();

	BrandDto getBrandById(long brandId);

	BrandDto updateBrand(BrandDto brandDto);
	
	BrandDto createBrand(BrandDto brandDto);

	void deleteBrand(long brandId, String updatedBy);
	
	List<BrandDto> getByBrandName(String brandName);
	
	BrandDto getBrandName(String brandName);
	
	 
}
