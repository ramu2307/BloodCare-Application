package com.cts.ram.Blood_Bank_Application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.ram.Blood_Bank_Application.model.BloodDonation;
import com.cts.ram.Blood_Bank_Application.model.BloodRequest;
import com.cts.ram.Blood_Bank_Application.model.BloodStock;
import com.cts.ram.Blood_Bank_Application.repository.BloodRequestRepository;
import com.cts.ram.Blood_Bank_Application.repository.BloodStockRepository;
@Service
public class BloodRequestService {

	@Autowired
	private BloodRequestRepository bloodRequestRepository;
	
	@Autowired
	private BloodStockRepository bloodStockRepository;
	
	public BloodRequest saveRequest(BloodRequest request) {
		return bloodRequestRepository.save(request);
	}
	
	public List<BloodRequest> getBloodRequestsByUserId(Long userId) {
		return bloodRequestRepository.findByUserId(userId);
	}
	
	public List<BloodRequest> findAllWhereStatusIsPending() {
        return bloodRequestRepository.findAllByStatus("Pending");
    }
	
	public void updateRequestStatus(Long requestId, String status) {
        BloodRequest request = bloodRequestRepository.findByReqId(requestId);
        request.setStatus(status);
        bloodRequestRepository.save(request);

        if ("Accepted".equals(status)) {
            // Update blood stock
        	BloodStock bloodStock = bloodStockRepository.findByBloodGroup(request.getBloodGroup()).orElseThrow(() -> new IllegalArgumentException("Invalid blood group"));
        	if (bloodStock.getUnitsAvailable() >= request.getUnitsRequested()) {
                bloodStock.setUnitsAvailable(bloodStock.getUnitsAvailable() - request.getUnitsRequested());
                bloodStockRepository.save(bloodStock);
            } else {
                throw new IllegalArgumentException("Not enough units available in stock");
            }
        }
    }
	
	public List<BloodRequest> getAll() {
		return bloodRequestRepository.findAll();
	}
	
	public void deleteRequestById(Long requestId) {
	    bloodRequestRepository.deleteById(requestId);
	}
}
