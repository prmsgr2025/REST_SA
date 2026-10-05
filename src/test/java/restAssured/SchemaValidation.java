package restAssured;

import org.testng.annotations.Test;

import java.io.File;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static io.restassured.RestAssured.given;
import static io.restassured.matcher.RestAssuredMatchers.*;

public class SchemaValidation {

    @Test
    public void jsonSchemaValidation() {
        File jsonSchema1 = new File("src/test/java/payloads/json-schema-emp.json");
        given()
                .baseUri("https://dummy.restapiexample.com/api/v1")
                .when()
                .get("/employee/1")
                .then()
                .log().all()
                //.statusCode(200)
                .body(matchesJsonSchema(jsonSchema1));
    }

    @Test
    public void xmlDTD_schemaValidation() {

        File dtdSchema1 = new File("src/test/java/payloads/bookstoreDTD.xml");
        given()
                .baseUri("https://dummy.restapiexample.com/api/v1")
                .queryParam("api", "sdjihjds89sduisdfids")
                .queryParam("mode", "xml")
                .when()
                .get("/employee/1")
                .then()
                .log().all()
                .body(matchesDtd(dtdSchema1));

    }

    @Test
    public void xmlXSD_schemaValidation() {

        File xsdSchema1 = new File("src/test/java/payloads/bookstoreXSD.xml");
        given()
                .baseUri("https://dummy.restapiexample.com/api/v1")
                .queryParam("api", "sdjihjds89sduisdfids")
                .queryParam("mode", "xml")
                .when()
                .get("/employee/1")
                .then()
                .log().all()
                .body(matchesXsd(xsdSchema1));
    }
}

