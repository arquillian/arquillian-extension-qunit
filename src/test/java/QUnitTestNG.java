import org.jboss.arquillian.qunit.testng.QUnitMethodAddingInterceptor;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;


@Test
@Listeners(QUnitMethodAddingInterceptor.class)
public class QUnitTestNG {
}
