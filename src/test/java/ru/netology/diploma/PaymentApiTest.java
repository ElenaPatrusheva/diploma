package ru.netology.diploma;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.diploma.model.ApiRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class PaymentApiTest {
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
    void successfulRequestTest() {
        given()
                .baseUri("http://localhost:8080")
                .when()
                .body(successRequestBody)
                .header("Content-Type", "application/json")
                .header("Accept",  "*/*")
                .post("/api/v1/pay")
                .then()
                .statusCode(200)
                .body("status", equalTo("APPROVED"))
                .contentType(ContentType.JSON);
    }
    @Test
    void unsuccessfulRequestTest() {
        successRequestBody.setNumber("4444 4444 4444 4442");
        given()
                .baseUri("http://localhost:8080")
                .when()
                .body(successRequestBody)
                .header("Content-Type", "application/json")
                .header("Accept",  "*/*")
                .post("/api/v1/pay")
                .then()
                .statusCode(200)
                .body("status", equalTo("DECLINED"))
                .contentType(ContentType.JSON);
    }

    @Test
    void incorrectCardNumberRequestTest() {
        successRequestBody.setNumber("4444 4444 4XX4 4441");
        given()
                .baseUri("http://localhost:8080")
                .when()
                .body(successRequestBody)
                .header("Content-Type", "application/json")
                .header("Accept",  "*/*")
                .post("/api/v1/pay")
                .then()
                .statusCode(500);
    }

    @Test
    void incorrectYearRequestTest() {
        successRequestBody.setYear("19");
        given()
                .baseUri("http://localhost:8080")
                .when()
                .body(successRequestBody)
                .header("Content-Type", "application/json")
                .header("Accept",  "*/*")
                .post("/api/v1/pay")
                .then()
                .statusCode(500);
    }

    @Test
    void incorrectMonthRequestTest() {
        successRequestBody.setMonth("36");
        given()
                .baseUri("http://localhost:8080")
                .when()
                .body(successRequestBody)
                .header("Content-Type", "application/json")
                .header("Accept",  "*/*")
                .post("/api/v1/pay")
                .then()
                .statusCode(500);
    }

    @Test
    void incorrectCvcRequestTest() {
        successRequestBody.setCvc(null);
        given()
                .baseUri("http://localhost:8080")
                .when()
                .body(successRequestBody)
                .header("Content-Type", "application/json")
                .header("Accept",  "*/*")
                .post("/api/v1/pay")
                .then()
                .statusCode(500);
    }
}
