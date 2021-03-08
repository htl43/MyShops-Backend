package com.htl43.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.hibernate.query.criteria.internal.expression.SearchedCaseExpression.WhenClause;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito.Then;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.htl43.exception.BusinessException;
import com.htl43.model.LoginDTO;
import com.htl43.model.OwnerAccount;
import com.htl43.repos.OwnerAccountDAO;

class OwnerAccountServiceTest {
	
	OwnerAccount ownerAccount = new OwnerAccount(1, "htl43@gmail.com", "267-731-4433", "Abc123", "Henry", "Pat");

	@Mock
	private OwnerAccountDAO ownerDaoTest;
	
	@InjectMocks
	private OwnerAccountService ownerServiceTest;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testEncryptPassword() {
		String password = "abc123456";
		String encryptPassword = ownerServiceTest.encryptPassword(password);
		assertFalse(encryptPassword.equals(password));
		assertNotNull(encryptPassword);
	}

	@Test
	void testSaveCustomerAccount() {
		
		// valid test
		when(ownerDaoTest.save(ownerAccount)).thenReturn(ownerAccount);
		OwnerAccount confirmAccount = ownerServiceTest.saveCustomerAccount(ownerAccount);
		verify(ownerDaoTest, times(1)).save(ownerAccount);
		assertEquals(confirmAccount, ownerAccount);
		
		//invalid test
		OwnerAccount invalidAccount = null;
		
		try {
			confirmAccount = ownerServiceTest.saveCustomerAccount(invalidAccount);
			verify(ownerDaoTest, times(1)).save(invalidAccount);
		} catch (BusinessException e) {
			assertNotNull(e);
		} 
	}

	@Test
	void testFindOwnerById() {
		// valid test
		Optional<OwnerAccount> ownerOpt = Optional.of(ownerAccount);
		when(ownerDaoTest.findById(1)).thenReturn(ownerOpt);
		OwnerAccount confirmAccount = ownerServiceTest.findOwnerById(1);
		verify(ownerDaoTest, times(1)).findById(1);
		assertEquals(ownerOpt.get(), confirmAccount);
		
		// invalid test
		try {
			confirmAccount = ownerServiceTest.findOwnerById(2);
			verify(ownerDaoTest, times(1)).findById(2);
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), "No Account is associated with the id=2");
		}
		
		
	}

	@Test
	void testGetAccountByLoginDTO() {
		LoginDTO loginDTO = new LoginDTO("htl43@gmail.com", "Abc123");
		String encrypPassword = ownerServiceTest.encryptPassword(loginDTO.getPassword());
		// valid test
		Optional<OwnerAccount> ownerOpt = Optional.of(ownerAccount);
		when(ownerDaoTest.findOwnerByEmailAndPassword(loginDTO.getEmail(), encrypPassword)).thenReturn(ownerOpt);
		OwnerAccount confirmAccount = ownerServiceTest.getAccountByLoginDTO(loginDTO);
		verify(ownerDaoTest, times(1)).findOwnerByEmailAndPassword(loginDTO.getEmail(), encrypPassword);
		assertEquals(ownerOpt.get(), confirmAccount);
		
		// invalid test
		LoginDTO invalidLoginDTO = new LoginDTO("htl43@gmail.com", "abc123");
		try {
			ownerServiceTest.getAccountByLoginDTO(invalidLoginDTO);
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), "No account is associated with email and password");
		}
	}

	@Test
	void testFindOwnerAccountByEmail() {
		
	}

	@Test
	void testUpdateAccountByFieldChange() {
		
	}

	@Test
	void testDeleteOwnerAccount() {
		
	}

}
