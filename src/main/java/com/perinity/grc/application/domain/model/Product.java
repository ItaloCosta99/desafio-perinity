package com.perinity.grc.application.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Product {
    private String id;
    private String name;
    private String type;
    private String details;
    private String dimensions;
    private Double weight;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private LocalDate createdAt;

    public Product() {}

    public Product(String id, String name, BigDecimal salePrice) {
        this.id = id;
        this.name = name;
        this.salePrice = salePrice;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }

    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}
