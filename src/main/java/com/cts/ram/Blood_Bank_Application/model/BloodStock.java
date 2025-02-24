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
public class BloodStock {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_req")
	@SequenceGenerator(name = "stock_req", sequenceName = "stock_req", allocationSize = 1)
	private Long stockId;
	
	@Column(nullable = false, unique = true)
	private String bloodGroup;
	
	@Column(nullable = false)
	private Long unitsAvailable;
	
	@Column(nullable = false)
	private Long donorsCount;
}
