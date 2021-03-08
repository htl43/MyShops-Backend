package com.htl43.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.htl43.exception.BusinessException;
import com.htl43.model.Business;
import com.htl43.model.LoginDTO;
import com.htl43.model.OwnerAccount;
import com.htl43.service.BusinessService;
import com.htl43.service.OwnerAccountService;

class AccountControllerTest {
	
	@Mock
	private OwnerAccountService ownerService;
	@Mock
	private BusinessService businessService;
	
	@InjectMocks
	private AccountController accountController;
	
	OwnerAccount ownerAccount = new OwnerAccount(1, "htl43@gmail.com", "267-731-4433", "Abc123", "Henry", "Pat");
	
	Business business1 = new Business(1, "Top Nails", "Beauty Salon", "200 N Broad, Philadelphia, PA, 19145", "211-456-1923", "activate",
			Date.valueOf("2021-01-01"), Date.valueOf("2021-07-01"), ownerAccount);
	
	Business business2 = new Business(2, "Best Food", "Restaurant", "135 W Eagle, Ardmore, PA, 19003", "267-666-7878", "activate",
			Date.valueOf("2021-03-01"), Date.valueOf("2025-07-01"), ownerAccount);
	
	
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testRegisterOwnerAccount() {
		when(ownerService.saveCustomerAccount(ownerAccount)).thenReturn(ownerAccount);
		ResponseEntity resp = accountController.registerOwnerAccount(ownerAccount);
		verify(ownerService, times(1)).saveCustomerAccount(ownerAccount);
		assertNotEquals(ownerAccount.getPassword(), "Abc123");
		assertEquals(HttpStatus.valueOf(201), resp.getStatusCode());
		assertEquals(ownerAccount, resp.getBody());

		OwnerAccount invalidAccount = new OwnerAccount(2, null, "267-731-4433", "Abc123", "Henry", "Pat");
		doThrow(BusinessException.class).when(ownerService).saveCustomerAccount(invalidAccount);
		resp = accountController.registerOwnerAccount(invalidAccount);
		verify(ownerService, times(1)).saveCustomerAccount(invalidAccount);;
		assertEquals(HttpStatus.valueOf(409), resp.getStatusCode());
		
	}

	@Test
	void testUpdateOwnerAccount() {
		OwnerAccount updateAccount = new OwnerAccount(1, "Tom2021@yahoo.com", "610-989-4356", "WTO999", "Tom", "Lee");
		ArgumentCaptor<OwnerAccount> saveCapture = ArgumentCaptor.forClass(OwnerAccount.class);
		doNothing().when(ownerService).updateAccountByFieldChange(saveCapture.capture());
		ResponseEntity resp = accountController.updateOwnerAccount(updateAccount);
		verify(ownerService, times(1)).updateAccountByFieldChange(updateAccount);
		assertEquals(HttpStatus.valueOf(202), resp.getStatusCode());
		assertEquals(updateAccount, saveCapture.getValue());
		updateAccount.setFirstname(null);
		doThrow(BusinessException.class).when(ownerService).updateAccountByFieldChange(updateAccount);
		resp = accountController.updateOwnerAccount(updateAccount);
		assertEquals(HttpStatus.valueOf(406), resp.getStatusCode());
	}

	@Test
	void testDeleteOwnerAccount() {
		ArgumentCaptor<OwnerAccount> saveCapture = ArgumentCaptor.forClass(OwnerAccount.class);
		doNothing().when(ownerService).deleteOwnerAccount(saveCapture.capture());
		ResponseEntity resp = accountController.deleteOwnerAccount(ownerAccount);
		verify(ownerService, times(1)).deleteOwnerAccount(ownerAccount);
		assertEquals(HttpStatus.valueOf(202), resp.getStatusCode());
		assertEquals(ownerAccount, saveCapture.getValue());
		// Verify if the account has been deleted
		doThrow(BusinessException.class).when(ownerService).deleteOwnerAccount(ownerAccount);
		ResponseEntity newResp = accountController.deleteOwnerAccount(ownerAccount);
		assertEquals(HttpStatus.valueOf(406), newResp.getStatusCode());
	}

