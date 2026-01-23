package com.perinity.grc.infrastructure.inbound.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

public record CreateSaleRequest(
    @NotBlank(message = "Client ID is required")
    String clientId,

    @NotBlank(message = "Seller Code is required")
    String sellerCode,

    @NotBlank(message = "Seller Name is required")
    String sellerName,

    @NotEmpty(message = "The sale must have at least one item")
    @Valid
    List<SaleItemRequest> items,

    @NotBlank(message = "Payment method is required")
    @Schema(examples = "CREDIT_CARD")
    String paymentMethod,

    @Schema(examples = "5212458646865736")
    String paymentDetails
) {}