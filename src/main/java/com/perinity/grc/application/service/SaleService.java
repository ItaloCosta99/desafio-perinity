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

import java.math.BigDecimal;
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

    public com.perinity.grc.infrastructure.inbound.rest.dto.MonthlyRevenueStats generateMonthlyReport(
            LocalDate referenceDate) {
        LocalDate startDate = referenceDate.minusMonths(11).withDayOfMonth(1);

        List<Sale> sales = saleRepository.findSalesBetween(startDate, referenceDate);

        var groupedByMonth = sales.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        s -> s.getSaleDate().getYear() + "-" + String.format("%02d", s.getSaleDate().getMonthValue())));

        List<com.perinity.grc.infrastructure.inbound.rest.dto.MonthlyRevenueStats.MonthlyRecord> records = new ArrayList<>();
        BigDecimal globalTotalRevenue = BigDecimal.ZERO;
        BigDecimal globalTotalTax = BigDecimal.ZERO;
        BigDecimal globalTotalFinal = BigDecimal.ZERO;

        for (int i = 0; i < 12; i++) {
            LocalDate currentMonthDate = startDate.plusMonths(i);
            String key = currentMonthDate.getYear() + "-" + String.format("%02d", currentMonthDate.getMonthValue());
            String label = currentMonthDate.getMonth().name().substring(0, 3) + "/" + currentMonthDate.getYear(); // ex:
                                                                                                                  // JAN/2025

            List<Sale> monthSales = groupedByMonth.getOrDefault(key, java.util.Collections.emptyList());

            BigDecimal monthRevenue = monthSales.stream().map(Sale::getTotalAmount).reduce(BigDecimal.ZERO,
                    BigDecimal::add);
            BigDecimal monthTax = monthSales.stream().map(Sale::getTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal monthFinal = monthSales.stream().map(Sale::getFinalAmount).reduce(BigDecimal.ZERO,
                    BigDecimal::add);

            records.add(new com.perinity.grc.infrastructure.inbound.rest.dto.MonthlyRevenueStats.MonthlyRecord(
                    label, monthRevenue, monthTax, monthFinal));

            globalTotalRevenue = globalTotalRevenue.add(monthRevenue);
            globalTotalTax = globalTotalTax.add(monthTax);
            globalTotalFinal = globalTotalFinal.add(monthFinal);
        }

        return new com.perinity.grc.infrastructure.inbound.rest.dto.MonthlyRevenueStats(
                records, globalTotalRevenue, globalTotalTax, globalTotalFinal);
    }
}