	@Test
	void testAddBusiness() {
		// Test add first bussiness
		when(businessService.addOwnerBusiness(business1)).thenReturn(business1);
		ResponseEntity resp = accountController.addBusiness(business1);
		verify(businessService, times(1)).addOwnerBusiness(business1);
		assertEquals(business1, resp.getBody());
		assertEquals(HttpStatus.valueOf(201), resp.getStatusCode());
		
		// Test add second bussiness
		when(businessService.addOwnerBusiness(business2)).thenReturn(business2);
		resp = accountController.addBusiness(business2);
		verify(businessService, times(1)).addOwnerBusiness(business2);
		assertEquals(business2, resp.getBody());
		assertEquals(HttpStatus.valueOf(201), resp.getStatusCode());
		
		// Test reject add business have same address
		Business business3 = new Business(3, "Perfect Nails", "Beauty Salon", null, "200-000-1111", "deactivate",
				Date.valueOf("2021-01-01"), Date.valueOf("2021-07-01"), ownerAccount);
		business3.setAddress(business1.getAddress());
		when(businessService.addOwnerBusiness(business3)).thenThrow(new BusinessException("A business with same address is already added"));
		resp = accountController.addBusiness(business3);
		verify(businessService, times(1)).addOwnerBusiness(business3);
		assertEquals(HttpStatus.valueOf(409), resp.getStatusCode());
		assertEquals(resp.getBody(), "A business with same address is already added");
	}

	@Test
	void testRemoveBusiness() {
		ArgumentCaptor<Business> saveCapture = ArgumentCaptor.forClass(Business.class);
		doNothing().when(businessService).removeOwnerBusiness(saveCapture.capture());
		ResponseEntity resp = accountController.removeBusiness(business1);
		verify(businessService, times(1)).removeOwnerBusiness(business1);
		assertEquals(HttpStatus.valueOf(202), resp.getStatusCode());
		assertEquals(business1, saveCapture.getValue());
		// Verify if the business has been deleted
		doThrow(new BusinessException("Can't delete")).when(businessService).removeOwnerBusiness(business1);;
		ResponseEntity newResp = accountController.removeBusiness(business1);
		assertEquals(HttpStatus.valueOf(409), newResp.getStatusCode());
		assertEquals(newResp.getBody(), "Can't delete");
	}

	@Test
	void testUpdateBusiness() {
		business1.setName("Super Nails");
		business1.setPhone("611-435-2235");
		ArgumentCaptor<Business> saveCapture = ArgumentCaptor.forClass(Business.class);
		doNothing().when(businessService).updateBusinessByFieldChange(saveCapture.capture());
		ResponseEntity resp = accountController.updateBusiness(business1);
		verify(businessService, times(1)).updateBusinessByFieldChange(business1);
		assertEquals(HttpStatus.valueOf(202), resp.getStatusCode());
		assertEquals(business1, saveCapture.getValue());
		business1.setAddress(null);
		doThrow(BusinessException.class).when(businessService).updateBusinessByFieldChange(business1);
		resp = accountController.updateBusiness(business1);
		assertEquals(HttpStatus.valueOf(406), resp.getStatusCode());
	}
	@Test
	void testLoginOwnerAccount() {
		LoginDTO loginDTO = new LoginDTO("htl43@gmail.com", "Abc123");
		when(ownerService.getAccountByLoginDTO(loginDTO)).thenReturn(ownerAccount);
		ResponseEntity resp = accountController.loginOwnerAccount(loginDTO);
		assertEquals(HttpStatus.valueOf(200), resp.getStatusCode());
		assertEquals(ownerAccount, resp.getBody());
		assertEquals("", ((OwnerAccount) resp.getBody()).getPassword());
		LoginDTO invalidLogin = new LoginDTO("invalid@gmail.com", "Abc123");
		doThrow(new BusinessException("Invalid login")).when(ownerService).getAccountByLoginDTO(invalidLogin);
		resp = accountController.loginOwnerAccount(invalidLogin);
		assertEquals(HttpStatus.valueOf(403), resp.getStatusCode());
		assertEquals(resp.getBody(), "Invalid login");	
	}

}
