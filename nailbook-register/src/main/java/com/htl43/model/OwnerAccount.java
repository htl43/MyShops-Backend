package com.htl43.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
public class OwnerAccount {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name="owner_id")
	private  int id;
	
	@Column (unique = true)
	@NotNull
	private String email;
	private String phone;
	@NotNull
	private String password;
	@NotNull
	private String firstname;
	@NotNull
	private String lastname;
	
	@OneToMany(mappedBy = "owner", fetch=FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.PERSIST)
	@JsonManagedReference
	private List<Business> listBusinesses;

	
	public OwnerAccount(String email, String phone, String password, String firstname, String lastname,
			List<Business> listBusinesses) {
		super();
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.listBusinesses = listBusinesses;
	}


	public OwnerAccount(int id, String email, String phone, String password, String firstname, String lastname) {
		super();
		this.id = id;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
	}


	
	
	
	
}
