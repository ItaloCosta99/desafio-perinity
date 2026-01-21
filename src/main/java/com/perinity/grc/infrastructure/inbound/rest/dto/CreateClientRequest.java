package com.perinity.grc.infrastructure.inbound.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record CreateClientRequest(
    @NotBlank(message = "Full name is required")
    String fullName,

    @NotBlank(message = "Mother's name is required")
    String motherName,

    @NotBlank(message = "Address is required")
    String address,

    @NotBlank(message = "CPF is required")
    @CPF(message = "Invalid CPF format")
    String cpf,

    @NotBlank(message = "RG is required")
    String rg,

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    LocalDate birthDate,

    @NotBlank(message = "Phone number is required")
    String phoneNumber,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email
) {}