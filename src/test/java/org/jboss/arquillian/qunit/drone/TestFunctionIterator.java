package org.jboss.arquillian.qunit.drone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;

import org.jboss.arquillian.qunit.junit.FunctionIterator;
import org.jboss.arquillian.qunit.junit.TestFile;
import org.jboss.arquillian.qunit.junit.TestFunction;
import org.jboss.arquillian.qunit.junit.TestModule;
import org.junit.Test;

public class TestFunctionIterator {

    @Test
    public void testEmptyFiles() {
        TestFile file = new TestFile(this.getClass(), this.getClass().getSimpleName());

        FunctionIterator iterator = new FunctionIterator(file);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testEmptyModules() {
        TestFile file = new TestFile(this.getClass(), this.getClass().getSimpleName());
        file.getOrAddModule("module1");
        file.getOrAddModule("module2");

        FunctionIterator iterator = new FunctionIterator(file);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteration() {
        TestFile file = new TestFile(this.getClass(), this.getClass().getSimpleName());
        TestModule module1 = file.getOrAddModule("module1");
        TestFunction function1 = module1.addFunction("function1", 0);
        TestFunction function2 = module1.addFunction("function2", 1);
        TestModule module2 = file.getOrAddModule("module2");
        TestFunction function3 = module2.addFunction("function1", 3);
        TestFunction function4 = module2.addFunction("function2", 4);

        FunctionIterator iterator = new FunctionIterator(file);

        assertTrue(iterator.hasNext());
        assertEquals(function1, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(function2, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(function3, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(function4, iterator.next());

        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            fail();
        } catch (NoSuchElementException e) {
            // expected
            // TODO: handle exception
        }
    }
}
