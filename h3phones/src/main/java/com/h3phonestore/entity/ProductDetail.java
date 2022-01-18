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
@Table(name = "productDetail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long productDetailId;

	private String screen;
	private String resolution;
	private String operatingSystem;
	private String rearCamera;
	private String frontCamera;
	private String chips;
	private String ram;
	private String internalMemory;
	private String sim;
	private String rechargeableBatteries;
	private String connector;
	private String headphoneJack;
	private String mobileNetwork;
	private String bluetooth;
	private String wifi;
	private String gps;
	private int prices;
	private int pricesImport;
	private String color;
	private String imageOverview;
	private String imageUnder;
	private String imageSide;
	private String imageOther;
	private int quantityImport;
	private int quantityExport;
	private float discount;
	private float vote;

	private String createdBy;

	Date createdDate;

	private String updatedBy;

	Date updatedDate;

	boolean deleteFlag;

	@ManyToOne
	@JoinColumn(name = "productId")
	private Product productInfo;
	
	@OneToMany(mappedBy = "productDetail")
	private List<ProductOrder> orderProductInfos;

}
