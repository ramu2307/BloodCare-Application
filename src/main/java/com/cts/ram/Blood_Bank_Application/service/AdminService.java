package com.cts.ram.Blood_Bank_Application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.ram.Blood_Bank_Application.model.Admin;
import com.cts.ram.Blood_Bank_Application.repository.AdminRepository;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	public Admin validateAdmin(String email, String password) {
		return adminRepository.findByEmailAndPassword(email, password);
	}

}
