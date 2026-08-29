package fr._42.cinema.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendConfirmationEmail(String toEmail, String confirmationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Confirm your Cinema account");
        message.setText(
                "Welcome to Cinema!\n\n" +
                        "Please confirm your account by clicking the link below:\n" +
                        confirmationLink + "\n\n" +
                        "If you did not create this account, you can ignore this email."
        );
        mailSender.send(message);
    }

}
