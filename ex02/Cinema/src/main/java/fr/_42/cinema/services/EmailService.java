package fr._42.cinema.services;

public interface EmailService {

    void sendConfirmationEmail(String toEmail, String confirmationLink);
}
