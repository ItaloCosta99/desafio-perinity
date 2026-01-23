package com.perinity.grc.application.domain.model;

import java.math.BigDecimal;

public class SaleItem {
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalItemValue;

    public SaleItem() {}

    public SaleItem(String productId, String productName, Integer quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalItemValue = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getTotalItemValue() { return totalItemValue; }
    public void setTotalItemValue(BigDecimal totalItemValue) { this.totalItemValue = totalItemValue; }
}