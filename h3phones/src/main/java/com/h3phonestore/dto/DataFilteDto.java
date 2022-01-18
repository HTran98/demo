package com.h3phonestore.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataFilteDto {
	private List<String> listColor;
	private List<String> listRam;
	private List<String> listInternalMemory;
}
