package com.h3phonestore.serviceImpl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.h3phonestore.entity.UserDetailCustom;
import com.h3phonestore.constants.Constants;
import com.h3phonestore.entity.User;
import com.h3phonestore.repository.UserRepository;

import javassist.NotFoundException;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = null;
		try {
			user = userRepository.findByUserNameAndDeleteFlag(username, Constants.DELETE_FLAG)
					.orElseThrow(() -> new NotFoundException("User không tồn tại"));
		} catch (NotFoundException e) {
			
			e.printStackTrace();
		}

		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserDetailCustom(user);
	}

	@Transactional
	public UserDetails loadUserById(Long id) throws NotFoundException {
		User user = userRepository.findById(id).get();

		return new UserDetailCustom(user);
	}

}
