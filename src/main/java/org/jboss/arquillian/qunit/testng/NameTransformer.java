package org.jboss.arquillian.qunit.testng;

public class NameTransformer {
    
    public static String transform(String name) {
        name = name.replaceAll("[^a-zA-Z0-9]", " ");
        name = name.trim();
        name = name.replaceAll(" +", " ");
        name = name.replace(' ', '_');
        return name;
    }
}
