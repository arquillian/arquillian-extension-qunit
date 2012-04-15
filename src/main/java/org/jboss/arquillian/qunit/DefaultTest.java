package org.jboss.arquillian.qunit;

public class DefaultTest implements Test {

    private String name;

    public DefaultTest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
