package com.h3phonestore.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "headphone")
@AllArgsConstructor
public class HeadPhoneDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long headPhoneId;

	private String headphoneUsageTime;
	
	private String chargingBoxUsageTime;
	
	private String chargingPort;
	
	private String audioTechnology;
	
	private String compatible;
	
	private String connectApplication;
	
	private String utilities;
	
	private String connectSameTime;
	
	private String connectTechnology;
	
	private String control;
	
	private String color;
	
	private String imageOverView;
	
	private String imageUnder;
	
	private String imageSide;
	
	private String imageOther;
	
	private int quantityImport;
	
	private int quantityExport;
	
    private int prices;
	
	private int pricesImport;
	
	private float discount;
	
	private float vote;

	private String createdBy;

	Date createdDate;

	private String updatedBy;

	Date updatedDate;

	boolean deleteFlag;

	@ManyToOne
	@JoinColumn(name = "productId")
	private Product product;
	
	@OneToMany(mappedBy = "headPhone")
	private List<ProductOrder> orderHeadPhoneInfos;
}
