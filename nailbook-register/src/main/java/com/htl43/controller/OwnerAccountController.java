package com.htl43.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.htl43.model.Business;
import com.htl43.model.OwnerAccount;
import com.htl43.repos.BusinessDAO;
import com.htl43.repos.OwnerAccountDAO;
import com.htl43.service.OwnerAccountService;

@RestController
@RequestMapping(value = "/account")
public class OwnerAccountController {

	private OwnerAccountDAO ownerDao;
	private OwnerAccountService ownerService;
	
	private BusinessDAO businessDAO;

	@Autowired
	public OwnerAccountController(OwnerAccountDAO ownerDao, OwnerAccountService ownerService, BusinessDAO businessDAO) {
		super();
		this.ownerDao = ownerDao;
		this.ownerService = ownerService;
		this.businessDAO = businessDAO;
	}

	@PostMapping("/owner")
	public ResponseEntity addOwnerAccount(@RequestBody OwnerAccount ownerAccount) {
		ownerAccount.setPassword(ownerService.encryptPassword(ownerAccount.getPassword()));
		System.out.println("Post Method Body= " + ownerAccount);
		ownerDao.save(ownerAccount);
		return ResponseEntity.status(201).build();
	}
	
	@PostMapping("/business")
	public ResponseEntity addBusiness(@RequestBody Business business) {
		
		int ownerId = business.getOwner().getId();
		Optional<OwnerAccount> ownerOpt = ownerDao.findById(ownerId);
		OwnerAccount owner = null;
		if(ownerOpt.isPresent()) {
			owner = ownerOpt.get();
			business.setOwner(owner);
			System.out.println("Business Post Method Body= " + business);
			businessDAO.save(business);
			return ResponseEntity.status(201).build();
		} else {
			return ResponseEntity.status(404).build();
		}	
	}
	
//	@GetMapping("/owner/{email}")
//	public ResponseEntity<OwnerAccount> findByEmail(@PathVariable String email) {
//		System.out.println("Get Request Owner by email= " + email);
//		Optional<OwnerAccount> ownerOpt = ownerDao.findOwnerByEmail(email);
//		OwnerAccount owner = null;
//		if(ownerOpt.isPresent()) {
//			owner = ownerOpt.get();
//			return ResponseEntity.status(200).body(owner);
//		} else {
//			return ResponseEntity.status(404).build();
//		}
//		
//	}
	
	@GetMapping("/owner/{id}")
	public ResponseEntity<OwnerAccount> findOwnerById(@PathVariable int id) {
		System.out.println("Get Request Owner by Id= " + id);
		Optional<OwnerAccount> ownerOpt = ownerDao.findById(id);
		OwnerAccount owner = null;
		if(ownerOpt.isPresent()) {
			owner = ownerOpt.get();
			return ResponseEntity.status(200).body(owner);
		} else {
			return ResponseEntity.status(404).build();
		}
		
	}
}