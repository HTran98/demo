package com.h3phonestore.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExportProductDetail {
	
	private ArrayList<ProductDetailDto> listProductDetail;
	
	private ArrayList<HeadPhoneDetailDto> listHeadPhone;
	

}
