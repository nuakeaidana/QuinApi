package APITests;

import static io.restassured.RestAssured.*;
import POJOClasses.Pojo;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import static org.testng.Assert.*;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;
import utilities.TestBase;
import static org.hamcrest.Matchers.*;
import java.util.HashMap;
import java.util.Map;

//CRUD operations
//positive scenarios
public class PositiveScenarios extends TestBase {
    static String binID;
    //valid post request
    @Test(priority = 5)
    public void createBin () {
        //valid authorization
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        header.put("Content-Type", "application/json");
        //valid body
        Map<String, Object> body = new HashMap<>();
        body.put("sample", "Good Bye");
        //sending post request
        Response response = given().headers(header)
                .body(body).and().post().prettyPeek();
        //getting ID  from response body
        binID= response.jsonPath().getString("metadata.id");
        //actual message
        String message = response.body().path("record.sample").toString();
        //asserting status code
        assertEquals(response.statusCode(), 200);
        //asserting content Type
        assertEquals(response.contentType(), "application/json; charset=utf-8");
        //asserting message inside body is posted
        assertEquals(message, "Good Bye");
    }
    //get request
    @Test(priority = 6)
    public void readBin() {
        //valid authorization
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        //sending get request
        Response response= given().accept(ContentType.JSON).and().headers(header).when()
                .get("/"+binID).prettyPeek();
        //assertion using path method
        String message = response.body().path("record.sample");
        //asserting status code
        assertEquals(response.statusCode(), 200);
        //asserting content Type
        assertEquals(response.contentType(), "application/json; charset=utf-8");
        //asserting message inside body is posted
        assertEquals(message, "Good Bye");
    }
    //put request
    @Test(priority = 7)
    public void updateBin () {
        //valid authorization
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        header.put("Content-Type", "application/json");
        //edit body
        Map<String, Object> body = new HashMap<>();
        body.put("sample", "Hello");
        //hamcrest continuous assertion, asserting status code, and edited body
                given().when().headers(header).and().body(body).put("/"+binID).prettyPeek()
                        .then().assertThat().statusCode(200)
                        .and().body("record.sample",equalTo("Hello"));
    }
    //sending get request with JsonSchema
    @Test(priority = 8)
    public void JsonSchemaAssertion(){
        //valid authorization
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
               //JsonSchema assertion
               given().accept(ContentType.JSON).and().headers(header).when()
                       .get("/"+binID).then().statusCode(200).and()
                       .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("JsonSchema.json"));
    }
    //sending get request as Pojo - de-serialization
    @Test (priority = 9)
   public void pojoAssertion () {
        //valid authorization
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        //sending get request
        Response response = given().accept(ContentType.JSON).and().headers(header).when()
                .get("/" + binID);
        //getting body as pojo class
        Pojo pojo = response.body().as(Pojo.class);
        //asserting body message with pojo
        String actualResult = pojo.getRecord().getSample();
        String expectedResult = "Hello";
        assertEquals(actualResult, expectedResult);
    }

    //deleting bin
    @Test(priority = 10)
    public void deleteBin () {
        //valid authorization
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        header.put("Content-Type", "application/json");
                //Hamcrest assertion
                given().and().headers(header).when().delete("/"+binID).then()
                        .assertThat().statusCode(200).and()
                        .body("message", equalTo("Bin deleted successfully"));

    }
    @Test(priority = 11)
    //sending get request after bin has been deleted
    public void BinAfterDeleted() {
        //valid authorization
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        //sending get request
        Response response = given().accept(ContentType.JSON).and().headers(header).when()
                .get("/" + binID).prettyPeek();
        //asserting status code and body message
        String message = response.body().path("message").toString();
        assertEquals(response.statusCode(), 404);
        assertEquals(message, "Bin not found or it doesn't belong to your account");
    }
}
