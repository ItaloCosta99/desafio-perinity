package com.perinity.grc.infrastructure.outbound.persistence;

import com.perinity.grc.application.domain.model.Product;
import com.perinity.grc.application.ports.output.ProductRepositoryPort;
import com.perinity.grc.infrastructure.outbound.persistence.entity.ProductEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductRepositoryAdapter implements ProductRepositoryPort, PanacheMongoRepository<ProductEntity> {

    @Override
    public Product save(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.id = product.getId();
        entity.name = product.getName();
        entity.type = product.getType();
        entity.details = product.getDetails();
        entity.dimensions = product.getDimensions();
        entity.weight = product.getWeight();
        entity.purchasePrice = product.getPurchasePrice();
        entity.salePrice = product.getSalePrice();
        entity.createdAt = product.getCreatedAt();

        persistOrUpdate(entity);
        return product;
    }

    @Override
    public Optional<Product> findById(String id) {
        return find("id", id).firstResultOptional()
                .map(this::toDomain);
    }

    @Override
    public List<Product> findAllProducts() {
        return listAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(String id) {
        return delete("id", id) > 0;
    }

    private Product toDomain(ProductEntity entity) {
        Product product = new Product();
        product.setId(entity.id);
        product.setName(entity.name);
        product.setType(entity.type);
        product.setDetails(entity.details);
        product.setDimensions(entity.dimensions);
        product.setWeight(entity.weight);
        product.setPurchasePrice(entity.purchasePrice);
        product.setSalePrice(entity.salePrice);
        product.setCreatedAt(entity.createdAt);
        return product;
    }
}