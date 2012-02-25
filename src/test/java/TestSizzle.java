import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;
import com.opera.core.systems.OperaDriver;

@RunWith(Arquillian.class)
public class TestSizzle {

	@Drone
	OperaDriver driver;

	@ArquillianResource
	URL contextPath;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		WebArchive war = ShrinkWrap
				.create(WebArchive.class, "sizzle.war")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "WEB-INF/web.xml")
				.addAsWebResource(new File("src/test/sizzle/test/index.html"),
						"test/index.html")
				.addAsWebResource(EmptyAsset.INSTANCE, "test/data/iframe.html")
				.addAsWebResource(
						new File("src/test/sizzle/test/data/with_fries.xml"),
						"test/data/with_fries.xml")
				.addAsWebResource(
						new File("src/test/sizzle/test/qunit/qunit/qunit.css"),
						"test/qunit/qunit/qunit.css")
				.addAsWebResource(
						new File("src/test/sizzle/test/data/testinit.js"),
						"test/data/testinit.js")
				.addAsWebResource(new File("src/test/sizzle/test/jquery.js"),
						"test/jquery.js")
				.addAsWebResource(new File("src/test/sizzle/sizzle.js"),
						"sizzle.js")
				.addAsWebResource(
						new File("src/test/sizzle/test/data/sizzle-jquery.js"),
						"test/data/sizzle-jquery.js")
				.addAsWebResource(
						new File("src/test/sizzle/test/qunit/qunit/qunit.js"),
						"test/qunit/qunit/qunit.js")
				.addAsWebResource(
						new File("src/test/sizzle/test/unit/selector.js"),
						"test/unit/selector.js");

		return war;
	}

	@Test
	public void test() throws MalformedURLException {
		if (driver.getClass().equals(HtmlUnitDriver.class)) {
			HtmlUnitDriver.class.cast(driver).setJavascriptEnabled(true);
		}

		driver.navigate().to(new URL(contextPath, "./test/index.html"));

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);

		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				WebElement testresult = driver.findElement(By
						.id("qunit-testresult"));
				if (testresult == null) {
					return false;
				}
				return !testresult.getText().contains("Running...");
			}
		});

		List<WebElement> passes = driver.findElements(By
				.cssSelector("#qunit-tests > li"));

		for (WebElement pass : passes) {
			WebElement module = pass.findElement(By.className("module-name"));
			WebElement test = pass.findElement(By.className("test-name"));
			WebElement failed = pass.findElement(By
					.cssSelector(".counts > .failed"));
			WebElement passed = pass.findElement(By
					.cssSelector(".counts > .passed"));

			System.out.println(module.getText() + ": " + test.getText() + " ("
					+ failed.getText() + ", " + passed.getText() + ")");
		}
	}
}
