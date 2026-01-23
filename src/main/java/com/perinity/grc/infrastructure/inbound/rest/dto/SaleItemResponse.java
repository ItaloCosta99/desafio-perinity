package com.perinity.grc.infrastructure.inbound.rest.dto;

import java.math.BigDecimal;

public record SaleItemResponse(
    String productName,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal totalItemValue) {
}