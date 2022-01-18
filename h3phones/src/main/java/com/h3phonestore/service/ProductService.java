package com.h3phonestore.service;

import java.util.List;

import com.h3phonestore.dto.ProductDto;

public interface ProductService {
	
	List<ProductDto> getAll();

	ProductDto getByProductId(long productId);

	ProductDto createProduct(ProductDto productDto);
	
	ProductDto getProduct(String productName, long brandId);

	void deleteProduct(long productId, String updatedBy);

	ProductDto updateProduct(ProductDto productDto);
	
	List<ProductDto> getByProductName(String productName);
	
	List<ProductDto> getByBrand(long brandId);
	
	List<ProductDto> getByProductType(int productType);
	
	List<ProductDto> saveData(List<ProductDto> listProduct);
}
