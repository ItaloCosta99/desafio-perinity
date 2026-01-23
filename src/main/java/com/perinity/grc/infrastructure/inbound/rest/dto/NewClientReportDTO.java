package com.perinity.grc.infrastructure.inbound.rest.dto;

import java.time.LocalDate;

public record NewClientReportDTO(
    String id,
    String name,
    LocalDate registerDate,
    LocalDate birthDate) {
}