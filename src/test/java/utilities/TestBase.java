package utilities;
import static io.restassured.RestAssured.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
// I put a logic that should apply to every scenario
public abstract class TestBase {

    @BeforeClass
    //baseURI and basePath to set up scenarios
    public static void setUp () {
        baseURI = ConfigurationReader.get("baseURI");
        basePath = ConfigurationReader.get("basePath");
    }
    @AfterClass
    //reset method to reset scenarios after the execution
    public static void tearDown () {
        reset();
    }

}

