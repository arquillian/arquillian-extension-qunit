import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.qunit.testng.ArquillianQUnit;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

@Test
public class TestArquillianQUnit extends ArquillianQUnit {

    @Drone
    private WebDriver browser;

}
