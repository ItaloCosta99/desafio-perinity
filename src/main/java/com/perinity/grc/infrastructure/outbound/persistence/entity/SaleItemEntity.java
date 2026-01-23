package com.perinity.grc.infrastructure.outbound.persistence.entity;

import java.math.BigDecimal;

public class SaleItemEntity {
    public String productId;
    public String productName;
    public Integer quantity;
    public BigDecimal unitPrice;
    public BigDecimal totalItemValue;
}