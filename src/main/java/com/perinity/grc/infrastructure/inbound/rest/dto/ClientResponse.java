package com.perinity.grc.infrastructure.inbound.rest.dto;

import java.time.LocalDate;

public record ClientResponse(
    String id,
    String fullName,
    String motherName,
    String address,
    String cpf,
    String rg,
    LocalDate birthDate,
    String phoneNumber,
    LocalDate createdAt,
    String email
) {}