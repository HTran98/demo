package com.h3phonestore.service;

import java.util.List;

import com.h3phonestore.dto.ProductOrderDto;
import com.h3phonestore.dto.ReportDto;
import com.h3phonestore.dto.SearchDateDto;

public interface ProductOrderService {
	List<ProductOrderDto> getOrederByUserName(String userName);

	void deleteProductOrder(long productOrderId, String updatedBy);

	ProductOrderDto createProductOrder(ProductOrderDto orderDto);

	ProductOrderDto updateProductOrder(ProductOrderDto orderDto);

	ProductOrderDto getOrderById(long orderId);

	void updateQuantityOrder(long productOrderId, long invoiceId, String updatedBy, int quantity, int totalPrices);

	List<ProductOrderDto> getProductDeliveryByUserName(String userName);

	int cancelDelivery(String updatedBy, long invoiceId);

	List<ProductOrderDto> getVoteProduct(String userName, int status);

	ProductOrderDto reviewProduct(ProductOrderDto invoiceDto, long productId);

	List<ProductOrderDto> getReviewProduct(long productDetailId);

	List<ProductOrderDto> getCheckReviewProduct(String userName);

	List<ProductOrderDto> getReviewHeadPhone(long headPhoneId);

	List<ProductOrderDto> getInvoice(long invoiceId);

	void updateSatus(long orderId, int status, String updatedBy);

	ReportDto getDataReport(SearchDateDto searchDateDto);


}
