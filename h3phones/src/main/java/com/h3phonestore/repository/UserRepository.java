package com.h3phonestore.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.h3phonestore.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<List<User>> findByUserNameContainingAndDeleteFlagOrPhoneNumbersContainingAndDeleteFlag(String userName, boolean deleteFlag,String phoneNumbers, boolean delFlag);

	Optional<User> findByUserIdAndDeleteFlag(long userId, boolean deleteFlag);
	
	@Query("update User u set u.deleteFlag = ?1, u.updatedDate = ?2, u.updatedBy = ?3 where u.userId = ?4  ")
	@Modifying
	int deleteByDeleteFlag(boolean deleteFlag, Date updatedDate, String updatedBy, long userId);
	
	Optional<User> findByUserNameAndDeleteFlag(String userName, boolean deleteFlag);
	
	Optional<List<User>> findByRoleInfoRoleIdAndDeleteFlag(long roleId, boolean deleteFlag);
	
	Optional<List<User>> findByRoleInfoRoleIdAndUserId(long roleId, long userId);
	
	Optional<List<User>> findByDeleteFlag(boolean deleteFlag);
	
	Optional<User> findByEmailAndDeleteFlag(String email, boolean deleteFlag);
}
