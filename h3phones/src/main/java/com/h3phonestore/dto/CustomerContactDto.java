package com.h3phonestore.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerContactDto {
	
	private long contactId;

	private String customerName;

	private String email;

	private String phoneNumbers;

	private String createdBy;
	
	private String contactContent;

	Date createdDate;

	String updatedBy;

	Date updatedDate;

	boolean deleteFlag;
	
	private UserDto userContact;
}
