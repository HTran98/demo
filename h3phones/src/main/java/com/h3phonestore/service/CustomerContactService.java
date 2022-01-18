package com.h3phonestore.service;

import java.util.List;

import com.h3phonestore.dto.CustomerContactDto;

public interface CustomerContactService {
	List<CustomerContactDto> getAll();

	void delete(long contactId, String updatedBy);

	CustomerContactDto createContact(CustomerContactDto contactDto);

	List<CustomerContactDto> findByKeyWords(String keyWords);
}
