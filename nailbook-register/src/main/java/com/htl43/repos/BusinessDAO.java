package com.htl43.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.htl43.model.Business;

public interface BusinessDAO extends JpaRepository<Business, Integer>{

	public Optional<Business> findByAddress(String address);

}
