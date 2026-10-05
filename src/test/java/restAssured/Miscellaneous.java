package restAssured;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.XmlConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class Miscellaneous {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "http://localhost:";
        RestAssured.port = 8080;
    }

    @Test
    public void portNumber() {

        given()
                .baseUri(RestAssured.baseURI)
                .port(RestAssured.port)
                .when()
                .get("/test1")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void responseTimeValidation() {

        given()
                .baseUri(RestAssured.baseURI)
                .port(RestAssured.port)
                .when()
                .get("/test1")
                .then()
                .time(lessThan(1500L), TimeUnit.MILLISECONDS);
    }

    @Test
    public void xmlNameSpaceValidation() {

        XmlConfig xmlConfig = new XmlConfig();
        xmlConfig.declareNamespace("perctg", "http://www.w3.org/2001/XMLSchema-instance");

        given()
                .config(RestAssuredConfig.config().xmlConfig(xmlConfig))
                .when()
                .get("/test1")
                .then()
                .log().all()
                .body("student.score[0]", equalTo("23"))
                .body("student.grouping[1]", equalTo("99.36"));
    }

    @Test
    public void responsePartsValidation() {

       Response response= given()
                            .baseUri(RestAssured.baseURI)
                            .port(RestAssured.port)
                            .when()
                            .get("/test1")
                            .then()
                            .extract().response();

       String href = response.path("href");
       String articleId = response.path("articalId");
       String articleUrl = response.path("articalUrl");
       Assert.assertEquals(href,articleId);
    }

    @Test
    public void responseAwareMatcherValidation() {

         given()
                .baseUri(RestAssured.baseURI)
                .port(RestAssured.port)
                .when()
                .get("/test1")
                .then()
                .body("articleUrl", response -> equalTo(response.path("href").toString()
                        + response.path("articleId").toString()));

    }

}
