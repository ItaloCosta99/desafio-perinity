package com.perinity.grc.application.service;

import com.perinity.grc.application.domain.exception.NotFoundException;
import com.perinity.grc.application.domain.model.Client;
import com.perinity.grc.application.domain.model.Product;
import com.perinity.grc.application.domain.model.Sale;
import com.perinity.grc.application.domain.model.SaleItem;
import com.perinity.grc.application.ports.output.ClientRepositoryPort;
import com.perinity.grc.application.ports.output.ProductRepositoryPort;
import com.perinity.grc.application.ports.output.SaleRepositoryPort;
import com.perinity.grc.infrastructure.inbound.rest.dto.MonthlyRevenueStats;
import com.perinity.grc.infrastructure.inbound.rest.dto.TopSellingProductDTO;

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
class SaleServiceTest {

    @Mock
    private SaleRepositoryPort saleRepository;
    @Mock
    private ClientRepositoryPort clientRepository;
    @Mock
    private ProductRepositoryPort productRepository;

    @InjectMocks
    private SaleService service;

    @Test
    void shouldCalculateTotalsAndTaxCorrectly() {
        // Arrange
        Client client = new Client();
        client.setId("client-1");
        client.setFullName("Maria Client");
        Mockito.when(clientRepository.findClientById("client-1")).thenReturn(Optional.of(client));

        Product product = new Product();
        product.setId("prod-1");
        product.setName("Product A");
        product.setSalePrice(new BigDecimal("100.00"));
        Mockito.when(productRepository.findProductById("prod-1")).thenReturn(Optional.of(product));

        Mockito.when(saleRepository.save(any(Sale.class))).thenAnswer(i -> i.getArgument(0));

        Sale inputSale = new Sale();
        inputSale.setClientId("client-1");
        SaleItem itemInput = new SaleItem();
        itemInput.setProductId("prod-1");
        itemInput.setQuantity(2);
        inputSale.setItems(List.of(itemInput));

        // Act
        Sale createdSale = service.create(inputSale);

        // Assert
        Assertions.assertEquals(0, new BigDecimal("200.00").compareTo(createdSale.getTotalAmount()));
        Assertions.assertEquals(0, new BigDecimal("18.00").compareTo(createdSale.getTaxAmount()));
        Assertions.assertEquals(0, new BigDecimal("218.00").compareTo(createdSale.getFinalAmount()));

        Assertions.assertEquals("Maria Client", createdSale.getClientName());
        Assertions.assertNotNull(createdSale.getSaleDate());
    }

    @Test
    void shouldThrowException_WhenClientNotFoundOnCreate() {
        // Arrange
        Mockito.when(clientRepository.findClientById("invalid-client"))
                .thenReturn(Optional.empty());

        Sale inputSale = new Sale();
        inputSale.setClientId("invalid-client");
        SaleItem itemInput = new SaleItem();
        itemInput.setProductId("prod-1");
        itemInput.setQuantity(2);
        inputSale.setItems(List.of(itemInput));

        // Act & Assert
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> service.create(inputSale));

        Assertions.assertTrue(exception.getMessage().contains("Client not found"));
    }

    @Test
    void shouldThrowException_WhenProductNotFoundOnCreate() {
        // Arrange
        Client client = new Client();
        client.setId("client-1");
        client.setFullName("Maria Client");
        Mockito.when(clientRepository.findClientById("client-1"))
                .thenReturn(Optional.of(client));

        Mockito.when(productRepository.findProductById("invalid-product"))
                .thenReturn(Optional.empty());

        Sale inputSale = new Sale();
        inputSale.setClientId("client-1");
        SaleItem itemInput = new SaleItem();
        itemInput.setProductId("invalid-product");
        itemInput.setQuantity(2);
        inputSale.setItems(List.of(itemInput));

        // Act & Assert
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> service.create(inputSale));

        Assertions.assertTrue(exception.getMessage().contains("Product not found"));
    }

    @Test
    void shouldFindAllSales() {
        // Arrange
        Sale sale1 = new Sale();
        sale1.setId("sale-1");
        Sale sale2 = new Sale();
        sale2.setId("sale-2");

        Mockito.when(saleRepository.findAllSales())
                .thenReturn(List.of(sale1, sale2));

        // Act
        List<Sale> result = service.findAll();

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("sale-1", result.get(0).getId());
    }

    @Test
    void shouldGenerateMonthlyReport() {
        // Arrange
        LocalDate referenceDate = LocalDate.of(2025, 1, 15);
        LocalDate startDate = LocalDate.of(2024, 2, 1);

        Sale sale1 = new Sale();
        sale1.setId("sale-1");
        sale1.setSaleDate(LocalDate.of(2025, 1, 10));
        sale1.setTotalAmount(BigDecimal.valueOf(100));
        sale1.setTaxAmount(BigDecimal.valueOf(9));
        sale1.setFinalAmount(BigDecimal.valueOf(109));

        Mockito.when(saleRepository.findSalesBetween(startDate, referenceDate))
                .thenReturn(List.of(sale1));

        // Act
        MonthlyRevenueStats report = service.generateMonthlyReport(referenceDate);

        // Assert
        Assertions.assertNotNull(report);
        Assertions.assertEquals(12, report.monthlyRecords().size());
        Assertions.assertEquals(0, BigDecimal.valueOf(100)
                .compareTo(report.totalRevenue()));
    }

    @Test
    void shouldHandleDeletedProductInTopSellingReport() {
        // Arrange
        SaleItem item = new SaleItem();
        item.setProductId("deleted-prod-id");
        item.setTotalItemValue(BigDecimal.TEN);

        Sale sale = new Sale();
        sale.setItems(List.of(item));

        Mockito.when(saleRepository.findAllSales()).thenReturn(List.of(sale));

        Mockito.when(productRepository.findProductById("deleted-prod-id"))
                .thenReturn(Optional.empty());

        // Act
        List<TopSellingProductDTO> report = service.getTopSellingProductsReport();

        // Assert
        Assertions.assertFalse(report.isEmpty());
        Assertions.assertEquals("Produto Desconhecido", report.get(0).name());
        Assertions.assertEquals("deleted-prod-id", report.get(0).id());
    }

    @Test
    void shouldGetEmptyTopSellingProductsReport() {
        // Arrange
        Mockito.when(saleRepository.findAllSales())
                .thenReturn(List.of());

        // Act
        List<TopSellingProductDTO> report = service.getTopSellingProductsReport();

        // Assert
        Assertions.assertTrue(report.isEmpty());
    }
}