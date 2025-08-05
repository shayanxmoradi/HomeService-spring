package org.example.homeservice.service.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class EmailService {
    private final JavaMailSender mailSender;


    @Value("${server.port}")
    private int port;

    public void sendActivationEmail(String to, String token) {
        String subject = "account acctivation link for Homeservice  +\n";
        String activationUrl = "http://localhost:" + port + "/activate?token=" + token;
        String message = "open this link to Activate your account: +\n" + activationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }
}
