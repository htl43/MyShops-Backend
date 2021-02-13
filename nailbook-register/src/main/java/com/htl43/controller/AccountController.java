package com.htl43.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.htl43.exception.BusinessException;
import com.htl43.model.Business;
import com.htl43.model.LoginDTO;
import com.htl43.model.OwnerAccount;
import com.htl43.service.BusinessService;
import com.htl43.service.OwnerAccountService;

@RestController
@RequestMapping(value = "/account")
public class AccountController {

	private OwnerAccountService ownerService;
	private BusinessService businessService;

	@Autowired
	public AccountController(OwnerAccountService ownerService, BusinessService businessService) {
		super();
		this.ownerService = ownerService;
		this.businessService = businessService;
	}

	@PostMapping("/owner/register")
	public ResponseEntity registerOwnerAccount(@RequestBody OwnerAccount ownerAccount) {
		try {
			ownerAccount.setPassword(ownerService.encryptPassword(ownerAccount.getPassword()));
			ownerService.saveCustomerAccount(ownerAccount);
			return ResponseEntity.status(201).build();
		} catch (BusinessException e) {
			return ResponseEntity.status(409).body(e.getMessage());
		}
		
		
	}
	
	@GetMapping("/owner/id/{id}")
	public ResponseEntity getAccountByOwnerId(@PathVariable int id) {
		System.out.println("getting owner by id " + id);
		try {
			OwnerAccount ownerAccount = ownerService.findOwnerById(id);
			return ResponseEntity.status(200).body(ownerAccount);
		} catch (BusinessException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
		
	}
	
	
	@PutMapping("/owner/update")
	public ResponseEntity updateOwnerAccount(@RequestBody OwnerAccount ownerAccount) {	
		System.out.println("getting owner delete request" + ownerAccount);
		try {
			ownerService.updateAccountByFieldChange(ownerAccount);		
			return ResponseEntity.status(202).build();
		} catch (BusinessException e) {
			return ResponseEntity.status(406).body(e.getMessage());
		}	
	}
	
	@DeleteMapping("/owner/delete")
	public ResponseEntity deleteOwnerAccount(@RequestBody OwnerAccount ownerAccount) {		
		try {
			ownerService.deleteOwnerAccount(ownerAccount);
			return ResponseEntity.status(202).build();
		} catch (BusinessException e) {
			return ResponseEntity.status(406).body(e.getMessage());
		}	
	}
	
	@PostMapping("/business/add")
	public ResponseEntity addBusiness(@RequestBody Business business) {		
		try {
			businessService.addOwnerBusiness(business);
			return ResponseEntity.status(201).build();
		} catch (BusinessException e) {
			return ResponseEntity.status(409).body(e.getMessage());
		}	
	}
	
	@DeleteMapping("/business/remove")
	public ResponseEntity removeBusiness(@RequestBody Business business) {
		System.out.println("getting business remove request" + business);
		try {
			businessService.removeOwnerBusiness(business);
			return ResponseEntity.status(202).build();
		} catch (BusinessException e) {
			return ResponseEntity.status(409).body(e.getMessage());
		}	
	}
	
	@PatchMapping("/business/update")
	public ResponseEntity updateBusiness(@RequestBody Business business) {	
		System.out.println("getting owner delete request" + business);
		try {
			businessService.updateBusinessByFieldChange(business);
			return ResponseEntity.status(202).build();
		} catch (BusinessException e) {
			return ResponseEntity.status(406).body(e.getMessage());
		}	
	}
	
	@GetMapping("/login")
	public ResponseEntity loginOwnerAccount(@RequestBody LoginDTO ownerLogin) {
		try {
			OwnerAccount ownerAccount = ownerService.getAccountByLoginDTO(ownerLogin);
			ownerAccount.setPassword("");
			return ResponseEntity.status(200).body(ownerAccount);
		} catch (BusinessException e) {
			return ResponseEntity.status(403).body(e.getMessage());
		}
		
	}
	
}