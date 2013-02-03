package org.jboss.arquillian.qunit.junit;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jboss.arquillian.qunit.testng.TestPackager;
import org.jboss.shrinkwrap.api.exporter.ExplodedExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class SuiteReader {

    public static TestSuite read(Class<?> suiteClass) throws MalformedURLException {

        WebArchive war = TestPackager.scan2(true);
        File destinationDir = new File("target/qunit-temp");
        destinationDir.mkdir();
        war.as(ExplodedExporter.class).exportExploded(destinationDir);

        final TestSuite suite = new TestSuite(suiteClass);

        HtmlUnitDriver driver = new HtmlUnitDriver(true);

        File file = new File("target/qunit-temp/test.war/test/index.html");
        URL url = file.toURL();

        TestFile testFile = suite.getOrAddFile(file.getAbsolutePath());
        UniqueName uniqueTestName = new UniqueName();

        int testNumber = 0;

        driver.get(url.toExternalForm());

        List<String> texts = (List<String>) driver.executeScript("return window.tests");

        for (Object textObj : texts) {

            String text = textObj.toString();

            int colon = text.indexOf(':');

            String moduleName = text.substring(0, colon).trim();
            String testName = text.substring(colon + 1).trim();
            testName = uniqueTestName.getName(moduleName, testName);

            TestModule module = testFile.getOrAddModule(moduleName);

            module.addFunction(testName, testNumber);

            testNumber += 1;
        }

        return suite;
    }

}
