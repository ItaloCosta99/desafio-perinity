package com.perinity.grc.application.ports.output;

import com.perinity.grc.application.domain.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
    Product save(Product product);
    Optional<Product> findById(String id);
    List<Product> findAllProducts();
    boolean delete(String id);
}