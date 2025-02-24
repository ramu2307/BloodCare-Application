package com.cts.ram.Blood_Bank_Application.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
public class BloodDonation {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "donation_seq")
	@SequenceGenerator(name = "donation_seq", sequenceName = "donation_sequence", allocationSize = 1)
	private Long donationId;
	
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
	
	private LocalDate donationDate;
	
	@Column(nullable = false)
	private String status;
	
	@PrePersist
	public void setDefaultStatus() {
		if(this.status == null) {
			this.status = "Pending";
		}
	}
	
	@Column(name = "user_id")
	private Long userId;
}
