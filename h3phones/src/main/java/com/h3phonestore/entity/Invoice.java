package com.h3phonestore.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invoice")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Invoice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	Date createdDate;

	private String updatedBy;

	Date updatedDate;

	boolean deleteFlag;
	
	boolean voteFlag;
	
	private int totalInvoice;
	
	@OneToMany(mappedBy = "invoiceInfo")
	private List<ProductOrder> orderInfo;
}
