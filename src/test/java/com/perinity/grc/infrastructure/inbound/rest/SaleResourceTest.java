package com.perinity.grc.infrastructure.inbound.rest;

import com.perinity.grc.infrastructure.inbound.rest.dto.CreateClientRequest;
import com.perinity.grc.infrastructure.inbound.rest.dto.CreateProductRequest;
import com.perinity.grc.infrastructure.inbound.rest.dto.CreateSaleRequest;
import com.perinity.grc.infrastructure.inbound.rest.dto.SaleItemRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
class SaleResourceTest {

    @Test
    void testSaleFlow() {
        CreateClientRequest clientReq = new CreateClientRequest(
                "Sale Client", "Mother", "Address", "549.959.880-09", "12.345.678-9", LocalDate.of(1999, 1, 1),
                "99999-9999",
                "client@test.com");
        String clientId = given().contentType(ContentType.JSON).body(clientReq)
                .post("/clients").then().statusCode(201).extract().path("id");

        CreateProductRequest prodReq = new CreateProductRequest(
                "Sale Prod", "Type", "Details", "Dimensions", 1.0, BigDecimal.TEN, BigDecimal.TEN);
        String prodId = given().contentType(ContentType.JSON).body(prodReq)
                .post("/products").then().statusCode(201).extract().path("id");

        CreateSaleRequest saleReq = new CreateSaleRequest(
                clientId, "S1", "Seller",
                List.of(new SaleItemRequest(prodId, 1)),
                "CASH", "10");

        given()
                .contentType(ContentType.JSON)
                .body(saleReq)
                .when().post("/sales")
                .then().log().ifValidationFails().statusCode(201);

        given().when().get("/sales").then().statusCode(200);

        given().when().get("/sales/report/monthly").then().statusCode(200);
        given().when().get("/sales/report/top-selling").then().statusCode(200);
    }

    @Test
    void testGetMonthlyReportWithSpecificDate() {
        given()
                .queryParam("date", "2025-01-15")
                .when().get("/sales/report/monthly")
                .then().statusCode(200);
    }

    @Test
    void testGetTopSellingProductsReport() {
        given()
                .when().get("/sales/report/top-selling")
                .then().statusCode(200);
    }

    @Test
    void testListAllSales() {
        given()
                .when().get("/sales")
                .then().statusCode(200);
    }
}