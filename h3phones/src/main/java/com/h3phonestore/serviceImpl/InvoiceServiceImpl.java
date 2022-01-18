package com.h3phonestore.serviceImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.h3phonestore.constants.Constants;
import com.h3phonestore.dto.HeadPhoneDetailDto;
import com.h3phonestore.dto.InvoiceDto;
import com.h3phonestore.dto.ProductDetailDto;
import com.h3phonestore.dto.ProductOrderDto;
import com.h3phonestore.dto.SearchDateDto;
import com.h3phonestore.entity.Invoice;
import com.h3phonestore.entity.ProductOrder;
import com.h3phonestore.repository.InvoiceRepository;
import com.h3phonestore.service.HeadPhoneService;
import com.h3phonestore.service.InvoiceService;
import com.h3phonestore.service.ProductDetailService;
import com.h3phonestore.service.ProductOrderService;

import javassist.NotFoundException;
import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {
	@Autowired
	private InvoiceRepository invoiceRepository;
	@Autowired
	private ProductOrderService productOrderService;
	@Autowired
	private ProductDetailService productDetailService;

	@Autowired
	private HeadPhoneService headPhoneService;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public List<InvoiceDto> getInvoiceByUser(String userName) {
		List<InvoiceDto> listInvoice = null;
		try {
			List<Invoice> listInvoiceEntity = invoiceRepository
					.findByOrderInfoUserOrderUserNameAndDeleteFlag(userName, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy hóa đơn"));

			listInvoice = listInvoiceEntity.stream().map(invoice -> modelMapper.map(invoice, InvoiceDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listInvoice;
	}

	@Override
	public InvoiceDto createInvoice(InvoiceDto invoiceDto) {

		Date createdDate = new Date();
		Date updatedDate = new Date();
		String invoiceCode;
		int i = 0;
		Date deliveryDate = createdDate;
		Calendar c = Calendar.getInstance();
		c.setTime(deliveryDate);
		c.add(Calendar.DATE, 4);
		deliveryDate = c.getTime();

		String pattern = "dd/MM/yyyy";

		DateFormat df = new SimpleDateFormat(pattern);
		String delivery = df.format(deliveryDate);

		int totalPrices = invoiceDto.getOrderInfo().get(i).getTotalPrices() + Constants.SHIP;
		invoiceDto.getOrderInfo().get(i).setTotalPrices(totalPrices);

		invoiceCode = RandomString.make(12);

		invoiceDto.setVoteFlag(false);

		invoiceDto.setCreatedDate(createdDate);
		invoiceDto.setUpdatedDate(updatedDate);
		invoiceDto.setInvoiceCode(invoiceCode.toUpperCase());
		invoiceDto.setDeleteFlag(Constants.DELETE_FLAG);
		invoiceDto.setDeliveryTime(delivery);
		invoiceDto.setStatus(1);

		int totalInvoice = 0;
		for (ProductOrderDto productOrder : invoiceDto.getOrderInfo()) {
			totalInvoice += productOrder.getTotalPrices();
		}

		invoiceDto.setTotalInvoice(totalInvoice);

		Invoice invoiceEntity = modelMapper.map(invoiceDto, Invoice.class);
		invoiceEntity = invoiceRepository.save(invoiceEntity);

		for (ProductOrderDto productOrder : invoiceDto.getOrderInfo()) {
			productOrderService.updateQuantityOrder(productOrder.getOrderId(), invoiceEntity.getInvoiceId(),
					invoiceDto.getCreatedBy(), productOrder.getQuantity(), productOrder.getTotalPrices());

		}

		return modelMapper.map(invoiceEntity, InvoiceDto.class);
	}

	@Override
	public InvoiceDto updateInvocie(InvoiceDto invoiceDto) {
		InvoiceDto invoice = null;

		try {
			Invoice invoiceEntity = invoiceRepository
					.findByInvoiceIdAndDeleteFlag(invoiceDto.getInvoiceId(), Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Hóa đơn không tồn tại"));

			Date updatedDate = new Date();

			invoiceEntity.setUpdatedDate(updatedDate);
			invoiceEntity.setUpdatedBy(invoiceDto.getUpdatedBy());
			invoiceEntity.setStatus(invoiceDto.getStatus());
			invoiceEntity.setDeliveryTime(invoiceDto.getDeliveryTime());

			for (ProductOrder productOrder : invoiceEntity.getOrderInfo()) {
				
				productOrderService.updateSatus(productOrder.getOrderId(), invoiceEntity.getStatus(),
						invoiceEntity.getUpdatedBy());
			}

			invoiceEntity = invoiceRepository.save(invoiceEntity);

			invoice = modelMapper.map(invoiceEntity, InvoiceDto.class);

		} catch (NotFoundException e) {

			e.printStackTrace();
		}

		return invoice;
	}

	@Override
	public void deleteInvoice(long invoiceId, String updatedBy) {
		try {
			Invoice invoice = invoiceRepository.findByInvoiceIdAndDeleteFlag(invoiceId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Hóa đơn không tồn tại"));
			Date updatedDate = new Date();

			invoiceRepository.deleteByDeleteFlag(Constants.DELETED_FLAG, updatedDate, updatedBy, invoiceId);

			for (ProductOrder productOrder : invoice.getOrderInfo()) {
				productOrderService.deleteProductOrder(productOrder.getOrderId(), updatedBy);
			}

		} catch (NotFoundException e) {

			e.printStackTrace();
		}

	}

	@Override
	public InvoiceDto getInvoiceById(long invoiceId) {
		InvoiceDto invoice = null;
		try {
			Invoice invoiceEntity = invoiceRepository.findByInvoiceIdAndDeleteFlag(invoiceId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy hóa đơn"));
			invoice = modelMapper.map(invoiceEntity, InvoiceDto.class);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return invoice;
	}

	@Override
	public List<InvoiceDto> getAll() {

		List<InvoiceDto> listInvoice = null;
		try {

			List<Invoice> listInvoiceEntity = invoiceRepository
					.findByDeleteFlagOrderByCreatedDateDesc(Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy hóa đơn"));

			listInvoice = listInvoiceEntity.stream().map(invoice -> modelMapper.map(invoice, InvoiceDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listInvoice;
	}

	@Override
	public List<InvoiceDto> getByKeyWords(String keyWords) {
		List<InvoiceDto> listInvoice = null;
		try {

			List<Invoice> listInvoiceEntity = invoiceRepository
					.findDistinctByCustomerContainingOrOrderInfoUserOrderPhoneNumbersContainingOrInvoiceCodeContaining(
							keyWords, keyWords, keyWords)
					.orElseThrow(() -> new NotFoundException("Không tìm thấy hóa đơn"));

			listInvoice = listInvoiceEntity.stream().map(invoice -> modelMapper.map(invoice, InvoiceDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listInvoice;
	}

	@Override
	public List<InvoiceDto> filterInvoice(SearchDateDto searchDto) {
		Date startDate = null;
		Date endDate = null;
		List<InvoiceDto> listInvoice = null;
		;
		List<Invoice> listInvoiceEntity = null;
		try {
			if (!StringUtils.isEmptyOrWhitespace(searchDto.getStartDate())) {

				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchDto.getStartDate());

			}
			if (!StringUtils.isEmptyOrWhitespace(searchDto.getEndDate())) {

				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchDto.getEndDate());

			}

			if (searchDto.getStatus() >= 0) {

				listInvoiceEntity = invoiceRepository
						.findByCreatedDateAndStatus(startDate, endDate, searchDto.getStatus())
						.orElseThrow(() -> new NotFoundException("Hoá đơn không tồn tại"));

			} else {

				listInvoiceEntity = invoiceRepository.findByCreatedDate(startDate, endDate)
						.orElseThrow(() -> new NotFoundException("Hoá đơn không tồn tại"));

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		listInvoice = listInvoiceEntity.stream().map(invoice -> modelMapper.map(invoice, InvoiceDto.class))
				.collect(Collectors.toList());
		return listInvoice;
	}

}