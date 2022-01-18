package com.h3phonestore.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h3phonestore.constants.Constants;
import com.h3phonestore.dto.BrandDto;
import com.h3phonestore.dto.ProductDto;
import com.h3phonestore.entity.Brand;
import com.h3phonestore.repository.BrandRepository;
import com.h3phonestore.service.BrandService;
import com.h3phonestore.service.ProductService;

import javassist.NotFoundException;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

	@Autowired
	BrandRepository brandRepository;

	@Autowired
	ProductService productService;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public BrandDto createBrand(BrandDto brandDto) {

		Date createdDate = new Date();
		Date updatedDate = new Date();
		brandDto.setUpdatedDate(updatedDate);
		brandDto.setCreatedDate(createdDate);
		brandDto.setDeleteFlag(Constants.DELETE_FLAG);

		Brand brandEntity = modelMapper.map(brandDto, Brand.class);

		brandEntity = brandRepository.save(brandEntity);
		return modelMapper.map(brandEntity, BrandDto.class);
	}

	@Override
	public List<BrandDto> getAll() {

		List<BrandDto> listBrand = null;
		try {
			List<Brand> listBrandEntity = brandRepository.findByDeleteFlag(Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có nhà cung cấp "));
			listBrand = listBrandEntity.stream().map(brand -> modelMapper.map(brand, BrandDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listBrand;
	}
	

	@Override
	public BrandDto getBrandName(String brandName) {
		
		BrandDto brand = null;
		try {
			Brand brandEntity = brandRepository.findByBrandNameAndDeleteFlag(brandName, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp"));
			brand = modelMapper.map(brandEntity, BrandDto.class);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return brand;
		
	}

	@Override
	public BrandDto getBrandById(long brandId) {
		BrandDto brand = null;
		try {
			Brand brandEntity = brandRepository.findByBrandIdAndDeleteFlag(brandId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp"));
			brand = modelMapper.map(brandEntity, BrandDto.class);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return brand;
	}

	@Override
	public BrandDto updateBrand(BrandDto brandDto) {
		BrandDto brand = null;

		try {
			Brand brandEntity = brandRepository.findByBrandIdAndDeleteFlag(brandDto.getBrandId(), Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp"));

			Date updatedDate = new Date();

			brandEntity.setUpdatedDate(updatedDate);
			brandEntity.setUpdatedBy(brandDto.getUpdatedBy());
			brandEntity.setBrandName(brandDto.getBrandName());

			brandEntity = brandRepository.save(brandEntity);

			brand = modelMapper.map(brandEntity, BrandDto.class);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return brand;
	}

	@Override
	public void deleteBrand(long brandId, String updatedBy) {
		try {
			brandRepository.findByBrandIdAndDeleteFlag(brandId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp"));

			Date updatedDate = new Date();
			List<ProductDto> listProduct = productService.getByBrand(brandId);
			for (ProductDto product : listProduct) {
				productService.deleteProduct(product.getProductId(), updatedBy);
			}

			brandRepository.deleteByDeleteFlag(Constants.DELETED_FLAG, updatedDate, updatedBy, brandId);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}

	}

	@Override
	public List<BrandDto> getByBrandName(String brandName) {
		List<BrandDto> listBrand = null;
		try {
			List<Brand> listBrandEntity = brandRepository.findByBrandNameContainingAndDeleteFlag(brandName, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy nhà cung cấp"));

			listBrand = listBrandEntity.stream().map(brand -> modelMapper.map(brand, BrandDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listBrand;
	}

}
