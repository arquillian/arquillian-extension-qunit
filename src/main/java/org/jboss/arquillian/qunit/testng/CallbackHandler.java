package org.jboss.arquillian.qunit.testng;

import java.util.HashMap;
import java.util.Map;

public class CallbackHandler {
    
    private Map<String, Callback> callbacks = new HashMap<String, Callback>();
    
    public void add(String name, Callback callback) {
        callbacks.put(name, callback);
    }
    
    public void call(String name) {
        callbacks.get(name).call();
    }
}
