package com.h3phonestore.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleDto {
	private long roleId;

	private String roleName;
	
	private String decscription;
	
	private String createdBy;
	
	Date createdDate;
	
	private String updatedBy;
	
	Date updatedDate;
	
	boolean deleteFlag;


	private List<UserDto> userInfo;
}
