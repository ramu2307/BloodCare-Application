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
public class BloodRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_seq")
	@SequenceGenerator(name = "request_seq", sequenceName = "request_sequence", allocationSize = 1)
	private Long reqId;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String bloodGroup;
	
	@Column(nullable = false)
	private int unitsRequested;
	
	@Column(nullable = false)
	private String patientDiease;
	
	@Column(nullable = false)
	private String mobile;
	
	@Column(nullable = false)
	private String gender;
	
	@Column(nullable = false)
	private int age;
	
	@Column(nullable = false)
	private String status;
	
	private LocalDate requestDate;
	
	@PrePersist
	public void setDefaultStatus() {
		if(this.status == null) {
			this.status = "Pending";
		}
	}
	
	@Column(name = "user_id")
	private Long userId;

}
