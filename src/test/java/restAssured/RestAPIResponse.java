package restAssured;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RestAPIResponse {
    String baseUrl = "https://jsonplaceholder.typicode.com";

   @Test
    public void methodGET1(){
        String endpoint = baseUrl + "/posts";
        var response = given()
                .when()
                .get(endpoint)
                .prettyPrint();
    }

    @Test
    public void methodGET2(){
        String endpoint = baseUrl + "/posts/1/comments";
        var response =
                given()
                        .queryParam("id", 2)
                        .when()
                        .get(endpoint)
                        //.prettyPrint();
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .body("email[0]", equalTo("Jayne_Kuhic@sydney.com"))
                        .body("name[0]", containsString("quo"));;
    }

    @Test
    public void assertBody(){
        String endpoint = baseUrl + "/posts/1/comments";
        var response =
                given()
                        .when()
                        .get(endpoint)
                        //.prettyPrint();
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .body("", hasSize(5))
                        .body("[0].id", notNullValue());
    }

    @Test
    public void assertHeaders(){
        String endpoint = baseUrl + "/posts/1/comments";
        var response =
                given()
                        .when()
                        .get(endpoint)
                        //.prettyPrint();
                        .then()
                        .log()
                        .headers()
                       .header("Content-Type", "application/json");
    }


    @Test
    public void deSerialization(){
        String endpoint = baseUrl + "/products";
        Products product = new Products(
                "Waterbotlle",
                "Stainless Stell",
                12.4,
                1
        );

        Products actualProduct = given()
                .param("id",2)
                .when()
                .get(endpoint)
                .as(Products.class);

        assertThat(actualProduct, equalTo(product));
    }

    @Test
    public void paramTypes(){
       String endpoint = baseUrl + "/posts/1/comments";
                //Path params
                 given()
                .pathParam("postId", 3) // 👈 Replaces {postId} dynamical
                .when()
                .get("/api/posts/{postId}/comments") // Endpoint contract: /api/posts/{postId}/comments
                .then()
                .statusCode(200);

               //Query prams
                 given()
                .queryParam("postId", 1) // 👈 Appends filtering query
                .when()
                .get("/api/comments")
                .then()
                .statusCode(200);

               //Authentication
                //1. basic
                given()
                .auth().basic("admin", "admin")
                 .when()
                 .get("/api/posts/{postId}/comments")
                 .then()
                  .statusCode(200);
                //2. oauth2
                given()
               .auth().oauth2("Token")
               .when()
               .get("/api/posts/{postId}/comments")
               .then()
               .statusCode(200);

    }
}
