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
        assertEquals(response.contentType(), "application/json; charset=utf-8");
        assertNotEquals(response.contentType(), "application/json");
        assertEquals(message, "Good Bye");

    }
    @Test
    public void invalidKey() {
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), "$2b$10$XBmMLw9bqqmwh74dyXnZl.AltiytfMiieorVFuqsdQyTvoWGxoZc");
        header.put("Content-Type", "application/json");
        Map<String, Object> body = new HashMap<>();
        body.put("sample", "No access");
        response = given().headers(header)
                .body(body).and().post().prettyPeek();
        assertEquals(response.statusCode(), 401);
        String message = response.body().path("message").toString();
        assertEquals(message, "Invalid X-Master-Key provided");

    }
    @Test
    public void noKey() {
        Map<String, Object> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        Map<String, Object> body = new HashMap<>();
        body.put("sample", "No access");
        response = given().headers(header)
                .body(body).and().post().prettyPeek();
        assertEquals(response.statusCode(), 401);
        String message = response.body().path("message").toString();
        assertEquals(message, "You need to pass X-Master-Key in the header");

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
     assertEquals(response.contentType(), "application/json; charset=utf-8");
     assertNotEquals(response.contentType(), "application/json");
     assertEquals(message, "Good Bye");

    }
    @Test
    public void InvalidID() {
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        header.put("Content-Type", "application/json");
        Map<String, Object> body = new HashMap<>();
        body.put("sample", "No ID");
        response = given().accept(ContentType.JSON).and().headers(header).when()
                .get("/"+ binID).prettyPeek();
        assertEquals(response.statusCode(), 422);
        String message = response.body().path("message").toString();
        assertEquals(message, "Invalid Record ID");

    }
    @Test
    public void noID() {
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        header.put("Content-Type", "application/json");
        Map<String, Object> body = new HashMap<>();
        body.put("sample", "No ID");
        response = given().accept(ContentType.JSON).and().headers(header).when()
                .get("/").prettyPeek();
        assertEquals(response.statusCode(), 404);
        String message = response.body().path("message").toString();
        assertEquals(message, "Route not found!");

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

        String message= given().accept(ContentType.JSON).and().headers(header).when()
                .get("/"+binID).toString();

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
    @Test(priority = 7)
    public void BinAfterDeleted() {
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));

        response = given().accept(ContentType.JSON).and().headers(header).when()
                .get("/" + binID).prettyPeek();
        String message = response.body().path("message").toString();
        assertEquals(response.statusCode(), 404);
        assertEquals(message, "Bin not found or it doesn't belong to your account");
    }
}
