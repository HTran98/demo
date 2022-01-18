package com.h3phonestore.service;

import java.util.List;

import com.h3phonestore.dto.DataFilteDto;
import com.h3phonestore.dto.ProductDetailDto;
import com.h3phonestore.dto.SearchProductDetailDto;

public interface ProductDetailService {
	List<ProductDetailDto> getAll();
	
	List<ProductDetailDto> getAllByProduct();

	ProductDetailDto getProductDetailById(long productDetailId);

	ProductDetailDto createProductDetail(ProductDetailDto productDetailDto);

	void deleteProductDetail(long productDetailId, String updatedBy);

	ProductDetailDto updateProductDetail(ProductDetailDto productDetailDto);

	List<ProductDetailDto> filterProductDetail(SearchProductDetailDto searchProductDetailDto);

	DataFilteDto getDataFilter();

	List<ProductDetailDto> searchProductDetail(String keyWords);

	List<ProductDetailDto> getByProduct(long productId);

	void updateVoteProductDetail(float vote, long id);
	
	List<ProductDetailDto> getProductOther(SearchProductDetailDto searchProductDetailDto);
	
	List<ProductDetailDto> getProductByPrice(int minPrices, int maxPrices);
	
	List<ProductDetailDto> getProductByBrand(List<String> brandNameList);
	
	List<ProductDetailDto> getProductByInternalMemory(List<String> internalMemoryList);
	
	List<ProductDetailDto> getProductByRam(List<String> ramList);
	
	List<ProductDetailDto> getProductByVote(float minVote, float maxVote);
	
	List<ProductDetailDto> getProductByDiscount(float minDiscount, float maxDiscount);
	
	List<ProductDetailDto> getProductSpecial();
	
	List<ProductDetailDto> saveData(List<ProductDetailDto> productDetailDtos);
}
