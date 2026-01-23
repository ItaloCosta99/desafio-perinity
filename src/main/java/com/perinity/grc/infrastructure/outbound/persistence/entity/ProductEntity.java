package com.perinity.grc.infrastructure.outbound.persistence.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;

import java.math.BigDecimal;
import java.time.LocalDate;

@MongoEntity(collection = "products")
public class ProductEntity {
    @BsonId
    public String id;
    public String name;
    public String type;
    public String details;
    public String dimensions;
    public Double weight;
    public BigDecimal purchasePrice;
    public BigDecimal salePrice;
    public LocalDate createdAt;
}