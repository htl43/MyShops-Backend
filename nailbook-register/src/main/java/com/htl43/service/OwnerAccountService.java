package com.htl43.service;

import org.springframework.stereotype.Service;

@Service
public class OwnerAccountService {
	
	public String encryptPassword(String password) {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return Integer.toString(result);
	}

	
	
	
}
