package org.jboss.arquillian.qunit.junit;

import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class JUnitRunner extends Suite {

    /**
     * Used by JUnit
     */
    public JUnitRunner(Class<?> suiteClass, RunnerBuilder builder) throws InitializationError {
        this(suiteClass, builder, new ClassGenerator());
    }

    /**
     * For testing purposes only
     */
    public JUnitRunner(Class<?> suiteClass, RunnerBuilder builder, ClassGenerator generator) throws InitializationError {
        super(builder, suiteClass, generator.getClasses());
    }

}
