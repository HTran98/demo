package com.h3phonestore.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.h3phonestore.entity.Invoice;
import com.h3phonestore.entity.ProductOrder;

public interface OrderRepository extends JpaRepository<ProductOrder, Long>{

	@Query("update ProductOrder b set b.deleteFlag = ?1, b.updatedDate = ?2, b.updatedBy = ?3 where b.orderId = ?4  ")
	@Modifying
	int deleteByDeleteFlag(boolean deleteFlag, Date updatedDate, String updatedBy, long orderId);
	
	Optional<List<ProductOrder>> findByDeleteFlag(boolean deleteFlag);
	
	Optional<List<ProductOrder>> findByUserOrderUserNameAndDeleteFlagAndStatusOrderByCreatedDateDesc(String userName, boolean deleteFlag, int status);
	
	Optional<ProductOrder> findByOrderIdAndDeleteFlag(long orderId, boolean deleteFlag);
	
	ProductOrder findByProductDetailProductDetailIdAndUserOrderUserIdAndDeleteFlagAndStatus(long productDetailId, long userId, boolean deleteFlag,int status);
	
	@Query("update ProductOrder b set b.status = ?1, b.updatedDate = ?2, b.updatedBy = ?3, b.quantity = ?4, b.totalPrices = ?5, b.invoiceInfo.invoiceId= ?6 where b.orderId = ?7  ")
	@Modifying
	int updateQuantityOrder(int status, Date updatedDate, String updatedBy,int quantity, int totalPrices,long invoiceId, long orderId);
	
	ProductOrder findByHeadPhoneHeadPhoneIdAndUserOrderUserIdAndDeleteFlagAndStatus(long headPhoneId, long userId, boolean deleteFlag,int status);
	
	Optional<List<ProductOrder>> findByUserOrderUserNameAndDeleteFlagAndStatusNotOrderByCreatedDateDesc(String userName, boolean deleteFlag, int status);
	
	@Query("update  ProductOrder o set o.status = ?1, o.updatedDate = ?2, o.updatedBy = ?3 where o.orderId = ?4")
	@Modifying
	int updatedByStatus(int status, Date updatedDate, String updatedBy, long orderId);
	
    Optional<List<ProductOrder>> findByProductDetailProductDetailIdAndCustomerReviewIsNotNullOrderByUpdatedDateDesc(long productDetailId);
	
	Optional<List<ProductOrder>> findByHeadPhoneHeadPhoneIdAndCustomerReviewIsNotNullOrderByUpdatedDateDesc(long headPhoneId);

	Optional<List<ProductOrder>> findByProductDetailProductDetailId(long productDetailId);
	
	Optional<List<ProductOrder>> findByHeadPhoneHeadPhoneId(long headPhoneId);

	List<ProductOrder> findByProductDetailProductDetailIdAndUserOrderUserName(long productDetailId,
			String userName);

	Optional<List<ProductOrder>> findByUserOrderUserNameAndVoteFlagAndStatusOrderByCreatedDateDesc(String userName, boolean voteFlag,
			int status);
	Optional<List<ProductOrder>> findByUserOrderUserNameAndStatusAndDeleteFlag(String userName, int status,
			boolean deleteFlag);
	
	Optional<List<ProductOrder>> findByInvoiceInfoInvoiceIdNotNullAndDeleteFlagOrderByCreatedDateDesc(boolean deleteFlag);
	
	Optional<List<ProductOrder>> findByInvoiceInfoInvoiceIdAndDeleteFlagOrderByCreatedDateDesc(long invoiceId, boolean deleteFlag);
	
	@Query("select i from ProductOrder i where i.status= :status"
	         + " and (:startDate is null or i.updatedDate >= :startDate)"
			 + " and (:endDate is null or i.updatedDate <= :endDate)")
	Optional<List<ProductOrder>> findByUpdatedDateAndStatus(@Param("startDate") Date startDate,
			@Param("endDate") Date endDate, @Param("status") int status);
}
