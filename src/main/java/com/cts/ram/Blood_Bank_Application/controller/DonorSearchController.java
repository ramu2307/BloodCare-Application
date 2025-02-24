package com.cts.ram.Blood_Bank_Application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cts.ram.Blood_Bank_Application.model.DonorProfile;
import com.cts.ram.Blood_Bank_Application.service.DonorProfileService;

@RestController
public class DonorSearchController {
	
	@Autowired
	private DonorProfileService donorProfileService;
	
	@GetMapping("/search")
	@ResponseBody
	public ResponseEntity<List<DonorProfile>> searchDonors(@RequestParam String bloodGroup) {
		
		List<DonorProfile> donors = donorProfileService.getDonorByBloodGroupStartsWith(bloodGroup);
		return ResponseEntity.ok(donors);
	}
	
	@GetMapping("/getAllDonors")
	@ResponseBody
	public ResponseEntity<List<DonorProfile>> getAllDonors() {
		List<DonorProfile> donors = donorProfileService.getDonorsList();
		return ResponseEntity.ok(donors);
	}

}
