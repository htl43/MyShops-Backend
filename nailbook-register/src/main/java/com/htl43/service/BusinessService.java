package com.htl43.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htl43.exception.BusinessException;
import com.htl43.model.Business;
import com.htl43.model.OwnerAccount;
import com.htl43.repos.BusinessDAO;

@Service
@Transactional
public class BusinessService {
	
	private BusinessDAO businessDAO;
	private OwnerAccountService ownerService;

	@Autowired
	public BusinessService(BusinessDAO businessDAO, OwnerAccountService ownerService) {
		super();
		this.businessDAO = businessDAO;
		this.ownerService = ownerService;
	}
	
	public Business addOwnerBusiness(Business business) throws BusinessException{
		
		int ownerId = business.getOwner().getId();

		try {
			OwnerAccount owner = ownerService.findOwnerById(ownerId);
			business.setOwner(owner);
			Business confirmBusiness = businessDAO.save(business);
			if(confirmBusiness!=null) {
				return confirmBusiness;
			} else {
				throw new BusinessException("Sorry! System can't save this business");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}	
	}

	public void removeOwnerBusiness(Business business) throws BusinessException {
		if(businessDAO.existsById(business.getId())) {
			businessDAO.deleteById(business.getId());
		} else {
			throw new BusinessException("No business associate with the id = " + business.getId());
		}
	}
	
	public Business findBusinessById(int id) throws BusinessException {
		Optional<Business> getBusiness = businessDAO.findById(id);
		Business business = null;
		if(getBusiness.isPresent()) {
			business = getBusiness.get();
			return business;
		} else {
			throw new BusinessException("No Account is associated with the id=" + id);
		}
		
	}
	
	public Business findBusinessByAddress(String address) throws BusinessException {
		Optional<Business> getBusiness = businessDAO.findByAddress(address);
		Business business = null;
		if(getBusiness.isPresent()) {
			business = getBusiness.get();		
		} 
		return business;
	}

	public void updateBusinessByFieldChange(Business business) {
		try {
			Business getBusiness = findBusinessById(business.getId());
			if(!(business.getName()==null || business.getName().isEmpty())) {			
				getBusiness.setName(business.getName());
		
			}
			if(!(business.getType()==null || business.getType().isEmpty())) {			
				getBusiness.setType(business.getType());
		
			}
			if(!(business.getPhone()==null || business.getPhone().isEmpty())) {			
				getBusiness.setPhone(business.getPhone());
		
			}
			if(!(business.getStatus()==null || business.getStatus().isEmpty())) {			
				getBusiness.setStatus(business.getStatus());
		
			}
			if(!(business.getDeactivated_date()==null)) {			
				getBusiness.setDeactivated_date(business.getDeactivated_date());
		
			}
			if(!(business.getAddress()==null || business.getAddress().isEmpty())) {			
				if(findBusinessByAddress(business.getAddress()) != null) {
					getBusiness.setAddress(business.getAddress());
				} else {
					throw new BusinessException("A business is exsited with this address: " + business.getAddress());
				}
			}
		} 
		catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		
	}
	
}
