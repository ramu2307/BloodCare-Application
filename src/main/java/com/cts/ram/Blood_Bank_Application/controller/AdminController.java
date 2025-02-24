package com.cts.ram.Blood_Bank_Application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.ram.Blood_Bank_Application.model.Admin;
import com.cts.ram.Blood_Bank_Application.model.BloodDonation;
import com.cts.ram.Blood_Bank_Application.model.BloodRequest;
import com.cts.ram.Blood_Bank_Application.model.BloodStock;
import com.cts.ram.Blood_Bank_Application.model.User;
import com.cts.ram.Blood_Bank_Application.repository.BloodRequestRepository;
import com.cts.ram.Blood_Bank_Application.service.AdminService;
import com.cts.ram.Blood_Bank_Application.service.BloodDonationService;
import com.cts.ram.Blood_Bank_Application.service.BloodRequestService;
import com.cts.ram.Blood_Bank_Application.service.BloodStockService;
import com.cts.ram.Blood_Bank_Application.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
@Controller
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private BloodDonationService bloodDonationService;
	
	@Autowired
	private BloodRequestService bloodRequestService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BloodStockService bloodStockService;
	

	@GetMapping("/adminLogin")
    public String showAdminLoginPage(Model model) {
        model.addAttribute("admin", new Admin());
        return "AdminLogin";
    }
	
	@PostMapping("/adminLogin")
    public String login(@ModelAttribute("admin") Admin admin, HttpSession session, Model model) {
        Admin loggedAdmin = adminService.validateAdmin(admin.getEmail(), admin.getPassword());
        if (loggedAdmin != null) {
        	session.setAttribute("loggedAdmin", loggedAdmin);
            return "redirect:/adminDashboard";
            
        } else {
            model.addAttribute("errorMessage", "Invalid email or password");
            model.addAttribute("admin", admin);
            return "redirect:/adminLogin";
        }
    }
	
	@GetMapping("/adminDashboard")
	public String showAdminDashboard(HttpSession session, Model model) {
		Admin loggedAdmin = (Admin) session.getAttribute("loggedAdmin");
        if (loggedAdmin == null) {
            return "redirect:/adminLogin";
            
        }
        List<BloodStock> bloodStockList = bloodStockService.fetchBloodStockDetails();
        System.out.println(bloodStockList.size());
        model.addAttribute("bloodStocks", bloodStockList);
        model.addAttribute("loggedAdminName", loggedAdmin.getAdminName());
        return "AdminDashboard";
	}
	
	@GetMapping("/adminBloodDonationsRequests")
	public String showBloodDonationRequests(HttpSession session, Model model) {
		Admin loggedAdmin = (Admin) session.getAttribute("loggedAdmin");
        if (loggedAdmin == null) {
            return "redirect:/adminDashboard"; 
        }
        List<BloodDonation> pendingBloodDonations = bloodDonationService.findAllWhereStatusIsPending();
        model.addAttribute("pendingBloodDonations", pendingBloodDonations);
        model.addAttribute("loggedAdminName", loggedAdmin.getAdminName());
        return "AdminDonationRequests";
	}
	
	@GetMapping("/donations/accept/{donationId}")
    public String acceptDonation(@PathVariable("donationId") Long donationId, Model model) {
        bloodDonationService.updateDonationStatus(donationId, "Accepted");
        return "redirect:/adminBloodDonationsRequests";
    }

    @GetMapping("/donations/decline/{donationId}")
    public String declineDonation(@PathVariable("donationId") Long donationId, Model model) {
        bloodDonationService.updateDonationStatus(donationId, "Rejected");
        return "redirect:/adminBloodDonationsRequests";
    }
    
    @GetMapping("/adminBloodRequests")
	public String showBloodRequests(HttpSession session, Model model) {
		Admin loggedAdmin = (Admin) session.getAttribute("loggedAdmin");
        if (loggedAdmin == null) {
            return "redirect:/adminDashboard"; 
        }
        List<BloodRequest> pendingBloodRequests = bloodRequestService.findAllWhereStatusIsPending();
        model.addAttribute("pendingBloodRequests", pendingBloodRequests);
        model.addAttribute("loggedAdminName", loggedAdmin.getAdminName());
        return "AdminBloodRequests";
	}
    
    @GetMapping("/requests/accept/{requestId}")
    public String acceptRequest(@PathVariable("requestId") Long requestId, Model model) {
    	bloodRequestService.updateRequestStatus(requestId, "Accepted");
        return "redirect:/adminBloodRequests";
    }

    @GetMapping("/requests/decline/{requestId}")
    public String declineRequest(@PathVariable("requestId") Long requestId, Model model) {
    	bloodRequestService.updateRequestStatus(requestId, "Rejected");
       	return "redirect:/adminBloodRequests";
    }
    
    @GetMapping("/adminManagesUsers")
    public String manageUsers(HttpSession session, Model model) {
    	Admin loggedAdmin = (Admin) session.getAttribute("loggedAdmin");
        if (loggedAdmin == null) {
            return "redirect:/adminDashboard"; 
        }
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("showMode", true);
        model.addAttribute("editMode", false);
        model.addAttribute("addMode", false);
        model.addAttribute("loggedAdminName", loggedAdmin.getAdminName());
        return "AdminManageUsers";
    }
    
    @GetMapping("/adminAddUsers")
    public String addUsers(HttpSession session, Model model) {
    	Admin loggedAdmin = (Admin) session.getAttribute("loggedAdmin");
        if (loggedAdmin == null) {
            return "redirect:/adminDashboard"; 
        }
        model.addAttribute("user", new User());
        model.addAttribute("showMode", false);
        model.addAttribute("editMode", false);
        model.addAttribute("addMode", true);
        model.addAttribute("loggedAdminName", loggedAdmin.getAdminName());
        return "AdminManageUsers";
    }
    
    @PostMapping("/adminAddUser")
    public String adminAddUsers(@ModelAttribute("user")User user,Model model) {
    	String currEmail = user.getEmail();
        try {
            if(currEmail != null && !currEmail.isEmpty()) {
                User existingUser = userService.fetchUserByEmail(currEmail);
                if(existingUser != null) {
                    throw new Exception("User with email " + currEmail + " already exists!");
                }
            }
            userService.saveUser(user);
            model.addAttribute("successMessage", "User Registered Successfully");
            return "redirect:/adminManagesUsers";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/adminAddUsers";
    }
    
    @GetMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId, Model model) {
        userService.deleteUserAndRelatedRecords(userId);
        return "redirect:/adminManagesUsers";
    }
    
    @GetMapping("/bloodHistory/active")
    public String adminBloodHistoryActive(HttpSession session, Model model) {
    	Admin loggedAdmin = (Admin) session.getAttribute("loggedAdmin");
        if (loggedAdmin == null) {
            return "redirect:/adminDashboard"; 
        }
        model.addAttribute("loggedAdminName", loggedAdmin.getAdminName());
        model.addAttribute("mode1", true);
        model.addAttribute("mode2", false);
        List<BloodRequest> pendingBloodRequests = bloodRequestService.getAll();
        model.addAttribute("pendingBloodRequests", pendingBloodRequests);
        List<BloodDonation> pendingBloodDonations = bloodDonationService.getAllDonations();
        model.addAttribute("pendingBloodDonations", pendingBloodDonations);
        return "AdminRequestsHistory";
    }
    
    @GetMapping("/bloodHistory/none")
    public String adminBloodHistoryNone(HttpSession session, Model model) {
    	Admin loggedAdmin = (Admin) session.getAttribute("loggedAdmin");
        if (loggedAdmin == null) {
            return "redirect:/adminDashboard"; 
        }
        model.addAttribute("loggedAdminName", loggedAdmin.getAdminName());
        model.addAttribute("mode1", false);
        model.addAttribute("mode2", true);
        List<BloodRequest> pendingBloodRequests = bloodRequestService.getAll();
        model.addAttribute("pendingBloodRequests", pendingBloodRequests);
        List<BloodDonation> pendingBloodDonations = bloodDonationService.getAllDonations();
        model.addAttribute("pendingBloodDonations", pendingBloodDonations);
        System.out.println(pendingBloodDonations.size());
        return "AdminRequestsHistory";
    }
    
    @GetMapping("/admin/requests/delete/{requestId}")
    public String deleteRequest(@PathVariable("requestId") Long requestId) {
    	bloodRequestService.deleteRequestById(requestId);
        return "redirect:/bloodHistory/active";
    }
    
    @GetMapping("/admin/donations/decline/{donationId}")
    public String declineDonation(@PathVariable("donationId") Long donationId) {
    	bloodDonationService.deleteDonationById(donationId);
        return "redirect:/bloodHistory/none";
    }
   
    @GetMapping("/adminLogout")
    public String adminLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
    
}
