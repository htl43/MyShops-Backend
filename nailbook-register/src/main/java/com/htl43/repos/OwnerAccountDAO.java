package com.htl43.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.htl43.model.OwnerAccount;

public interface OwnerAccountDAO extends JpaRepository<OwnerAccount, Integer>{

	public Optional<OwnerAccount> findOwnerByEmail(String email);

}
