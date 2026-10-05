package restAssured;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Authentication {

    // 1. Basic vs Digest
    // Basic - not completely encrypted(base64 encoded)
    // Digest- fully encrypted

    //2. Preemptive vs Challenged
    // Preemptive - Rest provides user credentials to a server before it requests
    // Challenged - Rest provides user credentials to a server upon it requests

    @Test
    public void authBasicAndDigest() {
        given()
                .baseUri("https://postman-echo.com")
                .auth().basic("postman", "password")
                //.auth().preemptive().basic("postman", "password")
                //.auth().digest("postman", "password")
                .when()
                .get("digest-auth")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void authOAuth1() {
        //For OAuth 1.0 - 4 important parts
        // 1. API Key - consumer key
        // 2. API Secret Key consumer secret
        // 3. Access Token,
        // 4. Access Token Secret

        given()
                .baseUri("https://postman-echo.com")
                .auth().oauth(
                             "API key",
                            "API Secret Key",
                            "Access Token",
                            "Access Token Secret"
                )
                .when()
                .get("oauth-auth")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void authOAuth2() {
        // For OAuth 2.0 -
        // 1. API Key - consumer key
        // 2. API Secret Key consumer secret
        // 3. Access Token
        // For each Consumer/API key request a new Access Token is generated on the fly.

    String accessToken= given()
                .baseUri("https://postman-echo.com/v1")
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .header("Accept-Language", "en-US")
                .param("grant_type", "client_credentials")
                .auth().preemptive().basic("postman", "password")
                .when()
                .post("oauth-auth/token")
                .then()
                .log().all()
                .statusCode(200)
                .extract().path("access_token");
    System.out.println(accessToken);

                given()
                .baseUri("https://postman-echo.com/v2")
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken)
                .when()
                .post("oauth-auth/token/v2")
                        .then()
                        .log().all()
                        .statusCode(200);
    }
}
