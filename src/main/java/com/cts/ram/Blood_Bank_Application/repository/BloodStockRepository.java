package com.cts.ram.Blood_Bank_Application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cts.ram.Blood_Bank_Application.model.BloodStock;

@Repository
public interface BloodStockRepository extends JpaRepository<BloodStock, Long>{

	Optional<BloodStock> findByBloodGroup(String bloodGroup);
	
	@Query("SELECT bs FROM BloodStock bs")
	List<BloodStock> findAll();
	
	
}
