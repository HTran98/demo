package com.h3phonestore.service;

import java.util.List;

import com.h3phonestore.dto.InvoiceDto;
import com.h3phonestore.dto.SearchDateDto;

public interface InvoiceService {

	List<InvoiceDto> getAll();

	List<InvoiceDto> getInvoiceByUser(String userName);

	InvoiceDto createInvoice(InvoiceDto invoice);

	InvoiceDto updateInvocie(InvoiceDto invoiceDto);

	void deleteInvoice(long invoiceId, String updatedBy);

	InvoiceDto getInvoiceById(long invoiceId);

	List<InvoiceDto> getByKeyWords(String keyWords);

	List<InvoiceDto> filterInvoice(SearchDateDto searchDto);
	
}
