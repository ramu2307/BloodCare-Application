package com.cts.ram.Blood_Bank_Application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.ram.Blood_Bank_Application.model.BloodStock;
import com.cts.ram.Blood_Bank_Application.model.DonorProfile;
import com.cts.ram.Blood_Bank_Application.model.User;
import com.cts.ram.Blood_Bank_Application.repository.BloodStockRepository;
import com.cts.ram.Blood_Bank_Application.repository.DonorProfileRepository;

@Service
public class DonorProfileService {

	@Autowired
    private DonorProfileRepository donorProfileRepository;
	
	@Autowired
	private BloodStockRepository bloodStockRepository;

    public DonorProfile getByUserId(Long userId) {
        return donorProfileRepository.findByUserId(userId);
    }

    public void updateOrSaveDonorProfile(DonorProfile donorProfile, User loggedUser) {
        DonorProfile existingProfile = donorProfileRepository.findByUserId(loggedUser.getUserId());
        if (existingProfile == null) {
        	
        	donorProfile.setName(loggedUser.getUsername());
        	donorProfile.setBloodGroup(loggedUser.getBloodGroup());
        	donorProfile.setUnitsDonated(0);
        	donorProfile.setMobile(loggedUser.getMobile());
        	donorProfile.setGender(loggedUser.getGender());
        	donorProfile.setAge(loggedUser.getAge());
        	donorProfile.setStatus("yes");
        	donorProfile.setUserId(loggedUser.getUserId());
            donorProfileRepository.save(donorProfile);
            
            BloodStock bloodStock = bloodStockRepository.findByBloodGroup(loggedUser.getBloodGroup())
                    .orElse(BloodStock.builder()
                            .bloodGroup(loggedUser.getBloodGroup())
                            .unitsAvailable(0L)
                            .donorsCount(0L)
                            .build());
            bloodStock.setDonorsCount(bloodStock.getDonorsCount() + 1);
            bloodStockRepository.save(bloodStock);
        } else {
            existingProfile.setAddress(donorProfile.getAddress());
            existingProfile.setCity(donorProfile.getCity());
            donorProfileRepository.save(existingProfile);
        }
    }
    
    public void update(DonorProfile donorProfile) {
        donorProfileRepository.save(donorProfile);
    }
    
    public List<DonorProfile> getDonorsList() {
        return donorProfileRepository.findByStatus("yes");
    }
    
    public List<DonorProfile> getDonorByBloodGroupStartsWith(String bloodGroup) {
    	return donorProfileRepository.findByBloodGroupStartingWithIgnoreCase(bloodGroup);
    }
    
    public Long getAllDonors() {
    	return donorProfileRepository.findAllDonors();
    }
    
    
}
