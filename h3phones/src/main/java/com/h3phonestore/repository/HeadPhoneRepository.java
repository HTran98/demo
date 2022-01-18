package com.h3phonestore.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.h3phonestore.entity.HeadPhoneDetail;

public interface HeadPhoneRepository extends JpaRepository<HeadPhoneDetail, Long> {
	@Query("update HeadPhoneDetail hp set hp.deleteFlag = ?1, hp.updatedDate = ?2, hp.updatedBy = ?3 where hp.headPhoneId = ?4  ")
	@Modifying
	int deleteByDeleteFlag(boolean deleteFlag, Date updatedDate, String updatedBy, long headPhoneId);

	Optional<List<HeadPhoneDetail>> findByDeleteFlag(boolean deleteFlag);

	Optional<HeadPhoneDetail> findByHeadPhoneIdAndDeleteFlag(long headPhoneId, boolean deleteFlag);

	Optional<List<HeadPhoneDetail>> findByProductProductIdAndDeleteFlag(long productId, boolean deleteFlag);

	Optional<List<HeadPhoneDetail>> findByProductBrandInfoBrandIdAndDeleteFlag(long brandId, boolean deleteFlag);
	
	Optional<List<HeadPhoneDetail>> findByProductProductIdAndProductBrandInfoBrandIdAndDeleteFlag(long productId, long brandId, boolean deleteFlag);
	
	Optional<List<HeadPhoneDetail>> findByProductProductNameContainingAndDeleteFlag(String productName, boolean deleteFlag);
	
	Optional<List<HeadPhoneDetail>> findByDeleteFlagAndVoteBetween(boolean deleteFlag,float minVote, float maxVote);

	Optional<List<HeadPhoneDetail>> findByDeleteFlagAndDiscountBetween(boolean deleteFlag,float minDiscount, float maxDiscount);

	Optional<List<HeadPhoneDetail>> findByDeleteFlagAndPricesBetween(boolean deleteFlag,int minPrices, int maxPrices);
	
	@Query("update HeadPhoneDetail hp set hp.vote = ?1 where hp.headPhoneId = ?2")
	@Modifying
	int updateVote(float vote, long headPhoneId);
	
	Optional<List<HeadPhoneDetail>> findByDeleteFlagAndProductBrandInfoBrandNameIn(boolean deleteFlag,
			List<String> brandNameList);
	Optional<List<HeadPhoneDetail>> findTop4DistinctByDeleteFlagAndPricesLessThanEqualOrProductProductNameAndHeadPhoneIdNotOrderByQuantityExportDesc(boolean deleteFlag,int prices, String brandName, long headPhoneId);
}
