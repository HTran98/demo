package com.h3phonestore.dto;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportDto {
	private Map<String, Integer> sales;
	private Map<String, Integer> revenue;
}
