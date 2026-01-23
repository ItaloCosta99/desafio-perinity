package com.perinity.grc.infrastructure.inbound.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UnsoldProductDTO(
    String name,
    Double weight,
    LocalDate createdAt,
    BigDecimal purchasePrice) {
}