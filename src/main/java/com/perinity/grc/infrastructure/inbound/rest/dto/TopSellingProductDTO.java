package com.perinity.grc.infrastructure.inbound.rest.dto;

import java.math.BigDecimal;

public record TopSellingProductDTO(
    String id,
    String name,
    BigDecimal salePrice,
    BigDecimal totalRevenueGenerated) {
}