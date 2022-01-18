package com.h3phonestore.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.h3phonestore.constants.Constants;
import com.h3phonestore.dto.HeadPhoneDetailDto;
import com.h3phonestore.dto.ProductDetailDto;
import com.h3phonestore.dto.ProductDto;
import com.h3phonestore.dto.ProductOrderDto;
import com.h3phonestore.dto.ReportDto;
import com.h3phonestore.dto.SearchDateDto;
import com.h3phonestore.entity.ProductOrder;
import com.h3phonestore.repository.OrderRepository;
import com.h3phonestore.service.HeadPhoneService;
import com.h3phonestore.service.ProductDetailService;
import com.h3phonestore.service.ProductOrderService;
import com.h3phonestore.service.ProductService;

import javassist.NotFoundException;

@Service
@Transactional
public class ProductOrderServiceImpl implements ProductOrderService {
	@Autowired
	OrderRepository productOrderRepository;

	@Autowired
	private ProductDetailService productDetailService;

	@Autowired
	private HeadPhoneService headPhoneService;

	@Autowired
	private ProductService productService;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public List<ProductOrderDto> getOrederByUserName(String userName) {
		List<ProductOrderDto> listProductOrder = null;
		try {
			List<ProductOrder> listProductOrderEntity = productOrderRepository
					.findByUserOrderUserNameAndDeleteFlagAndStatusOrderByCreatedDateDesc(userName,
							Constants.DELETE_FLAG, Constants.ORDER_STATUS)
					.orElseThrow(() -> new NotFoundException("Chưa đặt hàng"));
			listProductOrder = listProductOrderEntity.stream()
					.map(productOrder -> modelMapper.map(productOrder, ProductOrderDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProductOrder;
	}

	@Override
	public List<ProductOrderDto> getProductDeliveryByUserName(String userName) {
		List<ProductOrderDto> listProductOrder = null;
		try {
			List<ProductOrder> listProductOrderEntity = productOrderRepository
					.findByUserOrderUserNameAndDeleteFlagAndStatusNotOrderByCreatedDateDesc(userName,
							Constants.DELETE_FLAG, Constants.ORDER_STATUS)
					.orElseThrow(() -> new NotFoundException("Chưa đặt hàng"));
			listProductOrder = listProductOrderEntity.stream()
					.map(productOrder -> modelMapper.map(productOrder, ProductOrderDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listProductOrder;
	}

	@Override
	public void deleteProductOrder(long productOrderId, String updatedBy) {
		try {
			productOrderRepository.findByOrderIdAndDeleteFlag(productOrderId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tồn tại"));
			Date updatedDate = new Date();
			productOrderRepository.deleteByDeleteFlag(Constants.DELETED_FLAG, updatedDate, updatedBy, productOrderId);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}

	}

	@Override
	public ProductOrderDto createProductOrder(ProductOrderDto orderDto) {
		Date createdDate = new Date();
		Date updatedDate = new Date();
		int quantity = 0;
		ProductOrder productOrder;

		if (orderDto.getOrderType() == Constants.TYPEPHONE) {
			productOrder = productOrderRepository
					.findByProductDetailProductDetailIdAndUserOrderUserIdAndDeleteFlagAndStatus(
							orderDto.getProductDetail().getProductDetailId(), orderDto.getUserOrder().getUserId(),
							Constants.DELETE_FLAG, Constants.ORDER_STATUS);
			if (productOrder != null) {
				orderDto.setOrderId(productOrder.getOrderId());
				quantity = productOrder.getQuantity() + 1;
				orderDto.setQuantity(quantity);
				orderDto.setTotalPrices(quantity * productOrder.getProductDetail().getPrices());
			} else {
				ProductDetailDto productDetail = productDetailService
						.getProductDetailById(orderDto.getProductDetail().getProductDetailId());
				orderDto.setQuantity(1);
				orderDto.setTotalPrices(productDetail.getPrices());
			}
		}
		if (orderDto.getOrderType() == Constants.TYPEHEADPHONE) {
			productOrder = productOrderRepository.findByHeadPhoneHeadPhoneIdAndUserOrderUserIdAndDeleteFlagAndStatus(
					orderDto.getHeadPhone().getHeadPhoneId(), orderDto.getUserOrder().getUserId(),
					Constants.DELETE_FLAG, Constants.ORDER_STATUS);
			if (productOrder != null) {
				orderDto.setOrderId(productOrder.getOrderId());
				quantity = productOrder.getQuantity() + 1;
				orderDto.setQuantity(quantity);
				orderDto.setTotalPrices(quantity * productOrder.getHeadPhone().getPrices());
			} else {
				HeadPhoneDetailDto headPhone = headPhoneService.getById(orderDto.getHeadPhone().getHeadPhoneId());
				orderDto.setQuantity(1);
				orderDto.setTotalPrices(headPhone.getPrices());
			}

		}

		orderDto.setCreatedDate(createdDate);
		orderDto.setUpdatedDate(updatedDate);
		orderDto.setDeleteFlag(false);
		orderDto.setVoteFlag(false);
		orderDto.setStatus(Constants.ORDER_STATUS);

		productOrder = modelMapper.map(orderDto, ProductOrder.class);
		if (orderDto.getOrderType() == Constants.TYPEHEADPHONE) {
			productOrder.setProductDetail(null);
		}
		if (orderDto.getOrderType() == Constants.TYPEPHONE) {
			productOrder.setHeadPhone(null);
		}
		productOrder = productOrderRepository.save(productOrder);
		return modelMapper.map(productOrder, ProductOrderDto.class);
	}

	@Override
	public ProductOrderDto getOrderById(long orderId) {
		ProductOrderDto productOrder = null;
		try {
			ProductOrder productOrderEntity = productOrderRepository
					.findByOrderIdAndDeleteFlag(orderId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tồn tại"));

			productOrder = modelMapper.map(productOrderEntity, ProductOrderDto.class);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return productOrder;
	}

	@Override
	public ProductOrderDto updateProductOrder(ProductOrderDto orderDto) {
		ProductOrderDto productOrder = null;
		try {
			ProductOrder productOrderEntity = productOrderRepository
					.findByOrderIdAndDeleteFlag(orderDto.getOrderId(), Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tồn tại"));
			Date updatedDate = new Date();
			orderDto.setCreatedBy(productOrderEntity.getCreatedBy());
			orderDto.setCreatedDate(productOrderEntity.getCreatedDate());
			orderDto.setDeleteFlag(false);
			orderDto.setUpdatedDate(updatedDate);

			productOrderEntity = modelMapper.map(orderDto, ProductOrder.class);
			productOrderEntity = productOrderRepository.save(productOrderEntity);

			productOrder = modelMapper.map(productOrderEntity, ProductOrderDto.class);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return productOrder;
	}

	@Override
	public void updateQuantityOrder(long productOrderId, long invoiceId, String updatedBy, int quantity,
			int totalPrices) {

		try {
			productOrderRepository.findByOrderIdAndDeleteFlag(productOrderId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tồn tại"));
			Date updatedDate = new Date();
			productOrderRepository.updateQuantityOrder(Constants.PREPARE_STATUS, updatedDate, updatedBy, quantity,
					totalPrices, invoiceId, productOrderId);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
	}

	@Override
	public int cancelDelivery(String updatedBy, long invoiceId) {
		int cancelStatus = 0;
		try {
			ProductOrder invoice = productOrderRepository.findByOrderIdAndDeleteFlag(invoiceId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Hóa đơn không tồn tại"));
			if (invoice.getStatus() == Constants.PREPARE_STATUS) {
				Date updatedDate = new Date();

				productOrderRepository.updatedByStatus(Constants.CANCEL_STATUS, updatedDate, updatedBy, invoiceId);
			} else {
				cancelStatus = 1;
			}

		} catch (NotFoundException e) {

			e.printStackTrace();
		}

		return cancelStatus;
	}

	@Override
	public List<ProductOrderDto> getVoteProduct(String userName, int status) {

		List<ProductOrderDto> listInvoice = null;
		try {
			List<ProductOrder> listInvoiceEntity = productOrderRepository
					.findByUserOrderUserNameAndStatusAndDeleteFlag(userName, status, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy hóa đơn"));

			listInvoice = listInvoiceEntity.stream().map(invoice -> modelMapper.map(invoice, ProductOrderDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listInvoice;
	}

	@Override
	public ProductOrderDto reviewProduct(ProductOrderDto orderDto, long productId) {

		ProductOrderDto invoice = null;

		try {
			ProductOrder invoiceEntity = productOrderRepository
					.findByOrderIdAndDeleteFlag(orderDto.getOrderId(), Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Hóa đơn không tồn tại"));

			Date updatedDate = new Date();

			invoiceEntity.setUpdatedDate(updatedDate);
			invoiceEntity.setCustomerReview(orderDto.getCustomerReview());
			invoiceEntity.setCustomerVote(orderDto.getCustomerVote());
			invoiceEntity.setVoteFlag(true);

			invoiceEntity = productOrderRepository.save(invoiceEntity);

			if (invoiceEntity.getOrderType() == Constants.TYPEPHONE) {
				List<ProductOrder> listInvoiceEntity = productOrderRepository
						.findByProductDetailProductDetailId(productId)
						.orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm"));
				float vote = 0;
				int countCustomerVote = 0;
				int totalVote = 0;
				for (ProductOrder invoiceVote : listInvoiceEntity) {
					if (invoiceVote.getCustomerVote() > 0) {
						countCustomerVote++;
						totalVote += invoiceVote.getCustomerVote();
					}
				}
				vote = (float) totalVote / countCustomerVote;
				productDetailService.updateVoteProductDetail(vote, productId);
			}
			if (invoiceEntity.getOrderType() == Constants.TYPEHEADPHONE) {
				List<ProductOrder> listInvoiceEntity = productOrderRepository.findByHeadPhoneHeadPhoneId(productId)
						.orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm"));
				float vote = 0;
				int countCustomerVote = 0;
				int totalVote = 0;
				for (ProductOrder invoiceVote : listInvoiceEntity) {
					if (invoiceVote.getCustomerVote() > 0) {
						countCustomerVote++;
						totalVote += invoiceVote.getCustomerVote();
					}
				}
				vote = (float) totalVote / countCustomerVote;
				headPhoneService.updateVote(vote, productId);
			}

			invoice = modelMapper.map(invoiceEntity, ProductOrderDto.class);

		} catch (NotFoundException e) {

			e.printStackTrace();
		}

		return invoice;
	}

	@Override
	public List<ProductOrderDto> getReviewProduct(long productDetailId) {

		List<ProductOrderDto> listInvoice = null;
		try {
			List<ProductOrder> listInvoiceEntity = productOrderRepository
					.findByProductDetailProductDetailIdAndCustomerReviewIsNotNullOrderByUpdatedDateDesc(productDetailId)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy hóa đơn"));

			listInvoice = listInvoiceEntity.stream().map(invoice -> modelMapper.map(invoice, ProductOrderDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listInvoice;
	}

	@Override
	public List<ProductOrderDto> getCheckReviewProduct(String userName) {
		List<ProductOrderDto> listInvoice = null;
		try {

			List<ProductOrder> listInvoiceEntity = productOrderRepository
					.findByUserOrderUserNameAndVoteFlagAndStatusOrderByCreatedDateDesc(userName, false,
							Constants.SUCCESS_STATUS)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy hóa đơn"));

			listInvoice = listInvoiceEntity.stream().map(invoice -> modelMapper.map(invoice, ProductOrderDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listInvoice;
	}

	@Override
	public List<ProductOrderDto> getReviewHeadPhone(long headPhoneId) {
		List<ProductOrderDto> listInvoice = null;
		try {
			List<ProductOrder> listInvoiceEntity = productOrderRepository
					.findByHeadPhoneHeadPhoneIdAndCustomerReviewIsNotNullOrderByUpdatedDateDesc(headPhoneId)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy hóa đơn"));

			listInvoice = listInvoiceEntity.stream().map(invoice -> modelMapper.map(invoice, ProductOrderDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listInvoice;
	}

	@Override
	public List<ProductOrderDto> getInvoice(long invoiceId) {
		List<ProductOrderDto> listInvoice = null;
		try {
			List<ProductOrder> listInvoiceEntity = productOrderRepository
					.findByInvoiceInfoInvoiceIdAndDeleteFlagOrderByCreatedDateDesc(invoiceId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy hóa đơn"));

			listInvoice = listInvoiceEntity.stream().map(invoice -> modelMapper.map(invoice, ProductOrderDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listInvoice;
	}

	@Override
	public void updateSatus(long orderId, int status, String updatedBy) {

		try {
			ProductOrder productOrder = productOrderRepository
					.findByOrderIdAndDeleteFlag(orderId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tồn tại"));
			Date updatedDate = new Date();
			productOrderRepository.updatedByStatus(status, updatedDate, updatedBy, orderId);
			if (status == Constants.SUCCESS_STATUS && productOrder.getOrderType() == Constants.TYPEPHONE
					&& productOrder.getStatus() != Constants.SUCCESS_STATUS) {
				long productDetailId = productOrder.getProductDetail().getProductDetailId();
				ProductDetailDto productDetailDto = productDetailService.getProductDetailById(productDetailId);

				int quantity = productOrder.getQuantity() + productDetailDto.getQuantityExport();
				productDetailDto.setQuantityExport(quantity);
				productDetailDto.setUpdatedDate(updatedDate);
				productDetailDto.setUpdatedBy(updatedBy);

				productDetailService.updateProductDetail(productDetailDto);

			}
			if (status == Constants.SUCCESS_STATUS && productOrder.getOrderType() == Constants.TYPEHEADPHONE
					&& productOrder.getStatus() != Constants.SUCCESS_STATUS) {
				long headPhoneId = productOrder.getHeadPhone().getHeadPhoneId();
				HeadPhoneDetailDto headPhoneDto = headPhoneService.getById(headPhoneId);

				int quantity = productOrder.getQuantity() + headPhoneDto.getQuantityExport();
				headPhoneDto.setQuantityExport(quantity);
				headPhoneDto.setUpdatedDate(updatedDate);
				headPhoneDto.setUpdatedBy(updatedBy);

				headPhoneService.updateHeadPhone(headPhoneDto);

			}

			if (status == Constants.RETURN_STATUS && productOrder.getOrderType() == Constants.TYPEPHONE
					&& productOrder.getStatus() != Constants.RETURN_STATUS) {
				long productDetailId = productOrder.getProductDetail().getProductDetailId();
				ProductDetailDto productDetailDto = productDetailService.getProductDetailById(productDetailId);

				int quantity = productDetailDto.getQuantityExport() - productOrder.getQuantity();
				productDetailDto.setQuantityExport(quantity);
				productDetailDto.setUpdatedDate(updatedDate);
				productDetailDto.setUpdatedBy(updatedBy);

				productDetailService.updateProductDetail(productDetailDto);

			}
			if (status == Constants.RETURN_STATUS && productOrder.getOrderType() == Constants.TYPEHEADPHONE
					&& productOrder.getStatus() != Constants.RETURN_STATUS) {
				long headPhoneId = productOrder.getHeadPhone().getHeadPhoneId();
				HeadPhoneDetailDto headPhoneDto = headPhoneService.getById(headPhoneId);

				int quantity = headPhoneDto.getQuantityExport() - productOrder.getQuantity();
				headPhoneDto.setQuantityExport(quantity);
				headPhoneDto.setUpdatedDate(updatedDate);
				headPhoneDto.setUpdatedBy(updatedBy);

				headPhoneService.updateHeadPhone(headPhoneDto);

			}
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
	}

	@Override
	public ReportDto getDataReport(SearchDateDto searchDateDto) {
		Date startDate = null;
		Date endDate = null;
		ReportDto reportDto = new ReportDto();

		try {
			if (!StringUtils.isEmptyOrWhitespace(searchDateDto.getStartDate())) {
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchDateDto.getStartDate());
			}
			if (!StringUtils.isEmptyOrWhitespace(searchDateDto.getEndDate())) {
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchDateDto.getEndDate());
			}
			List<ProductOrder> listInvoice = productOrderRepository
					.findByUpdatedDateAndStatus(startDate, endDate, Constants.SUCCESS_STATUS)
					.orElseThrow(() -> new NotFoundException("Hoá đơn không tồn tại"));
			List<ProductDto> listProduct = productService.getAll();
			int total;
			int quantity;
			Map<String, Integer> salesData = new TreeMap<>();
			Map<String, Integer> revenueData = new TreeMap<>();

			int productType = Integer.parseInt(searchDateDto.getProductType().split(" ")[0]);
			for (ProductDto productDto : listProduct) {
				total = 0;
				quantity = 0;
				for (ProductOrder invoice : listInvoice) {

					if (productType == Constants.TYPEPHONE) {

						if (invoice.getOrderType() == Constants.TYPEPHONE && productDto.getProductName()
								.equals(invoice.getProductDetail().getProductInfo().getProductName())) {
							quantity += invoice.getQuantity();
							total += invoice.getTotalPrices();
						}
					}

					if (productType == Constants.TYPEHEADPHONE) {

						if (invoice.getOrderType() == Constants.TYPEHEADPHONE && productDto.getProductName()
								.equals(invoice.getHeadPhone().getProduct().getProductName())) {
							quantity += invoice.getQuantity();
							total += invoice.getTotalPrices();
						}
					}

					if (productType == Constants.TYPETOTAL) {

						if (invoice.getOrderType() == Constants.TYPEPHONE && productDto.getProductName()
								.equals(invoice.getProductDetail().getProductInfo().getProductName())) {
							quantity += invoice.getQuantity();
							total += invoice.getTotalPrices();
						}
						if (invoice.getOrderType() == Constants.TYPEHEADPHONE && productDto.getProductName()
								.equals(invoice.getHeadPhone().getProduct().getProductName())) {
							quantity += invoice.getQuantity();
							total += invoice.getTotalPrices();
						}
					}

				}

				salesData.put(productDto.getProductName(), quantity);
				revenueData.put(productDto.getProductName(), total);

			}

			reportDto.setRevenue(revenueData);
			reportDto.setSales(salesData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reportDto;
	}

}
