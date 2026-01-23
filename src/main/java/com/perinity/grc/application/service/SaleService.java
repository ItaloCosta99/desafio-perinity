package com.perinity.grc.application.service;

import com.perinity.grc.application.domain.exception.NotFoundException;
import com.perinity.grc.application.domain.model.Client;
import com.perinity.grc.application.domain.model.Product;
import com.perinity.grc.application.domain.model.Sale;
import com.perinity.grc.application.domain.model.SaleItem;
import com.perinity.grc.application.ports.output.ClientRepositoryPort;
import com.perinity.grc.application.ports.output.ProductRepositoryPort;
import com.perinity.grc.application.ports.output.SaleRepositoryPort;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class SaleService {

    private final SaleRepositoryPort saleRepository;
    private final ClientRepositoryPort clientRepository;
    private final ProductRepositoryPort productRepository;

    public SaleService(SaleRepositoryPort saleRepository,
            ClientRepositoryPort clientRepository,
            ProductRepositoryPort productRepository) {
        this.saleRepository = saleRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
    }

    public Sale create(Sale saleInput) {
        Client client = clientRepository.findClientById(saleInput.getClientId())
                .orElseThrow(() -> new NotFoundException("Client not found"));

        Sale newSale = new Sale();
        newSale.setId(UUID.randomUUID().toString());
        newSale.setClientId(client.getId());
        newSale.setClientName(client.getFullName());
        newSale.setSellerCode(saleInput.getSellerCode());
        newSale.setSellerName(saleInput.getSellerName());
        newSale.setPaymentMethod(saleInput.getPaymentMethod());
        newSale.setPaymentDetails(saleInput.getPaymentDetails());
        newSale.setSaleDate(LocalDate.now());

        List<SaleItem> processedItems = new ArrayList<>();

        for (SaleItem itemInput : saleInput.getItems()) {
            Product product = productRepository.findProductById(itemInput.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found: " + itemInput.getProductId()));

            SaleItem validItem = new SaleItem(
                    product.getId(),
                    product.getName(),
                    itemInput.getQuantity(),
                    product.getSalePrice());
            processedItems.add(validItem);
        }
        newSale.setItems(processedItems);

        newSale.calculateTotals();

        return saleRepository.save(newSale);
    }

    public List<Sale> findAll() {
        return saleRepository.findAllSales();
    }
}