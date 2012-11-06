package org.jboss.arquillian.qunit.testng;

public class TestFunction {

    private String name;
    private int testNumber;

    public TestFunction(String name, int testNumber) {
        this.name = name;
        this.testNumber = testNumber;
    }

    public String getName() {
        return name;
    }

    public int getTestNumber() {
        return testNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + testNumber;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TestFunction other = (TestFunction) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (testNumber != other.testNumber)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TestFunction [name=" + name + ", testNumber=" + testNumber + "]";
    }
}
