package com.htl43.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedRuntimeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htl43.exception.BusinessException;
import com.htl43.model.Business;
import com.htl43.model.LoginDTO;
import com.htl43.model.OwnerAccount;
import com.htl43.repos.OwnerAccountDAO;

@Service
@Transactional
public class OwnerAccountService {
	
	private OwnerAccountDAO ownerDao;
	
	@Autowired
	public OwnerAccountService(OwnerAccountDAO ownerDao) {
		super();
		this.ownerDao = ownerDao;
	}

	public String encryptPassword(String password) {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return Integer.toString(result);
	}

	public OwnerAccount saveCustomerAccount(OwnerAccount ownerAccount) throws BusinessException {		
		try {
			OwnerAccount confirmAccount = ownerDao.save(ownerAccount);
			if(confirmAccount!=null) {
				return confirmAccount;
			} else {
				throw new BusinessException("Sorry! System can't save owner account");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		
		
	}
	
	public OwnerAccount findOwnerById(int id) throws BusinessException {
		Optional<OwnerAccount> ownerOpt = ownerDao.findById(id);
		OwnerAccount owner = null;
		if(ownerOpt.isPresent()) {
			owner = ownerOpt.get();
			return owner;
		} else {
			throw new BusinessException("No Account is associated with the id=" + id);
		}
		
	}
	
	public OwnerAccount getAccountByLoginDTO(LoginDTO ownerLogin) throws BusinessException {
		ownerLogin.setPassword(encryptPassword(ownerLogin.getPassword()));
		Optional<OwnerAccount> ownerOpt = ownerDao.findOwnerByEmailAndPassword(ownerLogin.getEmail(), ownerLogin.getPassword());
		OwnerAccount owner = null;
		if(ownerOpt.isPresent()) {
			owner = ownerOpt.get();
			return owner;
		} else {
			throw new BusinessException("No account is associated with email and password");
		}
	
	}
	
	public OwnerAccount findOwnerAccountByEmail(String email) {
		Optional<OwnerAccount> ownerOpt = ownerDao.findByEmail(email);
		OwnerAccount owner = null;
		if(ownerOpt.isPresent()) {
			owner = ownerOpt.get();
			
		} 
		return owner;
	}
	
	public void updateAccountByFieldChange(OwnerAccount ownerAccount) throws BusinessException {
	
		try {
			OwnerAccount getOwnerAccount = findOwnerById(ownerAccount.getId());
			if(!(ownerAccount.getPassword()==null || ownerAccount.getPassword().isEmpty())) {
				ownerAccount.setPassword(encryptPassword(ownerAccount.getPassword()));
				if(!ownerAccount.getPassword().equals(getOwnerAccount.getPassword())) {
					getOwnerAccount.setPassword(ownerAccount.getPassword());
				} else {
					throw new BusinessException("The new password can't be same with old one");
				}
			}
			if(!(ownerAccount.getPhone()==null || ownerAccount.getPhone().isEmpty())) {
				getOwnerAccount.setPhone(ownerAccount.getPhone());
			}
			if(!(ownerAccount.getFirstname()==null || ownerAccount.getFirstname().isEmpty())) {				 
				getOwnerAccount.setFirstname(ownerAccount.getFirstname());

			}
			if(!(ownerAccount.getLastname()==null || ownerAccount.getLastname().isEmpty())) {		
				getOwnerAccount.setLastname(ownerAccount.getLastname());		
			}
			if(!(ownerAccount.getEmail()==null || ownerAccount.getEmail().isEmpty())) {			
				if(findOwnerAccountByEmail(ownerAccount.getEmail()) != null) {
					getOwnerAccount.setEmail(ownerAccount.getEmail());
				} else {
					throw new BusinessException("The email: " + ownerAccount.getEmail() + " is aldready existed");
				}
			}
		} 
		catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public void deleteOwnerAccount(OwnerAccount ownerAccount) throws BusinessException{
		if(!ownerDao.existsById(ownerAccount.getId())) {
			throw new BusinessException("No Owner Account exist with id=" + ownerAccount.getId());
		}
		try {
			ownerDao.deleteById(ownerAccount.getId());
		}
		catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}		
	}

}
