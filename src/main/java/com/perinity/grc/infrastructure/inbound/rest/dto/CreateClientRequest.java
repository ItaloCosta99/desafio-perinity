package com.perinity.grc.infrastructure.inbound.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record CreateClientRequest(
    
    @Schema(examples = "João Mário", description = "Nome completo do cliente")
    @NotBlank(message = "Full name is required")
    String fullName,

    @Schema(examples = "Maria da Silva", description = "Nome da mãe")
    @NotBlank(message = "Mother's name is required")
    String motherName,

    @Schema(examples = "Rua das Flores, 123 - São Paulo/SP", description = "Endereço completo")
    @NotBlank(message = "Address is required")
    String address,

    @Schema(examples = "784.714.620-79", description = "CPF válido com pontuação")
    @NotBlank(message = "CPF is required")
    @CPF(message = "Invalid CPF format")
    String cpf,

    @Schema(examples = "33.546.648-5", description = "Registro Geral (RG)")
    @NotBlank(message = "RG is required")
    String rg, // Renomeado de identityCard

    @Schema(examples = "1990-05-20", description = "Data de nascimento (AAAA-MM-DD)")
    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    LocalDate birthDate,

    @Schema(examples = "(11) 99999-8888", description = "Telefone celular com DDD")
    @NotBlank(message = "Phone number is required")
    String phoneNumber,

    @Schema(examples = "joao.silva@email.com", description = "E-mail para contato")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email
) {}