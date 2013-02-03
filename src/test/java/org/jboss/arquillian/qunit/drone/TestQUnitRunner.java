package org.jboss.arquillian.qunit.drone;

import org.jboss.arquillian.qunit.junit.QUnitRunner;
import org.jboss.arquillian.qunit.junit.QUnitTest;
import org.jboss.arquillian.qunit.junit.WebRoot;
import org.junit.runner.RunWith;

@RunWith(QUnitRunner.class)
@WebRoot("/home/lfryc/workspaces/arquillian/qunit")
@QUnitTest("test/index.html")
public class TestQUnitRunner {
}
