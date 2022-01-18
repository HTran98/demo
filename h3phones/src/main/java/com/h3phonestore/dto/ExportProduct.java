package com.h3phonestore.dto;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExportProduct {
	ArrayList<ProductDto> listProduct;
	
	ArrayList<BrandDto> listBrand;
}
