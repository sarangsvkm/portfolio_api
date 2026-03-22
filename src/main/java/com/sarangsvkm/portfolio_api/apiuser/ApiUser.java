package com.sarangsvkm.portfolio_api.apiuser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int userid;

	@Column(unique = true)
	private String username;

	@Column
	private String password;

	@Column
	private String role;

}
