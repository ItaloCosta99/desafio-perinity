package com.perinity.grc.application.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Sale {
    private String id;
    private String clientId;
    private String clientName;
    private String sellerCode; 
    private String sellerName; 
    private LocalDate saleDate;
    private List<SaleItem> items = new ArrayList<>();
    private BigDecimal totalAmount; 
    private BigDecimal taxAmount;   
    private BigDecimal finalAmount; 
    private String paymentMethod;   
    private String paymentDetails;

    public void calculateTotals() {
        this.totalAmount = items.stream()
                .map(SaleItem::getTotalItemValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.taxAmount = this.totalAmount.multiply(new BigDecimal("0.09"));
        
        this.finalAmount = this.totalAmount.add(this.taxAmount);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getSellerCode() { return sellerCode; }
    public void setSellerCode(String sellerCode) { this.sellerCode = sellerCode; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public LocalDate getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }

    public List<SaleItem> getItems() { return items; }
    public void setItems(List<SaleItem> items) { this.items = items; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public BigDecimal getFinalAmount() { return finalAmount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentDetails() { return paymentDetails; }
    public void setPaymentDetails(String paymentDetails) { this.paymentDetails = paymentDetails; }
}