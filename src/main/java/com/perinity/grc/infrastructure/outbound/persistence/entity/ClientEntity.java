package com.perinity.grc.infrastructure.outbound.persistence.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;

import java.time.LocalDate;

@MongoEntity(collection = "clients")
public class ClientEntity {

    @BsonId
    public String id;
    
    public String fullName;
    public String motherName;
    public String address;
    public String cpf;
    public String rg;
    public LocalDate birthDate;
    public String phoneNumber;
    public LocalDate createdAt;
    public String email;

}