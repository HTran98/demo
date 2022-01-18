package com.h3phonestore.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.h3phonestore.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
	@Query("update Product p set p.deleteFlag = ?1, p.updatedDate = ?2, p.updatedBy = ?3 where p.productId = ?4  ")
	@Modifying
	int deleteByDeleteFlag(boolean deleteFlag, Date updatedDate, String updatedBy, long productId);
	
	Optional<List<Product>> findByDeleteFlag(boolean deleteFlag);
	
	Optional<List<Product>> findByProductNameContainingAndDeleteFlagOrProductTypeAndDeleteFlag(String productName, boolean deleteFlag, String productType,boolean delFlag);
	
	Optional<Product> findByProductIdAndDeleteFlag(long productId, boolean deleteFlag);
	
	Optional<Product> findByProductNameAndBrandInfoBrandIdAndDeleteFlag(String productName,long brandId, boolean deleteFlag);
	
	Optional<List<Product>> findByBrandInfoBrandIdAndDeleteFlag(long brandId,boolean deleteFlag);
	
	Optional<List<Product>> findByProductTypeAndDeleteFlag(int productType,boolean deleteFlag);

}
