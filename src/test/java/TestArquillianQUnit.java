import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.testng.Arquillian;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Test
@Listeners(QUnitMethodAddingInterceptor.class)
public class TestArquillianQUnit extends Arquillian {
    
    @Drone
    private FirefoxDriver browser;
    
    @Test
    public void test() {
        
    }
}
