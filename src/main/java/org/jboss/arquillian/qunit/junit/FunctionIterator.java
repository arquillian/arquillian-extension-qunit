package org.jboss.arquillian.qunit.junit;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FunctionIterator implements Iterator<TestFunction> {

    private Iterator<TestModule> moduleIterator;
    private Iterator<TestFunction> moduleFunctionIterator;

    public FunctionIterator(TestFile testFile) {
        moduleIterator = testFile.getModules().iterator();
    }

    public boolean hasNext() {
        if (moduleFunctionIterator != null && moduleFunctionIterator.hasNext()) {
            return true;
        } else {
            if (seekToNextModule()) {
                return hasNext();
            } else {
                return false;
            }
        }
    }

    public TestFunction next() {
        if (moduleFunctionIterator != null && moduleFunctionIterator.hasNext()) {
            return moduleFunctionIterator.next();
        } else {
            if (seekToNextModule()) {
                return next();
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    private boolean seekToNextModule() {
        if (moduleIterator.hasNext()) {
            moduleFunctionIterator = moduleIterator.next().getFunctions().iterator();
            return true;
        } else {
            return false;
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
