package org.jboss.arquillian.qunit.junit;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoneFunctionIterator implements Iterator<TestFunction> {

    private FunctionIterator iterator;
    private TestFunction current;

    public DoneFunctionIterator(TestFile testFile) {
        iterator = new FunctionIterator(testFile);
    }

    public boolean hasNext() {
        if (current == null) {
            if (!seekNext()) {
                return false;
            }
        }
        return current.isDone();
    }

    public TestFunction next() {
        if (current == null) {
            if (!seekNext()) {
                throw new NoSuchElementException();
            }
        }
        if (current.isDone()) {
            TestFunction result = current;
            current = null;
            return result;
        } else {
            throw new NoSuchElementException();
        }
    }

    private boolean seekNext() {
        if (iterator.hasNext()) {
            current = iterator.next();
            return true;
        }
        return false;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
