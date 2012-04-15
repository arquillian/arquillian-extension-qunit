
import org.jboss.arquillian.testng.Arquillian;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(QUnitMethodAddingInterceptor.class)
public class ArquillianQUnit extends Arquillian {
}
