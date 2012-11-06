package org.jboss.arquillian.qunit.testng;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class SuiteReader {
    
    public static Collection<TestModule> read() throws MalformedURLException {
        
        Map<String, TestModule> modules = new LinkedHashMap<String, TestModule>();
        Set<String> pairsForUniqueness = new HashSet<String>();
        
        HtmlUnitDriver driver = new HtmlUnitDriver(true);

        URL url = new File("target/qunit-temp/test.war/test/index.html").toURL();
        
        int testNumber = 0;

        driver.get(url.toExternalForm());
        
        List<String> texts = (List<String>) driver.executeScript("return window.tests");
        
        for (String text : texts) {
            
            int colon = text.indexOf(':');
            
            String moduleName = NameTransformer.transform(text.substring(0, colon));
            String testName = NameTransformer.transform(text.substring(colon + 1));
            
            if (pairsForUniqueness.contains(moduleName + testName)) {
                int i = 1;
                while (pairsForUniqueness.contains(moduleName + testName + i)) {
                    i++;
                }
                testName = testName + i;
            }
            pairsForUniqueness.add(moduleName + testName);
            
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
