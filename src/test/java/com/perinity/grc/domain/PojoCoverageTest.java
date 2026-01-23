package com.perinity.grc.domain;

import com.perinity.grc.application.domain.exception.NotFoundException;
import com.perinity.grc.application.domain.model.Client;
import com.perinity.grc.infrastructure.inbound.rest.dto.*;
import com.perinity.grc.infrastructure.outbound.persistence.entity.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PojoCoverageTest {

  @Test
  void testClientDomain() {
    // Test constructor with 4 parameters
    Client c1 = new Client("1", "John", "123456789", "john@test.com");
    assertEquals("1", c1.getId());
    assertEquals("John", c1.getFullName());
    assertEquals("123456789", c1.getCpf());
    assertEquals("john@test.com", c1.getEmail());

    // Test default constructor and setters
    Client c2 = new Client();
    c2.setId("2");
    c2.setFullName("Jane");
    c2.setMotherName("Mother");
    c2.setAddress("Address");
    c2.setCpf("987654321");
    c2.setRg("1234567");
    c2.setBirthDate(LocalDate.of(1990, 1, 1));
    c2.setPhoneNumber("1234567890");
    c2.setCreatedAt(LocalDate.now());
    c2.setEmail("jane@test.com");

    assertEquals("2", c2.getId());
    assertEquals("Jane", c2.getFullName());
    assertEquals("Mother", c2.getMotherName());
    assertEquals("Address", c2.getAddress());
    assertEquals("987654321", c2.getCpf());
    assertEquals("1234567", c2.getRg());
    assertEquals(LocalDate.of(1990, 1, 1), c2.getBirthDate());
    assertEquals("1234567890", c2.getPhoneNumber());
    assertNotNull(c2.getCreatedAt());
    assertEquals("jane@test.com", c2.getEmail());
  }

  @Test
  void testExceptions() {
    NotFoundException ex = new NotFoundException("Error");
    assertEquals("Error", ex.getMessage());
  }

  @Test
  void testClientDTOs() {
    CreateClientRequest req = new CreateClientRequest(
        "Name", "Mother", "Addr", "CPF", "RG",
        LocalDate.now(), "Phone", "Email");

    assertEquals("Name", req.fullName());
    assertNotNull(req.toString());
    assertEquals(req, req);
    assertNotEquals(req, new Object());
    assertEquals(req.hashCode(), req.hashCode());

    ClientResponse res = new ClientResponse(
        "1", "Name", "Mother", "Addr", "CPF", "RG",
        LocalDate.now(), "Phone", LocalDate.now(), "Email");
    assertNotNull(res.toString());
    assertEquals("1", res.id());
  }

  @Test
  void testProductDTOs() {
    CreateProductRequest req = new CreateProductRequest(
        "Prod", "Type", "Det", "Dim", 1.0, BigDecimal.TEN, BigDecimal.TEN);
    assertEquals("Prod", req.name());
    assertNotNull(req.toString());

    ProductResponse res = new ProductResponse(
        "1", "Prod", "Type", "Det", "Dim", 1.0,
        BigDecimal.TEN, BigDecimal.TEN, LocalDate.now());
    assertEquals("1", res.id());
  }

  @Test
  void testSaleDTOs() {
    SaleItemRequest itemReq = new SaleItemRequest("p1", 1);
    CreateSaleRequest req = new CreateSaleRequest(
        "c1", "s1", "Seller", List.of(itemReq), "CASH", "0");
    assertEquals("c1", req.clientId());
    assertNotNull(req.toString());

    SaleItemResponse itemRes = new SaleItemResponse("P", 1, BigDecimal.TEN, BigDecimal.TEN);
    SaleResponse res = new SaleResponse(
        "1", "C", "S", LocalDate.now(), List.of(itemRes),
        BigDecimal.TEN, BigDecimal.ONE, BigDecimal.TEN, "CASH");
    assertEquals("1", res.id());
  }

  @Test
  void testReportDTOs() {
    MonthlyRevenueStats.MonthlyRecord record = new MonthlyRevenueStats.MonthlyRecord(
        "JAN", BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ONE);
    MonthlyRevenueStats stats = new MonthlyRevenueStats(
        List.of(record), BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ONE);
    assertNotNull(stats.toString());
    assertNotNull(record.toString());

    TopSellingProductDTO top = new TopSellingProductDTO("1", "N", BigDecimal.TEN, BigDecimal.TEN);
    assertEquals("1", top.id());

    UnsoldProductDTO unsold = new UnsoldProductDTO("N", 1.0, LocalDate.now(), BigDecimal.TEN);
    assertEquals("N", unsold.name());

    NewClientReportDTO newClient = new NewClientReportDTO("1", "N", LocalDate.now(), LocalDate.now());
    assertEquals("1", newClient.id());
  }

  @Test
  void testEntities() {
    ClientEntity ce = new ClientEntity();
    ce.id = "1";
    ce.fullName = "Test";
    assertEquals("1", ce.id);

    ProductEntity pe = new ProductEntity();
    pe.id = "1";
    assertEquals("1", pe.id);

    SaleEntity se = new SaleEntity();
    se.id = "1";
    se.items = Collections.emptyList();

    SaleItemEntity sie = new SaleItemEntity();
    sie.productId = "p1";

    assertEquals("1", se.id);
    assertEquals("p1", sie.productId);
  }

  @Test
  void testFullDtoCoverage() {

    CreateClientRequest req1 = new CreateClientRequest("N", "M", "A", "C", "R", LocalDate.now(), "P", "E");
    CreateClientRequest req2 = new CreateClientRequest("N", "M", "A", "C", "R", LocalDate.now(), "P", "E");
    CreateClientRequest req3 = new CreateClientRequest("X", "M", "A", "C", "R", LocalDate.now(), "P", "E");

    assertCoverage(req1, req2, req3);

    ClientResponse cr1 = new ClientResponse("1", "N", "M", "A", "C", "R", LocalDate.now(), "P", LocalDate.now(), "E");
    ClientResponse cr2 = new ClientResponse("1", "N", "M", "A", "C", "R", LocalDate.now(), "P", LocalDate.now(), "E");
    ClientResponse cr3 = new ClientResponse("2", "N", "M", "A", "C", "R", LocalDate.now(), "P", LocalDate.now(), "E");

    assertCoverage(cr1, cr2, cr3);

    CreateProductRequest pr1 = new CreateProductRequest("N", "T", "D", "Dim", 1.0, BigDecimal.ONE, BigDecimal.TEN);
    CreateProductRequest pr2 = new CreateProductRequest("N", "T", "D", "Dim", 1.0, BigDecimal.ONE, BigDecimal.TEN);
    CreateProductRequest pr3 = new CreateProductRequest("X", "T", "D", "Dim", 1.0, BigDecimal.ONE, BigDecimal.TEN);

    assertCoverage(pr1, pr2, pr3);

    NotFoundException ex = new NotFoundException("msg");
    assertEquals("msg", ex.getMessage());
  }

  private void assertCoverage(Object o1, Object o2, Object o3) {
    // Test equals
    assertEquals(o1, o2);
    assertNotEquals(o1, o3);
    assertNotEquals(o1, null);
    assertNotEquals(o1, new Object());
    assertEquals(o1, o1);

    assertEquals(o1.hashCode(), o2.hashCode());
    assertNotEquals(o1.hashCode(), o3.hashCode());

    assertNotNull(o1.toString());
  }

  @Test
  void testEntitiesFullCoverage() {
    ClientEntity c = new ClientEntity();
    c.id = "1";
    c.fullName = "Name";
    c.motherName = "Mother";

    SaleItemEntity sie = new SaleItemEntity();
    sie.productId = "1";
    sie.quantity = 10;
    sie.unitPrice = BigDecimal.ONE;
    sie.totalItemValue = BigDecimal.TEN;
    sie.productName = "Name";

    assertNotNull(sie.toString());
  }
}