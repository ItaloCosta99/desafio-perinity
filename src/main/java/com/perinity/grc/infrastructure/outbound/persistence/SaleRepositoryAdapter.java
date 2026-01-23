package com.perinity.grc.infrastructure.outbound.persistence;

import com.perinity.grc.application.domain.model.Sale;
import com.perinity.grc.application.domain.model.SaleItem;
import com.perinity.grc.application.ports.output.SaleRepositoryPort;
import com.perinity.grc.infrastructure.outbound.persistence.entity.SaleEntity;
import com.perinity.grc.infrastructure.outbound.persistence.entity.SaleItemEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SaleRepositoryAdapter implements SaleRepositoryPort, PanacheMongoRepository<SaleEntity> {

    @Override
    public Sale save(Sale sale) {
        SaleEntity entity = toEntity(sale);
        persistOrUpdate(entity);
        return sale;
    }

    @Override
    public List<Sale> findAllSales() {
        return listAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private SaleEntity toEntity(Sale sale) {
        SaleEntity entity = new SaleEntity();
        entity.id = sale.getId();
        entity.clientId = sale.getClientId();
        entity.clientName = sale.getClientName();
        entity.sellerCode = sale.getSellerCode();
        entity.sellerName = sale.getSellerName();
        entity.saleDate = sale.getSaleDate();
        entity.totalAmount = sale.getTotalAmount();
        entity.taxAmount = sale.getTaxAmount();
        entity.finalAmount = sale.getFinalAmount();
        entity.paymentMethod = sale.getPaymentMethod();
        entity.paymentDetails = sale.getPaymentDetails();
        
        if (sale.getItems() != null) {
            entity.items = sale.getItems().stream().map(item -> {
                SaleItemEntity itemEntity = new SaleItemEntity();
                itemEntity.productId = item.getProductId();
                itemEntity.productName = item.getProductName();
                itemEntity.quantity = item.getQuantity();
                itemEntity.unitPrice = item.getUnitPrice();
                itemEntity.totalItemValue = item.getTotalItemValue();
                return itemEntity;
            }).collect(Collectors.toList());
        }
        return entity;
    }

    private Sale toDomain(SaleEntity entity) {
        Sale sale = new Sale();
        sale.setId(entity.id);
        sale.setClientId(entity.clientId);
        sale.setClientName(entity.clientName);
        sale.setSellerCode(entity.sellerCode);
        sale.setSellerName(entity.sellerName);
        sale.setSaleDate(entity.saleDate);
        sale.setTotalAmount(entity.totalAmount);
        sale.setTaxAmount(entity.taxAmount);
        sale.setFinalAmount(entity.finalAmount);
        sale.setPaymentMethod(entity.paymentMethod);
        sale.setPaymentDetails(entity.paymentDetails);

        if (entity.items != null) {
            sale.setItems(entity.items.stream().map(itemEntity -> {
                SaleItem item = new SaleItem();
                item.setProductId(itemEntity.productId);
                item.setProductName(itemEntity.productName);
                item.setQuantity(itemEntity.quantity);
                item.setUnitPrice(itemEntity.unitPrice);
                item.setTotalItemValue(itemEntity.totalItemValue);
                return item;
            }).collect(Collectors.toList()));
        }
        return sale;
    }
}