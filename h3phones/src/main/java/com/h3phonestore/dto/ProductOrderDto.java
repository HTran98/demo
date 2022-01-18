package com.h3phonestore.dto;

import java.util.Date;

import com.h3phonestore.entity.Invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductOrderDto {
	private long orderId;

	private int quantity;

	private int totalPrices;

	private int orderType;

	private String createdBy;

	Date createdDate;

	private String updatedBy;

	Date updatedDate;

	boolean deleteFlag;

	boolean checkOrder;

	private ProductDetailDto productDetail;

	private HeadPhoneDetailDto headPhone;

	private UserDto userOrder;

	private int status;

	private String customerReview;

	boolean voteFlag;
	
	private int customerVote;

	private Invoice invoiceInfo;
}
