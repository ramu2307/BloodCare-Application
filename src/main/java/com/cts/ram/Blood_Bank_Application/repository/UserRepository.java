package com.cts.ram.Blood_Bank_Application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.ram.Blood_Bank_Application.model.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

	User findByEmailAndPassword(String email, String password);
	User findByEmail(String email);
	
	@Modifying
    @Transactional
    @Query("DELETE FROM User d WHERE d.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
	
}
