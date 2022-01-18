package com.h3phonestore.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.h3phonestore.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
	@Query("update Role r set r.deleteFlag = ?1, r.updatedDate = ?2, r.updatedBy = ?3 where r.roleId = ?4  ")
	@Modifying
	int deleteByDeleteFlag(boolean deleteFlag, Date updatedDate, String updatedBy, long roleId);
	
	Optional<List<Role>> findByDeleteFlag(boolean deleteFlag);
	
	Optional<Role> findByRoleIdAndDeleteFlag(long roleId, boolean deleteFlag);
	
	Optional<List<Role>> findByRoleNameContainingAndDeleteFlag(String roleName, boolean deleteFlag);
	
	Role findByRoleNameAndDeleteFlag(String roleName, boolean deleteFlag);
	
}
