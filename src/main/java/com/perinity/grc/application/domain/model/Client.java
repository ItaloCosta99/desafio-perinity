package com.perinity.grc.application.domain.model;

import java.time.LocalDate;

public class Client {
    private String id; 
    private String fullName;
    private String motherName;
    private String address;
    private String cpf;
    private String rg;
    private LocalDate birthDate;
    private String phoneNumber;
    private LocalDate createdAt;
    private String email;

    public Client() {}

    public Client(String id, String fullName, String cpf, String email) {
        this.id = id;
        this.fullName = fullName;
        this.cpf = cpf;
        this.email = email;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}