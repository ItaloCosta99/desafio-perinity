package com.perinity.grc.infrastructure.outbound.persistence.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@MongoEntity(collection = "sales")
public class SaleEntity {
    @BsonId
    public String id;
    public String clientId;
    public String clientName;
    public String sellerCode;
    public String sellerName;
    public LocalDate saleDate;
    public List<SaleItemEntity> items;
    public BigDecimal totalAmount;
    public BigDecimal taxAmount;
    public BigDecimal finalAmount;
    public String paymentMethod;
    public String paymentDetails;
}