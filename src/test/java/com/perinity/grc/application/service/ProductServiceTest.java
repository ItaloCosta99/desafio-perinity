package com.perinity.grc.application.service;

import com.perinity.grc.application.domain.model.Product;
import com.perinity.grc.application.ports.output.ProductRepositoryPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepositoryPort repositoryPort;

    @InjectMocks
    private ProductService service;

    @Test
    void shouldCreateProductSuccessfully() {
        // Arrange
        Product newProduct = new Product();
        newProduct.setName("Amortecedor Dianteiro");
        newProduct.setSalePrice(new BigDecimal("150.00"));

        // Mock
        Mockito.when(repositoryPort.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Product created = service.create(newProduct);

        // Assert
        Assertions.assertNotNull(created.getId());
        Assertions.assertNotNull(created.getCreatedAt());
        Assertions.assertEquals("Amortecedor Dianteiro", created.getName());
    }

    @Test
    void shouldUpdateProductAndKeepHistory() {
        // Arrange
        String id = "prod-123";
        Product existing = new Product();
        existing.setId(id);
        existing.setName("Old Name");
        existing.setCreatedAt(java.time.LocalDate.now().minusDays(5));

        Product updateData = new Product();
        updateData.setName("New Name");
        updateData.setSalePrice(new BigDecimal("200.00"));

        Mockito.when(repositoryPort.findProductById(id)).thenReturn(Optional.of(existing));
        Mockito.when(repositoryPort.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Product updated = service.update(id, updateData);

        // Assert
        Assertions.assertEquals("New Name", updated.getName());
        Assertions.assertEquals(id, updated.getId());
        Assertions.assertNotNull(updated.getCreatedAt());
    }
}