package com.htl43.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.htl43.model.OwnerAccount;

public interface OwnerAccountDAO extends JpaRepository<OwnerAccount, Integer>{
	
	@Query("SELECT owner FROM OwnerAccount owner WHERE owner.email=?1 AND owner.password=?2")
	public Optional<OwnerAccount> findOwnerByEmailAndPassword(String email, String password);
	
	@Modifying
	@Query("UPDATE OwnerAccount owner SET owner.password=?1 WHERE owner.id=?2")
	public int updatePasswordById(String password, Integer id);

	public Optional<OwnerAccount> findByEmail(String email);

}
