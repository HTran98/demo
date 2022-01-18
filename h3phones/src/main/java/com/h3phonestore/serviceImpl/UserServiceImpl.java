package com.h3phonestore.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.h3phonestore.constants.Constants;
import com.h3phonestore.dto.RoleDto;
import com.h3phonestore.dto.UserDto;
import com.h3phonestore.entity.Role;
import com.h3phonestore.entity.User;
import com.h3phonestore.repository.RoleRepository;
import com.h3phonestore.repository.UserRepository;
import com.h3phonestore.service.UserService;

import javassist.NotFoundException;



@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;

	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	ModelMapper modelMapper = new ModelMapper();

	@Override
	public List<UserDto> getAllUser() {
		try {
			List<User> listUserEntity = userRepository.findByDeleteFlag(Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy user nào"));
			List<UserDto> listUser = listUserEntity.stream()
					.map(userEntity -> modelMapper.map(userEntity, UserDto.class)).collect(Collectors.toList());

			return listUser;
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public UserDto addUser(UserDto user) {

		Date createdDate = new Date();
		Date updatedDate = new Date();

		user.setCreatedDate(createdDate);
		user.setUpdatedDate(updatedDate);
		user.setPassWords(bCryptPasswordEncoder.encode(user.getPassWords()));

		user.setDeleteFlag(Constants.DELETE_FLAG);
		if(user.getRoleInfo()== null) {
			Role role = roleRepository.findByRoleNameAndDeleteFlag("ROLE_USER", Constants.DELETE_FLAG);
			RoleDto roleDto = new RoleDto();
			roleDto.setRoleId(role.getRoleId());
			user.setRoleInfo(roleDto);
		}

		User userEntity = modelMapper.map(user, User.class);

		userEntity = userRepository.save(userEntity);
		return modelMapper.map(userEntity, UserDto.class);
	}

	@Override
	public UserDto updateUser(UserDto user) {

		try {
			User userEntity = userRepository.findByUserIdAndDeleteFlag(user.getUserId(), Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy user:" + user.getUserId()));

			Date updatedDate = new Date();
			user.setUpdatedDate(updatedDate);
			user.setCreatedDate(userEntity.getCreatedDate());
			user.setCreatedBy(userEntity.getCreatedBy());

			if (StringUtils.isEmptyOrWhitespace(user.getPassWords())) {
				user.setPassWords(userEntity.getPassWords());
			} else {
				user.setPassWords(bCryptPasswordEncoder.encode(user.getPassWords()));
			}

			userEntity = modelMapper.map(user, User.class);

			userEntity = userRepository.save(userEntity);

			return modelMapper.map(userEntity, UserDto.class);

		} catch (NotFoundException e) {

			e.printStackTrace();
			return null;
		}
	}

	@Override
	public UserDto findByUserId(long userId) {
		UserDto user = null;
		try {
			User userEntity = userRepository.findById(userId)
					.orElseThrow(() -> new NotFoundException("User không tồn tại"));

			user = modelMapper.map(userEntity, UserDto.class);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return user;

	}

	@Override
	public UserDto findByUserName(String userName) {
		UserDto user = null;
		try {
			User userEntity = userRepository.findByUserNameAndDeleteFlag(userName, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("User không tồn tại"));

			user = modelMapper.map(userEntity, UserDto.class);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return user;
	}

	@Override
	public void deleteUse(long userId, String updatedBy) {
		try {
			userRepository.findByUserIdAndDeleteFlag(userId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("User không tồn tại"));
			Date updatedDate = new Date();
			userRepository.deleteByDeleteFlag(Constants.DELETED_FLAG, updatedDate,updatedBy, userId);

		} catch (NotFoundException e) {

			e.printStackTrace();
		}

	}

	@Override
	public List<UserDto> findUserLike(String keyWords) {
		List<UserDto> userList = null;

		try {
			List<User> userEntityList = userRepository
					.findByUserNameContainingAndDeleteFlagOrPhoneNumbersContainingAndDeleteFlag(keyWords, Constants.DELETE_FLAG,
							keyWords, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy user phù hợp"));
			userList = userEntityList.stream().map(userEntity -> modelMapper.map(userEntity, UserDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return userList;
	}

	@Override
	public List<UserDto> findUser(long roleId, String userId) {
		List<UserDto> userList = null;

		long userEntityId = Long.parseLong(userId.split(" ")[2]);

		try {
			List<User> userEntityList = new ArrayList<User>();
			if (roleId != 0 && userEntityId == 0) {
				userEntityList = userRepository.findByRoleInfoRoleIdAndDeleteFlag(roleId, Constants.DELETE_FLAG)
						.orElseThrow(() -> new NotFoundException("Không tìm thấy user phù hợp"));
			} else {
				if (roleId == 0 && userEntityId != 0) {
					User user = userRepository.findByUserIdAndDeleteFlag(userEntityId, Constants.DELETE_FLAG)
							.orElseThrow(() -> new NotFoundException("Không tìm thấy user phù hợp"));
					userEntityList.add(user);
				}
				if (roleId != 0 && userEntityId != 0) {
					userEntityList = userRepository.findByRoleInfoRoleIdAndUserId(roleId, userEntityId)
							.orElseThrow(() -> new NotFoundException("Không tìm thấy user phù hợp"));
				}
				if (roleId == 0 && userEntityId == 0) {
					userEntityList = userRepository.findByDeleteFlag(Constants.DELETE_FLAG)
							.orElseThrow(() -> new NotFoundException("Không tìm thấy user phù hợp"));
				}
			}

			userList = userEntityList.stream().map(userEntity -> modelMapper.map(userEntity, UserDto.class))
					.collect(Collectors.toList());

		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return userList;
	}

	@Override
	public List<UserDto> findUserByRole(long roleId) {
		List<UserDto> userList = null;

		try {
			List<User> userEntityList = userRepository.findByRoleInfoRoleIdAndDeleteFlag(roleId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy user phù hợp"));

			userList = userEntityList.stream().map(userEntity -> modelMapper.map(userEntity, UserDto.class))
					.collect(Collectors.toList());

		} catch (NotFoundException e) {

			e.printStackTrace();
		}

		return userList;
	}

	@Override
	public UserDto findByEmail(String email) {
		UserDto user = null;
		try {
			User userEntity = userRepository.findByEmailAndDeleteFlag(email, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("User không tồn tại"));

			user = modelMapper.map(userEntity, UserDto.class);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return user;
	}
	
}
