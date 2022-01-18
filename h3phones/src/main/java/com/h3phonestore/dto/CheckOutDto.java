package com.h3phonestore.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckOutDto {
	private ArrayList<ProductOrderDto> listProductOrder;
	private ArrayList<HeadPhoneDetailDto> listHeadPhoneOrder;
	private String customer;
	private String phoneNumber;
	private String address;
	private String comment;
}
