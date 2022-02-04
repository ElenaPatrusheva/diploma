package ru.netology.diploma;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.diploma.model.ApiRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class CreditApiTest {
    private ApiRequest successRequestBody;

    @BeforeEach
    void init() {
        successRequestBody = ApiRequest.builder()
                .number("4444 4444 4444 4441")
                .year("23")
                .month("09")
                .cvc("123")
                .holder("IVAN IVANOV")
                .build();
    }
    @Test
    void successfulCreditRequestTest() {
        given()
                .baseUri("http://localhost:8080")
                .when()
                .body(successRequestBody)
                .header("Content-Type", "application/json")
                .header("Accept",  "*/*")
                .post("/api/v1/credit")
                .then()
                .statusCode(200)
                .body("status", equalTo("APPROVED"))
                .contentType(ContentType.JSON);
    }
    @Test
    void unsuccessfulCreditRequestTest() {
        successRequestBody.setNumber("4444 4444 4444 4442");
        given()
                .baseUri("http://localhost:8080")
                .when()
                .body(successRequestBody)
                .header("Content-Type", "application/json")
                .header("Accept",  "*/*")
                .post("/api/v1/credit")
                .then()
                .statusCode(200)
                .body("status", equalTo("DECLINED"))
                .contentType(ContentType.JSON);
    }

    @Test
    void incorrectCardNumberCreditRequestTest() {
        successRequestBody.setNumber("4444 4444 4XX4 4441");
        given()
                .baseUri("http://localhost:8080")
                .when()
                .body(successRequestBody)
                .header("Content-Type", "application/json")
                .header("Accept",  "*/*")
                .post("/api/v1/credit")
                .then()
                .statusCode(500);
    }

    @Test
    void incorrectYearCreditRequestTest() {
        successRequestBody.setYear("19");
        given()
                .baseUri("http://localhost:8080")
                .when()
                .body(successRequestBody)
                .header("Content-Type", "application/json")
                .header("Accept",  "*/*")
                .post("/api/v1/credit")
                .then()
                .statusCode(500);
    }

    @Test
    void incorrectMonthCreditRequestTest() {
        successRequestBody.setMonth("36");
        given()
                .baseUri("http://localhost:8080")
                .when()
                .body(successRequestBody)
                .header("Content-Type", "application/json")
                .header("Accept",  "*/*")
                .post("/api/v1/credit")
                .then()
                .statusCode(500);
    }

    @Test
    void incorrectCvcCreditRequestTest() {
        successRequestBody.setCvc("36");
        given()
                .baseUri("http://localhost:8080")
                .when()
                .body(successRequestBody)
                .header("Content-Type", "application/json")
                .header("Accept",  "*/*")
                .post("/api/v1/credit")
                .then()
                .statusCode(500);
    }
}
