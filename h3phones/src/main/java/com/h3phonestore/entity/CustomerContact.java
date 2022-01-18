package com.h3phonestore.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="customerContact")
@NoArgsConstructor
@AllArgsConstructor
public class CustomerContact {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long contactId;

	private String customerName;

	private String email;

	private String phoneNumbers;

	private String createdBy;
	
	private String contactContent;

	Date createdDate;

	String updatedBy;

	Date updatedDate;

	boolean deleteFlag;
	
	@ManyToOne
	@JoinColumn(name ="userContactId")
	private User userContact;
}
