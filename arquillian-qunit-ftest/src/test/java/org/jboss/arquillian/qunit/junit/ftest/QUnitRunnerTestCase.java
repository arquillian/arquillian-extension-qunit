/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.arquillian.qunit.junit.ftest;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.qunit.junit.QUnitRunner;
import org.jboss.arquillian.qunit.junit.annotations.QUnitResources;
import org.jboss.arquillian.qunit.junit.annotations.QUnitTest;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

/**
 * Junit uses the QunitRunner controller as a test controller.
 *
 * @author Tolis Emmanouilidis
 *
 */
@RunWith(QUnitRunner.class)
@QUnitResources("src/test/resources")
@RunAsClient
public class QUnitRunnerTestCase {

    private static final String DEPLOYMENT = "src/test/resources/archives/ticket-monster.war";

    /**
     * Creates the Archive which will be finally deployed on the AS.
     *
     * @return
     */
    @Deployment()
    public static Archive<?> createDeployment() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File(DEPLOYMENT));
    }

    /**
     * Test the qunit-tests-dom.html file.
     */
    @QUnitTest("assets/tests/ticketmonster/qunit-tests-dom.html")
    @InSequence(2)
    public void qunitDomTest() {
        // empty body - only the annotations are used
    }

    /**
     * Test the qunit-tests-ajax.html file.
     */
    @QUnitTest("assets/tests/ticketmonster/qunit-tests-ajax.html")
    @InSequence(1)
    public void qunitAjaxTest() {
        // empty body - only the annotations are used
    }

    /**
     * Test the qunit-tests.html file.
     */
    @QUnitTest("assets/tests/generic/qunitTest.html")
    @InSequence(3)
    public void qunitAssertionsTest() {
        // empty body - only the annotations are used
    }

}