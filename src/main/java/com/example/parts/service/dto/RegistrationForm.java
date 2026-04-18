package com.example.parts.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationForm {

    @NotBlank(message = "სახელი სავალდებულოა")
    @Size(min = 2, max = 100, message = "სახელი უნდა იყოს 2-100 სიმბოლო")
    private String fullName;

    @NotBlank(message = "ელ-ფოსტა სავალდებულოა")
    @Email(message = "შეიყვანე სწორი ელ-ფოსტა")
    private String email;

    @NotBlank(message = "ტელეფონი სავალდებულოა")
    @Size(min = 9, max = 30, message = "ტელეფონი უნდა იყოს 9-30 სიმბოლო")
    private String phone;

    @NotBlank(message = "პაროლი სავალდებულოა")
    @Size(min = 6, max = 50, message = "პაროლი მინიმუმ 6 სიმბოლო უნდა იყოს")
    private String password;

    @NotBlank(message = "გაიმეორე პაროლი")
    private String confirmPassword;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
