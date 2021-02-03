package com.htl43.model;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
public class Business {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="business_id")
	private int id;
	
	private String name;
	private String type;
	private String address;
	private String phone;
	private String status;
	private Date activated_date;
	private Date deactivated_date;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="owner_id")
	private OwnerAccount owner;
	

	public Business(String name, String type, String address, String phone, String status, Date activated_date,
			Date deactivated_date, OwnerAccount owner) {
		super();
		this.name = name;
		this.type = type;
		this.address = address;
		this.phone = phone;
		this.status = status;
		this.activated_date = activated_date;
		this.deactivated_date = deactivated_date;
		this.owner = owner;
	}
	
	

}
