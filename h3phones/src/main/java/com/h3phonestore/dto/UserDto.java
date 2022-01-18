package com.h3phonestore.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	private long userId;

	private String userName;

	private String passWords;
	
	private String fullName;

	private String email;

	private String phoneNumbers;

	private String address;

	private String createdBy;

	Date createdDate;

	String updatedBy;

	Date updatedDate;

	boolean deleteFlag;

	
	private RoleDto roleInfo;

	
	private List<ProductOrderDto> orderInfos;
	
	private List<CustomerContactDto> userContacts;
}
