package com.perinity.grc.infrastructure.inbound.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductResponse(
    String id,
    String name,
    String type,
    String details,
    String dimensions,
    Double weight,
    BigDecimal purchasePrice,
    BigDecimal salePrice,
    LocalDate createdAt
) {}