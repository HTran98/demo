package com.h3phonestore.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.h3phonestore.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

	@Query("update Brand b set b.deleteFlag = ?1, b.updatedDate = ?2, b.updatedBy = ?3 where b.brandId = ?4  ")
	@Modifying
	int deleteByDeleteFlag(boolean deleteFlag, Date updatedDate, String updatedBy, long brandId);

	Optional<Brand> findByBrandIdAndDeleteFlag(long brandId, boolean deleteFlag);
	
	Optional<Brand> findByBrandNameAndDeleteFlag(String brandName, boolean deleteFlag);

	Optional<List<Brand>> findByDeleteFlag(boolean deleteFlag);

	Optional<List<Brand>> findByBrandNameContainingAndDeleteFlag(String brandName, boolean deleteFlag);

}
