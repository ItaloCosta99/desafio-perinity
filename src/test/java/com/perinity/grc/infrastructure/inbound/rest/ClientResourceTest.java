package com.perinity.grc.infrastructure.inbound.rest;

import com.perinity.grc.infrastructure.inbound.rest.dto.CreateClientRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ClientResourceTest {

    @Test
    void testClientFlow() {
        CreateClientRequest request = new CreateClientRequest(
                "John Doe", "Jane Doe", "Street 1", "549.959.880-09", "456",
                LocalDate.of(1990, 1, 1), "999", "john@email.com");

        String id = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/clients")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract().path("id");

        given()
                .when().get("/clients")
                .then().statusCode(200);

        given()
                .when().get("/clients/" + id)
                .then()
                .statusCode(200)
                .body("fullName", is("John Doe"));

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/clients/" + id)
                .then().statusCode(200);

        given()
                .when().delete("/clients/" + id)
                .then().statusCode(204);

        given()
                .when().delete("/clients/" + id)
                .then().statusCode(404);

        given()
                .when().get("/clients/report/new-clients/2025")
                .then().statusCode(200);
    }

    @Test
    void testGetClientNotFound() {
        given()
                .when().get("/clients/invalid-id-that-does-not-exist")
                .then().statusCode(404);
    }

    @Test
    void testListAllClients() {
        given()
                .when().get("/clients")
                .then().statusCode(200);
    }

    @Test
    void testNewClientsReportForDifferentYears() {
        given()
                .when().get("/clients/report/new-clients/2024")
                .then().statusCode(200);

        given()
                .when().get("/clients/report/new-clients/2020")
                .then().statusCode(200);
    }
}