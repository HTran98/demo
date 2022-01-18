package com.h3phonestore.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h3phonestore.constants.Constants;
import com.h3phonestore.dto.CustomerContactDto;
import com.h3phonestore.entity.CustomerContact;
import com.h3phonestore.repository.CustomerContactRepository;
import com.h3phonestore.service.CustomerContactService;

import javassist.NotFoundException;

@Service
@Transactional
public class CustomerContactServiceImpl implements CustomerContactService {

	@Autowired
	CustomerContactRepository contactRepository;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public List<CustomerContactDto> getAll() {
		List<CustomerContactDto> listContact = null;
		try {
			List<CustomerContact> listContactEntity = contactRepository.findByDeleteFlag(Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Không có liên hệ nào"));
			listContact = listContactEntity.stream().map(contact -> modelMapper.map(contact, CustomerContactDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listContact;
	}

	@Override
	public void delete(long contactId, String updatedBy) {
		try {
			contactRepository.findByContactIdAndDeleteFlag(contactId, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("Liên hệ không tòn tại"));
			Date updatedDate = new Date();
			contactRepository.deleteByDeleteFlag(Constants.DELETED_FLAG, updatedDate, updatedBy, contactId);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}

	}

	@Override
	public CustomerContactDto createContact(CustomerContactDto contactDto) {

		Date createdDate = new Date();
		Date updatedDate = new Date();
		contactDto.setCreatedDate(createdDate);
		contactDto.setUpdatedDate(updatedDate);
		CustomerContact customerContact = modelMapper.map(contactDto, CustomerContact.class);
		customerContact = contactRepository.save(customerContact);

		return modelMapper.map(customerContact, CustomerContactDto.class);
	}

	@Override
	public List<CustomerContactDto> findByKeyWords(String keyWords) {

		List<CustomerContactDto> listContact = null;
		try {
			List<CustomerContact> listContactEntity = contactRepository
					.findByUserContactUserNameContainingOrEmailContainingOrPhoneNumbersContaining(keyWords, keyWords,
							keyWords)
					.orElseThrow(() -> new NotFoundException("Không có liên hệ nào"));
			listContact = listContactEntity.stream().map(contact -> modelMapper.map(contact, CustomerContactDto.class))
					.collect(Collectors.toList());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
		return listContact;
	}

}
