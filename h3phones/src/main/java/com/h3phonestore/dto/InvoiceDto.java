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
public class InvoiceDto {
	private long invoiceId;

	private String customer;

	private String address;

	private String phoneNumber;

	private int customerVote;

	private String invoiceCode;

	private String deliveryTime;

	private int status;

	private String payment;

	private String customerReview;

	private String comment;

	private String createdBy;

	private Date createdDate;

	private String updatedBy;

	private Date updatedDate;

	private boolean deleteFlag;
	
	private boolean voteFlag;
	
	private int totalInvoice;


	private List<ProductOrderDto> orderInfo;
}
