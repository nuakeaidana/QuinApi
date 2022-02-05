package APITests;

import static io.restassured.RestAssured.*;

import POJO.Pojo;
import io.restassured.http.ContentType;

import io.restassured.response.Response;
import static org.testng.Assert.*;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;
import utilities.TestBase;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import java.util.HashMap;
import java.util.Map;

public class BinTests extends TestBase {
    static String binID;
    static Response response;

    @Test(priority = 1)
    public void createBin () {
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        header.put("Content-Type", "application/json");
        Map<String, Object> body = new HashMap<>();
        body.put("sample", "Good Bye");

        response = given().headers(header)
                .body(body).and().post().prettyPeek();
        binID= response.jsonPath().getString("metadata.id");
        String message = response.body().path("record.sample").toString();
        assertEquals(response.statusCode(), 200);
        assertNotEquals(response.statusCode(),400);
        assertNotEquals(response.statusCode(),401);
        assertNotEquals(response.statusCode(),403);
        assertNotEquals(response.statusCode(),404);
        assertEquals(response.contentType(), "application/json; charset=utf-8");
        assertNotEquals(response.contentType(), "application/json");
        assertEquals(message, "Good Bye");

    }
    @Test(priority = 2)
    public void readBin() {
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));

      response= given().accept(ContentType.JSON).and().headers(header).when()
                            .get("/"+binID).prettyPeek();
    //assertion using path method
      String message = response.body().path("record.sample").toString();
     assertEquals(response.statusCode(), 200);
     assertNotEquals(response.statusCode(),400);
     assertNotEquals(response.statusCode(),401);
     assertNotEquals(response.statusCode(),403);
     assertNotEquals(response.statusCode(),404);
     assertEquals(response.contentType(), "application/json; charset=utf-8");
     assertNotEquals(response.contentType(), "application/json");
     assertEquals(message, "Good Bye");

    }

    @Test(priority = 3)
    public void updateBin () {
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        header.put("Content-Type", "application/json");

        Map<String, Object> body = new HashMap<>();
        body.put("record.sample", "Hello");
         given().when().headers(header).and().body(body).put("/"+binID).prettyPeek()
                .then().assertThat().statusCode(200);
//                .and().assertThat().body(matchesJsonSchemaInClasspath("SingleProjectSchema.json"));
    }
    @Test (priority = 4)
    public void ReadBinAfterUpdate() {
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));

        response= given().accept(ContentType.JSON).and().headers(header).when()
                .get("/"+binID).prettyPeek();
        Pojo pojo = response.body().as(Pojo.class);
        String actualResult = pojo.getRecord().getRecordSample();
        System.out.println(actualResult);
        String expectedResult = "Hello";
        assertEquals(actualResult, expectedResult);
    }
//    @Test (priority = 5)
//    public void pojoAssertion () {
//        Map<String, Object> header = new HashMap<>();
//        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
//
//        Response response = given().accept(ContentType.JSON).and().headers(header).when()
//                .get("/" + binID);
//        Pojo pojo = response.body().as(Pojo.class);
//        String actualResult = pojo.getRecord().getSample();
//        System.out.println(actualResult);
//        String expectedResult = "Hello";
//        assertEquals(actualResult, expectedResult);
//    }


    @Test(priority = 6)
    public void deleteBin () {
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        header.put("Content-Type", "application/json");

                given().and().headers(header).when().delete("/"+binID).prettyPrint();
                //Hamcrest
                given().and().headers(header).when().delete("/"+binID).then()
                        .assertThat().statusCode(404);
//                        .and().assertThat().body(matchesJsonSchemaInClasspath("SingleProjectSchema.json"));
    }

}
