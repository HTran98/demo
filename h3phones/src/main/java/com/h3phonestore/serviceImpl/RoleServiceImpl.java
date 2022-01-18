package com.h3phonestore.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h3phonestore.constants.Constants;
import com.h3phonestore.dto.RoleDto;
import com.h3phonestore.dto.UserDto;
import com.h3phonestore.entity.Role;
import com.h3phonestore.repository.RoleRepository;
import com.h3phonestore.service.RoleService;
import com.h3phonestore.service.UserService;

import javassist.NotFoundException;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserService userService;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public List<RoleDto> getAll() {
		List<RoleDto> roleList = null;
		try {
			List<Role> roleEntityList = roleRepository.findByDeleteFlag(Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tồn tại"));
			roleList = roleEntityList.stream().map(role -> modelMapper.map(role, RoleDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return roleList;
	}

	@Override
	public RoleDto updateRole(RoleDto roleDto) {
		RoleDto role = null;
		try {
			Role roleEntity = roleRepository.findByRoleIdAndDeleteFlag(roleDto.getRoleId(), Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Role không tồn tại"));

			Date updatedDate = new Date();

			roleEntity.setUpdatedBy(roleDto.getCreatedBy());
			roleEntity.setDecscription(roleDto.getDecscription());
			roleEntity.setRoleName(roleDto.getRoleName());
			roleEntity.setUpdatedDate(updatedDate);

			roleEntity = roleRepository.save(roleEntity);

			role = modelMapper.map(roleEntity, RoleDto.class);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return role;
	}

	@Override
	public RoleDto createRole(RoleDto roleDto) {
		Role role = roleRepository.findByRoleNameAndDeleteFlag(roleDto.getRoleName(), Constants.DELETE_FLAG);
		if (role != null) {
			return null;
		} else {
			Date createdDate = new Date();
			Date updatedDate = new Date();

			roleDto.setCreatedDate(createdDate);
			roleDto.setUpdatedDate(updatedDate);

			roleDto.setDeleteFlag(Constants.DELETE_FLAG);

			role = modelMapper.map(roleDto, Role.class);

			role = roleRepository.save(role);
			return modelMapper.map(role, RoleDto.class);
		}
	}

	@Override
	public void deleteRole(long roleId, String updatedBy) {
		try {
			roleRepository.findByRoleIdAndDeleteFlag(roleId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Role không tồn tại"));

			Date updatedDate = new Date();

			List<UserDto> listUser = userService.findUserByRole(roleId);
			for (UserDto user : listUser) {
				userService.deleteUse(user.getUserId(), updatedBy);
			}

			roleRepository.deleteByDeleteFlag(Constants.DELETED_FLAG, updatedDate, updatedBy, roleId);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}

	}

	@Override
	public RoleDto getRoleById(long roleId) {
		RoleDto roleDto = null;

		try {
			Role role = roleRepository.findByRoleIdAndDeleteFlag(roleId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tồn tại roleId:" + roleId));
			roleDto = modelMapper.map(role, RoleDto.class);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return roleDto;
	}

	@Override
	public List<RoleDto> getRoleByRoleName(String roleName) {
		List<RoleDto> roleList = null;
		try {
			List<Role> roleEntityList = roleRepository.findByRoleNameContainingAndDeleteFlag(roleName, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Role không tồn tại"));

			roleList = roleEntityList.stream().map(roleEntity -> modelMapper.map(roleEntity, RoleDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return roleList;
	}

}
