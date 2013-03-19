package org.jboss.arquillian.qunit.junit;

import java.util.Comparator;

public class QUnitTestMethodComparator implements Comparator<QUnitTestMethod> {

    private static final QUnitTestMethodComparator instance = new QUnitTestMethodComparator();

    public int compare(QUnitTestMethod q1, QUnitTestMethod q2) {
        return q1.getSequence() < q2.getSequence() ? -1 : 1;
    }

    public static QUnitTestMethodComparator getInstance() {
        return QUnitTestMethodComparator.instance;
    }
}
