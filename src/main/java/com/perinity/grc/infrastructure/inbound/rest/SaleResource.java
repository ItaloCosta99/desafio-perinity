package com.perinity.grc.infrastructure.inbound.rest;

import com.perinity.grc.application.domain.model.Sale;
import com.perinity.grc.application.domain.model.SaleItem;
import com.perinity.grc.application.service.SaleService;
import com.perinity.grc.infrastructure.inbound.rest.dto.CreateSaleRequest;
import com.perinity.grc.infrastructure.inbound.rest.dto.MonthlyRevenueStats;
import com.perinity.grc.infrastructure.inbound.rest.dto.SaleItemResponse;
import com.perinity.grc.infrastructure.inbound.rest.dto.SaleResponse;
import com.perinity.grc.infrastructure.inbound.rest.dto.TopSellingProductDTO;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Path("/sales")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SaleResource {

  private final SaleService service;

  @Inject
  public SaleResource(SaleService service) {
    this.service = service;
  }

  @POST
  public Response create(@Valid CreateSaleRequest request) {
    Sale domainInput = new Sale();
    domainInput.setClientId(request.clientId());
    domainInput.setSellerCode(request.sellerCode());
    domainInput.setSellerName(request.sellerName());
    domainInput.setPaymentMethod(request.paymentMethod());
    domainInput.setPaymentDetails(request.paymentDetails());

    List<SaleItem> items = request.items().stream().map(i -> {
      SaleItem item = new SaleItem();
      item.setProductId(i.productId());
      item.setQuantity(i.quantity());
      return item;
    }).collect(Collectors.toList());
    domainInput.setItems(items);

    Sale created = service.create(domainInput);

    return Response.created(URI.create("/sales/" + created.getId()))
        .entity(toResponse(created))
        .build();
  }

  @GET
  public List<SaleResponse> listAll() {
    return service.findAll().stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  private SaleResponse toResponse(Sale sale) {
    List<SaleItemResponse> itemResponses = sale.getItems().stream()
        .map(i -> new SaleItemResponse(
            i.getProductName(),
            i.getQuantity(),
            i.getUnitPrice(),
            i.getTotalItemValue()))
        .collect(Collectors.toList());

    return new SaleResponse(
        sale.getId(),
        sale.getClientName(),
        sale.getSellerName(),
        sale.getSaleDate(),
        itemResponses,
        sale.getTotalAmount(),
        sale.getTaxAmount(),
        sale.getFinalAmount(),
        sale.getPaymentMethod());
  }

  @GET
  @Path("/report/monthly")
  public MonthlyRevenueStats getMonthlyReport(
      @QueryParam("date") String dateStr) {

    LocalDate date = (dateStr == null) ? LocalDate.now() : LocalDate.parse(dateStr);
    return service.generateMonthlyReport(date);
  }

  @GET
  @Path("/report/top-selling")
  public List<TopSellingProductDTO> getTopSellingReport() {
    return service.getTopSellingProductsReport();
  }
}