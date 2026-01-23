package com.perinity.grc.infrastructure.inbound.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SaleResponse(
    String id,
    String clientName,
    String sellerName,
    LocalDate saleDate,
    List<SaleItemResponse> items,
    BigDecimal totalAmount,
    BigDecimal taxAmount,
    BigDecimal finalAmount,
    String paymentMethod) {
}