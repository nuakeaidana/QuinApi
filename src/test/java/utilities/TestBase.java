package utilities;
import static io.restassured.RestAssured.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

//abstraction
public abstract class TestBase {
    @BeforeClass
    public static void setup () {
        baseURI = ConfigurationReader.get("baseURI");
        basePath = ConfigurationReader.get("basePath");
    }
    @AfterClass
    public static void tearDown () {
        reset();
    }
}
