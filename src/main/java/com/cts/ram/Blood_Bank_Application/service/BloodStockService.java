package com.cts.ram.Blood_Bank_Application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.ram.Blood_Bank_Application.model.BloodStock;
import com.cts.ram.Blood_Bank_Application.repository.BloodStockRepository;

@Service
public class BloodStockService {

	@Autowired
	private BloodStockRepository bloodStockRepository;
	
	public List<BloodStock> fetchBloodStockDetails() {
		return bloodStockRepository.findAll();
	}
	
}
