package APITests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;
import utilities.TestBase;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

//Negative scenarios
public class NegativeScenarios extends TestBase {
    static String binID;
    //sending valid post body with invalid token to verify getting error message
    @Test(priority = 1)
    public void invalidKey() {
        //sending wrong token as a header
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("wrongToken"));
        header.put("Content-Type", "application/json");
        //creating valid body
        Map<String, Object> body = new HashMap<>();
        body.put("sample", "No access");
        //sending post action
        Response response = given().headers(header)
                .body(body).and().post().prettyPeek();
        //asserting status code
        assertEquals(response.statusCode(), 401);
        //asserting invalid message
        String message = response.body().path("message").toString();
        assertEquals(message, "Invalid X-Master-Key provided");
    }
    @Test(priority = 2)
    //sending valid post body with no token to verify getting error message
    public void noKey() {
        //sending no token as a header
        Map<String, Object> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        //creating valid body
        Map<String, Object> body = new HashMap<>();
        body.put("sample", "No access");
        //sending post action
        Response response = given().headers(header)
                .body(body).and().post().prettyPeek();
        //asserting status code
        assertEquals(response.statusCode(), 401);
        //asserting invalid message
        String message = response.body().path("message").toString();
        assertEquals(message, "You need to pass X-Master-Key in the header");
    }
    //valid body and invalid id as path parameter
    @Test (priority = 3)
    public void InvalidID() {
        //valid authorization
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        header.put("Content-Type", "application/json");
        //valid body
        Map<String, Object> body = new HashMap<>();
        body.put("sample", "No ID");
        //sending get request with invalid ID
        Response response = given().accept(ContentType.JSON).and().headers(header).body(body).when()
                .get("/"+ binID).prettyPeek();
        //assert status code
        assertEquals(response.statusCode(), 422);
        //assert invalid message
        String message = response.body().path("message").toString();
        assertEquals(message, "Invalid Record ID");
    }
    //valid body and valid authorization with no ID path parameter
    @Test(priority = 4)
    public void noID() {
        //valid authorization
        Map<String, Object> header = new HashMap<>();
        header.put(ConfigurationReader.get("key"), ConfigurationReader.get("token"));
        header.put("Content-Type", "application/json");
        //valid body
        Map<String, Object> body = new HashMap<>();
        body.put("sample", "No ID");
        //sending get request with no ID as path parameter
        Response response = given().accept(ContentType.JSON).and().headers(header).when().body(body)
                .get("/").prettyPeek();
        //asserting status code
        assertEquals(response.statusCode(), 404);
        //asserting invalid message
        String message = response.body().path("message").toString();
        assertEquals(message, "Route not found!");
    }
}
