package com.perinity.grc.infrastructure.inbound.rest;

import com.perinity.grc.infrastructure.inbound.rest.dto.CreateProductRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class ProductResourceTest {

  @Test
  void testProductFlow() {
    CreateProductRequest request = new CreateProductRequest(
        "Hammer", "Tool", "Heavy", "10x10", 1.0,
        new BigDecimal("50.00"), new BigDecimal("100.00"));

    String id = given()
        .contentType(ContentType.JSON)
        .body(request)
        .when().post("/products")
        .then().statusCode(201)
        .body("id", notNullValue())
        .extract().path("id");

    given().when().get("/products/" + id).then().statusCode(200);

    given().when().get("/products").then().statusCode(200);

    given().contentType(ContentType.JSON).body(request)
        .when().put("/products/" + id).then().statusCode(200);

    given().when().get("/products/report/oldest").then().statusCode(200);

    given().when().delete("/products/" + id).then().statusCode(204);
  }

  @Test
  void testGetProductNotFound() {
    given()
        .when().get("/products/invalid-product-id")
        .then().statusCode(404);
  }

  @Test
  void testDeleteProductNotFound() {
    given()
        .when().delete("/products/invalid-product-id")
        .then().statusCode(404);
  }

  @Test
  void testUpdateProductNotFound() {
    CreateProductRequest request = new CreateProductRequest(
        "Hammer", "Tool", "Heavy", "10x10", 1.0,
        new BigDecimal("50.00"), new BigDecimal("100.00"));

    given().contentType(ContentType.JSON).body(request)
        .when().put("/products/invalid-product-id")
        .then().statusCode(404);
  }

  @Test
  void testListAllProducts() {
    given().when().get("/products").then().statusCode(200);
  }

  @Test
  void testGetOldestProductsReport() {
    given().when().get("/products/report/oldest").then().statusCode(200);
  }
}