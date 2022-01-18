package com.h3phonestore.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.h3phonestore.entity.CustomerContact;

public interface CustomerContactRepository extends JpaRepository<CustomerContact, Long> {
	@Query("update CustomerContact c set c.deleteFlag = ?1, c.updatedDate = ?2, c.updatedBy = ?3 where c.contactId = ?4  ")
	@Modifying
	int deleteByDeleteFlag(boolean deleteFlag, Date updatedDate, String updatedBy, long contactId);

	Optional<List<CustomerContact>> findByDeleteFlag(boolean deleteFlag);

	Optional<List<CustomerContact>> findByUserContactUserNameContainingOrEmailContainingOrPhoneNumbersContaining(
			String userName, String email, String phoneNumbers);
	
	Optional<CustomerContact> findByContactIdAndDeleteFlag(long contactId, boolean deleteFlag);
}
