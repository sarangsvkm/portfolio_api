package com.sarangsvkm.portfolio_api.service;

import com.sarangsvkm.portfolio_api.entity.ContactRequest;
import com.sarangsvkm.portfolio_api.entity.Profile;
import com.sarangsvkm.portfolio_api.repository.ContactRequestRepository;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.Optional;
import java.util.List;

@Service
public class ContactRequestService {
    
    private final ContactRequestRepository repo;
    private final ProfileService profileService;

    public ContactRequestService(ContactRequestRepository repo, ProfileService profileService) {
        this.repo = repo;
        this.profileService = profileService;
    }

    public String generateAndSaveOtp(ContactRequest request) {
        // Generate a 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        
        Optional<ContactRequest> existing = repo.findByEmail(request.getEmail());
        ContactRequest contact;
        if (existing.isPresent()) {
            contact = existing.get();
            contact.setName(request.getName());
            contact.setPhone(request.getPhone());
        } else {
            contact = request;
        }
        
        contact.setOtp(otp);
        contact.setVerified(false);
        repo.save(contact);
        
        return otp;
    }

    public String verifyOtp(String email, String otp) throws Exception {
        Optional<ContactRequest> existing = repo.findByEmail(email);
        if (existing.isPresent()) {
            ContactRequest contact = existing.get();
            if (contact.getOtp() != null && contact.getOtp().equals(otp)) {
                contact.setVerified(true);
                contact.setOtp(null); // Clear OTP after successful verification
                repo.save(contact);
                
                // Fetch the profile phone number
                List<Profile> profiles = profileService.getAll();
                if (profiles != null && !profiles.isEmpty()) {
                    return profiles.get(0).getPhone();
                } else {
                    return "Profile not found.";
                }
            }
        }
        throw new RuntimeException("Invalid OTP or Email.");
    }
}
