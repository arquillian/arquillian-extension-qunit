import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.qunit.testng.ArquillianQUnit;
import org.jboss.arquillian.testng.Arquillian;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Test
public class TestArquillianQUnit extends ArquillianQUnit {

    @Drone
    private WebDriver browser;

}
