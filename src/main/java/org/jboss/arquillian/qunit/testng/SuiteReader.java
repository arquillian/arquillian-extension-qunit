package org.jboss.arquillian.qunit.testng;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class SuiteReader {

    public static Collection<TestModule> read() throws MalformedURLException {

        Map<String, TestModule> modules = new LinkedHashMap<String, TestModule>();

        HtmlUnitDriver driver = new HtmlUnitDriver(true);

        URL url = new File("target/qunit-temp/test.war/test/index.html").toURL();

        int testNumber = 0;

        driver.get(url.toExternalForm());

        List<String> texts = (List<String>) driver.executeScript("return window.tests");

        for (String text : texts) {

            int colon = text.indexOf(':');

            String moduleName = text.substring(0, colon);
            String testName = text.substring(colon + 1);

            TestModule module = modules.get(moduleName);
            if (module == null) {
                module = new TestModule(moduleName);
                modules.put(moduleName, module);
            }

            module.addFunction(new TestFunction(testName, ++testNumber));
        }

        return modules.values();
    }

}
