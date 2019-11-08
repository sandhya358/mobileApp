/**
 * Author: Sandhya Tummala
 * Class: This class services the API requests
 */
package com.appsdeveloperblog.app.ws.service.impl;

import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.UserRepository;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {
	
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	Utils utils;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * @UserDto is user entity comes from the controller Method to create the user
	 *          in database
	 */
	@Override
	public UserDto createUser(UserDto user) {
		if (userRepository.findByEmail(user.getEmail()) != null)
			throw new RuntimeException("record already exists");// duplicate rows

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);// create public userid
		userEntity.setEncryptedpassword(bCryptPasswordEncoder.encode(user.getPassword()));// encode password
		UserEntity storedUserDetails = userRepository.save(userEntity);

		UserDto returnValue = new UserDto();

		BeanUtils.copyProperties(storedUserDetails, returnValue);

		System.out.println("Value form the service layer " + returnValue.getUserId());

		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
