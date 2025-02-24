package com.cts.ram.Blood_Bank_Application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.ram.Blood_Bank_Application.model.BloodDonation;
import com.cts.ram.Blood_Bank_Application.model.BloodStock;
import com.cts.ram.Blood_Bank_Application.model.DonorProfile;
import com.cts.ram.Blood_Bank_Application.model.User;
import com.cts.ram.Blood_Bank_Application.repository.BloodDonationRepository;
import com.cts.ram.Blood_Bank_Application.repository.BloodStockRepository;
import com.cts.ram.Blood_Bank_Application.repository.DonorProfileRepository;

@Service
public class BloodDonationService {
	
	@Autowired
	private BloodDonationRepository bloodDonationRepository;
	
	@Autowired
	private DonorProfileRepository donorProfileRepository;
	
	@Autowired
	private BloodStockRepository bloodStockRepository;

	public void saveDonate(BloodDonation donate, User loggedUser) {

		donate.setName(loggedUser.getUsername());
		donate.setBloodGroup(loggedUser.getBloodGroup());
		donate.setMobile(loggedUser.getMobile());
		donate.setGender(loggedUser.getGender());
		donate.setAge(loggedUser.getAge());
		donate.setUserId(loggedUser.getUserId());
		
		bloodDonationRepository.save(donate);
	}
	
	public List<BloodDonation> getBloodDonationsByUserId(Long userId) {
		return bloodDonationRepository.findByUserId(userId);
	}
	
	public Integer getUnitsDonatedByUser(Long userId) {
		return bloodDonationRepository.findTotalUnitsDonatedByUserId(userId);
	}
	
	public List<BloodDonation> getAllDonations() {
		return bloodDonationRepository.findAll();
	}
	
	public List<BloodDonation> findAllWhereStatusIsPending() {
        return bloodDonationRepository.findAllByStatus("Pending");
    }
	
	public void updateDonationStatus(Long donationId, String status) {
        BloodDonation donation = bloodDonationRepository.findByDonationId(donationId);
        donation.setStatus(status);
        bloodDonationRepository.save(donation);
        
        
   
        if ("Accepted".equals(status)) {
            // Update donor profile
            DonorProfile donorProfile = donorProfileRepository.findByUserId(donation.getUserId());
            donorProfile.setUnitsDonated((donorProfile.getUnitsDonated() + donation.getUnitsDonated()));;
            donorProfileRepository.save(donorProfile);

            // Update blood stock
            BloodStock bloodStock = bloodStockRepository.findByBloodGroup(donation.getBloodGroup()).orElseThrow(() -> new IllegalArgumentException("Invalid blood group"));
            bloodStock.setUnitsAvailable(bloodStock.getUnitsAvailable() + donation.getUnitsDonated());
            bloodStockRepository.save(bloodStock);
        }
    }
	
	public void deleteDonationById(Long donationId) {
	    bloodDonationRepository.deleteById(donationId);
	}
}
