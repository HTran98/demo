package com.h3phonestore.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.h3phonestore.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	@Query("update Invoice b set b.deleteFlag = ?1, b.updatedDate = ?2, b.updatedBy = ?3 where b.invoiceId = ?4  ")
	@Modifying
	int deleteByDeleteFlag(boolean deleteFlag, Date updatedDate, String updatedBy, long invoiceId);

	Optional<Invoice> findByInvoiceCodeAndDeleteFlag(String invoiceCode, boolean deleteFlag);

	Optional<Invoice> findByInvoiceIdAndDeleteFlag(long invoiceId, boolean deleteFlag);

	Optional<List<Invoice>> findByDeleteFlagOrderByCreatedDateDesc(boolean deleteFlag);

	Optional<List<Invoice>> findDistinctByCustomerContainingOrOrderInfoUserOrderPhoneNumbersContainingOrInvoiceCodeContaining(
			String userName, String phoneNumber, String invoiceCode);

	Optional<List<Invoice>> findByOrderInfoUserOrderUserNameAndDeleteFlag(String userName, boolean deleteFlag);

	@Query("update  Invoice i set i.status = ?1, i.updatedDate = ?2, i.updatedBy = ?3 where i.invoiceId = ?4")
	@Modifying
	int updatedByStatus(int status, Date updatedDate, String updatedBy, long invoiceId);

	@Query("update  Invoice i set i.customerVote = ?2, i.customerReview = ?3  where i.invoiceId = ?1")
	@Modifying
	int updatedCustomerReview(long invocieId, int vote, String customerReview);

	Optional<List<Invoice>> findByCreatedDateBetween(Date startDate, Date endDate);

	@Query("select i from Invoice i where i.status= :status"
	         + " and (:startDate is null or i.createdDate >= :startDate)"
			 + " and (:endDate is null or i.createdDate <= :endDate)")
	Optional<List<Invoice>> findByCreatedDateAndStatus(@Param("startDate") Date startDate,
			@Param("endDate") Date endDate, @Param("status") int status);
	
	@Query("select i from Invoice i where "
	         + " (:startDate is null or i.createdDate >= :startDate)"
			 + " and (:endDate is null or i.createdDate <= :endDate)")
	Optional<List<Invoice>> findByCreatedDate(@Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
}
