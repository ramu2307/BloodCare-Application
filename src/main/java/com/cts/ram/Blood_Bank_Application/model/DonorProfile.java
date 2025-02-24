package com.cts.ram.Blood_Bank_Application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonorProfile {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_seq")
	@SequenceGenerator(name = "profile_seq", sequenceName = "profile_sequence", allocationSize = 1)
	private Long profileId;
	
	@Column(nullable = false)
	private String name;
		
	@Column(nullable = false)
	private String bloodGroup;
	
	@Column(nullable = false)
	private int unitsDonated;
	
	@Column(nullable = false)
	private String mobile;
	
	private String gender;
	
	@Column(nullable = false)
	private int age;
	
	@Column(nullable = false)
	private String city;
	
	@Column(nullable = false)
	private String address;
	
	@Column(nullable = false)
	private String status;
	
	
	@Column(name = "user_id")
	private Long userId;
}

