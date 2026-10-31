package com.sarangsvkm.portfolio_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Value("${resend.api.key:}")
    private String resendApiKey;

    @Value("${resend.from.email:Sarang <noreply@sarangsvkm.in>}")
    private String resendFromEmail;

    @Value("${spring.mail.username:}")
    private String smtpFromEmail;

    private final JavaMailSender mailSender;
    private final RestTemplate restTemplate;

    public EmailService(@org.springframework.beans.factory.annotation.Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.restTemplate = new RestTemplate();
    }

    public void sendOtpEmail(String toEmail, String name, String otp) {
        if (resendApiKey != null && !resendApiKey.trim().isEmpty()) {
            sendViaResend(toEmail, name, otp);
        } else {
            sendViaSmtp(toEmail, name, otp);
        }
    }

    private void sendViaResend(String toEmail, String name, String otp) {
        try {
            String url = "https://api.resend.com/emails";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(resendApiKey.trim());

            Map<String, Object> body = new HashMap<>();
            body.put("from", resendFromEmail);
            body.put("to", Collections.singletonList(toEmail));
            body.put("subject", "Your One-Time Password (OTP) for Portfolio Access");

            String html = String.format(
                "<div style=\"font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; max-width: 480px; margin: 0 auto; padding: 32px 24px; border: 1px solid #e2e8f0; border-radius: 12px; background: #ffffff;\">"
                + "<h2 style=\"color: #0f172a; margin-top: 0; font-size: 20px; font-weight: 600;\">Portfolio Access Verification</h2>"
                + "<p style=\"color: #475569; font-size: 15px; line-height: 1.5;\">Hi <b>%s</b>,</p>"
                + "<p style=\"color: #475569; font-size: 15px; line-height: 1.5;\">Use the following One-Time Password (OTP) to view contact details:</p>"
                + "<div style=\"text-align: center; margin: 28px 0;\">"
                + "<span style=\"display: inline-block; font-size: 32px; font-weight: 700; letter-spacing: 6px; color: #4f46e5; background: #eef2ff; padding: 12px 28px; border-radius: 8px; border: 1px dashed #6366f1;\">%s</span>"
                + "</div>"
                + "<p style=\"color: #64748b; font-size: 13px; line-height: 1.5;\">🕒 This code is valid for 10 minutes. If you did not request this code, you can safely ignore this email.</p>"
                + "<hr style=\"border: none; border-top: 1px solid #f1f5f9; margin: 24px 0 16px;\" />"
                + "<p style=\"color: #94a3b8; font-size: 12px; text-align: center; margin: 0;\">Sent from Sarang's Portfolio</p>"
                + "</div>",
                name != null ? name : "there",
                otp
            );
            body.put("html", html);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("✅ Resend: Successfully sent OTP email to {}", toEmail);
            } else {
                log.error("❌ Resend API returned error status: {} body: {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("❌ Failed to send email via Resend to {}: {}", toEmail, e.getMessage(), e);
        }
    }

    private void sendViaSmtp(String toEmail, String name, String otp) {
        if (mailSender == null) {
            log.error("❌ SMTP: JavaMailSender bean is not available/configured.");
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(smtpFromEmail != null && !smtpFromEmail.isBlank() ? smtpFromEmail : "noreply@portfolio.com");
            message.setTo(toEmail);
            message.setSubject("Your One-Time Password (OTP) for Portfolio Access");
            message.setText("Hi " + (name != null ? name : "there") + ",\n\nYour OTP to view my contact details is: " + otp
                    + "\n\nThis OTP is valid for 10 minutes.\n\nThank you!");
            mailSender.send(message);
            log.info("✅ SMTP: Successfully sent OTP email to {}", toEmail);
        } catch (org.springframework.mail.MailAuthenticationException e) {
            log.error("❌ SMTP Authentication Failed for {}. Check your Gmail App Password.", smtpFromEmail);
        } catch (Exception e) {
            log.error("❌ SMTP: Failed to send email to {}: {}", toEmail, e.getMessage(), e);
        }
    }
}