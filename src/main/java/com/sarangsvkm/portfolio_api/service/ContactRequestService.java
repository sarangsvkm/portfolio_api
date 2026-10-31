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
@SuppressWarnings("null")
public class ContactRequestService {

    private final ContactRequestRepository repo;
    private final ProfileService profileService;
    private final EmailService emailService;

    public ContactRequestService(ContactRequestRepository repo, ProfileService profileService,
            EmailService emailService) {
        this.repo = repo;
        this.profileService = profileService;
        this.emailService = emailService;
    }

    public String generateAndSaveOtp(ContactRequest request) {
        if (request == null)
            throw new IllegalArgumentException("Request cannot be null");

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
        contact.setCreatedAt(java.time.LocalDateTime.now()); // 🕒 Refresh timestamp for OTP expiry
        repo.save(contact);

        // Send the OTP via Email asynchronously
        java.util.concurrent.CompletableFuture.runAsync(() -> {
            emailService.sendOtpEmail(request.getEmail(), request.getName(), otp);
        });

        return otp;
    }

    public List<ContactRequest> getAll() {
        return repo.findAll();
    }

    public java.util.Map<String, String> verifyOtp(String email, String otp) throws Exception {
        Optional<ContactRequest> existing = repo.findByEmail(email);
        if (existing.isPresent()) {
            ContactRequest contact = existing.get();
            
            // 🕒 Check if OTP has expired (10 minutes)
            if (contact.getCreatedAt() != null && 
                contact.getCreatedAt().isBefore(java.time.LocalDateTime.now().minusMinutes(10))) {
                throw new RuntimeException("OTP has expired. Please request a new one.");
            }

            if (contact.getOtp() != null && contact.getOtp().equals(otp)) {
                contact.setVerified(true);
                contact.setOtp(null); // Clear OTP after successful verification
                repo.save(contact);

                // Fetch the profile sensitive details (non-redacted)
                List<Profile> profiles = profileService.getRealProfiles();
                if (profiles != null && !profiles.isEmpty()) {
                    Profile p = profiles.get(0);
                    java.util.Map<String, String> details = new java.util.HashMap<>();
                    details.put("phone", p.getPhone());
                    details.put("resumeUrl", p.getResumeUrl());
                    return details;
                } else {
                    throw new RuntimeException("Profile not found.");
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

    /**
     * 🧹 Background task to delete unverified contact requests older than 1 hour.
     * Runs every hour (3,600,000 ms).
     */
    @org.springframework.beans.factory.annotation.Value("${otp.expiry.minutes:10}")
    private int otpExpiryMinutes;

    @org.springframework.scheduling.annotation.Scheduled(fixedRate = 600000) // Run every 10 min
    public void cleanupExpiredOtps() {
        java.time.LocalDateTime expiryTime = java.time.LocalDateTime.now().minusMinutes(otpExpiryMinutes);
        List<ContactRequest> expiredRequests = repo.findAll().stream()
                .filter(cr -> !cr.isVerified() && cr.getCreatedAt() != null && cr.getCreatedAt().isBefore(expiryTime))
                .collect(java.util.stream.Collectors.toList());

        if (!expiredRequests.isEmpty()) {
            System.out.println("🧹 Cleaning up " + expiredRequests.size() + " expired unverified records.");
            repo.deleteAll(expiredRequests);
        }
    }
}
