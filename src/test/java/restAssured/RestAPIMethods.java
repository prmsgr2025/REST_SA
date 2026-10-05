package restAssured;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RestAPIMethods {
    String baseUrl = "https://jsonplaceholder.typicode.com";

   @Test
    public void methodGET1(){
        String endpoint = baseUrl + "/posts";
        var response = given()
                .when()
                .get(endpoint)
                .then();
        response.log().all();
    }

    @Test
    public void methodGET2(){
        String endpoint = baseUrl + "/posts/1/comments";
        var response =
                given()
                        .queryParam("id", 2)
                        .when()
                        .get(endpoint)
                        .then();
        response.log().body();
    }

    @Test
    public void methodPOST(){
       String endpoint = baseUrl + "/posts";
       String inputPayload = """
               {
                   "userId": 11,
                   "id": 1,
                   "title": "POST Method",
                   "body": "Testing post"
                }
               """;

       var response = given()
               .body(inputPayload)
               .when()
               .post(endpoint)
               .then();
       response.log().body();

    }

    @Test
    public void methodPUT(){
        String endpoint = baseUrl + "/posts";
        String inputPayload = """
               {
                   "title": "PUT Method",
                   "body": "Testing post"
                }
               """;

        var response = given()
                .pathParam("id", 101)
                .body(inputPayload)
                .when()
                .put(endpoint)
                .then();
        response.log().all();
    }

    @Test
    public void methodDELETE(){
        String endpoint = baseUrl + "/posts/1";
        String inputPayload = """
               {
                  "id": 100
                }
               """;

        var response = given()
                //.body(inputPayload)
                .when()
                .delete(endpoint)
                .then();
        response.statusCode(204);

    }

    @Test
    public void serializedProduct(){
       String endpoint = baseUrl + "/posts";
       Products product = new Products(
            "Waterbotlle",
               "Stainless Stell",
               12.4,
               1
       );

       var response = given()
               .body(product)
               .when()
               .post(endpoint)
               .then();
       response.log().body();
    }
}
