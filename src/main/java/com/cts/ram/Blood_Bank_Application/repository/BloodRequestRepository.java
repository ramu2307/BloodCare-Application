package com.cts.ram.Blood_Bank_Application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.ram.Blood_Bank_Application.model.BloodDonation;
import com.cts.ram.Blood_Bank_Application.model.BloodRequest;

import jakarta.transaction.Transactional;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long>{

	List<BloodRequest> findByUserId(Long userId);
	
	List<BloodRequest> findAllByStatus(String status);
	
	BloodRequest findByReqId(Long reqId);
	
	@Modifying
    @Transactional
    @Query("DELETE FROM BloodRequest b WHERE b.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
