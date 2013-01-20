package org.jboss.arquillian.qunit.drone;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.qunit.junit.UniqueName;
import org.junit.Test;

public class TestUniqueName {

    @Test
    public void test() {
        String module = "module";
        String test = "test";

        UniqueName unique = new UniqueName();
        assertEquals("test", unique.getName(module, test));
        assertEquals("test(1)", unique.getName(module, test));
        assertEquals("test(2)", unique.getName(module, test));
    }
}
