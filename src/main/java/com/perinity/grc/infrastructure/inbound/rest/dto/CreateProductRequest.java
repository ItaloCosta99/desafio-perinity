package com.perinity.grc.infrastructure.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

public record CreateProductRequest(
    @NotBlank(message = "Name is required")
    @Schema(examples = "Amortecedor Dianteiro")
    String name,

    @NotBlank(message = "Type is required")
    @Schema(examples = "Peça Mecânica")
    String type,

    @Schema(examples = "Compatível com Honda Civic 2020")
    String details,

    @Schema(examples = "30x10x10 cm")
    String dimensions,

    @Positive(message = "Weight must be positive")
    @Schema(examples = "2.5")
    Double weight,

    @NotNull(message = "Purchase price is required")
    @Positive(message = "Price must be positive")
    @Schema(examples = "100.00")
    BigDecimal purchasePrice,

    @NotNull(message = "Sale price is required")
    @Positive(message = "Price must be positive")
    @Schema(examples = "150.00")
    BigDecimal salePrice
) {}