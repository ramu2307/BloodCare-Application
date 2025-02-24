package com.cts.ram.Blood_Bank_Application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.ram.Blood_Bank_Application.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long>{
	Admin findByEmailAndPassword(String email, String password);
}
