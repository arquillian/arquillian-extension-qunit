/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.qunit.junit.ftest;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.qunit.junit.QUnitRunner;
import org.jboss.arquillian.qunit.junit.annotations.QUnitResources;
import org.jboss.arquillian.qunit.junit.annotations.QUnitTest;
import org.jboss.arquillian.qunit.showcase.data.CarRepository;
import org.jboss.arquillian.qunit.showcase.model.Car;
import org.jboss.arquillian.qunit.showcase.rest.CarService;
import org.jboss.arquillian.qunit.showcase.rest.JaxRsActivator;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

/**
 * Junit uses the QunitRunner controller as a test controller.
 *
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 *
 */
@RunWith(QUnitRunner.class)
@QUnitResources("src/test/resources/assets")
@RunAsClient
public class QUnitRunnerTestCase {

    /**
     * Creates the Archive which will be finally deployed on the AS.
     *
     */
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "rest-service.war")
                .addClasses(Car.class, CarService.class, CarRepository.class, JaxRsActivator.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    /**
     * Execute the qunit-tests-ajax.html QUnit Test Suite.
     */
    @QUnitTest("tests/rest-service/qunit-tests-ajax.html")
    @InSequence(1)
    public void qunitAjaxTest() {
        // empty body - only the annotations are used
    }

    /**
     * Execute the qunit-assertions.html QUnit Test Suite.
     */
    @QUnitTest("tests/generic/qunit-assertions.html")
    @InSequence(2)
    public void qunitAssertionsTest() {
        // empty body - only the annotations are used
    }

    /**
     * Execute the qunit-tests-stuck.html QUnit Test Suite.
     */
    // @QUnitTest("tests/rest-service/qunit-tests-stuck.html")
    // @InSequence(3)
    // public void qunitStuckTest() {
    // empty body - only the annotations are used
    // }

}