package com.sarangsvkm.portfolio_api.service;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    @Value("${resend.from.email:Sarang <onboarding@resend.dev>}")
    private String resendFromEmail;

    @Value("${brevo.api.key:}")
    private String brevoApiKey;

    @Value("${brevo.from.email:sarangsvkm@gmail.com}")
    private String brevoFromEmail;

    @Value("${spring.mail.username:}")
    private String smtpFromEmail;

    private final JavaMailSender mailSender;
    private final RestTemplate restTemplate;

    public EmailService(@org.springframework.beans.factory.annotation.Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.restTemplate = new RestTemplate();
    }

    public void sendOtpEmail(String toEmail, String name, String otp) {
        String recipientName = (name != null && !name.trim().isEmpty()) ? name.trim() : "there";
        String htmlBody = buildHtmlOtpTemplate(recipientName, otp);

        // 1. Try Resend API (HTTP Port 443 - Recommended for Render/Cloud)
        if (resendApiKey != null && !resendApiKey.trim().isEmpty()) {
            boolean sent = sendViaResend(toEmail, recipientName, otp, htmlBody);
            if (sent) return;
        }

        // 2. Try Brevo API (HTTP Port 443 - 300 free emails/day)
        if (brevoApiKey != null && !brevoApiKey.trim().isEmpty()) {
            boolean sent = sendViaBrevo(toEmail, recipientName, htmlBody);
            if (sent) return;
        }

        // 3. Fallback to JavaMailSender SMTP (Gmail / Custom SMTP)
        sendViaSmtp(toEmail, recipientName, otp, htmlBody);
    }

    private boolean sendViaResend(String toEmail, String name, String otp, String htmlBody) {
        try {
            String url = "https://api.resend.com/emails";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(resendApiKey.trim());

            String fromAddress = (resendFromEmail != null && !resendFromEmail.trim().isEmpty())
                    ? resendFromEmail.trim()
                    : "Sarang <onboarding@resend.dev>";

            Map<String, Object> body = new HashMap<>();
            body.put("from", fromAddress);
            body.put("to", Collections.singletonList(toEmail));
            body.put("subject", "Your One-Time Password (OTP) — Sarang S Portfolio");
            body.put("html", htmlBody);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("✅ Resend API: Successfully sent OTP email to {}", toEmail);
                return true;
            } else {
                log.error("❌ Resend API returned error status: {} body: {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("❌ Failed to send email via Resend to {}: {}", toEmail, e.getMessage());
        }
        return false;
    }

    private boolean sendViaBrevo(String toEmail, String name, String htmlBody) {
        try {
            String url = "https://api.brevo.com/v3/smtp/email";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", brevoApiKey.trim());

            Map<String, Object> sender = new HashMap<>();
            sender.put("name", "Sarang S");
            sender.put("email", (brevoFromEmail != null && !brevoFromEmail.isBlank()) ? brevoFromEmail.trim() : "sarangsvkm@gmail.com");

            Map<String, Object> toRecipient = new HashMap<>();
            toRecipient.put("email", toEmail);
            toRecipient.put("name", name);

            Map<String, Object> body = new HashMap<>();
            body.put("sender", sender);
            body.put("to", Collections.singletonList(toRecipient));
            body.put("subject", "Your One-Time Password (OTP) — Sarang S Portfolio");
            body.put("htmlContent", htmlBody);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("✅ Brevo API: Successfully sent OTP email to {}", toEmail);
                return true;
            } else {
                log.error("❌ Brevo API returned error status: {} body: {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("❌ Failed to send email via Brevo to {}: {}", toEmail, e.getMessage());
        }
        return false;
    }

    private void sendViaSmtp(String toEmail, String name, String otp, String htmlBody) {
        if (mailSender == null) {
            log.error("❌ SMTP: JavaMailSender bean is not configured and no API key (Resend/Brevo) provided.");
            return;
        }
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            String from = (smtpFromEmail != null && !smtpFromEmail.isBlank()) ? smtpFromEmail : "sarangsvkm@gmail.com";
            helper.setFrom(from, "Sarang S");
            helper.setTo(toEmail);
            helper.setSubject("Your One-Time Password (OTP) — Sarang S Portfolio");
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
            log.info("✅ SMTP: Successfully sent HTML OTP email to {}", toEmail);
        } catch (org.springframework.mail.MailAuthenticationException e) {
            log.error("❌ SMTP Authentication Failed for {}. If using Gmail, you must generate a 16-character App Password at myaccount.google.com/apppasswords. Normal passwords are rejected.", smtpFromEmail);
        } catch (Exception e) {
            log.error("❌ SMTP: Failed to send email to {}: {}. (Note: Cloud hosts like Render block SMTP port 587 by default; set RESEND_API_KEY instead).", toEmail, e.getMessage());
        }
    }

    private String buildHtmlOtpTemplate(String name, String otp) {
        return "<!DOCTYPE html>\n"
            + "<html lang=\"en\">\n"
            + "<head>\n"
            + "  <meta charset=\"UTF-8\">\n"
            + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
            + "  <title>Your Verification Code</title>\n"
            + "</head>\n"
            + "<body style=\"margin: 0; padding: 0; background-color: #0b0f19; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; -webkit-font-smoothing: antialiased;\">\n"
            + "  <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"background-color: #0b0f19; padding: 40px 15px;\">\n"
            + "    <tr>\n"
            + "      <td align=\"center\">\n"
            + "        <table role=\"presentation\" width=\"100%\" style=\"max-width: 520px; background: #111827; border: 1px solid rgba(255, 255, 255, 0.1); border-radius: 24px; overflow: hidden; box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);\" cellspacing=\"0\" cellpadding=\"0\">\n"
            + "          <tr>\n"
            + "            <td style=\"height: 4px; background: linear-gradient(90deg, #6366f1 0%, #a855f7 50%, #ec4899 100%);\"></td>\n"
            + "          </tr>\n"
            + "          <tr>\n"
            + "            <td style=\"padding: 36px 36px 20px 36px; text-align: center;\">\n"
            + "              <div style=\"display: inline-block; padding: 6px 16px; border-radius: 9999px; background: rgba(99, 102, 241, 0.15); border: 1px solid rgba(99, 102, 241, 0.3); color: #818cf8; font-size: 11px; font-weight: 700; letter-spacing: 2px; text-transform: uppercase; margin-bottom: 16px;\">\n"
            + "                PORTFOLIO ACCESS VERIFICATION\n"
            + "              </div>\n"
            + "              <h1 style=\"margin: 0 0 8px 0; color: #ffffff; font-size: 26px; font-weight: 800; letter-spacing: -0.5px;\">\n"
            + "                Your Verification Code\n"
            + "              </h1>\n"
            + "              <p style=\"margin: 0; color: #94a3b8; font-size: 14px; line-height: 1.5;\">\n"
            + "                Enter this One-Time Password (OTP) to unlock Sarang S's full professional resume & credentials.\n"
            + "              </p>\n"
            + "            </td>\n"
            + "          </tr>\n"
            + "          <tr>\n"
            + "            <td style=\"padding: 0 36px;\">\n"
            + "              <p style=\"margin: 0 0 16px 0; color: #cbd5e1; font-size: 15px; line-height: 1.6;\">\n"
            + "                Hi <strong style=\"color: #ffffff;\">" + escapeHtml(name) + "</strong>,\n"
            + "              </p>\n"
            + "              <p style=\"margin: 0 0 24px 0; color: #94a3b8; font-size: 14px; line-height: 1.6;\">\n"
            + "                Thank you for your interest in my engineering profile. Use the verification code below to access the complete ATS-optimized resume:\n"
            + "              </p>\n"
            + "            </td>\n"
            + "          </tr>\n"
            + "          <tr>\n"
            + "            <td style=\"padding: 0 36px;\">\n"
            + "              <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"background: rgba(99, 102, 241, 0.08); border: 1px dashed rgba(99, 102, 241, 0.4); border-radius: 16px; text-align: center;\">\n"
            + "                <tr>\n"
            + "                  <td style=\"padding: 24px 16px;\">\n"
            + "                    <div style=\"color: #818cf8; font-size: 11px; font-weight: 700; letter-spacing: 2px; text-transform: uppercase; margin-bottom: 8px;\">\n"
            + "                      ONE-TIME PASSWORD\n"
            + "                    </div>\n"
            + "                    <div style=\"font-family: 'Courier New', Courier, monospace; font-size: 38px; font-weight: 800; letter-spacing: 10px; color: #ffffff; text-shadow: 0 0 20px rgba(99, 102, 241, 0.5);\">\n"
            + "                      " + escapeHtml(otp) + "\n"
            + "                    </div>\n"
            + "                  </td>\n"
            + "                </tr>\n"
            + "              </table>\n"
            + "            </td>\n"
            + "          </tr>\n"
            + "          <tr>\n"
            + "            <td style=\"padding: 20px 36px 28px 36px;\">\n"
            + "              <div style=\"background: rgba(255, 255, 255, 0.03); border-radius: 12px; padding: 12px 16px; border: 1px solid rgba(255, 255, 255, 0.05);\">\n"
            + "                <p style=\"margin: 0; color: #94a3b8; font-size: 12px; line-height: 1.6; text-align: center;\">\n"
            + "                  ⏱️ <strong>Valid for 10 minutes.</strong> If you did not request this verification, you can safely disregard this email.\n"
            + "                </p>\n"
            + "              </div>\n"
            + "            </td>\n"
            + "          </tr>\n"
            + "          <tr>\n"
            + "            <td style=\"padding: 0 36px;\">\n"
            + "              <div style=\"height: 1px; background: rgba(255, 255, 255, 0.08);\"></div>\n"
            + "            </td>\n"
            + "          </tr>\n"
            + "          <tr>\n"
            + "            <td style=\"padding: 24px 36px 32px 36px; text-align: center;\">\n"
            + "              <p style=\"margin: 0 0 4px 0; color: #ffffff; font-size: 14px; font-weight: 700;\">\n"
            + "                Sarang S\n"
            + "              </p>\n"
            + "              <p style=\"margin: 0 0 8px 0; color: #64748b; font-size: 12px;\">\n"
            + "                Software Developer • Java & Spring Boot | React.js | React Native | Electron\n"
            + "              </p>\n"
            + "              <p style=\"margin: 0; color: #475569; font-size: 11px;\">\n"
            + "                <a href=\"https://sarangsvkm.in\" style=\"color: #818cf8; text-decoration: none;\">sarangsvkm.in</a> • Alappuzha, Kerala, India\n"
            + "              </p>\n"
            + "            </td>\n"
            + "          </tr>\n"
            + "        </table>\n"
            + "      </td>\n"
            + "    </tr>\n"
            + "  </table>\n"
            + "</body>\n"
            + "</html>";
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }
}
