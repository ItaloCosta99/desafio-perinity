package com.perinity.grc.application.service;

import com.perinity.grc.application.domain.model.Client;
import com.perinity.grc.application.domain.model.Product;
import com.perinity.grc.application.domain.model.Sale;
import com.perinity.grc.application.domain.model.SaleItem;
import com.perinity.grc.application.ports.output.ClientRepositoryPort;
import com.perinity.grc.application.ports.output.ProductRepositoryPort;
import com.perinity.grc.application.ports.output.SaleRepositoryPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
}