package com.sarangsvkm.portfolio_api.service;

import com.sarangsvkm.portfolio_api.entity.ContactRequest;
import com.sarangsvkm.portfolio_api.entity.Profile;
import com.sarangsvkm.portfolio_api.repository.ContactRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.Random;
import java.util.Optional;
import java.util.List;

@Service
public class ContactRequestService {
    
    private final ContactRequestRepository repo;
    private final ProfileService profileService;
    private final JavaMailSender mailSender;
    private final com.sarangsvkm.portfolio_api.repository.SystemConfigRepository configRepo;

    public ContactRequestService(ContactRequestRepository repo, ProfileService profileService, JavaMailSender mailSender, com.sarangsvkm.portfolio_api.repository.SystemConfigRepository configRepo) {
        this.repo = repo;
        this.profileService = profileService;
        this.mailSender = mailSender;
        this.configRepo = configRepo;
    }


    public String generateAndSaveOtp(ContactRequest request) {
        if (request == null) throw new IllegalArgumentException("Request cannot be null");
        
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
        
        // Send the OTP via Email asynchronously
        java.util.concurrent.CompletableFuture.runAsync(() -> {
            sendOtpEmail(request.getEmail(), request.getName(), otp);
        });
        
        return otp;
    }

    private void sendOtpEmail(String toEmail, String name, String otp) {
        try {
            String fromEmail = configRepo.findByConfigKey("spring.mail.username")
                    .map(com.sarangsvkm.portfolio_api.entity.SystemConfig::getConfigValue)
                    .orElse("sarangsvkmsuperuser@gmail.com");

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Your One-Time Password (OTP) for Portfolio Access");
            message.setText("Hi " + name + ",\n\nYour OTP to view my contact details is: " + otp + "\n\nThis OTP is valid for a short period.\n\nThank you!");
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + toEmail + ": " + e.getMessage());
        }
    }


    public List<ContactRequest> getAll() {
        return repo.findAll();
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

    public void delete(Long id) {
        if (id != null) {
            repo.deleteById(id);
        }
    }
}
