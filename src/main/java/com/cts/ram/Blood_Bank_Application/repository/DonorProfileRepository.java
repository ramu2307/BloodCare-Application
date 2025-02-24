package com.cts.ram.Blood_Bank_Application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.ram.Blood_Bank_Application.model.DonorProfile;

import jakarta.transaction.Transactional;

@Repository
public interface DonorProfileRepository extends JpaRepository<DonorProfile, Long>{

	 DonorProfile findByUserId(Long userId);
	 
	 List<DonorProfile> findByStatus(String status);
	 
	 
	 @Query("SELECT d FROM DonorProfile d WHERE d.status = 'yes' AND UPPER(d.bloodGroup) LIKE UPPER(CONCAT(:bloodGroupPrefix, '%'))")
	 List<DonorProfile> findByBloodGroupStartingWithIgnoreCase(String bloodGroupPrefix);
	 
	 @Query("SELECT COUNT(d) FROM DonorProfile d")
	 Long findAllDonors();
	 
	 @Modifying
     @Transactional
     @Query("DELETE FROM DonorProfile d WHERE d.userId = :userId")
     void deleteByUserId(@Param("userId") Long userId);
}
