package com.sarangsvkm.portfolio_api.config;

import com.sarangsvkm.portfolio_api.repository.SystemConfigRepository;
import com.sarangsvkm.portfolio_api.entity.SystemConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    private final SystemConfigRepository repository;

    public MailConfig(SystemConfigRepository repository) {
        this.repository = repository;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        String username = repository.findByConfigKey("spring.mail.username")
                .map(SystemConfig::getConfigValue)
                .orElse("sarangsvkmsuperuser@gmail.com"); // Fallback if not found yet

        String password = repository.findByConfigKey("spring.mail.password")
                .map(SystemConfig::getConfigValue)
                .orElse("kbfaapgyduldupoo"); // Fallback if not found yet

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
