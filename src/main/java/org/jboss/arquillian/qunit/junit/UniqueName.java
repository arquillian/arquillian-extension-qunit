package org.jboss.arquillian.qunit.junit;

import java.util.HashSet;
import java.util.Set;

public class UniqueName {

    private Set<String> tests = new HashSet<String>();

    public String getName(String module, String test) {

        String result = test;

        if (tests.contains(test)) {
            int i = 1;
            while (tests.contains(result = suffixName(test, i++))) {
            }
        }

        tests.add(result);
        return result;
    }

    private String suffixName(String test, int i) {
        StringBuffer b = new StringBuffer(test.length() + 3);
        b.append(test);
        b.append("(");
        b.append(i);
        b.append(')');
        return b.toString();
    }
}
