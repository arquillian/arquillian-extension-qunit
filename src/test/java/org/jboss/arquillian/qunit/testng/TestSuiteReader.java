package org.jboss.arquillian.qunit.testng;

import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class TestSuiteReader {

    @Test
    public void test() {
        Collection<TestModule> modules = SuiteReader.read();
        
        System.out.println(modules);
        
        
    }
}
