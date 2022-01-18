package com.h3phonestore.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchDateDto {
	private String startDate;
	private String endDate;
	private int status;
	private String productType;
}
