package restAssured;

import io.restassured.http.*;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ReqResAPI {
    private static final Logger log = LoggerFactory.getLogger(ReqResAPI.class);
    //free_user_3KAuk4DhPts72B1mX9v6ug2WLFh
    //
    String token= "rc_live_6b30a6bd509946809e22eb2e2e291893";

    String baseUrl = "https://api.restcountries.com";

    @Test
    public void testGET(){
        String endpoint = baseUrl + "/countries";
        Response res = given()
                        .header("Authorization", "Bearer " + token)
                        //.auth().oauth2("Bearer "+token)
                        .pathParam("version", "v5")
                        .when()
                        .get(endpoint+"/{version}")
                        .then()
                        .statusCode(200).extract().response();
        String body = res.asString();
        //response.log().all();

        //Status line
        String statusLine = given().when().get(endpoint+"/{version}")
                .then()
                .statusCode(200)
                .body("version", equalTo("v5"))
                .extract()
                .statusLine();
        System.out.println(statusLine);
    }

    @Test
    public void validateJsonResponse(){
        String endpoint = baseUrl + "/countries";
        given()
                .header("Authorization", "Bearer " + token)
                //.auth().oauth2("Bearer "+token)
                .pathParam("version", "v5")
                .queryParam("q","usa")
                .when()
                .get(endpoint+"/{version}")
                .then()
                .statusCode(200)
                .body(
                        "data.objects[6].currencies[0].code", equalTo("USD")
                );
    }

    @Test
    public void validateXmlResponse(){
        //7e19b1048e0e94233d3f8079b989de09
        String endpoint = "";
        given()
                .header("Authorization", "Bearer " + token)
                //.auth().oauth2("Bearer "+token)
                .pathParam("version", "v5")
                .queryParam("q","usa")
                .queryParam("mode", "xml")
                .when()
                .get(endpoint+"/{version}")
                .then()
                .statusCode(200)
                .body(
                        "current.country@currency", equalTo("USD"),
                        "current.country.timezone", equalTo("UST")
                );

        String flag = given().when().get(endpoint+"/{version}")
                .then()
                .statusCode(200)
                .body("version", equalTo("v5"))
                .extract().path("current.country.@flag");
        System.out.println(flag);
    }

    @Test
    public void testPOST(){
        String endpoint = "https://dummy.restapiexample.com/api/v1";
        File payloadFile = new File("src/test/java/payloads/emp.json");

//
//        Integer empId = given()
//                .contentType(ContentType.JSON)
//                .body(payloadFile)
//                .when()
//                .post(endpoint+"/create")
//                .then()
//                .statusCode(200)
//                .extract()
//                        .body().path("data.id");


        //System.out.println(empId);


        given()
                .when()
                .get(endpoint+"/employee/3184")
                .then()
                .log().status();
    }

    @Test
    public void testPUTandDELETE(){
        String endpoint = "https://dummy.restapiexample.com/api/v1";
        String payLoad = """
                {
                  "name":"API10",
                  "salary":"310",
                  "age":"25"
                }
                """;
//        given()
//                .contentType(ContentType.JSON)
//                .body(payLoad)
//                .when()
//                .put(endpoint+"/update/3184")
//                .then()
//                .statusCode(200)
//                .body("data.name", equalTo("API10"));


        given()
                .when()
                .delete(endpoint+"/delete/2")
                .then()
                .statusCode(200);

    }

    @Test
    public void testLOGS(){
        String endpoint = "https://dummy.restapiexample.com/api/v1";
        //Log all
//        given()
//                .when()
//                .get(endpoint+"/employees")
//                .then()
//                .statusCode(200)
                //.log().all();
                // .log().everything()
//        //Log body
//        given()
//                .when()
//                .get(endpoint+"/employees")
//                .then()
//                .log()
//                .body();
        //Headers, cookies, status, ifError, ifStatusCodeIsEqualTo,
        given()
                .when()
                .get(endpoint+"/employeess")
                .then()
                .log()
                //.headers();
                //.status();
                //.cookies();;
                //.ifError();
                //.ifStatusCodeIsEqualTo(404);
                .ifValidationFails()
                .statusCode(200);
    }

    @Test
    public void testParameterTypes(){
        String endpoint = "https://dummy.restapiexample.com/api/v1";
        //Path params
        given()
                .pathParam("id","1")
        .when()
                .get(endpoint+"/employees/{id}")
                .then()
                .log().headers();
                //.statusCode(200);

        //Multi query-params - queryParams
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id","1");
        paramsMap.put("employee_age","61");

        given()
                .queryParams(paramsMap)
                .when()
                .get(endpoint+"/employees")
                .then()
                .log().all()
                .statusCode(200);

        //Multi-value params
        given()
                .queryParam("id","1;2;4;5")
                .when()
                .get(endpoint+"/employees")
                .then()
                .log().all()
                .statusCode(200);

        //Form params
            given()
                    .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                    .formParam("id","1")
                    .formParam("employee_age","61")
                    .formParams(paramsMap)
                    .when()
                    .post(endpoint+"/employees")
                    .then()
                    .log().all();

    }

    @Test
    public void testHeadersCookies(){
        Cookie cookies = new Cookie.Builder("usertype","int").setSecured(true).setComment("restapi")
                .setHttpOnly(true).build();
        String endpoint = "https://dummy.restapiexample.com/api/v1";

    //Headers headers =  given()
       Map<String,String> cookiesMap = new HashMap<>();
       cookiesMap = given()
                .header("Accept","application/json")
                .header("Content-Type","application/json")
//                .header("Authorization","Bearer "+token)
//                .header("Accept-Language","en-US")
//                .header("x-api-key",token)
//                .headers("ABC","","","")
//                .cookie("hgg","hgf")
//                .cookie(cookies)
        .when()
                .get(endpoint+"/employees")
                .then()
                .log().all()
                .statusCode(200)
               .extract().cookies();
                //.extract().headers();
                //.header("Server","nginx/1.29.8");

        //System.out.println(headers.getValue("Content-Encoding"));
        System.out.println(cookiesMap.get("usertype"));
    }


}
