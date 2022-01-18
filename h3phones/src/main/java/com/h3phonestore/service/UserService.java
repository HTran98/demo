package com.h3phonestore.service;

import java.util.List;

import com.h3phonestore.dto.UserDto;



public interface UserService {
	List<UserDto> getAllUser();

	UserDto findByUserName(String userName);
	
	UserDto findByUserId(long userId);

	void deleteUse(long userId, String updatedBy);

	UserDto addUser(UserDto user);

	UserDto updateUser(UserDto user);
	
	List<UserDto> findUserLike(String keyWords);
	
	List<UserDto> findUser(long roleId, String userId);
	
	List<UserDto> findUserByRole(long roleId);
	
	UserDto findByEmail(String email);

}
