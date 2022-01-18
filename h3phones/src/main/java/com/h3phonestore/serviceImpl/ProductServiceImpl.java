package com.h3phonestore.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h3phonestore.constants.Constants;
import com.h3phonestore.dto.ProductDetailDto;
import com.h3phonestore.dto.ProductDto;
import com.h3phonestore.entity.Product;
import com.h3phonestore.repository.ProductRepository;
import com.h3phonestore.service.ProductDetailService;
import com.h3phonestore.service.ProductService;

import javassist.NotFoundException;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductDetailService productDetailService;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public List<ProductDto> getAll() {
		List<ProductDto> listProduct = null;
		try {
			List<Product> listProductEntity = productRepository.findByDeleteFlag(Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm "));
			listProduct = listProductEntity.stream().map(product -> modelMapper.map(product, ProductDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProduct;
	}

	@Override
	public ProductDto getByProductId(long productId) {
		ProductDto product = null;
		try {
			Product productEntity = productRepository.findByProductIdAndDeleteFlag(productId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm "));

			product = modelMapper.map(productEntity, ProductDto.class);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return product;
	}

	@Override
	public ProductDto getProduct(String productName, long brandId) {
		ProductDto product = null;
		try {
			Product productEntity = productRepository
					.findByProductNameAndBrandInfoBrandIdAndDeleteFlag(productName, brandId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm "));

			product = modelMapper.map(productEntity, ProductDto.class);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return product;
	}

	@Override
	public ProductDto createProduct(ProductDto productDto) {

		Date createdDate = new Date();
		Date updatedDate = new Date();

		productDto.setCreatedDate(createdDate);
		productDto.setUpdatedDate(updatedDate);
		productDto.setDeleteFlag(Constants.DELETE_FLAG);

		Product productEntity = modelMapper.map(productDto, Product.class);

		productEntity = productRepository.save(productEntity);

		return modelMapper.map(productEntity, ProductDto.class);
	}

	@Override
	public List<ProductDto> saveData(List<ProductDto> listProductDto) {
		
		Date createdDate = new Date();
		Date updatedDate = new Date();
		for(int i = 0; i<listProductDto.size(); i++)
		{
			listProductDto.get(i).setCreatedDate(createdDate);
			listProductDto.get(i).setUpdatedDate(updatedDate);
		}

		List<ProductDto> listProduct = null;
		List<Product> listProductEntity = listProductDto.stream()
				.map(product -> modelMapper.map(product, Product.class)).collect(Collectors.toList());
		listProductEntity = productRepository.saveAll(listProductEntity);

		listProduct = listProductEntity.stream().map(productEntity -> modelMapper.map(productEntity, ProductDto.class))
				.collect(Collectors.toList());
		return listProduct;
	}

	@Override
	public void deleteProduct(long productId, String updatedBy) {
		try {
			productRepository.findByProductIdAndDeleteFlag(productId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm "));
			List<ProductDetailDto> productDetailList = productDetailService.getByProduct(productId);
			for (ProductDetailDto productDetail : productDetailList) {
				productDetailService.deleteProductDetail(productDetail.getProductDetailId(), updatedBy);
			}
			Date updatedDate = new Date();

			productRepository.deleteByDeleteFlag(Constants.DELETED_FLAG, updatedDate, updatedBy, productId);

		} catch (NotFoundException e) {

			e.printStackTrace();
		}
	}

	@Override
	public ProductDto updateProduct(ProductDto productDto) {
		ProductDto product = null;
		try {
			Product productEntity = productRepository
					.findByProductIdAndDeleteFlag(productDto.getProductId(), Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm "));

			Date updatedDate = new Date();

			productDto.setUpdatedDate(updatedDate);
			productDto.setCreatedBy(productEntity.getCreatedBy());
			productDto.setCreatedDate(productEntity.getCreatedDate());
			productDto.setDeleteFlag(Constants.DELETE_FLAG);

			productEntity = modelMapper.map(productDto, Product.class);

			productEntity = productRepository.save(productEntity);

			product = modelMapper.map(productEntity, ProductDto.class);

		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return product;
	}

	@Override
	public List<ProductDto> getByProductName(String productName) {
		List<ProductDto> listProduct = null;
		try {
			List<Product> listProductEntity = productRepository
					.findByProductNameContainingAndDeleteFlagOrProductTypeAndDeleteFlag(productName,
							Constants.DELETE_FLAG, productName, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm"));
			listProduct = listProductEntity.stream().map(product -> modelMapper.map(product, ProductDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProduct;
	}

	@Override
	public List<ProductDto> getByBrand(long brandId) {

		List<ProductDto> listProduct = null;
		try {
			List<Product> listProductEntity = productRepository
					.findByBrandInfoBrandIdAndDeleteFlag(brandId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm"));
			listProduct = listProductEntity.stream().map(product -> modelMapper.map(product, ProductDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProduct;
	}

	@Override
	public List<ProductDto> getByProductType(int productType) {
		List<ProductDto> listProduct = null;
		try {
			List<Product> listProductEntity = productRepository
					.findByProductTypeAndDeleteFlag(productType, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm"));
			listProduct = listProductEntity.stream().map(product -> modelMapper.map(product, ProductDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProduct;
	}

}
