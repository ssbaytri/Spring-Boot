package fr._42.cinema.dto;

import fr._42.cinema.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class SignUpRequestDTO {

    @NotEmpty(message = "{errors.firstName.required}")
    private String firstName;

    @NotEmpty(message = "{errors.lastName.required}")
    private String lastName;

    @NotEmpty(message = "{errors.phoneNumber.required}")
    @Pattern(regexp = "^\\+\\d{1,3}\\(\\d+\\)\\d+$", message = "{errors.incorrect.phone}")
    private String phoneNumber;

    @NotEmpty(message = "{errors.email.required}")
    @Email(message = "{errors.incorrect.email}")
    private String email;

    @NotEmpty(message = "{errors.password.required}")
    @ValidPassword(message = "{errors.incorrect.password}")
    private String password;

    public SignUpRequestDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}