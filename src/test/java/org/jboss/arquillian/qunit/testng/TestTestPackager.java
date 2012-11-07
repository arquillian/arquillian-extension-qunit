package org.jboss.arquillian.qunit.testng;

import java.io.IOException;

import org.junit.Test;

public class TestTestPackager {
    
    @Test
    public void test() throws IOException {
        TestPackager.scan2(true);
    }
}
