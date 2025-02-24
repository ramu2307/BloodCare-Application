package com.cts.ram.Blood_Bank_Application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.ram.Blood_Bank_Application.model.User;
import com.cts.ram.Blood_Bank_Application.repository.BloodDonationRepository;
import com.cts.ram.Blood_Bank_Application.repository.BloodRequestRepository;
import com.cts.ram.Blood_Bank_Application.repository.DonorProfileRepository;
import com.cts.ram.Blood_Bank_Application.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BloodRequestRepository bloodRequestRepository;
	
	@Autowired
	private BloodDonationRepository bloodDonationRepository;
	
	@Autowired
	private DonorProfileRepository donorProfileRepository;

	public User validateUser(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}
	
	public User fetchUserByEmailAndPassword(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}

	public User saveUser(User user) {
		return userRepository.save(user);	
	}
	
	public User fetchUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public List<User> getAllUsers() {
        return userRepository.findAll();
    }
	
	@Transactional
	public void deleteUserAndRelatedRecords(Long userId) {
        // Delete blood requests
        bloodRequestRepository.deleteByUserId(userId);

        // Delete donation requests
        bloodDonationRepository.deleteByUserId(userId);

        // Delete donor profile
        donorProfileRepository.deleteByUserId(userId);

        // Delete user
        userRepository.deleteByUserId(userId);
    }
}
