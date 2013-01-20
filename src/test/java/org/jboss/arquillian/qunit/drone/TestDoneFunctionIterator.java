package org.jboss.arquillian.qunit.drone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.qunit.junit.DoneFunctionIterator;
import org.jboss.arquillian.qunit.junit.TestFile;
import org.jboss.arquillian.qunit.junit.TestFunction;
import org.jboss.arquillian.qunit.junit.TestModule;
import org.junit.Test;

public class TestDoneFunctionIterator {

    @Test
    public void testSequantial() {
        TestFile file = new TestFile(this.getClass(), this.getClass().getSimpleName());
        TestModule module1 = file.getOrAddModule("module1");
        TestFunction function1 = module1.addFunction("function1", 0);
        TestFunction function2 = module1.addFunction("function2", 1);
        TestModule module2 = file.getOrAddModule("module2");
        TestFunction function3 = module2.addFunction("function1", 3);
        TestFunction function4 = module2.addFunction("function2", 4);

        DoneFunctionIterator iterator = new DoneFunctionIterator(file);

        assertFalse(iterator.hasNext());

        function1.markDone();
        assertTrue(iterator.hasNext());
        assertEquals(function1, iterator.next());
        assertFalse(iterator.hasNext());

        function2.markDone();
        function3.markDone();
        assertTrue(iterator.hasNext());
        assertEquals(function2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(function3, iterator.next());
        assertFalse(iterator.hasNext());

        function4.markDone();
        assertTrue(iterator.hasNext());
        assertEquals(function4, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNotInOrder() {
        TestFile file = new TestFile(this.getClass(), this.getClass().getSimpleName());
        TestModule module1 = file.getOrAddModule("module1");
        TestFunction function1 = module1.addFunction("function1", 0);
        TestFunction function2 = module1.addFunction("function2", 1);
        TestModule module2 = file.getOrAddModule("module2");
        TestFunction function3 = module2.addFunction("function1", 3);
        TestFunction function4 = module2.addFunction("function2", 4);

        DoneFunctionIterator iterator = new DoneFunctionIterator(file);

        assertFalse(iterator.hasNext());

        function1.markDone();
        assertTrue(iterator.hasNext());
        assertEquals(function1, iterator.next());
        assertFalse(iterator.hasNext());

        function4.markDone();
        assertFalse(iterator.hasNext());

        function2.markDone();
        assertTrue(iterator.hasNext());
        assertEquals(function2, iterator.next());
        assertFalse(iterator.hasNext());

        function3.markDone();
        assertTrue(iterator.hasNext());
        assertEquals(function3, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(function4, iterator.next());
        assertFalse(iterator.hasNext());
    }
}
