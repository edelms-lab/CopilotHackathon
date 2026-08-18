package com.microsoft.hackathon.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class DemoResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/hello?key=world")
                .then()
                .statusCode(200)
                .body(is("hello world"));
    }

    // Create a unit test to validate /diffdates that calculates the difference
    // between two dates
    @Test
    public void testDiffDatesEndpoint() {
        given()
                .when().get("/diffdates?date1=01-01-2020&date2=05-01-2020")
                .then()
                .statusCode(200)
                .body(is("Difference between 01-01-2020 and 05-01-2020 is 4 days"));
    }

}