package com.cts.ram.Blood_Bank_Application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.ram.Blood_Bank_Application.model.BloodDonation;

import jakarta.transaction.Transactional;

@Repository
public interface BloodDonationRepository extends JpaRepository<BloodDonation, Long>{

	List<BloodDonation> findByUserId(Long userId);
	
	BloodDonation findByDonationId(Long donationId);
	
	@Query("SELECT COALESCE(SUM(b.unitsDonated), 0) FROM BloodDonation b WHERE b.userId = :userId AND b.status = 'Accepted'")
	Integer findTotalUnitsDonatedByUserId(Long userId);
	
	List<BloodDonation> findAll();
	
	List<BloodDonation> findAllByStatus(String status);
	
	@Modifying
    @Transactional
    @Query("DELETE FROM BloodDonation d WHERE d.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
