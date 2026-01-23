package com.perinity.grc.application.service;

import com.perinity.grc.application.domain.model.Product;
import com.perinity.grc.application.domain.exception.NotFoundException;
import com.perinity.grc.application.ports.output.ProductRepositoryPort;
import com.perinity.grc.infrastructure.inbound.rest.dto.UnsoldProductDTO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

        @Mock
        private ProductRepositoryPort repository;

        @InjectMocks
        private ProductService service;

        @Test
        void shouldCreateProductWithGeneratedIdWhenIdIsNull() {
                // Arrange
                Product product = new Product();
                product.setId(null);
                product.setName("Test Product");

                Mockito.when(repository.save(any(Product.class)))
                                .thenAnswer(i -> i.getArgument(0));

                // Act
                Product created = service.create(product);

                // Assert
                Assertions.assertNotNull(created.getId());
                Assertions.assertNotNull(created.getCreatedAt());
                Mockito.verify(repository).save(any(Product.class));
        }

        @Test
        void shouldCreateProductWithGeneratedIdWhenIdIsEmpty() {
                // Arrange
                Product product = new Product();
                product.setId("");
                product.setName("Test Product");

                Mockito.when(repository.save(any(Product.class)))
                                .thenAnswer(i -> i.getArgument(0));

                // Act
                Product created = service.create(product);

                // Assert
                Assertions.assertNotNull(created.getId());
                Assertions.assertFalse(created.getId().isEmpty());
                Assertions.assertNotNull(created.getCreatedAt());
        }

        @Test
        void shouldCreateProductWithGeneratedCreatedAtWhenNull() {
                // Arrange
                Product product = new Product();
                product.setId("prod-123");
                product.setName("Test Product");
                product.setCreatedAt(null);

                Mockito.when(repository.save(any(Product.class)))
                                .thenAnswer(i -> i.getArgument(0));

                // Act
                Product created = service.create(product);

                // Assert
                Assertions.assertEquals("prod-123", created.getId());
                Assertions.assertNotNull(created.getCreatedAt());
                Assertions.assertEquals(LocalDate.now(), created.getCreatedAt());
        }

        @Test
        void shouldCreateProductWithoutGeneratingWhenBothFieldsPresent() {
                // Arrange
                String expectedId = "existing-id";
                LocalDate expectedDate = LocalDate.of(2025, 1, 1);

                Product product = new Product();
                product.setId(expectedId);
                product.setName("Test Product");
                product.setCreatedAt(expectedDate);

                Mockito.when(repository.save(any(Product.class)))
                                .thenAnswer(i -> i.getArgument(0));

                // Act
                Product created = service.create(product);

                // Assert
                Assertions.assertEquals(expectedId, created.getId());
                Assertions.assertEquals(expectedDate, created.getCreatedAt());
        }

        @Test
        void shouldFindProductById() {
                // Arrange
                Product product = new Product();
                product.setId("prod-1");
                Mockito.when(repository.findProductById("prod-1"))
                                .thenReturn(Optional.of(product));

                // Act
                Optional<Product> found = service.findById("prod-1");

                // Assert
                Assertions.assertTrue(found.isPresent());
                Assertions.assertEquals("prod-1", found.get().getId());
        }

        @Test
        void shouldReturnEmptyWhenProductNotFound() {
                // Arrange
                Mockito.when(repository.findProductById("invalid-id"))
                                .thenReturn(Optional.empty());

                // Act
                Optional<Product> found = service.findById("invalid-id");

                // Assert
                Assertions.assertFalse(found.isPresent());
        }

        @Test
        void shouldFindAllProducts() {
                // Arrange
                Product prod1 = new Product();
                prod1.setId("1");
                Product prod2 = new Product();
                prod2.setId("2");

                Mockito.when(repository.findAllProducts())
                                .thenReturn(List.of(prod1, prod2));

                // Act
                List<Product> products = service.findAll();

                // Assert
                Assertions.assertEquals(2, products.size());
        }

        @Test
        void shouldDeleteProduct() {
                // Arrange
                Mockito.when(repository.deleteProduct("prod-1")).thenReturn(true);

                // Act
                boolean deleted = service.delete("prod-1");

                // Assert
                Assertions.assertTrue(deleted);
        }

        @Test
        void shouldReturnFalseWhenProductNotDeletedDoesNotExist() {
                // Arrange
                Mockito.when(repository.deleteProduct("invalid-id")).thenReturn(false);

                // Act
                boolean deleted = service.delete("invalid-id");

                // Assert
                Assertions.assertFalse(deleted);
        }

        @Test
        void shouldFindAllProductsEmpty() {
                // Arrange
                Mockito.when(repository.findAllProducts())
                                .thenReturn(List.of());

                // Act
                List<Product> products = service.findAll();

                // Assert
                Assertions.assertTrue(products.isEmpty());
        }

        @Test
        void shouldUpdateProduct() {
                // Arrange
                Product existing = new Product();
                existing.setId("prod-1");
                existing.setName("Old Name");
                existing.setWeight(1.0);
                existing.setPurchasePrice(BigDecimal.TEN);

                Product updateData = new Product();
                updateData.setName("New Name");
                updateData.setType("NewType");
                updateData.setDetails("NewDetails");
                updateData.setDimensions("NewDimensions");
                updateData.setWeight(2.0);
                updateData.setPurchasePrice(BigDecimal.valueOf(20));
                updateData.setSalePrice(BigDecimal.valueOf(30));

                Mockito.when(repository.findProductById("prod-1"))
                                .thenReturn(Optional.of(existing));
                Mockito.when(repository.save(any(Product.class)))
                                .thenAnswer(i -> i.getArgument(0));

                // Act
                Product updated = service.update("prod-1", updateData);

                // Assert
                Assertions.assertEquals("New Name", updated.getName());
                Assertions.assertEquals("NewType", updated.getType());
                Assertions.assertEquals(2.0, updated.getWeight());
                Mockito.verify(repository).save(any(Product.class));
        }

        @Test
        void shouldThrowExceptionWhenUpdatingNonExistentProduct() {
                // Arrange
                Product updateData = new Product();
                Mockito.when(repository.findProductById("invalid-id"))
                                .thenReturn(Optional.empty());

                // Act & Assert
                NotFoundException exception = Assertions.assertThrows(
                                NotFoundException.class,
                                () -> service.update("invalid-id", updateData));

                Assertions.assertTrue(exception.getMessage().contains("Product not found"));
        }

        @Test
        void shouldGetOldestProductsReportEmpty() {
                // Arrange
                Mockito.when(repository.findOldestProducts(3))
                                .thenReturn(List.of());

                // Act
                List<UnsoldProductDTO> report = service.getOldestProductsReport();

                // Assert
                Assertions.assertTrue(report.isEmpty());
        }

        @Test
        void shouldGetOldestProductsReportSortedByPrice() {
                // Arrange
                Product prod1 = new Product();
                prod1.setName("Product 1");
                prod1.setWeight(1.0);
                prod1.setCreatedAt(LocalDate.now());
                prod1.setPurchasePrice(BigDecimal.valueOf(100));

                Product prod2 = new Product();
                prod2.setName("Product 2");
                prod2.setWeight(2.0);
                prod2.setCreatedAt(LocalDate.now());
                prod2.setPurchasePrice(BigDecimal.valueOf(50));

                Product prod3 = new Product();
                prod3.setName("Product 3");
                prod3.setWeight(3.0);
                prod3.setCreatedAt(LocalDate.now());
                prod3.setPurchasePrice(BigDecimal.valueOf(200));

                Mockito.when(repository.findOldestProducts(3))
                                .thenReturn(List.of(prod1, prod2, prod3));

                // Act
                List<UnsoldProductDTO> report = service.getOldestProductsReport();

                // Assert
                Assertions.assertEquals(3, report.size());
                // Verificar ordem decrescente por preço
                Assertions.assertEquals(0, BigDecimal.valueOf(200)
                                .compareTo(report.get(0).purchasePrice()));
                Assertions.assertEquals(0, BigDecimal.valueOf(100)
                                .compareTo(report.get(1).purchasePrice()));
                Assertions.assertEquals(0, BigDecimal.valueOf(50)
                                .compareTo(report.get(2).purchasePrice()));
        }
}