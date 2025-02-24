package com.cts.ram.Blood_Bank_Application.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cts.ram.Blood_Bank_Application.model.BloodDonation;
import com.cts.ram.Blood_Bank_Application.model.BloodRequest;
import com.cts.ram.Blood_Bank_Application.model.BloodStock;
import com.cts.ram.Blood_Bank_Application.model.DonorProfile;
import com.cts.ram.Blood_Bank_Application.model.User;
import com.cts.ram.Blood_Bank_Application.service.BloodDonationService;
import com.cts.ram.Blood_Bank_Application.service.BloodRequestService;
import com.cts.ram.Blood_Bank_Application.service.BloodStockService;
import com.cts.ram.Blood_Bank_Application.service.DonorProfileService;
import com.cts.ram.Blood_Bank_Application.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class UserController {
    
    @Autowired
    private UserService userService;
    @Autowired
	private BloodRequestService bloodRequestService;
    @Autowired
    private DonorProfileService donorProfileService;
    @Autowired
    private BloodDonationService bloodDonationService;
    @Autowired
    private BloodStockService bloodStockService;
    
    
    @GetMapping("/")
    public String defaultPage(Model model) {
        model.addAttribute("user", new User());
        return "HomePage";
    }

    @GetMapping("/userLogin")
    public String showUserLoginPage(Model model) {
    	model.addAttribute("user", new User());
        return "UserLogin";
    }
        
    @PostMapping("/user/login-form")
    public String login(@ModelAttribute("user") User user, HttpSession session, Model model) {
        User loggedUser = userService.validateUser(user.getEmail(), user.getPassword());
        if (loggedUser != null) {
            session.setAttribute("loggedUser", loggedUser);
            return "redirect:/user/dashboard";
        } else {
            model.addAttribute("errorMessage", "Invalid email or password");
            return "UserLogin";
        }
    }
    
    @GetMapping("/userRegister")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "UserRegister";
    }
    
    @PostMapping("/UserRegister-form")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
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
            return "userLogin";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "UserLogin";
    }
    
   

    @GetMapping("/user/dashboard")
    public String showHomePage(HttpSession session,Model model) {
    	User loggedUser = (User) session.getAttribute("loggedUser");
    	if(loggedUser == null) {
    		return "redirect:/user/login-form";
    	}
    	
    	List<BloodStock> bloodStockList = bloodStockService.fetchBloodStockDetails();
    	
    	List<BloodRequest> requestListByUser = bloodRequestService.getBloodRequestsByUserId(loggedUser.getUserId());
    	List<BloodDonation> donationListByUser = bloodDonationService.getBloodDonationsByUserId(loggedUser.getUserId());
    	Long totalDonors = donorProfileService.getAllDonors();
    	Integer totalUnitsDonatedByUser = bloodDonationService.getUnitsDonatedByUser(loggedUser.getUserId());
        model.addAttribute("userTotalDonations", donationListByUser.size());
        model.addAttribute("userTotalRequests", requestListByUser.size());
        model.addAttribute("totalDonors", totalDonors);
        model.addAttribute("totalUnitsDonatedByUser", totalUnitsDonatedByUser);
    	model.addAttribute("bloodStocks", bloodStockList);
    	model.addAttribute("loggedUserName", loggedUser.getUsername());
        return "UserDashBoard";
    }
    
    @GetMapping("/requestBlood")
    public String requestBloodForm(HttpSession session ,Model model) {
    	User loggedUser = (User) session.getAttribute("loggedUser");
    	if (loggedUser == null) {
            return "redirect:/user/dashboard";
        }
    	model.addAttribute("loggedUserName", loggedUser.getUsername());
        model.addAttribute("request", new BloodRequest());
        return "UserMakeBloodRequest";
    }
    
    @PostMapping("/blood/request-form")
    public String requestBlood(@ModelAttribute("request") BloodRequest request, RedirectAttributes model, HttpSession session) {
    	
    	User loggedUser = (User) session.getAttribute("loggedUser");
    	if (loggedUser == null) {
            return "redirect:/user/dashboard";
        }
    	request.setUserId(loggedUser.getUserId());
        bloodRequestService.saveRequest(request);
    	model.addFlashAttribute("successMessage", "Blood request submitted successfully!");
        return "redirect:/requestBlood";
    }
    
    
    @GetMapping("/requesthistoryfromuser")
    public String showRequestHistory(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/user/dashboard";
        }
        model.addAttribute("loggedUserName", loggedUser.getUsername());
        List<BloodRequest> requestList = bloodRequestService.getBloodRequestsByUserId(loggedUser.getUserId());
        System.out.println(requestList.size());
        model.addAttribute("requests", requestList);
        return "UserRequestHistory";
    }
    
    @GetMapping("/registerAsDonor")
    public String registerAsDonor(HttpSession session, Model model) {
    	User loggedUser = (User) session.getAttribute("loggedUser");
    	if (loggedUser == null) {
            return "redirect:/user/dashboard";
        }
    	model.addAttribute("loggedUserName", loggedUser.getUsername());
    	DonorProfile donorProfile = donorProfileService.getByUserId(loggedUser.getUserId());
    	boolean hasDonorProfile = donorProfile != null;
        if (donorProfile == null) {
            donorProfile = new DonorProfile();
        }
        model.addAttribute("donorProfile", donorProfile);
        model.addAttribute("willingnessStatus", donorProfile.getStatus());
        model.addAttribute("hasDonorProfile", hasDonorProfile);
        return "UserRegisterAsDonor";
    }
    
    @PostMapping("/donorProfileUpdate")
    public String updateDonorProfile(@ModelAttribute("donorProfile") DonorProfile donorProfile, RedirectAttributes model, HttpSession session) {
    	User loggedUser = (User) session.getAttribute("loggedUser");
    	if (loggedUser == null) {
            return "redirect:/user/dashboard";
        }
    	
		donorProfileService.updateOrSaveDonorProfile(donorProfile, loggedUser);
    	
    	model.addFlashAttribute("successMessage", "Profile Updated succesfully");
        return "redirect:/registerAsDonor";
    }
    
    
    @PostMapping("/updateWillingnessStatus")
    @ResponseBody
    public ResponseEntity<String> updateWillingnessStatus(@RequestBody Map<String, String> payload, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        String status = payload.get("status");
        DonorProfile donorProfile = donorProfileService.getByUserId(loggedUser.getUserId());
        if (donorProfile != null) {
            donorProfile.setStatus(status.equals("yes") ? "yes" : "no");
            donorProfileService.update(donorProfile);
        }

        return ResponseEntity.ok("Willingness status updated successfully");
    }
    


    
    @GetMapping("/donateBlood")
    public String bloodDonationForm(HttpSession session, Model model) {
    	User loggedUser = (User) session.getAttribute("loggedUser");
    	if (loggedUser == null) {
            return "redirect:/user/dashboard";
        }
    	model.addAttribute("loggedUserName", loggedUser.getUsername());
        model.addAttribute("donate", new BloodDonation());
        return "UserBloodDonate";
    }
    
    @PostMapping("/user/donate-form")
    public String submitDonation(@ModelAttribute("donate") BloodDonation donate, RedirectAttributes model, HttpSession session) {
    	
    	User loggedUser = (User) session.getAttribute("loggedUser");
    	if (loggedUser == null) {
            return "redirect:/user/dashboard";
        }
    	
        bloodDonationService.saveDonate(donate, loggedUser);
    	model.addFlashAttribute("successMessage", "Blood Donation Form submitted successfully!");
        return "redirect:/donateBlood";
    }
    
    @GetMapping("/donationHistoryFromUser")
    public String showDonationHistory(HttpSession session, Model model) {
    	User loggedUser = (User) session.getAttribute("loggedUser");
    	if (loggedUser == null) {
            return "redirect:/user/dashboard";
        }
    	List<BloodDonation> donationHistory = bloodDonationService.getAllDonations();
    	model.addAttribute("loggedUserName", loggedUser.getUsername());
    	model.addAttribute("donations", donationHistory);
    	return "UserDonationHistory";
    	
    }
    
    @GetMapping("/donorList")
    public String donorList(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/user/dashboard";
        }
        model.addAttribute("loggedUserName", loggedUser.getUsername());
        List<DonorProfile> totalDonorsList = donorProfileService.getDonorsList();
        model.addAttribute("donors", totalDonorsList);
        return "UserDonorsList";
    }
    
    @GetMapping("/bloodStock")
    public String bloodStockList(HttpSession session, Model model) {
    	User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/user/dashboard";
        }
        model.addAttribute("loggedUserName", loggedUser.getUsername());
        List<BloodStock> bloodStockList = bloodStockService.fetchBloodStockDetails();
        System.out.println(bloodStockList.size());
        model.addAttribute("bloodStocks", bloodStockList); 
        return "UserBloodStock";
    }
    
    @GetMapping("/editProfile")
    public String editUserProfile(HttpSession session , Model model) {
    	User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/user/dashboard";
        }
        model.addAttribute("loggedUserName", loggedUser.getUsername());
        model.addAttribute("user", loggedUser);
        model.addAttribute("editMode", false);
        return "UserEditProfile";
    }
    
    @PostMapping("/saveProfile")
    public String saveProfile(@ModelAttribute("user") User user, HttpSession session, RedirectAttributes attributes) {
    	User loggedUser = (User) session.getAttribute("loggedUser");
    	user.setUserId(loggedUser.getUserId());
    	user.setEmail(loggedUser.getEmail());
    	user.setPassword(loggedUser.getPassword());
    	user.setToDonate(loggedUser.getToDonate());
    	user.setBloodGroup(loggedUser.getBloodGroup());
    	user.setGender(loggedUser.getGender());
        
        userService.saveUser(user);
        
        session.setAttribute("loggedUser", user);
        attributes.addFlashAttribute("editMode", false);
        return "redirect:/editProfile";
    }
    
    

    @GetMapping("/editProfileForm")
    public String showEditForm(HttpSession session, RedirectAttributes attributes, Model model) {
    	User loggedUser = (User) session.getAttribute("loggedUser");
        model.addAttribute("user", loggedUser);
        model.addAttribute("loggedUserName", loggedUser.getUsername());
        model.addAttribute("editMode", true);
        return "UserEditProfile";
    }
    
    @GetMapping("/userLogout")
    public String userLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

}

