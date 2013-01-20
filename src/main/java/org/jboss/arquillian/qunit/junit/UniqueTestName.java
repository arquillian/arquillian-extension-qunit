package org.jboss.arquillian.qunit.junit;

import java.util.HashSet;
import java.util.Set;

public class UniqueTestName {

    private Set<String> pairs = new HashSet<String>();

    public String getName(String module, String test) {
        String base = module + ":";

        if (pairs.contains(base + test)) {
            int i = 0;
            while (pairs.contains(base + test + i)) {
            }
            pairs.add(base + test + i);
            return test + i;
        }

        pairs.add(base + test);
        return test;
    }
}
