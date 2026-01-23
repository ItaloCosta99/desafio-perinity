package com.perinity.grc.infrastructure.outbound.persistence;

import com.perinity.grc.application.domain.model.Product;
import com.perinity.grc.application.ports.output.ProductRepositoryPort;
import com.perinity.grc.infrastructure.outbound.persistence.entity.ProductEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductRepositoryAdapter
        implements ProductRepositoryPort, PanacheMongoRepositoryBase<ProductEntity, String> {

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
    public Optional<Product> findProductById(String id) {
        return findByIdOptional(id)
                .map(this::toDomain);
    }

    @Override
    public List<Product> findAllProducts() {
        return listAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteProduct(String id) {
        return delete("id", id) > 0;
    }

    @Override
    public List<Product> findOldestProducts(int limit) {
        return findAll(Sort.ascending("createdAt"))
                .page(0, limit)
                .list()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
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