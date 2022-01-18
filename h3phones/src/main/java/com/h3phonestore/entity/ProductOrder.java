package com.h3phonestore.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "productOrder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long orderId;

	private int quantity;

	private int totalPrices;

	private String createdBy;
	
	private int orderType;

	Date createdDate;

	private String updatedBy;

	private Date updatedDate;
	
	private int customerVote;
	
	private int status;
	
	private String customerReview;
	
	private boolean voteFlag;

	private boolean deleteFlag;

	@ManyToOne
	@JoinColumn(name = "productDetailId")
	private ProductDetail productDetail;
	
	@ManyToOne
	@JoinColumn(name = "headPhoneId")
	private HeadPhoneDetail headPhone;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User userOrder;
	
	@ManyToOne
	@JoinColumn(name = "invoiceId")
	private Invoice invoiceInfo;

}
