package org.jboss.arquillian.qunit;

import java.util.List;

public interface Module {

    String getName();
    
    List<? extends Test> getTests();
}
