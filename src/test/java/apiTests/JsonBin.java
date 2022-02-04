package apiTests;

import static io.restassured.RestAssured.*;

import POJO.JsonIn;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;
import utilities.TestBase;

import java.util.HashMap;
import java.util.Map;

public class JsonBin extends TestBase {
    static String binID;

    @Test(priority = 1)
    public void create () {
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        header.put("X-Bin-Name", "Aidana");
        header.put("Content-Type", "application/json");
        //polymorphism
        Map<String, Object> body = new HashMap<>();
        body.put("example", "Good Bye");

        binID = given().headers(header)
                .body(body).and().post().jsonPath().getString("metadata.id");
    }
    @Test(priority = 2)
    public void get() {
        Map<String, Object> header = new HashMap<>();
        //encapsulation
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));

                        given().accept(ContentType.JSON).and().headers(header).when()
                            .get("/"+binID).prettyPeek();
    }

    @Test(priority = 3)
    public void update () {
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        header.put("Content-Type", "application/json");

        Map<String, Object> body = new HashMap<>();
        body.put("example", "Hello");

                given().when().headers(header).and().body(body).put("/"+binID).prettyPrint();
    }
    @Test(priority = 4)
    public void assertion () {
        Map<String, Object> header = new HashMap<>();
        //encapsulation
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        String actualResult = given().accept(ContentType.JSON).and().headers(header).when()
                .get("/"+binID).jsonPath().getString("record.example");
        //System.out.println(actualResult);
        String expectedResult = "Hello";
        Assert.assertEquals(actualResult,expectedResult);
    }
    @Test (priority = 5)
    public void pojoAssertion () {
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));

        Response response = given().accept(ContentType.JSON).and().headers(header).when()
                .get("/"+binID);
        JsonIn jsonIn = response.body().as(JsonIn.class);
        String actualResult = jsonIn.getRecord().getExample();
        System.out.println(actualResult);
        String expectedResult = "Hello";
        Assert.assertEquals(actualResult,expectedResult);
    }
    //incorrect
    @Test (priority = 6)
    public void NegativeTest () {
        Map<String, Object> header = new HashMap<>();

        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        String actualResult = given().accept(ContentType.JSON).and().headers(header).when()
                .get("/"+binID).jsonPath().getString("record.example");
        String expectedResult = "Hello";
        Assert.assertEquals(actualResult,expectedResult);
    }

    @Test(priority = 7)
    public void delete () {
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        header.put("Content-Type", "application/json");

                given().and().headers(header).when().delete("/"+binID).prettyPrint();
    }

}
