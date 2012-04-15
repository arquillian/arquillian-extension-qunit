package org.jboss.arquillian.qunit;

import java.util.List;

public class DefaultModule implements Module {

    private String name;
    private List<? extends Test> tests;

    public DefaultModule(String name, List<? extends Test> tests) {
        this.name = name;
        this.tests = tests;
    }

    public String getName() {
        return name;
    }

    public List<? extends Test> getTests() {
        return tests;
    }

}
