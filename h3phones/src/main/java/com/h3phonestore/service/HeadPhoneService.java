package com.h3phonestore.service;

import java.util.List;

import com.h3phonestore.dto.HeadPhoneDetailDto;
import com.h3phonestore.dto.SearchProductDetailDto;

public interface HeadPhoneService {
	List<HeadPhoneDetailDto> getAll();

	void deleteHeadPhone(long headPhoneId, String updatedBy);

	HeadPhoneDetailDto updateHeadPhone(HeadPhoneDetailDto heDetailDto);

	HeadPhoneDetailDto createHeadPhone(HeadPhoneDetailDto heDetailDto);

	HeadPhoneDetailDto getById(long headPhoneId);

	List<HeadPhoneDetailDto> getByBrand(long brandId);

	List<HeadPhoneDetailDto> filterHeadPhoneDetail(SearchProductDetailDto searchProductDetailDto);

	List<HeadPhoneDetailDto> searchHeadPhone(String keyWords);

	List<HeadPhoneDetailDto> saveData(List<HeadPhoneDetailDto> listHeadPhoneDto);

	List<HeadPhoneDetailDto> getProductByPrice(int minPrices, int maxPrices);

	List<HeadPhoneDetailDto> getProductByBrand(List<String> brandNameList);

	List<HeadPhoneDetailDto> getProductByVote(float minVote, float maxVote);
	
	List<HeadPhoneDetailDto> getProductOther(String brandName, int prices,long headPhoneId);
	
	void updateVote(float vote, long headPhoneId);
}
