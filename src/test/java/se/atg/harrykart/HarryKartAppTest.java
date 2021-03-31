package se.atg.harrykart;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.net.URI;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("java-test")
public class HarryKartAppTest {

    private final static URI harryKartApp = URI.create("http://localhost:8181/java/api/play");

    @BeforeAll
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("Trying to GET instead of POST should return 405 Method not allowed")
    void useGetOnPostEndpointShouldNotBePossible() {
        when()
                .get(harryKartApp)
        .then()
                .assertThat()
                .statusCode(405);
    }

    @Test
    @DisplayName("Happy Flow")
    void cantPlayYet() {
        File xmlDataInFile = new File("src/test/resources/input_0.xml");
        given()
                .header("Content-Type", ContentType.XML)
                .body(xmlDataInFile)
        .when()
                .post(harryKartApp)
        .then()
                .assertThat()
                .statusCode(200)
                .and()
                .header("Content-Type", ContentType.JSON.toString())
                .and()
                .body("ranking.size()", is(3))
                .body("ranking[0].position", equalTo(1))
                .body("ranking[0].horse", equalTo("TIMETOBELUCKY"));
    }
}
