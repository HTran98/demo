package com.h3phonestore.service;

import java.util.List;

import com.h3phonestore.dto.RoleDto;

public interface RoleService {
	List<RoleDto> getAll();

	RoleDto createRole(RoleDto roleDto);
	
	RoleDto updateRole(RoleDto roleDto);

	void deleteRole(long roleId, String updatedBy);
	
	RoleDto getRoleById(long roleId);
	
	List<RoleDto> getRoleByRoleName(String roleName);
}
