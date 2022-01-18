package com.h3phonestore.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.h3phonestore.entity.ProductDetail;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

	@Query("update ProductDetail pd set pd.deleteFlag = ?1, pd.updatedDate = ?2, pd.updatedBy = ?3 where pd.productDetailId = ?4  ")
	@Modifying
	int deleteByDeleteFlag(boolean deleteFlag, Date updatedDate, String updatedBy, long productDetailId);

	@Query("select pd from ProductDetail pd where pd.deleteFlag = ?1 and pd.productInfo.productId = ?2 group by pd.internalMemory order by created_date desc")
	Optional<List<ProductDetail>> findByDeleteFlagAndProduct(boolean deleteFlag, long productId);
	
	Optional<List<ProductDetail>> findByDeleteFlagOrderByCreatedDateDesc(boolean deleteFlag);

	Optional<ProductDetail> findByProductDetailIdAndDeleteFlag(long productDetailId, boolean deleteFlag);

	Optional<List<ProductDetail>> findByProductInfoProductIdAndDeleteFlag(long productId, boolean deleteFlag);

	Optional<List<ProductDetail>> findByProductInfoBrandInfoBrandIdAndDeleteFlag(long brandId, boolean deleteFlag);

	@Query("select pd from ProductDetail pd where pd.productInfo.productId = :productId"
			+ " and pd.productInfo.brandInfo.brandId = :brandId" + " and (:color is null or pd.color = :color)"
			+ " and (:internalMemory is null or pd.internalMemory = :internalMemory)"
			+ " and (:ram is null or pd.ram = :ram)" + " and pd.deleteFlag = :deleteFlag")
	Optional<List<ProductDetail>> findByProductAndBrand(@Param("productId") long productId,
			@Param("brandId") long brandId, @Param("color") String color,
			@Param("internalMemory") String internalMemory, @Param("ram") String ram,
			@Param("deleteFlag") boolean deleteFlag);

	@Query("select pd from ProductDetail pd where pd.productInfo.productId = :productId"
			+ " and (:color is null or pd.color = :color)"
			+ " and (:internalMemory is null or pd.internalMemory = :internalMemory)"
			+ " and (:ram is null or pd.ram = :ram)" + " and pd.deleteFlag = :deleteFlag")
	Optional<List<ProductDetail>> findByProduct(@Param("productId") long productId, @Param("color") String color,
			@Param("internalMemory") String internalMemory, @Param("ram") String ram,
			@Param("deleteFlag") boolean deleteFlag);

	@Query("select pd from ProductDetail pd where" + " pd.productInfo.brandInfo.brandId = :brandId"
			+ " and (:color is null or pd.color = :color)"
			+ " and (:internalMemory is null or pd.internalMemory = :internalMemory)"
			+ " and (:ram is null or pd.ram = :ram)" + " and pd.deleteFlag = :deleteFlag")

	Optional<List<ProductDetail>> findByBrand(@Param("brandId") long brandId, @Param("color") String color,
			@Param("internalMemory") String internalMemory, @Param("ram") String ram,
			@Param("deleteFlag") boolean deleteFlag);

	@Query("select pd from ProductDetail pd where" + " (:color is null or pd.color = :color)"
			+ " and (:internalMemory is null or pd.internalMemory = :internalMemory)"
			+ " and (:ram is null or pd.ram = :ram)" + " and pd.deleteFlag = :deleteFlag")

	Optional<List<ProductDetail>> findByColorAndInternalMemory(@Param("color") String color,
			@Param("internalMemory") String internalMemory, @Param("ram") String ram,
			@Param("deleteFlag") boolean deleteFlag);

	@Query("select pd.color from ProductDetail pd group by pd.color")
	List<String> findByColor();

	@Query("select pd.ram from ProductDetail pd group by pd.ram")
	List<String> findByRam();

	@Query("select pd.internalMemory from ProductDetail pd group by pd.internalMemory")
	List<String> findByInternalMemory();

	@Query("select pd from ProductDetail pd where ( pd.productInfo.productName like %" + "?1" + "%"
			+ " or pd.productInfo.brandInfo.brandName like %?1%)" + " and deleteFlag = ?2")
	Optional<List<ProductDetail>> findProductDetail(String keyWords, boolean deleteFlag);

	@Query("update ProductDetail pd set pd.vote = ?1 where pd.productDetailId = ?2")
	@Modifying
	int updateVoteproductDetail(float vote, long productDetailId);

	Optional<List<ProductDetail>> findTop4DistinctByProductInfoBrandInfoBrandNameAndDeleteFlagOrRamAndDeleteFlagOrInternalMemoryAndDeleteFlagAndProductDetailIdNotOrderByQuantityExportDesc(
			String brandName, boolean deleteFlag, String ram, boolean del, String internalMemory, boolean delf,long productDetailId);

	Optional<List<ProductDetail>> findByDeleteFlagAndProductInfoBrandInfoBrandNameIn(boolean deleteFlag,
			List<String> brandNameList);

	Optional<List<ProductDetail>> findByDeleteFlagAndVoteBetween(boolean deleteFlag,float minVote, float maxVote);

	Optional<List<ProductDetail>> findByDeleteFlagAndDiscountBetween(boolean deleteFlag,float minDiscount, float maxDiscount);

	Optional<List<ProductDetail>> findByDeleteFlagAndPricesBetween(boolean deleteFlag,int minPrices, int maxPrices);

	Optional<List<ProductDetail>> findTop5DistinctByDeleteFlagOrderByDiscountDesc(boolean deleteFlag);

	Optional<List<ProductDetail>> findByDeleteFlagAndInternalMemoryIn(boolean deleteFlag,
			List<String> internalMemoryList);

	Optional<List<ProductDetail>> findByDeleteFlagAndRamIn(boolean deleteFlag, List<String> ramList);
	
	@Query("select pd from ProductDetail pd where (:startDate is null or pd.createdDate >= :startDate)"
	+" and (:endDate is null or pd.createdDate <= :endDate)"
	+" and pd.deleteFlag = :deleteFlag"
	+" group by pd.productInfo.productId")
	Optional<List<ProductDetail>> findByCreatedDate(@Param("startDate")Date startDate, @Param("endDate")Date endDate, @Param("deleteFlag")boolean deleteFlag);
}
