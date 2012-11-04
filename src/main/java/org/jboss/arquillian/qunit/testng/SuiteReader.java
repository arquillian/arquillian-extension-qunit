package org.jboss.arquillian.qunit.testng;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class SuiteReader {
    
    public static Collection<TestModule> read() {
        
        Map<String, TestModule> modules = new LinkedHashMap<String, TestModule>();
        
        HtmlUnitDriver driver = new HtmlUnitDriver(true);

        URL url = Thread.currentThread().getContextClassLoader().getResource("qunit-reader/suite-reader.html");

        String urlAsString = url.toExternalForm();
        String urlWithParams = urlAsString + "?suite=test-suite.js";

        driver.get(urlWithParams);

        List<WebElement> elements = driver.findElements(By.cssSelector("ul li"));
        
        for (WebElement element : elements) {
            
            String text = element.getText();
            
            int colon = text.indexOf(':');
            
            String moduleName = text.substring(0, colon);
            String testName = text.substring(colon + 1);
            
            TestModule module = modules.get(moduleName);
            if (module == null) {
                module = new TestModule(moduleName);
                modules.put(moduleName, module);
            }
            
            module.addFunction(new TestFunction(testName));
        }
        
        return modules.values();
    }
    
}
