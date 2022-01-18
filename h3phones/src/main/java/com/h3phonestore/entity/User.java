
package com.h3phonestore.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;

	private String userName;

	private String passWords;

	private String fullName;

	private String email;

	private String phoneNumbers;

	private String address;

	private String createdBy;

	Date createdDate;

	String updatedBy;

	Date updatedDate;

	boolean deleteFlag;

	@ManyToOne
	@JoinColumn(name = "roleId")
	private Role roleInfo;

	@OneToMany(mappedBy = "userOrder")
	private List<ProductOrder> orderInfos;

	@OneToMany(mappedBy = "userContact")
	private List<CustomerContact> userContacts;

}
