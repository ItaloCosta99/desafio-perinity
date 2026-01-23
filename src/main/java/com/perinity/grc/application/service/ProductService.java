package com.perinity.grc.application.service;

import com.perinity.grc.application.domain.model.Product;
import com.perinity.grc.application.domain.exception.NotFoundException;
import com.perinity.grc.application.ports.output.ProductRepositoryPort;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ProductService {

    private final ProductRepositoryPort repository;

    public ProductService(ProductRepositoryPort repository) {
        this.repository = repository;
    }

    public Product create(Product product) {
        if (product.getId() == null || product.getId().isEmpty()) {
            product.setId(UUID.randomUUID().toString());
        }
        if (product.getCreatedAt() == null) {
            product.setCreatedAt(LocalDate.now());
        }
        return repository.save(product);
    }

    public Optional<Product> findById(String id) {
        return repository.findProductById(id);
    }

    public List<Product> findAll() {
        return repository.findAllProducts();
    }

    public boolean delete(String id) {
        return repository.deleteProduct(id);
    }

    public Product update(String id, Product data) {
        return repository.findProductById(id)
                .map(existing -> {
                    existing.setName(data.getName());
                    existing.setType(data.getType());
                    existing.setDetails(data.getDetails());
                    existing.setDimensions(data.getDimensions());
                    existing.setWeight(data.getWeight());
                    existing.setPurchasePrice(data.getPurchasePrice());
                    existing.setSalePrice(data.getSalePrice());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }
}