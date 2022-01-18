package com.h3phonestore.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.h3phonestore.constants.Constants;
import com.h3phonestore.dto.DataFilteDto;
import com.h3phonestore.dto.ProductDetailDto;
import com.h3phonestore.dto.ProductDto;
import com.h3phonestore.dto.SearchProductDetailDto;
import com.h3phonestore.entity.ProductDetail;
import com.h3phonestore.repository.ProductDetailRepository;
import com.h3phonestore.service.ProductDetailService;
import com.h3phonestore.service.ProductService;

import javassist.NotFoundException;

@Service
@Transactional
public class ProductDetailServiceImpl implements ProductDetailService {
	@Autowired
	private ProductDetailRepository productDetailRepository;
	
	@Autowired
	private ProductService productService;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public List<ProductDetailDto> getAll() {
		List<ProductDetailDto> listProductDetail = null;
		try {
			List<ProductDetail> listProductDetailEntity = productDetailRepository
					.findByDeleteFlagOrderByCreatedDateDesc(Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có thông tin"));
			listProductDetail = listProductDetailEntity.stream()
					.map(productDetail -> modelMapper.map(productDetail, ProductDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProductDetail;
	}

	@Override
	public List<ProductDetailDto> getAllByProduct() {
		
		List<ProductDetailDto> listProductDetail = null;
		List<ProductDto> listProduct = productService.getAll();
		try {
			List<ProductDetail> listProductDetailEntitys = new ArrayList<ProductDetail>();
			
			List<ProductDetail> listProductDetailEntity = null;
			for (ProductDto product : listProduct) {
				listProductDetailEntity = productDetailRepository
						.findByDeleteFlagAndProduct(Constants.DELETE_FLAG, product.getProductId())
						.orElseThrow(() -> new NotFoundException("Không có thông tin"));
				listProductDetailEntitys.addAll(listProductDetailEntity);
			}
			
			listProductDetail = listProductDetailEntitys.stream()
					.map(productDetail -> modelMapper.map(productDetail, ProductDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProductDetail;
		
	}

	@Override
	public List<ProductDetailDto> getByProduct(long productId) {
		List<ProductDetailDto> listProductDetail = null;
		try {
			List<ProductDetail> listProductDetailEntity = productDetailRepository
					.findByProductInfoProductIdAndDeleteFlag(productId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có thông tin"));
			listProductDetail = listProductDetailEntity.stream()
					.map(productDetail -> modelMapper.map(productDetail, ProductDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProductDetail;
	}

	@Override
	public ProductDetailDto getProductDetailById(long productDetailId) {
		ProductDetailDto productDetail = null;
		try {
			ProductDetail productDetailEntity = productDetailRepository
					.findByProductDetailIdAndDeleteFlag(productDetailId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có thong tin"));
			productDetail = modelMapper.map(productDetailEntity, ProductDetailDto.class);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return productDetail;
	}

	@Override
	public ProductDetailDto createProductDetail(ProductDetailDto productDetailDto) {
		Date createdDate = new Date();
		Date updatedDate = new Date();

		productDetailDto.setCreatedDate(createdDate);
		productDetailDto.setUpdatedDate(updatedDate);
		productDetailDto.setDeleteFlag(Constants.DELETE_FLAG);
		productDetailDto.setVote(5);

		ProductDetail productDetailEntity = modelMapper.map(productDetailDto, ProductDetail.class);
		productDetailEntity = productDetailRepository.save(productDetailEntity);
		return modelMapper.map(productDetailEntity, ProductDetailDto.class);
	}

	@Override
	public void deleteProductDetail(long productDetailId, String updatedBy) {
		try {
			productDetailRepository.findByProductDetailIdAndDeleteFlag(productDetailId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có thông tin"));

			Date updatedDate = new Date();
			productDetailRepository.deleteByDeleteFlag(Constants.DELETED_FLAG, updatedDate, updatedBy, productDetailId);

		} catch (NotFoundException e) {

			e.printStackTrace();
		}

	}

	@Override
	public ProductDetailDto updateProductDetail(ProductDetailDto productDetailDto) {

		ProductDetailDto productDetail = null;
		try {
			ProductDetail productDetailEntity = productDetailRepository
					.findByProductDetailIdAndDeleteFlag(productDetailDto.getProductDetailId(), Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có thông tin"));
			Date updatedDate = new Date();

			productDetailDto.setUpdatedDate(updatedDate);
			productDetailDto.setCreatedBy(productDetailEntity.getCreatedBy());
			productDetailDto.setCreatedDate(productDetailEntity.getCreatedDate());
			productDetailDto.setDeleteFlag(Constants.DELETE_FLAG);
			productDetailDto.setVote(productDetailEntity.getVote());
			int quantityImport = productDetailEntity.getQuantityImport()+productDetailDto.getNewQuantityImport();
			
			productDetailDto.setQuantityImport(quantityImport);
			if (StringUtils.isEmpty(productDetailDto.getImageOverview())) {
				productDetailDto.setImageOverview(productDetailEntity.getImageOverview());
			}
			if (StringUtils.isEmpty(productDetailDto.getImageUnder())) {
				productDetailDto.setImageUnder(productDetailEntity.getImageUnder());
			}
			if (StringUtils.isEmpty(productDetailDto.getImageSide())) {
				productDetailDto.setImageSide(productDetailEntity.getImageSide());
			}
			if (StringUtils.isEmpty(productDetailDto.getImageOther())) {
				productDetailDto.setImageOther(productDetailEntity.getImageOther());
			}

			productDetailEntity = modelMapper.map(productDetailDto, ProductDetail.class);

			productDetailEntity = productDetailRepository.save(productDetailEntity);

			productDetail = modelMapper.map(productDetailEntity, ProductDetailDto.class);

		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return productDetail;
	}

	@Override
	public List<ProductDetailDto> filterProductDetail(SearchProductDetailDto searchDto) {

		List<ProductDetailDto> listProductDetail = null;
		List<ProductDetail> listProductDetailEntity = null;
		long productId;
		if (searchDto.getProductDetailId() == 0) {
			productId = Long.parseLong(searchDto.getProductId().split(" ")[2]);
		} else {
			productId = searchDto.getProductDetailId();
		}

		if (StringUtils.isEmptyOrWhitespace(searchDto.getColor())) {
			searchDto.setColor(null);
		}
		if (StringUtils.isEmptyOrWhitespace(searchDto.getRam())) {
			searchDto.setRam(null);
		}
		if (StringUtils.isEmptyOrWhitespace(searchDto.getInternalMemory())) {
			searchDto.setInternalMemory(null);
		}

		try {
			if (searchDto.getBrandId() == 0 && productId != 0) {
				listProductDetailEntity = productDetailRepository
						.findByProduct(productId, searchDto.getColor(), searchDto.getInternalMemory(),
								searchDto.getRam(), Constants.DELETE_FLAG)
						.orElseThrow(() -> new NotFoundException("Không có sản phẩm hợp lệ"));
			} else {
				if (searchDto.getBrandId() != 0 && productId == 0) {
					listProductDetailEntity = productDetailRepository
							.findByBrand(searchDto.getBrandId(), searchDto.getColor(), searchDto.getInternalMemory(),
									searchDto.getRam(), Constants.DELETE_FLAG)
							.orElseThrow(() -> new NotFoundException("Không có sản phẩm hợp lệ"));
				}
				if (searchDto.getBrandId() != 0 && productId != 0) {
					listProductDetailEntity = productDetailRepository
							.findByProductAndBrand(productId, searchDto.getBrandId(), searchDto.getColor(),
									searchDto.getInternalMemory(), searchDto.getRam(), Constants.DELETE_FLAG)
							.orElseThrow(() -> new NotFoundException("Không có sản phẩm hợp lệ"));
				}
				if (searchDto.getBrandId() == 0 && productId == 0) {
					listProductDetailEntity = productDetailRepository
							.findByColorAndInternalMemory(searchDto.getColor(), searchDto.getInternalMemory(),
									searchDto.getRam(), Constants.DELETE_FLAG)
							.orElseThrow(() -> new NotFoundException("Không có sản phẩm hợp lệ"));
				}
				if (searchDto.getBrandId() == 0 && productId == 0
						&& StringUtils.isEmptyOrWhitespace(searchDto.getColor())
						&& StringUtils.isEmptyOrWhitespace(searchDto.getRam())
						&& StringUtils.isEmptyOrWhitespace(searchDto.getInternalMemory())) {
					listProductDetailEntity = productDetailRepository.findByDeleteFlagOrderByCreatedDateDesc(Constants.DELETE_FLAG)
							.orElseThrow(() -> new NotFoundException("Không có sản phẩm hợp lệ"));
				}

			}

			listProductDetail = listProductDetailEntity.stream()
					.map(productDetail -> modelMapper.map(productDetail, ProductDetailDto.class))
					.collect(Collectors.toList());

		} catch (NotFoundException e) {

			e.printStackTrace();
		}

		return listProductDetail;
	}

	@Override
	public DataFilteDto getDataFilter() {
		DataFilteDto data = new DataFilteDto();

		data.setListColor(productDetailRepository.findByColor());
		data.setListRam(productDetailRepository.findByRam());
		data.setListInternalMemory(productDetailRepository.findByInternalMemory());

		return data;
	}

	@Override
	public List<ProductDetailDto> searchProductDetail(String keyWords) {
		List<ProductDetailDto> listProductDetail = null;
		try {
			List<ProductDetail> listProductDetailEntity = productDetailRepository
					.findProductDetail(keyWords, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có thông tin"));
			listProductDetail = listProductDetailEntity.stream()
					.map(productDetail -> modelMapper.map(productDetail, ProductDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProductDetail;

	}

	@Override
	public void updateVoteProductDetail(float vote, long productDetailId) {

		try {
			productDetailRepository.findByProductDetailIdAndDeleteFlag(productDetailId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có thông tin"));

			productDetailRepository.updateVoteproductDetail(vote, productDetailId);

		} catch (NotFoundException e) {

			e.printStackTrace();
		}

	}

	@Override
	public List<ProductDetailDto> getProductOther(SearchProductDetailDto searchDto) {

		List<ProductDetailDto> listProductDetail = null;
		try {
			List<ProductDetail> listProductDetailEntity = productDetailRepository
					.findTop4DistinctByProductInfoBrandInfoBrandNameAndDeleteFlagOrRamAndDeleteFlagOrInternalMemoryAndDeleteFlagAndProductDetailIdNotOrderByQuantityExportDesc(
							searchDto.getBrandName(), Constants.DELETE_FLAG, searchDto.getRam(), Constants.DELETE_FLAG,
							searchDto.getInternalMemory(), Constants.DELETE_FLAG,searchDto.getProductDetailId())
					.orElseThrow(() -> new NotFoundException("Không có thông tin"));
			listProductDetail = listProductDetailEntity.stream()
					.map(productDetail -> modelMapper.map(productDetail, ProductDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProductDetail;
	}

	@Override
	public List<ProductDetailDto> getProductByPrice(int minPrices, int maxPrices) {
		List<ProductDetailDto> listProductDetail = null;
		try {
			List<ProductDetail> listProductDetailEntity = productDetailRepository
					.findByDeleteFlagAndPricesBetween(Constants.DELETE_FLAG,minPrices, maxPrices)
					.orElseThrow(() -> new NotFoundException("Không có thông tin"));
			listProductDetail = listProductDetailEntity.stream()
					.map(productDetail -> modelMapper.map(productDetail, ProductDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProductDetail;
	}

	@Override
	public List<ProductDetailDto> getProductByBrand(List<String> brandNameList) {
		List<ProductDetailDto> listProductDetail = null;
		try {
			List<ProductDetail> listProductDetailEntity = productDetailRepository
					.findByDeleteFlagAndProductInfoBrandInfoBrandNameIn(Constants.DELETE_FLAG, brandNameList)
					.orElseThrow(() -> new NotFoundException("Không có thông tin"));
			listProductDetail = listProductDetailEntity.stream()
					.map(productDetail -> modelMapper.map(productDetail, ProductDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProductDetail;
	}

	@Override
	public List<ProductDetailDto> getProductByVote(float minVote, float maxVote) {
		List<ProductDetailDto> listProductDetail = null;
		try {
			List<ProductDetail> listProductDetailEntity = productDetailRepository.findByDeleteFlagAndVoteBetween(Constants.DELETE_FLAG,minVote, maxVote)
					.orElseThrow(() -> new NotFoundException("Không có thông tin"));
			listProductDetail = listProductDetailEntity.stream()
					.map(productDetail -> modelMapper.map(productDetail, ProductDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProductDetail;
	}

	@Override
	public List<ProductDetailDto> getProductByDiscount(float minDiscount, float maxDiscount) {
		List<ProductDetailDto> listProductDetail = null;
		try {
			List<ProductDetail> listProductDetailEntity = productDetailRepository
					.findByDeleteFlagAndDiscountBetween(Constants.DELETE_FLAG,minDiscount, maxDiscount)
					.orElseThrow(() -> new NotFoundException("Không có thông tin"));
			listProductDetail = listProductDetailEntity.stream()
					.map(productDetail -> modelMapper.map(productDetail, ProductDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProductDetail;
	}

	@Override
	public List<ProductDetailDto> getProductSpecial() {
		List<ProductDetailDto> listProductDetail = null;
		try {
			List<ProductDetail> listProductDetailEntity = productDetailRepository
					.findTop5DistinctByDeleteFlagOrderByDiscountDesc(Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có thông tin"));
			listProductDetail = listProductDetailEntity.stream()
					.map(productDetail -> modelMapper.map(productDetail, ProductDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProductDetail;
	}

	@Override
	public List<ProductDetailDto> getProductByInternalMemory(List<String> internalMemoryList) {
		List<ProductDetailDto> listProductDetail = null;
		try {
			List<ProductDetail> listProductDetailEntity = productDetailRepository
					.findByDeleteFlagAndInternalMemoryIn(Constants.DELETE_FLAG, internalMemoryList)
					.orElseThrow(() -> new NotFoundException("Không có thông tin"));
			listProductDetail = listProductDetailEntity.stream()
					.map(productDetail -> modelMapper.map(productDetail, ProductDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProductDetail;
	}

	@Override
	public List<ProductDetailDto> getProductByRam(List<String> ramList) {
		List<ProductDetailDto> listProductDetail = null;
		try {
			List<ProductDetail> listProductDetailEntity = productDetailRepository
					.findByDeleteFlagAndRamIn(Constants.DELETE_FLAG, ramList)
					.orElseThrow(() -> new NotFoundException("Không có thong tin"));
			listProductDetail = listProductDetailEntity.stream()
					.map(productDetail -> modelMapper.map(productDetail, ProductDetailDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProductDetail;
	}

	@Override
	public List<ProductDetailDto> saveData(List<ProductDetailDto> productDetailDtos) {
		List<ProductDetailDto> listProductDetail = null;

		Date date = new Date();

		for (int i = 0; i < productDetailDtos.size(); i++) {
			productDetailDtos.get(i).setCreatedDate(date);
			productDetailDtos.get(i).setUpdatedDate(date);
		}
		List<ProductDetail> listProductDetailEntity = productDetailDtos.stream()
				.map(productDetail -> modelMapper.map(productDetail, ProductDetail.class)).collect(Collectors.toList());

		listProductDetailEntity = productDetailRepository.saveAll(listProductDetailEntity);

		listProductDetail = listProductDetailEntity.stream()
				.map(productDetailEntity -> modelMapper.map(productDetailEntity, ProductDetailDto.class))
				.collect(Collectors.toList());

		return listProductDetail;
	}

}
