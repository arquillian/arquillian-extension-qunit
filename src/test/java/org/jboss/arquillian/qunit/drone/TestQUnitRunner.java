package org.jboss.arquillian.qunit.drone;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.qunit.junit.QUnitRunner;
import org.jboss.arquillian.qunit.junit.QUnitTest;
import org.jboss.arquillian.qunit.junit.WebRoot;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

/**
 * Instuct Junit to use the QunitRunner controller.
 *
 * @author aemmanou
 *
 */
//@RunWith(QUnitRunner.class)
//@WebRoot("src/main/webapp")
//@RunAsClient
public class TestQUnitRunner {

    //private static final String DEPLOYMENT = "../../../mypath/target/my-app.war";

    /**
     * Creates the Archive which will be finally deployed on the AS.
     *
     * @return
     */
    //@Deployment()
    //public static WebArchive createDeployment() {
        //return ShrinkWrap.createFromZipFile(WebArchive.class, new File(DEPLOYMENT));
    //}

    /**
     * Test the qunit-tests-dom.html file.
     */
    //@QUnitTest("qunit-tests-dom.html")
    //@InSequence(1)
    //public void qunitDomTest() {
        // empty body - only the annotations are used
    //}

    /**
     * Test the qunit-tests-ajax.html file.
     */
    //@QUnitTest("qunit-tests-ajax.html")
    //@InSequence(2)
    //public void qunitAjaxTest() {
        // empty body - only the annotations are used
    //}

}
